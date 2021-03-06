package it.eng.fimind.service.alert;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.siemens.mindsphere.sdk.assetmanagement.model.AspectType;
import com.siemens.mindsphere.sdk.assetmanagement.model.AssetResource;
import com.siemens.mindsphere.sdk.assetmanagement.model.Location;
import com.siemens.mindsphere.sdk.assetmanagement.model.Variable;
import com.siemens.mindsphere.sdk.assetmanagement.model.VariableDefinition;
import com.siemens.mindsphere.sdk.timeseries.model.Timeseries;

import it.eng.fimind.model.fiware.alert.Alert;
import it.eng.fimind.util.MindSphereGateway;
import it.eng.fimind.util.MindSphereMapper;
import it.eng.fimind.util.ServiceResult;


/**
 * Root resource (exposed at "alert" path)
 */
@Path("alert")
public class AlertServices {
	private static Logger logger = Logger.getLogger(AlertServices.class);
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getIt() {
		return "Alert Service: got it!!";
	}

	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteIt(@QueryParam("id") String id) {
		logger.debug("[AlertServices] DELETE Request");
		ServiceResult serviceResult = new ServiceResult();

		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		if(mindSphereGateway.deleteAssetOnCascade(id))
		{
			serviceResult.setMessage("Deleted successfully!");
			return Response.status(200).entity(serviceResult).build();
		}
		else {
			serviceResult.setResult("Something went wrong, check your FI-MIND logs");
			return Response.status(500).entity(serviceResult).build();
		}
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createDataInJSON(@HeaderParam("Debug-mode") String debugMode, @Valid Alert alert) { 
		logger.debug("[AlertServices] POST Request");
		ServiceResult serviceResult = new ServiceResult();
		
		logger.debug("Id ="+alert.getId());
		
		if(debugMode!=null && debugMode.equals("true")){
			logger.debug("DEBUG MODE FOR --- Alert ---");
			createMindSphereAssetFromAlert(alert, true);
			serviceResult.setResult("Test gone fine");
			return Response.status(200).entity(serviceResult).build();
		}else {
			Boolean result = true;
			MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
			if(!mindSphereGateway.assetDoesAlreadyExist(alert.getId()))
				result = createMindSphereAssetFromAlert(alert, false);
			
			if(result)
				result = createMindSphereTimeSeriesFromAlert(alert);
			
			if(result) {
				serviceResult.setResult("Alert added succesfully");
				return Response.status(201).entity(serviceResult).build();
			}
			else {
				serviceResult.setResult("Something went wrong, check your FI-MIND logs");
				return Response.status(500).entity(serviceResult).build();
			}
		}
	}

	public Boolean createMindSphereAssetFromAlert(Alert alert, Boolean isDebugMode) {
		Boolean result = false;
		
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		MindSphereMapper mindSphereMapper = new MindSphereMapper();
		
		Location mindSphereLocation = null;
		if(alert.getLocation()!=null && alert.getAddress()!=null)
			mindSphereLocation = mindSphereMapper.fiLocAddrToMiLocation(alert.getLocation(), alert.getAddress());
		else if(alert.getLocation()!=null)
			mindSphereLocation = mindSphereMapper.fiLocationToMiLocation(alert.getLocation());
		else if(alert.getAddress()!=null) 
			mindSphereLocation = mindSphereMapper.fiAddressToMiLocation(alert.getAddress());
		
		
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
			values.add(alert.getSource());
			varDefDataTypes.add("String");
		}
		if(alert.getDataProvider()!=null) {
			keys.add("dataProvider");
			values.add(alert.getDataProvider());
			varDefDataTypes.add("String");
		}
		if(alert.getCategory()!=null) {
			keys.add("category");
			values.add(alert.getCategory());
			varDefDataTypes.add("String");
		}
		if(alert.getSubCategory()!=null) {
			keys.add("subCategory");
			values.add(alert.getSubCategory());
			varDefDataTypes.add("String");
		}
		if(alert.getValidFrom()!=null) {
			keys.add("validFrom");
			values.add(alert.getValidFrom());
			varDefDataTypes.add("Timestamp");
		}
		if(alert.getValidTo()!=null) {
			keys.add("validTo");
			values.add(alert.getValidTo());
			varDefDataTypes.add("Timestamp");
		}
		if(alert.getAlertSource()!=null) {
			keys.add("alertSource");
			values.add(alert.getAlertSource());
			varDefDataTypes.add("String");
		}
		if(alert.getData()!=null) {
			keys.add("data");
			values.add(alert.getData().toString());
			varDefDataTypes.add("String");
		}
		List<VariableDefinition> assetVariablesDefinitions = mindSphereMapper.fiPropertiesToMiVariablesDefinitions(keys, values, varDefDataTypes);
		List<Variable> assetVariables = mindSphereMapper.fiPropertiesToMiVariables(keys, values, varDefDataTypes);

		
		List<String> properties = Stream.of("dateIssued", "severity").collect(Collectors.toList());
		List<String> uoms = Stream.of("t", "Dimensionless").collect(Collectors.toList());
		List<String> dataTypes = Stream.of("Timestamp", "String").collect(Collectors.toList());
		AspectType aspectType;
		if(alert.getDescription()!=null)
			aspectType = mindSphereMapper.fiStateToMiAspectType(alert.getId(), (String) alert.getDescription(), properties, uoms, dataTypes);
		else
			aspectType = mindSphereMapper.fiStateToMiAspectType(alert.getId(), properties, uoms, dataTypes);
					
		
		if(isDebugMode) {
			logger.debug(mindSphereGateway.createAsset(alert.getId(), mindSphereLocation, assetVariablesDefinitions, assetVariables, aspectType));
			result = true;
		}else {
			result = mindSphereGateway.saveAsset(alert.getId(), mindSphereLocation, assetVariablesDefinitions, assetVariables, aspectType);
			if(result)
				logger.debug("Alert created");
			else 		
				logger.error("Alert couldn't be created");
		}
		return result;
	}
	
	public boolean createMindSphereTimeSeriesFromAlert(Alert alert) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();		
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+alert.getId()+"\"}");
		try {
			List<Timeseries> timeSeriesList = new ArrayList<Timeseries>();
			
			Timeseries timeseriesPoint = new Timeseries();
			
			timeseriesPoint.getFields().put("_time", Instant.now().toString());
			
			if(alert.getDateIssued()!=null) {
				timeseriesPoint.getFields().put("dateIssued", alert.getDateIssued());
			}
			if(alert.getSeverity()!=null) {
				timeseriesPoint.getFields().put("severity", alert.getSeverity());
			}
			
			timeSeriesList.add(timeseriesPoint);
			mindSphereGateway.putTimeSeries(assets.get(0).getAssetId(), alert.getId(), timeSeriesList);
			logger.debug("Alert updated");
		
		} catch (Exception e) {
			// Exception handling
			e.printStackTrace();
			return false;
		}
		return true;
	}	
}
