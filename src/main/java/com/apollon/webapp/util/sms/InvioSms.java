package com.apollon.webapp.util.sms;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.internet.AddressException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParamBean;
import org.apache.http.util.EntityUtils;

import com.apollon.Constants;
import com.apollon.model.GestioneApplicazione;
import com.apollon.model.ListaInvioEmailSms;
import com.apollon.model.RicercaTransfert;
import com.apollon.util.firebase.FirebaseCloudMessaging;

/**
	If you use maven, add the folowing dependency to your pom.xml.
	<dependency>
	    <groupId>org.apache.httpcomponents</groupId>
	    <artifactId>httpclient</artifactId>
	    <version>4.1.1</version>
	</dependency>
	
	Otherwise download Apache HttpComponents from http://hc.apache.org/
	and add the libs to your classpath.
	
	http://www.skebby.it/business/index/send-docs
	http://freesms.skebby.it/invia-sms-gratis/
	http://www.skebby.it/shop-sms/prezzi-sms-economici/
	
	Invio SMS Classic
	Invia un SMS Classic e restituisce il numero di SMS residui
	
		Url per richieste HTTP:
	
	http://gateway.skebby.it/api/send/smseasy/advanced/http.php
	 Secure https://gateway.skebby.it/api/send/smseasy/advanced/http.php 
	
	I parametri devono essere passati con metodo POST, nello standard urlencode. È stato disabilitato il metodo GET perchè con tanti destinatari la richiesta potrebbe essere troncata. 
	
	Parametri obbligatori: 
	Parametro	Descrizione
	String method	send_sms_classic
	String username	Username con cui ci si è registrati
	String password	Password con cui ci si è registrati
	String[] recipients	Destinatario/i, in formato internazionale senza + o 00, sotto forma di Array ad esempio:
	
	recipients[]=393334455666&recipients[]=393334455667 
	
	New Personalizzazione testo per ogni destinatario 
	String text	Testo del messaggio fino a lunghezza massima di 1530 caratteri o 10 SMS concatenati in un unico messaggio. Per le modalità di conteggio si veda Modalità di conteggio SMS lunghi tradizionali
	
	Parametri opzionali: 
	Parametro	Descrizione
	String sender_number*	Permette di specificare un qualsiasi numero di telefono come mittente, il numero deve essere in formato internazionale senza + o 00, ad esempio: 393334455666
	
	ATTENZIONE: è importante specificare il numero in formato internazionale altrimenti il destinatario dell'SMS potrebbe non riuscire a rispondere al messaggio.
	String sender_string*	Permette di specificare una stringa alfanumerica di lunghezza massima di 11 caratteri da utilizzare come mittente, caratteri supportati: [a-zA-Z0-9 .]
	String charset	Vedi nota sull'utilizzo del charset
	String delivery_start	Per programmare invio SMS, usare il formato RFC 2822.
	Vedi specifica RFC 2822
	String encoding_scheme	Per inviare messaggi con caratteri speciali o con accenti comunemente usati nei seguenti linguaggi: Arabo, Cinese, Coreano, Cirillico, Francese, Giapponese, Spagnolo e le lingue Slave
	Valori accettati:
	normal (7 bit, default)
	UCS2
	Testo del messaggio fino alla lunghezza massima di 670 caratteri o 10 SMS concatenati in un unico messaggio. Per le modalità di conteggio si veda Modalità conteggio SMS lunghi UCS2
	Integer validity_period	È possibile specificare per quanti minuti (o ore) l'operatore deve riprovare ad inviare l'SMS in caso di cellulare spento o non raggiungibile. 
	Espresso in minuti, numero intero (valore minimo 5 minuti), default 2gg = 2880(60*48)**
	
	Esempio di body HTTP per invio multiplo SMS Classic con mittente personalizzato alfanumerico: 
	method=send_sms_classic&username=user&password=pass&recipients[]=393471234567&recipients[]=393477654321&text=Ciao+Mario+come+stai&sender_string=LucaRossi
	
	
	Modalità di conteggio dei messaggi per invio SMS lunghi 
	SMS tradizionali:
	Il costo del messaggio sarà conteggiato ogni 153 caratteri, ad eccezione del primo SMS che ne avrà a disposizione 160, fino ad un massimo di 1530 o 10 SMS concatenati.
	
	Caratteri	Numero di SMS addebitati
	0-160	1
	161-306	2
	307-459	3
	460-612	4
	613-765	5
	766-918	6
	919-1071	7
	1072-1224	8
	1225-1377	9
	1378-1530	10
	
	Alcuni caratteri contano doppio: 
	[	Parentesi quadra aperta
	\	Backslash
	]	Parentesi quadra chiusa
	^	Potenza
	{	Parentesi graffa aperta
	|	Barra verticale
	}	Parentesi graffa chiusa
	~	Tilde
	€	Simbolo dell'euro
	Per maggiori dettagli consulta http://en.wikipedia.org/wiki/GSM_03.38



	ESEMPIO DI INVIO:
    // SMS SKEBBY
	//Single dispatch
	//String [] recipients = new String[]{"391234567890"};
	//Multiple dispatch
    String [] recipients = new String[]{"393289126869",""};

    String username = "MatteoManili";
    String password = "giulietta01";

    // SMS CLASSIC dispatch with custom alphanumeric sender
    SmsSkebby aa = new SmsSkebby();
    String result = aa.skebbyGatewaySendSMS(username, password, recipients, "prova invio sms da skebb", "send_sms_basic", null, null);
    
    log.debug("result: "+result);
    
    // SMS Basic dispatch ("send_sms_basic" costa di meno ma visualizza un numero di telefono farlocco: 393664610123)
    // String result = skebbyGatewaySendSMS(username, password, recipients, "Hi Mike, how are you?", "send_sms_basic", null, null);
     
    // SMS CLASSIC dispatch with custom numeric sender ("send_sms_classic" costa di più  ma visualizza il mio numero di telefono 393289126869)
    // String result = skebbyGatewaySendSMS(username, password, recipients, "Hi Mike, how are you?", "send_sms_classic", "393471234567", null);
     
    // SMS CLASSIC PLUS dispatch (with delivery report) with custom alphanumeric sender
    // String result = skebbyGatewaySendSMS(username, password, recipients, "Hi Mike, how are you?", "send_sms_classic_report", null, "John");

    // SMS CLASSIC PLUS dispatch (with delivery report) with custom numeric sender
    // String result = skebbyGatewaySendSMS(username, password, recipients, "Hi Mike, how are you?", "send_sms_classic_report", "393471234567", null);

	// ------------------------------------------------------------------
	// Check the complete documentation at http://www.skebby.com/business/index/send-docs/
	// ------------------------------------------------------------------
	// For eventual errors see http:#www.skebby.com/business/index/send-docs/#errorCodesSection
	// WARNING: in case of error DON'T retry the sending, since they are blocking errors
	// ------------------------------------------------------------------	

	Un altra alternativa è skebby è https://www.nexmo.com/pricing il prezzo è cmq. di 0,06 
	https://www.nexmo.com/pricing
	
	
	*** usare queste api costano molto meno: https://rapidapi.com/telesign/api/telesign-sms-verify/pricing
*/

@SuppressWarnings("deprecation")
public class InvioSms extends Invio_Email_Sms_UTIL {
	private static final Log log = LogFactory.getLog(InvioSms.class);
	
	
	public static boolean Lancia_SMS_Gateway(ServletContext servletContext, ListaInvioEmailSms sms) throws AddressException, AuthenticationFailedException, SendFailedException, MessagingException, IOException {
		boolean esito = false;
		if(gestioneApplicazioneDao.getName("INVIO_SMS_ABILITATO").getValueNumber() == 1 && CheckAmbienteProduzione_Static_ContextNameProduzione_AND_IpAddessProduzione(servletContext)) {
			esito = InviaEmail_SMS_GATEWAY( sms.getNumeroDestinatario(), sms.getTestoMessaggio() );
			log.debug("InviaEmail_SMS_GATEWAY PRODUZIONE: "+ sms.getNumeroDestinatario()+" | "+sms.getDataInvio()+" | "+sms.getTestoMessaggio() );
			
		}else if( sms.getNumeroDestinatario().equals(ApplicationMessagesUtil.DammiMessageSource("cellulare.matteo")) /*|| sms.getNumeroDestinatario().equals("+393934040437") telefono silvia*/ ) {
			esito = InviaEmail_SMS_GATEWAY( sms.getNumeroDestinatario(), sms.getTestoMessaggio() );
			log.debug("InviaEmail_SMS_GATEWAY PRODUZIONE: "+ sms.getNumeroDestinatario()+" | "+sms.getDataInvio()+" | "+sms.getTestoMessaggio() );
			
		}else{
			log.debug("SMS NON INVIATO TEST SMS_GATEWAY Testo sms: "+sms.getTestoMessaggio());
			esito = true;
		}
		Invio_Email_Sms_UTIL.ListaInvioEmailSms_ConfermaInviato(sms.getId());
		return esito;
	}
	
	
	public static String Crea_SMS_Gateway(RicercaTransfert ricTransfert, String numeroDestinatario, String testoSms, int TipoMessaggioSms) {
		ListaInvioEmailSms NuovoSms = DammiNuovo_ListaInvioEmailSms(ricTransfert, TipoMessaggioSms, testoSms, numeroDestinatario, new Date());
		NuovoSms = listaInvioSmsDao.saveListaInvioSms( NuovoSms );
		return Constants.SMS_STATUS_SUCCESS;
    }
	
	public static String Crea_SMS_Gateway(String numeroDestinatario, String testoSms, int TipoMessaggioSms) {
		ListaInvioEmailSms NuovoSms = DammiNuovo_ListaInvioEmailSms(null, TipoMessaggioSms, testoSms, numeroDestinatario, new Date());
		NuovoSms = listaInvioSmsDao.saveListaInvioSms( NuovoSms );
		return Constants.SMS_STATUS_SUCCESS;
    }
    
	/**
	 * Il costo del messaggio sarà conteggiato ogni 153 caratteri e costa 0,046€
	 * 
	SmsSkebby skebby = new SmsSkebby();
	[send_sms_basic] con il basic mi fa vedere ogni volta un numero diverso (lasciare null gli ultimi due parametri)
	[send_sms_classic] con il classic mi fa vedere il mio numero di telefono (lasciare null gli ultimi due parametri altrimenti mi fa vedere un altro numero, ma sempre lo stesso)
	il penultimo e l'ultimo parametro sono per i clienti bisness (con partita iva) servono a personalizzare il mittente o con numero o lettere
	
	questo metodo alla fine inserisce nella GestioneApplicazione gli sms rimanenti
	
	possibili return
	return "success" // il messaggio è stato inviato
	return "" // il risultato dell'invio non è stato inviato
	*/
	public static String Invio_SMS_Skebby(HttpServletRequest request, String []destinatari, String testoSms, String DomainSms) {
		try{
			ListaInvioEmailSms NuovoSms = DammiNuovo_ListaInvioEmailSms(null, TIPO_MESSAGGIO_SMS_RAPIDO, testoSms, destinatari[0], new Date());
			String statusInvioSms = "";
			
			final String smsType = "send_sms_classic"; final String SMS_SKEBBY_STATUS_SUCCESS = "success"; boolean business = true;
			String senderNumberBisness = null; String senderStringBisness = null;
			if(business){
				senderNumberBisness = ApplicationMessagesUtil.DammiMessageSource("cellulare.matteo"); // "+393289126869";
				senderStringBisness = DomainSms; // APOLLO oppure NCCTRANSFERONLINE
			}
			String result = "";
			if(gestioneApplicazioneDao.getName("INVIO_SMS_ABILITATO").getValueNumber() == 1 && CheckAmbienteApolloTransfert_Mondoserver(request)){
				result = SkebbyGatewaySendSMS(destinatari, testoSms, smsType, senderNumberBisness, senderStringBisness, Constants.ENCODING_UTF_8);
				NuovoSms = listaInvioSmsDao.saveListaInvioSms( NuovoSms );
				log.debug("SMS INVIATO PRODUZIONE SMS_SKEBBY | Result sms: "+result +" Testo sms: "+testoSms+" Lunghezza SMS: "+ testoSms.length() +" Telefono: "+senderNumberBisness );
				
			}else if( destinatari[0].equals(ApplicationMessagesUtil.DammiMessageSource("cellulare.matteo")) || destinatari[0].equals("+393934040437")/*telefono silvia*/ ) {
				result = SkebbyGatewaySendSMS(destinatari, testoSms, smsType, senderNumberBisness, senderStringBisness, Constants.ENCODING_UTF_8);
				NuovoSms = listaInvioSmsDao.saveListaInvioSms( NuovoSms );
				log.debug("SMS INVIATO TEST SMS_SKEBBY | Result sms: "+result +" Testo sms: "+testoSms+" Lunghezza SMS: "+ testoSms.length() +" Telefono: "+senderNumberBisness );
				
			}else{
				log.debug("SMS NON INVIATO TEST SMS_SKEBBY | Result sms: "+result +" Testo sms: "+testoSms+" Lunghezza SMS: "+ testoSms.length() +" Telefono: "+senderNumberBisness );
			}
			// esempio di result
			//Result sms: status=success&id=7de2c404-3a92-4035-bd96-409b4fe99a43&remaining_sms=323 Testo sms: codice: 7324 Lunghezza SMS: 12 Telefono: +393289126869
			String remaining_sms = ""; String PartStatus = "";
			if(result != null && result.contains("&")){
				String[] parts = result.split("&");
				if(result.contains("status")){
					String statusPart = parts[0]; // 004
					String[] partStatus = statusPart.split("=");
					PartStatus = partStatus[1];
					//SALVO LA CONFERMA INVIO SMS se il result skebby è success
					if(PartStatus.equals( SMS_SKEBBY_STATUS_SUCCESS )) {
						ListaInvioEmailSms_ConfermaInviato( NuovoSms.getId() );
						statusInvioSms = Constants.SMS_STATUS_SUCCESS;
					}
				}
				if(result.contains("remaining_sms")){
					String remaining_smsPart = parts[2]; // 034556
					String[] partRemaing_sms = remaining_smsPart.split("=");
					remaining_sms = partRemaing_sms[1];
					GestioneApplicazione gestApp = gestioneApplicazioneDao.getName("SMS_SKEBBY_RIMANENTI") ;
					gestApp.setValueNumber( Long.parseLong( remaining_sms ));
					gestioneApplicazioneDao.saveGestioneApplicazione(gestApp);
				}
				log.debug("status="+PartStatus + " remaining_sms="+remaining_sms);
			}
			return statusInvioSms;
			
		}catch(IOException ioe){
			ioe.printStackTrace();
			return null;
		}catch(Exception exc){
			exc.printStackTrace();
			return null;
		}
    }
	
	public static String Invio_SMS_Firebase(HttpServletRequest request, String numeroDestinatario, String testoSms) throws IOException {
		ListaInvioEmailSms NuovoSms = DammiNuovo_ListaInvioEmailSms(null, TIPO_MESSAGGIO_SMS_RAPIDO, testoSms, numeroDestinatario, new Date());
		String statusInvioSms = "";
		if(gestioneApplicazioneDao.getName("INVIO_SMS_ABILITATO").getValueNumber() == 1 && CheckAmbienteApolloTransfert_Mondoserver(request)) {
			FirebaseCloudMessaging.sendCommonMessageSms(request, testoSms, numeroDestinatario, NuovoSms.getToken());
			NuovoSms = listaInvioSmsDao.saveListaInvioSms( NuovoSms );
			ConfermaSmsInviatoToken( NuovoSms.getToken() ); // Confermo subito l'invio perché non c'è maniera di verificarare se l'sms è stato inviato effettivamente
			statusInvioSms = Constants.SMS_STATUS_SUCCESS;
			log.debug("SMS INVIATO PRODUZIONE SMS_FIREBASE | Result sms: "+statusInvioSms +" Testo sms: "+testoSms+" Lunghezza SMS: "+ testoSms.length() +" Telefono: "+numeroDestinatario );
			
		}else if( numeroDestinatario.equals(ApplicationMessagesUtil.DammiMessageSource("cellulare.matteo")) || numeroDestinatario.equals("+393934040437") /*telefono silvia*/ ) {
			FirebaseCloudMessaging.sendCommonMessageSms(request, testoSms, numeroDestinatario, NuovoSms.getToken());
			NuovoSms = listaInvioSmsDao.saveListaInvioSms( NuovoSms );
			ConfermaSmsInviatoToken( NuovoSms.getToken() ); // Confermo subito l'invio perché non c'è maniera di verificarare se l'sms è stato inviato effettivamente
			statusInvioSms = Constants.SMS_STATUS_SUCCESS;
			log.debug("SMS INVIATO TEST SMS_FIREBASE | Result sms: "+statusInvioSms +" Testo sms: "+testoSms+" Lunghezza SMS: "+ testoSms.length() +" Telefono: "+numeroDestinatario );
			
		}else{
			log.debug("SMS NON INVIATO TEST SMS_FIREBASE Testo sms: "+testoSms);
		}

		return statusInvioSms;
    }
	
	
	@SuppressWarnings("resource")
	private static String SkebbyGatewaySendSMS(String [] recipients, String text, String smsType, String senderNumber, String senderString, String charset) throws IOException, Exception{
        
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		Properties props = new Properties();
		InputStream resourceStream = loader.getResourceAsStream( "sms.skebby.properties" );
	    props.load(resourceStream);
		
        if (!charset.equals( Constants.ENCODING_UTF_8 ) && !charset.equals("ISO-8859-1")) {
            throw new IllegalArgumentException("Charset not supported.");
        }
         
        // vecchio
        //String endpoint = "http://gateway.skebby.it/api/send/smseasy/advanced/http.php";
        
        // nuovo
        String endpoint = "http://api.skebby.it/api/send/smseasy/advanced/http.php";
        
        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, 10*1000);
        DefaultHttpClient httpclient = new DefaultHttpClient(params);
        HttpProtocolParamBean paramsBean = new HttpProtocolParamBean(params);
        paramsBean.setVersion(HttpVersion.HTTP_1_1);
        paramsBean.setContentCharset(charset);
        paramsBean.setHttpElementCharset(charset);
         
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("method", smsType));
        formparams.add(new BasicNameValuePair("username", props.getProperty("username.skebby")));
        formparams.add(new BasicNameValuePair("password", props.getProperty("password.skebby")));
        if(null != senderNumber)
            formparams.add(new BasicNameValuePair("sender_number", senderNumber));
        if(null != senderString)
            formparams.add(new BasicNameValuePair("sender_string", senderString));
         
        for (String recipient : recipients) {
            formparams.add(new BasicNameValuePair("recipients[]", recipient.replace("+", "")));
        }
        formparams.add(new BasicNameValuePair("text", text));
        formparams.add(new BasicNameValuePair("charset", charset));
 
     
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, charset);
        HttpPost post = new HttpPost(endpoint);
        post.setEntity(entity);
         
        HttpResponse response = httpclient.execute(post);
        HttpEntity resultEntity = response.getEntity();
        if(null != resultEntity){
            return EntityUtils.toString(resultEntity);
        }
        return null;
    }
}
