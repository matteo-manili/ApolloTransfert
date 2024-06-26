package com.apollon.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;
import org.springframework.stereotype.Service;

import com.apollon.dao.AutoveicoloDao;
import com.apollon.model.Autoveicolo;
import com.apollon.service.AutoveicoloManager;




/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Service("AutoveicoloManager")
public class AutoveicoloManagerImpl extends GenericManagerImpl<Autoveicolo, Long> implements AutoveicoloManager {

	private AutoveicoloDao autoveicoloDao;
	
	@Override
    @Autowired
	public void setAutoveicoloDao(AutoveicoloDao autoveicoloDao) {
		this.autoveicoloDao = autoveicoloDao;
	}

	@Override
	public Autoveicolo get(Long id) {
		return this.autoveicoloDao.get(id);
	}
	
	@Override
	public boolean autoveicoliPresentiNonCancellati(long idAutista) {
		return this.autoveicoloDao.autoveicoliPresentiNonCancellati(idAutista);
	}
	
	@Override
	public Autoveicolo getConInfoAutista(Long idAuto) {
		return this.autoveicoloDao.getConInfoAutista(idAuto);
	}
	
	@Override
	public Autoveicolo getAutoveicolo_By_Targa(String targa){
		return this.autoveicoloDao.getAutoveicolo_By_Targa(targa);
	}
	
	@Override
	public List<Autoveicolo> getAutoveicolo() {
		return autoveicoloDao.getAutoveicolo();
	}
	
	@Override
	public List<Autoveicolo> getAutoveicolo_by_OrderModelloAutoScout() {
		return autoveicoloDao.getAutoveicolo_by_OrderModelloAutoScout();
	}

	/**
	 * visualizzaAutoCancellate TRUE: include anche le auto cancellate, FALSE: esclude le auto cancellate
	 */
	@Override
	public List<Autoveicolo> getAutoveicoloByAutista(long idAutista, boolean visualizzaAutoCancellate) {
		return autoveicoloDao.getAutoveicoloByAutista(idAutista, visualizzaAutoCancellate);
	}
	
	@Override
	public List<Autoveicolo> getAutoveicoloByAutista_Agenda(long idAutista) {
		return autoveicoloDao.getAutoveicoloByAutista_Agenda(idAutista);
	}
	
	@Override
	public List<Autoveicolo> getAutovecoloList_like_NomeMarca(String term) {
		return autoveicoloDao.getAutovecoloList_like_NomeMarca(term);
	}
	
	@Override
	public List<Autoveicolo> getAutovecoloList_like_NomeModello(String term) {
		return autoveicoloDao.getAutovecoloList_like_NomeModello(term);
	}
	
	@Override
	public Autoveicolo saveAutoveicolo(Autoveicolo autoveicolo) throws DataIntegrityViolationException, HibernateJdbcException {
		return autoveicoloDao.saveAutoveicolo(autoveicolo);
	}

	@Override
    public void removeAutoveicolo(long id) {
		autoveicoloDao.remove(id);
    }
	
	
	
}
