package com.apollon.webapp.util.login;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;


/**
 * Aggiungere al security.xml: <http auto-config="true" entry-point-ref="restAuthenticationEntryPoint"> NON FUNZIONA !!!!
 * 
 * @author Matteo
 *
 */

//@Component("restAuthenticationEntryPoint")
public final class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
 


	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
			throws IOException, ServletException {
		System.out.println("assasassasasasasasaffdfdfdffdfdfdfdfd");
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
		
	}
}
