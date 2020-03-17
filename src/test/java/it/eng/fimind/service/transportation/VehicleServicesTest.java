package it.eng.fimind.service.transportation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.eng.fimind.model.fiware.transportation.Vehicle;

public class VehicleServicesTest {
    public static void main(String[] args) throws IOException {
    	String fileName = "vehicle.json";

        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
        System.out.println("File Found : " + file.exists());

        String jsonBody = new String(Files.readAllBytes(file.toPath()));

        ObjectMapper objectMapper = new ObjectMapper();
        Vehicle vehicle = objectMapper.readValue(jsonBody, Vehicle.class);
        
        VehicleServices vehicleServices = new VehicleServices();
        vehicleServices.createMindSphereAssetFromVehicle(vehicle);
    }
}
