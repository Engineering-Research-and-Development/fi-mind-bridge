package it.eng.fimind.service.aas;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.siemens.mindsphere.sdk.assetmanagement.model.AspectType;
import com.siemens.mindsphere.sdk.assetmanagement.model.AspectVariable;
import com.siemens.mindsphere.sdk.assetmanagement.model.AssetResource;
import com.siemens.mindsphere.sdk.assetmanagement.model.AspectType.CategoryEnum;
import com.siemens.mindsphere.sdk.assetmanagement.model.AspectType.ScopeEnum;
import com.siemens.mindsphere.sdk.assetmanagement.model.AspectVariable.DataTypeEnum;
import com.siemens.mindsphere.sdk.timeseries.model.Timeseries;

import it.eng.fimind.model.zvei.aas.AssetAdministrationShell;
import it.eng.fimind.model.zvei.aas.ConceptDescriptionsObjects;
import it.eng.fimind.model.zvei.aas.EmbeddedDataSpecificationsElement;
import it.eng.fimind.model.zvei.aas.KeysElement;

import it.eng.fimind.util.MindSphereGateway;
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
	public Response createDataInJSON(@Valid AssetAdministrationShell aas) { 
		ServiceResult serviceResult = new ServiceResult();
		logger.debug("Id ="+aas.getId());
		if(!aasDoesAlreadyExist(aas)) {
			createMindSphereAssetFromAAS(aas);
		}
		createMindSphereTimeSeriesFromAAS(aas);
		getEverythingFromAAS(aas);
		
		serviceResult.setResult("OK");
		return Response.status(201).entity(serviceResult).build();
	}
	
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
	
	private boolean createMindSphereAssetFromAAS(AssetAdministrationShell aas) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		AspectType aspectType = new AspectType();
		
		aspectType.setName((String) aas.getId()+"Aspect");
		//aspectType.setDescription(aas.getDescription());
		aspectType.setScope(ScopeEnum.PRIVATE);
		aspectType.setCategory(CategoryEnum.DYNAMIC);
		
		List<AspectVariable> variables=new ArrayList<AspectVariable>();

		for (int i=0; i<aas.getConceptDescriptions().size();i++) {
			ConceptDescriptionsObjects current_conceptDescription = aas.getConceptDescriptions().get(i);
			for (int j=0; j<current_conceptDescription.getEmbeddedDataSpecifications().size();j++) {
				EmbeddedDataSpecificationsElement current_embeddedDataSpecification = current_conceptDescription.getEmbeddedDataSpecifications().get(j);
				
				String property = current_embeddedDataSpecification.getDataSpecificationContent().getShortName();
				String uom = current_embeddedDataSpecification.getDataSpecificationContent().getPreferredName().getText();
				
				AspectVariable var = new AspectVariable();
				var.setName(property);
				var.setDataType(DataTypeEnum.STRING);
				var.setLength(20);
				var.setUnit(uom);
				var.setSearchable(true);
				var.setQualityCode(true);
				variables.add(var);
			}
		}
				
		aspectType.setVariables(variables);
		mindSphereGateway.createAsset(aas.getId(), aspectType);
		logger.debug("AssetAdministrationShell created");
		return true;
	}

	
	private boolean createMindSphereTimeSeriesFromAAS(AssetAdministrationShell aas) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+aas.getId()+"Asset\"}");
		try {
			List<Timeseries> timeSeriesList = new ArrayList<Timeseries>();
			Date now = new Date();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			String instant = df.format(now);
			Timeseries timeseriesPoint=new Timeseries();
			timeseriesPoint.getFields().put("_time", instant);
			
			for (int i=0; i<aas.getConceptDescriptions().size();i++) {
				ConceptDescriptionsObjects current_conceptDescription = aas.getConceptDescriptions().get(i);
				for (int j=0; j<current_conceptDescription.getEmbeddedDataSpecifications().size();j++) {
					EmbeddedDataSpecificationsElement current_embeddedDataSpecification = current_conceptDescription.getEmbeddedDataSpecifications().get(j);
					String property = current_embeddedDataSpecification.getDataSpecificationContent().getShortName();
					for (int k=0; k<current_embeddedDataSpecification.getDataSpecificationContent().getUnitId().getKeys().size();k++) {
						KeysElement current_key = current_embeddedDataSpecification.getDataSpecificationContent().getUnitId().getKeys().get(k);
						timeseriesPoint.getFields().put(property,current_key.getValue());
					}
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
