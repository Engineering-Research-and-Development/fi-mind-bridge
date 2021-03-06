package it.eng.fimind.service.device;


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

import it.eng.fimind.model.fiware.device.DeviceModel;
import it.eng.fimind.util.MindSphereGateway;
import it.eng.fimind.util.MindSphereMapper;
import it.eng.fimind.util.ServiceResult;

/**
 * Root resource (exposed at "device" path)
 */
@Path("deviceModel")
public class DeviceModelServices {
	private static Logger logger = Logger.getLogger(DeviceModelServices.class);

	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getIt() {
		return "Device Model Service: got it!!";
	}

	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteIt(@QueryParam("id") String id) {
		logger.debug("[DeviceModelServices] DELETE Request");
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
	public Response createDataInJSON(@HeaderParam("debug-mode") String debugMode, @Valid DeviceModel deviceModel) { 
		logger.debug("[DeviceModelServices] POST Request");
		ServiceResult serviceResult = new ServiceResult();
		
		logger.debug("Id ="+deviceModel.getId());
		
		if(debugMode!=null && debugMode.equals("true")){
			logger.debug("DEBUG MODE FOR --- DeviceModel ---");
			createMindSphereAssetFromDeviceModel(deviceModel, true);
			serviceResult.setResult("Test gone fine");
			return Response.status(200).entity(serviceResult).build();
		}else {
			Boolean result = true;
			MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
			if(!mindSphereGateway.assetDoesAlreadyExist(deviceModel.getId()))
				result = createMindSphereAssetFromDeviceModel(deviceModel, false);
			
			if(result)
				result = createMindSphereTimeSeriesFromDeviceModel(deviceModel);
	
			if(result) {
				serviceResult.setResult("DeviceModel added succesfully");
				return Response.status(201).entity(serviceResult).build();
			}
			else {
				serviceResult.setResult("Something went wrong, check your FI-MIND logs");
				return Response.status(500).entity(serviceResult).build();
			}
		}
	}
	
	public Boolean createMindSphereAssetFromDeviceModel(DeviceModel deviceModel, Boolean isDebugMode) {
		Boolean result = false;
		
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		MindSphereMapper mindSphereMapper = new MindSphereMapper();
				
		List<String> keys = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		List<String> varDefDataTypes = new ArrayList<String>();

		if(deviceModel.getType()!=null) {
			keys.add("entityType");
			values.add(deviceModel.getType());
			varDefDataTypes.add("String");
		}
		if(deviceModel.getSource()!=null) {
			keys.add("source");
			values.add(deviceModel.getSource());
			varDefDataTypes.add("String");
		}
		if(deviceModel.getDataProvider()!=null) {
			keys.add("dataProvider");
			values.add(deviceModel.getDataProvider());
			varDefDataTypes.add("String");
		}
		if(deviceModel.getCategory()!=null) {
			keys.add("category");
			values.add(deviceModel.getCategory().toString());
			varDefDataTypes.add("String");
		}
		if(deviceModel.getDeviceClass()!=null) {
			keys.add("deviceClass");
			values.add(deviceModel.getDeviceClass());
			varDefDataTypes.add("String");
		}
		if(deviceModel.getFunction()!=null) {
			keys.add("function");
			values.add(deviceModel.getFunction().toString());
			varDefDataTypes.add("String");
		}
		if(deviceModel.getSupportedProtocol()!=null) {
			keys.add("supportedProtocol");
			values.add(deviceModel.getSupportedProtocol().toString());
			varDefDataTypes.add("String");
		}
		if(deviceModel.getSupportedUnits()!=null) {
			keys.add("supportedUnits");
			values.add(deviceModel.getSupportedUnits().toString());
			varDefDataTypes.add("String");
		}
		if(deviceModel.getEnergyLimitationClass()!=null) {
			keys.add("energyLimitationClass");		
			values.add(deviceModel.getEnergyLimitationClass());
			varDefDataTypes.add("String");
		}
		if(deviceModel.getBrandName()!=null) {
			keys.add("brandName");
			values.add(deviceModel.getBrandName());
			varDefDataTypes.add("String");
		}
		if(deviceModel.getModelName()!=null) {
			keys.add("modelName");
			values.add(deviceModel.getModelName());
			varDefDataTypes.add("String");
		}
		if(deviceModel.getManufacturerName()!=null) {
			keys.add("manufacturerName");
			values.add(deviceModel.getManufacturerName());
			varDefDataTypes.add("String");
		}
		if(deviceModel.getName()!=null) {
			keys.add("entityName");
			values.add(deviceModel.getName());
			varDefDataTypes.add("String");
		}
		if(deviceModel.getDocumentation()!=null) {
			keys.add("documentation");
			values.add(deviceModel.getDocumentation());
			varDefDataTypes.add("String");
		}
		if(deviceModel.getImage()!=null) {
			keys.add("image");
			values.add(deviceModel.getImage());
			varDefDataTypes.add("String");
		}
		if(deviceModel.getDateCreated()!=null) {
			keys.add("dateCreated");
			values.add(deviceModel.getDateCreated());
			varDefDataTypes.add("Timestamp");
		}
		List<VariableDefinition> assetVariablesDefinitions = mindSphereMapper.fiPropertiesToMiVariablesDefinitions(keys, values, varDefDataTypes);
		List<Variable> assetVariables = mindSphereMapper.fiPropertiesToMiVariables(keys, values, varDefDataTypes);
			
		
		List<String> properties = Stream.of("dateModified").collect(Collectors.toList());
		List<String> uoms = Stream.of("t").collect(Collectors.toList());
		List<String> dataTypes = Stream.of("Timestamp").collect(Collectors.toList());
		if(deviceModel.getControlledProperty()!=null && deviceModel.getSupportedUnits()!=null) {
			for (int i=0; i<deviceModel.getControlledProperty().size(); i++) {
				String property = deviceModel.getControlledProperty().get(i);
				String uom = deviceModel.getSupportedUnits().get(i);
				properties.add(property);
				uoms.add(uom);
				dataTypes.add("Double");
			}
		}
		AspectType aspectType;
		if(deviceModel.getDescription()!=null)
			aspectType = mindSphereMapper.fiStateToMiAspectType(deviceModel.getId(), (String) deviceModel.getDescription(), properties, uoms, dataTypes);
		else
			aspectType = mindSphereMapper.fiStateToMiAspectType(deviceModel.getId(), properties, uoms, dataTypes);
				
		
		if(isDebugMode) {
			logger.debug(mindSphereGateway.createAsset(deviceModel.getId(), assetVariablesDefinitions, assetVariables, aspectType));
			result = true;
		}else {
			result =  mindSphereGateway.saveAsset(deviceModel.getId(), assetVariablesDefinitions, assetVariables, aspectType);
			if(result)
				logger.debug("DeviceModel created");
			else 		
				logger.error("DeviceModel couldn't be created");
		}
		return result;
	}
	
	public boolean createMindSphereTimeSeriesFromDeviceModel(DeviceModel deviceModel) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+deviceModel.getId()+"\"}");
		try {
			List<Timeseries> timeSeriesList = new ArrayList<Timeseries>();

			Timeseries timeseriesPoint = new Timeseries();
			
			timeseriesPoint.getFields().put("_time", Instant.now().toString());
			
			if(deviceModel.getDateModified()!=null) {
				timeseriesPoint.getFields().put("dateModified", deviceModel.getDateModified());
			}
			
			timeSeriesList.add(timeseriesPoint);
			mindSphereGateway.putTimeSeries(assets.get(0).getAssetId(), deviceModel.getId(), timeSeriesList);
			logger.debug("DeviceModel updated");
		} catch (Exception e) {
			// Exception handling
			e.printStackTrace();
			return false;
		}
		return true;
	}	
}
