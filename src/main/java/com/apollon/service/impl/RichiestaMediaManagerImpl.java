package com.apollon.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;
import org.springframework.stereotype.Service;

import com.apollon.dao.RichiestaMediaDao;
import com.apollon.model.RichiestaMedia;
import com.apollon.service.RichiestaMediaManager;





/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Service("RichiestaMediaManager")
public class RichiestaMediaManagerImpl extends GenericManagerImpl<RichiestaMedia, Long> implements RichiestaMediaManager {

	private RichiestaMediaDao richiestaMediaDao;
	
	@Override
    @Autowired
	public void setRichiestaMediaDao(RichiestaMediaDao richiestaMediaDao) {
		this.richiestaMediaDao = richiestaMediaDao;
	}

	
	@Override
	public RichiestaMedia get(Long id) {
		return this.richiestaMediaDao.get(id);
	}
	
	@Override
	public List<RichiestaMedia> getRichiestaMedia() {
		return richiestaMediaDao.getRichiestaMedia();
	}
	
	
	@Override
	public RichiestaMedia getRichiestaMedia_by_IdRicercaTransfert(Long idRicercaTransfert) {
		return richiestaMediaDao.getRichiestaMedia_by_IdRicercaTransfert(idRicercaTransfert);
	}
	
	
	@Override
	public RichiestaMedia getRichiestaMedia_by_IdRicercaTransfert_e_IdClasseAutoveicolo(long idRicercaTransfert, long idClasseAutoveicolo) {
		return richiestaMediaDao.getRichiestaMedia_by_IdRicercaTransfert_e_IdClasseAutoveicolo(idRicercaTransfert, idClasseAutoveicolo);
	}
	
	
	@Override
	public RichiestaMedia saveRichiestaMedia(RichiestaMedia richiestaMedia) throws DataIntegrityViolationException, HibernateJdbcException {
		return richiestaMediaDao.saveRichiestaMedia(richiestaMedia);
	}


	@Override
    public void removeRichiestaMedia(long id) {
		richiestaMediaDao.remove(id);
    }



	
	
	
	
}
