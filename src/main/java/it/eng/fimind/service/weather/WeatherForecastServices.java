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

import it.eng.fimind.model.fiware.weather.WeatherForecast;
import it.eng.fimind.util.MindSphereGateway;
import it.eng.fimind.util.MindSphereMapper;
import it.eng.fimind.util.ServiceResult;

/**
 * Root resource (exposed at "weatherforecast" path)
 */
@Path("weatherforecast")
public class WeatherForecastServices {
	private static Logger logger = Logger.getLogger(WeatherForecastServices.class);
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getIt() {
		return "WeatherForecast Service: got it!!";
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createDataInJSON(@Valid WeatherForecast weatherForecast) { 
		ServiceResult serviceResult=new ServiceResult();
		logger.debug("Id ="+weatherForecast.getId());
		
		if(!weatherForecastDoesAlreadyExist(weatherForecast))
			saveMindSphereAsset(createMindSphereAssetFromWeatherForecast(weatherForecast));
		
		createMindSphereTimeSeriesFromWeatherForecast(weatherForecast);
		
		serviceResult.setResult("OK");
		return Response.status(201).entity(serviceResult).build();
	}

	
	private Boolean weatherForecastDoesAlreadyExist(WeatherForecast weatherForecast)
	{
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+weatherForecast.getId()+"Asset\"}");
		return assets.size()>0;
	}
	
	private Asset createMindSphereAssetFromWeatherForecast(WeatherForecast weatherForecast) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		MindSphereMapper mindSphereMapper = new MindSphereMapper();
		
		Location mindSphereLocation = null;
		if(weatherForecast.getLocation().getType().equals("Point")) 
			mindSphereLocation = mindSphereMapper.fiLocationToMiLocation(weatherForecast.getLocation());
		else 
			mindSphereLocation = mindSphereMapper.fiAddressToMiLocation(weatherForecast.getAddress());
		
		List<String> keys = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		keys.add("DataProvider");
		values.add(weatherForecast.getDataProvider());
		keys.add("DateModified");
		values.add(weatherForecast.getDateModified());
		keys.add("DateCreated");
		values.add(weatherForecast.getDateCreated());
		keys.add("Name");
		values.add(weatherForecast.getName());
		keys.add("DateRetrieved");
		values.add(weatherForecast.getDateRetrieved());
		keys.add("DateIssued");
		values.add(weatherForecast.getDateIssued());
		keys.add("Validity");
		values.add(weatherForecast.getValidity());
		keys.add("ValidFrom");
		values.add(weatherForecast.getValidFrom());
		keys.add("ValidTo");
		values.add(weatherForecast.getValidTo());
		keys.add("Source");
		values.add(weatherForecast.getSource());
		keys.add("RefPointOfInterest");
		values.add(weatherForecast.getRefPointOfInterest());
		List<Variable> assetVariables = mindSphereMapper.fiPropertiesToMiVariables(keys, values);

		
		List<String> properties = Stream.of("WeatherType", "Visibility", "Temperature", "FeelsLikeTemperature", "RelativeHumidity", "PrecipitationProbability", "WindDirection", "WindSpeed", "MinTemperature", "MinFeelsLikeTemperature", "MinRelativeHumidity", "MaxTemperature", "MaxFeelsLikeTemperature", "MaxRelativeHumidity").collect(Collectors.toList());
		List<String> uoms = Stream.of("Empiric Data", "Empiric Data", "c°", "c°", "%", "%/100", "°", "m/s", "c°", "c°", "%", "c°", "c°", "%").collect(Collectors.toList());
		AspectType aspectType = mindSphereMapper.fiStateToMiAspectType(weatherForecast.getId(), "None", properties, uoms);
		
		
		return mindSphereGateway.createAsset(weatherForecast.getId(), mindSphereLocation, assetVariables, aspectType);
	}
	
	private boolean saveMindSphereAsset(Asset asset) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		logger.debug("WeatherForecast created");
		return mindSphereGateway.saveAsset(asset);
	}
	
	private boolean createMindSphereTimeSeriesFromWeatherForecast(WeatherForecast weatherForecast) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+weatherForecast.getId()+"Asset\"}");
		try {
			List<Timeseries> timeSeriesList = new ArrayList<Timeseries>();
			Date now = new Date();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			String instant = df.format(now);
			Timeseries timeseriesPoint=new Timeseries();
			timeseriesPoint.getFields().put("_time", instant);
		
			timeseriesPoint.getFields().put("WeatherType",weatherForecast.getWeatherType());
			timeseriesPoint.getFields().put("Visibility",weatherForecast.getVisibility());
			timeseriesPoint.getFields().put("Temperature",weatherForecast.getTemperature());
			timeseriesPoint.getFields().put("FeelsLikeTemperature",weatherForecast.getFeelsLikeTemperature());
			timeseriesPoint.getFields().put("RelativeHumidity",weatherForecast.getRelativeHumidity());
			timeseriesPoint.getFields().put("PrecipitationProbability",weatherForecast.getPrecipitationProbability());
			timeseriesPoint.getFields().put("WindDirection",weatherForecast.getWindDirection());
			timeseriesPoint.getFields().put("WindSpeed",weatherForecast.getWindSpeed());
			timeseriesPoint.getFields().put("MinTemperature",weatherForecast.getDayMinimum().getTemperature());
			timeseriesPoint.getFields().put("MinFeelsLikeTemperature",weatherForecast.getDayMinimum().getFeelsLikeTemperature());
			timeseriesPoint.getFields().put("MinRelativeHumidity",weatherForecast.getDayMinimum().getRelativeHumidity());
			timeseriesPoint.getFields().put("MaxTemperature",weatherForecast.getDayMaximum().getTemperature());
			timeseriesPoint.getFields().put("MaxFeelsLikeTemperature",weatherForecast.getDayMaximum().getFeelsLikeTemperature());
			timeseriesPoint.getFields().put("MaxRelativeHumidity",weatherForecast.getDayMaximum().getRelativeHumidity());

			timeSeriesList.add(timeseriesPoint);
			mindSphereGateway.putTimeSeries(assets.get(0).getAssetId(), weatherForecast.getId()+"AspectType", timeSeriesList);
			logger.debug("WeatherForecast updated");

		} catch (Exception e) {
			// Exception handling
			e.printStackTrace();
			return false;
		}
		return true;
	}	
}