package com.apollon.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;
import org.springframework.stereotype.Service;

import com.apollon.dao.ComuniDao;
import com.apollon.model.Comuni;
import com.apollon.service.ComuniManager;




/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Service("ComuniManager")
public class ComuniManagerImpl extends GenericManagerImpl<Comuni, Long> implements ComuniManager {

	private ComuniDao comuniDao;
	
	@Override
    @Autowired
	public void setComuniDao(ComuniDao comuniDao) {
		this.comuniDao = comuniDao;
	}

	
	
	@Override
	public Comuni get(Long id) {
		return this.comuniDao.get(id);
	}
	

	@Override
	public List<Comuni> getComuni() {
		return comuniDao.getComuni();
	}
	
	@Override
	public List<Comuni> getComuniByIdProvincia(long idProvincia) {
		return comuniDao.getComuniByIdProvincia(idProvincia);
	}
	
	@Override
	public List<Comuni> getNomeComuneBy_Like(String term) {
		return comuniDao.getNomeComuneBy_Like(term);
	}
	
	@Override
	public List<Comuni> getNomeComuneByLikeNome_Chosen(String term) {
		return comuniDao.getNomeComuneByLikeNome_Chosen(term);
	}
	
	
	@Override
	public List<Long> getListComuniByRegione_soloID(long idRegione) {
		return comuniDao.getListComuniByRegione_soloID(idRegione);
	}
	
	@Override
	public List<Long> getListComuniByProvincia_soloID(long idProvincia) {
		return comuniDao.getListComuniByProvincia_soloID(idProvincia);
	}
	
	@Override
	public List<Long> getListComuniByComuneAppartenente_soloID_Suggerimenti(long idComune_Provincia) {
		return comuniDao.getListComuniByComuneAppartenente_soloID_Suggerimenti(idComune_Provincia);
	}
	
	@Override
	public Comuni getComuniByNomeComune_Equal(String nomeComune, String siglaProvincia){
		return comuniDao.getComuniByNomeComune_Equal(nomeComune, siglaProvincia);
	}
	
	@Override
	public Comuni saveComuni(Comuni comuni) throws DataIntegrityViolationException, HibernateJdbcException {
		return comuniDao.saveComuni(comuni);
	}

	
	@Override
    public void removeComuni(long id) {
		comuniDao.remove(id);
    }
	
	
	
}
