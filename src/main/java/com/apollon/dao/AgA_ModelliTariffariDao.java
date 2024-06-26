package com.apollon.dao;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;

import com.apollon.model.AgA_ModelliTariffari;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface AgA_ModelliTariffariDao extends GenericDao<AgA_ModelliTariffari, Long> {
	
	AgA_ModelliTariffari get(Long id);
	
	AgA_ModelliTariffari saveAgA_ModelliTariffari(AgA_ModelliTariffari agA_ModelliTariffari) throws DataIntegrityViolationException, HibernateJdbcException;

	List<AgA_ModelliTariffari> getAgA_ModelliTariffari_by_idAutoveicoloModelTariff(Long idAutoveicoloModelTariff);

	AgA_ModelliTariffari getAgA_ModelliTariffari_by_IdAutoveicoloModelTariff_e_KmCorsa(long idAutoveicoloModelTariff, int kmCorsa);









	


	



}
