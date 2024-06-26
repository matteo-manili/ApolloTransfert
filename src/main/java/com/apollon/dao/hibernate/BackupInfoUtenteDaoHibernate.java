package com.apollon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.apollon.dao.BackupInfoUtenteDao;
import com.apollon.model.BackupInfoUtente;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@EnableTransactionManagement
@Repository("BackupInfoUtenteDao")
public class BackupInfoUtenteDaoHibernate extends GenericDaoHibernate<BackupInfoUtente, Long> implements BackupInfoUtenteDao {

	public BackupInfoUtenteDaoHibernate() {
		super(BackupInfoUtente.class);
	}
	
	
	
	@Override
    @Transactional(readOnly = true)
	public BackupInfoUtente get(Long id){
		BackupInfoUtente backupInfoUtente = (BackupInfoUtente) getSession().get(BackupInfoUtente.class, id);
		return backupInfoUtente;
	}
	
	
	
	@Transactional(readOnly = true)
	@Override
	public BackupInfoUtente getBackupInfoUtenteByUser(long idUser) {
		return (BackupInfoUtente) getSession().createCriteria(BackupInfoUtente.class).add(Restrictions.eq("user.id", idUser)).uniqueResult();
		
	}
	

	
	
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public List<BackupInfoUtente> getBackupInfoUtente() {
        return getSession().createCriteria(BackupInfoUtente.class).list();
	}
	

	
	
	@Transactional
	@Override
	public BackupInfoUtente saveBackupInfoUtente(BackupInfoUtente backupInfoUtente) {
		getSession().saveOrUpdate(backupInfoUtente);
		//getSession().flush();
		return backupInfoUtente;
	}
	
	

}
