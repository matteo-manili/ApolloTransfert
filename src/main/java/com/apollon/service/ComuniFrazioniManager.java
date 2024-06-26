package com.apollon.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;

import com.apollon.dao.ComuniFrazioniDao;
import com.apollon.model.ComuniFrazioni;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface ComuniFrazioniManager extends GenericManager<ComuniFrazioni, Long> {
	
	void setComuniFrazioniDao(ComuniFrazioniDao comuniFrazioniDao);
	
	ComuniFrazioni get(Long id);
	
	List<ComuniFrazioni> getComuniFrazioni();
	
	List<ComuniFrazioni> getNomeFrazioneBy_Like(String term);

	ComuniFrazioni getComuniFrazioniByNomeFrazione_Equal(String nomeFrazione, Long idComune);

	ComuniFrazioni saveComuniFrazioni(ComuniFrazioni comuniFrazioni) throws DataIntegrityViolationException, HibernateJdbcException;
	
	void removeComuniFrazioni(long userComuniFrazioni);
	

}
