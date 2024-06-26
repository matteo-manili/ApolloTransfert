package com.apollon.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;
import org.springframework.stereotype.Service;
import com.apollon.dao.FattureDao;
import com.apollon.model.Fatture;
import com.apollon.model.RichiestaMediaAutista;
import com.apollon.service.FattureManager;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Service("FattureManager")
public class FattureManagerImpl extends GenericManagerImpl<Fatture, Long> implements FattureManager {

	private FattureDao fattureDao;
	
	@Override
    @Autowired
	public void setFattureDao(FattureDao fattureDao) {
		this.fattureDao = fattureDao;
	}

	
	
	@Override
	public Fatture get(Long id) {
		return this.fattureDao.get(id);
	}
	

	@Override
	public List<Fatture> getFatture() {
		return fattureDao.getFatture();
	}
	
	
	@Override
	public List<Fatture> getFatture_By_ProgressivoFattua_IdCorsa(long term) {
		return fattureDao.getFatture_By_ProgressivoFattua_IdCorsa(term);
	}
	
	
	@Override
	public Fatture getFatturaBy_IdRicercaTransfert(long idCorsa){
		return fattureDao.getFatturaBy_IdRicercaTransfert(idCorsa);
	}
	
	
	@Override
	public Fatture getFatturaBy_IdRitardo(long idRitardo){
		return fattureDao.getFatturaBy_IdRitardo(idRitardo);
	}
	
	@Override
	public Fatture getFatturaBy_IdSupplemento(long idSupplemento){
		return fattureDao.getFatturaBy_IdSupplemento(idSupplemento);
	}
	
	
	@Override
	public Fatture getFatturaBy_IdRicercaTransfert_Rimbroso(long idCorsa){
		return fattureDao.getFatturaBy_IdRicercaTransfert_Rimbroso(idCorsa);
	}
	
	
	@Override
	public RichiestaMediaAutista getObjectFatturaBy_IdricercaTransfertCorsaMediaConfermata(long idCorsa){
		return fattureDao.getObjectFatturaBy_IdricercaTransfertCorsaMediaConfermata(idCorsa);
	}
	

	@Override
	public Long dammiNumeroProgressivoFattura(){
		return fattureDao.dammiNumeroProgressivoFattura();
	}


	@Override
	public void removeFatturabyRitardo(Long idRitardo){                         
		fattureDao.removeFatturabyRitardo(idRitardo);
	}
	

	@Override
	public void removeFatturabySupplemento(Long idSupplemento){                         
		fattureDao.removeFatturabySupplemento(idSupplemento);
	}
	
	
	
	@Override
	public Fatture saveFatture(Fatture fatture) throws DataIntegrityViolationException, HibernateJdbcException {
		return fattureDao.saveFatture(fatture);
	}


	@Override
    public void removeFatture(long id) {
		fattureDao.remove(id);
    }



	
	
	
	
}
