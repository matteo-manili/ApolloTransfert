package com.apollon.webapp.util;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.mail.MessagingException;
import javax.servlet.ServletContext;

import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import com.apollon.webapp.util.ApplicationUtils.ApplicationMessagesUtil;
import com.apollon.webapp.util.controller.gestioneApplicazione.GestioneApplicazioneUtil;
import com.apollon.webapp.util.email.EmailMarketing_AgenzieViaggi;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 *	*******************************************************************************************************************************
 *	IN MONDOSERVER L'APPLICAZIONE VIENE DEPLOYATA SUL TOMCAT DUE VOLTE (PER MOTIVI SISTEMISTICI) SU DUE PATH DIVERSI "" e "/html"
 *	QUINDI IL quartz.Scheduler O LO SpringScheduling SI AVVIA DUE VOLTE. BISOGNA FARNE PARTIRE SOLO UNO !!!
 *	*******************************************************************************************************************************
 *
 *	Fare la configurazione della Schedulazione in /apollon/src/main/webapp/WEB-INF/applicationContext.xml in cui si aggiungono i link:
	xmlns:task="http://www.springframework.org/schema/task"
	xsi:schemaLocation="... http://www.springframework.org/schema/task http://www.springframework.org/schema/task/spring-task.xsd"
 *
 * 	E si setta il bean in questo modo
	<!-- 5 ore sono: 18000000 -->
	<!-- 1 ora è: 3600000 -->
	<!-- 5 secondi sono: 5000 -->
	<!-- 
	<task:scheduled-tasks scheduler="myScheduler">
		<task:scheduled ref="beanScheduling" method="printMessage" fixed-delay="3600000" initial-delay="30000"/> 
		<!-- <task:scheduled ref="beanScheduling" method="printMessage" fixed-delay="5000" initial-delay="30000"/> -->
	</task:scheduled-tasks>
	<task:scheduler id="myScheduler" pool-size="10"/>  -->
 *
 *	Per documentazione vedere:
 *	http://www.springframework.org/schema/task/spring-task-4.0.xsd 
 *	https://docs.spring.io/spring/docs/3.2.x/spring-framework-reference/html/scheduling.html
 *
 */
@Component("beanScheduling")
public class SchedulerSpring implements ServletContextAware  {
	
	public SchedulerSpring() {
		super();
	}
	
	private ServletContext servletContext;
	
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
	
	/**
	 *	*******************************************************************************************************************************
	 *	IN MONDOSERVER L'APPLICAZIONE VIENE DEPLOYATA SUL TOMCAT DUE VOLTE (PER MOTIVI SISTEMISTICI) SU DUE PATH DIVERSI "" e "/html"
	 *	QUINDI IL quartz.Scheduler O LO SpringScheduling SI AVVIA DUE VOLTE. BISOGNA FARNE PARTIRE SOLO UNO !!!
	 *	*******************************************************************************************************************************
	 */
	public void printMessage() {
		if(GestioneApplicazioneUtil.CheckContextPathCorretto(servletContext)) {
			System.out.println("Start Spring Scheduling: "+new Date().toString());
			try {
				String CurrentIpAddress = InetAddress.getLocalHost().getHostAddress();
				System.out.println("CurrentIpAddress: "+CurrentIpAddress +" ip.domain.mondoserver.apollotransfert: "+ApplicationMessagesUtil.DammiMessageSource("ip.domain.mondoserver.apollotransfert", null) );
				//CONTROLLARE ANCHE QUANDO GIRA SU SVILUPPO, NON DEVE PARTIRE
				//System.out.println( "RISULTATO getHostName: "+ InetAddress.getLocalHost().getHostName() );
				//System.out.println( "RISULTATO getLocalHost: "+ InetAddress.getLocalHost().getLocalHost() );
				//System.out.println( "RISULTATO getHostAddress: "+ InetAddress.getLocalHost().getHostAddress() );
				System.out.println( ApplicationMessagesUtil.DammiMessageSource("indirizzo.sede", null) );
				if( servletContext.getServletContextName().equals( ApplicationMessagesUtil.DammiMessageSource("context.name.apollotransfert", null) ) 
					&& ApplicationMessagesUtil.DammiMessageSource("ip.domain.mondoserver.apollotransfert", null).equals(CurrentIpAddress)){
					try {
						final int TotaleNumeroEmailDaInviare = 10; // prima era 3
						List<String> indirizziEmailInviate = EmailMarketing_AgenzieViaggi.inviaEmailMarketing_AgenzieViaggi_FieraMilano(null, null , TotaleNumeroEmailDaInviare );
						System.out.println( "Totale email inviate: "+indirizziEmailInviate.size());
					} catch (InterruptedException | MessagingException | IOException e) {
						e.printStackTrace();
					}
				}
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	/*
	 * SVILUPPO:
	 * RISULTATO getHostName: wc1tomcat3.mondoserver.com
	RISULTATO getLocalHost: wc1tomcat3.mondoserver.com/192.168.100.103
	RISULTATO getHostAddress: 192.168.100.103
	 */
	/* PRODUZIONE:
	 * RISULTATO getHostName: wc1tomcat3.mondoserver.com
	RISULTATO getLocalHost: wc1tomcat3.mondoserver.com/192.168.100.103
	RISULTATO getLocalHost: wc1tomcat3.mondoserver.com/192.168.100.103
	 */
}
