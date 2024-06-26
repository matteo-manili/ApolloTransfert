package com.apollon.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;

import com.apollon.dao.ComuniDao;
import com.apollon.model.Comuni;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface ComuniManager extends GenericManager<Comuni, Long> {
	
	void setComuniDao(ComuniDao comuniDao);
	
	
	Comuni get(Long id);
	
	List<Comuni> getComuni();
	
	Comuni saveComuni(Comuni comuni) throws DataIntegrityViolationException, HibernateJdbcException;
	
	void removeComuni(long userComuni);

	List<Comuni> getComuniByIdProvincia(long idProvincia);

	List<Long> getListComuniByRegione_soloID(long idRegione);

	List<Long> getListComuniByProvincia_soloID(long idProvincia);
	
	List<Long> getListComuniByComuneAppartenente_soloID_Suggerimenti(long idComune_Provincia);

	List<Comuni> getNomeComuneByLikeNome_Chosen(String term);

	List<Comuni> getNomeComuneBy_Like(String term);

	Comuni getComuniByNomeComune_Equal(String nomeComune, String siglaProvincia);


	

}
