package it.eng.fimind.service.weather;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siemens.mindsphere.sdk.assetmanagement.model.Asset;

import it.eng.fimind.model.fiware.weather.WeatherForecast;

public class WeatherForecastServicesTest {
    public static void main(String[] args) throws IOException {
    	String fileName = "weatherforecast.json";

        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
        System.out.println("File Found : " + file.exists());

        String jsonBody = new String(Files.readAllBytes(file.toPath()));

        ObjectMapper objectMapper = new ObjectMapper();
        WeatherForecast weatherForecast = objectMapper.readValue(jsonBody, WeatherForecast.class);
        
        WeatherForecastServices weatherForecastServices = new WeatherForecastServices();
        Asset asset = weatherForecastServices.createMindSphereAssetFromWeatherForecast(weatherForecast);
        System.out.println(asset);
    }
}