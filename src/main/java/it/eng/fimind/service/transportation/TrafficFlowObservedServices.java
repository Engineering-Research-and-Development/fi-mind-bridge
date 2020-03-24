package it.eng.fimind.service.transportation;

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

import it.eng.fimind.model.fiware.transportation.TrafficFlowObserved;
import it.eng.fimind.util.MindSphereGateway;
import it.eng.fimind.util.MindSphereMapper;
import it.eng.fimind.util.ServiceResult;

/**
 * Root resource (exposed at "trafficflowobserved" path)
 */
@Path("trafficFlowObserved")
public class TrafficFlowObservedServices {
	private static Logger logger = Logger.getLogger(TrafficFlowObservedServices.class);
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getIt() {
		return "TrafficFlowObserved Service: got it!!";
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createDataInJSON(@HeaderParam("debug-mode") String debugMode, @Valid TrafficFlowObserved trafficFlowObserved) { 
		ServiceResult serviceResult = new ServiceResult();
		logger.debug("Id ="+trafficFlowObserved.getId());
		
		if(debugMode!=null && debugMode.equals("true")){
			System.out.println("DEBUG MODE FOR --- TrafficFlowObserved ---");
			createMindSphereAssetFromTrafficFlowObserved(trafficFlowObserved, true);
			serviceResult.setResult("Test gone fine");
			return Response.status(200).entity(serviceResult).build();
		}else {
			Boolean result = false;
			if(!trafficFlowObservedDoesAlreadyExist(trafficFlowObserved)) 
				result = createMindSphereAssetFromTrafficFlowObserved(trafficFlowObserved, false);
			
			result = createMindSphereTimeSeriesFromTrafficFlowObserved(trafficFlowObserved);
			
			if(result) {
				serviceResult.setResult("TrafficFlowObserved added succesfully");
				return Response.status(201).entity(serviceResult).build();
			}
			else {
				serviceResult.setResult("Something went wrong, check your FI-MIND logs");
				return Response.status(500).entity(serviceResult).build();
			}
		}
	}
	
	
	private Boolean trafficFlowObservedDoesAlreadyExist(TrafficFlowObserved trafficFlowObserved)
	{
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+trafficFlowObserved.getId()+"Asset\"}");
		return assets.size()>0;
	}
	
	public Boolean createMindSphereAssetFromTrafficFlowObserved(TrafficFlowObserved trafficFlowObserved, Boolean isDebugMode) 
	{
		Boolean result = false;
		
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		MindSphereMapper mindSphereMapper = new MindSphereMapper();
		
		Location mindSphereLocation = null;
		if(trafficFlowObserved.getLocation()!=null) {
			if(trafficFlowObserved.getLocation().getType().equals("Point")) 
				mindSphereLocation = mindSphereMapper.fiLocationToMiLocation(trafficFlowObserved.getLocation());
		}else if(trafficFlowObserved.getAddress()!=null) 
			mindSphereLocation = mindSphereMapper.fiAddressToMiLocation(trafficFlowObserved.getAddress());
		
		
		List<String> keys = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		List<String> varDefDataTypes = new ArrayList<String>();

		if(trafficFlowObserved.getSource()!=null) {
			keys.add("Source");
			values.add(trafficFlowObserved.getSource());
			varDefDataTypes.add("String");
		}
		if(trafficFlowObserved.getVehicleType()!=null) {
			keys.add("VehicleType");		
			values.add(trafficFlowObserved.getVehicleType());
			varDefDataTypes.add("String");
		}
		if(trafficFlowObserved.getVehicleSubType()!=null) {
			keys.add("VehicleSubType");
			values.add(trafficFlowObserved.getVehicleSubType());
			varDefDataTypes.add("String");
		}
		if(trafficFlowObserved.getDataProvider()!=null) {
			keys.add("DataProvider");
			values.add(trafficFlowObserved.getDataProvider());
			varDefDataTypes.add("String");
		}
		if(trafficFlowObserved.getRefRoadSegment()!=null) {
			keys.add("RefRoadSegment");		
			values.add(trafficFlowObserved.getRefRoadSegment());
			varDefDataTypes.add("String");
		}
		if(trafficFlowObserved.getLaneId()!=null) {
			keys.add("LaneId");
			values.add(trafficFlowObserved.getLaneId().toString());
			varDefDataTypes.add("Integer");
		}
		if(trafficFlowObserved.getLaneDirection()!=null) {
			keys.add("LaneDirection");
			values.add(trafficFlowObserved.getLaneDirection());
			varDefDataTypes.add("String");
		}
		if(trafficFlowObserved.getDateCreated()!=null) {
			keys.add("DateCreated");
			values.add(trafficFlowObserved.getDateCreated());
			varDefDataTypes.add("Timestamp");
		}
		if(trafficFlowObserved.getName()!=null) {
			keys.add("Name");		
			values.add(trafficFlowObserved.getName());
			varDefDataTypes.add("String");
		}
		List<VariableDefinition> assetVariablesDefinitions = mindSphereMapper.fiPropertiesToMiVariablesDefinitions(keys, values, varDefDataTypes);
		List<Variable> assetVariables = mindSphereMapper.fiPropertiesToMiVariables(keys, values, varDefDataTypes);

		
		List<String> properties = Stream.of("DateModified", "DateObserved", "DateObservedFrom", "DateObservedTo", "Intensity","Occupancy", "AverageVehicleSpeed", "AverageVehicleLength", "Congested", "AverageHeadwayTime", "AverageGapDistance", "ReversedLane").collect(Collectors.toList());
		List<String> uoms = Stream.of("t", "t", "t", "t", "Dimensionless", "Dimensionless", "km/h", "m", "Dimensionless", "s", "m", "Dimensionless").collect(Collectors.toList());
		List<String> dataTypes = Stream.of("Timestamp", "String", "Timestamp", "Timestamp", "Integer", "Integer", "Double", "Double", "Boolean", "Double", "Double", "Boolean").collect(Collectors.toList());
		AspectType aspectType = mindSphereMapper.fiStateToMiAspectType(trafficFlowObserved.getId(), trafficFlowObserved.getDescription(), properties, uoms, dataTypes);
		
		
		if(isDebugMode) {
			System.out.println(mindSphereGateway.createAsset(trafficFlowObserved.getId(), mindSphereLocation, assetVariablesDefinitions, assetVariables, aspectType));
			result = true;
		}else {
			result = mindSphereGateway.saveAsset(trafficFlowObserved.getId(), mindSphereLocation, assetVariablesDefinitions, assetVariables, aspectType);
			if(result)
				logger.debug("TrafficFlowObserved created");
			else 		
				logger.error("TrafficFlowObserved couldn't be created");
		}
		return result;
	}
	
	public boolean createMindSphereTimeSeriesFromTrafficFlowObserved(TrafficFlowObserved trafficFlowObserved) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+trafficFlowObserved.getId()+"Asset\"}");
		try {
			List<Timeseries> timeSeriesList = new ArrayList<Timeseries>();
			Date now = new Date();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			String instant = df.format(now);
			Timeseries timeseriesPoint=new Timeseries();
			timeseriesPoint.getFields().put("_time", instant);
		
			if(trafficFlowObserved.getDateModified()!=null) {
				timeseriesPoint.getFields().put("DateModified", trafficFlowObserved.getDateModified());
			}
			if(trafficFlowObserved.getDateObserved()!=null) {
				timeseriesPoint.getFields().put("DateObserved", trafficFlowObserved.getDateObserved());
			}
			if(trafficFlowObserved.getDateObservedFrom()!=null) {	
				timeseriesPoint.getFields().put("DateObservedFrom", trafficFlowObserved.getDateObservedFrom());
			}
			if(trafficFlowObserved.getDateObservedTo()!=null) {
				timeseriesPoint.getFields().put("DateObservedTo", trafficFlowObserved.getDateObservedTo());
			}
			if(trafficFlowObserved.getIntensity()!=null) {
				timeseriesPoint.getFields().put("Intensity", trafficFlowObserved.getIntensity());
			}
			if(trafficFlowObserved.getOccupancy()!=null) {
				timeseriesPoint.getFields().put("Occupancy", trafficFlowObserved.getOccupancy());
			}
			if(trafficFlowObserved.getAverageVehicleSpeed()!=null) {
				timeseriesPoint.getFields().put("AverageVehicleSpeed", trafficFlowObserved.getAverageVehicleSpeed());
			}
			if(trafficFlowObserved.getAverageVehicleLength()!=null) {
				timeseriesPoint.getFields().put("AverageVehicleLength", trafficFlowObserved.getAverageVehicleLength());
			}
			if(trafficFlowObserved.getCongested()!=null) {
				timeseriesPoint.getFields().put("Congested", trafficFlowObserved.getCongested());
			}
			if(trafficFlowObserved.getAverageHeadwayTime()!=null) {
				timeseriesPoint.getFields().put("AverageHeadwayTime", trafficFlowObserved.getAverageHeadwayTime());
			}
			if(trafficFlowObserved.getAverageGapDistance()!=null) {
				timeseriesPoint.getFields().put("AverageGapDistance", trafficFlowObserved.getAverageGapDistance());
			}
			if(trafficFlowObserved.getReversedLane()!=null) {
				timeseriesPoint.getFields().put("ReversedLane", trafficFlowObserved.getReversedLane());
			}
			
			timeSeriesList.add(timeseriesPoint);
			mindSphereGateway.putTimeSeries(assets.get(0).getAssetId(), trafficFlowObserved.getId()+"AspectType", timeSeriesList);
			logger.debug("TrafficFlowObserved updated");

		} catch (Exception e) {
			// Exception handling
			e.printStackTrace();
			return false;
		}
		return true;
	}	
}