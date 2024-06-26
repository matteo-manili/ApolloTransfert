package com.apollon.webapp.util.controller.gestioneApplicazione;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.apollon.Constants;
import com.apollon.dao.GestioneApplicazioneDao;
import com.apollon.model.GestioneApplicazione;
import com.apollon.webapp.util.ApplicationUtils;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public class GestioneApplicazioneUtil extends ApplicationUtils {
	
	private static final Log log = LogFactory.getLog(GestioneApplicazioneUtil.class);
	private static GestioneApplicazioneDao gestioneApplicazioneDao = (GestioneApplicazioneDao) contextDao.getBean("GestioneApplicazioneDao");
	
	public static GestioneApplicazione getNameGestApplicaz(List<GestioneApplicazione> gestApplicazList, String name) {
		for(GestioneApplicazione ite: gestApplicazList) {
			if(ite.getName().equals(name)) {
				return ite;
			}
		}
		return null;
	}
	
	
	/**
	 * Questo serve per prendere informazioni e fare rimborsi di una transazione PayPal
	 */
	public static String CredenzialiRESTPayPal(){
		long payPalSWITCH = gestioneApplicazioneDao.getName("SWITCH_PAYPAL_KEY_LIVE_TEST").getValueNumber();
		if(payPalSWITCH == 0l){
			return Constants.PAYPAL_SANDBOX_CLIENT_ID +"#"+ Constants.PAYPAL_SANDBOX_CLIENT_SECRET +"#"+"sandbox";
		}else{
			return Constants.PAYPAL_LIVE_CLIENT_ID +"#"+ Constants.PAYPAL_LIVE_CLIENT_SECRET +"#"+"live";
		}
	}
	
	/**
	 * questo serve per fare girare il botton payPal e pagare online
	 */
	public static String CredenzialiWebButtonPayPal(){
		long payPalSWITCH = gestioneApplicazioneDao.getName("SWITCH_PAYPAL_KEY_LIVE_TEST").getValueNumber();
		return CredenzialiWebButtonPayPal(payPalSWITCH);
	}
	
	public static String CredenzialiWebButtonPayPal(long payPalSWITCH){
		if(payPalSWITCH == 0l){
			return Constants.PAYPAL_SANDBOX_CLIENT_ID +"#"+ Constants.PAYPAL_SANDBOX; // "sandbox";
		}else{
			return Constants.PAYPAL_LIVE_CLIENT_ID +"#"+ Constants.PAYPAL_LIVE; // "production";
		}
	}
	

	//PUBLISCHABLE - servono per passarle alla jsp dove si fa il pagamento
	public static String CredenzialiPublishableStripe(){
		long stripeSWITCH = gestioneApplicazioneDao.getName("SWITCH_STRIPE_KEY_LIVE_TEST").getValueNumber();
		return CredenzialiPublishableStripe(stripeSWITCH);
	}
	
	public static String CredenzialiPublishableStripe(long stripeSWITCH){
		if(stripeSWITCH == 0l){
			return Constants.STRIPE_PUBLISCHABLE_KEY_TEST;  
		}else{
			return Constants.STRIPE_PUBLISCHABLE_KEY_LIVE;
		}
	}

	
	//SECRET - servono per fare le chiamte API JAVA
	public static String CredenzialiSecretStripe(){
		long stripeSWITCH = gestioneApplicazioneDao.getName("SWITCH_STRIPE_KEY_LIVE_TEST").getValueNumber();
		if(stripeSWITCH == 0l){
			return Constants.STRIPE_SECRET_KEY_TEST;  
		}else{
			return Constants.STRIPE_SECRET_KEY_LIVE;
		}
	}
	
	public static String SwitchTestProduzione(String mode){
		if(mode.equals("TEST")){
			log.debug("switchTestProduzione: "+mode);

			GestioneApplicazione INVIO_SMS_ABILITATO = gestioneApplicazioneDao.getName("INVIO_SMS_ABILITATO");
			INVIO_SMS_ABILITATO.setValueNumber(0l);
			gestioneApplicazioneDao.saveGestioneApplicazione(INVIO_SMS_ABILITATO);
			
			GestioneApplicazione SWITCH_STRIPE_KEY_LIVE_TEST = gestioneApplicazioneDao.getName("SWITCH_STRIPE_KEY_LIVE_TEST");
			SWITCH_STRIPE_KEY_LIVE_TEST.setValueNumber(0l);
			gestioneApplicazioneDao.saveGestioneApplicazione(SWITCH_STRIPE_KEY_LIVE_TEST);
			
			GestioneApplicazione SWITCH_PAYPAL_KEY_LIVE_TEST = gestioneApplicazioneDao.getName("SWITCH_PAYPAL_KEY_LIVE_TEST");
			SWITCH_PAYPAL_KEY_LIVE_TEST.setValueNumber(0l);
			gestioneApplicazioneDao.saveGestioneApplicazione(SWITCH_PAYPAL_KEY_LIVE_TEST);
			
			GestioneApplicazione GOOGLE_API_MAP = gestioneApplicazioneDao.getName("GOOGLE_API_MAP");
			GOOGLE_API_MAP.setValueString( Constants.API_KEY_3 );
			gestioneApplicazioneDao.saveGestioneApplicazione(GOOGLE_API_MAP);
			
			GestioneApplicazione GOOGLE_API_MAP_JS = gestioneApplicazioneDao.getName("GOOGLE_API_MAP_JS");
			GOOGLE_API_MAP_JS.setValueString( Constants.API_KEY_1 );
			gestioneApplicazioneDao.saveGestioneApplicazione(GOOGLE_API_MAP_JS);
			
			GestioneApplicazione SWITCH_TEST_PRODUZIONE = gestioneApplicazioneDao.getName("SWITCH_TEST_PRODUZIONE");
			SWITCH_TEST_PRODUZIONE.setValueNumber(0l);
			gestioneApplicazioneDao.saveGestioneApplicazione(SWITCH_TEST_PRODUZIONE);
			return "impostati dati TEST";
			
		}else if(mode.equals("PRODUZIONE")){
			log.debug("switchTestProduzione: "+mode);
			
			GestioneApplicazione INVIO_SMS_ABILITATO = gestioneApplicazioneDao.getName("INVIO_SMS_ABILITATO");
			INVIO_SMS_ABILITATO.setValueNumber(1l);
			gestioneApplicazioneDao.saveGestioneApplicazione(INVIO_SMS_ABILITATO);
			
			GestioneApplicazione SWITCH_STRIPE_KEY_LIVE_TEST = gestioneApplicazioneDao.getName("SWITCH_STRIPE_KEY_LIVE_TEST");
			SWITCH_STRIPE_KEY_LIVE_TEST.setValueNumber(1l);
			gestioneApplicazioneDao.saveGestioneApplicazione(SWITCH_STRIPE_KEY_LIVE_TEST);
			
			GestioneApplicazione SWITCH_PAYPAL_KEY_LIVE_TEST = gestioneApplicazioneDao.getName("SWITCH_PAYPAL_KEY_LIVE_TEST");
			SWITCH_PAYPAL_KEY_LIVE_TEST.setValueNumber(1l);
			gestioneApplicazioneDao.saveGestioneApplicazione(SWITCH_PAYPAL_KEY_LIVE_TEST);
			
			GestioneApplicazione GOOGLE_API_MAP = gestioneApplicazioneDao.getName("GOOGLE_API_MAP");
			GOOGLE_API_MAP.setValueString( Constants.API_KEY_8 );
			gestioneApplicazioneDao.saveGestioneApplicazione(GOOGLE_API_MAP);
			
			GestioneApplicazione GOOGLE_API_MAP_JS = gestioneApplicazioneDao.getName("GOOGLE_API_MAP_JS");
			GOOGLE_API_MAP_JS.setValueString( Constants.API_KEY_4 );
			gestioneApplicazioneDao.saveGestioneApplicazione(GOOGLE_API_MAP_JS);
			
			GestioneApplicazione gestApp = gestioneApplicazioneDao.getName("SWITCH_TEST_PRODUZIONE");
			gestApp.setValueNumber(1l);
			gestioneApplicazioneDao.saveGestioneApplicazione(gestApp);
			return "Impostati dati PRODUZIONE";
		}
		return "Nessuna impostazione";
	}
	
	
	
	
}
