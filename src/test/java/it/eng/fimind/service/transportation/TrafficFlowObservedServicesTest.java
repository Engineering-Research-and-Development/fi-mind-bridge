package it.eng.fimind.service.transportation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.eng.fimind.model.fiware.transportation.TrafficFlowObserved;

public class TrafficFlowObservedServicesTest {
    public static void main(String[] args) throws IOException {
    	String fileName = "trafficflowobserved.json";

        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
        System.out.println("File Found : " + file.exists());

        String jsonBody = new String(Files.readAllBytes(file.toPath()));

        ObjectMapper objectMapper = new ObjectMapper();
        TrafficFlowObserved trafficFlowObserved = objectMapper.readValue(jsonBody, TrafficFlowObserved.class);
        
        TrafficFlowObservedServices trafficFlowObservedServices = new TrafficFlowObservedServices();
        trafficFlowObservedServices.createMindSphereAssetFromTrafficFlowObserved(trafficFlowObserved);
    }
}
