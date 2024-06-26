package com.apollon.dao.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.apollon.dao.DisponibilitaDao;
import com.apollon.model.Disponibilita;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@EnableTransactionManagement
@Repository("DisponibilitaDao")
public class DisponibilitaDaoHibernate extends GenericDaoHibernate<Disponibilita, Long> implements DisponibilitaDao {

	public DisponibilitaDaoHibernate() {
		super(Disponibilita.class);
	}
	
	
	
	@Override
    @Transactional(readOnly = true)
	public Disponibilita get(Long id){
		Disponibilita disponibilita = (Disponibilita) getSession().get(Disponibilita.class, id);
		return disponibilita;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Disponibilita> getDisponibilita() {
        return getSession().createCriteria(Disponibilita.class).list();
	}
	


	
	@Override
	@Transactional(readOnly = true)
	public Disponibilita getDisponibilitaByAutoveicolo(Long idAutoveicolo){
        return (Disponibilita) getSession().createCriteria(Disponibilita.class).add(Restrictions.eq("autoveicolo.id", idAutoveicolo)).uniqueResult();
	}

	
	

	
	@Transactional
	@Override
	public Disponibilita saveDisponibilita(Disponibilita disponibilita) throws DataIntegrityViolationException, HibernateJdbcException {
		getSession().saveOrUpdate(disponibilita);
		//getSession().flush();
		return disponibilita;
	}
	
	

	@Override
	@Transactional(readOnly = true)
	public void removeDisponibilitaByIdAutoveicolo(Long idAutoveicolo){                         
		Query q = getSession().createQuery("DELETE Disponibilita WHERE autoveicolo.id = :X");
		q.setParameter("X", idAutoveicolo);
		q.executeUpdate();
	}
	

}
