package com.apollon.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;
import org.springframework.stereotype.Service;

import com.apollon.dao.RitardiDao;
import com.apollon.model.Ritardi;
import com.apollon.service.RitardiManager;





/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Service("RitardiManager")
public class RitardiManagerImpl extends GenericManagerImpl<Ritardi, Long> implements RitardiManager {

	private RitardiDao ritardiDao;
	
	@Override
    @Autowired
	public void setRitardiDao(RitardiDao ritardiDao) {
		this.ritardiDao = ritardiDao;
	}

	
	
	@Override
	public Ritardi get(Long id) {
		return this.ritardiDao.get(id);
	}
	

	@Override
	public List<Ritardi> getRitardi() {
		return ritardiDao.getRitardi();
	}
	
	
	@Override
	public Ritardi getRitardoBy_IdRicercaTransfert(long idCorsa){
		return ritardiDao.getRitardoBy_IdRicercaTransfert(idCorsa);
	}
	
	
	@Override
	public List<Ritardi> getRitardiCliente(long idUser) {
		return ritardiDao.getRitardiCliente(idUser);
	}
	

	@Override
	public Ritardi saveRitardi(Ritardi ritardi) throws DataIntegrityViolationException, HibernateJdbcException {
		return ritardiDao.saveRitardi(ritardi);
	}


	@Override
    public void removeRitardi(long id) {
		ritardiDao.remove(id);
    }



	
	
	
	
}
