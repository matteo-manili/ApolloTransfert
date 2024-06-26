package com.apollon.service;

import java.util.List;

import com.apollon.dao.AutistaAeroportiDao;
import com.apollon.model.AutistaAeroporti;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface AutistaAeroportiManager extends GenericManager<AutistaAeroporti, Long> {
	
	void setAutistaAeroportiDao(AutistaAeroportiDao autistaAeroportiDao);
	
	
	AutistaAeroporti get(Long id);
	
	List<AutistaAeroporti> getAutistaAeroporti();
	
	AutistaAeroporti saveAutistaAeroporti(AutistaAeroporti autistaAeroporti) throws Exception;
	
	void removeAutistaAeroporti(long userAutistaAeroporti);

	List<AutistaAeroporti> getAutistaAeroportiByIdAutista(Long idAutista);

	void removeAutistaAeroportiByIdAutista(long idAutista);


}
