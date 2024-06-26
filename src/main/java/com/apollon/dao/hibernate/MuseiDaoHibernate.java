package com.apollon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.apollon.dao.MuseiDao;
import com.apollon.model.Musei;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@EnableTransactionManagement
@Repository("MuseiDao")
public class MuseiDaoHibernate extends GenericDaoHibernate<Musei, Long> implements MuseiDao {

	public MuseiDaoHibernate() {
		super(Musei.class);
	}
	
	
	
	@Override
    @Transactional(readOnly = true)
	public Musei get(Long id){
		Musei musei = (Musei) getSession().get(Musei.class, id);
		return musei;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Musei> getMusei() {
        return getSession().createCriteria(Musei.class).list();
	}
	
	@Override
    @Transactional(readOnly = true)
	public Musei getMuseiBy_PlaceId(String PlaceId){
		Criterion crit1 = Restrictions. eq("placeId", PlaceId) ;
		return (Musei) getSession().createCriteria(Musei.class).add(crit1).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Musei> getMuseiBy_LIKE(String term){
		Criterion crit1 = Restrictions. like("nomeMuseo", "%"+term+"%", MatchMode.END) ;
		return getSession().createCriteria(Musei.class).add(crit1) .list();
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Musei> getMuseiByIdComune(Long idComune){
        return getSession().createCriteria(Musei.class).add(Restrictions.eq("comuni.id", idComune)).list();
	}

	
	

	
	@Transactional
	@Override
	public Musei saveMusei(Musei musei) throws DataIntegrityViolationException, HibernateJdbcException {
		getSession().saveOrUpdate(musei);
		//getSession().flush();
		return musei;
	}
	
	

}
