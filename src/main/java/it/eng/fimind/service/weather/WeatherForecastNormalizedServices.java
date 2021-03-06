package it.eng.fimind.service.weather;

import java.time.Instant;
import java.util.ArrayList;
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

import it.eng.fimind.model.fiware.weather.WeatherForecastNormalized;
import it.eng.fimind.util.MindSphereGateway;
import it.eng.fimind.util.MindSphereMapper;
import it.eng.fimind.util.ServiceResult;

/**
 * Root resource (exposed at "weatherforecastnormalized" path)
 */
@Path("weatherForecastNormalized")
public class WeatherForecastNormalizedServices {
	private static Logger logger = Logger.getLogger(WeatherForecastNormalizedServices.class);
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getIt() {
		return "WeatherForecastNormalized Service: got it!!";
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createDataInJSON(@HeaderParam("debug-mode") String debugMode, @Valid WeatherForecastNormalized weatherForecast) { 
		logger.debug("[WeatherForecastNormalizedServices] POST Request");
		ServiceResult serviceResult = new ServiceResult();
		
		logger.debug("Id ="+weatherForecast.getId());
		
		if(debugMode!=null && debugMode.equals("true")){
			logger.debug("DEBUG MODE FOR --- WeatherForecastNormalized ---");
			createMindSphereAssetFromWeatherForecast(weatherForecast, true);
			serviceResult.setResult("Test gone fine");
			return Response.status(200).entity(serviceResult).build();
		}else {
			Boolean result = true;
			MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
			if(!mindSphereGateway.assetDoesAlreadyExist(weatherForecast.getId()))
				result = createMindSphereAssetFromWeatherForecast(weatherForecast, false);
			
			if(result)
				result = createMindSphereTimeSeriesFromWeatherForecast(weatherForecast);
			
			if(result) {
				serviceResult.setResult("WeatherForecastNormalized added succesfully");
				return Response.status(201).entity(serviceResult).build();
			}
			else {
				serviceResult.setResult("Something went wrong, check your FI-MIND logs");
				return Response.status(500).entity(serviceResult).build();
			}
		}
	}
	
	private Boolean createMindSphereAssetFromWeatherForecast(WeatherForecastNormalized weatherForecast, Boolean isDebugMode) {
		Boolean result = false;
		
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		MindSphereMapper mindSphereMapper = new MindSphereMapper();
		
		Location mindSphereLocation = null;
		if(weatherForecast.getLocation()!=null && weatherForecast.getAddress()!=null)
			mindSphereLocation = mindSphereMapper.fiLocAddrToMiLocation(weatherForecast.getLocation().getValue(), weatherForecast.getAddress().getValue());
		else if(weatherForecast.getLocation()!=null)
			mindSphereLocation = mindSphereMapper.fiLocationToMiLocation(weatherForecast.getLocation().getValue());
		else if(weatherForecast.getAddress()!=null) 
			mindSphereLocation = mindSphereMapper.fiAddressToMiLocation(weatherForecast.getAddress().getValue());
	
		
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
			values.add((String) weatherForecast.getDataProvider().getValue());
			varDefDataTypes.add("String");
		}
		if(weatherForecast.getDateCreated()!=null) {
			keys.add("dateCreated");
			values.add((String) weatherForecast.getDateCreated().getValue());
			varDefDataTypes.add("Timestamp");
		}
		if(weatherForecast.getName()!=null) {
			keys.add("entityName");
			values.add((String) weatherForecast.getName().getValue());
			varDefDataTypes.add("String");
		}
		if(weatherForecast.getValidity()!=null) {
			keys.add("validity");
			values.add((String) weatherForecast.getValidity().getValue());
			varDefDataTypes.add("String");
		}
		if(weatherForecast.getValidFrom()!=null) {
			keys.add("validFrom");
			values.add((String) weatherForecast.getValidFrom().getValue());
			varDefDataTypes.add("Timestamp");
		}
		if(weatherForecast.getValidTo()!=null) {
			keys.add("validTo");
			values.add((String) weatherForecast.getValidTo().getValue());
			varDefDataTypes.add("Timestamp");
		}
		if(weatherForecast.getSource()!=null) {
			keys.add("source");		
			values.add((String) weatherForecast.getSource().getValue());
			varDefDataTypes.add("String");
		}
		if(weatherForecast.getRefPointOfInterest()!=null) {
			keys.add("refPointOfInterest");
			values.add((String) weatherForecast.getRefPointOfInterest().getValue());
			varDefDataTypes.add("String");
		}
		List<VariableDefinition> assetVariablesDefinitions = mindSphereMapper.fiPropertiesToMiVariablesDefinitions(keys, values, varDefDataTypes);
		List<Variable> assetVariables = mindSphereMapper.fiPropertiesToMiVariables(keys, values, varDefDataTypes);
		
		
		List<String> properties = Stream.of("dateModified", "dateRetrieved", "dateIssued", "validity", "validFrom", "validTo", "weatherType", "visibility", "temperature", "feelsLikeTemperature", "relativeHumidity", "precipitationProbability", "windDirection", "windSpeed", "minTemperature", "minFeelsLikeTemperature", "minRelativeHumidity", "maxTemperature", "maxFeelsLikeTemperature", "maxRelativeHumidity").collect(Collectors.toList());
		List<String> uoms = Stream.of("t", "t", "t", "t", "t", "t", "Dimensionless", "Dimensionless", "c°", "c°", "%", "%/100", "°", "m/s", "c°", "c°", "%", "c°", "c°", "%").collect(Collectors.toList());
		List<String> dataTypes = Stream.of("Timestamp", "Timestamp", "Timestamp", "String", "Timestamp", "Timestamp", "String", "String", "Double", "Double", "Double", "Double", "Double", "Double", "Double", "Double", "Double", "Double", "Double", "Double").collect(Collectors.toList());
		AspectType aspectType = mindSphereMapper.fiStateToMiAspectType(weatherForecast.getId(), properties, uoms, dataTypes);
		
		
		if(isDebugMode) {
			logger.debug(mindSphereGateway.createAsset(weatherForecast.getId(), mindSphereLocation, assetVariablesDefinitions, assetVariables, aspectType));
			result = true;
		}else {
			result = mindSphereGateway.saveAsset(weatherForecast.getId(), mindSphereLocation, assetVariablesDefinitions, assetVariables, aspectType);
			if(result)
				logger.debug("WeatherForecastNormalized created");
			else 		
				logger.error("WeatherForecastNormalized couldn't be created");
		}
		return result;
	}
	
	private boolean createMindSphereTimeSeriesFromWeatherForecast(WeatherForecastNormalized weatherForecast) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+weatherForecast.getId()+"\"}");
		try {
			List<Timeseries> timeSeriesList = new ArrayList<Timeseries>();

			Timeseries timeseriesPoint = new Timeseries();
			
			timeseriesPoint.getFields().put("_time", Instant.now().toString());
		
			if(weatherForecast.getDateModified()!=null) {
				timeseriesPoint.getFields().put("dateModified",(String) weatherForecast.getDateModified().getValue());
			}
			if(weatherForecast.getDateRetrieved()!=null) {
				timeseriesPoint.getFields().put("dateRetrieved",(String) weatherForecast.getDateRetrieved().getValue());
			}
			if(weatherForecast.getDateIssued()!=null) {
				timeseriesPoint.getFields().put("dateIssued",(String) weatherForecast.getDateIssued().getValue());
			}
			if(weatherForecast.getValidity()!=null) {				
				timeseriesPoint.getFields().put("validity",(String) weatherForecast.getValidity().getValue());
			}
			if(weatherForecast.getValidFrom()!=null) {
				timeseriesPoint.getFields().put("validFrom",(String) weatherForecast.getValidFrom().getValue());
			}
			if(weatherForecast.getValidTo()!=null) {
				timeseriesPoint.getFields().put("validTo",(String) weatherForecast.getValidTo().getValue());
			}
			if(weatherForecast.getWeatherType()!=null) {
				timeseriesPoint.getFields().put("weatherType",(String) weatherForecast.getWeatherType().getValue());
			}
			if(weatherForecast.getVisibility()!=null) {
				timeseriesPoint.getFields().put("visibility",(String) weatherForecast.getVisibility().getValue());
			}
			if(weatherForecast.getTemperature()!=null) {
				timeseriesPoint.getFields().put("temperature", Double.valueOf(weatherForecast.getTemperature().getValue().toString()));
			}
			if(weatherForecast.getFeelsLikeTemperature()!=null) {
				timeseriesPoint.getFields().put("feelsLikeTemperature", Double.valueOf(weatherForecast.getFeelsLikeTemperature().getValue().toString()));
			}
			if(weatherForecast.getRelativeHumidity()!=null) {
				timeseriesPoint.getFields().put("relativeHumidity", Double.valueOf(weatherForecast.getRelativeHumidity().getValue().toString()));
			}
			if(weatherForecast.getPrecipitationProbability()!=null) {
				timeseriesPoint.getFields().put("precipitationProbability", Double.valueOf(weatherForecast.getPrecipitationProbability().getValue().toString()));
			}
			if(weatherForecast.getWindDirection()!=null) {
				timeseriesPoint.getFields().put("windDirection", Double.valueOf(weatherForecast.getWindDirection().getValue().toString()));
			}
			if(weatherForecast.getWindSpeed()!=null) {
				timeseriesPoint.getFields().put("windSpeed", Double.valueOf(weatherForecast.getWindSpeed().getValue().toString()));
			}
			if(weatherForecast.getDayMinimum()!=null) {
				timeseriesPoint.getFields().put("minTemperature", Double.valueOf(weatherForecast.getDayMinimum().getValue().getTemperature().toString()));
				timeseriesPoint.getFields().put("minFeelsLikeTemperature", Double.valueOf(weatherForecast.getDayMinimum().getValue().getFeelsLikeTemperature().toString()));
				timeseriesPoint.getFields().put("minRelativeHumidity", Double.valueOf(weatherForecast.getDayMinimum().getValue().getRelativeHumidity().toString()));
			}
			if(weatherForecast.getDayMaximum()!=null) {
				timeseriesPoint.getFields().put("maxTemperature", Double.valueOf(weatherForecast.getDayMaximum().getValue().getTemperature().toString()));
				timeseriesPoint.getFields().put("maxFeelsLikeTemperature", Double.valueOf(weatherForecast.getDayMaximum().getValue().getFeelsLikeTemperature().toString()));
				timeseriesPoint.getFields().put("maxRelativeHumidity", Double.valueOf(weatherForecast.getDayMaximum().getValue().getRelativeHumidity().toString()));
			}

			timeSeriesList.add(timeseriesPoint);
			mindSphereGateway.putTimeSeries(assets.get(0).getAssetId(), weatherForecast.getId(), timeSeriesList);
			logger.debug("WeatherForecastNormalized updated");

		} catch (Exception e) {
			// Exception handling
			e.printStackTrace();
			return false;
		}
		return true;
	}	
}
