package com.apollon.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;
import org.springframework.stereotype.Service;

import com.apollon.dao.AutistaLicenzeNccDao;
import com.apollon.model.AutistaLicenzeNcc;
import com.apollon.service.AutistaLicenzeNccManager;





/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Service("AutistaLicenzeNccManager")
public class AutistaLicenzeNccManagerImpl extends GenericManagerImpl<AutistaLicenzeNcc, Long> implements AutistaLicenzeNccManager {

	private AutistaLicenzeNccDao autistaLicenzeNccDao;
	
	@Override
    @Autowired
	public void setAutistaLicenzeNccDao(AutistaLicenzeNccDao autistaLicenzeNccDao) {
		this.autistaLicenzeNccDao = autistaLicenzeNccDao;
	}

	
	@Override
	public AutistaLicenzeNcc get(Long id) {
		return this.autistaLicenzeNccDao.get(id);
	}
	

	@Override
	public List<AutistaLicenzeNcc> getAutistaLicenzeNcc() {
		return autistaLicenzeNccDao.getAutistaLicenzeNcc();
	}

	@Override
	public List<AutistaLicenzeNcc> getAutistaLicenzeNcc_By_Autista(long idAutista) {
		return autistaLicenzeNccDao.getAutistaLicenzeNcc_By_Autista(idAutista);
	}

	@Override
	public AutistaLicenzeNcc saveAutistaLicenzeNcc(AutistaLicenzeNcc autistaLicenzeNcc) throws DataIntegrityViolationException, HibernateJdbcException {
		return autistaLicenzeNccDao.saveAutistaLicenzeNcc(autistaLicenzeNcc);
	}


	@Override
    public void removeAutistaLicenzeNcc(long id) {
		autistaLicenzeNccDao.remove(id);
    }



	
	
	
	
}
