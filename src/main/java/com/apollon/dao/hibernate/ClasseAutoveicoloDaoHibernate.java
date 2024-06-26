package com.apollon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.apollon.dao.ClasseAutoveicoloDao;
import com.apollon.model.ClasseAutoveicolo;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@EnableTransactionManagement
@Repository("ClasseAutoveicoloDao")
public class ClasseAutoveicoloDaoHibernate extends GenericDaoHibernate<ClasseAutoveicolo, Long> implements ClasseAutoveicoloDao {

	public ClasseAutoveicoloDaoHibernate() {
		super(ClasseAutoveicolo.class);
	}
	
	
	
	@Override
    @Transactional(readOnly = true)
	public ClasseAutoveicolo get(Long id){
		ClasseAutoveicolo classeAutoveicolo = (ClasseAutoveicolo) getSession().get(ClasseAutoveicolo.class, id);
		return classeAutoveicolo;
	}
	
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public List<ClasseAutoveicolo> getClasseAutoveicolo() {
        return getSession().createCriteria(ClasseAutoveicolo.class).addOrder(Order.asc("id")) .list();
	}
	
	
	@Override
	public ClasseAutoveicolo saveClasseAutoveicolo(ClasseAutoveicolo classeAutoveicolo) {
		getSession().saveOrUpdate(classeAutoveicolo);
		//getSession().flush();
		return classeAutoveicolo;
	}
	
	

}
