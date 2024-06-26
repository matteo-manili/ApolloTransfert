package com.apollon.dao.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.apollon.dao.AutistaMuseiDao;
import com.apollon.model.AutistaMusei;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@EnableTransactionManagement
@Repository("AutistaMuseiDao")
public class AutistaMuseiDaoHibernate extends GenericDaoHibernate<AutistaMusei, Long> implements AutistaMuseiDao {

	public AutistaMuseiDaoHibernate() {
		super(AutistaMusei.class);
	}
	
	
	
	@Override
    @Transactional(readOnly = true)
	public AutistaMusei get(Long id){
		AutistaMusei autistaMusei = (AutistaMusei) getSession().get(AutistaMusei.class, id);
		return autistaMusei;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<AutistaMusei> getAutistaMusei() {
        return getSession().createCriteria(AutistaMusei.class).list();
	}
	

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<AutistaMusei> getAutistaMuseiByIdAutista(Long idAutista){
        return getSession().createCriteria(AutistaMusei.class).add(Restrictions.eq("autista.id", idAutista)).list();
	}


	@Transactional
	@Override
	public AutistaMusei saveAutistaMusei(AutistaMusei autistaMusei) throws DataIntegrityViolationException, HibernateJdbcException {
		getSession().saveOrUpdate(autistaMusei);
		//getSession().flush();
		return autistaMusei;
	}
	
	
	@Override
	@Transactional(readOnly = true)
	public void removeAutistaMuseiByIdAutista(Long idAutista){                         
		Query q = getSession().createQuery("DELETE AutistaMusei WHERE autista.id = :X");
		q.setParameter("X", idAutista);
		q.executeUpdate();
	}

}
