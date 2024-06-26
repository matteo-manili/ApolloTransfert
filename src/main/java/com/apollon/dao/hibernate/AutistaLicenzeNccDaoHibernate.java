package com.apollon.dao.hibernate;


import java.util.List;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.apollon.dao.AutistaLicenzeNccDao;
import com.apollon.model.AutistaLicenzeNcc;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@EnableTransactionManagement
@Repository("AutistaLicenzeNccDao")
public class AutistaLicenzeNccDaoHibernate extends GenericDaoHibernate<AutistaLicenzeNcc, Long> implements AutistaLicenzeNccDao {

	public AutistaLicenzeNccDaoHibernate() {
		super(AutistaLicenzeNcc.class);
	}
	
	
	@Override
    @Transactional(readOnly = true)
	public AutistaLicenzeNcc get(Long id){
		AutistaLicenzeNcc autistaLicenzeNcc = (AutistaLicenzeNcc) getSession().get(AutistaLicenzeNcc.class, id);
		return autistaLicenzeNcc;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<AutistaLicenzeNcc> getAutistaLicenzeNcc() {
        return getSession().createCriteria(AutistaLicenzeNcc.class).addOrder(Order.desc("autista.id")).list();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<AutistaLicenzeNcc> getAutistaLicenzeNcc_By_Autista(long idAutista) {
        return getSession().createCriteria(AutistaLicenzeNcc.class)
        		.add( Restrictions.eq("autista.id", idAutista) ).list();
	}
	
	
	@Transactional
	@Override
	public AutistaLicenzeNcc saveAutistaLicenzeNcc(AutistaLicenzeNcc autistaLicenzeNcc) throws DataIntegrityViolationException, HibernateJdbcException {
		getSession().saveOrUpdate(autistaLicenzeNcc);
		//getSession().flush();
		return autistaLicenzeNcc;
	}
	
	


	

}
