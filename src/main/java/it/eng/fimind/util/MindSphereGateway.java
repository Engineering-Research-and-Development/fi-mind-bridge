package it.eng.fimind.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.siemens.mindsphere.sdk.assetmanagement.apiclient.AspecttypeClient;
import com.siemens.mindsphere.sdk.assetmanagement.apiclient.AssetsClient;
import com.siemens.mindsphere.sdk.assetmanagement.apiclient.AssettypeClient;
import com.siemens.mindsphere.sdk.assetmanagement.apiclient.StructureClient;
import com.siemens.mindsphere.sdk.assetmanagement.model.AspectType;
import com.siemens.mindsphere.sdk.assetmanagement.model.AspectTypeResource;
import com.siemens.mindsphere.sdk.assetmanagement.model.Asset;
import com.siemens.mindsphere.sdk.assetmanagement.model.AssetListResource;
import com.siemens.mindsphere.sdk.assetmanagement.model.AssetResource;
import com.siemens.mindsphere.sdk.assetmanagement.model.AssetType;
import com.siemens.mindsphere.sdk.assetmanagement.model.AssetType.ScopeEnum;
import com.siemens.mindsphere.sdk.assetmanagement.model.AssetTypeAspects;
import com.siemens.mindsphere.sdk.assetmanagement.model.AssetTypeListResource;
import com.siemens.mindsphere.sdk.assetmanagement.model.AssetTypeResource;
import com.siemens.mindsphere.sdk.assetmanagement.model.AssetTypeResourceAspects;
import com.siemens.mindsphere.sdk.assetmanagement.model.DeleteAspectTypeRequest;
import com.siemens.mindsphere.sdk.assetmanagement.model.DeleteAssetRequest;
import com.siemens.mindsphere.sdk.assetmanagement.model.DeleteAssetTypeRequest;
import com.siemens.mindsphere.sdk.assetmanagement.model.GetAspectTypeRequest;
import com.siemens.mindsphere.sdk.assetmanagement.model.GetAssetTypeRequest;
import com.siemens.mindsphere.sdk.assetmanagement.model.GetRootAssetRequest;
import com.siemens.mindsphere.sdk.assetmanagement.model.ListAssetTypesRequest;
import com.siemens.mindsphere.sdk.assetmanagement.model.ListAssetVariablesRequest;
import com.siemens.mindsphere.sdk.assetmanagement.model.ListAssetsRequest;
import com.siemens.mindsphere.sdk.assetmanagement.model.Location;
import com.siemens.mindsphere.sdk.assetmanagement.model.RootAssetResource;
import com.siemens.mindsphere.sdk.assetmanagement.model.SaveAspectTypeRequest;
import com.siemens.mindsphere.sdk.assetmanagement.model.SaveAssetTypeRequest;
import com.siemens.mindsphere.sdk.assetmanagement.model.Variable;
import com.siemens.mindsphere.sdk.assetmanagement.model.VariableDefinition;
import com.siemens.mindsphere.sdk.assetmanagement.model.VariableListResource;
import com.siemens.mindsphere.sdk.core.MindsphereCredentials;
import com.siemens.mindsphere.sdk.core.RestClientConfig;
import com.siemens.mindsphere.sdk.core.exception.MindsphereException;
import com.siemens.mindsphere.sdk.timeseries.apiclient.TimeSeriesClient;
import com.siemens.mindsphere.sdk.timeseries.model.GetTimeseriesRequest;
import com.siemens.mindsphere.sdk.timeseries.model.Timeseries;

public class MindSphereGateway {
	private static MindSphereGateway instance = null;

	private TimeSeriesClient timeSeriesClient;
	private AspecttypeClient aspectTypeClient;
	private AssettypeClient assetTypeClient;
	private AssetsClient assetsClient;
	private StructureClient structureClient;
	
	public String tenant;
			
	private MindSphereGateway(){
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			File file = new File(classLoader.getResource("config.properties").getFile());
			FileInputStream input;

			input = new FileInputStream(file);
			// load a properties file
			Properties prop = new Properties();

			prop.load(input);

			RestClientConfig config = RestClientConfig.builder()
					.connectionTimeoutInSeconds(100)
					.build();

			MindsphereCredentials credentials = MindsphereCredentials.tenantCredentialsBuilder()
					.clientId(prop.getProperty("client-id"))
					.clientSecret(prop.getProperty("client-secret"))
					.tenant(prop.getProperty("tenant"))
					.build();

			timeSeriesClient = TimeSeriesClient.builder()
					.mindsphereCredentials(credentials)
					.restClientConfig(config)
					.build();
			
			
			// Construct the AspecttypeClient object
			aspectTypeClient = AspecttypeClient.builder()
			                                        .mindsphereCredentials(credentials)
			                                        .restClientConfig(config)
			                                        .build();
			
			// Construct the AssettypeClient object
			assetTypeClient = AssettypeClient.builder()
			                                      .mindsphereCredentials(credentials)
			                                      .restClientConfig(config)
			                                      .build();
			// Construct the AssetClient object
			assetsClient = AssetsClient.builder()
			                              .mindsphereCredentials(credentials)
			                              .restClientConfig(config)
			                              .build();
			
			// Construct the StructureClient object
			structureClient = StructureClient.builder()
					.mindsphereCredentials(credentials)
                    .restClientConfig(config)
                    .build();
			
			tenant = prop.getProperty("tenant");
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static synchronized MindSphereGateway getMindSphereGateway() {
		if (instance == null) {
			instance = new MindSphereGateway();
		}
		return instance;
	}
	
	public String getTenant() {
		return this.tenant;
	}
	
	public List<Timeseries> getTimeSeries(String entity, String propertySetName){
		List<Timeseries> timeSeries = null;
		GetTimeseriesRequest requestObject = new GetTimeseriesRequest();
		requestObject.setEntity(entity);
		requestObject.setPropertysetname(propertySetName);
		try {
			timeSeries = timeSeriesClient.getTimeseries(requestObject);
		} catch (MindsphereException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return timeSeries;
	}
	
	public boolean putTimeSeries(String entity, String propertySetName, List<Timeseries> timeSeries){
		Boolean success = false;
		try {
			timeSeriesClient.putTimeseries(entity, propertySetName, timeSeries);
			success = true;
		} catch (MindsphereException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			success = false;
		}
		return success;
	}

	
	private String getRootAsset() {
		//Create client object "assetClient" as shown above
		RootAssetResource assetResource = null;
		GetRootAssetRequest requestObject = new GetRootAssetRequest();
		try{
		    assetResource = assetsClient.getRootAsset(requestObject);
		} catch (MindsphereException e) {
		    // Exception handling
			return null;
		}
		return assetResource.getAssetId();
	}
	
	public List<AssetResource> getAssets(){
		AssetListResource assetListResource = null;
		ListAssetsRequest  requestObject = new ListAssetsRequest();

		try {
			assetListResource = assetsClient.listAssets(requestObject);
			 
		}catch (MindsphereException e) {
		    // Exception handling
			e.printStackTrace();
		}
		return assetListResource.getEmbedded().getAssets();
	}
	
	public List<AssetResource> getFilteredAssets(String order, String filter){
		AssetListResource assetListResource = null;
		ListAssetsRequest  requestObject = new ListAssetsRequest();
		requestObject.setSort(order);
		requestObject.setFilter(filter);
		try {
			assetListResource = assetsClient.listAssets(requestObject);
		}catch (MindsphereException e) {
		    // Exception handling
			e.printStackTrace();
		}
		return assetListResource.getEmbedded().getAssets();
	}
	
	public Boolean assetDoesAlreadyExist(String id)
	{
		MindSphereGateway mindSphereGateway = MindSphereGateway.getMindSphereGateway();
		List<AssetResource> assets = mindSphereGateway.getFilteredAssets("ASC", "{\"name\":\""+id+"\"}");
		return assets.size()>0;
	}
	
	public AspectTypeResource getAspectById(String id){
		AspectTypeResource aspect = null;
		GetAspectTypeRequest requestObject = new GetAspectTypeRequest();
		requestObject.setId(id);
		try {
			aspect = aspectTypeClient.getAspectType(requestObject);
		}catch (MindsphereException e) {
		    // Exception handling
			e.printStackTrace();
		}
		return aspect;
	}
	
	public AssetTypeResource getAssetTypeById(String id){
		AssetTypeResource assetType = null;
		GetAssetTypeRequest requestObject = new GetAssetTypeRequest();
		requestObject.setId(id);

		try {
			assetType = assetTypeClient.getAssetType(requestObject);
		}catch (MindsphereException e) {
		    // Exception handling
			e.printStackTrace();
		}
		return assetType;
	}
	
	public List<AssetTypeResource> getFilteredAssetTypes(String order, String filter){
		AssetTypeListResource assetType = null;
		ListAssetTypesRequest requestObject = new ListAssetTypesRequest();
		requestObject.setSort(order);
		requestObject.setFilter(filter);

		try {
			assetType = assetTypeClient.listAssetTypes(requestObject);
		}catch (MindsphereException e) {
		    // Exception handling
			e.printStackTrace();
		}
		return assetType.getEmbedded().getAssetTypes();
	}
	
	public VariableListResource getAssetVariablesById(String id){
		VariableListResource structureVariables = null;
		ListAssetVariablesRequest requestObject = new ListAssetVariablesRequest();
		requestObject.setId(id);
		
		try{
			structureVariables = structureClient.listAssetVariables(requestObject);
		} catch (MindsphereException e) {
			 // Exception handling
			e.printStackTrace();
		}
		
		return structureVariables;
	}
	
	public Boolean deleteAsset(String id) {
		Boolean success = false;
		DeleteAssetRequest  requestObject = new DeleteAssetRequest();
		requestObject.setId(id);
		requestObject.setIfMatch("0");
		
		try {
			assetsClient.deleteAsset(requestObject);
			success = true;
		}catch (MindsphereException e) {
		    // Exception handling
			success = false;
			e.printStackTrace();
		}
		return success;
	}
	
	public Boolean deleteAssetType(String id) {
		Boolean success = false;
		DeleteAssetTypeRequest  requestObject = new DeleteAssetTypeRequest();
		requestObject.setId(id);
		requestObject.setIfMatch("0");
		
		try {
			assetTypeClient.deleteAssetType(requestObject);
			success = true;
		}catch (MindsphereException e) {
		    // Exception handling
			success = false;
			e.printStackTrace();
		}
		return success;
	}
	
	public Boolean deleteAspectType(String id) {
		Boolean success = false;
		DeleteAspectTypeRequest  requestObject = new DeleteAspectTypeRequest();
		requestObject.setId(id);
		requestObject.setIfMatch("0");

		try {
			aspectTypeClient.deleteAspectType(requestObject);
			success = true;
		}catch (MindsphereException e) {
		    // Exception handling
			success = false;
			e.printStackTrace();
		}
		return success;
	}
	
	public Boolean deleteAssetOnCascade(String id) {
		Boolean success = false;

		List<AssetResource> assets = getFilteredAssets("ASC", "{\"name\":\""+id+"\"}");
		List<AssetTypeResource> assetTypes = getFilteredAssetTypes("ASC", "{\"id\":{\"in\":[\""+tenant+"."+id+"\"]}}&exploded=true");

		
		if(assets.size()>0) {	
			deleteAsset(assets.get(0).getAssetId());
			deleteAssetType(assets.get(0).getTypeId());
		}
		
		if(assetTypes.size()>0) {
			for(int i=0;i<assetTypes.get(0).getAspects().size();i++) {
				AssetTypeResourceAspects curr_aspectType = assetTypes.get(0).getAspects().get(i);
				deleteAspectType(curr_aspectType.getAspectType().getId());
			}
		}
		
		success = true;
		
		return success;
	}
	
	
	public Asset createAsset(String id, Location location, List<VariableDefinition> variablesDefinition, List<Variable> variables, AspectType aspectType) {
		try {
			SaveAspectTypeRequest saveAspectTypeRequest = new SaveAspectTypeRequest();
			saveAspectTypeRequest.setId(tenant+"."+id);
			saveAspectTypeRequest.setAspecttype(aspectType);

			List<AssetTypeAspects> aspects = new ArrayList<>();
		    AssetTypeAspects assetTypeAspects = new AssetTypeAspects();
		    assetTypeAspects.setAspectTypeId(tenant+"."+id);
		    assetTypeAspects.setName(id);	    
		    aspects.add(assetTypeAspects);	
		    
			AssetType assetType = new AssetType();
		    assetType.setAspects(aspects);
		    assetType.setScope(ScopeEnum.PRIVATE);
		    assetType.setName(id);
		    assetType.setParentTypeId("core.basicasset");
		    assetType.setVariables(variablesDefinition);
		    
		    SaveAssetTypeRequest saveAssetTypeRequest = new SaveAssetTypeRequest();
		    saveAssetTypeRequest.setId(tenant+"."+id);
		    saveAssetTypeRequest.setAssettype(assetType);
			
			Asset asset = new Asset();
			asset.setName(id);
			asset.setParentId(getRootAsset());
			asset.setTypeId(tenant+"."+id);

			asset.setLocation(location);
			asset.setVariables(variables);
			
			return asset;
		}
		catch (Exception e) {
			// Exception handling
			e.printStackTrace();
			return null;
		}
	}
	
	public Asset createAsset(String id, List<VariableDefinition> variablesDefinition, List<Variable> variables, AspectType aspectType) {
		try {
			SaveAspectTypeRequest saveAspectTypeRequest = new SaveAspectTypeRequest();
			saveAspectTypeRequest.setId(tenant+"."+id);
			saveAspectTypeRequest.setAspecttype(aspectType);

			List<AssetTypeAspects> aspects = new ArrayList<>();
		    AssetTypeAspects assetTypeAspects = new AssetTypeAspects();
		    assetTypeAspects.setAspectTypeId(tenant+"."+id);
		    assetTypeAspects.setName(id);	    
		    aspects.add(assetTypeAspects);	
		    
			AssetType assetType = new AssetType();
		    assetType.setAspects(aspects);
		    assetType.setScope(ScopeEnum.PRIVATE);
		    assetType.setName(id);
		    assetType.setParentTypeId("core.basicasset");
		    assetType.setVariables(variablesDefinition);
		    
		    SaveAssetTypeRequest saveAssetTypeRequest = new SaveAssetTypeRequest();
		    saveAssetTypeRequest.setId(tenant+"."+id);
		    saveAssetTypeRequest.setAssettype(assetType);
			
			Asset asset = new Asset();
			asset.setName(id);
			asset.setParentId(getRootAsset());
			asset.setTypeId(tenant+"."+id);
									
			asset.setVariables(variables);
			
			return asset;
			
		}catch (Exception e) {
			// Exception handling
			e.printStackTrace();
			return null;
		}		
	}
	
	public Boolean saveAsset(String id, Location location, List<VariableDefinition> variablesDefinition, List<Variable> variables, AspectType aspectType) {
		try {
			SaveAspectTypeRequest saveAspectTypeRequest = new SaveAspectTypeRequest();
			saveAspectTypeRequest.setId(tenant+"."+id);
			saveAspectTypeRequest.setAspecttype(aspectType);		
			aspectTypeClient.saveAspectType(saveAspectTypeRequest);
			
			List<AssetTypeAspects> aspects = new ArrayList<>();
		    AssetTypeAspects assetTypeAspects = new AssetTypeAspects();
		    assetTypeAspects.setAspectTypeId(tenant+"."+id);
		    assetTypeAspects.setName(id);	    
		    aspects.add(assetTypeAspects);	
		    
			AssetType assetType = new AssetType();
		    assetType.setAspects(aspects);
		    assetType.setScope(ScopeEnum.PRIVATE);
		    assetType.setName(id);
		    assetType.setParentTypeId("core.basicasset");
		    assetType.setVariables(variablesDefinition);
		    
		    SaveAssetTypeRequest saveAssetTypeRequest = new SaveAssetTypeRequest();
		    saveAssetTypeRequest.setId(tenant+"."+id);
		    saveAssetTypeRequest.setAssettype(assetType);
 			assetTypeClient.saveAssetType(saveAssetTypeRequest);
 			
			Asset asset = new Asset();
			asset.setName(id);
			asset.setParentId(getRootAsset());
			asset.setTypeId(tenant+"."+id);
			
			asset.setLocation(location);
			asset.setVariables(variables);
			
			assetsClient.addAsset(asset);
			
			return true;

		}catch (MindsphereException e) {
			// Exception handling
			System.out.println(e.getErrorMessage());
			System.out.println(e.getErrorStatusCode());
			System.out.println(e.getLogref());
			return false;
		}
		catch (Exception e) {
			// Exception handling
			e.printStackTrace();
			return false;
		}
	}

	public Boolean saveAsset(String id, List<VariableDefinition> variablesDefinition, List<Variable> variables, AspectType aspectType) {
		try {
			SaveAspectTypeRequest saveAspectTypeRequest = new SaveAspectTypeRequest();
			saveAspectTypeRequest.setId(tenant+"."+id);
			saveAspectTypeRequest.setAspecttype(aspectType);
			aspectTypeClient.saveAspectType(saveAspectTypeRequest);

			List<AssetTypeAspects> aspects = new ArrayList<>();
		    AssetTypeAspects assetTypeAspects = new AssetTypeAspects();
		    assetTypeAspects.setAspectTypeId(tenant+"."+id);
		    assetTypeAspects.setName(id);	    
		    aspects.add(assetTypeAspects);	
		    
			AssetType assetType = new AssetType();
		    assetType.setAspects(aspects);
		    assetType.setScope(ScopeEnum.PRIVATE);
		    assetType.setName(id);
		    assetType.setParentTypeId("core.basicasset");
		    assetType.setVariables(variablesDefinition);
		    
		    SaveAssetTypeRequest saveAssetTypeRequest = new SaveAssetTypeRequest();
		    saveAssetTypeRequest.setId(tenant+"."+id);
		    saveAssetTypeRequest.setAssettype(assetType);
 			assetTypeClient.saveAssetType(saveAssetTypeRequest);
 			
			Asset asset = new Asset();
			asset.setName(id);
			asset.setParentId(getRootAsset());
			asset.setTypeId(tenant+"."+id);
			
			asset.setVariables(variables);
			
			assetsClient.addAsset(asset);
			
			return true;

		}catch (MindsphereException e) {
			// Exception handling
			System.out.println(e.getErrorMessage());
			System.out.println(e.getErrorStatusCode());
			System.out.println(e.getLogref());
			return false;
		}
		catch (Exception e) {
			// Exception handling
			e.printStackTrace();
			return false;
		}
	}
}

//saveAspectTypeRequest.setId(tenant+"."+id+"AspectType");
//assetTypeAspects.setAspectTypeId(tenant+"."+id+"AspectType");
//assetTypeAspects.setName(id+"AspectType");	   
//assetType.setName(id+"AssetType");
//saveAssetTypeRequest.setId(tenant+"."+id+"AssetType");
//saveAssetTypeRequest.setAssettype(assetType);
//asset.setName(id+"Asset");
//asset.setTypeId(tenant+"."+id+"AssetType");