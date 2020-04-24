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

import it.eng.fimind.model.fiware.transportation.VehicleModelNormalized;
import it.eng.fimind.util.MindSphereGateway;
import it.eng.fimind.util.MindSphereMapper;
import it.eng.fimind.util.ServiceResult;

/**
 * Root resource (exposed at "vehiclemodelnormalized" path)
 */
@Path("vehicleModelNormalized")
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
	public Response createDataInJSON(@HeaderParam("debug-mode") String debugMode, @Valid VehicleModelNormalized vehicleModel) { 
		ServiceResult serviceResult = new ServiceResult();
		logger.debug("Id ="+vehicleModel.getId());
		
		if(debugMode!=null && debugMode.equals("true")){
			logger.debug("DEBUG MODE FOR --- VehicleModelNormalized ---");
			createMindSphereAssetFromVehicleModel(vehicleModel, true);
			serviceResult.setResult("Test gone fine");
			return Response.status(200).entity(serviceResult).build();
		}else {
			Boolean result = true;
			if(!vehicleModelDoesAlreadyExist(vehicleModel)) 
				result = createMindSphereAssetFromVehicleModel(vehicleModel, false);
				
			if(result)
				result = createMindSphereTimeSeriesFromVehicleModel(vehicleModel);
			
			if(result) {
				serviceResult.setResult("VehicleModelNormalized added succesfully");
				return Response.status(201).entity(serviceResult).build();
			}
			else {
				serviceResult.setResult("Something went wrong, check your FI-MIND logs");
				return Response.status(500).entity(serviceResult).build();
			}
		}
	}

	
	private Boolean vehicleModelDoesAlreadyExist(VehicleModelNormalized vehicleModel)
	{
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+vehicleModel.getId()+"\"}");
		return assets.size()>0;
	}
	
	private Boolean createMindSphereAssetFromVehicleModel(VehicleModelNormalized vehicleModel, Boolean isDebugMode) {
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
			values.add((String) vehicleModel.getSource().getValue());
			varDefDataTypes.add("String");
		}
		if(vehicleModel.getDataProvider()!=null) {
			keys.add("dataProvider");
			values.add((String) vehicleModel.getDataProvider().getValue());
			varDefDataTypes.add("String");
		}
		if(vehicleModel.getName()!=null) {
			keys.add("entityName");
			values.add((String) vehicleModel.getName().getValue());
			varDefDataTypes.add("String");
		}
		if(vehicleModel.getVehicleType()!=null) {
			keys.add("vehicleType");
			values.add((String) vehicleModel.getVehicleType().getValue());
			varDefDataTypes.add("String");
		}
		if(vehicleModel.getBrandName()!=null) {
			keys.add("brandName");
			values.add((String) vehicleModel.getBrandName().getValue());
			varDefDataTypes.add("String");
		}
		if(vehicleModel.getModelName()!=null) {
			keys.add("modelName");
			values.add((String) vehicleModel.getModelName().getValue());
			varDefDataTypes.add("String");
		}
		if(vehicleModel.getManufacturerName()!=null) {
			keys.add("manufacturerName");
			values.add((String) vehicleModel.getManufacturerName().getValue());
			varDefDataTypes.add("String");
		}
		if(vehicleModel.getVehicleModelDate()!=null) {
			keys.add("vehicleModelDate");
			values.add((String) vehicleModel.getVehicleModelDate().getValue());
			varDefDataTypes.add("String");
		}
		if(vehicleModel.getCargoVolume()!=null) {
			keys.add("cargoVolume");
			values.add((String) vehicleModel.getCargoVolume().getValue());
			varDefDataTypes.add("Double");
		}
		if(vehicleModel.getFuelType()!=null) {
			keys.add("fuelType");
			values.add((String) vehicleModel.getFuelType().getValue());
			varDefDataTypes.add("String");
		}
		if(vehicleModel.getFuelConsumption()!=null) {
			keys.add("fuelConsumption");
			values.add((String) vehicleModel.getFuelConsumption().getValue());
			varDefDataTypes.add("Double");
		}
		if(vehicleModel.getHeight()!=null) {
			keys.add("height");
			values.add((String) vehicleModel.getHeight().getValue());
			varDefDataTypes.add("Double");
		}
		if(vehicleModel.getWidth()!=null) {
			keys.add("width");
			values.add((String) vehicleModel.getWidth().getValue());
			varDefDataTypes.add("Double");
		}
		if(vehicleModel.getDepth()!=null) {
			keys.add("depth");
			values.add((String) vehicleModel.getDepth().getValue());
			varDefDataTypes.add("Double");
		}
		if(vehicleModel.getWeight()!=null) {
			keys.add("weight");
			values.add((String) vehicleModel.getWeight().getValue());
			varDefDataTypes.add("Double");
		}
		if(vehicleModel.getVehicleEngine()!=null) {
			keys.add("vehicleEngine");
			values.add((String) vehicleModel.getVehicleEngine().getValue());
			varDefDataTypes.add("String");
		}
		if(vehicleModel.getUrl()!=null) {
			keys.add("url");
			values.add((String) vehicleModel.getUrl().getValue());
			varDefDataTypes.add("String");
		}
		if(vehicleModel.getImage()!=null) {
			keys.add("image");
			values.add((String) vehicleModel.getImage().getValue());
			varDefDataTypes.add("String");
		}
		if(vehicleModel.getDateCreated()!=null) {
			keys.add("dateCreated");
			values.add((String) vehicleModel.getDateCreated().getValue());
			varDefDataTypes.add("Timestamp");
		}
		List<VariableDefinition> assetVariablesDefinitions = mindSphereMapper.fiPropertiesToMiVariablesDefinitions(keys, values, varDefDataTypes);
		List<Variable> assetVariables = mindSphereMapper.fiPropertiesToMiVariables(keys, values, varDefDataTypes);

		
		List<String> properties = Stream.of("dateModified").collect(Collectors.toList());
		List<String> uoms = Stream.of("t").collect(Collectors.toList());
		List<String> dataTypes = Stream.of("Timestamp").collect(Collectors.toList());
		AspectType aspectType = mindSphereMapper.fiStateToMiAspectType(vehicleModel.getId(), (String) vehicleModel.getDescription().getValue(), properties, uoms, dataTypes);
		
		
		if(isDebugMode) {
			logger.debug(mindSphereGateway.createAsset(vehicleModel.getId(), assetVariablesDefinitions, assetVariables, aspectType));
			result = true;
		}else {
			result = mindSphereGateway.saveAsset(vehicleModel.getId(), assetVariablesDefinitions, assetVariables, aspectType);
			if(result)
				logger.debug("VehicleModelNormalized created");
			else 		
				logger.error("VehicleModelNormalized couldn't be created");
		}
		return result;
	}
	
	public boolean createMindSphereTimeSeriesFromVehicleModel(VehicleModelNormalized vehicleModel) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+vehicleModel.getId()+"\"}");
		try {
			List<Timeseries> timeSeriesList = new ArrayList<Timeseries>();
			Date now = new Date();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			String instant = df.format(now);
			Timeseries timeseriesPoint = new Timeseries();
			timeseriesPoint.getFields().put("_time", instant);
		
			if(vehicleModel.getDateModified()!=null){
				timeseriesPoint.getFields().put("dateModified", (String) vehicleModel.getDateModified().getValue());
			}
			
			timeSeriesList.add(timeseriesPoint);
			mindSphereGateway.putTimeSeries(assets.get(0).getAssetId(), vehicleModel.getId(), timeSeriesList);
			logger.debug("VehicleModelNormalized updated");

		} catch (Exception e) {
			// Exception handling
			e.printStackTrace();
			return false;
		}
		return true;
	}	
}

