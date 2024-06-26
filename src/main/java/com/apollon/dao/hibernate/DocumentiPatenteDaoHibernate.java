package com.apollon.dao.hibernate;



import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.apollon.dao.DocumentiPatenteDao;
import com.apollon.model.DocumentiPatente;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@EnableTransactionManagement
@Repository("DocumentiPatenteDao")
public class DocumentiPatenteDaoHibernate extends GenericDaoHibernate<DocumentiPatente, Long> implements DocumentiPatenteDao {

	public DocumentiPatenteDaoHibernate() {
		super(DocumentiPatente.class);
	}
	
	
	
	@Override
    @Transactional(readOnly = true)
	public DocumentiPatente get(Long id){
		DocumentiPatente documentiPatente = (DocumentiPatente) getSession().get(DocumentiPatente.class, id);
		return documentiPatente;
	}
	
	
	
	@Transactional
	@Override
	public DocumentiPatente saveDocumentiPatente(DocumentiPatente documentiPatente) throws DataIntegrityViolationException, HibernateJdbcException {
		getSession().saveOrUpdate(documentiPatente);
		//getSession().flush();
		return documentiPatente;
	}
	
	


	

}
