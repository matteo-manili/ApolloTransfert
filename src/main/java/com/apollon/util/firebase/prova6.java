package com.apollon.util.firebase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONException;
import org.json.JSONObject;

public class prova6 {



/**
* 
* Method to send push notification to Android FireBased Cloud messaging Server.
*
* @param tokenId Generated and provided from Android Client Developer
* @param server_key Key which is Generated in FCM Server 
* @param message which contains actual information.
* 
*/
	final static private String FCM_URL = "https://fcm.googleapis.com/fcm/send";
	private static String tokenId = "AAAA4aN8UQM:APA91bGfAwROQK9HbwScYVPRePrCH4f6HtPpMNPWSpONg4ttXLrTBR3rMPnPd72-flD9VOo1NUQIBICzEy27Tuw-yx1PSHYJmdSuh6sW4pfbygBgoGuxV99-aF7H0uhUdWx5ePOXS7hQ";
	private static String key = "AIzaSyB-imWprD7EBpNSxGZOCci25AZNOXV4iyg";
	private static String senderID = "969110475011";
	
	public static void send_FCM_Notification(String tokenId, String server_key, String message){
	
		message = "Welcome to FCM Server push notification!.";
		try{
			// Create URL instance.
			URL url = new URL(FCM_URL);
			
			// create connection.
			HttpURLConnection conn;
			conn = (HttpURLConnection) url.openConnection();
			conn.setUseCaches(false);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			
			//set method as POST or GET
			conn.setRequestMethod("POST");
			
			//pass FCM server key
			conn.setRequestProperty("Authorization","key="+server_key);
			
			//Specify Message Format
			conn.setRequestProperty("Content-Type","application/json");
			
			//Create JSON Object & pass value
			
			JSONObject infoJson = new JSONObject();
			infoJson.put("title","Here is your notification.");
			infoJson.put("body", message);
			
			JSONObject json = new JSONObject();
			json.put("to",tokenId.trim());
			json.put("notification", infoJson);
			
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(json.toString());
			wr.flush();
			int status = 0;
			if( null != conn ){
				status = conn.getResponseCode();
			}
			
			if( status != 0){
				if( status == 200 ){
					//SUCCESS message
					BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					System.out.println("Android Notification Response : " + reader.readLine());
				}else if(status == 401){
					//client side error
					System.out.println("Notification Response : TokenId : " + tokenId + " Error occurred :");
				}else if(status == 501){
					//server side error
					System.out.println("Notification Response : [ errorCode=ServerError ] TokenId : " + tokenId);
				}else if( status == 503){
					//server side error
					System.out.println("Notification Response : FCM Service is Unavailable  TokenId : " + tokenId);
				}
			}
		
		}catch(MalformedURLException mlfexception){
			// Prototcal Error
			System.out.println("Error occurred while sending push Notification!.." + mlfexception.getMessage());
		}catch(IOException mlfexception){
			//URL problem
			System.out.println("Reading URL, Error occurred while sending push Notification!.." + mlfexception.getMessage());
		}catch(JSONException jsonexception){
			//Message format error
			System.out.println("Message Format, Error occurred while sending push Notification!.." + jsonexception.getMessage());
		}catch (Exception exception) {
			//General Error or exception.
			System.out.println("Error occurred while sending push Notification!.." + exception.getMessage());
		}
	
	}

}





//TestFCM.java:
/*
public class TestFCM {




public static void main(String[] args) {



//Just I am passed dummy information

String tokenId = "d3U2xL0dPEM:APA91bF9lLjYLJDzGFgUnR2pyxQNFWkbLT9P8kdcrK0NCdZET3I-vCqAFrf21fwNn7eU3N8IbjY52nSn0saDtlXPLuI_sCgHvOhXbtzS9NeE2Fu5f3IwEUOi2nzSzoW3_a3O1lFlx35O";

String server_key = "AIzaSyAn9G3Vbeo0ronzW1RWliM-8SYASkEtVGI" ;

String message = "Welcome to FCM Server push notification!.";



//Method to send Push Notification

FCM.send_FCM_Notification( tokenId,server_key,message);

}


}

*/
