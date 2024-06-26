package com.apollon.service;

import java.util.List;

import com.apollon.dao.DisponibilitaDao;
import com.apollon.model.Disponibilita;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface DisponibilitaManager extends GenericManager<Disponibilita, Long> {
	
	void setDisponibilitaDao(DisponibilitaDao disponibilitaDao);
	
	
	Disponibilita get(Long id);
	
	List<Disponibilita> getDisponibilita();
	
	Disponibilita getDisponibilitaByAutoveicolo(Long idAutoveicolo);
	
	Disponibilita saveDisponibilita(Disponibilita disponibilita) throws Exception;

	void removeDisponibilitaByIdAutoveicolo(long idAutoveicolo);

	void removeDisponibilita(long idDisponibilita);


	
	

	

	


	

}
