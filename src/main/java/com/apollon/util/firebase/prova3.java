package com.apollon.util.firebase;


import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Scanner;

public class prova3 {
    private static final String PROJECT_ID = "apollotransfert-2";
    private static final String BASE_URL = "https://fcm.googleapis.com";
    private static final String FCM_SEND_ENDPOINT = "/v1/projects/" + PROJECT_ID + "/messages:send";

    private static final String MESSAGING_SCOPE = "https://www.googleapis.com/auth/firebase.messaging";
    private static final String[] SCOPES = { MESSAGING_SCOPE };

    public static final String MESSAGE_KEY = "message";

    /**
     * Retrieve a valid access token that can be use to authorize requests to the FCM REST
     * API.
     *
     * @return Access token.
     * @throws IOException
     */
    // [START retrieve_access_token]
    private static String getAccessToken() throws IOException {
    	
    	ClassLoader loader = Thread.currentThread().getContextClassLoader();
		//InputStream resourceStream = loader.getResourceAsStream( "google-services.json" );
		InputStream resourceStream = loader.getResourceAsStream( "apollotransfert-2-firebase-adminsdk-veqmq-68b15f61f6.json" );
    	
        GoogleCredential googleCredential = GoogleCredential.fromStream( resourceStream ).createScoped(Arrays.asList(SCOPES));
        googleCredential.refreshToken();
        return googleCredential.getAccessToken();
    }
    // [END retrieve_access_token]

    /**
     * Create HttpURLConnection that can be used for both retrieving and publishing.
     *
     * @return Base HttpURLConnection.
     * @throws IOException
     */
    private static HttpURLConnection getConnection() throws IOException {
        // [START use_access_token]
        
    	String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";
    	String AUTH_KEY_FCM = "AAAA4aN8UQM:APA91bGfAwROQK9HbwScYVPRePrCH4f6HtPpMNPWSpONg4ttXLrTBR3rMPnPd72-flD9VOo1NUQIBICzEy27Tuw-yx1PSHYJmdSuh6sW4pfbygBgoGuxV99-aF7H0uhUdWx5ePOXS7hQ";
    	
    	URL url = new URL(BASE_URL + FCM_SEND_ENDPOINT);
    	//URL url = new URL(API_URL_FCM);
        
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        
        httpURLConnection.setRequestProperty("Authorization", "Bearer " + AUTH_KEY_FCM);
        //httpURLConnection.setRequestProperty("Authorization", "Bearer " + getAccessToken());
        //httpURLConnection.setRequestProperty("Authorization", "key=" + AUTH_KEY_FCM);
 
        httpURLConnection.setRequestMethod("POST");
        
        httpURLConnection.setRequestProperty("Content-Type", "application/json; UTF-8");
        return httpURLConnection;
        // [END use_access_token]
    }

    /**
     * Send request to FCM message using HTTP.
     *
     * @param fcmMessage Body of the HTTP request.
     * @throws IOException
     */
    private static void sendMessage(JsonObject fcmMessage) throws IOException {
        HttpURLConnection connection = getConnection();
        connection.setDoOutput(true);
        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        outputStream.write(fcmMessage.toString().getBytes("UTF-8"));
        outputStream.flush();
        outputStream.close();

        int responseCode = connection.getResponseCode();
        if (responseCode == 200) {
            String response = inputstreamToString(connection.getInputStream());
            System.out.println("Message sent to Firebase for delivery, response:");
            System.out.println(response);
        } else {
            System.out.println("Unable to send message to Firebase:");
            String response = inputstreamToString(connection.getErrorStream());
            System.out.println(response);
        }
    }

    /**
     * Build the android payload that will customize how a message is received on Android.
     *
     * @return android payload of an FCM request.
     */
    private static JsonObject buildAndroidOverridePayload() {
        JsonObject androidNotification = new JsonObject();
        androidNotification.addProperty("click_action", "android.intent.action.MAIN");

        JsonObject androidNotificationPayload = new JsonObject();
        androidNotificationPayload.add("notification", androidNotification);

        return androidNotificationPayload;
    }

    /**
     * Build the apns payload that will customize how a message is received on iOS.
     *
     * @return apns payload of an FCM request.
     */
    private static JsonObject buildApnsHeadersOverridePayload() {
        JsonObject apnsHeaders = new JsonObject();
        apnsHeaders.addProperty("apns-priority", "10");

        return apnsHeaders;
    }

    /**
     * Build aps payload that will add a badge field to the message being sent to
     * iOS devices.
     *
     * @return JSON object with aps payload defined.
     */
    private static JsonObject buildApsOverridePayload() {
        JsonObject badgePayload = new JsonObject();
        badgePayload.addProperty("badge", 1);

        JsonObject apsPayload = new JsonObject();
        apsPayload.add("aps", badgePayload);

        return apsPayload;
    }

    /**
     * Send notification message to FCM for delivery to registered devices.
     *
     * @throws IOException
     */
    public static void sendCommonMessage(String deviceToken, String title, String body) throws IOException {
        JsonObject notificationMessage = buildNotificationMessageWithDeviceToken(deviceToken, title, body);
        System.out.println("FCM request body for message using common notification object:");
        prettyPrint(notificationMessage);
        sendMessage(notificationMessage);
    }

    /**
     * Send notification message to FCM for delivery to specific topic.
     *
     * @throws IOException
     */
    public static void sendTopicMessage(String topic, String title, String body) throws IOException {
        JsonObject notificationMessage = buildNotificationMessage(topic, title, body);
        System.out.println("FCM request body for message using common notification object:");
        prettyPrint(notificationMessage);
        sendMessage(notificationMessage);
    }

    /**
     * Construct the body of a notification message request.
     *
     * @return JSON of notification message.
     */
    private static JsonObject buildNotificationMessageWithDeviceToken(String deviceToken, String title, String body) {
        JsonObject jNotification = new JsonObject();
        jNotification.addProperty("title", title);
        jNotification.addProperty("body", body);
        
        JsonObject jMessage = new JsonObject();
        jMessage.add("notification", jNotification);
        jMessage.addProperty("token", deviceToken);

        JsonObject jFcm = new JsonObject();
        jFcm.add(MESSAGE_KEY, jMessage);

        return jFcm;
    }

    private static JsonObject buildNotificationMessage(String topic, String title, String body) {
        JsonObject jNotification = new JsonObject();
        jNotification.addProperty("title", title);
        jNotification.addProperty("body", body);

        JsonObject jMessage = new JsonObject();
        jMessage.add("notification", jNotification);
        jMessage.addProperty("topic", topic);

        JsonObject jFcm = new JsonObject();
        jFcm.add(MESSAGE_KEY, jMessage);

        return jFcm;
    }

    /**
     * Read contents of InputStream into String.
     *
     * @param inputStream InputStream to read.
     * @return String containing contents of InputStream.
     * @throws IOException
     */
    private static String inputstreamToString(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        Scanner scanner = new Scanner(inputStream);
        while (scanner.hasNext()) {
            stringBuilder.append(scanner.nextLine());
        }
        return stringBuilder.toString();
    }

    /**
     * Pretty print a JsonObject.
     *
     * @param jsonObject JsonObject to pretty print.
     */
    private static void prettyPrint(JsonObject jsonObject) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(gson.toJson(jsonObject) + "\n");
    }
}
