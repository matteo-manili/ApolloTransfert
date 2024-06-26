package com.apollon.dao.hibernate;

import java.util.Calendar;
import java.util.Date;
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

import com.apollon.dao.RichiestaAutistaMedioDao;
import com.apollon.model.RichiestaMediaAutista;
import com.apollon.util.DateUtil;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@EnableTransactionManagement
@Repository("RichiestaAutistaMedioDao")
public class RichiestaAutistaMedioDaoHibernate extends GenericDaoHibernate<RichiestaMediaAutista, Long> implements RichiestaAutistaMedioDao {

	public RichiestaAutistaMedioDaoHibernate() {
		super(RichiestaMediaAutista.class);
	}
	
	
	
	@Override
    @Transactional(readOnly = true)
	public RichiestaMediaAutista get(Long id){
		RichiestaMediaAutista richiestaMediaAutista = (RichiestaMediaAutista) getSession().get(RichiestaMediaAutista.class, id);
		return richiestaMediaAutista;
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<RichiestaMediaAutista> getRichiestaAutista_by_IdRicerca(Long idRicTransfert){
		
		Criterion crit = Restrictions.and(
				Restrictions.eq("ricTransfert.id", idRicTransfert),
				Restrictions.eq("richiestaMedia.classeAutoveicoloScelta", true)
		);
		
		return getSession().createCriteria(RichiestaMediaAutista.class).add( crit )
				.createAlias("richiestaMedia", "richiestaMedia").createAlias("richiestaMedia.ricercaTransfert", "ricTransfert").list();
	}
	
	
	@Override
    @Transactional(readOnly = true)
	public RichiestaMediaAutista getRichiestaAutista_by_IdRicerca_and_IdAutista(Long idRicTransfert, Long idAutista){
		Criterion crit = Restrictions.and(
				Restrictions.eq("ricTransfert.id", idRicTransfert), 
				Restrictions.eq("autista.id", idAutista),
				Restrictions.eq("richiestaMedia.classeAutoveicoloScelta", true));
		return (RichiestaMediaAutista) getSession().createCriteria(RichiestaMediaAutista.class)
				.createAlias("richiestaMedia", "richiestaMedia").createAlias("richiestaMedia.ricercaTransfert", "ricTransfert")
				.add( crit ).uniqueResult();
	}
	
	

	@SuppressWarnings("unchecked")
	@Override
    @Transactional(readOnly = true)
	public List<RichiestaMediaAutista> getRichiestaAutista_by_RichiestaMediaScelta(Long idRicTransfert){
		Criterion crit = Restrictions.and(
				Restrictions.eq("ricTransfert.id", idRicTransfert), 
				Restrictions.eq("richiestaMedia.classeAutoveicoloScelta", true));
		return getSession().createCriteria(RichiestaMediaAutista.class)
				.createAlias("richiestaMedia", "richiestaMedia").createAlias("richiestaMedia.ricercaTransfert", "ricTransfert")
				.add( crit ).addOrder(Order.asc("ordineAutista")).list();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
    @Transactional(readOnly = true)
	/*
	 * // MI RITORNA UNA LISTA MA IN PRATICA è SEMPRE UN SOLO AUTISTA (A PARTE CASI RARI)
	 */
	public List<RichiestaMediaAutista> getRichiestaAutista_by_RichiestaMediaScelta_Autista(Long idRicTransfert, Long idAutista){
		Criterion crit = Restrictions.and(
				Restrictions.eq("ricTransfert.id", idRicTransfert), 
				Restrictions.eq("richiestaMedia.classeAutoveicoloScelta", true),
				Restrictions.eq("autista.id", idAutista));
		return getSession().createCriteria(RichiestaMediaAutista.class)
				.createAlias("richiestaMedia", "richiestaMedia")
				.createAlias("richiestaMedia.ricercaTransfert", "ricTransfert")
				.add( crit ).addOrder(Order.asc("ordineAutista")).list();
	}
	
	

	@Override
    @Transactional(readOnly = true)
	public RichiestaMediaAutista getInfoCliente_by_AutistaPanel(Long idRicTransfert, String tokenAutista, int NumOreInfoCliente){
	    //Date date = DateUtil.AggiungiOre_a_DataAdesso(NumOreInfoCliente);
		Criterion crit = Restrictions.and( 
				Restrictions.eq("tokenAutista", tokenAutista)
				,Restrictions.eq("ric.id", idRicTransfert)
				//,Restrictions.eq("corsaConfermata", true)
				//,Restrictions.le("ric.dataOraPrelevamentoDate", date)
				);
		return (RichiestaMediaAutista) getSession().createCriteria(RichiestaMediaAutista.class)
				.createAlias("richiestaMedia.ricercaTransfert", "ric").add( crit ).uniqueResult(); // 29/11/2016 16:45
		
		/*
		if(richAutistMedio != null){
			return true;
		}else{
			return false;
		}*/
	
	}
	
	
	@Override
    @Transactional(readOnly = true)
	public RichiestaMediaAutista getInfoAutista_by_ClientePanel(Long idRicTransfert, int NumOreInfoAutista){
	    Date date = DateUtil.AggiungiOre_a_DataAdesso( NumOreInfoAutista );
		Criterion crit = Restrictions.and( Restrictions.eq("RICERCA_TRANSFERT.id", idRicTransfert),
											Restrictions.eq("corsaConfermata", true),
											Restrictions.le("RICERCA_TRANSFERT.dataOraPrelevamentoDate", date),
											Restrictions.eq("RICHIESTA_MEDIA.classeAutoveicoloScelta", true));
		return (RichiestaMediaAutista) getSession().createCriteria(RichiestaMediaAutista.class)
				.createAlias("richiestaMedia", "RICHIESTA_MEDIA").createAlias("RICHIESTA_MEDIA.ricercaTransfert", "RICERCA_TRANSFERT")
				.createAlias("RICERCA_TRANSFERT.user", "USER")
				
				.add( crit ).uniqueResult();
		// 29/11/2016 16:45
	}

	
	
	@Override
	@Transactional(readOnly = true)
	public RichiestaMediaAutista getRichiestaAutista_by_token(String token){
		Criterion crit = Restrictions.eq("tokenAutista", token);
		return (RichiestaMediaAutista) getSession().createCriteria(RichiestaMediaAutista.class).add( crit ).uniqueResult();
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<RichiestaMediaAutista> getRichiestaAutistaMedio_By_IdRicercaTransfert_and_ChiamataPrenotata(Long idRicercaTransfert){
		Criterion crit = Restrictions.and( 	
				Restrictions.eq("ricTransfert.id", idRicercaTransfert), 
				Restrictions.eq("chiamataPrenotata", true));
        return getSession().createCriteria(RichiestaMediaAutista.class).add( crit )
        		.createAlias("richiestaMedia.ricercaTransfert", "ricTransfert")
        		.addOrder(Order.asc("dataChiamataPrenotata")).list();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<RichiestaMediaAutista> getRichiestaAutistaMedio_By_IdRicercaTransfert_and_CorsaConfermata(Long idRicercaTransfert){
		Criterion crit = Restrictions.and( 	
				Restrictions.eq("ricTransfert.id", idRicercaTransfert), 
				Restrictions.eq("corsaConfermata", true));
        return getSession().createCriteria(RichiestaMediaAutista.class).add( crit )
        		.createAlias("richiestaMedia.ricercaTransfert", "ricTransfert").list();
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<RichiestaMediaAutista> getRichiestaAutistaMedio() {
        return getSession().createCriteria(RichiestaMediaAutista.class).list();
	}
	


	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<RichiestaMediaAutista> getRichiestaAutistaMedioByAutista(Long idAutista){
        return getSession().createCriteria(RichiestaMediaAutista.class)
        		.add(Restrictions.eq("autista.id", idAutista)).addOrder(Order.asc("id")).list();
	}


	
	@Transactional
	@Override
	public RichiestaMediaAutista saveRichiestaAutistaMedio(RichiestaMediaAutista richiestaMediaAutista) throws DataIntegrityViolationException, HibernateJdbcException {
		getSession().saveOrUpdate(richiestaMediaAutista);
		//getSession().flush();
		return richiestaMediaAutista;
	}
	
	

	@Override
	@Transactional(readOnly = true)
	public void removeRichiestaAutistaMedioByIdAutista(Long idAutista){                         
		Query q = getSession().createQuery("DELETE RichiestaAutistaMedio WHERE autista.id = :X");
		q.setParameter("X", idAutista);
		q.executeUpdate();
	}
	

}
