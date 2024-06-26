package com.apollon.dao.hibernate;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.apollon.dao.SupplementiDao;
import com.apollon.model.Supplementi;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@EnableTransactionManagement
@Repository("SupplementiDao")
public class SupplementiDaoHibernate extends GenericDaoHibernate<Supplementi, Long> implements SupplementiDao {

	public SupplementiDaoHibernate() {
		super(Supplementi.class);
	}
	
	
	@Override
    @Transactional(readOnly = true)
	public Supplementi get(Long id){
		Supplementi supplementi = (Supplementi) getSession().get(Supplementi.class, id);
		return supplementi;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Supplementi> getSupplementi() {
		return getSession().createCriteria(Supplementi.class)
				.addOrder(Order.asc("pagato")).addOrder(Order.desc("ricercaTransfert")).addOrder(Order.desc("id")).list();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Supplementi> getSupplementiBy_IdRicercaTransfert(long idCorsa) {
		return getSession().createCriteria(Supplementi.class).add(Restrictions.eq("ricercaTransfert.id", idCorsa)).addOrder(Order.desc("id")).list();
	}
	
	

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Supplementi> getSupplementiCliente(long idUser) {
		Criterion crit = Restrictions.gt("prezzo", new BigDecimal(0) );
        return getSession().createCriteria(Supplementi.class).createAlias("ricercaTransfert", "ricTransfert")
        		.add( Restrictions.eq("ricTransfert.user.id", idUser) ).add(crit).addOrder(Order.asc("pagato")).list();
	}
	
	
	@Transactional
	@Override
	public void removeSupplementobyId(Long idSupplemento) {                         
		Query q = getSession().createQuery("DELETE Supplementi WHERE id = :X");
		q.setParameter("X", idSupplemento);
		q.executeUpdate();
	}
	
	
	@Transactional
	@Override
	public Supplementi saveSupplementi(Supplementi supplementi) throws DataIntegrityViolationException, HibernateJdbcException {
		getSession().saveOrUpdate(supplementi);
		//getSession().flush();
		return supplementi;
	}
	
	


	

}
