package com.apollon.util.firebase;

import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by giuseppe on 29/05/16.
 */
public class prova2 {
	
	final static private String FCM_URL = "https://fcm.googleapis.com/fcm/send";
	private static String tokenId = "AAAA4aN8UQM:APA91bG33mcZIPIc4x_229EyjnQQcG7WDaMw675SMKDuF1076jmNduuaRUHrHLINjPip0Jxt8zUgt-6bSsUTe9v_vFWzt2S3KkYAHZIM0WUsnb9pIKxxPOnPC5--Q3BC5P-_fCqqSkSg";
	private static String key = "AIzaSyB-imWprD7EBpNSxGZOCci25AZNOXV4iyg";
	private static String senderID = "969110475011";

    public static boolean send() {
    	Map<String, String> data = new HashMap<String, String>();
    	data.put("A","one");
    	data.put("B","two");
        try {
            //preparazione  JSON contenente il FCM content. Cosa mandare e dove mandare
            JSONObject jFcmData = new JSONObject();
            JSONObject jData = new JSONObject();
            for (String key_data : data.keySet()) {
                jData.put(key_data, data.get(key_data));
            }
            jFcmData.put("to", tokenId);
            jFcmData.put("data", jData);

            //creazione connessione per mandare FCM Message request
            URL url = new URL( FCM_URL );
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization", "key=" + key);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            // Invio FCM message content.
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(jFcmData.toString().getBytes());

            // Lettura FCM response.
            InputStream inputStream = conn.getInputStream();
            String resp = IOUtils.toString(inputStream);
            Gson gson = new Gson();
            Type typeOfHashMap = new TypeToken<Map<String, Object>>() {
            }.getType();
            Map<String, Object> newMap = gson.fromJson(resp, typeOfHashMap);

            String result = ((double) newMap.get("success") == 1) ? "SUCCESS" : "FAILURE";
            System.out.println("Response: " + result);
            if ((double) newMap.get("success") == 0) {
                System.out.println("Dettagli " + resp);
            }
            return ((double) newMap.get("success") == 1);

        } catch (IOException e) {
            System.out.println("Unable to send GCM message.");
            System.out.println("Please ensure that API_KEY has been replaced by the server " +
                    "API key, and that the device's registration token is correct (if specified).");
            e.printStackTrace();
        }
        return true;
    }

    public static void sendToTopic2() throws IOException {
    	Map<String, String> data = new HashMap<String, String>();
    	data.put("A","one");
    	data.put("B","two");
        HttpTransport transport = new NetHttpTransport();
        HttpRequestFactory requestFactory = transport.createRequestFactory(request -> {
            HttpHeaders headers = new HttpHeaders();
            headers.setAuthorization("key=" + key);
            headers.setContentType("application/json");
            request.setHeaders(headers);
        });
        //preparazione  JSON contenente il FCM content. Cosa mandare e dove mandare
        Map<String, Object> datamap = new HashMap<>();
        datamap.put("to", "topics/" + tokenId);
        datamap.put("data", data);
        //datamap.put("topic", "example");
        //datamap.put("to", tokenId);
        HttpContent content = new JsonHttpContent(new JacksonFactory(), datamap);
        try {
            content.writeTo(System.out);
        } catch (IOException e) {
            e.printStackTrace();
        }

        HttpRequest request = null;
        try {
            request = requestFactory.buildPostRequest(new GenericUrl("https://fcm.googleapis.com/fcm/send"), content);
            request.setUnsuccessfulResponseHandler(new HttpBackOffUnsuccessfulResponseHandler(new ExponentialBackOff()));
            System.out.println("Invio ...");
            com.google.api.client.http.HttpResponse response = request.execute();
            String resp = IOUtils.toString(response.getContent());
            System.out.println("Response " + resp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
