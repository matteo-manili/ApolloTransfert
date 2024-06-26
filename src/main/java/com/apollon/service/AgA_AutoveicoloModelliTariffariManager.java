package com.apollon.service;

import java.util.List;

import com.apollon.dao.AgA_AutoveicoloModelliTariffariDao;
import com.apollon.model.AgA_AutoveicoloModelliTariffari;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface AgA_AutoveicoloModelliTariffariManager extends GenericManager<AgA_AutoveicoloModelliTariffari, Long> {
	
	void setAgA_AutoveicoloModelliTariffariDao(AgA_AutoveicoloModelliTariffariDao agA_AutoveicoloModelliTariffariDao);
	
	
	AgA_AutoveicoloModelliTariffari get(Long id);
	
	List<AgA_AutoveicoloModelliTariffari> getAgA_AutoveicoloModelliTariffari();
	
	List<AgA_AutoveicoloModelliTariffari> getAgA_AutoveicoloModelliTariffari_by_IdAutoveicolo(long idAutoveicolo);
	
	AgA_AutoveicoloModelliTariffari saveAgA_AutoveicoloModelliTariffari(AgA_AutoveicoloModelliTariffari agA_AutoveicoloModelliTariffari) throws Exception;

	void removeAgA_AutoveicoloModelliTariffari(long idAgA_AutoveicoloModelliTariffari);

	void EliminaModelliTariffari(long idAutoModelTariff);












	
	

	

	


	

}
