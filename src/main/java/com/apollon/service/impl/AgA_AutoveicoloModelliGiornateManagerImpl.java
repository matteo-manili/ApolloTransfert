package com.apollon.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;
import org.springframework.stereotype.Service;
import com.apollon.dao.AgA_AutoveicoloModelliGiornateDao;
import com.apollon.model.AgA_AutoveicoloModelliGiornate;
import com.apollon.service.AgA_AutoveicoloModelliGiornateManager;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Service("AgA_AutoveicoloModelliGiornateManager")
public class AgA_AutoveicoloModelliGiornateManagerImpl extends GenericManagerImpl<AgA_AutoveicoloModelliGiornate, Long> implements AgA_AutoveicoloModelliGiornateManager {

	private AgA_AutoveicoloModelliGiornateDao agA_AutoveicoloModelliGiornateDao;
	
	@Override
    @Autowired
	public void setAgA_AutoveicoloModelliGiornateDao(AgA_AutoveicoloModelliGiornateDao agA_AutoveicoloModelliGiornateDao) {
		this.agA_AutoveicoloModelliGiornateDao = agA_AutoveicoloModelliGiornateDao;
	}

	
	@Override
	public AgA_AutoveicoloModelliGiornate get(Long id) {
		return this.agA_AutoveicoloModelliGiornateDao.get(id);
	}
	
	@Override
	public List<AgA_AutoveicoloModelliGiornate> getAgA_AutoveicoloModelliGiornate() {
		return agA_AutoveicoloModelliGiornateDao.getAgA_AutoveicoloModelliGiornate();
	}
	
	
	@Override
	public List<AgA_AutoveicoloModelliGiornate> getAgA_AutoveicoloModelliGiornate_by_IdAutoveicolo(long idAutoveicolo) {
		return agA_AutoveicoloModelliGiornateDao.getAgA_AutoveicoloModelliGiornate_by_IdAutoveicolo(idAutoveicolo);
	}
	
	
	@Override
	public List<AgA_AutoveicoloModelliGiornate> AutoveicoloModelliGiornate_ExistsTariffari_by_IdAutoveicolo(long idAutoveicolo) {
		return agA_AutoveicoloModelliGiornateDao.AutoveicoloModelliGiornate_ExistsTariffari_by_IdAutoveicolo(idAutoveicolo);
	}
	
	
	@Override
	public AgA_AutoveicoloModelliGiornate saveAgA_AutoveicoloModelliGiornate(AgA_AutoveicoloModelliGiornate agA_AutoveicoloModelliGiornate) throws DataIntegrityViolationException, HibernateJdbcException {
		return agA_AutoveicoloModelliGiornateDao.saveAgA_AutoveicoloModelliGiornate(agA_AutoveicoloModelliGiornate);
	}


	@Override
    public void removeAgA_AutoveicoloModelliGiornate(long id) {
		agA_AutoveicoloModelliGiornateDao.remove(id);
    }



	
	
	
	
}
