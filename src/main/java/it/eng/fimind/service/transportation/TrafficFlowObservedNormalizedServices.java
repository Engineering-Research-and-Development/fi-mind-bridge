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

import it.eng.fimind.model.fiware.transportation.TrafficFlowObservedNormalized;
import it.eng.fimind.util.MindSphereGateway;
import it.eng.fimind.util.MindSphereMapper;
import it.eng.fimind.util.ServiceResult;

/**
 * Root resource (exposed at "trafficflowobservednormalized" path)
 */
@Path("trafficflowobservednormalized")
public class TrafficFlowObservedNormalizedServices {
	private static Logger logger = Logger.getLogger(TrafficFlowObservedNormalizedServices.class);
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getIt() {
		return "TrafficFlowObservedNormalized Service: got it!!";
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createDataInJSON(@Valid TrafficFlowObservedNormalized trafficFlowObserved) { 
		ServiceResult serviceResult=new ServiceResult();
		logger.debug("Id ="+trafficFlowObserved.getId());
		
		if(!trafficFlowObservedDoesAlreadyExist(trafficFlowObserved)) 
			saveMindSphereAsset(createMindSphereAssetFromTrafficFlowObserved(trafficFlowObserved));
		
		createMindSphereTimeSeriesFromTrafficFlowObserved(trafficFlowObserved);
		
		serviceResult.setResult("OK");
		return Response.status(201).entity(serviceResult).build();
	}

	
	private Boolean trafficFlowObservedDoesAlreadyExist(TrafficFlowObservedNormalized trafficFlowObserved)
	{
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+trafficFlowObserved.getId()+"Asset\"}");
		return assets.size()>0;
	}
	
	private Asset createMindSphereAssetFromTrafficFlowObserved(TrafficFlowObservedNormalized trafficFlowObserved) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
MindSphereMapper mindSphereMapper = new MindSphereMapper();
		
		Location mindSphereLocation = null;
		if(trafficFlowObserved.getLocation().getType().equals("Point")) 
			mindSphereLocation = mindSphereMapper.fiLocationToMiLocation(trafficFlowObserved.getLocation().getValue());
		else 
			mindSphereLocation = mindSphereMapper.fiAddressToMiLocation(trafficFlowObserved.getAddress().getValue());
		
		
		List<String> keys = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		keys.add("Source");
		values.add((String) trafficFlowObserved.getSource().getValue());
		keys.add("VehicleType");
		values.add((String) trafficFlowObserved.getVehicleType().getValue());
		keys.add("VehicleSubType");
		values.add((String) trafficFlowObserved.getVehicleSubType().getValue());
		keys.add("DataProvider");
		values.add((String) trafficFlowObserved.getDataProvider().getValue());
		keys.add("RefRoadSegment");
		values.add((String) trafficFlowObserved.getRefRoadSegment().getValue());
		keys.add("DateModified");
		values.add((String) trafficFlowObserved.getDateModified().getValue());
		keys.add("LaneId");
		values.add((String) trafficFlowObserved.getLaneId().getValue().toString());
		keys.add("LaneDirection");
		values.add((String) trafficFlowObserved.getLaneDirection().getValue());
		keys.add("ReversedLane");
		values.add((String) trafficFlowObserved.getReversedLane().getValue());
		keys.add("DateObserved");
		values.add((String) trafficFlowObserved.getDateObserved().getValue());
		keys.add("DateObservedFrom");
		values.add((String) trafficFlowObserved.getDateObservedFrom().getValue());
		keys.add("DateObservedTo");
		values.add((String) trafficFlowObserved.getDateObservedTo().getValue());
		keys.add("DateCreated");
		values.add((String) trafficFlowObserved.getDateCreated().getValue());
		keys.add("Name");
		values.add((String) trafficFlowObserved.getName().getValue());
		List<Variable> assetVariables = mindSphereMapper.fiPropertiesToMiVariables(keys, values);
		

		List<String> properties = Stream.of("Intensity","Occupancy", "AverageVehicleSpeed", "AverageVehicleLength", "Congested", "AverageHeadwayTime", "AverageGapDistance").collect(Collectors.toList());
		List<String> uoms = Stream.of("Dimensionless", "Dimensionless", "km/h", "m", "Dimensionless", "s", "m").collect(Collectors.toList());
		AspectType aspectType = mindSphereMapper.fiStateToMiAspectType(trafficFlowObserved.getId(), (String) trafficFlowObserved.getDescription().getValue(), properties, uoms);
		
		
		return mindSphereGateway.createAsset(trafficFlowObserved.getId(), mindSphereLocation, assetVariables, aspectType);
	}
	
	private boolean saveMindSphereAsset(Asset asset) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		logger.debug("TrafficFlowObservedNormalized created");
		return mindSphereGateway.saveAsset(asset);
	}
	
	private boolean createMindSphereTimeSeriesFromTrafficFlowObserved(TrafficFlowObservedNormalized trafficFlowObserved) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+trafficFlowObserved.getId()+"Asset\"}");
		try {
			List<Timeseries> timeSeriesList = new ArrayList<Timeseries>();
			Date now = new Date();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			String instant = df.format(now);
			Timeseries timeseriesPoint=new Timeseries();
			timeseriesPoint.getFields().put("_time", instant);
		
			timeseriesPoint.getFields().put("Intensity",(Integer) trafficFlowObserved.getIntensity().getValue());
			timeseriesPoint.getFields().put("Occupancy",(Integer) trafficFlowObserved.getOccupancy().getValue());
			timeseriesPoint.getFields().put("AverageVehicleSpeed",(Double) trafficFlowObserved.getAverageVehicleSpeed().getValue());
			timeseriesPoint.getFields().put("AverageVehicleLength",(Double) trafficFlowObserved.getAverageVehicleLength().getValue());
			timeseriesPoint.getFields().put("Congested",(Boolean) trafficFlowObserved.getCongested().getValue());
			timeseriesPoint.getFields().put("AverageHeadwayTime",(Double) trafficFlowObserved.getAverageHeadwayTime().getValue());
			timeseriesPoint.getFields().put("AverageGapDistance",(Double) trafficFlowObserved.getAverageGapDistance().getValue());
			timeseriesPoint.getFields().put("ReversedLane",(Boolean) trafficFlowObserved.getReversedLane().getValue());

			timeSeriesList.add(timeseriesPoint);
			mindSphereGateway.putTimeSeries(assets.get(0).getAssetId(), trafficFlowObserved.getId()+"AspectType", timeSeriesList);
			logger.debug("TrafficFlowObservedNormalized updated");

		} catch (Exception e) {
			// Exception handling
			e.printStackTrace();
			return false;
		}
		return true;
	}	
}