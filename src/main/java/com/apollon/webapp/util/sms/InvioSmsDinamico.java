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
import com.apollon.util.Telefono_Prefisso_e_Formato;
import com.apollon.webapp.util.controller.home.HomeUtil_Sms_Email;
import com.apollon.webapp.util.email.InviaEmail;

public class InvioSmsDinamico extends Invio_Email_Sms_UTIL {
	private static final Log log = LogFactory.getLog(InvioSmsDinamico.class);
	
	public static boolean InviaSmsDinamico_AvvisoRicordoCorsa(ServletContext servletContext, ListaInvioEmailSms smsDinamico ) throws AddressException, AuthenticationFailedException, SendFailedException, MessagingException, IOException {
		boolean esitoInvio = false;
		// sms.avviso.ricordo.corsa=Ti Ricordiamo che tra {0} ore avverra' il prelevamento per la corsa: {1}
		Integer numeroOre = null;
		if( smsDinamico.getTipoMessaggio().equals(TIPO_MESSAGGIO_SMS_AVVISO_CORSA_ANDATA_CLIENTE_24_ORE) 
				|| smsDinamico.getTipoMessaggio().equals(TIPO_MESSAGGIO_SMS_AVVISO_CORSA_RITORNO_CLIENTE_24_ORE)
				|| smsDinamico.getTipoMessaggio().equals(TIPO_MESSAGGIO_SMS_AVVISO_CORSA_ANDATA_AUTISTA_24_ORE)
				|| smsDinamico.getTipoMessaggio().equals(TIPO_MESSAGGIO_SMS_AVVISO_CORSA_RITORNO_AUTISTA_24_ORE) ) {
			numeroOre = new Integer(ORE_24);
		}else if( smsDinamico.getTipoMessaggio().equals(TIPO_MESSAGGIO_SMS_AVVISO_CORSA_ANDATA_AUTISTA_48_ORE) 
				|| smsDinamico.getTipoMessaggio().equals(TIPO_MESSAGGIO_SMS_AVVISO_CORSA_RITORNO_AUTISTA_48_ORE) ) {
			numeroOre = new Integer(ORE_48);
		}
		
		// ANDATA
		if( smsDinamico.getTipoMessaggio().equals(TIPO_MESSAGGIO_SMS_AVVISO_CORSA_ANDATA_CLIENTE_24_ORE)
				|| smsDinamico.getTipoMessaggio().equals(TIPO_MESSAGGIO_SMS_AVVISO_CORSA_ANDATA_AUTISTA_24_ORE)
				|| smsDinamico.getTipoMessaggio().equals(TIPO_MESSAGGIO_SMS_AVVISO_CORSA_ANDATA_AUTISTA_48_ORE) ) {
			RicercaTransfert ricTransfert = ricercaTransfertDao.get( smsDinamico.getRicercaTransfert().getId() );
			RichiestaMediaAutista richMediaAutista = ricercaTransfertDao.getRichiestaMediaAutista_AutistaAssegnatoCorsaMedia(ricTransfert.getId());
			
			

			
			Date date = DateUtil.TogliOre_a_Data(ricTransfert.getDataOraPrelevamentoDate(), numeroOre);
			
			
			if( richMediaAutista != null ) {
				String urlTxtSms = "";
				if( smsDinamico.getTipoMessaggio().equals(TIPO_MESSAGGIO_SMS_AVVISO_CORSA_ANDATA_CLIENTE_24_ORE) ) {
					urlTxtSms = InviaEmail.DammiUrl_InfoCorsa_Cliente(ricTransfert, null);
				}else if( smsDinamico.getTipoMessaggio().equals(TIPO_MESSAGGIO_SMS_AVVISO_CORSA_ANDATA_AUTISTA_24_ORE)
						|| smsDinamico.getTipoMessaggio().equals(TIPO_MESSAGGIO_SMS_AVVISO_CORSA_ANDATA_AUTISTA_48_ORE) ) {
					urlTxtSms = HomeUtil_Sms_Email.UrlSmsCorsaDisponibileAutista(null, richMediaAutista);
				}
				String telefono = "";
				if( smsDinamico.getTipoMessaggio().equals(TIPO_MESSAGGIO_SMS_AVVISO_CORSA_ANDATA_CLIENTE_24_ORE) ) {
					telefono = (ricTransfert.getTelefonoPasseggero() != null && !ricTransfert.getTelefonoPasseggero().equals("")) ? ricTransfert.getTelefonoPasseggero() : ricTransfert.getUser().getPhoneNumber(); 
					
				}else if( smsDinamico.getTipoMessaggio().equals(TIPO_MESSAGGIO_SMS_AVVISO_CORSA_ANDATA_AUTISTA_24_ORE)
						|| smsDinamico.getTipoMessaggio().equals(TIPO_MESSAGGIO_SMS_AVVISO_CORSA_ANDATA_AUTISTA_48_ORE) ) {
					telefono = richMediaAutista.getAutista().getUser().getPhoneNumber(); 
				}
				String testoSms = ApplicationMessagesUtil.DammiMessageSource("sms.avviso.ricordo.corsa", new String[]{ numeroOre.toString(), urlTxtSms }, null);
				smsDinamico.setTestoMessaggio(testoSms);
				smsDinamico.setNumeroDestinatario( telefono );
				smsDinamico.setDataInvio(date);
				smsDinamico = listaInvioSmsDao.saveListaInvioSms( smsDinamico );
				// controllo se la data prelevamento è inferiore di adesso && la corsa è approvata && Un autista autista l'ha preso in carico
				if( date.getTime() < new Date().getTime() && ricTransfert.getApprovazioneAndata() != Constants.NON_APPROVATA && richMediaAutista != null) {
					esitoInvio = InvioSms.Lancia_SMS_Gateway(servletContext, smsDinamico);
				}
			}else if( date.getTime() < new Date().getTime() && ricTransfert.getApprovazioneAndata() == Constants.NON_APPROVATA && richMediaAutista == null) {
				/* listaInvioSmsDao.remove(smsDinamico); */ 
				//System.out.println("RIMUOVO SMS: "+smsDinamico.getId());
			}
		// RITORNO
		} else if( smsDinamico.getTipoMessaggio().equals(TIPO_MESSAGGIO_SMS_AVVISO_CORSA_RITORNO_CLIENTE_24_ORE) 
				|| smsDinamico.getTipoMessaggio().equals(TIPO_MESSAGGIO_SMS_AVVISO_CORSA_RITORNO_AUTISTA_24_ORE) 
				|| smsDinamico.getTipoMessaggio().equals(TIPO_MESSAGGIO_SMS_AVVISO_CORSA_RITORNO_AUTISTA_48_ORE) ) {
			RicercaTransfert ricTransfert = ricercaTransfertDao.get( smsDinamico.getRicercaTransfert().getId() );
			if( ricTransfert.isRitorno() ) {
				RichiestaMediaAutista richMediaAutista = ricercaTransfertDao.getRichiestaMediaAutista_AutistaAssegnatoCorsaMedia(ricTransfert.getId());
				Date date = DateUtil.TogliOre_a_Data(ricTransfert.getDataOraRitornoDate(), numeroOre);
				if( richMediaAutista != null ) {
					String urlTxtSms = "";
					if( smsDinamico.getTipoMessaggio().equals(TIPO_MESSAGGIO_SMS_AVVISO_CORSA_RITORNO_CLIENTE_24_ORE) ) {
						urlTxtSms = InviaEmail.DammiUrl_InfoCorsa_Cliente(ricTransfert, null);
					}else if( smsDinamico.getTipoMessaggio().equals(TIPO_MESSAGGIO_SMS_AVVISO_CORSA_RITORNO_AUTISTA_24_ORE)
							|| smsDinamico.getTipoMessaggio().equals(TIPO_MESSAGGIO_SMS_AVVISO_CORSA_RITORNO_AUTISTA_48_ORE) ) {
						urlTxtSms = HomeUtil_Sms_Email.UrlSmsCorsaDisponibileAutista(null, richMediaAutista);
					}
					String telefono = "";
					if( smsDinamico.getTipoMessaggio().equals(TIPO_MESSAGGIO_SMS_AVVISO_CORSA_ANDATA_CLIENTE_24_ORE) ) {
						telefono = (ricTransfert.getTelefonoPasseggero() != null && !ricTransfert.getTelefonoPasseggero().equals("")) ? ricTransfert.getTelefonoPasseggero() : ricTransfert.getUser().getPhoneNumber(); 
						
					}else if( smsDinamico.getTipoMessaggio().equals(TIPO_MESSAGGIO_SMS_AVVISO_CORSA_ANDATA_AUTISTA_24_ORE)
							|| smsDinamico.getTipoMessaggio().equals(TIPO_MESSAGGIO_SMS_AVVISO_CORSA_ANDATA_AUTISTA_48_ORE) ) {
						telefono = richMediaAutista.getAutista().getUser().getPhoneNumber(); 
					}
					String testoSms = ApplicationMessagesUtil.DammiMessageSource("sms.avviso.ricordo.corsa", new String[]{ numeroOre.toString(), urlTxtSms }, null);
					smsDinamico.setTestoMessaggio(testoSms);
					smsDinamico.setNumeroDestinatario( telefono );
					smsDinamico.setDataInvio(date);
					smsDinamico = listaInvioSmsDao.saveListaInvioSms( smsDinamico );
					// controllo se la data prelevamento è inferiore di adesso && la corsa è approvata && Un autista autista l'ha preso in carico
					if( date.getTime() < new Date().getTime() && ricTransfert.getApprovazioneRitorno() != Constants.NON_APPROVATA && richMediaAutista != null) {
						esitoInvio = InvioSms.Lancia_SMS_Gateway(servletContext, smsDinamico);
					}
				}else if( date.getTime() < new Date().getTime() && ricTransfert.getApprovazioneRitorno() == Constants.NON_APPROVATA && richMediaAutista == null) {
					/* listaInvioSmsDao.remove(smsDinamico); */ 
					//System.out.println("DEVO RIMUOVERE SMS: "+smsDinamico.getId());
				}
			}
			
		}
		return esitoInvio;
	}
	
	
	public static boolean InviaSmsDinamico_AvvisoContattoDisponibile(ServletContext servletContext, ListaInvioEmailSms smsDinamico) throws AddressException, AuthenticationFailedException, SendFailedException, MessagingException, IOException {
		boolean esitoInvio = false;
		// sms.avviso.contatto.disponibile=Da questo momento puoi visualizzazione Il Contatto Autista alla Pagina: {0}
		// SMS per CLIENTE
		if( smsDinamico.getTipoMessaggio().equals(TIPO_MESSAGGIO_SMS_AVVISO_CORSA_ANDATA_CLIENTE_CONTATTO_AUTISTA_DISPONIBLE )) {
			Long numOreNumeroTelefonovisualzAutistaCliente = gestioneApplicazioneDao.getName("NUM_ORE_NUMERO_TELEFONO_VISUALIZZABILE_AUTISTA_CLIENTE").getValueNumber();
			RicercaTransfert ricTransfert = ricercaTransfertDao.get( smsDinamico.getRicercaTransfert().getId() );
			RichiestaMediaAutista richMediaAutista = ricercaTransfertDao.getRichiestaMediaAutista_AutistaAssegnatoCorsaMedia(ricTransfert.getId());
			Date date = DateUtil.TogliOre_a_Data(ricTransfert.getDataOraPrelevamentoDate(), numOreNumeroTelefonovisualzAutistaCliente.intValue());
			if( richMediaAutista != null ) {
				String testoSms = ApplicationMessagesUtil.DammiMessageSource("sms.avviso.contatto.disponibile.autista", new String[]{
						InviaEmail.DammiUrl_InfoCorsa_Cliente(ricTransfert, null) }, null);
				String telefono = (ricTransfert.getTelefonoPasseggero() != null && Telefono_Prefisso_e_Formato.NumeroTelefonicoValido(ricTransfert.getTelefonoPasseggero())) 
						? ricTransfert.getTelefonoPasseggero() : ricTransfert.getUser().getPhoneNumber(); 
				smsDinamico.setTestoMessaggio( testoSms );
				smsDinamico.setNumeroDestinatario( telefono );
				smsDinamico.setDataInvio(date);
				smsDinamico = listaInvioSmsDao.saveListaInvioSms( smsDinamico );
				// controllo se la data prelevamento è inferiore di adesso && la corsa è approvata && Un autista autista l'ha preso in carico
				if( date.getTime() < new Date().getTime() && ricTransfert.getApprovazioneAndata() != Constants.NON_APPROVATA) {
					esitoInvio = InvioSms.Lancia_SMS_Gateway(servletContext, smsDinamico);
				}
			}else if( date.getTime() < new Date().getTime() && ricTransfert.getApprovazioneAndata() == Constants.NON_APPROVATA) {
				/* listaInvioSmsDao.remove(smsDinamico); */ 
				//System.out.println("RIMUOVO SMS: "+smsDinamico.getId());
			}
			
		} else if( smsDinamico.getTipoMessaggio().equals(TIPO_MESSAGGIO_SMS_AVVISO_CORSA_RITORNO_CLIENTE_CONTATTO_AUTISTA_DISPONIBLE )) {
			Long numOreNumeroTelefonovisualzAutistaCliente = gestioneApplicazioneDao.getName("NUM_ORE_NUMERO_TELEFONO_VISUALIZZABILE_AUTISTA_CLIENTE").getValueNumber();
			RicercaTransfert ricTransfert = ricercaTransfertDao.get( smsDinamico.getRicercaTransfert().getId() );
			if( ricTransfert.isRitorno() ) {
				RichiestaMediaAutista richMediaAutista = ricercaTransfertDao.getRichiestaMediaAutista_AutistaAssegnatoCorsaMedia(ricTransfert.getId());
				Date date = DateUtil.TogliOre_a_Data(ricTransfert.getDataOraRitornoDate() , numOreNumeroTelefonovisualzAutistaCliente.intValue());
				
				if( richMediaAutista != null ) {
					String testoSms = ApplicationMessagesUtil.DammiMessageSource("sms.avviso.contatto.disponibile.autista", new String[]{
							InviaEmail.DammiUrl_InfoCorsa_Cliente(ricTransfert, null) }, null);
					String telefono = (ricTransfert.getTelefonoPasseggero() != null && Telefono_Prefisso_e_Formato.NumeroTelefonicoValido(ricTransfert.getTelefonoPasseggero())) 
							? ricTransfert.getTelefonoPasseggero() : ricTransfert.getUser().getPhoneNumber(); 
					smsDinamico.setTestoMessaggio(testoSms);
					smsDinamico.setNumeroDestinatario( telefono );
					smsDinamico.setDataInvio(date);
					smsDinamico = listaInvioSmsDao.saveListaInvioSms( smsDinamico );
					// controllo se la data prelevamento è inferiore di adesso && la corsa è approvata && Un autista autista l'ha preso in carico
					if( date.getTime() < new Date().getTime() && ricTransfert.getApprovazioneRitorno() != Constants.NON_APPROVATA) {
						esitoInvio = InvioSms.Lancia_SMS_Gateway(servletContext, smsDinamico);
					}
				}else if( date.getTime() < new Date().getTime() && ricTransfert.getApprovazioneRitorno() == Constants.NON_APPROVATA) {
					/* listaInvioSmsDao.remove(smsDinamico); */ 
					//System.out.println("RIMUOVO SMS: "+smsDinamico.getId());
				}
			}
		}
		// SMS per AUTISTA
		if( smsDinamico.getTipoMessaggio().equals(TIPO_MESSAGGIO_SMS_AVVISO_CORSA_ANDATA_AUTISTA_CONTATTO_CLIENTE_DISPONIBLE )) {
			Long numOreNumeroTelefonovisualzAutistaCliente = gestioneApplicazioneDao.getName("NUM_ORE_NUMERO_TELEFONO_VISUALIZZABILE_AUTISTA_CLIENTE").getValueNumber();
			RicercaTransfert ricTransfert = ricercaTransfertDao.get( smsDinamico.getRicercaTransfert().getId() );
			RichiestaMediaAutista richMediaAutista = ricercaTransfertDao.getRichiestaMediaAutista_AutistaAssegnatoCorsaMedia(ricTransfert.getId());
			Date date = DateUtil.TogliOre_a_Data(ricTransfert.getDataOraPrelevamentoDate(), numOreNumeroTelefonovisualzAutistaCliente.intValue());
			if( richMediaAutista != null ) {
				String testoSms = ApplicationMessagesUtil.DammiMessageSource("sms.avviso.contatto.disponibile.cliente", new String[]{
						HomeUtil_Sms_Email.UrlSmsCorsaDisponibileAutista(null, richMediaAutista) }, null);
				String telefono = richMediaAutista.getAutista().getUser().getPhoneNumber(); 
				smsDinamico.setTestoMessaggio(testoSms);
				smsDinamico.setNumeroDestinatario( telefono );
				smsDinamico.setDataInvio(date);
				smsDinamico = listaInvioSmsDao.saveListaInvioSms( smsDinamico );
				// controllo se la data prelevamento è inferiore di adesso && la corsa è approvata && Un autista autista l'ha preso in carico
				if( date.getTime() < new Date().getTime() && ricTransfert.getApprovazioneAndata() != Constants.NON_APPROVATA) {
					esitoInvio = InvioSms.Lancia_SMS_Gateway(servletContext, smsDinamico);
				}
			}else if( date.getTime() < new Date().getTime() && ricTransfert.getApprovazioneAndata() == Constants.NON_APPROVATA) {
				/* listaInvioSmsDao.remove(smsDinamico); */ 
				//System.out.println("RIMUOVO SMS: "+smsDinamico.getId());
			}
		}else if( smsDinamico.getTipoMessaggio().equals(TIPO_MESSAGGIO_SMS_AVVISO_CORSA_RITORNO_AUTISTA_CONTATTO_CLIENTE_DISPONIBLE )) {
			Long numOreNumeroTelefonovisualzAutistaCliente = gestioneApplicazioneDao.getName("NUM_ORE_NUMERO_TELEFONO_VISUALIZZABILE_AUTISTA_CLIENTE").getValueNumber();
			RicercaTransfert ricTransfert = ricercaTransfertDao.get( smsDinamico.getRicercaTransfert().getId() );
			if( ricTransfert.isRitorno() ) {
				RichiestaMediaAutista richMediaAutista = ricercaTransfertDao.getRichiestaMediaAutista_AutistaAssegnatoCorsaMedia(ricTransfert.getId());
				Date date = DateUtil.TogliOre_a_Data(ricTransfert.getDataOraRitornoDate(), numOreNumeroTelefonovisualzAutistaCliente.intValue());
				if( richMediaAutista != null ) {
					String testoSms = ApplicationMessagesUtil.DammiMessageSource("sms.avviso.contatto.disponibile.cliente", new String[]{
							HomeUtil_Sms_Email.UrlSmsCorsaDisponibileAutista(null, richMediaAutista) }, null);
					String telefono = richMediaAutista.getAutista().getUser().getPhoneNumber(); 
					smsDinamico.setTestoMessaggio(testoSms);
					smsDinamico.setNumeroDestinatario( telefono );
					smsDinamico.setDataInvio(date);
					smsDinamico = listaInvioSmsDao.saveListaInvioSms( smsDinamico );
					// controllo se la data prelevamento è inferiore di adesso && la corsa è approvata && Un autista autista l'ha preso in carico
					if( date.getTime() < new Date().getTime() && ricTransfert.getApprovazioneRitorno() != Constants.NON_APPROVATA) {
						esitoInvio = InvioSms.Lancia_SMS_Gateway(servletContext, smsDinamico);
					}
				}else if( date.getTime() < new Date().getTime() && ricTransfert.getApprovazioneRitorno() == Constants.NON_APPROVATA) {
					/* listaInvioSmsDao.remove(smsDinamico); */ 
					//System.out.println("RIMUOVO SMS: "+smsDinamico.getId());
				}
			}
		}
		return esitoInvio;
	}
	
	
}
