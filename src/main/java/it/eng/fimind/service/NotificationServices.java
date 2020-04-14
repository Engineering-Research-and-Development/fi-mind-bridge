package it.eng.fimind.service;

import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.eng.fimind.model.NotificationContent;
import it.eng.fimind.model.fiware.alert.AlertNormalized;
import it.eng.fimind.model.fiware.building.BuildingNormalized;
import it.eng.fimind.model.fiware.building.BuildingOperationNormalized;
import it.eng.fimind.model.fiware.common.Metadata;
import it.eng.fimind.model.fiware.common.TimeInstant;
import it.eng.fimind.model.fiware.device.DeviceModelNormalized;
import it.eng.fimind.model.fiware.device.DeviceNormalized;
import it.eng.fimind.model.fiware.transportation.TrafficFlowObservedNormalized;
import it.eng.fimind.model.fiware.transportation.VehicleModelNormalized;
import it.eng.fimind.model.fiware.transportation.VehicleNormalized;
import it.eng.fimind.model.fiware.weather.WeatherForecastNormalized;
import it.eng.fimind.model.fiware.weather.WeatherObservedNormalized;
import it.eng.fimind.model.zvei.aas.AssetAdministrationShell;
import it.eng.fimind.service.aas.AssetAdministrationShellServices;
import it.eng.fimind.service.alert.AlertNormalizedServices;
import it.eng.fimind.service.building.BuildingNormalizedServices;
import it.eng.fimind.service.building.BuildingOperationNormalizedServices;
import it.eng.fimind.service.device.DeviceModelNormalizedServices;
import it.eng.fimind.service.device.DeviceNormalizedServices;
import it.eng.fimind.service.transportation.TrafficFlowObservedNormalizedServices;
import it.eng.fimind.service.transportation.VehicleModelNormalizedServices;
import it.eng.fimind.service.transportation.VehicleNormalizedServices;
import it.eng.fimind.service.weather.WeatherForecastNormalizedServices;
import it.eng.fimind.service.weather.WeatherObservedNormalizedServices;


@Path("fiware-notification")
public class NotificationServices {
	private static Logger logger = Logger.getLogger(NotificationServices.class);
	
	private String unmapForbiddenChars(String value) {
		String result = value;
		if(result.contains("%3C"))
			result = result.replace("%3C", "<");
		if(result.contains("%3E"))
			result = result.replace("%3E", ">");
		if(result.contains("%22"))
			result = result.replace("%22", "\"");
		if(result.contains("%27"))
			result = result.replace("%27", "'");
		if(result.contains("%3D"))
			result = result.replace("%3D", "=");
		if(result.contains("%3B"))
			result = result.replace("%3B", ";");
		if(result.contains("%28"))
			result = result.replace("%28", "(");
		if(result.contains("%29"))
			result = result.replace("%29", ")");
		return result;
	}
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	public Response get() { 
		 logger.debug("[NotificationServices] GET Request");
		 String result = "Data GET";
		 return Response.status(201).entity(result).build(); 
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createDataInJSON(String data) { 
		logger.debug("[NotificationServices] POST Request");
		ObjectMapper mapper = new ObjectMapper();
		//JSON from String to Object
		try {
			NotificationContent notificationContent = mapper.readValue(data, NotificationContent.class);
			logger.debug("notificationContent="+notificationContent);
			
			// TODO: add notification management
			for (it.eng.fimind.model.Entity entity:notificationContent.getData()) {
				logger.debug("entity.getType()="+entity.getType());
				
				data = unmapForbiddenChars(data);
				
				if (entity.getType().equalsIgnoreCase("Alert")){
					logger.debug("Alert");
					logger.debug("data="+data);
				 					 					
					AlertNormalized alertNotified = new AlertNormalized();	
					alertNotified = mapper.readValue( mapper.writeValueAsString(entity), AlertNormalized.class);
					
					AlertNormalizedServices alertNormalizedServices = new AlertNormalizedServices();		
					alertNormalizedServices.createDataInJSON(null, alertNotified);				 
				}
				if (entity.getType().equalsIgnoreCase("AssetAdministrationShell")){
					logger.debug("AssetAdministrationShell");
					logger.debug("data="+data);
				 					 
					AssetAdministrationShell aasNotified = new AssetAdministrationShell();	
					aasNotified = mapper.readValue( mapper.writeValueAsString(entity), AssetAdministrationShell.class);
					
					AssetAdministrationShellServices assetAdministrationShellServices = new AssetAdministrationShellServices();		
					assetAdministrationShellServices.createDataInJSON(null,aasNotified);				 
				}
				if (entity.getType().equalsIgnoreCase("Building")){
					logger.debug("Building");
					logger.debug("data="+data);
				 					 
					BuildingNormalized buildingNotified = new BuildingNormalized();	
					buildingNotified = mapper.readValue( mapper.writeValueAsString(entity), BuildingNormalized.class);
					
					BuildingNormalizedServices buildingNormalizedServices = new BuildingNormalizedServices();		
					buildingNormalizedServices.createDataInJSON(null, buildingNotified);				 
				}
				if (entity.getType().equalsIgnoreCase("BuildingOperation")){
					logger.debug("BuildingOperation");
					logger.debug("data="+data);
				 					 
					BuildingOperationNormalized buildingOperationNormalized = new BuildingOperationNormalized();	
					buildingOperationNormalized = mapper.readValue( mapper.writeValueAsString(entity), BuildingOperationNormalized.class);
					
					BuildingOperationNormalizedServices buildingOperationNormalizedServices = new BuildingOperationNormalizedServices();		
					buildingOperationNormalizedServices.createDataInJSON(null, buildingOperationNormalized);				 
				}
				if (entity.getType().equalsIgnoreCase("Device")){
					logger.debug("Device");
					logger.debug("data="+data);
				 					 
					DeviceNormalized deviceNotified = new DeviceNormalized();	
					deviceNotified = mapper.readValue( mapper.writeValueAsString(entity), DeviceNormalized.class);
					
					DeviceNormalizedServices deviceNormalizedServices = new DeviceNormalizedServices();		
					String val = (String) entity.getAttributes().get("value").getValue();
					String resultVal = java.net.URLDecoder.decode(val, StandardCharsets.UTF_8.name());
						
					Date now = new Date();
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
					String instant = df.format(now);
					
					TimeInstant ti = new TimeInstant();
					ti.setValue(instant);
					ti.setType("DateTime");
					Metadata mt = new Metadata();			
					mt.setTimeInstant(ti);
					
					deviceNotified.getValue().setMetadata(mt);
					deviceNotified.getValue().setValue(resultVal);	
			        deviceNormalizedServices.createDataInJSON(null, deviceNotified);				 
				}
				if (entity.getType().equalsIgnoreCase("DeviceModel")) {
					logger.debug("DeviceModel");
					logger.debug("data="+data);
					
				    DeviceModelNormalized deviceModelNotified = new DeviceModelNormalized();				    
				    mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
				    deviceModelNotified = mapper.readValue( mapper.writeValueAsString(entity), DeviceModelNormalized.class);
					
					DeviceModelNormalizedServices deviceModelNormalizedServices = new DeviceModelNormalizedServices();
					deviceModelNormalizedServices.createDataInJSON(null, deviceModelNotified);
				}
				if (entity.getType().equalsIgnoreCase("TrafficFlowObserved")) {
					logger.debug("TrafficFlowObserved");
					logger.debug("data="+data);
					
					TrafficFlowObservedNormalized trafficFlowObservedNotified = new TrafficFlowObservedNormalized();				    
				    mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
				    trafficFlowObservedNotified = mapper.readValue( mapper.writeValueAsString(entity), TrafficFlowObservedNormalized.class);
					
				    TrafficFlowObservedNormalizedServices trafficFlowObservedNormalizedServices = new TrafficFlowObservedNormalizedServices();
				    trafficFlowObservedNormalizedServices.createDataInJSON(null, trafficFlowObservedNotified);
				}
				if (entity.getType().equalsIgnoreCase("Vehicle")) {
					logger.debug("Vehicle");
					logger.debug("data="+data);
					
					VehicleNormalized vehicleNotified = new VehicleNormalized();				    
				    mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
				    vehicleNotified = mapper.readValue( mapper.writeValueAsString(entity), VehicleNormalized.class);
					
				    VehicleNormalizedServices vehicleNormalizedServices = new VehicleNormalizedServices();
				    vehicleNormalizedServices.createDataInJSON(null, vehicleNotified);				
				}
				if (entity.getType().equalsIgnoreCase("VehicleModel")) {
					logger.debug("VehicleModel");
					logger.debug("data="+data);
					
					VehicleModelNormalized vehicleModelNotified = new VehicleModelNormalized();				    
				    mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
				    vehicleModelNotified = mapper.readValue( mapper.writeValueAsString(entity), VehicleModelNormalized.class);
					
				    VehicleModelNormalizedServices vehicleModelNormalizedServices = new VehicleModelNormalizedServices();
				    vehicleModelNormalizedServices.createDataInJSON(null, vehicleModelNotified);				
				}
				if (entity.getType().equalsIgnoreCase("WeatherForecast")) {
					logger.debug("WeatherForecast");
					logger.debug("data="+data);
					
					WeatherForecastNormalized weatherForecastNotified = new WeatherForecastNormalized();				    
				    mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
				    weatherForecastNotified = mapper.readValue( mapper.writeValueAsString(entity), WeatherForecastNormalized.class);
					
				    WeatherForecastNormalizedServices weatherForecastNormalizedServices = new WeatherForecastNormalizedServices();
				    weatherForecastNormalizedServices.createDataInJSON(null, weatherForecastNotified);				
				}
				if (entity.getType().equalsIgnoreCase("WeatherObserved")) {
					logger.debug("WeatherObserved");
					logger.debug("data="+data);
					
					WeatherObservedNormalized weatherObservedNotified = new WeatherObservedNormalized();				    
				    mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
				    weatherObservedNotified = mapper.readValue( mapper.writeValueAsString(entity), WeatherObservedNormalized.class);
					
				    WeatherObservedNormalizedServices weatherObservedNormalizedServices = new WeatherObservedNormalizedServices();
				    weatherObservedNormalizedServices.createDataInJSON(null, weatherObservedNotified);				
				}
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		String result = "Data post: "+data;
		return Response.status(201).entity(result).build(); 
	}
	
	
	 
}
