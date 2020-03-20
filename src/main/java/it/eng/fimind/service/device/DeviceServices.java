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
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.siemens.mindsphere.sdk.assetmanagement.model.AspectType;
import com.siemens.mindsphere.sdk.assetmanagement.model.Asset;
import com.siemens.mindsphere.sdk.assetmanagement.model.AssetResource;
import com.siemens.mindsphere.sdk.assetmanagement.model.Location;
import com.siemens.mindsphere.sdk.assetmanagement.model.Variable;
import com.siemens.mindsphere.sdk.assetmanagement.model.VariableDefinition;
import com.siemens.mindsphere.sdk.timeseries.model.Timeseries;

import it.eng.fimind.model.fiware.device.Device;
import it.eng.fimind.util.MindSphereGateway;
import it.eng.fimind.util.MindSphereMapper;
import it.eng.fimind.util.ServiceResult;

/**
 * Root resource (exposed at "device" path)
 */
@Path("device")
public class DeviceServices {
	private static Logger logger = Logger.getLogger(DeviceServices.class);
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getIt() {
		return "Device Service: got it!!";
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createDataInJSON(@Valid Device device) { 
		ServiceResult serviceResult=new ServiceResult();
		logger.debug("Id ="+device.getId());
		
		if(!deviceDoesAlreadyExist(device)) 
			saveMindSphereAsset(createMindSphereAssetFromDevice(device));
		
		createMindSphereTimeSeriesFromDevice(device);
		
		serviceResult.setResult("OK");
		return Response.status(201).entity(serviceResult).build();
	}
	
	private Boolean deviceDoesAlreadyExist(Device device)
	{
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+device.getId()+"Asset\"}");
		return assets.size()>0;
	}
	
	public Asset createMindSphereAssetFromDevice(Device device) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		MindSphereMapper mindSphereMapper = new MindSphereMapper();
		
		device.setId(device.getId().replaceAll("-","_"));

		Location mindSphereLocation = null;
		if(device.getLocation()!=null) {
			if(device.getLocation().getType().equals("Point")) 
				mindSphereLocation = mindSphereMapper.fiLocationToMiLocation(device.getLocation());
		}else if(device.getAddress()!=null) 
			mindSphereLocation = mindSphereMapper.fiAddressToMiLocation(device.getAddress());
		
		
		List<String> keys = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		if(device.getSource()!=null) {
			keys.add("Source");
			values.add(device.getSource());
		}
		if(device.getDataProvider()!=null) {
			keys.add("DataProvider");
			values.add(device.getDataProvider());
		}
		if(device.getCategory()!=null) {
			keys.add("Category");
			values.add(device.getCategory().toString());
		}
		if(device.getMnc()!=null) {
			keys.add("Mnc");
			values.add(device.getMnc());
		}
		if(device.getMcc()!=null) {
			keys.add("Mcc");
			values.add(device.getMcc());
		}
		if(device.getMacAddress()!=null) {
			keys.add("MacAddress");
			values.add(device.getMacAddress().toString());
		}
		if(device.getSupportedProtocol()!=null) {
			keys.add("SupportedProtocol");
			values.add(device.getSupportedProtocol().toString());
		}
		if(device.getConfiguration()!=null) {
			keys.add("Configuration");
			values.add(device.getConfiguration());
		}
		if(device.getName()!=null) {
			keys.add("Name");
			values.add(device.getName());
		}
		if(device.getDateInstalled()!=null) {
			keys.add("DateInstalled");
			values.add(device.getDateInstalled());
		}
		if(device.getDateFirstUsed()!=null) {
			keys.add("DateFirstUsed");
			values.add(device.getDateFirstUsed());
		}
		if(device.getDateManufactured()!=null) {
			keys.add("DateManufactured");
			values.add(device.getDateManufactured());
		}
		if(device.getHardwareVersion()!=null) {
			keys.add("HardwareVersion");
			values.add(device.getHardwareVersion());
		}
		if(device.getSoftwareVersion()!=null) {
			keys.add("SoftwareVersion");
			values.add(device.getSoftwareVersion());
		}
		if(device.getFirmwareVersion()!=null) {
			keys.add("FirmwareVersion");
			values.add(device.getFirmwareVersion());
		}
		if(device.getOsVersion()!=null) {
			keys.add("OsVersion");
			values.add(device.getOsVersion());
		}
		if(device.getSerialNumber()!=null) {
			keys.add("SerialNumber");
			values.add(device.getSerialNumber());
		}
		if(device.getProvider()!=null) {
			keys.add("Provider");
			values.add(device.getProvider());
		}
		if(device.getRefDeviceModel()!=null) {
			keys.add("RefDeviceModel");
			values.add(device.getRefDeviceModel());
		}
		if(device.getDateModified()!=null) {
			keys.add("DateModified");
			values.add(device.getDateModified());
		}
		if(device.getDateCreated()!=null) {
			keys.add("DateCreated");
			values.add(device.getDateCreated());
		}
		if(device.getOwner()!=null) {
			keys.add("Owner");
			values.add(device.getOwner().toString());
		}
		List<VariableDefinition> assetVariablesDefinitions = mindSphereMapper.fiPropertiesToMiVariablesDefinitions(keys, values);
		List<Variable> assetVariables = mindSphereMapper.fiPropertiesToMiVariables(keys, values);

	
		List<String> properties = Stream.of("ControlledAssets","IpAddress", "DateLastCalibration","BatteryLevel","Rssi","DeviceState", "DateLastValueReported").collect(Collectors.toList());
		List<String> uoms = Stream.of("Dimensionless", "Dimensionless","Dimensionless", "ms","%/100","%/100", "Dimensionless", "ms", "Numeric").collect(Collectors.toList());
		List<String> dataTypes = Stream.of("String","String", "String","Double","Double","String", "String").collect(Collectors.toList());
		for (int i=0; i<device.getControlledProperty().size(); i++) {
			String property = device.getControlledProperty().get(i);
			String uom = "Undefined";
			properties.add(property);
			uoms.add(uom);
			dataTypes.add("Double");
		}
		AspectType aspectType = mindSphereMapper.fiStateToMiAspectType(device.getId(), device.getDescription(), properties, uoms, dataTypes);
		
		
		return mindSphereGateway.createAsset(device.getId(), mindSphereLocation, assetVariablesDefinitions, assetVariables, aspectType);
	}
	
	private Boolean saveMindSphereAsset(Asset asset) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		Boolean result = mindSphereGateway.saveAsset(asset);
		if(result)
			logger.debug("Device created");
		else 		
			logger.error("Device couldn't be created");
		return result;
	}
	
	public boolean createMindSphereTimeSeriesFromDevice(Device device) {
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
				timeseriesPoint.getFields().put("ControlledAssets", device.getControlledAsset().toString());
			}
			if(device.getIpAddress()!=null) {
				timeseriesPoint.getFields().put("IpAddress", device.getIpAddress());
			}
			if(device.getDateLastCalibration()!=null) {
				timeseriesPoint.getFields().put("DateLastCalibration", device.getDateLastCalibration());
			}
			if(device.getBatteryLevel()!=null) {
				timeseriesPoint.getFields().put("BatteryLevel", device.getBatteryLevel());
			}
			if(device.getRssi()!=null) {
				timeseriesPoint.getFields().put("Rssi", device.getRssi());
			}
			if(device.getDeviceState()!=null) {
				timeseriesPoint.getFields().put("DeviceState", device.getDeviceState());
			}
			if(device.getDateLastValueReported()!=null) {
				timeseriesPoint.getFields().put("DateLastValueReported", device.getDateLastValueReported());
			}
			
			if(device.getValue()!=null && device.getControlledProperty()!=null) {
				Pattern pattern = Pattern.compile("[+-]?([0-9]*[.])?[0-9]+");
				Matcher matcher = pattern.matcher(device.getValue());		
				List<String> values = new ArrayList<String>();
				while (matcher.find()) {
					values.add(matcher.group());
				}
				for (int i=0; i<device.getControlledProperty().size(); i++) {
					String property = device.getControlledProperty().get(i);
					String value = values.get(i);
					timeseriesPoint.getFields().put(property,value);
				}
			}
			
			timeSeriesList.add(timeseriesPoint);
			mindSphereGateway.putTimeSeries(assets.get(0).getAssetId(), device.getId()+"AspectType", timeSeriesList);
		} catch (Exception e) {
			// Exception handling
			e.printStackTrace();
			return false;
		}
		return true;
	}	
}
