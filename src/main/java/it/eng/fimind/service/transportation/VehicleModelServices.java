package it.eng.fimind.service.transportation;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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

	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteIt(@QueryParam("id") String id) {
		logger.debug("[VehicleModelServices] DELETE Request");
		ServiceResult serviceResult = new ServiceResult();

		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		if(mindSphereGateway.deleteAssetOnCascade(id))
		{
			serviceResult.setMessage("Deleted successfully!");
			return Response.status(200).entity(serviceResult).build();
		}
		else {
			serviceResult.setResult("Something went wrong, check your FI-MIND logs");
			return Response.status(500).entity(serviceResult).build();
		}
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createDataInJSON(@HeaderParam("debug-mode") String debugMode, @Valid VehicleModel vehicleModel) { 
		logger.debug("[VehicleModelServices] POST Request");
		ServiceResult serviceResult = new ServiceResult();
	
		logger.debug("Id ="+vehicleModel.getId());
		
		if(debugMode!=null && debugMode.equals("true")){
			logger.debug("DEBUG MODE FOR --- VehicleModel ---");
			createMindSphereAssetFromVehicleModel(vehicleModel, true);
			serviceResult.setResult("Test gone fine");
			return Response.status(200).entity(serviceResult).build();
		}else {
			Boolean result = true;
			MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
			if(!mindSphereGateway.assetDoesAlreadyExist(vehicleModel.getId()))
				result = createMindSphereAssetFromVehicleModel(vehicleModel, false);
			
			if(result)
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
	
	public Boolean createMindSphereAssetFromVehicleModel(VehicleModel vehicleModel, Boolean isDebugMode) {
		Boolean result = false;
		
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		MindSphereMapper mindSphereMapper = new MindSphereMapper();
		
		List<String> keys = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		List<String> varDefDataTypes = new ArrayList<String>();

		if(vehicleModel.getType()!=null) {
			keys.add("entityType");
			values.add(vehicleModel.getType());
			varDefDataTypes.add("String");
		}
		if(vehicleModel.getSource()!=null) {
			keys.add("source");
			values.add(vehicleModel.getSource());
			varDefDataTypes.add("String");
		}
		if(vehicleModel.getDataProvider()!=null) {
			keys.add("dataProvider");
			values.add(vehicleModel.getDataProvider());
			varDefDataTypes.add("String");
		}
		if(vehicleModel.getName()!=null) {
			keys.add("entityName");
			values.add(vehicleModel.getName());
			varDefDataTypes.add("String");
		}
		if(vehicleModel.getVehicleType()!=null) {
			keys.add("vehicleType");
			values.add(vehicleModel.getVehicleType());
			varDefDataTypes.add("String");
		}
		if(vehicleModel.getBrandName()!=null) {
			keys.add("brandName");
			values.add(vehicleModel.getBrandName());
			varDefDataTypes.add("String");
		}
		if(vehicleModel.getModelName()!=null) {
			keys.add("modelName");
			values.add(vehicleModel.getModelName());
			varDefDataTypes.add("String");
		}
		if(vehicleModel.getManufacturerName()!=null) {
			keys.add("manufacturerName");
			values.add(vehicleModel.getManufacturerName());
			varDefDataTypes.add("String");
		}
		if(vehicleModel.getVehicleModelDate()!=null) {
			keys.add("vehicleModelDate");
			values.add(vehicleModel.getVehicleModelDate());
			varDefDataTypes.add("String");
		}
		if(vehicleModel.getCargoVolume()!=null) {
			keys.add("cargoVolume");
			values.add(vehicleModel.getCargoVolume().toString());
			varDefDataTypes.add("Double");
		}
		if(vehicleModel.getFuelType()!=null) {
			keys.add("fuelType");
			values.add(vehicleModel.getFuelType());
			varDefDataTypes.add("String");
		}
		if(vehicleModel.getFuelConsumption()!=null) {
			keys.add("fuelConsumption");
			values.add(vehicleModel.getFuelConsumption().toString());
			varDefDataTypes.add("Double");
		}
		if(vehicleModel.getHeight()!=null) {
			keys.add("height");
			values.add(vehicleModel.getHeight().toString());
			varDefDataTypes.add("Double");
		}
		if(vehicleModel.getWidth()!=null) {
			keys.add("width");
			values.add(vehicleModel.getWidth().toString());
			varDefDataTypes.add("Double");
		}
		if(vehicleModel.getDepth()!=null) {
			keys.add("depth");
			values.add(vehicleModel.getDepth().toString());
			varDefDataTypes.add("Double");
		}
		if(vehicleModel.getWeight()!=null) {
			keys.add("weight");
			values.add(vehicleModel.getWeight().toString());
			varDefDataTypes.add("Double");
		}
		if(vehicleModel.getVehicleEngine()!=null) {
			keys.add("vehicleEngine");
			values.add(vehicleModel.getVehicleEngine());
			varDefDataTypes.add("String");
		}
		if(vehicleModel.getUrl()!=null) {
			keys.add("url");
			values.add(vehicleModel.getUrl());
			varDefDataTypes.add("String");
		}
		if(vehicleModel.getImage()!=null) {
			keys.add("image");
			values.add(vehicleModel.getImage());
			varDefDataTypes.add("String");
		}
		if(vehicleModel.getDateCreated()!=null) {
			keys.add("dateCreated");
			values.add(vehicleModel.getDateCreated());
			varDefDataTypes.add("Timestamp");
		}
		List<VariableDefinition> assetVariablesDefinitions = mindSphereMapper.fiPropertiesToMiVariablesDefinitions(keys, values, varDefDataTypes);
		List<Variable> assetVariables = mindSphereMapper.fiPropertiesToMiVariables(keys, values, varDefDataTypes);


		List<String> properties = Stream.of("dateModified").collect(Collectors.toList());
		List<String> uoms = Stream.of("t").collect(Collectors.toList());
		List<String> dataTypes = Stream.of("Timestamp").collect(Collectors.toList());
		AspectType aspectType;
		if(vehicleModel.getDescription()!=null)
			aspectType = mindSphereMapper.fiStateToMiAspectType(vehicleModel.getId(), (String) vehicleModel.getDescription(), properties, uoms, dataTypes);
		else
			aspectType = mindSphereMapper.fiStateToMiAspectType(vehicleModel.getId(), properties, uoms, dataTypes);
					
		
		if(isDebugMode) {
			logger.debug(mindSphereGateway.createAsset(vehicleModel.getId(), assetVariablesDefinitions, assetVariables, aspectType));
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
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+vehicleModel.getId()+"\"}");
		try {
			List<Timeseries> timeSeriesList = new ArrayList<Timeseries>();

			Timeseries timeseriesPoint = new Timeseries();
			
			timeseriesPoint.getFields().put("_time", Instant.now().toString());
		
			if(vehicleModel.getDateModified()!=null){
				timeseriesPoint.getFields().put("dateModified",vehicleModel.getDateModified());
			}
			
			timeSeriesList.add(timeseriesPoint);
			mindSphereGateway.putTimeSeries(assets.get(0).getAssetId(), vehicleModel.getId(), timeSeriesList);
			logger.debug("VehicleModel updated");

		} catch (Exception e) {
			// Exception handling
			e.printStackTrace();
			return false;
		}
		return true;
	}	
}

