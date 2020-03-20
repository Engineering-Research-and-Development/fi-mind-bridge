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
import com.siemens.mindsphere.sdk.assetmanagement.model.AssetTypeResource;
import com.siemens.mindsphere.sdk.assetmanagement.model.GetAspectTypeRequest;
import com.siemens.mindsphere.sdk.assetmanagement.model.GetAssetTypeRequest;
import com.siemens.mindsphere.sdk.assetmanagement.model.GetRootAssetRequest;
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
	
	public Asset createAsset(String id, Location location, List<VariableDefinition> variablesDefinition, List<Variable> variables, AspectType aspectType) {
		Asset asset = new Asset();
		try {
			SaveAspectTypeRequest saveAspectTypeRequest = new SaveAspectTypeRequest();
			saveAspectTypeRequest.setId("engineer."+id+"AspectType");
			saveAspectTypeRequest.setAspecttype(aspectType);
			aspectTypeClient.saveAspectType(saveAspectTypeRequest);

			List<AssetTypeAspects> aspects = new ArrayList<>();
		    AssetTypeAspects assetTypeAspects = new AssetTypeAspects();
		    assetTypeAspects.setAspectTypeId("engineer."+id+"AspectType");
		    assetTypeAspects.setName(id+"AspectType");	    
		    aspects.add(assetTypeAspects);	
		    
			AssetType assetType = new AssetType();
		    assetType.setAspects(aspects);
		    assetType.setScope(ScopeEnum.PRIVATE);
		    assetType.setName(id+"AssetType");
		    assetType.setParentTypeId("core.basicasset");
		    assetType.setVariables(variablesDefinition);
		    
		    SaveAssetTypeRequest saveAssetTypeRequest = new SaveAssetTypeRequest();
		    saveAssetTypeRequest.setId("engineer."+id+"AssetType");
		    saveAssetTypeRequest.setAssettype(assetType);
			assetTypeClient.saveAssetType(saveAssetTypeRequest);
		    			
			asset.setName(id+"Asset");
			asset.setParentId(getRootAsset());
			asset.setTypeId("engineer."+id+"AssetType");
			
			asset.setLocation(location);
			asset.setVariables(variables);
			
			return asset;

		}catch (MindsphereException e) {
			// Exception handling
			System.out.println(e.getMessage());
			e.printStackTrace();
			return null;
		}
		catch (Exception e) {
			// Exception handling
			e.printStackTrace();
			return null;
		}
	}
	
	public Asset createAsset(String id, List<VariableDefinition> variablesDefinition, List<Variable> variables, AspectType aspectType) {
		Asset asset = new Asset();

		try {
			SaveAspectTypeRequest saveAspectTypeRequest = new SaveAspectTypeRequest();
			saveAspectTypeRequest.setId("engineer."+id+"AspectType");
			saveAspectTypeRequest.setAspecttype(aspectType);
			aspectTypeClient.saveAspectType(saveAspectTypeRequest);
			
			List<AssetTypeAspects> aspects = new ArrayList<>();
		    AssetTypeAspects assetTypeAspects = new AssetTypeAspects();
		    assetTypeAspects.setAspectTypeId("engineer."+id+"AspectType");
		    assetTypeAspects.setName(id+"AspectType");	    
		    aspects.add(assetTypeAspects);
		 
			AssetType assetType = new AssetType();
		    assetType.setAspects(aspects);
		    assetType.setScope(ScopeEnum.PRIVATE);
		    assetType.setName(id+"AssetType");
		    assetType.setParentTypeId("core.basicasset");
		    assetType.setVariables(variablesDefinition);
	    
		    SaveAssetTypeRequest saveAssetTypeRequest = new SaveAssetTypeRequest();
		    saveAssetTypeRequest.setId("engineer."+id+"AssetType");
		    saveAssetTypeRequest.setAssettype(assetType);
			assetTypeClient.saveAssetType(saveAssetTypeRequest);
		    
			asset.setName(id+"Asset");
			asset.setParentId(getRootAsset());
			asset.setTypeId("engineer."+id+"AssetType");
									
			asset.setVariables(variables);
			
			return asset;
			
		}catch (MindsphereException e) {
			// Exception handling
			System.out.println(e.getMessage());
			e.printStackTrace();
			return null;
		}
		catch (Exception e) {
			// Exception handling
			e.printStackTrace();
			return null;
		}		
	}
	
	public Boolean saveAsset(Asset asset) {
		try {
			assetsClient.addAsset(asset);
		}catch (MindsphereException e) {
			// Exception handling
			System.out.println(e.getMessage());
			return false;
		}
		catch (Exception e) {
			// Exception handling
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
