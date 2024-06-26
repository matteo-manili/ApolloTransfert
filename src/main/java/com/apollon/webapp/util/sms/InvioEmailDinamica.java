package com.apollon.webapp.util.sms;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.internet.AddressException;
import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.apollon.Constants;
import com.apollon.model.ListaInvioEmailSms;
import com.apollon.model.RicercaTransfert;
import com.apollon.model.RichiestaMediaAutista;
import com.apollon.util.DateUtil;
import com.apollon.webapp.util.email.EmailMarketing_ClientiRecencioneSconto;

public class InvioEmailDinamica extends Invio_Email_Sms_UTIL {
	private static final Log log = LogFactory.getLog(InvioEmailDinamica.class);
	
	
	@SuppressWarnings("unlikely-arg-type")
	public static boolean InviaEmailDinamica_ScriviRecensioneCorsa(ServletContext servletContext, ListaInvioEmailSms smsDinamico ) 
			throws AddressException, AuthenticationFailedException, SendFailedException, MessagingException, IOException, InterruptedException {
		boolean esitoInvio = false;
		if( smsDinamico.getTipoMessaggio().equals(TIPO_MESSAGGIO_EMAIL_SCRIVI_RECENSIONE_TRANSFER)) {
			Integer numeroOre = new Integer(ORE_72);
			RicercaTransfert ricTransfert = ricercaTransfertDao.get( smsDinamico.getRicercaTransfert().getId() );
			RichiestaMediaAutista richMediaAutista = ricercaTransfertDao.getRichiestaMediaAutista_AutistaAssegnatoCorsaMedia(ricTransfert.getId());
			if( ricTransfert.getApprovazioneAndata() == Constants.APPROVATA && richMediaAutista != null ) {
				Date date = DateUtil.AggiungiOre_a_Data(ricTransfert.isRitorno() ? ricTransfert.getDataOraRitornoDate()  : ricTransfert.getDataOraPrelevamentoDate(), numeroOre);
				
				smsDinamico.setTestoMessaggio(null);
				smsDinamico.setNumeroDestinatario( null );
				smsDinamico.setDataInvio(date);
				smsDinamico = listaInvioSmsDao.saveListaInvioSms( smsDinamico );
				// controllo se la data prelevamento è inferiore di adesso && la corsa è approvata && Un autista autista l'ha preso in carico
				if( date.getTime() < new Date().getTime() && ricTransfert.getApprovazioneAndata() == Constants.APPROVATA && richMediaAutista != null) {
					
					if(gestioneApplicazioneDao.getName("INVIO_SMS_ABILITATO").getValueNumber() == 1 && CheckAmbienteProduzione_Static_ContextNameProduzione_AND_IpAddessProduzione(servletContext)) {
						esitoInvio = EmailMarketing_ClientiRecencioneSconto.inviaEmailMarketing_ClientiRecencioneSconto(null, null, ricTransfert.getUser().getId());
						log.debug("InviaEmailDinamica_ScriviRecensioneCorsa PRODUZIONE INVIATA: " );
						
					}else if( ricTransfert.getUser().equals(ApplicationMessagesUtil.DammiMessageSource("email.matteo.manili.gmail")) ) {
						esitoInvio = EmailMarketing_ClientiRecencioneSconto.inviaEmailMarketing_ClientiRecencioneSconto(null, null, ricTransfert.getUser().getId());
						log.debug("InviaEmailDinamica_ScriviRecensioneCorsa TEST INVIATA" );
						
					}else{
						log.debug("InviaEmailDinamica_ScriviRecensioneCorsa NON INVIATA");
						esitoInvio = true;
					}
					Invio_Email_Sms_UTIL.ListaInvioEmailSms_ConfermaInviato(smsDinamico.getId());
				}
			}
		}
		return esitoInvio;
	}
	
	
	

	
	
}
