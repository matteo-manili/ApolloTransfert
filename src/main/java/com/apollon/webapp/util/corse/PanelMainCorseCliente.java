package com.apollon.webapp.util.corse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.apollon.dao.RicercaTransfertDao;
import com.apollon.model.RichiestaMediaAutista;
import com.apollon.model.RicercaTransfert;
import com.apollon.model.RichiestaAutistaParticolare;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public class PanelMainCorseCliente extends PanelCorseTemplateUtil {
	
	private static RicercaTransfertDao ricercaTransfertDao = (RicercaTransfertDao) contextDao.getBean("RicercaTransfertDao");

	private JSONObject mainCorse;
	private int corseDaEseguireCliente;
	private int corseEseguiteCliente;
	
	public JSONObject getMainCorse() {
		return mainCorse;
	}
	public int getCorseDaEseguireCliente() {
		return corseDaEseguireCliente;
	}
	public int getCorseEseguiteCliente() {
		return corseEseguiteCliente;
	}
	
	/**
	 * Fa vedere il Pannello Corse Cliente ma con solo la corsa passata (per sicurezza magari l'hotel passa il link al suo cliente)
	 * nel caso dei link senza autenticazione https://localhost:8443/apollon/visualizza-corsa-cliente?token=admin@6485
	 */
	public PanelMainCorseCliente(HttpServletRequest request, Long idRicercaTransfert) {
		DammiCorse(null, request, idRicercaTransfert);
	}

	/**
	 * Fa vedere il Pannello Corse con tutte le corse dello user in home-user
	 */
	public PanelMainCorseCliente(Long idUser, HttpServletRequest request) {
		DammiCorse(idUser, request, null);
	}
	
	private void DammiCorse(Long idUser, HttpServletRequest request, Long idRicercaTransfert) {
		List<RicercaTransfert> corseAutistaAgendaAutistaList = ricercaTransfertDao.getCorseClienteAgendaAutista(idUser, idRicercaTransfert);
		List<RichiestaMediaAutista> corseAutistaRicTransfertMedioList = ricercaTransfertDao.getCorseClienteRichiestaAutistaMedio(idUser, idRicercaTransfert);
		List<RichiestaAutistaParticolare> corseAutistaRicTransfertParticList = ricercaTransfertDao.getCorseClienteRichiestaAutistaParticolare(idUser, idRicercaTransfert);
		List<RicercaTransfert> corseAutistaRicTransfertMultiploList = ricercaTransfertDao.getCorseClienteRichiestaAutistaMultiplo(idUser, idRicercaTransfert);
		JSONArray corseARRAY = new JSONArray();
		corseARRAY = CorsaAgendaAutista_ClienteTemplate(corseAutistaAgendaAutistaList, corseARRAY, request);
		corseARRAY = CorseMedieAutista(corseAutistaRicTransfertMedioList, corseARRAY, request);
		corseARRAY = CorsaParticolareTemplate_2(corseAutistaRicTransfertParticList, corseARRAY, request);
		corseARRAY = CorsaMultiplaTemplate_2(corseAutistaRicTransfertMultiploList, corseARRAY, request);
		this.corseDaEseguireCliente = super.corseDaEseguire;
		this.corseEseguiteCliente = super.corseEseguite;
		// ordinamento
	    JSONArray jsonArr = new JSONArray( corseARRAY.toString() );
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
		
	
	
	
}
