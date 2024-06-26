package com.apollon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * vedere: https://www.mkyong.com/webservices/jax-rs/restful-java-client-with-apache-httpclient/
 * per i comandi vedere: https://gist.github.com/subfuzion/08c5d85437d5d4f00e58
 * @author Matteo
 *
 */
public class CURL {
	
	private static void RestCall_GET_1() {
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet getRequest = new HttpGet("https://localhost:8443/apollon/testRestGET_1_parametro1?parametro1=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjaXJvIiwicm9sZXMiOlsiUk9MRV9VU0VSIl0sImlzRW5hYmxlZCI6dHJ1ZSwiZXhwIjoxNTczNzIzNjI2LCJpYXQiOjE1NzM2MjM2Mjc2NDZ9.MON5-866pb3CCIrc5NSySi8RB23BKcpPm_b7k3S-oe8");
			//HttpGet getRequest = new HttpGet("https://apollotransfert.com/testRestGET_1_parametro1?parametro1=buongiorno");
			
			getRequest.addHeader("accept", "application/json");
			HttpResponse response = httpClient.execute(getRequest);
			
			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
			}
			
			BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
			String output;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				System.out.println(output);
			}
			httpClient.getConnectionManager().shutdown();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void RestCall_GET_2() {
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet getRequest = new HttpGet("https://localhost:8443/apollon/testRestGET_2");
			getRequest.addHeader("accept", "application/json");
			HttpResponse response = httpClient.execute(getRequest);
			
			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
			}
			
			BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
			String output;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				System.out.println(output);
			}
			httpClient.getConnectionManager().shutdown();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void RestCall_POST() {
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			//HttpPost postRequest = new HttpPost("https://localhost:8443/apollon/api_getListaAutoveicoliAutista");
			HttpPost postRequest = new HttpPost("https://www.apollotransfert.com/api_getListaAutoveicoliAutista");
			StringEntity input = new StringEntity("");
			input.setContentType("application/json; charset=UTF-8");
			
			postRequest.addHeader("Authorization", "eyJhbGciOiJIUzI1NiJ9.eyJhdXRpc3RhRGVub21pbmF6aW9uZUF6aWVuZGEiOiJDSVJPIFBBUVVBTEUgU1JMIiwiYXV0aXN0YUF0dGl2byI6dHJ1ZSwiYXV0aXN0YUFwcHJvdmF0byI6ZmFsc2UsImlkQXV0aXN0YSI6MiwiZXhwIjoyODY5Nzk4NzE3LCJpYXQiOjE1NzM3OTg3MTc3MTEsImZ1bGxOYW1lVXNlciI6IkNpcm8gUGFzcXVhbGUiLCJhdXRpc3RhQmFubiI6ZmFsc2V9.f2I7CKA0VFTlnbSfgIQ19-1HOalDqHvs61RvcEVD3j4");
			
			postRequest.setEntity(input);
			HttpResponse response = httpClient.execute(postRequest);

			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
			}
			
			BufferedReader br = new BufferedReader( new InputStreamReader((response.getEntity().getContent())));
			String output;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				System.out.println(output);
			}
			httpClient.getConnectionManager().shutdown();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void RestCall_POST_AUTH() {
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			//HttpPost postRequest = new HttpPost("https://localhost:8443/apollon/j_security_check");
			//HttpPost postRequest = new HttpPost("https://ncctransferonline.it/j_security_check");
			HttpPost postRequest = new HttpPost("https://apollotransfert.com/j_security_check");
			
			
			/*
			StringEntity input = new StringEntity("  {\"j_username\":\"admin\",\"j_password\":\"crauti\"}  ");
			input.setContentType("application/json");
			*/
			StringEntity input = new StringEntity("j_username=ciro&j_password=sasa");
			input.setContentType("application/x-www-form-urlencoded");
			postRequest.setEntity(input);
			//postRequest.addHeader("rest-authentication", "true"); //xhr.setRequestHeader("X-Ajax-call", "true");
			HttpResponse response = httpClient.execute(postRequest);

			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
			}
			
			BufferedReader br = new BufferedReader( new InputStreamReader((response.getEntity().getContent())));
			String output;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				System.out.println(output);
			}
			httpClient.getConnectionManager().shutdown();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		//RestCall_GET_1();
		//System.out.println("---------------------------");
		//RestCall_GET_2();
		//System.out.println("---------------------------");
		//RestCall_POST();
		//System.out.println("---------------------------");
		RestCall_POST_AUTH();
	}

}
