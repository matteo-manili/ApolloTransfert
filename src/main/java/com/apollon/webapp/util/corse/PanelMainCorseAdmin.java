package com.apollon.webapp.util.corse;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.apollon.dao.RicercaTransfertDao;
import com.apollon.model.RicercaTransfert;
import com.apollon.model.RichiestaAutistaParticolare;
import com.apollon.webapp.util.bean.GestioneCorseMedieAdmin;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public class PanelMainCorseAdmin extends PanelCorseTemplateUtil {
	
	private static RicercaTransfertDao ricercaTransfertDao = (RicercaTransfertDao) contextDao.getBean("RicercaTransfertDao");
	private JSONObject mainCorse;
	public JSONObject getMainCorse() {
		return mainCorse;
	}
	
	public PanelMainCorseAdmin(boolean inApprov, boolean approv, boolean nonApprov, Date fromDate, Date toDate, Long idRic, Locale locale, HttpServletRequest request) {
		this.mainCorse = new JSONObject();
		JSONArray corseARRAY = new JSONArray();
		//---- CORSA AGENDA_AUTISTA
		long startTime = System.nanoTime();
		List<RicercaTransfert> ricercaTransfertAgendaAutistaList = ricercaTransfertDao.getCorseAutistaAgendaAutistaALL(inApprov, approv, nonApprov, fromDate, toDate, idRic);
		System.out.println("SECONDI: "+((double)(System.nanoTime() - startTime)) / 1000000000.0);
		//---- CORSA MULTIPLA
		startTime = System.nanoTime();
		List<RicercaTransfert> corseAutistaRicTransfertMultiplaList = ricercaTransfertDao.getCorseAutistaRichiestaAutistaMultiplaALL(inApprov, approv, nonApprov, fromDate, toDate, idRic);
		System.out.println("SECONDI: "+((double)(System.nanoTime() - startTime)) / 1000000000.0);
		//---- CORSA PARTICOLARE
		startTime = System.nanoTime();
		List<RichiestaAutistaParticolare> corseAutistaRicTransfertParticList = ricercaTransfertDao.getCorseAutistaRichiestaAutistaParticolareALL(inApprov, approv, nonApprov, fromDate, toDate, idRic);
		System.out.println("SECONDI: "+((double)(System.nanoTime() - startTime)) / 1000000000.0);
		//---- CORSA MEDIA
		startTime = System.nanoTime();
		List<GestioneCorseMedieAdmin> corseAutistaRicTransfertMedioList = ricercaTransfertDao.getCorseAutistaRichiestaAutistaMedioALL(inApprov, approv, nonApprov, fromDate, toDate, idRic);
		System.out.println("SECONDI: "+((double)(System.nanoTime() - startTime)) / 1000000000.0);
		
		corseARRAY = CorsaAgendaAutista_ClienteTemplate(ricercaTransfertAgendaAutistaList, corseARRAY, request);
		corseARRAY = CorsaMultiplaTemplate_2(corseAutistaRicTransfertMultiplaList, corseARRAY, request);
		corseARRAY = CorsaParticolareTemplate_2(corseAutistaRicTransfertParticList, corseARRAY, request);
		corseARRAY = CorseMedieAdmin(corseAutistaRicTransfertMedioList, corseARRAY, request);
		
		// ordinamento
	    JSONArray sortedJsonArray = new JSONArray();

	    List<JSONObject> jsonValues = new ArrayList<JSONObject>();
	    for (int i = 0; i < corseARRAY.length(); i++) {
	        jsonValues.add(corseARRAY.getJSONObject(i));
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
	    for (int i = 0; i < corseARRAY.length(); i++) {
	        sortedJsonArray.put(jsonValues.get(i));
	    }
		
		// infilo dentro
		//mainCorse.put("mainCorse", corseARRAY);
	    this.mainCorse.put("mainCorse", sortedJsonArray);
	}


}
