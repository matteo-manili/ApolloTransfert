package com.apollon.dao.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.apollon.dao.AutistaAeroportiDao;
import com.apollon.model.AutistaAeroporti;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@EnableTransactionManagement
@Repository("AutistaAeroportiDao")
public class AutistaAeroportiDaoHibernate extends GenericDaoHibernate<AutistaAeroporti, Long> implements AutistaAeroportiDao {

	public AutistaAeroportiDaoHibernate() {
		super(AutistaAeroporti.class);
	}
	
	
	
	@Override
    @Transactional(readOnly = true)
	public AutistaAeroporti get(Long id){
		AutistaAeroporti autistaAeroporti = (AutistaAeroporti) getSession().get(AutistaAeroporti.class, id);
		return autistaAeroporti;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<AutistaAeroporti> getAutistaAeroporti() {
        return getSession().createCriteria(AutistaAeroporti.class).list();
	}
	

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<AutistaAeroporti> getAutistaAeroportiByIdAutista(Long idAutista){
        return getSession().createCriteria(AutistaAeroporti.class).add(Restrictions.eq("autista.id", idAutista)).list();
	}

	
	

	
	@Transactional
	@Override
	public AutistaAeroporti saveAutistaAeroporti(AutistaAeroporti autistaAeroporti) throws DataIntegrityViolationException, HibernateJdbcException {
		getSession().saveOrUpdate(autistaAeroporti);
		//getSession().flush();
		return autistaAeroporti;
	}
	
	

	@Override
	@Transactional(readOnly = true)
	public void removeAutistaAeroportiByIdAutista(Long idAutista){
		Query qTariffe = getSession().createQuery("DELETE Tariffe WHERE autista.id = :X");
		qTariffe.setParameter("X", idAutista);
		qTariffe.executeUpdate();
		
		Query q = getSession().createQuery("DELETE AutistaAeroporti WHERE autista.id = :X");
		q.setParameter("X", idAutista);
		q.executeUpdate();
	}
	

}
