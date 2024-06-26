package com.apollon.service;

import java.util.List;

import com.apollon.dao.AutistaSottoAutistiDao;
import com.apollon.model.AutistaSottoAutisti;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface AutistaSottoAutistiManager extends GenericManager<AutistaSottoAutisti, Long> {
	
	void setAutistaSottoAutistiDao(AutistaSottoAutistiDao autistaSottoAutistiDao);
	
	
	AutistaSottoAutisti get(Long id);
	
	List<AutistaSottoAutisti> getAutistaSottoAutisti();

	List<AutistaSottoAutisti> getAutistaSottoAutisti_By_Autista(long idAutista);
	
	void removeAutistaSottoAutisti(long idAutistaSottoAutisti);
	
	AutistaSottoAutisti saveAutistaSottoAutisti(AutistaSottoAutisti autistaSottoAutisti) throws Exception;


	


	
	












	
	

	

	


	

}
