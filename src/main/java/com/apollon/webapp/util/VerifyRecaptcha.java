package com.apollon.webapp.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import org.json.JSONObject;
import com.apollon.Constants;

/**
 * 
 * Vedere: https://www.google.com/recaptcha/admin#list (console dove settare i domini permessi [apollotransfert.com,ncctransferonline.it,sviluppo.apollotransfert.com,localhost] 
 * e dove prendere le chiavi e un esempio)
 * vedere: https://developers.google.com/recaptcha/docs/invisible (documentazione ufficiale) (ci sono esempi javascript)
 * e vedere: https://www.journaldev.com/7133/how-to-integrate-google-recaptcha-in-java-web-application (sito dove ho preso il codice)
 * e vedere: https://appuntidiweb.wordpress.com/2017/11/26/come-implementare-invisible-recaptcha-il-captcha-invisibile-di-google/ (documentazione in italiano)
 * 
 * @author Matteo - matteo.manili@gmail.com
 */
public class VerifyRecaptcha {
	private final static String USER_AGENT = "Mozilla/5.0";

	public static boolean Verify(String gRecaptchaResponse) throws IOException {
		if (gRecaptchaResponse == null || "".equals(gRecaptchaResponse)) {
			return false;
		}
		try{
			URL obj = new URL( Constants.RECAPTCHA_URL );
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
	
			// add reuqest header
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Accept-Language", "en-US,en,it;q=0.5");
			String postParams = "secret=" + Constants.RECAPTCHA_SECRET + "&response=" + gRecaptchaResponse;
	
			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(postParams);
			wr.flush();
			wr.close();
	
			//int responseCode = con.getResponseCode();
	
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
	
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			JSONObject jb = new JSONObject( response.toString() );
			return jb.getBoolean( "success" );

		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
}