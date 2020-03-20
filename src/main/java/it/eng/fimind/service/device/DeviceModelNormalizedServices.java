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

import it.eng.fimind.model.fiware.device.DeviceModelNormalized;
import it.eng.fimind.util.MindSphereGateway;
import it.eng.fimind.util.MindSphereMapper;
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
		
		if(!deviceModelDoesAlreadyExist(deviceModel)) 
			saveMindSphereAsset(createMindSphereAssetFromDeviceModel(deviceModel));
		
		serviceResult.setResult("OK");
		return Response.status(201).entity(serviceResult).build();
	}
	
	private Boolean deviceModelDoesAlreadyExist(DeviceModelNormalized deviceModel)
	{
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+deviceModel.getId()+"Asset\"}");
		return assets.size()>0;
	}
	
	private Asset createMindSphereAssetFromDeviceModel(DeviceModelNormalized deviceModel) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		MindSphereMapper mindSphereMapper = new MindSphereMapper();
		
		deviceModel.setId(deviceModel.getId().replaceAll("-","_"));

		List<String> keys = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		if(deviceModel.getSource()!=null) {
			keys.add("Source");
			values.add((String)deviceModel.getSource().getValue());
		}
		if(deviceModel.getDataProvider()!=null) {
			keys.add("DataProvider");
			values.add((String)deviceModel.getDataProvider().getValue());
		}
		if(deviceModel.getCategory()!=null) {
			keys.add("Category");
			values.add((String)deviceModel.getCategory().getValue().toString());
		}
		if(deviceModel.getDeviceClass()!=null) {
			keys.add("DeviceClass");
			values.add((String)deviceModel.getDeviceClass().getValue());
		}
		if(deviceModel.getFunction()!=null) {
			keys.add("Function");
			values.add((String)deviceModel.getFunction().getValue().toString());
		}
		if(deviceModel.getSupportedProtocol()!=null) {
			keys.add("SupportedProtocol");
			values.add((String)deviceModel.getSupportedProtocol().getValue().toString());
		}
		if(deviceModel.getSupportedUnits()!=null) {
			keys.add("SupportedUnits");
			values.add((String)deviceModel.getSupportedUnits().getValue().toString());
		}
		if(deviceModel.getEnergyLimitationClass()!=null) {
			keys.add("EnergyLimitationClass");		
			values.add((String)deviceModel.getEnergyLimitationClass().getValue());
		}
		if(deviceModel.getBrandName()!=null) {
			keys.add("BrandName");
			values.add((String)deviceModel.getBrandName().getValue());
		}
		if(deviceModel.getModelName()!=null) {
			keys.add("ModelName");
			values.add((String)deviceModel.getModelName().getValue());
		}
		if(deviceModel.getManufacturerName()!=null) {
			keys.add("ManufacturerName");
			values.add((String)deviceModel.getManufacturerName().getValue());
		}
		if(deviceModel.getName()!=null) {
			keys.add("Name");
			values.add((String)deviceModel.getName().getValue());
		}
		if(deviceModel.getDocumentation()!=null) {
			keys.add("Documentation");
			values.add((String)deviceModel.getDocumentation().getValue());
		}
		if(deviceModel.getImage()!=null) {
			keys.add("Image");
			values.add((String)deviceModel.getImage().getValue());
		}
		if(deviceModel.getDateModified()!=null) {
			keys.add("DateModified");
			values.add((String)deviceModel.getDateModified().getValue());
		}
		if(deviceModel.getDateCreated()!=null) {
			keys.add("DateCreated");
			values.add((String) deviceModel.getDateCreated().getValue());
		}
		List<VariableDefinition> assetVariablesDefinitions = mindSphereMapper.fiPropertiesToMiVariablesDefinitions(keys, values);
		List<Variable> assetVariables = mindSphereMapper.fiPropertiesToMiVariables(keys, values);

	
		List<String> properties = new ArrayList<String>();
		List<String> uoms = new ArrayList<String>();
		List<String> dataTypes = new ArrayList<String>();
		if(deviceModel.getControlledProperty()!=null) {
			for (int i=0; i<deviceModel.getControlledProperty().getValue().size(); i++) {
				String property = (String) deviceModel.getControlledProperty().getValue().get(i);
				String uom = (String) deviceModel.getSupportedUnits().getValue().get(i);
				properties.add(property);
				uoms.add(uom);
				dataTypes.add("Double");
			}
		}
		AspectType aspectType = mindSphereMapper.fiStateToMiAspectType(deviceModel.getId(), (String) deviceModel.getDescription().getValue(), properties, uoms, dataTypes);
		
		
		return mindSphereGateway.createAsset(deviceModel.getId(), assetVariablesDefinitions, assetVariables, aspectType);
	}
	
	private Boolean saveMindSphereAsset(Asset asset) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		Boolean result = mindSphereGateway.saveAsset(asset);
		if(result)
			logger.debug("DeviceModelNormalized created");
		else 		
			logger.error("DeviceModelNormalized couldn't be created");
		return result;
	}
	
}
