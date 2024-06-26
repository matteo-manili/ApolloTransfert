package com.apollon.dao.hibernate;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import com.apollon.Constants;
import com.apollon.dao.AgenzieViaggioBitDao;
import com.apollon.model.AgenzieViaggioBit;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@EnableTransactionManagement
@Repository("AgenzieViaggioBitDao")
public class AgenzieViaggioBitDaoHibernate extends GenericDaoHibernate<AgenzieViaggioBit, Long> implements AgenzieViaggioBitDao {

	public AgenzieViaggioBitDaoHibernate() {
		super(AgenzieViaggioBit.class);
	}
	
	
	
	@Override
    @Transactional(readOnly = true)
	public AgenzieViaggioBit get(Long id){
		AgenzieViaggioBit agenzieViaggioBit = (AgenzieViaggioBit) getSession().get(AgenzieViaggioBit.class, id);
		return agenzieViaggioBit;
	}
	
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public List<AgenzieViaggioBit> getAgenzieViaggioBit() {
        return getSession().createCriteria(AgenzieViaggioBit.class).list();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<AgenzieViaggioBit> getAgenzieViaggioBit_DESC() {
        return getSession().createCriteria(AgenzieViaggioBit.class)
        		.addOrder( Order.desc("id") ).list();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<AgenzieViaggioBit> getAgenzieViaggioBit_ASC() {
        return getSession().createCriteria(AgenzieViaggioBit.class)
        		.addOrder( Order.asc("id") ).list();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<AgenzieViaggioBit> getAgenzieViaggioBit_Unsubscribe() {
        return getSession().createCriteria(AgenzieViaggioBit.class)
        		.addOrder(Order.desc("unsubscribe")).addOrder(Order.desc("dataInvioLastEmail")).list();
	}
	
	
	@Override
	@Transactional(readOnly = true)
	public AgenzieViaggioBit getEmailAgenzieViaggioBit(String email){
		Criterion criterion1 = Restrictions.eq("email", email) ;
		Criteria criteria = getSession().createCriteria(AgenzieViaggioBit.class).add(criterion1);
		return (AgenzieViaggioBit) criteria.uniqueResult();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<AgenzieViaggioBit> getAgenzieViaggioBy_LIKE(String term){
		Criterion crit1 = Restrictions.or(
				Restrictions.like("citta_e_indirizzo", term, MatchMode.ANYWHERE)
				,Restrictions.like("email", term, MatchMode.ANYWHERE)
				,Restrictions.like("nome", term, MatchMode.ANYWHERE)
				,Restrictions.like("sitoWeb", term, MatchMode.ANYWHERE)
				,Restrictions.like("sitoWebScraping", term, MatchMode.ANYWHERE)
				,Restrictions.like("parametriSconto", term, MatchMode.ANYWHERE) );
		return getSession().createCriteria(AgenzieViaggioBit.class).add(crit1).list();
	}
	
	
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<AgenzieViaggioBit> getNomeAgenzieViaggioBit(String nome){
		Criterion criterion1 = Restrictions.eq("nome", nome) ;
		Criteria criteria = getSession().createCriteria(AgenzieViaggioBit.class).add(criterion1);
		return criteria.list();
	}
	
	
	@Override
    @Transactional(readOnly = true)
	public AgenzieViaggioBit getAgenzieViaggioBit_TokenSconto(String token){
		
		String queryString = "SELECT * FROM data_agenzie_viaggio_bit WHERE json_extract(parametriSconto, '$."+Constants.CodiceScontoJSON+"') = :token ";
		AgenzieViaggioBit agenzieViaggioBit = (AgenzieViaggioBit) this.getSession()
				.createSQLQuery( queryString ).addEntity(AgenzieViaggioBit.class).setParameter("token", token).uniqueResult();
		return agenzieViaggioBit;
		
		/*
		Criterion criterion = Restrictions.and(
				Restrictions.like("parametriSconto", token, MatchMode.ANYWHERE),
				Restrictions.like("parametriSconto", Constants.CodiceScontoJSON, MatchMode.ANYWHERE) );
		return (AgenzieViaggioBit) getSession().createCriteria(AgenzieViaggioBit.class).add( criterion ).uniqueResult();
		*/
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<AgenzieViaggioBit> getAgenzieViaggioBit_Nome(String term) {
		Criterion criterion3 = Restrictions.like("nome", term+"%", MatchMode.START) ;
		Criteria criteria = getSession().createCriteria(AgenzieViaggioBit.class)
				.add( criterion3 ).setMaxResults(5).addOrder( Order.asc("nome") ); ;
		return criteria.list();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<AgenzieViaggioBit> getAgenzie_CodiceScontoUsato() {
		String queryString = "SELECT * FROM data_agenzie_viaggio_bit WHERE json_extract(parametriSconto, '$."+Constants.CodiceScontoUsatoJSON+"') = true ";
		List<AgenzieViaggioBit> agenzieViaggioBit = (List<AgenzieViaggioBit>) this.getSession().createSQLQuery( queryString ).addEntity(AgenzieViaggioBit.class).list();
		return agenzieViaggioBit;
	}
	
	
	
	@Transactional
	@Override
	public AgenzieViaggioBit saveAgenzieViaggioBit(AgenzieViaggioBit agenzieViaggioBit) {
		getSession().saveOrUpdate(agenzieViaggioBit);
		getSession().flush();
		return agenzieViaggioBit;
	}
	
	

}
