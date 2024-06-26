package com.apollon.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apollon.dao.ModelloAutoNumeroPostiDao;
import com.apollon.model.ModelloAutoNumeroPosti;
import com.apollon.service.ModelloAutoNumeroPostiManager;





/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Service("ModelloAutoNumeroPostiManager")
public class ModelloAutoNumeroPostiManagerImpl extends GenericManagerImpl<ModelloAutoNumeroPosti, Long> implements ModelloAutoNumeroPostiManager {

	private ModelloAutoNumeroPostiDao modelloAutoNumeroPostiDao;
	
	@Override
    @Autowired
	public void setModelloAutoNumeroPostiDao(ModelloAutoNumeroPostiDao modelloAutoNumeroPostiDao) {
		this.modelloAutoNumeroPostiDao = modelloAutoNumeroPostiDao;
	}

	
	
	@Override
	public ModelloAutoNumeroPosti get(Long id) {
		return this.modelloAutoNumeroPostiDao.get(id);
	}
	

	@Override
	public ModelloAutoNumeroPosti getModelloAutoNumeroPosti_By_ModelloAutoScout_NumeroPosti(Long IdModelloAutoScout, Long IdNumeroPosti) {
		return modelloAutoNumeroPostiDao.getModelloAutoNumeroPosti_By_ModelloAutoScout_NumeroPosti(IdModelloAutoScout, IdNumeroPosti);
	}
	
	
	@Override
	public List<ModelloAutoNumeroPosti> getModelloAutoNumeroPosti() {
		return modelloAutoNumeroPostiDao.getModelloAutoNumeroPosti();
	}

	
	@Override
	public List<ModelloAutoNumeroPosti> getModelloAutoNumeroPosti_By_IdModelloAutoScout(long IdModelloAutoScout) {
		return modelloAutoNumeroPostiDao.getModelloAutoNumeroPosti_By_IdModelloAutoScout(IdModelloAutoScout);
	}
	
	
	@Override
	public ModelloAutoNumeroPosti saveModelloAutoNumeroPosti(ModelloAutoNumeroPosti modelloAutoNumeroPosti) throws Exception {
		return modelloAutoNumeroPostiDao.saveModelloAutoNumeroPosti(modelloAutoNumeroPosti);
	}


	
	@Override
	public void removeModelloAutoNumeroPosti(long id) {
		modelloAutoNumeroPostiDao.remove(id);
		
	}



}
