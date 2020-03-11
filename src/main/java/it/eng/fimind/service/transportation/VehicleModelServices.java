package it.eng.fimind.service.transportation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
import com.siemens.mindsphere.sdk.assetmanagement.model.AspectType.CategoryEnum;
import com.siemens.mindsphere.sdk.assetmanagement.model.AspectType.ScopeEnum;
import com.siemens.mindsphere.sdk.assetmanagement.model.AspectVariable;
import com.siemens.mindsphere.sdk.assetmanagement.model.AspectVariable.DataTypeEnum;
import com.siemens.mindsphere.sdk.assetmanagement.model.AssetResource;
import com.siemens.mindsphere.sdk.timeseries.model.Timeseries;

import it.eng.fimind.model.fiware.transportation.VehicleModel;
import it.eng.fimind.util.MindSphereGateway;
import it.eng.fimind.util.ServiceResult;

/**
 * Root resource (exposed at "vehiclemodel" path)
 */
@Path("vehiclemodel")
public class VehicleModelServices {
	private static Logger logger = Logger.getLogger(VehicleModelServices.class);
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getIt() {
		return "VehicleModel Services : got it!!";
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createDataInJSON(@Valid VehicleModel vehicleModel) { 
		ServiceResult serviceResult=new ServiceResult();
		logger.debug("Id ="+vehicleModel.getId());
		if(!vehicleModelDoesAlreadyExist(vehicleModel)) {
			createMindSphereAssetFromVehicleModel(vehicleModel);
		}
		createMindSphereTimeSeriesFromVehicleModel(vehicleModel);
		
		serviceResult.setResult("OK");
		return Response.status(201).entity(serviceResult).build();
	}

	
	private Boolean vehicleModelDoesAlreadyExist(VehicleModel vehicleModel)
	{
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+vehicleModel.getId()+"Asset\"}");
		return assets.size()>0;
	}
	
	private boolean createMindSphereAssetFromVehicleModel(VehicleModel vehicleModel) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		AspectType aspectType = new AspectType();
		
		aspectType.setName((String) vehicleModel.getId()+"Aspect");
		aspectType.setDescription((String) vehicleModel.getDescription());
		aspectType.setScope(ScopeEnum.PRIVATE);
		aspectType.setCategory(CategoryEnum.DYNAMIC);
		
		List<AspectVariable> variables=new ArrayList<AspectVariable>();

		List<String> properties = Stream.of("FuelConsumption").collect(Collectors.toList());
		List<String> uoms = Stream.of("l/100km").collect(Collectors.toList());

		for(int i=0; i<properties.size();i++) {
			AspectVariable var = new AspectVariable();
			var.setName(properties.get(i));
			var.setDataType(DataTypeEnum.STRING);
			var.setLength(20);
			var.setUnit(uoms.get(i));
			var.setSearchable(true);
			var.setQualityCode(true);
			variables.add(var);
		}
		
		aspectType.setVariables(variables);
		mindSphereGateway.createAsset(vehicleModel.getId(), aspectType);
		logger.debug("VehicleModel created");
		return true;
	}
	
	private boolean createMindSphereTimeSeriesFromVehicleModel(VehicleModel vehicleModel) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+vehicleModel.getId()+"Asset\"}");
		try {
			List<Timeseries> timeSeriesList = new ArrayList<Timeseries>();
			Date now = new Date();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			String instant = df.format(now);
			Timeseries timeseriesPoint = new Timeseries();
			timeseriesPoint.getFields().put("_time", instant);
		
			timeseriesPoint.getFields().put("FuelConsumption",vehicleModel.getFuelConsumption());

			timeSeriesList.add(timeseriesPoint);
			mindSphereGateway.putTimeSeries(assets.get(0).getAssetId(), vehicleModel.getId()+"AspectType", timeSeriesList);
			logger.debug("VehicleModel updated");

		} catch (Exception e) {
			// Exception handling
			e.printStackTrace();
			return false;
		}
		return true;
	}	
}
