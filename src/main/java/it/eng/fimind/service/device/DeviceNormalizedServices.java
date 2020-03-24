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
			System.out.println("DEBUG MODE FOR --- DeviceNormalized ---");
			createMindSphereAssetFromDevice(device, true);
			serviceResult.setResult("Test gone fine");
			return Response.status(200).entity(serviceResult).build();
		}else {
			Boolean result = false;
			if(!deviceDoesAlreadyExist(device)) 
				result = createMindSphereAssetFromDevice(device, false);
			
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
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+device.getId()+"Asset\"}");
		return assets.size()>0;
	}
	
	private Boolean createMindSphereAssetFromDevice(DeviceNormalized device, Boolean isDebugMode) {
		Boolean result = false;
		
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		MindSphereMapper mindSphereMapper = new MindSphereMapper();
		
		device.setId(device.getId().replaceAll("-","_"));
	
		Location mindSphereLocation = null;
		if(device.getLocation()!=null) {
			if(device.getLocation().getValue().getType().equals("Point")) 
				mindSphereLocation = mindSphereMapper.fiLocationToMiLocation(device.getLocation().getValue());
		}else if(device.getAddress()!=null) 
			mindSphereLocation = mindSphereMapper.fiAddressToMiLocation(device.getAddress().getValue());
		
		
		List<String> keys = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		List<String> varDefDataTypes = new ArrayList<String>();

		if(device.getSource()!=null) {
			keys.add("Source");
			values.add((String) device.getSource().getValue());
			varDefDataTypes.add("String");
		}
		if(device.getDataProvider()!=null) {
			keys.add("DataProvider");
			values.add((String) device.getDataProvider().getValue());
			varDefDataTypes.add("String");
		}
		if(device.getCategory()!=null) {
			keys.add("Category");
			values.add((String) device.getCategory().getValue().toString());
			varDefDataTypes.add("String");
		}
		if(device.getMnc()!=null) {
			keys.add("Mnc");
			values.add((String) device.getMnc().getValue());
			varDefDataTypes.add("String");
		}
		if(device.getMcc()!=null) {
			keys.add("Mcc");
			values.add((String) device.getMcc().getValue());
			varDefDataTypes.add("String");
		}
		if(device.getMacAddress()!=null) {
			keys.add("MacAddress");
			values.add((String) device.getMacAddress().getValue().toString());
			varDefDataTypes.add("String");
		}
		if(device.getSupportedProtocol()!=null) {
			keys.add("SupportedProtocol");
			values.add((String) device.getSupportedProtocol().getValue().toString());
			varDefDataTypes.add("String");
		}
		if(device.getConfiguration()!=null) {
			keys.add("Configuration");
			values.add((String) device.getConfiguration().getValue());
			varDefDataTypes.add("String");
		}
		if(device.getName()!=null) {
			keys.add("Name");
			values.add((String) device.getName().getValue());
			varDefDataTypes.add("String");
		}
		if(device.getDateInstalled()!=null) {
			keys.add("DateInstalled");
			values.add((String) device.getDateInstalled().getValue());
			varDefDataTypes.add("Timestamp");
		}
		if(device.getDateFirstUsed()!=null) {
			keys.add("DateFirstUsed");
			values.add((String) device.getDateFirstUsed().getValue());
			varDefDataTypes.add("Timestamp");
		}
		if(device.getDateManufactured()!=null) {
			keys.add("DateManufactured");
			values.add((String) device.getDateManufactured().getValue());
			varDefDataTypes.add("Timestamp");
		}
		if(device.getHardwareVersion()!=null) {
			keys.add("HardwareVersion");
			values.add((String) device.getHardwareVersion().getValue());
			varDefDataTypes.add("String");
		}
		if(device.getSoftwareVersion()!=null) {
			keys.add("SoftwareVersion");
			values.add((String) device.getSoftwareVersion().getValue());
			varDefDataTypes.add("String");
		}
		if(device.getFirmwareVersion()!=null) {
			keys.add("FirmwareVersion");
			values.add((String) device.getFirmwareVersion().getValue());
			varDefDataTypes.add("String");
		}
		if(device.getOsVersion()!=null) {
			keys.add("OsVersion");
			values.add((String) device.getOsVersion().getValue());
			varDefDataTypes.add("String");
		}
		if(device.getSerialNumber()!=null) {
			keys.add("SerialNumber");
			values.add((String) device.getSerialNumber().getValue());
			varDefDataTypes.add("String");
		}
		if(device.getProvider()!=null) {
			keys.add("Provider");
			values.add((String) device.getProvider().getValue());
			varDefDataTypes.add("String");
		}
		if(device.getRefDeviceModel()!=null) {
			keys.add("RefDeviceModel");
			values.add((String) device.getRefDeviceModel().getValue());
			varDefDataTypes.add("String");
		}
		if(device.getDateModified()!=null) {
			keys.add("DateModified");
			values.add((String) device.getDateModified().getValue());
			varDefDataTypes.add("Timestamp");
		}
		if(device.getDateCreated()!=null) {
			keys.add("DateCreated");
			values.add((String) device.getDateCreated().getValue());
			varDefDataTypes.add("Timestamp");
		}
		if(device.getOwner()!=null) {
			keys.add("Owner");
			values.add((String) device.getOwner().toString());
			varDefDataTypes.add("String");
		}
		List<VariableDefinition> assetVariablesDefinitions = mindSphereMapper.fiPropertiesToMiVariablesDefinitions(keys, values, varDefDataTypes);
		List<Variable> assetVariables = mindSphereMapper.fiPropertiesToMiVariables(keys, values, varDefDataTypes);

	
		List<String> properties = Stream.of("ControlledAssets","IpAddress", "DateLastCalibration","BatteryLevel","Rssi","DeviceState", "DateLastValueReported").collect(Collectors.toList());
		List<String> uoms = Stream.of("Dimensionless", "Dimensionless","Dimensionless", "ms","%/100","%/100", "Dimensionless", "ms", "Numeric").collect(Collectors.toList());
		List<String> dataTypes = Stream.of("String","String", "String","Double","Double","String", "String").collect(Collectors.toList());
		for (int i=0; i<device.getControlledProperty().getValue().size(); i++) {
			String property = device.getControlledProperty().getValue().get(i).toString();
			String uom = "Undefined";
			properties.add(property);
			uoms.add(uom);
			dataTypes.add("Double");
		}
		AspectType aspectType = mindSphereMapper.fiStateToMiAspectType(device.getId(), (String) device.getDescription().getValue(), properties, uoms, dataTypes);
		
		
		if(isDebugMode) {
			System.out.println(mindSphereGateway.createAsset(device.getId(), mindSphereLocation, assetVariablesDefinitions, assetVariables, aspectType));
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
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+device.getId()+"Asset\"}");
		try {
			List<Timeseries> timeSeriesList = new ArrayList<Timeseries>();
			Date now = new Date();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			String instant = df.format(now);
			Timeseries timeseriesPoint = new Timeseries();
			timeseriesPoint.getFields().put("_time", instant);
					
			if(device.getControlledAsset()!=null) {
				timeseriesPoint.getFields().put("ControlledAssets", (String) device.getControlledAsset().getValue().toString());
			}
			if(device.getIpAddress()!=null) {
				timeseriesPoint.getFields().put("IpAddress", (String) device.getIpAddress().getValue().toString());
			}
			if(device.getDateLastCalibration()!=null) {
				timeseriesPoint.getFields().put("DateLastCalibration", (String) device.getDateLastCalibration().getValue());
			}
			if(device.getBatteryLevel()!=null) {
				timeseriesPoint.getFields().put("BatteryLevel", (String) device.getBatteryLevel().getValue());
			}
			if(device.getRssi()!=null) {
				timeseriesPoint.getFields().put("Rssi", (String) device.getRssi().getValue());
			}
			if(device.getDeviceState()!=null) {
				timeseriesPoint.getFields().put("DeviceState", (String) device.getDeviceState().getValue());
			}
			if(device.getDateLastValueReported()!=null) {
				timeseriesPoint.getFields().put("DateLastValueReported", (String) device.getDateLastValueReported().getValue());
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
			mindSphereGateway.putTimeSeries(assets.get(0).getAssetId(), device.getId()+"AspectType", timeSeriesList);
			logger.debug("DeviceNormalized updated");
		} catch (Exception e) {
			// Exception handling
			e.printStackTrace();
			return false;
		}
		return true;
	}	
}