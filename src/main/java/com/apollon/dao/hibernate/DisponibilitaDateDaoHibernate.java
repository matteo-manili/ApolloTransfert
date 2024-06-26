package com.apollon.dao.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.apollon.dao.DisponibilitaDateDao;
import com.apollon.model.DisponibilitaDate;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@EnableTransactionManagement
@Repository("DisponibilitaDateDao")
public class DisponibilitaDateDaoHibernate extends GenericDaoHibernate<DisponibilitaDate, Long> implements DisponibilitaDateDao {

	public DisponibilitaDateDaoHibernate() {
		super(DisponibilitaDate.class);
	}
	
	
	
	@Override
    @Transactional(readOnly = true)
	public DisponibilitaDate get(Long id){
		DisponibilitaDate disponibilitaDate = (DisponibilitaDate) getSession().get(DisponibilitaDate.class, id);
		return disponibilitaDate;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<DisponibilitaDate> getDisponibilitaDate() {
        return getSession().createCriteria(DisponibilitaDate.class).list();
	}
	





	@Override
	@Transactional(readOnly = true)
	public DisponibilitaDate getDisponibilitaDateBy_Data(Date data){
		
        return (DisponibilitaDate) getSession().createCriteria(DisponibilitaDate.class).add( Restrictions.eq("data", data) ).uniqueResult();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<DisponibilitaDate> getDisponibilitaDateByIdDisponibilita(Long IdDisponibilita){
        return getSession().createCriteria(DisponibilitaDate.class).add(Restrictions.eq("disponibilita.id", IdDisponibilita)).list();
	}

	
	

	
	@Transactional
	@Override
	public DisponibilitaDate saveDisponibilitaDate(DisponibilitaDate disponibilitaDate) throws DataIntegrityViolationException, HibernateJdbcException {
		getSession().saveOrUpdate(disponibilitaDate);
		return disponibilitaDate;
	}
	
	

	@Override
	@Transactional(readOnly = true)
	public void removeDisponibilitaDateByIdDisponibilita(Long idDisponibilita){                         
		Query q = getSession().createQuery("DELETE DisponibilitaDate WHERE disponibilita.id = :X");
		q.setParameter("X", idDisponibilita);
		q.executeUpdate();
	}
	

}
