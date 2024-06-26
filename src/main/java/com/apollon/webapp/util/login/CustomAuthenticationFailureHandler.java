package com.apollon.webapp.util.login;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.apollon.Constants;

/**
 * @author Matteo - matteo.manili@gmail.com
 * 
 * per il ajax spring login vedere: https://gal-levinsky.blogspot.it/2011/08/spring-security-3-ajax-login.html?showComment=1484708527942#c804789591960377995
 per autenticazione personalizzata vedere: http://www.baeldung.com/spring_redirect_after_login
 http://www.jcombat.com/spring/custom-authentication-success-handler-with-spring-security
 *
 */
@Component("authenticationFailureHandler")
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {   
	private final Log log = LogFactory.getLog(getClass());
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
    @SuppressWarnings("unused")
	@Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
    	HttpSession session = request.getSession();
    	//DefaultSavedRequest defaultSavedRequest = (DefaultSavedRequest) session.getAttribute(WebAttributes.SAVED_REQUES);
    	//check if login is originated from ajax call
    	log.info("sono in onAuthenticationFailure getServerName: "+request.getServerName());
    	/*
    	Enumeration<String> enumerat = request.getHeaderNames();
    	while (enumerat.hasMoreElements()) {
    	    String param = enumerat.nextElement();
    	    System.out.println("enumerat: "+param  +" getHeader: "+ request.getHeader(param) );
    	}
    	*/
    	// lo scopo è quello di apire la agenda autista di un autista mettendoci davanti alla user dell'autista "@admin@" e la password dell'amministratore, 
    	// così da aprire la agenda senza chiedere la password all'autista
    	// TODO DA CONTINUARE: Scrivendo la email dell'austista nella agenda autista e aggiungendoci "@admin@" e mettendo la password dell'amministraotre, 
    	// mi si visualizza la agenda dell'autista. Per evitare di chiedergli a password.
    	//==================== NUOVO ==========================
    	String username = request.getParameter("j_username");
    	String password = request.getParameter("j_password");
    	System.out.println("aa:"+username+"bb:"+password);
    	if(username != null && password != null && username.contains("@admin@")) { // matteo.manili@tiscali.it
    		
    		String Url = "";
    		if( request.getServerName().equals("localhost") ) {
    			Url = "https://localhost:8443/apollon/j_security_check";
    		}else if( request.getServerName().contains("apollotransfert.com") ) {
    			Url = "https://apollotransfert.com/j_security_check";
    		}
    		
    		try {
    			System.out.println("URL:"+Url);
    			//SendHttpPOST_1(Url, response);
    			SendHttpPOST_2(Url, response);
    			
    		}catch(Exception exc) {
    			exc.printStackTrace();
    		}
    		
    		
    	}else {
    	//===============================================
    	
	    	if("true".equals(request.getHeader("X-Ajax-call"))) {
	    		log.debug("onAuthenticationFailure X-Ajax-call FAILURE");
	    		try {
	    			response.getWriter().print("failure");//return "ok" string
	    			response.getWriter().flush();
	    		} catch (IOException e) {
	    		//handle exception...
	    		}
	    	}else if ("true".equals(request.getHeader("rest-authentication"))) {
	    		log.debug("onAuthenticationFailure rest-authentication FAILURE");
	    		try {
	    			response.getWriter().print("failure");//return "ok" string
	    			response.getWriter().flush();
	    		} catch (IOException e) {               
	    		//handle exception...
	    		}
	    	} else {
	    		log.debug("onAuthenticationFailure FAILURE");
	            redirectStrategy.sendRedirect(request, response, "/login?error=true");
	    	}
    	
    	}
    	
    }
    
    private int SendHttpPOST_2( String Url, HttpServletResponse response ) throws Exception {
	    HttpClient httpclient = HttpClients.createDefault();
	    HttpPost httppost = new HttpPost( Url );

	    // Request parameters and other properties.
	    List<NameValuePair> params = new ArrayList<NameValuePair>(2);
	    params.add(new BasicNameValuePair("j_username", "ciro"));
	    params.add(new BasicNameValuePair("j_password", "sasa"));
	    httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
		httppost.addHeader("accept", "application/json");

	    //Execute and get the response.
	    HttpResponse response_new = httpclient.execute(httppost);
	    if (response_new.getStatusLine().getStatusCode() == 200) {
			BufferedReader br = new BufferedReader( new InputStreamReader((response_new.getEntity().getContent())));
			String output; String resp = "";
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				if( output != null ) {
					resp = resp + output;
				}
				// System.out.println(output);
			}
			System.out.println( "resp: "+resp );
			response.setContentType("application/json");
    	    response.setCharacterEncoding(Constants.ENCODING_UTF_8);
    	    response.getWriter().write(resp);
    	    return response_new.getStatusLine().getStatusCode();
	    }
	    return 0;
    }
    
    private int SendHttpPOST_1( String Url, HttpServletResponse response ) throws Exception {
    	DefaultHttpClient httpClient = new DefaultHttpClient();
    	HttpPost postRequest = new HttpPost( Url );
		//StringEntity input = new StringEntity("j_username="+username+"&j_password="+password);
		StringEntity input = new StringEntity("j_username=ciro&j_password=sasa");
		postRequest.setEntity(input);
		
		input.setContentType("application/x-www-form-urlencoded");
		postRequest.addHeader("accept", "application/json");
		postRequest.addHeader("User-Agent", "Mozilla"); 
		postRequest.addHeader("autista", "pompeitransfer@gmail.com");
		
		HttpResponse response_new = httpClient.execute(postRequest);
		if (response_new.getStatusLine().getStatusCode() == 200) {
			BufferedReader br = new BufferedReader( new InputStreamReader((response_new.getEntity().getContent())));
			String output; String resp = "";
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				if( output != null ) {
					resp = resp + output;
				}
				// System.out.println(output);
			}
			httpClient.getConnectionManager().shutdown();
			System.out.println( "resp: "+resp );
			response.setContentType("application/json");
    	    response.setCharacterEncoding(Constants.ENCODING_UTF_8);
    	    response.getWriter().write(resp);
    	    return response_new.getStatusLine().getStatusCode();
		}
		return 0;
		
    }
    
    
}
