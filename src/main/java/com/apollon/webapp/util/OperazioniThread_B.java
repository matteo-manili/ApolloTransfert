package com.apollon.webapp.util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.VelocityEngine;
import com.apollon.Constants;
import com.apollon.dao.RichiestaAutistaMedioDao;
import com.apollon.dao.RichiestaAutistaParticolareDao;
import com.apollon.model.RicercaTransfert;
import com.apollon.model.RichiestaAutistaParticolare;
import com.apollon.model.RichiestaMediaAutista;
import com.apollon.webapp.util.controller.home.HomeUtil_Sms_Email;
import com.apollon.webapp.util.sms.InvioSms;
import com.apollon.webapp.util.sms.Invio_Email_Sms_UTIL;

public class OperazioniThread_B extends ApplicationUtils implements Runnable {
	
	private static final Log log = LogFactory.getLog(OperazioniThread_B.class);
	private  RichiestaAutistaMedioDao richiestaAutistaMedioDao = (RichiestaAutistaMedioDao) contextDao.getBean("RichiestaAutistaMedioDao");
	private  RichiestaAutistaParticolareDao richiestaAutistaParticolareDao = (RichiestaAutistaParticolareDao) contextDao.getBean("RichiestaAutistaParticolareDao");
	
	public  RicercaTransfert ricTransfert;
	public  VelocityEngine velocityEngine;	
	public  HttpServletRequest_Util httpServletRequest_Util;
	//public HttpServletRequest request;
	
	public OperazioniThread_B(RicercaTransfert ricTransfert, HttpServletRequest_Util httpServletRequest_Util, VelocityEngine velocityEngine) {
		this.ricTransfert = ricTransfert;
		this.httpServletRequest_Util = httpServletRequest_Util;
		this.velocityEngine = velocityEngine;
	}
	
	
	@Override
	public void run() {
		if( ricTransfert.getTipoServizio().equals(Constants.SERVIZIO_PARTICOLARE) || ricTransfert.getTipoServizio().equals(Constants.SERVIZIO_MULTIPLO) ) {
			log.debug("InviaSmsEmailPreventiviAutisti run()");
			log.debug("httpServletRequestWrapper: "+httpServletRequest_Util.getServerName()+" | "+httpServletRequest_Util.getLocale());
			try {
				List<RichiestaAutistaParticolare> ListaDaOrdinare = new ArrayList<RichiestaAutistaParticolare>();
				ListaDaOrdinare.addAll(ricTransfert.getRichiestaAutistaParticolare());
				List<Object[]> ObjectList = richiestaAutistaParticolareDao.InvioSmsRicevuti_Autisti(ricTransfert.getId());
				for(RichiestaAutistaParticolare ite: ListaDaOrdinare) {
					for(Object[] ite_object: ObjectList){
						BigInteger elemt_0 = (BigInteger) ite_object[0];
						BigInteger elemt_1 = (BigInteger) ite_object[1];
						if(ite.getId() == elemt_0.longValue() ) {
							ite.setTotaleSmsRicevutiAutista( elemt_1.intValue() );
							//System.out.println("elemt_0:"+elemt_0+" | elemt_1:"+elemt_1);
						}
					}
				}
				
				Collections.sort( ListaDaOrdinare, new Comparator<RichiestaAutistaParticolare>() {
					/*
					 * ORDINAMENTO: ORDER BY AUTISTA.AUTO.CLASSEAUTOVEICOLO ASC, AUTISTA.numCorseEseguite ASC, AUTISTA.user.signupDate ASC  ";
					 * importante primo ordinamento classeautoveicolo perché i preventivi devono essere inviati per prima alle auto che 
					 * hanno il numero minore di posti per i passeggeri e casualmente l'id della classeAutoveivolo parte dal più basso per le auto 3 posti
					 */
			        public int compare(RichiestaAutistaParticolare a, RichiestaAutistaParticolare b) {
			        	int c;
			        	Long ValClasseAutoA = a.getAutoveicolo().getClasseAutoveicoloReale().getId();
			        	Long ValClasseAutoB = b.getAutoveicolo().getClasseAutoveicoloReale().getId();
		                c = ValClasseAutoA.compareTo(ValClasseAutoB);
		                if (c == 0){
		                	Integer ValTotaleSmsRicevutiAutistaA = a.getTotaleSmsRicevutiAutista();
				        	Integer ValTotaleSmsRicevutiAutistaB = b.getTotaleSmsRicevutiAutista();
			                c = ValTotaleSmsRicevutiAutistaA.compareTo(ValTotaleSmsRicevutiAutistaB);
			                if (c == 0){
			                	Integer ValNumeroCorseEseguiteA = a.getAutoveicolo().getAutista().getNumCorseEseguite();
					        	Integer ValNumeroCorseEseguiteB = b.getAutoveicolo().getAutista().getNumCorseEseguite();
				                c = ValNumeroCorseEseguiteA.compareTo(ValNumeroCorseEseguiteB);
				                if (c == 0){
					                Date valDataRegistrazioneA = a.getAutoveicolo().getAutista().getUser().getSignupDate();
				                	Date valDateRegistrazioneB = b.getAutoveicolo().getAutista().getUser().getSignupDate();
					                c = valDataRegistrazioneA.compareTo(valDateRegistrazioneB);
				                }
			                }
		                }
		                return c;
			        }
			    });
				int conta = 0; final int NumeroMassimoInvioSmsRichiestaPreventivi = 30; 
				/*
				System.out.println(".------------------------------------------------");
				for(RichiestaAutistaParticolare ite: ListaDaOrdinare ) {
					System.out.println("AUTISTA invio PREVETNIVO:"+ite.getAutoveicolo().getAutista().getUser().getFullName());
				}
				*/
				for(RichiestaAutistaParticolare ite: ListaDaOrdinare) {
					RichiestaAutistaParticolare richParticRicaricato = richiestaAutistaParticolareDao.get( ite.getId() );
					if(conta < NumeroMassimoInvioSmsRichiestaPreventivi) {
						String telefonoDestinatario = richParticRicaricato.getAutoveicolo().getAutista().getUser().getPhoneNumber();
						String testoSms = HomeUtil_Sms_Email.TestoSmsRichiestaPreventivo_Autista(httpServletRequest_Util.getLocale(), richParticRicaricato);
						InvioSms.Crea_SMS_Gateway(ricTransfert, telefonoDestinatario, testoSms, Invio_Email_Sms_UTIL.TIPO_MESSAGGIO_SMS_RAPIDO);
						//System.out.println( "ValTotaleSmsRicevutiAutista: "+ite.getTotaleSmsRicevutiAutista() );
						log.debug("testoSms: "+testoSms );
						richParticRicaricato.setInvioSms(true);
						richParticRicaricato = richiestaAutistaParticolareDao.saveRichiestaAutistaParticolare(richParticRicaricato);
					}
					conta++;
					// INVIO EMAIL
					HomeUtil_Sms_Email.Security_InviaEmailRichiestaPreventivoAutista(richParticRicaricato, httpServletRequest_Util, velocityEngine);
					log.debug("....INVIO UN MESSAGGIO AUTISTA PREVENTIVO....");
					Thread.sleep( TimeUnit.SECONDS.toMillis(30) ); // ASPETTA TOT TEMPO PRIMA DI INVIARE IL PROSSIMO SMS
				}
			}catch(Exception exc) {
				exc.printStackTrace();
			}
		
		}else if( ricTransfert.getTipoServizio().equals(Constants.SERVIZIO_STANDARD) ) {
			log.debug("Invia_Sms_e_Email_CorsaDisponibileAutistiMedio run()");
			List<RichiestaMediaAutista> richiestaMediaAutista = richiestaAutistaMedioDao.getRichiestaAutista_by_RichiestaMediaScelta(ricTransfert.getId());
			try {
				for(int conta=0; conta < richiestaMediaAutista.size(); conta++) {
					// ricarico tutte le informazioni perché è un thread separato dal principale e fa un ciclo ogni 5 minuti. 
					// prima del salvataggio devo ricaricare il recordo dell'autista perché altrimenti sovrascrive eventuali modifiche avvenute da quando inizia il thread.
					// as esempio una assegnazione ad uno autista fatta subito dopo l'acquisto di una corsa.
					RichiestaMediaAutista richiestaAutistaMedioIte_ricaricato = richiestaAutistaMedioDao.get( richiestaMediaAutista.get(conta).getId() );
					while( richiestaAutistaMedioIte_ricaricato.isInvioSms() == false ) { // ripeti il ciclo fino quando isInvioSms diventa true
						String telefonoDestinatario = richiestaAutistaMedioIte_ricaricato.getAutista().getUser().getPhoneNumber();
						String testoSms = HomeUtil_Sms_Email.TestoSmsCorsaDisponbile_Autista(httpServletRequest_Util.getLocale(), richiestaAutistaMedioIte_ricaricato);
						String result = InvioSms.Crea_SMS_Gateway(ricTransfert, telefonoDestinatario, testoSms, Invio_Email_Sms_UTIL.TIPO_MESSAGGIO_SMS_RAPIDO);
						log.debug("testoSms: "+testoSms);
						if(result.equals(Constants.SMS_STATUS_SUCCESS)){
							richiestaAutistaMedioIte_ricaricato.setInvioSms( true );
			        		richiestaAutistaMedioDao.saveRichiestaAutistaMedio( richiestaAutistaMedioIte_ricaricato );
			        	}else if( !ApplicationUtils.CheckAmbienteApolloTransfert_Mondoserver(httpServletRequest_Util) ){
			        		// setto cmq. a true per fare i test
			        		richiestaAutistaMedioIte_ricaricato.setInvioSms( true );
			        		richiestaAutistaMedioDao.saveRichiestaAutistaMedio( richiestaAutistaMedioIte_ricaricato );
			        	}
					}
					// INVIO EMAIL
					HomeUtil_Sms_Email.Security_InviaEmailCorsaDisponibile(richiestaAutistaMedioIte_ricaricato, httpServletRequest_Util, velocityEngine);
					log.debug("....INVIO UN MESSAGGIO AUTISTA DISPONBILE....");
					Thread.sleep( TimeUnit.MINUTES.toMillis(5) ); // ASPETTA TOT TEMPO PRIMA DI INVIARE IL PROSSIMO SMS
				}
			}catch(Exception exc) {
				exc.printStackTrace();
			}
		}
	}



	
	
	
}
