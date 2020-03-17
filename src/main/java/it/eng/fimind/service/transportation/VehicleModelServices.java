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

import it.eng.fimind.model.fiware.transportation.VehicleModel;
import it.eng.fimind.util.MindSphereGateway;
import it.eng.fimind.util.MindSphereMapper;
import it.eng.fimind.util.ServiceResult;

/**
 * Root resource (exposed at "vehiclemodel" path)
 */
@Path("vehiclemodel")
public class VehicleModelServices {
	private static Logger logger = Logger.getLogger(VehicleModelServices.class);
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getIt() {
		return "VehicleModel Services : got it!!";
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createDataInJSON(@Valid VehicleModel vehicleModel) { 
		ServiceResult serviceResult=new ServiceResult();
		logger.debug("Id ="+vehicleModel.getId());
		
		if(!vehicleModelDoesAlreadyExist(vehicleModel)) 
			saveMindSphereAsset(createMindSphereAssetFromVehicleModel(vehicleModel));
		
		createMindSphereTimeSeriesFromVehicleModel(vehicleModel);
		
		serviceResult.setResult("OK");
		return Response.status(201).entity(serviceResult).build();
	}

	
	private Boolean vehicleModelDoesAlreadyExist(VehicleModel vehicleModel)
	{
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+vehicleModel.getId()+"Asset\"}");
		return assets.size()>0;
	}
	
	public Asset createMindSphereAssetFromVehicleModel(VehicleModel vehicleModel) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		MindSphereMapper mindSphereMapper = new MindSphereMapper();
		
		
		List<String> keys = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		keys.add("Source");
		values.add(vehicleModel.getSource());
		keys.add("DataProvider");
		values.add(vehicleModel.getDataProvider());
		keys.add("Name");
		values.add(vehicleModel.getName());
		keys.add("VehicleType");
		values.add(vehicleModel.getVehicleType());
		keys.add("BrandName");
		values.add(vehicleModel.getBrandName());
		keys.add("ModelName");
		values.add(vehicleModel.getModelName());
		keys.add("ManufacturerName");
		values.add(vehicleModel.getManufacturerName());
		keys.add("VehicleModelDate");
		values.add(vehicleModel.getVehicleModelDate());
		keys.add("CargoVolume");
		values.add(vehicleModel.getCargoVolume().toString());
		keys.add("FuelType");
		values.add(vehicleModel.getFuelType());
		keys.add("FuelConsumption");
		values.add(vehicleModel.getFuelConsumption().toString());
		keys.add("Height");
		values.add(vehicleModel.getHeight().toString());
		keys.add("Width");
		values.add(vehicleModel.getWidth().toString());
		keys.add("Depth");
		values.add(vehicleModel.getDepth().toString());
		keys.add("Weight");
		values.add(vehicleModel.getWeight().toString());
		keys.add("VehicleEngine");
		values.add(vehicleModel.getVehicleEngine());
		List<Variable> assetVariables = mindSphereMapper.fiPropertiesToMiVariables(keys, values);
		

		List<String> properties = Stream.of("FuelConsumption").collect(Collectors.toList());
		List<String> uoms = Stream.of("l/100km").collect(Collectors.toList());
		AspectType aspectType = mindSphereMapper.fiStateToMiAspectType(vehicleModel.getId(), vehicleModel.getDescription(), properties, uoms);
		
		
		return mindSphereGateway.createAsset(vehicleModel.getId(), assetVariables, aspectType);
	}
	
	private boolean saveMindSphereAsset(Asset asset) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		logger.debug("VehicleModel created");
		return mindSphereGateway.saveAsset(asset);
	}
	
	public boolean createMindSphereTimeSeriesFromVehicleModel(VehicleModel vehicleModel) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+vehicleModel.getId()+"Asset\"}");
		try {
			List<Timeseries> timeSeriesList = new ArrayList<Timeseries>();
			Date now = new Date();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			String instant = df.format(now);
			Timeseries timeseriesPoint = new Timeseries();
			timeseriesPoint.getFields().put("_time", instant);
		
			timeseriesPoint.getFields().put("FuelConsumption",vehicleModel.getFuelConsumption());

			timeSeriesList.add(timeseriesPoint);
			mindSphereGateway.putTimeSeries(assets.get(0).getAssetId(), vehicleModel.getId()+"AspectType", timeSeriesList);
			logger.debug("VehicleModel updated");

		} catch (Exception e) {
			// Exception handling
			e.printStackTrace();
			return false;
		}
		return true;
	}	
}

