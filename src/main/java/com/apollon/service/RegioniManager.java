package com.apollon.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;

import com.apollon.dao.RegioniDao;
import com.apollon.model.MacroRegioni;
import com.apollon.model.Regioni;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface RegioniManager extends GenericManager<Regioni, Long> {
	
	void setRegioniDao(RegioniDao regioniDao);
	
	Regioni get(Long id);
	
	List<Regioni> getRegioniByAutista(long idAutista);
	
	List<Regioni> getRegioni();
	
	List<Regioni> getRegioniItaliane();
	
	List<Regioni> getMacroRegioni_by_id(long idMacroRegione);

	List<MacroRegioni> getMacroRegioniList();
	
	Object dammiMenuTerrTariffeTransfer_LIKE_Url(String term);

	List<Regioni> getMacroRegioniNordItalia();

	List<Regioni> getMacroRegioniCentroItalia();

	List<Regioni> getMacroRegioniSudItalia();
	
	Regioni saveRegioni(Regioni regioni) throws DataIntegrityViolationException, HibernateJdbcException;
	
	void removeRegioni(long userRegioni);

	List<Regioni> getNomeRegioneByLikeNome_Chosen(String term);

	List<Regioni> getNomeRegioneBy_Like(String term);


	


}
