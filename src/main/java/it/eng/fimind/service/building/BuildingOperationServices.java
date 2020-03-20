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
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.siemens.mindsphere.sdk.assetmanagement.model.AspectType;
import com.siemens.mindsphere.sdk.assetmanagement.model.Asset;
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
	public Response createDataInJSON(@Valid BuildingOperation buildingOperation) { 
		ServiceResult serviceResult = new ServiceResult();
		logger.debug("Id ="+buildingOperation.getId());
		
		if(!buildingOperationDoesAlreadyExist(buildingOperation)) 
			saveMindSphereAsset(createMindSphereAssetFromBuildingOperation(buildingOperation));
			
		createMindSphereTimeSeriesFromBuildingOperation(buildingOperation);
		
		serviceResult.setResult("OK");
		return Response.status(201).entity(serviceResult).build();
	}

	private Boolean buildingOperationDoesAlreadyExist(BuildingOperation buildingOperation)
	{
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+buildingOperation.getId()+"Asset\"}");
		return assets.size()>0;
	}
	
	public Asset createMindSphereAssetFromBuildingOperation(BuildingOperation buildingOperation) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		MindSphereMapper mindSphereMapper = new MindSphereMapper();
	
		buildingOperation.setId(buildingOperation.getId().replaceAll("-","_"));

		List<String> keys = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		if(buildingOperation.getSource()!=null)
		{
			keys.add("Source");
			values.add(buildingOperation.getSource());
		}
		if(buildingOperation.getDataProvider()!=null)
		{
			keys.add("DataProvider");
			values.add(buildingOperation.getDataProvider());
		}
		if(buildingOperation.getDateModified()!=null)
		{
			keys.add("DateModified");
			values.add(buildingOperation.getDateModified());
		}
		if(buildingOperation.getDateCreated()!=null)
		{
			keys.add("DateCreated");		
			values.add(buildingOperation.getDateCreated());

		}
		if(buildingOperation.getRefBuilding()!=null)
		{
			keys.add("RefBuilding");
			values.add(buildingOperation.getRefBuilding());
		}
		if(buildingOperation.getRefOperator()!=null)
		{
			keys.add("RefOperator");		
			values.add(buildingOperation.getRefOperator());
		}
		if(buildingOperation.getRefRelatedBuildingOperation()!=null)
		{
			keys.add("RefRelatedBuildingOperation");
			values.add(buildingOperation.getRefRelatedBuildingOperation().toString());
		}
		if(buildingOperation.getStartDate()!=null)
		{
			keys.add("StartDate");		
			values.add(buildingOperation.getStartDate());
		}
		if(buildingOperation.getEndDate()!=null)
		{
			keys.add("EndDate");
			values.add(buildingOperation.getEndDate());
		}
		if(buildingOperation.getDateStarted()!=null)
		{
			keys.add("DateStarted");
			values.add(buildingOperation.getDateStarted());
		}
		if(buildingOperation.getDateFinished()!=null)
		{
			keys.add("DateFinished");
			values.add(buildingOperation.getDateFinished());
		}
		if(buildingOperation.getRefRelatedDeviceOperation()!=null)
		{	
			keys.add("RefRelatedDeviceOperation");
			values.add(buildingOperation.getRefRelatedDeviceOperation().toString());
		}
		values.add(buildingOperation.getRefRelatedDeviceOperation().toString());
		List<VariableDefinition> assetVariablesDefinitions = mindSphereMapper.fiPropertiesToMiVariablesDefinitions(keys, values);
		List<Variable> assetVariables = mindSphereMapper.fiPropertiesToMiVariables(keys, values);

	
		List<String> properties = Stream.of("OperationType","Status","Result").collect(Collectors.toList());
		List<String> uoms = Stream.of("Dimensionless","Dimensionless","Dimensionless").collect(Collectors.toList());
		List<String> dataTypes = Stream.of("String", "String", "String").collect(Collectors.toList());
		if(buildingOperation.getOperationSequence()!=null) {
			for (int i=0; i<buildingOperation.getOperationSequence().size(); i++) {
				String property = buildingOperation.getOperationSequence().get(i).split("=")[0];
				String uom = "na";
				properties.add(property);
				uoms.add(uom);
				dataTypes.add("Double");
			}
		}
		AspectType aspectType = mindSphereMapper.fiStateToMiAspectType(buildingOperation.getId(), buildingOperation.getDescription(), properties, uoms, dataTypes);
		
		
		return mindSphereGateway.createAsset(buildingOperation.getId(), assetVariablesDefinitions, assetVariables, aspectType);
	}
	
	private Boolean saveMindSphereAsset(Asset asset) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		Boolean result = mindSphereGateway.saveAsset(asset);
		if(result)
			logger.debug("BuildingOperation created");
		else 		
			logger.error("BuildingOperation couldn't be created");
		return result;
	}
	
	public boolean createMindSphereTimeSeriesFromBuildingOperation(BuildingOperation buildingOperation) {
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
				timeseriesPoint.getFields().put("OperationType", buildingOperation.getOperationType());
			}
			if(buildingOperation.getStatus()!=null) {
				timeseriesPoint.getFields().put("Status", buildingOperation.getStatus());
			}
			if(buildingOperation.getResult()!=null) {
				timeseriesPoint.getFields().put("Result", buildingOperation.getResult());
			}
			if(buildingOperation.getOperationSequence()!=null) {
				for (int i=0; i<buildingOperation.getOperationSequence().size(); i++) {
					String property = buildingOperation.getOperationSequence().get(i).toString().split("=")[0];
					Double value = Double.parseDouble(buildingOperation.getOperationSequence().get(i).toString().split("=")[1]);
					timeseriesPoint.getFields().put(property,value);
				}
			}
			
			timeSeriesList.add(timeseriesPoint);
			mindSphereGateway.putTimeSeries(assets.get(0).getAssetId(), buildingOperation.getId()+"AspectType", timeSeriesList);
			logger.debug("buildingOperation updated");
		
		} catch (Exception e) {
			// Exception handling
			e.printStackTrace();
			return false;
		}
		return true;
	}	
	
}