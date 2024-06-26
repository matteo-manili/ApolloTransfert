package com.apollon.service;

import java.util.List;

import com.apollon.dao.NumeroPostiAutoDao;
import com.apollon.model.NumeroPostiAuto;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface NumeroPostiAutoManager extends GenericManager<NumeroPostiAuto, Long> {
	
	void setNumeroPostiAutoDao(NumeroPostiAutoDao numeroPostiAutoDao);
	
	
	NumeroPostiAuto get(Long id);
	
	List<NumeroPostiAuto> getNumeroPostiAuto();
	
	NumeroPostiAuto saveNumeroPostiAuto(NumeroPostiAuto numeroPostiAuto) throws Exception;
	
	void removeNumeroPostiAuto(long userNumeroPostiAuto);

}
