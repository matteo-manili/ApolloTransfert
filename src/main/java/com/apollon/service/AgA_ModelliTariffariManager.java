package com.apollon.service;

import java.util.List;

import com.apollon.dao.AgA_ModelliTariffariDao;
import com.apollon.model.AgA_ModelliTariffari;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface AgA_ModelliTariffariManager extends GenericManager<AgA_ModelliTariffari, Long> {
	
	void setAgA_ModelliTariffariDao(AgA_ModelliTariffariDao agA_ModelliTariffariDao);
	
	AgA_ModelliTariffari get(Long id);
	


	
	AgA_ModelliTariffari saveAgA_ModelliTariffari(AgA_ModelliTariffari agA_ModelliTariffari) throws Exception;
	
	void removeAgA_ModelliTariffari(long idAgA_ModelliTariffari);

	List<AgA_ModelliTariffari> getAgA_ModelliTariffari_by_idAutoveicoloModelTariff(Long idModelloTariffario);

	AgA_ModelliTariffari getAgA_ModelliTariffari_by_IdAutoveicoloModelTariff_e_KmCorsa(Long id, int ite);


}
