package com.apollon.dao;

import java.util.List;

import com.apollon.model.NumeroPostiAuto;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface NumeroPostiAutoDao extends GenericDao<NumeroPostiAuto, Long> {
	
	NumeroPostiAuto get(Long id);
	
	List<NumeroPostiAuto> getNumeroPostiAuto();
	
	NumeroPostiAuto saveNumeroPostiAuto(NumeroPostiAuto numeroPostiAuto);


}
