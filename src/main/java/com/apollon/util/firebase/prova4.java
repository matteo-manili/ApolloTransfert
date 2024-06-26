package com.apollon.util.firebase;

import java.io.IOException;
import java.io.InputStream;

import com.apollon.webapp.util.ApplicationUtils;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.Transaction.Result;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.WebpushConfig;
import com.google.firebase.messaging.WebpushNotification;


public class prova4 extends ApplicationUtils {

	private static String tokenId = "AAAA4aN8UQM:APA91bG33mcZIPIc4x_229EyjnQQcG7WDaMw675SMKDuF1076jmNduuaRUHrHLINjPip0Jxt8zUgt-6bSsUTe9v_vFWzt2S3KkYAHZIM0WUsnb9pIKxxPOnPC5--Q3BC5P-_fCqqSkSg";
	private static String key = "AIzaSyB-imWprD7EBpNSxGZOCci25AZNOXV4iyg";
	private static String senderID = "969110475011";
	
	public static void Connect() {
		try {
			
			
			// INIZIALIZZO FIREBASE
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			InputStream resourceStream = loader.getResourceAsStream( "apollotransfert-2-firebase-adminsdk-veqmq-68b15f61f6.json" );
			FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredentials(GoogleCredentials.fromStream(resourceStream))
					.setDatabaseUrl("https://apollotransfert-2.firebaseio.com")
					.build();
			FirebaseApp.initializeApp(options);
			
			
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
}


