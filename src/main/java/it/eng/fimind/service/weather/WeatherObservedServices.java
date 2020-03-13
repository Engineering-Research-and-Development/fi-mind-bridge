package it.eng.fimind.service.weather;

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

import it.eng.fimind.model.fiware.weather.WeatherObserved;
import it.eng.fimind.util.MindSphereGateway;
import it.eng.fimind.util.MindSphereMapper;
import it.eng.fimind.util.ServiceResult;

/**
 * Root resource (exposed at "weatherobserved" path)
 */
@Path("weatherobserved")
public class WeatherObservedServices {
	private static Logger logger = Logger.getLogger(WeatherObservedServices.class);
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getIt() {
		return "WeatherObserved Service: got it!!";
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createDataInJSON(@Valid WeatherObserved weatherObserved) { 
		ServiceResult serviceResult = new ServiceResult();
		logger.debug("Id ="+weatherObserved.getId());
		
		if(!weatherObservedDoesAlreadyExist(weatherObserved))
			saveMindSphereAsset(createMindSphereAssetFromWeatherObserved(weatherObserved));
		
		
		createMindSphereTimeSeriesFromWeatherObserved(weatherObserved);
		
		serviceResult.setResult("OK");
		return Response.status(201).entity(serviceResult).build();
	}

	
	private Boolean weatherObservedDoesAlreadyExist(WeatherObserved weatherObserved)
	{
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+weatherObserved.getId()+"Asset\"}");
		return assets.size()>0;
	}
	
	private Asset createMindSphereAssetFromWeatherObserved(WeatherObserved weatherObserved) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		MindSphereMapper mindSphereMapper = new MindSphereMapper();
		
		Location mindSphereLocation = null;
		if(weatherObserved.getLocation().getType().equals("Point")) 
			mindSphereLocation = mindSphereMapper.fiLocationToMiLocation(weatherObserved.getLocation());
		else 
			mindSphereLocation = mindSphereMapper.fiAddressToMiLocation(weatherObserved.getAddress());
		
		List<String> keys = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		keys.add("DataProvider");
		values.add(weatherObserved.getDataProvider());
		keys.add("DateModified");
		values.add(weatherObserved.getDateModified());
		keys.add("DateCreated");
		values.add(weatherObserved.getDateCreated());
		keys.add("Name");
		values.add(weatherObserved.getName());
		keys.add("DateObserved");
		values.add(weatherObserved.getDateObserved());
		keys.add("Source");
		values.add(weatherObserved.getSource());
		keys.add("RefDevice");
		values.add(weatherObserved.getRefDevice());
		keys.add("RefPointOfInterest");
		values.add(weatherObserved.getRefPointOfInterest());
		List<Variable> assetVariables = mindSphereMapper.fiPropertiesToMiVariables(keys, values);

		
		List<String> properties = Stream.of("WeatherType", "DewPoint", "Visibility", "Temperature", "RelativeHumidity", "Precipitation", "WindDirection", "WindSpeed", "AtmosphericPressure", "PressureTendency", "SolarRadiation", "Illuminance", "StreamGauge", "SnowHeight").collect(Collectors.toList());
		List<String> uoms = Stream.of("Empiric Data", "c°", "Empiric Data", "c°", "%", "l/m2", "°", "m/s", "hPa", "Empiric Data", "W/m2", "lux", "cm", "cm").collect(Collectors.toList());
		AspectType aspectType = mindSphereMapper.fiStateToMiAspectType(weatherObserved.getId(), "None", properties, uoms);
		
		
		return mindSphereGateway.createAsset(weatherObserved.getId(), mindSphereLocation, assetVariables, aspectType);
	}
	
	private boolean saveMindSphereAsset(Asset asset) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		logger.debug("WeatherObserved created");
		return mindSphereGateway.saveAsset(asset);
	}
	
	private boolean createMindSphereTimeSeriesFromWeatherObserved(WeatherObserved weatherObserved) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+weatherObserved.getId()+"Asset\"}");
		try {
			List<Timeseries> timeSeriesList = new ArrayList<Timeseries>();
			Date now = new Date();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			String instant = df.format(now);
			Timeseries timeseriesPoint=new Timeseries();
			timeseriesPoint.getFields().put("_time", instant);
		
			timeseriesPoint.getFields().put("WeatherType",weatherObserved.getWeatherType());
			timeseriesPoint.getFields().put("DewPoint",weatherObserved.getDewPoint());
			timeseriesPoint.getFields().put("Visibility",weatherObserved.getVisibility());
			timeseriesPoint.getFields().put("Temperature",weatherObserved.getTemperature());
			timeseriesPoint.getFields().put("RelativeHumidity",weatherObserved.getRelativeHumidity());
			timeseriesPoint.getFields().put("Precipitation",weatherObserved.getPrecipitation());
			timeseriesPoint.getFields().put("WindDirection",weatherObserved.getWindDirection());
			timeseriesPoint.getFields().put("WindSpeed",weatherObserved.getWindSpeed());
			timeseriesPoint.getFields().put("AtmosphericPressure",weatherObserved.getAtmosphericPressure());
			timeseriesPoint.getFields().put("PressureTendency",weatherObserved.getPressureTendency());
			timeseriesPoint.getFields().put("SolarRadiation",weatherObserved.getSolarRadiation());
			timeseriesPoint.getFields().put("Illuminance",weatherObserved.getIlluminance());
			timeseriesPoint.getFields().put("StreamGauge",weatherObserved.getStreamGauge());
			timeseriesPoint.getFields().put("SnowHeight",weatherObserved.getSnowHeight());

			timeSeriesList.add(timeseriesPoint);
			mindSphereGateway.putTimeSeries(assets.get(0).getAssetId(), weatherObserved.getId()+"AspectType", timeSeriesList);
			logger.debug("WeatherObserved updated");

		} catch (Exception e) {
			// Exception handling
			e.printStackTrace();
			return false;
		}
		return true;
	}	
}