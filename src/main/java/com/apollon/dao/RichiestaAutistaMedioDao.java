package com.apollon.dao;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;

import com.apollon.model.RichiestaMediaAutista;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface RichiestaAutistaMedioDao extends GenericDao<RichiestaMediaAutista, Long> {
	
	RichiestaMediaAutista get(Long id);
	
	List<RichiestaMediaAutista> getRichiestaAutistaMedio();
	
	RichiestaMediaAutista saveRichiestaAutistaMedio(RichiestaMediaAutista richiestaMediaAutista) throws DataIntegrityViolationException, HibernateJdbcException;

	void removeRichiestaAutistaMedioByIdAutista(Long IdAutista);

	List<RichiestaMediaAutista> getRichiestaAutistaMedioByAutista(Long idAutista);

	RichiestaMediaAutista getRichiestaAutista_by_IdRicerca_and_IdAutista(Long idRicTransfert, Long idAutista);

	List<RichiestaMediaAutista> getRichiestaAutista_by_IdRicerca(Long idRicTransfert);

	RichiestaMediaAutista getRichiestaAutista_by_token(String token);

	List<RichiestaMediaAutista> getRichiestaAutistaMedio_By_IdRicercaTransfert_and_ChiamataPrenotata(Long idRicercaTransfert);

	List<RichiestaMediaAutista> getRichiestaAutistaMedio_By_IdRicercaTransfert_and_CorsaConfermata(Long idRicercaTransfert);

	RichiestaMediaAutista getInfoCliente_by_AutistaPanel(Long idRicTransfert, String tokenAutista, int NumOreInfoCliente);

	RichiestaMediaAutista getInfoAutista_by_ClientePanel(Long idRicTransfert, int NumOreInfoAutista);

	List<RichiestaMediaAutista> getRichiestaAutista_by_RichiestaMediaScelta(Long idRicTransfert);

	List<RichiestaMediaAutista> getRichiestaAutista_by_RichiestaMediaScelta_Autista(Long idRicTransfert, Long idAutista);

	


	



}
