package com.apollon.webapp.util.controller.autoveicolo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.apollon.Constants;
import com.apollon.dao.ClasseAutoveicoloDao;
import com.apollon.model.Autoveicolo;
import com.apollon.model.ClasseAutoveicolo;
import com.apollon.model.Disponibilita;
import com.apollon.model.DisponibilitaDate;
import com.apollon.model.RichiestaMediaAutistaAutoveicolo;
import com.apollon.util.UtilString;
import com.apollon.webapp.util.ApplicationUtils;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public class AutoveicoloUtil extends ApplicationUtils {
	
	private static ClasseAutoveicoloDao classeAutoveicoloDao = (ClasseAutoveicoloDao) contextDao.getBean("ClasseAutoveicoloDao");
	private static String[] Parametri = UtilString.RimuoviTuttiGliSpazi(gestioneApplicazioneDao.getName("PARAMETRI_CALCOLO_TARIFFA_AUTOVEICOLO").getValueString()).split("-");
	
	//public static ClasseAutoveicolo DammiAutoClasseReale(ClasseAutoveicolo ClasseAutoveicoloOriginale, String AnnoImmatricolazione) {
	public static ClasseAutoveicolo DammiAutoClasseReale(Long IdClasseAutoveicoloOriginale, String AnnoImmatricolazione) {
		if(IdClasseAutoveicoloOriginale != null) {
			// LUXURY
			if( IdClasseAutoveicoloOriginale == Constants.AUTO_LUXURY 
					&& Integer.parseInt(AnnoImmatricolazione.trim()) >= DammiAnniMaxImmatricPrimaClasse_e_Luxury(Parametri) ){
				return classeAutoveicoloDao.get( Constants.AUTO_LUXURY );
			
			}else if( IdClasseAutoveicoloOriginale == Constants.AUTO_LUXURY &&
					Integer.parseInt(AnnoImmatricolazione.trim()) < DammiAnniMaxImmatricPrimaClasse_e_Luxury(Parametri) &&
						Integer.parseInt(AnnoImmatricolazione.trim()) >= DammiAnniMaxImmatric_Intermedia_Luxury(Parametri) ){
				return classeAutoveicoloDao.get( Constants.AUTO_PRIMA_CLASSE );
				
			}else if(IdClasseAutoveicoloOriginale == Constants.AUTO_LUXURY 
					&& Integer.parseInt(AnnoImmatricolazione.trim()) < DammiAnniMaxImmatric_Intermedia_Luxury(Parametri)){
				return classeAutoveicoloDao.get( Constants.AUTO_ECONOMY );
				
			// PRIMA CLASSE
			}else if(IdClasseAutoveicoloOriginale == Constants.AUTO_PRIMA_CLASSE 
					&& Integer.parseInt(AnnoImmatricolazione.trim()) >= DammiAnniMaxImmatricPrimaClasse_e_Luxury(Parametri)){
				return classeAutoveicoloDao.get( Constants.AUTO_PRIMA_CLASSE );
				
			}else if(IdClasseAutoveicoloOriginale == Constants.AUTO_PRIMA_CLASSE 
					&& Integer.parseInt(AnnoImmatricolazione.trim()) < DammiAnniMaxImmatricPrimaClasse_e_Luxury(Parametri)){
				return classeAutoveicoloDao.get( Constants.AUTO_ECONOMY );
				
			// AUTO ECONOMY
			}else if( IdClasseAutoveicoloOriginale == Constants.AUTO_ECONOMY){ 
				return classeAutoveicoloDao.get( Constants.AUTO_ECONOMY );
			
			// VAN PRIMA CLASSE
			}else if( IdClasseAutoveicoloOriginale == Constants.AUTO_VAN_PRIMA_CLASSE
					&& Integer.parseInt(AnnoImmatricolazione.trim()) >= DammiAnniMaxImmatricPrimaClasse_e_Luxury(Parametri)){ 
				return classeAutoveicoloDao.get( Constants.AUTO_VAN_PRIMA_CLASSE );
				
			}else if( IdClasseAutoveicoloOriginale == Constants.AUTO_VAN_PRIMA_CLASSE
					&& Integer.parseInt(AnnoImmatricolazione.trim()) < DammiAnniMaxImmatricPrimaClasse_e_Luxury(Parametri)){ 
				return classeAutoveicoloDao.get( Constants.AUTO_VAN_ECONOMY );

			// VAN ECONOMY
			}else if( IdClasseAutoveicoloOriginale == Constants.AUTO_VAN_ECONOMY){ 
				return classeAutoveicoloDao.get( Constants.AUTO_VAN_ECONOMY );
				
			}else{
				return classeAutoveicoloDao.get( Constants.AUTO_ECONOMY );
			}
		}else {
			// AUTO NON CLASSIFICATA
			return classeAutoveicoloDao.get( Constants.AUTO_ECONOMY );
		}

	}
	

	public static int DammiAnniMaxImmatricPrimaClasse_e_Luxury(String[] Parametri){
		int NumeroAnniMaxImmatricPrimaClasse = Integer.parseInt(Parametri[Constants.PARAM_MAX_ANNI_AUTO_PRIMA_CLASSE]);
		return Calendar.getInstance().get(Calendar.YEAR) - NumeroAnniMaxImmatricPrimaClasse;
	}
	
	/**
	 * il 1.5 di DammiAnniMaxImmatricPrimaClasse_e_Luxury // 2010 e 2007
	 */
	public static int DammiAnniMaxImmatric_Intermedia_Luxury(String[] Parametri){
		int NumeroAnniMaxImmatricPrimaClasse = (int) (Integer.parseInt(Parametri[Constants.PARAM_MAX_ANNI_AUTO_PRIMA_CLASSE]) * 1.6);
		return Calendar.getInstance().get(Calendar.YEAR) - NumeroAnniMaxImmatricPrimaClasse;
	}
	
	/**
	 * L'immatricolazione auto non può essere più vecchia di un certo anno
	 */
	public static boolean AnnoImmatricolazioneValido(String[] Parametri, int annoImmatricolazione){
		int MaxAnniImmatricolazione = (int) (Integer.parseInt(Parametri[Constants.PARAM_MAX_ANNI_AUTO]));
		if(annoImmatricolazione >= (Calendar.getInstance().get(Calendar.YEAR) - MaxAnniImmatricolazione)){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Mi ritorna l'anno massimo di immatricolazione
	 */
	public static String AnnoImmatricolazioneMassimo(String[] Parametri){
		int MaxAnniImmatricolazione = (int) (Integer.parseInt(Parametri[Constants.PARAM_MAX_ANNI_AUTO]));
		return Integer.toString(Calendar.getInstance().get(Calendar.YEAR) - MaxAnniImmatricolazione);
	}
	
	
	public static String DammiAutiveicoliRichiestiAutistaList_String(Set<RichiestaMediaAutistaAutoveicolo> richiestaMediaAutistaAutoveicolo, HttpServletRequest request ){
		List<RichiestaMediaAutistaAutoveicolo> richAutistMedioAutoList = new ArrayList<RichiestaMediaAutistaAutoveicolo>();
		richAutistMedioAutoList.addAll( richiestaMediaAutistaAutoveicolo );
		List<Autoveicolo> autoList = new ArrayList<Autoveicolo>();
		for(RichiestaMediaAutistaAutoveicolo richAutistMedioAutoList_ite: richAutistMedioAutoList){
			autoList.add(richAutistMedioAutoList_ite.getAutoveicolo());
		}
		return DammiAutiveicoliRichiestiAutistaList_String(autoList, request);
	}
	
	
	public static String DammiAutiveicoliRichiestiAutistaList_String(List<Autoveicolo> autoList, HttpServletRequest request){
		int numAuto = autoList.size();
		int i = 1;
		String autoListString = "";
		for(Autoveicolo autoList_ite: autoList){
			autoListString = autoListString + ApplicationMessagesUtil.DammiMessageSource( autoList_ite.getClasseAutoveicoloReale().getNome(), request.getLocale())
			+" "+autoList_ite.getMarcaModello()
			+" "+ApplicationMessagesUtil.DammiMessageSource("posti.auto.autista", new Object[] {autoList_ite.getModelloAutoNumeroPosti().getNumeroPostiAuto().getNumero()}, request.getLocale())
			+" Anno: "+autoList_ite.getAnnoImmatricolazione()
			+" Targa: "+autoList_ite.getTarga();
			if(i < numAuto){
				autoListString = autoListString + " | ";
			}
			i++;
		}
		return autoListString;
	}
	
	public static Map<Long, String> DammiDescrizioneCategorieAutoMap(Locale locale){
        String annoCorrente = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
        String minAnnoImmatricAuto = AnnoImmatricolazioneMassimo(Parametri);
        int minAnnoImmatricPrimaClasse_e_Luxory = DammiAnniMaxImmatricPrimaClasse_e_Luxury(Parametri);
        List<ClasseAutoveicolo> classeAutovList = classeAutoveicoloDao.getClasseAutoveicolo();
        Map<Long, String> descrizioneCategorieAuto = new HashMap<Long, String>();
        for(ClasseAutoveicolo ite: classeAutovList){
        	if(ite.getId() == Constants.AUTO_ECONOMY){
        		String desc = ApplicationMessagesUtil.DammiMessageSource("desc.categoria.auto."+ite.getId(), new Object[] { 
        				ApplicationMessagesUtil.DammiMessageSource(ite.getNome(), locale),
        				ApplicationMessagesUtil.DammiMessageSource(ite.getNome()+".num.pass", locale),
        				minAnnoImmatricAuto, //1998
        				annoCorrente, //2018
        				Integer.toString(minAnnoImmatricPrimaClasse_e_Luxory) //2011
        		}, locale); 
        		descrizioneCategorieAuto.put(ite.getId(), desc);
        	}else if(ite.getId() == Constants.AUTO_PRIMA_CLASSE){
        		String desc = ApplicationMessagesUtil.DammiMessageSource("desc.categoria.auto."+ite.getId(), new Object[] { 
        				ApplicationMessagesUtil.DammiMessageSource(ite.getNome(), locale),
        				ApplicationMessagesUtil.DammiMessageSource(ite.getNome()+".num.pass", locale),
        				Integer.toString(minAnnoImmatricPrimaClasse_e_Luxory), //2011
        				annoCorrente //2018
        		}, locale); 
        		descrizioneCategorieAuto.put(ite.getId(), desc);
        	}else if(ite.getId() == Constants.AUTO_LUXURY){
        		String desc = ApplicationMessagesUtil.DammiMessageSource("desc.categoria.auto."+ite.getId(), new Object[] { 
        				ApplicationMessagesUtil.DammiMessageSource(ite.getNome(), locale),
        				ApplicationMessagesUtil.DammiMessageSource(ite.getNome()+".num.pass", locale),
        				Integer.toString(minAnnoImmatricPrimaClasse_e_Luxory), //2011
        				annoCorrente //2018
        		}, locale); 
        		descrizioneCategorieAuto.put(ite.getId(), desc);
        	}else if(ite.getId() == Constants.AUTO_VAN_ECONOMY){
        		String desc = ApplicationMessagesUtil.DammiMessageSource("desc.categoria.auto."+ite.getId(), new Object[] { 
        				ApplicationMessagesUtil.DammiMessageSource(ite.getNome(), locale),
        				ApplicationMessagesUtil.DammiMessageSource(ite.getNome()+".num.pass", locale),
        				minAnnoImmatricAuto, //1998
        				annoCorrente, //2018
        				Integer.toString(minAnnoImmatricPrimaClasse_e_Luxory) //2011
        		}, locale); 
        		descrizioneCategorieAuto.put(ite.getId(), desc);
        	}else if(ite.getId() == Constants.AUTO_VAN_PRIMA_CLASSE){
        		String desc = ApplicationMessagesUtil.DammiMessageSource("desc.categoria.auto."+ite.getId(), new Object[] { 
        				ApplicationMessagesUtil.DammiMessageSource(ite.getNome(), locale),
        				ApplicationMessagesUtil.DammiMessageSource(ite.getNome()+".num.pass", locale),
        				Integer.toString(minAnnoImmatricPrimaClasse_e_Luxory), //2011
        				annoCorrente //2018
        		}, locale); 
        		descrizioneCategorieAuto.put(ite.getId(), desc);
        	}
        }
		return descrizioneCategorieAuto;
	}
	

	public static List<String> DammiDescrizioneCategorieAutoList(Locale locale){
		List<String> descrizioneCategorieAutoList = new ArrayList<String>();
		for(Map.Entry<Long, String> entry: DammiDescrizioneCategorieAutoMap(locale).entrySet()){
			descrizioneCategorieAutoList.add(entry.getValue());
		}
		return descrizioneCategorieAutoList;
	}
	
	
	/**
	 * esempio di risultato:
	var dates3 = [
		new Date(1463522400000),
		new Date(1463608800000),
		new Date(1463695200000),
	 */
	public static String getDateSelezionateLong(Disponibilita disponibilita) {
		String string = "";
		String start = "[";
		String end = "]";
		if(disponibilita != null){
			List<DisponibilitaDate> list = new ArrayList<DisponibilitaDate>(disponibilita.getDisponibilitaDate());
			for(int i=0; i < list.size(); i++){
				string = string +"new Date("+ list.get(i). getData().getTime() +")";
				if(i < list.size()-1){
					string = string +",";
				}
			}
			return start + string + end;
		}
		string = "new Date(0)";
		return start + string + end;
	}
	
	
}
