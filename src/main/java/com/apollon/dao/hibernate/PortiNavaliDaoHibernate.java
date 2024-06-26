package com.apollon.dao.hibernate;

import java.util.List;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import com.apollon.dao.PortiNavaliDao;
import com.apollon.model.PortiNavali;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@EnableTransactionManagement
@Repository("PortiNavaliDao")
public class PortiNavaliDaoHibernate extends GenericDaoHibernate<PortiNavali, Long> implements PortiNavaliDao {

	public PortiNavaliDaoHibernate() {
		super(PortiNavali.class);
	}
	
	
	
	@Override
    @Transactional(readOnly = true)
	public PortiNavali get(Long id){
		PortiNavali portiNavali = (PortiNavali) getSession().get(PortiNavali.class, id);
		return portiNavali;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<PortiNavali> getPortiNavali() {
        return getSession().createCriteria(PortiNavali.class).list();
	}
	
	
	@Override
    @Transactional(readOnly = true)
	public PortiNavali getPortiNavaliBy_PlaceId(String PlaceId){
		Criterion crit1 = Restrictions. eq("placeId", PlaceId) ;
		return (PortiNavali) getSession().createCriteria(PortiNavali.class).add(crit1).uniqueResult();
	}
	

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<PortiNavali> getPortiNavaliBy_LIKE(String term){
		Criterion crit1 = Restrictions. like("nomePorto", "%"+term+"%", MatchMode.END) ;
		return getSession().createCriteria(PortiNavali.class).add(crit1) .list();
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<PortiNavali> getPortiNavaliByIdComune(Long idComune){
        return getSession().createCriteria(PortiNavali.class).add(Restrictions.eq("comuni.id", idComune)).list();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<PortiNavali> getPortiNavaliBy_ListProvince(List<Long> listProvince, Long portiNavaleEsclusoId){
		Criterion crit = Restrictions.and(
				Restrictions.in("PROVINCIA.id", listProvince),
				Restrictions.neOrIsNotNull("id", portiNavaleEsclusoId));
		return getSession().createCriteria(PortiNavali.class)
        	.createAlias("comuni", "COMUNE").createAlias("COMUNE.province", "PROVINCIA")
        	.add(crit).addOrder(Order.desc("numeroPartenzeAnno")).list();
	}

	

	@Transactional
	@Override
	public PortiNavali savePortiNavali(PortiNavali portiNavali) throws DataIntegrityViolationException, HibernateJdbcException {
		getSession().saveOrUpdate(portiNavali);
		//getSession().flush();
		return portiNavali;
	}
	
	

}
