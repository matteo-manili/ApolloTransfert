package com.apollon.dao.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.apollon.dao.GestioneApplicazioneDao;
import com.apollon.model.GestioneApplicazione;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@EnableTransactionManagement
@Repository("GestioneApplicazioneDao")
public class GestioneApplicazioneDaoHibernate extends GenericDaoHibernate<GestioneApplicazione, Long> implements GestioneApplicazioneDao {

	public GestioneApplicazioneDaoHibernate() {
		super(GestioneApplicazione.class);
	}
	
	
	@Override
    @Transactional(readOnly = true)
	public GestioneApplicazione get(Long id){
		GestioneApplicazione gestioneApplicazione = (GestioneApplicazione) getSession().get(GestioneApplicazione.class, id);
		return gestioneApplicazione;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<GestioneApplicazione> getGestioneApplicazione() {
        return getSession().createCriteria(GestioneApplicazione.class).addOrder(Order.asc("id")).list();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<GestioneApplicazione> getGestioneApplicazione_senzaCommenti() {
		Criteria criteria =  getSession().createCriteria(GestioneApplicazione.class)
			.setProjection(Projections.projectionList()
					.add(Projections.property("name"), "name").add(Projections.property("valueString"), "valueString").add(Projections.property("valueNumber"), "valueNumber"))
	    		.setResultTransformer(Transformers.aliasToBean(GestioneApplicazione.class));
		return criteria.list();
	}
	
	
	@Override
	@Transactional(readOnly = true)
	public GestioneApplicazione getName(String name){
		Criterion criterion = Restrictions.eq("name", name) ;
		return (GestioneApplicazione) getSession().createCriteria(GestioneApplicazione.class).add( criterion ).uniqueResult();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<GestioneApplicazione> getGestioneApplicazioneBy_LIKE(String term){
		Long valueNumber = null;
		try{
			valueNumber = Long.parseLong(term);
		}catch ( NumberFormatException e) {
		    valueNumber = null;
		  }
		Criterion crit1 = Restrictions.or(
				Restrictions.like("name", "%"+term+"%", MatchMode.END),
				Restrictions.like("commento", "%"+term+"%", MatchMode.END),
				//Restrictions.like("valueNumber", "%"+term+"%", MatchMode.END),
				Restrictions.like("valueNumber", valueNumber),
				Restrictions.like("valueString", "%"+term+"%", MatchMode.END));
		return getSession().createCriteria(GestioneApplicazione.class).add(crit1) .list();
	}


	@Transactional
	@Override
	public GestioneApplicazione saveGestioneApplicazione(GestioneApplicazione gestioneApplicazione) throws DataIntegrityViolationException, HibernateJdbcException {
		getSession().saveOrUpdate(gestioneApplicazione);
		//getSession().flush();
		return gestioneApplicazione;
	}
	
	

	

}
