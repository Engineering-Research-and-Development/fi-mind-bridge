package it.eng.fimind.service.device;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import it.eng.fimind.util.HttpRequests;

public class DeviceServicesTest {
    public static void main(String[] args) throws IOException {
    	String fileName = "device.json";

        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
        System.out.println("File Found : " + file.exists());

        String jsonBody = new String(Files.readAllBytes(file.toPath()));

        HttpRequests httpReq = new HttpRequests();
        httpReq.sendPOST("http://localhost:8080/fimind/webapi/device", jsonBody);
    }
}
