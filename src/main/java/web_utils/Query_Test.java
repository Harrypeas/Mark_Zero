package web_utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Query_Test {
	public static void main(String[] args)
	{
		doGet();
	}
	
	public static void doGet()
	{
		try {
			String param = "user_name=" + URLEncoder.encode("harrypeas","utf-8") + "&user_id=" + URLEncoder.encode("123","utf-8");

			URL url = new URL("http://localhost:8080/LoginValidation/LoginValidation?" + param);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			connection.setRequestMethod("GET");
			connection.connect();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line = "";
			while ((line = reader.readLine()) != null)
			{
				System.out.println(line);
			}
			
			reader.close();
			connection.disconnect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
	}
	
	public static void doPost()
	{

		try {

			URL url = new URL("http://localhost:8080/LoginValidation/LoginValidation");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			
			String param = "user_name=" + URLEncoder.encode("harrypeas","utf-8") + "&user_id=" + URLEncoder.encode("123","utf-8");

			connection.setRequestMethod("POST");
			connection.connect();
			DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
			dStream.writeBytes(param);
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line = "";
			while ((line = reader.readLine()) != null)
			{
				System.out.println(line);
			}
			
			reader.close();
			connection.disconnect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
