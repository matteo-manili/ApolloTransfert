package com.apollon.webapp.util.login;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.stereotype.Component;

import com.apollon.Constants;
import com.apollon.dao.UserDao;
import com.apollon.webapp.rest.JwtTokenUtil;

/**
 * @author Matteo - matteo.manili@gmail.com
 * 
 * per il ajax spring login vedere: https://gal-levinsky.blogspot.it/2011/08/spring-security-3-ajax-login.html?showComment=1484708527942#c804789591960377995
 per autenticazione personalizzata vedere: http://www.baeldung.com/spring_redirect_after_login
 http://www.jcombat.com/spring/custom-authentication-success-handler-with-spring-security
 *
 */
@Component("authenticationSuccessHandler")
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {   
	private final Log log = LogFactory.getLog(getClass());
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    public static ApplicationContext contextDao = new ClassPathXmlApplicationContext("App-Database-Spring-Module-Web.xml");
    public static UserDao userDao = (UserDao) contextDao.getBean("userDao");
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
    	//DefaultSavedRequest defaultSavedRequest = (DefaultSavedRequest) session.getAttribute(WebAttributes.SAVED_REQUES);
    	log.info("sono in CustomAuthenticationSuccessHandler getServerName: "+request.getServerName());
    	// matteo.manili@tiscali.it
    	/*
		Enumeration<String> enumerat = request.getHeaderNames();
    	while(enumerat.hasMoreElements()) {
    	    String param = enumerat.nextElement();
    	    System.out.println("enumerat: "+param  +" getHeader: "+ request.getHeader(param) );
    	}
    	*/
    	if("true".equals(request.getHeader("X-Ajax-call"))) {
    		log.debug("Chiamata da login ajax SUCCESS");
    		try {
    			response.getWriter().print("success"); //return "ok" string
    			response.getWriter().flush();
    		} catch (IOException e) {               
    			//handle exception...
    		}
    	}else if(request.getHeader("accept") != null && request.getHeader("accept").contains("application/json") ) {
    		log.debug("Chiamata da login rest-authentication SUCCESS");
    		JSONObject mainObj = new JSONObject();
    		try{
    			UserDetails userDetails = userDao.loadUserByUsername(authentication.getName());
		        final String token = JwtTokenUtil.GenerateToken(userDetails);
		        System.out.println("token jwt: "+token);
		        System.out.println("getAuthorities: "+userDetails.getAuthorities());
		        mainObj.put("utente", userDetails.getUsername());
		        mainObj.put("jwt", token);
    		}catch(Exception ee){
    			System.out.println("Exception onAuthenticationSuccess:"+ee.getMessage());
    		} finally {
    			response.setContentType("application/json");
        	    response.setCharacterEncoding(Constants.ENCODING_UTF_8);
        	    response.getWriter().write(mainObj.toString());
    		}
    	} else {
    		log.debug("Chiamata non da login ajax SUCCESS");
    		setAlwaysUseDefaultTargetUrl(false);       
    		DefaultSavedRequest defaultSavedRequest = (DefaultSavedRequest) request.getSession().getAttribute("SPRING_SECURITY_SAVED_REQUEST");
    		try {
    			/*
	    		System.out.println("sasasasa1: "+ getDefaultTargetUrl() );
	    		System.out.println("sasasasa2: "+ getTargetUrlParameter() );
	    		System.out.println("sasasasa5: "+ defaultSavedRequest.getMethod() );
	    		System.out.println("sasasasa6: "+ defaultSavedRequest.getQueryString() );
	    		System.out.println("sasasasa7: "+ defaultSavedRequest.getServletPath() );
	    		System.out.println("sasasasa8: "+ getRedirectStrategy() );
	    		System.out.println("sasasasa9: "+ determineTargetUrl(request, response) );
	    		System.out.println("sasasasa10: "+ authentication.getDetails() );
	    		System.out.println("sasasasa11: "+ authentication.getPrincipal() );
	    		System.out.println("sasasasa12: "+ defaultSavedRequest.getPathInfo() );
	    		*/
    		}catch(NullPointerException nP){
    			log.debug("NullPointerException: "+ nP.getMessage() );
			}
    		String pathInfoSavedRequest = ""; 
    		if(defaultSavedRequest != null && defaultSavedRequest.getPathInfo() != null) {
	    		pathInfoSavedRequest = defaultSavedRequest.getPathInfo();
	    		if(defaultSavedRequest.getQueryString() != null){
	    			pathInfoSavedRequest = defaultSavedRequest.getPathInfo() +"?"+ defaultSavedRequest.getQueryString();
	    		}
    		}
    		String targetUrl = determineTargetUrl(authentication, pathInfoSavedRequest); 
            redirectStrategy.sendRedirect(request, response, targetUrl);
            clearAuthenticationAttributes(request);
    	}
    }
    
    
    protected String determineTargetUrl(Authentication authentication, String pathInfoSavedRequest) {
        Set<String> authorities = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        if (authorities.contains("ROLE_ADMIN")) {
        	if(pathInfoSavedRequest != null && !pathInfoSavedRequest.equals("") && !pathInfoSavedRequest.equals("/login")){
        		return pathInfoSavedRequest;
        	}else{
        		return "/home-user";
        	}
        } else if (authorities.contains("ROLE_USER")) {
        	if(pathInfoSavedRequest != null && !pathInfoSavedRequest.equals("") && !pathInfoSavedRequest.equals("/login")){
        		return pathInfoSavedRequest;
        	}else{
        		return "/home-user";
        	}
        } else {
            throw new IllegalStateException();
        }
    }
 
	public RedirectStrategy getRedirectStrategy() {
		return redirectStrategy;
	}
 
	public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
		this.redirectStrategy = redirectStrategy;
	}
    
	/*
	private RequestCache requestCache = new HttpSessionRequestCache();
	
	public void setRequestCache(RequestCache requestCache) {
		this.requestCache = requestCache;
	}
	*/
    
}
