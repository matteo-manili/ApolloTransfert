package com.apollon.dao;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;

import com.apollon.model.AgA_AutoveicoloModelliTariffari;
import com.apollon.model.AgA_Tariffari;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface AgA_AutoveicoloModelliTariffariDao extends GenericDao<AgA_AutoveicoloModelliTariffari, Long> {
	
	AgA_AutoveicoloModelliTariffari get(Long id);
	
	List<AgA_AutoveicoloModelliTariffari> getAgA_AutoveicoloModelliTariffari();
	
	AgA_AutoveicoloModelliTariffari saveAgA_AutoveicoloModelliTariffari(AgA_AutoveicoloModelliTariffari agA_AutoveicoloModelliTariffari) throws DataIntegrityViolationException, HibernateJdbcException;

	List<AgA_AutoveicoloModelliTariffari> getAgA_AutoveicoloModelliTariffari_by_IdAutoveicolo(long idAutoveicolo);

	void EliminaModelliTariffari(long idAutoModelTariff);












	


	



}
