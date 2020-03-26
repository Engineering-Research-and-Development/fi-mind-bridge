package it.eng.fimind.service.aas;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.siemens.mindsphere.sdk.assetmanagement.model.AspectType;
import com.siemens.mindsphere.sdk.assetmanagement.model.AssetResource;
import com.siemens.mindsphere.sdk.assetmanagement.model.Variable;
import com.siemens.mindsphere.sdk.assetmanagement.model.VariableDefinition;
import com.siemens.mindsphere.sdk.timeseries.model.Timeseries;

import it.eng.fimind.model.zvei.aas.AssetAdministrationShell;
import it.eng.fimind.model.zvei.aas.SubmodelElements;
import it.eng.fimind.model.zvei.aas.SubmodelsObjects;
import it.eng.fimind.util.MindSphereGateway;
import it.eng.fimind.util.MindSphereMapper;
import it.eng.fimind.util.ServiceResult;


/**
 * Root resource (exposed at "aas" path)
 */
@Path("aas")
public class AssetAdministrationShellServices {
	private static Logger logger = Logger.getLogger(AssetAdministrationShellServices.class);
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getIt() {
		return "AssetAdministrationShell Service: got it!!";
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createDataInJSON(@HeaderParam("debug-mode") String debugMode, @Valid AssetAdministrationShell aas) { 
		ServiceResult serviceResult = new ServiceResult();
		logger.debug("Id ="+aas.getId());
		
		if(debugMode!=null && debugMode.equals("true")){
			System.out.println("DEBUG MODE FOR --- AssetAdministrationShell ---");
			createMindSphereAssetFromAAS(aas, true);
			serviceResult.setResult("Test gone fine");
			return Response.status(200).entity(serviceResult).build();
		}else {
			Boolean result = false;
			if(!aasDoesAlreadyExist(aas)) 
				result = createMindSphereAssetFromAAS(aas, false);
			
			result = createMindSphereTimeSeriesFromAAS(aas);
			
			if(result) {
				serviceResult.setResult("AssetAdministrationShell added succesfully");
				return Response.status(201).entity(serviceResult).build();
			}
			else {
				serviceResult.setResult("Something went wrong, check your FI-MIND logs");
				return Response.status(500).entity(serviceResult).build();
			}
		}
	}
	
	@SuppressWarnings("unused")
	private void getEverythingFromAAS(AssetAdministrationShell aas) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+aas.getId()+"Asset\"}");
		System.out.println(assets.get(0));
		System.out.println(mindSphereGateway.getAspectById("engineer."+aas.getId()+"AspectType"));
		System.out.println(mindSphereGateway.getTimeSeries(assets.get(0).getAssetId(), aas.getId()+"AspectType"));
	}
	
	private Boolean aasDoesAlreadyExist(AssetAdministrationShell aas)
	{
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+aas.getId()+"Asset\"}");
		return assets.size()>0;
	}
	
	public Boolean createMindSphereAssetFromAAS(AssetAdministrationShell aas, Boolean isDebugMode) 
	{	
		Boolean result = false;

		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		MindSphereMapper mindSphereMapper = new MindSphereMapper();

		List<VariableDefinition> assetVariablesDefinitions = mindSphereMapper.fiPropertiesToMiVariablesDefinitions(new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>());
		List<Variable> assetVariables = mindSphereMapper.fiPropertiesToMiVariables(new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>());

		List<String> properties = new ArrayList<String>();
		List<String> uoms = new ArrayList<String>();
		List<String> dataTypes = new ArrayList<String>();

		for (int i=0; i<aas.getSubmodels().size();i++) {
			SubmodelsObjects current_submodel = aas.getSubmodels().get(i);
			String idShort = current_submodel.getIdShort();
			if(!idShort.equalsIgnoreCase("DataSheet"))
				continue;
			for (int j=0; j<current_submodel.getSubmodelElements().size();j++) {
				SubmodelElements current_submodelElement = current_submodel.getSubmodelElements().get(j);	
				properties.add(current_submodelElement.getIdShort());
				uoms.add(current_submodelElement.getMimeType());
				dataTypes.add("String");
			}
		}
		AspectType aspectType = mindSphereMapper.fiStateToMiAspectType(aas.getId(), "None", properties, uoms, dataTypes);
		
		if(isDebugMode) {
			System.out.println(mindSphereGateway.createAsset(aas.getId(), assetVariablesDefinitions, assetVariables, aspectType));
			result = true;
		}else {
			result = mindSphereGateway.saveAsset(aas.getId(), assetVariablesDefinitions, assetVariables, aspectType);
			if(result)
				logger.debug("AssetAdministrationShell created");
			else 		
				logger.error("AssetAdministrationShell couldn't be created");
		}
		return result;
	}
	
	public boolean createMindSphereTimeSeriesFromAAS(AssetAdministrationShell aas) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+aas.getId()+"Asset\"}");
		try {
			List<Timeseries> timeSeriesList = new ArrayList<Timeseries>();
			Date now = new Date();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			String instant = df.format(now);
			Timeseries timeseriesPoint=new Timeseries();
			timeseriesPoint.getFields().put("_time", instant);
			
			for (int i=0; i<aas.getSubmodels().size();i++) {
				SubmodelsObjects current_submodel = aas.getSubmodels().get(i);
				String idShort = current_submodel.getIdShort();
				if(!idShort.equalsIgnoreCase("DataSheet"))
					continue;
				
				for (int j=0; j<current_submodel.getSubmodelElements().size();j++) {
					SubmodelElements current_submodelElement = current_submodel.getSubmodelElements().get(j);	
					String property = current_submodelElement.getIdShort();
					Object value = current_submodelElement.getValue();
					timeseriesPoint.getFields().put(property,value.toString());
				}
			}

			timeSeriesList.add(timeseriesPoint);
			mindSphereGateway.putTimeSeries(assets.get(0).getAssetId(), aas.getId()+"AspectType", timeSeriesList);
			logger.debug("AssetAdministrationShell updated");
		
		} catch (Exception e) {
			// Exception handling
			e.printStackTrace();
			return false;
		}
		return true;
	}	



}

