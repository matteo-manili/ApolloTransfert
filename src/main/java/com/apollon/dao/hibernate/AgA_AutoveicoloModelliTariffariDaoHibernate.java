package com.apollon.dao.hibernate;

import java.util.List;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import com.apollon.dao.AgA_AutoveicoloModelliTariffariDao;
import com.apollon.model.AgA_AutoveicoloModelliTariffari;
import com.apollon.model.AgA_ModelliTariffari;
import com.apollon.model.AgA_Tariffari;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@EnableTransactionManagement
@Repository("AgA_AutoveicoloModelliTariffariDao")
public class AgA_AutoveicoloModelliTariffariDaoHibernate extends GenericDaoHibernate<AgA_AutoveicoloModelliTariffari, Long> implements AgA_AutoveicoloModelliTariffariDao {

	public AgA_AutoveicoloModelliTariffariDaoHibernate() {
		super(AgA_AutoveicoloModelliTariffari.class);
	}
	
	@Transactional
	@Override
	public AgA_AutoveicoloModelliTariffari saveAgA_AutoveicoloModelliTariffari(AgA_AutoveicoloModelliTariffari agA_AutoveicoloModelliTariffari) throws DataIntegrityViolationException, HibernateJdbcException {
		getSession().saveOrUpdate(agA_AutoveicoloModelliTariffari);
		//getSession().flush();
		return agA_AutoveicoloModelliTariffari;
	}
	
	
	@Override
    @Transactional(readOnly = true)
	public AgA_AutoveicoloModelliTariffari get(Long id){
		AgA_AutoveicoloModelliTariffari agA_AutoveicoloModelliTariffari = (AgA_AutoveicoloModelliTariffari) getSession().get(AgA_AutoveicoloModelliTariffari.class, id);
		return agA_AutoveicoloModelliTariffari;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<AgA_AutoveicoloModelliTariffari> getAgA_AutoveicoloModelliTariffari() {
        return getSession().createCriteria(AgA_AutoveicoloModelliTariffari.class).addOrder(Order.desc("id")).list();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<AgA_AutoveicoloModelliTariffari> getAgA_AutoveicoloModelliTariffari_by_IdAutoveicolo(long idAutoveicolo) {
        return getSession().createCriteria(AgA_AutoveicoloModelliTariffari.class).add(Restrictions.eq("autoveicolo.id", idAutoveicolo)).addOrder(Order.asc("id")).list();
	}

	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public void EliminaModelliTariffari(long idAutoModelTariff) {
		// faccio le select
		List<AgA_ModelliTariffari> agA_ModelliTariffariList = getSession().createCriteria(AgA_ModelliTariffari.class)
        		.add(Restrictions.eq("agA_AutoveicoloModelliTariffari.id", idAutoModelTariff)).list();
		List<AgA_Tariffari> agA_TariffariList = getSession().createCriteria(AgA_Tariffari.class)
				.add(Restrictions.eq("agA_AutoveicoloModelliTariffari.id", idAutoModelTariff)).list();
		// Creo i Tariffari
		for(AgA_Tariffari iteTariff: agA_TariffariList) {
			for(AgA_ModelliTariffari ite_modelTariff: agA_ModelliTariffariList) {
				AgA_Tariffari agA_Tariffari = new AgA_Tariffari(ite_modelTariff.getKmCorsa(), ite_modelTariff.isEseguiCorse(), ite_modelTariff.getPrezzoCorsa(), ite_modelTariff.getKmRaggioArea(), iteTariff.getAgA_Giornate());
				getSession().save(agA_Tariffari);
			}
		}
		// Rimuovo tutti i riferimenti del agA_AutoveicoloModelliTariffari vedere i CASCADE=REMOVE
		remove( idAutoModelTariff );
	}
	

}
