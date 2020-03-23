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
import com.siemens.mindsphere.sdk.assetmanagement.model.AssetResource;
import com.siemens.mindsphere.sdk.assetmanagement.model.Location;
import com.siemens.mindsphere.sdk.assetmanagement.model.Variable;
import com.siemens.mindsphere.sdk.assetmanagement.model.VariableDefinition;
import com.siemens.mindsphere.sdk.timeseries.model.Timeseries;

import it.eng.fimind.model.fiware.weather.WeatherObserved;
import it.eng.fimind.util.MindSphereGateway;
import it.eng.fimind.util.MindSphereMapper;
import it.eng.fimind.util.ServiceResult;

/**
 * Root resource (exposed at "weatherobserved" path)
 */
@Path("weatherObserved")
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
			createMindSphereAssetFromWeatherObserved(weatherObserved);
		
		
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
	
	public Boolean createMindSphereAssetFromWeatherObserved(WeatherObserved weatherObserved) {
		Boolean result = false;
		
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		MindSphereMapper mindSphereMapper = new MindSphereMapper();
		
		weatherObserved.setId(weatherObserved.getId().replaceAll("-","_"));

		Location mindSphereLocation = null;
		if(weatherObserved.getLocation()!=null) {
			if(weatherObserved.getLocation().getType().equals("Point")) 
				mindSphereLocation = mindSphereMapper.fiLocationToMiLocation(weatherObserved.getLocation());
		}else if(weatherObserved.getAddress()!=null) 
			mindSphereLocation = mindSphereMapper.fiAddressToMiLocation(weatherObserved.getAddress());
		
		List<String> keys = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		List<String> varDefDataTypes = new ArrayList<String>();

		if(weatherObserved.getDataProvider()!=null) {
			keys.add("DataProvider");
			values.add(weatherObserved.getDataProvider());
			varDefDataTypes.add("String");
		}
		if(weatherObserved.getDateModified()!=null) {
			keys.add("DateModified");		
			values.add(weatherObserved.getDateModified());
			varDefDataTypes.add("Timestamp");
		}
		if(weatherObserved.getDateCreated()!=null) {
			keys.add("DateCreated");
			values.add(weatherObserved.getDateCreated());
			varDefDataTypes.add("Timestamp");
		}
		if(weatherObserved.getName()!=null) {	
			keys.add("Name");
			values.add(weatherObserved.getName());
			varDefDataTypes.add("String");
		}
		if(weatherObserved.getDateObserved()!=null) {
			keys.add("DateObserved");
			values.add(weatherObserved.getDateObserved());
			varDefDataTypes.add("Timestamp");
		}
		if(weatherObserved.getSource()!=null) {
			keys.add("Source");
			values.add(weatherObserved.getSource());
			varDefDataTypes.add("String");
		}
		if(weatherObserved.getRefDevice()!=null) {
			keys.add("RefDevice");
			values.add(weatherObserved.getRefDevice());
			varDefDataTypes.add("String");
		}
		if(weatherObserved.getRefPointOfInterest()!=null) {
			keys.add("RefPointOfInterest");
			values.add(weatherObserved.getRefPointOfInterest());
			varDefDataTypes.add("String");
		}
		List<VariableDefinition> assetVariablesDefinitions = mindSphereMapper.fiPropertiesToMiVariablesDefinitions(keys, values, varDefDataTypes);
		List<Variable> assetVariables = mindSphereMapper.fiPropertiesToMiVariables(keys, values, varDefDataTypes);

		
		List<String> properties = Stream.of("WeatherType", "DewPoint", "Visibility", "Temperature", "RelativeHumidity", "Precipitation", "WindDirection", "WindSpeed", "AtmosphericPressure", "PressureTendency", "SolarRadiation", "Illuminance", "StreamGauge", "SnowHeight").collect(Collectors.toList());
		List<String> uoms = Stream.of("Dimensionless", "c°", "Dimensionless", "c°", "%", "l/m2", "°", "m/s", "hPa", "Dimensionless", "W/m2", "lux", "cm", "cm").collect(Collectors.toList());
		List<String> dataTypes = Stream.of("String", "Double", "String", "Double", "Double", "Double", "Double", "Double", "Double", "String", "Double", "Double", "Double", "Double").collect(Collectors.toList());
		AspectType aspectType = mindSphereMapper.fiStateToMiAspectType(weatherObserved.getId(), "None", properties, uoms, dataTypes);

		
		result = mindSphereGateway.saveAsset(weatherObserved.getId(), mindSphereLocation, assetVariablesDefinitions, assetVariables, aspectType);	
		if(result)
			logger.debug("WeatherObserved created");
		else 		
			logger.error("WeatherObserved couldn't be created");
		return result;
	}
	
	public boolean createMindSphereTimeSeriesFromWeatherObserved(WeatherObserved weatherObserved) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+weatherObserved.getId()+"Asset\"}");
		try {
			List<Timeseries> timeSeriesList = new ArrayList<Timeseries>();
			Date now = new Date();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			String instant = df.format(now);
			Timeseries timeseriesPoint=new Timeseries();
			timeseriesPoint.getFields().put("_time", instant);
		
			if(weatherObserved.getWeatherType()!=null) {
				timeseriesPoint.getFields().put("WeatherType",weatherObserved.getWeatherType());
			}
			if(weatherObserved.getDewPoint()!=null) {
				timeseriesPoint.getFields().put("DewPoint",weatherObserved.getDewPoint());
			}
			if(weatherObserved.getVisibility()!=null) {
				timeseriesPoint.getFields().put("Visibility",weatherObserved.getVisibility());
			}
			if(weatherObserved.getTemperature()!=null) {
				timeseriesPoint.getFields().put("Temperature",weatherObserved.getTemperature());
			}
			if(weatherObserved.getRelativeHumidity()!=null) {
				timeseriesPoint.getFields().put("RelativeHumidity",weatherObserved.getRelativeHumidity());
			}
			if(weatherObserved.getPrecipitation()!=null) {
				timeseriesPoint.getFields().put("Precipitation",weatherObserved.getPrecipitation());
			}
			if(weatherObserved.getWindDirection()!=null) {
				timeseriesPoint.getFields().put("WindDirection",weatherObserved.getWindDirection());
			}
			if(weatherObserved.getWindSpeed()!=null) {
				timeseriesPoint.getFields().put("WindSpeed",weatherObserved.getWindSpeed());
			}
			if(weatherObserved.getAtmosphericPressure()!=null) {
				timeseriesPoint.getFields().put("AtmosphericPressure",weatherObserved.getAtmosphericPressure());
			}
			if(weatherObserved.getPressureTendency()!=null) {
				timeseriesPoint.getFields().put("PressureTendency",weatherObserved.getPressureTendency());
			}
			if(weatherObserved.getSolarRadiation()!=null) {
				timeseriesPoint.getFields().put("SolarRadiation",weatherObserved.getSolarRadiation());
			}
			if(weatherObserved.getIlluminance()!=null) {
				timeseriesPoint.getFields().put("Illuminance",weatherObserved.getIlluminance());
			}
			if(weatherObserved.getStreamGauge()!=null) {
				timeseriesPoint.getFields().put("StreamGauge",weatherObserved.getStreamGauge());
			}
			if(weatherObserved.getSnowHeight()!=null) {
				timeseriesPoint.getFields().put("SnowHeight",weatherObserved.getSnowHeight());
			}

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