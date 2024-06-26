package com.apollon.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apollon.dao.AgenzieViaggioBitDao;
import com.apollon.model.AgenzieViaggioBit;
import com.apollon.service.AgenzieViaggioBitManager;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Service("AgenzieViaggioBitManager")
public class AgenzieViaggioBitManagerImpl extends GenericManagerImpl<AgenzieViaggioBit, Long> implements AgenzieViaggioBitManager {

	private AgenzieViaggioBitDao agenzieViaggioBitDao;
	
	@Override
    @Autowired
	public void setAgenzieViaggioBitDao(AgenzieViaggioBitDao agenzieViaggioBitDao) {
		this.agenzieViaggioBitDao = agenzieViaggioBitDao;
	}

	
	
	@Override
	public AgenzieViaggioBit get(Long id) {
		return this.agenzieViaggioBitDao.get(id);
	}
	
	@Override
	public List<AgenzieViaggioBit> getAgenzieViaggioBit() {
		return agenzieViaggioBitDao.getAgenzieViaggioBit();
	}
	
	@Override
	public List<AgenzieViaggioBit> getAgenzieViaggioBit_DESC() {
		return agenzieViaggioBitDao.getAgenzieViaggioBit_DESC();
	}
	
	@Override
	public List<AgenzieViaggioBit> getAgenzieViaggioBit_ASC() {
		return agenzieViaggioBitDao.getAgenzieViaggioBit_ASC();
	}
	
	@Override
	public List<AgenzieViaggioBit> getAgenzieViaggioBit_Unsubscribe() {
		return agenzieViaggioBitDao.getAgenzieViaggioBit_Unsubscribe();
	}
	
	@Override
	public AgenzieViaggioBit getEmailAgenzieViaggioBit(String email) {
		return agenzieViaggioBitDao.getEmailAgenzieViaggioBit(email);
	}
	
	
	@Override
	public List<AgenzieViaggioBit> getAgenzieViaggioBy_LIKE(String email) {
		return agenzieViaggioBitDao.getAgenzieViaggioBy_LIKE(email);
	}
	
	@Override
	public AgenzieViaggioBit getAgenzieViaggioBit_TokenSconto(String token) {
		return this.agenzieViaggioBitDao.getAgenzieViaggioBit_TokenSconto(token);
	}
	
	
	@Override
	public List<AgenzieViaggioBit> getNomeAgenzieViaggioBit(String nome) {
		return agenzieViaggioBitDao.getNomeAgenzieViaggioBit(nome);
	}
	
	@Override
	public List<AgenzieViaggioBit> getAgenzieViaggioBit_Nome(String term) {
		return agenzieViaggioBitDao.getAgenzieViaggioBit_Nome(term);
	}
	
	@Override
	public AgenzieViaggioBit saveAgenzieViaggioBit(AgenzieViaggioBit agenzieViaggioBit) throws Exception {
		return agenzieViaggioBitDao.saveAgenzieViaggioBit(agenzieViaggioBit);
	}

	
	@Override
    public void removeAgenzieViaggioBit(long id) {
		agenzieViaggioBitDao.remove(id);
    }
	
	
	
}
