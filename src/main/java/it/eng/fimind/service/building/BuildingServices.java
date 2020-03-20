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
import com.siemens.mindsphere.sdk.assetmanagement.model.VariableDefinition;
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
		
		building.setId(building.getId().replaceAll("-","_"));

		Location mindSphereLocation = null;
		if(building.getLocation()!=null) {
			if(building.getLocation().getType().equals("Point")) 
				mindSphereLocation = mindSphereMapper.fiLocationToMiLocation(building.getLocation());
		}else if(building.getAddress()!=null) 
			mindSphereLocation = mindSphereMapper.fiAddressToMiLocation(building.getAddress());
		
		
		List<String> keys = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		if(building.getSource()!=null) {
			keys.add("Source");
			values.add(building.getSource());
		}
		if(building.getDataProvider()!=null) {
			keys.add("DataProvider");
			values.add(building.getDataProvider());
		}
		if(building.getDateModified()!=null) {
			keys.add("DateModified");
			values.add(building.getDateModified());
		}
		if(building.getDateCreated()!=null) {
			keys.add("DateCreated");
			values.add(building.getDateCreated());
		}
		if(building.getOwner()!=null) {
			keys.add("Owner");
			values.add(building.getOwner().toString());
		}
		if(building.getCategory()!=null) {
			keys.add("Category");
			values.add(building.getCategory().toString());
		}
		if(building.getOccupier()!=null) {
			keys.add("Occupier");
			values.add(building.getOccupier().toString());
		}
		if(building.getFloorsAboveGround()!=null) {
			keys.add("FloorsAboveGround");
			values.add(building.getFloorsAboveGround().toString());
		}		
		if(building.getFloorsBelowGround()!=null) {
			keys.add("FloorsBelowGround");
			values.add(building.getFloorsBelowGround().toString());
		}
		if(building.getMapUrl()!=null) {
			keys.add("RefMap");
			values.add(building.getMapUrl());
		}
		List<VariableDefinition> assetVariablesDefinitions = mindSphereMapper.fiPropertiesToMiVariablesDefinitions(keys, values);
		List<Variable> assetVariables = mindSphereMapper.fiPropertiesToMiVariables(keys, values);

	
		List<String> properties = Stream.of("OpeningHours").collect(Collectors.toList());
		List<String> uoms = Stream.of("Dimensionless").collect(Collectors.toList());
		List<String> dataTypes = Stream.of("String").collect(Collectors.toList());
		AspectType aspectType = mindSphereMapper.fiStateToMiAspectType(building.getId(), building.getDescription(), properties, uoms, dataTypes);
		
		
		return mindSphereGateway.createAsset(building.getId(), mindSphereLocation, assetVariablesDefinitions, assetVariables, aspectType);
	}
	
	private Boolean saveMindSphereAsset(Asset asset) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		Boolean result = mindSphereGateway.saveAsset(asset);
		if(result)
			logger.debug("Building created");
		else 		
			logger.error("Building couldn't be created");
		return result;
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
			
			if(building.getOpeningHours()!=null) {
				timeseriesPoint.getFields().put("OpeningHours", building.getOpeningHours().toString());
			}
		
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