package com.apollon.service;

import java.util.List;

import com.apollon.dao.RitardiDao;
import com.apollon.model.Ritardi;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface RitardiManager extends GenericManager<Ritardi, Long> {
	
	void setRitardiDao(RitardiDao ritardiDao);
	
	
	Ritardi get(Long id);
	
	List<Ritardi> getRitardi();
	
	Ritardi getRitardoBy_IdRicercaTransfert(long idCorsa);
	
	List<Ritardi> getRitardiCliente(long idUser);
	
	void removeRitardi(long idRitardi);
	
	Ritardi saveRitardi(Ritardi ritardi) throws Exception;

	
	












	
	

	

	


	

}
