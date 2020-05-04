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
			logger.debug("DEBUG MODE FOR --- WeatherObservedNormalized ---");
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
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+weatherObserved.getId()+"\"}");
		return assets.size()>0;
	}
	
	private Boolean createMindSphereAssetFromWeatherObserved(WeatherObservedNormalized weatherObserved, Boolean isDebugMode) {
		Boolean result = false;
		
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		MindSphereMapper mindSphereMapper = new MindSphereMapper();
				
		Location mindSphereLocation = null;
		if(weatherObserved.getLocation()!=null && weatherObserved.getAddress()!=null)
			mindSphereLocation = mindSphereMapper.fiLocAddrToMiLocation(weatherObserved.getLocation().getValue(), weatherObserved.getAddress().getValue());
		else if(weatherObserved.getLocation()!=null)
			mindSphereLocation = mindSphereMapper.fiLocationToMiLocation(weatherObserved.getLocation().getValue());
		else if(weatherObserved.getAddress()!=null) 
			mindSphereLocation = mindSphereMapper.fiAddressToMiLocation(weatherObserved.getAddress().getValue());
		
		
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
			values.add((String) weatherObserved.getDataProvider().getValue());
			varDefDataTypes.add("String");
		}
		if(weatherObserved.getDateCreated()!=null) {
			keys.add("dateCreated");
			values.add((String) weatherObserved.getDateCreated().getValue());
			varDefDataTypes.add("Timestamp");
		}
		if(weatherObserved.getName()!=null) {	
			keys.add("entityName");
			values.add((String) weatherObserved.getName().getValue());
			varDefDataTypes.add("String");
		}
		if(weatherObserved.getDateObserved()!=null) {
			keys.add("dateObserved");
			values.add((String) weatherObserved.getDateObserved().getValue());
			varDefDataTypes.add("Timestamp");
		}
		if(weatherObserved.getSource()!=null) {
			keys.add("source");
			values.add((String) weatherObserved.getSource().getValue());
			varDefDataTypes.add("String");
		}
		if(weatherObserved.getStationCode()!=null) {
			keys.add("stationCode");
			values.add((String) weatherObserved.getStationCode().getValue());
			varDefDataTypes.add("String");
		}
		if(weatherObserved.getStationName()!=null) {
			keys.add("stationName");
			values.add((String) weatherObserved.getStationName().getValue());
			varDefDataTypes.add("String");
		}
		if(weatherObserved.getRefDevice()!=null) {
			keys.add("refDevice");
			values.add((String) weatherObserved.getRefDevice().getValue());
			varDefDataTypes.add("String");
		}
		if(weatherObserved.getRefPointOfInterest()!=null) {
			keys.add("refPointOfInterest");
			values.add((String) weatherObserved.getRefPointOfInterest().getValue());
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
				logger.debug("WeatherObservedNormalized created");
			else 		
				logger.error("WeatherObservedNormalized couldn't be created");
		}
		return result;
	}
	
	private boolean createMindSphereTimeSeriesFromWeatherObserved(WeatherObservedNormalized weatherObserved) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+weatherObserved.getId()+"\"}");
		try {
			List<Timeseries> timeSeriesList = new ArrayList<Timeseries>();

			Timeseries timeseriesPoint = new Timeseries();
			
			timeseriesPoint.getFields().put("_time", Instant.now().toString());
		
			if(weatherObserved.getDateModified()!=null) {
				timeseriesPoint.getFields().put("dateModified",(String) weatherObserved.getDateModified().getValue());
			}
			if(weatherObserved.getDateObserved()!=null) {
				timeseriesPoint.getFields().put("dateObserved",(String) weatherObserved.getDateObserved().getValue());
			}
			if(weatherObserved.getWeatherType()!=null) {
				timeseriesPoint.getFields().put("weatherType",(String) weatherObserved.getWeatherType().getValue());
			}
			if(weatherObserved.getDewPoint()!=null) {
				timeseriesPoint.getFields().put("dewPoint", Double.valueOf(weatherObserved.getDewPoint().getValue().toString()));
			}
			if(weatherObserved.getVisibility()!=null) {
				timeseriesPoint.getFields().put("visibility",(String) weatherObserved.getVisibility().getValue());
			}
			if(weatherObserved.getTemperature()!=null) {
				timeseriesPoint.getFields().put("temperature", Double.valueOf(weatherObserved.getTemperature().getValue().toString()));
			}
			if(weatherObserved.getRelativeHumidity()!=null) {
				timeseriesPoint.getFields().put("relativeHumidity", Double.valueOf(weatherObserved.getRelativeHumidity().getValue().toString()));
			}
			if(weatherObserved.getPrecipitation()!=null) {
				timeseriesPoint.getFields().put("precipitation", Double.valueOf(weatherObserved.getPrecipitation().getValue().toString()));
			}
			if(weatherObserved.getWindDirection()!=null) {
				timeseriesPoint.getFields().put("windDirection", Double.valueOf(weatherObserved.getWindDirection().getValue().toString()));
			}
			if(weatherObserved.getWindSpeed()!=null) {
				timeseriesPoint.getFields().put("windSpeed", Double.valueOf(weatherObserved.getWindSpeed().getValue().toString()));
			}
			if(weatherObserved.getAtmosphericPressure()!=null) {
				timeseriesPoint.getFields().put("atmosphericPressure", Double.valueOf(weatherObserved.getAtmosphericPressure().getValue().toString()));
			}
			if(weatherObserved.getPressureTendency()!=null) {
				timeseriesPoint.getFields().put("pressureTendency", Double.valueOf(weatherObserved.getPressureTendency().getValue().toString()));
			}
			if(weatherObserved.getSolarRadiation()!=null) {
				timeseriesPoint.getFields().put("solarRadiation", Double.valueOf(weatherObserved.getSolarRadiation().getValue().toString()));
			}
			if(weatherObserved.getIlluminance()!=null) {
				timeseriesPoint.getFields().put("illuminance", Double.valueOf(weatherObserved.getIlluminance().getValue().toString()));
			}
			if(weatherObserved.getStreamGauge()!=null) {
				timeseriesPoint.getFields().put("streamGauge", Double.valueOf(weatherObserved.getStreamGauge().getValue().toString()));
			}
			if(weatherObserved.getSnowHeight()!=null) {
				timeseriesPoint.getFields().put("snowHeight", Double.valueOf(weatherObserved.getSnowHeight().getValue().toString()));
			}

			timeSeriesList.add(timeseriesPoint);
			mindSphereGateway.putTimeSeries(assets.get(0).getAssetId(), weatherObserved.getId(), timeSeriesList);
			logger.debug("WeatherObservedNormalized updated");

		} catch (Exception e) {
			// Exception handling
			e.printStackTrace();
			return false;
		}
		return true;
	}	
}