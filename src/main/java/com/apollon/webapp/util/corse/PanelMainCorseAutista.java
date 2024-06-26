package com.apollon.webapp.util.corse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.apollon.Constants;
import com.apollon.model.RichiestaMediaAutista;
import com.apollon.model.RicercaTransfert;
import com.apollon.model.RichiestaAutistaParticolare;
import com.apollon.model.User;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public class PanelMainCorseAutista extends PanelCorseTemplateUtil {
	
	private JSONObject mainCorse;
	private int corseDaEseguire;
	private int corseEseguite;
	private int corseDisponibili;
	
	public JSONObject getMainCorse() {
		return mainCorse;
	}
	public int getCorseDaEseguire() {
		return corseDaEseguire;
	}
	public int getCorseEseguite() {
		return corseEseguite;
	}
	public int getCorseDisponibili() {
		return corseDisponibili;
	}
	
	/**
	 * Mi restituisce tutte le corse Particolari e Medie (Da Eseguire, Disponibili ed Eseguite) relative ad un autista, popolando l'oggetto JSON
	 */
	public PanelMainCorseAutista(User user, Long idAutista, Locale locale, HttpServletRequest request) {
		
		List<RicercaTransfert> ricTransfertList = ricercaTransfertDao.getCorseAutistaAgendaAutista(user.getId());
		List<RichiestaAutistaParticolare> corseAutistaRicTransfertMultiploList = ricercaTransfertDao.getCorseAutistaRichiestaAutistaMultiplo(user.getId(), null);
		List<RichiestaAutistaParticolare> corseAutistaRicTransfertParticList = ricercaTransfertDao.getCorseAutistaRichiestaAutistaParticolare(user.getId(), null);
		List<RichiestaMediaAutista> corseAutistaRicTransfertMedioList = ricercaTransfertDao.getCorseAutistaRichiestaAutistaMedio(user.getId(), null);
		List<RichiestaMediaAutista> corseDisponibiliMedioList = ricercaTransfertDao.getCorsaMediaDisponibile_by_idAutista(user.getId(), null);
		
		JSONArray corseARRAY = new JSONArray();
		corseARRAY = CorsaAgendaAutista_AutistaTemplate(ricTransfertList, idAutista, corseARRAY, request);
		corseARRAY = CorsaParticolareTemplate_2(corseAutistaRicTransfertMultiploList, corseARRAY, request);
		corseARRAY = CorsaParticolareTemplate_2(corseAutistaRicTransfertParticList, corseARRAY, request);
		corseARRAY = CorseMedieAutista(corseAutistaRicTransfertMedioList, corseARRAY, request);
		corseARRAY = CorseMedieDisponibiliAutista(corseDisponibiliMedioList, corseARRAY, request);
		
		this.corseDaEseguire = super.corseDaEseguire;
		this.corseDisponibili = super.corseDisponibili;
		this.corseEseguite = super.corseEseguite;

		// ordinamento
		String jsonArrStr = corseARRAY.toString();
	    JSONArray jsonArr = new JSONArray(jsonArrStr);
	    JSONArray sortedJsonArray = new JSONArray();
	    List<JSONObject> jsonValues = new ArrayList<JSONObject>();
	    for (int i = 0; i < jsonArr.length(); i++) {
	        jsonValues.add(jsonArr.getJSONObject(i));
	    }
	    Collections.sort( jsonValues, new Comparator<JSONObject>() {
	        //You can change "Name" with "ID" if you want to sort by ID
	        private static final String KEY_NAME = "getTimeAndata";

	        @Override
	        public int compare(JSONObject a, JSONObject b) {
	        	Long valA = new Long(0);
	        	Long valB = new Long(0);
	            try {
	                valA = (Long) a.get(KEY_NAME);
	                valB = (Long) b.get(KEY_NAME);
	            } 
	            catch (JSONException e) {
	                //do something
	            }

	            return valA.compareTo(valB);
	            //if you want to change the sort order, simply use the following:
	            //return -valA.compareTo(valB);
	        }
	    });
	    for (int i = 0; i < jsonArr.length(); i++) {
	    	sortedJsonArray.put(jsonValues.get(i));
	    }
		// infilo dentro
		//mainCorse.put("mainCorse", corseARRAY);
	    this.mainCorse = new JSONObject();
	    this.mainCorse.put("mainCorse", sortedJsonArray);
	}
	
	/**
	 * Mi restituisce una Corsa Particolare o Media (Da Eseguire, Disponibili ed Eseguite) relative ad un autista, popolando l'oggetto JSON
	 */
	public PanelMainCorseAutista(String token, Locale locale, HttpServletRequest request) {
		RichiestaAutistaParticolare corsaAutRicTransfMultiploPrenot = PanelCorseTemplateUtil.controllaTipoToken(token, Constants.SERVIZIO_MULTIPLO) ?
				ricercaTransfertDao.getCorsaAutRicTransfMultiploPrenotazione(token) : new RichiestaAutistaParticolare();
		List<RichiestaAutistaParticolare> corseAutistaRicTransfertMultiploList = PanelCorseTemplateUtil.controllaTipoToken(token, Constants.SERVIZIO_MULTIPLO) ? 
					ricercaTransfertDao.getCorseAutistaRichiestaAutistaMultiplo(0, token) : new ArrayList<RichiestaAutistaParticolare>();
		
		RichiestaAutistaParticolare corsaAutRicTransfPartPrenot = PanelCorseTemplateUtil.controllaTipoToken(token, Constants.SERVIZIO_PARTICOLARE) ?
				ricercaTransfertDao.getCorsaAutRicTransfPartPrenotazione(token) : new RichiestaAutistaParticolare();
		List<RichiestaAutistaParticolare> corseAutistaRicTransfertParticList = PanelCorseTemplateUtil.controllaTipoToken(token, Constants.SERVIZIO_PARTICOLARE) ? 
					ricercaTransfertDao.getCorseAutistaRichiestaAutistaParticolare(0, token) : new ArrayList<RichiestaAutistaParticolare>();
		
		List<RichiestaMediaAutista> corseAutistaRicTransfertMedioList = PanelCorseTemplateUtil.controllaTipoToken(token, Constants.SERVIZIO_STANDARD) ?
					ricercaTransfertDao.getCorseAutistaRichiestaAutistaMedio(0, token) : new ArrayList<RichiestaMediaAutista>();
		List<RichiestaMediaAutista> corseDisponibiliMedioList = PanelCorseTemplateUtil.controllaTipoToken(token, Constants.SERVIZIO_STANDARD) ? 
					ricercaTransfertDao.getCorsaMediaDisponibile_by_idAutista(0, token) : new ArrayList<RichiestaMediaAutista>();

		JSONArray corseARRAY = new JSONArray();
		
		corseARRAY = CorsaParticolareTemplate_1(corsaAutRicTransfMultiploPrenot, corseARRAY, request);
		corseARRAY = CorsaParticolareTemplate_2(corseAutistaRicTransfertMultiploList, corseARRAY, request);
		
		corseARRAY = CorsaParticolareTemplate_1(corsaAutRicTransfPartPrenot, corseARRAY, request);
		corseARRAY = CorsaParticolareTemplate_2(corseAutistaRicTransfertParticList, corseARRAY, request);
		
		corseARRAY = CorseMedieAutista(corseAutistaRicTransfertMedioList, corseARRAY, request);
		corseARRAY = CorseMedieDisponibiliAutista(corseDisponibiliMedioList, corseARRAY, request);
		/*
		if(richAutistPart != null){
			List<RichiestaAutistaParticolare> corsaAutistaRicTransfertParticList = new ArrayList<RichiestaAutistaParticolare>();
			corsaAutistaRicTransfertParticList.add(richAutistPart);
			corseARRAY = corseParticolariAdmin(corsaAutistaRicTransfertParticList, corseARRAY);
		}
		
		if(richAutistMedio != null){
			List<RichiestaAutistaMedio> corsaAutistaRicTransfertMedioList = new ArrayList<RichiestaAutistaMedio>();
			corsaAutistaRicTransfertMedioList.add(richAutistMedio);
			corseARRAY = corseMedieDisponibiliAutista(corsaAutistaRicTransfertMedioList, corseARRAY);
		}
		*/
	    this.mainCorse = new JSONObject();
	    this.mainCorse.put("mainCorse", corseARRAY);
	}

	/**
	 * Mi restituisce una Corsa AgendaAutistaScelta (Da Eseguire ed Eseguite) relative ad un autista, popolando l'oggetto JSON
	 */
	public PanelMainCorseAutista(RicercaTransfert ricTransfert, long idAutista, Locale locale, HttpServletRequest request) {
		List<RicercaTransfert> ricTransfertList = new ArrayList<RicercaTransfert>();
		ricTransfertList.add(ricTransfert);
		JSONArray corseARRAY = new JSONArray();
		corseARRAY = CorsaAgendaAutista_AutistaTemplate(ricTransfertList, idAutista, corseARRAY, request);
	    this.mainCorse = new JSONObject();
	    this.mainCorse.put("mainCorse", corseARRAY);
	}
	
	
	
}
