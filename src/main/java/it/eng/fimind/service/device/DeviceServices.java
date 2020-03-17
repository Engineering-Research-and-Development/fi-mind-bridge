package it.eng.fimind.service.device;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
//import java.util.Date;
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
		
	
		Location mindSphereLocation = mindSphereMapper.fiLocationToMiLocation(device.getLocation());

		
		List<String> keys = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		keys.add("Source");
		values.add(device.getSource());
		keys.add("DataProvider");
		values.add(device.getDataProvider());
		keys.add("Category");
		values.add(device.getCategory().toString());
		keys.add("Mnc");
		values.add(device.getMnc());
		keys.add("Mcc");
		values.add(device.getMcc());
		keys.add("MacAddress");
		values.add(device.getMacAddress().toString());
		keys.add("SupportedProtocol");
		values.add(device.getSupportedProtocol().toString());
		keys.add("Configuration");
		values.add(device.getConfiguration());
		keys.add("Name");
		values.add(device.getName());
		keys.add("DateInstalled");
		values.add(device.getDateInstalled());
		keys.add("DateFirstUsed");
		values.add(device.getDateFirstUsed());
		keys.add("DateManufactured");
		values.add(device.getDateManufactured());
		keys.add("HardwareVersion");
		values.add(device.getHardwareVersion());
		keys.add("SoftwareVersion");
		values.add(device.getSoftwareVersion());
		keys.add("FirmwareVersion");
		values.add(device.getFirmwareVersion());
		keys.add("OsVersion");
		values.add(device.getOsVersion());
		keys.add("SerialNumber");
		values.add(device.getSerialNumber());
		keys.add("Provider");
		values.add(device.getProvider());
		keys.add("RefDeviceModel");
		values.add(device.getRefDeviceModel());
		keys.add("DateModified");
		values.add(device.getDateModified());
		keys.add("DateCreated");
		values.add(device.getDateCreated());
		keys.add("Owner");
		values.add(device.getOwner().toString());
		List<Variable> assetVariables = mindSphereMapper.fiPropertiesToMiVariables(keys, values);
		
	
		List<String> properties = Stream.of("ControlledAssets","IpAddress", "DateLastCalibration","BatteryLevel","Rssi","DeviceState", "DateLastValueReported").collect(Collectors.toList());
		List<String> uoms = Stream.of("Dimensionless", "Dimensionless","Dimensionless", "ms","%/100","%/100", "Dimensionless", "ms", "Numeric").collect(Collectors.toList());
		for (int i=0; i<device.getControlledProperty().size(); i++) {
			String property = device.getControlledProperty().get(i);
			String uom = "Undefined";
			properties.add(property);
			uoms.add(uom);
		}
		AspectType aspectType = mindSphereMapper.fiStateToMiAspectType(device.getId(), device.getDescription(), properties, uoms);
		
		
		return mindSphereGateway.createAsset(device.getId(), mindSphereLocation, assetVariables, aspectType);
	}
	
	private boolean saveMindSphereAsset(Asset asset) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		logger.debug("Device created");
		return mindSphereGateway.saveAsset(asset);
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
			
			
			timeseriesPoint.getFields().put("ControlledAssets", device.getControlledAsset().toString());
			timeseriesPoint.getFields().put("IpAddress", device.getIpAddress());
			timeseriesPoint.getFields().put("DateLastCalibration", device.getDateLastCalibration());
			timeseriesPoint.getFields().put("BatteryLevel", device.getBatteryLevel());
			timeseriesPoint.getFields().put("Rssi", device.getRssi());
			timeseriesPoint.getFields().put("DeviceState", device.getDeviceState());
			timeseriesPoint.getFields().put("DateLastValueReported", device.getDateLastValueReported());

			
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
