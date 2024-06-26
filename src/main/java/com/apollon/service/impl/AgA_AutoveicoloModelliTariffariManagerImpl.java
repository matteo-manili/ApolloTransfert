package com.apollon.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;
import org.springframework.stereotype.Service;

import com.apollon.dao.AgA_AutoveicoloModelliTariffariDao;
import com.apollon.model.AgA_AutoveicoloModelliTariffari;
import com.apollon.service.AgA_AutoveicoloModelliTariffariManager;





/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Service("AgA_AutoveicoloModelliTariffariManager")
public class AgA_AutoveicoloModelliTariffariManagerImpl extends GenericManagerImpl<AgA_AutoveicoloModelliTariffari, Long> implements AgA_AutoveicoloModelliTariffariManager {

	private AgA_AutoveicoloModelliTariffariDao agA_AutoveicoloModelliTariffariDao;
	
	@Override
    @Autowired
	public void setAgA_AutoveicoloModelliTariffariDao(AgA_AutoveicoloModelliTariffariDao agA_AutoveicoloModelliTariffariDao) {
		this.agA_AutoveicoloModelliTariffariDao = agA_AutoveicoloModelliTariffariDao;
	}

	
	@Override
	public AgA_AutoveicoloModelliTariffari get(Long id) {
		return this.agA_AutoveicoloModelliTariffariDao.get(id);
	}
	
	@Override
	public List<AgA_AutoveicoloModelliTariffari> getAgA_AutoveicoloModelliTariffari() {
		return agA_AutoveicoloModelliTariffariDao.getAgA_AutoveicoloModelliTariffari();
	}
	
	
	@Override
	public List<AgA_AutoveicoloModelliTariffari> getAgA_AutoveicoloModelliTariffari_by_IdAutoveicolo(long idAutoveicolo) {
		return agA_AutoveicoloModelliTariffariDao.getAgA_AutoveicoloModelliTariffari_by_IdAutoveicolo(idAutoveicolo);
	}
	
	
	@Override
	public AgA_AutoveicoloModelliTariffari saveAgA_AutoveicoloModelliTariffari(AgA_AutoveicoloModelliTariffari agA_AutoveicoloModelliTariffari) throws DataIntegrityViolationException, HibernateJdbcException {
		return agA_AutoveicoloModelliTariffariDao.saveAgA_AutoveicoloModelliTariffari(agA_AutoveicoloModelliTariffari);
	}


	@Override
    public void removeAgA_AutoveicoloModelliTariffari(long id) {
		agA_AutoveicoloModelliTariffariDao.remove(id);
    }

	@Override
    public void EliminaModelliTariffari(long idAutoModelTariff) {
		agA_AutoveicoloModelliTariffariDao.EliminaModelliTariffari(idAutoModelTariff);
    }

	
	
	
	
}
