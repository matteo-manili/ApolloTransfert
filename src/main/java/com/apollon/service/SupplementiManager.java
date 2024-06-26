package com.apollon.service;

import java.util.List;

import com.apollon.dao.SupplementiDao;
import com.apollon.model.Supplementi;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface SupplementiManager extends GenericManager<Supplementi, Long> {
	
	void setSupplementiDao(SupplementiDao supplementiDao);
	
	
	Supplementi get(Long id);
	
	List<Supplementi> getSupplementi();
	
	List<Supplementi> getSupplementiBy_IdRicercaTransfert(long idCorsa);
	
	List<Supplementi> getSupplementiCliente(long idUser);
	
	void removeSupplementi(long idSupplementi);
	
	Supplementi saveSupplementi(Supplementi supplementi) throws Exception;

	

	
	












	
	

	

	


	

}
