package com.apollon.dao.hibernate;

import java.util.List;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import com.apollon.dao.AgA_AutoveicoloModelliGiornateDao;
import com.apollon.model.AgA_AutoveicoloModelliGiornate;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@EnableTransactionManagement
@Repository("AgA_AutoveicoloModelliGiornateDao")
public class AgA_AutoveicoloModelliGiornateDaoHibernate extends GenericDaoHibernate<AgA_AutoveicoloModelliGiornate, Long> implements AgA_AutoveicoloModelliGiornateDao {

	public AgA_AutoveicoloModelliGiornateDaoHibernate() {
		super(AgA_AutoveicoloModelliGiornate.class);
	}
	
	@Transactional
	@Override
	public AgA_AutoveicoloModelliGiornate saveAgA_AutoveicoloModelliGiornate(AgA_AutoveicoloModelliGiornate agA_AutoveicoloModelliGiornate) throws DataIntegrityViolationException, HibernateJdbcException {
		getSession().saveOrUpdate(agA_AutoveicoloModelliGiornate);
		//getSession().flush();
		return agA_AutoveicoloModelliGiornate;
	}
	
	@Override
    @Transactional(readOnly = true)
	public AgA_AutoveicoloModelliGiornate get(Long id){
		AgA_AutoveicoloModelliGiornate agA_AutoveicoloModelliGiornate = (AgA_AutoveicoloModelliGiornate) getSession().get(AgA_AutoveicoloModelliGiornate.class, id);
		return agA_AutoveicoloModelliGiornate;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<AgA_AutoveicoloModelliGiornate> getAgA_AutoveicoloModelliGiornate() {
        return getSession().createCriteria(AgA_AutoveicoloModelliGiornate.class).addOrder(Order.desc("id")).list();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<AgA_AutoveicoloModelliGiornate> getAgA_AutoveicoloModelliGiornate_by_IdAutoveicolo(long idAutoveicolo) {
        return getSession().createCriteria(AgA_AutoveicoloModelliGiornate.class).add(Restrictions.eq("autoveicolo.id", idAutoveicolo)).addOrder(Order.asc("id")).list();
	}
	
	
	/**
	 * per usare il setResultTransformer in un createSQLQuery usare l'alias delle colonne della select corrispondente agli attributi della classe passata a Transformers.aliasToBean
	 * e aggiungeli al addScalar
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<AgA_AutoveicoloModelliGiornate> AutoveicoloModelliGiornate_ExistsTariffari_by_IdAutoveicolo(long idAutoveicolo) {
		String queryString_1 = "SELECT autoModelGiornate.id_aga_autoveicolo_modelli_giornate AS id, autoModelGiornate.nomeGiornata AS nomeGiornata "
				+ "FROM aga_autoveicolo_modelli_giornate autoModelGiornate "
				+ "WHERE "
				+ "autoModelGiornate.id_autoveicolo = :idAutoveicolo "
				+ "AND EXISTS (SELECT * "
					+ "FROM aga_modelli_giornate giornate "
					+ "WHERE autoModelGiornate.id_aga_autoveicolo_modelli_giornate = giornate.id_aga_autoveicolo_modelli_giornate) "
				+ "ORDER BY autoModelGiornate.id_aga_autoveicolo_modelli_giornate ASC ";
		List<AgA_AutoveicoloModelliGiornate> aaa = this.getSession().createSQLQuery( queryString_1 )
				.addScalar("id", StandardBasicTypes.LONG)
				.addScalar("nomeGiornata", StandardBasicTypes.STRING)
				.setResultTransformer(Transformers.aliasToBean(AgA_AutoveicoloModelliGiornate.class))
				.setParameter("idAutoveicolo", idAutoveicolo).list();
		return aaa;
	}

	

	
	


	

}
