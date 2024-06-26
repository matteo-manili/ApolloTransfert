package com.apollon.dao.hibernate;

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
import com.apollon.dao.RitardiDao;
import com.apollon.model.Ritardi;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@EnableTransactionManagement
@Repository("RitardiDao")
public class RitardiDaoHibernate extends GenericDaoHibernate<Ritardi, Long> implements RitardiDao {

	public RitardiDaoHibernate() {
		super(Ritardi.class);
	}
	
	
	@Override
    @Transactional(readOnly = true)
	public Ritardi get(Long id){
		Ritardi ritardi = (Ritardi) getSession().get(Ritardi.class, id);
		return ritardi;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Ritardi> getRitardi() {
        return getSession().createCriteria(Ritardi.class)
        		.addOrder(Order.asc("pagatoAndata")).addOrder(Order.asc("pagatoRitorno"))
        		.addOrder(Order.desc("prezzoAndata")).addOrder(Order.desc("prezzoRitorno")).list();
	}
	
	
	@Override
	@Transactional(readOnly = true)
	public Ritardi getRitardoBy_IdRicercaTransfert(long idCorsa){
		return (Ritardi) getSession().createCriteria(Ritardi.class).add(Restrictions.eq("ricercaTransfert.id", idCorsa)).uniqueResult();
	}
	
	

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Ritardi> getRitardiCliente(long idUser) {
		
		Criterion crit = Restrictions.or(
				Restrictions.gt("numeroMezzoreRitardiAndata", 0),
				Restrictions.gt("numeroMezzoreRitardiRitorno", 0));
		
        return getSession().createCriteria(Ritardi.class).createAlias("ricercaTransfert", "ricTransfert")
        		.add( Restrictions.eq("ricTransfert.user.id", idUser) ).add(crit)
        		.addOrder(Order.asc("pagatoAndata")).addOrder(Order.asc("pagatoRitorno")).list();
	}
	
	
	
	
	
	
	@Transactional
	@Override
	public void removeRitardobyId(Long idRitardo){                         
		Query q = getSession().createQuery("DELETE Ritardi WHERE id = :X");
		q.setParameter("X", idRitardo);
		q.executeUpdate();
	}
	
	
	
	@Transactional
	@Override
	public Ritardi saveRitardi(Ritardi ritardi) throws DataIntegrityViolationException, HibernateJdbcException {
		getSession().saveOrUpdate(ritardi);
		//getSession().flush();
		return ritardi;
	}
	
	


	

}
