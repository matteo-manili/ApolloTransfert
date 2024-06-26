package com.apollon.webapp.util.login;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;

/**
 * non funziona da errore
 * 
 * @author Matteo
 */

//@Configuration
//@EnableWebSecurity
//@Component("webSecurityConfigurerAdapter")
public class WebSecurityConfig_1 extends WebSecurityConfigurerAdapter {

	@Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .headers().addHeaderWriter(new StaticHeadersWriter("Access-Control-Allow-Origin","*"))
            .and()
            .authorizeRequests().antMatchers("/**").permitAll();
    }
	
}
