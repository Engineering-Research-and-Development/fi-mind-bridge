package it.eng.fimind.service.building;

import java.time.Instant;
import java.util.ArrayList;
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
import com.siemens.mindsphere.sdk.assetmanagement.model.Variable;
import com.siemens.mindsphere.sdk.assetmanagement.model.VariableDefinition;
import com.siemens.mindsphere.sdk.timeseries.model.Timeseries;

import it.eng.fimind.model.fiware.building.BuildingOperation;
import it.eng.fimind.util.MindSphereGateway;
import it.eng.fimind.util.MindSphereMapper;
import it.eng.fimind.util.ServiceResult;

/**
 * Root resource (exposed at "buildingoperation" path)
 */
@Path("buildingOperation")
public class BuildingOperationServices {
	private static Logger logger = Logger.getLogger(BuildingOperationServices.class);
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getIt() {
		return "BuildingOperation Service: got it!!";
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createDataInJSON(@HeaderParam("debug-mode") String debugMode, @Valid BuildingOperation buildingOperation) { 
		ServiceResult serviceResult = new ServiceResult();
		logger.debug("Id ="+buildingOperation.getId());
		
		if(debugMode!=null && debugMode.equals("true")){
			logger.debug("DEBUG MODE FOR --- BuildingOperation ---");
			createMindSphereAssetFromBuildingOperation(buildingOperation, true);
			serviceResult.setResult("Test gone fine");
			return Response.status(200).entity(serviceResult).build();
		}else {
			Boolean result = true;
			if(!buildingOperationDoesAlreadyExist(buildingOperation)) 
				result = createMindSphereAssetFromBuildingOperation(buildingOperation, false);
			
			if(result)
				result = createMindSphereTimeSeriesFromBuildingOperation(buildingOperation);
			
			if(result) {
				serviceResult.setResult("BuildingOperation added succesfully");
				return Response.status(201).entity(serviceResult).build();
			}
			else {
				serviceResult.setResult("Something went wrong, check your FI-MIND logs");
				return Response.status(500).entity(serviceResult).build();
			}
		}
	}
	
	
	private Boolean buildingOperationDoesAlreadyExist(BuildingOperation buildingOperation)
	{
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+buildingOperation.getId()+"\"}");
		return assets.size()>0;
	}
	
	public Boolean createMindSphereAssetFromBuildingOperation(BuildingOperation buildingOperation, Boolean isDebugMode) {
		Boolean result = false;
		
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		MindSphereMapper mindSphereMapper = new MindSphereMapper();
	
		List<String> keys = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		List<String> varDefDataTypes = new ArrayList<String>();

		if(buildingOperation.getType()!=null) {
			keys.add("entityType");
			values.add(buildingOperation.getType());
			varDefDataTypes.add("String");
		}
		if(buildingOperation.getSource()!=null)
		{
			keys.add("source");
			values.add(buildingOperation.getSource());
			varDefDataTypes.add("String");
		}
		if(buildingOperation.getDataProvider()!=null)
		{
			keys.add("dataProvider");
			values.add(buildingOperation.getDataProvider());
			varDefDataTypes.add("String");
		}
		if(buildingOperation.getDateCreated()!=null)
		{
			keys.add("dateCreated");		
			values.add(buildingOperation.getDateCreated());
			varDefDataTypes.add("Timestamp");

		}
		if(buildingOperation.getRefBuilding()!=null)
		{
			keys.add("refBuilding");
			values.add(buildingOperation.getRefBuilding());
			varDefDataTypes.add("String");
		}
		if(buildingOperation.getRefOperator()!=null)
		{
			keys.add("refOperator");		
			values.add(buildingOperation.getRefOperator());
			varDefDataTypes.add("String");
		}
		if(buildingOperation.getRefRelatedBuildingOperation()!=null)
		{
			keys.add("refRelatedBuildingOperation");
			values.add(buildingOperation.getRefRelatedBuildingOperation().toString());
			varDefDataTypes.add("String");
		}
		if(buildingOperation.getStartDate()!=null)
		{
			keys.add("startDate");		
			values.add(buildingOperation.getStartDate());
			varDefDataTypes.add("Timestamp");
		}
		if(buildingOperation.getEndDate()!=null)
		{
			keys.add("endDate");
			values.add(buildingOperation.getEndDate());
			varDefDataTypes.add("Timestamp");
		}
		if(buildingOperation.getRefRelatedDeviceOperation()!=null)
		{	
			keys.add("refRelatedDeviceOperation");
			values.add(buildingOperation.getRefRelatedDeviceOperation().toString());
			varDefDataTypes.add("String");
		}
		List<VariableDefinition> assetVariablesDefinitions = mindSphereMapper.fiPropertiesToMiVariablesDefinitions(keys, values, varDefDataTypes);
		List<Variable> assetVariables = mindSphereMapper.fiPropertiesToMiVariables(keys, values, varDefDataTypes);

	
		List<String> properties = Stream.of("dateModified", "operationType","status","result","dateStarted", "dateFinished").collect(Collectors.toList());
		List<String> uoms = Stream.of("t", "Dimensionless","Dimensionless","Dimensionless", "t", "t").collect(Collectors.toList());
		List<String> dataTypes = Stream.of("Timestamp", "String", "String", "String", "Timestamp", "Timestamp").collect(Collectors.toList());
		if(buildingOperation.getOperationSequence()!=null) {
			for (int i=0; i<buildingOperation.getOperationSequence().size(); i++) {
				String property = buildingOperation.getOperationSequence().get(i).split("=")[0];
				properties.add(property);
				uoms.add("Undefined");
				dataTypes.add("Double");
			}
		}
		AspectType aspectType;
		if(buildingOperation.getDescription()!=null)
			aspectType = mindSphereMapper.fiStateToMiAspectType(buildingOperation.getId(), (String) buildingOperation.getDescription(), properties, uoms, dataTypes);
		else
			aspectType = mindSphereMapper.fiStateToMiAspectType(buildingOperation.getId(), properties, uoms, dataTypes);
					
		
		if(isDebugMode) {
			logger.debug(mindSphereGateway.createAsset(buildingOperation.getId(), assetVariablesDefinitions, assetVariables, aspectType));
			result = true;
		}else {
			result = mindSphereGateway.saveAsset(buildingOperation.getId(), assetVariablesDefinitions, assetVariables, aspectType);
			if(result)
				logger.debug("BuildingOperation created");
			else 		
				logger.error("BuildingOperation couldn't be created");
		}
		return result;
	}
	
	public boolean createMindSphereTimeSeriesFromBuildingOperation(BuildingOperation buildingOperation) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+buildingOperation.getId()+"\"}");
		try {
			List<Timeseries> timeSeriesList = new ArrayList<Timeseries>();

			Timeseries timeseriesPoint = new Timeseries();
	
			timeseriesPoint.getFields().put("_time", Instant.now().toString());
			
			if(buildingOperation.getDateModified()!=null)
			{
				timeseriesPoint.getFields().put("dateModified", buildingOperation.getDateModified());
			}
			if(buildingOperation.getOperationType()!=null) {
				timeseriesPoint.getFields().put("operationType", buildingOperation.getOperationType());
			}
			if(buildingOperation.getStatus()!=null) {
				timeseriesPoint.getFields().put("status", buildingOperation.getStatus());
			}
			if(buildingOperation.getResult()!=null) {
				timeseriesPoint.getFields().put("result", buildingOperation.getResult());
			}
			if(buildingOperation.getDateStarted()!=null)
			{
				timeseriesPoint.getFields().put("dateStarted", buildingOperation.getDateStarted());
			}
			if(buildingOperation.getDateFinished()!=null)
			{
				timeseriesPoint.getFields().put("dateFinished", buildingOperation.getDateFinished());
			}
			if(buildingOperation.getOperationSequence()!=null) {
				for (int i=0; i<buildingOperation.getOperationSequence().size(); i++) {
					String property = buildingOperation.getOperationSequence().get(i).toString().split("=")[0];
					Double value = Double.parseDouble(buildingOperation.getOperationSequence().get(i).toString().split("=")[1]);
					timeseriesPoint.getFields().put(property,value);
				}
			}
			
			timeSeriesList.add(timeseriesPoint);
			mindSphereGateway.putTimeSeries(assets.get(0).getAssetId(), buildingOperation.getId(), timeSeriesList);
			logger.debug("BuildingOperation updated");
		
		} catch (Exception e) {
			// Exception handling
			e.printStackTrace();
			return false;
		}
		return true;
	}	
	
}