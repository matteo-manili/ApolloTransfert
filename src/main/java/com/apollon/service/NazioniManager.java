package com.apollon.service;

import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;
import com.apollon.dao.NazioniDao;
import com.apollon.model.Nazioni;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface NazioniManager extends GenericManager<Nazioni, Long> {
	
	void setNazioniDao(NazioniDao regioniDao);
	
	Nazioni get(Long id);
	
	List<Nazioni> getNazioni();
	
	Nazioni saveNazioni(Nazioni regioni) throws DataIntegrityViolationException, HibernateJdbcException;
	
	void removeNazioni(long userNazioni);

	List<Nazioni> getNomeNazioneBy_Like(String term);


	


}
