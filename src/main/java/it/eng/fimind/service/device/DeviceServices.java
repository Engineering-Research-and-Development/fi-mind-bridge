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

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
//import com.siemens.mindsphere.sdk.core.exception.MindsphereException;
//import com.siemens.mindsphere.sdk.iot.asset.model.AspectTypeDto;
//import com.siemens.mindsphere.sdk.iot.asset.model.AspectVariable;
import com.siemens.mindsphere.sdk.iot.asset.model.Assets;
//import com.siemens.mindsphere.sdk.iot.asset.model.CategoryEnum;
//import com.siemens.mindsphere.sdk.iot.asset.model.DataTypeEnum;
//import com.siemens.mindsphere.sdk.iot.asset.model.ScopeEnum;
import com.siemens.mindsphere.sdk.iot.timeseries.model.TimeseriesData;

import it.eng.fimind.model.fiware.device.Device;
import it.eng.fimind.util.MindSphereGateway;
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
		createMindSphereTimeSeriesFromDevice(device);
		serviceResult.setResult("OK");
		return Response.status(201).entity(serviceResult).build();
	}
	
	private boolean createMindSphereTimeSeriesFromDevice(Device device) {
		MindSphereGateway mindSphereGateway=MindSphereGateway.getMindSphereGateway();
		Assets assets=mindSphereGateway.getFilteredAssets("ASC", "%7B%22name%22%3A%22"+device.getId()+"Asset%22%7D");
		try {
			List<TimeseriesData> timeSeriesList=new ArrayList<TimeseriesData>();
			Date now=new Date();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			String instant = df.format(now);
			TimeseriesData timeseriesPoint=new TimeseriesData();
			timeseriesPoint.getData().put("_time", instant);
			Pattern pattern = Pattern.compile("[+-]?([0-9]*[.])?[0-9]+");
			Matcher matcher = pattern.matcher(device.getValue());
			List<String> values=new ArrayList<String>();
			while (matcher.find()) {
				values.add(matcher.group());
			}
			for (int i=0; i<device.getControlledProperty().size(); i++) {
				String property=device.getControlledProperty().get(i);
				String value=values.get(i);
				timeseriesPoint.getData().put(property,value);
			}
			timeSeriesList.add(timeseriesPoint);
			mindSphereGateway.createTimeseries(assets.getEmbedded().getAssets().get(0).getAssetId(), device.getId()+"AspectType", timeSeriesList, true);
		} catch (Exception e) {
			// Exception handling
			e.printStackTrace();
			return false;
		}
		return true;
	}	
}
