package com.apollon.dao.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.apollon.Constants;
import com.apollon.dao.AutistaDao;
import com.apollon.model.Autista;
import com.apollon.model.AutistaAeroporti;
import com.apollon.model.AutistaPortiNavali;
import com.apollon.model.AutistaZone;
import com.apollon.model.Autoveicolo;
import com.apollon.model.BackupInfoUtente;
import com.apollon.model.RicercaTransfert;
import com.apollon.model.RichiestaMediaAutista;
import com.apollon.model.RichiestaAutistaParticolare;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@EnableTransactionManagement
@Repository("AutistaDao")
public class AutistaDaoHibernate extends GenericDaoHibernate<Autista, Long> implements AutistaDao {

	public AutistaDaoHibernate() {
		super(Autista.class);
	}
	
	@Override
    @Transactional(readOnly = true)
	public Autista get(Long id){
		Autista autista = (Autista) getSession().get(Autista.class, id);
		return autista;
	}
	
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public List<Autista> ListAutista_Approvati() {
		Criterion crit1 = Restrictions.and(
				Restrictions.eq("bannato", false)
				,Restrictions.eq("attivo", true)
				,Restrictions.eq("autistaDocumento.approvatoGenerale", true)
			);
		List<Autista> aaa = getSession().createCriteria(Autista.class).add(crit1).list();
		return aaa;
	}
	
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public List<AutistaZone> getAutistiNordItalia() {
		
		//String queryString4 = "SELECT c, c.autista.id, c.autista.percentualeServizio FROM Autoveicolo c WHERE c.autista.user.id = :id  ";
		//String queryString = "FROM Autista c ORDER BY c.id DESC";
		//Query query = getSession().createQuery(queryString).setMaxResults( maxResults ).setFirstResult( firstResult );
		// Questo funziona solo quando nel SELECT c'è un solo tipo di Oggetto. (SIA IN CRITERIA CHE IN HQL)
		//Query query = getSession().createQuery(queryString).setMaxResults( maxResults ).setFirstResult( firstResult ).setResultTransformer(Transformers.aliasToBean(Autista.class)); 
				
		//return query.list();
		
		String queryString4 = "select distinct zone FROM AutistaZone zone, Province prov, Regioni reg   " 
				+ "WHERE zone.province = prov.id "
				+ "AND prov.regioni = reg.id "
				+ "AND reg.macroRegioni = 1";
				//+ "AND( reg.macroRegioni = 3"
				//+ "OR reg.macroRegioni = 2 )";
		
		Query q = this.getSession().createQuery(queryString4);
		//q.setParameter("id", idAutista);
		return q.list();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
    @Transactional(readOnly = true)
	public List<AutistaZone> lazyAutistaZone(Long idAutista){
		//@Query("SELECT p FROM Person p JOIN FETCH p.roles WHERE p.id = (:id)")
		/*
		 * select * from Business b 
			left outer join campaigns c on c.business_id = b.id
			left join promotions  p on p.campaign_id = c.id
			where b.id=:id
		 */
		//String query = "SELECT autista FROM Autista autista JOIN FETCH autista.tarffe i WHERE autista.id = :id";
		//SELECT e FROM AutistaZone e JOIN FETCH e.phones p where p.autista = '613'
		//String query = "SELECT p FROM AutistaZone p JOIN FETCH p.id WHERE p.autista = (:id)";
		
		String query = "from AutistaZone p where p.autista.id = :id";
		Query q = this.getSession().createQuery(query);
		q.setParameter("id", idAutista);
		return  (List<AutistaZone>) q.list();
	}
	
	
	/**
	 * mi ritorna una lista di oggetti ZON.id_comune, COM.nomeComune, ZON.id_provincia, PRO.nomeProvincia
	 * COMUNI E PROVINCE
	 */
	@SuppressWarnings("unchecked")
	@Override
    @Transactional(readOnly = true)
	public List<Object> lazyAutistaZone_light(Long idAutista){
		/*
		 * la guida totale è: https://docs.jboss.org/hibernate/orm/4.1/manual/en-US/html/index.html
		 * 
		 * Usare Native SQL (SQL PURO) utilizzado createSQLQuery
		 * vedere: https://docs.jboss.org/hibernate/orm/4.1/manual/en-US/html/ch18.html
		 */
		String query = "SELECT ZON.id_comune, COM.nomeComune, ZON.id_provincia, PRO.nomeProvincia "
			+ "FROM zona ZON "
			+ "LEFT OUTER JOIN data_comuni COM "
			+ "ON ZON.id_comune = COM.id_comune "
			+ "LEFT OUTER JOIN data_province PRO "
			+ "ON ZON.id_provincia  = PRO.id_provincia "
			+ ""
			+ "WHERE ZON.id_autista = :id";
		Query q = this.getSession().createSQLQuery(query);
		q.setParameter("id", idAutista);
		List<Object> aa = q.list();
		
		return  aa;
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Override
    @Transactional(readOnly = true)
	public List<Autoveicolo> lazyAutoveicolo(Long id){
		String query = "from Autoveicolo p where p.autista.id = :id";
		Query q = this.getSession().createQuery(query);
		q.setParameter("id", id);
		return  (List<Autoveicolo>) q.list();
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Override
    @Transactional(readOnly = true)
	public List<AutistaAeroporti> lazyAutistaAeroporti(Long id){
		String query = "from AutistaAeroporti p where p.autista.id = :id";
		Query q = this.getSession().createQuery(query);
		q.setParameter("id", id);
		return  (List<AutistaAeroporti>) q.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
    @Transactional(readOnly = true)
	public List<AutistaPortiNavali> lazyAutistaPortiNavali(Long id){
		String query = "from AutistaPortiNavali p where p.autista.id = :id";
		Query q = this.getSession().createQuery(query);
		q.setParameter("id", id);
		return  (List<AutistaPortiNavali>) q.list();
	}
	
	
	@Transactional(readOnly = true)
	@Override
	public Autista getAutistaByUser(long idUser) {
		return (Autista) getSession().createCriteria(Autista.class).add(Restrictions.eq("user.id", idUser)).uniqueResult();
	}
	
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public List<Autista> getAutistaByAutistaZone(long idAutista) {
		Criteria criteria =	 getSession().createCriteria(Autista.class).add(Restrictions.eq("id", idAutista));
		return criteria.list();
	}
	
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public List<Autista> getAutistiList() {
        return getSession().createCriteria(Autista.class).addOrder(Order.desc("id")).list();
        //return getSession().createCriteria(Autista.class).addOrder(Order.desc("autistaDocumento.approvatoGenerale")).list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Object[]> getDocumentiAutisti_da_Approvare(int maxResults, Integer firstResult) {
		String DocumentiApprovati = "exists (SELECT AUTISTA_1 FROM Autista AUTISTA_1, AutistaSottoAutisti DIPENDENTI, AutistaLicenzeNcc LICENZE, Autoveicolo AUTO "
		+ "WHERE "
		+ "AUTISTA.id = AUTISTA_1.id "
		+ "AND AUTISTA_1.autistaDocumento.approvatoDocumenti = true "
		+ "AND AUTISTA_1.autistaDocumento.approvatoContratto = false "
		+ "AND ((DIPENDENTI.autista.id = AUTISTA.id AND AUTISTA.azienda = true AND DIPENDENTI.approvato = true) "
			+ " OR AUTISTA.azienda = false) "
		+ "AND LICENZE.autista.id = AUTISTA.id AND LICENZE.approvato = true "
		+ "AND AUTO.autista.id = AUTISTA.id AND AUTO.autoveicoloCartaCircolazione.approvatoCartaCircolazione = true "
			+"AND "
			+ "(AUTO.autoveicoloCartaCircolazione.nomeFileCartaCircolazione IS NOT NULL "
				+ "OR AUTO.autoveicoloCartaCircolazione.nomeFileCartaCircolazione NOT LIKE '') "
		+ "AND ";
		String partitaIva = "AUTISTA_1.autistaDocumento.partitaIva IS NOT NULL AND AUTISTA_1.autistaDocumento.partitaIva NOT LIKE '' ";
		String partitaIvaDenominazione = "AUTISTA_1.autistaDocumento.partitaIvaDenominazione IS NOT NULL AND AUTISTA_1.autistaDocumento.partitaIvaDenominazione NOT LIKE '' ";
		String iban = "AUTISTA_1.autistaDocumento.iban IS NOT NULL AND AUTISTA_1.autistaDocumento.iban NOT LIKE '' ";
		String numeroPatente = "PATENTE.numeroPatente IS NOT NULL AND PATENTE.numeroPatente NOT LIKE '' ";
		String dataScadenzaPatente = "PATENTE.dataScadenzaPatente IS NOT NULL AND PATENTE.dataScadenzaPatente NOT LIKE '' ";
		String nomeFilePatenteFronte = "PATENTE.nomeFilePatenteFronte IS NOT NULL AND PATENTE.nomeFilePatenteFronte NOT LIKE '' ";
		String nomeFilePatenteRetro = "PATENTE.nomeFilePatenteRetro IS NOT NULL AND PATENTE.nomeFilePatenteRetro NOT LIKE '' ";
		String documentoAggiuntivo ="AUTISTA_1.autistaDocumento.documentoAggiuntivo IS NOT NULL AND AUTISTA_1.autistaDocumento.documentoAggiuntivo NOT LIKE '' ";
		
		String numeroRuoloConducenti = "ISCRIZ_RUOLO.numeroRuoloConducenti IS NOT NULL AND ISCRIZ_RUOLO.numeroRuoloConducenti NOT LIKE '' ";
		String dataIscrizioneRuoloConducenti = "ISCRIZ_RUOLO.dataIscrizioneRuoloConducenti IS NOT NULL AND ISCRIZ_RUOLO.dataIscrizioneRuoloConducenti NOT LIKE '' ";
		String numeroCAP = "DOC_CAP.numeroCAP IS NOT NULL AND DOC_CAP.numeroCAP NOT LIKE '' ";
		String dataScadenzaCAP = "DOC_CAP.dataScadenzaCAP IS NOT NULL AND DOC_CAP.dataScadenzaCAP NOT LIKE '' ";
		String nomeFileDocumentoRuoloConducenti = "ISCRIZ_RUOLO.nomeFileDocumentoRuoloConducenti IS NOT NULL AND ISCRIZ_RUOLO.nomeFileDocumentoRuoloConducenti NOT LIKE '' ";
		String nomeFileCAP = "DOC_CAP.nomeFileCAP IS NOT NULL AND DOC_CAP.nomeFileCAP NOT LIKE '' ";
		
		String Query = "SELECT AUTISTA, "
		// TAB_1 e TAB_2 (AUTISTI PRIVATI)
		+ " exists (SELECT ISCRIZ_RUOLO FROM Autista AUTISTA_1, DocumentiPatente PATENTE, DocumentiIscrizioneRuolo ISCRIZ_RUOLO, DocumentiCap DOC_CAP where "
		+ "AUTISTA_1.id = AUTISTA.id "
		+ "AND PATENTE.id = AUTISTA.autistaDocumento.documentiPatente.id "
		+ "AND "+partitaIva
		+ "AND "+partitaIvaDenominazione
		+ "AND "+iban
		+ "AND "+numeroPatente
		+ "AND "+dataScadenzaPatente	
		+ "AND "+nomeFilePatenteFronte	
		+ "AND "+nomeFilePatenteRetro
		+ "AND ISCRIZ_RUOLO.id = AUTISTA.autistaDocumento.documentiIscrizioneRuolo.id "
		+ "AND DOC_CAP.id = AUTISTA.autistaDocumento.documentiCap.id "
		+ "AND "+numeroRuoloConducenti
		+ "AND "+dataIscrizioneRuoloConducenti
		+ "AND "+numeroCAP
		+ "AND "+dataScadenzaCAP
		+ "AND "+nomeFileDocumentoRuoloConducenti
		+ "AND "+nomeFileCAP
		+ "AND AUTISTA.azienda = false "
		+ "AND AUTISTA_1.autistaDocumento.approvatoDocumenti = false "
		+ "AND AUTISTA_1.autistaDocumento.approvatoGenerale = false) as TAB_1_2_PRIVATI, "	
				
		// TAB_1 (AZIENDE o COOPERATIVE)
		+ "exists (SELECT AUTISTA_1 FROM Autista AUTISTA_1, DocumentiPatente PATENTE where "
		+ "AUTISTA_1.id = AUTISTA.id "
		+ "AND PATENTE.id = AUTISTA.autistaDocumento.documentiPatente.id "
		+ "AND "+partitaIva
		+ "AND "+partitaIvaDenominazione
		+ "AND "+iban
		+ "AND "+numeroPatente
		+ "AND "+dataScadenzaPatente	
		+ "AND "+nomeFilePatenteFronte	
		+ "AND "+nomeFilePatenteRetro
		+ "AND AUTISTA.azienda = true "
		+ "AND AUTISTA_1.autistaDocumento.approvatoDocumenti = false "
		+ "AND AUTISTA_1.autistaDocumento.approvatoGenerale = false) as TAB_1_AZIENDE, "
		
		// LICENZE
		+ "exists (SELECT LICENZE FROM AutistaLicenzeNcc LICENZE where LICENZE.autista.id = AUTISTA.id "
		+ "AND LICENZE.approvato = false ) as AutistaLicenzeNccExists, "
		
		// DIPENDENTI
		+ "exists (SELECT DIPENDENTI FROM AutistaSottoAutisti DIPENDENTI where DIPENDENTI.autista.id = AUTISTA.id "
		+ "AND AUTISTA.azienda = true AND DIPENDENTI.approvato = false ) as AutistaSottoAutistiExists, "
		
		// CARTA CIRCOLAZIOE
		+ "exists (SELECT AUTO FROM Autoveicolo AUTO where AUTO.autista.id = AUTISTA.id "
		+ "AND AUTO.autoveicoloCartaCircolazione.approvatoCartaCircolazione = false "
		+"AND "
		+ "(AUTO.autoveicoloCartaCircolazione.nomeFileCartaCircolazione IS NOT NULL "
			+ "OR AUTO.autoveicoloCartaCircolazione.nomeFileCartaCircolazione NOT LIKE '') "
		+ ") as CartaCircolazioneExists, "
		
		// AUTISTI CHE HANNO INSERITO TUTTI I DOCUMENTI MA CHE NON HANNO CARICATO IL CONTRATTO ------------
		+DocumentiApprovati+ "((AUTISTA_1.autistaDocumento.nomeFileContratto IS NULL OR AUTISTA_1.autistaDocumento.nomeFileContratto LIKE '') "
		+ "OR (AUTISTA_1.autistaDocumento.nomeFileContratto_2 IS NULL OR AUTISTA_1.autistaDocumento.nomeFileContratto_2 LIKE '')) "
		+ ") as TuttoApprovatoTranneContrattoExists, "
		
		// AUTISTI CHE HANNO INSERITO TUTTI I DOCUMENTI E CHE HANNO CARICATO IL CONTRATTO ------------
		+DocumentiApprovati+ "((AUTISTA_1.autistaDocumento.nomeFileContratto IS NOT NULL AND AUTISTA_1.autistaDocumento.nomeFileContratto NOT LIKE '') "
		+ "OR (AUTISTA_1.autistaDocumento.nomeFileContratto_2 IS NOT NULL AND AUTISTA_1.autistaDocumento.nomeFileContratto_2 NOT LIKE '')) "
		+ ") as TuttoApprovatoConContrattoExists, "
		
		// CAMPI SINGOLI 
		+ "exists (SELECT AUTISTA_1 FROM Autista AUTISTA_1 where AUTISTA_1.id = AUTISTA.id "
		+ "AND "+partitaIva+") as partitaIvaExists, "
		+ "exists (SELECT AUTISTA_1 FROM Autista AUTISTA_1 where AUTISTA_1.id = AUTISTA.id "
		+ "AND "+partitaIvaDenominazione+") as partitaIvaDenominazioneExists, "
		+ "exists (SELECT AUTISTA_1 FROM Autista AUTISTA_1 where AUTISTA_1.id = AUTISTA.id "
		+ "AND "+iban+" ) as ibanExists, "
		+ " exists (SELECT PATENTE FROM DocumentiPatente PATENTE where PATENTE.id = AUTISTA.autistaDocumento.documentiPatente.id "
		+ "AND "+numeroPatente+") as numeroPatenteExists, "
		+ " exists (SELECT PATENTE FROM DocumentiPatente PATENTE where PATENTE.id = AUTISTA.autistaDocumento.documentiPatente.id "
		+ "AND "+dataScadenzaPatente+") as dataScadenzaPatenteExists, "	
		+ " exists (SELECT PATENTE FROM DocumentiPatente PATENTE where PATENTE.id = AUTISTA.autistaDocumento.documentiPatente.id "
		+ "AND "+nomeFilePatenteFronte+") as nomeFilePatenteFronteExists, "	
		+ " exists (SELECT PATENTE FROM DocumentiPatente PATENTE where PATENTE.id = AUTISTA.autistaDocumento.documentiPatente.id "
		+ "AND "+nomeFilePatenteRetro+") as nomeFilePatenteRetroExists, "	
		+ "exists (SELECT AUTISTA_1 FROM Autista AUTISTA_1 where AUTISTA_1.id = AUTISTA.id "
		+ "AND "+documentoAggiuntivo+") as documentoAggiuntivoExists, "
		+ " exists (SELECT ISCRIZ_RUOLO FROM DocumentiIscrizioneRuolo ISCRIZ_RUOLO where ISCRIZ_RUOLO.id = AUTISTA.autistaDocumento.documentiIscrizioneRuolo.id "
		+ "AND "+numeroRuoloConducenti+" AND AUTISTA.azienda = false) as numeroRuoloConducentiExists, "
		+ " exists (SELECT ISCRIZ_RUOLO FROM DocumentiIscrizioneRuolo ISCRIZ_RUOLO where ISCRIZ_RUOLO.id = AUTISTA.autistaDocumento.documentiIscrizioneRuolo.id "
		+ "AND "+dataIscrizioneRuoloConducenti+" AND AUTISTA.azienda = false) as dataIscrizioneRuoloConducentiExists, "
		+ " exists (SELECT DOC_CAP FROM DocumentiCap DOC_CAP where DOC_CAP.id = AUTISTA.autistaDocumento.documentiCap.id "
		+ "AND "+numeroCAP+" AND AUTISTA.azienda = false) as numeroCAPExists, "
		+ " exists (SELECT DOC_CAP FROM DocumentiCap DOC_CAP where DOC_CAP.id = AUTISTA.autistaDocumento.documentiCap.id "
		+ "AND "+dataScadenzaCAP+" AND AUTISTA.azienda = false) as dataScadenzaCAPExists, "
		+ " exists (SELECT ISCRIZ_RUOLO FROM DocumentiIscrizioneRuolo ISCRIZ_RUOLO where ISCRIZ_RUOLO.id = AUTISTA.autistaDocumento.documentiIscrizioneRuolo.id "
		+ "AND "+nomeFileDocumentoRuoloConducenti+" AND AUTISTA.azienda = false) as nomeFileDocumentoRuoloConducentiExists, "
		+ " exists (SELECT DOC_CAP FROM DocumentiCap DOC_CAP where DOC_CAP.id = AUTISTA.autistaDocumento.documentiCap.id "
		+ "AND "+nomeFileCAP+" AND AUTISTA.azienda = false) as nomeFileCAPExists "
		
		//----------------------------------------------
		+ "FROM Autista AUTISTA where AUTISTA.bannato = false "
		+ "ORDER BY "
		+ "TuttoApprovatoConContrattoExists DESC, " //tutti documenti approvati e INSERITO CONTRATTO e autista non approvato
		+ "TAB_1_2_PRIVATI DESC, "
		+ "TAB_1_AZIENDE DESC, "
		+ "AutistaLicenzeNccExists DESC, "
		+ "AutistaSottoAutistiExists DESC, "
		+ "CartaCircolazioneExists DESC, "
		+ "TuttoApprovatoTranneContrattoExists DESC, " //tutti documenti approvati e NON INSERITO CONTRATTO e autista non approvato
		+ "AUTISTA.autistaDocumento.approvatoGenerale DESC, " // AUTISTA APPROVATO
		// CAMPI SINGOLI
		+ "partitaIvaExists DESC, "
		+ "partitaIvaDenominazioneExists DESC, "//187 (CDY)
		+ "ibanExists DESC, "
		+ "numeroPatenteExists DESC, "
		+ "dataScadenzaPatenteExists DESC, "
		+ "nomeFilePatenteFronteExists DESC, "
		+ "nomeFilePatenteRetroExists DESC, "
		+ "documentoAggiuntivoExists DESC, "
		+ "numeroRuoloConducentiExists DESC, "
		+ "dataIscrizioneRuoloConducentiExists DESC, "
		+ "numeroCAPExists DESC, "
		+ "dataScadenzaCAPExists DESC, "
		+ "nomeFileDocumentoRuoloConducentiExists DESC, "
		+ "nomeFileCAPExists DESC, "

		+ "AUTISTA.user.signupDate DESC ";
		Query q = this.getSession().createQuery( Query );
		return q.setMaxResults( maxResults ).setFirstResult( firstResult ).list();
	}
	
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public List<Autista> getAutistaTable() {
		Criteria criteria = getSession().createCriteria(Autista.class)
			.setProjection(Projections.projectionList()
				.add(Projections.property("id"), "id"))
			.setResultTransformer(Transformers.aliasToBean(Autista.class))
			.addOrder(Order.desc("id"));
		return criteria.list();
	}
	
	@Override
	@Transactional(readOnly = true)
	public int getCountAutista() {
		return (int)(long)getSession().createCriteria(Autista.class)
			.setProjection(Projections.rowCount()).uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public List<Autista> getAutistaTable_2_limit(int maxResults, Integer firstResult) {
		//String queryString4 = "SELECT c, c.autista.id, c.autista.percentualeServizio FROM Autoveicolo c WHERE c.autista.user.id = :id  ";
		//String queryString = "FROM Autista c ORDER BY c.id DESC";
		//Query query = getSession().createQuery(queryString).setMaxResults( maxResults ).setFirstResult( firstResult );
		// Questo funziona solo quando nel SELECT c'è un solo tipo di Oggetto. (SIA IN CRITERIA CHE IN HQL)
		//Query query = getSession().createQuery(queryString).setMaxResults( maxResults ).setFirstResult( firstResult ).setResultTransformer(Transformers.aliasToBean(Autista.class)); 
		
		//return query.list();
		
		return getSession().createCriteria(Autista.class)
				.setMaxResults( maxResults ).setFirstResult( firstResult ).addOrder(Order.desc("id")).list();
		
		//List<Autista> aa = query. list();
		/*
		for(Object ite: aa){
			Object[] we = (Object[]) ite;
			Autoveicolo autov = (Autoveicolo)we[0];
		}
		*/
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Autista> getAutistiBy_LIKE(String term){
		Criterion crit1 = Restrictions.or(
				Restrictions.like("user.firstName", "%"+term+"%", MatchMode.END)
				,Restrictions.like("user.lastName", "%"+term+"%", MatchMode.END)
				,Restrictions.like("user.username", "%"+term+"%", MatchMode.END)
				,Restrictions.like("user.phoneNumber", "%"+term+"%", MatchMode.END)
				,Restrictions.like("user.email", "%"+term+"%", MatchMode.END)
				,Restrictions.like("autistaDocumento.partitaIva", "%"+term+"%", MatchMode.END)
				,Restrictions.like("autistaDocumento.partitaIvaDenominazione", "%"+term+"%", MatchMode.END)
				// (Altrimenti senza LEFT_OUTER_JOIN non trova niente perché il join può trovare la tabella NULLA
				// invece con il LEFT_OUTER_JOIN il join è indiretto e trova comunque qualcosa anche se la tabella è NULLA
				// indiretto e va bene anche se non trova niente o null)
				,Restrictions.like("PATENTE.numeroPatente", "%"+term+"%", MatchMode.END) 					// LEFT_OUTER_JOIN
				,Restrictions.like("ISCRIZIONE_RUOLO.numeroRuoloConducenti", "%"+term+"%", MatchMode.END) 	// LEFT_OUTER_JOIN
				,Restrictions.like("CAP.numeroCAP", "%"+term+"%", MatchMode.END) 							// LEFT_OUTER_JOIN
				,Restrictions.like("AUTOVEICOLO.targa", "%"+term+"%", MatchMode.END)
			);
		return getSession().createCriteria(Autista.class)
				.createAlias("user", "user")
				.createAlias("autistaDocumento.documentiPatente", "PATENTE", JoinType.LEFT_OUTER_JOIN)
				.createAlias("autistaDocumento.documentiIscrizioneRuolo", "ISCRIZIONE_RUOLO", JoinType.LEFT_OUTER_JOIN)
				.createAlias("autistaDocumento.documentiCap", "CAP", JoinType.LEFT_OUTER_JOIN)
				.createAlias("autoveicolo", "AUTOVEICOLO")
				.add(crit1).list();
	}
	
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public List<Autista> getIdAutisti() {
        return getSession().createCriteria(Autista.class)
		.setProjection(Projections.projectionList()
				.add(Projections.property("id"), "id"))
			.setResultTransformer(Transformers.aliasToBean(Autista.class)).list();
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public int getCalcolaNumeroCorseApprovate(long idAutista) {
		Criterion critMedioAndata = Restrictions.and(
				Restrictions.eq("autista.id", idAutista),
				Restrictions.eq("ricTrans.approvazioneAndata", Constants.APPROVATA));
		Criterion critMedioRitorno = Restrictions.and(
				Restrictions.eq("autista.id", idAutista),
				Restrictions.eq("ricTrans.approvazioneRitorno", Constants.APPROVATA));
		List<RicercaTransfert> ricTransferAndata = getSession().createCriteria(RichiestaMediaAutista.class)
				.createAlias("richiestaMedia.ricercaTransfert", "ricTrans").createAlias("richiestaMedia", "richiestaM")
				.add(critMedioAndata).setProjection(Projections.distinct(Projections.property("richiestaM.ricercaTransfert"))).list();
		List<RicercaTransfert> ricTransferRitorno = getSession().createCriteria(RichiestaMediaAutista.class)
				.createAlias("richiestaMedia.ricercaTransfert", "ricTrans").createAlias("richiestaMedia", "richiestaM")
				.add(critMedioRitorno).setProjection( Projections.distinct(Projections.property("richiestaM.ricercaTransfert"))).list();
		
		/*
		for(RicercaTransfert ite: ricTransferAndata) {
			System.out.println("getCalcolaNumeroCorseApprovate: "+ite.getId()+" | "+ite.isRitorno()+" | "+ite.getFormattedAddress_Partenza()+" "+ite.getFormattedAddress_Arrivo());
		}
		*/
		
		Criterion critPartAndata = Restrictions.and(
				Restrictions.eq("auto.autista.id", idAutista),
				Restrictions.eq("ricTrans.approvazioneAndata", Constants.APPROVATA));
		Criterion critPartRitorno = Restrictions.and(
				Restrictions.eq("auto.autista.id", idAutista),
				Restrictions.eq("ricTrans.approvazioneRitorno", Constants.APPROVATA));
		List<RichiestaAutistaParticolare> richPartAndata = getSession().createCriteria(RichiestaAutistaParticolare.class)
				.createAlias("autoveicolo", "auto").createAlias("ricercaTransfert", "ricTrans").add(critPartAndata).list(); 
		List<RichiestaAutistaParticolare> richPartRitorno = getSession().createCriteria(RichiestaAutistaParticolare.class)
				.createAlias("autoveicolo", "auto").createAlias("ricercaTransfert", "ricTrans").add(critPartRitorno).list(); 
		
		int numCorseApprov = 0;
		numCorseApprov = ricTransferAndata.size() + ricTransferRitorno.size() + richPartAndata.size() + richPartRitorno.size();
		return numCorseApprov;
	}
	
	
	@Transactional
	@Override
	public int updateNumeroCorseEseguite(long idAutista, int numCorseEseguite) {
		String query = "update Autista set numCorseEseguite = :numCorseEseguite WHERE id = :idAutista ";
	    Query setQuery = getSession().createQuery(query);
	    setQuery.setParameter("idAutista", idAutista);
	    setQuery.setParameter("numCorseEseguite", numCorseEseguite);
		return setQuery.executeUpdate();
	}
	
	
	@Transactional
	@Override
	public Autista saveAutista(Autista autista) {
		getSession().saveOrUpdate(autista);
		
        BackupInfoUtente backup = new BackupInfoUtente(autista, new Date(), /*autista.getPrezzoDesideratoRicTransfert(),*/ 
        		autista.getNote(), autista.getAutistaDocumento().getIban(), autista.getNumCorseEseguite(),
        		autista.isBannato(), autista.isAttivo(), autista.getAutistaDocumento().getNumeroCartaIdentita());
        getSession().save(backup);
        
		return autista;
	}
	
	

}
