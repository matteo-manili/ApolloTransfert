package com.apollon.webapp.util.controller.ritardi;

import java.math.BigDecimal;

import org.json.JSONObject;
import org.springframework.web.servlet.ModelAndView;

import com.apollon.Constants;
import com.apollon.dao.FattureDao;
import com.apollon.dao.GestioneApplicazioneDao;
import com.apollon.dao.RicercaTransfertDao;
import com.apollon.dao.RitardiDao;
import com.apollon.model.Fatture;
import com.apollon.model.RicercaTransfert;
import com.apollon.model.Ritardi;
import com.apollon.webapp.util.ApplicationUtils;
import com.apollon.webapp.util.CalcoloPrezzi;
import com.apollon.webapp.util.controller.home.HomeUtil;



/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public class RitardiUtil extends ApplicationUtils {
	
	private static RitardiDao ritardiDao = (RitardiDao) contextDao.getBean("RitardiDao");
	private static RicercaTransfertDao ricercaTransfertDao = (RicercaTransfertDao) contextDao.getBean("RicercaTransfertDao");
	private static GestioneApplicazioneDao gestioneApplicazioneDao = (GestioneApplicazioneDao) contextDao.getBean("GestioneApplicazioneDao");
	private static FattureDao fattureDao = (FattureDao) contextDao.getBean("FattureDao");
	
	public static final String ANDATA = "ANDATA";
	public static final String RITORNO = "RITORNO";
	
	private static Ritardi SetRitardoAndata(Ritardi ritardo, int numeroMezzore, BigDecimal prezzoRitardo) {
		ritardo.setNumeroMezzoreRitardiAndata( numeroMezzore );
		ritardo.setPrezzoAndata(prezzoRitardo);
		if(prezzoRitardo.compareTo(BigDecimal.ZERO) > 0) {
			ritardo.setPagatoAndata(false);
		}else{
			ritardo.setPagatoAndata(true);
		}
		return ritardo;
	}

	public static void GestioneRitardo(long idRicTransfert, String ritardoAndataRitorno, int numeroMezzore) {
		RicercaTransfert ricTransf = ricercaTransfertDao.get( idRicTransfert );
		BigDecimal prezzoRitardo = CalcoloPrezzi.CalcolaPrezzoRitardo( numeroMezzore );
		try{
			if(ricTransf.getRitardo() != null) {
				//Ritardi ritardo = ricTransf.getRitardo();
				Ritardi ritardo = ritardiDao.get( ricTransf.getRitardo() );
				if(ritardoAndataRitorno.equalsIgnoreCase( RitardiUtil.ANDATA )) {
					ritardo = SetRitardoAndata(ritardo, numeroMezzore, prezzoRitardo);
				}else if(ritardoAndataRitorno.equalsIgnoreCase( RitardiUtil.RITORNO )) {
					ritardo = SetRitardoRitorno(ritardo, numeroMezzore, prezzoRitardo);
				}
				ritardiDao.saveRitardi(ritardo);
				Gestione_Ritardo_e_Fattura(ritardo);
			}else{
				Ritardi ritardo = new Ritardi(0, 0, BigDecimal.ZERO, BigDecimal.ZERO, true, true);
				if(ritardoAndataRitorno.equalsIgnoreCase( "ANDATA" )) {
					ritardo = SetRitardoAndata(ritardo, numeroMezzore, prezzoRitardo);
					
				}else if(ritardoAndataRitorno.equalsIgnoreCase( "RITORNO" )) {
					ritardo = SetRitardoRitorno(ritardo, numeroMezzore, prezzoRitardo);
				}
				ritardo.setRicercaTransfert(ricTransf);
				ritardiDao.saveRitardi(ritardo);
				JSONObject infoDatiPasseggero = HomeUtil.GetInfoDatiPasseggero(ricTransf);
				infoDatiPasseggero.put(Constants.Ritardo_Id, ritardo.getId());
				ricTransf.setInfoPasseggero(infoDatiPasseggero.toString());
				ricercaTransfertDao.saveRicercaTransfert(ricTransf);
				Gestione_Ritardo_e_Fattura(ritardo);
			}
		}catch(NumberFormatException nfe){ }
	}

	private static void Gestione_Ritardo_e_Fattura(Ritardi ritardo) {
		if( (ritardo.getNumeroMezzoreRitardiAndata() > 0 || ritardo.getNumeroMezzoreRitardiRitorno() > 0) 
				&& fattureDao.getFatturaBy_IdRitardo(ritardo.getId()) == null ){
				// creo la fattura per il ritardo
				Fatture fattura = new Fatture();
				fattura.setProgressivoFattura( fattureDao.dammiNumeroProgressivoFattura() );
				fattura.setRitardi(ritardo);
				fattura.setNumeroSollecitiInviatiRitardo(0);
				fattureDao.saveFatture(fattura);
		}else if(ritardo.getNumeroMezzoreRitardiAndata() == 0 && ritardo.getNumeroMezzoreRitardiRitorno() == 0 ) { 
			
			
			//ritardo.set
			
			// rimuovo
			/* I Supplementi e i Ritardi non devono essere cancellati perché sono collegati alla fattura e la fattura non rimossa ma bensì stornata a 0.00
			RicercaTransfert ricTrans = ricercaTransfertDao.get( ritardo.getRicercaTransfert().getId() );
			JSONObject infoDatiPasseggero = new JSONObject( ricTrans.getInfoPasseggero() );
			infoDatiPasseggero.remove(Constants.Ritardo_Id);
			ricTrans.setInfoPasseggero( infoDatiPasseggero.toString() );
			ricercaTransfertDao.saveRicercaTransfert(ricTrans);
			fattureDao.removeFatturabyRitardo( ritardo.getId() );
			ritardiDao.removeRitardobyId( ritardo.getId() );
			*/
		}
	}

	
	private static Ritardi SetRitardoRitorno(Ritardi ritardo, int numeroMezzore, BigDecimal prezzoRitardo) {
		ritardo.setNumeroMezzoreRitardiRitorno( numeroMezzore );
		ritardo.setPrezzoRitorno(prezzoRitardo);
		if (prezzoRitardo.compareTo(BigDecimal.ZERO) > 0) {
			ritardo.setPagatoRitorno(false);
		}else{
			ritardo.setPagatoRitorno(true);
		}
		return ritardo;
	}
	
	
	/**
	 * Object Attribute per ModelAndView
	 */
	public static ModelAndView AddAttribute_Ritardi(ModelAndView mav) {
		BigDecimal prezzoOrarioRitardoCliete = new BigDecimal( gestioneApplicazioneDao.getName("VALORE_PERCENTUALE_SERVIZIO_E_VALORE_EURO_ORA_RITARDO_CLIENTE").getValueString() );
		long percentualeServiziorRitardoCliete = gestioneApplicazioneDao.getName("VALORE_PERCENTUALE_SERVIZIO_E_VALORE_EURO_ORA_RITARDO_CLIENTE").getValueNumber();
		return AddAttribute_Ritardi(mav, prezzoOrarioRitardoCliete, percentualeServiziorRitardoCliete);
	}
	
	public static ModelAndView AddAttribute_Ritardi(ModelAndView mav, BigDecimal prezzoOrarioRitardoCliete, long percentualeServiziorRitardoCliete) {
		BigDecimal prezzoRitardoClieteConCostoServizio = CalcoloPrezzi.CalcolaPercentuale(prezzoOrarioRitardoCliete, (int) percentualeServiziorRitardoCliete);
		mav.addObject("VALORE_EURO_ORA_RITARDO_CLIENTE_CON_TASSA_SERVZIO", prezzoOrarioRitardoCliete.add(prezzoRitardoClieteConCostoServizio).divide(new BigDecimal(2)) );
		mav.addObject("VALORE_EURO_ORA_RITARDO_CLIENTE", prezzoOrarioRitardoCliete.divide(new BigDecimal(2)));
		return mav;
	}
	
	
}
