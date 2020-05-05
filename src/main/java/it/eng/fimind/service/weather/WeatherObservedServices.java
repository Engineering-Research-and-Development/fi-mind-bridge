package it.eng.fimind.service.weather;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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

	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteIt(@QueryParam("id") String id) {
		logger.debug("[WeatherObservedServices] DELETE Request");
		ServiceResult serviceResult = new ServiceResult();

		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		if(mindSphereGateway.deleteAssetOnCascade(id))
		{
			serviceResult.setMessage("Deleted successfully!");
			return Response.status(200).entity(serviceResult).build();
		}
		else {
			serviceResult.setResult("Something went wrong, check your FI-MIND logs");
			return Response.status(500).entity(serviceResult).build();
		}
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createDataInJSON(@HeaderParam("debug-mode") String debugMode, @Valid WeatherObserved weatherObserved) { 
		logger.debug("[WeatherObservedServices] POST Request");
		ServiceResult serviceResult = new ServiceResult();
	
		logger.debug("Id ="+weatherObserved.getId());
		
		if(debugMode!=null && debugMode.equals("true")){
			logger.debug("DEBUG MODE FOR --- WeatherObserved ---");
			createMindSphereAssetFromWeatherObserved(weatherObserved, true);
			serviceResult.setResult("Test gone fine");
			return Response.status(200).entity(serviceResult).build();
		}else {
			Boolean result = true;
			if(!weatherObservedDoesAlreadyExist(weatherObserved)) 
				result = createMindSphereAssetFromWeatherObserved(weatherObserved, false);
			
			if(result)
				result = createMindSphereTimeSeriesFromWeatherObserved(weatherObserved);
			
			if(result) {
				serviceResult.setResult("WeatherObserved added succesfully");
				return Response.status(201).entity(serviceResult).build();
			}
			else {
				serviceResult.setResult("Something went wrong, check your FI-MIND logs");
				return Response.status(500).entity(serviceResult).build();
			}
		}
	}

	
	private Boolean weatherObservedDoesAlreadyExist(WeatherObserved weatherObserved)
	{
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+weatherObserved.getId()+"\"}");
		return assets.size()>0;
	}
	
	public Boolean createMindSphereAssetFromWeatherObserved(WeatherObserved weatherObserved, Boolean isDebugMode) {
		Boolean result = false;
		
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		MindSphereMapper mindSphereMapper = new MindSphereMapper();
		
		Location mindSphereLocation = null;
		if(weatherObserved.getLocation()!=null && weatherObserved.getAddress()!=null)
			mindSphereLocation = mindSphereMapper.fiLocAddrToMiLocation(weatherObserved.getLocation(), weatherObserved.getAddress());
		else if(weatherObserved.getLocation()!=null)
			mindSphereLocation = mindSphereMapper.fiLocationToMiLocation(weatherObserved.getLocation());
		else if(weatherObserved.getAddress()!=null) 
			mindSphereLocation = mindSphereMapper.fiAddressToMiLocation(weatherObserved.getAddress());
		
		
		List<String> keys = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		List<String> varDefDataTypes = new ArrayList<String>();

		if(weatherObserved.getType()!=null) {
			keys.add("entityType");
			values.add(weatherObserved.getType());
			varDefDataTypes.add("String");
		}
		if(weatherObserved.getDataProvider()!=null) {
			keys.add("dataProvider");
			values.add(weatherObserved.getDataProvider());
			varDefDataTypes.add("String");
		}
		if(weatherObserved.getDateCreated()!=null) {
			keys.add("dateCreated");
			values.add(weatherObserved.getDateCreated());
			varDefDataTypes.add("Timestamp");
		}
		if(weatherObserved.getName()!=null) {	
			keys.add("entityName");
			values.add(weatherObserved.getName());
			varDefDataTypes.add("String");
		}
		if(weatherObserved.getSource()!=null) {
			keys.add("source");
			values.add(weatherObserved.getSource());
			varDefDataTypes.add("String");
		}
		if(weatherObserved.getStationCode()!=null) {
			keys.add("stationCode");
			values.add(weatherObserved.getStationCode());
			varDefDataTypes.add("String");
		}
		if(weatherObserved.getStationName()!=null) {
			keys.add("stationName");
			values.add(weatherObserved.getStationName());
			varDefDataTypes.add("String");
		}
		if(weatherObserved.getRefDevice()!=null) {
			keys.add("refDevice");
			values.add(weatherObserved.getRefDevice());
			varDefDataTypes.add("String");
		}
		if(weatherObserved.getRefPointOfInterest()!=null) {
			keys.add("refPointOfInterest");
			values.add(weatherObserved.getRefPointOfInterest());
			varDefDataTypes.add("String");
		}
		List<VariableDefinition> assetVariablesDefinitions = mindSphereMapper.fiPropertiesToMiVariablesDefinitions(keys, values, varDefDataTypes);
		List<Variable> assetVariables = mindSphereMapper.fiPropertiesToMiVariables(keys, values, varDefDataTypes);

		
		List<String> properties = Stream.of("dateModified","dateObserved","weatherType", "dewPoint", "visibility", "temperature", "relativeHumidity", "precipitation", "windDirection", "windSpeed", "atmosphericPressure", "pressureTendency", "solarRadiation", "illuminance", "streamGauge", "snowHeight").collect(Collectors.toList());
		List<String> uoms = Stream.of("t", "t","Dimensionless", "c°", "Dimensionless", "c°", "%", "l/m2", "°", "m/s", "hPa", "Dimensionless", "W/m2", "lux", "cm", "cm").collect(Collectors.toList());
		List<String> dataTypes = Stream.of("Timestamp", "Timestamp", "String", "Double", "String", "Double", "Double", "Double", "Double", "Double", "Double", "Double", "Double", "Double", "Double", "Double").collect(Collectors.toList());
		AspectType aspectType = mindSphereMapper.fiStateToMiAspectType(weatherObserved.getId(), properties, uoms, dataTypes);

		
		if(isDebugMode) {
			logger.debug(mindSphereGateway.createAsset(weatherObserved.getId(), mindSphereLocation, assetVariablesDefinitions, assetVariables, aspectType));
			result = true;
		}else {
			result = mindSphereGateway.saveAsset(weatherObserved.getId(), mindSphereLocation, assetVariablesDefinitions, assetVariables, aspectType);
			if(result)
				logger.debug("WeatherObserved created");
			else 		
				logger.error("WeatherObserved couldn't be created");
		}
		return result;
	}
	
	public boolean createMindSphereTimeSeriesFromWeatherObserved(WeatherObserved weatherObserved) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+weatherObserved.getId()+"\"}");
		try {
			List<Timeseries> timeSeriesList = new ArrayList<Timeseries>();

			Timeseries timeseriesPoint = new Timeseries();
			
			timeseriesPoint.getFields().put("_time", Instant.now().toString());
		
			if(weatherObserved.getDateModified()!=null) {
				timeseriesPoint.getFields().put("dateModified",weatherObserved.getDateModified());
			}
			if(weatherObserved.getDateObserved()!=null) {
				timeseriesPoint.getFields().put("dateObserved",weatherObserved.getDateObserved());
			}
			if(weatherObserved.getWeatherType()!=null) {
				timeseriesPoint.getFields().put("weatherType",weatherObserved.getWeatherType());
			}
			if(weatherObserved.getDewPoint()!=null) {
				timeseriesPoint.getFields().put("dewPoint",weatherObserved.getDewPoint());
			}
			if(weatherObserved.getVisibility()!=null) {
				timeseriesPoint.getFields().put("visibility",weatherObserved.getVisibility());
			}
			if(weatherObserved.getTemperature()!=null) {
				timeseriesPoint.getFields().put("temperature",weatherObserved.getTemperature());
			}
			if(weatherObserved.getRelativeHumidity()!=null) {
				timeseriesPoint.getFields().put("relativeHumidity",weatherObserved.getRelativeHumidity());
			}
			if(weatherObserved.getPrecipitation()!=null) {
				timeseriesPoint.getFields().put("precipitation",weatherObserved.getPrecipitation());
			}
			if(weatherObserved.getWindDirection()!=null) {
				timeseriesPoint.getFields().put("windDirection",weatherObserved.getWindDirection());
			}
			if(weatherObserved.getWindSpeed()!=null) {
				timeseriesPoint.getFields().put("windSpeed",weatherObserved.getWindSpeed());
			}
			if(weatherObserved.getAtmosphericPressure()!=null) {
				timeseriesPoint.getFields().put("atmosphericPressure",weatherObserved.getAtmosphericPressure());
			}
			if(weatherObserved.getPressureTendency()!=null) {
				timeseriesPoint.getFields().put("pressureTendency",weatherObserved.getPressureTendency());
			}
			if(weatherObserved.getSolarRadiation()!=null) {
				timeseriesPoint.getFields().put("solarRadiation",weatherObserved.getSolarRadiation());
			}
			if(weatherObserved.getIlluminance()!=null) {
				timeseriesPoint.getFields().put("illuminance",weatherObserved.getIlluminance());
			}
			if(weatherObserved.getStreamGauge()!=null) {
				timeseriesPoint.getFields().put("streamGauge",weatherObserved.getStreamGauge());
			}
			if(weatherObserved.getSnowHeight()!=null) {
				timeseriesPoint.getFields().put("snowHeight",weatherObserved.getSnowHeight());
			}

			timeSeriesList.add(timeseriesPoint);
			mindSphereGateway.putTimeSeries(assets.get(0).getAssetId(), weatherObserved.getId(), timeSeriesList);
			logger.debug("WeatherObserved updated");

		} catch (Exception e) {
			// Exception handling
			e.printStackTrace();
			return false;
		}
		return true;
	}	
}