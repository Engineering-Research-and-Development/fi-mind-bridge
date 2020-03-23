package it.eng.fimind.service.building;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siemens.mindsphere.sdk.assetmanagement.model.Asset;

import it.eng.fimind.model.fiware.building.Building;

public class BuildingServicesTest {
    public static void main(String[] args) throws IOException {
    	String fileName = "building.json";

        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
        System.out.println("File Found : " + file.exists());

        String jsonBody = new String(Files.readAllBytes(file.toPath()));

        ObjectMapper objectMapper = new ObjectMapper();
        Building building = objectMapper.readValue(jsonBody, Building.class);
        
        BuildingServices buildingServices = new BuildingServices();
        Asset asset = buildingServices.createMindSphereAssetFromBuilding(building);
        System.out.println(asset);
    }
}
