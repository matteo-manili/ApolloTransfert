package com.apollon.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apollon.dao.RichiestaAutistaMedioDao;
import com.apollon.model.RichiestaMediaAutista;
import com.apollon.service.RichiestaAutistaMedioManager;





/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Service("RichiestaAutistaMedioManager")
public class RichiestaAutistaMedioManagerImpl extends GenericManagerImpl<RichiestaMediaAutista, Long> implements RichiestaAutistaMedioManager {

	private RichiestaAutistaMedioDao richiestaAutistaMedioDao;
	
	@Override
    @Autowired
	public void setRichiestaAutistaMedioDao(RichiestaAutistaMedioDao richiestaAutistaMedioDao) {
		this.richiestaAutistaMedioDao = richiestaAutistaMedioDao;
	}

	
	@Override
	public RichiestaMediaAutista get(Long id) {
		return this.richiestaAutistaMedioDao.get(id);
	}
	
	/* (non-Javadoc)
	 * @see com.apollon.service.RichiestaAutistaMedioManager#getRichiestaAutista_by_IdRicerca_and_IdAuto(long, long)
	 */
	@Override
	public RichiestaMediaAutista getRichiestaAutista_by_IdRicerca_and_IdAutista(long idRicTransfert, long dAutista) {
		return richiestaAutistaMedioDao.getRichiestaAutista_by_IdRicerca_and_IdAutista(idRicTransfert, dAutista);
	}
	
	@Override
	public RichiestaMediaAutista getInfoCliente_by_AutistaPanel(Long idRicTransfert, String tokenAutista, int NumOreInfoCliente){
		return richiestaAutistaMedioDao.getInfoCliente_by_AutistaPanel(idRicTransfert, tokenAutista, NumOreInfoCliente);
	}
	
	@Override
	public RichiestaMediaAutista getInfoAutista_by_ClientePanel(Long idRicTransfert, int NumOreInfoAutista){ //4545
		return richiestaAutistaMedioDao.getInfoAutista_by_ClientePanel(idRicTransfert, NumOreInfoAutista);
	}
	
	@Override
	public RichiestaMediaAutista getRichiestaAutista_by_token(String token){
		return richiestaAutistaMedioDao.getRichiestaAutista_by_token(token);
	}
	
	@Override
	public List<RichiestaMediaAutista> getRichiestaAutista_by_IdRicerca(long idRicTransfert) {
		return richiestaAutistaMedioDao.getRichiestaAutista_by_IdRicerca(idRicTransfert);
	}
	
	@Override
	public List<RichiestaMediaAutista> getRichiestaAutistaMedio_By_IdRicercaTransfert_and_ChiamataPrenotata(Long idRicercaTransfert){
		return richiestaAutistaMedioDao.getRichiestaAutistaMedio_By_IdRicercaTransfert_and_ChiamataPrenotata(idRicercaTransfert);
	}
	
	@Override
	public List<RichiestaMediaAutista> getRichiestaAutistaMedio_By_IdRicercaTransfert_and_CorsaConfermata(Long idRicercaTransfert){
		return richiestaAutistaMedioDao.getRichiestaAutistaMedio_By_IdRicercaTransfert_and_CorsaConfermata(idRicercaTransfert);
	}
	
	@Override
	public List<RichiestaMediaAutista> getRichiestaAutistaMedio() {
		return richiestaAutistaMedioDao.getRichiestaAutistaMedio();
	}
	
	@Override
	public List<RichiestaMediaAutista> getRichiestaAutistaMedioByAutista(Long idAutista){
		return richiestaAutistaMedioDao.getRichiestaAutistaMedioByAutista(idAutista);
	}

	@Override
	public RichiestaMediaAutista saveRichiestaAutistaMedio(RichiestaMediaAutista richiestaMediaAutista) throws Exception {
		return richiestaAutistaMedioDao.saveRichiestaAutistaMedio(richiestaMediaAutista);
	}

	@Override
    public void removeRichiestaAutistaMedioByIdAutista(long idAutista) {
		richiestaAutistaMedioDao.removeRichiestaAutistaMedioByIdAutista(idAutista);
    }
	
	@Override
    public void removeRichiestaAutistaMedio(long id) {
		richiestaAutistaMedioDao.remove(id);
    }



	
	
	
	
}
