package com.apollon.dao.hibernate;


import java.util.List;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.apollon.dao.AutistaSottoAutistiDao;
import com.apollon.model.AutistaSottoAutisti;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@EnableTransactionManagement
@Repository("AutistaSottoAutistiDao")
public class AutistaSottoAutistiDaoHibernate extends GenericDaoHibernate<AutistaSottoAutisti, Long> implements AutistaSottoAutistiDao {

	public AutistaSottoAutistiDaoHibernate() {
		super(AutistaSottoAutisti.class);
	}
	
	
	
	@Override
    @Transactional(readOnly = true)
	public AutistaSottoAutisti get(Long id){
		AutistaSottoAutisti autistaSottoAutisti = (AutistaSottoAutisti) getSession().get(AutistaSottoAutisti.class, id);
		return autistaSottoAutisti;
	}
	
	
	

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<AutistaSottoAutisti> getAutistaSottoAutisti() {
        return getSession().createCriteria(AutistaSottoAutisti.class).addOrder(Order.desc("autista.id")).list();
	}
	
	

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<AutistaSottoAutisti> getAutistaSottoAutisti_By_Autista(long idAutista) {
        return getSession().createCriteria(AutistaSottoAutisti.class)
        		.add( Restrictions.eq("autista.id", idAutista) ).list();
	}
	
	
	
	@Transactional
	@Override
	public AutistaSottoAutisti saveAutistaSottoAutisti(AutistaSottoAutisti autistaSottoAutisti) throws DataIntegrityViolationException, HibernateJdbcException {
		getSession().saveOrUpdate(autistaSottoAutisti);
		//getSession().flush();
		return autistaSottoAutisti;
	}
	
	


	

}
