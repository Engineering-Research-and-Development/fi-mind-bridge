package it.eng.fimind.service.building;

import java.util.ArrayList;
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

import it.eng.fimind.model.fiware.building.BuildingNormalized;
import it.eng.fimind.util.MindSphereGateway;
import it.eng.fimind.util.MindSphereMapper;
import it.eng.fimind.util.ServiceResult;

/**
 * Root resource (exposed at "buildingnormalized" path)
 */
@Path("buildingnormalized")
public class BuildingNormalizedServices {
	private static Logger logger = Logger.getLogger(BuildingNormalizedServices.class);
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getIt() {
		return "BuildingNormalized Service: got it!!";
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createDataInJSON(@Valid BuildingNormalized building) { 
		ServiceResult serviceResult = new ServiceResult();
		logger.debug("Id ="+building.getId());
		
		if(!buildingDoesAlreadyExist(building)) 
			saveMindSphereAsset(createMindSphereAssetFromBuilding(building));
				
		serviceResult.setResult("OK");
		return Response.status(201).entity(serviceResult).build();
	}

	private Boolean buildingDoesAlreadyExist(BuildingNormalized building)
	{
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+building.getId()+"Asset\"}");
		return assets.size()>0;
	}
	
	private Asset createMindSphereAssetFromBuilding(BuildingNormalized building) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		MindSphereMapper mindSphereMapper = new MindSphereMapper();
		
		Location mindSphereLocation = null;
		if(building.getLocation().getType().equals("Point")) 
			mindSphereLocation = mindSphereMapper.fiLocationToMiLocation(building.getLocation().getValue());
		else 
			mindSphereLocation = mindSphereMapper.fiAddressToMiLocation(building.getAddress().getValue());
		
		
		List<String> keys = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		keys.add("Source");
		values.add((String) building.getSource().getValue());
		keys.add("DataProvider");
		values.add((String) building.getDataProvider().getValue());
		keys.add("DateModified");
		values.add((String) building.getDateModified().getValue());
		keys.add("DateCreated");
		values.add((String) building.getDateCreated().getValue());
		keys.add("Owner");
		values.add((String) building.getOwner().getValue().toString());
		keys.add("Category");
		values.add((String) building.getCategory().getValue());
		keys.add("Occupier");
		values.add((String) building.getOccupier().getValue().toString());
		keys.add("FloorsAboveGround");
		values.add((String) building.getFloorsAboveGround().getValue().toString());
		keys.add("FloorsBelowGround");
		values.add((String) building.getFloorsBelowGround().getValue().toString());
		keys.add("RefMap");
		values.add((String) building.getRefMap().getValue());
		keys.add("OpeningHours");
		values.add((String) building.getOpeningHours().getValue().toString());
		List<Variable> assetVariables = mindSphereMapper.fiPropertiesToMiVariables(keys, values);
		
	
		List<String> properties = Stream.of("OpeningHours").collect(Collectors.toList());
		List<String> uoms = Stream.of("Dimensionless").collect(Collectors.toList());
		AspectType aspectType = mindSphereMapper.fiStateToMiAspectType(building.getId(), (String) building.getDescription().getValue(), properties, uoms);
		
		
		return mindSphereGateway.createAsset(building.getId(), mindSphereLocation, assetVariables, aspectType);
	}
	
	private boolean saveMindSphereAsset(Asset asset) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		logger.debug("BuildingNormalized created");
		return mindSphereGateway.saveAsset(asset);
	}
	
}