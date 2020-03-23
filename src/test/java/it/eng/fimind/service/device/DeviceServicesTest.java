package it.eng.fimind.service.device;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siemens.mindsphere.sdk.assetmanagement.model.Asset;

import it.eng.fimind.model.fiware.device.Device;

public class DeviceServicesTest {
    public static void main(String[] args) throws IOException {
    	String fileName = "device.json";

        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
        System.out.println("File Found : " + file.exists());

        String jsonBody = new String(Files.readAllBytes(file.toPath()));

        ObjectMapper objectMapper = new ObjectMapper();
        Device device = objectMapper.readValue(jsonBody, Device.class);
        
        DeviceServices deviceServices = new DeviceServices();
        Asset asset = deviceServices.createMindSphereAssetFromDevice(device);
        System.out.println(asset);
    }
}
