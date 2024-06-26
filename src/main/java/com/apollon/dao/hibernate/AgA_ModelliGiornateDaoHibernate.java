package com.apollon.dao.hibernate;

import java.util.List;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import com.apollon.dao.AgA_ModelliGiornateDao;
import com.apollon.model.AgA_ModelliGiornate;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@EnableTransactionManagement
@Repository("AgA_ModelliGiornateDao")
public class AgA_ModelliGiornateDaoHibernate extends GenericDaoHibernate<AgA_ModelliGiornate, Long> implements AgA_ModelliGiornateDao {

	public AgA_ModelliGiornateDaoHibernate() {
		super(AgA_ModelliGiornate.class);
	}
	
	
	@Override
    @Transactional(readOnly = true)
	public AgA_ModelliGiornate get(Long id){
		AgA_ModelliGiornate agA_ModelliGiornate = (AgA_ModelliGiornate) getSession().get(AgA_ModelliGiornate.class, id);
		return agA_ModelliGiornate;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<AgA_ModelliGiornate> getAgA_ModelliGiornate_by_idAutoveicoloModelGiornata(Long idAutoveicoloModelGiornata) {
        return getSession().createCriteria(AgA_ModelliGiornate.class)
        		.add(Restrictions.eq("agA_AutoveicoloModelliGiornate.id", idAutoveicoloModelGiornata)).list();
	}
	

	@Override
	@Transactional(readOnly = true)
	public AgA_ModelliGiornate getAgA_ModelliGiornate_by_IdAutoveicoloModelGiornata_e_Orario(long idAutoveicoloModelGiornata, int orario) {
        Criterion crit = Restrictions.and(Restrictions.eq("agA_AutoveicoloModelliGiornate.id", idAutoveicoloModelGiornata),
        				Restrictions.eq("orario", orario));
        return (AgA_ModelliGiornate) getSession().createCriteria(AgA_ModelliGiornate.class).add( crit ).uniqueResult();
	}
	

	@Transactional
	@Override
	public AgA_ModelliGiornate saveAgA_ModelliGiornate(AgA_ModelliGiornate agA_ModelliGiornate) throws DataIntegrityViolationException, HibernateJdbcException {
		getSession().saveOrUpdate(agA_ModelliGiornate);
		//getSession().flush();
		return agA_ModelliGiornate;
	}
	
	


	

}
