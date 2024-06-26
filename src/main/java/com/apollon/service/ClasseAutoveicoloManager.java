package com.apollon.service;

import java.util.List;

import com.apollon.dao.ClasseAutoveicoloDao;
import com.apollon.model.ClasseAutoveicolo;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface ClasseAutoveicoloManager extends GenericManager<ClasseAutoveicolo, Long> {
	
	void setClasseAutoveicoloDao(ClasseAutoveicoloDao classeAutoveicoloDao);
	
	
	ClasseAutoveicolo get(Long id);
	
	List<ClasseAutoveicolo> getClasseAutoveicolo();
	
	ClasseAutoveicolo saveClasseAutoveicolo(ClasseAutoveicolo classeAutoveicolo) throws Exception;
	
	void removeClasseAutoveicolo(long userClasseAutoveicolo);

}
