package com.apollon.util;

import java.util.List;

import org.json.JSONObject;

import com.apollon.Constants;
import com.apollon.dao.AgenzieViaggioBitDao;
import com.apollon.dao.ProvinceDao;
import com.apollon.model.AgenzieViaggioBit;
import com.apollon.webapp.util.ApplicationUtils;

/**
 * @author Matteo - matteo.manili@gmail.com
 *	
 */
public class CatturaEmailMarketing_UTIL extends ApplicationUtils {

	public static ProvinceDao provinceDao = (ProvinceDao) contextDao.getBean("ProvinceDao");
	
	
	public static String DammiTokenScontoEmailUnivoco() {
		try{
			String TokenCodiceSconto = "";
			while( true ){
				TokenCodiceSconto = UtilBukowski.getRandomToken__LettGrandi_Numeri( Constants.LunghezzaTokenScontoEmailMarketing );
				if( ControllaTokenCodiceScontoUnivoco_Globale(TokenCodiceSconto) ) {
					break;
				}
			}
			JSONObject parametriSconto = new JSONObject();
			parametriSconto.put(Constants.PercentualeScontoJSON, Constants.ValorePercentualeScontoEmailMarketing);
			parametriSconto.put(Constants.CodiceScontoJSON, TokenCodiceSconto);
			parametriSconto.put(Constants.CodiceScontoUsatoJSON, false);
			return parametriSconto.toString();

		}catch (Exception exc) {
			exc.printStackTrace();
			return null;
		}
	}
	
	
	
	
	
	/**
	 * !!! ATTENZIONE !!!
	 * AGGIORNAMENTO DI TUTTI GLI SCONTI TABELLA EMAIL MARKETING (agenzieViaggioBit) DA FARE PARTIRE COL RUN MAIN
	 */
	public static void EseguiScriptAggiornamentiSconti() {
		List<AgenzieViaggioBit> agenzList = agenzieViaggioBitDao.getAgenzieViaggioBit();
		for(AgenzieViaggioBit ite: agenzList){
			ite.setParametriSconto( CatturaEmailMarketing_UTIL.DammiTokenScontoEmailUnivoco() );
			agenzieViaggioBitDao.saveAgenzieViaggioBit(ite);
		}
	}
	

}
