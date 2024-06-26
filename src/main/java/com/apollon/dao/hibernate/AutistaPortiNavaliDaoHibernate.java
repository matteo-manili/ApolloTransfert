package com.apollon.dao.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.apollon.dao.AutistaPortiNavaliDao;
import com.apollon.model.AutistaPortiNavali;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@EnableTransactionManagement
@Repository("AutistaPortiNavaliDao")
public class AutistaPortiNavaliDaoHibernate extends GenericDaoHibernate<AutistaPortiNavali, Long> implements AutistaPortiNavaliDao {

	public AutistaPortiNavaliDaoHibernate() {
		super(AutistaPortiNavali.class);
	}
	
	
	
	@Override
    @Transactional(readOnly = true)
	public AutistaPortiNavali get(Long id){
		AutistaPortiNavali autistaPortiNavali = (AutistaPortiNavali) getSession().get(AutistaPortiNavali.class, id);
		return autistaPortiNavali;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<AutistaPortiNavali> getAutistaPortiNavali() {
        return getSession().createCriteria(AutistaPortiNavali.class).list();
	}
	

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<AutistaPortiNavali> getAutistaPortiNavaliByIdAutista(Long idAutista){
        return getSession().createCriteria(AutistaPortiNavali.class).add(Restrictions.eq("autista.id", idAutista)).list();
	}


	@Transactional
	@Override
	public AutistaPortiNavali saveAutistaPortiNavali(AutistaPortiNavali autistaPortiNavali) throws DataIntegrityViolationException, HibernateJdbcException {
		getSession().saveOrUpdate(autistaPortiNavali);
		//getSession().flush();
		return autistaPortiNavali;
	}
	
	@Override
	@Transactional(readOnly = true)
	public void removeAutistaPortiNavaliByIdAutista(Long idAutista){
		Query qTariffe = getSession().createQuery("DELETE Tariffe WHERE autista.id = :X");
		qTariffe.setParameter("X", idAutista);
		qTariffe.executeUpdate();
		
		Query q = getSession().createQuery("DELETE AutistaPortiNavali WHERE autista.id = :X");
		q.setParameter("X", idAutista);
		q.executeUpdate();
	}

}
