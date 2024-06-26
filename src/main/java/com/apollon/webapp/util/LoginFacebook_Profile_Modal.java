package com.apollon.webapp.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * vedere: https://www.chillyfacts.com/java-integrate-login-facebook-using-graph-api/
 * 
 * https://developers.facebook.com/apps/669241703502668/settings/basic/?business_id=959749504536233
 * https://developers.facebook.com/docs/facebook-login/web/login-button/
 * https://developers.facebook.com/docs/graph-api/reference/user/
 * 
 * 
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public class LoginFacebook_Profile_Modal  {

	public LoginFacebook_Profile_Bean call_me(String access_token) throws Exception {
	     String url = "https://graph.facebook.com/v2.12/me?fields=id,name,first_name,last_name,email,picture&access_token="+access_token; 
	     URL obj = new URL(url);
	     HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	     // optional default is GET
	     con.setRequestMethod("GET");
	     //add request header
	     con.setRequestProperty("User-Agent", "Mozilla/5.0");
	     int responseCode = con.getResponseCode();
	     //System.out.println("\nSending 'GET' request to URL : " + url);
	     //System.out.println("Response Code : " + responseCode);
	     BufferedReader in = new BufferedReader(
	             new InputStreamReader(con.getInputStream()));
	     String inputLine;
	     StringBuffer response = new StringBuffer();
	     while ((inputLine = in.readLine()) != null) {
	     	response.append(inputLine);
	     }
	     in.close();
	     //System.out.println(response);
	     LoginFacebook_Profile_Bean obj_Profile_Bean=new LoginFacebook_Profile_Bean();
	     
	     try {
		     JSONObject myResponse = new JSONObject(response.toString());
		     obj_Profile_Bean.setId(myResponse.getString("id"));
		     obj_Profile_Bean.setName(myResponse.getString("name"));
		     obj_Profile_Bean.setFirst_name(myResponse.getString("first_name"));
		     obj_Profile_Bean.setLast_name(myResponse.getString("last_name"));
		     obj_Profile_Bean.setEmail(myResponse.getString("email"));
		     JSONObject picture_reponse=myResponse.getJSONObject("picture");
		     JSONObject data_response=picture_reponse.getJSONObject("data");
		     //System.out.println("URL : "+data_response.getString("url"));
		     obj_Profile_Bean.setPicture(data_response.getString("url"));
	     
	     }catch( JSONException jE  ) {
	    	 obj_Profile_Bean.setEmail( null );
	    	 return obj_Profile_Bean;
	     }
	     
	     
		return obj_Profile_Bean;
		
		
		
	   }

}
