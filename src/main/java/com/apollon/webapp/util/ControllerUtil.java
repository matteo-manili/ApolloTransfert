package com.apollon.webapp.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Convenience class for setting and retrieving cookies.
 */
public final class ControllerUtil {
    private static final Log log = LogFactory.getLog(ControllerUtil.class);

    /**
     * Faccio la request.getRequestURL() 
     * da questo url https://www.apollotransfert.com/transfer-perugia mi ritorna transfer-perugia
     */
    public static String getPageURL(HttpServletRequest request){
    	String PaginaUrl = request.getRequestURL().toString();
		String[] UrlSplit = PaginaUrl.split("/");
		return UrlSplit[UrlSplit.length - 1];
    }
    
    /**
     * Faccio la request.getHeader("referer")
     * Questo qui request.getHeader("referer") mi prende la pagina precedente all'interno del mio dominio (all'esterno sembra che non funziona)
	 * se faccio il request.getRequestURL() mi prende la attuale, invece facendo request.getHeader("referer") mi prende l'url dove ero precedentemente 
	 * (ma solo se sto all'interno del mio dominio).
	 * Quindi trovandomi a fare una chiamata AJAX POST o GET all'interno di una jsp per capire da che url provenivo devo fare il referer.
	 * Attualmente lo utilizzo per salvare i dati dl visitatore.
     */
    public static String getPageRefererURL(HttpServletRequest request){
    	try{
	    	String PaginaProvenienza = request.getHeader("referer"); 
			String[] UrlSplit = PaginaProvenienza.split("/");
			return UrlSplit[UrlSplit.length - 1]; //prendo la parte destra dall'ultimo /
    	}catch(Exception exc){
    		log.debug(exc.getMessage());
    		return "";
    	}
    }
    
    
	/**
	 * Mi ritorna il numero di pagina selezionato nella Tabella
	 * inserire nella jsp:
	 * <display:table name="autistiList" cellspacing="0" cellpadding="0" requestURI="" 
			id="autisti" partialList="true" pagesize="15" size="resultSize" class="table table-condensed table-striped">  
			
			vedere qui: http://www.displaytag.org/1.2/tut_externalSortAndPage.html
			provare questa:
			 (Integer.parseInt(request.getParameter((new ParamEncoder(tableId).encodeParameterName(TableTagParameters.PARAMETER_PAGE)))) - 1) * pageSize.
	 */
    public static final int DammiPageNumberDisplayTable_by_QueryString(HttpServletRequest request, int PageSizeTable){
   		int page = 0;
   		if(request.getQueryString() != null && !request.getQueryString().equals("")){
   			String aa[] = request.getQueryString().split("&");
   			for(String ite: aa){
   				if(ite.contains("-p")){
   					page = Integer.parseInt((ite.split("=")[1]));
   					page = (page - 1) * PageSizeTable;
   				}
   			}
   		}
   		return page;
   	}
    
    /**
     * Checkstyle rule: utility classes should not have public constructor
     */
    private ControllerUtil() {
    }

    /**
     * Convenience method to set a cookie
     *
     * @param response the current response
     * @param name the name of the cookie
     * @param value the value of the cookie
     * @param path the path to set it on
     */
    public static void setCookie(HttpServletResponse response, String name, String value, String path) {
        if (log.isDebugEnabled()) {
            log.debug("Setting cookie '" + name + "' on path '" + path + "'");
        }

        Cookie cookie = new Cookie(name, value);
        cookie.setSecure(false);
        cookie.setPath(path);
        cookie.setMaxAge(3600 * 24 * 30); // 30 days
        response.addCookie(cookie);
    }

    /**
     * Convenience method to get a cookie by name
     *
     * @param request the current request
     * @param name the name of the cookie to find
     *
     * @return the cookie (if found), null if not found
     */
    public static Cookie getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        Cookie returnCookie = null;

        if (cookies == null) {
            return returnCookie;
        }

        for (final Cookie thisCookie : cookies) {
            if (thisCookie.getName().equals(name) && !"".equals(thisCookie.getValue())) {
                returnCookie = thisCookie;
                break;
            }
        }
        return returnCookie;
    }

    /**
     * Convenience method for deleting a cookie by name
     *
     * @param response the current web response
     * @param cookie the cookie to delete
     * @param path the path on which the cookie was set (i.e. /appfuse)
     */
    public static void deleteCookie(HttpServletResponse response,
                                    Cookie cookie, String path) {
        if (cookie != null) {
            // Delete the cookie by setting its maximum age to zero
            cookie.setMaxAge(0);
            cookie.setPath(path);
            response.addCookie(cookie);
        }
    }

    /**
     * Convenience method to get the application's URL based on request
     * variables.
     * 
     * @param request the current request
     * @return URL to application
     */
    public static String getAppURL(HttpServletRequest request) {
        if (request == null) return "";
        
        StringBuffer url = new StringBuffer();
        int port = request.getServerPort();
        if (port < 0) {
            port = 80; // Work around java.net.URL bug
        }
        String scheme = request.getScheme();
        url.append(scheme);
        url.append("://");
        url.append(request.getServerName());
        if ((scheme.equals("http") && (port != 80)) || (scheme.equals("https") && (port != 443))) {
            url.append(':');
            url.append(port);
        }
        url.append(request.getContextPath());
        return url.toString();
    }
}
