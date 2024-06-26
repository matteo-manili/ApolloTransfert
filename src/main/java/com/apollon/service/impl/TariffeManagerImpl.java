package com.apollon.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apollon.dao.TariffeDao;
import com.apollon.model.Tariffe;
import com.apollon.service.TariffeManager;





/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Service("TariffeManager")
public class TariffeManagerImpl extends GenericManagerImpl<Tariffe, Long> implements TariffeManager {

	private TariffeDao tariffeDao;
	
	@Override
    @Autowired
	public void setTariffeDao(TariffeDao tariffeDao) {
		this.tariffeDao = tariffeDao;
	}

	
	
	@Override
	public Tariffe get(Long id) {
		return this.tariffeDao.get(id);
	}
	
	
	@Override
	public List<Tariffe> getTariffe() {
		return tariffeDao.getTariffe();
	}
	
	
	
	@Override
	public boolean TariffeImpostate(Long id){
			return tariffeDao.TariffeImpostate(id);
	}
	
	@Override
	public Tariffe getTariffeBy_Autoveicolo_e_Zona(Long idAutoveicolo, Long idZona){
		return tariffeDao.getTariffeBy_Autoveicolo_e_Zona(idAutoveicolo, idZona);
	}
	
	@Override
	public Tariffe getTariffeBy_Autoveicolo_e_Aeroporto(Long idAutoveicolo, Long idAero){
		return tariffeDao.getTariffeBy_Autoveicolo_e_Aeroporto(idAutoveicolo, idAero);
	}
	
	@Override
	public Tariffe getTariffeBy_Autoveicolo_e_Porto(Long idAutoveicolo, Long idPorto){
		return tariffeDao.getTariffeBy_Autoveicolo_e_Porto(idAutoveicolo, idPorto);
	}
	
	
	@Override
	public List<Tariffe> getTariffeByIdAutista(Long idAutista){
		return tariffeDao.getTariffeByIdAutista(idAutista);
	}
	
	
	@Override
	public Tariffe saveTariffe(Tariffe tariffe) throws Exception {
		return tariffeDao.saveTariffe(tariffe);
	}

	
	
	@Override
    public void removeTariffeByIdAutista(long idAutista) {
		tariffeDao.removeTariffeByIdAutista(idAutista);
    }
	
	
	@Override
    public void removeTariffe(long id) {
		tariffeDao.remove(id);
    }
	
	
	
}
