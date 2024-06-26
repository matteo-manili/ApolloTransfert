package com.apollon.dao.hibernate;

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
import com.apollon.dao.FattureDao;
import com.apollon.model.Fatture;
import com.apollon.model.RicercaTransfert;
import com.apollon.model.RichiestaMediaAutista;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@EnableTransactionManagement
@Repository("FattureDao")
public class FattureDaoHibernate extends GenericDaoHibernate<Fatture, Long> implements FattureDao {

	public FattureDaoHibernate() {
		super(Fatture.class);
	}
	
	
	@Override
    @Transactional(readOnly = true)
	public Fatture get(Long id){
		Fatture fatture = (Fatture) getSession().get(Fatture.class, id);
		return fatture;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Fatture> getFatture() {
        return getSession().createCriteria(Fatture.class).addOrder(Order.desc("progressivoFattura")).list();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Fatture> getFatture_By_ProgressivoFattua_IdCorsa(long term) {
		Criterion crit1 = Restrictions.or(
				Restrictions.eq("progressivoFattura", term),
				Restrictions.eq("ricercaTransfert.id", term),
				Restrictions.eq("ricercaTransfertRimborso.id", term));
        return getSession().createCriteria(Fatture.class).add(crit1).addOrder(Order.desc("progressivoFattura")).list();
	}
	
	
	@Override
	@Transactional(readOnly = true)
	public RichiestaMediaAutista getObjectFatturaBy_IdricercaTransfertCorsaMediaConfermata(long idCorsa){
		RicercaTransfert ricTransfert = (RicercaTransfert) getSession().get(RicercaTransfert.class, idCorsa);
		if(ricTransfert.isRitorno() == false){
			Criterion crit1 = Restrictions.and(
					Restrictions.eq("RIC_TRANSFERT.id", idCorsa),
					//Restrictions.eq("RIC_TRANSFERT.approvazioneAndata", Constants.APPROVATA),
					Restrictions.eq("RIC_TRANSFERT.pagamentoEseguitoMedio", true),
					Restrictions.eq("RICHIESTA_MEDIA.classeAutoveicoloScelta", true));
			return (RichiestaMediaAutista) getSession().createCriteria(RichiestaMediaAutista.class)
					.createAlias("richiestaMedia", "RICHIESTA_MEDIA").createAlias("RICHIESTA_MEDIA.ricercaTransfert", "RIC_TRANSFERT")
					.add( crit1 ).addOrder(Order.desc("corsaConfermata")).list().get(0); //non mettere unique da errore. la lista serve per prendere l'eventuale autista che conferma la corsa
		}else{
			Criterion crit1 = Restrictions.and(
					Restrictions.eq("RIC_TRANSFERT.id", idCorsa),
					//Restrictions.eq("RIC_TRANSFERT.approvazioneAndata", Constants.APPROVATA),
					//Restrictions.eq("RIC_TRANSFERT.approvazioneRitorno", Constants.APPROVATA),
					Restrictions.eq("RIC_TRANSFERT.pagamentoEseguitoMedio", true),
					Restrictions.eq("RICHIESTA_MEDIA.classeAutoveicoloScelta", true));
			return (RichiestaMediaAutista) getSession().createCriteria(RichiestaMediaAutista.class)
					.createAlias("richiestaMedia", "RICHIESTA_MEDIA").createAlias("RICHIESTA_MEDIA.ricercaTransfert", "RIC_TRANSFERT")
					.add( crit1 ).addOrder(Order.desc("corsaConfermata")).list().get(0); //non mettere unique da errore. la lista serve per prendere l'eventuale autista che conferma la corsa
		}
	}
	


	
	@Override
	@Transactional(readOnly = true)
	public Fatture getFatturaBy_IdRicercaTransfert(long idCorsa){
		return (Fatture) getSession().createCriteria(Fatture.class).add(Restrictions.eq("ricercaTransfert.id", idCorsa)).uniqueResult();
	}
	
	
	
	@Override
	@Transactional(readOnly = true)
	public Fatture getFatturaBy_IdRitardo(long idRitardo){
		return (Fatture) getSession().createCriteria(Fatture.class).add(Restrictions.eq("ritardi.id", idRitardo)).uniqueResult();
	}
	
	@Override
	@Transactional(readOnly = true)
	public Fatture getFatturaBy_IdSupplemento(long idSupplemento){
		return (Fatture) getSession().createCriteria(Fatture.class).add(Restrictions.eq("supplementi.id", idSupplemento)).uniqueResult();
	}
	
	
	@Override
	@Transactional(readOnly = true)
	public Fatture getFatturaBy_IdRicercaTransfert_Rimbroso(long idCorsa){
		return (Fatture) getSession().createCriteria(Fatture.class).add(Restrictions.eq("ricercaTransfertRimborso.id", idCorsa)).uniqueResult();
	}
	
	

	@Override
	@Transactional(readOnly = true)
	public Long dammiNumeroProgressivoFattura(){
		if( getSession().createCriteria(Fatture.class).setProjection( Projections.max("progressivoFattura")).uniqueResult() != null){
			return (Long) getSession().createCriteria(Fatture.class)
					.setProjection( Projections.max("progressivoFattura") ).uniqueResult() + 1;
		}else{
			return 1l;
		}
	}
	
	
	@Transactional
	@Override
	public void removeFatturabyRitardo(Long idRitardo){                         
		Query q = getSession().createQuery("DELETE Fatture WHERE ritardi.id = :X");
		q.setParameter("X", idRitardo);
		q.executeUpdate();
	}
	
	
	@Transactional
	@Override
	public void removeFatturabySupplemento(Long idSupplemento){                         
		Query q = getSession().createQuery("DELETE Fatture WHERE supplementi.id = :X");
		q.setParameter("X", idSupplemento);
		q.executeUpdate();
	}
	
	
	@Transactional
	@Override
	public Fatture saveFatture(Fatture fatture) throws DataIntegrityViolationException, HibernateJdbcException {
		getSession().saveOrUpdate(fatture);
		//getSession().flush();
		return fatture;
	}
	
	


	

}
