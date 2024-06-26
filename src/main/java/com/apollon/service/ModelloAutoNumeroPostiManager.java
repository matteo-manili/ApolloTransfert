package com.apollon.service;

import java.util.List;

import com.apollon.dao.ModelloAutoNumeroPostiDao;
import com.apollon.model.ModelloAutoNumeroPosti;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface ModelloAutoNumeroPostiManager extends GenericManager<ModelloAutoNumeroPosti, Long> {
	
	void setModelloAutoNumeroPostiDao(ModelloAutoNumeroPostiDao modelloAutoNumeroPostiDao);
	
	
	ModelloAutoNumeroPosti get(Long id);
	
	ModelloAutoNumeroPosti getModelloAutoNumeroPosti_By_ModelloAutoScout_NumeroPosti(Long IdModelloAutoScout, Long IdNumeroPosti);
	
	List<ModelloAutoNumeroPosti> getModelloAutoNumeroPosti();
	
	ModelloAutoNumeroPosti saveModelloAutoNumeroPosti(ModelloAutoNumeroPosti modelloAutoNumeroPosti) throws Exception;
	
	void removeModelloAutoNumeroPosti(long userModelloAutoNumeroPosti);

	List<ModelloAutoNumeroPosti> getModelloAutoNumeroPosti_By_IdModelloAutoScout(long IdModelloAutoScout);


	//void removeModelloAutoNumeroPosti(Long idModelloAutoNumeroPosti);


	



	

}
