package it.eng.fimind.service.device;


import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.siemens.mindsphere.sdk.iot.asset.model.AspectTypeDto;
import com.siemens.mindsphere.sdk.iot.asset.model.AspectVariable;
import com.siemens.mindsphere.sdk.iot.asset.model.CategoryEnum;
import com.siemens.mindsphere.sdk.iot.asset.model.DataTypeEnum;
import com.siemens.mindsphere.sdk.iot.asset.model.ScopeEnum;

import it.eng.fimind.model.fiware.device.DeviceModel;
import it.eng.fimind.util.MindSphereGateway;
import it.eng.fimind.util.ServiceResult;

/**
 * Root resource (exposed at "device" path)
 */
@Path("deviceModel")
public class DeviceModelServices {
	private static Logger logger = Logger.getLogger(DeviceModelServices.class);

	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getIt() {
		return "Device Model Service: got it!!";
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createDataInJSON(@Valid DeviceModel deviceModel) { 
		ServiceResult serviceResult=new ServiceResult();
		logger.debug("Id ="+deviceModel.getId());
		createMindSphereAssetFromDeviceModel(deviceModel);
		serviceResult.setResult("OK");
		return Response.status(201).entity(serviceResult).build();
	}
	
	private boolean createMindSphereAssetFromDeviceModel(DeviceModel deviceModel) {
		MindSphereGateway mindSphereGateway=MindSphereGateway.getMindSphereGateway();
		AspectTypeDto aspectTypeDto=new AspectTypeDto();
		
		aspectTypeDto.setName(deviceModel.getId()+"Aspect");
		aspectTypeDto.setDescription(deviceModel.getDescription());
		aspectTypeDto.setScope(ScopeEnum.PRIVATE);
		aspectTypeDto.setCategory(CategoryEnum.DYNAMIC);
		List<AspectVariable> variables=new ArrayList<AspectVariable>();

		for (int i=0; i<deviceModel.getControlledProperty().size();i++) {
			String property=deviceModel.getControlledProperty().get(i);
			String uom="";
			if (deviceModel.getSupportedUnits()!=null)
				if (deviceModel.getSupportedUnits().get(i)!=null)
					uom=deviceModel.getSupportedUnits().get(i);
			AspectVariable var=new AspectVariable();
			var.setName(property);
			var.setDataType(DataTypeEnum.STRING);
			var.setLength(20);
			var.setUnit(uom);
			var.setSearchable(true);
			var.setQualitycode(true);
			variables.add(var);
		}
		
		aspectTypeDto.setVariables(variables);
		mindSphereGateway.createAsset(deviceModel.getId(), aspectTypeDto);
		logger.debug("Asset created");
		return true;
	}

	
}
