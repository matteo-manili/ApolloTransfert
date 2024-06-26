package com.apollon.webapp.util.controller.homeUtente;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.apollon.Constants;
import com.apollon.dao.RicercaTransfertDao;
import com.apollon.model.Autoveicolo;
import com.apollon.model.RichiestaMediaAutista;
import com.apollon.model.RichiestaMediaAutistaAutoveicolo;
import com.apollon.model.RichiestaAutistaParticolare;
import com.apollon.webapp.util.ApplicationUtils;
import com.apollon.webapp.util.ControllerUtil;
import com.apollon.webapp.util.bean.InfoCorsaAutista;
import com.apollon.webapp.util.bean.InfoCorsaCliente;
import com.apollon.webapp.util.controller.documenti.DocumentiInfoUtil;
import com.apollon.webapp.util.controller.ritardi.RitardiUtil;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public class HomeUtenteUtil extends ApplicationUtils{
	private static RicercaTransfertDao ricercaTransfertDao = (RicercaTransfertDao) contextDao.getBean("RicercaTransfertDao");

	
	/**
	 * mi ritorna true se i documenti non sono inseriti
	 */
	public static boolean ControlloDocumentiInseriti(DocumentiInfoUtil docUtil) {
		if((docUtil.documentiCompletatiInclusoContratto == false 
				&& docUtil.documentiCompletatiEsclusoContratto == true
				&& docUtil.documentiApprovatiEsclusoContratto == true)
				|| (docUtil.documentiCompletatiInclusoContratto == false 
						&& docUtil.documentiCompletatiEsclusoContratto == false
						&& docUtil.documentiApprovatiEsclusoContratto == false)){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Object Attribute per ModelAndView
	 */
	public static ModelAndView Add_PanelInfoCorseCliente(ModelAndView mav, HttpServletRequest request) {
		String linkModificaDatiFattura = (CheckAmbienteVenditore(request.getServletContext())) ? 
				ApplicationMessagesUtil.DammiMessageSource("w3.domain.ncctransferonline.name", request.getLocale()) : 
					ApplicationMessagesUtil.DammiMessageSource("w3.domain.apollotransfert.name", request.getLocale());
		mav.addObject("linkModificaDatiFattura", "https://"+ linkModificaDatiFattura +"/userform");
        mav.addObject("textLinkModificaDatiFattura", ApplicationMessagesUtil.DammiMessageSource("menu.user", request.getLocale()));
        mav.addObject("maxOreDisdettaCliente", gestioneApplicazioneDao.getName("NUM_ORE_TRA_ORA_PRELEV_AD_ADESSO_DISDETTA_AUTISTA_MEDIO").getValueNumber());
        mav = RitardiUtil.AddAttribute_Ritardi(mav);
        return mav;
	}
	
	public static List<Autoveicolo> AutoveicoliUtilizzabiliAutistaMedioList(long idRicTransfert, long idUser) throws Exception {
		List<RichiestaMediaAutista> corseDisponibiliMedioList = ricercaTransfertDao.getAutoveicoliUtilizzabiliAutistaMedio(idRicTransfert, idUser);
		List<RichiestaMediaAutistaAutoveicolo> richAutistMedioAutoList = new ArrayList<RichiestaMediaAutistaAutoveicolo>();
		richAutistMedioAutoList.addAll( corseDisponibiliMedioList.get(0).getRichiestaMediaAutistaAutoveicolo() );
		List<Autoveicolo> autoveicoliUtilizzabili = new ArrayList<Autoveicolo>();
		for(RichiestaMediaAutistaAutoveicolo richAutistMedioAutoList_ite: richAutistMedioAutoList){
			autoveicoliUtilizzabili.add(richAutistMedioAutoList_ite.getAutoveicolo());
		}
		return autoveicoliUtilizzabili;
	}

	
	// CORSE DISPONIBILI PER AUTISTA
	public static List<InfoCorsaAutista> GetInfoCorseMedieDisponibiliAutistaList(List<RichiestaMediaAutista> corseDisponibiliMedioList, HttpServletRequest request) throws Exception {
		List<InfoCorsaAutista> infoCorsaAutistaList = new ArrayList<InfoCorsaAutista>();
		InfoCorsaAutista infoCorsaAutista;
		List<Long> idCorseInserite = new ArrayList<Long>();
		//List<String> listAutoveicoliUtilizzabiliPerAutistaMedio = GetAutoveicoliUtilizzabiliAutistaMedioList()
		for(RichiestaMediaAutista corseDisponibiliMedio_ite: corseDisponibiliMedioList){
			long idRicTransfert = corseDisponibiliMedio_ite.getRichiestaMedia().getRicercaTransfert().getId();
			if(!idCorseInserite.contains(corseDisponibiliMedio_ite.getRichiestaMedia().getRicercaTransfert().getId()) && ricercaTransfertDao.getCorsaMediaDisponibile_by_idRicerca( idRicTransfert )){
				infoCorsaAutista = new InfoCorsaAutista();
				infoCorsaAutista.setTipoCorsa(Constants.SERVIZIO_STANDARD); // se di tipo richiesta Medio oppure se di tipo richiesta Particolare
				infoCorsaAutista.setRicTransfert( corseDisponibiliMedio_ite.getRichiestaMedia().getRicercaTransfert() );
				infoCorsaAutista.setClasseAutoveicoloScelta(corseDisponibiliMedio_ite.getRichiestaMedia().getClasseAutoveicolo());
				infoCorsaAutista.setTariffaPerKm(corseDisponibiliMedio_ite.getRichiestaMedia().getTariffaPerKm());
				infoCorsaAutista.setPrezzoTotaleCliente(corseDisponibiliMedio_ite.getRichiestaMedia().getPrezzoTotaleCliente());
				infoCorsaAutista.setPercentualeServizio( 
						new String(corseDisponibiliMedio_ite.getRichiestaMedia().getRicercaTransfert().getPercentualeServizioRichiestaMediaScelta() + "% -"+corseDisponibiliMedio_ite.getPrezzoCommissioneServizio() )+"€");
				infoCorsaAutista.setPrezzoTotaleAutista( corseDisponibiliMedio_ite.getPrezzoTotaleAutista() );
				infoCorsaAutista.setNomeCliente( corseDisponibiliMedio_ite.getRichiestaMedia().getRicercaTransfert().getUser().getUsername() );
				infoCorsaAutista.setTelefonoCliente( corseDisponibiliMedio_ite.getRichiestaMedia().getRicercaTransfert().getUser().getPhoneNumber() );
				// prima gli passavo long idUser direttamente dalla servlet
				List<Autoveicolo> autoveicoliUtilizzabili = 
						AutoveicoliUtilizzabiliAutistaMedioList(corseDisponibiliMedio_ite.getRichiestaMedia().getRicercaTransfert().getId(), corseDisponibiliMedio_ite.getAutista().getUser().getId());
				infoCorsaAutista.setAutoveicolo( autoveicoliUtilizzabili );
				if( corseDisponibiliMedio_ite.getRichiestaMedia().getRicercaTransfert().getDataOraPrelevamentoDate().getTime() < new Date().getTime() ){
					infoCorsaAutista.setStatusEseguitoAndata(true);
				}else{
					infoCorsaAutista.setStatusEseguitoAndata(false);
				}
				if(corseDisponibiliMedio_ite.getRichiestaMedia().getRicercaTransfert().isRitorno() && corseDisponibiliMedio_ite.getRichiestaMedia().getRicercaTransfert().getDataOraRitornoDate() != null){
					if( corseDisponibiliMedio_ite.getRichiestaMedia().getRicercaTransfert().getDataOraRitornoDate().getTime() < new Date().getTime() ){
						infoCorsaAutista.setStatusEseguitoRitorno(true);
					}else{
						infoCorsaAutista.setStatusEseguitoRitorno(false);
					}
				}
				String linkPrenotaCorsaMedio = ControllerUtil.getAppURL(request) + "/"+Constants.URL_PRENOTA_CORSA+"?token="+corseDisponibiliMedio_ite.getTokenAutista();
				infoCorsaAutista.setLinkPrenotaCorsaMedio(linkPrenotaCorsaMedio);
				infoCorsaAutistaList.add(infoCorsaAutista);
				idCorseInserite.add( corseDisponibiliMedio_ite.getRichiestaMedia().getRicercaTransfert().getId() );
			}else{
				//Corsa non disponibile, gia prenotata da un altro autista
			}
		}
		Collections.sort(infoCorsaAutistaList);
		return infoCorsaAutistaList;
	}

	
	// CORSE AUTISTA
	public static List<InfoCorsaAutista> GetInfoCorseAutistaList(List<RichiestaAutistaParticolare> ricTransfertParticList, List<RichiestaMediaAutista> corseAutistaRicTransfertMedioList, 
			HttpServletRequest request) throws Exception{
		List<InfoCorsaAutista> infoCorsaAutistaList = new ArrayList<InfoCorsaAutista>();
		InfoCorsaAutista infoCorsaAutista;
		//---- particolare
		for(RichiestaAutistaParticolare ricTransfertParticList_ite: ricTransfertParticList){
			infoCorsaAutista = new InfoCorsaAutista();
			infoCorsaAutista.setTipoCorsa(Constants.SERVIZIO_PARTICOLARE); // se di tipo richiesta Medio oppure se di tipo richiesta Particolare
			infoCorsaAutista.setRicTransfert( ricTransfertParticList_ite.getRicercaTransfert() );
			infoCorsaAutista.setClasseAutoveicoloScelta(ricTransfertParticList_ite.getClasseAutoveicoloScelta());
			infoCorsaAutista.setPrezzoTotaleCliente(ricTransfertParticList_ite.getPrezzoTotaleCliente());
			infoCorsaAutista.setPercentualeServizio( 
					new String(ricTransfertParticList_ite.getPercentualeServizio().toString() + "% -"+ricTransfertParticList_ite.getPrezzoCommissioneServizio() )+"€");
			infoCorsaAutista.setPrezzoTotaleAutista( ricTransfertParticList_ite.getPrezzoTotaleAutista() );
			infoCorsaAutista.setNomeCliente( ricTransfertParticList_ite.getRicercaTransfert().getUser().getUsername() );
			infoCorsaAutista.setTelefonoCliente( ricTransfertParticList_ite.getRicercaTransfert().getUser().getPhoneNumber() );
			List<Autoveicolo> autoveicoloUtilizzabile = new ArrayList<Autoveicolo>();
			autoveicoloUtilizzabile.add(ricTransfertParticList_ite.getAutoveicolo());
			infoCorsaAutista.setAutoveicolo( autoveicoloUtilizzabile );
			if( ricTransfertParticList_ite.getRicercaTransfert().getDataOraPrelevamentoDate().getTime() < new Date().getTime() ){
				infoCorsaAutista.setStatusEseguitoAndata(true);
			}else{
				infoCorsaAutista.setStatusEseguitoAndata(false);
			}
			if(ricTransfertParticList_ite.getRicercaTransfert().isRitorno() && ricTransfertParticList_ite.getRicercaTransfert().getDataOraRitornoDate() != null){
				if( ricTransfertParticList_ite.getRicercaTransfert().getDataOraRitornoDate().getTime() < new Date().getTime() ){
					infoCorsaAutista.setStatusEseguitoRitorno(true);
				}else{
					infoCorsaAutista.setStatusEseguitoRitorno(false);
				}
			}
			infoCorsaAutistaList.add(infoCorsaAutista);
		}
		
		
		//---- medio
		for(RichiestaMediaAutista ricTransfertMediocList_ite: corseAutistaRicTransfertMedioList) {
			infoCorsaAutista = new InfoCorsaAutista();
			infoCorsaAutista.setTipoCorsa(Constants.SERVIZIO_STANDARD); // se di tipo richiesta Medio oppure se di tipo richiesta Particolare
			infoCorsaAutista.setRicTransfert( ricTransfertMediocList_ite.getRichiestaMedia().getRicercaTransfert() );
			infoCorsaAutista.setClasseAutoveicoloScelta(ricTransfertMediocList_ite.getRichiestaMedia().getClasseAutoveicolo());
			infoCorsaAutista.setTariffaPerKm(ricTransfertMediocList_ite.getRichiestaMedia().getTariffaPerKm());
			infoCorsaAutista.setPrezzoTotaleCliente(ricTransfertMediocList_ite.getRichiestaMedia().getPrezzoTotaleCliente());
			infoCorsaAutista.setPercentualeServizio( 
					new String(ricTransfertMediocList_ite.getRichiestaMedia().getRicercaTransfert().getPercentualeServizioRichiestaMediaScelta() + "% -"+ricTransfertMediocList_ite.getPrezzoCommissioneServizio() )+"€");
			infoCorsaAutista.setPrezzoTotaleAutista( ricTransfertMediocList_ite.getPrezzoTotaleAutista() );
			infoCorsaAutista.setNomeCliente( ricTransfertMediocList_ite.getRichiestaMedia().getRicercaTransfert().getUser().getUsername() );
			infoCorsaAutista.setTelefonoCliente( ricTransfertMediocList_ite.getRichiestaMedia().getRicercaTransfert().getUser().getPhoneNumber() );
			// prima gli passavo long idUser direttamente dalla servlet
			List<Autoveicolo> autoveicoliUtilizzabili = 
					AutoveicoliUtilizzabiliAutistaMedioList(ricTransfertMediocList_ite.getRichiestaMedia().getRicercaTransfert().getId(), ricTransfertMediocList_ite.getAutista().getUser().getId());
			infoCorsaAutista.setAutoveicolo( autoveicoliUtilizzabili );
			if( ricTransfertMediocList_ite.getRichiestaMedia().getRicercaTransfert().getDataOraPrelevamentoDate().getTime() < new Date().getTime() ){
				infoCorsaAutista.setStatusEseguitoAndata(true);
			}else{
				infoCorsaAutista.setStatusEseguitoAndata(false);
			}
			if(ricTransfertMediocList_ite.getRichiestaMedia().getRicercaTransfert().isRitorno() && ricTransfertMediocList_ite.getRichiestaMedia().getRicercaTransfert().getDataOraRitornoDate() != null){
				if( ricTransfertMediocList_ite.getRichiestaMedia().getRicercaTransfert().getDataOraRitornoDate().getTime() < new Date().getTime() ){
					infoCorsaAutista.setStatusEseguitoRitorno(true);
				}else{
					infoCorsaAutista.setStatusEseguitoRitorno(false);
				}
			}
			String linkPrenotaCorsaMedio = ControllerUtil.getAppURL(request) + "/"+Constants.URL_PRENOTA_CORSA+"?token="+ricTransfertMediocList_ite.getTokenAutista();
			infoCorsaAutista.setLinkPrenotaCorsaMedio(linkPrenotaCorsaMedio);
			infoCorsaAutistaList.add(infoCorsaAutista);
		}
		Collections.sort(infoCorsaAutistaList);
		return infoCorsaAutistaList;
	}
	
	
	// CORSE CLIENTE
	public static List<InfoCorsaCliente> GetInfoCorseClienteList(List<RichiestaAutistaParticolare> ricTransfertParticList, List<RichiestaMediaAutista> ricTransfertMedioList) 
			throws Exception {
		List<InfoCorsaCliente> infoCorsaClienteList = new ArrayList<InfoCorsaCliente>();
		InfoCorsaCliente infoCorsaCliente;
		List<Long> idCorseInserite = new ArrayList<Long>();
		for(RichiestaMediaAutista ricTransfertMedioList_ite: ricTransfertMedioList) {
			infoCorsaCliente = new InfoCorsaCliente();
			RichiestaMediaAutista richAutistaMedioSmsCorsaConfermata = ricercaTransfertDao
					.getCorseClienteRichAutistaMedioVerificaInvioSmsCorsaConfermata( ricTransfertMedioList_ite.getRichiestaMedia().getRicercaTransfert().getId() );
			// QUI CONTROLLO SE LA CORSA è STATA PRENOTATA DA UN AUTISTA
			if(!idCorseInserite.contains(ricTransfertMedioList_ite.getRichiestaMedia().getRicercaTransfert().getId()) && richAutistaMedioSmsCorsaConfermata != null
					&& ricTransfertMedioList_ite.isInvioSmsCorsaConfermata()){
				infoCorsaCliente.setTipoCorsa(Constants.SERVIZIO_STANDARD); // se di tipo richiesta Medio oppure se di tipo richiesta Particolare
				infoCorsaCliente.setRicTransfert( ricTransfertMedioList_ite.getRichiestaMedia().getRicercaTransfert() );
				infoCorsaCliente.setClasseAutoveicoloScelta(ricTransfertMedioList_ite.getRichiestaMedia().getClasseAutoveicolo());
				infoCorsaCliente.setTariffaPerKm(ricTransfertMedioList_ite.getRichiestaMedia().getTariffaPerKm());
				infoCorsaCliente.setMaggiorazioneNotturna(ricTransfertMedioList_ite.getRichiestaMedia().getMaggiorazioneNotturna());
				infoCorsaCliente.setPrezzo(ricTransfertMedioList_ite.getRichiestaMedia().getPrezzoTotaleAutista());
				infoCorsaCliente.setPrezzoTotaleCliente( ricTransfertMedioList_ite.getRichiestaMedia().getPrezzoTotaleCliente() );
				infoCorsaCliente.setAutista( ricTransfertMedioList_ite.getAutista() );
				//infoCorsaCliente.setAutoveicolo( ricTransfertMedioList_ite.getAutoveicolo() );
				if( ricTransfertMedioList_ite.getRichiestaMedia().getRicercaTransfert().getDataOraPrelevamentoDate().getTime() < new Date().getTime() ){
					infoCorsaCliente.setStatusEseguitoAndata(true);
				}else{
					infoCorsaCliente.setStatusEseguitoAndata(false);
				}
				if(ricTransfertMedioList_ite.getRichiestaMedia().getRicercaTransfert().isRitorno() && ricTransfertMedioList_ite.getRichiestaMedia().getRicercaTransfert().getDataOraRitornoDate() != null){
					if( ricTransfertMedioList_ite.getRichiestaMedia().getRicercaTransfert().getDataOraRitornoDate().getTime() < new Date().getTime() ){
						infoCorsaCliente.setStatusEseguitoRitorno(true);
					}else{
						infoCorsaCliente.setStatusEseguitoRitorno(false);
					}
				}
				infoCorsaClienteList.add(infoCorsaCliente);
				idCorseInserite.add( ricTransfertMedioList_ite.getRichiestaMedia().getRicercaTransfert().getId() );
				
			}else if(!idCorseInserite.contains(ricTransfertMedioList_ite.getRichiestaMedia().getRicercaTransfert().getId()) ) {
				infoCorsaCliente.setTipoCorsa(Constants.SERVIZIO_STANDARD); // se di tipo richiesta Medio oppure se di tipo richiesta Particolare
				infoCorsaCliente.setRicTransfert( ricTransfertMedioList_ite.getRichiestaMedia().getRicercaTransfert() );
				infoCorsaCliente.setClasseAutoveicoloScelta(ricTransfertMedioList_ite.getRichiestaMedia().getClasseAutoveicolo());
				infoCorsaCliente.setTariffaPerKm(ricTransfertMedioList_ite.getRichiestaMedia().getTariffaPerKm());
				infoCorsaCliente.setMaggiorazioneNotturna(ricTransfertMedioList_ite.getRichiestaMedia().getMaggiorazioneNotturna());
				infoCorsaCliente.setPrezzo(ricTransfertMedioList_ite.getRichiestaMedia().getPrezzoTotaleAutista());
				infoCorsaCliente.setPrezzoTotaleCliente( ricTransfertMedioList_ite.getRichiestaMedia().getPrezzoTotaleCliente() );
				infoCorsaCliente.setAutista( null );
				infoCorsaCliente.setAutoveicolo( null );
				if( ricTransfertMedioList_ite.getRichiestaMedia().getRicercaTransfert().getDataOraPrelevamentoDate().getTime() < new Date().getTime() ){
					infoCorsaCliente.setStatusEseguitoAndata(true);
				}else{
					infoCorsaCliente.setStatusEseguitoAndata(false);
				}
				if(ricTransfertMedioList_ite.getRichiestaMedia().getRicercaTransfert().isRitorno() && ricTransfertMedioList_ite.getRichiestaMedia().getRicercaTransfert().getDataOraRitornoDate() != null){
					if( ricTransfertMedioList_ite.getRichiestaMedia().getRicercaTransfert().getDataOraRitornoDate().getTime() < new Date().getTime() ){
						infoCorsaCliente.setStatusEseguitoRitorno(true);
					}else{
						infoCorsaCliente.setStatusEseguitoRitorno(false);
					}
				}
				infoCorsaClienteList.add(infoCorsaCliente);
				idCorseInserite.add( ricTransfertMedioList_ite.getRichiestaMedia().getRicercaTransfert().getId() );
			}
		}
		
		// corse particolari
		for(RichiestaAutistaParticolare ricTransfertParticList_ite: ricTransfertParticList) {
			infoCorsaCliente = new InfoCorsaCliente();
			infoCorsaCliente.setTipoCorsa(Constants.SERVIZIO_PARTICOLARE); // se di tipo richiesta Medio oppure se di tipo richiesta Particolare
			infoCorsaCliente.setRicTransfert( ricTransfertParticList_ite.getRicercaTransfert() );
			infoCorsaCliente.setClasseAutoveicoloScelta(ricTransfertParticList_ite.getClasseAutoveicoloScelta());
			infoCorsaCliente.setRimborsoCliente(ricTransfertParticList_ite.getRimborsoCliente());
			infoCorsaCliente.setPrezzoTotaleCliente( ricTransfertParticList_ite.getPrezzoTotaleCliente() );
			infoCorsaCliente.setAutista( ricTransfertParticList_ite.getAutoveicolo().getAutista() );
			infoCorsaCliente.setAutoveicolo( ricTransfertParticList_ite.getAutoveicolo() );
			if( ricTransfertParticList_ite.getRicercaTransfert().getDataOraPrelevamentoDate().getTime() < new Date().getTime() ){
				infoCorsaCliente.setStatusEseguitoAndata(true);
			}else{
				infoCorsaCliente.setStatusEseguitoAndata(false);
			}
			if(ricTransfertParticList_ite.getRicercaTransfert().isRitorno() && ricTransfertParticList_ite.getRicercaTransfert().getDataOraRitornoDate() != null){
				if( ricTransfertParticList_ite.getRicercaTransfert().getDataOraRitornoDate().getTime() < new Date().getTime() ){
					infoCorsaCliente.setStatusEseguitoRitorno(true);
				}else{
					infoCorsaCliente.setStatusEseguitoRitorno(false);
				}
			}
			infoCorsaClienteList.add(infoCorsaCliente);
		}
		Collections.sort(infoCorsaClienteList);
		return infoCorsaClienteList;
    }
}
