package com.apollon.dao;

import java.util.List;
import com.apollon.model.Autista;
import com.apollon.model.AutistaAeroporti;
import com.apollon.model.AutistaPortiNavali;
import com.apollon.model.AutistaZone;
import com.apollon.model.Autoveicolo;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface AutistaDao extends GenericDao<Autista, Long> {
	
	Autista get(Long id);
	
	Autista getAutistaByUser(long idUser);
	
	List<Autista> getAutistiList();
	
	Autista saveAutista(Autista autista);

	List<Autista> getAutistaByAutistaZone(long idAutista);

	List<Autista> getIdAutisti();

	List<Autista> getAutistiBy_LIKE(String term);

	int getCalcolaNumeroCorseApprovate(long idAutista);

	int updateNumeroCorseEseguite(long idAutista, int numCorseEseguite);

	List<Autista> getAutistaTable();
	int getCountAutista();
	List<Autista> getAutistaTable_2_limit(int maxResults, Integer firstResult);

	List<AutistaZone> lazyAutistaZone(Long id);
	List<AutistaAeroporti> lazyAutistaAeroporti(Long id);
	List<AutistaPortiNavali> lazyAutistaPortiNavali(Long id);
	List<Autoveicolo> lazyAutoveicolo(Long id);

	List<Object> lazyAutistaZone_light(Long idAutista);

	List<Object[]> getDocumentiAutisti_da_Approvare(int maxResults, Integer firstResult);
	
	List<AutistaZone> getAutistiNordItalia();
	
	List<Autista> ListAutista_Approvati();
	

	

	

}
