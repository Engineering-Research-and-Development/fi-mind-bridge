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

import it.eng.fimind.model.fiware.weather.WeatherObservedNormalized;
import it.eng.fimind.util.MindSphereGateway;
import it.eng.fimind.util.MindSphereMapper;
import it.eng.fimind.util.ServiceResult;

/**
 * Root resource (exposed at "weatherobservednormalized" path)
 */
@Path("weatherObservedNormalized")
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
		
		weatherObserved.setId(weatherObserved.getId().replaceAll("-","_"));
		
		Location mindSphereLocation = null;
		if(weatherObserved.getLocation()!=null) {
			if(weatherObserved.getLocation().getValue().getType().equals("Point")) 
				mindSphereLocation = mindSphereMapper.fiLocationToMiLocation(weatherObserved.getLocation().getValue());
		}else if(weatherObserved.getAddress()!=null) 
			mindSphereLocation = mindSphereMapper.fiAddressToMiLocation(weatherObserved.getAddress().getValue());
		
		
		List<String> keys = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		if(weatherObserved.getDataProvider()!=null) {
			keys.add("DataProvider");
			values.add((String) weatherObserved.getDataProvider().getValue());
		}
		if(weatherObserved.getDataProvider()!=null) {
			keys.add("DateModified");		
			values.add((String) weatherObserved.getDateModified().getValue());
		}
		if(weatherObserved.getDataProvider()!=null) {
			keys.add("DateCreated");
			values.add((String) weatherObserved.getDateCreated().getValue());
		}
		if(weatherObserved.getDataProvider()!=null) {	
			keys.add("Name");
			values.add((String) weatherObserved.getName().getValue());
		}
		if(weatherObserved.getDataProvider()!=null) {
			keys.add("DateObserved");
			values.add((String) weatherObserved.getDateObserved().getValue());
		}
		if(weatherObserved.getDataProvider()!=null) {
			keys.add("Source");
			values.add((String) weatherObserved.getSource().getValue());
		}
		if(weatherObserved.getDataProvider()!=null) {
			keys.add("RefDevice");
			values.add((String) weatherObserved.getRefDevice().getValue());
		}
		if(weatherObserved.getDataProvider()!=null) {
			keys.add("RefPointOfInterest");
			values.add((String) weatherObserved.getRefPointOfInterest().getValue());
		}
		List<VariableDefinition> assetVariablesDefinitions = mindSphereMapper.fiPropertiesToMiVariablesDefinitions(keys, values);
		List<Variable> assetVariables = mindSphereMapper.fiPropertiesToMiVariables(keys, values);

		
		List<String> properties = Stream.of("WeatherType", "DewPoint", "Visibility", "Temperature", "RelativeHumidity", "Precipitation", "WindDirection", "WindSpeed", "AtmosphericPressure", "PressureTendency", "SolarRadiation", "Illuminance", "StreamGauge", "SnowHeight").collect(Collectors.toList());
		List<String> uoms = Stream.of("Dimensionless", "c°", "Dimensionless", "c°", "%", "l/m2", "°", "m/s", "hPa", "Dimensionless", "W/m2", "lux", "cm", "cm").collect(Collectors.toList());
		List<String> dataTypes = Stream.of("String", "Double", "String", "Double", "Double", "Double", "Double", "Double", "Double", "String", "Double", "Double", "Double", "Double").collect(Collectors.toList());
		AspectType aspectType = mindSphereMapper.fiStateToMiAspectType(weatherObserved.getId(), "None", properties, uoms, dataTypes);
		
		
		return mindSphereGateway.createAsset(weatherObserved.getId(), mindSphereLocation, assetVariablesDefinitions, assetVariables, aspectType);
	}
	
	private Boolean saveMindSphereAsset(Asset asset) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		Boolean result = mindSphereGateway.saveAsset(asset);
		if(result)
			logger.debug("WeatherObservedNormalized created");
		else 		
			logger.error("WeatherObservedNormalized couldn't be created");
		return result;
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
		
			if(weatherObserved.getWeatherType()!=null) {
				timeseriesPoint.getFields().put("WeatherType",(String) weatherObserved.getWeatherType().getValue());
			}
			if(weatherObserved.getDewPoint()!=null) {
				timeseriesPoint.getFields().put("DewPoint",(Double) weatherObserved.getDewPoint().getValue());
			}
			if(weatherObserved.getVisibility()!=null) {
				timeseriesPoint.getFields().put("Visibility",(String) weatherObserved.getVisibility().getValue());
			}
			if(weatherObserved.getTemperature()!=null) {
				timeseriesPoint.getFields().put("Temperature",(Double) weatherObserved.getTemperature().getValue());
			}
			if(weatherObserved.getRelativeHumidity()!=null) {
				timeseriesPoint.getFields().put("RelativeHumidity",(Double) weatherObserved.getRelativeHumidity().getValue());
			}
			if(weatherObserved.getPrecipitation()!=null) {
				timeseriesPoint.getFields().put("Precipitation",(Double) weatherObserved.getPrecipitation().getValue());
			}
			if(weatherObserved.getWindDirection()!=null) {
				timeseriesPoint.getFields().put("WindDirection",(Double) weatherObserved.getWindDirection().getValue());
			}
			if(weatherObserved.getWindSpeed()!=null) {
				timeseriesPoint.getFields().put("WindSpeed",(Double) weatherObserved.getWindSpeed().getValue());
			}
			if(weatherObserved.getAtmosphericPressure()!=null) {
				timeseriesPoint.getFields().put("AtmosphericPressure",(Double) weatherObserved.getAtmosphericPressure().getValue());
			}
			if(weatherObserved.getPressureTendency()!=null) {
				timeseriesPoint.getFields().put("PressureTendency",(String) weatherObserved.getPressureTendency().getValue());
			}
			if(weatherObserved.getSolarRadiation()!=null) {
				timeseriesPoint.getFields().put("SolarRadiation",(Double) weatherObserved.getSolarRadiation().getValue());
			}
			if(weatherObserved.getIlluminance()!=null) {
				timeseriesPoint.getFields().put("Illuminance",(Double) weatherObserved.getIlluminance().getValue());
			}
			if(weatherObserved.getStreamGauge()!=null) {
				timeseriesPoint.getFields().put("StreamGauge",(Double) weatherObserved.getStreamGauge().getValue());
			}
			if(weatherObserved.getSnowHeight()!=null) {
				timeseriesPoint.getFields().put("SnowHeight",(Double) weatherObserved.getSnowHeight().getValue());
			}

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