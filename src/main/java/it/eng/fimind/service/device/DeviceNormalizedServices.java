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
	public Response createDataInJSON(@Valid DeviceNormalized device) { 
		ServiceResult serviceResult=new ServiceResult();
		logger.debug("Id ="+device.getId());
		
		if(!deviceDoesAlreadyExist(device)) 
			saveMindSphereAsset(createMindSphereAssetFromDevice(device));
		
		createMindSphereTimeSeriesFromDevice(device);
		
		serviceResult.setResult("OK");
		return Response.status(201).entity(serviceResult).build();
	}
	
	private Boolean deviceDoesAlreadyExist(DeviceNormalized device)
	{
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+device.getId()+"Asset\"}");
		return assets.size()>0;
	}
	
	private Asset createMindSphereAssetFromDevice(DeviceNormalized device) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		MindSphereMapper mindSphereMapper = new MindSphereMapper();
		
	
		Location mindSphereLocation = mindSphereMapper.fiLocationToMiLocation(device.getLocation().getValue());

		
		List<String> keys = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		keys.add("Source");
		values.add((String) device.getSource().getValue());
		keys.add("DataProvider");
		values.add((String) device.getDataProvider().getValue());
		keys.add("Category");
		values.add((String) device.getCategory().getValue().toString());
		keys.add("Mnc");
		values.add((String) device.getMnc().getValue());
		keys.add("Mcc");
		values.add((String) device.getMcc().getValue());
		keys.add("MacAddress");
		values.add((String) device.getMacAddress().getValue().toString());
		keys.add("SupportedProtocol");
		values.add((String) device.getSupportedProtocol().getValue().toString());
		keys.add("Configuration");
		values.add((String) device.getConfiguration().getValue());
		keys.add("Name");
		values.add((String) device.getName().getValue());
		keys.add("DateInstalled");
		values.add((String) device.getDateInstalled().getValue());
		keys.add("DateFirstUsed");
		values.add((String) device.getDateFirstUsed().getValue());
		keys.add("DateManufactured");
		values.add((String) device.getDateManufactured().getValue());
		keys.add("HardwareVersion");
		values.add((String) device.getHardwareVersion().getValue());
		keys.add("SoftwareVersion");
		values.add((String) device.getSoftwareVersion().getValue());
		keys.add("FirmwareVersion");
		values.add((String) device.getFirmwareVersion().getValue());
		keys.add("OsVersion");
		values.add((String) device.getOsVersion().getValue());
		keys.add("SerialNumber");
		values.add((String) device.getSerialNumber().getValue());
		keys.add("Provider");
		values.add((String) device.getProvider().getValue());
		keys.add("RefDeviceModel");
		values.add((String) device.getRefDeviceModel().getValue());
		keys.add("DateModified");
		values.add((String) device.getDateModified().getValue());
		keys.add("DateCreated");
		values.add((String) device.getDateCreated().getValue());
		keys.add("Owner");
		values.add((String) device.getOwner().toString());
		List<Variable> assetVariables = mindSphereMapper.fiPropertiesToMiVariables(keys, values);
		
	
		List<String> properties = Stream.of("ControlledAssets","IpAddress", "DateLastCalibration","BatteryLevel","Rssi","DeviceState", "DateLastValueReported").collect(Collectors.toList());
		List<String> uoms = Stream.of("Dimensionless", "Dimensionless","Dimensionless", "ms","%/100","%/100", "Dimensionless", "ms", "Numeric").collect(Collectors.toList());
		AspectType aspectType = mindSphereMapper.fiStateToMiAspectType(device.getId(), (String) device.getDescription().getValue(), properties, uoms);
		
		
		return mindSphereGateway.createAsset(device.getId(), mindSphereLocation, assetVariables, aspectType);
	}
	
	private boolean saveMindSphereAsset(Asset asset) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		logger.debug("DeviceNormalized created");
		return mindSphereGateway.saveAsset(asset);
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
			
			
			timeseriesPoint.getFields().put("ControlledAssets", (String) device.getControlledAsset().getValue().toString());
			timeseriesPoint.getFields().put("IpAddress", (String) device.getIpAddress().getValue().toString());
			timeseriesPoint.getFields().put("DateLastCalibration", (String) device.getDateLastCalibration().getValue());
			timeseriesPoint.getFields().put("BatteryLevel", (String) device.getBatteryLevel().getValue());
			timeseriesPoint.getFields().put("Rssi", (String) device.getRssi().getValue());
			timeseriesPoint.getFields().put("DeviceState", (String) device.getDeviceState().getValue());
			timeseriesPoint.getFields().put("DateLastValueReported", (String) device.getDateLastValueReported().getValue());

			
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