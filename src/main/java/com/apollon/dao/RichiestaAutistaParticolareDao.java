package com.apollon.dao;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;

import com.apollon.model.RichiestaAutistaParticolare;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface RichiestaAutistaParticolareDao extends GenericDao<RichiestaAutistaParticolare, Long> {
	
	RichiestaAutistaParticolare get(Long id);
	
	List<RichiestaAutistaParticolare> getRichiestaAutistaParticolare();
	
	List<RichiestaAutistaParticolare> getRichiestaAutistaParticolare_by_idRicercaTransfert(long idRiceraTransfert);
	
	RichiestaAutistaParticolare saveRichiestaAutistaParticolare(RichiestaAutistaParticolare richiestaAutistaParticolare) throws DataIntegrityViolationException, HibernateJdbcException;

	void removeRichiestaAutistaParticolareByIdAutista(Long IdAutista);

	List<RichiestaAutistaParticolare> getRichiestaAutistaParticolareByAutista(Long idAutista);

	RichiestaAutistaParticolare getRichiestaAutista_by_IdRicerca_and_IdAuto(Long idRicTransfert, Long idAuto);

	RichiestaAutistaParticolare getRichiestaAutista_by_token(String token);

	RichiestaAutistaParticolare getInfoCliente_by_AutistaPanel(Long idRicTransfert, String token, int NumOreInfoCliente);

	RichiestaAutistaParticolare getInfoAutista_by_ClientePanel(Long idRicTransfert, int NumOreInfoAutista);

	List<RichiestaAutistaParticolare> getInfoAutista_by_ClientePanel_CorsaMultipla(Long idRicTransfert, int NumOreInfoAutista);
	
	List<Object[]> InvioSmsRicevuti_Autisti(long idRicTransfert);

	
	



}
