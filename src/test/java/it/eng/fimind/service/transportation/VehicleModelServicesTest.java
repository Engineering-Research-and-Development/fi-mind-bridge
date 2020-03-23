package it.eng.fimind.service.transportation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siemens.mindsphere.sdk.assetmanagement.model.Asset;

import it.eng.fimind.model.fiware.transportation.VehicleModel;

public class VehicleModelServicesTest {
    public static void main(String[] args) throws IOException {
    	String fileName = "vehiclemodel.json";

        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
        System.out.println("File Found : " + file.exists());

        String jsonBody = new String(Files.readAllBytes(file.toPath()));

        ObjectMapper objectMapper = new ObjectMapper();
        VehicleModel vehicleModel = objectMapper.readValue(jsonBody, VehicleModel.class);
        
        VehicleModelServices vehicleModelServices = new VehicleModelServices();
        Asset asset = vehicleModelServices.createMindSphereAssetFromVehicleModel(vehicleModel);
        System.out.println(asset);
    }
}
