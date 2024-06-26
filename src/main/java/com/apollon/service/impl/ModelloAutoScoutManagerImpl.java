package com.apollon.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apollon.dao.ModelloAutoScoutDao;
import com.apollon.model.ModelloAutoScout;
import com.apollon.service.ModelloAutoScoutManager;




/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Service("ModelloAutoScoutManager")
public class ModelloAutoScoutManagerImpl extends GenericManagerImpl<ModelloAutoScout, Long> implements ModelloAutoScoutManager {

	private ModelloAutoScoutDao modelloAutoScoutDao;
	
	@Override
    @Autowired
	public void setModelloAutoScoutDao(ModelloAutoScoutDao modelloAutoScoutDao) {
		this.modelloAutoScoutDao = modelloAutoScoutDao;
	}

	
	
	
	@Override
	public ModelloAutoScout get(Long id) {
		return this.modelloAutoScoutDao.get(id);
	}
	
	
	@Override
	public Long getMaxValue_idAutoScout(){
		return this.modelloAutoScoutDao.getMaxValue_idAutoScout();
	}
		
	
	@Override
	public List<ModelloAutoScout> getNomeModelloList_like_NomeModello(String term)  {
		return this.modelloAutoScoutDao.getNomeModelloList_like_NomeModello(term);
	}
	
	@Override
	public List<ModelloAutoScout> getNomeModelloList_like_NomeMarca(String term)  {
		return this.modelloAutoScoutDao.getNomeModelloList_like_NomeMarca(term);
	}
	
	
	@Override
	public List<ModelloAutoScout> getModelloAutoScoutByMarca(Long idModello) {
		return this.modelloAutoScoutDao.getModelloAutoScoutByMarca(idModello);
	}
	
	
	
	
	
	@Override
	public List<ModelloAutoScout> getModelloAutoScoutDescrizione(String term, long idMarcaAutoScout) throws Exception {
		return this.modelloAutoScoutDao.getModelloAutoScoutDescrizione(term, idMarcaAutoScout);
	}

	
	@Override
	public List<ModelloAutoScout> getModelloAutoScout() {
		return modelloAutoScoutDao.getModelloAutoScout();
	}
	
	@Override
	public List<ModelloAutoScout> getModelliAutoScout_by_UtilizzatiDagliAutisti(){
		return modelloAutoScoutDao.getModelliAutoScout_by_UtilizzatiDagliAutisti();
	}
	
	
	@Override
	public ModelloAutoScout saveModelloAutoScout(ModelloAutoScout modelloAutoScout) throws Exception {
		return modelloAutoScoutDao.saveModelloAutoScout(modelloAutoScout);
	}
	
	
	@Override
    public void removeModelloAutoScout(long id) {
		modelloAutoScoutDao.remove(id);
    }


	
	
	
	
}
