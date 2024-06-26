package com.apollon.dao;

import java.util.List;

import com.apollon.model.BackupInfoUtente;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface BackupInfoUtenteDao extends GenericDao<BackupInfoUtente, Long> {
	
	BackupInfoUtente get(Long id);
	
	BackupInfoUtente getBackupInfoUtenteByUser(long idUser);
	
	List<BackupInfoUtente> getBackupInfoUtente();
	
	BackupInfoUtente saveBackupInfoUtente(BackupInfoUtente backupInfoUtente);


	


}
