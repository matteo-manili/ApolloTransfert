package com.apollon.dao.hibernate;

import java.util.List;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.apollon.dao.AgA_ModelliTariffariDao;
import com.apollon.model.AgA_ModelliTariffari;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@EnableTransactionManagement
@Repository("AgA_ModelliTariffariDao")
public class AgA_ModelliTariffariDaoHibernate extends GenericDaoHibernate<AgA_ModelliTariffari, Long> implements AgA_ModelliTariffariDao {

	public AgA_ModelliTariffariDaoHibernate() {
		super(AgA_ModelliTariffari.class);
	}
	
	
	@Override
    @Transactional(readOnly = true)
	public AgA_ModelliTariffari get(Long id){
		AgA_ModelliTariffari agA_ModelliTariffari = (AgA_ModelliTariffari) getSession().get(AgA_ModelliTariffari.class, id);
		return agA_ModelliTariffari;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<AgA_ModelliTariffari> getAgA_ModelliTariffari_by_idAutoveicoloModelTariff(Long idAutoveicoloModelTariff) {
        return getSession().createCriteria(AgA_ModelliTariffari.class)
        		.add(Restrictions.eq("agA_AutoveicoloModelliTariffari.id", idAutoveicoloModelTariff)).list();
	}
	

	@Override
	@Transactional(readOnly = true)
	public AgA_ModelliTariffari getAgA_ModelliTariffari_by_IdAutoveicoloModelTariff_e_KmCorsa(long idAutoveicoloModelTariff, int kmCorsa) {
        Criterion crit = Restrictions.and(Restrictions.eq("agA_AutoveicoloModelliTariffari.id", idAutoveicoloModelTariff),
        				Restrictions.eq("kmCorsa", kmCorsa));
        return (AgA_ModelliTariffari) getSession().createCriteria(AgA_ModelliTariffari.class).add( crit ).uniqueResult();
	}
	

	@Transactional
	@Override
	public AgA_ModelliTariffari saveAgA_ModelliTariffari(AgA_ModelliTariffari agA_ModelliTariffari) throws DataIntegrityViolationException, HibernateJdbcException {
		getSession().saveOrUpdate(agA_ModelliTariffari);
		//getSession().flush();
		return agA_ModelliTariffari;
	}
	
	


	

}
