package com.apollon.dao.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import com.apollon.dao.AeroportiDao;
import com.apollon.model.Aeroporti;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@EnableTransactionManagement
@Repository("AeroportiDao")
public class AeroportiDaoHibernate extends GenericDaoHibernate<Aeroporti, Long> implements AeroportiDao {

	public AeroportiDaoHibernate() {
		super(Aeroporti.class);
	}
	
	
	
	@Override
    @Transactional(readOnly = true)
	public Aeroporti get(Long id){
		Aeroporti aeroporti = (Aeroporti) getSession().get(Aeroporti.class, id);
		return aeroporti;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Aeroporti> getAeroporti() {
        return getSession().createCriteria(Aeroporti.class).list();
	}
	
	
	@Override
    @Transactional(readOnly = true)
	public Aeroporti getAeroportoBy_PlaceId(String PlaceId){
		Criterion crit1 = Restrictions. eq("placeId", PlaceId) ;
		return (Aeroporti) getSession().createCriteria(Aeroporti.class).add(crit1).uniqueResult();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Aeroporti> getAeroportiBy_LIKE(String term){
		Criterion crit1 = Restrictions. like("nomeAeroporto", "%"+term+"%", MatchMode.END) ;
		return getSession().createCriteria(Aeroporti.class).add(crit1) .list();
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Aeroporti> getAeroportiByIdComune(Long idComune){
        return getSession().createCriteria(Aeroporti.class).add(Restrictions.eq("comuni.id", idComune)).list();
	}

	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Aeroporti> getAeroportiBy_ListProvince(List<Long> listProvince, Long aeroportoEsclusoId){
		Criterion crit = Restrictions.and(Restrictions.in("PROVINCIA.id", listProvince), Restrictions.neOrIsNotNull("id", aeroportoEsclusoId));
		return getSession().createCriteria(Aeroporti.class)
        	.createAlias("comuni", "COMUNE").createAlias("COMUNE.province", "PROVINCIA").add(crit).addOrder(Order.desc("numeroVoliAnno")).list();
	}
	
	
	@Override
	@Transactional(readOnly = true)
	public Aeroporti getAeroporto_LIKE_Url(String term){
		Criterion crit1 = Restrictions. like("url", term, MatchMode.EXACT) ;
		return (Aeroporti) getSession().createCriteria(Aeroporti.class).add(crit1).uniqueResult();
	}
	
	
	// non lo uso più....
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Aeroporti> getAeroporti_Pagination(int jtStartIndex, int jtPageSize) {
		Criteria crit = getSession().createCriteria(Aeroporti.class)
				.setMaxResults(jtPageSize)
        		.addOrder( Order.asc( "nomeAeroporto" ));
		return crit.list();
        
	}
	
	
	/*
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Aeroporti> getAeroporti_Pagination_SQL(int jtStartIndex, int jtPageSize) {
		
		String queryString = "SELECT * FROM Aeroporti  "
    			+ "WHERE idAnnuncio IN (:listIDAnnunci) AND immagine1 IS NOT NULL AND nomeImmagine IS NOT NULL ";
    	Query query = getSession().createQuery(queryString);
    	query.setParameterList("listIDAnnunci", "weee");
    	
    	return  query.list();
	}
*/

	@Transactional
	@Override
	public Aeroporti saveAeroporti(Aeroporti aeroporti) throws DataIntegrityViolationException, HibernateJdbcException {
		getSession().saveOrUpdate(aeroporti);
		//getSession().flush();
		return aeroporti;
	}
	
	

}
