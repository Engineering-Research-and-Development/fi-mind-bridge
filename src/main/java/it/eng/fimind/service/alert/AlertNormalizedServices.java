package it.eng.fimind.service.alert;

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

import it.eng.fimind.model.fiware.alert.AlertNormalized;
import it.eng.fimind.util.MindSphereGateway;
import it.eng.fimind.util.MindSphereMapper;
import it.eng.fimind.util.ServiceResult;


/**
 * Root resource (exposed at "alertnormalized" path)
 */
@Path("alertNormalized")
public class AlertNormalizedServices {
	private static Logger logger = Logger.getLogger(AlertNormalizedServices.class);
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getIt() {
		return "AlertNormalized Service: got it!!";
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createDataInJSON(@Valid AlertNormalized alert) { 
		ServiceResult serviceResult = new ServiceResult();
		logger.debug("Id ="+alert.getId());
		
		if(!alertDoesAlreadyExist(alert)) 
			saveMindSphereAsset(createMindSphereAssetFromAlert(alert));
		
		createMindSphereTimeSeriesFromAlert(alert);
		
		serviceResult.setResult("OK");
		return Response.status(201).entity(serviceResult).build();
	}

	private Boolean alertDoesAlreadyExist(AlertNormalized alert)
	{
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+alert.getId()+"Asset\"}");
		return assets.size()>0;
	}
	
	private Asset createMindSphereAssetFromAlert(AlertNormalized alert) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		MindSphereMapper mindSphereMapper = new MindSphereMapper();

		alert.setId(alert.getId().replace("-","_"));

		Location mindSphereLocation = null;
		if(alert.getLocation()!=null) {
			if(alert.getLocation().getValue().getType().equals("Point")) 
				mindSphereLocation = mindSphereMapper.fiLocationToMiLocation(alert.getLocation().getValue());
		}else if(alert.getAddress()!=null) 
			mindSphereLocation = mindSphereMapper.fiAddressToMiLocation(alert.getAddress().getValue());
		
		List<String> keys = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		if(alert.getSource()!=null) {
			keys.add("Source");
			values.add((String) alert.getSource().getValue());
		}
		if(alert.getDataProvider()!=null) {
			keys.add("DataProvider");
			values.add((String) alert.getDataProvider().getValue());
		}
		if(alert.getCategory()!=null) {
			keys.add("Category");
			values.add((String) alert.getCategory().getValue());
		}
		if(alert.getSubCategory()!=null) {
			keys.add("SubCategory");
			values.add((String) alert.getSubCategory().getValue());
		}
		if(alert.getDateIssued()!=null) {
			keys.add("DateIssued");
			values.add((String) alert.getDateIssued().getValue());
		}
		if(alert.getValidFrom()!=null) {
			keys.add("ValidFrom");
			values.add((String) alert.getValidFrom().getValue());
		}
		if(alert.getValidTo()!=null) {
			keys.add("ValidTo");
			values.add((String) alert.getValidTo().getValue());
		}
		if(alert.getAlertSource()!=null) {
			keys.add("AlertSource");
			values.add((String) alert.getAlertSource().getValue());
		}
		if(alert.getData()!=null) {
			keys.add("Data");
			values.add((String) alert.getData().getValue());
		}
		List<VariableDefinition> assetVariablesDefinitions = mindSphereMapper.fiPropertiesToMiVariablesDefinitions(keys, values);
		List<Variable> assetVariables = mindSphereMapper.fiPropertiesToMiVariables(keys, values);

	
		List<String> properties = Stream.of("Severity").collect(Collectors.toList());
		List<String> uoms = Stream.of("Dimensionless").collect(Collectors.toList());
		List<String> dataTypes = Stream.of("String").collect(Collectors.toList());
		AspectType aspectType = mindSphereMapper.fiStateToMiAspectType(alert.getId(), (String) alert.getDescription().getValue(), properties, uoms, dataTypes);
		
		
		return mindSphereGateway.createAsset(alert.getId(), mindSphereLocation, assetVariablesDefinitions, assetVariables, aspectType);
	}

	private Boolean saveMindSphereAsset(Asset asset) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		Boolean result = mindSphereGateway.saveAsset(asset);
		if(result)
			logger.debug("AlertNormalized created");
		else 		
			logger.error("AlertNormalized couldn't be created");
		return result;
	}
	
	private boolean createMindSphereTimeSeriesFromAlert(AlertNormalized alert) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+alert.getId()+"Asset\"}");
		try {
			List<Timeseries> timeSeriesList = new ArrayList<Timeseries>();
			Date now = new Date();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			String instant = df.format(now);
			Timeseries timeseriesPoint=new Timeseries();
			timeseriesPoint.getFields().put("_time", instant);
			
			if(alert.getSeverity()!=null) {
				timeseriesPoint.getFields().put("Severity",(String) alert.getSeverity().getValue());
			}
			
			timeSeriesList.add(timeseriesPoint);
			mindSphereGateway.putTimeSeries(assets.get(0).getAssetId(), alert.getId()+"AspectType", timeSeriesList);
			logger.debug("AlertNormalized updated");
		
		} catch (Exception e) {
			// Exception handling
			e.printStackTrace();
			return false;
		}
		return true;
	}	
}
