package it.eng.fimind.service.device;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
import com.siemens.mindsphere.sdk.assetmanagement.model.Location;
import com.siemens.mindsphere.sdk.assetmanagement.model.Variable;
import com.siemens.mindsphere.sdk.assetmanagement.model.VariableDefinition;
import com.siemens.mindsphere.sdk.timeseries.model.Timeseries;

import it.eng.fimind.model.fiware.device.DeviceNormalized;
import it.eng.fimind.util.MindSphereGateway;
import it.eng.fimind.util.MindSphereMapper;
import it.eng.fimind.util.ServiceResult;

@Path("deviceNormalized")
public class DeviceNormalizedServices {
	private static Logger logger = Logger.getLogger(DeviceNormalizedServices.class);

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getIt() {
		return "Device Normalized Service: got it!!";
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createDataInJSON(@HeaderParam("debug-mode") String debugMode, @Valid DeviceNormalized device) { 
		ServiceResult serviceResult = new ServiceResult();
		logger.debug("Id ="+device.getId());
		
		if(debugMode!=null && debugMode.equals("true")){
			logger.debug("DEBUG MODE FOR --- DeviceNormalized ---");
			createMindSphereAssetFromDevice(device, true);
			serviceResult.setResult("Test gone fine");
			return Response.status(200).entity(serviceResult).build();
		}else {
			Boolean result = true;
			if(!deviceDoesAlreadyExist(device)) 
				result = createMindSphereAssetFromDevice(device, false);
			
			if(result)
				result = createMindSphereTimeSeriesFromDevice(device);
			
			if(result) {
				serviceResult.setResult("DeviceNormalized added succesfully");
				return Response.status(201).entity(serviceResult).build();
			}
			else {
				serviceResult.setResult("Something went wrong, check your FI-MIND logs");
				return Response.status(500).entity(serviceResult).build();
			}
		}
	}
	
	private Boolean deviceDoesAlreadyExist(DeviceNormalized device)
	{
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+device.getId()+"\"}");
		return assets.size()>0;
	}
	
	private Boolean createMindSphereAssetFromDevice(DeviceNormalized device, Boolean isDebugMode) {
		Boolean result = false;
		
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		MindSphereMapper mindSphereMapper = new MindSphereMapper();
			
		Location mindSphereLocation = null;
		if(device.getLocation()!=null && device.getAddress()!=null)
			mindSphereLocation = mindSphereMapper.fiLocAddrToMiLocation(device.getLocation().getValue(), device.getAddress().getValue());
		else if(device.getLocation()!=null)
			mindSphereLocation = mindSphereMapper.fiLocationToMiLocation(device.getLocation().getValue());
		else if(device.getAddress()!=null) 
			mindSphereLocation = mindSphereMapper.fiAddressToMiLocation(device.getAddress().getValue());
		
		
		List<String> keys = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		List<String> varDefDataTypes = new ArrayList<String>();

		if(device.getType()!=null) {
			keys.add("entityType");
			values.add(device.getType());
			varDefDataTypes.add("String");
		}
		if(device.getSource()!=null) {
			keys.add("source");
			values.add((String) device.getSource().getValue());
			varDefDataTypes.add("String");
		}
		if(device.getDataProvider()!=null) {
			keys.add("dataProvider");
			values.add((String) device.getDataProvider().getValue());
			varDefDataTypes.add("String");
		}
		if(device.getCategory()!=null) {
			keys.add("category");
			values.add((String) device.getCategory().getValue().toString());
			varDefDataTypes.add("String");
		}
		if(device.getMnc()!=null) {
			keys.add("mnc");
			values.add((String) device.getMnc().getValue());
			varDefDataTypes.add("String");
		}
		if(device.getMcc()!=null) {
			keys.add("mcc");
			values.add((String) device.getMcc().getValue());
			varDefDataTypes.add("String");
		}
		if(device.getMacAddress()!=null) {
			keys.add("macAddress");
			values.add((String) device.getMacAddress().getValue().toString());
			varDefDataTypes.add("String");
		}
		if(device.getSupportedProtocol()!=null) {
			keys.add("supportedProtocol");
			values.add((String) device.getSupportedProtocol().getValue().toString());
			varDefDataTypes.add("String");
		}
		if(device.getConfiguration()!=null) {
			keys.add("configuration");
			values.add((String) device.getConfiguration().getValue());
			varDefDataTypes.add("String");
		}
		if(device.getName()!=null) {
			keys.add("entityName");
			values.add((String) device.getName().getValue());
			varDefDataTypes.add("String");
		}
		if(device.getDateInstalled()!=null) {
			keys.add("dateInstalled");
			values.add((String) device.getDateInstalled().getValue());
			varDefDataTypes.add("Timestamp");
		}
		if(device.getDateFirstUsed()!=null) {
			keys.add("dateFirstUsed");
			values.add((String) device.getDateFirstUsed().getValue());
			varDefDataTypes.add("Timestamp");
		}
		if(device.getDateManufactured()!=null) {
			keys.add("dateManufactured");
			values.add((String) device.getDateManufactured().getValue());
			varDefDataTypes.add("Timestamp");
		}
		if(device.getSerialNumber()!=null) {
			keys.add("serialNumber");
			values.add((String) device.getSerialNumber().getValue());
			varDefDataTypes.add("String");
		}
		if(device.getProvider()!=null) {
			keys.add("provider");
			values.add((String) device.getProvider().getValue());
			varDefDataTypes.add("String");
		}
		if(device.getRefDeviceModel()!=null) {
			keys.add("refDeviceModel");
			values.add((String) device.getRefDeviceModel().getValue());
			varDefDataTypes.add("String");
		}
		if(device.getDateCreated()!=null) {
			keys.add("dateCreated");
			values.add((String) device.getDateCreated().getValue());
			varDefDataTypes.add("Timestamp");
		}
		if(device.getOwner()!=null) {
			keys.add("owner");
			values.add((String) device.getOwner().toString());
			varDefDataTypes.add("String");
		}
		List<VariableDefinition> assetVariablesDefinitions = mindSphereMapper.fiPropertiesToMiVariablesDefinitions(keys, values, varDefDataTypes);
		List<Variable> assetVariables = mindSphereMapper.fiPropertiesToMiVariables(keys, values, varDefDataTypes);

	
		List<String> properties = Stream.of("controlledAssets", "ipAddress", "hardwareVersion", "softwareVersion", "firmwareVersion", "osVersion", "dateLastCalibration","batteryLevel","rssi","deviceState", "dateLastValueReported", "dateModified").collect(Collectors.toList());
		List<String> uoms = Stream.of("Dimensionless", "Dimensionless", "Dimensionless", "Dimensionless", "Dimensionless", "Dimensionless", "t","%/100","%/100", "Dimensionless", "t", "t").collect(Collectors.toList());
		List<String> dataTypes = Stream.of("String","String", "String","String", "String","String", "Timestamp","Double","Double","String", "Timestamp", "Timestamp").collect(Collectors.toList());
		if(device.getControlledProperty()!=null) {
			for (int i=0; i<device.getControlledProperty().getValue().size(); i++) {
				String property = device.getControlledProperty().getValue().get(i).toString();
				properties.add(property);
				uoms.add("Not Available");
				dataTypes.add("Double");
			}
		}
		AspectType aspectType = mindSphereMapper.fiStateToMiAspectType(device.getId(), (String) device.getDescription().getValue(), properties, uoms, dataTypes);
		
		
		if(isDebugMode) {
			logger.debug(mindSphereGateway.createAsset(device.getId(), mindSphereLocation, assetVariablesDefinitions, assetVariables, aspectType));
			result = true;
		}else {
			result =  mindSphereGateway.saveAsset(device.getId(), mindSphereLocation, assetVariablesDefinitions, assetVariables, aspectType);
			if(result)
				logger.debug("DeviceNormalized created");
			else 		
				logger.error("DeviceNormalized couldn't be created");
		}
		return result;
	}
	
	private boolean createMindSphereTimeSeriesFromDevice(DeviceNormalized device) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+device.getId()+"\"}");
		try {
			List<Timeseries> timeSeriesList = new ArrayList<Timeseries>();
			Date now = new Date();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			String instant = df.format(now);
			Timeseries timeseriesPoint = new Timeseries();
			timeseriesPoint.getFields().put("_time", instant);
					
			if(device.getControlledAsset()!=null) {
				timeseriesPoint.getFields().put("controlledAssets", (String) device.getControlledAsset().getValue().toString());
			}
			if(device.getIpAddress()!=null) {
				timeseriesPoint.getFields().put("ipAddress", (String) device.getIpAddress().getValue().toString());
			}
			if(device.getHardwareVersion()!=null) {
				timeseriesPoint.getFields().put("hardwareVersion", device.getHardwareVersion().getValue());
			}
			if(device.getSoftwareVersion()!=null) {
				timeseriesPoint.getFields().put("softwareVersion", device.getSoftwareVersion().getValue());
			}
			if(device.getFirmwareVersion()!=null) {
				timeseriesPoint.getFields().put("firmwareVersion", device.getFirmwareVersion().getValue());
			}
			if(device.getOsVersion()!=null) {
				timeseriesPoint.getFields().put("osVersion", device.getOsVersion().getValue());
			}
			if(device.getDateLastCalibration()!=null) {
				timeseriesPoint.getFields().put("dateLastCalibration", (String) device.getDateLastCalibration().getValue());
			}
			if(device.getBatteryLevel()!=null) {
				timeseriesPoint.getFields().put("batteryLevel", (String) device.getBatteryLevel().getValue());
			}
			if(device.getRssi()!=null) {
				timeseriesPoint.getFields().put("rssi", (String) device.getRssi().getValue());
			}
			if(device.getDeviceState()!=null) {
				timeseriesPoint.getFields().put("deviceState", (String) device.getDeviceState().getValue());
			}
			if(device.getDateLastValueReported()!=null) {
				timeseriesPoint.getFields().put("dateLastValueReported", (String) device.getDateLastValueReported().getValue());
			}
			if(device.getDateModified()!=null) {
				timeseriesPoint.getFields().put("dateModified", (String) device.getDateModified().getValue());
			}
			if(device.getValue()!=null && device.getControlledProperty()!=null) {
				Pattern pattern = Pattern.compile("[+-]?([0-9]*[.])?[0-9]+");
				Matcher matcher = pattern.matcher(device.getValue().getValue().toString());		
				List<String> values = new ArrayList<String>();
				while (matcher.find()) {
					values.add(matcher.group());
				}
				for (int i=0; i<device.getControlledProperty().getValue().size(); i++) {
					String property = (String) device.getControlledProperty().getValue().get(i);
					String value = values.get(i);
					timeseriesPoint.getFields().put(property,value);
				}
			}
			
			timeSeriesList.add(timeseriesPoint);
			mindSphereGateway.putTimeSeries(assets.get(0).getAssetId(), device.getId(), timeSeriesList);
			logger.debug("DeviceNormalized updated");
		} catch (Exception e) {
			// Exception handling
			e.printStackTrace();
			return false;
		}
		return true;
	}	
}