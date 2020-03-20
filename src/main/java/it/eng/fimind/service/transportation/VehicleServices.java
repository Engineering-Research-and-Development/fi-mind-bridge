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
	public Response createDataInJSON(@Valid Vehicle vehicle) { 
		ServiceResult serviceResult=new ServiceResult();
		logger.debug("Id ="+vehicle.getId());

		if(!vehicleDoesAlreadyExist(vehicle)) 
			saveMindSphereAsset(createMindSphereAssetFromVehicle(vehicle));
		
		createMindSphereTimeSeriesFromVehicle(vehicle);
		
		serviceResult.setResult("OK");
		return Response.status(201).entity(serviceResult).build();
	}

	
	private Boolean vehicleDoesAlreadyExist(Vehicle vehicle)
	{
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+vehicle.getId()+"Asset\"}");
		return assets.size()>0;
	}
	
	public Asset createMindSphereAssetFromVehicle(Vehicle vehicle) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		MindSphereMapper mindSphereMapper = new MindSphereMapper();
		
		vehicle.setId(vehicle.getId().replaceAll("-","_"));

		List<String> keys = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		if(vehicle.getSource()!=null) {
			keys.add("Source");
			values.add(vehicle.getSource());
		}
		if(vehicle.getDataProvider()!=null) {
			keys.add("DataProvider");
			values.add(vehicle.getDataProvider());
		}
		if(vehicle.getName()!=null) {
			keys.add("Name");
			values.add(vehicle.getName());
		}
		if(vehicle.getVehicleType()!=null) {
			keys.add("VehicleType");
			values.add(vehicle.getVehicleType());
		}
		if(vehicle.getCategory()!=null) {
			keys.add("Category");
			values.add(vehicle.getCategory());
		}
		if(vehicle.getVehicleIdentificationNumber()!=null) {
			keys.add("VehicleIdentificationNumber");
			values.add(vehicle.getVehicleIdentificationNumber());
		}
		if(vehicle.getVehiclePlateIdentifier()!=null) {
			keys.add("VehiclePlateIdentifier");
			values.add(vehicle.getVehiclePlateIdentifier());
		}
		if(vehicle.getFleetVehicleId()!=null) {
			keys.add("FleetVehicleId");
			values.add(vehicle.getFleetVehicleId());
		}
		if(vehicle.getDateVehicleFirstRegistered()!=null) {
			keys.add("DateVehicleFirstRegistered");
			values.add(vehicle.getDateVehicleFirstRegistered());
		}
		if(vehicle.getDateFirstUsed()!=null) {
			keys.add("DateFirstUsed");
			values.add(vehicle.getDateFirstUsed());
		}
		if(vehicle.getPurchaseDate()!=null) {
			keys.add("PurchaseDate");
			values.add(vehicle.getPurchaseDate());
		}
		if(vehicle.getVehicleConfiguration()!=null) {
			keys.add("VehicleConfiguration");
			values.add(vehicle.getVehicleConfiguration());
		}
		if(vehicle.getColor()!=null) {
			keys.add("Color");
			values.add(vehicle.getColor());
		}
		if(vehicle.getOwner()!=null) {
			keys.add("Owner");
			values.add(vehicle.getOwner());
		}
		if(vehicle.getFeature()!=null) {
			keys.add("Feature");
			values.add(vehicle.getFeature().toString());
		}
		if(vehicle.getServiceProvided()!=null) {
			keys.add("ServiceProvided");
			values.add(vehicle.getServiceProvided().toString());
		}
		if(vehicle.getVehicleSpecialUsage()!=null) {
			keys.add("VehicleSpecialUsage");
			values.add(vehicle.getVehicleSpecialUsage());
		}
		if(vehicle.getRefVehicleModel()!=null) {
			keys.add("RefVehicleModel");
			values.add(vehicle.getRefVehicleModel());
		}
		if(vehicle.getAreaServed()!=null) {
			keys.add("AreaServed");
			values.add(vehicle.getAreaServed());
		}
		if(vehicle.getDateModified()!=null) {
			keys.add("DateModified");
			values.add(vehicle.getDateModified());
		}
		if(vehicle.getDateCreated()!=null) {
			keys.add("DateCreated");
			values.add(vehicle.getDateCreated());
		}
		List<VariableDefinition> assetVariablesDefinitions = mindSphereMapper.fiPropertiesToMiVariablesDefinitions(keys, values);
		List<Variable> assetVariables = mindSphereMapper.fiPropertiesToMiVariables(keys, values);


		List<String> properties = Stream.of("Location", "PreviousLocation", "Speed", "Heading", "MileageFromOdometer","ServiceStatus").collect(Collectors.toList());
		List<String> uoms = Stream.of("Coordinates","Coordinates","km/h", "°", "km","Dimensionless").collect(Collectors.toList());
		List<String> dataTypes = Stream.of("String","String","Double","Double","Double", "String").collect(Collectors.toList());
		AspectType aspectType = mindSphereMapper.fiStateToMiAspectType(vehicle.getId(), vehicle.getDescription(), properties, uoms, dataTypes);
		
		
		return mindSphereGateway.createAsset(vehicle.getId(), assetVariablesDefinitions, assetVariables, aspectType);
	}
	
	private Boolean saveMindSphereAsset(Asset asset) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		Boolean result = mindSphereGateway.saveAsset(asset);
		if(result)
			logger.debug("Vehicle created");
		else 		
			logger.error("Vehicle couldn't be created");
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
			if(vehicle.getLocation()!=null) {
				String prev_location = vehicle.getPreviousLocation().getCoordinates().get(0) + "," +  vehicle.getPreviousLocation().getCoordinates().get(1);
				timeseriesPoint.getFields().put("PreviousLocation",prev_location);
			}
			if(vehicle.getSpeed()!=null) {
				timeseriesPoint.getFields().put("Speed", vehicle.getSpeed());
			}
			if(vehicle.getHeading()!=null) {
				timeseriesPoint.getFields().put("Heading", vehicle.getHeading());
			}
			if(vehicle.getMileageFromOdometer()!=null) {
				timeseriesPoint.getFields().put("MileageFromOdometer", vehicle.getMileageFromOdometer());
			}
			if(vehicle.getServiceStatus()!=null) {
				timeseriesPoint.getFields().put("ServiceStatus", vehicle.getServiceStatus());
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