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

import it.eng.fimind.model.fiware.weather.WeatherForecast;
import it.eng.fimind.util.MindSphereGateway;
import it.eng.fimind.util.MindSphereMapper;
import it.eng.fimind.util.ServiceResult;

/**
 * Root resource (exposed at "weatherforecast" path)
 */
@Path("weatherForecast")
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
	public Response createDataInJSON(@HeaderParam("debug-mode") String debugMode, @Valid WeatherForecast weatherForecast) { 
		ServiceResult serviceResult = new ServiceResult();
		logger.debug("Id ="+weatherForecast.getId());
		
		if(debugMode!=null && debugMode.equals("true")){
			logger.debug("DEBUG MODE FOR --- WeatherForecast ---");
			createMindSphereAssetFromWeatherForecast(weatherForecast, true);
			serviceResult.setResult("Test gone fine");
			return Response.status(200).entity(serviceResult).build();
		}else {
			Boolean result = true;
			if(!weatherForecastDoesAlreadyExist(weatherForecast)) 
				result = createMindSphereAssetFromWeatherForecast(weatherForecast, false);
			
			if(result)
				result = createMindSphereTimeSeriesFromWeatherForecast(weatherForecast);
			
			if(result) {
				serviceResult.setResult("WeatherForecast added succesfully");
				return Response.status(201).entity(serviceResult).build();
			}
			else {
				serviceResult.setResult("Something went wrong, check your FI-MIND logs");
				return Response.status(500).entity(serviceResult).build();
			}
		}
	}

	
	private Boolean weatherForecastDoesAlreadyExist(WeatherForecast weatherForecast)
	{
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+weatherForecast.getId()+"\"}");
		return assets.size()>0;
	}
	
	public Boolean createMindSphereAssetFromWeatherForecast(WeatherForecast weatherForecast, Boolean isDebugMode) {
		Boolean result = false;
		
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		MindSphereMapper mindSphereMapper = new MindSphereMapper();
		
		Location mindSphereLocation = null;
		if(weatherForecast.getLocation()!=null && weatherForecast.getAddress()!=null)
			mindSphereLocation = mindSphereMapper.fiLocAddrToMiLocation(weatherForecast.getLocation(), weatherForecast.getAddress());
		else if(weatherForecast.getLocation()!=null)
			mindSphereLocation = mindSphereMapper.fiLocationToMiLocation(weatherForecast.getLocation());
		else if(weatherForecast.getAddress()!=null) 
			mindSphereLocation = mindSphereMapper.fiAddressToMiLocation(weatherForecast.getAddress());
		
		
		List<String> keys = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		List<String> varDefDataTypes = new ArrayList<String>();
		
		if(weatherForecast.getType()!=null) {
			keys.add("entityType");
			values.add(weatherForecast.getType());
			varDefDataTypes.add("String");
		}
		if(weatherForecast.getDataProvider()!=null) {
			keys.add("dataProvider");
			values.add(weatherForecast.getDataProvider());
			varDefDataTypes.add("String");
		}
		if(weatherForecast.getDateCreated()!=null) {
			keys.add("dateCreated");
			values.add(weatherForecast.getDateCreated());
			varDefDataTypes.add("Timestamp");
		}
		if(weatherForecast.getName()!=null) {
			keys.add("entityName");
			values.add(weatherForecast.getName());
			varDefDataTypes.add("String");
		}
		if(weatherForecast.getSource()!=null) {
			keys.add("source");		
			values.add(weatherForecast.getSource());
			varDefDataTypes.add("String");
		}
		if(weatherForecast.getRefPointOfInterest()!=null) {
			keys.add("refPointOfInterest");
			values.add(weatherForecast.getRefPointOfInterest());
			varDefDataTypes.add("String");
		}
		List<VariableDefinition> assetVariablesDefinitions = mindSphereMapper.fiPropertiesToMiVariablesDefinitions(keys, values, varDefDataTypes);
		List<Variable> assetVariables = mindSphereMapper.fiPropertiesToMiVariables(keys, values, varDefDataTypes);
		
		
		List<String> properties = Stream.of("dateModified", "dateRetrieved", "dateIssued", "validity", "validFrom", "validTo", "weatherType", "visibility", "temperature", "feelsLikeTemperature", "relativeHumidity", "precipitationProbability", "windDirection", "windSpeed", "minTemperature", "minFeelsLikeTemperature", "minRelativeHumidity", "maxTemperature", "maxFeelsLikeTemperature", "maxRelativeHumidity").collect(Collectors.toList());
		List<String> uoms = Stream.of("t", "t", "t", "t", "t", "t", "Dimensionless", "Dimensionless", "c°", "c°", "%", "%/100", "°", "m/s", "c°", "c°", "%", "c°", "c°", "%").collect(Collectors.toList());
		List<String> dataTypes = Stream.of("Timestamp", "Timestamp", "Timestamp", "String", "Timestamp", "Timestamp", "String", "String", "Double", "Double", "Double", "Double", "Double", "Double", "Double", "Double", "Double", "Double", "Double", "Double").collect(Collectors.toList());
		AspectType aspectType = mindSphereMapper.fiStateToMiAspectType(weatherForecast.getId(), "None", properties, uoms, dataTypes);
		

		if(isDebugMode) {
			logger.debug(mindSphereGateway.createAsset(weatherForecast.getId(), mindSphereLocation, assetVariablesDefinitions, assetVariables, aspectType));
			result = true;
		}else {
			result = mindSphereGateway.saveAsset(weatherForecast.getId(), mindSphereLocation, assetVariablesDefinitions, assetVariables, aspectType);
			if(result)
				logger.debug("WeatherForecast created");
			else 		
				logger.error("WeatherForecast couldn't be created");
		}
		return result;
	}
	
	public boolean createMindSphereTimeSeriesFromWeatherForecast(WeatherForecast weatherForecast) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+weatherForecast.getId()+"\"}");
		try {
			List<Timeseries> timeSeriesList = new ArrayList<Timeseries>();
			Date now = new Date();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			String instant = df.format(now);
			Timeseries timeseriesPoint = new Timeseries();
			timeseriesPoint.getFields().put("_time", instant);
		
			if(weatherForecast.getDateModified()!=null) {
				timeseriesPoint.getFields().put("dateModified",weatherForecast.getDateModified());
			}
			if(weatherForecast.getDateRetrieved()!=null) {
				timeseriesPoint.getFields().put("dateRetrieved",weatherForecast.getDateRetrieved());
			}
			if(weatherForecast.getDateIssued()!=null) {
				timeseriesPoint.getFields().put("dateIssued",weatherForecast.getDateIssued());
			}
			if(weatherForecast.getValidity()!=null) {				
				timeseriesPoint.getFields().put("validity",weatherForecast.getValidity());
			}
			if(weatherForecast.getValidFrom()!=null) {
				timeseriesPoint.getFields().put("validFrom",weatherForecast.getValidFrom());
			}
			if(weatherForecast.getValidTo()!=null) {
				timeseriesPoint.getFields().put("validTo",weatherForecast.getValidTo());
			}
			if(weatherForecast.getWeatherType()!=null) {
				timeseriesPoint.getFields().put("weatherType",weatherForecast.getWeatherType());
			}
			if(weatherForecast.getVisibility()!=null) {
				timeseriesPoint.getFields().put("visibility",weatherForecast.getVisibility());
			}
			if(weatherForecast.getTemperature()!=null) {
				timeseriesPoint.getFields().put("temperature",weatherForecast.getTemperature());
			}
			if(weatherForecast.getFeelsLikeTemperature()!=null) {
				timeseriesPoint.getFields().put("feelsLikeTemperature",weatherForecast.getFeelsLikeTemperature());
			}
			if(weatherForecast.getRelativeHumidity()!=null) {
				timeseriesPoint.getFields().put("relativeHumidity",weatherForecast.getRelativeHumidity());
			}
			if(weatherForecast.getPrecipitationProbability()!=null) {
				timeseriesPoint.getFields().put("precipitationProbability",weatherForecast.getPrecipitationProbability());
			}
			if(weatherForecast.getWindDirection()!=null) {
				timeseriesPoint.getFields().put("windDirection",weatherForecast.getWindDirection());
			}
			if(weatherForecast.getWindSpeed()!=null) {
				timeseriesPoint.getFields().put("windSpeed",weatherForecast.getWindSpeed());
			}
			if(weatherForecast.getDayMinimum()!=null) {
				timeseriesPoint.getFields().put("minTemperature",weatherForecast.getDayMinimum().getTemperature());
				timeseriesPoint.getFields().put("minFeelsLikeTemperature",weatherForecast.getDayMinimum().getFeelsLikeTemperature());
				timeseriesPoint.getFields().put("minRelativeHumidity",weatherForecast.getDayMinimum().getRelativeHumidity());
			}
			if(weatherForecast.getDayMaximum()!=null) {
				timeseriesPoint.getFields().put("maxTemperature",weatherForecast.getDayMaximum().getTemperature());
				timeseriesPoint.getFields().put("maxFeelsLikeTemperature",weatherForecast.getDayMaximum().getFeelsLikeTemperature());
				timeseriesPoint.getFields().put("maxRelativeHumidity",weatherForecast.getDayMaximum().getRelativeHumidity());
			}

			timeSeriesList.add(timeseriesPoint);
			mindSphereGateway.putTimeSeries(assets.get(0).getAssetId(), weatherForecast.getId(), timeSeriesList);
			logger.debug("WeatherForecast updated");

		} catch (Exception e) {
			// Exception handling
			e.printStackTrace();
			return false;
		}
		return true;
	}	
}