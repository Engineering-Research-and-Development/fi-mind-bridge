package it.eng.fimind.service;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.siemens.mindsphere.sdk.assetmanagement.model.AssetResource;
import com.siemens.mindsphere.sdk.assetmanagement.model.AssetTypeResource;

import it.eng.fimind.model.fiware.device.Device;
import it.eng.fimind.service.device.DeviceServices;
import it.eng.fimind.util.MindSphereGateway;
import it.eng.fimind.util.ServiceResult;

@Path("testDevice")
public class TestDeviceServices {
	private static Logger logger = Logger.getLogger(OCBServices.class);

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@QueryParam("id") String id) { 
		ServiceResult serviceResult = new ServiceResult();	
		logger.debug("[TestDeviceServices] GET Request");

		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		
		logger.debug(id);
		
		Device device = new Device();	
		device.setId(id);
		device.setBatteryLevel((float) 45.4);
		device.setType("Device");
		DeviceServices deviceServices = new DeviceServices();		
        deviceServices.createDataInJSON(null, device);				 
        
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+id+"\"}");
		List<AssetTypeResource> assetTypes = mindSphereGateway.getFilteredAssetTypes("ASC", "{\"id\":{\"in\":[\"engineer."+id+"\"]}}&exploded=true");
		if(assets.size()>0) {
			logger.debug("-----------asset----------");
			logger.debug(assets.get(0));
			logger.error("'aspects' element of upper response is empty -> "+ assets.get(0).getAspects());
			logger.debug("--------assetTypes--------");
			logger.debug(assetTypes.get(0));
			logger.error("'aspects' element of upper response is NOT empty -> " + assetTypes.get(0).getAspects());

		}
		
		return Response.status(200).entity(serviceResult).build();
	}
}
