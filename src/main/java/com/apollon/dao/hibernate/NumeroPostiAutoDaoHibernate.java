package com.apollon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.apollon.dao.NumeroPostiAutoDao;
import com.apollon.model.NumeroPostiAuto;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@EnableTransactionManagement
@Repository("NumeroPostiAutoDao")
public class NumeroPostiAutoDaoHibernate extends GenericDaoHibernate<NumeroPostiAuto, Long> implements NumeroPostiAutoDao {

	public NumeroPostiAutoDaoHibernate() {
		super(NumeroPostiAuto.class);
	}
	
	
	
	@Override
    @Transactional(readOnly = true)
	public NumeroPostiAuto get(Long id){
		NumeroPostiAuto numeroPostiAuto = (NumeroPostiAuto) getSession().get(NumeroPostiAuto.class, id);
		return numeroPostiAuto;
	}
	
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public List<NumeroPostiAuto> getNumeroPostiAuto() {
        return getSession().createCriteria(NumeroPostiAuto.class).addOrder(Order.asc("id")) .list();
	}
	
	
	@Override
	public NumeroPostiAuto saveNumeroPostiAuto(NumeroPostiAuto numeroPostiAuto) {
		getSession().saveOrUpdate(numeroPostiAuto);
		//getSession().flush();
		return numeroPostiAuto;
	}
	
	

}
