package com.apollon.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;

import com.apollon.dao.AutoveicoloDao;
import com.apollon.model.Autoveicolo;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface AutoveicoloManager extends GenericManager<Autoveicolo, Long> {
	
	void setAutoveicoloDao(AutoveicoloDao autoveicoloDao);
	
	
	Autoveicolo get(Long id);
	
	Autoveicolo getConInfoAutista(Long idAuto);
	
	List<Autoveicolo> getAutoveicolo();
	
	List<Autoveicolo> getAutoveicolo_by_OrderModelloAutoScout();
	
	/**
	 * visualizzaAutoCancellate TRUE: include anche le auto cancellate, FALSE: esclude le auto cancellate
	 */
	List<Autoveicolo> getAutoveicoloByAutista(long idAutista, boolean visualizzaAutoCancellate);
	
	List<Autoveicolo> getAutoveicoloByAutista_Agenda(long idAutista);
	
	Autoveicolo saveAutoveicolo(Autoveicolo autoveicolo) throws DataIntegrityViolationException, HibernateJdbcException;
	
	void removeAutoveicolo(long userAutoveicolo);

	boolean autoveicoliPresentiNonCancellati(long idAutista);

	Autoveicolo getAutoveicolo_By_Targa(String targa);

	List<Autoveicolo> getAutovecoloList_like_NomeMarca(String term);

	List<Autoveicolo> getAutovecoloList_like_NomeModello(String term);
	
	
}
