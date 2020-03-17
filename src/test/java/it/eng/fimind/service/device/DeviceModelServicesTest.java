package it.eng.fimind.service.device;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.eng.fimind.model.fiware.device.DeviceModel;

public class DeviceModelServicesTest {
    public static void main(String[] args) throws IOException {
    	String fileName = "devicemodel.json";

        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
        System.out.println("File Found : " + file.exists());

        String jsonBody = new String(Files.readAllBytes(file.toPath()));

        ObjectMapper objectMapper = new ObjectMapper();
        DeviceModel deviceModel = objectMapper.readValue(jsonBody, DeviceModel.class);
        
        DeviceModelServices deviceModelServices = new DeviceModelServices();
        deviceModelServices.createMindSphereAssetFromDeviceModel(deviceModel);
    }
}
