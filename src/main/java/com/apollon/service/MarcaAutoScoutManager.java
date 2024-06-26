package com.apollon.service;

import java.util.List;

import com.apollon.dao.MarcaAutoScoutDao;
import com.apollon.model.MarcaAutoScout;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface MarcaAutoScoutManager extends GenericManager<MarcaAutoScout, Long> {
	
	void setMarcaAutoScoutDao(MarcaAutoScoutDao marcaAutoScoutDao);
	
	
	MarcaAutoScout get(Long id);
	
	MarcaAutoScout getMarcaAutoScout_by_IdAutoScout(Long idAutoScout);
	
	List<MarcaAutoScout> getMarcaAutoScout();
	
	List<MarcaAutoScout> getMarcaAutoScoutDescrizione(String term);
	
	MarcaAutoScout saveMarcaAutoScout(MarcaAutoScout marcaAutoScout) throws Exception;
	
	void removeMarcaAutoScout(long userMarcaAutoScout);

	MarcaAutoScout getNomeMarcaAutoScout(String nomeMarca);


}
