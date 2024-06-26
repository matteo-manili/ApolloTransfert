package com.apollon.webapp.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.mail.MessagingException;
import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.apollon.Constants;
import com.apollon.model.ListaInvioEmailSms;
import com.apollon.webapp.util.controller.gestioneApplicazione.GestioneApplicazioneUtil;
import com.apollon.webapp.util.email.EmailMarketing_AgenzieViaggi;
import com.apollon.webapp.util.email.EmailMarketing_Autisti_Comunicazioni;
import com.apollon.webapp.util.sms.InvioEmailDinamica;
import com.apollon.webapp.util.sms.InvioSms;
import com.apollon.webapp.util.sms.Invio_Email_Sms_UTIL;
import com.apollon.webapp.util.sms.InvioSmsDinamico;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 *	*******************************************************************************************************************************
 *	IN MONDOSERVER L'APPLICAZIONE VIENE DEPLOYATA SUL TOMCAT DUE VOLTE (PER MOTIVI SISTEMISTICI) SU DUE PATH DIVERSI "" e "/html"
 *	QUINDI IL quartz.Scheduler O LO SpringScheduling SI AVVIA DUE VOLTE. BISOGNA FARNE PARTIRE SOLO UNO !!!
 *	*******************************************************************************************************************************
 *
 *	http://www.quartz-scheduler.org/documentation/quartz-2.2.x/quick-start.html
 *
 *	// 0 2 9 ? * 2-6 | funziona!	
 *	1: 0-59 	secondi
 *	2: 0-59 	minuti
 *	3: 0-23 	ore
 *	4: 1-31 	numeri del mese 
 *				[se valorizzato non valorizzare "giorno della settimana" uno dei due DEVE essere valorizzato] se non valorizzato mettere ?
 *	5: 1-12 	mese - per tutti i mesi mettere *
 *	6: 1-7 		giorno della settimana (inizia da domenica)
 *				[se valorizzato non valorizzare "numeri del mese" uno dei due DEVE essere valorizzato] se non valorizzato mettere ? 
 *	7: 2020		anno, opzionale, non metterlo.
 *
 */
public class SchedulerQuartz extends ApplicationUtils implements Job {
	private static final Log log = LogFactory.getLog(SchedulerQuartz.class);
	
	private static ServletContext servletContext;
	//private static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private static final String JobMain = "JobMain"; public static final String GroupMain = "GroupMain";
	
	private static final String Trigger_PuliziaDatabase_GiornateTariffari= "PuliziaDatabase_GiornateTariffari";
	private static final String Trigger_PuliziaDatabase_RichiestaMedia = "PuliziaDatabase_RichiestaMedia";
	private static final String Trigger_InvioEmailMarketing = "InvioEmailMarketing";
	private static final String Trigger_3_min = "Trigger_3_min"; 
	private static final String Trigger_4_min = "Trigger_4_min";
	public static final String Trigger_5_sec = "Trigger_5_sec";
	
	/**
	 *	*******************************************************************************************************************************
	 *	IN MONDOSERVER L'APPLICAZIONE VIENE DEPLOYATA SUL TOMCAT DUE VOLTE (PER MOTIVI SISTEMISTICI) SU DUE PATH DIVERSI "" e "/html"
	 *	QUINDI IL quartz.Scheduler O LO SpringScheduling SI AVVIA DUE VOLTE. BISOGNA FARNE PARTIRE SOLO UNO !!!
	 *	*******************************************************************************************************************************
	 */
	public static void AvviaScdulatoriQuartz(ServletContext sx) {
		try {
			if(GestioneApplicazioneUtil.CheckContextPathCorretto(sx)) {
				servletContext = sx;
				
				JobKey jobKey = new JobKey(JobMain, GroupMain);
				JobDetail job = JobBuilder.newJob(SchedulerQuartz.class).withIdentity(jobKey).build();
				Set<Trigger> triggerList = new HashSet<Trigger>();
		
				// TRIGGERS
				Trigger trigger_1 = TriggerBuilder.newTrigger().withIdentity(Trigger_InvioEmailMarketing, GroupMain)
						.withSchedule(
						SimpleScheduleBuilder.simpleSchedule()
							.withIntervalInHours(1).repeatForever())
					.startAt(addSeconds(new Date(), 20)) // serve a far partire il tirgger TOT secondi dopo l'inizializzazione
					.build();
				triggerList.add(trigger_1);
				
				Trigger trigger_2 = TriggerBuilder.newTrigger().withIdentity(Trigger_4_min, GroupMain)
					.withSchedule(
						SimpleScheduleBuilder.simpleSchedule()
							.withIntervalInMinutes(4).repeatForever())
							//.withIntervalInSeconds(30).repeatForever())
					.startAt(addSeconds(new Date(), 20)) // serve a far partire il tirgger TOT secondi dopo l'inizializzazione
					.build();
				triggerList.add(trigger_2);
				
				Trigger trigger_3 = TriggerBuilder.newTrigger().withIdentity(Trigger_5_sec, GroupMain)
						.withSchedule(
							SimpleScheduleBuilder.simpleSchedule()
								.withIntervalInSeconds(5).repeatForever())
						.startAt(addSeconds(new Date(), 20)) // serve a far partire il tirgger TOT secondi dopo l'inizializzazione
						.build();
				triggerList.add(trigger_3);
				
				Trigger trigger_4 = TriggerBuilder.newTrigger().withIdentity(Trigger_PuliziaDatabase_RichiestaMedia, GroupMain)
					    .withSchedule(CronScheduleBuilder.cronSchedule("0 0 3 ? * 1"))
					    .startAt(addSeconds(new Date(), 20)) // serve a far partire il tirgger TOT secondi dopo l'inizializzazione
					    .build();
				triggerList.add(trigger_4);
				
				Trigger trigger_5 = TriggerBuilder.newTrigger().withIdentity(Trigger_PuliziaDatabase_GiornateTariffari, GroupMain)
					    .withSchedule(CronScheduleBuilder.cronSchedule("0 0 4 ? * 1-7"))
					    .startAt(addSeconds(new Date(), 20)) // serve a far partire il tirgger TOT secondi dopo l'inizializzazione
					    .build();
				triggerList.add(trigger_5);

				
				// FACCIO PARTIRE I TRIGGERS
				Map<JobDetail, Set<? extends Trigger>> triggersAndJobs = new HashMap<>();
				triggersAndJobs.put(job, triggerList );
				Scheduler scheduler = new StdSchedulerFactory().getScheduler();
				scheduler.start();
				scheduler.scheduleJobs(triggersAndJobs, false);
			}
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static Date addSeconds(Date date, Integer seconds) {
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    cal.add(Calendar.SECOND, seconds);
	    return cal.getTime();
	}
	
	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		try {
			/*
			log.info("Scheduler Quartz! "+dateFormat.format(new Date()) +" | TriggerName: "+jobExecutionContext.getTrigger().getKey().getName() 
					+" | ServletContextName(): "+servletContext.getServletContextName() );
			*/
			if( jobExecutionContext.getTrigger().getKey().getName().equals(Trigger_PuliziaDatabase_GiornateTariffari) ) {
				if( CheckAmbienteProduzione_Static_ContextNameProduzione_AND_IpAddessProduzione(servletContext) ){
					PuliziaDatabase.EseguiPuliziaDatabase_GiornateTariffari();
				}
			}else if( jobExecutionContext.getTrigger().getKey().getName().equals(Trigger_PuliziaDatabase_RichiestaMedia) ) {
				if( CheckAmbienteProduzione_Static_ContextNameProduzione_AND_IpAddessProduzione(servletContext) ){
					PuliziaDatabase.EseguiPuliziaDatabase_RichiestaMedia();
				}
			}else if( jobExecutionContext.getTrigger().getKey().getName().equals(Trigger_InvioEmailMarketing) ) {
				try {
					String CurrentIpAddress = InetAddress.getLocalHost().getHostAddress();
					log.debug("trigger: "+Trigger_InvioEmailMarketing+" | CurrentIpAddress: "+CurrentIpAddress +" ip.domain.mondoserver.apollotransfert: "+ApplicationMessagesUtil.DammiMessageSource("ip.domain.mondoserver.apollotransfert", null) );
					//CONTROLLARE ANCHE QUANDO GIRA SU SVILUPPO, NON DEVE PARTIRE
					//System.out.println( "RISULTATO getHostName: "+ InetAddress.getLocalHost().getHostName() );
					//System.out.println( "RISULTATO getLocalHost: "+ InetAddress.getLocalHost().getLocalHost() );
					//System.out.println( "RISULTATO getHostAddress: "+ InetAddress.getLocalHost().getHostAddress() );
					if( CheckAmbienteProduzione_Static_ContextNameProduzione_AND_IpAddessProduzione(servletContext) ){
						try {
							List<String> indirizziEmailInviate = EmailMarketing_AgenzieViaggi.inviaEmailMarketing_AgenzieViaggi_FieraMilano(null, null, 10 );
							log.debug("Totale email inviate EmailMarketing_AgenzieViaggi: "+indirizziEmailInviate.size());
							indirizziEmailInviate = EmailMarketing_Autisti_Comunicazioni.inviaEmailMarketing_Autisti(null, null, 10, 
									Constants.VM_COMUNICAZIONE_AUTISTI_AGENDA_AUTISTA); 
							log.debug("Totale email inviate EmailMarketing_Autisti_Comunicazioni: "+indirizziEmailInviate.size());
						} catch (InterruptedException | MessagingException | IOException e) {
							e.printStackTrace();
						}
					}
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			// TODO VA IN java.lang.OutOfMemoryError: unable to create new native thread 
			}else if(jobExecutionContext.getTrigger().getKey().getName().equals( Trigger_4_min )) {
				//log.debug("trigger: "+Trigger_3_min);
				List<Object[]> listObject = listaInvioSmsDao.Check_TIPO_MESSAGGIO_SMS_AVVISO_CORSA();
				for( Object ite_object: listObject ) {
					ListaInvioEmailSms messaggiono_firstElemnt = (ListaInvioEmailSms) ite_object;
					boolean Un_MessaggioInviato = false;
					//System.out.println("messaggiono_firstElemnt TIPO MESS: "+messaggiono_firstElemnt.getTipoMessaggio());
					if(messaggiono_firstElemnt.getTipoMessaggio() == Invio_Email_Sms_UTIL.TIPO_MESSAGGIO_SMS_AVVISO_MATTEO_CORSA_VENDUTA) {
						Un_MessaggioInviato = InvioSms.Lancia_SMS_Gateway(servletContext, messaggiono_firstElemnt);
					
					}else if( messaggiono_firstElemnt.getTipoMessaggio() == Invio_Email_Sms_UTIL.TIPO_MESSAGGIO_SMS_AVVISO_CORSA_ANDATA_CLIENTE_CONTATTO_AUTISTA_DISPONIBLE
							|| messaggiono_firstElemnt.getTipoMessaggio() == Invio_Email_Sms_UTIL.TIPO_MESSAGGIO_SMS_AVVISO_CORSA_RITORNO_CLIENTE_CONTATTO_AUTISTA_DISPONIBLE 
							|| messaggiono_firstElemnt.getTipoMessaggio() == Invio_Email_Sms_UTIL.TIPO_MESSAGGIO_SMS_AVVISO_CORSA_ANDATA_AUTISTA_CONTATTO_CLIENTE_DISPONIBLE
							|| messaggiono_firstElemnt.getTipoMessaggio() == Invio_Email_Sms_UTIL.TIPO_MESSAGGIO_SMS_AVVISO_CORSA_RITORNO_AUTISTA_CONTATTO_CLIENTE_DISPONIBLE ) {
						Un_MessaggioInviato = InvioSmsDinamico.InviaSmsDinamico_AvvisoContattoDisponibile(servletContext, messaggiono_firstElemnt);
						
					}else if( messaggiono_firstElemnt.getTipoMessaggio() == Invio_Email_Sms_UTIL.TIPO_MESSAGGIO_SMS_AVVISO_CORSA_ANDATA_CLIENTE_24_ORE
							|| messaggiono_firstElemnt.getTipoMessaggio() == Invio_Email_Sms_UTIL.TIPO_MESSAGGIO_SMS_AVVISO_CORSA_RITORNO_CLIENTE_24_ORE
							|| messaggiono_firstElemnt.getTipoMessaggio() == Invio_Email_Sms_UTIL.TIPO_MESSAGGIO_SMS_AVVISO_CORSA_ANDATA_AUTISTA_24_ORE
							|| messaggiono_firstElemnt.getTipoMessaggio() == Invio_Email_Sms_UTIL.TIPO_MESSAGGIO_SMS_AVVISO_CORSA_RITORNO_AUTISTA_24_ORE
							|| messaggiono_firstElemnt.getTipoMessaggio() == Invio_Email_Sms_UTIL.TIPO_MESSAGGIO_SMS_AVVISO_CORSA_ANDATA_AUTISTA_48_ORE
							|| messaggiono_firstElemnt.getTipoMessaggio() == Invio_Email_Sms_UTIL.TIPO_MESSAGGIO_SMS_AVVISO_CORSA_RITORNO_AUTISTA_48_ORE ) {
						Un_MessaggioInviato = InvioSmsDinamico.InviaSmsDinamico_AvvisoRicordoCorsa(servletContext, messaggiono_firstElemnt);
					
					}else if( messaggiono_firstElemnt.getTipoMessaggio() == Invio_Email_Sms_UTIL.TIPO_MESSAGGIO_EMAIL_SCRIVI_RECENSIONE_TRANSFER ) {
						Un_MessaggioInviato = InvioEmailDinamica.InviaEmailDinamica_ScriviRecensioneCorsa(servletContext, messaggiono_firstElemnt);
					}
					
					if(Un_MessaggioInviato) {
						break;
					}
				}
			}else if(jobExecutionContext.getTrigger().getKey().getName().equals( Trigger_5_sec )) {
				// Se sono più di un messaggio ne prendo solo uno, il primo, e al prossimo trigger prenderò il prossimo. Così ne invia uno ogni 5 secondi
				List<Object[]> listObject = listaInvioSmsDao.Check_TIPO_MESSAGGIO_SMS_RAPIDO();
				if( listObject != null && listObject.size() > 0 ) {
					Object obj_firstElemnt = (Object) listObject.get(0);
					ListaInvioEmailSms messaggiono_firstElemnt = (ListaInvioEmailSms) obj_firstElemnt;
					if( messaggiono_firstElemnt.getTipoMessaggio() == Invio_Email_Sms_UTIL.TIPO_MESSAGGIO_SMS_RAPIDO ) {
						InvioSms.Lancia_SMS_Gateway(servletContext, messaggiono_firstElemnt);
					}
				}
			}
		}catch(Exception exc) {
			exc.printStackTrace();
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
