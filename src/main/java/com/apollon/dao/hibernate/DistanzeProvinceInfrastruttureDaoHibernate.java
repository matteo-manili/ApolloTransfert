package com.apollon.dao.hibernate;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import com.apollon.dao.DistanzeProvinceInfrastruttureDao;
import com.apollon.model.DistanzeProvinceInfrastrutture;
import com.apollon.util.DateUtil;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@EnableTransactionManagement
@Repository("DistanzeProvinceInfrastruttureDao")
public class DistanzeProvinceInfrastruttureDaoHibernate extends GenericDaoHibernate<DistanzeProvinceInfrastrutture, Long> implements DistanzeProvinceInfrastruttureDao {

	public DistanzeProvinceInfrastruttureDaoHibernate() {
		super(DistanzeProvinceInfrastrutture.class);
	}
	
	
	@Override
    @Transactional(readOnly = true)
	public DistanzeProvinceInfrastrutture get(Long id){
		DistanzeProvinceInfrastrutture distanzeProvinceInfrastrutture = (DistanzeProvinceInfrastrutture) getSession().get(DistanzeProvinceInfrastrutture.class, id);
		return distanzeProvinceInfrastrutture;
	}
	
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public List<DistanzeProvinceInfrastrutture> getDistanzeProvinceInfrastrutture() {
        return getSession().createCriteria(DistanzeProvinceInfrastrutture.class).list();
	}
	
	
	private static Criterion Criterion_TogliSettimane(Integer MaxNumeroSettimane_OldDataRequestDistance){
		if(MaxNumeroSettimane_OldDataRequestDistance != null){
			return Restrictions.ge("dataRequestDistance", DateUtil.TogliSettimaneDaAdesso(MaxNumeroSettimane_OldDataRequestDistance));
		}else{
			return Restrictions.and();
		}
	}
	
	
	/**
	@UniqueConstraint(columnNames={"id_provinciaAndata", "id_provinciaArrivo"}),
	@UniqueConstraint(columnNames={"id_provinciaAndata", "id_aeroportoArrivo"}),
	@UniqueConstraint(columnNames={"id_provinciaAndata", "id_portoArrivo"}),
	@UniqueConstraint(columnNames={"id_provinciaAndata", "id_museoArrivo"}),
	 */
	@Override
	@Transactional(readOnly = true)
	public DistanzeProvinceInfrastrutture getProvinciaAndata_InfraArrivo(
			long idProvinciaAndata, Long idProvinciaArrivo, 
			Long idAeroportoArrivo, Long idPortoArrivo, Long idMuseoArrivo, Integer MaxNumeroSettimane_OldDataRequestDistance){
		
		Criterion criterion1 = null;
		
		if(idProvinciaArrivo != null) {
			criterion1 = Restrictions.and( Restrictions.eq("provinciaAndata.id", idProvinciaAndata), Restrictions.eq("provinciaArrivo.id", idProvinciaArrivo));
			
		}else if(idAeroportoArrivo != null) {
			criterion1 = Restrictions.and( Restrictions.eq("provinciaAndata.id", idProvinciaAndata), Restrictions.eq("aeroportoArrivo.id", idAeroportoArrivo));
			
		}else if(idPortoArrivo != null) {
			criterion1 = Restrictions.and( Restrictions.eq("provinciaAndata.id", idProvinciaAndata), Restrictions.eq("portoArrivo.id", idPortoArrivo));
			
		}else if(idMuseoArrivo != null) {
			criterion1 = Restrictions.and( Restrictions.eq("provinciaAndata.id", idProvinciaAndata), Restrictions.eq("museoArrivo.id", idMuseoArrivo));
		}
		Criteria criteria = getSession().createCriteria(DistanzeProvinceInfrastrutture.class).add(criterion1).add(Criterion_TogliSettimane(MaxNumeroSettimane_OldDataRequestDistance));
		return (DistanzeProvinceInfrastrutture) criteria.uniqueResult();
	}
	
	
	/**
	@UniqueConstraint(columnNames={"id_aeroportoAndata", "id_provinciaArrivo"}),
	@UniqueConstraint(columnNames={"id_aeroportoAndata", "id_aeroportoArrivo"}),
	@UniqueConstraint(columnNames={"id_aeroportoAndata", "id_portoArrivo"}),
	@UniqueConstraint(columnNames={"id_aeroportoAndata", "id_museoArrivo"}),
	*/
	@Override
	@Transactional(readOnly = true)
	public DistanzeProvinceInfrastrutture getAeroportoAndata_InfraArrivo(long idAeroportoAndata, 
			Long idProvinciaArrivo, Long idAeroportoArrivo, Long idPortoArrivo, Long idMuseoArrivo, Integer MaxNumeroSettimane_OldDataRequestDistance){
		Criterion criterion1 = null;
		if(idProvinciaArrivo != null){
			criterion1 = Restrictions.and( Restrictions.eq("aeroportoAndata.id", idAeroportoAndata), Restrictions.eq("provinciaArrivo.id", idProvinciaArrivo));
			
		}else if(idAeroportoArrivo != null){
			criterion1 = Restrictions.and( Restrictions.eq("aeroportoAndata.id", idAeroportoAndata), Restrictions.eq("aeroportoArrivo.id", idAeroportoArrivo));
			
		}else if(idPortoArrivo != null){
			criterion1 = Restrictions.and( Restrictions.eq("aeroportoAndata.id", idAeroportoAndata), Restrictions.eq("portoArrivo.id", idPortoArrivo));
			
		}else if(idMuseoArrivo != null){
			criterion1 = Restrictions.and( Restrictions.eq("aeroportoAndata.id", idAeroportoAndata), Restrictions.eq("museoArrivo.id", idMuseoArrivo));
		}
		Criteria criteria = getSession().createCriteria(DistanzeProvinceInfrastrutture.class)
				.add(criterion1).add(Criterion_TogliSettimane(MaxNumeroSettimane_OldDataRequestDistance));
		return (DistanzeProvinceInfrastrutture) criteria.uniqueResult();
	}
	

	/**
	@UniqueConstraint(columnNames={"id_museoAndata", "id_provinciaArrivo"}),
	@UniqueConstraint(columnNames={"id_museoAndata", "id_aeroportoArrivo"}),
	@UniqueConstraint(columnNames={"id_museoAndata", "id_portoArrivo"}),
	@UniqueConstraint(columnNames={"id_museoAndata", "id_museoArrivo"})
	*/
	@Override
	@Transactional(readOnly = true)
	public DistanzeProvinceInfrastrutture getPortoAndata_InfraArrivo(long idPortoAndata, 
			Long idProvinciaArrivo, Long idAeroportoArrivo, Long idPortoArrivo, Long idMuseoArrivo, Integer MaxNumeroSettimane_OldDataRequestDistance){
		Criterion criterion1 = null;
		if(idProvinciaArrivo != null){
			criterion1 = Restrictions.and( Restrictions.eq("portoAndata.id", idPortoAndata), Restrictions.eq("provinciaArrivo.id", idProvinciaArrivo));
			
		}else if(idAeroportoArrivo != null){
			criterion1 = Restrictions.and( Restrictions.eq("portoAndata.id", idPortoAndata), Restrictions.eq("aeroportoArrivo.id", idAeroportoArrivo));
			
		}else if(idPortoArrivo != null){
			criterion1 = Restrictions.and( Restrictions.eq("portoAndata.id", idPortoAndata), Restrictions.eq("portoArrivo.id", idPortoArrivo));
			
		}else if(idMuseoArrivo != null){
			criterion1 = Restrictions.and( Restrictions.eq("portoAndata.id", idPortoAndata), Restrictions.eq("museoArrivo.id", idMuseoArrivo));
		}
		Criteria criteria = getSession().createCriteria(DistanzeProvinceInfrastrutture.class)
				.add(criterion1).add(Criterion_TogliSettimane(MaxNumeroSettimane_OldDataRequestDistance));
		return (DistanzeProvinceInfrastrutture) criteria.uniqueResult();
	}
	
	
	/**
	@UniqueConstraint(columnNames={"id_museoAndata", "id_provinciaArrivo"}),
	@UniqueConstraint(columnNames={"id_museoAndata", "id_aeroportoArrivo"}),
	@UniqueConstraint(columnNames={"id_museoAndata", "id_portoArrivo"}),
	@UniqueConstraint(columnNames={"id_museoAndata", "id_museoArrivo"})
	*/
	@Override
	@Transactional(readOnly = true)
	public DistanzeProvinceInfrastrutture getMuseoAndata_InfraArrivo(long idMuseoAndata, 
			Long idProvinciaArrivo, Long idAeroportoArrivo, Long idPortoArrivo, Long idMuseoArrivo, Integer MaxNumeroSettimane_OldDataRequestDistance){
		Criterion criterion1 = null;
		if(idProvinciaArrivo != null){
			criterion1 = Restrictions.and( Restrictions.eq("museoAndata.id", idMuseoAndata), Restrictions.eq("provinciaArrivo.id", idProvinciaArrivo));
			
		}else if(idAeroportoArrivo != null){
			criterion1 = Restrictions.and( Restrictions.eq("museoAndata.id", idMuseoAndata), Restrictions.eq("aeroportoArrivo.id", idAeroportoArrivo));
			
		}else if(idPortoArrivo != null){
			criterion1 = Restrictions.and( Restrictions.eq("museoAndata.id", idMuseoAndata), Restrictions.eq("portoArrivo.id", idPortoArrivo));
			
		}else if(idMuseoArrivo != null){
			criterion1 = Restrictions.and( Restrictions.eq("museoAndata.id", idMuseoAndata), Restrictions.eq("museoArrivo.id", idMuseoArrivo));
		}
		Criteria criteria = getSession().createCriteria(DistanzeProvinceInfrastrutture.class)
				.add(criterion1).add(Criterion_TogliSettimane(MaxNumeroSettimane_OldDataRequestDistance));
		return (DistanzeProvinceInfrastrutture) criteria.uniqueResult();
	}
	
	
	
	@Transactional
	@Override
	public DistanzeProvinceInfrastrutture saveDistanzeProvinceInfrastrutture(DistanzeProvinceInfrastrutture distanzeProvinceInfrastrutture) {
		getSession().saveOrUpdate(distanzeProvinceInfrastrutture);
		getSession().flush();
		return distanzeProvinceInfrastrutture;
	}
	
	
	
	@Transactional
	@Override
	public int removeAeroporti_AndataArrivo(Long idAeroporto) {                         
		Query q = getSession().createSQLQuery("DELETE FROM data_distanze WHERE id_aeroportoAndata = :X OR id_aeroportoArrivo = :X ");
		q.setParameter("X", idAeroporto); return q.executeUpdate();
	}
	
	@Transactional
	@Override
	public int removePortiNavali_AndataArrivo(Long idPortoNavale) {                         
		Query q = getSession().createSQLQuery("DELETE FROM data_distanze WHERE id_portoAndata = :X OR id_portoArrivo = :X ");
		q.setParameter("X", idPortoNavale); return q.executeUpdate();
	}
	
	@Transactional
	@Override
	public int removeMusei_AndataArrivo(Long idMuseo) {                         
		Query q = getSession().createSQLQuery("DELETE FROM data_distanze WHERE id_museoAndata = :X OR id_museoArrivo = :X ");
		q.setParameter("X", idMuseo); return q.executeUpdate();
	}
}
