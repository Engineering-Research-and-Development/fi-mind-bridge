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
			System.out.println("DEBUG MODE FOR --- WeatherForecast ---");
			createMindSphereAssetFromWeatherForecast(weatherForecast, true);
			serviceResult.setResult("Test gone fine");
			return Response.status(200).entity(serviceResult).build();
		}else {
			Boolean result = false;
			if(!weatherForecastDoesAlreadyExist(weatherForecast)) 
				result = createMindSphereAssetFromWeatherForecast(weatherForecast, false);
			
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
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+weatherForecast.getId()+"Asset\"}");
		return assets.size()>0;
	}
	
	public Boolean createMindSphereAssetFromWeatherForecast(WeatherForecast weatherForecast, Boolean isDebugMode) {
		Boolean result = false;
		
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		MindSphereMapper mindSphereMapper = new MindSphereMapper();
		
		weatherForecast.setId(weatherForecast.getId().replaceAll("-","_"));

		Location mindSphereLocation = null;
		if(weatherForecast.getLocation()!=null) {
			if(weatherForecast.getLocation().getType().equals("Point")) 
				mindSphereLocation = mindSphereMapper.fiLocationToMiLocation(weatherForecast.getLocation());
		}else if(weatherForecast.getAddress()!=null) 
			mindSphereLocation = mindSphereMapper.fiAddressToMiLocation(weatherForecast.getAddress());
		
		List<String> keys = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		List<String> varDefDataTypes = new ArrayList<String>();
		
		if(weatherForecast.getDataProvider()!=null) {
			keys.add("DataProvider");
			values.add(weatherForecast.getDataProvider());
			varDefDataTypes.add("String");
		}
		if(weatherForecast.getDateModified()!=null) {
			keys.add("DateModified");		
			values.add(weatherForecast.getDateModified());
			varDefDataTypes.add("Timestamp");
		}
		if(weatherForecast.getDateCreated()!=null) {
			keys.add("DateCreated");
			values.add(weatherForecast.getDateCreated());
			varDefDataTypes.add("Timestamp");
		}
		if(weatherForecast.getName()!=null) {
			keys.add("Name");
			values.add(weatherForecast.getName());
			varDefDataTypes.add("String");
		}
		if(weatherForecast.getSource()!=null) {
			keys.add("Source");		
			values.add(weatherForecast.getSource());
			varDefDataTypes.add("String");
		}
		if(weatherForecast.getRefPointOfInterest()!=null) {
			keys.add("RefPointOfInterest");
			values.add(weatherForecast.getRefPointOfInterest());
			varDefDataTypes.add("String");
		}
		List<VariableDefinition> assetVariablesDefinitions = mindSphereMapper.fiPropertiesToMiVariablesDefinitions(keys, values, varDefDataTypes);
		List<Variable> assetVariables = mindSphereMapper.fiPropertiesToMiVariables(keys, values, varDefDataTypes);
		
		
		List<String> properties = Stream.of("DateRetrieved", "DateIssued", "Validity", "ValidFrom", "ValidTo", "WeatherType", "Visibility", "Temperature", "FeelsLikeTemperature", "RelativeHumidity", "PrecipitationProbability", "WindDirection", "WindSpeed", "MinTemperature", "MinFeelsLikeTemperature", "MinRelativeHumidity", "MaxTemperature", "MaxFeelsLikeTemperature", "MaxRelativeHumidity").collect(Collectors.toList());
		List<String> uoms = Stream.of("t", "t", "t", "t", "t", "Dimensionless", "Dimensionless", "c°", "c°", "%", "%/100", "°", "m/s", "c°", "c°", "%", "c°", "c°", "%").collect(Collectors.toList());
		List<String> dataTypes = Stream.of("Timestamp", "Timestamp", "Timestamp", "Timestamp", "Timestamp", "String", "String", "Double", "Double", "Double", "Double", "Double", "Double", "Double", "Double", "Double", "Double", "Double", "Double").collect(Collectors.toList());
		AspectType aspectType = mindSphereMapper.fiStateToMiAspectType(weatherForecast.getId(), "None", properties, uoms, dataTypes);
		

		if(isDebugMode) {
			System.out.println(mindSphereGateway.createAsset(weatherForecast.getId(), mindSphereLocation, assetVariablesDefinitions, assetVariables, aspectType));
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
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+weatherForecast.getId()+"Asset\"}");
		try {
			List<Timeseries> timeSeriesList = new ArrayList<Timeseries>();
			Date now = new Date();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			String instant = df.format(now);
			Timeseries timeseriesPoint = new Timeseries();
			timeseriesPoint.getFields().put("_time", instant);
		
			if(weatherForecast.getDateRetrieved()!=null) {
				timeseriesPoint.getFields().put("DateRetrieved",weatherForecast.getDateRetrieved());
			}
			if(weatherForecast.getDateIssued()!=null) {
				timeseriesPoint.getFields().put("DateIssued",weatherForecast.getDateIssued());
			}
			if(weatherForecast.getValidity()!=null) {				
				timeseriesPoint.getFields().put("Validity",weatherForecast.getValidity());
			}
			if(weatherForecast.getValidFrom()!=null) {
				timeseriesPoint.getFields().put("ValidFrom",weatherForecast.getValidFrom());
			}
			if(weatherForecast.getValidTo()!=null) {
				timeseriesPoint.getFields().put("ValidTo",weatherForecast.getValidTo());
			}
			if(weatherForecast.getWeatherType()!=null) {
				timeseriesPoint.getFields().put("WeatherType",weatherForecast.getWeatherType());
			}
			if(weatherForecast.getVisibility()!=null) {
				timeseriesPoint.getFields().put("Visibility",weatherForecast.getVisibility());
			}
			if(weatherForecast.getTemperature()!=null) {
				timeseriesPoint.getFields().put("Temperature",weatherForecast.getTemperature());
			}
			if(weatherForecast.getFeelsLikeTemperature()!=null) {
				timeseriesPoint.getFields().put("FeelsLikeTemperature",weatherForecast.getFeelsLikeTemperature());
			}
			if(weatherForecast.getRelativeHumidity()!=null) {
				timeseriesPoint.getFields().put("RelativeHumidity",weatherForecast.getRelativeHumidity());
			}
			if(weatherForecast.getPrecipitationProbability()!=null) {
				timeseriesPoint.getFields().put("PrecipitationProbability",weatherForecast.getPrecipitationProbability());
			}
			if(weatherForecast.getWindDirection()!=null) {
				timeseriesPoint.getFields().put("WindDirection",weatherForecast.getWindDirection());
			}
			if(weatherForecast.getWindSpeed()!=null) {
				timeseriesPoint.getFields().put("WindSpeed",weatherForecast.getWindSpeed());
			}
			if(weatherForecast.getDayMinimum()!=null) {
				timeseriesPoint.getFields().put("MinTemperature",weatherForecast.getDayMinimum().getTemperature());
				timeseriesPoint.getFields().put("MinFeelsLikeTemperature",weatherForecast.getDayMinimum().getFeelsLikeTemperature());
				timeseriesPoint.getFields().put("MinRelativeHumidity",weatherForecast.getDayMinimum().getRelativeHumidity());
			}
			if(weatherForecast.getDayMaximum()!=null) {
				timeseriesPoint.getFields().put("MaxTemperature",weatherForecast.getDayMaximum().getTemperature());
				timeseriesPoint.getFields().put("MaxFeelsLikeTemperature",weatherForecast.getDayMaximum().getFeelsLikeTemperature());
				timeseriesPoint.getFields().put("MaxRelativeHumidity",weatherForecast.getDayMaximum().getRelativeHumidity());
			}

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