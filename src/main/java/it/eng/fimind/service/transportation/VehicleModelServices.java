package it.eng.fimind.service.transportation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import it.eng.fimind.model.fiware.transportation.VehicleModel;
import it.eng.fimind.util.MindSphereGateway;
import it.eng.fimind.util.MindSphereMapper;
import it.eng.fimind.util.ServiceResult;

/**
 * Root resource (exposed at "vehiclemodel" path)
 */
@Path("vehicleModel")
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
	public Response createDataInJSON(@HeaderParam("debug-mode") String debugMode, @Valid VehicleModel vehicleModel) { 
		ServiceResult serviceResult = new ServiceResult();
		logger.debug("Id ="+vehicleModel.getId());
		
		if(debugMode!=null && debugMode.equals("true")){
			System.out.println("DEBUG MODE FOR --- VehicleModel ---");
			createMindSphereAssetFromVehicleModel(vehicleModel, true);
			serviceResult.setResult("Test gone fine");
			return Response.status(200).entity(serviceResult).build();
		}else {
			Boolean result = false;
			if(!vehicleModelDoesAlreadyExist(vehicleModel)) 
				result = createMindSphereAssetFromVehicleModel(vehicleModel, false);
			
			result = createMindSphereTimeSeriesFromVehicleModel(vehicleModel);
			
			if(result) {
				serviceResult.setResult("VehicleModel added succesfully");
				return Response.status(201).entity(serviceResult).build();
			}
			else {
				serviceResult.setResult("Something went wrong, check your FI-MIND logs");
				return Response.status(500).entity(serviceResult).build();
			}
		}
	}

	
	private Boolean vehicleModelDoesAlreadyExist(VehicleModel vehicleModel)
	{
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+vehicleModel.getId()+"Asset\"}");
		return assets.size()>0;
	}
	
	public Boolean createMindSphereAssetFromVehicleModel(VehicleModel vehicleModel, Boolean isDebugMode) {
		Boolean result = false;
		
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		MindSphereMapper mindSphereMapper = new MindSphereMapper();
		
		vehicleModel.setId(vehicleModel.getId().replaceAll("-","_"));

		List<String> keys = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		List<String> varDefDataTypes = new ArrayList<String>();

		if(vehicleModel.getSource()!=null) {
			keys.add("Source");
			values.add(vehicleModel.getSource());
			varDefDataTypes.add("String");
		}
		if(vehicleModel.getDataProvider()!=null) {
			keys.add("DataProvider");
			values.add(vehicleModel.getDataProvider());
			varDefDataTypes.add("String");
		}
		if(vehicleModel.getName()!=null) {
			keys.add("Name");
			values.add(vehicleModel.getName());
			varDefDataTypes.add("String");
		}
		if(vehicleModel.getVehicleType()!=null) {
			keys.add("VehicleType");
			values.add(vehicleModel.getVehicleType());
			varDefDataTypes.add("String");
		}
		if(vehicleModel.getBrandName()!=null) {
			keys.add("BrandName");
			values.add(vehicleModel.getBrandName());
			varDefDataTypes.add("String");
		}
		if(vehicleModel.getModelName()!=null) {
			keys.add("ModelName");
			values.add(vehicleModel.getModelName());
			varDefDataTypes.add("String");
		}
		if(vehicleModel.getManufacturerName()!=null) {
			keys.add("ManufacturerName");
			values.add(vehicleModel.getManufacturerName());
			varDefDataTypes.add("String");
		}
		if(vehicleModel.getVehicleModelDate()!=null) {
			keys.add("VehicleModelDate");
			values.add(vehicleModel.getVehicleModelDate());
			varDefDataTypes.add("String");
		}
		if(vehicleModel.getCargoVolume()!=null) {
			keys.add("CargoVolume");
			values.add(vehicleModel.getCargoVolume().toString());
			varDefDataTypes.add("Integer");
		}
		if(vehicleModel.getFuelType()!=null) {
			keys.add("FuelType");
			values.add(vehicleModel.getFuelType());
			varDefDataTypes.add("String");
		}
		if(vehicleModel.getFuelConsumption()!=null) {
			keys.add("FuelConsumption");
			values.add(vehicleModel.getFuelConsumption().toString());
			varDefDataTypes.add("Double");
		}
		if(vehicleModel.getHeight()!=null) {
			keys.add("Height");
			values.add(vehicleModel.getHeight().toString());
			varDefDataTypes.add("Double");
		}
		if(vehicleModel.getWidth()!=null) {
			keys.add("Width");
			values.add(vehicleModel.getWidth().toString());
			varDefDataTypes.add("Double");
		}
		if(vehicleModel.getDepth()!=null) {
			keys.add("Depth");
			values.add(vehicleModel.getDepth().toString());
			varDefDataTypes.add("Double");
		}
		if(vehicleModel.getWeight()!=null) {
			keys.add("Weight");
			values.add(vehicleModel.getWeight().toString());
			varDefDataTypes.add("Double");
		}
		if(vehicleModel.getVehicleEngine()!=null) {
			keys.add("VehicleEngine");
			values.add(vehicleModel.getVehicleEngine());
			varDefDataTypes.add("String");
		}
		if(vehicleModel.getUrl()!=null) {
			keys.add("Url");
			values.add(vehicleModel.getUrl());
			varDefDataTypes.add("String");
		}
		if(vehicleModel.getImage()!=null) {
			keys.add("Image");
			values.add(vehicleModel.getImage());
			varDefDataTypes.add("String");
		}
		if(vehicleModel.getDateModified()!=null) {
			keys.add("DateModified");
			values.add(vehicleModel.getDateModified());
			varDefDataTypes.add("Timestamp");
		}
		if(vehicleModel.getDateCreated()!=null) {
			keys.add("DateCreated");
			values.add(vehicleModel.getDateCreated());
			varDefDataTypes.add("Timestamp");
		}
		List<VariableDefinition> assetVariablesDefinitions = mindSphereMapper.fiPropertiesToMiVariablesDefinitions(keys, values, varDefDataTypes);
		List<Variable> assetVariables = mindSphereMapper.fiPropertiesToMiVariables(keys, values, varDefDataTypes);


		List<String> properties = new ArrayList<String>();
		List<String> uoms = new ArrayList<String>();
		List<String> dataTypes = new ArrayList<String>();
		AspectType aspectType = mindSphereMapper.fiStateToMiAspectType(vehicleModel.getId(), vehicleModel.getDescription(), properties, uoms, dataTypes);
		
		
		if(isDebugMode) {
			System.out.println(mindSphereGateway.createAsset(vehicleModel.getId(), assetVariablesDefinitions, assetVariables, aspectType));
			result = true;
		}else {
			result = mindSphereGateway.saveAsset(vehicleModel.getId(), assetVariablesDefinitions, assetVariables, aspectType);
			if(result)
				logger.debug("VehicleModel created");
			else 		
				logger.error("VehicleModel couldn't be created");
		}
		return result;
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

