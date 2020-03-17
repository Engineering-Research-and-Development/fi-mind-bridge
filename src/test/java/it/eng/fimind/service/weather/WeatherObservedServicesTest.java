package it.eng.fimind.service.weather;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.eng.fimind.model.fiware.weather.WeatherObserved;

public class WeatherObservedServicesTest {
    public static void main(String[] args) throws IOException {
    	String fileName = "weatherobserved.json";

        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
        System.out.println("File Found : " + file.exists());

        String jsonBody = new String(Files.readAllBytes(file.toPath()));

        ObjectMapper objectMapper = new ObjectMapper();
        WeatherObserved weatherObserved = objectMapper.readValue(jsonBody, WeatherObserved.class);
        
        WeatherObservedServices weatherObservedServices = new WeatherObservedServices();
        weatherObservedServices.createMindSphereAssetFromWeatherObserved(weatherObserved);
    }
}
