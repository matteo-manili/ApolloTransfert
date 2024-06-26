package com.apollon.dao;

import java.util.ArrayList;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;
import com.apollon.model.MacroRegioni;
import com.apollon.model.Regioni;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface RegioniDao extends GenericDao<Regioni, Long> {
	
	Regioni saveRegioni(Regioni regioni) throws DataIntegrityViolationException, HibernateJdbcException;

	MacroRegioni saveMacroRegioni(MacroRegioni macroRegioni)throws DataIntegrityViolationException, HibernateJdbcException;
	
	Regioni get(Long id);
	
	List<Regioni> getRegioni();
	
	List<Regioni> getRegioniItaliane();

	List<MacroRegioni> getMacroRegioni();
	
	List<MacroRegioni> getMacroRegioniList();
	
	List<Regioni> getMacroRegioni_by_id(long idMacroRegione);
	
	List<Regioni> getMacroRegioniNordItalia();

	List<Regioni> getMacroRegioniCentroItalia();
	
	List<Regioni> getMacroRegioniSudItalia();
	
	List<Regioni> getRegioniByAutista(long idAutista);
	
	Regioni getRegioneBy_NomeRegione_e_SiglaNazione(String termNomeRegione, String termSiglaNazione);

	List<Regioni> getNomeRegioneByLikeNome_Chosen(String term);

	List<Regioni> getNomeRegioneBy_Like(String term);
	
	Object dammiMenuTerrTariffeTransfer_LIKE_Url(String term);
	
	List<Object[]> MenuTariffe_Province();

	List<List<Object[]>> Menu_Lista_ProvinceItalianeOrderByAbitanti_MaxResult(int maxResults, Integer MaxNumeroSettimane_OldDataRequestDistance);

	List<List<Object[]>> Menu_Lista_MacroRegioneOrderByAbitanti_MaxResult(Long idMacroRegione, int maxResults, Integer MaxNumeroSettimane_OldDataRequestDistance);

	List<List<Object[]>> Menu_Lista_RegioneOrderByAbitanti_MaxResult(Long idRegione, int maxResults, Integer MaxNumeroSettimane_OldDataRequestDistance);

	List<List<Object[]>> Menu_Lista_ProvinciaOrderByAbitanti_MaxResult(Long idProvincia ,Integer MaxNumeroSettimane_OldDataRequestDistance);

	

	


	

}
