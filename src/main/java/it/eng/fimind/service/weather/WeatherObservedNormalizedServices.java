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
	public Response createDataInJSON(@HeaderParam("debug-mode") String debugMode, @Valid WeatherObservedNormalized weatherObserved) { 
		ServiceResult serviceResult = new ServiceResult();
		logger.debug("Id ="+weatherObserved.getId());
		
		if(debugMode!=null && debugMode.equals("true")){
			System.out.println("DEBUG MODE FOR --- WeatherObservedNormalized ---");
			createMindSphereAssetFromWeatherObserved(weatherObserved, true);
			serviceResult.setResult("Test gone fine");
			return Response.status(200).entity(serviceResult).build();
		}else {
			Boolean result = false;
			if(!weatherObservedDoesAlreadyExist(weatherObserved)) 
				result = createMindSphereAssetFromWeatherObserved(weatherObserved, false);
			
			result = createMindSphereTimeSeriesFromWeatherObserved(weatherObserved);
			
			if(result) {
				serviceResult.setResult("WeatherObservedNormalized added succesfully");
				return Response.status(201).entity(serviceResult).build();
			}
			else {
				serviceResult.setResult("Something went wrong, check your FI-MIND logs");
				return Response.status(500).entity(serviceResult).build();
			}
		}
	}

	
	private Boolean weatherObservedDoesAlreadyExist(WeatherObservedNormalized weatherObserved)
	{
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+weatherObserved.getId()+"Asset\"}");
		return assets.size()>0;
	}
	
	private Boolean createMindSphereAssetFromWeatherObserved(WeatherObservedNormalized weatherObserved, Boolean isDebugMode) {
		Boolean result = false;
		
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
		List<String> varDefDataTypes = new ArrayList<String>();
		
		if(weatherObserved.getDataProvider()!=null) {
			keys.add("DataProvider");
			values.add((String) weatherObserved.getDataProvider().getValue());
			varDefDataTypes.add("String");
		}
		if(weatherObserved.getDateModified()!=null) {
			keys.add("DateModified");		
			values.add((String) weatherObserved.getDateModified().getValue());
			varDefDataTypes.add("Timestamp");
		}
		if(weatherObserved.getDateCreated()!=null) {
			keys.add("DateCreated");
			values.add((String) weatherObserved.getDateCreated().getValue());
			varDefDataTypes.add("Timestamp");
		}
		if(weatherObserved.getName()!=null) {	
			keys.add("Name");
			values.add((String) weatherObserved.getName().getValue());
			varDefDataTypes.add("String");
		}
		if(weatherObserved.getDateObserved()!=null) {
			keys.add("DateObserved");
			values.add((String) weatherObserved.getDateObserved().getValue());
			varDefDataTypes.add("Timestamp");
		}
		if(weatherObserved.getSource()!=null) {
			keys.add("Source");
			values.add((String) weatherObserved.getSource().getValue());
			varDefDataTypes.add("String");
		}
		if(weatherObserved.getRefDevice()!=null) {
			keys.add("RefDevice");
			values.add((String) weatherObserved.getRefDevice().getValue());
			varDefDataTypes.add("String");
		}
		if(weatherObserved.getRefPointOfInterest()!=null) {
			keys.add("RefPointOfInterest");
			values.add((String) weatherObserved.getRefPointOfInterest().getValue());
			varDefDataTypes.add("String");
		}
		List<VariableDefinition> assetVariablesDefinitions = mindSphereMapper.fiPropertiesToMiVariablesDefinitions(keys, values, varDefDataTypes);
		List<Variable> assetVariables = mindSphereMapper.fiPropertiesToMiVariables(keys, values, varDefDataTypes);

		
		List<String> properties = Stream.of("DateObserved","WeatherType", "DewPoint", "Visibility", "Temperature", "RelativeHumidity", "Precipitation", "WindDirection", "WindSpeed", "AtmosphericPressure", "PressureTendency", "SolarRadiation", "Illuminance", "StreamGauge", "SnowHeight").collect(Collectors.toList());
		List<String> uoms = Stream.of("t", "Dimensionless", "c°", "Dimensionless", "c°", "%", "l/m2", "°", "m/s", "hPa", "Dimensionless", "W/m2", "lux", "cm", "cm").collect(Collectors.toList());
		List<String> dataTypes = Stream.of("Timestamp","String", "Double", "String", "Double", "Double", "Double", "Double", "Double", "Double", "String", "Double", "Double", "Double", "Double").collect(Collectors.toList());
		AspectType aspectType = mindSphereMapper.fiStateToMiAspectType(weatherObserved.getId(), "None", properties, uoms, dataTypes);
		
		
		if(isDebugMode) {
			System.out.println(mindSphereGateway.createAsset(weatherObserved.getId(), mindSphereLocation, assetVariablesDefinitions, assetVariables, aspectType));
			result = true;
		}else {
			result = mindSphereGateway.saveAsset(weatherObserved.getId(), mindSphereLocation, assetVariablesDefinitions, assetVariables, aspectType);
			if(result)
				logger.debug("WeatherObservedNormalized created");
			else 		
				logger.error("WeatherObservedNormalized couldn't be created");
		}
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
		
			if(weatherObserved.getDateObserved()!=null) {
				timeseriesPoint.getFields().put("DateObserved",(String) weatherObserved.getDateObserved().getValue());
			}
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