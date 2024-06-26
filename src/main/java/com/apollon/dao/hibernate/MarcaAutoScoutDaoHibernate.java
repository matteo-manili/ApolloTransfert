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
import com.apollon.dao.MarcaAutoScoutDao;
import com.apollon.model.MarcaAutoScout;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@EnableTransactionManagement
@Repository("MarcaAutoScoutDao")
public class MarcaAutoScoutDaoHibernate extends GenericDaoHibernate<MarcaAutoScout, Long> implements MarcaAutoScoutDao {

	public MarcaAutoScoutDaoHibernate() {
		super(MarcaAutoScout.class);
	}
	
	
	
	@Override
    @Transactional(readOnly = true)
	public MarcaAutoScout get(Long id){
		MarcaAutoScout marcaAutoScout = (MarcaAutoScout) getSession().get(MarcaAutoScout.class, id);
		return marcaAutoScout;
	}
	
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public List<MarcaAutoScout> getMarcaAutoScout() {
        return getSession().createCriteria(MarcaAutoScout.class).list();
	}
	
	
	@Override
	@Transactional(readOnly = true)
	public MarcaAutoScout getNomeMarcaAutoScout(String nomeMarca){
		Criterion criterion1 = Restrictions.eq("name", nomeMarca) ;
		Criteria criteria = getSession().createCriteria(MarcaAutoScout.class).add(criterion1);
		return (MarcaAutoScout) criteria.uniqueResult();
	}
	
	
	@Override
    @Transactional(readOnly = true)
	public MarcaAutoScout getMarcaAutoScout_by_IdAutoScout(Long idAutoScout){
		Criterion criterion1 = Restrictions.eq("idAutoScout", idAutoScout) ;
		Criteria criteria = getSession().createCriteria(MarcaAutoScout.class).add(criterion1);
		return (MarcaAutoScout) criteria.uniqueResult();
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<MarcaAutoScout> getMarcaAutoScoutDescrizione(String term) {
		
		Criterion criterion3 = Restrictions.like("name", term+"%", MatchMode.START) ;
		Criteria criteria = getSession().createCriteria(MarcaAutoScout.class)
				.add( criterion3 ).setMaxResults(5).addOrder( Order.asc("name") ); ;
		return criteria.list();
	}
	
	
	@Transactional
	@Override
	public MarcaAutoScout saveMarcaAutoScout(MarcaAutoScout marcaAutoScout) {
		getSession().saveOrUpdate(marcaAutoScout);
		getSession().flush();
		return marcaAutoScout;
	}
	
	

}
