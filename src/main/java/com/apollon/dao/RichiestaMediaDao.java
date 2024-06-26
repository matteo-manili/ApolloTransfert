package com.apollon.dao;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;

import com.apollon.model.RichiestaMedia;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface RichiestaMediaDao extends GenericDao<RichiestaMedia, Long> {
	
	RichiestaMedia get(Long id);
	
	List<RichiestaMedia> getRichiestaMedia();
	
	List<RichiestaMedia> getListRichiestaMedia_by_IdRicercaTransfert(long idRicercaTransfert);
	
	RichiestaMedia saveRichiestaMedia(RichiestaMedia richiestaMedia) throws DataIntegrityViolationException, HibernateJdbcException;

	RichiestaMedia getRichiestaMedia_by_IdRicercaTransfert(Long idRicercaTransfert);

	RichiestaMedia getRichiestaMedia_by_IdRicercaTransfert_e_IdClasseAutoveicolo(long idRicercaTransfert, long idClasseAutoveicolo);

	int PuliziaDataBase_RichiestaMedia();

	












	


	



}
