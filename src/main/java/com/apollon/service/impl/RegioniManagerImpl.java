package com.apollon.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;
import org.springframework.stereotype.Service;

import com.apollon.dao.RegioniDao;
import com.apollon.model.MacroRegioni;
import com.apollon.model.Regioni;
import com.apollon.service.RegioniManager;




/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Service("RegioniManager")
public class RegioniManagerImpl extends GenericManagerImpl<Regioni, Long> implements RegioniManager {

	private RegioniDao regioniDao;
	
	@Override
    @Autowired
	public void setRegioniDao(RegioniDao regioniDao) {
		this.regioniDao = regioniDao;
	}

	
	
	@Override
	public Regioni get(Long id) {
		return this.regioniDao.get(id);
	}
	
	@Override
	public List<Regioni> getRegioni() {
		return regioniDao.getRegioni();
	}
	
	@Override
	public List<Regioni> getRegioniItaliane() {
		return regioniDao.getRegioniItaliane();
	}
	
	@Override
	public List<Regioni> getMacroRegioni_by_id(long idMacroRegione) {
		return regioniDao.getMacroRegioni_by_id(idMacroRegione);
	}
	
	@Override
	public List<MacroRegioni> getMacroRegioniList() {
		return regioniDao.getMacroRegioniList();
	}
	
	@Override
	public Object dammiMenuTerrTariffeTransfer_LIKE_Url(String term) {
		return regioniDao.dammiMenuTerrTariffeTransfer_LIKE_Url(term);
	}
	
	@Override
	public List<Regioni> getMacroRegioniNordItalia() {
		return regioniDao.getMacroRegioniNordItalia();
	}
	
	@Override
	public List<Regioni> getMacroRegioniCentroItalia() {
		return regioniDao.getMacroRegioniCentroItalia();
	}
	
	@Override
	public List<Regioni> getMacroRegioniSudItalia() {
		return regioniDao.getMacroRegioniSudItalia();
	}
	

	
	@Override
	public List<Regioni> getNomeRegioneBy_Like(String term) {
		return regioniDao.getNomeRegioneBy_Like(term);
	}
	
	
	@Override
	public List<Regioni> getNomeRegioneByLikeNome_Chosen(String term) {
		return regioniDao.getNomeRegioneByLikeNome_Chosen(term);
	}
	
	@Override
	public List<Regioni> getRegioniByAutista(long idAutista) {
		return regioniDao.getRegioniByAutista(idAutista);
	}
	
	
	
	@Override
	public Regioni saveRegioni(Regioni regioni) throws DataIntegrityViolationException, HibernateJdbcException {
		return regioniDao.saveRegioni(regioni);
	}

	
	@Override
    public void removeRegioni(long id) {
		regioniDao.remove(id);
    }
	
	
	
}
