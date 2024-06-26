package com.apollon.dao.hibernate;

import java.util.List;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import com.apollon.dao.ComunicazioniDao;
import com.apollon.model.Comunicazioni;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@EnableTransactionManagement
@Repository("ComunicazioniDao")
public class ComunicazioniDaoHibernate extends GenericDaoHibernate<Comunicazioni, Long> implements ComunicazioniDao {

	public ComunicazioniDaoHibernate() {
		super(Comunicazioni.class);
	}
	

	
	@Transactional
	@Override
	public Comunicazioni saveComunicazioni(Comunicazioni emailAutistiMarketing) {
		getSession().saveOrUpdate(emailAutistiMarketing);
		//getSession().flush();
		return emailAutistiMarketing;
	}
	
	@Override
    @Transactional(readOnly = true)
	public Comunicazioni get(Long id){
		Comunicazioni emailAutistiMarketing = (Comunicazioni) getSession().get(Comunicazioni.class, id);
		return emailAutistiMarketing;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public List<Comunicazioni> getComunicazioni() {
        return getSession().createCriteria(Comunicazioni.class).list();
	}
	

	
	@Transactional(readOnly = true)
	@Override
	public Comunicazioni Counicazione_By_Comunicazione(String StringComunicazione) {
		Criterion crit1 = Restrictions.or(
				Restrictions.like("comunicazione", StringComunicazione)
			);
		Comunicazioni comunicazione = (Comunicazioni) getSession().createCriteria(Comunicazioni.class).add(crit1).uniqueResult();
		return comunicazione;
	}
	
	
	

	
	
	
	
	

}
