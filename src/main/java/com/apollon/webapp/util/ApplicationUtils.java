package com.apollon.webapp.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.cert.CertStore;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.AuthenticationFailedException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.VelocityEngine;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.cms.AttributeTable;
import org.bouncycastle.asn1.cms.IssuerAndSerialNumber;
import org.bouncycastle.asn1.smime.SMIMECapabilitiesAttribute;
import org.bouncycastle.asn1.smime.SMIMECapability;
import org.bouncycastle.asn1.smime.SMIMECapabilityVector;
import org.bouncycastle.asn1.smime.SMIMEEncryptionKeyPreferenceAttribute;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.mail.smime.SMIMEException;
import org.bouncycastle.mail.smime.SMIMESignedGenerator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.apollon.Constants;
import com.apollon.dao.AgenzieViaggioBitDao;
import com.apollon.dao.AutistaDao;
import com.apollon.dao.AutoveicoloDao;
import com.apollon.dao.GestioneApplicazioneDao;
import com.apollon.dao.ListaInvioSmsDao;
import com.apollon.dao.RicercaTransfertDao;
import com.apollon.dao.UserDao;
import com.apollon.model.RicercaTransfert;
import com.apollon.model.User;
import com.apollon.util.VelocityCurrency;
import com.apollon.util.UtilString;
import com.apollon.webapp.util.email.CreateSignedMail;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public class ApplicationUtils {
	private static final Log log = LogFactory.getLog(ApplicationUtils.class);
	
	private static ApplicationContext contextUtiliy = new ClassPathXmlApplicationContext("applicationContext-service.xml");
	public static VelocityEngine velocityEngineUtility = (VelocityEngine)contextUtiliy.getBean("velocityEngine");
	public static ResourceBundleMessageSource messageSourceUtility = (ResourceBundleMessageSource)contextUtiliy.getBean("messageSource_Service");
	
	public static ApplicationContext contextDao = new ClassPathXmlApplicationContext("App-Database-Spring-Module-Web.xml");
	public static UserDao userDao = (UserDao) contextDao.getBean("userDao");
	public static GestioneApplicazioneDao gestioneApplicazioneDao = (GestioneApplicazioneDao) contextDao.getBean("GestioneApplicazioneDao");
	public static RicercaTransfertDao ricercaTransfertDao = (RicercaTransfertDao) contextDao.getBean("RicercaTransfertDao");
	public static AutoveicoloDao autoveicoloDao = (AutoveicoloDao) contextDao.getBean("AutoveicoloDao");
	public static AutistaDao autistaDao = (AutistaDao) contextDao.getBean("AutistaDao");
	public static ListaInvioSmsDao listaInvioSmsDao = (ListaInvioSmsDao) contextDao.getBean("ListaInvioSmsDao");
	public static AgenzieViaggioBitDao agenzieViaggioBitDao = (AgenzieViaggioBitDao) contextDao.getBean("AgenzieViaggioBitDao");
	
	
	//"mail.ncctransferonline.properties"; //mail.apollotransfert2.properties"; //"mail.apollotransfert.properties"; // "mail.gmail.apollotransfert.properties";
	
	public static class ApplicationMessagesUtil {
		private static Locale LocaleGeneral;
		public ApplicationMessagesUtil() {
			super();
		}
		public ApplicationMessagesUtil(Locale locale) {
			super();
			LocaleGeneral = locale;
		}
		public static String DammiMessageSource(String msgKey) {
	        return DammiMessageSource(msgKey, new Object[] { null }, null);
	    }
		public static String DammiMessageSource(String msgKey, Locale locale) {
	        return DammiMessageSource(msgKey, new Object[] { null }, locale);
	    }
		public static String DammiMessageSource(String msgKey, String arg, Locale locale) {
	        return DammiMessageSource(msgKey, new Object[] { arg }, locale);
	    }
	    public static String DammiMessageSource(String msgKey, Object[] args, Locale locale) {
	    	if(locale != null){
	    		return messageSourceUtility.getMessage(msgKey, args, locale);
	    	}else if( LocaleGeneral != null ){
	    		return messageSourceUtility.getMessage(msgKey, args, LocaleGeneral);
	    	}else{
	    		return messageSourceUtility.getMessage(msgKey, args, Constants.Locale_IT);
	    	}
	    }
	}
	
	
	public static boolean ControllaTokenCodiceScontoUnivoco_Globale(String TokenCodiceSconto) {
		if(agenzieViaggioBitDao.getAgenzieViaggioBit_TokenSconto(TokenCodiceSconto) == null && userDao.getUser_by_TokenRecensione(TokenCodiceSconto) == null) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * Faccio 3 controlli per Confermare che mi trovo in ApolloTransfert in Produzione su Mondoserver: <br>
	 * 1 uso la vecchia maniera che è già testata: request.getServerName() <br>
	 * 2 Controllo quale ServletContextName è settato bel web.xml nel tag <display-name> (da testare) <br>
	 * 3 Controllo l'indirizzo IP corrisponde a quello di ip.domain.mondoserver.apollotransfert (da testare)
	 * 
	 * @return Mi ritorna true se è vero
	 */
	public static boolean CheckAmbienteApolloTransfert_Mondoserver(HttpServletRequest request) throws UnknownHostException {
		log.debug("CheckAmbienteApolloTransfert_Mondoserver");
		try{
			//HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest(); // Mi da Errore, Non funziona bene
			log.debug("requests.getServerName(): "+(request != null ? request.getServerName() : null) );
			log.debug("request.getServletContext().getServletContextName(): "+(request != null ? request.getServletContext().getServletContextName() : null));
			log.debug("InetAddress.getLocalHost().getHostAddress(): "+InetAddress.getLocalHost().getHostAddress()); //mi da l'ip locale
			log.debug("request.getContextPath(): "+(request != null ? request.getContextPath() : null)); 
			/*
			requests.getServerName(): www.apollotransfert.com
			request.getServletContext().getServletContextName(): ContextApolloTransfert
			request.getContextPath(): 
			 */
			if( request != null && request.getServerName().contains(messageSourceUtility.getMessage("domain.apollotransfert.name", null, null)) 
					&& request.getServletContext().getServletContextName().equals(messageSourceUtility.getMessage("context.name.apollotransfert", null, null))
					&& InetAddress.getLocalHost().getHostAddress().equals(ApplicationMessagesUtil.DammiMessageSource("ip.domain.mondoserver.apollotransfert", null)) ){
				log.debug("SONO IN AMBIENTE PRODUZIONE");
				return true;
			}else{
				log.debug("NON SONO IN AMBIENTE PRODUZIONE");
				return false;
			}
		}catch(NullPointerException nullExc){
			System.out.println("NON SONO IN AMBIENTE PRODUZIONE NullPointerException");
			nullExc.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 1 Controllo se mi trovo in ServletContextName: ContextApolloTransfert | ApolloTransfertSviluppo <br>
	 * Controllo quale ServletContextName è settato bel web.xml nel tag <display-name> <br>
	 * 
	 * @return Mi ritorna true se è vero
	 */
	public static boolean CheckAmbienteApolloTransfert_Static_ContextNameProduzione_OR_ContextNameSviluppo(ServletContext servletContext) {
		//System.out.println("servletContext.getServletContextName(): "+servletContext.getServletContextName());
		//System.out.println("servletContext.getContextPath(): "+servletContext.getContextPath());
		if( servletContext.getServletContextName().equals( messageSourceUtility.getMessage("context.name.apollotransfert", null, null)) 
				|| servletContext.getServletContextName().equals( messageSourceUtility.getMessage("context.name.apollotransfert.sviluppo", null, null)) ){
			return true;
		}else{
			return false;
		}
	}


	/**
	 * 1 Controllo se mi trovo in ServletContextName: ContextApolloTransfert <br>
	 * Controllo quale ServletContextName è settato bel web.xml nel tag <display-name> <br>
	 * 2 Controllo l'indirizzo IP corrisponde a quello di ip.domain.mondoserver.apollotransfert 
	 * 
	 * @return Mi ritorna true se è vero
	 */
	public static boolean CheckAmbienteProduzione_Static_ContextNameProduzione_AND_IpAddessProduzione(ServletContext servletContext) throws UnknownHostException {
		String CurrentIpAddress = InetAddress.getLocalHost().getHostAddress();
		if( servletContext.getServletContextName().equals( ApplicationMessagesUtil.DammiMessageSource("context.name.apollotransfert", null) ) 
				&& ApplicationMessagesUtil.DammiMessageSource("ip.domain.mondoserver.apollotransfert", null).equals(CurrentIpAddress)) {
			return true;
		}else {
			return false;
		}
	}
	
	public static boolean SetParameterIpDomainMondoserver_Form_TableGestioneApplicazione() {
		try {
			String IpAddressProduzione = gestioneApplicazioneDao.getName("SWITCH_TEST_PRODUZIONE").getValueString();
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			
			//System.out.println( ApplicationUtils.class.getClassLoader().getResource("ApplicationResources.properties").getFile() );
			//File file = new File( ApplicationUtils.class.getClassLoader().getResource("ApplicationResources.properties").getFile() );
			
			log.debug("getResource: "+ loader.getResource( "ApplicationResources.properties" ).getPath() );
			File file = new File( loader.getResource( "ApplicationResources.properties" ).getPath() );

			//ip.domain.mondoservers
			Properties props = new Properties();
			FileInputStream in = new FileInputStream( file );
			props.load( in );
			log.debug("ip.domain.mondoserver.apollotransfert: "+props.getProperty("context.name.apollotransfert").toString());
			in.close();
			
			FileOutputStream out = new FileOutputStream( file );
			//FileOutputStream out = new FileOutputStream( pathFile );
			props.setProperty("ip.domain.mondoserver.apollotransfert", IpAddressProduzione);
			props.store(out, null);
			out.close();
			return true;
		}catch(Exception exc) {
			exc.printStackTrace();
			return false;
		}
	}
	
	
	
	/**
	 *	*******************************************************************************************************************************
	 *	IN MONDOSERVER L'APPLICAZIONE VIENE DEPLOYATA SUL TOMCAT DUE VOLTE (PER MOTIVI SISTEMISTICI) SU DUE PATH DIVERSI "" e "/html"
	 *	QUINDI IL quartz.Scheduler O LO SpringScheduling SI AVVIA DUE VOLTE. BISOGNA FARNE PARTIRE SOLO UNO !!!
	 *	*******************************************************************************************************************************
	 */
	public static boolean CheckContextPathCorretto(ServletContext servletContext) {
		if(servletContext.getContextPath().equals("/apollon") /* SERVER SVILUPPO */ || servletContext.getContextPath().equals("/html") /* SERVER MONDOSERVER */ ) {
			System.out.println("CheckContextPathCorretto ContextPath: "+ servletContext.getContextPath() + " SI" );
			return true;
		}else{
			System.out.println("CheckContextPathCorretto ContextPath: "+ servletContext.getContextPath() + " NO" );
			return false;
		}
	}
	
	/**
	 * Controllo se mi trovo in ambiente Venditore
	 * Controllo quale ServletContextName è settato bel web.xml nel tag <display-name> (da testare)
	 * 
	 * @return Mi ritorna true se è vero
	 */
	public static boolean CheckAmbienteVenditore(ServletContext servletContext) {
		//System.out.println("servletContext.getServletContextName(): "+servletContext.getServletContextName());
		//System.out.println("servletContext.getContextPath(): "+servletContext.getContextPath());
		if( servletContext.getServletContextName().equals( messageSourceUtility.getMessage("context.name.ncctransferonline", null, null)) ){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean CheckDomainTranfertClienteVenditore(RicercaTransfert ricTransfert) {
		if(ricTransfert.getUserVenditore() != null){
			return true;
		}else{
			return false;
		}
	}
	
	public static Map<String, Integer> DammiParametriPercentualiVenditori() {
		String[] Parametri = UtilString.RimuoviTuttiGliSpazi(gestioneApplicazioneDao.getName("PERC_SERV_COMMISS_VENDITORE").getValueString()).split("-");
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put(Constants.VENDIDORE_PERC_MIN, Integer.parseInt(Parametri[0]));
		map.put(Constants.VENDIDORE_PERC_DEFAULT, Integer.parseInt(Parametri[1]));
		map.put(Constants.VENDIDORE_PERC_MAX, Integer.parseInt(Parametri[2]));
		return map;
	}

	public static int DammiNumMinimoAutistiCorsaMedia() {
		return (int)(long) gestioneApplicazioneDao.getName("NUM_MIN_AUTISTI_CORSA_MEDIA").getValueNumber();
	}
	
	public static final String[] ParametriCorseParticolari = UtilString.RimuoviTuttiGliSpazi(gestioneApplicazioneDao.getName("CORSE_PARTICOLARI").getValueString()).split("-");
	
	public static BigDecimal DammiTassaIVA = new BigDecimal(gestioneApplicazioneDao.getName("TASSA_IVA").getValueNumber());
	
	public static String Dammi_KEY_GOOGLE_API() {
		return gestioneApplicazioneDao.getName("GOOGLE_API_MAP").getValueString();
	}
	
	
	/**
	 * Il FooterEmail ha contenuti dinamici in base al destinatario della Email. <br>
	 * Se il destinatario è un cliente bisogna passargli il RicercaTransfert, <br>
	 * altrimenti se il destinatario è un Autista o Email di Marketing passargli null al RicercaTransfert
	 */
	public static String FooterEmail(VelocityEngine velocityEngine, Map<String, Object> modelVelocity, String contextName, Locale locale) {
		String HttpsUrlDomain = ""; String UrlDomain = ""; String webAppName = "";
		if(contextName.equals("context.name.apollotransfert")){
			HttpsUrlDomain = ApplicationMessagesUtil.DammiMessageSource("https.w3.domain.apollotransfert.name", locale);
			UrlDomain = ApplicationMessagesUtil.DammiMessageSource("w3.domain.apollotransfert.name", locale);
			webAppName = ApplicationMessagesUtil.DammiMessageSource("webapp.apollotransfert.name", locale);
		}else if(contextName.equals("context.name.ncctransferonline")){
			HttpsUrlDomain = ApplicationMessagesUtil.DammiMessageSource("https.w3.domain.ncctransferonline.name", locale);
			UrlDomain = ApplicationMessagesUtil.DammiMessageSource("w3.domain.ncctransferonline.name", locale);
			webAppName = ApplicationMessagesUtil.DammiMessageSource("webapp.ncctransferonline.name", locale); 
		}
		return DammiFooterEmail(velocityEngine, modelVelocity, HttpsUrlDomain, UrlDomain, webAppName, locale);
	}

	
	/**
	 * Il FooterEmail ha contenuti dinamici in base al destinatario della Email. <br>
	 * Se il destinatario è un cliente bisogna passargli il RicercaTransfert, <br>
	 * altrimenti se il destinatario è un Autista o Email di Marketing passargli null al RicercaTransfert
	 */
	public static String FooterEmail(VelocityEngine velocityEngine, Map<String, Object> modelVelocity, HttpServletRequest request) {
		String HttpsUrlDomain = ""; String UrlDomain = ""; String webAppName = "";
		HttpsUrlDomain = (CheckAmbienteVenditore(request.getServletContext())) ?
				ApplicationMessagesUtil.DammiMessageSource("https.w3.domain.ncctransferonline.name", request.getLocale()) : ApplicationMessagesUtil.DammiMessageSource("https.w3.domain.apollotransfert.name", request.getLocale());
		UrlDomain = (CheckAmbienteVenditore(request.getServletContext())) ?
				ApplicationMessagesUtil.DammiMessageSource("w3.domain.ncctransferonline.name", request.getLocale()) : ApplicationMessagesUtil.DammiMessageSource("w3.domain.apollotransfert.name", request.getLocale());
		webAppName = (CheckAmbienteVenditore(request.getServletContext())) ?
				ApplicationMessagesUtil.DammiMessageSource("webapp.ncctransferonline.name", request.getLocale()) : ApplicationMessagesUtil.DammiMessageSource("webapp.apollotransfert.name", request.getLocale());
		return DammiFooterEmail(velocityEngine, modelVelocity, HttpsUrlDomain, UrlDomain, webAppName, request.getLocale());
	}
	
	
	/**
	 * Il FooterEmail ha contenuti dinamici in base al destinatario della Email. <br>
	 * Se il destinatario è un cliente bisogna passargli il RicercaTransfert, <br>
	 * altrimenti se il destinatario è un Autista o Email di Marketing passargli null al RicercaTransfert
	 */
	public static String FooterEmail(VelocityEngine velocityEngine, Map<String, Object> modelVelocity, RicercaTransfert ricTransfert, Locale locale) {
		String HttpsUrlDomain = ""; String UrlDomain = ""; String webAppName = "";
		if(ricTransfert != null){
			HttpsUrlDomain = (CheckDomainTranfertClienteVenditore(ricTransfert)) ?
					ApplicationMessagesUtil.DammiMessageSource("https.w3.domain.ncctransferonline.name", locale) : ApplicationMessagesUtil.DammiMessageSource("https.w3.domain.apollotransfert.name", locale);
			UrlDomain = (CheckDomainTranfertClienteVenditore(ricTransfert)) ?
					ApplicationMessagesUtil.DammiMessageSource("w3.domain.ncctransferonline.name", locale) : ApplicationMessagesUtil.DammiMessageSource("w3.domain.apollotransfert.name", locale);
			webAppName = (CheckDomainTranfertClienteVenditore(ricTransfert)) ?
					ApplicationMessagesUtil.DammiMessageSource("webapp.ncctransferonline.name", locale) : ApplicationMessagesUtil.DammiMessageSource("webapp.apollotransfert.name", locale);
		}
		return DammiFooterEmail(velocityEngine, modelVelocity, HttpsUrlDomain, UrlDomain, webAppName, locale);
	}
	
	
	private static String DammiFooterEmail(VelocityEngine velocityEngine, Map<String, Object> modelVelocity,
			String HttpsUrlDomain, String UrlDomain, String webAppName, Locale locale) {
		modelVelocity.put("linkDomain", "<a href=\""+HttpsUrlDomain+"\">"+UrlDomain+"</a>");
		modelVelocity.put("webAppName", webAppName);
		modelVelocity.put("currency", new VelocityCurrency());
		//modelVelocity.put("numberTool", new NumberTool()); // da provare $numberTool.format("number", $varObj.prezzoCorsa)
		modelVelocity.put(Constants.VM_ATTRIBUTE_MESSAGE_SOURCE, new ApplicationMessagesUtil(locale));
        if(velocityEngine != null){
			return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, Constants.VM_EMAIL_INFO_FOOTER_INCLUDE, Constants.ENCODING_UTF_8, modelVelocity);
		}else{
			return DammiTemplateVELOCITY(Constants.VM_EMAIL_INFO_FOOTER_INCLUDE, modelVelocity);
		}
	}
	
	
	/**
	 * CONVERTE TESTO DI FILE VM (VELOCITY) QUANDO ESEGUO LA APPLICAZIONE NON ON LINE
	 * Attualmente utilizzo questo metodo quando il velocityEngine è nullo
	 * ma lo potrei usare per tutto senza passare il velocityEngine dal controller.
	 * Ma ci sarebbe da rifare un refactory generale, va bene così
	 */
	public static String DammiTemplateVELOCITY(String templateName, Map<String, Object> modelVelocity) {
		return VelocityEngineUtils.mergeTemplateIntoString(velocityEngineUtility, templateName, Constants.ENCODING_UTF_8, modelVelocity);
	}
	
	// -------------------------------------------------------------------
	// ------------------------------ EMAIL ------------------------------
	// -------------------------------------------------------------------
	
	public static void InviaEmail_HTML_User(User user, String TemplateMail, String oggettoEmail, VelocityEngine velocityEngine, 
			Map<String, Object> modelVelocity, ByteArrayOutputStream baos_allegato, String fileNameAllegato, boolean EmailMarketing, String DomainEmail, boolean trustpilot) 
					throws AuthenticationFailedException, SendFailedException, AddressException, MessagingException, IOException {
		String Email = user.getEmail(); String FullName = user.getFullName();
		InviaEmail_HTML(Email, FullName, TemplateMail, oggettoEmail, velocityEngine, modelVelocity, baos_allegato, fileNameAllegato, EmailMarketing, DomainEmail, trustpilot);
	}
	
	public static boolean InviaEmail_HTML_Other(String Email, String FullName, String TemplateMail, String oggettoEmail, VelocityEngine velocityEngine, 
			Map<String, Object> modelVelocity, ByteArrayOutputStream baos_allegato, String fileNameAllegato, boolean EmailMarketing, String DomainEmail, boolean trustpilot) 
					throws AuthenticationFailedException, SendFailedException, AddressException, MessagingException, IOException {
		return InviaEmail_HTML(Email, FullName, TemplateMail, oggettoEmail, velocityEngine, modelVelocity, baos_allegato, fileNameAllegato, EmailMarketing, DomainEmail, trustpilot);
	}
	
	public static boolean InviaEmail_HTML_Other(String Email, String TemplateMail, String oggettoEmail, VelocityEngine velocityEngine, 
			Map<String, Object> modelVelocity, ByteArrayOutputStream baos_allegato, String fileNameAllegato, boolean EmailMarketing, String DomainEmail, boolean trustpilot) 
					throws AuthenticationFailedException, SendFailedException, AddressException, MessagingException, IOException {
		return InviaEmail_HTML(Email, "", TemplateMail, oggettoEmail, velocityEngine, modelVelocity, baos_allegato, fileNameAllegato, EmailMarketing, DomainEmail, trustpilot);
	}
	
	
	/**
	 * invia email_HTML
	 * Inviare Email con l'smtp di gmail da www.apollotransfert.com non è possibile perché gmail la riconosce come una frode.
	 * Posso inviare Email con l'smtp di gmail solo da localhost è per questo che EmailMarketing è settato a true solo se invio da 
	 * InviaEmailMarketing.inviaEmailMarketingAutisti_InfoMancanti_e_AutistaInfo() dal main(String[] args)
	 * Per inviare una email in HTML corretta vedere: https://stackoverflow.com/questions/5028670/how-to-set-mimebodypart-contenttype-to-text-html
	 */
	protected static boolean InviaEmail_HTML(String EmailDestinatario, String FullName, String TemplateMail, String oggettoEmail, VelocityEngine velocityEngine, 
		Map<String, Object> modelVelocity, ByteArrayOutputStream baos_allegato, String fileNameAllegato, boolean EmailMarketing, String DomainEmail, boolean trustpilot) 
				throws AuthenticationFailedException, SendFailedException, AddressException, MessagingException, IOException { 
		Properties props = DammiBestMailProviderServer(EmailMarketing, EmailDestinatario, DomainEmail);
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(props.getProperty("mail.username"), props.getProperty("mail.password"));
					}});
	    //Session session = Session.getInstance(props);
	    Message message = new MimeMessage(session);
		message.setFrom( new InternetAddress(props.getProperty("mail.default.from")) );
		if(FullName != null && !FullName.equals("")){
			message.setRecipient(Message.RecipientType.TO, new InternetAddress( EmailDestinatario, FullName ));
		}else{
			message.setRecipient(Message.RecipientType.TO, new InternetAddress( EmailDestinatario ));
		}

		if( trustpilot ) {
			// dice a trustpilot di inviare una email al cliente in cui chiede di scrivere una recensione
			// vedere: https://businessapp.b2b.trustpilot.com/invitations/eti-settings
			message.setRecipient(Message.RecipientType.BCC , new InternetAddress( ApplicationMessagesUtil.DammiMessageSource("email.trustpilot.apollotransfert") ));	
		}
		
		message.setSubject(oggettoEmail);
		String testoHtml = "";
		if(velocityEngine != null){
			testoHtml = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, TemplateMail, Constants.ENCODING_UTF_8, modelVelocity);
		}else{
			testoHtml = DammiTemplateVELOCITY(TemplateMail, modelVelocity);
		}
		//-------------------- TESTO --------------------
		MimeMultipart multipart = new MimeMultipart("related");
		MimeBodyPart messageBodyPart_HTML = new MimeBodyPart();
		messageBodyPart_HTML.setContent(testoHtml, "text/html; charset="+Constants.ENCODING_UTF_8);
	    multipart.addBodyPart(messageBodyPart_HTML);
	    //-------------------- ALLEGATO --------------------     
	    if(baos_allegato != null && baos_allegato.size() > 0 && fileNameAllegato != null && !fileNameAllegato.equals("") ){
	    	String extension = FilenameUtils.getExtension( fileNameAllegato );
		    MimeBodyPart messageBodyPart_Allegato = new MimeBodyPart();
	        DataSource source = new ByteArrayDataSource(baos_allegato.toByteArray(), "application/"+extension);
	        messageBodyPart_Allegato.setDataHandler(new DataHandler(source));
	        messageBodyPart_Allegato.setFileName(fileNameAllegato);
	        multipart.addBodyPart(messageBodyPart_Allegato);
	    }
	    //-------------------- IMMAGINE -------------------- 
	    // TODO per fare vedere l'immagine sulla email inserire il tag <img src="cid:logo"> il cui codice corrisponde al messageBodyPart_Image.setContentID
	    // il problema è che Gmail fa vedere le email segnate con l'allegato ma aprendole poi non si vede l'allegato
	    // RISOTO: inserire nella immagine src="https://www.apollotransfert.com/nuova_grafica/images/email_images/banner.jpg"
	    /*
	    MimeBodyPart messageBodyPart_Image = new MimeBodyPart();
        messageBodyPart_Image.attachFile("src/main/webapp/nuova_grafica/images/email_images/logo.png");
        messageBodyPart_Image.setContentID("<" + "logo"+ ">");
        messageBodyPart_Image.setDisposition(MimeBodyPart.INLINE);
        multipart.addBodyPart(messageBodyPart_Image);

	    messageBodyPart_Image = new MimeBodyPart();
        messageBodyPart_Image.attachFile( new FileDataSource("src/main/webapp/nuova_grafica/images/email_images/banner.jpg").getFile() );
        messageBodyPart_Image.setContentID("<" + "banner"+ ">");
        messageBodyPart_Image.setDisposition(MimeBodyPart.INLINE);
        multipart.addBodyPart(messageBodyPart_Image);
		*/
        
        
	    /**
	     * non funziona da java.security.NoSuchProviderException: no such provider: BC
	     * multipart = CriptaEmail().generate(messageBodyPart1, "BC");
	     */
	    message.setContent(multipart);
        message.saveChanges();
        System.out.println("SEND EMAIL: "+EmailDestinatario);
        // TODO RICORDA DI DECOMMENTARE IL SEND X INVIARE LA EMAIL !!!
        Transport.send(message);
        
    	// POSSO ANCHE INVIARE LA MAIL FACENDO L'AUTENTICAZIONE USANDO IL Transport AL POSTO DI FARLA SUL Message
        //Transport tr = session.getTransport( props.getProperty("mail.transport.protocol") );
        //tr.connect (props.getProperty("mail.host"), Integer.parseInt(props.getProperty("mail.port")), props.getProperty("mail.username"), props.getProperty("mail.password"));
        //tr.sendMessage(message, message.getAllRecipients());
        //tr.close();
    	log.debug("EMAIL INVIATA: "+EmailDestinatario);
    	return true;
	}
	

	/**
	 * Invio SMS: EMAIL TO SMS (Gateway) <br>
	 * Inserire nell'oggetto il numero telefonico con questo formato: 00393289126869 e il Testo dell'sms nel corpo della email <br>
	 * 
	 * Account Email Google Gmail: Consentire alle app meno sicure di accedere al tuo account !!!!! <br>
	 * Altrimenti da Errore javax.mail.AuthenticationFailedException <br>
	 * Per consentire di inviare le mail andare su "Accesso app meno sicure" alla pagina: https://myaccount.google.com/security <br>
	 * info: https://support.google.com/accounts/answer/6010255?hl=it
	 * 
	 * @param oggettoEmail
	 * @param TestoSms
	 * @return
	 * @throws AuthenticationFailedException
	 * @throws SendFailedException
	 * @throws AddressException
	 * @throws MessagingException
	 * @throws IOException
	 */
	protected static boolean InviaEmail_SMS_GATEWAY(String NumeroTelefonico, String TestoSms) throws AuthenticationFailedException, SendFailedException, AddressException, MessagingException, IOException { 
		NumeroTelefonico = NumeroTelefonico.replace("+", "00"); // se lascio il segno + il gateway sms non funziona
		NumeroTelefonico = UtilString.RimuoviTuttiGliSpazi(NumeroTelefonico); // se ci sono gli spazi tra i numeri non funziona
		
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		Properties props = new Properties();
		
		InputStream resourceStream = loader.getResourceAsStream( "mail.gmail.apollotransfert.properties" ); 
		// Da errore, non permette di inviare email a un indirizzo email con stesso dominio esempio: info@apollotransfert.com
		// InputStream resourceStream = loader.getResourceAsStream( EmailApolloTransfert_da_Usare );
		props.load(resourceStream);
		
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(props.getProperty("mail.username"), props.getProperty("mail.password"));
					}});
	    Message message = new MimeMessage(session);
		message.setFrom( new InternetAddress(props.getProperty("mail.default.from")) );
		
		final String SMS_GATEWAY = ApplicationMessagesUtil.DammiMessageSource("email.sms.apollotransfert", null); // "matteo.manili@gmail.com"; 
		System.out.println("SMS_GATEWAY: "+SMS_GATEWAY);
		message.setRecipient(Message.RecipientType.TO, new InternetAddress( SMS_GATEWAY ));
		
		message.setSubject( NumeroTelefonico );
		message.setText( TestoSms );
		
        Transport.send(message);
    	log.debug("EMAIL SMS INVIATA: "+SMS_GATEWAY);
    	log.debug("SMS GATEWAY Testo sms: " +TestoSms+ " Telefono: " +NumeroTelefonico );
    	return true;
	}
	
	/**
	 * LE EMAIL DEVONO ESSERE INVIATE NON DAL PROVIDER GMAIL
	 * Invio Email dal Form Contatti, significa che Nel Form ci metto la Email dell'utente Contatto e nel TO ci Metto la mia Email (AL CONTRARIO DEL SOLITO)
	 * @throws MessagingException 
	 * @throws IOException
	 */
	protected static boolean InviaEmail_Contatti_HTML(String TemplateMail, String OggettoEmail, String NomeMittente, String EmailMittente, 
			VelocityEngine velocityEngine, Map<String, Object> modelVelocity, String DomainEmail) 
					throws AuthenticationFailedException, SendFailedException, AddressException, MessagingException, IOException {
		Properties props = new Properties();
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		if(DomainEmail.equals(Constants.EMAIL_FROM_APOLLOTRANSFERT)){
	    	InputStream resourceStream = loader.getResourceAsStream( gestioneApplicazioneDao.getName("DEFAULT_EMAIL_SERVER_APOLLOTRANSFERT").getValueString() );
		    props.load(resourceStream);
	    }else if(DomainEmail.equals(Constants.EMAIL_FROM_NCCTRANSFERONLINE)) {
	    	InputStream resourceStream = loader.getResourceAsStream( gestioneApplicazioneDao.getName("DEFAULT_EMAIL_SERVER_NCCTRANSFERONLINE").getValueString() );
		    props.load(resourceStream);
	    }
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(props.getProperty("mail.username"), props.getProperty("mail.password"));
					}});
	    Message message = new MimeMessage(session);
	    String EmailDestinatario = ApplicationMessagesUtil.DammiMessageSource("email.matteo.manili.gmail");
	    message.setFrom(new InternetAddress( props.getProperty("mail.default.from") ));
		message.setRecipient( Message.RecipientType.TO, new InternetAddress( EmailDestinatario) );
		message.setSubject(OggettoEmail);
		String testoHtml = "";
		if(velocityEngine != null){
			testoHtml = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, TemplateMail, Constants.ENCODING_UTF_8, modelVelocity);
		}else{
			testoHtml = DammiTemplateVELOCITY(TemplateMail, modelVelocity);
		}
		//-------------------- TESTO --------------------
		Multipart multipart = new MimeMultipart();
		MimeBodyPart messageBodyPart1 = new MimeBodyPart();
	    messageBodyPart1.setContent(testoHtml, "text/html; charset="+Constants.ENCODING_UTF_8);
		//------------- UNISCO TESTO --------------
		multipart.addBodyPart(messageBodyPart1);
	    /**
	     * non funziona da java.security.NoSuchProviderException: no such provider: BC
	     * multipart = CriptaEmail().generate(messageBodyPart1, "BC");
	     */
	    message.setContent(multipart);
        message.saveChanges();
    	Transport.send(message);
    	log.debug("EMAIL INVIATA: "+EmailDestinatario);
    	return true;
	}
	
	
	private static Properties DammiBestMailProviderServer(boolean EmailMarketing, String Email, String DomainEmail) throws IOException {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		Properties props = new Properties();
		if(EmailMarketing && DomainEmail.equals(Constants.EMAIL_FROM_APOLLOTRANSFERT) && Email.toLowerCase().contains("@gmail.com") ){
			InputStream resourceStream = loader.getResourceAsStream("mail.gmail.apollotransfert.properties");
		    props.load(resourceStream);
		    log.debug("EMAIL INVIATA CON SMTP GMAIL");
			return props;
		}else if(EmailMarketing && DomainEmail.equals(Constants.EMAIL_FROM_NCCTRANSFERONLINE) && Email.toLowerCase().contains("@gmail.com") ){
			InputStream resourceStream = loader.getResourceAsStream("mail.gmail.ncctransferonline.properties");
		    props.load(resourceStream);
		    log.debug("EMAIL INVIATA CON SMTP GMAIL");
			return props;
		
		}else if(DomainEmail.equals(Constants.EMAIL_FROM_APOLLOTRANSFERT) && Email.toLowerCase().contains("@apollotransfert.com") ){  
	    	// le email inviate a nome-utente@apollotransfert.com non possono essere inviate col server email di smtp2.apollotransfert.com
	    	InputStream resourceStream = loader.getResourceAsStream("mail.gmail.apollotransfert.properties");
		    props.load(resourceStream);
		    log.debug("EMAIL INVIATA CON SMTP GMAIL");
		    return props;
		}else if(DomainEmail.equals(Constants.EMAIL_FROM_NCCTRANSFERONLINE) && Email.toLowerCase().contains("@apollotransfert.com") ){  
	    	// le email inviate a nome-utente@apollotransfert.com non possono essere inviate col server email di smtp2.apollotransfert.com
	    	InputStream resourceStream = loader.getResourceAsStream("mail.gmail.ncctransferonline.properties");
		    props.load(resourceStream);
		    log.debug("EMAIL INVIATA CON SMTP GMAIL");
		    return props;
	    
		}else if(DomainEmail.equals(Constants.EMAIL_FROM_APOLLOTRANSFERT)){
	    	InputStream resourceStream = loader.getResourceAsStream( gestioneApplicazioneDao.getName("DEFAULT_EMAIL_SERVER_APOLLOTRANSFERT").getValueString() );
		    props.load(resourceStream);
		    log.debug("EMAIL INVIATA CON SMTP MONDOSERVER apollotransfert");
		    return props;
	    }else if(DomainEmail.equals(Constants.EMAIL_FROM_NCCTRANSFERONLINE)) {
	    	InputStream resourceStream = loader.getResourceAsStream( gestioneApplicazioneDao.getName("DEFAULT_EMAIL_SERVER_NCCTRANSFERONLINE").getValueString() );
		    props.load(resourceStream);
		    log.debug("EMAIL INVIATA CON SMTP MONDOSERVER ncctransferonline");
		    return props;
	    
	    }else{
	    	InputStream resourceStream = loader.getResourceAsStream( gestioneApplicazioneDao.getName("DEFAULT_EMAIL_SERVER_APOLLOTRANSFERT").getValueString() );
		    props.load(resourceStream);
		    log.debug("EMAIL INVIATA CON SMTP MONDOSERVER apollotransfert");
		    return props;
	    }
		
		/*
		InputStream resourceStream = loader.getResourceAsStream( EmailApolloTransfert_da_Usare );
	    props.load(resourceStream);
	    log.debug("EMAIL INVIATA CON SMTP MONDOSERVER apollotransfert");
	    return props;
	    */

	}
	
	/**
	 * Controlla se l'email in ingresso è una email di test cioè: matteo.manili@gmail.com | matteo.manili@tiscali.it | matteo.manili@tiscalinet.it
	 */
	public static boolean CheckEmailTesting(String Email){
		if( Email.equals(ApplicationMessagesUtil.DammiMessageSource("email.matteo.manili.gmail"))
				|| Email.equals(ApplicationMessagesUtil.DammiMessageSource("email.matteo.manili.tiscali")) 
				|| Email.equals(ApplicationMessagesUtil.DammiMessageSource("email.matteo.manili.tiscalinet")) 
				|| Email.equals(ApplicationMessagesUtil.DammiMessageSource("email.alessandro.faraci")) ){
			return true;
		}else{
			return false;
		}
	}
	
	 /**
     * non funziona da java.security.NoSuchProviderException: no such provider: BC
     * multipart = CriptaEmail().generate(messageBodyPart1, "BC");
     */
	@Deprecated
	private static SMIMESignedGenerator CriptaEmail() throws GeneralSecurityException, IOException, SMIMEException, MessagingException{
		KeyPairGenerator kpg  = KeyPairGenerator.getInstance("RSA", "BC");
        kpg.initialize(1024, new SecureRandom());
        // cert that issued the signing certificate
        String              signDN = "O=Bouncy Castle, C=AU";
        KeyPair             signKP = kpg.generateKeyPair();
        X509Certificate     signCert = CreateSignedMail.makeCertificate(signKP, signDN, signKP, signDN);
        // cert we sign against
        String              origDN = "CN=Eric H. Echidna, E=eric@bouncycastle.org, O=Bouncy Castle, C=AU";
        KeyPair             origKP = kpg.generateKeyPair();
        X509Certificate     origCert = CreateSignedMail.makeCertificate(origKP, origDN, signKP, signDN);

        List<X509Certificate> certList = new ArrayList<X509Certificate>();
        
        certList.add(origCert);
        certList.add(signCert);

        // create a CertStore containing the certificates we want carried
        // in the signature
        CertStore certsAndcrls = CertStore.getInstance("Collection", new CollectionCertStoreParameters(certList), "BC");

        // create some smime capabilities in case someone wants to respond
        ASN1EncodableVector         signedAttrs = new ASN1EncodableVector();
        SMIMECapabilityVector       caps = new SMIMECapabilityVector();

        caps.addCapability(SMIMECapability.dES_EDE3_CBC);
        caps.addCapability(SMIMECapability.rC2_CBC, 128);
        caps.addCapability(SMIMECapability.dES_CBC);

        signedAttrs.add(new SMIMECapabilitiesAttribute(caps));

        // add an encryption key preference for encrypted responses -
        // normally this would be different from the signing certificate...
        IssuerAndSerialNumber   issAndSer = new IssuerAndSerialNumber(new X509Name(signDN), origCert.getSerialNumber());

        signedAttrs.add(new SMIMEEncryptionKeyPreferenceAttribute(issAndSer));

        // create the generator for creating an smime/signed message
        SMIMESignedGenerator gen = new SMIMESignedGenerator();

        // add a signer to the generator - this specifies we are using SHA1 and
        // adding the smime attributes above to the signed attributes that
        // will be generated as part of the signature. The encryption algorithm
        // used is taken from the key - in this RSA with PKCS1Padding
        gen.addSigner(origKP.getPrivate(), origCert, SMIMESignedGenerator.DIGEST_SHA1, new AttributeTable(signedAttrs), null);

        // add our pool of certs and cerls (if any) to go with the signature
        gen.addCertificatesAndCRLs(certsAndcrls);
        return gen;
	}

	
}
