package com.apollon.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;
import org.springframework.stereotype.Service;

import com.apollon.dao.AgA_ModelliTariffariDao;
import com.apollon.model.AgA_ModelliTariffari;
import com.apollon.service.AgA_ModelliTariffariManager;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Service("AgA_ModelliTariffariManager")
public class AgA_ModelliTariffariManagerImpl extends GenericManagerImpl<AgA_ModelliTariffari, Long> implements AgA_ModelliTariffariManager {

	private AgA_ModelliTariffariDao agA_ModelliTariffariDao;
	
	@Override
    @Autowired
	public void setAgA_ModelliTariffariDao(AgA_ModelliTariffariDao agA_ModelliTariffariDao) {
		this.agA_ModelliTariffariDao = agA_ModelliTariffariDao;
	}

	@Override
	public AgA_ModelliTariffari get(Long id) {
		return this.agA_ModelliTariffariDao.get(id);
	}
	
	@Override
	public AgA_ModelliTariffari saveAgA_ModelliTariffari(AgA_ModelliTariffari agA_ModelliTariffari) throws DataIntegrityViolationException, HibernateJdbcException {
		return agA_ModelliTariffariDao.saveAgA_ModelliTariffari(agA_ModelliTariffari);
	}

	@Override
    public void removeAgA_ModelliTariffari(long id) {
		agA_ModelliTariffariDao.remove(id);
    }
	
	@Override
	public List<AgA_ModelliTariffari> getAgA_ModelliTariffari_by_idAutoveicoloModelTariff(Long idModelloTariffario) {
		return agA_ModelliTariffariDao.getAgA_ModelliTariffari_by_idAutoveicoloModelTariff(idModelloTariffario);
	}

	@Override
	public AgA_ModelliTariffari getAgA_ModelliTariffari_by_IdAutoveicoloModelTariff_e_KmCorsa(Long id,int ite) {
		return agA_ModelliTariffariDao.getAgA_ModelliTariffari_by_IdAutoveicoloModelTariff_e_KmCorsa(id, ite);
	}


	
	
	
}
