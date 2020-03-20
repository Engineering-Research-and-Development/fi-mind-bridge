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
import com.siemens.mindsphere.sdk.assetmanagement.model.Asset;
import com.siemens.mindsphere.sdk.assetmanagement.model.AssetResource;
import com.siemens.mindsphere.sdk.assetmanagement.model.Variable;
import com.siemens.mindsphere.sdk.assetmanagement.model.VariableDefinition;

import it.eng.fimind.model.fiware.device.DeviceModel;
import it.eng.fimind.util.MindSphereGateway;
import it.eng.fimind.util.MindSphereMapper;
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
		
		if(!deviceModelDoesAlreadyExist(deviceModel)) 
			saveMindSphereAsset(createMindSphereAssetFromDeviceModel(deviceModel));
				
		serviceResult.setResult("OK");
		return Response.status(201).entity(serviceResult).build();
	}
	
	private Boolean deviceModelDoesAlreadyExist(DeviceModel deviceModel)
	{
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+deviceModel.getId()+"Asset\"}");
		return assets.size()>0;
	}
	
	public Asset createMindSphereAssetFromDeviceModel(DeviceModel deviceModel) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		MindSphereMapper mindSphereMapper = new MindSphereMapper();
		
		deviceModel.setId(deviceModel.getId().replaceAll("-","_"));
		
		List<String> keys = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		if(deviceModel.getSource()!=null) {
			keys.add("Source");
			values.add(deviceModel.getSource());
		}
		if(deviceModel.getDataProvider()!=null) {
			keys.add("DataProvider");
			values.add(deviceModel.getDataProvider());
		}
		if(deviceModel.getCategory()!=null) {
			keys.add("Category");
			values.add(deviceModel.getCategory().toString());
		}
		if(deviceModel.getDeviceClass()!=null) {
			keys.add("DeviceClass");
			values.add(deviceModel.getDeviceClass());
		}
		if(deviceModel.getFunction()!=null) {
			keys.add("Function");
			values.add(deviceModel.getFunction().toString());
		}
		if(deviceModel.getSupportedProtocol()!=null) {
			keys.add("SupportedProtocol");
			values.add(deviceModel.getSupportedProtocol().toString());
		}
		if(deviceModel.getSupportedUnits()!=null) {
			keys.add("SupportedUnits");
			values.add(deviceModel.getSupportedUnits().toString());
		}
		if(deviceModel.getEnergyLimitationClass()!=null) {
			keys.add("EnergyLimitationClass");		
			values.add(deviceModel.getEnergyLimitationClass());
		}
		if(deviceModel.getBrandName()!=null) {
			keys.add("BrandName");
			values.add(deviceModel.getBrandName());
		}
		if(deviceModel.getModelName()!=null) {
			keys.add("ModelName");
			values.add(deviceModel.getModelName());
		}
		if(deviceModel.getManufacturerName()!=null) {
			keys.add("ManufacturerName");
			values.add(deviceModel.getManufacturerName());
		}
		if(deviceModel.getName()!=null) {
			keys.add("Name");
			values.add(deviceModel.getName());
		}
		if(deviceModel.getDocumentation()!=null) {
			keys.add("Documentation");
			values.add(deviceModel.getDocumentation());
		}
		if(deviceModel.getImage()!=null) {
			keys.add("Image");
			values.add(deviceModel.getImage());
		}
		if(deviceModel.getDateModified()!=null) {
			keys.add("DateModified");
			values.add(deviceModel.getDateModified());
		}
		if(deviceModel.getDateCreated()!=null) {
			keys.add("DateCreated");
			values.add(deviceModel.getDateCreated());
		}
		List<VariableDefinition> assetVariablesDefinitions = mindSphereMapper.fiPropertiesToMiVariablesDefinitions(keys, values);
		List<Variable> assetVariables = mindSphereMapper.fiPropertiesToMiVariables(keys, values);
			
		
		List<String> properties = new ArrayList<String>();
		List<String> uoms = new ArrayList<String>();
		List<String> dataTypes = new ArrayList<String>();
		if(deviceModel.getControlledProperty()!=null && deviceModel.getSupportedUnits()!=null) {
			for (int i=0; i<deviceModel.getControlledProperty().size(); i++) {
				String property = deviceModel.getControlledProperty().get(i);
				String uom = deviceModel.getSupportedUnits().get(i);
				properties.add(property);
				uoms.add(uom);
				dataTypes.add("Double");
			}
		}
		AspectType aspectType = mindSphereMapper.fiStateToMiAspectType(deviceModel.getId(), deviceModel.getDescription(), properties, uoms, dataTypes);
		
		return mindSphereGateway.createAsset(deviceModel.getId(), assetVariablesDefinitions, assetVariables, aspectType);
	}
	
	private Boolean saveMindSphereAsset(Asset asset) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		Boolean result = mindSphereGateway.saveAsset(asset);
		if(result)
			logger.debug("DeviceModel created");
		else 		
			logger.error("DeviceModel couldn't be created");
		return result;
	}

}
