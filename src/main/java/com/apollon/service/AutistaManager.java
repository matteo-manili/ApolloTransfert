package com.apollon.service;

import java.util.List;
import com.apollon.dao.AutistaDao;
import com.apollon.model.Autista;
import com.apollon.model.AutistaAeroporti;
import com.apollon.model.AutistaPortiNavali;
import com.apollon.model.AutistaZone;
import com.apollon.model.Autoveicolo;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface AutistaManager extends GenericManager<Autista, Long> {
	
	void setAutistaDao(AutistaDao autistaDao);
	
	
	Autista get(Long id);
	
	Autista getAutistaByUser(Long id);
	
	List<Autista> getAutistiList();
	
	Autista saveAutista(Autista autista) throws Exception;
	
	void removeAutista(long userAutista) throws Exception;

	List<Autista> getAutistiBy_LIKE(String term);

	int getCalcolaNumeroCorseApprovate(long idAutista);

	int updateNumeroCorseEseguite(long idAutista, int numCorseEseguite);

	List<Autista> getAutistaTable();
	List<Autista> getAutistaTable_2_limit(int maxResults, Integer firstResult, boolean documentiCompletatiFrazione);
	int getCountAutista();

	List<AutistaZone> lazyAutistaZone(long idAutista);
	List<Autoveicolo> lazyAutoveicolo(long idAutista);
	List<AutistaAeroporti> lazyAutistaAeroporti(long idAutista);
	List<AutistaPortiNavali> lazyAutistaPortiNavali(long idAutista);
	
	List<Autista> ListAutista_Approvati();
}
