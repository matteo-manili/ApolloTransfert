package com.apollon.service;

import java.util.List;

import com.apollon.dao.RichiestaAutistaMedioAutoveicoloDao;
import com.apollon.model.RichiestaMediaAutistaAutoveicolo;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface RichiestaAutistaMedioAutoveicoloManager extends GenericManager<RichiestaMediaAutistaAutoveicolo, Long> {
	
	void setRichiestaAutistaMedioAutoveicoloDao(RichiestaAutistaMedioAutoveicoloDao richiestaAutistaMedioAutoveicoloDao);
	
	
	RichiestaMediaAutistaAutoveicolo get(Long id);
	
	List<RichiestaMediaAutistaAutoveicolo> getRichiestaAutistaMedioAutoveicolo();
	
	List<RichiestaMediaAutistaAutoveicolo> getRichiestaAutistaMedioAutoveicoloByAutista(Long idAutista);
	
	RichiestaMediaAutistaAutoveicolo saveRichiestaAutistaMedioAutoveicolo(RichiestaMediaAutistaAutoveicolo richiestaMediaAutistaAutoveicolo) throws Exception;

	void removeRichiestaAutistaMedioAutoveicoloByIdAutista(long idAutista);

	void removeRichiestaAutistaMedioAutoveicolo(long idRichiestaAutistaMedioAutoveicolo);

	RichiestaMediaAutistaAutoveicolo getRichiestaAutista_by_IdRicerca_and_IdAuto(long idRicTransfert, long idAuto);

	List<RichiestaMediaAutistaAutoveicolo> getRichiestaAutista_by_IdRicerca(long idRicTransfert);

	RichiestaMediaAutistaAutoveicolo getRichiestaAutista_by_token(String token);


	List<RichiestaMediaAutistaAutoveicolo> getRichiestaAutistaMedioAutoveicolo_By_IdRicercaTransfert_and_CorsaConfermata(Long idRicercaTransfert);




	
	

	

	


	

}
