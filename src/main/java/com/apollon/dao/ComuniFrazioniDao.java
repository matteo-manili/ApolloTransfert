package com.apollon.dao;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;

import com.apollon.model.ComuniFrazioni;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface ComuniFrazioniDao extends GenericDao<ComuniFrazioni, Long> {
	
	ComuniFrazioni get(Long id);
	
	List<ComuniFrazioni> getComuniFrazioni();
	
	ComuniFrazioni getComuniFrazioniByNomeFrazione_Equal(String nomeFrazione, Long idComune);
	
	List<ComuniFrazioni> getComuniFrazioniByNomeFrazione_Equal(String nomeFrazione);
	
	List<ComuniFrazioni> getNomeFrazioneBy_Like(String term);

	ComuniFrazioni saveComuniFrazioni(ComuniFrazioni comuniFrazioni) throws DataIntegrityViolationException, HibernateJdbcException;

	

	

	


}
