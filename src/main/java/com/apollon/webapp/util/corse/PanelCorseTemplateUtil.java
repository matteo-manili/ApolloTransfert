package com.apollon.webapp.util.corse;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONArray;
import org.json.JSONObject;
import com.apollon.Constants;
import com.apollon.dao.RichiestaAutistaMedioDao;
import com.apollon.dao.RitardiDao;
import com.apollon.dao.SupplementiDao;
import com.apollon.model.RichiestaMediaAutista;
import com.apollon.model.Ritardi;
import com.apollon.model.Supplementi;
import com.apollon.model.RicercaTransfert;
import com.apollon.model.RichiestaAutistaParticolare;
import com.apollon.util.DateUtil;
import com.apollon.webapp.util.ApplicationUtils;
import com.apollon.webapp.util.ControlloDateRicerca;
import com.apollon.webapp.util.bean.AgendaAutista_Autista;
import com.apollon.webapp.util.bean.GestioneCorseMedieAdmin;
import com.apollon.webapp.util.bean.ResultRicerca_Autista_Tariffe.AgendaAutistaScelta;
import com.apollon.webapp.util.controller.autoveicolo.AutoveicoloUtil;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public class PanelCorseTemplateUtil extends ApplicationUtils {
	private  RichiestaAutistaMedioDao richiestaAutistaMedioDao = (RichiestaAutistaMedioDao) contextDao.getBean("RichiestaAutistaMedioDao");
	private  RitardiDao ritardiDao = (RitardiDao) contextDao.getBean("RitardiDao");
	private  SupplementiDao supplementiDao = (SupplementiDao) contextDao.getBean("SupplementiDao");
	protected int corseDaEseguire;
	protected int corseEseguite;
	protected int corseDisponibili;
	
	/**
	 * Mi Ritorna True se le prime 3 lettere coincidono con il parametro
	 * @param token
	 * @param startPartToken
	 * @return
	 */
	public static boolean controllaTipoToken(String token, final String startPartToken) {
		final String TOKEN_MEDIA_OLD = "med"; final String TOKEN_PART_OLD = "par"; // vecchi token string (ancora presenti nel database nei record vecchi)
		if( (startPartToken.equals(Constants.SERVIZIO_MULTIPLO)) && (token.substring(0, Constants.SERVIZIO_MULTIPLO.length()).equals(Constants.SERVIZIO_MULTIPLO) ) ) {
			return true;
		}else if( (startPartToken.equals(Constants.SERVIZIO_PARTICOLARE) || startPartToken.equals(TOKEN_PART_OLD))
				&& (token.substring(0, Constants.SERVIZIO_PARTICOLARE.length()).equals(Constants.SERVIZIO_PARTICOLARE) || token.substring(0, TOKEN_PART_OLD.length()).equals(TOKEN_PART_OLD)) ) {
			return true;
		}else if( (startPartToken.equals(Constants.SERVIZIO_STANDARD) || startPartToken.equals(TOKEN_MEDIA_OLD))
				&& (token.substring(0, Constants.SERVIZIO_STANDARD.length()).equals(Constants.SERVIZIO_STANDARD) || token.substring(0, TOKEN_MEDIA_OLD.length()).equals(TOKEN_MEDIA_OLD)) ) {
				return true;
		}else {
			return false;
		}
	}
	
	protected JSONObject DammiIntestazioneCorsa(long getDataOraPrelevamentoDate, long getDurataConTrafficoValue, String formattedAddressPartenza, 
			String formattedAddressplaceArrivo, String namePartenza, String nameArrivo, JSONObject jSONObject){
		String partenza = ""; String arrivo = "";
		if(formattedAddressPartenza != null && !formattedAddressPartenza.equals("")){
			partenza = formattedAddressPartenza;
		}else{
			partenza = namePartenza;
		}
		if(formattedAddressplaceArrivo != null && !formattedAddressplaceArrivo.equals("")){
			arrivo = formattedAddressplaceArrivo;
		}else{
			arrivo = nameArrivo;
		}
		if( getDataOraPrelevamentoDate > new Date().getTime() ){
			jSONObject.put("colorPanel", "panel-success" );
			jSONObject.put("intestazione", "CORSA DA ESEGUIRE | "+partenza+" → "+arrivo);
		}
		if( getDataOraPrelevamentoDate < new Date().getTime() &&
				getDataOraPrelevamentoDate + 
					TimeUnit.SECONDS.toMillis( getDurataConTrafficoValue) > new Date().getTime()){
			jSONObject.put("colorPanel", "panel-success" );
			jSONObject.put("intestazione", "CORSA IN ESECUZIONE | "+partenza+" → "+arrivo);
			jSONObject.put("blinkeffect", true );
		}
		if( getDataOraPrelevamentoDate < new Date().getTime() &&
				getDataOraPrelevamentoDate + 
					TimeUnit.SECONDS.toMillis( getDurataConTrafficoValue) < new Date().getTime()){
			jSONObject.put("colorPanel", "panel-info" );
			jSONObject.put("intestazione", "CORSA ESEGUITA | "+partenza+" → "+arrivo);
		}
		return jSONObject;
	}
	
	protected JSONObject DammiApprovazione_e_CollapsePanel(boolean collapsePanel, int andataApprov, int ritornoApprov, JSONObject jSONObject){
		if(collapsePanel ){
			jSONObject.put("collapsePanel", "collapse" );
		}else{
			jSONObject.put("collapsePanel", "" );
		}
		if(andataApprov == Constants.IN_APPROVAZIONE ){
			jSONObject.put("flagApprov_A", "glyphicon-question-sign");
		}
		if(andataApprov == Constants.APPROVATA ){
			jSONObject.put("flagApprov_A", "glyphicon-ok");
		}
		if(andataApprov == Constants.NON_APPROVATA ){
			jSONObject.put("flagApprov_A", "glyphicon-remove" );
		}
		if(ritornoApprov == Constants.IN_APPROVAZIONE ){
			jSONObject.put("flagApprov_R", "glyphicon-question-sign");
		}
		if(ritornoApprov == Constants.APPROVATA){
			jSONObject.put("flagApprov_R", "glyphicon-ok");
		}
		if(ritornoApprov == Constants.NON_APPROVATA ){ 
			jSONObject.put("flagApprov_R", "glyphicon-remove" );
		}
		return jSONObject;
	}
	
	protected JSONObject DammiApprovazioneCheckedRadio(int approv, JSONObject jSONObject){
		if(approv == Constants.IN_APPROVAZIONE){
			jSONObject.put("radioInApprov", "checked" );
			jSONObject.put("radioApprov", "" );
			jSONObject.put("radioNonApprov", "" );
		}else if(approv == Constants.APPROVATA){
			jSONObject.put("radioInApprov", "" );
			jSONObject.put("radioApprov", "checked" );
			jSONObject.put("radioNonApprov", "" );
		}else if(approv== Constants.NON_APPROVATA){
			jSONObject.put("radioInApprov", "" );
			jSONObject.put("radioApprov", "" );
			jSONObject.put("radioNonApprov", "checked" );
		}
		return jSONObject;
	}
	
	protected JSONObject DammiSupplemento_Sintesti(RicercaTransfert ricTransf, JSONObject jsonObject) {
		if(ricTransf.getSupplementi_Id() != null) {
			List<Supplementi> supplementiList = supplementiDao.getSupplementiBy_IdRicercaTransfert(ricTransf.getId());
			List<JSONObject> supplementoJSList = new ArrayList<JSONObject>();
			for(Supplementi supplemento: supplementiList) {
				JSONObject supplementoJS = new JSONObject();
				supplementoJS.put("id", supplemento.getId() );
				supplementoJS.put("prezzo", supplemento.getPrezzo());
				supplementoJS.put("pagato", supplemento.isPagato() );
				supplementoJS.put("descrizione", supplemento.getDescrizione() );
				supplementoJSList.add(supplementoJS);
			}
			jsonObject.put("supplemento", supplementoJSList);
		}
		return jsonObject;
	}
	
	private JSONObject PanelHeading_Andata(String keyJsonAndataRitorno, RicercaTransfert ric_ite, JSONObject joCorsaAge) {
		// ANDATA
		JSONObject joCorsaAndataPart = new JSONObject();
		joCorsaAndataPart = DammiIntestazioneCorsa(ric_ite.getDataOraPrelevamentoDate().getTime(), 
				ric_ite.getDurataConTrafficoValue(), ric_ite.getFormattedAddress_Partenza(), 
					ric_ite.getFormattedAddress_Arrivo(), ric_ite.getName_Partenza(), 
						ric_ite.getName_Arrivo(), joCorsaAndataPart);
		joCorsaAndataPart.put("oraPrelev", DateUtil.FORMATO_DATA_ORA.format(ric_ite.getDataOraPrelevamentoDate()) );
		joCorsaAndataPart.put("durata", ric_ite.getDurataConTrafficoText() );
		joCorsaAndataPart.put("distanza", ric_ite.getDistanzaText() );
		joCorsaAndataPart = DammiApprovazioneCheckedRadio(ric_ite.getApprovazioneAndata(), joCorsaAndataPart);
		joCorsaAge.put(keyJsonAndataRitorno, joCorsaAndataPart);
		return joCorsaAge;
	}
	
	private JSONObject PanelHeading_Ritorno(String keyJsonAndataRitorno, RicercaTransfert ric_ite, JSONObject joCorsaAge) {
		if(ric_ite.isRitorno()){
			JSONObject joCorsaRitornoPart = new JSONObject();
			joCorsaRitornoPart = DammiIntestazioneCorsa(
					ric_ite.getDataOraRitornoDate().getTime(), 
					ric_ite.getDurataConTrafficoValueRitorno(), 
					ric_ite.getFormattedAddress_Arrivo(), 
					ric_ite.getFormattedAddress_Partenza(), ric_ite.getName_Arrivo(), 
					ric_ite.getName_Partenza(), joCorsaRitornoPart);
			joCorsaRitornoPart.put("oraPrelev", DateUtil.FORMATO_DATA_ORA.format(ric_ite.getDataOraRitornoDate()) );
			joCorsaRitornoPart.put("durata", ric_ite.getDurataConTrafficoTextRitorno() );
			joCorsaRitornoPart.put("distanza", ric_ite.getDistanzaTextRitorno() );
			joCorsaRitornoPart = DammiApprovazioneCheckedRadio(ric_ite.getApprovazioneRitorno(), joCorsaRitornoPart);
			joCorsaAge.put(keyJsonAndataRitorno, joCorsaRitornoPart);
		}
		return joCorsaAge;
	}
	
	private JSONObject Autoveicolo_CorsaAgendaAutista(String keyJsonAndataRitorno, AgendaAutista_Autista ageAutista, JSONObject joCorsaAge, HttpServletRequest request) {
		// ANDATA
		JSONObject joAutoveicolo = new JSONObject();
		joAutoveicolo.put("classeAutoveicoloSceltaCliente", ApplicationMessagesUtil
				.DammiMessageSource(ageAutista.getClasseAutoveicoloReale().getNome(), new Object[] {}, request.getLocale()));
		joAutoveicolo.put("descrizioneCategorieAuto", AutoveicoloUtil
				.DammiDescrizioneCategorieAutoMap(request.getLocale()).get(ageAutista.getClasseAutoveicoloReale().getId()));
		joAutoveicolo.put("prezzoAutista", ageAutista.getPrezzoCorsa().toString());
		joAutoveicolo.put("autoveicoloRichiesto", ageAutista.getMarcaModello()+" "+ageAutista.getTarga()+" "
		+ApplicationMessagesUtil.DammiMessageSource("posti.auto.autista", new Object[] {ageAutista.getNumeroPostiAutoveicolo()}, request.getLocale()) );
		joCorsaAge.put(keyJsonAndataRitorno, joAutoveicolo);
		return joCorsaAge;
	}
	
	
	
	/**
	 * DA ESEGUIRE O ESEGUITA (AUTISTA) AGENDA_AUTISTA
	 */
	public JSONArray CorsaAgendaAutista_AutistaTemplate(List<RicercaTransfert> ricercaTransfertList, Long idAutista, JSONArray corseARRAY, HttpServletRequest request) {
		for(RicercaTransfert ric_ite: ricercaTransfertList) {
			AgendaAutistaScelta ageScelta = ric_ite.getAgendaAutistaScelta();
			Long idAutistaAndata = ageScelta.getAgendaAutista_AutistaAndata().getIdAutista(); 
			Long idAutistaRitorno = ageScelta.getAgendaAutista_AutistaRitorno() != null ? ageScelta.getAgendaAutista_AutistaRitorno().getIdAutista() : null;
			JSONObject joCorsaAge = new JSONObject();
			if(ric_ite.getRitardo() != null) {
				Ritardi ritardo =  ritardiDao.get( ric_ite.getRitardo() );
				JSONObject ritardoJS = new JSONObject();
				ritardoJS.put("id", ritardo.getId() );
				if(!ritardo.isPagatoAndata()){
					ritardoJS.put("numeroMezzoreAndata", ritardo.getNumeroMezzoreRitardiAndata());
					ritardoJS.put("pagatoAndata", ritardo.isPagatoAndata() );
				}
				if(ric_ite.isRitorno()){
					if(!ritardo.isPagatoRitorno()){
						ritardoJS.put("numeroMezzoreRitorno", ritardo.getNumeroMezzoreRitardiRitorno());
						ritardoJS.put("pagatoRitorno", ritardo.isPagatoRitorno() );
					}
				}
				joCorsaAge.put("ritardo", ritardoJS);
			}
			DammiSupplemento_Sintesti(ric_ite, joCorsaAge);
			joCorsaAge.put("getTimeAndata", ric_ite.getDataOraPrelevamentoDate().getTime());
			/**
			 * se la data prelevamento + la durata percorrenza è superiore alla data di adesso allora la corsa è da eseguire
			 * idem se c'è il ritorno
			 */
			boolean corsaDaEseguire = false;
			// calcolo andata
			long datasommataAndata = ric_ite.getDataOraPrelevamentoDate().getTime() + 
					TimeUnit.SECONDS.toMillis( ric_ite.getDurataConTrafficoValue() );
			Date dataArrivoPrevisto = new Date( datasommataAndata );
			if(dataArrivoPrevisto.getTime() > new Date().getTime()){
				corsaDaEseguire = true;
			}else if( ric_ite.isRitorno() ){
				// calcolo ritorno
				long datasommataRitorno = ric_ite.getDataOraRitornoDate().getTime() + 
						TimeUnit.SECONDS.toMillis( ric_ite.getDurataConTrafficoValueRitorno() );
				Date dataArrivoRitornoPrevisto = new Date( datasommataRitorno );
				if(dataArrivoRitornoPrevisto.getTime() > new Date().getTime()){
					corsaDaEseguire = true;
				}
			}
			if(corsaDaEseguire){
				joCorsaAge.put("template", "AGENDA_AUTISTA_DA_ESEGUIRE");
				corseDaEseguire ++;
			}else{
				joCorsaAge.put("template", "AGENDA_AUTISTA_ESEGUITA");
				corseEseguite ++;
			}
			joCorsaAge.put("idcorsa", ric_ite.getId() );
			//joCorsaAge.put("tokenAutista", ricPart_ite.getToken() );
			joCorsaAge.put("downloadFatturaPDF", Constants.SERVIZIO_AGENDA_AUTISTA+"-"+ric_ite.getId() );
			joCorsaAge = DammiApprovazione_e_CollapsePanel(ric_ite.isCollapsePanelCorseAdmin(), ric_ite.getApprovazioneAndata(), ric_ite.getApprovazioneRitorno(), joCorsaAge);
			
			if(ric_ite.isRitorno() && idAutista == idAutistaAndata.longValue() && idAutista == idAutistaRitorno.longValue() ){
				joCorsaAge.put("intestazione", "AGENDA AUTISTA - A/R - "+DateUtil.FORMATO_DATA_ORA.format(ric_ite.getDataOraPrelevamentoDate())+" - "+DateUtil.FORMATO_DATA_ORA.format(ric_ite.getDataOraRitornoDate()));
				joCorsaAge = PanelHeading_Andata("andata", ric_ite, joCorsaAge);
				joCorsaAge = PanelHeading_Ritorno("ritorno", ric_ite, joCorsaAge);
				joCorsaAge = Autoveicolo_CorsaAgendaAutista("andataAutoveicolo", ageScelta.getAgendaAutista_AutistaAndata(), joCorsaAge, request);
				joCorsaAge = Autoveicolo_CorsaAgendaAutista("ritornoAutoveicolo", ageScelta.getAgendaAutista_AutistaRitorno(), joCorsaAge, request);
				joCorsaAge.put("fullNameAutisa", ageScelta.getAgendaAutista_AutistaAndata().getFullName() );
				joCorsaAge.put("prezzoTotaleAutista", ageScelta.getPrezzoTotaleAutisti().toString());
				
			}else if(ric_ite.isRitorno() && idAutista != idAutistaAndata.longValue() && idAutista == idAutistaRitorno.longValue() ){
				joCorsaAge.put("intestazione", "AGENDA AUTISTA - SOLO ANDATA - "+DateUtil.FORMATO_DATA_ORA.format(ric_ite.getDataOraRitornoDate()));
				joCorsaAge = PanelHeading_Ritorno("andata", ric_ite, joCorsaAge);
				joCorsaAge = Autoveicolo_CorsaAgendaAutista("andataAutoveicolo", ageScelta.getAgendaAutista_AutistaRitorno(), joCorsaAge, request);
				joCorsaAge.put("fullNameAutisa", ageScelta.getAgendaAutista_AutistaRitorno().getFullName() );
				
			}else if( idAutista == idAutistaAndata.longValue() ) {
				joCorsaAge.put("intestazione", "AGENDA AUTISTA - SOLO ANDATA - "+DateUtil.FORMATO_DATA_ORA.format(ric_ite.getDataOraPrelevamentoDate()));
				joCorsaAge = PanelHeading_Andata("andata", ric_ite, joCorsaAge);
				joCorsaAge = Autoveicolo_CorsaAgendaAutista("andataAutoveicolo", ageScelta.getAgendaAutista_AutistaAndata(), joCorsaAge, request);
				joCorsaAge.put("fullNameAutisa", ageScelta.getAgendaAutista_AutistaAndata().getFullName() );
			}
				
			joCorsaAge.put("numPasseggeri", ric_ite.getNumeroPasseggeri());
			joCorsaAge.put("notePerAutista", ric_ite.getNotePerAutista() );
			joCorsaAge.put("noteCorsa", ric_ite.getNote() );
			joCorsaAge.put("noteAutista", "DA IMPLEMENTARE" );
			joCorsaAge.put("rimborsoCliente", (ric_ite.getAgendaAutista_RimborsoCliente() != null) ? ric_ite.getAgendaAutista_RimborsoCliente().toString() : BigDecimal.ZERO.setScale(2).toString());
			joCorsaAge.put("nomeTelefonoPasseggero", ric_ite.getNomeTelefonoPasseggero());
			joCorsaAge.put("nomePasseggero", ric_ite.getNomePasseggero());
			joCorsaAge.put("telefonoPasseggero", ric_ite.getTelefonoPasseggero());
			joCorsaAge.put("fullNameCliente", ric_ite.getUser().getFullName() );
			if(request.isUserInRole(Constants.ADMIN_ROLE)){
				joCorsaAge.put("telCliente", ric_ite.getUser().getPhoneNumber() );
			}else{
				joCorsaAge.put("telCliente", "###########" );
			}
			joCorsaAge.put("noteCliente", ric_ite.getUser().getNote() );
			
			corseARRAY.put(joCorsaAge);
		}
		return corseARRAY;
	}
	
	/**
	 * DA ESEGUIRE O ESEGUITA (CLIENTE) e (ADMIN) AGENDA_AUTISTA
	 */
	public JSONArray CorsaAgendaAutista_ClienteTemplate(List<RicercaTransfert> ricercaTransfertList, JSONArray corseARRAY, HttpServletRequest request) {
		for(RicercaTransfert ric_ite: ricercaTransfertList) {
			JSONObject joCorsaAge = new JSONObject();
			if(ric_ite.getRitardo() != null) {
				Ritardi ritardo =  ritardiDao.get( ric_ite.getRitardo() );
				JSONObject ritardoJS = new JSONObject();
				ritardoJS.put("id", ritardo.getId() );
				if(!ritardo.isPagatoAndata()){
					ritardoJS.put("numeroMezzoreAndata", ritardo.getNumeroMezzoreRitardiAndata());
					ritardoJS.put("pagatoAndata", ritardo.isPagatoAndata() );
				}
				if(ric_ite.isRitorno()){
					if(!ritardo.isPagatoRitorno()){
						ritardoJS.put("numeroMezzoreRitorno", ritardo.getNumeroMezzoreRitardiRitorno());
						ritardoJS.put("pagatoRitorno", ritardo.isPagatoRitorno() );
					}
				}
				joCorsaAge.put("ritardo", ritardoJS);
			}
			DammiSupplemento_Sintesti(ric_ite, joCorsaAge);
			joCorsaAge.put("getTimeAndata", ric_ite.getDataOraPrelevamentoDate().getTime());
			/**
			 * se la data prelevamento + la durata percorrenza è superiore alla data di adesso allora la corsa è da eseguire
			 * idem se c'è il ritorno
			 */
			boolean corsaDaEseguire = false;
			// calcolo andata
			long datasommataAndata = ric_ite.getDataOraPrelevamentoDate().getTime() + 
					TimeUnit.SECONDS.toMillis( ric_ite.getDurataConTrafficoValue() );
			Date dataArrivoPrevisto = new Date( datasommataAndata );
			if(dataArrivoPrevisto.getTime() > new Date().getTime()){
				corsaDaEseguire = true;
			}else if( ric_ite.isRitorno() ){
				// calcolo ritorno
				long datasommataRitorno = ric_ite.getDataOraRitornoDate().getTime() + 
						TimeUnit.SECONDS.toMillis( ric_ite.getDurataConTrafficoValueRitorno() );
				Date dataArrivoRitornoPrevisto = new Date( datasommataRitorno );
				if(dataArrivoRitornoPrevisto.getTime() > new Date().getTime()){
					corsaDaEseguire = true;
				}
			}
			if(corsaDaEseguire){
				joCorsaAge.put("template", "AGENDA_AUTISTA_DA_ESEGUIRE");
				corseDaEseguire ++;
			}else{
				joCorsaAge.put("template", "AGENDA_AUTISTA_ESEGUITA");
				corseEseguite ++;
			}
			joCorsaAge.put("idcorsa", ric_ite.getId() );
			//joCorsaAge.put("tokenAutista", ricPart_ite.getToken() );
			joCorsaAge.put("downloadFatturaPDF", Constants.SERVIZIO_AGENDA_AUTISTA + "-" + ric_ite.getId() );
			if(ric_ite.isRitorno()){
				joCorsaAge.put("intestazione", "AGENDA AUTISTA - A/R - "+DateUtil.FORMATO_DATA_ORA.format(ric_ite.getDataOraPrelevamentoDate())+" - "+DateUtil.FORMATO_DATA_ORA.format(ric_ite.getDataOraRitornoDate()));
			}else{
				joCorsaAge.put("intestazione", "AGENDA AUTISTA - SOLO ANDATA - "+DateUtil.FORMATO_DATA_ORA.format(ric_ite.getDataOraPrelevamentoDate()));
			}
			joCorsaAge = DammiApprovazione_e_CollapsePanel(ric_ite.isCollapsePanelCorseAdmin(), ric_ite.getApprovazioneAndata(), 
					ric_ite.getApprovazioneRitorno(), joCorsaAge);
			AgendaAutistaScelta ageScelta = ric_ite.getAgendaAutistaScelta();
			joCorsaAge.put("prezzoCliente", ageScelta.getPrezzoTotaleCliente().toString() );
			
			if( ageScelta.getAgendaAutista_AutistaAndata() != null ) {
				AgendaAutista_Autista ageAndata = ageScelta.getAgendaAutista_AutistaAndata();
				joCorsaAge.put("classeAutoveicoloSceltaCliente_Andata", ApplicationMessagesUtil
						.DammiMessageSource(ageAndata.getClasseAutoveicoloReale().getNome(), new Object[] {}, request.getLocale()));
				joCorsaAge.put("descrizioneCategorieAuto_Andata", AutoveicoloUtil
						.DammiDescrizioneCategorieAutoMap(request.getLocale()).get(ageAndata.getClasseAutoveicoloReale().getId()));
				joCorsaAge.put("idAutista_Andata", ageAndata.getIdAutista());
				joCorsaAge.put("fullNameAutisa_Andata", ageAndata.getFullName() );
				joCorsaAge.put("telAutista_Andata", ageAndata.getPhoneNumber() );
				joCorsaAge.put("corseEffAutista_Andata", ageAndata.getNumCorseEseguite() );
				joCorsaAge.put("percServAutista_Andata", ageAndata.getPercentualeServizio() );
				joCorsaAge.put("prezzoCommServ_Andata", ageAndata.getPrezzoCommissioneServizio().toString() );
				joCorsaAge.put("prezzoAutista_Andata", ageAndata.getPrezzoCorsa().toString());
				joCorsaAge.put("autoveicoloRichiesto_Andata", ageAndata.getMarcaModello()+" "+ageAndata.getTarga()+" "
				+ApplicationMessagesUtil.DammiMessageSource("posti.auto.autista", new Object[] {ageAndata.getNumeroPostiAutoveicolo()}, request.getLocale()) );
				joCorsaAge.put("noteAutista_Andata", ageAndata.getNoteAutista() );
			}
			
			if( ageScelta.getAgendaAutista_AutistaRitorno() != null ) {
				AgendaAutista_Autista ageRitorno = ageScelta.getAgendaAutista_AutistaRitorno();
				joCorsaAge.put("classeAutoveicoloSceltaCliente_Ritorno", ApplicationMessagesUtil
						.DammiMessageSource(ageRitorno.getClasseAutoveicoloReale().getNome(), new Object[] {}, request.getLocale()));
				joCorsaAge.put("descrizioneCategorieAuto_Ritorno", AutoveicoloUtil
						.DammiDescrizioneCategorieAutoMap(request.getLocale()).get(ageRitorno.getClasseAutoveicoloReale().getId()));
				joCorsaAge.put("idAutista_Ritorno", ageRitorno.getIdAutista());
				joCorsaAge.put("fullNameAutisa_Ritorno", ageRitorno.getFullName() );
				joCorsaAge.put("telAutista_Ritorno", ageRitorno.getPhoneNumber() );
				joCorsaAge.put("corseEffAutista_Ritorno", ageRitorno.getNumCorseEseguite() );
				joCorsaAge.put("percServAutista_Ritorno", ageRitorno.getPercentualeServizio() );
				joCorsaAge.put("prezzoCommServ_Ritorno", ageRitorno.getPrezzoCommissioneServizio().toString() );
				joCorsaAge.put("prezzoAutista_Ritorno", ageRitorno.getPrezzoCorsa().toString());
				joCorsaAge.put("autoveicoloRichiesto_Ritorno", ageRitorno.getMarcaModello()+" "+ageRitorno.getTarga()+" "
				+ApplicationMessagesUtil.DammiMessageSource("posti.auto.autista", new Object[] {ageRitorno.getNumeroPostiAutoveicolo()}, request.getLocale()) );
				joCorsaAge.put("noteAutista_Ritorno", ageRitorno.getNoteAutista() );
			}
				
			joCorsaAge.put("numPasseggeri", ric_ite.getNumeroPasseggeri());
			joCorsaAge.put("notePerAutista", ric_ite.getNotePerAutista() );
			joCorsaAge.put("noteCorsa", ric_ite.getNote() );
			joCorsaAge.put("rimborsoCliente", (ric_ite.getAgendaAutista_RimborsoCliente() != null) ? ric_ite.getAgendaAutista_RimborsoCliente().toString() : BigDecimal.ZERO.setScale(2).toString());
			joCorsaAge.put("nomeTelefonoPasseggero", ric_ite.getNomeTelefonoPasseggero());
			joCorsaAge.put("nomePasseggero", ric_ite.getNomePasseggero());
			joCorsaAge.put("telefonoPasseggero", ric_ite.getTelefonoPasseggero());
			joCorsaAge.put("fullNameCliente", ric_ite.getUser().getFullName() );
			if(request.isUserInRole(Constants.ADMIN_ROLE)){
				joCorsaAge.put("telCliente", ric_ite.getUser().getPhoneNumber() );
			}else{
				joCorsaAge.put("telCliente", "###########" );
			}
			joCorsaAge.put("noteCliente", ric_ite.getUser().getNote() );
			// ANDATA
			joCorsaAge = PanelHeading_Andata("andata", ric_ite, joCorsaAge);
			// RITORNO
			if(ric_ite.isRitorno()){
				joCorsaAge = PanelHeading_Ritorno("ritorno", ric_ite, joCorsaAge);
			}
			corseARRAY.put(joCorsaAge);
		}
		return corseARRAY;
	}
	
	/**
	 * PREVENTIVO (AUTISTA) 
	 */
	public JSONArray CorsaParticolareTemplate_1(RichiestaAutistaParticolare ricPart_ite, JSONArray corseARRAY, HttpServletRequest request){
		if(ricPart_ite != null && ricPart_ite.getRicercaTransfert() != null){
			JSONObject joCorsaPart = new JSONObject();
			joCorsaPart.put("getTimeAndata", ricPart_ite.getRicercaTransfert().getDataOraPrelevamentoDate().getTime());
			joCorsaPart.put("corsaValida", ControlloDateRicerca.ControlloDataPrelevamentoSuperioreTotOraDaAdesso(ricPart_ite.getRicercaTransfert().getDataOraPrelevamentoDate(), Constants.SERVIZIO_PARTICOLARE) );
			joCorsaPart.put("preventivoInviato", ricPart_ite.getPreventivo_inviato_cliente() );
			//joCorsaPart.put("preventivoInviato", false );
			joCorsaPart.put("template", "PARTICOLARE_PRENOTAZIONE" );
			joCorsaPart.put("idcorsa", ricPart_ite.getRicercaTransfert().getId() );
			joCorsaPart.put("idcorsaPart", ricPart_ite.getId() );
			if(ricPart_ite.getRicercaTransfert().isRitorno()){
				joCorsaPart.put("intestazione", "RICHIESTA PREVENTIVO - A/R - "+DateUtil.FORMATO_DATA_ORA.format(ricPart_ite.getRicercaTransfert().getDataOraPrelevamentoDate())+" - "+DateUtil.FORMATO_DATA_ORA.format(ricPart_ite.getRicercaTransfert().getDataOraRitornoDate()));
			}else{
				joCorsaPart.put("intestazione", "RICHIESTA PREVENTIVO - SOLO ANDATA - "+DateUtil.FORMATO_DATA_ORA.format(ricPart_ite.getRicercaTransfert().getDataOraPrelevamentoDate()));
			}
			joCorsaPart = DammiApprovazione_e_CollapsePanel(ricPart_ite.getRicercaTransfert().isCollapsePanelCorseAdmin(), ricPart_ite.getRicercaTransfert().getApprovazioneAndata(), 
					ricPart_ite.getRicercaTransfert().getApprovazioneRitorno(), joCorsaPart);
			joCorsaPart.put("validitaPreventivoData", ricPart_ite.getPreventivo_validita_data() != null ? 
					DateUtil.FORMATO_DATA_ORA.format(new Date(ricPart_ite.getPreventivo_validita_data())) : null );
			joCorsaPart.put("classeAutoveicoloSceltaCliente", ApplicationMessagesUtil.DammiMessageSource( ricPart_ite.getClasseAutoveicoloScelta().getNome(), 
					new Object[] {}, request.getLocale()));
			joCorsaPart.put("descrizioneCategorieAuto", AutoveicoloUtil.DammiDescrizioneCategorieAutoMap(request.getLocale()).get(ricPart_ite.getClasseAutoveicoloScelta().getId()));
			joCorsaPart.put("numPasseggeri", ricPart_ite.getRicercaTransfert().getTipoServizio().equals(Constants.SERVIZIO_MULTIPLO) ? 
					"max "+ricPart_ite.getAutoveicolo().getNumeroPostiPasseggeri() : ricPart_ite.getRicercaTransfert().getNumeroPasseggeri());
			if( ricPart_ite.getRicercaTransfert().getTipoServizio().equals(Constants.SERVIZIO_MULTIPLO) ) {
				joCorsaPart.put("descServizioMultiplo", ApplicationMessagesUtil.DammiMessageSource("descrizione.servizio.multiplo.panel.autista", new String[]{ 
						ApplicationMessagesUtil.DammiMessageSource("descrizione.servizio.multiplo", request.getLocale()) }, request.getLocale()));
			}
			joCorsaPart.put("notePerAutista", ricPart_ite.getRicercaTransfert().getNotePerAutista());
			joCorsaPart.put("noteCorsa", ricPart_ite.getRicercaTransfert().getNote() );
			joCorsaPart.put("idAutista", ricPart_ite.getAutoveicolo().getAutista().getId() );
			joCorsaPart.put("fullNameAutisa", ricPart_ite.getAutoveicolo().getAutista().getUser().getFullName() );
			joCorsaPart.put("telAutista", ricPart_ite.getAutoveicolo().getAutista().getUser().getPhoneNumber() );
			joCorsaPart.put("noteAutista", ricPart_ite.getAutoveicolo().getAutista().getNote() );
			joCorsaPart.put("corseEffAutista", ricPart_ite.getAutoveicolo().getAutista().getNumCorseEseguite() );
			joCorsaPart.put("percServAutista", ricPart_ite.getRicercaTransfert().getPercentualeServizioRichiestaMediaScelta() /*ricPart_ite.getAutoveicolo().getAutista().getPercentualeServizio()*/ );
			joCorsaPart.put("prezzoCommServ", ricPart_ite.getPrezzoCommissioneServizio().add(ricPart_ite.getPrezzoCommissioneServizioIva()).toString() );
			joCorsaPart.put("prezzoAutista", ricPart_ite.getPrezzoTotaleAutista() != null ? ricPart_ite.getPrezzoTotaleAutista().toString() : BigDecimal.ZERO.setScale(2).toString());
			joCorsaPart.put("rimborsoCliente", (ricPart_ite.getRimborsoCliente() !=  null) ? ricPart_ite.getRimborsoCliente().toString() : BigDecimal.ZERO.setScale(2).toString());
			joCorsaPart.put("nomeTelefonoPasseggero", ricPart_ite.getRicercaTransfert().getNomeTelefonoPasseggero());
			joCorsaPart.put("nomePasseggero", ricPart_ite.getRicercaTransfert().getNomePasseggero());
			joCorsaPart.put("telefonoPasseggero", ricPart_ite.getRicercaTransfert().getTelefonoPasseggero());
			joCorsaPart.put("autoveicoloRichiesto", ricPart_ite.getAutoveicolo().getMarcaModello()+" "
					+ricPart_ite.getAutoveicolo().getTarga()+" "+ ApplicationMessagesUtil.DammiMessageSource("posti.auto.autista", new Object[] {ricPart_ite.getAutoveicolo().getModelloAutoNumeroPosti().getNumeroPostiAuto().getNumero()}, request.getLocale()) );
			joCorsaPart.put("prezzoCliente", ricPart_ite.getPrezzoTotaleCliente() != null ? ricPart_ite.getPrezzoTotaleCliente().toString() : BigDecimal.ZERO.setScale(2).toString());
			// ANDATA
			joCorsaPart = PanelHeading_Andata("andata", ricPart_ite.getRicercaTransfert(), joCorsaPart);
			// RITORNO
			if(ricPart_ite.getRicercaTransfert().isRitorno()){
				joCorsaPart = PanelHeading_Ritorno("ritorno", ricPart_ite.getRicercaTransfert(), joCorsaPart);
			}
			corseARRAY.put(joCorsaPart);
		}
		return corseARRAY;
	}
	
	/**
	 * DA ESEGUIRE O ESEGUITA (AUTISTA e CLIENTE)
	 */
	public JSONArray CorsaParticolareTemplate_2(List<RichiestaAutistaParticolare> corseAutistaRicTransfertParticList, JSONArray corseARRAY, HttpServletRequest request){
		for(RichiestaAutistaParticolare ricPart_ite: corseAutistaRicTransfertParticList){
			JSONObject joCorsaPart = new JSONObject();
			if(ricPart_ite.getRicercaTransfert().getRitardo() != null) {
				Ritardi ritardo =  ritardiDao.get( ricPart_ite.getRicercaTransfert().getRitardo() );
				JSONObject ritardoJS = new JSONObject();
				ritardoJS.put("id", ritardo.getId() );
				if(!ritardo.isPagatoAndata()){
					ritardoJS.put("numeroMezzoreAndata", ritardo.getNumeroMezzoreRitardiAndata());
					ritardoJS.put("pagatoAndata", ritardo.isPagatoAndata() );
				}
				if(ricPart_ite.getRicercaTransfert().isRitorno()){
					if(!ritardo.isPagatoRitorno()){
						ritardoJS.put("numeroMezzoreRitorno", ritardo.getNumeroMezzoreRitardiRitorno());
						ritardoJS.put("pagatoRitorno", ritardo.isPagatoRitorno() );
					}
				}
				joCorsaPart.put("ritardo", ritardoJS);
			}
			DammiSupplemento_Sintesti(ricPart_ite.getRicercaTransfert(), joCorsaPart);
			joCorsaPart.put("getTimeAndata", ricPart_ite.getRicercaTransfert().getDataOraPrelevamentoDate().getTime());
			/**
			 * se la data prelevamento + la durata percorrenza è superiore alla data di adesso allora la corsa è da eseguire
			 * idem se c'è il ritorno
			 */
			boolean corsaDaEseguire = false;
			// calcolo andata
			long datasommataAndata = ricPart_ite.getRicercaTransfert().getDataOraPrelevamentoDate().getTime() + 
					TimeUnit.SECONDS.toMillis( ricPart_ite.getRicercaTransfert().getDurataConTrafficoValue() );
			Date dataArrivoPrevisto = new Date( datasommataAndata );
			if(dataArrivoPrevisto.getTime() > new Date().getTime()){
				corsaDaEseguire = true;
			}else if( ricPart_ite.getRicercaTransfert().isRitorno() ){
				// calcolo ritorno
				long datasommataRitorno = ricPart_ite.getRicercaTransfert().getDataOraRitornoDate().getTime() + 
						TimeUnit.SECONDS.toMillis( ricPart_ite.getRicercaTransfert().getDurataConTrafficoValueRitorno() );
				Date dataArrivoRitornoPrevisto = new Date( datasommataRitorno );
				if(dataArrivoRitornoPrevisto.getTime() > new Date().getTime()){
					corsaDaEseguire = true;
				}
			}
			if(corsaDaEseguire){
				joCorsaPart.put("template", "PARTICOLARE_DA_ESEGUIRE");
				corseDaEseguire ++;
			}else{
				joCorsaPart.put("template", "PARTICOLARE_ESEGUITA");
				corseEseguite ++;
			}
			joCorsaPart.put("idcorsa", ricPart_ite.getRicercaTransfert().getId() );
			joCorsaPart.put("tokenAutista", ricPart_ite.getToken() );
			joCorsaPart.put("downloadFatturaPDF", Constants.SERVIZIO_PARTICOLARE + "-" + ricPart_ite.getId() );
			if(ricPart_ite.getRicercaTransfert().isRitorno()){
				joCorsaPart.put("intestazione", "CORSA PARTICOLARE - A/R - "+DateUtil.FORMATO_DATA_ORA.format(ricPart_ite.getRicercaTransfert().getDataOraPrelevamentoDate())+" - "+DateUtil.FORMATO_DATA_ORA.format(ricPart_ite.getRicercaTransfert().getDataOraRitornoDate()));
			}else{
				joCorsaPart.put("intestazione", "CORSA PARTICOLARE - SOLO ANDATA - "+DateUtil.FORMATO_DATA_ORA.format(ricPart_ite.getRicercaTransfert().getDataOraPrelevamentoDate()));
			}
			joCorsaPart = DammiApprovazione_e_CollapsePanel(ricPart_ite.getRicercaTransfert().isCollapsePanelCorseAdmin(), ricPart_ite.getRicercaTransfert().getApprovazioneAndata(), 
					ricPart_ite.getRicercaTransfert().getApprovazioneRitorno(), joCorsaPart);
			
			joCorsaPart.put("classeAutoveicoloSceltaCliente", ApplicationMessagesUtil.DammiMessageSource( ricPart_ite.getClasseAutoveicoloScelta().getNome(), 
					new Object[] {}, request.getLocale()));
			joCorsaPart.put("descrizioneCategorieAuto", AutoveicoloUtil.DammiDescrizioneCategorieAutoMap(request.getLocale()).get(ricPart_ite.getClasseAutoveicoloScelta().getId()));
			joCorsaPart.put("numPasseggeri", ricPart_ite.getRicercaTransfert().getTipoServizio().equals(Constants.SERVIZIO_MULTIPLO) ? 
					"max "+ricPart_ite.getAutoveicolo().getNumeroPostiPasseggeri() : ricPart_ite.getRicercaTransfert().getNumeroPasseggeri());
			if( ricPart_ite.getRicercaTransfert().getTipoServizio().equals(Constants.SERVIZIO_MULTIPLO) ) {
				joCorsaPart.put("descServizioMultiplo", ApplicationMessagesUtil.DammiMessageSource("descrizione.servizio.multiplo.panel.autista", new String[]{ 
						ApplicationMessagesUtil.DammiMessageSource("descrizione.servizio.multiplo", request.getLocale()) }, request.getLocale()));
			}
			joCorsaPart.put("notePerAutista", ricPart_ite.getRicercaTransfert().getNotePerAutista() );
			joCorsaPart.put("noteCorsa", ricPart_ite.getRicercaTransfert().getNote() );
			joCorsaPart.put("idAutista", ricPart_ite.getAutoveicolo().getAutista().getId() );
			joCorsaPart.put("fullNameAutisa", ricPart_ite.getAutoveicolo().getAutista().getUser().getFullName() );
			joCorsaPart.put("telAutista", ricPart_ite.getAutoveicolo().getAutista().getUser().getPhoneNumber() );
			joCorsaPart.put("noteAutista", ricPart_ite.getAutoveicolo().getAutista().getNote() );
			joCorsaPart.put("corseEffAutista", ricPart_ite.getAutoveicolo().getAutista().getNumCorseEseguite() );
			joCorsaPart.put("percServAutista", ricPart_ite.getRicercaTransfert().getPercentualeServizioRichiestaMediaScelta() /*ricPart_ite.getAutoveicolo().getAutista().getPercentualeServizio()*/ );
			joCorsaPart.put("prezzoCommServ", ricPart_ite.getPrezzoCommissioneServizio().toString() );
			joCorsaPart.put("prezzoAutista", ricPart_ite.getPrezzoTotaleAutista().toString());
			joCorsaPart.put("rimborsoCliente", (ricPart_ite.getRimborsoCliente() != null) ? ricPart_ite.getRimborsoCliente().toString() : BigDecimal.ZERO.setScale(2).toString());
			joCorsaPart.put("nomeTelefonoPasseggero", ricPart_ite.getRicercaTransfert().getNomeTelefonoPasseggero());
			joCorsaPart.put("nomePasseggero", ricPart_ite.getRicercaTransfert().getNomePasseggero());
			joCorsaPart.put("telefonoPasseggero", ricPart_ite.getRicercaTransfert().getTelefonoPasseggero());
			joCorsaPart.put("autoveicoloRichiesto", ricPart_ite.getAutoveicolo().getMarcaModello()+" "
					+ricPart_ite.getAutoveicolo().getTarga()+" "+ ApplicationMessagesUtil.DammiMessageSource("posti.auto.autista", new Object[] {ricPart_ite.getAutoveicolo().getModelloAutoNumeroPosti().getNumeroPostiAuto().getNumero()}, request.getLocale()) );
			joCorsaPart.put("fullNameCliente", ricPart_ite.getRicercaTransfert().getUser().getFullName() );
			if(request.isUserInRole(Constants.ADMIN_ROLE)){
				joCorsaPart.put("telCliente", ricPart_ite.getRicercaTransfert().getUser().getPhoneNumber() );
			}else{
				joCorsaPart.put("telCliente", "###########" );
			}
			joCorsaPart.put("noteCliente", ricPart_ite.getRicercaTransfert().getUser().getNote() );
			joCorsaPart.put("prezzoCliente", ricPart_ite.getPrezzoTotaleCliente().toString() );
			// ANDATA
			joCorsaPart = PanelHeading_Andata("andata", ricPart_ite.getRicercaTransfert(), joCorsaPart);
			// RITORNO
			if(ricPart_ite.getRicercaTransfert().isRitorno()){
				joCorsaPart = PanelHeading_Ritorno("ritorno", ricPart_ite.getRicercaTransfert(), joCorsaPart);
			}
			corseARRAY.put(joCorsaPart);
		}
		return corseARRAY;
	}
	
	/**
	 * DA ESEGUIRE O ESEGUITA (CLIENTE)
	 */
	public JSONArray CorsaMultiplaTemplate_2(List<RicercaTransfert> corseAutistaRicTransfertMultipleList, JSONArray corseARRAY, HttpServletRequest request) {
		for(RicercaTransfert ricPart_ite: corseAutistaRicTransfertMultipleList){
			JSONObject joCorsaMultipla = new JSONObject();
			
			if(ricPart_ite.getRitardo() != null) {
				Ritardi ritardo = ritardiDao.get( ricPart_ite.getRitardo() );
				JSONObject ritardoJS = new JSONObject();
				ritardoJS.put("id", ritardo.getId() );
				if(!ritardo.isPagatoAndata()){
					ritardoJS.put("numeroMezzoreAndata", ritardo.getNumeroMezzoreRitardiAndata());
					ritardoJS.put("pagatoAndata", ritardo.isPagatoAndata() );
				}
				if(ricPart_ite.isRitorno()){
					if(!ritardo.isPagatoRitorno()){
						ritardoJS.put("numeroMezzoreRitorno", ritardo.getNumeroMezzoreRitardiRitorno());
						ritardoJS.put("pagatoRitorno", ritardo.isPagatoRitorno() );
					}
				}
				joCorsaMultipla.put("ritardo", ritardoJS);
			}
			DammiSupplemento_Sintesti(ricPart_ite, joCorsaMultipla);
			joCorsaMultipla.put("getTimeAndata", ricPart_ite.getDataOraPrelevamentoDate().getTime());
			/**
			 * se la data prelevamento + la durata percorrenza è superiore alla data di adesso allora la corsa è da eseguire
			 * idem se c'è il ritorno
			 */
			boolean corsaDaEseguire = false;
			// calcolo andata
			long datasommataAndata = ricPart_ite.getDataOraPrelevamentoDate().getTime() + 
					TimeUnit.SECONDS.toMillis( ricPart_ite.getDurataConTrafficoValue() );
			Date dataArrivoPrevisto = new Date( datasommataAndata );
			if(dataArrivoPrevisto.getTime() > new Date().getTime()){
				corsaDaEseguire = true;
			}else if( ricPart_ite.isRitorno() ){
				// calcolo ritorno
				long datasommataRitorno = ricPart_ite.getDataOraRitornoDate().getTime() + 
						TimeUnit.SECONDS.toMillis( ricPart_ite.getDurataConTrafficoValueRitorno() );
				Date dataArrivoRitornoPrevisto = new Date( datasommataRitorno );
				if(dataArrivoRitornoPrevisto.getTime() > new Date().getTime()){
					corsaDaEseguire = true;
				}
			}
			if(corsaDaEseguire){
				joCorsaMultipla.put("template", "MULTIPLA_DA_ESEGUIRE");
				corseDaEseguire ++;
			}else{
				joCorsaMultipla.put("template", "MULTIPLA_ESEGUITA");
				corseEseguite ++;
			}
			joCorsaMultipla.put("idcorsa", ricPart_ite.getId() );
			
			joCorsaMultipla.put("downloadFatturaPDF", Constants.SERVIZIO_PARTICOLARE + "-" + ricPart_ite.getId() );
			if(ricPart_ite.isRitorno()){
				joCorsaMultipla.put("intestazione", "CORSA MULTIPLA - A/R - "+DateUtil.FORMATO_DATA_ORA.format(ricPart_ite.getDataOraPrelevamentoDate())+" - "+DateUtil.FORMATO_DATA_ORA.format(ricPart_ite.getDataOraRitornoDate()));
			}else{
				joCorsaMultipla.put("intestazione", "CORSA MULTIPLA - SOLO ANDATA - "+DateUtil.FORMATO_DATA_ORA.format(ricPart_ite.getDataOraPrelevamentoDate()));
			}
			joCorsaMultipla = DammiApprovazione_e_CollapsePanel(ricPart_ite.isCollapsePanelCorseAdmin(), ricPart_ite.getApprovazioneAndata(), 
					ricPart_ite.getApprovazioneRitorno(), joCorsaMultipla);
			joCorsaMultipla.put("noteCorsa", ricPart_ite.getNote() );
			joCorsaMultipla.put("notePerAutista", ricPart_ite.getNotePerAutista() );
			joCorsaMultipla.put("percServAutista", ricPart_ite.getPercentualeServizioRichiestaMediaScelta() /*ricPart_ite.getAutoveicolo().getAutista().getPercentualeServizio()*/ );
			joCorsaMultipla.put("nomeTelefonoPasseggero", ricPart_ite.getNomeTelefonoPasseggero());
			joCorsaMultipla.put("nomePasseggero", ricPart_ite.getNomePasseggero());
			joCorsaMultipla.put("telefonoPasseggero", ricPart_ite.getTelefonoPasseggero());
			List<JSONObject> joCorsaPartList = new ArrayList<JSONObject>();
			BigDecimal prezzoClienteTotale = BigDecimal.ZERO;
			BigDecimal rimborsoClienteTotale = BigDecimal.ZERO;
			for(RichiestaAutistaParticolare ite: ricPart_ite.getRichiestaAutistaParticolareAcquistato_Multiplo()) {
				JSONObject joCorsaPart = new JSONObject();
				joCorsaPart.put("tokenAutista", ite.getToken() );
				joCorsaPart.put("classeAutoveicoloSceltaCliente", ApplicationMessagesUtil.DammiMessageSource( ite.getClasseAutoveicoloScelta().getNome(), 
						new Object[] {}, request.getLocale()));
				joCorsaPart.put("descrizioneCategorieAuto", AutoveicoloUtil.DammiDescrizioneCategorieAutoMap(request.getLocale()).get(ite.getClasseAutoveicoloScelta().getId()));
				
				joCorsaPart.put("idAutista", ite.getAutoveicolo().getAutista().getId() );
				joCorsaPart.put("fullNameAutisa", ite.getAutoveicolo().getAutista().getUser().getFullName() );
				joCorsaPart.put("telAutista", ite.getAutoveicolo().getAutista().getUser().getPhoneNumber() ); 
				joCorsaPart.put("noteAutista", ite.getAutoveicolo().getAutista().getNote() );
				joCorsaPart.put("corseEffAutista", ite.getAutoveicolo().getAutista().getNumCorseEseguite() );
				joCorsaPart.put("prezzoCommServ", ite.getPrezzoCommissioneServizio().toString() );
				joCorsaPart.put("prezzoAutista", ite.getPrezzoTotaleAutista().toString());
				joCorsaPart.put("autoveicoloRichiesto", ite.getAutoveicolo().getMarcaModello()+" "
						+ite.getAutoveicolo().getTarga()+" "+ ApplicationMessagesUtil.DammiMessageSource("posti.auto.autista", new Object[] {ite.getAutoveicolo().getModelloAutoNumeroPosti().getNumeroPostiAuto().getNumero()}, request.getLocale()) );
				joCorsaPart.put("prezzoCliente", ite.getPrezzoTotaleCliente().toString() );
				prezzoClienteTotale = prezzoClienteTotale.add( ite.getPrezzoTotaleCliente() );
				joCorsaPart.put("rimborsoCliente", (ite.getRimborsoCliente() != null) ? ite.getRimborsoCliente().toString() : BigDecimal.ZERO.setScale(2).toString());
				rimborsoClienteTotale = rimborsoClienteTotale.add( ite.getRimborsoCliente() != null ? ite.getRimborsoCliente() : BigDecimal.ZERO );
				joCorsaPart.put("numPasseggeri", "max "+ite.getAutoveicolo().getNumeroPostiPasseggeri());
				joCorsaPartList.add( joCorsaPart );
			}
			joCorsaMultipla.put("joCorsaPartList", joCorsaPartList );
			
			joCorsaMultipla.put("numPasseggeriTotale", ricPart_ite.getNumeroPasseggeri() );
			joCorsaMultipla.put("prezzoClienteTotale", prezzoClienteTotale );
			joCorsaMultipla.put("rimborsoClienteTotale", rimborsoClienteTotale );
			
			joCorsaMultipla.put("fullNameCliente", ricPart_ite.getUser().getFullName() );
			if(request.isUserInRole(Constants.ADMIN_ROLE)){
				joCorsaMultipla.put("telCliente", ricPart_ite.getUser().getPhoneNumber() );
			}else{
				joCorsaMultipla.put("telCliente", "###########" );
			}
			joCorsaMultipla.put("noteCliente", ricPart_ite.getUser().getNote() );
			// ANDATA
			joCorsaMultipla = PanelHeading_Andata("andata", ricPart_ite, joCorsaMultipla);
			// RITORNO
			if(ricPart_ite.isRitorno()){
				joCorsaMultipla = PanelHeading_Ritorno("ritorno", ricPart_ite, joCorsaMultipla);
			}
			corseARRAY.put(joCorsaMultipla);
		}
		return corseARRAY;
	}
	
	
	/**
	 * CORSE MEDIE DA ESEGUIRE O ESEGUITA (CLIENTE e AUTISTA)
	 */
	public JSONArray CorseMedieAutista(List<RichiestaMediaAutista> corseAutistaRicTransfertMedioList, JSONArray corseARRAY, HttpServletRequest request){
		for(RichiestaMediaAutista gestCorseMedie_ite: corseAutistaRicTransfertMedioList){
			JSONObject joCorsaMedia = new JSONObject();
			joCorsaMedia.put("getTimeAndata", gestCorseMedie_ite.getRichiestaMedia().getRicercaTransfert().getDataOraPrelevamentoDate().getTime());
			/**
			 * se la data prelevamento + la durata percorrenza è superiore alla data di adesso allora la corsa è da eseguire
			 * idem se c'è il ritorno
			 */
			boolean corsaDaEseguire = false;
			// calcolo andata
			long datasommataAndata = gestCorseMedie_ite.getRichiestaMedia().getRicercaTransfert().getDataOraPrelevamentoDate().getTime() + 
					TimeUnit.SECONDS.toMillis( gestCorseMedie_ite.getRichiestaMedia().getRicercaTransfert().getDurataConTrafficoValue() );
			Date dataArrivoPrevisto = new Date( datasommataAndata );
			if(dataArrivoPrevisto.getTime() > new Date().getTime()){
				corsaDaEseguire = true;
			}else if( gestCorseMedie_ite.getRichiestaMedia().getRicercaTransfert().isRitorno() ){
				// calcolo ritorno
				long datasommataRitorno = gestCorseMedie_ite.getRichiestaMedia().getRicercaTransfert().getDataOraRitornoDate().getTime() + 
						TimeUnit.SECONDS.toMillis( gestCorseMedie_ite.getRichiestaMedia().getRicercaTransfert().getDurataConTrafficoValueRitorno() );
				Date dataArrivoRitornoPrevisto = new Date( datasommataRitorno );
				if(dataArrivoRitornoPrevisto.getTime() > new Date().getTime()){
					corsaDaEseguire = true;
				}
			}
			if(corsaDaEseguire){
				joCorsaMedia.put("template", "MEDIA_DA_ESEGUIRE" );
				joCorsaMedia.put("daEseguire", true );
				corseDaEseguire ++;
			}else{
				joCorsaMedia.put("template", "MEDIA_ESEGUITA" );
				corseEseguite ++;
			}
			joCorsaMedia.put("idcorsa", gestCorseMedie_ite.getRichiestaMedia().getRicercaTransfert().getId());
			joCorsaMedia.put("idcorsaMedia", gestCorseMedie_ite.getId());
			joCorsaMedia.put("downloadFatturaPDF", Constants.SERVIZIO_STANDARD + "-" + gestCorseMedie_ite.getId() );
			final Integer ORE = (int) (long) gestioneApplicazioneDao.getName("NUM_ORE_TRA_ORA_PRELEV_AD_ADESSO_DISDETTA_AUTISTA_MEDIO").getValueNumber();
			joCorsaMedia.put("numMaxOreDisdettaCorsa", ORE );
			if(gestCorseMedie_ite.getRichiestaMedia().getRicercaTransfert().isRitorno()){
				joCorsaMedia.put("intestazione", "CORSA - A/R - "+DateUtil.FORMATO_DATA_ORA.format(gestCorseMedie_ite.getRichiestaMedia().getRicercaTransfert().getDataOraPrelevamentoDate()) +
							" - "+DateUtil.FORMATO_DATA_ORA.format(gestCorseMedie_ite.getRichiestaMedia().getRicercaTransfert().getDataOraRitornoDate()));
			}else{
				joCorsaMedia.put("intestazione", "CORSA - SOLO ANDATA - "+ DateUtil.FORMATO_DATA_ORA.format(gestCorseMedie_ite.getRichiestaMedia().getRicercaTransfert().getDataOraPrelevamentoDate()) );
			} 
			joCorsaMedia = DammiApprovazione_e_CollapsePanel(gestCorseMedie_ite.getRichiestaMedia().getRicercaTransfert().isCollapsePanelCorseAdmin(), gestCorseMedie_ite.getRichiestaMedia().getRicercaTransfert().getApprovazioneAndata(), 
					gestCorseMedie_ite.getRichiestaMedia().getRicercaTransfert().getApprovazioneRitorno(), joCorsaMedia);
			joCorsaMedia.put("numPasseggeri", gestCorseMedie_ite.getRichiestaMedia().getRicercaTransfert().getNumeroPasseggeri() );
			joCorsaMedia.put("notePerAutista", gestCorseMedie_ite.getRichiestaMedia().getRicercaTransfert().getNotePerAutista() );
			joCorsaMedia.put("fullNameCliente", gestCorseMedie_ite.getRichiestaMedia().getRicercaTransfert().getUser().getFullName() );
			//joCorsaMedia.put("telCliente", gestCorseMedie_ite.getRicercaTransfert().getUser().getPhoneNumber() );
			joCorsaMedia.put("noteCliente", gestCorseMedie_ite.getRichiestaMedia().getRicercaTransfert().getUser().getNote() );
			String prezzoCliente = "";
			if(gestCorseMedie_ite.getRichiestaMedia().getRicercaTransfert().isPagamentoParziale()){
				prezzoCliente = ApplicationMessagesUtil.DammiMessageSource("prezzo.cliente.parziale.piu.prezzo.autista", new String[]{
						gestCorseMedie_ite.getRichiestaMedia().getRicercaTransfert().getRichiestaMediaScelta().getPrezzoCommissioneServizio().toString(), 
						gestCorseMedie_ite.getRichiestaMedia().getRicercaTransfert().getRichiestaMediaScelta().getPrezzoTotaleAutista().toString()}, request.getLocale());
			}else{
				prezzoCliente = gestCorseMedie_ite.getRichiestaMedia().getPrezzoTotaleCliente()+"\u20AC".toString();
			}
			if(gestCorseMedie_ite.getRichiestaMedia().getMaggiorazioneNotturna() != null){
				prezzoCliente = prezzoCliente + ApplicationMessagesUtil.DammiMessageSource("prezzo.autista.inclusa.maggiorazione.notturna", new String[]{
						gestCorseMedie_ite.getRichiestaMedia().getMaggiorazioneNotturna().toString()}, request.getLocale());
			}
			joCorsaMedia.put("prezzoCliente", prezzoCliente);
			joCorsaMedia.put("classeAutoveicoloSceltaCliente", ApplicationMessagesUtil.DammiMessageSource( gestCorseMedie_ite.getRichiestaMedia().getClasseAutoveicolo().getNome(), 
					new Object[] {}, request.getLocale()));
			joCorsaMedia.put("descrizioneCategorieAuto", AutoveicoloUtil.DammiDescrizioneCategorieAutoMap(request.getLocale())
					.get(gestCorseMedie_ite.getRichiestaMedia().getClasseAutoveicolo().getId()));
			String rimborsoCliente = ((gestCorseMedie_ite.getRichiestaMedia().getRimborsoCliente() != null) 
					? gestCorseMedie_ite.getRichiestaMedia().getRimborsoCliente().toString() : BigDecimal.ZERO.setScale(2).toString());
			joCorsaMedia.put("rimborsoCliente", rimborsoCliente );
			joCorsaMedia.put("nomeTelefonoPasseggero", gestCorseMedie_ite.getRichiestaMedia().getRicercaTransfert().getNomeTelefonoPasseggero());
			joCorsaMedia.put("nomePasseggero", gestCorseMedie_ite.getRichiestaMedia().getRicercaTransfert().getNomePasseggero());
			joCorsaMedia.put("telefonoPasseggero", gestCorseMedie_ite.getRichiestaMedia().getRicercaTransfert().getTelefonoPasseggero());
			joCorsaMedia.put("maggiorazioneNotturna", (gestCorseMedie_ite.getRichiestaMedia().getMaggiorazioneNotturna() != null) ? gestCorseMedie_ite.getRichiestaMedia().getMaggiorazioneNotturna().toString() : BigDecimal.ZERO.setScale(2).toString() ); 
			// ANDATA
			joCorsaMedia = PanelHeading_Andata("andata", gestCorseMedie_ite.getRichiestaMedia().getRicercaTransfert(), joCorsaMedia);
			// RITORNO
			if(gestCorseMedie_ite.getRichiestaMedia().getRicercaTransfert().isRitorno()){
				joCorsaMedia = PanelHeading_Ritorno("ritorno", gestCorseMedie_ite.getRichiestaMedia().getRicercaTransfert(), joCorsaMedia);
			}
			joCorsaMedia.put("corseEffAutista", gestCorseMedie_ite.getAutista().getNumCorseEseguite());
			joCorsaMedia.put("percServAutista", gestCorseMedie_ite.getRichiestaMedia().getRicercaTransfert().getPercentualeServizioRichiestaMediaScelta() /*gestCorseMedie_ite.getAutista().getPercentualeServizio()*/);
			joCorsaMedia.put("prezzoCommServ", gestCorseMedie_ite.getPrezzoCommissioneServizio().toString());
			
			String prezzoAutista = "";
			if(gestCorseMedie_ite.getRichiestaMedia().getRicercaTransfert().isPagamentoParziale()){
				prezzoAutista = ApplicationMessagesUtil.DammiMessageSource("prezzo.autista.contanti", new String[]{
						gestCorseMedie_ite.getPrezzoTotaleAutista().toString()}, request.getLocale());
			}else{
				prezzoAutista = gestCorseMedie_ite.getPrezzoTotaleAutista().toString()+"\u20AC (prezzo autista)";
			}
			if(gestCorseMedie_ite.getRichiestaMedia().getMaggiorazioneNotturna() != null){
				prezzoAutista = prezzoAutista + ApplicationMessagesUtil.DammiMessageSource("prezzo.autista.inclusa.maggiorazione.notturna", new String[]{
						gestCorseMedie_ite.getRichiestaMedia().getMaggiorazioneNotturna().toString()}, request.getLocale());
			}
			joCorsaMedia.put("tokenAutista", gestCorseMedie_ite.getTokenAutista() );
			joCorsaMedia.put("prezzoAutista", prezzoAutista);
			joCorsaMedia.put("tariffaPerKm", gestCorseMedie_ite.getTariffaPerKm().toString());
			joCorsaMedia.put("maggiorazioneNotturna", (gestCorseMedie_ite.getRichiestaMedia().getMaggiorazioneNotturna() != null) ? gestCorseMedie_ite.getRichiestaMedia().getMaggiorazioneNotturna().toString() : null);
			joCorsaMedia.put("idAutista", gestCorseMedie_ite.getAutista().getId());
			joCorsaMedia.put("fullNameAutisa", gestCorseMedie_ite.getAutista().getUser().getFullName());
			joCorsaMedia.put("autoveicoloRichiesto", AutoveicoloUtil.DammiAutiveicoliRichiestiAutistaList_String(gestCorseMedie_ite.getRichiestaMediaAutistaAutoveicolo(), request) );
			corseARRAY.put(joCorsaMedia);
		}
		return corseARRAY;
	}
	
	
	public JSONArray CorseMedieDisponibiliAutista(List<RichiestaMediaAutista> corseAutistaRicTransfertMedioList, JSONArray corseARRAY, HttpServletRequest request){
		for(RichiestaMediaAutista gestCorseMedie_ite: corseAutistaRicTransfertMedioList){
			JSONObject joCorsaMedia = new JSONObject();
			joCorsaMedia.put("getTimeAndata", gestCorseMedie_ite.getRichiestaMedia().getRicercaTransfert().getDataOraPrelevamentoDate().getTime());
			final Integer ORE = (int) (long) gestioneApplicazioneDao.getName("NUM_ORE_TRA_ORA_PRELEV_AD_ADESSO_DISDETTA_AUTISTA_MEDIO").getValueNumber();
			joCorsaMedia.put("numMaxOreDisdettaCorsa", ORE );
			joCorsaMedia.put("template", "MEDIA_DISPONIBILE" );
			corseDisponibili++;
			List<RichiestaMediaAutista> richAutistMedioPrenotata = richiestaAutistaMedioDao
					.getRichiestaAutistaMedio_By_IdRicercaTransfert_and_ChiamataPrenotata( gestCorseMedie_ite.getRichiestaMedia().getRicercaTransfert().getId() );
			if( (gestCorseMedie_ite.getRichiestaMedia().getRicercaTransfert().getApprovazioneAndata() == Constants.NON_APPROVATA
					&& !gestCorseMedie_ite.getRichiestaMedia().getRicercaTransfert().isRitorno())
				|| (gestCorseMedie_ite.getRichiestaMedia().getRicercaTransfert().getApprovazioneAndata() == Constants.NON_APPROVATA
					&& gestCorseMedie_ite.getRichiestaMedia().getRicercaTransfert().getApprovazioneRitorno() == Constants.NON_APPROVATA
					&& gestCorseMedie_ite.getRichiestaMedia().getRicercaTransfert().isRitorno())){
				joCorsaMedia.put("corsaNonApprovata", true);
			}else{
				if(!gestCorseMedie_ite.isChiamataPrenotata()){
					if(richAutistMedioPrenotata == null || richAutistMedioPrenotata.size() == 0){
						joCorsaMedia.put("corsaPrenotabile", true);
					}else{
						joCorsaMedia.put("corsaGiaPrenotata", true);
					}
				}else{
					joCorsaMedia.put("corsaRiservata", true);
				}
			}
			joCorsaMedia.put("idcorsa", gestCorseMedie_ite.getRichiestaMedia().getRicercaTransfert().getId());
			joCorsaMedia.put("idcorsaMedia", gestCorseMedie_ite.getId());
			if(gestCorseMedie_ite.getRichiestaMedia().getRicercaTransfert().isRitorno()){
				joCorsaMedia.put("intestazione", "CORSA DISP - A/R - "+DateUtil.FORMATO_DATA_ORA.format(gestCorseMedie_ite.getRichiestaMedia().getRicercaTransfert().getDataOraPrelevamentoDate()) +
							" - "+DateUtil.FORMATO_DATA_ORA.format(gestCorseMedie_ite.getRichiestaMedia().getRicercaTransfert().getDataOraRitornoDate()));
			}else{
				joCorsaMedia.put("intestazione", "CORSA DISP - SOLO ANDATA - "+DateUtil.FORMATO_DATA_ORA.format(gestCorseMedie_ite.getRichiestaMedia().getRicercaTransfert().getDataOraPrelevamentoDate()));
			}
			joCorsaMedia = DammiApprovazione_e_CollapsePanel(gestCorseMedie_ite.getRichiestaMedia().getRicercaTransfert().isCollapsePanelCorseAdmin(), 
						gestCorseMedie_ite.getRichiestaMedia().getRicercaTransfert().getApprovazioneAndata(), 
							gestCorseMedie_ite.getRichiestaMedia().getRicercaTransfert().getApprovazioneRitorno(), joCorsaMedia);
			joCorsaMedia.put("numPasseggeri", gestCorseMedie_ite.getRichiestaMedia().getRicercaTransfert().getNumeroPasseggeri() );
			joCorsaMedia.put("notePerAutista", gestCorseMedie_ite.getRichiestaMedia().getRicercaTransfert().getNotePerAutista() );
			joCorsaMedia.put("fullNameCliente", gestCorseMedie_ite.getRichiestaMedia().getRicercaTransfert().getUser().getFullName() );
			//joCorsaMedia.put("telCliente", gestCorseMedie_ite.getRicercaTransfert().getUser().getPhoneNumber() );
			joCorsaMedia.put("noteCliente", gestCorseMedie_ite.getRichiestaMedia().getRicercaTransfert().getUser().getNote() );
			joCorsaMedia.put("prezzoCliente", gestCorseMedie_ite.getRichiestaMedia().getPrezzoTotaleCliente().toString() );
			joCorsaMedia.put("classeAutoveicoloSceltaCliente", ApplicationMessagesUtil.DammiMessageSource( gestCorseMedie_ite.getRichiestaMedia().getClasseAutoveicolo().getNome(), 
					new Object[] {}, request.getLocale()));
			joCorsaMedia.put("descrizioneCategorieAuto", AutoveicoloUtil.DammiDescrizioneCategorieAutoMap(request.getLocale()).get(gestCorseMedie_ite.getRichiestaMedia().getClasseAutoveicolo().getId()));
			String rimborsoCliente = ((gestCorseMedie_ite.getRichiestaMedia().getRimborsoCliente() != null) 
					? gestCorseMedie_ite.getRichiestaMedia().getRimborsoCliente().toString() : BigDecimal.ZERO.setScale(2).toString());
			joCorsaMedia.put("rimborsoCliente", rimborsoCliente );
			joCorsaMedia.put("nomeTelefonoPasseggero", gestCorseMedie_ite.getRichiestaMedia().getRicercaTransfert().getNomeTelefonoPasseggero());
			joCorsaMedia.put("nomePasseggero", gestCorseMedie_ite.getRichiestaMedia().getRicercaTransfert().getNomePasseggero());
			joCorsaMedia.put("telefonoPasseggero", gestCorseMedie_ite.getRichiestaMedia().getRicercaTransfert().getTelefonoPasseggero());
			joCorsaMedia.put("maggiorazioneNotturna", (gestCorseMedie_ite.getRichiestaMedia().getMaggiorazioneNotturna() != null) ? gestCorseMedie_ite.getRichiestaMedia().getMaggiorazioneNotturna().toString() : BigDecimal.ZERO.setScale(2).toString() );
			// ANDATA
			joCorsaMedia = PanelHeading_Andata("andata", gestCorseMedie_ite.getRichiestaMedia().getRicercaTransfert(), joCorsaMedia);
			// RITORNO
			if(gestCorseMedie_ite.getRichiestaMedia().getRicercaTransfert().isRitorno()){
				joCorsaMedia = PanelHeading_Ritorno("ritorno", gestCorseMedie_ite.getRichiestaMedia().getRicercaTransfert(), joCorsaMedia);
			}
			joCorsaMedia.put("corseEffAutista", gestCorseMedie_ite.getAutista().getNumCorseEseguite());
			joCorsaMedia.put("percServAutista", gestCorseMedie_ite.getRichiestaMedia().getRicercaTransfert().getPercentualeServizioRichiestaMediaScelta() /*gestCorseMedie_ite.getAutista().getPercentualeServizio()*/);
			joCorsaMedia.put("prezzoCommServ", gestCorseMedie_ite.getPrezzoCommissioneServizio().toString());
			String prezzoAutista = "";
			if(gestCorseMedie_ite.getRichiestaMedia().getRicercaTransfert().isPagamentoParziale()){
				prezzoAutista = ApplicationMessagesUtil.DammiMessageSource("prezzo.autista.contanti", new String[]{
						gestCorseMedie_ite.getPrezzoTotaleAutista().toString()}, request.getLocale());
			}else{
				prezzoAutista = gestCorseMedie_ite.getPrezzoTotaleAutista().toString()+"\u20AC (prezzo autista)";
			}
			if(gestCorseMedie_ite.getRichiestaMedia().getMaggiorazioneNotturna() != null){
				prezzoAutista = prezzoAutista + ApplicationMessagesUtil.DammiMessageSource("prezzo.autista.inclusa.maggiorazione.notturna", new String[]{
						gestCorseMedie_ite.getRichiestaMedia().getMaggiorazioneNotturna().toString()}, request.getLocale());
			}
			joCorsaMedia.put("tokenAutista", gestCorseMedie_ite.getTokenAutista());
			joCorsaMedia.put("prezzoAutista", prezzoAutista);
			joCorsaMedia.put("tariffaPerKm", gestCorseMedie_ite.getTariffaPerKm().toString());
			joCorsaMedia.put("maggiorazioneNotturna", (gestCorseMedie_ite.getRichiestaMedia().getMaggiorazioneNotturna() != null) ? gestCorseMedie_ite.getRichiestaMedia().getMaggiorazioneNotturna().toString() : null);
			joCorsaMedia.put("idAutista", gestCorseMedie_ite.getAutista().getId());
			joCorsaMedia.put("fullNameAutisa", gestCorseMedie_ite.getAutista().getUser().getFullName());
			joCorsaMedia.put("autoveicoloRichiesto", AutoveicoloUtil.DammiAutiveicoliRichiestiAutistaList_String(gestCorseMedie_ite.getRichiestaMediaAutistaAutoveicolo(), request));
			corseARRAY.put(joCorsaMedia);
		}
		return corseARRAY;
	}

	/**
	 *  CORSE MEDIE ADMIN
	 */
	public JSONArray CorseMedieAdmin(List<GestioneCorseMedieAdmin> corseAutistaRicTransfertMedioList, JSONArray corseARRAY, HttpServletRequest request){
		for(GestioneCorseMedieAdmin gestCorseMedie_ite: corseAutistaRicTransfertMedioList){
			JSONObject joCorsaMedia = new JSONObject();
			if(gestCorseMedie_ite.getRicTransfert().getRitardo() != null) {
				Ritardi ritardo =  ritardiDao.get( gestCorseMedie_ite.getRicTransfert().getRitardo() );
				JSONObject ritardoJS = new JSONObject();
				ritardoJS.put("id", ritardo.getId() );
				if(!ritardo.isPagatoAndata()){
					ritardoJS.put("numeroMezzoreAndata", ritardo.getNumeroMezzoreRitardiAndata());
					ritardoJS.put("pagatoAndata", ritardo.isPagatoAndata() );
				}
				if(gestCorseMedie_ite.getRicTransfert().isRitorno()){
					if(!ritardo.isPagatoRitorno()){
						ritardoJS.put("numeroMezzoreRitorno", ritardo.getNumeroMezzoreRitardiRitorno());
						ritardoJS.put("pagatoRitorno", ritardo.isPagatoRitorno() );
					}
				}
				joCorsaMedia.put("ritardo", ritardoJS);
			}
			DammiSupplemento_Sintesti(gestCorseMedie_ite.getRicTransfert(), joCorsaMedia);
			joCorsaMedia.put("getTimeAndata", gestCorseMedie_ite.getRicTransfert().getDataOraPrelevamentoDate().getTime());
			joCorsaMedia.put("template", "MEDIA");
			joCorsaMedia.put("idcorsa", gestCorseMedie_ite.getRicTransfert().getId());
			if(gestCorseMedie_ite.getRicTransfert().isRitorno()){
				joCorsaMedia.put("intestazione", "CORSA - A/R - "+DateUtil.FORMATO_DATA_ORA.format(gestCorseMedie_ite.getRicTransfert().getDataOraPrelevamentoDate())+" - "+DateUtil.FORMATO_DATA_ORA.format(gestCorseMedie_ite.getRicTransfert().getDataOraRitornoDate()));
			}else{
				joCorsaMedia.put("intestazione", "CORSA - SOLO ANDATA - "+DateUtil.FORMATO_DATA_ORA.format(gestCorseMedie_ite.getRicTransfert().getDataOraPrelevamentoDate()));
			}
			joCorsaMedia = DammiApprovazione_e_CollapsePanel(gestCorseMedie_ite.getRicTransfert().isCollapsePanelCorseAdmin(), gestCorseMedie_ite.getRicTransfert().getApprovazioneAndata(), 
					gestCorseMedie_ite.getRicTransfert().getApprovazioneRitorno(), joCorsaMedia);
			joCorsaMedia.put("numPasseggeri", gestCorseMedie_ite.getRicTransfert().getNumeroPasseggeri() );
			joCorsaMedia.put("notePerAutista", gestCorseMedie_ite.getRicTransfert().getNotePerAutista() );
			joCorsaMedia.put("noteCorsa", gestCorseMedie_ite.getRicTransfert().getNote() );
			joCorsaMedia.put("idAutistaAssegnato", gestCorseMedie_ite.getIdAutistaAssegnato() );
			joCorsaMedia.put("fullNameCliente", gestCorseMedie_ite.getRicTransfert().getUser().getFullName() );
			if(request.isUserInRole(Constants.ADMIN_ROLE)){
				joCorsaMedia.put("telCliente", gestCorseMedie_ite.getRicTransfert().getUser().getPhoneNumber() );
			}else{
				joCorsaMedia.put("telCliente", "###########" );
			}
			joCorsaMedia.put("noteCliente", gestCorseMedie_ite.getRicTransfert().getUser().getNote() );
			
			String prezzoCliente = "";
			if(gestCorseMedie_ite.getRicTransfert().isPagamentoParziale()){
				prezzoCliente = ApplicationMessagesUtil.DammiMessageSource("prezzo.cliente.parziale.piu.prezzo.autista", new String[]{
						gestCorseMedie_ite.getRicTransfert().getRichiestaMediaScelta().getPrezzoCommissioneServizio().toString(), 
						gestCorseMedie_ite.getRicTransfert().getRichiestaMediaScelta().getPrezzoTotaleAutista().toString()}, request.getLocale());
			}else{
				prezzoCliente = gestCorseMedie_ite.getPrezzoCliente()+"\u20AC".toString();
			}
			if(gestCorseMedie_ite.getMaggiorazioneNotturna() != null){
				prezzoCliente = prezzoCliente + ApplicationMessagesUtil.DammiMessageSource("prezzo.autista.inclusa.maggiorazione.notturna", new String[]{
						gestCorseMedie_ite.getMaggiorazioneNotturna().toString()}, request.getLocale());
			}
			joCorsaMedia.put("prezzoCliente", prezzoCliente);
			joCorsaMedia.put("classeAutoveicoloSceltaCliente", ApplicationMessagesUtil.DammiMessageSource( gestCorseMedie_ite.getClasseAutoveicoloSceltaCliente().getNome(), 
					new Object[] { }, request.getLocale()));
			joCorsaMedia.put("descrizioneCategorieAuto", AutoveicoloUtil.DammiDescrizioneCategorieAutoMap(request.getLocale()).get(gestCorseMedie_ite.getClasseAutoveicoloSceltaCliente().getId()));
			
			String rimborsoCliente = ((gestCorseMedie_ite.getRimborsoCliente() != null) ? gestCorseMedie_ite.getRimborsoCliente().toString() : BigDecimal.ZERO.setScale(2).toString());
			joCorsaMedia.put("rimborsoCliente", rimborsoCliente );
			joCorsaMedia.put("nomeTelefonoPasseggero", gestCorseMedie_ite.getRicTransfert().getNomeTelefonoPasseggero());
			joCorsaMedia.put("nomePasseggero", gestCorseMedie_ite.getRicTransfert().getNomePasseggero());
			joCorsaMedia.put("telefonoPasseggero", gestCorseMedie_ite.getRicTransfert().getTelefonoPasseggero());
			joCorsaMedia.put("maggiorazioneNotturna", (gestCorseMedie_ite.getMaggiorazioneNotturna() != null) ? gestCorseMedie_ite.getMaggiorazioneNotturna().toString() : BigDecimal.ZERO.setScale(2).toString() );
			// ANDATA
			joCorsaMedia = PanelHeading_Andata("andata", gestCorseMedie_ite.getRicTransfert(), joCorsaMedia);
			// RITORNO
			if(gestCorseMedie_ite.getRicTransfert().isRitorno()){
				joCorsaMedia = PanelHeading_Ritorno("ritorno", gestCorseMedie_ite.getRicTransfert(), joCorsaMedia);
			}
			// AUTISTI
			JSONArray corsaMediaAutistiARRAY = new JSONArray();
			for(GestioneCorseMedieAdmin.GestioneCorseMedieAdminAutisti richAutistaMedio: gestCorseMedie_ite.getGestioneCorseMedieAdminAutisti() ){
				JSONObject joCorsaMediaAutisti = new JSONObject();
				JSONObject joCorsaMediaAutista = new JSONObject();
				joCorsaMediaAutista.put("fullNameAutisa", richAutistaMedio.getFullNameAutisa() );
				joCorsaMediaAutista.put("idAutista", richAutistaMedio.getIdAutista() );
				joCorsaMediaAutista.put("tokenAutista", richAutistaMedio.getTokenAutista() );
				joCorsaMediaAutista.put("assegnato", richAutistaMedio.isAssegnato() );
				joCorsaMediaAutista.put("telAutista", richAutistaMedio.getTelAutista());
				joCorsaMediaAutista.put("corseEffAutista", richAutistaMedio.getCorseEffAutista());
				joCorsaMediaAutista.put("percServAutista", gestCorseMedie_ite.getRicTransfert().getPercentualeServizioRichiestaMediaScelta());
				joCorsaMediaAutista.put("prezzoCommServ", richAutistaMedio.getPrezzoCommServ());
				joCorsaMediaAutista.put("prezzoAutista", richAutistaMedio.getPrezzoAutista());
				joCorsaMediaAutista.put("tariffaPerKm", richAutistaMedio.getTariffaPerKm().toString());
				joCorsaMediaAutista.put("maggiorazioneNotturna", (richAutistaMedio.getMaggiorazioneNotturna() != null) ? richAutistaMedio.getMaggiorazioneNotturna().toString() : null);
				joCorsaMediaAutista.put("ordineChiamataPrenotata", richAutistaMedio.getOrdineChiamataPrenotata());
				joCorsaMediaAutista.put("ordineAutista", richAutistaMedio.getOrdineAutista());
				joCorsaMediaAutista.put("invioSms", richAutistaMedio.isInvioSms());
				joCorsaMediaAutista.put("autoveicoloRichiesto", AutoveicoloUtil.DammiAutiveicoliRichiestiAutistaList_String(richAutistaMedio.getAutoveicoliRichiestiList(), request) );
				joCorsaMediaAutista.put("noteAutista", richAutistaMedio.getNoteAutista());
				joCorsaMediaAutisti.put("autista", joCorsaMediaAutista);
				corsaMediaAutistiARRAY.put( joCorsaMediaAutisti );
			}
			joCorsaMedia.put("autisti", corsaMediaAutistiARRAY);
			corseARRAY.put(joCorsaMedia);
		}
		return corseARRAY;
	}
	

}
