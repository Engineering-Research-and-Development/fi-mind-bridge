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
import com.siemens.mindsphere.sdk.assetmanagement.model.Variable;
import com.siemens.mindsphere.sdk.assetmanagement.model.VariableDefinition;
import com.siemens.mindsphere.sdk.timeseries.model.Timeseries;

import it.eng.fimind.model.fiware.transportation.Vehicle;
import it.eng.fimind.util.MindSphereGateway;
import it.eng.fimind.util.MindSphereMapper;
import it.eng.fimind.util.ServiceResult;

/**
 * Root resource (exposed at "vehicle" path)
 */
@Path("vehicle")
public class VehicleServices {
	private static Logger logger = Logger.getLogger(VehicleServices.class);
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getIt() {
		return "Vehicle Service: got it!!";
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createDataInJSON(@HeaderParam("debug-mode") String debugMode, @Valid Vehicle vehicle) { 
		ServiceResult serviceResult = new ServiceResult();
		logger.debug("Id ="+vehicle.getId());
		
		if(debugMode!=null && debugMode.equals("true")){
			System.out.println("DEBUG MODE FOR --- Vehicle ---");
			createMindSphereAssetFromVehicle(vehicle, true);
			serviceResult.setResult("Test gone fine");
			return Response.status(200).entity(serviceResult).build();
		}else {
			Boolean result = false;
			if(!vehicleDoesAlreadyExist(vehicle)) 
				result = createMindSphereAssetFromVehicle(vehicle, false);
			
			if(result)
				result = createMindSphereTimeSeriesFromVehicle(vehicle);
			
			if(result) {
				serviceResult.setResult("Vehicle added succesfully");
				return Response.status(201).entity(serviceResult).build();
			}
			else {
				serviceResult.setResult("Something went wrong, check your FI-MIND logs");
				return Response.status(500).entity(serviceResult).build();
			}
		}
	}

	
	private Boolean vehicleDoesAlreadyExist(Vehicle vehicle)
	{
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+vehicle.getId()+"Asset\"}");
		return assets.size()>0;
	}
	
	public Boolean createMindSphereAssetFromVehicle(Vehicle vehicle, Boolean isDebugMode) {
		Boolean result = false;
		
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		MindSphereMapper mindSphereMapper = new MindSphereMapper();
		
		List<String> keys = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		List<String> varDefDataTypes = new ArrayList<String>();
		
		if(vehicle.getType()!=null) {
			keys.add("entityType");
			values.add(vehicle.getType());
			varDefDataTypes.add("String");
		}
		if(vehicle.getSource()!=null) {
			keys.add("source");
			values.add(vehicle.getSource());
			varDefDataTypes.add("String");
		}
		if(vehicle.getDataProvider()!=null) {
			keys.add("dataProvider");
			values.add(vehicle.getDataProvider());
			varDefDataTypes.add("String");
		}
		if(vehicle.getName()!=null) {
			keys.add("entityName"); //"Name" attribute seems to be reserved!!
			values.add(vehicle.getName());
			varDefDataTypes.add("String");
		}
		if(vehicle.getVehicleType()!=null) {
			keys.add("vehicleType");
			values.add(vehicle.getVehicleType());
			varDefDataTypes.add("String");
		}
		if(vehicle.getCategory()!=null) {
			keys.add("category");
			values.add(vehicle.getCategory().toString());
			varDefDataTypes.add("String");
		}
		if(vehicle.getVehicleIdentificationNumber()!=null) {
			keys.add("vehicleIdentificationNumber");
			values.add(vehicle.getVehicleIdentificationNumber());
			varDefDataTypes.add("String");
		}
		if(vehicle.getVehiclePlateIdentifier()!=null) {
			keys.add("vehiclePlateIdentifier");
			values.add(vehicle.getVehiclePlateIdentifier());
			varDefDataTypes.add("String");
		}
		if(vehicle.getFleetVehicleId()!=null) {
			keys.add("fleetVehicleId");
			values.add(vehicle.getFleetVehicleId());
			varDefDataTypes.add("String");
		}
		if(vehicle.getDateVehicleFirstRegistered()!=null) {
			keys.add("dateVehicleFirstRegistered");
			values.add(vehicle.getDateVehicleFirstRegistered());
			varDefDataTypes.add("String");
		}
		if(vehicle.getDateFirstUsed()!=null) {
			keys.add("dateFirstUsed");
			values.add(vehicle.getDateFirstUsed());
			varDefDataTypes.add("Timestamp");
		}
		if(vehicle.getPurchaseDate()!=null) {
			keys.add("purchaseDate");
			values.add(vehicle.getPurchaseDate());
			varDefDataTypes.add("String");
		}
		if(vehicle.getVehicleConfiguration()!=null) {
			keys.add("vehicleConfiguration");
			values.add(vehicle.getVehicleConfiguration());
			varDefDataTypes.add("String");
		}
		if(vehicle.getColor()!=null) {
			keys.add("color");
			values.add(vehicle.getColor());
			varDefDataTypes.add("String");
		}
		if(vehicle.getOwner()!=null) {
			keys.add("owner");
			values.add(vehicle.getOwner());
			varDefDataTypes.add("String");
		}
		if(vehicle.getFeature()!=null) {
			keys.add("feature");
			values.add(vehicle.getFeature().toString());
			varDefDataTypes.add("String");
		}
		if(vehicle.getServiceProvided()!=null) {
			keys.add("serviceProvided");
			values.add(vehicle.getServiceProvided().toString());
			varDefDataTypes.add("String");
		}
		if(vehicle.getVehicleSpecialUsage()!=null) {
			keys.add("vehicleSpecialUsage");
			values.add(vehicle.getVehicleSpecialUsage());
			varDefDataTypes.add("String");
		}
		if(vehicle.getRefVehicleModel()!=null) {
			keys.add("refVehicleModel");
			values.add(vehicle.getRefVehicleModel());
			varDefDataTypes.add("String");
		}
		if(vehicle.getAreaServed()!=null) {
			keys.add("areaServed");
			values.add(vehicle.getAreaServed());
			varDefDataTypes.add("String");
		}
		if(vehicle.getDateCreated()!=null) {
			keys.add("dateCreated");
			values.add(vehicle.getDateCreated());
			varDefDataTypes.add("Timestamp");
		}
		List<VariableDefinition> assetVariablesDefinitions = mindSphereMapper.fiPropertiesToMiVariablesDefinitions(keys, values, varDefDataTypes);
		List<Variable> assetVariables = mindSphereMapper.fiPropertiesToMiVariables(keys, values, varDefDataTypes);


		List<String> properties = Stream.of("location", "previousLocation", "speed", "heading", "mileageFromOdometer","serviceStatus", "dateModfied").collect(Collectors.toList());
		List<String> uoms = Stream.of("Coordinates","Coordinates","km/h", "°", "km","Dimensionless", "t").collect(Collectors.toList());
		List<String> dataTypes = Stream.of("String","String","Double","Double","Double", "String", "Timestamp").collect(Collectors.toList());
		AspectType aspectType = mindSphereMapper.fiStateToMiAspectType(vehicle.getId(), vehicle.getDescription(), properties, uoms, dataTypes);
		
		
		if(isDebugMode) {
			System.out.println(mindSphereGateway.createAsset(vehicle.getId(), assetVariablesDefinitions, assetVariables, aspectType));
			result = true;
		}else {
			result = mindSphereGateway.saveAsset(vehicle.getId(), assetVariablesDefinitions, assetVariables, aspectType);
			if(result)
				logger.debug("Vehicle created");
			else 		
				logger.error("Vehicle couldn't be created");
		}
		return result;
	}
	
	public boolean createMindSphereTimeSeriesFromVehicle(Vehicle vehicle) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+vehicle.getId()+"Asset\"}");
		try {
			List<Timeseries> timeSeriesList = new ArrayList<Timeseries>();
			Date now = new Date();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			String instant = df.format(now);
			Timeseries timeseriesPoint=new Timeseries();
			timeseriesPoint.getFields().put("_time", instant);
		
			if(vehicle.getLocation()!=null) {
				String curr_location = vehicle.getLocation().getCoordinates().get(0) + "," +  vehicle.getLocation().getCoordinates().get(1);
				timeseriesPoint.getFields().put("Location",curr_location);
			}
			if(vehicle.getPreviousLocation()!=null) {
				String prev_location = vehicle.getPreviousLocation().getCoordinates().get(0) + "," +  vehicle.getPreviousLocation().getCoordinates().get(1);
				timeseriesPoint.getFields().put("PreviousLocation",prev_location);
			}
			if(vehicle.getSpeed()!=null) {
				timeseriesPoint.getFields().put("speed", vehicle.getSpeed());
			}
			if(vehicle.getHeading()!=null) {
				timeseriesPoint.getFields().put("heading", vehicle.getHeading());
			}
			if(vehicle.getMileageFromOdometer()!=null) {
				timeseriesPoint.getFields().put("mileageFromOdometer", vehicle.getMileageFromOdometer());
			}
			if(vehicle.getServiceStatus()!=null) {
				timeseriesPoint.getFields().put("serviceStatus", vehicle.getServiceStatus());
			}
			if(vehicle.getDateModified()!=null) {
				timeseriesPoint.getFields().put("dateModified", vehicle.getDateModified());
			}
			
			timeSeriesList.add(timeseriesPoint);
			mindSphereGateway.putTimeSeries(assets.get(0).getAssetId(), vehicle.getId()+"AspectType", timeSeriesList);
			logger.debug("Vehicle updated");

		} catch (Exception e) {
			// Exception handling
			e.printStackTrace();
			return false;
		}
		return true;
	}	
}