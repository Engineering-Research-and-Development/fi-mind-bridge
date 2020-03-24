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

import it.eng.fimind.model.fiware.transportation.VehicleNormalized;
import it.eng.fimind.util.MindSphereGateway;
import it.eng.fimind.util.MindSphereMapper;
import it.eng.fimind.util.ServiceResult;

/**
 * Root resource (exposed at "vehiclenormalized" path)
 */
@Path("vehicleNormalized")
public class VehicleNormalizedServices {
	private static Logger logger = Logger.getLogger(VehicleNormalizedServices.class);
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getIt() {
		return "VehicleNormalized Service: got it!!";
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createDataInJSON(@HeaderParam("debug-mode") String debugMode, @Valid VehicleNormalized vehicle) { 
		ServiceResult serviceResult = new ServiceResult();
		logger.debug("Id ="+vehicle.getId());
		
		if(debugMode!=null && debugMode.equals("true")){
			System.out.println("DEBUG MODE FOR --- VehicleNormalized ---");
			createMindSphereAssetFromVehicle(vehicle, true);
			serviceResult.setResult("Test gone fine");
			return Response.status(200).entity(serviceResult).build();
		}else {
			Boolean result = false;
			if(!vehicleDoesAlreadyExist(vehicle)) 
				result = createMindSphereAssetFromVehicle(vehicle, false);
			
			result = createMindSphereTimeSeriesFromVehicle(vehicle);
			
			if(result) {
				serviceResult.setResult("VehicleNormalized added succesfully");
				return Response.status(201).entity(serviceResult).build();
			}
			else {
				serviceResult.setResult("Something went wrong, check your FI-MIND logs");
				return Response.status(500).entity(serviceResult).build();
			}
		}
	}

	
	private Boolean vehicleDoesAlreadyExist(VehicleNormalized vehicle)
	{
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+vehicle.getId()+"Asset\"}");
		return assets.size()>0;
	}
	
	private Boolean createMindSphereAssetFromVehicle(VehicleNormalized vehicle, Boolean isDebugMode) {
		Boolean result = false;
		
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		MindSphereMapper mindSphereMapper = new MindSphereMapper();
		
		List<String> keys = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		List<String> varDefDataTypes = new ArrayList<String>();

		if(vehicle.getSource()!=null) {
			keys.add("Source");
			values.add((String) vehicle.getSource().getValue());
			varDefDataTypes.add("String");
		}
		if(vehicle.getDataProvider()!=null) {
			keys.add("DataProvider");
			values.add((String) vehicle.getDataProvider().getValue());
			varDefDataTypes.add("String");
		}
		if(vehicle.getName()!=null) {
			keys.add("VehicleName");
			values.add((String) vehicle.getName().getValue());
			varDefDataTypes.add("String");
		}
		if(vehicle.getVehicleType()!=null) {
			keys.add("VehicleType");
			values.add((String) vehicle.getVehicleType().getValue());
			varDefDataTypes.add("String");
		}
		if(vehicle.getCategory()!=null) {
			keys.add("Category");
			values.add((String) vehicle.getCategory().getValue().toString());
			varDefDataTypes.add("String");
		}
		if(vehicle.getVehicleIdentificationNumber()!=null) {
			keys.add("VehicleIdentificationNumber");
			values.add((String) vehicle.getVehicleIdentificationNumber().getValue());
			varDefDataTypes.add("String");
		}
		if(vehicle.getVehiclePlateIdentifier()!=null) {
			keys.add("VehiclePlateIdentifier");
			values.add((String) vehicle.getVehiclePlateIdentifier().getValue());
			varDefDataTypes.add("String");
		}
		if(vehicle.getFleetVehicleId()!=null) {
			keys.add("FleetVehicleId");
			values.add((String) vehicle.getFleetVehicleId().getValue());
			varDefDataTypes.add("String");
		}
		if(vehicle.getDateVehicleFirstRegistered()!=null) {
			keys.add("DateVehicleFirstRegistered");
			values.add((String) vehicle.getDateVehicleFirstRegistered().getValue());
			varDefDataTypes.add("String");
		}
		if(vehicle.getDateFirstUsed()!=null) {
			keys.add("DateFirstUsed");
			values.add((String) vehicle.getDateFirstUsed().getValue());
			varDefDataTypes.add("Timestamp");
		}
		if(vehicle.getPurchaseDate()!=null) {
			keys.add("PurchaseDate");
			values.add((String) vehicle.getPurchaseDate().getValue());
			varDefDataTypes.add("String");
		}
		if(vehicle.getVehicleConfiguration()!=null) {
			keys.add("VehicleConfiguration");
			values.add((String) vehicle.getVehicleConfiguration().getValue());
			varDefDataTypes.add("String");
		}
		if(vehicle.getColor()!=null) {
			keys.add("Color");
			values.add((String) vehicle.getColor().getValue());
			varDefDataTypes.add("String");
		}
		if(vehicle.getOwner()!=null) {
			keys.add("Owner");
			values.add((String) vehicle.getOwner().getValue());
			varDefDataTypes.add("String");
		}
		if(vehicle.getFeature()!=null) {
			keys.add("Feature");
			values.add((String) vehicle.getFeature().getValue().toString());
			varDefDataTypes.add("String");
		}
		if(vehicle.getServiceProvided()!=null) {
			keys.add("ServiceProvided");
			values.add((String) vehicle.getServiceProvided().getValue().toString());
			varDefDataTypes.add("String");
		}
		if(vehicle.getVehicleSpecialUsage()!=null) {
			keys.add("VehicleSpecialUsage");
			values.add((String) vehicle.getVehicleSpecialUsage().getValue());
			varDefDataTypes.add("String");
		}
		if(vehicle.getRefVehicleModel()!=null) {
			keys.add("RefVehicleModel");
			values.add((String) vehicle.getRefVehicleModel().getValue());
			varDefDataTypes.add("String");
		}
		if(vehicle.getAreaServed()!=null) {
			keys.add("AreaServed");
			values.add((String) vehicle.getAreaServed().getValue());
			varDefDataTypes.add("String");
		}
		if(vehicle.getDateCreated()!=null) {
			keys.add("DateCreated");
			values.add((String) vehicle.getDateCreated().getValue());
			varDefDataTypes.add("Timestamp");
		}
		List<VariableDefinition> assetVariablesDefinitions = mindSphereMapper.fiPropertiesToMiVariablesDefinitions(keys, values, varDefDataTypes);
		List<Variable> assetVariables = mindSphereMapper.fiPropertiesToMiVariables(keys, values, varDefDataTypes);


		List<String> properties = Stream.of("Location", "PreviousLocation", "Speed", "Heading", "MileageFromOdometer","ServiceStatus", "DateModfied").collect(Collectors.toList());
		List<String> uoms = Stream.of("Coordinates","Coordinates","km/h", "°", "km","Dimensionless", "t").collect(Collectors.toList());
		List<String> dataTypes = Stream.of("String","String","Double","Double","Double", "String", "Timestamp").collect(Collectors.toList());
		AspectType aspectType = mindSphereMapper.fiStateToMiAspectType(vehicle.getId(), (String) vehicle.getDescription().getValue(), properties, uoms, dataTypes);
		
		
		if(isDebugMode) {
			System.out.println(mindSphereGateway.createAsset(vehicle.getId(), assetVariablesDefinitions, assetVariables, aspectType));
			result = true;
		}else {
			result = mindSphereGateway.saveAsset(vehicle.getId(), assetVariablesDefinitions, assetVariables, aspectType);
			if(result)
				logger.debug("VehicleNormalized created");
			else 		
				logger.error("VehicleNormalized couldn't be created");
		}
		return result;
	}
	
	private boolean createMindSphereTimeSeriesFromVehicle(VehicleNormalized vehicle) {
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
				String curr_location = vehicle.getLocation().getValue().getCoordinates().get(0) + "," +  vehicle.getLocation().getValue().getCoordinates().get(1);
				timeseriesPoint.getFields().put("Location",curr_location);
			}
			if(vehicle.getLocation()!=null) {
				String prev_location = vehicle.getPreviousLocation().getValue().getCoordinates().get(0) + "," +  vehicle.getPreviousLocation().getValue().getCoordinates().get(1);
				timeseriesPoint.getFields().put("PreviousLocation",prev_location);
			}
			if(vehicle.getSpeed()!=null) {
				timeseriesPoint.getFields().put("Speed",(Double) vehicle.getSpeed().getValue());
			}
			if(vehicle.getHeading()!=null) {
				timeseriesPoint.getFields().put("Heading",(Double) vehicle.getHeading().getValue());
			}
			if(vehicle.getMileageFromOdometer()!=null) {
				timeseriesPoint.getFields().put("MileageFromOdometer",(Double) vehicle.getMileageFromOdometer().getValue());
			}
			if(vehicle.getServiceStatus()!=null) {
				timeseriesPoint.getFields().put("ServiceStatus",(String) vehicle.getServiceStatus().getValue());
			}
			if(vehicle.getDateModified()!=null) {
				timeseriesPoint.getFields().put("DateModified", vehicle.getDateModified());
			}
			
			timeSeriesList.add(timeseriesPoint);
			mindSphereGateway.putTimeSeries(assets.get(0).getAssetId(), vehicle.getId()+"AspectType", timeSeriesList);
			logger.debug("VehicleNormalized updated");

		} catch (Exception e) {
			// Exception handling
			e.printStackTrace();
			return false;
		}
		return true;
	}	
}
