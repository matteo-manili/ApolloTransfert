package com.apollon.dao;

import java.util.List;

import com.apollon.model.Autista;
import com.apollon.model.ComunicazioniUser;
import com.apollon.model.User;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface ComunicazioniUserDao extends GenericDao<ComunicazioniUser, Long> {
	
	ComunicazioniUser saveComunicazioniUser(ComunicazioniUser emailAutistiMarketing);
	
	ComunicazioniUser get(Long id);
	
	List<ComunicazioniUser> getComunicazioniUser();
	
	ComunicazioniUser ComunicazioneUser_By_Comunicazione_e_User(String TemplateEmailComunicazione, Long UserId);


}
