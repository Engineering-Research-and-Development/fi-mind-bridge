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
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.siemens.mindsphere.sdk.assetmanagement.model.AspectType;
import com.siemens.mindsphere.sdk.assetmanagement.model.Asset;
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
	public Response createDataInJSON(@Valid TrafficFlowObserved trafficFlowObserved) { 
		ServiceResult serviceResult=new ServiceResult();
		logger.debug("Id ="+trafficFlowObserved.getId());
		
		if(!trafficFlowObservedDoesAlreadyExist(trafficFlowObserved)) 
			saveMindSphereAsset(createMindSphereAssetFromTrafficFlowObserved(trafficFlowObserved));
		
		createMindSphereTimeSeriesFromTrafficFlowObserved(trafficFlowObserved);
		
		serviceResult.setResult("OK");
		return Response.status(201).entity(serviceResult).build();
	}

	
	private Boolean trafficFlowObservedDoesAlreadyExist(TrafficFlowObserved trafficFlowObserved)
	{
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+trafficFlowObserved.getId()+"Asset\"}");
		return assets.size()>0;
	}
	
	public Asset createMindSphereAssetFromTrafficFlowObserved(TrafficFlowObserved trafficFlowObserved) 
	{
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		MindSphereMapper mindSphereMapper = new MindSphereMapper();
		
		trafficFlowObserved.setId(trafficFlowObserved.getId().replaceAll("-","_"));

		Location mindSphereLocation = null;
		if(trafficFlowObserved.getLocation()!=null) {
			if(trafficFlowObserved.getLocation().getType().equals("Point")) 
				mindSphereLocation = mindSphereMapper.fiLocationToMiLocation(trafficFlowObserved.getLocation());
		}else if(trafficFlowObserved.getAddress()!=null) 
			mindSphereLocation = mindSphereMapper.fiAddressToMiLocation(trafficFlowObserved.getAddress());
		
		
		List<String> keys = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		if(trafficFlowObserved.getSource()!=null) {
			keys.add("Source");
			values.add(trafficFlowObserved.getSource());
		}
		if(trafficFlowObserved.getVehicleType()!=null) {
			keys.add("VehicleType");		
			values.add(trafficFlowObserved.getVehicleType());
		}
		if(trafficFlowObserved.getVehicleSubType()!=null) {
			keys.add("VehicleSubType");
			values.add(trafficFlowObserved.getVehicleSubType());
		}
		if(trafficFlowObserved.getDataProvider()!=null) {
			keys.add("DataProvider");
			values.add(trafficFlowObserved.getDataProvider());
		}
		if(trafficFlowObserved.getRefRoadSegment()!=null) {
			keys.add("RefRoadSegment");		
			values.add(trafficFlowObserved.getRefRoadSegment());
		}
		if(trafficFlowObserved.getDateModified()!=null) {
			keys.add("DateModified");
			values.add(trafficFlowObserved.getDateModified());
		}
		if(trafficFlowObserved.getLaneId()!=null) {
			keys.add("LaneId");
			values.add(trafficFlowObserved.getLaneId().toString());
		}
		if(trafficFlowObserved.getLaneDirection()!=null) {
			keys.add("LaneDirection");
			values.add(trafficFlowObserved.getLaneDirection());
		}
		if(trafficFlowObserved.getDateObserved()!=null) {
			keys.add("DateObserved");
			values.add(trafficFlowObserved.getDateObserved());
		}
		if(trafficFlowObserved.getDateObservedFrom()!=null) {	
			keys.add("DateObservedFrom");
			values.add(trafficFlowObserved.getDateObservedFrom());
		}
		if(trafficFlowObserved.getDateObservedTo()!=null) {
			keys.add("DateObservedTo");
			values.add(trafficFlowObserved.getDateObservedTo());
		}
		if(trafficFlowObserved.getDateCreated()!=null) {
			keys.add("DateCreated");
			values.add(trafficFlowObserved.getDateCreated());
		}
		if(trafficFlowObserved.getName()!=null) {
			keys.add("Name");		
			values.add(trafficFlowObserved.getName());
		}
		List<VariableDefinition> assetVariablesDefinitions = mindSphereMapper.fiPropertiesToMiVariablesDefinitions(keys, values);
		List<Variable> assetVariables = mindSphereMapper.fiPropertiesToMiVariables(keys, values);

		
		List<String> properties = Stream.of("Intensity","Occupancy", "AverageVehicleSpeed", "AverageVehicleLength", "Congested", "AverageHeadwayTime", "AverageGapDistance", "ReversedLane").collect(Collectors.toList());
		List<String> uoms = Stream.of("Dimensionless", "Dimensionless", "km/h", "m", "Dimensionless", "s", "m", "Dimensionless").collect(Collectors.toList());
		List<String> dataTypes = Stream.of("Integer", "Integer", "Double", "Double", "Boolean", "Double", "Double", "Boolean").collect(Collectors.toList());
		AspectType aspectType = mindSphereMapper.fiStateToMiAspectType(trafficFlowObserved.getId(), trafficFlowObserved.getDescription(), properties, uoms, dataTypes);
		
		
		return mindSphereGateway.createAsset(trafficFlowObserved.getId(), mindSphereLocation, assetVariablesDefinitions, assetVariables, aspectType);
	}
	
	private Boolean saveMindSphereAsset(Asset asset) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		Boolean result = mindSphereGateway.saveAsset(asset);
		if(result)
			logger.debug("TrafficFlowObserved created");
		else 		
			logger.error("TrafficFlowObserved couldn't be created");
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