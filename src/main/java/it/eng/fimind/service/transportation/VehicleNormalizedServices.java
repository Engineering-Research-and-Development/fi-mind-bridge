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

import it.eng.fimind.model.fiware.transportation.VehicleNormalized;
import it.eng.fimind.util.MindSphereGateway;
import it.eng.fimind.util.MindSphereMapper;
import it.eng.fimind.util.ServiceResult;

/**
 * Root resource (exposed at "vehiclenormalized" path)
 */
@Path("vehiclenormalized")
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
	public Response createDataInJSON(@Valid VehicleNormalized vehicle) { 
		ServiceResult serviceResult=new ServiceResult();
		logger.debug("Id ="+vehicle.getId());

		if(!vehicleDoesAlreadyExist(vehicle)) 
			saveMindSphereAsset(createMindSphereAssetFromVehicle(vehicle));
		
		createMindSphereTimeSeriesFromVehicle(vehicle);
		
		serviceResult.setResult("OK");
		return Response.status(201).entity(serviceResult).build();
	}

	
	private Boolean vehicleDoesAlreadyExist(VehicleNormalized vehicle)
	{
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+vehicle.getId()+"Asset\"}");
		return assets.size()>0;
	}
	
	private Asset createMindSphereAssetFromVehicle(VehicleNormalized vehicle) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		MindSphereMapper mindSphereMapper = new MindSphereMapper();
		
		
		List<String> keys = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		keys.add("Source");
		values.add((String) vehicle.getSource().getValue());
		keys.add("DataProvider");
		values.add((String) vehicle.getDataProvider().getValue());
		keys.add("Name");
		values.add((String) vehicle.getName().getValue());
		keys.add("VehicleType");
		values.add((String) vehicle.getVehicleType().getValue());
		keys.add("Category");
		values.add((String) vehicle.getCategory().getValue());
		keys.add("VehicleIdentificationNumber");
		values.add((String) vehicle.getVehicleIdentificationNumber().getValue());
		keys.add("VehiclePlateIdentifier");
		values.add((String) vehicle.getVehiclePlateIdentifier().getValue());
		keys.add("FleetVehicleId");
		values.add((String) vehicle.getFleetVehicleId().getValue());
		keys.add("DateVehicleFirstRegistered");
		values.add((String) vehicle.getDateVehicleFirstRegistered().getValue());
		keys.add("DateFirstUsed");
		values.add((String) vehicle.getDateFirstUsed().getValue());
		keys.add("PurchaseDate");
		values.add((String) vehicle.getPurchaseDate().getValue());
		keys.add("VehicleConfiguration");
		values.add((String) vehicle.getVehicleConfiguration().getValue());
		keys.add("Color");
		values.add((String) vehicle.getColor().getValue());
		keys.add("Owner");
		values.add((String) vehicle.getOwner().getValue());
		keys.add("Feature");
		values.add((String) vehicle.getFeature().getValue().toString());
		keys.add("ServiceProvided");
		values.add((String) vehicle.getServiceProvided().getValue().toString());
		keys.add("VehicleSpecialUsage");
		values.add((String) vehicle.getVehicleSpecialUsage().getValue());
		keys.add("RefVehicleModel");
		values.add((String) vehicle.getRefVehicleModel().getValue());
		keys.add("AreaServed");
		values.add((String) vehicle.getAreaServed().getValue());
		keys.add("DateModified");
		values.add((String) vehicle.getDateModified().getValue());
		keys.add("DateCreated");
		values.add((String) vehicle.getDateCreated().getValue());
		List<Variable> assetVariables = mindSphereMapper.fiPropertiesToMiVariables(keys, values);
		

		List<String> properties = Stream.of("Location", "PreviousLocation", "Speed", "Heading", "MileageFromOdometer","ServiceStatus").collect(Collectors.toList());
		List<String> uoms = Stream.of("Coordinates","Coordinates","km/h", "°", "km","Dimensionless").collect(Collectors.toList());
		AspectType aspectType = mindSphereMapper.fiStateToMiAspectType(vehicle.getId(), (String) vehicle.getDescription().getValue(), properties, uoms);
		
		
		return mindSphereGateway.createAsset(vehicle.getId(), assetVariables, aspectType);
	}
	
	private boolean saveMindSphereAsset(Asset asset) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		logger.debug("VehicleModel created");
		return mindSphereGateway.saveAsset(asset);
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
		
			String curr_location = vehicle.getLocation().getValue().getCoordinates().get(0) + "," +  vehicle.getLocation().getValue().getCoordinates().get(1);
			String prev_location = vehicle.getPreviousLocation().getValue().getCoordinates().get(0) + "," +  vehicle.getPreviousLocation().getValue().getCoordinates().get(1);

			timeseriesPoint.getFields().put("Location",curr_location);
			timeseriesPoint.getFields().put("PreviousLocation",prev_location);
			timeseriesPoint.getFields().put("Speed",(Double) vehicle.getSpeed().getValue());
			timeseriesPoint.getFields().put("Heading",(Double) vehicle.getHeading().getValue());
			timeseriesPoint.getFields().put("MileageFromOdometer",(Double) vehicle.getMileageFromOdometer().getValue());

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
