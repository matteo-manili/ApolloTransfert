package com.apollon.dao;

import java.util.List;

import com.apollon.model.MarcaAutoScout;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface MarcaAutoScoutDao extends GenericDao<MarcaAutoScout, Long> {
	
	MarcaAutoScout get(Long id);
	
	MarcaAutoScout getMarcaAutoScout_by_IdAutoScout(Long idAutoScout);
	
	List<MarcaAutoScout> getMarcaAutoScout();
	
	List<MarcaAutoScout> getMarcaAutoScoutDescrizione(String term);
	
	MarcaAutoScout saveMarcaAutoScout(MarcaAutoScout marcaAutoScout);

	MarcaAutoScout getNomeMarcaAutoScout(String nomeMarca);

	

	


}
