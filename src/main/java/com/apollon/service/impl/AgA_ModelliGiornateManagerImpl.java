package com.apollon.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;
import org.springframework.stereotype.Service;

import com.apollon.dao.AgA_ModelliGiornateDao;
import com.apollon.model.AgA_ModelliGiornate;
import com.apollon.service.AgA_ModelliGiornateManager;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Service("AgA_ModelliGiornateManager")
public class AgA_ModelliGiornateManagerImpl extends GenericManagerImpl<AgA_ModelliGiornate, Long> implements AgA_ModelliGiornateManager {

	private AgA_ModelliGiornateDao agA_ModelliGiornateDao;
	
	@Override
    @Autowired
	public void setAgA_ModelliGiornateDao(AgA_ModelliGiornateDao agA_ModelliGiornateDao) {
		this.agA_ModelliGiornateDao = agA_ModelliGiornateDao;
	}

	@Override
	public AgA_ModelliGiornate get(Long id) {
		return this.agA_ModelliGiornateDao.get(id);
	}
	
	@Override
	public AgA_ModelliGiornate saveAgA_ModelliGiornate(AgA_ModelliGiornate agA_ModelliGiornate) throws DataIntegrityViolationException, HibernateJdbcException {
		return agA_ModelliGiornateDao.saveAgA_ModelliGiornate(agA_ModelliGiornate);
	}

	@Override
    public void removeAgA_ModelliGiornate(long id) {
		agA_ModelliGiornateDao.remove(id);
    }
	
	@Override
	public List<AgA_ModelliGiornate> getAgA_ModelliGiornate_by_idAutoveicoloModelGiornata(Long idAutoveicoloModelGiornata) {
		return agA_ModelliGiornateDao.getAgA_ModelliGiornate_by_idAutoveicoloModelGiornata(idAutoveicoloModelGiornata);
	}

	@Override
	public AgA_ModelliGiornate getAgA_ModelliGiornate_by_IdAutoveicoloModelGiornata_e_Orario(long idAutoveicoloModelGiornata, int orario) {
		return agA_ModelliGiornateDao.getAgA_ModelliGiornate_by_IdAutoveicoloModelGiornata_e_Orario(idAutoveicoloModelGiornata, orario);
	}


	
	
	
}
