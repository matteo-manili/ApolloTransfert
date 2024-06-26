package com.apollon.dao.hibernate;

import java.util.List;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import com.apollon.dao.ModelloAutoNumeroPostiDao;
import com.apollon.model.ModelloAutoNumeroPosti;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@EnableTransactionManagement
@Repository("ModelloAutoNumeroPostiDao")
public class ModelloAutoNumeroPostiDaoHibernate extends GenericDaoHibernate<ModelloAutoNumeroPosti, Long> implements ModelloAutoNumeroPostiDao {

	public ModelloAutoNumeroPostiDaoHibernate() {
		super(ModelloAutoNumeroPosti.class);
	}
	
	
	
	@Override
    @Transactional(readOnly = true)
	public ModelloAutoNumeroPosti get(Long id){
		ModelloAutoNumeroPosti modelloAutoNumeroPosti = (ModelloAutoNumeroPosti) getSession().get(ModelloAutoNumeroPosti.class, id);
		return modelloAutoNumeroPosti;
	}
	
	
	@Transactional(readOnly = true)
    @Override
	public ModelloAutoNumeroPosti getModelloAutoNumeroPosti_By_ModelloAutoScout_NumeroPosti(Long IdModelloAutoScout, Long IdNumeroPosti){
		
		Criterion crit = Restrictions.and(
				Restrictions.eq("modelloAutoScout.id", IdModelloAutoScout), Restrictions.eq("numeroPostiAuto.id", IdNumeroPosti));
		
        return (ModelloAutoNumeroPosti) getSession().createCriteria(ModelloAutoNumeroPosti.class).add(crit).uniqueResult() ; 
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<ModelloAutoNumeroPosti> getModelloAutoNumeroPosti() {
        return getSession().createCriteria(ModelloAutoNumeroPosti.class).list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<ModelloAutoNumeroPosti> getModelloAutoNumeroPosti_By_IdModelloAutoScout(long IdModelloAutoScout) {
		
        return getSession().createCriteria(ModelloAutoNumeroPosti.class)
        		.add( Restrictions.eq("modelloAutoScout.id", IdModelloAutoScout) ).list();
	}
	

	@Transactional
	@Override
	public ModelloAutoNumeroPosti saveModelloAutoNumeroPosti(ModelloAutoNumeroPosti modelloAutoNumeroPosti) throws DataIntegrityViolationException, HibernateJdbcException {
		getSession().saveOrUpdate(modelloAutoNumeroPosti);
		//getSession().flush();
		return modelloAutoNumeroPosti;
	}
	
	
	/*
	@Transactional
	@Override
	public void removeModelloAutoNumeroPosti(Long idModelloAutoNumeroPosti){                         
		Query q = getSession().createQuery("DELETE ModelloAutoNumeroPosti WHERE id = :X");
		q.setParameter("X", idModelloAutoNumeroPosti);
		q.executeUpdate();
	}
	*/

}
