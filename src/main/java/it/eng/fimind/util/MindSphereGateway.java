package it.eng.fimind.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.siemens.mindsphere.sdk.auth.model.MindsphereCredentials;
import com.siemens.mindsphere.sdk.core.RestClientConfig;
import com.siemens.mindsphere.sdk.core.exception.MindsphereException;
import com.siemens.mindsphere.sdk.iot.asset.apiclient.AspectTypeClient;
import com.siemens.mindsphere.sdk.iot.asset.apiclient.AssetClient;
import com.siemens.mindsphere.sdk.iot.asset.apiclient.AssetTypeClient;
import com.siemens.mindsphere.sdk.iot.asset.model.AspectTypeDto;
import com.siemens.mindsphere.sdk.iot.asset.model.AspectTypeResource;
import com.siemens.mindsphere.sdk.iot.asset.model.AssetDto;
import com.siemens.mindsphere.sdk.iot.asset.model.AssetResource;
import com.siemens.mindsphere.sdk.iot.asset.model.AssetTypeDto;
import com.siemens.mindsphere.sdk.iot.asset.model.AssetTypeDtoAspects;
import com.siemens.mindsphere.sdk.iot.asset.model.AssetTypeResource;
import com.siemens.mindsphere.sdk.iot.asset.model.Assets;
import com.siemens.mindsphere.sdk.iot.asset.model.ScopeEnum;
import com.siemens.mindsphere.sdk.iot.timeseries.apiclient.TimeseriesClient;
import com.siemens.mindsphere.sdk.iot.timeseries.model.TimeseriesData;

public class MindSphereGateway {
	private static MindSphereGateway instance = null;

	private TimeseriesClient timeseriesClient;
	private AspectTypeClient aspecttypeClient;
	private AssetTypeClient assetTypeClient;
	private AssetClient assetClient;
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

			MindsphereCredentials credentials = MindsphereCredentials.builder()
					.clientId(prop.getProperty("client-id"))
					.clientSecret(prop.getProperty("client-secret"))
					.tenant(prop.getProperty("tenant"))
					.build();



			timeseriesClient = TimeseriesClient.builder()
					.mindsphereCredentials(credentials)
					.restClientConfig(config)
					.build();
			
			
			// Construct the AspecttypeClient object
			aspecttypeClient = AspectTypeClient.builder()
			                                        .mindsphereCredentials(credentials)
			                                        .restClientConfig(config)
			                                        .build();
			
			// Construct the AssettypeClient object
			assetTypeClient = AssetTypeClient.builder()
			                                      .mindsphereCredentials(credentials)
			                                      .restClientConfig(config)
			                                      .build();
			// Construct the AssetClient object
			assetClient = AssetClient.builder()
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

	
	public boolean putTimeSeries(String entity,
			String propertySetName,
			List<TimeseriesData> timeSeriesList,
			Boolean sync){
		try {
			return timeseriesClient.putTimeseries(entity, propertySetName, timeSeriesList, sync);
		} catch (MindsphereException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	
	private String getRootAsset() {
		//Create client object "assetClient" as shown above
		AssetResource assetResource = null;
		try{
		    assetResource = assetClient.getRootAsset();
		} catch (MindsphereException e) {
		    // Exception handling
			return null;
		}
		return assetResource.getAssetId();
	}
	
	

	public boolean createTimeseries(String assetId, String aspectId,  List<TimeseriesData>  timeSeriesList, boolean sync) {
		try {
			timeseriesClient.putTimeseries(assetId, aspectId, timeSeriesList, true);
		} catch (MindsphereException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	
	public Assets getAssets(){
		Assets assets = null;
		try {
			assets = assetClient.getAssets();
		}catch (MindsphereException e) {
		    // Exception handling
			e.printStackTrace();
		}
		return assets;
	}
	
	public Assets getFilteredAssets(String order, String filter){
		Assets assets = null;
		try {
			assets = assetClient.getAssets(order, filter);
		}catch (MindsphereException e) {
		    // Exception handling
			e.printStackTrace();
		}
		return assets;
	}
	
	public AspectTypeResource getAspectById(String id){
		AspectTypeResource aspect = null;
		try {
			aspect = aspecttypeClient.getAspectTypeById(id);
		}catch (MindsphereException e) {
		    // Exception handling
			e.printStackTrace();
		}
		return aspect;
	}
	
	public AssetTypeResource getAssetTypeById(String id){
		AssetTypeResource assetType = null;
		try {
			assetType = assetTypeClient.getAssetTypeById(id);
		}catch (MindsphereException e) {
		    // Exception handling
			e.printStackTrace();
		}
		return assetType;
	}
	
	public boolean createAsset(String id,
            AspectTypeDto aspecttype) {
		


		try {
			
			aspecttypeClient.createAspectType("engineer."+id+"AspectType", aspecttype);
		    AssetTypeDto assettype=new AssetTypeDto();
		    List<AssetTypeDtoAspects> aspects=new ArrayList<>();
		   
		    AssetTypeDtoAspects assetTypeDtoAspects=new AssetTypeDtoAspects();
		    assetTypeDtoAspects.setAspectTypeId("engineer."+id+"AspectType");
		    assetTypeDtoAspects.setName(id+"AspectType");
		    
		    aspects.add(assetTypeDtoAspects);
			assettype.setAspects(aspects);
			assettype.setScope(ScopeEnum.PRIVATE);
			assettype.setName(id+"AssetType");
			assettype.setParentTypeId("core.basicasset");
			assetTypeClient.createAssetType("engineer."+id+"AssetType", assettype);
		    
			
			AssetDto assetDto=new AssetDto();
		    assetDto.setName(id+"Asset");
		    
		    assetDto.setParentId(getRootAsset());
		    assetDto.setTypeId("engineer."+id+"AssetType");
		    
			assetClient. createAsset(assetDto);
		} 
		catch (Exception e) {
			// Exception handling
			e.printStackTrace();
			return false;
		}
		return true;
	} 

}
