package com.apollon.dao;

import java.util.Date;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;
import com.apollon.model.Autoveicolo;
import com.apollon.webapp.util.bean.AgendaAutista_Autista;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface AutoveicoloDao extends GenericDao<Autoveicolo, Long> {
	
	Autoveicolo get(Long id);
	
	Autoveicolo getConInfoAutista(Long idAuto);
	
	List<Autoveicolo> getAutoveicolo();
	
	List<Autoveicolo> getAutoveicolo_by_OrderModelloAutoScout();
	
	List<Autoveicolo> getAutoveicoloByAutista_Agenda(long idAutista);

	List<Autoveicolo> getAutoveicoloByAutista(long idAutista, boolean visualizzaAutoCancellate);

	boolean autoveicoliPresentiNonCancellati(long idAutista);

	boolean ControlloAutoveicoliSospesiMenu(long idAutista);
	
	/**
	 * 	Mi ritorna una lista di dati Object composta di Object[]: Autoveicolo, Long Autista id, Int Autista percentualeServizio
	 */
	List<Object> getAutoveicoliCalcoloTariffe_percentualeAutistaList(long idUser);


	Autoveicolo getAutoveicolo_By_Targa(String targa);
	
	Autoveicolo saveAutoveicolo(Autoveicolo autoveicolo) throws DataIntegrityViolationException, HibernateJdbcException;

	List<Autoveicolo> getAutovecoloList_like_NomeMarca(String term);

	List<Autoveicolo> getAutovecoloList_like_NomeModello(String term);

	List<AgendaAutista_Autista> Result_AgendaAutista(long PercentualeServizio, int numeroPasseggeri, Date dataOraPrelevamento, long distanza, double latPartenza, double lngPartenza, double latArrivo, double lngArrivo);

	List<Object[]> Result_AgendaAutista_TEST(Date dataOraPrelevamento, long durataConTrafficoValue);

	

	

}
