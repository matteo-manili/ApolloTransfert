package com.apollon.service;

import java.util.List;

import com.apollon.dao.RichiestaAutistaMedioDao;
import com.apollon.model.RichiestaMediaAutista;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface RichiestaAutistaMedioManager extends GenericManager<RichiestaMediaAutista, Long> {
	
	void setRichiestaAutistaMedioDao(RichiestaAutistaMedioDao richiestaAutistaMedioDao);
	
	
	RichiestaMediaAutista get(Long id);
	
	List<RichiestaMediaAutista> getRichiestaAutistaMedio();
	
	List<RichiestaMediaAutista> getRichiestaAutistaMedioByAutista(Long idAutista);
	
	RichiestaMediaAutista saveRichiestaAutistaMedio(RichiestaMediaAutista richiestaMediaAutista) throws Exception;

	void removeRichiestaAutistaMedioByIdAutista(long idAutista);

	void removeRichiestaAutistaMedio(long idRichiestaAutistaMedio);

	RichiestaMediaAutista getRichiestaAutista_by_IdRicerca_and_IdAutista(long idRicTransfert, long idAutista);

	List<RichiestaMediaAutista> getRichiestaAutista_by_IdRicerca(long idRicTransfert);

	RichiestaMediaAutista getRichiestaAutista_by_token(String token);

	List<RichiestaMediaAutista> getRichiestaAutistaMedio_By_IdRicercaTransfert_and_ChiamataPrenotata(Long idRicercaTransfert);

	List<RichiestaMediaAutista> getRichiestaAutistaMedio_By_IdRicercaTransfert_and_CorsaConfermata(Long idRicercaTransfert);

	RichiestaMediaAutista getInfoCliente_by_AutistaPanel(Long idRicTransfert, String tokenAutista, int NumOreInfoCliente);

	RichiestaMediaAutista getInfoAutista_by_ClientePanel(Long idRicTransfert, int NumOreInfoAutista);




	
	

	

	


	

}
