package com.apollon.dao.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.apollon.dao.RichiestaAutistaMedioAutoveicoloDao;
import com.apollon.model.RichiestaMediaAutistaAutoveicolo;



/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@EnableTransactionManagement
@Repository("RichiestaAutistaMedioAutoveicoloDao")
public class RichiestaAutistaMedioAutoveicoloDaoHibernate extends GenericDaoHibernate<RichiestaMediaAutistaAutoveicolo, Long> implements RichiestaAutistaMedioAutoveicoloDao {

	public RichiestaAutistaMedioAutoveicoloDaoHibernate() {
		super(RichiestaMediaAutistaAutoveicolo.class);
	}
	
	
	
	@Override
    @Transactional(readOnly = true)
	public RichiestaMediaAutistaAutoveicolo get(Long id){
		RichiestaMediaAutistaAutoveicolo richiestaMediaAutistaAutoveicolo = (RichiestaMediaAutistaAutoveicolo) getSession().get(RichiestaMediaAutistaAutoveicolo.class, id);
		return richiestaMediaAutistaAutoveicolo;
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<RichiestaMediaAutistaAutoveicolo> getRichiestaAutista_by_IdRicerca(Long idRicTransfert){
		Criterion crit = 	Restrictions.eq("ricercaTransfert.id", idRicTransfert);
		return getSession().createCriteria(RichiestaMediaAutistaAutoveicolo.class).add( crit ).list();
	}
	
	
	@Override
    @Transactional(readOnly = true)
	public RichiestaMediaAutistaAutoveicolo getRichiestaAutista_by_IdRicerca_and_IdAuto(Long idRicTransfert, Long idAuto){
		
		Criterion crit = Restrictions.and( 	Restrictions.eq("ricercaTransfert.id", idRicTransfert), 
											Restrictions.eq("autoveicolo.id", idAuto));
					
		
		return (RichiestaMediaAutistaAutoveicolo) getSession().createCriteria(RichiestaMediaAutistaAutoveicolo.class).add( crit ).uniqueResult();
	}
	
	
	
	@Override
	@Transactional(readOnly = true)
	public RichiestaMediaAutistaAutoveicolo getRichiestaAutista_by_token(String token){
		
		Criterion crit = Restrictions.eq("tokenAutista", token);

		return (RichiestaMediaAutistaAutoveicolo) getSession().createCriteria(RichiestaMediaAutistaAutoveicolo.class).add( crit ).uniqueResult();
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<RichiestaMediaAutistaAutoveicolo> getRichiestaAutistaMedioAutoveicolo_By_IdRicercaTransfert_and_CorsaConfermata(Long idRicercaTransfert){
		Criterion crit = Restrictions.and( Restrictions.eq("ricercaTransfert.id", idRicercaTransfert), 
				Restrictions.eq("corsaConfermata", true));
        return getSession().createCriteria(RichiestaMediaAutistaAutoveicolo.class).add( crit ).list();
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<RichiestaMediaAutistaAutoveicolo> getRichiestaAutistaMedioAutoveicolo() {
        return getSession().createCriteria(RichiestaMediaAutistaAutoveicolo.class).list();
	}
	


	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<RichiestaMediaAutistaAutoveicolo> getRichiestaAutistaMedioAutoveicoloByAutista(Long idAutista){
        return getSession().createCriteria(RichiestaMediaAutistaAutoveicolo.class).add(Restrictions.eq("auto.autista.id", idAutista)).createAlias("autoveicolo", "auto")
        		
        		.addOrder(Order.asc("id")).list();
	}


	
	@Transactional
	@Override
	public RichiestaMediaAutistaAutoveicolo saveRichiestaAutistaMedioAutoveicolo(RichiestaMediaAutistaAutoveicolo richiestaMediaAutistaAutoveicolo) throws DataIntegrityViolationException, HibernateJdbcException {
		getSession().saveOrUpdate(richiestaMediaAutistaAutoveicolo);
		//getSession().flush();
		return richiestaMediaAutistaAutoveicolo;
	}
	
	

	@Override
	@Transactional(readOnly = true)
	public void removeRichiestaAutistaMedioAutoveicoloByIdAutista(Long idAutista){                         
		Query q = getSession().createQuery("DELETE RichiestaAutistaMedioAutoveicolo WHERE autista.id = :X");
		q.setParameter("X", idAutista);
		q.executeUpdate();
	}
	

}
