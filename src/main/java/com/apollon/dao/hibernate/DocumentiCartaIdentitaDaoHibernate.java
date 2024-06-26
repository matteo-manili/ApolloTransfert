package com.apollon.dao.hibernate;



import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.apollon.dao.DocumentiCartaIdentitaDao;
import com.apollon.model.DocumentiCartaIdentita;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@EnableTransactionManagement
@Repository("DocumentiCartaIdentitaDao")
public class DocumentiCartaIdentitaDaoHibernate extends GenericDaoHibernate<DocumentiCartaIdentita, Long> implements DocumentiCartaIdentitaDao {

	public DocumentiCartaIdentitaDaoHibernate() {
		super(DocumentiCartaIdentita.class);
	}
	
	
	
	@Override
    @Transactional(readOnly = true)
	public DocumentiCartaIdentita get(Long id){
		DocumentiCartaIdentita documentiCartaIdentita = (DocumentiCartaIdentita) getSession().get(DocumentiCartaIdentita.class, id);
		return documentiCartaIdentita;
	}
	
	
	
	@Transactional
	@Override
	public DocumentiCartaIdentita saveDocumentiCartaIdentita(DocumentiCartaIdentita documentiCartaIdentita) throws DataIntegrityViolationException, HibernateJdbcException {
		getSession().saveOrUpdate(documentiCartaIdentita);
		//getSession().flush();
		return documentiCartaIdentita;
	}
	
	


	

}
