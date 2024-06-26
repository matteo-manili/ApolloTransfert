package com.apollon.service;

import java.util.List;

import com.apollon.dao.ModelloAutoScoutDao;
import com.apollon.model.ModelloAutoScout;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface ModelloAutoScoutManager extends GenericManager<ModelloAutoScout, Long> {
	
	void setModelloAutoScoutDao(ModelloAutoScoutDao modelloAutoScoutDao);
	
	
	ModelloAutoScout get(Long id);
	
	Long getMaxValue_idAutoScout();
	
	List<ModelloAutoScout> getNomeModelloList_like_NomeModello(String term);

	List<ModelloAutoScout> getNomeModelloList_like_NomeMarca(String term);
	
	List<ModelloAutoScout> getModelloAutoScoutByMarca(Long id);
	
	List<ModelloAutoScout> getModelloAutoScoutDescrizione(String term, long idMarcaAutoScout) throws Exception;
	
	List<ModelloAutoScout> getModelloAutoScout();
	
	List<ModelloAutoScout> getModelliAutoScout_by_UtilizzatiDagliAutisti();
	
	ModelloAutoScout saveModelloAutoScout(ModelloAutoScout modelloAutoScout) throws Exception;
	
	void removeModelloAutoScout(long id);


	



}
