package it.eng.fimind.service.building;

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
import com.siemens.mindsphere.sdk.assetmanagement.model.Asset;
import com.siemens.mindsphere.sdk.assetmanagement.model.AssetResource;
import com.siemens.mindsphere.sdk.assetmanagement.model.Location;
import com.siemens.mindsphere.sdk.assetmanagement.model.Variable;
import com.siemens.mindsphere.sdk.timeseries.model.Timeseries;

import it.eng.fimind.model.fiware.building.Building;
import it.eng.fimind.util.MindSphereGateway;
import it.eng.fimind.util.MindSphereMapper;
import it.eng.fimind.util.ServiceResult;

/**
 * Root resource (exposed at "building" path)
 */
@Path("building")
public class BuildingServices {
	private static Logger logger = Logger.getLogger(BuildingServices.class);
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getIt() {
		return "Building Service: got it!!";
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createDataInJSON(@Valid Building building) { 
		ServiceResult serviceResult = new ServiceResult();
		logger.debug("Id ="+building.getId());
		
		if(!buildingDoesAlreadyExist(building)) 
			saveMindSphereAsset(createMindSphereAssetFromBuilding(building));
		
		createMindSphereTimeSeriesFromBuilding(building);
		
		serviceResult.setResult("OK");
		return Response.status(201).entity(serviceResult).build();
	}

	private Boolean buildingDoesAlreadyExist(Building building)
	{
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+building.getId()+"Asset\"}");
		return assets.size()>0;
	}
	
	public Asset createMindSphereAssetFromBuilding(Building building) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		MindSphereMapper mindSphereMapper = new MindSphereMapper();
		
		Location mindSphereLocation = null;
		if(building.getLocation().getType().equals("Point")) 
			mindSphereLocation = mindSphereMapper.fiLocationToMiLocation(building.getLocation());
		else 
			mindSphereLocation = mindSphereMapper.fiAddressToMiLocation(building.getAddress());
		
		
		List<String> keys = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		keys.add("Source");
		values.add(building.getSource());
		keys.add("DataProvider");
		values.add(building.getDataProvider());
		keys.add("DateModified");
		values.add(building.getDateModified());
		keys.add("DateCreated");
		values.add(building.getDateCreated());
		keys.add("Owner");
		values.add(building.getOwner().toString());
		keys.add("Category");
		values.add(building.getCategory());
		keys.add("Occupier");
		values.add(building.getOccupier().toString());
		keys.add("FloorsAboveGround");
		values.add(building.getFloorsAboveGround().toString());
		keys.add("FloorsBelowGround");
		values.add(building.getFloorsBelowGround().toString());
		keys.add("RefMap");
		values.add(building.getRefMap());
		keys.add("OpeningHours");
		values.add(building.getOpeningHours().toString());
		List<Variable> assetVariables = mindSphereMapper.fiPropertiesToMiVariables(keys, values);
		
	
		List<String> properties = Stream.of("OpeningHours").collect(Collectors.toList());
		List<String> uoms = Stream.of("Dimensionless").collect(Collectors.toList());
		AspectType aspectType = mindSphereMapper.fiStateToMiAspectType(building.getId(), building.getDescription(), properties, uoms);
		
		
		return mindSphereGateway.createAsset(building.getId(), mindSphereLocation, assetVariables, aspectType);
	}
	
	private boolean saveMindSphereAsset(Asset asset) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		logger.debug("Building created");
		return mindSphereGateway.saveAsset(asset);
	}
	
	public boolean createMindSphereTimeSeriesFromBuilding(Building building) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+building.getId()+"Asset\"}");
		try {
			List<Timeseries> timeSeriesList = new ArrayList<Timeseries>();
			Date now = new Date();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			String instant = df.format(now);
			Timeseries timeseriesPoint = new Timeseries();
			timeseriesPoint.getFields().put("_time", instant);
			
			timeseriesPoint.getFields().put("OpeningHours", building.getOpeningHours());

			timeSeriesList.add(timeseriesPoint);
			mindSphereGateway.putTimeSeries(assets.get(0).getAssetId(), building.getId()+"AspectType", timeSeriesList);
			logger.debug("buildingOperation updated");
		
		} catch (Exception e) {
			// Exception handling
			e.printStackTrace();
			return false;
		}
		return true;
	}	
}