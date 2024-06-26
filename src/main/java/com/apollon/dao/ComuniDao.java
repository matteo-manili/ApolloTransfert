package com.apollon.dao;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;

import com.apollon.model.Comuni;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface ComuniDao extends GenericDao<Comuni, Long> {
	
	Comuni get(Long id);
	
	List<Comuni> getComuni();
	
	Comuni saveComuni(Comuni comuni) throws DataIntegrityViolationException, HibernateJdbcException;

	Comuni getComuniByNomeComune_Equal(String nomeComune, String siglaProvincia);
	
	List<Comuni> getComuniByNomeComune_Equal(String nomeComune);
	
	List<Comuni> getComuniByNomeComune_Equal_List(String term);

	List<Comuni> getComuniByIdProvincia(Long idProvincia);

	List<Long> getListComuniByRegione_soloID(long idRegione);

	List<Long> getListComuniByProvincia_soloID(long idProvincia);
	
	List<Long> getListComuniByComuneAppartenente_soloID_Suggerimenti(long idComune_Provincia);

	List<Comuni> getNomeComuneByLikeNome_Chosen(String term);

	List<Comuni> getNomeComuneBy_Like(String term);

	long get_idRegione(long idComune);

	List<Object> getComuni_Altri();
	
	List<Object> getComuni_Altri_Tutti();

	int Update_Clear_Column_Catasto();

	List<Object> getComuni_Duplicati();

	

}
