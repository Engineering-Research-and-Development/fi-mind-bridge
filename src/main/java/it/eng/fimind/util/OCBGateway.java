package it.eng.fimind.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

public class OCBGateway {
	String ocb_url;
	
	public OCBGateway(){
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("config.properties").getFile());
		FileInputStream input;
		try {
			input = new FileInputStream(file);
			// load a properties file
			Properties prop = new Properties();
			prop.load(input);
			ocb_url = prop.getProperty("ocb-url");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
	}
	
	public void pushToOCB(String jsonBody){
		try {
			URL obj = new URL(ocb_url+"/v2/entities?options=keyValues");
						
			HttpURLConnection con;
			con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("content-type", "application/json");
			con.setRequestProperty("fiware-service", "connector");
			con.setRequestProperty("fiware-servicepath", "/mindsphere");
			con.setDoOutput(true);

			// For POST only - START
			con.setDoOutput(true);
			OutputStream os = con.getOutputStream();
			os.write(jsonBody.getBytes());
			os.flush();
			os.close();
			// For POST only - END
	
			int responseCode = con.getResponseCode();
			System.out.println("POST Response Code :: " + responseCode);
	
			if (responseCode == HttpURLConnection.HTTP_CREATED) { //success
				BufferedReader in = new BufferedReader(new InputStreamReader(
						con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();
	
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
	
				// print result
				System.out.println(response.toString());
			} else {
				System.out.println("POST request not worked");
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}


