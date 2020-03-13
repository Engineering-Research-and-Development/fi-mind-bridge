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
	
	private Asset createMindSphereAssetFromVehicle(Vehicle vehicle) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		MindSphereMapper mindSphereMapper = new MindSphereMapper();
		
		
		List<String> keys = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		keys.add("Source");
		values.add(vehicle.getSource());
		keys.add("DataProvider");
		values.add(vehicle.getDataProvider());
		keys.add("Name");
		values.add(vehicle.getName());
		keys.add("VehicleType");
		values.add(vehicle.getVehicleType());
		keys.add("Category");
		values.add(vehicle.getCategory());
		keys.add("VehicleIdentificationNumber");
		values.add(vehicle.getVehicleIdentificationNumber());
		keys.add("VehiclePlateIdentifier");
		values.add(vehicle.getVehiclePlateIdentifier());
		keys.add("FleetVehicleId");
		values.add(vehicle.getFleetVehicleId());
		keys.add("DateVehicleFirstRegistered");
		values.add(vehicle.getDateVehicleFirstRegistered());
		keys.add("DateFirstUsed");
		values.add(vehicle.getDateFirstUsed());
		keys.add("PurchaseDate");
		values.add(vehicle.getPurchaseDate());
		keys.add("VehicleConfiguration");
		values.add(vehicle.getVehicleConfiguration());
		keys.add("Color");
		values.add(vehicle.getColor());
		keys.add("Owner");
		values.add(vehicle.getOwner());
		keys.add("Feature");
		values.add(vehicle.getFeature().toString());
		keys.add("ServiceProvided");
		values.add(vehicle.getServiceProvided().toString());
		keys.add("VehicleSpecialUsage");
		values.add(vehicle.getVehicleSpecialUsage());
		keys.add("RefVehicleModel");
		values.add(vehicle.getRefVehicleModel());
		keys.add("AreaServed");
		values.add(vehicle.getAreaServed());
		keys.add("DateModified");
		values.add(vehicle.getDateModified());
		keys.add("DateCreated");
		values.add(vehicle.getDateCreated());
		List<Variable> assetVariables = mindSphereMapper.fiPropertiesToMiVariables(keys, values);
		

		List<String> properties = Stream.of("Location", "PreviousLocation", "Speed", "Heading", "MileageFromOdometer","ServiceStatus").collect(Collectors.toList());
		List<String> uoms = Stream.of("Coordinates","Coordinates","km/h", "°", "km","Dimensionless").collect(Collectors.toList());
		AspectType aspectType = mindSphereMapper.fiStateToMiAspectType(vehicle.getId(), vehicle.getDescription(), properties, uoms);
		
		
		return mindSphereGateway.createAsset(vehicle.getId(), assetVariables, aspectType);
	}
	
	private boolean saveMindSphereAsset(Asset asset) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		logger.debug("Vehicle created");
		return mindSphereGateway.saveAsset(asset);
	}
	
	private boolean createMindSphereTimeSeriesFromVehicle(Vehicle vehicle) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+vehicle.getId()+"Asset\"}");
		try {
			List<Timeseries> timeSeriesList = new ArrayList<Timeseries>();
			Date now = new Date();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			String instant = df.format(now);
			Timeseries timeseriesPoint=new Timeseries();
			timeseriesPoint.getFields().put("_time", instant);
		
			String curr_location = vehicle.getLocation().getCoordinates().get(0) + "," +  vehicle.getLocation().getCoordinates().get(1);
			String prev_location = vehicle.getPreviousLocation().getCoordinates().get(0) + "," +  vehicle.getPreviousLocation().getCoordinates().get(1);

			timeseriesPoint.getFields().put("Location",curr_location);
			timeseriesPoint.getFields().put("PreviousLocation",prev_location);
			timeseriesPoint.getFields().put("Speed",vehicle.getSpeed());
			timeseriesPoint.getFields().put("Heading",vehicle.getHeading());
			timeseriesPoint.getFields().put("MileageFromOdometer",vehicle.getMileageFromOdometer());
			timeseriesPoint.getFields().put("ServiceStatus",vehicle.getServiceStatus());

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