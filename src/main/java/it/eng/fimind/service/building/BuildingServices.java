package it.eng.fimind.service.building;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.siemens.mindsphere.sdk.assetmanagement.model.AspectType;
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

	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteIt(@QueryParam("id") String id) {
		logger.debug("[BuildingServices] DELETE Request");
		ServiceResult serviceResult = new ServiceResult();

		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		if(mindSphereGateway.deleteAssetOnCascade(id))
		{
			serviceResult.setMessage("Deleted successfully!");
			return Response.status(200).entity(serviceResult).build();
		}
		else {
			serviceResult.setResult("Something went wrong, check your FI-MIND logs");
			return Response.status(500).entity(serviceResult).build();
		}
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createDataInJSON(@HeaderParam("debug-mode") String debugMode, @Valid Building building) { 
		logger.debug("[BuildingServices] POST Request");
		ServiceResult serviceResult = new ServiceResult();
		
		logger.debug("Id ="+building.getId());
		
		if(debugMode!=null && debugMode.equals("true")){
			logger.debug("DEBUG MODE FOR --- Building ---");
			createMindSphereAssetFromBuilding(building, true);
			serviceResult.setResult("Test gone fine");
			return Response.status(200).entity(serviceResult).build();
		}else {
			Boolean result = true;
			MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
			if(!mindSphereGateway.assetDoesAlreadyExist(building.getId()))
				result = createMindSphereAssetFromBuilding(building, false);
			
			if(result)
				result = createMindSphereTimeSeriesFromBuilding(building);
			
			if(result) {
				serviceResult.setResult("Building added succesfully");
				return Response.status(201).entity(serviceResult).build();
			}
			else {
				serviceResult.setResult("Something went wrong, check your FI-MIND logs");
				return Response.status(500).entity(serviceResult).build();
			}
		}
	}
	
	public Boolean createMindSphereAssetFromBuilding(Building building, Boolean isDebugMode) {
		Boolean result = false;
		
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		MindSphereMapper mindSphereMapper = new MindSphereMapper();
		
		Location mindSphereLocation = null;
		if(building.getLocation()!=null && building.getAddress()!=null)
			mindSphereLocation = mindSphereMapper.fiLocAddrToMiLocation(building.getLocation(), building.getAddress());
		else if(building.getLocation()!=null)
			mindSphereLocation = mindSphereMapper.fiLocationToMiLocation(building.getLocation());
		else if(building.getAddress()!=null) 
			mindSphereLocation = mindSphereMapper.fiAddressToMiLocation(building.getAddress());
		
		
		List<String> keys = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		List<String> varDefDataTypes = new ArrayList<String>();

		if(building.getType()!=null) {
			keys.add("entityType");
			values.add(building.getType());
			varDefDataTypes.add("String");
		}
		if(building.getSource()!=null) {
			keys.add("source");
			values.add(building.getSource());
			varDefDataTypes.add("String");
		}
		if(building.getDataProvider()!=null) {
			keys.add("dataProvider");
			values.add(building.getDataProvider());
			varDefDataTypes.add("String");
		}
		if(building.getDateCreated()!=null) {
			keys.add("dateCreated");
			values.add(building.getDateCreated());
			varDefDataTypes.add("Timestamp");
		}
		if(building.getOwner()!=null) {
			keys.add("owner");
			values.add(building.getOwner().toString());
			varDefDataTypes.add("String");
		}
		if(building.getCategory()!=null) {
			keys.add("category");
			values.add(building.getCategory().toString());
			varDefDataTypes.add("String");
		}
		if(building.getContainedInPlace()!=null) {
			keys.add("containedInPlace");
			values.add(building.getContainedInPlace().getCoordinates().toString());
			varDefDataTypes.add("String");
		}
		if(building.getOccupier()!=null) {
			keys.add("occupier");
			values.add(building.getOccupier().toString());
			varDefDataTypes.add("String");
		}
		if(building.getFloorsAboveGround()!=null) {
			keys.add("floorsAboveGround");
			values.add(building.getFloorsAboveGround().toString());
			varDefDataTypes.add("Integer");
		}		
		if(building.getFloorsBelowGround()!=null) {
			keys.add("floorsBelowGround");
			values.add(building.getFloorsBelowGround().toString());
			varDefDataTypes.add("Integer");
		}
		if(building.getMapUrl()!=null) {
			keys.add("refMap");
			values.add(building.getMapUrl());
			varDefDataTypes.add("String");
		}
		List<VariableDefinition> assetVariablesDefinitions = mindSphereMapper.fiPropertiesToMiVariablesDefinitions(keys, values, varDefDataTypes);
		List<Variable> assetVariables = mindSphereMapper.fiPropertiesToMiVariables(keys, values, varDefDataTypes);

	
		List<String> properties = Stream.of("dateModified", "openingHours").collect(Collectors.toList());
		List<String> uoms = Stream.of("t", "Dimensionless").collect(Collectors.toList());
		List<String> dataTypes = Stream.of("Timestamp", "String").collect(Collectors.toList());
		AspectType aspectType;
		if(building.getDescription()!=null)
			aspectType = mindSphereMapper.fiStateToMiAspectType(building.getId(), (String) building.getDescription(), properties, uoms, dataTypes);
		else
			aspectType = mindSphereMapper.fiStateToMiAspectType(building.getId(), properties, uoms, dataTypes);
					
		
		if(isDebugMode) {
			logger.debug(mindSphereGateway.createAsset(building.getId(), mindSphereLocation, assetVariablesDefinitions, assetVariables, aspectType));
			result = true;
		}else {
			result =  mindSphereGateway.saveAsset(building.getId(), mindSphereLocation, assetVariablesDefinitions, assetVariables, aspectType);
			if(result)
				logger.debug("Building created");
			else 		
				logger.error("Building couldn't be created");
		}
		return result;	
	}
	
	public boolean createMindSphereTimeSeriesFromBuilding(Building building) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+building.getId()+"\"}");
		try {
			List<Timeseries> timeSeriesList = new ArrayList<Timeseries>();

			Timeseries timeseriesPoint = new Timeseries();
			
			timeseriesPoint.getFields().put("_time", Instant.now().toString());
			
			if(building.getDateModified()!=null) {
				timeseriesPoint.getFields().put("dateModified", building.getDateModified());
			}
			if(building.getOpeningHours()!=null) {
				timeseriesPoint.getFields().put("openingHours", building.getOpeningHours().toString());
			}
		
			timeSeriesList.add(timeseriesPoint);
			mindSphereGateway.putTimeSeries(assets.get(0).getAssetId(), building.getId(), timeSeriesList);
			logger.debug("Building updated");
		
		} catch (Exception e) {
			// Exception handling
			e.printStackTrace();
			return false;
		}
		return true;
	}	
}