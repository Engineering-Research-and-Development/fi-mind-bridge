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
import com.siemens.mindsphere.sdk.timeseries.model.Timeseries;

import it.eng.fimind.model.fiware.transportation.TrafficFlowObserved;
import it.eng.fimind.util.MindSphereGateway;
import it.eng.fimind.util.MindSphereMapper;
import it.eng.fimind.util.ServiceResult;

/**
 * Root resource (exposed at "trafficflowobserved" path)
 */
@Path("trafficflowobserved")
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
	
	private Asset createMindSphereAssetFromTrafficFlowObserved(TrafficFlowObserved trafficFlowObserved) 
	{
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		MindSphereMapper mindSphereMapper = new MindSphereMapper();
		
		Location mindSphereLocation = null;
		if(trafficFlowObserved.getLocation().getType().equals("Point")) 
			mindSphereLocation = mindSphereMapper.fiLocationToMiLocation(trafficFlowObserved.getLocation());
		else 
			mindSphereLocation = mindSphereMapper.fiAddressToMiLocation(trafficFlowObserved.getAddress());
		
		
		List<String> keys = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		keys.add("Source");
		values.add(trafficFlowObserved.getSource());
		keys.add("VehicleType");
		values.add(trafficFlowObserved.getVehicleType());
		keys.add("VehicleSubType");
		values.add(trafficFlowObserved.getVehicleSubType());
		keys.add("DataProvider");
		values.add(trafficFlowObserved.getDataProvider());
		keys.add("RefRoadSegment");
		values.add(trafficFlowObserved.getRefRoadSegment());
		keys.add("DateModified");
		values.add(trafficFlowObserved.getDateModified());
		keys.add("LaneId");
		values.add(trafficFlowObserved.getLaneId().toString());
		keys.add("LaneDirection");
		values.add(trafficFlowObserved.getLaneDirection());
		keys.add("ReversedLane");
		values.add(trafficFlowObserved.getReversedLane().toString());
		keys.add("DateObserved");
		values.add(trafficFlowObserved.getDateObserved());
		keys.add("DateObservedFrom");
		values.add(trafficFlowObserved.getDateObservedFrom());
		keys.add("DateObservedTo");
		values.add(trafficFlowObserved.getDateObservedTo());
		keys.add("DateCreated");
		values.add(trafficFlowObserved.getDateCreated());
		keys.add("Name");
		values.add(trafficFlowObserved.getName());
		List<Variable> assetVariables = mindSphereMapper.fiPropertiesToMiVariables(keys, values);
		

		List<String> properties = Stream.of("Intensity","Occupancy", "AverageVehicleSpeed", "AverageVehicleLength", "Congested", "AverageHeadwayTime", "AverageGapDistance").collect(Collectors.toList());
		List<String> uoms = Stream.of("Dimensionless", "Dimensionless", "km/h", "m", "Dimensionless", "s", "m").collect(Collectors.toList());
		AspectType aspectType = mindSphereMapper.fiStateToMiAspectType(trafficFlowObserved.getId(), trafficFlowObserved.getDescription(), properties, uoms);
		
		
		return mindSphereGateway.createAsset(trafficFlowObserved.getId(), mindSphereLocation, assetVariables, aspectType);
	}
	
	private boolean saveMindSphereAsset(Asset asset) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		logger.debug("TrafficFlowObserved created");
		return mindSphereGateway.saveAsset(asset);
	}
	
	private boolean createMindSphereTimeSeriesFromTrafficFlowObserved(TrafficFlowObserved trafficFlowObserved) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+trafficFlowObserved.getId()+"Asset\"}");
		try {
			List<Timeseries> timeSeriesList = new ArrayList<Timeseries>();
			Date now = new Date();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			String instant = df.format(now);
			Timeseries timeseriesPoint=new Timeseries();
			timeseriesPoint.getFields().put("_time", instant);
		
			timeseriesPoint.getFields().put("Intensity",(Integer) trafficFlowObserved.getIntensity());
			timeseriesPoint.getFields().put("Occupancy",(Integer) trafficFlowObserved.getOccupancy());
			timeseriesPoint.getFields().put("AverageVehicleSpeed",(Double) trafficFlowObserved.getAverageVehicleSpeed());
			timeseriesPoint.getFields().put("AverageVehicleLength",(Double) trafficFlowObserved.getAverageVehicleLength());
			timeseriesPoint.getFields().put("Congested",(Boolean) trafficFlowObserved.getCongested());
			timeseriesPoint.getFields().put("AverageHeadwayTime",(Double) trafficFlowObserved.getAverageHeadwayTime());
			timeseriesPoint.getFields().put("AverageGapDistance",(Double) trafficFlowObserved.getAverageGapDistance());

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