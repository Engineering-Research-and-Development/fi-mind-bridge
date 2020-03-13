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
import com.siemens.mindsphere.sdk.timeseries.model.Timeseries;

import it.eng.fimind.model.fiware.building.BuildingOperation;
import it.eng.fimind.util.MindSphereGateway;
import it.eng.fimind.util.MindSphereMapper;
import it.eng.fimind.util.ServiceResult;

/**
 * Root resource (exposed at "buildingoperation" path)
 */
@Path("buildingoperation")
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
	
	private Asset createMindSphereAssetFromBuildingOperation(BuildingOperation buildingOperation) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		MindSphereMapper mindSphereMapper = new MindSphereMapper();
	
		
		List<String> keys = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		keys.add("Source");
		values.add(buildingOperation.getSource());
		keys.add("DataProvider");
		values.add(buildingOperation.getDataProvider());
		keys.add("DateModified");
		values.add(buildingOperation.getDateModified());
		keys.add("DateCreated");
		values.add(buildingOperation.getDateCreated());
		keys.add("RefBuilding");
		values.add(buildingOperation.getRefBuilding());
		keys.add("RefOperator");
		values.add(buildingOperation.getRefOperator());
		keys.add("OperationType");
		values.add(buildingOperation.getOperationType());
		keys.add("OperationSequence");
		values.add(buildingOperation.getOperationSequence().toString());
		keys.add("RefRelatedBuildingOperation");
		values.add(buildingOperation.getRefRelatedBuildingOperation().toString());
		keys.add("StartDate");
		values.add(buildingOperation.getStartDate());
		keys.add("EndDate");
		values.add(buildingOperation.getEndDate());
		keys.add("DateStarted");
		values.add(buildingOperation.getDateStarted());
		keys.add("DateFinished");
		values.add(buildingOperation.getDateFinished());
		keys.add("RefRelatedDeviceOperation");
		values.add(buildingOperation.getRefRelatedDeviceOperation().toString());
		List<Variable> assetVariables = mindSphereMapper.fiPropertiesToMiVariables(keys, values);
		
	
		List<String> properties = Stream.of("Status","Result").collect(Collectors.toList());
		List<String> uoms = Stream.of("Dimensionless","Dimensionless").collect(Collectors.toList());
		AspectType aspectType = mindSphereMapper.fiStateToMiAspectType(buildingOperation.getId(), buildingOperation.getDescription(), properties, uoms);
		
		
		return mindSphereGateway.createAsset(buildingOperation.getId(), assetVariables, aspectType);
	}
	
	private boolean saveMindSphereAsset(Asset asset) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		logger.debug("Building Operation created");
		return mindSphereGateway.saveAsset(asset);
	}
	
	private boolean createMindSphereTimeSeriesFromBuildingOperation(BuildingOperation buildingOperation) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+buildingOperation.getId()+"Asset\"}");
		try {
			List<Timeseries> timeSeriesList = new ArrayList<Timeseries>();
			Date now = new Date();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			String instant = df.format(now);
			Timeseries timeseriesPoint = new Timeseries();
			timeseriesPoint.getFields().put("_time", instant);
			
			timeseriesPoint.getFields().put("Status", buildingOperation.getStatus());
			timeseriesPoint.getFields().put("Result", buildingOperation.getResult());

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