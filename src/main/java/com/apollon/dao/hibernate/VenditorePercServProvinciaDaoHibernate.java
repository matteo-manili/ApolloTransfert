package com.apollon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.apollon.dao.VenditorePercServProvinciaDao;
import com.apollon.model.VenditorePercServProvincia;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@EnableTransactionManagement
@Repository("VenditorePercServProvinciaDao")
public class VenditorePercServProvinciaDaoHibernate extends GenericDaoHibernate<VenditorePercServProvincia, Long> implements VenditorePercServProvinciaDao {

	public VenditorePercServProvinciaDaoHibernate() {
		super(VenditorePercServProvincia.class);
	}
	
	
	@Override
    @Transactional(readOnly = true)
	public VenditorePercServProvincia get(Long id){
		VenditorePercServProvincia VenditorePercServProvincia = (VenditorePercServProvincia) getSession().get(VenditorePercServProvincia.class, id);
		return VenditorePercServProvincia;
	}
	
	
	@Override
    @Transactional(readOnly = true)
	public VenditorePercServProvincia getVenditorePercServProvincia_by_Venditore_Prov(long idUser, long idProvincia){
		Criterion criterion1 = Restrictions.and( Restrictions.eq("user.id", idUser), Restrictions.eq("province.id", idProvincia));
		return (VenditorePercServProvincia) getSession().createCriteria(VenditorePercServProvincia.class).add(criterion1).uniqueResult();
	}
	
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public List<VenditorePercServProvincia> getVenditorePercServProvincia() {
        return getSession().createCriteria(VenditorePercServProvincia.class).list();
	}
	
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public List<VenditorePercServProvincia> getVenditorePercServProvincia_by_Venditore(long idUser) {
		Criterion criterion1 = Restrictions.and( Restrictions.eq("user.id", idUser));
        return getSession().createCriteria(VenditorePercServProvincia.class).add(criterion1).list();
	}
	
	
	@Transactional
	@Override
	public VenditorePercServProvincia saveVenditorePercServProvincia(VenditorePercServProvincia VenditorePercServProvincia) {
		getSession().saveOrUpdate(VenditorePercServProvincia);
		getSession().flush();
		return VenditorePercServProvincia;
	}
	
	

}
