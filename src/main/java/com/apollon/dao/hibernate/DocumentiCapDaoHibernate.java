package com.apollon.dao.hibernate;



import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.apollon.dao.DocumentiCapDao;
import com.apollon.model.DocumentiCap;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@EnableTransactionManagement
@Repository("DocumentiCapDao")
public class DocumentiCapDaoHibernate extends GenericDaoHibernate<DocumentiCap, Long> implements DocumentiCapDao {

	public DocumentiCapDaoHibernate() {
		super(DocumentiCap.class);
	}
	
	
	
	@Override
    @Transactional(readOnly = true)
	public DocumentiCap get(Long id){
		DocumentiCap documentiCap = (DocumentiCap) getSession().get(DocumentiCap.class, id);
		return documentiCap;
	}
	
	
	
	@Transactional
	@Override
	public DocumentiCap saveDocumentiCap(DocumentiCap documentiCap) throws DataIntegrityViolationException, HibernateJdbcException {
		getSession().saveOrUpdate(documentiCap);
		//getSession().flush();
		return documentiCap;
	}
	
	


	

}
