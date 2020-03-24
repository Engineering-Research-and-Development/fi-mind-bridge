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
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.siemens.mindsphere.sdk.assetmanagement.model.AspectType;
import com.siemens.mindsphere.sdk.assetmanagement.model.AssetResource;
import com.siemens.mindsphere.sdk.assetmanagement.model.Location;
import com.siemens.mindsphere.sdk.assetmanagement.model.Variable;
import com.siemens.mindsphere.sdk.assetmanagement.model.VariableDefinition;
import com.siemens.mindsphere.sdk.timeseries.model.Timeseries;

import it.eng.fimind.model.fiware.building.BuildingNormalized;
import it.eng.fimind.util.MindSphereGateway;
import it.eng.fimind.util.MindSphereMapper;
import it.eng.fimind.util.ServiceResult;

/**
 * Root resource (exposed at "buildingnormalized" path)
 */
@Path("buildingNormalized")
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
	public Response createDataInJSON(@HeaderParam("debug-mode") String debugMode, @Valid BuildingNormalized building) { 
		ServiceResult serviceResult = new ServiceResult();
		logger.debug("Id ="+building.getId());
		
		if(debugMode!=null && debugMode.equals("true")){
			System.out.println("DEBUG MODE FOR --- BuildingNormalized ---");
			createMindSphereAssetFromBuilding(building, true);
			serviceResult.setResult("Test gone fine");
			return Response.status(200).entity(serviceResult).build();
		}else {
			Boolean result = false;
			if(!buildingDoesAlreadyExist(building)) 
				result = createMindSphereAssetFromBuilding(building, false);
			
			result = createMindSphereTimeSeriesFromBuilding(building);
			
			if(result) {
				serviceResult.setResult("BuildingNormalized added succesfully");
				return Response.status(201).entity(serviceResult).build();
			}
			else {
				serviceResult.setResult("Something went wrong, check your FI-MIND logs");
				return Response.status(500).entity(serviceResult).build();
			}
		}
	}
	

	private Boolean buildingDoesAlreadyExist(BuildingNormalized building)
	{
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+building.getId()+"Asset\"}");
		return assets.size()>0;
	}
	
	private Boolean createMindSphereAssetFromBuilding(BuildingNormalized building, Boolean isDebugMode) {
		Boolean result = false;
		
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		MindSphereMapper mindSphereMapper = new MindSphereMapper();
		
		Location mindSphereLocation = null;
		if(building.getLocation()!=null) {
			if(building.getLocation().getValue().getType().equals("Point")) 
				mindSphereLocation = mindSphereMapper.fiLocationToMiLocation(building.getLocation().getValue());
		}else if(building.getAddress()!=null) 
			mindSphereLocation = mindSphereMapper.fiAddressToMiLocation(building.getAddress().getValue());
		
		List<String> keys = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		List<String> varDefDataTypes = new ArrayList<String>();

		if(building.getSource()!=null) {
			keys.add("Source");
			values.add((String) building.getSource().getValue());
			varDefDataTypes.add("String");
		}
		if(building.getDataProvider()!=null) {
			keys.add("DataProvider");
			values.add((String) building.getDataProvider().getValue());
			varDefDataTypes.add("String");
		}
		if(building.getDateCreated()!=null) {
			keys.add("DateCreated");
			values.add((String) building.getDateCreated().getValue());
			varDefDataTypes.add("Timestamp");
		}
		if(building.getOwner()!=null) {
			keys.add("Owner");
			values.add((String) building.getOwner().getValue().toString());
			varDefDataTypes.add("String");
		}
		if(building.getCategory()!=null) {
			keys.add("Category");
			values.add((String) building.getCategory().getValue().toString());
			varDefDataTypes.add("String");
		}
		if(building.getOccupier()!=null) {
			keys.add("Occupier");
			values.add((String) building.getOccupier().getValue().toString());
			varDefDataTypes.add("String");
		}
		if(building.getFloorsAboveGround()!=null) {
			keys.add("FloorsAboveGround");
			values.add((String) building.getFloorsAboveGround().getValue().toString());
			varDefDataTypes.add("Integer");
		}
		if(building.getFloorsBelowGround()!=null) {
			keys.add("FloorsBelowGround");
			values.add((String) building.getFloorsBelowGround().getValue().toString());
			varDefDataTypes.add("Integer");
		}
		if(building.getMapUrl()!=null) {
			keys.add("RefMap");
			values.add((String) building.getMapUrl().getValue());
			varDefDataTypes.add("String");
		}
		List<VariableDefinition> assetVariablesDefinitions = mindSphereMapper.fiPropertiesToMiVariablesDefinitions(keys, values, varDefDataTypes);
		List<Variable> assetVariables = mindSphereMapper.fiPropertiesToMiVariables(keys, values, varDefDataTypes);

	
		List<String> properties = Stream.of("DataModfiied", "OpeningHours").collect(Collectors.toList());
		List<String> uoms = Stream.of("t", "Dimensionless").collect(Collectors.toList());
		List<String> dataTypes = Stream.of("Timestamp", "String").collect(Collectors.toList());
		AspectType aspectType = mindSphereMapper.fiStateToMiAspectType(building.getId(), (String) building.getDescription().getValue(), properties, uoms, dataTypes);
		
		
		if(isDebugMode) {
			System.out.println(mindSphereGateway.createAsset(building.getId(), mindSphereLocation, assetVariablesDefinitions, assetVariables, aspectType));
			result = true;
		}else {
			result =  mindSphereGateway.saveAsset(building.getId(), mindSphereLocation, assetVariablesDefinitions, assetVariables, aspectType);
			if(result)
				logger.debug("BuildingNormalized created");
			else 		
				logger.error("BuildingNormalized couldn't be created");
		}
		return result;	
	}
	
	public boolean createMindSphereTimeSeriesFromBuilding(BuildingNormalized building) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+building.getId()+"Asset\"}");
		try {
			List<Timeseries> timeSeriesList = new ArrayList<Timeseries>();
			Date now = new Date();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			String instant = df.format(now);
			Timeseries timeseriesPoint = new Timeseries();
			timeseriesPoint.getFields().put("_time", instant);
			
			if(building.getDateModified()!=null) {
				timeseriesPoint.getFields().put("DateModified", (String) building.getDateModified().getValue());
			}
			if(building.getOpeningHours()!=null) {
				timeseriesPoint.getFields().put("OpeningHours", (String) building.getOpeningHours().getValue().toString());
			}
			
			timeSeriesList.add(timeseriesPoint);
			mindSphereGateway.putTimeSeries(assets.get(0).getAssetId(), building.getId()+"AspectType", timeSeriesList);
			logger.debug("BuildingNormalized updated");
		
		} catch (Exception e) {
			// Exception handling
			e.printStackTrace();
			return false;
		}
		return true;
	}	
	
}