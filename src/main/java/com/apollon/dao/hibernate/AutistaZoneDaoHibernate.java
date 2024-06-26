package com.apollon.dao.hibernate;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import com.apollon.dao.AutistaZoneDao;
import com.apollon.model.AutistaAeroporti;
import com.apollon.model.AutistaPortiNavali;
import com.apollon.model.AutistaZone;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@EnableTransactionManagement
@Repository("AutistaZoneDao")
public class AutistaZoneDaoHibernate extends GenericDaoHibernate<AutistaZone, Long> implements AutistaZoneDao {

	public AutistaZoneDaoHibernate() {
		super(AutistaZone.class);
	}
	
	
	@Override
    @Transactional(readOnly = true)
	public AutistaZone get(Long id){
		AutistaZone AutistaZone = (AutistaZone) getSession().get(AutistaZone.class, id);
		return AutistaZone;
	}
	
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public List<AutistaZone> getAutistaZone() {
        return getSession().createCriteria(AutistaZone.class).list();
	}
	
	
	/*
	 * public List<Autista> getAutistaTable() {
		Criteria criteria = getSession().createCriteria(Autista.class)
			.setProjection(Projections.projectionList()
				.add(Projections.property("id"), "id")
				.add(Projections.property("numCorseEseguite"), "numCorseEseguite")
				.add(Projections.property("percentualeServizio"), "percentualeServizio")
				.add(Projections.property("bannato"), "bannato")
				.add(Projections.property("attivo"), "attivo")
				.add(Projections.property("user"), "user"))
			.setResultTransformer(Transformers.aliasToBean(Autista.class))
			.addOrder(Order.desc("id"));
		return criteria.list();
	}
	 */
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public List<AutistaZone> getRegioneAutisti_table() {
		Criterion crit1 = Restrictions.and(
				Restrictions. eq("autista.attivo", true),
				Restrictions. eq("autista.bannato", false));
		Criteria criteria =  getSession().createCriteria(AutistaZone.class).createAlias("autista", "autista").add( crit1 )
			.setProjection(Projections.projectionList()
					.add(Projections.property("comuni"), "comuni")
					.add(Projections.property("province"), "province")
					.add(Projections.property("regioni"), "regioni")
					.add(Projections.property("autista"), "autista"))
        		.setResultTransformer(Transformers.aliasToBean(AutistaZone.class));
		return criteria.list();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<AutistaZone> getAutistaZoneByAutista(long idAutista) {
        return getSession().createCriteria(AutistaZone.class)
        		.add(Restrictions.eq("autista.id", idAutista)).addOrder(Order.asc("id")).list(); 
	}
	
	
	
	/**
	 * mi restituice se c'è almeno una zona con un servizio attivo
	 */
	@Transactional(readOnly = true)
	@Override
	public boolean ControllaServiziAttivi(long idAutista) {

		Long listZone =  (Long) getSession().createCriteria(AutistaZone.class)
			.setProjection(Projections.rowCount())
			.add( Restrictions.and( Restrictions.eq("servizioAttivo", true), Restrictions.eq("autista.id", idAutista)) ).uniqueResult();
		if(listZone != null && listZone > 0){
			return true;
		}else{
			
			listZone =  (Long) getSession().createCriteria(AutistaZone.class).createAlias("zoneLungaPercorrenza", "zoneLP")
				.setProjection(Projections.projectionList()
						.add(Projections.property("id"), "id"))
				.add( Restrictions.and( Restrictions.eq("zoneLP.servizioAttivo", true), Restrictions.eq("autista.id", idAutista)) ).uniqueResult();
			if(listZone != null && listZone > 0){
				return true;
			}else{
				
				listZone =  (Long) getSession().createCriteria(AutistaAeroporti.class)
					.setProjection(Projections.projectionList()
							.add(Projections.property("id"), "id"))
					.add( Restrictions.and( Restrictions.eq("servizioAttivo", true), Restrictions.eq("autista.id", idAutista)) ).uniqueResult();
				if(listZone != null && listZone > 0){
					return true;
				}else{
					
					listZone =  (Long) getSession().createCriteria(AutistaPortiNavali.class)
					.setProjection(Projections.projectionList()
							.add(Projections.property("id"), "id"))
					.add( Restrictions.and( Restrictions.eq("servizioAttivo", true), Restrictions.eq("autista.id", idAutista)) ).uniqueResult();
						
					if(listZone != null && listZone > 0){
						return true;
					}else{
						
						return false;
					}
				}
			}
		}
	}
	
	
	
	
	
	@Override
	@Transactional(readOnly = true)
	public long getNumeroRegioniAutista(long idAutista) {
		return (long) getSession().createCriteria(AutistaZone.class)
				.add( Restrictions.isNotNull("regioni.id") )
				.add(Restrictions.eq("autista.id", idAutista))
				.setProjection(Projections.rowCount()).uniqueResult();
	}
	
	
	@Override
	@Transactional(readOnly = true)
	public AutistaZone getAutistaZoneBy_Autista_e_Regione(long idAutista, Long idRegione) {
		
        return (AutistaZone) getSession().createCriteria(AutistaZone.class)
        		.add(Restrictions.eq("autista.id", idAutista)).add(Restrictions.eq("regioni.id", idRegione)).uniqueResult();
	}
	
	
	@Override
	@Transactional(readOnly = true)
	public AutistaZone getAutistaZoneBy_Autista_e_Provincia(long idAutista, Long idProvincia) {
		
        return (AutistaZone) getSession().createCriteria(AutistaZone.class)
        		.add(Restrictions.eq("autista.id", idAutista)).add(Restrictions.eq("province.id", idProvincia)).uniqueResult();
	}
	
	
	@Override
	@Transactional(readOnly = true)
	public AutistaZone getAutistaZoneBy_Autista_e_Comune(long idAutista, Long idComune) {
		
        return (AutistaZone) getSession().createCriteria(AutistaZone.class)
        		.add(Restrictions.eq("autista.id", idAutista)).add(Restrictions.eq("comuni.id", idComune)).uniqueResult();
	}
	
	
	@Override
	@Transactional
	public AutistaZone saveAutistaZone(AutistaZone AutistaZone) throws DataIntegrityViolationException, HibernateJdbcException {
		getSession().saveOrUpdate(AutistaZone);
		//getSession().flush();
		return AutistaZone;
	}
	
	

}
