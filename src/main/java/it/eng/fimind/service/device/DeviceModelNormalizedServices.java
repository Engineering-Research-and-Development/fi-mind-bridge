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

import com.siemens.mindsphere.sdk.assetmanagement.model.AspectType;
import com.siemens.mindsphere.sdk.assetmanagement.model.AspectVariable;
import com.siemens.mindsphere.sdk.assetmanagement.model.AspectType.CategoryEnum;
import com.siemens.mindsphere.sdk.assetmanagement.model.AspectVariable.DataTypeEnum;
import com.siemens.mindsphere.sdk.assetmanagement.model.AspectType.ScopeEnum;

import it.eng.fimind.model.fiware.device.DeviceModelNormalized;
import it.eng.fimind.util.MindSphereGateway;
import it.eng.fimind.util.ServiceResult;

@Path("deviceModelNormalized")
public class DeviceModelNormalizedServices {

	private static Logger logger = Logger.getLogger(DeviceModelNormalizedServices.class);

	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getIt() {
		return "Device Model Service: got it!!";
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createDataInJSON(@Valid DeviceModelNormalized deviceModel) { 
		ServiceResult serviceResult = new ServiceResult();
		logger.debug("Id ="+deviceModel.getId());
		createMindSphereAssetFromDeviceModel(deviceModel);
		serviceResult.setResult("OK");
		return Response.status(201).entity(serviceResult).build();
	}
	
	private boolean createMindSphereAssetFromDeviceModel(DeviceModelNormalized deviceModel) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		AspectType aspectTypeDto=new AspectType();
		
		aspectTypeDto.setName((String) deviceModel.getId()+"Aspect");
		aspectTypeDto.setDescription((String) deviceModel.getDescription().getValue());
		aspectTypeDto.setScope(ScopeEnum.PRIVATE);
		aspectTypeDto.setCategory(CategoryEnum.DYNAMIC);
		List<AspectVariable> variables=new ArrayList<AspectVariable>();

		for (int i=0; i<deviceModel.getControlledProperty().getValue().size();i++) {
			String property = (String) deviceModel.getControlledProperty().getValue().get(i);
			String uom = "";
			if (deviceModel.getSupportedUnits()!=null)
				if (deviceModel.getSupportedUnits().getValue().get(i)!=null)
					uom = (String) deviceModel.getSupportedUnits().getValue().get(i);
			AspectVariable var=new AspectVariable();
			var.setName(property);
			var.setDataType(DataTypeEnum.STRING);
			var.setLength(20);
			var.setUnit(uom);
			var.setSearchable(true);
			var.setQualityCode(true);
			variables.add(var);
		}
		
		aspectTypeDto.setVariables(variables);
		mindSphereGateway.createAsset(deviceModel.getId(), aspectTypeDto);
		logger.debug("Asset created");
		return true;
	}
}
