package com.apollon.dao;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;

import com.apollon.model.RichiestaMediaAutistaAutoveicolo;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface RichiestaAutistaMedioAutoveicoloDao extends GenericDao<RichiestaMediaAutistaAutoveicolo, Long> {
	
	RichiestaMediaAutistaAutoveicolo get(Long id);
	
	List<RichiestaMediaAutistaAutoveicolo> getRichiestaAutistaMedioAutoveicolo();
	
	RichiestaMediaAutistaAutoveicolo saveRichiestaAutistaMedioAutoveicolo(RichiestaMediaAutistaAutoveicolo richiestaMediaAutistaAutoveicolo) throws DataIntegrityViolationException, HibernateJdbcException;

	void removeRichiestaAutistaMedioAutoveicoloByIdAutista(Long IdAutista);

	List<RichiestaMediaAutistaAutoveicolo> getRichiestaAutistaMedioAutoveicoloByAutista(Long idAutista);

	RichiestaMediaAutistaAutoveicolo getRichiestaAutista_by_IdRicerca_and_IdAuto(Long idRicTransfert, Long idAuto);

	List<RichiestaMediaAutistaAutoveicolo> getRichiestaAutista_by_IdRicerca(Long idRicTransfert);

	RichiestaMediaAutistaAutoveicolo getRichiestaAutista_by_token(String token);

	List<RichiestaMediaAutistaAutoveicolo> getRichiestaAutistaMedioAutoveicolo_By_IdRicercaTransfert_and_CorsaConfermata(Long idRicercaTransfert);

	


	



}
