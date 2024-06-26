package com.apollon.service;

import java.util.List;

import com.apollon.dao.RichiestaMediaDao;
import com.apollon.model.RichiestaMedia;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface RichiestaMediaManager extends GenericManager<RichiestaMedia, Long> {
	
	void setRichiestaMediaDao(RichiestaMediaDao richiestaMediaDao);
	
	
	RichiestaMedia get(Long id);
	
	List<RichiestaMedia> getRichiestaMedia();
	
	RichiestaMedia saveRichiestaMedia(RichiestaMedia richiestaMedia) throws Exception;

	void removeRichiestaMedia(long idRichiestaMedia);


	RichiestaMedia getRichiestaMedia_by_IdRicercaTransfert(Long idRicercaTransfert);


	RichiestaMedia getRichiestaMedia_by_IdRicercaTransfert_e_IdClasseAutoveicolo(long idRicercaTransfert, long idClasseAutoveicolo);













	
	

	

	


	

}
