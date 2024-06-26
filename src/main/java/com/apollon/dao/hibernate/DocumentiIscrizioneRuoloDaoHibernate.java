package com.apollon.dao.hibernate;



import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.apollon.dao.DocumentiIscrizioneRuoloDao;
import com.apollon.model.DocumentiIscrizioneRuolo;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@EnableTransactionManagement
@Repository("DocumentiIscrizioneRuoloDao")
public class DocumentiIscrizioneRuoloDaoHibernate extends GenericDaoHibernate<DocumentiIscrizioneRuolo, Long> implements DocumentiIscrizioneRuoloDao {

	public DocumentiIscrizioneRuoloDaoHibernate() {
		super(DocumentiIscrizioneRuolo.class);
	}
	
	
	
	@Override
    @Transactional(readOnly = true)
	public DocumentiIscrizioneRuolo get(Long id){
		DocumentiIscrizioneRuolo documentiIscrizioneRuolo = (DocumentiIscrizioneRuolo) getSession().get(DocumentiIscrizioneRuolo.class, id);
		return documentiIscrizioneRuolo;
	}
	
	
	
	@Transactional
	@Override
	public DocumentiIscrizioneRuolo saveDocumentiIscrizioneRuolo(DocumentiIscrizioneRuolo documentiIscrizioneRuolo) throws DataIntegrityViolationException, HibernateJdbcException {
		getSession().saveOrUpdate(documentiIscrizioneRuolo);
		//getSession().flush();
		return documentiIscrizioneRuolo;
	}
	
	


	

}
