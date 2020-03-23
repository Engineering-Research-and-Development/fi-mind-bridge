package it.eng.fimind.service.building;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siemens.mindsphere.sdk.assetmanagement.model.Asset;

import it.eng.fimind.model.fiware.building.BuildingOperation;

public class BuildingOperationServicesTest {
    public static void main(String[] args) throws IOException {
    	String fileName = "buildingoperation.json";

        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
        System.out.println("File Found : " + file.exists());

        String jsonBody = new String(Files.readAllBytes(file.toPath()));

        ObjectMapper objectMapper = new ObjectMapper();
        BuildingOperation buildingOperation = objectMapper.readValue(jsonBody, BuildingOperation.class);
        
        BuildingOperationServices buildingOperationServices = new BuildingOperationServices();
        Asset asset = buildingOperationServices.createMindSphereAssetFromBuildingOperation(buildingOperation);
        System.out.println(asset);
    }
}
