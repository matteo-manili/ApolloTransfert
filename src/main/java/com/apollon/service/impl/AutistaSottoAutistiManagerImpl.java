package com.apollon.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;
import org.springframework.stereotype.Service;

import com.apollon.dao.AutistaSottoAutistiDao;
import com.apollon.model.AutistaSottoAutisti;
import com.apollon.service.AutistaSottoAutistiManager;





/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Service("AutistaSottoAutistiManager")
public class AutistaSottoAutistiManagerImpl extends GenericManagerImpl<AutistaSottoAutisti, Long> implements AutistaSottoAutistiManager {

	private AutistaSottoAutistiDao autistaSottoAutistiDao;
	
	@Override
    @Autowired
	public void setAutistaSottoAutistiDao(AutistaSottoAutistiDao autistaSottoAutistiDao) {
		this.autistaSottoAutistiDao = autistaSottoAutistiDao;
	}

	
	@Override
	public AutistaSottoAutisti get(Long id) {
		return this.autistaSottoAutistiDao.get(id);
	}
	

	@Override
	public List<AutistaSottoAutisti> getAutistaSottoAutisti() {
		return autistaSottoAutistiDao.getAutistaSottoAutisti();
	}

	@Override
	public List<AutistaSottoAutisti> getAutistaSottoAutisti_By_Autista(long idAutista) {
		return autistaSottoAutistiDao.getAutistaSottoAutisti_By_Autista(idAutista);
	}
	

	@Override
	public AutistaSottoAutisti saveAutistaSottoAutisti(AutistaSottoAutisti autistaSottoAutisti) throws DataIntegrityViolationException, HibernateJdbcException {
		return autistaSottoAutistiDao.saveAutistaSottoAutisti(autistaSottoAutisti);
	}


	@Override
    public void removeAutistaSottoAutisti(long id) {
		autistaSottoAutistiDao.remove(id);
    }



	
	
	
	
}
