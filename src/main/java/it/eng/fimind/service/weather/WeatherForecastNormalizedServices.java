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
	public Response createDataInJSON(@Valid WeatherForecastNormalized weatherForecast) { 
		ServiceResult serviceResult = new ServiceResult();
		logger.debug("Id ="+weatherForecast.getId());
		
		if(!weatherForecastDoesAlreadyExist(weatherForecast))
			saveMindSphereAsset(createMindSphereAssetFromWeatherForecast(weatherForecast));
		
		createMindSphereTimeSeriesFromWeatherForecast(weatherForecast);
		
		serviceResult.setResult("OK");
		return Response.status(201).entity(serviceResult).build();
	}

	
	private Boolean weatherForecastDoesAlreadyExist(WeatherForecastNormalized weatherForecast)
	{
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+weatherForecast.getId()+"Asset\"}");
		return assets.size()>0;
	}
	
	private Asset createMindSphereAssetFromWeatherForecast(WeatherForecastNormalized weatherForecast) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		MindSphereMapper mindSphereMapper = new MindSphereMapper();
		
		weatherForecast.setId(weatherForecast.getId().replaceAll("-","_"));

		Location mindSphereLocation = null;
		if(weatherForecast.getLocation()!=null) {
			if(weatherForecast.getLocation().getValue().getType().equals("Point")) 
				mindSphereLocation = mindSphereMapper.fiLocationToMiLocation(weatherForecast.getLocation().getValue());
		}else if(weatherForecast.getAddress()!=null) 
			mindSphereLocation = mindSphereMapper.fiAddressToMiLocation(weatherForecast.getAddress().getValue());
		
		
		List<String> keys = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		if(weatherForecast.getDataProvider()!=null) {
			keys.add("DataProvider");
			values.add((String) weatherForecast.getDataProvider().getValue());
		}
		if(weatherForecast.getDateModified()!=null) {
			keys.add("DateModified");		
			values.add((String) weatherForecast.getDateModified().getValue());
		}
		if(weatherForecast.getDateCreated()!=null) {
			keys.add("DateCreated");
			values.add((String) weatherForecast.getDateCreated().getValue());
		}
		if(weatherForecast.getName()!=null) {
			keys.add("Name");
			values.add((String) weatherForecast.getName().getValue());
		}
		if(weatherForecast.getDateRetrieved()!=null) {
			keys.add("DateRetrieved");
			values.add((String) weatherForecast.getDateRetrieved().getValue());
		}
		if(weatherForecast.getDateIssued()!=null) {
			keys.add("DateIssued");
			values.add((String) weatherForecast.getDateIssued().getValue());
		}
		if(weatherForecast.getValidity()!=null) {
			keys.add("Validity");
			values.add((String) weatherForecast.getValidity().getValue());
		}
		if(weatherForecast.getValidFrom()!=null) {
			keys.add("ValidFrom");
			values.add((String) weatherForecast.getValidFrom().getValue());
		}
		if(weatherForecast.getValidTo()!=null) {
			keys.add("ValidTo");
			values.add((String) weatherForecast.getValidTo().getValue());
		}
		if(weatherForecast.getSource()!=null) {
			keys.add("Source");		
			values.add((String) weatherForecast.getSource().getValue());
		}
		if(weatherForecast.getRefPointOfInterest()!=null) {
			keys.add("RefPointOfInterest");
			values.add((String) weatherForecast.getRefPointOfInterest().getValue());
		}
		List<VariableDefinition> assetVariablesDefinitions = mindSphereMapper.fiPropertiesToMiVariablesDefinitions(keys, values);
		List<Variable> assetVariables = mindSphereMapper.fiPropertiesToMiVariables(keys, values);
		
		
		List<String> properties = Stream.of("WeatherType", "Visibility", "Temperature", "FeelsLikeTemperature", "RelativeHumidity", "PrecipitationProbability", "WindDirection", "WindSpeed", "MinTemperature", "MinFeelsLikeTemperature", "MinRelativeHumidity", "MaxTemperature", "MaxFeelsLikeTemperature", "MaxRelativeHumidity").collect(Collectors.toList());
		List<String> uoms = Stream.of("Dimensionless", "Dimensionless", "c°", "c°", "%", "%/100", "°", "m/s", "c°", "c°", "%", "c°", "c°", "%").collect(Collectors.toList());
		List<String> dataTypes = Stream.of("String", "String", "Double", "Double", "Double", "Double", "Double", "Double", "Double", "Double", "Double", "Double", "Double", "Double").collect(Collectors.toList());
		AspectType aspectType = mindSphereMapper.fiStateToMiAspectType(weatherForecast.getId(), "None", properties, uoms, dataTypes);
		
		
		return mindSphereGateway.createAsset(weatherForecast.getId(), mindSphereLocation, assetVariablesDefinitions, assetVariables, aspectType);
	}
		
	private Boolean saveMindSphereAsset(Asset asset) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		Boolean result = mindSphereGateway.saveAsset(asset);
		if(result)
			logger.debug("WeatherForecastNormalized created");
		else 		
			logger.error("WeatherForecastNormalized couldn't be created");
		return result;
	}
	
	private boolean createMindSphereTimeSeriesFromWeatherForecast(WeatherForecastNormalized weatherForecast) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+weatherForecast.getId()+"Asset\"}");
		try {
			List<Timeseries> timeSeriesList = new ArrayList<Timeseries>();
			Date now = new Date();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			String instant = df.format(now);
			Timeseries timeseriesPoint=new Timeseries();
			timeseriesPoint.getFields().put("_time", instant);
		
			if(weatherForecast.getWeatherType()!=null) {
				timeseriesPoint.getFields().put("WeatherType",(String) weatherForecast.getWeatherType().getValue());
			}
			if(weatherForecast.getVisibility()!=null) {
				timeseriesPoint.getFields().put("Visibility",(String) weatherForecast.getVisibility().getValue());
			}
			if(weatherForecast.getTemperature()!=null) {
				timeseriesPoint.getFields().put("Temperature",(Double) weatherForecast.getTemperature().getValue());
			}
			if(weatherForecast.getFeelsLikeTemperature()!=null) {
				timeseriesPoint.getFields().put("FeelsLikeTemperature",(Double) weatherForecast.getFeelsLikeTemperature().getValue());
			}
			if(weatherForecast.getRelativeHumidity()!=null) {
				timeseriesPoint.getFields().put("RelativeHumidity",(Double) weatherForecast.getRelativeHumidity().getValue());
			}
			if(weatherForecast.getPrecipitationProbability()!=null) {
				timeseriesPoint.getFields().put("PrecipitationProbability",(Double) weatherForecast.getPrecipitationProbability().getValue());
			}
			if(weatherForecast.getWindDirection()!=null) {
				timeseriesPoint.getFields().put("WindDirection",(Double) weatherForecast.getWindDirection().getValue());
			}
			if(weatherForecast.getWindSpeed()!=null) {
				timeseriesPoint.getFields().put("WindSpeed",(Double) weatherForecast.getWindSpeed().getValue());
			}
			if(weatherForecast.getDayMinimum()!=null) {
				timeseriesPoint.getFields().put("MinTemperature",(Double) weatherForecast.getDayMinimum().getTemperature().getValue());
				timeseriesPoint.getFields().put("MinFeelsLikeTemperature",(Double) weatherForecast.getDayMinimum().getFeelsLikeTemperature().getValue());
				timeseriesPoint.getFields().put("MinRelativeHumidity",(Double) weatherForecast.getDayMinimum().getRelativeHumidity().getValue());
			}
			if(weatherForecast.getDayMaximum()!=null) {
				timeseriesPoint.getFields().put("MaxTemperature",(Double) weatherForecast.getDayMaximum().getTemperature().getValue());
				timeseriesPoint.getFields().put("MaxFeelsLikeTemperature",(Double) weatherForecast.getDayMaximum().getFeelsLikeTemperature().getValue());
				timeseriesPoint.getFields().put("MaxRelativeHumidity",(Double) weatherForecast.getDayMaximum().getRelativeHumidity().getValue());
			}

			timeSeriesList.add(timeseriesPoint);
			mindSphereGateway.putTimeSeries(assets.get(0).getAssetId(), weatherForecast.getId()+"AspectType", timeSeriesList);
			logger.debug("WeatherForecastNormalized updated");

		} catch (Exception e) {
			// Exception handling
			e.printStackTrace();
			return false;
		}
		return true;
	}	
}
