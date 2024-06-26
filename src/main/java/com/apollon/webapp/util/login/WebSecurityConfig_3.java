package com.apollon.webapp.util.login;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * non funziona da errore
 * 
 * @author Matteo
 */

//@Configuration
//@EnableWebSecurity
public class WebSecurityConfig_3 extends WebSecurityConfigurerAdapter {
	private static final Log log = LogFactory.getLog(WebSecurityConfig_3.class);

	//@Autowired
	protected void registerAuthentication(AuthenticationManagerBuilder auth) throws Exception {
		log.debug("WebSecurityConfig_3 registerAuthentication");
		auth.inMemoryAuthentication().withUser("user").password("password").roles("USER");
	}
	
	/*
	@Override
    protected void configure(HttpSecurity http) throws Exception {
		log.debug("WebSecurityConfig_3 configure");
        http
        .addFilterBefore(new CorsFilter(), ChannelProcessingFilter.class);
    }    
	*/
	
	/*
	@Override
    protected void configure(HttpSecurity http) throws Exception {
		log.debug("WebSecurityConfig_3 configure");
        http
        .addFilterBefore(new CORSFilter(), ChannelProcessingFilter.class)
        .csrf().disable().headers()
        .and().formLogin().loginPage("/j_security_check").permitAll()
    	.and().logout().permitAll()
        .and().authorizeRequests().antMatchers("/**").permitAll()
        .and().authorizeRequests().anyRequest().authenticated();
    }
	*/
	
	

	
 	/*
	@Override
	protected void configure(HttpSecurity http) throws Exception {
	http
	.addFilterBefore(new CORSFilter(), ChannelProcessingFilter.class)
	
	.formLogin()
	.loginPage("/login")
	.and()
	.authorizeRequests()
	.anyRequest().authenticated();
	}
 	 
	@Override
	protected void configure(HttpSecurity http) throws Exception {
	    http.csrf().disable()
	        .headers().addHeaderWriter(new StaticHeadersWriter("Access-Control-Allow-Origin","*"))
	.and()
	.authorizeRequests().antMatchers("/**").permitAll();
	}
  
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
	        .addFilterBefore(new CORSFilter(), ChannelProcessingFilter.class).antMatcher("/*")
	.csrf().disable().headers().disable()
		.authorizeRequests().antMatchers("/agenda-autista/**").permitAll().anyRequest().authenticated();
	//.and().formLogin().loginPage("/login").permitAll()
	//.and().logout().permitAll();
	}
	*/
	
	/*
	
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
		        .addFilterBefore(new CORSFilter(), ChannelProcessingFilter.class).antMatcher("/*")
		        .csrf(). disable();
		//.and().formLogin().loginPage("/login").permitAll()
		//.and().logout().permitAll();
		}
		*/
}
