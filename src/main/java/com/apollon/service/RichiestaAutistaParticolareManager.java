package com.apollon.service;

import java.util.List;

import com.apollon.dao.RichiestaAutistaParticolareDao;
import com.apollon.model.RichiestaAutistaParticolare;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface RichiestaAutistaParticolareManager extends GenericManager<RichiestaAutistaParticolare, Long> {
	
	void setRichiestaAutistaParticolareDao(RichiestaAutistaParticolareDao richiestaAutistaParticolareDao);
	
	
	RichiestaAutistaParticolare get(Long id);
	
	List<RichiestaAutistaParticolare> getRichiestaAutistaParticolare();
	
	List<RichiestaAutistaParticolare> getRichiestaAutistaParticolareByAutista(Long idAutista);
	
	RichiestaAutistaParticolare saveRichiestaAutistaParticolare(RichiestaAutistaParticolare richiestaAutistaParticolare) throws Exception;

	void removeRichiestaAutistaParticolareByIdAutista(long idAutista);

	void removeRichiestaAutistaParticolare(long idRichiestaAutistaParticolare);

	RichiestaAutistaParticolare getRichiestaAutista_by_IdRicerca_and_IdAuto(long idRicTransfert, long idAuto);

	RichiestaAutistaParticolare getRichiestaAutista_by_token(String token);

	RichiestaAutistaParticolare getInfoCliente_by_AutistaPanel(Long idRicTransfert, String token, int NumOreInfoCliente);

	RichiestaAutistaParticolare getInfoAutista_by_ClientePanel(Long idRicTransfert, int NumOreInfoAutista);

	List<RichiestaAutistaParticolare> getInfoAutista_by_ClientePanel_CorsaMultipla(Long idRicTransfert,int NumOreInfoAutista);
	




	
	

	

	


	

}
