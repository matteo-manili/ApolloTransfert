package com.apollon.util.firebase;

import com.apollon.model.GestioneApplicazione;
import com.apollon.webapp.util.ApplicationUtils;
import com.apollon.webapp.util.sms.Invio_Email_Sms_UTIL;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;

/**
 * Firebase Cloud Messaging (FCM) can be used to send messages to clients on
 * iOS, Android and Web.
 *
 * This sample uses FCM to send two types of messages to clients that are
 * subscribed to the `news` topic. One type of message is a simple notification
 * message (display message). The other is a notification message (display
 * notification) with platform specific customizations, for topic_apollo_1, a badge is
 * added to messages that are sent to iOS devices.
 */
public class FirebaseCloudMessaging extends ApplicationUtils {

	private static final String PROJECT_ID = "apollotransfert-2";
	private static final String BASE_URL = "https://fcm.googleapis.com";
	private static final String FCM_SEND_ENDPOINT = "/v1/projects/" + PROJECT_ID + "/messages:send";
	
	private static final String MESSAGING_SCOPE = "https://www.googleapis.com/auth/firebase.messaging";
	private static final String[] SCOPES = { MESSAGING_SCOPE };
	
	public static final int RESPONSE_CODE_MESSAGGIO_INVIATO = 200;
	
	// MESSAGGIO
	private static final String MESSAGE_KEY = "message";
	// TOKEN (invio veloce)
	private static final String TOKEN = "token";
	// TOPIC (invio lento)
	private static final String TOPIC = "topic";
	private static final String NAME_TOPIC_APOLLO_1 = "topic_apollo_1";
	// PARAMETRI_PER_ANDROID_APP
	private static final String ANDROID_APP_PARAM = "android";
	//PRIORITY (invio veloce)
	private static final String PRIORITY = "priority";
	private static final String PRIORITY_HIGH = "high"; public static final String PRIORITY_NORMAL = "normal";
	// RESTRICTED_PACKAGE
	private static final String RESTRICTED_PACKAGE_NAME = "restricted_package_name";
	private static final String NOME_PACKAGE_APP = "apollotransfert.com";
	// TIME_TO_LIVE
	private static final String TIME_TO_LIVE = "time_to_live"; // non funziona forse è deprecato o non so dove posizionarlo nel json
	//NOTIFICA (non usare se il messaggio serve per fare automatismi)
	private static final String NOTIFICATION = "notification";
	private static final String TITLE = "title"; private static final String BODY = "body";
	//DATA
	private static final String DATA = "data";
	private static final String TESTO_MESSAGGIO_JSON = "TestoMessaggio"; 
	private static final String NUMERO_TELEFONO_JSON = "NumeroTelefono";
	private static final String TOKEN_SMS_JSON = "TokenSms";
	
	
	
	/**
	 * Create HttpURLConnection that can be used for both retrieving and publishing.
	 *
	 * @return Base HttpURLConnection.
	 * @throws IOException
	 */
	private static HttpURLConnection getConnection() throws IOException {
		// [START use_access_token]
		URL url = new URL(BASE_URL + FCM_SEND_ENDPOINT);
		HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
		httpURLConnection.setRequestProperty("Authorization", "Bearer " + getAccessToken());
		httpURLConnection.setRequestProperty("Content-Type", "application/json; UTF-8");
		return httpURLConnection;
		// [END use_access_token]
	}

	/**
	 * Retrieve a valid access token that can be use to authorize requests to the
	 * FCM REST API.
	 *
	 * @return Access token.
	 * @throws IOException
	 */
	private static String getAccessToken() throws IOException {
		// [START retrieve_access_token]
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		InputStream resourceStream = loader.getResourceAsStream("apollotransfert-2-firebase-adminsdk-veqmq-68b15f61f6.json");
		GoogleCredential googleCredential = GoogleCredential.fromStream(resourceStream).createScoped(Arrays.asList(SCOPES));
		googleCredential.refreshToken();
		return googleCredential.getAccessToken();
		// [END retrieve_access_token]
	}

	/**
	 * Send notification message to FCM for delivery to registered devices.
	 *
	 *	Ritorna il ResponseCode del messagio Firebase: 200 da esito messagio positivo
	 * @throws IOException
	 */
	public static int sendCommonMessageSms(HttpServletRequest request, String TestoMessaggio, String NumeroTelefono, String TokenSms) throws IOException {
		JsonObject notificationMessage = buildNotificationMessageSms(request, TestoMessaggio, NumeroTelefono, TokenSms);
		System.out.println("FCM request body for message using common notification object:");
		prettyPrint(notificationMessage);
		return sendMessage(notificationMessage, TokenSms);
	}
	
	public static int sendCommonMessageConfirmToken(String tokenDeviceFcm) throws IOException {
		JsonObject notificationMessage = buildNotificationMessageConfirmToken(tokenDeviceFcm);
		System.out.println("FCM request body for message using common notification object:");
		prettyPrint(notificationMessage);
		return sendMessage(notificationMessage, null);
	}
	
	
	/**
	 * Invio un messaggio di Test
	 * @param token
	 * @return
	 * @throws UnknownHostException
	 */
	private static JsonObject buildNotificationMessageConfirmToken(String token) throws UnknownHostException {
		JsonObject jMessage = new JsonObject();
		jMessage.addProperty(TOKEN, token);
		// paramentri opzionali
		JsonObject jAndroidAppParam = new JsonObject();
		// è un parametro opzionale, invia il messaggio a un determinato package
		jAndroidAppParam.addProperty(RESTRICTED_PACKAGE_NAME, NOME_PACKAGE_APP); 
		
    	jMessage.add(ANDROID_APP_PARAM, jAndroidAppParam);
    	// impacchetto il messaggio
		JsonObject jFcm = new JsonObject();
		jFcm.add(MESSAGE_KEY, jMessage);
		return jFcm;
	}
	
	/**
	 * Construct the body of a notification message request.<br>
	 * 
	 * !!! IMPORTANTE !!! le notifiche si generano prima della lettura del messaggio, Solo dopo l'apertura della notifica si esegue la lettura del messaggio.
	 * Ciò comporta che l'App non legge il messaggio prima di non aver aperto la notifica e quindi non esegue ciò che deve eseguire. 
	 * Per Disabilitare le notifiche semplicemente non aggiungerle al Json <br>
	 * 
	 * Generale:
	 * https://firebase.google.com/docs/cloud-messaging/concept-options
	 * 
	 * MESSAGGIO TIPO TOKEN: 
	 * Inviare Messaggi con Token per un sigolo device oppure per piccoli gruppi. Questo metodo assicura che il messaggio venga spedita in tempo reale, 
	 * settare anche la "priority" a "high".
	 * vedere: https://firebase.google.com/docs/cloud-messaging/concept-options
	 * 
	 * IMPOSTAZIONE PRIORITA': 
	 * Per settare la velocità:
	 * Il metodo per inviare messaggi velocemente è usare il token e settare la "priority" a high, opzioni sono normal" e "high"
	 * vedere: https://firebase.google.com/docs/cloud-messaging/android/first-message
	 * 
	 * IMPOSTAZIONE time_to_live: (non funziona forse è deprecato o non so dove posizionarlo nel json)
	 * Questo parametro specifica per quanto tempo (in secondi) il messaggio deve essere conservato nella memoria FCM se il dispositivo è offline. 
	 * Il tempo massimo di permanenza supportato è di 4 settimane e il valore predefinito è 4 settimane. Per ulteriori informazioni, 
	 * vedere Impostazione della durata di un messaggio.
	 * vedere: https://firebase.google.com/docs/cloud-messaging/android/first-message
	 * 
	 * MESSAGGIO TIPO TOPIC: 
	 * Messaggio Topic (per gruppi di device) arrivano lentamente, anche dopo 30 minuti, ma serve per inviare messaggi a tanti device che hanno sottoscritto un certo topic:
	 * https://firebase.google.com/docs/cloud-messaging/android/topic-messaging
	 * 
	 * 
	 * Lista completa dei parametri:
	 * https://firebase.google.com/docs/cloud-messaging/http-server-ref
	 *
	 * @return JSON of notification message.
	 * @throws UnknownHostException 
	 */
	private static JsonObject buildNotificationMessageSms(HttpServletRequest request, String TestoMessaggio, String NumeroTelefono, String TokenSms) throws UnknownHostException {
		
		JsonObject jMessage = new JsonObject();
		
		// questo imposta la notifica al messaggio, ciè la tendina che si apre in alto. 
		// La notifica comporta che il messaggio verrà letto solo dopo che è stata aperta la notifica. 
		// Quindi non va usata nel caso in cui i messaggi devono fare qualcosa in automatico.
		/*
		JsonObject jNotification = new JsonObject();
		jNotification.addProperty(TITLE, "ApolloTransfert");
		jNotification.addProperty(BODY, "Messaggio da ApolloTransfert");
		jMessage.add(NOTIFICATION, jNotification);
		*/
		
		if( CheckAmbienteApolloTransfert_Mondoserver(request) ) {
			// Questo serve a inviare messaggio a uno specifico device (oppure a un piccolo gruppo) - VELOCE
			// Solo sul server di produzione trovo il token corretto perché la app setta il token sul server di produzione e non sul di di localhost
			GestioneApplicazione gestioneApp_TokenDeviceFcm = gestioneApplicazioneDao.getName("TOKEN_DEVICE_FCM");
			jMessage.addProperty(TOKEN, gestioneApp_TokenDeviceFcm.getValueString());
		}else {
			// se mi trovo su localhos devo usare la forma topc perché non dispongo del token!
			// questo serve a inviare a un gruppo di device che hanno sottoscritto un determimanto topic. Inviarie i messaggi col Topic 
			// abbassa le prestazioni di velocità (non usare se servono messaggi in tempo reale) - LENTO
			jMessage.addProperty(TOPIC, NAME_TOPIC_APOLLO_1);
			// per mandare messaggi via token in ambiente di sviluppo (recuperare la chiave token device)
			//jMessage.addProperty(TOKEN, "c9IbJSmR3h0:APA91bFlLbojP0Mvxi7jW1ogWwmwiUjMd5tdZNIJmpOpfA8T-D46bvkZYgwDsi0xdO1LkOd9H9LTpBW8cMyMe4sKaLBlx_myGbNym60lkjnUgd8vAxKkcPdHYZ3CJ689VnNe5GElBSvn");
		}
		
		// paramentri opzionali
		JsonObject jAndroidAppParam = new JsonObject();
		jAndroidAppParam.addProperty(PRIORITY, PRIORITY_HIGH); // VELOCE
		// è un parametro opzionale, invia il messaggio a un determinato package
		jAndroidAppParam.addProperty(RESTRICTED_PACKAGE_NAME, NOME_PACKAGE_APP);
    	jMessage.add(ANDROID_APP_PARAM, jAndroidAppParam);
		
    	// allego informazioni Data
		JsonObject jData = new JsonObject();
		jData.addProperty(TESTO_MESSAGGIO_JSON, TestoMessaggio);
		jData.addProperty(NUMERO_TELEFONO_JSON, NumeroTelefono); // numero silvia +393934040437 +393289126869
		jData.addProperty(TOKEN_SMS_JSON, TokenSms);
    	jMessage.add(DATA, jData);
    	
    	// impacchetto il messaggio
		JsonObject jFcm = new JsonObject();
		jFcm.add(MESSAGE_KEY, jMessage);
		return jFcm;
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

	/**
	 * Send request to FCM message using HTTP.
	 *
	 * @param fcmMessage Body of the HTTP request.
	 * @throws IOException
	 */
	private static int sendMessage(JsonObject fcmMessage, String tokenSms) throws IOException {
		HttpURLConnection connection = getConnection();
		connection.setDoOutput(true);
		DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
		outputStream.writeBytes(fcmMessage.toString());
		outputStream.flush();
		outputStream.close();
		int responseCode = connection.getResponseCode();
		if (responseCode == RESPONSE_CODE_MESSAGGIO_INVIATO) {
			String response = inputstreamToString(connection.getInputStream());
			System.out.println("Message sent to Firebase for delivery, response: "+response +" responseCode: "+responseCode);
		} else {
			System.out.println("Unable to send message to Firebase:");
			String response = inputstreamToString(connection.getErrorStream());
			System.out.println(response);
		}
		return responseCode;
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

	// ====================================================================================================
	// ====================================================================================================
	// ====================================================================================================
	
	/**
	 * Send a message that uses the common FCM fields to send a notification message
	 * to all platforms. Also platform specific overrides are used to customize how
	 * the message is received on Android and iOS.
	 *
	 * @throws IOException
	 */
	/*
	private static void sendOverrideMessage() throws IOException {
		JsonObject overrideMessage = buildOverrideMessage();
		System.out.println("FCM request body for override message:");
		prettyPrint(overrideMessage);
		sendMessage(overrideMessage);
	}
	*/

	/**
	 * Build the body of an FCM request. This body defines the common notification
	 * object as well as platform specific customizations using the android and apns
	 * objects.
	 *
	 * @return JSON representation of the FCM request body.
	 */
	/*
	private static JsonObject buildOverrideMessage() {
		JsonObject jNotificationMessage = buildNotificationMessage();
		JsonObject messagePayload = jNotificationMessage.get(MESSAGE_KEY).getAsJsonObject();
		messagePayload.add("android", buildAndroidOverridePayload());
		JsonObject apnsPayload = new JsonObject();
		apnsPayload.add("headers", buildApnsHeadersOverridePayload());
		apnsPayload.add("payload", buildApsOverridePayload());
		messagePayload.add("apns", apnsPayload);
		jNotificationMessage.add(MESSAGE_KEY, messagePayload);
		return jNotificationMessage;
	}
	*/
	/**
	 * Build the android payload that will customize how a message is received on
	 * Android.
	 *
	 * @return android payload of an FCM request.
	 */
	/*
	private static JsonObject buildAndroidOverridePayload() {
		JsonObject androidNotification = new JsonObject();
		androidNotification.addProperty("click_action", "android.intent.action.MAIN");
		JsonObject androidNotificationPayload = new JsonObject();
		androidNotificationPayload.add("notification", androidNotification);
		return androidNotificationPayload;
	}
	*/
	/**
	 * Build the apns payload that will customize how a message is received on iOS.
	 *
	 * @return apns payload of an FCM request.
	 */
	/*
	private static JsonObject buildApnsHeadersOverridePayload() {
		JsonObject apnsHeaders = new JsonObject();
		apnsHeaders.addProperty("apns-priority", "10");
		return apnsHeaders;
	}
	*/
	/**
	 * Build aps payload that will add a badge field to the message being sent to
	 * iOS devices.
	 *
	 * @return JSON object with aps payload defined.
	 */
	/*
	private static JsonObject buildApsOverridePayload() {
		JsonObject badgePayload = new JsonObject();
		badgePayload.addProperty("badge", 1);
		JsonObject apsPayload = new JsonObject();
		apsPayload.add("aps", badgePayload);
		return apsPayload;
	}
	*/

}
