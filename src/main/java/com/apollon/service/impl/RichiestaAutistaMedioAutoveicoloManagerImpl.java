package com.apollon.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apollon.dao.RichiestaAutistaMedioAutoveicoloDao;
import com.apollon.model.RichiestaMediaAutistaAutoveicolo;
import com.apollon.service.RichiestaAutistaMedioAutoveicoloManager;





/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Service("RichiestaAutistaMedioAutoveicoloManager")
public class RichiestaAutistaMedioAutoveicoloManagerImpl extends GenericManagerImpl<RichiestaMediaAutistaAutoveicolo, Long> implements RichiestaAutistaMedioAutoveicoloManager {

	private RichiestaAutistaMedioAutoveicoloDao richiestaAutistaMedioAutoveicoloDao;
	
	@Override
    @Autowired
	public void setRichiestaAutistaMedioAutoveicoloDao(RichiestaAutistaMedioAutoveicoloDao richiestaAutistaMedioAutoveicoloDao) {
		this.richiestaAutistaMedioAutoveicoloDao = richiestaAutistaMedioAutoveicoloDao;
	}

	
	
	@Override
	public RichiestaMediaAutistaAutoveicolo get(Long id) {
		return this.richiestaAutistaMedioAutoveicoloDao.get(id);
	}
	

	
	/* (non-Javadoc)
	 * @see com.apollon.service.RichiestaAutistaMedioAutoveicoloManager#getRichiestaAutista_by_IdRicerca_and_IdAuto(long, long)
	 */
	@Override
	public RichiestaMediaAutistaAutoveicolo getRichiestaAutista_by_IdRicerca_and_IdAuto(long idRicTransfert, long idAuto) {
		return richiestaAutistaMedioAutoveicoloDao.getRichiestaAutista_by_IdRicerca_and_IdAuto(idRicTransfert, idAuto);
	}
	
	
	@Override
	public RichiestaMediaAutistaAutoveicolo getRichiestaAutista_by_token(String token){
		return richiestaAutistaMedioAutoveicoloDao.getRichiestaAutista_by_token(token);
		
		
	}
	
	
	@Override
	public List<RichiestaMediaAutistaAutoveicolo> getRichiestaAutista_by_IdRicerca(long idRicTransfert) {
		return richiestaAutistaMedioAutoveicoloDao.getRichiestaAutista_by_IdRicerca(idRicTransfert);
	}
	
	
	@Override
	public List<RichiestaMediaAutistaAutoveicolo> getRichiestaAutistaMedioAutoveicolo_By_IdRicercaTransfert_and_CorsaConfermata(Long idRicercaTransfert){
		return richiestaAutistaMedioAutoveicoloDao.getRichiestaAutistaMedioAutoveicolo_By_IdRicercaTransfert_and_CorsaConfermata(idRicercaTransfert);
	}
	
	
	
	@Override
	public List<RichiestaMediaAutistaAutoveicolo> getRichiestaAutistaMedioAutoveicolo() {
		return richiestaAutistaMedioAutoveicoloDao.getRichiestaAutistaMedioAutoveicolo();
	}
	
	

	
	
	@Override
	public List<RichiestaMediaAutistaAutoveicolo> getRichiestaAutistaMedioAutoveicoloByAutista(Long idAutista){
		return richiestaAutistaMedioAutoveicoloDao.getRichiestaAutistaMedioAutoveicoloByAutista(idAutista);
	}
	
	

	
	
	@Override
	public RichiestaMediaAutistaAutoveicolo saveRichiestaAutistaMedioAutoveicolo(RichiestaMediaAutistaAutoveicolo richiestaMediaAutistaAutoveicolo) throws Exception {
		return richiestaAutistaMedioAutoveicoloDao.saveRichiestaAutistaMedioAutoveicolo(richiestaMediaAutistaAutoveicolo);
	}

	
	
	@Override
    public void removeRichiestaAutistaMedioAutoveicoloByIdAutista(long idAutista) {
		richiestaAutistaMedioAutoveicoloDao.removeRichiestaAutistaMedioAutoveicoloByIdAutista(idAutista);
    }
	
	
	@Override
    public void removeRichiestaAutistaMedioAutoveicolo(long id) {
		richiestaAutistaMedioAutoveicoloDao.remove(id);
    }



	
	
	
	
}
