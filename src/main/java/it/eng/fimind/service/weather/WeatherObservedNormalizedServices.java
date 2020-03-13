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

import it.eng.fimind.model.fiware.weather.WeatherObservedNormalized;
import it.eng.fimind.util.MindSphereGateway;
import it.eng.fimind.util.MindSphereMapper;
import it.eng.fimind.util.ServiceResult;

/**
 * Root resource (exposed at "weatherobservednormalized" path)
 */
@Path("weatherobservednormalized")
public class WeatherObservedNormalizedServices {
	private static Logger logger = Logger.getLogger(WeatherObservedNormalizedServices.class);
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getIt() {
		return "WeatherObservedNormalized Service: got it!!";
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createDataInJSON(@Valid WeatherObservedNormalized weatherObserved) { 
		ServiceResult serviceResult=new ServiceResult();
		logger.debug("Id ="+weatherObserved.getId());
		
		if(!weatherObservedDoesAlreadyExist(weatherObserved))
			saveMindSphereAsset(createMindSphereAssetFromWeatherObserved(weatherObserved));
		
		createMindSphereTimeSeriesFromWeatherObserved(weatherObserved);
		
		serviceResult.setResult("OK");
		return Response.status(201).entity(serviceResult).build();
	}

	
	private Boolean weatherObservedDoesAlreadyExist(WeatherObservedNormalized weatherObserved)
	{
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+weatherObserved.getId()+"Asset\"}");
		return assets.size()>0;
	}
	
	private Asset createMindSphereAssetFromWeatherObserved(WeatherObservedNormalized weatherObserved) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		MindSphereMapper mindSphereMapper = new MindSphereMapper();
		
		Location mindSphereLocation = null;
		if(weatherObserved.getLocation().getType().equals("Point")) 
			mindSphereLocation = mindSphereMapper.fiLocationToMiLocation(weatherObserved.getLocation().getValue());
		else 
			mindSphereLocation = mindSphereMapper.fiAddressToMiLocation(weatherObserved.getAddress().getValue());
		
		List<String> keys = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		keys.add("DataProvider");
		values.add((String) weatherObserved.getDataProvider().getValue());
		keys.add("DateModified");
		values.add((String) weatherObserved.getDateModified().getValue());
		keys.add("DateCreated");
		values.add((String) weatherObserved.getDateCreated().getValue());
		keys.add("Name");
		values.add((String) weatherObserved.getName().getValue());
		keys.add("DateObserved");
		values.add((String) weatherObserved.getDateObserved().getValue());
		keys.add("Source");
		values.add((String) weatherObserved.getSource().getValue());
		keys.add("RefDevice");
		values.add((String) weatherObserved.getRefDevice().getValue());
		keys.add("RefPointOfInterest");
		values.add((String) weatherObserved.getRefPointOfInterest().getValue());
		List<Variable> assetVariables = mindSphereMapper.fiPropertiesToMiVariables(keys, values);

		
		List<String> properties = Stream.of("WeatherType", "DewPoint", "Visibility", "Temperature", "RelativeHumidity", "Precipitation", "WindDirection", "WindSpeed", "AtmosphericPressure", "PressureTendency", "SolarRadiation", "Illuminance", "StreamGauge", "SnowHeight").collect(Collectors.toList());
		List<String> uoms = Stream.of("Empiric Data", "c°", "Empiric Data", "c°", "%", "l/m2", "°", "m/s", "hPa", "Empiric Data", "W/m2", "lux", "cm", "cm").collect(Collectors.toList());
		AspectType aspectType = mindSphereMapper.fiStateToMiAspectType(weatherObserved.getId(), "None", properties, uoms);
		
		
		return mindSphereGateway.createAsset(weatherObserved.getId(), mindSphereLocation, assetVariables, aspectType);
	}
	
	private boolean saveMindSphereAsset(Asset asset) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		logger.debug("WeatherObservedNormalized created");
		return mindSphereGateway.saveAsset(asset);
	}
	
	private boolean createMindSphereTimeSeriesFromWeatherObserved(WeatherObservedNormalized weatherObserved) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+weatherObserved.getId()+"Asset\"}");
		try {
			List<Timeseries> timeSeriesList = new ArrayList<Timeseries>();
			Date now = new Date();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			String instant = df.format(now);
			Timeseries timeseriesPoint=new Timeseries();
			timeseriesPoint.getFields().put("_time", instant);
		
			timeseriesPoint.getFields().put("WeatherType",(String) weatherObserved.getWeatherType().getValue());
			timeseriesPoint.getFields().put("DewPoint",(Double) weatherObserved.getDewPoint().getValue());
			timeseriesPoint.getFields().put("Visibility",(String) weatherObserved.getVisibility().getValue());
			timeseriesPoint.getFields().put("Temperature",(Double) weatherObserved.getTemperature().getValue());
			timeseriesPoint.getFields().put("RelativeHumidity",(Double) weatherObserved.getRelativeHumidity().getValue());
			timeseriesPoint.getFields().put("Precipitation",(Double) weatherObserved.getPrecipitation().getValue());
			timeseriesPoint.getFields().put("WindDirection",(Double) weatherObserved.getWindDirection().getValue());
			timeseriesPoint.getFields().put("WindSpeed",(Double) weatherObserved.getWindSpeed().getValue());
			timeseriesPoint.getFields().put("AtmosphericPressure",(Double) weatherObserved.getAtmosphericPressure().getValue());
			timeseriesPoint.getFields().put("PressureTendency",(String) weatherObserved.getPressureTendency().getValue());
			timeseriesPoint.getFields().put("SolarRadiation",(Double) weatherObserved.getSolarRadiation().getValue());
			timeseriesPoint.getFields().put("Illuminance",(Double) weatherObserved.getIlluminance().getValue());
			timeseriesPoint.getFields().put("StreamGauge",(Double) weatherObserved.getStreamGauge().getValue());
			timeseriesPoint.getFields().put("SnowHeight",(Double) weatherObserved.getSnowHeight().getValue());

			timeSeriesList.add(timeseriesPoint);
			mindSphereGateway.putTimeSeries(assets.get(0).getAssetId(), weatherObserved.getId()+"AspectType", timeSeriesList);
			logger.debug("WeatherObservedNormalized updated");

		} catch (Exception e) {
			// Exception handling
			e.printStackTrace();
			return false;
		}
		return true;
	}	
}