package com.apollon.dao;

import java.util.List;

import com.apollon.model.ModelloAutoScout;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface ModelloAutoScoutDao extends GenericDao<ModelloAutoScout, Long> {
	
	ModelloAutoScout get(Long id);
	
	Long getMaxValue_idAutoScout();
	
	ModelloAutoScout getModelloAutoScout_by_idModelloAutoScout(long idModelloAutoScout);
	
	List<ModelloAutoScout> getNomeModelloList_like_NomeModello(String term);
	
	List<ModelloAutoScout> getNomeModelloList_like_NomeMarca(String term);
	
	List<ModelloAutoScout> getModelloAutoScout();
	
	List<ModelloAutoScout> getModelloAutoScoutByMarca(long idModello);
	
	List<ModelloAutoScout> getModelloAutoScoutDescrizione(String term, long idMarcaAutoScout) throws Exception;
	
	ModelloAutoScout saveModelloAutoScout(ModelloAutoScout modelloAutoScout);

	List<ModelloAutoScout> getModelliAutoScout_by_UtilizzatiDagliAutisti();

	

	

	

	


	
}
