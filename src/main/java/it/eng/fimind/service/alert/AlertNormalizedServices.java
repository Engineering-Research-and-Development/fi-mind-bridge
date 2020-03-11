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
import com.siemens.mindsphere.sdk.assetmanagement.model.AspectVariable;
import com.siemens.mindsphere.sdk.assetmanagement.model.AssetResource;
import com.siemens.mindsphere.sdk.assetmanagement.model.AspectType.CategoryEnum;
import com.siemens.mindsphere.sdk.assetmanagement.model.AspectType.ScopeEnum;
import com.siemens.mindsphere.sdk.assetmanagement.model.AspectVariable.DataTypeEnum;
import com.siemens.mindsphere.sdk.timeseries.model.Timeseries;

import it.eng.fimind.model.fiware.alert.AlertNormalized;
import it.eng.fimind.util.MindSphereGateway;
import it.eng.fimind.util.ServiceResult;


/**
 * Root resource (exposed at "alertnormalized" path)
 */
@Path("alertnormalized")
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
		if(!alertDoesAlreadyExist(alert)) {
			createMindSphereAssetFromAlert(alert);
		}
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
	
	private boolean createMindSphereAssetFromAlert(AlertNormalized alert) {
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		AspectType aspectType = new AspectType();
		
		aspectType.setName((String) alert.getId()+"Aspect");
		aspectType.setDescription((String) alert.getDescription().getValue());
		aspectType.setScope(ScopeEnum.PRIVATE);
		aspectType.setCategory(CategoryEnum.DYNAMIC);
		
		List<AspectVariable> variables=new ArrayList<AspectVariable>();

		List<String> properties = Stream.of("Category", "SubCategory", "Severity").collect(Collectors.toList());
		List<String> uoms = Stream.of("Empiric Data", "Empiric Data", "Empiric Data").collect(Collectors.toList());

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
		mindSphereGateway.createAsset(alert.getId(), aspectType);
		logger.debug("AlertNormalized created");
		return true;
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
			
			timeseriesPoint.getFields().put("Category",(String) alert.getCategory().getValue());
			timeseriesPoint.getFields().put("SubCategory",(String) alert.getSubCategory().getValue());
			timeseriesPoint.getFields().put("Severity",(String) alert.getSeverity().getValue());
	
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
