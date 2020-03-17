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
import it.eng.fimind.model.fiware.common.Metadata;
import it.eng.fimind.model.fiware.common.TimeInstant;
import it.eng.fimind.model.fiware.device.DeviceModelNormalized;
import it.eng.fimind.model.fiware.device.DeviceNormalized;
import it.eng.fimind.service.device.DeviceModelNormalizedServices;
import it.eng.fimind.service.device.DeviceNormalizedServices;


@Path("fiware-notification")
public class NotificationServices {
	private static Logger logger = Logger.getLogger(NotificationServices.class);
	
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
				
				if (entity.getType().equalsIgnoreCase("Device")){
					logger.debug("Device");
					logger.debug("data="+data);
				 	  
				 
					DeviceNormalized deviceNotified = new DeviceNormalized();
					
					deviceNotified = mapper.readValue( mapper.writeValueAsString(entity), DeviceNormalized.class);
					
					DeviceNormalizedServices deviceNormalizedServices=new DeviceNormalizedServices();
					
					String val = (String) entity.getAttributes().get("value").getValue();
					
					String resultVal = java.net.URLDecoder.decode(val, StandardCharsets.UTF_8.name());
					
					Date now=new Date();
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
					String instant = df.format(now);
				
					
					TimeInstant ti= new TimeInstant();
					ti.setValue(instant);
 					ti.setType("DateTime");
					Metadata mt=new Metadata();
					
					mt.setTimeInstant(ti);
					
					deviceNotified.getValue().setMetadata(mt);
					deviceNotified.getValue().setValue(resultVal);
					
			        deviceNormalizedServices.createDataInJSON(deviceNotified);				 
				}
				if (entity.getType().equalsIgnoreCase("DeviceModel")) {
					logger.debug("DeviceModel");
					logger.debug("data="+data);
					
				    DeviceModelNormalized deviceModelNotified = new DeviceModelNormalized();
				    
				    mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
                    
				    deviceModelNotified = mapper.readValue( mapper.writeValueAsString(entity), DeviceModelNormalized.class);
					
					DeviceModelNormalizedServices deviceModelNormalizedServices=new DeviceModelNormalizedServices();
					
					deviceModelNormalizedServices.createDataInJSON(deviceModelNotified);
					
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
