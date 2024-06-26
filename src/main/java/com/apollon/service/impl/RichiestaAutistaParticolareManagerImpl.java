package com.apollon.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;
import org.springframework.stereotype.Service;

import com.apollon.dao.RichiestaAutistaParticolareDao;
import com.apollon.model.RichiestaAutistaParticolare;
import com.apollon.service.RichiestaAutistaParticolareManager;





/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Service("RichiestaAutistaParticolareManager")
public class RichiestaAutistaParticolareManagerImpl extends GenericManagerImpl<RichiestaAutistaParticolare, Long> implements RichiestaAutistaParticolareManager {

	private RichiestaAutistaParticolareDao richiestaAutistaParticolareDao;
	
	@Override
    @Autowired
	public void setRichiestaAutistaParticolareDao(RichiestaAutistaParticolareDao richiestaAutistaParticolareDao) {
		this.richiestaAutistaParticolareDao = richiestaAutistaParticolareDao;
	}

	@Override
	public RichiestaAutistaParticolare get(Long id) {
		return this.richiestaAutistaParticolareDao.get(id);
	}
	
	/* (non-Javadoc)
	 * @see com.apollon.service.RichiestaAutistaParticolareManager#getRichiestaAutista_by_IdRicerca_and_IdAuto(long, long)
	 */
	@Override
	public RichiestaAutistaParticolare getRichiestaAutista_by_IdRicerca_and_IdAuto(long idRicTransfert, long idAuto) {
		return richiestaAutistaParticolareDao.getRichiestaAutista_by_IdRicerca_and_IdAuto(idRicTransfert, idAuto);
	}
	
	@Override
	public RichiestaAutistaParticolare getInfoCliente_by_AutistaPanel(Long idRicTransfert, String token, int NumOreInfoCliente){
		return richiestaAutistaParticolareDao.getInfoCliente_by_AutistaPanel(idRicTransfert, token, NumOreInfoCliente);
	}
	
	@Override
	public RichiestaAutistaParticolare getInfoAutista_by_ClientePanel(Long idRicTransfert, int NumOreInfoAutista){
		return richiestaAutistaParticolareDao.getInfoAutista_by_ClientePanel(idRicTransfert, NumOreInfoAutista);
	}
	
	@Override
	public List<RichiestaAutistaParticolare> getInfoAutista_by_ClientePanel_CorsaMultipla(Long idRicTransfert, int NumOreInfoAutista){
		return richiestaAutistaParticolareDao.getInfoAutista_by_ClientePanel_CorsaMultipla(idRicTransfert, NumOreInfoAutista);
	}
	
	@Override
	public RichiestaAutistaParticolare getRichiestaAutista_by_token(String token) {
		return richiestaAutistaParticolareDao.getRichiestaAutista_by_token(token);
	}
	
	
	@Override
	public List<RichiestaAutistaParticolare> getRichiestaAutistaParticolare() {
		return richiestaAutistaParticolareDao.getRichiestaAutistaParticolare();
	}
	
	
	@Override
	public List<RichiestaAutistaParticolare> getRichiestaAutistaParticolareByAutista(Long idAutista){
		return richiestaAutistaParticolareDao.getRichiestaAutistaParticolareByAutista(idAutista);
	}

	
	@Override
	public RichiestaAutistaParticolare saveRichiestaAutistaParticolare(RichiestaAutistaParticolare richiestaAutistaParticolare) throws DataIntegrityViolationException, HibernateJdbcException {
		return richiestaAutistaParticolareDao.saveRichiestaAutistaParticolare(richiestaAutistaParticolare);
	}

	
	@Override
    public void removeRichiestaAutistaParticolareByIdAutista(long idAutista) {
		richiestaAutistaParticolareDao.removeRichiestaAutistaParticolareByIdAutista(idAutista);
    }
	
	
	@Override
    public void removeRichiestaAutistaParticolare(long id) {
		richiestaAutistaParticolareDao.remove(id);
    }



	
	
	
	
}
