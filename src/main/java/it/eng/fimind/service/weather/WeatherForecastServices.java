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

import it.eng.fimind.model.fiware.weather.WeatherForecast;
import it.eng.fimind.util.MindSphereGateway;
import it.eng.fimind.util.ServiceResult;

/**
 * Root resource (exposed at "weatherforecast" path)
 */
@Path("weatherforecast")
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
	public Response createDataInJSON(@Valid WeatherForecast weatherForecast) { 
		ServiceResult serviceResult=new ServiceResult();
		logger.debug("Id ="+weatherForecast.getId());
		if(!weatherForecastDoesAlreadyExist(weatherForecast)) {
			createMindSphereAssetFromWeatherForecast(weatherForecast);
		}
		createMindSphereTimeSeriesFromWeatherForecast(weatherForecast);
		
		serviceResult.setResult("OK");
		return Response.status(201).entity(serviceResult).build();
	}

	
	private Boolean weatherForecastDoesAlreadyExist(WeatherForecast weatherForecast)
	{
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+weatherForecast.getId()+"Asset\"}");
		return assets.size()>0;
	}
	
	private boolean createMindSphereAssetFromWeatherForecast(WeatherForecast weatherForecast) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		AspectType aspectType = new AspectType();
		
		aspectType.setName((String) weatherForecast.getId()+"Aspect");
		//aspectType.setDescription(aas.getDescription());
		aspectType.setScope(ScopeEnum.PRIVATE);
		aspectType.setCategory(CategoryEnum.DYNAMIC);
		
		List<AspectVariable> variables=new ArrayList<AspectVariable>();

		List<String> properties = Stream.of("WeatherType", "Visibility", "Temperature", "FeelsLikeTemperature", "RelativeHumidity", "PrecipitationProbability", "WindDirection", "WindSpeed", "MinTemperature", "MinFeelsLikeTemperature", "MinRelativeHumidity", "MaxTemperature", "MaxFeelsLikeTemperature", "MaxRelativeHumidity").collect(Collectors.toList());
		List<String> uoms = Stream.of("Empiric Data", "Empiric Data", "c°", "c°", "%", "%/100", "°", "m/s", "c°", "c°", "%", "c°", "c°", "%").collect(Collectors.toList());

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
		mindSphereGateway.createAsset(weatherForecast.getId(), aspectType);
		logger.debug("WeatherForecast created");
		return true;
	}
	
	private boolean createMindSphereTimeSeriesFromWeatherForecast(WeatherForecast weatherForecast) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+weatherForecast.getId()+"Asset\"}");
		try {
			List<Timeseries> timeSeriesList = new ArrayList<Timeseries>();
			Date now = new Date();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			String instant = df.format(now);
			Timeseries timeseriesPoint=new Timeseries();
			timeseriesPoint.getFields().put("_time", instant);
		
			timeseriesPoint.getFields().put("WeatherType",weatherForecast.getWeatherType());
			timeseriesPoint.getFields().put("Visibility",weatherForecast.getVisibility());
			timeseriesPoint.getFields().put("Temperature",weatherForecast.getTemperature());
			timeseriesPoint.getFields().put("FeelsLikeTemperature",weatherForecast.getFeelsLikeTemperature());
			timeseriesPoint.getFields().put("RelativeHumidity",weatherForecast.getRelativeHumidity());
			timeseriesPoint.getFields().put("PrecipitationProbability",weatherForecast.getPrecipitationProbability());
			timeseriesPoint.getFields().put("WindDirection",weatherForecast.getWindDirection());
			timeseriesPoint.getFields().put("WindSpeed",weatherForecast.getWindSpeed());
			timeseriesPoint.getFields().put("MinTemperature",weatherForecast.getDayMinimum().getTemperature());
			timeseriesPoint.getFields().put("MinFeelsLikeTemperature",weatherForecast.getDayMinimum().getFeelsLikeTemperature());
			timeseriesPoint.getFields().put("MinRelativeHumidity",weatherForecast.getDayMinimum().getRelativeHumidity());
			timeseriesPoint.getFields().put("MaxTemperature",weatherForecast.getDayMaximum().getTemperature());
			timeseriesPoint.getFields().put("MaxFeelsLikeTemperature",weatherForecast.getDayMaximum().getFeelsLikeTemperature());
			timeseriesPoint.getFields().put("MaxRelativeHumidity",weatherForecast.getDayMaximum().getRelativeHumidity());

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