package it.eng.fimind.service.device;


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

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createDataInJSON(@HeaderParam("debug-mode") String debugMode, @Valid DeviceModel deviceModel) { 
		ServiceResult serviceResult = new ServiceResult();
		logger.debug("Id ="+deviceModel.getId());
		
		if(debugMode!=null && debugMode.equals("true")){
			System.out.println("DEBUG MODE FOR --- DeviceModel ---");
			createMindSphereAssetFromDeviceModel(deviceModel, true);
			serviceResult.setResult("Test gone fine");
			return Response.status(200).entity(serviceResult).build();
		}else {
			Boolean result = false;
			if(!deviceModelDoesAlreadyExist(deviceModel)) 
				result = createMindSphereAssetFromDeviceModel(deviceModel, false);
			
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
	
	
	private Boolean deviceModelDoesAlreadyExist(DeviceModel deviceModel)
	{
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+deviceModel.getId()+"Asset\"}");
		return assets.size()>0;
	}
	
	public Boolean createMindSphereAssetFromDeviceModel(DeviceModel deviceModel, Boolean isDebugMode) {
		Boolean result = false;
		
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		MindSphereMapper mindSphereMapper = new MindSphereMapper();
				
		List<String> keys = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		List<String> varDefDataTypes = new ArrayList<String>();

		if(deviceModel.getSource()!=null) {
			keys.add("Source");
			values.add(deviceModel.getSource());
			varDefDataTypes.add("String");
		}
		if(deviceModel.getDataProvider()!=null) {
			keys.add("DataProvider");
			values.add(deviceModel.getDataProvider());
			varDefDataTypes.add("String");
		}
		if(deviceModel.getCategory()!=null) {
			keys.add("Category");
			values.add(deviceModel.getCategory().toString());
			varDefDataTypes.add("String");
		}
		if(deviceModel.getDeviceClass()!=null) {
			keys.add("DeviceClass");
			values.add(deviceModel.getDeviceClass());
			varDefDataTypes.add("String");
		}
		if(deviceModel.getFunction()!=null) {
			keys.add("Function");
			values.add(deviceModel.getFunction().toString());
			varDefDataTypes.add("String");
		}
		if(deviceModel.getSupportedProtocol()!=null) {
			keys.add("SupportedProtocol");
			values.add(deviceModel.getSupportedProtocol().toString());
			varDefDataTypes.add("String");
		}
		if(deviceModel.getSupportedUnits()!=null) {
			keys.add("SupportedUnits");
			values.add(deviceModel.getSupportedUnits().toString());
			varDefDataTypes.add("String");
		}
		if(deviceModel.getEnergyLimitationClass()!=null) {
			keys.add("EnergyLimitationClass");		
			values.add(deviceModel.getEnergyLimitationClass());
			varDefDataTypes.add("String");
		}
		if(deviceModel.getBrandName()!=null) {
			keys.add("BrandName");
			values.add(deviceModel.getBrandName());
			varDefDataTypes.add("String");
		}
		if(deviceModel.getModelName()!=null) {
			keys.add("ModelName");
			values.add(deviceModel.getModelName());
			varDefDataTypes.add("String");
		}
		if(deviceModel.getManufacturerName()!=null) {
			keys.add("ManufacturerName");
			values.add(deviceModel.getManufacturerName());
			varDefDataTypes.add("String");
		}
		if(deviceModel.getName()!=null) {
			keys.add("DeviceModelName");
			values.add(deviceModel.getName());
			varDefDataTypes.add("String");
		}
		if(deviceModel.getDocumentation()!=null) {
			keys.add("Documentation");
			values.add(deviceModel.getDocumentation());
			varDefDataTypes.add("String");
		}
		if(deviceModel.getImage()!=null) {
			keys.add("Image");
			values.add(deviceModel.getImage());
			varDefDataTypes.add("String");
		}
		if(deviceModel.getDateCreated()!=null) {
			keys.add("DateCreated");
			values.add(deviceModel.getDateCreated());
			varDefDataTypes.add("Timestamp");
		}
		List<VariableDefinition> assetVariablesDefinitions = mindSphereMapper.fiPropertiesToMiVariablesDefinitions(keys, values, varDefDataTypes);
		List<Variable> assetVariables = mindSphereMapper.fiPropertiesToMiVariables(keys, values, varDefDataTypes);
			
		
		List<String> properties = Stream.of("DateModified").collect(Collectors.toList());
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
		AspectType aspectType = mindSphereMapper.fiStateToMiAspectType(deviceModel.getId(), deviceModel.getDescription(), properties, uoms, dataTypes);
		
		
		if(isDebugMode) {
			System.out.println(mindSphereGateway.createAsset(deviceModel.getId(), assetVariablesDefinitions, assetVariables, aspectType));
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
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+deviceModel.getId()+"Asset\"}");
		try {
			List<Timeseries> timeSeriesList = new ArrayList<Timeseries>();
			Date now = new Date();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			String instant = df.format(now);
			Timeseries timeseriesPoint = new Timeseries();
			timeseriesPoint.getFields().put("_time", instant);
			
			if(deviceModel.getDateModified()!=null) {
				timeseriesPoint.getFields().put("DateModified", deviceModel.getDateModified());
			}
			
			timeSeriesList.add(timeseriesPoint);
			mindSphereGateway.putTimeSeries(assets.get(0).getAssetId(), deviceModel.getId()+"AspectType", timeSeriesList);
			logger.debug("DeviceModel updated");
		} catch (Exception e) {
			// Exception handling
			e.printStackTrace();
			return false;
		}
		return true;
	}	
}
