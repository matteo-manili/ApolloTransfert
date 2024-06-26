package com.apollon.dao.hibernate;

import java.util.List;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import com.apollon.dao.ComunicazioniUserDao;
import com.apollon.model.ComunicazioniUser;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@EnableTransactionManagement
@Repository("ComunicazioniUserDao")
public class ComunicazioniUserDaoHibernate extends GenericDaoHibernate<ComunicazioniUser, Long> implements ComunicazioniUserDao {

	public ComunicazioniUserDaoHibernate() {
		super(ComunicazioniUser.class);
	}
	

	
	@Transactional
	@Override
	public ComunicazioniUser saveComunicazioniUser(ComunicazioniUser emailAutistiMarketing) {
		getSession().saveOrUpdate(emailAutistiMarketing);
		//getSession().flush();
		return emailAutistiMarketing;
	}
	
	@Override
    @Transactional(readOnly = true)
	public ComunicazioniUser get(Long id){
		ComunicazioniUser emailAutistiMarketing = (ComunicazioniUser) getSession().get(ComunicazioniUser.class, id);
		return emailAutistiMarketing;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public List<ComunicazioniUser> getComunicazioniUser() {
        return getSession().createCriteria(ComunicazioniUser.class).list();
	}
	

	
	@Override
    @Transactional(readOnly = true)
	public ComunicazioniUser ComunicazioneUser_By_Comunicazione_e_User(String TemplateEmailComunicazione, Long userID) {
		Criterion crit1 = Restrictions.and(
				Restrictions.eq("user.id", userID) ,Restrictions.like("comunicazioni.comunicazione", TemplateEmailComunicazione)
			);
		ComunicazioniUser aaa = (ComunicazioniUser) getSession().createCriteria(ComunicazioniUser.class).
				createAlias("comunicazioni", "comunicazioni").add(crit1).uniqueResult();
		return aaa;
	}
	
	

	
	
	
	
	

}
