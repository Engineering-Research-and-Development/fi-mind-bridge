package it.eng.fimind.service.device;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.log4j.Logger;

import com.siemens.mindsphere.sdk.assetmanagement.model.AssetResource;
import com.siemens.mindsphere.sdk.timeseries.model.Timeseries;

import it.eng.fimind.model.fiware.device.DeviceNormalized;
import it.eng.fimind.util.MindSphereGateway;
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
		createMindSphereTimeSeriesFromDevice(device);
		serviceResult.setResult("OK");
		return Response.status(201).entity(serviceResult).build();
	}
	
	private boolean createMindSphereTimeSeriesFromDevice(DeviceNormalized device) {
		MindSphereGateway mindSphereGateway=MindSphereGateway.getMindSphereGateway();
	 	List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+device.getId()+"Asset\"}");
	 	try {	
			List<Timeseries> timeSeriesList = new ArrayList<Timeseries>();
			String instant = device.getValue().getMetadata().getTimeInstant().getValue();
			Timeseries timeseriesPoint = new Timeseries();
			timeseriesPoint.getFields().put("_time", instant);
			Pattern pattern = Pattern.compile("[+-]?([0-9]*[.])?[0-9]+");
			Matcher matcher = pattern.matcher((String) device.getValue().getValue());
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
			logger.debug("timeSeriesList ="+timeSeriesList.toString());
			
			mindSphereGateway.putTimeSeries(assets.get(0).getAssetId(), device.getId()+"AspectType", timeSeriesList);
		} catch (Exception e) {
			// Exception handling
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
