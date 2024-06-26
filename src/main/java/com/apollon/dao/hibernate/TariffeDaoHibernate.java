package com.apollon.dao.hibernate;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.apollon.dao.TariffeDao;
import com.apollon.model.Tariffe;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@EnableTransactionManagement
@Repository("TariffeDao")
public class TariffeDaoHibernate extends GenericDaoHibernate<Tariffe, Long> implements TariffeDao {

	public TariffeDaoHibernate() {
		super(Tariffe.class);
	}
	
	
	
	@Override
    @Transactional(readOnly = true)
	public Tariffe get(Long id){
		Tariffe tariffe = (Tariffe) getSession().get(Tariffe.class, id);
		return tariffe;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Tariffe> getTariffe() {
        return getSession().createCriteria(Tariffe.class).list();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Tariffe> getTariffe_by_OrderProvincia() {
        return getSession().createCriteria(Tariffe.class).createAlias("autistaZone.province.regioni", "regioni")
        		.addOrder(Order.asc("regioni.id")).list();
	}
	


	@Override
	@Transactional(readOnly = true)
	public Tariffe getTariffeBy_Autoveicolo_e_Zona(Long idAutoveicolo, Long idZona){
		
		
		//Criterion criterion1 = Restrictions.and(criterion_ANNUNCIO_ATTIVO, Restrictions.isNotNull("immagine1"), Restrictions.isNotNull("nomeImmagine"));
		//.add(Restrictions.and( Restrictions.gt("lat", 0.0 ), Restrictions.gt("lng", 0.0 ))).add(criterion_ANNUNCIO_ATTIVO)
		
		
        return (Tariffe) getSession().createCriteria(Tariffe.class)
        		.add(Restrictions.and( Restrictions.eq("autoveicolo.id", idAutoveicolo), Restrictions.eq("autistaZone.id", idZona) )).uniqueResult();
	}
	
	
	@Override
	@Transactional(readOnly = true)
	public Tariffe getTariffeBy_Autoveicolo_e_Aeroporto(Long idAutoveicolo, Long idAero){
		
		
		//Criterion criterion1 = Restrictions.and(criterion_ANNUNCIO_ATTIVO, Restrictions.isNotNull("immagine1"), Restrictions.isNotNull("nomeImmagine"));
		//.add(Restrictions.and( Restrictions.gt("lat", 0.0 ), Restrictions.gt("lng", 0.0 ))).add(criterion_ANNUNCIO_ATTIVO)
		
		
        return (Tariffe) getSession().createCriteria(Tariffe.class)
        		.add(Restrictions.and( Restrictions.eq("autoveicolo.id", idAutoveicolo), Restrictions.eq("autistaAeroporti.id", idAero) )).uniqueResult();
	}


	@Override
	@Transactional(readOnly = true)
	public Tariffe getTariffeBy_Autoveicolo_e_Porto(Long idAutoveicolo, Long idPorto){
		
        return (Tariffe) getSession().createCriteria(Tariffe.class)
        		.add(Restrictions.and( Restrictions.eq("autoveicolo.id", idAutoveicolo), Restrictions.eq("autistaPortiNavali.id", idPorto) )).uniqueResult();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Tariffe> getTariffeByIdAutista(Long idAutista){
        return getSession().createCriteria(Tariffe.class).add(Restrictions.eq("autista.id", idAutista)).list();
	}

	/**
	 * questo metodo mi dice se almeno una tariffa di (tariffaST,tariffaLP,tariffaAERO,tariffaPORTO) è stata impostata ed è superiore a 0.00 euro
	 */
	@Override
	@Transactional(readOnly = true)
	public boolean TariffeImpostate(Long id){
		Criterion criterion = Restrictions. and( 
				Restrictions.eq("autista.id", id),
				Restrictions.eq("auto.autoveicoloCancellato", false));
	
		BigDecimal resultMax = (BigDecimal) getSession().createCriteria(Tariffe.class).setProjection( Projections.max("tariffeValori.tariffaST"))
				.createAlias("autoveicolo", "auto")
				.add( criterion ).uniqueResult();
		if(resultMax != null && resultMax.compareTo(BigDecimal.ZERO) > 0 ){
			return true;
		}else{
			resultMax = (BigDecimal) getSession().createCriteria(Tariffe.class).setProjection( Projections.max("tariffeValori.tariffaLP"))
					.createAlias("autoveicolo", "auto")
					.add( criterion ).uniqueResult();
			if(resultMax != null && resultMax.compareTo(BigDecimal.ZERO) > 0 ){
				return true;
			}else{
				resultMax = (BigDecimal) getSession().createCriteria(Tariffe.class).setProjection( Projections.max("tariffeValori.tariffaAERO"))
						.createAlias("autoveicolo", "auto")
						.add( criterion ).uniqueResult();
				if(resultMax != null && resultMax.compareTo(BigDecimal.ZERO) > 0 ){
					return true;
				}else{
					resultMax = (BigDecimal) getSession().createCriteria(Tariffe.class).setProjection( Projections.max("tariffeValori.tariffaPORTO"))
							.createAlias("autoveicolo", "auto")
							.add( criterion ).uniqueResult();
					if(resultMax != null && resultMax.compareTo(BigDecimal.ZERO) > 0 ){
						return true;
					}else{
						return false;
					}
				}
			}
		}
		
	}
	
	
	
	
	
	@Transactional
	@Override
	public Tariffe saveTariffe(Tariffe tariffe) throws DataIntegrityViolationException, HibernateJdbcException {
		getSession().saveOrUpdate(tariffe);
		//getSession().flush();
		return tariffe;
	}
	
	

	@Override
	@Transactional(readOnly = true)
	public void removeTariffeByIdAutista(Long idAutista){                         
		Query q = getSession().createQuery("DELETE Tariffe WHERE autista.id = :X");
		q.setParameter("X", idAutista);
		q.executeUpdate();
	}
	

}
