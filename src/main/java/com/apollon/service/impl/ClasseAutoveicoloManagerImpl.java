package com.apollon.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apollon.dao.ClasseAutoveicoloDao;
import com.apollon.model.ClasseAutoveicolo;
import com.apollon.service.ClasseAutoveicoloManager;




/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Service("ClasseAutoveicoloManager")
public class ClasseAutoveicoloManagerImpl extends GenericManagerImpl<ClasseAutoveicolo, Long> implements ClasseAutoveicoloManager {

	private ClasseAutoveicoloDao classeAutoveicoloDao;
	
	@Override
    @Autowired
	public void setClasseAutoveicoloDao(ClasseAutoveicoloDao classeAutoveicoloDao) {
		this.classeAutoveicoloDao = classeAutoveicoloDao;
	}

	
	
	@Override
	public ClasseAutoveicolo get(Long id) {
		return this.classeAutoveicoloDao.get(id);
	}
	

	@Override
	public List<ClasseAutoveicolo> getClasseAutoveicolo() {
		return classeAutoveicoloDao.getClasseAutoveicolo();
	}
	
	
	
	@Override
	public ClasseAutoveicolo saveClasseAutoveicolo(ClasseAutoveicolo classeAutoveicolo) throws Exception {
		return classeAutoveicoloDao.saveClasseAutoveicolo(classeAutoveicolo);
	}

	
	@Override
    public void removeClasseAutoveicolo(long id) {
		classeAutoveicoloDao.remove(id);
    }
	
	
	
}
