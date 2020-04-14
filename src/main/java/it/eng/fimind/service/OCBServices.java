package it.eng.fimind.service;

import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.siemens.mindsphere.sdk.assetmanagement.model.AssetResource;
import com.siemens.mindsphere.sdk.assetmanagement.model.Location;
import com.siemens.mindsphere.sdk.assetmanagement.model.Variable;
import com.siemens.mindsphere.sdk.timeseries.model.Timeseries;

import it.eng.fimind.util.MindSphereGateway;
import it.eng.fimind.util.OCBGateway;
import it.eng.fimind.util.ServiceResult;

@Path("ocb-export")
public class OCBServices {
	private static Logger logger = Logger.getLogger(OCBServices.class);
		
	private String mapForbiddenChars(String value) {
		String result = value;
		if(result.contains("<"))
			result = result.replace("<", "%3C");
		if(result.contains(">"))
			result = result.replace(">", "%3E");
//		if(result.contains("\""))
//			result = result.replace("\"", "%22");
		if(result.contains("'"))
			result = result.replace("'", "%27");
		if(result.contains("="))
			result = result.replace("=", "%3D");
		if(result.contains(";"))
			result = result.replace(";", "%3B");
		if(result.contains("("))
			result = result.replace("(", "%28");
		if(result.contains(")"))
			result = result.replace(")", "%29");
		return result;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@HeaderParam("debug-mode") String debugMode, @Context UriInfo info) { 
		ServiceResult serviceResult = new ServiceResult();	
		logger.debug("[OCBServices] GET Request");
		
		String asset_id = info.getQueryParameters().getFirst("asset_id");

		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		OCBGateway ocbGateway = new OCBGateway();
		
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+asset_id+"Asset\"}");
		if(assets.size()>0) {
			AssetResource asset = assets.get(0);
						
			JsonObject ocb_payload = new JsonObject();
			ocb_payload.addProperty("id", asset_id);
			
			List<Variable> variables = asset.getVariables();
			for(int i=0;i<variables.size();i++) {
				Variable var = variables.get(i);
				
				if(var.getName().equals("entityType")) {
					ocb_payload.addProperty("type", var.getValue());				
				}else if (var.getName().equals("entityName"))
					ocb_payload.addProperty("name", var.getValue());				
				else
					ocb_payload.addProperty(var.getName(), var.getValue());
			}
			
			
			Location mindSphereLocation = asset.getLocation();
			if(mindSphereLocation.getLongitude()!=null && mindSphereLocation.getLatitude()!=null) {
				JsonObject pointLocation = new JsonObject();
				pointLocation.addProperty("type", "Point");
				JsonArray coordinates = new JsonArray();
				JsonPrimitive latitude = new JsonPrimitive(mindSphereLocation.getLatitude().toPlainString());
				coordinates.add(latitude);
				JsonPrimitive longitude = new JsonPrimitive(mindSphereLocation.getLongitude().toPlainString());
				coordinates.add(longitude);
				pointLocation.add("coordinates", coordinates);
				//TODO BigDecimal are represented as String
				ocb_payload.add("location", pointLocation);
			}
			if(mindSphereLocation.getCountry()!=null || mindSphereLocation.getLocality()!=null || mindSphereLocation.getStreetAddress()!=null) {
				JsonObject address = new JsonObject();

				if(mindSphereLocation.getCountry()!=null)
					address.addProperty("country", mindSphereLocation.getCountry());
				if(mindSphereLocation.getRegion()!=null)
					address.addProperty("region", mindSphereLocation.getRegion());
				if(mindSphereLocation.getLocality()!=null)
					address.addProperty("addressLocality", mindSphereLocation.getLocality());
				if(mindSphereLocation.getStreetAddress()!=null)
					address.addProperty("streetAddress", mindSphereLocation.getStreetAddress());
				if(mindSphereLocation.getPostalCode()!=null)
					address.addProperty("postalCode", mindSphereLocation.getPostalCode());
							
				ocb_payload.add("address", address);
			}
			
			
			List<Timeseries> timeSeriesList = mindSphereGateway.getTimeSeries(assets.get(0).getAssetId(), asset_id+"AspectType");
			Timeseries timeSeries = timeSeriesList.get(0); //should we fetch more than latest one?
			Map<String,Object> map = timeSeries.getFields();
			
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				String key = entry.getKey();
				Object valueObject = entry.getValue();
				
				if(valueObject instanceof Double)
					ocb_payload.addProperty(key, (Double) valueObject);
				else 
					ocb_payload.addProperty(key, (String) valueObject);
			}
			
			String type = ocb_payload.get("type").toString().replace("\"","");
			if(type.equals("Alert"))
			{
				String data = ocb_payload.get("data").toString();
				data = data.substring(2, data.length()-2);

				JsonObject dataObject = new JsonObject();

				String[] dataList = data.split(",");
				for(int i=0;i<dataList.length;i++) {
					String[] currentJsonObj = dataList[i].split("=");
					String key = currentJsonObj[0];
					String value = currentJsonObj[1];
					dataObject.addProperty(key, value);
				}
				
				ocb_payload.remove("data");		
				ocb_payload.add("data",dataObject);
			}
			else if(type.equals("WeatherForecast")) 
			{
				String minTemperature = ocb_payload.get("minTemperature").toString();
				String minFeelsLikeTemperature = ocb_payload.get("minFeelsLikeTemperature").toString();
				String minRelativeHumidity = ocb_payload.get("minRelativeHumidity").toString();
				
				JsonObject dayMinimum = new JsonObject();
				dayMinimum.addProperty("temperature", minTemperature);
				dayMinimum.addProperty("feelsLikeTemperature", minFeelsLikeTemperature);
				dayMinimum.addProperty("relativeHumidity", minRelativeHumidity);

				ocb_payload.remove("minTemperature");
				ocb_payload.remove("minFeelsLikeTemperature");
				ocb_payload.remove("minRelativeHumidity");
				ocb_payload.add("dayMinimum", dayMinimum);
				
				String maxTemperature = ocb_payload.get("maxTemperature").toString();
				String maxFeelsLikeTemperature = ocb_payload.get("maxFeelsLikeTemperature").toString();
				String maxRelativeHumidity = ocb_payload.get("maxRelativeHumidity").toString();
				
				JsonObject dayMaximum = new JsonObject();
				dayMaximum.addProperty("temperature", maxTemperature);
				dayMaximum.addProperty("feelsLikeTemperature", maxFeelsLikeTemperature);
				dayMaximum.addProperty("relativeHumidity", maxRelativeHumidity);

				ocb_payload.remove("maxTemperature");
				ocb_payload.remove("maxFeelsLikeTemperature");
				ocb_payload.remove("maxRelativeHumidity");
				ocb_payload.add("dayMaximum", dayMaximum);				
			}
		
			String ocb_payload_str = ocb_payload.toString();
			ocb_payload_str = mapForbiddenChars(ocb_payload_str);
			
			if(debugMode!=null && debugMode.equals("true")){
				System.out.println("DEBUG MODE FOR --- OCBServices ---");
				System.out.println(ocb_payload_str);
				serviceResult.setResult(ocb_payload_str);
			}else{
				ocbGateway.pushToOCB(ocb_payload_str);
				serviceResult.setResult("Entity imported succesfully to OCB!");
			}		
		}	
		return Response.status(200).entity(serviceResult).build();
	}
}