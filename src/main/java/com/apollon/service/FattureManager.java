package com.apollon.service;

import java.util.List;

import com.apollon.dao.FattureDao;
import com.apollon.model.Fatture;
import com.apollon.model.RichiestaMediaAutista;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface FattureManager extends GenericManager<Fatture, Long> {
	
	void setFattureDao(FattureDao fattureDao);
	
	
	Fatture get(Long id);
	
	List<Fatture> getFatture();
	
	Fatture getFatturaBy_IdRicercaTransfert(long idCorsa);
	
	Fatture getFatturaBy_IdRitardo(long idRitardo);
	
	Fatture getFatturaBy_IdRicercaTransfert_Rimbroso(long idCorsa);
	
	RichiestaMediaAutista getObjectFatturaBy_IdricercaTransfertCorsaMediaConfermata(long idCorsa);
	
	/**
	 * mi restituisce il progressivo più alto più 1
	 * @return progressivoFattura
	 */
	Long dammiNumeroProgressivoFattura();

	void removeFatture(long idFatture);
	
	Fatture saveFatture(Fatture fatture) throws Exception;

	List<Fatture> getFatture_By_ProgressivoFattua_IdCorsa(long term);

	Fatture getFatturaBy_IdSupplemento(long idSupplemento);


	void removeFatturabyRitardo(Long idRitardo);

	void removeFatturabySupplemento(Long idSupplemento);


	


	

}
