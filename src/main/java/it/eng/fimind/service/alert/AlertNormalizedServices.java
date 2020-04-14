package it.eng.fimind.service.alert;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
import com.siemens.mindsphere.sdk.assetmanagement.model.Location;
import com.siemens.mindsphere.sdk.assetmanagement.model.Variable;
import com.siemens.mindsphere.sdk.assetmanagement.model.VariableDefinition;
import com.siemens.mindsphere.sdk.timeseries.model.Timeseries;

import it.eng.fimind.model.fiware.alert.AlertNormalized;
import it.eng.fimind.util.MindSphereGateway;
import it.eng.fimind.util.MindSphereMapper;
import it.eng.fimind.util.ServiceResult;


/**
 * Root resource (exposed at "alertnormalized" path)
 */
@Path("alertNormalized")
public class AlertNormalizedServices {
	private static Logger logger = Logger.getLogger(AlertNormalizedServices.class);
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getIt() {
		return "AlertNormalized Service: got it!!";
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createDataInJSON(@HeaderParam("debug-mode") String debugMode, @Valid AlertNormalized alert) { 
		ServiceResult serviceResult = new ServiceResult();
		logger.debug("Id ="+alert.getId());
		
		if(debugMode!=null && debugMode.equals("true")){
			System.out.println("DEBUG MODE FOR --- AlertNormalized ---");
			createMindSphereAssetFromAlert(alert, true);
			serviceResult.setResult("Test gone fine");
			return Response.status(200).entity(serviceResult).build();
		}else {
			Boolean result = false;
			if(!alertDoesAlreadyExist(alert)) 
				result = createMindSphereAssetFromAlert(alert, false);
			
			if(result)
				result = createMindSphereTimeSeriesFromAlert(alert);
			
			if(result) {
				serviceResult.setResult("AlertNormalized added succesfully");
				return Response.status(201).entity(serviceResult).build();
			}
			else {
				serviceResult.setResult("Something went wrong, check your FI-MIND logs");
				return Response.status(500).entity(serviceResult).build();
			}
		}
	}

	private Boolean alertDoesAlreadyExist(AlertNormalized alert)
	{
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+alert.getId()+"Asset\"}");
		return assets.size()>0;
	}
	
	private Boolean createMindSphereAssetFromAlert(AlertNormalized alert, Boolean isDebugMode) {
		Boolean result = false;
		
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		MindSphereMapper mindSphereMapper = new MindSphereMapper();

		Location mindSphereLocation = null;
		if(alert.getLocation()!=null && alert.getAddress()!=null)
			mindSphereLocation = mindSphereMapper.fiLocAddrToMiLocation(alert.getLocation().getValue(), alert.getAddress().getValue());
		else if(alert.getLocation()!=null)
			mindSphereLocation = mindSphereMapper.fiLocationToMiLocation(alert.getLocation().getValue());
		else if(alert.getAddress()!=null) 
			mindSphereLocation = mindSphereMapper.fiAddressToMiLocation(alert.getAddress().getValue());
		
		
		List<String> keys = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		List<String> varDefDataTypes = new ArrayList<String>();

		if(alert.getType()!=null) {
			keys.add("entityType");
			values.add(alert.getType());
			varDefDataTypes.add("String");
		}
		if(alert.getSource()!=null) {
			keys.add("source");
			values.add((String) alert.getSource().getValue());
			varDefDataTypes.add("String");
		}
		if(alert.getDataProvider()!=null) {
			keys.add("dataProvider");
			values.add((String) alert.getDataProvider().getValue());
			varDefDataTypes.add("String");
		}
		if(alert.getCategory()!=null) {
			keys.add("category");
			values.add((String) alert.getCategory().getValue());
			varDefDataTypes.add("String");
		}
		if(alert.getSubCategory()!=null) {
			keys.add("subCategory");
			values.add((String) alert.getSubCategory().getValue());
			varDefDataTypes.add("String");
		}
		if(alert.getValidFrom()!=null) {
			keys.add("validFrom");
			values.add((String) alert.getValidFrom().getValue());
			varDefDataTypes.add("Timestamp");
		}
		if(alert.getValidTo()!=null) {
			keys.add("validTo");
			values.add((String) alert.getValidTo().getValue());
			varDefDataTypes.add("Timestamp");
		}
		if(alert.getAlertSource()!=null) {
			keys.add("alertSource");
			values.add((String) alert.getAlertSource().getValue());
			varDefDataTypes.add("String");
		}
		if(alert.getData()!=null) {
			keys.add("data");
			values.add((String) alert.getData().getValue());
			varDefDataTypes.add("String");
		}
		List<VariableDefinition> assetVariablesDefinitions = mindSphereMapper.fiPropertiesToMiVariablesDefinitions(keys, values, varDefDataTypes);
		List<Variable> assetVariables = mindSphereMapper.fiPropertiesToMiVariables(keys, values, varDefDataTypes);

	
		List<String> properties = Stream.of("dateIssued", "severity").collect(Collectors.toList());
		List<String> uoms = Stream.of("t", "Dimensionless").collect(Collectors.toList());
		List<String> dataTypes = Stream.of("Timestamp", "String").collect(Collectors.toList());
		AspectType aspectType = mindSphereMapper.fiStateToMiAspectType(alert.getId(), (String) alert.getDescription().getValue(), properties, uoms, dataTypes);
		
		
		if(isDebugMode) {
			System.out.println(mindSphereGateway.createAsset(alert.getId(), mindSphereLocation, assetVariablesDefinitions, assetVariables, aspectType));
			result = true;
		}else {
			result = mindSphereGateway.saveAsset(alert.getId(), mindSphereLocation, assetVariablesDefinitions, assetVariables, aspectType);
			if(result)
				logger.debug("AlertNormalized created");
			else 		
				logger.error("AlertNormalized couldn't be created");
		}
		return result;
	}

	private boolean createMindSphereTimeSeriesFromAlert(AlertNormalized alert) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+alert.getId()+"Asset\"}");
		try {
			List<Timeseries> timeSeriesList = new ArrayList<Timeseries>();
			Date now = new Date();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			String instant = df.format(now);
			Timeseries timeseriesPoint=new Timeseries();
			timeseriesPoint.getFields().put("_time", instant);
			
			if(alert.getDateIssued()!=null) {
				timeseriesPoint.getFields().put("dateIssued",(String) alert.getDateIssued().getValue());
			}
			if(alert.getSeverity()!=null) {
				timeseriesPoint.getFields().put("severity",(String) alert.getSeverity().getValue());
			}
			
			timeSeriesList.add(timeseriesPoint);
			mindSphereGateway.putTimeSeries(assets.get(0).getAssetId(), alert.getId()+"AspectType", timeSeriesList);
			logger.debug("AlertNormalized updated");
		
		} catch (Exception e) {
			// Exception handling
			e.printStackTrace();
			return false;
		}
		return true;
	}	
}
