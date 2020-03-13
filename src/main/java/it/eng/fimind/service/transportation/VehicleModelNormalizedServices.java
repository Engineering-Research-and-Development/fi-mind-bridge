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

import it.eng.fimind.model.fiware.transportation.VehicleModelNormalized;
import it.eng.fimind.util.MindSphereGateway;
import it.eng.fimind.util.MindSphereMapper;
import it.eng.fimind.util.ServiceResult;

/**
 * Root resource (exposed at "vehiclemodelnormalized" path)
 */
@Path("vehiclemodelnormalized")
public class VehicleModelNormalizedServices {
	private static Logger logger = Logger.getLogger(VehicleModelNormalizedServices.class);
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getIt() {
		return "VehicleModelNormalized Services : got it!!";
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createDataInJSON(@Valid VehicleModelNormalized vehicleModel) { 
		ServiceResult serviceResult = new ServiceResult();
		logger.debug("Id ="+vehicleModel.getId());
		
		if(!vehicleModelDoesAlreadyExist(vehicleModel)) 
			saveMindSphereAsset(createMindSphereAssetFromVehicleModel(vehicleModel));
		
		createMindSphereTimeSeriesFromVehicleModel(vehicleModel);
		
		serviceResult.setResult("OK");
		return Response.status(201).entity(serviceResult).build();
	}

	
	private Boolean vehicleModelDoesAlreadyExist(VehicleModelNormalized vehicleModel)
	{
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+vehicleModel.getId()+"Asset\"}");
		return assets.size()>0;
	}
	
	private Asset createMindSphereAssetFromVehicleModel(VehicleModelNormalized vehicleModel) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
MindSphereMapper mindSphereMapper = new MindSphereMapper();
		
		
		List<String> keys = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		keys.add("Source");
		values.add((String) vehicleModel.getSource().getValue());
		keys.add("DataProvider");
		values.add((String) vehicleModel.getDataProvider().getValue());
		keys.add("Name");
		values.add((String) vehicleModel.getName().getValue());
		keys.add("VehicleType");
		values.add((String) vehicleModel.getVehicleType().getValue());
		keys.add("BrandName");
		values.add((String) vehicleModel.getBrandName().getValue());
		keys.add("ModelName");
		values.add((String) vehicleModel.getModelName().getValue());
		keys.add("ManufacturerName");
		values.add((String) vehicleModel.getManufacturerName().getValue());
		keys.add("VehicleModelDate");
		values.add((String) vehicleModel.getVehicleModelDate().getValue());
		keys.add("CargoVolume");
		values.add((String) vehicleModel.getCargoVolume().getValue());
		keys.add("FuelType");
		values.add((String) vehicleModel.getFuelType().getValue());
		keys.add("FuelConsumption");
		values.add((String) vehicleModel.getFuelConsumption().getValue());
		keys.add("Height");
		values.add((String) vehicleModel.getHeight().getValue());
		keys.add("Width");
		values.add((String) vehicleModel.getWidth().getValue());
		keys.add("Depth");
		values.add((String) vehicleModel.getDepth().getValue());
		keys.add("Weight");
		values.add((String) vehicleModel.getWeight().getValue());
		keys.add("VehicleEngine");
		values.add((String) vehicleModel.getVehicleEngine().getValue());
		List<Variable> assetVariables = mindSphereMapper.fiPropertiesToMiVariables(keys, values);
		

		List<String> properties = Stream.of("FuelConsumption").collect(Collectors.toList());
		List<String> uoms = Stream.of("l/100km").collect(Collectors.toList());
		AspectType aspectType = mindSphereMapper.fiStateToMiAspectType(vehicleModel.getId(), (String) vehicleModel.getDescription().getValue(), properties, uoms);
		
		
		return mindSphereGateway.createAsset(vehicleModel.getId(), assetVariables, aspectType);
	}
	
	private boolean saveMindSphereAsset(Asset asset) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		logger.debug("TrafficFlowObserved created");
		return mindSphereGateway.saveAsset(asset);
	}
	
	private boolean createMindSphereTimeSeriesFromVehicleModel(VehicleModelNormalized vehicleModel) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+vehicleModel.getId()+"Asset\"}");
		try {
			List<Timeseries> timeSeriesList = new ArrayList<Timeseries>();
			Date now = new Date();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			String instant = df.format(now);
			Timeseries timeseriesPoint = new Timeseries();
			timeseriesPoint.getFields().put("_time", instant);
		
			timeseriesPoint.getFields().put("FuelConsumption",(Double) vehicleModel.getFuelConsumption().getValue());

			timeSeriesList.add(timeseriesPoint);
			mindSphereGateway.putTimeSeries(assets.get(0).getAssetId(), vehicleModel.getId()+"AspectType", timeSeriesList);
			logger.debug("VehicleModelNormalized updated");

		} catch (Exception e) {
			// Exception handling
			e.printStackTrace();
			return false;
		}
		return true;
	}	
}

