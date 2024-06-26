package com.apollon.dao;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;

import com.apollon.model.ModelloAutoNumeroPosti;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface ModelloAutoNumeroPostiDao extends GenericDao<ModelloAutoNumeroPosti, Long> {
	
	ModelloAutoNumeroPosti get(Long id);
	
	ModelloAutoNumeroPosti getModelloAutoNumeroPosti_By_ModelloAutoScout_NumeroPosti(Long IdModelloAutoScout, Long IdNumeroPosti);
	
	List<ModelloAutoNumeroPosti> getModelloAutoNumeroPosti();
	
	ModelloAutoNumeroPosti saveModelloAutoNumeroPosti(ModelloAutoNumeroPosti modelloAutoNumeroPosti) throws DataIntegrityViolationException, HibernateJdbcException;

	List<ModelloAutoNumeroPosti> getModelloAutoNumeroPosti_By_IdModelloAutoScout(long IdModelloAutoScout);

	//void removeModelloAutoNumeroPosti(Long idModelloAutoNumeroPosti);

	

}
