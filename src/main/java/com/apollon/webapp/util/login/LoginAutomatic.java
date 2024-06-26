package com.apollon.webapp.util.login;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import com.apollon.model.User;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public class LoginAutomatic {
	
	public static void loginAutomatic(User user, HttpServletRequest request, Locale locale){
        // log user in automatically
        final UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken( user.getUsername(), user.getPassword(), user.getAuthorities()) ;
        auth.setDetails(user);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

}
