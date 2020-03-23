package it.eng.fimind.service.alert;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siemens.mindsphere.sdk.assetmanagement.model.Asset;

import it.eng.fimind.model.fiware.alert.Alert;

public class AlertServicesTest {
    public static void main(String[] args) throws IOException {
    	String fileName = "alert.json";

        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
        System.out.println("File Found : " + file.exists());

        String jsonBody = new String(Files.readAllBytes(file.toPath()));

        ObjectMapper objectMapper = new ObjectMapper();
        Alert alert = objectMapper.readValue(jsonBody, Alert.class);
        
        AlertServices alertServices = new AlertServices();
        Asset asset = alertServices.createMindSphereAssetFromAlert(alert);
        System.out.println(asset);
    }
}
