package com.apollon.dao;

import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;
import com.apollon.model.Nazioni;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface NazioniDao extends GenericDao<Nazioni, Long> {
	
	Nazioni get(Long id);
	
	List<Nazioni> getNazioni();

	List<Nazioni> getNomeNazioneBy_Like(String term);
	
	Nazioni getNazioneBy_SiglaNazione(String term);
	
	Nazioni saveNazioni(Nazioni regioni) throws DataIntegrityViolationException, HibernateJdbcException;

	



}
