package com.apollon.dao.hibernate;

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
import com.apollon.dao.RichiestaAutistaParticolareDao;
import com.apollon.model.RicercaTransfert;
import com.apollon.model.RichiestaAutistaParticolare;
import com.apollon.util.DateUtil;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@EnableTransactionManagement
@Repository("RichiestaAutistaParticolareDao")
public class RichiestaAutistaParticolareDaoHibernate extends GenericDaoHibernate<RichiestaAutistaParticolare, Long> implements RichiestaAutistaParticolareDao {

	public RichiestaAutistaParticolareDaoHibernate() {
		super(RichiestaAutistaParticolare.class);
	}
	
	
	@Override
    @Transactional(readOnly = true)
	public RichiestaAutistaParticolare get(Long id){
		RichiestaAutistaParticolare richiestaAutistaParticolare = (RichiestaAutistaParticolare) getSession().get(RichiestaAutistaParticolare.class, id);
		return richiestaAutistaParticolare;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<RichiestaAutistaParticolare> getRichiestaAutistaParticolare() {
        return getSession().createCriteria(RichiestaAutistaParticolare.class).list();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
    @Transactional(readOnly = true)
	public List<RichiestaAutistaParticolare> getRichiestaAutistaParticolare_by_idRicercaTransfert(long idRiceraTransfert) {
		return getSession().createCriteria(RichiestaAutistaParticolare.class).add(Restrictions.eq("ricercaTransfert.id", idRiceraTransfert) ).list();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Object[]> InvioSmsRicevuti_Autisti(long idRicTransfert){
		String queryString = "SELECT RICH_PART.id_richiesta_autista_particolare, "
		+ "(SELECT COUNT(*) FROM richiesta_autista_particolare RICH_PART_2 WHERE RICH_PART_2.id_autoveicolo = RICH_PART.id_autoveicolo AND RICH_PART_2.invioSms = true) "
		+ "FROM richiesta_autista_particolare RICH_PART WHERE "
		+ "RICH_PART.id_ricerca_transfert = :idRicTransfert ";
		Query q = this.getSession().createSQLQuery( queryString );
		q.setParameter("idRicTransfert", idRicTransfert);
		//System.out.println( "size: "+q.list().size() );
		List<Object[]> zz = q.list();
		return zz;
	}
	
	
	@Override
    @Transactional(readOnly = true)
	public RichiestaAutistaParticolare getRichiestaAutista_by_IdRicerca_and_IdAuto(Long idRicTransfert, Long idAuto){
		Criterion crit = Restrictions.and( Restrictions.eq("ricercaTransfert.id", idRicTransfert), Restrictions.eq("autoveicolo.id", idAuto));
		return (RichiestaAutistaParticolare) getSession().createCriteria(RichiestaAutistaParticolare.class).add( crit ).uniqueResult();
	}
	
	
	@Override
    @Transactional(readOnly = true)
	public RichiestaAutistaParticolare getInfoCliente_by_AutistaPanel(Long idRicTransfert, String token, int NumOreInfoCliente){
		//Date date = DateUtil.AggiungiOre_a_DataAdesso( NumOreInfoCliente );
		Criterion crit = Restrictions.and( Restrictions.eq("token", token),
				Restrictions.eq("ricercaTransfert.id", idRicTransfert)
				//,Restrictions.eq("invioSmsCorsaConfermata", true),
				//,Restrictions.le("ric.dataOraPrelevamentoDate", date)
					);
		return (RichiestaAutistaParticolare) getSession().createCriteria(RichiestaAutistaParticolare.class)
				.createAlias("ricercaTransfert", "ric").add( crit ).uniqueResult();
		/*
		if(richAutistPart != null){
			return true;
		}else{
			return false;
		}
		*/
	}
	
	
	@Override
    @Transactional(readOnly = true)
	public RichiestaAutistaParticolare getInfoAutista_by_ClientePanel(Long idRicTransfert, int NumOreInfoAutista) {
	    Date date = DateUtil.AggiungiOre_a_DataAdesso( NumOreInfoAutista );
		Criterion crit = Restrictions.and( Restrictions.eq("ricercaTransfert.id", idRicTransfert), Restrictions.eq("invioSmsCorsaConfermata", true),
				Restrictions.le("RIC.dataOraPrelevamentoDate", date));
		return (RichiestaAutistaParticolare) getSession().createCriteria(RichiestaAutistaParticolare.class)
				.createAlias("ricercaTransfert", "RIC").add( crit ).uniqueResult();
	}
	
	
	@Override
    @Transactional(readOnly = true)
	public List<RichiestaAutistaParticolare> getInfoAutista_by_ClientePanel_CorsaMultipla(Long idRicTransfert, int NumOreInfoAutista) {
	    Date date = DateUtil.AggiungiOre_a_DataAdesso(NumOreInfoAutista);
		Criterion crit = Restrictions.and( 	Restrictions.eq("id", idRicTransfert), Restrictions.le("dataOraPrelevamentoDate", date));
		RicercaTransfert ricTransfert = (RicercaTransfert) getSession().createCriteria(RicercaTransfert.class).add( crit ).uniqueResult();
		if(ricTransfert != null) {
			return ricTransfert.getRichiestaAutistaParticolareAcquistato_Multiplo();
		}else {
			return null;
		}
	}
	
	
	@Override
	@Transactional(readOnly = true)
	public RichiestaAutistaParticolare getRichiestaAutista_by_token(String token){
		Criterion crit = Restrictions.eq("token", token);
		return (RichiestaAutistaParticolare) getSession().createCriteria(RichiestaAutistaParticolare.class).add( crit ).uniqueResult();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<RichiestaAutistaParticolare> getRichiestaAutistaParticolareByAutista(Long idAutista){
        return getSession().createCriteria(RichiestaAutistaParticolare.class).add(Restrictions.eq("autista.id", idAutista)).addOrder(Order.asc("id")).list();
	}
	
	
	
	@Transactional
	@Override
	public RichiestaAutistaParticolare saveRichiestaAutistaParticolare(RichiestaAutistaParticolare richiestaAutistaParticolare) throws DataIntegrityViolationException, HibernateJdbcException {
		getSession().saveOrUpdate(richiestaAutistaParticolare);
		//getSession().flush();
		return richiestaAutistaParticolare;
	}
	
	

	@Override
	@Transactional(readOnly = true)
	public void removeRichiestaAutistaParticolareByIdAutista(Long idAutista){                         
		Query q = getSession().createQuery("DELETE RichiestaAutistaParticolare WHERE autista.id = :X");
		q.setParameter("X", idAutista);
		q.executeUpdate();
	}
	

}
