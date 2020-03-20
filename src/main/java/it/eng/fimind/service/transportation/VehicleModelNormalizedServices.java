package it.eng.fimind.service.transportation;

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

import it.eng.fimind.model.fiware.transportation.VehicleModelNormalized;
import it.eng.fimind.util.MindSphereGateway;
import it.eng.fimind.util.MindSphereMapper;
import it.eng.fimind.util.ServiceResult;

/**
 * Root resource (exposed at "vehiclemodelnormalized" path)
 */
@Path("vehicleModelNormalized")
public class VehicleModelNormalizedServices {
	private static Logger logger = Logger.getLogger(VehicleModelNormalizedServices.class);
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getIt() {
		return "VehicleModelNormalized Services : got it!!";
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createDataInJSON(@Valid VehicleModelNormalized vehicleModel) { 
		ServiceResult serviceResult = new ServiceResult();
		logger.debug("Id ="+vehicleModel.getId());
		
		if(!vehicleModelDoesAlreadyExist(vehicleModel)) 
			saveMindSphereAsset(createMindSphereAssetFromVehicleModel(vehicleModel));
				
		serviceResult.setResult("OK");
		return Response.status(201).entity(serviceResult).build();
	}

	
	private Boolean vehicleModelDoesAlreadyExist(VehicleModelNormalized vehicleModel)
	{
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+vehicleModel.getId()+"Asset\"}");
		return assets.size()>0;
	}
	
	private Asset createMindSphereAssetFromVehicleModel(VehicleModelNormalized vehicleModel) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		MindSphereMapper mindSphereMapper = new MindSphereMapper();
		
		vehicleModel.setId(vehicleModel.getId().replaceAll("-","_"));

		List<String> keys = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		if(vehicleModel.getSource()!=null) {
			keys.add("Source");
			values.add((String) vehicleModel.getSource().getValue());
		}
		if(vehicleModel.getDataProvider()!=null) {
			keys.add("DataProvider");
			values.add((String) vehicleModel.getDataProvider().getValue());
		}
		if(vehicleModel.getName()!=null) {
			keys.add("Name");
			values.add((String) vehicleModel.getName().getValue());
		}
		if(vehicleModel.getVehicleType()!=null) {
			keys.add("VehicleType");
			values.add((String) vehicleModel.getVehicleType().getValue());
		}
		if(vehicleModel.getBrandName()!=null) {
			keys.add("BrandName");
			values.add((String) vehicleModel.getBrandName().getValue());
		}
		if(vehicleModel.getModelName()!=null) {
			keys.add("ModelName");
			values.add((String) vehicleModel.getModelName().getValue());
		}
		if(vehicleModel.getManufacturerName()!=null) {
			keys.add("ManufacturerName");
			values.add((String) vehicleModel.getManufacturerName().getValue());
		}
		if(vehicleModel.getVehicleModelDate()!=null) {
			keys.add("VehicleModelDate");
			values.add((String) vehicleModel.getVehicleModelDate().getValue());
		}
		if(vehicleModel.getCargoVolume()!=null) {
			keys.add("CargoVolume");
			values.add((String) vehicleModel.getCargoVolume().getValue());
		}
		if(vehicleModel.getFuelType()!=null) {
			keys.add("FuelType");
			values.add((String) vehicleModel.getFuelType().getValue());
		}
		if(vehicleModel.getFuelConsumption()!=null) {
			keys.add("FuelConsumption");
			values.add((String) vehicleModel.getFuelConsumption().getValue());
		}
		if(vehicleModel.getHeight()!=null) {
			keys.add("Height");
			values.add((String) vehicleModel.getHeight().getValue());
		}
		if(vehicleModel.getWidth()!=null) {
			keys.add("Width");
			values.add((String) vehicleModel.getWidth().getValue());
		}
		if(vehicleModel.getDepth()!=null) {
			keys.add("Depth");
			values.add((String) vehicleModel.getDepth().getValue());
		}
		if(vehicleModel.getWeight()!=null) {
			keys.add("Weight");
			values.add((String) vehicleModel.getWeight().getValue());
		}
		if(vehicleModel.getVehicleEngine()!=null) {
			keys.add("VehicleEngine");
			values.add((String) vehicleModel.getVehicleEngine().getValue());
		}
		if(vehicleModel.getUrl()!=null) {
			keys.add("Url");
			values.add((String) vehicleModel.getUrl().getValue());
		}
		if(vehicleModel.getImage()!=null) {
			keys.add("Image");
			values.add((String) vehicleModel.getImage().getValue());
		}
		if(vehicleModel.getDateModified()!=null) {
			keys.add("DateModified");
			values.add((String) vehicleModel.getDateModified().getValue());
		}
		if(vehicleModel.getDateCreated()!=null) {
			keys.add("DateCreated");
			values.add((String) vehicleModel.getDateCreated().getValue());
		}
		List<VariableDefinition> assetVariablesDefinitions = mindSphereMapper.fiPropertiesToMiVariablesDefinitions(keys, values);
		List<Variable> assetVariables = mindSphereMapper.fiPropertiesToMiVariables(keys, values);

		
		List<String> properties = new ArrayList<String>();
		List<String> uoms = new ArrayList<String>();
		List<String> dataTypes = new ArrayList<String>();
		AspectType aspectType = mindSphereMapper.fiStateToMiAspectType(vehicleModel.getId(), (String) vehicleModel.getDescription().getValue(), properties, uoms, dataTypes);
		
		
		return mindSphereGateway.createAsset(vehicleModel.getId(), assetVariablesDefinitions, assetVariables, aspectType);
	}
	
	private Boolean saveMindSphereAsset(Asset asset) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		Boolean result = mindSphereGateway.saveAsset(asset);
		if(result)
			logger.debug("VehicleModelNormalized created");
		else 		
			logger.error("VehicleModelNormalized couldn't be created");
		return result;
	}
	
}

