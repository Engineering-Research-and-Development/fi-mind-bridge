package it.eng.fimind.service.device;

import java.time.Instant;
import java.util.ArrayList;
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

import it.eng.fimind.model.fiware.device.DeviceModelNormalized;
import it.eng.fimind.util.MindSphereGateway;
import it.eng.fimind.util.MindSphereMapper;
import it.eng.fimind.util.ServiceResult;

@Path("deviceModelNormalized")
public class DeviceModelNormalizedServices {

	private static Logger logger = Logger.getLogger(DeviceModelNormalizedServices.class);

	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getIt() {
		return "Device Model Service: got it!!";
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createDataInJSON(@HeaderParam("debug-mode") String debugMode, @Valid DeviceModelNormalized deviceModel) { 
		logger.debug("[DeviceModelNormalizedServices] POST Request");
		ServiceResult serviceResult = new ServiceResult();
		
		logger.debug("Id ="+deviceModel.getId());
		
		if(debugMode!=null && debugMode.equals("true")){
			logger.debug("DEBUG MODE FOR --- DeviceModelNormalized ---");
			createMindSphereAssetFromDeviceModel(deviceModel, true);
			serviceResult.setResult("Test gone fine");
			return Response.status(200).entity(serviceResult).build();
		}else {
			Boolean result = true;
			if(!deviceModelDoesAlreadyExist(deviceModel)) 
				result = createMindSphereAssetFromDeviceModel(deviceModel, false);
			
			if(result)
				result = createMindSphereTimeSeriesFromDeviceModel(deviceModel);		
			
			if(result) {
				serviceResult.setResult("DeviceModelNormalized added succesfully");
				return Response.status(201).entity(serviceResult).build();
			}
			else {
				serviceResult.setResult("Something went wrong, check your FI-MIND logs");
				return Response.status(500).entity(serviceResult).build();
			}
		}
	}
	
	private Boolean deviceModelDoesAlreadyExist(DeviceModelNormalized deviceModel)
	{
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+deviceModel.getId()+"\"}");
		return assets.size()>0;
	}
	
	private Boolean createMindSphereAssetFromDeviceModel(DeviceModelNormalized deviceModel, Boolean isDebugMode) {
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
			values.add((String)deviceModel.getSource().getValue());
			varDefDataTypes.add("String");
		}
		if(deviceModel.getDataProvider()!=null) {
			keys.add("dataProvider");
			values.add((String)deviceModel.getDataProvider().getValue());
			varDefDataTypes.add("String");
		}
		if(deviceModel.getCategory()!=null) {
			keys.add("category");
			values.add((String)deviceModel.getCategory().getValue().toString());
			varDefDataTypes.add("String");
		}
		if(deviceModel.getDeviceClass()!=null) {
			keys.add("deviceClass");
			values.add((String)deviceModel.getDeviceClass().getValue());
			varDefDataTypes.add("String");
		}
		if(deviceModel.getFunction()!=null) {
			keys.add("function");
			values.add((String)deviceModel.getFunction().getValue().toString());
			varDefDataTypes.add("String");
		}
		if(deviceModel.getSupportedProtocol()!=null) {
			keys.add("supportedProtocol");
			values.add((String)deviceModel.getSupportedProtocol().getValue().toString());
			varDefDataTypes.add("String");
		}
		if(deviceModel.getSupportedUnits()!=null) {
			keys.add("supportedUnits");
			values.add((String)deviceModel.getSupportedUnits().getValue().toString());
			varDefDataTypes.add("String");
		}
		if(deviceModel.getEnergyLimitationClass()!=null) {
			keys.add("energyLimitationClass");		
			values.add((String)deviceModel.getEnergyLimitationClass().getValue());
			varDefDataTypes.add("String");
		}
		if(deviceModel.getBrandName()!=null) {
			keys.add("brandName");
			values.add((String)deviceModel.getBrandName().getValue());
			varDefDataTypes.add("String");
		}
		if(deviceModel.getModelName()!=null) {
			keys.add("modelName");
			values.add((String)deviceModel.getModelName().getValue());
			varDefDataTypes.add("String");
		}
		if(deviceModel.getManufacturerName()!=null) {
			keys.add("manufacturerName");
			values.add((String)deviceModel.getManufacturerName().getValue());
			varDefDataTypes.add("String");
		}
		if(deviceModel.getName()!=null) {
			keys.add("entityName");
			values.add((String)deviceModel.getName().getValue());
			varDefDataTypes.add("String");
		}
		if(deviceModel.getDocumentation()!=null) {
			keys.add("documentation");
			values.add((String)deviceModel.getDocumentation().getValue());
			varDefDataTypes.add("String");
		}
		if(deviceModel.getImage()!=null) {
			keys.add("image");
			values.add((String)deviceModel.getImage().getValue());
			varDefDataTypes.add("String");
		}
		if(deviceModel.getDateCreated()!=null) {
			keys.add("dateCreated");
			values.add((String) deviceModel.getDateCreated().getValue());
			varDefDataTypes.add("Timestamp");
		}
		List<VariableDefinition> assetVariablesDefinitions = mindSphereMapper.fiPropertiesToMiVariablesDefinitions(keys, values, varDefDataTypes);
		List<Variable> assetVariables = mindSphereMapper.fiPropertiesToMiVariables(keys, values, varDefDataTypes);

	
		List<String> properties = Stream.of("dateModified").collect(Collectors.toList());
		List<String> uoms = Stream.of("t").collect(Collectors.toList());
		List<String> dataTypes = Stream.of("Timestamp").collect(Collectors.toList());
		if(deviceModel.getControlledProperty()!=null) {
			for (int i=0; i<deviceModel.getControlledProperty().getValue().size(); i++) {
				String property = (String) deviceModel.getControlledProperty().getValue().get(i);
				String uom = (String) deviceModel.getSupportedUnits().getValue().get(i);
				properties.add(property);
				uoms.add(uom);
				dataTypes.add("Double");
			}
		}
		AspectType aspectType;
		if(deviceModel.getDescription()!=null)
			aspectType = mindSphereMapper.fiStateToMiAspectType(deviceModel.getId(), (String) deviceModel.getDescription().getValue(), properties, uoms, dataTypes);
		else
			aspectType = mindSphereMapper.fiStateToMiAspectType(deviceModel.getId(), properties, uoms, dataTypes);
			

		if(isDebugMode) {
			logger.debug(mindSphereGateway.createAsset(deviceModel.getId(), assetVariablesDefinitions, assetVariables, aspectType));
			result = true;
		}else {
			result =  mindSphereGateway.saveAsset(deviceModel.getId(), assetVariablesDefinitions, assetVariables, aspectType);
			if(result)
				logger.debug("DeviceModelNormalized created");
			else 		
				logger.error("DeviceModelNormalized couldn't be created");
		}
		return result;
	}
	
	public boolean createMindSphereTimeSeriesFromDeviceModel(DeviceModelNormalized deviceModel) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+deviceModel.getId()+"\"}");
		try {
			List<Timeseries> timeSeriesList = new ArrayList<Timeseries>();

			Timeseries timeseriesPoint = new Timeseries();
			
			timeseriesPoint.getFields().put("_time", Instant.now().toString());
			
			if(deviceModel.getDateModified()!=null) {
				timeseriesPoint.getFields().put("dateModified", (String) deviceModel.getDateModified().getValue());
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
