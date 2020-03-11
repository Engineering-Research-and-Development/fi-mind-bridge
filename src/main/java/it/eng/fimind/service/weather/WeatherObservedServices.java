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
import com.siemens.mindsphere.sdk.assetmanagement.model.AspectType.CategoryEnum;
import com.siemens.mindsphere.sdk.assetmanagement.model.AspectType.ScopeEnum;
import com.siemens.mindsphere.sdk.assetmanagement.model.AspectVariable;
import com.siemens.mindsphere.sdk.assetmanagement.model.AspectVariable.DataTypeEnum;
import com.siemens.mindsphere.sdk.assetmanagement.model.AssetResource;
import com.siemens.mindsphere.sdk.timeseries.model.Timeseries;

import it.eng.fimind.model.fiware.weather.WeatherObserved;
import it.eng.fimind.util.MindSphereGateway;
import it.eng.fimind.util.ServiceResult;

/**
 * Root resource (exposed at "weatherobserved" path)
 */
@Path("weatherobserved")
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
		ServiceResult serviceResult=new ServiceResult();
		logger.debug("Id ="+weatherObserved.getId());
		if(!weatherObservedDoesAlreadyExist(weatherObserved)) {
			createMindSphereAssetFromWeatherObserved(weatherObserved);
		}
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
	
	private boolean createMindSphereAssetFromWeatherObserved(WeatherObserved weatherObserved) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		AspectType aspectType = new AspectType();
		
		aspectType.setName((String) weatherObserved.getId()+"Aspect");
		//aspectType.setDescription(aas.getDescription());
		aspectType.setScope(ScopeEnum.PRIVATE);
		aspectType.setCategory(CategoryEnum.DYNAMIC);
		
		List<AspectVariable> variables=new ArrayList<AspectVariable>();

		List<String> properties = Stream.of("WeatherType", "DewPoint", "Visibility", "Temperature", "RelativeHumidity", "Precipitation", "WindDirection", "WindSpeed", "AtmosphericPressure", "PressureTendency", "SolarRadiation", "Illuminance", "StreamGauge", "SnowHeight").collect(Collectors.toList());
		List<String> uoms = Stream.of("Empiric Data", "c°", "Empiric Data", "c°", "%", "l/m2", "°", "m/s", "hPa", "Empiric Data", "W/m2", "lux", "cm", "cm").collect(Collectors.toList());

		for(int i=0; i<properties.size();i++) {
			AspectVariable var = new AspectVariable();
			var.setName(properties.get(i));
			var.setDataType(DataTypeEnum.STRING);
			var.setLength(20);
			var.setUnit(uoms.get(i));
			var.setSearchable(true);
			var.setQualityCode(true);
			variables.add(var);
		}
		
		aspectType.setVariables(variables);
		mindSphereGateway.createAsset(weatherObserved.getId(), aspectType);
		logger.debug("WeatherObserved created");
		return true;
	}
	
	private boolean createMindSphereTimeSeriesFromWeatherObserved(WeatherObserved weatherObserved) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+weatherObserved.getId()+"Asset\"}");
		try {
			List<Timeseries> timeSeriesList = new ArrayList<Timeseries>();
			Date now = new Date();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			String instant = df.format(now);
			Timeseries timeseriesPoint=new Timeseries();
			timeseriesPoint.getFields().put("_time", instant);
		
			timeseriesPoint.getFields().put("WeatherType",weatherObserved.getWeatherType());
			timeseriesPoint.getFields().put("DewPoint",weatherObserved.getDewPoint());
			timeseriesPoint.getFields().put("Visibility",weatherObserved.getVisibility());
			timeseriesPoint.getFields().put("Temperature",weatherObserved.getTemperature());
			timeseriesPoint.getFields().put("RelativeHumidity",weatherObserved.getRelativeHumidity());
			timeseriesPoint.getFields().put("Precipitation",weatherObserved.getPrecipitation());
			timeseriesPoint.getFields().put("WindDirection",weatherObserved.getWindDirection());
			timeseriesPoint.getFields().put("WindSpeed",weatherObserved.getWindSpeed());
			timeseriesPoint.getFields().put("AtmosphericPressure",weatherObserved.getAtmosphericPressure());
			timeseriesPoint.getFields().put("PressureTendency",weatherObserved.getPressureTendency());
			timeseriesPoint.getFields().put("SolarRadiation",weatherObserved.getSolarRadiation());
			timeseriesPoint.getFields().put("Illuminance",weatherObserved.getIlluminance());
			timeseriesPoint.getFields().put("StreamGauge",weatherObserved.getStreamGauge());
			timeseriesPoint.getFields().put("SnowHeight",weatherObserved.getSnowHeight());

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