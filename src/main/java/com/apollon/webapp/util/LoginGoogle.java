package com.apollon.webapp.util;

import com.apollon.Constants;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;

/**
 * vedere: https://www.logicbig.com/tutorials/java-ee-tutorial/java-servlet/servlet-oauth.html e https://developers.google.com/identity/sign-in/web/sign-in
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public class LoginGoogle {

	
	public static GoogleIdToken.Payload getPayload(String tokenString) throws Exception {
		JacksonFactory jacksonFactory = new JacksonFactory();
		GoogleIdTokenVerifier googleIdTokenVerifier = new GoogleIdTokenVerifier(new NetHttpTransport(), jacksonFactory);
		GoogleIdToken token = GoogleIdToken.parse(jacksonFactory, tokenString);
		if (googleIdTokenVerifier.verify(token)) {
			GoogleIdToken.Payload payload = token.getPayload();
			if (!Constants.GOOGLE_CLIENT_ID.equals(payload.getAudience())) {
				return null;
			} else if (!Constants.GOOGLE_CLIENT_ID.equals(payload.getAuthorizedParty())) {
				return null;
			}
			return payload;
		} else {
			return null;
		}
	}
	
	
	/*
	public static GoogleIdToken.Payload getPayload(String tokenString) throws Exception {

		JacksonFactory jacksonFactory = new JacksonFactory();
		GoogleIdTokenVerifier googleIdTokenVerifier = new GoogleIdTokenVerifier(new NetHttpTransport(), jacksonFactory);

		GoogleIdToken token = GoogleIdToken.parse(jacksonFactory, tokenString);

		if (googleIdTokenVerifier.verify(token)) {
			GoogleIdToken.Payload payload = token.getPayload();
			if (!Constants.GOOGLE_CLIENT_ID.equals(payload.getAudience())) {
				throw new IllegalArgumentException("Audience mismatch");
			} else if (!Constants.GOOGLE_CLIENT_ID.equals(payload.getAuthorizedParty())) {
				throw new IllegalArgumentException("Client ID mismatch");
			}
			return payload;
		} else {
			throw new IllegalArgumentException("id token cannot be verified");
		}
	}
	*/

}
