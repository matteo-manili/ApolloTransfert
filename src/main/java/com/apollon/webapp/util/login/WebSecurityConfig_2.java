package com.apollon.webapp.util.login;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * non funziona da errore
 * 
 * @author Matteo
 */

//@Configuration
//@EnableWebSecurity
public class WebSecurityConfig_2 extends WebMvcConfigurerAdapter {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**");
	}
	
	/*

@Override
public void addCorsMappings(CorsRegistry registry) {
	registry.addMapping("/api/**")
		.allowedOrigins("http://domain2.com")
		.allowedMethods("PUT", "DELETE")
			.allowedHeaders("header1", "header2", "header3")
		.exposedHeaders("header1", "header2")
		.allowCredentials(false).maxAge(3600);
}

	 */
	
}
