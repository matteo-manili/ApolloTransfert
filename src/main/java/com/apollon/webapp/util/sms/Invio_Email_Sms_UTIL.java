package com.apollon.webapp.util.sms;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.apollon.Constants;
import com.apollon.model.ListaInvioEmailSms;
import com.apollon.model.RicercaTransfert;
import com.apollon.util.DateUtil;
import com.apollon.util.UtilBukowski;
import com.apollon.webapp.util.ApplicationUtils;

public class Invio_Email_Sms_UTIL extends ApplicationUtils {
	private static final Log log = LogFactory.getLog(Invio_Email_Sms_UTIL.class);
	public static final int LUNHGHEZZA_TOKEN_SMS = 10;
	protected static final Integer ORE_24 = 24; // 1 giorno
	protected static final Integer ORE_48 = 48; // 2 giorni
	protected static final Integer ORE_72 = 72; // 3 giorni
	
    public static final int TIPO_MESSAGGIO_SMS_RAPIDO = 0;
    public static final int TIPO_MESSAGGIO_SMS_AVVISO_MATTEO_CORSA_VENDUTA = 1;
    public static final int TIPO_MESSAGGIO_SMS_AVVISO_CORSA_ANDATA_CLIENTE_CONTATTO_AUTISTA_DISPONIBLE = 2;
    public static final int TIPO_MESSAGGIO_SMS_AVVISO_CORSA_RITORNO_CLIENTE_CONTATTO_AUTISTA_DISPONIBLE = 3;
    public static final int TIPO_MESSAGGIO_SMS_AVVISO_CORSA_ANDATA_CLIENTE_24_ORE = 4;
    public static final int TIPO_MESSAGGIO_SMS_AVVISO_CORSA_RITORNO_CLIENTE_24_ORE = 5;
    public static final int TIPO_MESSAGGIO_SMS_AVVISO_CORSA_ANDATA_AUTISTA_CONTATTO_CLIENTE_DISPONIBLE = 6;
    public static final int TIPO_MESSAGGIO_SMS_AVVISO_CORSA_RITORNO_AUTISTA_CONTATTO_CLIENTE_DISPONIBLE = 7;
    public static final int TIPO_MESSAGGIO_SMS_AVVISO_CORSA_ANDATA_AUTISTA_48_ORE = 8;
    public static final int TIPO_MESSAGGIO_SMS_AVVISO_CORSA_RITORNO_AUTISTA_48_ORE = 9;
    public static final int TIPO_MESSAGGIO_SMS_AVVISO_CORSA_ANDATA_AUTISTA_24_ORE = 10;
    public static final int TIPO_MESSAGGIO_SMS_AVVISO_CORSA_RITORNO_AUTISTA_24_ORE = 11;
    
    public static final int TIPO_MESSAGGIO_EMAIL_SCRIVI_RECENSIONE_TRANSFER = 101;
	

    public static void Crea_ListaInvioEmailSms_EMAIL_ScriviRecensioneTransfer(RicercaTransfert ricTransfert, Locale locale) {
    	if( !ricTransfert.getUser().getEmail().contains(Constants.FAKE_EMAIL) ) {
    		ListaInvioEmailSms NuovoSms = DammiNuovo_ListaInvioEmailSms(ricTransfert, TIPO_MESSAGGIO_EMAIL_SCRIVI_RECENSIONE_TRANSFER, null, null, null);
    		listaInvioSmsDao.saveListaInvioSms( NuovoSms );
    		System.out.println( "INSERITO LISTAINVIOSMS: "+NuovoSms.getId() );
    	}
    }
    
    /**
     * Controllo che l'orario Avviso Corsa sia precedente di TOT ORE all'orario Appuntamento
     * 
     * @param DataOraAppuntamento
     * @param DistanzaOre
     * @return
     */
    private static boolean ControlloOrarioAvvisoCorsa_PrecedenteAppuntamento(Date DataOraAppuntamento, int DistanzaOre) {
		Date date = DateUtil.AggiungiOre_a_DataAdesso(DistanzaOre);
		if( date.getTime() < DataOraAppuntamento.getTime() ) {
			return true;
		}else {
			return false;
		}
    }
    
	public static void Crea_ListaInvioEmailSms_SMS_Cliente_Autista(RicercaTransfert ricTransfert, Locale locale) {
		ListaInvioEmailSms NuovoSms = DammiNuovo_ListaInvioEmailSms(ricTransfert, TIPO_MESSAGGIO_SMS_AVVISO_CORSA_ANDATA_CLIENTE_CONTATTO_AUTISTA_DISPONIBLE, null, null, null);
		listaInvioSmsDao.saveListaInvioSms( NuovoSms );
		if( ricTransfert.isRitorno() ) {
			NuovoSms = DammiNuovo_ListaInvioEmailSms(ricTransfert, TIPO_MESSAGGIO_SMS_AVVISO_CORSA_RITORNO_CLIENTE_CONTATTO_AUTISTA_DISPONIBLE, null, null, null);
			listaInvioSmsDao.saveListaInvioSms( NuovoSms );
		}
		//----------
		if( ControlloOrarioAvvisoCorsa_PrecedenteAppuntamento(ricTransfert.getDataOraPrelevamentoDate(), ORE_24) ) {
			NuovoSms = DammiNuovo_ListaInvioEmailSms(ricTransfert, TIPO_MESSAGGIO_SMS_AVVISO_CORSA_ANDATA_CLIENTE_24_ORE, null, null, null);
			listaInvioSmsDao.saveListaInvioSms( NuovoSms );
		}
		if( ricTransfert.isRitorno() && ControlloOrarioAvvisoCorsa_PrecedenteAppuntamento(ricTransfert.getDataOraRitornoDate(), ORE_24)) {
			NuovoSms = DammiNuovo_ListaInvioEmailSms(ricTransfert, TIPO_MESSAGGIO_SMS_AVVISO_CORSA_RITORNO_CLIENTE_24_ORE, null, null, null);
			listaInvioSmsDao.saveListaInvioSms( NuovoSms );
		}
		//----------
		NuovoSms = DammiNuovo_ListaInvioEmailSms(ricTransfert, TIPO_MESSAGGIO_SMS_AVVISO_CORSA_ANDATA_AUTISTA_CONTATTO_CLIENTE_DISPONIBLE, null, null, null);
		listaInvioSmsDao.saveListaInvioSms( NuovoSms );
		if( ricTransfert.isRitorno() ) {
			NuovoSms = DammiNuovo_ListaInvioEmailSms(ricTransfert, TIPO_MESSAGGIO_SMS_AVVISO_CORSA_RITORNO_AUTISTA_CONTATTO_CLIENTE_DISPONIBLE, null, null, null);
			listaInvioSmsDao.saveListaInvioSms( NuovoSms );
		}
		//----------
		if( ControlloOrarioAvvisoCorsa_PrecedenteAppuntamento(ricTransfert.getDataOraPrelevamentoDate(), ORE_48) ) {
			NuovoSms = DammiNuovo_ListaInvioEmailSms(ricTransfert, TIPO_MESSAGGIO_SMS_AVVISO_CORSA_ANDATA_AUTISTA_48_ORE, null, null, null);
			listaInvioSmsDao.saveListaInvioSms( NuovoSms );
		}
		if( ricTransfert.isRitorno() && ControlloOrarioAvvisoCorsa_PrecedenteAppuntamento(ricTransfert.getDataOraRitornoDate(), ORE_48)) {
			NuovoSms = DammiNuovo_ListaInvioEmailSms(ricTransfert, TIPO_MESSAGGIO_SMS_AVVISO_CORSA_RITORNO_AUTISTA_48_ORE, null, null, null);
			listaInvioSmsDao.saveListaInvioSms( NuovoSms );
		}
		//----------
		if( ControlloOrarioAvvisoCorsa_PrecedenteAppuntamento(ricTransfert.getDataOraPrelevamentoDate(), ORE_24) ) {
			NuovoSms = DammiNuovo_ListaInvioEmailSms(ricTransfert, TIPO_MESSAGGIO_SMS_AVVISO_CORSA_ANDATA_AUTISTA_24_ORE, null, null, null);
			listaInvioSmsDao.saveListaInvioSms( NuovoSms );
		}
		if( ricTransfert.isRitorno() && ControlloOrarioAvvisoCorsa_PrecedenteAppuntamento(ricTransfert.getDataOraRitornoDate(), ORE_24) ) {
			NuovoSms = DammiNuovo_ListaInvioEmailSms(ricTransfert, TIPO_MESSAGGIO_SMS_AVVISO_CORSA_RITORNO_AUTISTA_24_ORE, null, null, null);
			listaInvioSmsDao.saveListaInvioSms( NuovoSms );
		}
	}
	
	
	/**
	 * Preparazione nuovo SMS 
	 * 
	 * @param testoMessaggio
	 * @param numeroDestinatario
	 * @return
	 */
	public static ListaInvioEmailSms DammiNuovo_ListaInvioEmailSms(RicercaTransfert ricercaTransfert, Integer tipoMessaggio, String testoMessaggio, String numeroDestinatario, Date dataInvio) {
		String token = "";
		while( true ){
			token = UtilBukowski.getRandomToken__LettGrandi_Numeri( LUNHGHEZZA_TOKEN_SMS );
			if(listaInvioSmsDao.getSms_By_Token(token) == null) {
				return new ListaInvioEmailSms(ricercaTransfert, tipoMessaggio, testoMessaggio, numeroDestinatario, token, dataInvio);
			}
		}
    }
	
	
	public static boolean ListaInvioEmailSms_ConfermaInviato(Long idListaInvioSms) {
		System.out.println("ConfermaSmsInviato idListaInvioSms: "+idListaInvioSms);
		ListaInvioEmailSms Sms = listaInvioSmsDao.get( idListaInvioSms );
		if(Sms != null && Sms.isInviato() == false) {
			Sms.setInviato(true);
			Sms.setDataInvio(new Date());
			listaInvioSmsDao.saveListaInvioSms(Sms);
			return true;
		}else {
			return false;
		}
	}
	
	
	/**
	 * Confermo la certezza dell'invo SMS
	 * @param tokenSms
	 */
	public static boolean ConfermaSmsInviatoToken(String tokenSms) {
		System.out.println("ConfermaSmsInviato tokenSms: "+tokenSms);
		ListaInvioEmailSms Sms = listaInvioSmsDao.getSms_By_Token(tokenSms);
		if(Sms != null && Sms.isInviato() == false) {
			Sms.setInviato(true);
			Sms.setDataInvio(new Date());
			listaInvioSmsDao.saveListaInvioSms(Sms);
			return true;
		}else {
			return false;
		}
	}
	
	
}
