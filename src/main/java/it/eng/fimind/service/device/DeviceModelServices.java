package it.eng.fimind.service.device;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.siemens.mindsphere.sdk.timeseries.model.Timeseries;

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
		
		createMindSphereTimeSeriesFromDeviceModel(deviceModel);
		
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
		
		
		List<String> keys = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		keys.add("Source");
		values.add(deviceModel.getSource());
		keys.add("DataProvider");
		values.add(deviceModel.getDataProvider());
		keys.add("Category");
		values.add(deviceModel.getCategory().toString());
		keys.add("DeviceClass");
		values.add(deviceModel.getDeviceClass());
		keys.add("Function");
		values.add(deviceModel.getFunction().toString());
		keys.add("SupportedProtocol");
		values.add(deviceModel.getSupportedProtocol().toString());
		keys.add("SupportedUnits");
		values.add(deviceModel.getSupportedUnits().toString());
		keys.add("EnergyLimitationClass");
		values.add(deviceModel.getEnergyLimitationClass());
		keys.add("BrandName");
		values.add(deviceModel.getBrandName());
		keys.add("ModelName");
		values.add(deviceModel.getModelName());
		keys.add("ManufacturerName");
		values.add(deviceModel.getManufacturerName());
		keys.add("Name");
		values.add(deviceModel.getName());
		keys.add("Documentation");
		values.add(deviceModel.getDocumentation());
		keys.add("Image");
		values.add(deviceModel.getDocumentation());
		keys.add("DateModified");
		values.add(deviceModel.getDateModified());
		keys.add("DateCreated");
		values.add(deviceModel.getDateCreated());
		List<Variable> assetVariables = mindSphereMapper.fiPropertiesToMiVariables(keys, values);
		
	
		List<String> properties = new ArrayList<String>();
		List<String> uoms = new ArrayList<String>();
		for (int i=0; i<deviceModel.getControlledProperty().size(); i++) {
			String property = deviceModel.getControlledProperty().get(i);
			String uom = deviceModel.getSupportedUnits().get(i);
			properties.add(property);
			uoms.add(uom);
		}
		AspectType aspectType = mindSphereMapper.fiStateToMiAspectType(deviceModel.getId(), deviceModel.getDescription(), properties, uoms);
		
		
		return mindSphereGateway.createAsset(deviceModel.getId(), assetVariables, aspectType);
	}
	
	private boolean saveMindSphereAsset(Asset asset) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		logger.debug("DeviceModel created");
		return mindSphereGateway.saveAsset(asset);
	}
	
	public boolean createMindSphereTimeSeriesFromDeviceModel(DeviceModel deviceModel) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+deviceModel.getId()+"Asset\"}");
		try {
			List<Timeseries> timeSeriesList = new ArrayList<Timeseries>();
			Date now = new Date();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			String instant = df.format(now);
			Timeseries timeseriesPoint = new Timeseries();
			timeseriesPoint.getFields().put("_time", instant);


			for (int i=0; i<deviceModel.getControlledProperty().size(); i++) {
				String property = deviceModel.getControlledProperty().get(i);
				String value = deviceModel.getSupportedUnits().get(i);
				timeseriesPoint.getFields().put(property,value);
			}
			
			timeSeriesList.add(timeseriesPoint);
			mindSphereGateway.putTimeSeries(assets.get(0).getAssetId(), deviceModel.getId()+"AspectType", timeSeriesList);
		} catch (Exception e) {
			// Exception handling
			e.printStackTrace();
			return false;
		}
		return true;
	}	

}
