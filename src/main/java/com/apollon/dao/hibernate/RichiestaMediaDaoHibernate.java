package com.apollon.dao.hibernate;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.apollon.Constants;
import com.apollon.dao.RichiestaMediaDao;
import com.apollon.model.RichiestaMedia;
import com.apollon.util.DateUtil;
import com.apollon.webapp.rest.AgA_Giornata;
import com.apollon.webapp.util.PuliziaDatabase;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@EnableTransactionManagement
@Repository("RichiestaMediaDao")
public class RichiestaMediaDaoHibernate extends GenericDaoHibernate<RichiestaMedia, Long> implements RichiestaMediaDao {
	private static final Log log = LogFactory.getLog(RichiestaMediaDaoHibernate.class);
	
	public RichiestaMediaDaoHibernate() {
		super(RichiestaMedia.class);
	}
	
	@Transactional
	@Override
	public RichiestaMedia saveRichiestaMedia(RichiestaMedia richiestaMedia) throws DataIntegrityViolationException, HibernateJdbcException {
		getSession().saveOrUpdate(richiestaMedia);
		//getSession().flush();
		return richiestaMedia;
	}
	
	@Override
    @Transactional(readOnly = true)
	public RichiestaMedia get(Long id){
		RichiestaMedia richiestaMedia = (RichiestaMedia) getSession().get(RichiestaMedia.class, id);
		return richiestaMedia;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<RichiestaMedia> getRichiestaMedia() {
        return getSession().createCriteria(RichiestaMedia.class).addOrder(Order.desc("id")).list();
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<RichiestaMedia> getListRichiestaMedia_by_IdRicercaTransfert(long idRicercaTransfert) {
        return getSession().createCriteria(RichiestaMedia.class).add(Restrictions.eq("ricercaTransfert.id", idRicercaTransfert)).list();
	}
	
	
	
	@Override
	@Transactional(readOnly = true)
	public RichiestaMedia getRichiestaMedia_by_IdRicercaTransfert(Long idRicercaTransfert) {
        Criterion crit = Restrictions.and(Restrictions.eq("ricercaTransfert.id", idRicercaTransfert),
				Restrictions.eq("classeAutoveicoloScelta", true));
        return (RichiestaMedia) getSession().createCriteria(RichiestaMedia.class).add( crit ).uniqueResult();
	}
	
	
	@Override
	@Transactional(readOnly = true)
	public RichiestaMedia getRichiestaMedia_by_IdRicercaTransfert_e_IdClasseAutoveicolo(long idRicercaTransfert, long idClasseAutoveicolo) {
        Criterion crit = Restrictions.and(Restrictions.eq("ricercaTransfert.id", idRicercaTransfert),
        				Restrictions.eq("classeAutoveicolo.id", idClasseAutoveicolo));
        return (RichiestaMedia) getSession().createCriteria(RichiestaMedia.class).add( crit ).uniqueResult();
	}
	

	/**
	 * Successivamente eliminare anche le righe della tabella richiesta_media_autista che hanno condizione corsaConfemata = 0
	 */
	@Override
	@Transactional
	public int PuliziaDataBase_RichiestaMedia() {
		
		int NumeroOreSuccesive_CancellazioneDati_CorsaVenduta = 48;
		
		String SqlCommon_RIC = "(RIC.pagamentoEseguitoMedio = false AND RIC.id IS NULL) "
			+ "OR (RIC.pagamentoEseguitoMedio = true AND RIC.id IS NOT NULL AND RM.classeAutoveicoloScelta = false "
				+ "AND ((RIC.ritorno = false AND DATE_SUB(NOW(), INTERVAL "+NumeroOreSuccesive_CancellazioneDati_CorsaVenduta+" HOUR) > RIC.dataOraPrelevamentoDate) "
					+ "OR (RIC.ritorno = true AND DATE_SUB(NOW(), INTERVAL "+NumeroOreSuccesive_CancellazioneDati_CorsaVenduta+" HOUR) > RIC.dataOraRitornoDate))) ";
		
		String queryString_RMMA_2 = "DELETE RMAA FROM richiesta_media_autista_autoveicolo RMAA  "
				+ "INNER JOIN richiesta_media_autista RMA ON RMA.id_richiesta_media_autista = RMAA.id_richiesta_media_autista " 
				+ "INNER JOIN richiesta_media RM ON RM.id_richiesta_media = RMA.id_richiesta_media " 
				+ "INNER JOIN ricerca_transfert RIC ON RIC.id_ricerca_transfert = RM.id_ricerca_transfert " 
				+ "WHERE "+SqlCommon_RIC+"  ";
		int result1 = getSession().createSQLQuery( queryString_RMMA_2 ).executeUpdate();
		log.debug("queryString_RMMA_2 OK");
		
		String queryString_2 = "DELETE RMA FROM richiesta_media_autista RMA "
				+ "INNER JOIN richiesta_media RM ON RM.id_richiesta_media = RMA.id_richiesta_media "
				+ "INNER JOIN ricerca_transfert RIC ON RIC.id_ricerca_transfert = RM.id_ricerca_transfert " 
				+ "WHERE "+ SqlCommon_RIC;
		int result2 = getSession().createSQLQuery( queryString_2 ).executeUpdate();
		log.debug("queryString_2 OK");
		
		String queryString_3 = "DELETE RM FROM richiesta_media RM "
				+ "INNER JOIN ricerca_transfert RIC ON RIC.id_ricerca_transfert = RM.id_ricerca_transfert " 
				+ "WHERE "+SqlCommon_RIC;
		int result3 = getSession().createSQLQuery( queryString_3 ).executeUpdate();
		log.debug("queryString_3 OK");
		log.debug("result1: "+result1+" result2: "+result2+" result3: "+result3);
		return result1 + result2 + result3;
	}
	
	


	

}
