package com.apollon.dao;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;

import com.apollon.model.Fatture;
import com.apollon.model.RichiestaMediaAutista;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface FattureDao extends GenericDao<Fatture, Long> {
	
	Fatture get(Long id);
	
	List<Fatture> getFatture();
	
	/**
	 * mi da il numero più alto più 1
	 */
	Long dammiNumeroProgressivoFattura();

	RichiestaMediaAutista getObjectFatturaBy_IdricercaTransfertCorsaMediaConfermata(long idCorsa);
	
	Fatture saveFatture(Fatture fatture) throws DataIntegrityViolationException, HibernateJdbcException;

	Fatture getFatturaBy_IdRicercaTransfert(long idCorsa);

	Fatture getFatturaBy_IdRitardo(long idRitardo);
	
	Fatture getFatturaBy_IdRicercaTransfert_Rimbroso(long idCorsa);

	List<Fatture> getFatture_By_ProgressivoFattua_IdCorsa(long term);

	Fatture getFatturaBy_IdSupplemento(long idSupplemento);

	void removeFatturabyRitardo(Long idRitardo);
	
	void removeFatturabySupplemento(Long idSupplemento);

	

	


	










	


	



}
