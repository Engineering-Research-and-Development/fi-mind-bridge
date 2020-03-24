package it.eng.fimind.service.building;

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
import com.siemens.mindsphere.sdk.assetmanagement.model.Variable;
import com.siemens.mindsphere.sdk.assetmanagement.model.VariableDefinition;
import com.siemens.mindsphere.sdk.timeseries.model.Timeseries;

import it.eng.fimind.model.fiware.building.BuildingOperationNormalized;
import it.eng.fimind.util.MindSphereGateway;
import it.eng.fimind.util.MindSphereMapper;
import it.eng.fimind.util.ServiceResult;

/**
 * Root resource (exposed at "buildingoperationnormalized" path)
 */
@Path("buildingOperationNormalized")
public class BuildingOperationNormalizedServices {
	private static Logger logger = Logger.getLogger(BuildingOperationNormalizedServices.class);
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getIt() {
		return "BuildingOperationNormalized Service: got it!!";
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createDataInJSON(@HeaderParam("debug-mode") String debugMode, @Valid BuildingOperationNormalized buildingOperation) { 
		ServiceResult serviceResult = new ServiceResult();
		logger.debug("Id ="+buildingOperation.getId());
		
		if(debugMode!=null && debugMode.equals("true")){
			System.out.println("DEBUG MODE FOR --- BuildingOperation ---");
			createMindSphereAssetFromBuildingOperation(buildingOperation, true);
			serviceResult.setResult("Test gone fine");
			return Response.status(200).entity(serviceResult).build();
		}else {
			Boolean result = false;
			if(!buildingOperationDoesAlreadyExist(buildingOperation)) 
				result = createMindSphereAssetFromBuildingOperation(buildingOperation, false);
			
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

	private Boolean buildingOperationDoesAlreadyExist(BuildingOperationNormalized buildingOperation)
	{
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+buildingOperation.getId()+"Asset\"}");
		return assets.size()>0;
	}
	
	private Boolean createMindSphereAssetFromBuildingOperation(BuildingOperationNormalized buildingOperation, Boolean isDebugMode) {
		Boolean result = false;
		
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		MindSphereMapper mindSphereMapper = new MindSphereMapper();
	
		buildingOperation.setId(buildingOperation.getId().replaceAll("-","_"));

		List<String> keys = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		List<String> varDefDataTypes = new ArrayList<String>();

		if(buildingOperation.getSource()!=null)
		{
			keys.add("Source");
			values.add((String) buildingOperation.getSource().getValue());
			varDefDataTypes.add("String");
		}
		if(buildingOperation.getDataProvider()!=null)
		{
			keys.add("DataProvider");
			values.add((String) buildingOperation.getDataProvider().getValue());
			varDefDataTypes.add("String");
		}
		if(buildingOperation.getDateModified()!=null)
		{
			keys.add("DateModified");
			values.add((String) buildingOperation.getDateModified().getValue());
			varDefDataTypes.add("Timestamp");
		}
		if(buildingOperation.getDateCreated()!=null)
		{
			keys.add("DateCreated");		
			values.add((String) buildingOperation.getDateCreated().getValue());
			varDefDataTypes.add("Timestamp");

		}
		if(buildingOperation.getRefBuilding()!=null)
		{
			keys.add("RefBuilding");
			values.add((String) buildingOperation.getRefBuilding().getValue());
			varDefDataTypes.add("String");
		}
		if(buildingOperation.getRefOperator()!=null)
		{
			keys.add("RefOperator");		
			values.add((String) buildingOperation.getRefOperator().getValue());
			varDefDataTypes.add("String");
		}
		if(buildingOperation.getRefRelatedBuildingOperation()!=null)
		{
			keys.add("RefRelatedBuildingOperation");
			values.add((String) buildingOperation.getRefRelatedBuildingOperation().getValue().toString());
			varDefDataTypes.add("String");
		}
		if(buildingOperation.getStartDate()!=null)
		{
			keys.add("StartDate");		
			values.add((String) buildingOperation.getStartDate().getValue());
			varDefDataTypes.add("Timestamp");
		}
		if(buildingOperation.getEndDate()!=null)
		{
			keys.add("EndDate");
			values.add((String) buildingOperation.getEndDate().getValue());
			varDefDataTypes.add("Timestamp");
		}
		if(buildingOperation.getRefRelatedDeviceOperation()!=null)
		{	
			keys.add("RefRelatedDeviceOperation");
			values.add((String) buildingOperation.getRefRelatedDeviceOperation().getValue().toString());
			varDefDataTypes.add("String");
		}
		List<VariableDefinition> assetVariablesDefinitions = mindSphereMapper.fiPropertiesToMiVariablesDefinitions(keys, values, varDefDataTypes);
		List<Variable> assetVariables = mindSphereMapper.fiPropertiesToMiVariables(keys, values, varDefDataTypes);
		
	
		List<String> properties = Stream.of("OperationType","Status","Result","DateStarted", "DateFinished").collect(Collectors.toList());
		List<String> uoms = Stream.of("Dimensionless","Dimensionless","Dimensionless", "t", "t").collect(Collectors.toList());
		List<String> dataTypes = Stream.of("String", "String", "String", "Timestamp", "Timestamp").collect(Collectors.toList());
		if(buildingOperation.getOperationSequence()!=null) {
			for (int i=0; i<buildingOperation.getOperationSequence().getValue().size(); i++) {
				String property = buildingOperation.getOperationSequence().getValue().get(i).toString().split("=")[0];
				String uom = "Undefined";
				properties.add(property);
				uoms.add(uom);
				dataTypes.add("Double");
			}
		}
		AspectType aspectType = mindSphereMapper.fiStateToMiAspectType(buildingOperation.getId(), (String) buildingOperation.getDescription().getValue(), properties, uoms, dataTypes);

		
		if(isDebugMode) {
			System.out.println(mindSphereGateway.createAsset(buildingOperation.getId(), assetVariablesDefinitions, assetVariables, aspectType));
			result = true;
		}else {
			result = mindSphereGateway.saveAsset(buildingOperation.getId(), assetVariablesDefinitions, assetVariables, aspectType);
			if(result)
				logger.debug("BuildingOperationNormalized created");
			else 		
				logger.error("BuildingOperationNormalized couldn't be created");
		}
		return result;
	}
	
	private boolean createMindSphereTimeSeriesFromBuildingOperation(BuildingOperationNormalized buildingOperation) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+buildingOperation.getId()+"Asset\"}");
		try {
			List<Timeseries> timeSeriesList = new ArrayList<Timeseries>();
			Date now = new Date();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			String instant = df.format(now);
			Timeseries timeseriesPoint = new Timeseries();
			timeseriesPoint.getFields().put("_time", instant);
			
			if(buildingOperation.getOperationType()!=null) {
				timeseriesPoint.getFields().put("OperationType", (String) buildingOperation.getOperationType().getValue());
			}
			if(buildingOperation.getStatus()!=null) {
				timeseriesPoint.getFields().put("Status", (String) buildingOperation.getStatus().getValue());
			}
			if(buildingOperation.getResult()!=null) {
				timeseriesPoint.getFields().put("Result", (String) buildingOperation.getResult().getValue());
			}
			if(buildingOperation.getDateStarted()!=null)
			{
				timeseriesPoint.getFields().put("DateStarted", (String) buildingOperation.getDateStarted().getValue());
			}
			if(buildingOperation.getDateFinished()!=null)
			{
				timeseriesPoint.getFields().put("DateFinished", (String) buildingOperation.getDateFinished().getValue());
			}
			if(buildingOperation.getOperationSequence()!=null) {
				for (int i=0; i<buildingOperation.getOperationSequence().getValue().size(); i++) {
					String property = buildingOperation.getOperationSequence().getValue().get(i).toString().split("=")[0];
					String value = buildingOperation.getOperationSequence().getValue().get(i).toString().split("=")[1];
					timeseriesPoint.getFields().put(property,value);
				}
			}
			timeSeriesList.add(timeseriesPoint);
			mindSphereGateway.putTimeSeries(assets.get(0).getAssetId(), buildingOperation.getId()+"AspectType", timeSeriesList);
			logger.debug("BuildingOperationNormalized updated");
		
		} catch (Exception e) {
			// Exception handling
			e.printStackTrace();
			return false;
		}
		return true;
	}	
	
}