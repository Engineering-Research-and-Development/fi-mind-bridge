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

import it.eng.fimind.model.fiware.transportation.TrafficFlowObservedNormalized;
import it.eng.fimind.util.MindSphereGateway;
import it.eng.fimind.util.MindSphereMapper;
import it.eng.fimind.util.ServiceResult;

/**
 * Root resource (exposed at "trafficflowobservednormalized" path)
 */
@Path("trafficFlowObservedNormalized")
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
	public Response createDataInJSON(@HeaderParam("debug-mode") String debugMode, @Valid TrafficFlowObservedNormalized trafficFlowObserved) { 
		ServiceResult serviceResult = new ServiceResult();
		logger.debug("Id ="+trafficFlowObserved.getId());
		
		if(debugMode!=null && debugMode.equals("true")){
			logger.debug("DEBUG MODE FOR --- TrafficFlowObservedNormalized ---");
			createMindSphereAssetFromTrafficFlowObserved(trafficFlowObserved, true);
			serviceResult.setResult("Test gone fine");
			return Response.status(200).entity(serviceResult).build();
		}else {
			Boolean result = true;
			if(!trafficFlowObservedDoesAlreadyExist(trafficFlowObserved)) 
				result = createMindSphereAssetFromTrafficFlowObserved(trafficFlowObserved, false);
			
			if(result)
				result = createMindSphereTimeSeriesFromTrafficFlowObserved(trafficFlowObserved);
			
			if(result) {
				serviceResult.setResult("TrafficFlowObservedNormalized added succesfully");
				return Response.status(201).entity(serviceResult).build();
			}
			else {
				serviceResult.setResult("Something went wrong, check your FI-MIND logs");
				return Response.status(500).entity(serviceResult).build();
			}
		}
	}

	
	private Boolean trafficFlowObservedDoesAlreadyExist(TrafficFlowObservedNormalized trafficFlowObserved)
	{
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+trafficFlowObserved.getId()+"\"}");
		return assets.size()>0;
	}
	
	private Boolean createMindSphereAssetFromTrafficFlowObserved(TrafficFlowObservedNormalized trafficFlowObserved, Boolean isDebugMode) {
		Boolean result = false;
		
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		MindSphereMapper mindSphereMapper = new MindSphereMapper();
		
		Location mindSphereLocation = null;
		if(trafficFlowObserved.getLocation()!=null && trafficFlowObserved.getAddress()!=null)
			mindSphereLocation = mindSphereMapper.fiLocAddrToMiLocation(trafficFlowObserved.getLocation().getValue(), trafficFlowObserved.getAddress().getValue());
		else if(trafficFlowObserved.getLocation()!=null)
			mindSphereLocation = mindSphereMapper.fiLocationToMiLocation(trafficFlowObserved.getLocation().getValue());
		else if(trafficFlowObserved.getAddress()!=null) 
			mindSphereLocation = mindSphereMapper.fiAddressToMiLocation(trafficFlowObserved.getAddress().getValue());
		
		
		List<String> keys = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		List<String> varDefDataTypes = new ArrayList<String>();

		if(trafficFlowObserved.getType()!=null) {
			keys.add("entityType");
			values.add(trafficFlowObserved.getType());
			varDefDataTypes.add("String");
		}
		if(trafficFlowObserved.getSource()!=null) {
			keys.add("source");
			values.add((String) trafficFlowObserved.getSource().getValue());
			varDefDataTypes.add("String");
		}
		if(trafficFlowObserved.getVehicleType()!=null) {
			keys.add("vehicleType");		
			values.add((String) trafficFlowObserved.getVehicleType().getValue());
			varDefDataTypes.add("String");
		}
		if(trafficFlowObserved.getVehicleSubType()!=null) {
			keys.add("vehicleSubType");
			values.add((String) trafficFlowObserved.getVehicleSubType().getValue());
			varDefDataTypes.add("String");
		}
		if(trafficFlowObserved.getDataProvider()!=null) {
			keys.add("dataProvider");
			values.add((String) trafficFlowObserved.getDataProvider().getValue());
			varDefDataTypes.add("String");
		}
		if(trafficFlowObserved.getRefRoadSegment()!=null) {
			keys.add("refRoadSegment");		
			values.add((String) trafficFlowObserved.getRefRoadSegment().getValue());
			varDefDataTypes.add("String");
		}
		if(trafficFlowObserved.getLaneId()!=null) {
			keys.add("laneId");
			values.add((String) trafficFlowObserved.getLaneId().getValue().toString());
			varDefDataTypes.add("Integer");
		}
		if(trafficFlowObserved.getLaneDirection()!=null) {
			keys.add("laneDirection");
			values.add((String) trafficFlowObserved.getLaneDirection().getValue());
			varDefDataTypes.add("String");
		}
		if(trafficFlowObserved.getDateCreated()!=null) {
			keys.add("dateCreated");
			values.add((String) trafficFlowObserved.getDateCreated().getValue());
			varDefDataTypes.add("Timestamp");
		}
		if(trafficFlowObserved.getName()!=null) {
			keys.add("entityName");		
			values.add((String) trafficFlowObserved.getName().getValue());
			varDefDataTypes.add("String");
		}
		List<VariableDefinition> assetVariablesDefinitions = mindSphereMapper.fiPropertiesToMiVariablesDefinitions(keys, values, varDefDataTypes);
		List<Variable> assetVariables = mindSphereMapper.fiPropertiesToMiVariables(keys, values, varDefDataTypes);


		List<String> properties = Stream.of("dateModified", "dateObserved", "dateObservedFrom", "dateObservedTo", "intensity","occupancy", "averageVehicleSpeed", "averageVehicleLength", "congested", "averageHeadwayTime", "averageGapDistance", "reversedLane").collect(Collectors.toList());
		List<String> uoms = Stream.of("t", "t", "t", "t", "Dimensionless", "Dimensionless", "km/h", "m", "Dimensionless", "s", "m", "Dimensionless").collect(Collectors.toList());
		List<String> dataTypes = Stream.of("Timestamp", "String", "Timestamp", "Timestamp", "Integer", "Double", "Double", "Double", "Boolean", "Double", "Double", "Boolean").collect(Collectors.toList());
		AspectType aspectType;
		if(trafficFlowObserved.getDescription()!=null)
			aspectType = mindSphereMapper.fiStateToMiAspectType(trafficFlowObserved.getId(), (String) trafficFlowObserved.getDescription().getValue(), properties, uoms, dataTypes);
		else
			aspectType = mindSphereMapper.fiStateToMiAspectType(trafficFlowObserved.getId(), properties, uoms, dataTypes);
				
		
		if(isDebugMode) {
			logger.debug(mindSphereGateway.createAsset(trafficFlowObserved.getId(), mindSphereLocation, assetVariablesDefinitions, assetVariables, aspectType));
			result = true;
		}else {
			result = mindSphereGateway.saveAsset(trafficFlowObserved.getId(), mindSphereLocation, assetVariablesDefinitions, assetVariables, aspectType);
			if(result)
				logger.debug("TrafficFlowObservedNormalized created");
			else 		
				logger.error("TrafficFlowObservedNormalized couldn't be created");
		}
		return result;
	}

	private boolean createMindSphereTimeSeriesFromTrafficFlowObserved(TrafficFlowObservedNormalized trafficFlowObserved) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+trafficFlowObserved.getId()+"\"}");
		try {
			List<Timeseries> timeSeriesList = new ArrayList<Timeseries>();
			Date now = new Date();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			String instant = df.format(now);
			Timeseries timeseriesPoint=new Timeseries();
			timeseriesPoint.getFields().put("_time", instant);
		
			if(trafficFlowObserved.getDateModified()!=null) {
				timeseriesPoint.getFields().put("dateModified",(String) trafficFlowObserved.getDateModified().getValue());
			}
			if(trafficFlowObserved.getDateObserved()!=null) {
				timeseriesPoint.getFields().put("dateObserved",(String) trafficFlowObserved.getDateObserved().getValue());
			}
			if(trafficFlowObserved.getDateObservedFrom()!=null) {	
				timeseriesPoint.getFields().put("dateObservedFrom",(String) trafficFlowObserved.getDateObservedFrom().getValue());
			}
			if(trafficFlowObserved.getDateObservedTo()!=null) {
				timeseriesPoint.getFields().put("dateObservedTo",(String) trafficFlowObserved.getDateObservedTo().getValue());
			}
			if(trafficFlowObserved.getIntensity()!=null) {
				timeseriesPoint.getFields().put("intensity",(Integer) trafficFlowObserved.getIntensity().getValue());
			}
			if(trafficFlowObserved.getOccupancy()!=null) {
				timeseriesPoint.getFields().put("occupancy",(Double) trafficFlowObserved.getOccupancy().getValue());
			}
			if(trafficFlowObserved.getAverageVehicleSpeed()!=null) {
				timeseriesPoint.getFields().put("averageVehicleSpeed",(Double) trafficFlowObserved.getAverageVehicleSpeed().getValue());
			}
			if(trafficFlowObserved.getAverageVehicleLength()!=null) {
				timeseriesPoint.getFields().put("averageVehicleLength",(Double) trafficFlowObserved.getAverageVehicleLength().getValue());
			}
			if(trafficFlowObserved.getCongested()!=null) {
				timeseriesPoint.getFields().put("congested",(Boolean) trafficFlowObserved.getCongested().getValue());
			}
			if(trafficFlowObserved.getAverageHeadwayTime()!=null) {
				timeseriesPoint.getFields().put("averageHeadwayTime",(Double) trafficFlowObserved.getAverageHeadwayTime().getValue());
			}
			if(trafficFlowObserved.getAverageGapDistance()!=null) {
				timeseriesPoint.getFields().put("averageGapDistance",(Double) trafficFlowObserved.getAverageGapDistance().getValue());
			}
			if(trafficFlowObserved.getReversedLane()!=null) {
				timeseriesPoint.getFields().put("reversedLane",(Boolean) trafficFlowObserved.getReversedLane().getValue());
			}

			timeSeriesList.add(timeseriesPoint);
			mindSphereGateway.putTimeSeries(assets.get(0).getAssetId(), trafficFlowObserved.getId(), timeSeriesList);
			logger.debug("TrafficFlowObservedNormalized updated");

		} catch (Exception e) {
			// Exception handling
			e.printStackTrace();
			return false;
		}
		return true;
	}	
}