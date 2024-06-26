package com.apollon.webapp.util.controller.documenti;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.apollon.dao.AutistaLicenzeNccDao;
import com.apollon.dao.AutistaSottoAutistiDao;
import com.apollon.dao.AutoveicoloDao;
import com.apollon.model.Autista;
import com.apollon.model.AutistaLicenzeNcc;
import com.apollon.model.AutistaSottoAutisti;
import com.apollon.model.Autoveicolo;
import com.apollon.webapp.util.ApplicationUtils;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public class DocumentiInfoUtil extends ApplicationUtils{
	private static AutistaLicenzeNccDao autistaLicenzeNccDao = (AutistaLicenzeNccDao) contextDao.getBean("AutistaLicenzeNccDao");
	private static AutoveicoloDao autoveicoloDao = (AutoveicoloDao) contextDao.getBean("AutoveicoloDao");
	private static AutistaSottoAutistiDao autistaSottoAutistiDao = (AutistaSottoAutistiDao) contextDao.getBean("AutistaSottoAutistiDao");
	
	private Autista autista;
	public Map<String, Boolean> tabDocCheck = new HashMap<String, Boolean>();
	public boolean documentiCompletatiInclusoContratto;
	public boolean documentiCompletatiEsclusoContratto;
	public boolean documentiApprovatiEsclusoContratto;
	public String documentiCompletatiFrazione;
	public String documentiApprovatiFrazione;
	
	public DocumentiInfoUtil(Autista autista) {
		this.autista = autista;
		ControlliDocumentiCompletati();
		
		
		for (Map.Entry<String, Boolean> mapTabCheck_ite : tabDocCheck.entrySet()){
			if(mapTabCheck_ite.getKey().contains("completato") && !mapTabCheck_ite.getKey().contains("contrattoTab-completato")){ 
				if(mapTabCheck_ite.getValue() == false){
					documentiCompletatiEsclusoContratto = false;
					break;
				}else{
					documentiCompletatiEsclusoContratto = true;
				}
			}
		}
		
		for (Map.Entry<String, Boolean> mapTabCheck_ite : tabDocCheck.entrySet()){
			if(mapTabCheck_ite.getKey().contains("completato")){ 
				if(mapTabCheck_ite.getValue() == false){
					documentiCompletatiInclusoContratto = false;
					break;
				}else{
					documentiCompletatiInclusoContratto = true;
				}
			}
		}
		
		for(Map.Entry<String, Boolean> mapTabCheck_ite : tabDocCheck.entrySet()){
			if(mapTabCheck_ite.getKey().contains("approv") && !mapTabCheck_ite.getKey().contains("contrattoTab-approv")){
				if(mapTabCheck_ite.getValue() == false){
					documentiApprovatiEsclusoContratto = false;
					break;
				}else{
					documentiApprovatiEsclusoContratto = true;
				}
				
			}
		}
		
		int i = 0; int iTot = 0;
		for (Map.Entry<String, Boolean> mapTabCheck_ite : tabDocCheck.entrySet()){
			if(mapTabCheck_ite.getKey().contains("completato")){
				iTot++;
				if(mapTabCheck_ite.getValue() == true){
					i++;
				}
			}
		}
		documentiCompletatiFrazione = Integer.toString(i) +"/"+ Integer.toString(iTot);
		i = 0; iTot = 0;
		for (Map.Entry<String, Boolean> mapTabCheck_ite : tabDocCheck.entrySet()){
			if(mapTabCheck_ite.getKey().contains("approv")){
				iTot++;
				if(mapTabCheck_ite.getValue() == true){
					i++;
				}
			}
		}
		documentiApprovatiFrazione = Integer.toString(i) +"/"+ Integer.toString(iTot);
	}
	
	
	/**
	 * CheckDocumenti
	 */
	private void ControlliDocumentiCompletati(){
		//Contratto
		if(autista.getAutistaDocumento().getNomeFileContratto() == null || autista.getAutistaDocumento().getNomeFileContratto().equals("")){
    		tabDocCheck.put("contrattoTab-completato", false);
    	}else{
    		tabDocCheck.put("contrattoTab-completato", true);
    	}
		if(autista.getAutistaDocumento().isApprovatoContratto()){
    		tabDocCheck.put("contrattoTab-approv", true);
    	}else{
    		tabDocCheck.put("contrattoTab-approv", false);
    	}
		
    	//Info Collaboratore
    	if(autista.getAutistaDocumento().getPartitaIva() == null || autista.getAutistaDocumento().getPartitaIva().equals("")
			|| autista.getAutistaDocumento().getPartitaIvaDenominazione() == null || autista.getAutistaDocumento().getPartitaIvaDenominazione().equals("")
			|| autista.getAutistaDocumento().getIban() == null || autista.getAutistaDocumento().getIban().equals("")
			|| autista.getAutistaDocumento().getDocumentiPatente() == null 
			|| autista.getAutistaDocumento().getDocumentiPatente().getNumeroPatente() == null || autista.getAutistaDocumento().getDocumentiPatente().getNumeroPatente().equals("")
			|| autista.getAutistaDocumento().getDocumentiPatente().getDataScadenzaPatente() == null || autista.getAutistaDocumento().getDocumentiPatente().getDataScadenzaPatente().equals("")
			|| autista.getAutistaDocumento().getDocumentiPatente().getNomeFilePatenteFronte() == null || autista.getAutistaDocumento().getDocumentiPatente().getNomeFilePatenteFronte().equals("")
    			){
    		tabDocCheck.put("collaboratoreTab-completato", false);
    	}else{
    		tabDocCheck.put("collaboratoreTab-completato", true);
    	}
    	
    	//Iscrizione Ruolo
    	if(!autista.isAzienda()){
    		if(autista.getAutistaDocumento().getDocumentiIscrizioneRuolo() == null
    			|| autista.getAutistaDocumento().getDocumentiIscrizioneRuolo().getNumeroRuoloConducenti() == null || autista.getAutistaDocumento().getDocumentiIscrizioneRuolo().getNumeroRuoloConducenti().equals("")
    			|| autista.getAutistaDocumento().getDocumentiIscrizioneRuolo().getDataIscrizioneRuoloConducenti() == null
    			|| autista.getAutistaDocumento().getDocumentiIscrizioneRuolo().getNomeFileDocumentoRuoloConducenti() == null || autista.getAutistaDocumento().getDocumentiIscrizioneRuolo().getNomeFileDocumentoRuoloConducenti().equals("")
        		|| autista.getAutistaDocumento().getDocumentiCap() == null 
    			|| autista.getAutistaDocumento().getDocumentiCap().getNumeroCAP() == null || autista.getAutistaDocumento().getDocumentiCap().getNumeroCAP().equals("") 
    			|| autista.getAutistaDocumento().getDocumentiCap().getDataScadenzaCAP() == null 
    			|| autista.getAutistaDocumento().getDocumentiCap().getNomeFileCAP() == null || autista.getAutistaDocumento().getDocumentiCap().getNomeFileCAP().equals("")){
        		tabDocCheck.put("iscrizioneRuoloTab-completato", false);
        	}else{
        		tabDocCheck.put("iscrizioneRuoloTab-completato", true);
        	}
    	}
    	
    	if(autista.getAutistaDocumento().isApprovatoDocumenti()){
    		tabDocCheck.put("collaboratoreTab-approv", true);
    		tabDocCheck.put("iscrizioneRuoloTab-approv", true);
    	}else{
    		tabDocCheck.put("collaboratoreTab-approv", false);
    		tabDocCheck.put("iscrizioneRuoloTab-approv", false);
    	}
    	
     	// Licenza-e NCC
    	List<AutistaLicenzeNcc> autistaLicenzeNccList = autistaLicenzeNccDao.getAutistaLicenzeNcc_By_Autista(autista.getId());
    	if(autistaLicenzeNccList != null && autistaLicenzeNccList.size() > 0){
	    	if(autistaLicenzeNccList.size() > 0 ){
	    		tabDocCheck.put("licenzeTab-completato", true);
	    	}else{
	    		tabDocCheck.put("licenzeTab-completato", false);
	    	}
	    	for(AutistaLicenzeNcc autistaLicenzeNccList_ite: autistaLicenzeNccList){
	    		if(autistaLicenzeNccList_ite.isApprovato()){
	    			tabDocCheck.put("licenzeTab-approv", true);
	    			break;
	    		}else{
	    			tabDocCheck.put("licenzeTab-approv", false);
	    		}
	    	}
    	}else{
    		tabDocCheck.put("licenzeTab-completato", false);
    		tabDocCheck.put("licenzeTab-approv", false);
    	}
    	
     	//Carta di Circolazione
    	List<Autoveicolo> listAuto = autoveicoloDao.getAutoveicoloByAutista(autista.getId(), false);
    	if(listAuto != null && listAuto.size() > 0){
	    	for(Autoveicolo listAuto_ite: listAuto){
	    		if(listAuto_ite.getAutoveicoloCartaCircolazione().getNomeFileCartaCircolazione() != null){
	    			tabDocCheck.put("cartaCircolazioneTab-completato", true);
	    			break;
	    		}else{
	    			tabDocCheck.put("cartaCircolazioneTab-completato", false);
	    		}
	    	}
	    	for(Autoveicolo listAuto_ite: listAuto){
	    		if(listAuto_ite.getAutoveicoloCartaCircolazione().getNomeFileCartaCircolazione() != null && listAuto_ite.getAutoveicoloCartaCircolazione().isApprovatoCartaCircolazione()){
	    			tabDocCheck.put("cartaCircolazioneTab-approv", true);
	    			break;
	    		}else{
	    			tabDocCheck.put("cartaCircolazioneTab-approv", false);
	    		}
	    	}
    	}else{
			tabDocCheck.put("cartaCircolazioneTab-completato", false);
			tabDocCheck.put("cartaCircolazioneTab-approv", false);
    	}
    	
    	// Dipendenti Autisti
    	if( autista.isAzienda() ){
    		List<AutistaSottoAutisti> autistaSottoAutistiList = autistaSottoAutistiDao.getAutistaSottoAutisti_By_Autista(autista.getId());
    		if( autistaSottoAutistiList != null && autistaSottoAutistiList.size() > 0){
    			tabDocCheck.put("dipendentiTab-completato", true);
    			for(AutistaSottoAutisti autistaSottoAutistiList_ite: autistaSottoAutistiList){
        			if(autistaSottoAutistiList_ite.isApprovato()){
        				tabDocCheck.put("dipendentiTab-approv", true);
        				break;
        			}else{
        				tabDocCheck.put("dipendentiTab-approv", false);
        			}
        		}
    		}else{
    			tabDocCheck.put("dipendentiTab-completato", false);
    			tabDocCheck.put("dipendentiTab-approv", false);
    		}
    		
    	}
    }

	
	
	/**
	 * Dettaglio Campi Assenti Info Collabotore
	 */
	public List<String> DettaglioDocumentiAssenti(){
    	List<String> listaCampi = new ArrayList<String>();
    	
    	//Contratto
    	if(this.documentiCompletatiEsclusoContratto == true && this.documentiApprovatiEsclusoContratto == true){
    		if(autista.getAutistaDocumento().getNomeFileContratto() == null || autista.getAutistaDocumento().getNomeFileContratto().equals("")){
    			listaCampi.add("Contratto (In allegato alla Email)");
        	}
    	}
    	
    	// info collaboratore
    	if(autista.getAutistaDocumento().getPartitaIva() == null || autista.getAutistaDocumento().getPartitaIva().equals("")){
    		listaCampi.add("Numero Partita Iva");
    	}
		if(autista.getAutistaDocumento().getPartitaIvaDenominazione() == null || autista.getAutistaDocumento().getPartitaIvaDenominazione().equals("")){
			listaCampi.add("Denominazione Partita Iva");
		}
		if(autista.getAutistaDocumento().getIban() == null || autista.getAutistaDocumento().getIban().equals("")){
			listaCampi.add("IBAN");
		}
		if(autista.getAutistaDocumento().getDocumentiPatente() == null || autista.getAutistaDocumento().getDocumentiPatente().getNumeroPatente() == null 
				|| autista.getAutistaDocumento().getDocumentiPatente().getNumeroPatente().equals("")){
			listaCampi.add("Numero Patente");
		}
		if(autista.getAutistaDocumento().getDocumentiPatente() == null || autista.getAutistaDocumento().getDocumentiPatente().getDataScadenzaPatente() == null 
				|| autista.getAutistaDocumento().getDocumentiPatente().getDataScadenzaPatente().equals("")){
			listaCampi.add("Scadenza Patente");
		}
		if(autista.getAutistaDocumento().getDocumentiPatente() == null || autista.getAutistaDocumento().getDocumentiPatente().getNomeFilePatenteFronte() == null 
				|| autista.getAutistaDocumento().getDocumentiPatente().getNomeFilePatenteFronte().equals("")){
			listaCampi.add("Documento Patente");
		}

		// Iscrizione al Ruolo Collaboratore
		if(!autista.isAzienda()){
			if(autista.getAutistaDocumento().getDocumentiIscrizioneRuolo() == null
	    			|| autista.getAutistaDocumento().getDocumentiIscrizioneRuolo().getNumeroRuoloConducenti() == null || autista.getAutistaDocumento().getDocumentiIscrizioneRuolo().getNumeroRuoloConducenti().equals("")){
				listaCampi.add("Numero Ruolo Conducenti");
			}
			if(autista.getAutistaDocumento().getDocumentiIscrizioneRuolo() == null || autista.getAutistaDocumento().getDocumentiIscrizioneRuolo().getDataIscrizioneRuoloConducenti() == null){
				listaCampi.add("Data Iscrizione Ruolo Conducenti");
			}
			if(autista.getAutistaDocumento().getDocumentiIscrizioneRuolo() == null || autista.getAutistaDocumento().getDocumentiIscrizioneRuolo().getNomeFileDocumentoRuoloConducenti() == null 
	    			|| autista.getAutistaDocumento().getDocumentiIscrizioneRuolo().getNomeFileDocumentoRuoloConducenti().equals("")){
				listaCampi.add("Documento Iscrizione Ruolo Conducenti");
			}
			if(autista.getAutistaDocumento().getDocumentiCap() == null || autista.getAutistaDocumento().getDocumentiCap().getNumeroCAP() == null 
					|| autista.getAutistaDocumento().getDocumentiCap().getNumeroCAP().equals("")){
				listaCampi.add("Numero CAP");
			}
			if(autista.getAutistaDocumento().getDocumentiCap() == null || autista.getAutistaDocumento().getDocumentiCap().getDataScadenzaCAP() == null ){
				listaCampi.add("Data Scadenza CAP");
			}
			if(autista.getAutistaDocumento().getDocumentiCap() == null || autista.getAutistaDocumento().getDocumentiCap().getNomeFileCAP() == null 
					|| autista.getAutistaDocumento().getDocumentiCap().getNomeFileCAP().equals("")){
				listaCampi.add("Documento CAP");
			}
		}
		
		// Licenza/e
		List<AutistaLicenzeNcc> autistaLicenzeNccList = autistaLicenzeNccDao.getAutistaLicenzeNcc_By_Autista(autista.getId());
    	if(autistaLicenzeNccList == null || autistaLicenzeNccList.size() <= 0){
	    		listaCampi.add("Licenza/e");
    	}
    	
    	// Dipendenti Autisti
    	if( autista.isAzienda() ){
    		List<AutistaSottoAutisti> autistaSottoAutistiList = autistaSottoAutistiDao.getAutistaSottoAutisti_By_Autista(autista.getId());
    		if(autistaSottoAutistiList.size() <= 0){
    			listaCampi.add("Dipendenti");
    		}
    	}
    	
    	//Carta di Circolazione
    	List<Autoveicolo> listAuto = autoveicoloDao.getAutoveicoloByAutista(autista.getId(), false);
    	if(listAuto != null && listAuto.size() > 0){
    		boolean flag = false;
	    	for(Autoveicolo listAuto_ite: listAuto){
	    		if(listAuto_ite.getAutoveicoloCartaCircolazione().getNomeFileCartaCircolazione() != null){
	    			flag = true;
	    			break;
	    		}
	    	}
	    	if(!flag){
	    		listaCampi.add("Carta di Circolazione");
	    	}
    	}
		
		return listaCampi;
	}
	
	
}
