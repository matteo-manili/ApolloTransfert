package com.apollon.webapp.filter;

import com.apollon.Constants;
import com.itextpdf.text.log.SysoCounter;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.jstl.core.Config;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Filter to wrap request with a request including user preferred locale.
 */
public class LocaleFilter extends OncePerRequestFilter {
	
    /**
     * This method looks for a "locale" request parameter. If it finds one, it sets it as the preferred locale
     * and also configures it to work with JSTL.
     *
     * @param request the current request
     * @param response the current response
     * @param chain the chain
     * @throws IOException when something goes wrong
     * @throws ServletException when a communication failure happens
     */
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String locale = request.getParameter("locale");
        Locale preferredLocale = null;
        
        //System.out.println("sono in doFilterInternal getServerName: "+request.getServerName());

        if (locale != null) {
            int indexOfUnderscore = locale.indexOf('_');
            if (indexOfUnderscore != -1) {
                String language = locale.substring(0, indexOfUnderscore);
                String country = locale.substring(indexOfUnderscore + 1);
                preferredLocale = new Locale(language, country);
            } else {
                preferredLocale = new Locale(locale);
            }
        }
        HttpSession session = request.getSession(false);
        if (session != null) {
            if (preferredLocale == null) {
                preferredLocale = (Locale) session.getAttribute(Constants.PREFERRED_LOCALE_KEY);
            } else {
                session.setAttribute(Constants.PREFERRED_LOCALE_KEY, preferredLocale);
                Config.set(session, Config.FMT_LOCALE, preferredLocale);
            }

            if (preferredLocale != null && !(request instanceof LocaleRequestWrapper)) {
                request = new LocaleRequestWrapper(request, preferredLocale);
                LocaleContextHolder.setLocale(preferredLocale);
            }
        }
        
        
        /*
         * RENDE TUTTI I CONTROLLER APERTI DALL'ESTERNO
         * 
         * questi devono stare sempre prima del chain.doFilter(request, response); altrimenti non vengono applicati al response!!!!
         * 
         * poi forse questi set devono essere tolti perché a livello generale abilitano il CORS a tutti i @Controller che non sono protetti dalla sicurity 
         * di spring e a quelli che non sono gestiti dal @CrossOrigin (come ad esempio il https://www.apollotransfert.com/chi-siamo POST)
         */
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Max-Age", "3600");
		//response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
		//response.setHeader("Access-Control-Allow-Headers", "Content-Type");
		response.setHeader("Access-Control-Allow-Headers", Constants.JWT_HEADER_AUTHORIZATION+", content-type");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		
		// WEBSOCKET
		response.setHeader("Sec-WebSocket-Accept", "true");
		response.setHeader("Connection", "Upgrade");
		response.setHeader("Upgrade", "websocket");
		response.setHeader("Sec-WebSocket-Version", "13");
		response.setHeader("Sec-WebSocket-Protocol", "protocol1");
		//response.setHeader("Sec-WebSocket-Key", "SGVsbG8sIHdvcmxkIQ==");
		
		/*
		 * 1) Ricordati che JWT e' stateless .. quindi ogni richiesta e' come se fosse la prima :)
			2) JwtAuthenticationTokenFilter scatta ad ogni richiesta e valida il token per capire se considerarti autenticato .. se il token e' ok con l istruzione
			
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
			setta nel contesto di spring security che sei autenticato e l'autentication bean authentication. Tale bean sono le info di sessione.. in qualsiasi controller 
			autenticato con SecurityContextHolder.getContext().getAuthentication();
			potrai prenderti le info di sessione dell utente e verificare ad esempio se un utente puo fare una determinata operazione con i dati recuperati da 
			SecurityContextHolder.getContext().getAuthentication() che non sono altro che i dati che hai deciso di mettere nel token
		 */
		/*
		System.out.println("jwt_token sasas: "+request.getHeader("jwt_token"));
		String authToken = request.getHeader("jwt_token");
		User userDetails = JwtTokenUtil.getUserDetails("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjaXJvIiwicm9sZXMiOlsiUk9MRV9VU0VSIl0sImlzRW5hYmxlZCI6dHJ1ZSwiZXhwIjoxNTczNzIzNjI2LCJpYXQiOjE1NzM2MjM2Mjc2NDZ9.MON5-866pb3CCIrc5NSySi8RB23BKcpPm_b7k3S-oe8");
		if (jwtTokenUtil.validateToken(authToken, userDetails)) {
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        */
        
		/*
        String serverName = request.getServerName();
        String RequestURI = request.getRequestURI(); 
        String ContextPath = request.getContextPath();
        String url = RequestURI.substring(ContextPath.length());
        String queryString = request.getQueryString() != null ? "?" + request.getQueryString() : "";
        String[] parts = url.split("/");
        
        System.out.println("serverName: "+serverName);
        System.out.println("RequestURI: "+RequestURI);
        System.out.println("ContextPath: "+ContextPath);
        System.out.println("url: "+url);
        System.out.println("queryString: "+queryString);
        System.out.println("parts: "+parts);
		*/
        

		
		

		
		
		
		final String ProperyHeaderLocation = "Location";
		
        // DA NO-WWW A WWW
        if( request.getServerName().contains("apollotransfert") && !request.getServerName().startsWith("www") 
        		&& !request.getRequestURI().startsWith("/j_security_check")
        		&& !request.getRequestURI().startsWith("/agenda-autista") 
        		&& !request.getRequestURI().startsWith("/api_") 
        		&& !request.getRequestURI().startsWith("/chiamatasocket") 
        		
        ) {
        	String newUrlDomain = "https://www."+request.getServerName() + request.getRequestURI() + (request.getQueryString() != null ? "?" + request.getQueryString() : "");
            System.out.println("newUrlDomain: "+newUrlDomain);
            response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
            response.setHeader(ProperyHeaderLocation, newUrlDomain);
            
        // DA /home a / 
        }else if(request.getRequestURI().substring(request.getContextPath().length()).startsWith("/home") 
        		&& !request.getRequestURI().substring(request.getContextPath().length()).contains("/home-user") ){
            String newUrl = request.getContextPath() + "/" + (request.getQueryString() != null ? "?" + request.getQueryString() : "");
            System.out.println("newUrl1: "+newUrl);
            response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
            response.setHeader(ProperyHeaderLocation, newUrl);
            
        // DA /transfer- a / 
    	} else if(request.getRequestURI().substring(request.getContextPath().length()).startsWith("/"+Constants.URL_TRANSFER)){
    		String newUrl = request.getContextPath() + "/";
            System.out.println("newUrl2: "+newUrl);
            response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
            response.setHeader(ProperyHeaderLocation, newUrl);
            
    	}else {
    		
    		
    		// --------- INIZIO LANGUAGE NEW -------------
    		
    		//System.out.println("LOCALE: "+request.getLocale().getCountry() + " LANGUAGE: "+request.getLocale().getLanguage());
    		
    		
            Locale defaultLocale = new Locale("en", "EN");
            List<String> listLinguePermesse = Arrays.asList( new String[]{defaultLocale.getLanguage(), "it", "es"} );
            Locale localeRequest = request.getLocale() != null ? request.getLocale() : defaultLocale; 
            if( !listLinguePermesse.contains(localeRequest.getLanguage()) ) {
            	localeRequest = defaultLocale;
            }

            String RequestURI = request.getRequestURI(); 
            String ContextPath = request.getContextPath();
            String url = RequestURI.substring(ContextPath.length());
            
            
            List<String> listUrlsMultiLanguage = Arrays.asList( new String[]{"home-user", "userform", "login", "signup", "chi-siamo", "contatti", "tariffe-transfer", 
            		"collaboratori", "ncc-agenzie-viaggio", "ncc-aziende"} );

        	String[] parts = url.split("/");
        	
        	//System.out.println("url: "+url +" | parts.length: "+parts.length);
        	
        	
        	if( parts.length == 0 || listUrlsMultiLanguage.contains(parts[1])) { // aggiungo il language, esempio: /it
        		String queryString = request.getQueryString() != null ? "?"+request.getQueryString() : "";
    			String newUrl = ContextPath+"/"+localeRequest.getLanguage() + (url.equals("/") ? "" : url) + queryString;
    			response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
    			response.setHeader(ProperyHeaderLocation, newUrl);
        	
    			
        	}else if( parts.length >= 1  ) { // se invece il language è già presente, esempio: /it
        		Config.set( request.getServletContext(), Config.FMT_LOCALE, new Locale(parts[1], "") );
    			Config.set(request, Config.FMT_LOCALE, new Locale(parts[1], "") );
    			if( session != null ) {
    				Config.set(session, Config.FMT_LOCALE, new Locale(parts[1], "") );
    			}
    			chain.doFilter(request, response);
    			LocaleContextHolder.setLocaleContext(null);
        		
        		
        	}
            
        	// --------- FINE LANGUAGE NEW -------------
    		
        	
    		//chain.doFilter(request, response);
            //LocaleContextHolder.setLocaleContext(null);
    	}
		

        
    }
}
