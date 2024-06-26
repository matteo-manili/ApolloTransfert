package com.apollon.util.firebase;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.ws.rs.core.MediaType;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.firebase.database.Transaction.Result;
import com.google.firebase.messaging.Message;

public class prova1 {

	public static void InviaMessaggioFirebase() throws ClientProtocolException, IOException {
		
		/*
        URL url = new URL("https://fcm.googleapis.com/fcm/send");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization","key=AIzaSyB-imWprD7EBpNSxGZOCci25AZNOXV4iyg");
        conn.setRequestProperty("Content-Type", "application/json");

        JSONObject json = new JSONObject();

        json.put("to", "969110475011");


        JSONObject info = new JSONObject();
        info.put("title", "TechnoWeb");   // Notification title
        info.put("body", "Hello Test notification"); // Notification body

        json.put("notification", info);

        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(json.toString());
        wr.flush();
        conn.getInputStream();
		*/
		
		//----------------------------------------------------------------------
		
		
		String AUTH_KEY_FCM = "AIzaSyB-imWprD7EBpNSxGZOCci25AZNOXV4iyg";
		String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";

		String authKey = AUTH_KEY_FCM; // You FCM AUTH key
		String FMCurl = API_URL_FCM; 
		
		URL url = new URL(FMCurl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		
		conn.setUseCaches(false);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Authorization","key="+authKey);
		conn.setRequestProperty("Content-Type","application/json");
		
		JSONObject json = new JSONObject();
		
		//json.put("to", "969110475011");
		json.put("to", "969110475011"); 
		
		JSONObject info = new JSONObject();
		info.put("title", "Notificatoin Title"); // Notification title
		info.put("body", "Hello Test notification"); // Notification body
		json.put("notification", info);
		
		OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		wr.write(json.toString());
		wr.flush();
		conn.getInputStream();
		
		
		//-------------------------------------------------------------
		
		/*
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost("https://fcm.googleapis.com/fcm/send");
		post.setHeader("Content-type", "application/json");
		post.setHeader("Authorization", "key=AIzaSyB-imWprD7EBpNSxGZOCci25AZNOXV4iyg");

		JSONObject message = new JSONObject();
		message.put("to", "AAAA4aN8UQM:APA91bGfAwROQK9HbwScYVPRePrCH4f6HtPpMNPWSpONg4ttXLrTBR3rMPnPd72-flD9VOo1NUQIBICzEy27Tuw-yx1PSHYJmdSuh6sW4pfbygBgoGuxV99-aF7H0uhUdWx5ePOXS7hQ");
		message.put("priority", "high");

		JSONObject notification = new JSONObject();
		notification.put("title", "Java");
		notification.put("body", "Notificação do Java");

		message.put("notification", notification);

		post.setEntity(new StringEntity(message.toString(), "UTF-8"));
		HttpResponse response1 = client.execute(post);
		System.out.println(response1);
		System.out.println(message);
		*/
		
		//---------------------------------------------------------
		
		/*
		String url = "https://gcm-http.googleapis.com/gcm/send";     
		String API_KEY = "AIzaSyB-imWprD7EBpNSxGZOCci25AZNOXV4iyg";    
		String token = "AAAA4aN8UQM:APA91bGfAwROQK9HbwScYVPRePrCH4f6HtPpMNPWSpONg4ttXLrTBR3rMPnPd72-flD9VOo1NUQIBICzEy27Tuw-yx1PSHYJmdSuh6sW4pfbygBgoGuxV99-aF7H0uhUdWx5ePOXS7hQ";
		String TOPIC = "JavaSampleApproach";
		
		JSONObject json = new JSONObject(); 
		JSONObject json1 = new JSONObject();    
		JSONArray jsonArray = new JSONArray();    
		
		jsonArray.put( token );   
		 
		// for mutliple tokens
		//json.put("registration_ids", jsonArray);   
		
		// for single token
		json.put("to",token );       
		 
		// populate message
		json1.put("tap", false);
		json1.put("title","huraayyy");      
		json1.put("body", "corpoooo");
		
		json.put("data", json1);         
		 
		final CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		try{
			//Create POST request
			final HttpPost request = new HttpPost(url);     
			request.setHeader("Content-type", MediaType.APPLICATION_JSON); 
			request.setHeader("Authorization", "Key="+API_KEY);    
			
			StringEntity params = new StringEntity(json.toString());        
			request.setEntity(params);      
			 
			// get response from server
			HttpResponse response2 = httpClient.execute(request);       
			String mresult = EntityUtils.toString(response2.getEntity());        
			 
			System.out.println("result........" +mresult); 
		
		}catch (Exception ex) {     
			ex.printStackTrace();  
		} finally {
			if(httpClient != null) {       
				try {      
				httpClient.close();    
				} catch (IOException e) { 
					e.printStackTrace();       
				}       
			}  
		}
		*/
		
		
		
		
	}
	
	
	
}
