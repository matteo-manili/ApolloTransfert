package com.apollon.dao.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import com.apollon.dao.AgA_TariffariDao;
import com.apollon.model.AgA_Giornate;
import com.apollon.model.AgA_ModelliTariffari;
import com.apollon.model.AgA_Tariffari;
import com.apollon.util.NumberUtil;
import com.apollon.util.UtilBukowski;
import com.apollon.webapp.rest.AgA_General;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@EnableTransactionManagement
@Repository("AgA_TariffariDao")
public class AgA_TariffariDaoHibernate extends GenericDaoHibernate<AgA_Tariffari, Long> implements AgA_TariffariDao {

	public AgA_TariffariDaoHibernate() {
		super(AgA_Tariffari.class);
	}
	
	
	@Override
    @Transactional(readOnly = true)
	public AgA_Tariffari get(Long id){
		AgA_Tariffari agA_Tariffari = (AgA_Tariffari) getSession().get(AgA_Tariffari.class, id);
		return agA_Tariffari;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<AgA_Tariffari> getAgA_Tariffari_by_idGiornata(Long idGiornata) {
        return getSession().createCriteria(AgA_Tariffari.class).add(Restrictions.eq("agA_Giornate.id", idGiornata)).list();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<AgA_Tariffari> getAgA_Tariffari_by_dataGiornataOrario_idAutoveicolo(Date dataGiornataOrario, long idAutoveicolo) {
		Criterion crit = Restrictions.and(Restrictions.eq("agA_Giornate.dataGiornataOrario", dataGiornataOrario),Restrictions.eq("agA_Giornate.autoveicolo.id", idAutoveicolo));
        return getSession().createCriteria(AgA_Tariffari.class).createAlias("agA_Giornate", "agA_Giornate").add( crit ).list();
	}
	
	

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<AgA_Tariffari> ConvertiModelliTariffari_in_Tariffari_ed_EliminaIdModelliTariffari(Date dataGiornataOrario, long idAutoveicolo) {
		
		Criterion crit = Restrictions.and(Restrictions.eq("agA_Giornate.dataGiornataOrario", dataGiornataOrario),Restrictions.eq("agA_Giornate.autoveicolo.id", idAutoveicolo));
		List<AgA_Tariffari> agA_TariffariList = getSession().createCriteria(AgA_Tariffari.class).createAlias("agA_Giornate", "agA_Giornate").add( crit ).list();
		
		//Raccolgo i ModelliTariffari da trasformare i Tariffari
		HashMap<Long, List<AgA_ModelliTariffari>> IdGiornata_ModelliTariffariList = new HashMap<Long, List<AgA_ModelliTariffari>>();
		for(AgA_Tariffari ite: agA_TariffariList) {
			if( ite.getAgA_AutoveicoloModelliTariffari() != null && IdGiornata_ModelliTariffariList.containsKey(ite.getAgA_Giornate().getId()) == false ) {
				List<AgA_ModelliTariffari> modelliTariffariList = getSession().createCriteria(AgA_ModelliTariffari.class)
						.add(Restrictions.eq("agA_AutoveicoloModelliTariffari.id", ite.getAgA_AutoveicoloModelliTariffari().getId() )).list();
				IdGiornata_ModelliTariffariList.put(ite.getAgA_Giornate().getId(), modelliTariffariList);
			}
		}
		if( IdGiornata_ModelliTariffariList.isEmpty() == false) {
			// Creo i Tariffari
			for (Map.Entry<Long, List<AgA_ModelliTariffari>> entry : IdGiornata_ModelliTariffariList.entrySet())  {
				AgA_Giornate agA_Giornate = (AgA_Giornate) getSession().get(AgA_Giornate.class, entry.getKey());
				for( AgA_ModelliTariffari ite : entry.getValue() ) {
					AgA_Tariffari agA_Tariffari = new AgA_Tariffari(ite.getKmCorsa(), ite.isEseguiCorse(), ite.getPrezzoCorsa(), ite.getKmRaggioArea(), agA_Giornate);
					getSession().save(agA_Tariffari);
				}
			}
			// Elimino i ModelliTariffari
			for(AgA_Tariffari ite: agA_TariffariList) {
				if( ite.getAgA_AutoveicoloModelliTariffari() != null  ) {
					getSession().delete(ite);
				}
			}
			// Rifaccio la Select
			List<Long> idGiornateList = new ArrayList<Long>();
			for(AgA_Tariffari ite: agA_TariffariList) {
				idGiornateList.add(ite.getAgA_Giornate().getId());
			}
			return getSession().createCriteria(AgA_Tariffari.class).add( 
					Restrictions.and(Restrictions.in("agA_Giornate.id", NumberUtil.removeDuplicatesLong(idGiornateList))) ).list();
		
		}else {
			return agA_TariffariList;
		}
	}
	
	
	

	@Override
	@Transactional(readOnly = true)
	public AgA_Tariffari getAgA_Tariffari_by_IdGiornata_e_KmCorsa(long idGiornata, int kmCorsa) {
        Criterion crit = Restrictions.and(Restrictions.eq("agA_Giornate.id", idGiornata),Restrictions.eq("kmCorsa", kmCorsa));
        return (AgA_Tariffari) getSession().createCriteria(AgA_Tariffari.class).add( crit ).uniqueResult();
	}
	

	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<AgA_Tariffari> getAgA_Tariffari_by_IdGiornata_e_kmCorsaFrom_kmCorsaTo(long idGiornata, Integer kmCorsaFrom, Integer kmCorsaTo) {
		Criterion crit = Restrictions.and(
				Restrictions.eq("agA_Giornate.id", idGiornata), 
				Restrictions.ge("kmCorsa", kmCorsaFrom),Restrictions.le("kmCorsa", kmCorsaTo) );
        return getSession().createCriteria(AgA_Tariffari.class).add( crit ).list();
	}
	
	

	@Transactional
	@Override
	public AgA_Tariffari saveAgA_Tariffari(AgA_Tariffari agA_Tariffari) throws DataIntegrityViolationException, HibernateJdbcException {
		getSession().saveOrUpdate(agA_Tariffari);
		//getSession().flush();
		return agA_Tariffari;
	}
	
	
	@Transactional
	@Override
	public void removeAgA_Tariffari_by_idGiornata(Long idGiornata) {       
		// Elimina
		String queryString_1 = "DELETE aga_tariffari "
				+ "FROM aga_giornate, aga_tariffari "
				+ "WHERE "
				+ "aga_giornate.id_aga_giornate = :idGiornata "
				+ "AND aga_giornate.id_aga_giornate = aga_tariffari.id_aga_giornate "
				+ "AND aga_tariffari.id_ricerca_transfert IS NULL ";
		getSession().createSQLQuery( queryString_1 ).setParameter("idGiornata", idGiornata).executeUpdate();
	}

	

}
