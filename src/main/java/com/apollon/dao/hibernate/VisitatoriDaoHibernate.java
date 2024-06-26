package com.apollon.dao.hibernate;

import java.util.List;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import com.apollon.dao.VisitatoriDao;
import com.apollon.model.Visitatori;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@EnableTransactionManagement
@Repository("VisitatoriDao")
public class VisitatoriDaoHibernate extends GenericDaoHibernate<Visitatori, Long> implements VisitatoriDao {

	public VisitatoriDaoHibernate() {
		super(Visitatori.class);
	}
	
	
	@Override
    @Transactional(readOnly = true)
	public Visitatori get(Long id){
		Visitatori visitatori = (Visitatori) getSession().get(Visitatori.class, id);
		return visitatori;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Visitatori> getVisitatori() {
        return getSession().createCriteria(Visitatori.class).addOrder(Order.desc("id")).list();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Visitatori> getVisitatoriTable(int maxResults, Integer firstResult) {
        return getSession().createCriteria(Visitatori.class)
        		.setFirstResult(firstResult).setMaxResults(maxResults)
        		.addOrder(Order.desc("id")).list();
	}
	
	
	@Transactional(readOnly = true)
	@Override
	public int getCountVisitatoriTable(){
		return (int)(long)getSession().createCriteria(Visitatori.class)
			.setProjection(Projections.rowCount()).uniqueResult();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Visitatori> getVisitatoriBy_LIKE(String term){
		
		Criterion crit = Restrictions.or(  
				Restrictions. like("city", "%"+term+"%", MatchMode.END),
				Restrictions. like("ipAddress", "%"+term+"%", MatchMode.END),
				Restrictions. like("nomeProviderIpLocationApi", "%"+term+"%", MatchMode.END),
				Restrictions. like("hostname", "%"+term+"%", MatchMode.END),
				Restrictions. like("provider", "%"+term+"%", MatchMode.END),
				Restrictions. like("countryCode", "%"+term+"%", MatchMode.END),
				Restrictions. like("countryName", "%"+term+"%", MatchMode.END),
				Restrictions. like("regionName", "%"+term+"%", MatchMode.END),
				Restrictions. like("city", "%"+term+"%", MatchMode.END),
				Restrictions. like("postalCode", "%"+term+"%", MatchMode.END));

		return getSession().createCriteria(Visitatori.class).add(crit).addOrder(Order.desc("dataVisita")).list();
	}
	
	
	

	
	@Transactional
	@Override
	public Visitatori saveVisitatori(Visitatori visitatori) throws DataIntegrityViolationException, HibernateJdbcException {
		getSession().saveOrUpdate(visitatori);
		//getSession().flush();
		return visitatori;
	}
	
	


	

}
