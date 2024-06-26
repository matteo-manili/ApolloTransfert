package com.apollon.webapp.util.controller.tariffe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataIntegrityViolationException;
import com.apollon.Constants;
import com.apollon.dao.VenditorePercServProvinciaDao;
import com.apollon.model.Province;
import com.apollon.model.User;
import com.apollon.model.VenditorePercServProvincia;
import com.apollon.webapp.util.ApplicationUtils;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public class TariffeVenditoreUtil extends ApplicationUtils{
	public static VenditorePercServProvinciaDao venditorePercServProvincia = (VenditorePercServProvinciaDao) contextDao.getBean("VenditorePercServProvinciaDao");
	
	public static boolean ControlloValorePercentualeVenditoreConsentito(Map<String, Integer> mapPercentualiVenditore, int valorePercProvVenditore){
		if(valorePercProvVenditore < mapPercentualiVenditore.get(Constants.VENDIDORE_PERC_MIN) 
				|| valorePercProvVenditore > mapPercentualiVenditore.get(Constants.VENDIDORE_PERC_MAX) ){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * Passando una percentuale più bassa mi restituisce la percentuale minima.<br>
	 * Passando una percentuale più alta mi restituisce la percentuale massima
	 */
	public static int DammiValorePercentualeVenditoreConsentito(Map<String, Integer> mapPercentualiVenditore, int valorePercProvVenditore){
		if(valorePercProvVenditore < mapPercentualiVenditore.get(Constants.VENDIDORE_PERC_MIN) ){
			return mapPercentualiVenditore.get(Constants.VENDIDORE_PERC_MIN);
		}else if(valorePercProvVenditore > mapPercentualiVenditore.get(Constants.VENDIDORE_PERC_MAX)){
			return mapPercentualiVenditore.get(Constants.VENDIDORE_PERC_MAX);
		}else{
			return valorePercProvVenditore;
		}
	}
	
	
	/**
	 * Mi ritorna la Percentale della provincia che il venditore ha impostato.
	 * Quando in una RicercaTransfert il prelievo e l'arrivo si trovano in due province diverse, questo motodo mi ritorna la media delle 2 percentuali
	 * che il venditore ha impostato nella sa lista Province.
	 */
	public static int DammiPercentualeMediaVenditore_by_ProvPartenza_ProvArrivo(long idVenditore, long idProvPartenza, long idProvArrivo){
		Integer PercentualeVenditorePartenza = null; Integer PercentualeVenditoreArrivo = null; Integer PercentualeVenditoreProvDefault = null;
		VenditorePercServProvincia vendPercServProvPartenza = venditorePercServProvincia.getVenditorePercServProvincia_by_Venditore_Prov(
				idVenditore, idProvPartenza );
		if( vendPercServProvPartenza != null ){
			PercentualeVenditorePartenza = vendPercServProvPartenza.getPercentualeServizio();
		}else{
			PercentualeVenditoreProvDefault = DammiParametriPercentualiVenditori().get(Constants.VENDIDORE_PERC_DEFAULT);
			PercentualeVenditorePartenza = PercentualeVenditoreProvDefault;
		}
		if( idProvPartenza != idProvArrivo){
			VenditorePercServProvincia vendPercServProvArrivo = venditorePercServProvincia.getVenditorePercServProvincia_by_Venditore_Prov(
					idVenditore, idProvArrivo );
			if(vendPercServProvArrivo != null){
				PercentualeVenditoreArrivo = vendPercServProvArrivo.getPercentualeServizio();
			}else{
				PercentualeVenditoreArrivo = (PercentualeVenditoreProvDefault != null ? PercentualeVenditoreProvDefault : DammiParametriPercentualiVenditori().get(Constants.VENDIDORE_PERC_DEFAULT));
			}
		}else{
			PercentualeVenditoreArrivo = PercentualeVenditorePartenza;
		}
		return CalcolaMedia(PercentualeVenditorePartenza, PercentualeVenditoreArrivo);
	}
	
	public static int DammiPercentualeMediaVenditore_by_ProvPartenza_ProvArrivo(HashMap<Long, Integer> hmap, int PercentualeVenditoreProvDefault,
			long idProvPartenza, long idProvArrivo){
		Integer PercentualeVenditorePartenza = hmap.get(idProvPartenza);
		Integer PercentualeVenditoreArrivo = hmap.get(idProvArrivo);
		return CalcolaMedia( 
				(PercentualeVenditorePartenza != null) ?  PercentualeVenditorePartenza : PercentualeVenditoreProvDefault,
				(PercentualeVenditoreArrivo != null) ?  PercentualeVenditoreArrivo : PercentualeVenditoreProvDefault);
	}
	
	public static HashMap<Long, Integer> DammiHashTablePercentualiProvinceVenditore(List<VenditorePercServProvincia> venditorePercServProvinciaList){
		if(venditorePercServProvinciaList != null){
			HashMap<Long, Integer> hmap = new HashMap<Long, Integer>();
			for(VenditorePercServProvincia ite : venditorePercServProvinciaList){
				hmap.put(ite.getProvince().getId(), ite.getPercentualeServizio());
			}
			return hmap;
		}else{
			return null;
		}
	}
	
	private static int CalcolaMedia(Integer PercentualeVenditorePartenza, Integer PercentualeVenditoreArrivo){
		int result = (PercentualeVenditorePartenza + PercentualeVenditoreArrivo) / 2;
		//System.out.println("result: "+result +" PercentualeVenditorePartenza: "+PercentualeVenditorePartenza +" PercentualeVenditoreArrivo: "+PercentualeVenditoreArrivo);
		return result;
	}
	
	
	
	
	
	
	
	public static boolean SalvaVenditorePercServProvincia(Map<String, Integer> mapPercentualiVenditore, User user, Province prov_ite, String valorePercProvVenditore) throws Exception{
		if( ControlloValorePercentualeVenditoreConsentito(mapPercentualiVenditore, Integer.parseInt(valorePercProvVenditore)) ){
			try{
				VenditorePercServProvincia vendPercServProv = new VenditorePercServProvincia();
				vendPercServProv.setPercentualeServizio(Integer.parseInt(valorePercProvVenditore));
				vendPercServProv.setProvince(prov_ite);
				vendPercServProv.setUser(user);
				venditorePercServProvincia.saveVenditorePercServProvincia(vendPercServProv);
				return true;
			}catch (final DataIntegrityViolationException dataIntegrViolException) {
				VenditorePercServProvincia vendPercServProv = venditorePercServProvincia
						.getVenditorePercServProvincia_by_Venditore_Prov(user.getId(), prov_ite.getId());
				vendPercServProv.setPercentualeServizio( Integer.parseInt(valorePercProvVenditore) );
				vendPercServProv.setProvince(prov_ite);
				vendPercServProv.setUser(user);
				venditorePercServProvincia.saveVenditorePercServProvincia(vendPercServProv);
				return true;
	        }
		}else{
			return false;
		}
	}
	
	
}
