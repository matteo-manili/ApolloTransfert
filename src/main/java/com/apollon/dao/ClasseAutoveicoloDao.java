package com.apollon.dao;

import java.util.List;

import com.apollon.model.ClasseAutoveicolo;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface ClasseAutoveicoloDao extends GenericDao<ClasseAutoveicolo, Long> {
	
	ClasseAutoveicolo get(Long id);
	
	List<ClasseAutoveicolo> getClasseAutoveicolo();
	
	ClasseAutoveicolo saveClasseAutoveicolo(ClasseAutoveicolo classeAutoveicolo);


}
