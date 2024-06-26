package com.apollon.dao.hibernate;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.apollon.Constants;
import com.apollon.dao.RicercaTransfertDao;
import com.apollon.model.Aeroporti;
import com.apollon.model.AutistaAeroporti;
import com.apollon.model.AutistaMusei;
import com.apollon.model.AutistaPortiNavali;
import com.apollon.model.Autoveicolo;
import com.apollon.model.ClasseAutoveicolo;
import com.apollon.model.Disponibilita;
import com.apollon.model.Musei;
import com.apollon.model.PortiNavali;
import com.apollon.model.Province;
import com.apollon.model.RicercaTransfert;
import com.apollon.model.RichiestaMedia;
import com.apollon.model.RichiestaMediaAutista;
import com.apollon.model.RichiestaMediaAutistaAutoveicolo;
import com.apollon.model.RichiestaAutistaParticolare;
import com.apollon.model.Tariffe;
import com.apollon.util.DammiTempoOperazione;
import com.apollon.util.DateUtil;
import com.apollon.webapp.util.bean.DataPrenotazioneCorsaAutista;
import com.apollon.webapp.util.bean.GestioneCorseMedieAdmin;
import com.apollon.webapp.util.bean.GestioneCorseMedieAdmin.GestioneCorseMedieAdminAutisti;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@EnableTransactionManagement
@Repository("RicercaTransfertDao")
public class RicercaTransfertDaoHibernate extends GenericDaoHibernate<RicercaTransfert, Long> implements RicercaTransfertDao {

	public RicercaTransfertDaoHibernate() {
		super(RicercaTransfert.class);
	}
	
	//--------------------------------------------------------------
	//--------- REPORT ----------------------------------
	//--------------------------------------------------------------
	
	String dataRicercaTransfer_Report = "'01/01/2018'";
	
	
	/**
	 * Elenco Autisti che hanno fatto almeno una corsa
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Object[]> report_7(){
		String queryString = "SELECT USER.email, USER.first_name, USER.last_name, USER.id "
		+ "FROM autista AS AUTISTA INNER JOIN app_user AS USER ON AUTISTA.id_user = USER.id  "
		+ "WHERE AUTISTA.numCorseEseguite > 1 "
		+ "ORDER BY AUTISTA.numCorseEseguite DESC ";
		Query q = this.getSession().createSQLQuery( queryString );
		System.out.println( "size: "+q.list().size() );
		List<Object[]> zz = q.list();
		return zz;
	}
	
	
	
	/**
	 * NUMERO DI KILOMETRI PIù RICERCATI 
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<RicercaTransfert> report_6(){
		String queryString = "SELECT RIC.* "
		+ "FROM ricerca_transfert RIC WHERE "
		+ "RIC.dataRicerca >= STR_TO_DATE("+dataRicercaTransfer_Report+", '%d/%m/%Y') "
		+ "AND RIC.siglaProvicia_Partenza <> 'LT' "
		+ "Order by RIC.id_ricerca_transfert DESC ";
		Query q = this.getSession().createSQLQuery( queryString ).addEntity(RicercaTransfert.class);
		List<RicercaTransfert> zz = (List<RicercaTransfert>) q.list();
		//System.out.println( "size: "+q.list().size() );
		//List<Object[]> zz = q.list();
		return zz;
	}
	
	/**
	 * LISTA TRANSFER RICERCATI
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Object[]> report_5(){
		String queryString = "SELECT RIC.dataRicerca, RIC.id_ricerca_transfert, RIC.dataOraPrelevamentoDate, RIC.dataOraRitornoDate, RIC.numeroPasseggeri, "
				+ "(SELECT VISIT.city FROM visitatori VISIT WHERE RIC.id_visitatori is not null AND RIC.id_visitatori = VISIT.id_visitatori ) AS CITY, "
				+ "RIC.formattedAddress_Partenza, RIC.formattedAddress_Arrivo, RIC.distanzaText, RIC.ritorno, RIC.distanzaTextRitorno, "
				+ "(SELECT USER.email FROM app_user USER WHERE RIC.id is not null AND RIC.id = USER.id ) AS EMAIL_CLIENTE, "
				+ "json_extract(RIC.infoPasseggero, '$."+Constants.PaymentProviderAmountJSON+"') AS PAY_LORDO "
		+ "FROM ricerca_transfert RIC WHERE "
		+ "RIC.dataRicerca >= STR_TO_DATE("+dataRicercaTransfer_Report+", '%d/%m/%Y') "
		+ "Order by RIC.id_ricerca_transfert DESC ";
		Query q = this.getSession().createSQLQuery( queryString );
		//System.out.println( "size: "+q.list().size() );
		List<Object[]> zz = q.list();
		return zz;
	}
	
	/**
	 * LISTA DEI CLIENTI CHE HANNO ACQUISTATO PIU' SERVIZI E LORDO TOTALE ACQUISTATO e naziolità
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Object[]> report_4(){
		String queryString = "SELECT DISTINCT USER.email, "
			+ "(SELECT COUNT(*) "
				+ "FROM ricerca_transfert RIC_2 WHERE RIC_2.id = RIC.id AND RIC_2.dataRicerca >= STR_TO_DATE("+dataRicercaTransfer_Report+", '%d/%m/%Y') ) AS COUNT, "
			+ "(SELECT SUM(json_extract(RIC_2.infoPasseggero, '$."+Constants.PaymentProviderAmountJSON+"'))  "
				+ "FROM ricerca_transfert RIC_2 WHERE RIC_2.id = RIC.id AND RIC_2.dataRicerca >= STR_TO_DATE("+dataRicercaTransfer_Report+", '%d/%m/%Y') ) AS TOTALE, "
			+ "(SELECT VISIT.countryName "
				+ "FROM ricerca_transfert RIC_2, visitatori VISIT "
				+ "WHERE RIC_2.id = RIC.id AND RIC_2.id_visitatori = VISIT.id_visitatori AND RIC_2.dataRicerca >= STR_TO_DATE("+dataRicercaTransfer_Report+", '%d/%m/%Y') "
				+ "GROUP BY VISIT.countryCode "
				+ "ORDER BY COUNT( VISIT.countryCode ) DESC LIMIT 1) AS COUNTRY_COD,"
				+ "USER.first_name, USER.last_name, USER.id "
		+ "FROM ricerca_transfert RIC INNER JOIN app_user USER ON RIC.id = USER.id  "
		+ "AND RIC.id is not null "
		+ "AND RIC.dataRicerca >= STR_TO_DATE("+dataRicercaTransfer_Report+", '%d/%m/%Y') "
		+ "Order by COUNT DESC, RIC.id_ricerca_transfert ASC ";
		Query q = this.getSession().createSQLQuery( queryString );
		//System.out.println( "size: "+q.list().size() );
		List<Object[]> zz = q.list();
		return zz;
	}
	
	/**
	 * NUMERO AUTOVEICOLI ATTIVI
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Object[]> report_3(){
		String queryString = "SELECT count(*)  "
		+ "FROM autoveicolo MACCHINA, autista DRIVER WHERE "
		+ "MACCHINA.id_autista = DRIVER.id_autista "
		+ "AND DRIVER.approvatoGenerale = 1 "
		+ "AND DRIVER.attivo = 1 "
		+ "AND DRIVER.bannato = 0 "
		+ "AND MACCHINA.autoveicolo_sospeso = 0 "
		+ "AND MACCHINA.autoveicolo_cancellato = 0 ";
		Query q = this.getSession().createSQLQuery( queryString );
		//System.out.println( "size: "+q.list().size() );
		List<Object[]> zz = q.list();
		return zz;
	}
	
	/**
	 * TOTALE AMMONTARE LORDO CORSE VENDUTE 
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Object[]> report_2(){
		String queryString = "SELECT " 
		+ "SUM( json_extract(RIC.infoPasseggero, '$."+Constants.PaymentProviderAmountJSON+"') ) AS lordo "
		+ "FROM ricerca_transfert RIC WHERE "
		+ "RIC.id is not null "
		+ "AND RIC.dataRicerca >= STR_TO_DATE("+dataRicercaTransfer_Report+", '%d/%m/%Y') ";
		Query q = this.getSession().createSQLQuery( queryString );
		//System.out.println( "size: "+q.list().size() );
		List<Object[]> zz = q.list();
		return zz;
	}
	
	
	//String dataRicercaTransfer_Report = "'28/08/2019'";
	
	
	/**
	 * AMMONTARE, LORDO, TASSE, COMMISSIONE, TOTALE MESE, EMAIL CLIENTE
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Object[]> report_2_bis_1(){
		String queryString = "SELECT RIC.id_ricerca_transfert, RIC.tipoServizio  "
		//+ "SUM( json_extract(RIC.infoPasseggero, '$."+Constants.PaymentProviderAmountJSON+"') ) AS lordo " // 16330.0
		//+ "SUM(json_extract(RIC.infoPasseggero, '$."+Constants.PaymentProviderAmountJSON+"')) - SUM(json_extract(RIC.infoPasseggero, '$."+Constants.PaymentProviderFeeJSON+"')) AS lordo "
		//+ "(SELECT VISIT.city FROM visitatori VISIT WHERE RIC.id_visitatori is not null AND RIC.id_visitatori = VISIT.id_visitatori ) AS NETTO "
		+ "FROM ricerca_transfert RIC WHERE "
		+ "RIC.id is not null "
		+ "AND ( "
			+ "EXISTS (SELECT * FROM richiesta_media RIC_MEDIA, richiesta_media_autista RIC_MEDIA_AUTISTA WHERE "
			+ "RIC.id_ricerca_transfert = RIC_MEDIA.id_ricerca_transfert AND RIC_MEDIA.classeAutoveicoloScelta = 1 "
			+ "AND RIC_MEDIA.id_richiesta_media = RIC_MEDIA_AUTISTA.id_richiesta_media AND RIC_MEDIA_AUTISTA.corsaConfermata = 1) "	
			
			+ "OR EXISTS (SELECT * FROM richiesta_autista_particolare RIC_PART WHERE RIC.id_ricerca_transfert = RIC_PART.id_ricerca_transfert) "
			
			+ "OR EXISTS (SELECT * FROM aga_tariffari TARIFF WHERE RIC.id_ricerca_transfert = TARIFF.id_ricerca_transfert ) "
		+ ")"
		//+"AND EXISTS (SELECT * FROM aga_tariffari TARIFF WHERE RIC.id_ricerca_transfert = TARIFF.id_ricerca_transfert ) " 
		+ "AND RIC.dataRicerca >= STR_TO_DATE("+dataRicercaTransfer_Report+", '%d/%m/%Y') "
		+ "Order by RIC.dataRicerca DESC ";
		Query q = this.getSession().createSQLQuery( queryString );
		//System.out.println( "size: "+q.list().size() );
		List<Object[]> zz = q.list();
		
		/*
		for(Object[] ite : zz) {
			System.out.println( "saas: "+Long.parseLong(ite[0].toString()) );
			
		}
		*/
		
		return zz;
	}
	
	/**
	 * NUMERO TOTALE DI TRANSFER VENDUTI
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Object[]> report_1(){
		String queryString = "SELECT count(*) " 
		+ "FROM ricerca_transfert RIC WHERE "
		+ "RIC.id is not null "
		+ "AND (RIC.approvazioneAndata = 1 OR RIC.approvazioneAndata = 2) "
		+ "AND RIC.dataRicerca >= STR_TO_DATE("+dataRicercaTransfer_Report+", '%d/%m/%Y') ";
		Query q = this.getSession().createSQLQuery( queryString );
		//System.out.println( "size: "+q.list().size() );
		List<Object[]> zz = q.list();
		return zz;
	}
	
	
	//--------------------------------------------------------------
	//--------- RICHIESTA PREVENTIVI -------------------------------
	//--------------------------------------------------------------
	
	/**
	 * controllo se il (telefonoCustomer o la email o l'indirizzo IP) OPPURE IdUser (LOGGATO) ha già richiesto più di "NumeroTentativiMassimo" preventivo dalle ultime 24 ore
	 */
	@Override
	@Transactional(readOnly = true)
	public boolean CheckRichiestaPreventivi_MAXinvii_IN24Ore(Date DataRicerca, String PhoneNumberCustomer, String RicTransfert_Email, 
			Long RicTransfert_IdUser, String IpAddess, int NumeroTentativiMassimo){
		log.debug( "CheckRichiestaPreventivi_MAXinvii_IN24Ore" );
		log.debug( "DataRicerca: "+DataRicerca );
		log.debug( "PhoneNumberCustomer: "+PhoneNumberCustomer );
		log.debug( "RicTransfert_Email: "+RicTransfert_Email );
		log.debug( "RicTransfert_IdUser: "+RicTransfert_IdUser );
		Date date = DateUtil.TogliOre_a_Data(new Date(), 24);
		String queryString = "SELECT id_ricerca_transfert "
				+ "FROM ricerca_transfert "
				+ "WHERE "
				+ "( phoneNumberCustomer = :PhoneNumberCustomer "
				+ "OR json_extract(infoPasseggero, '$."+Constants.RicTransfert_Email+"') = :RicTransfert_Email "
				+ "OR json_extract(infoPasseggero, '$."+Constants.RicTransfert_IpAddress+"') = :IpAddess "
				+ "OR json_extract(infoPasseggero, '$."+Constants.RicTransfert_IdUser+"') = :RicTransfert_IdUser ) "
				+ "AND json_extract(infoPasseggero, '$."+Constants.RichiestaPreventivi_Inviata+"') = true "
				+ "AND :nowCalendar < DataRicerca ";
		Query q = this.getSession().createSQLQuery( queryString );
		q.setParameter("PhoneNumberCustomer", PhoneNumberCustomer);
		q.setParameter("RicTransfert_Email", RicTransfert_Email);
		q.setParameter("IpAddess", IpAddess);
		q.setParameter("RicTransfert_IdUser", RicTransfert_IdUser);
		q.setParameter("nowCalendar", date);
		//System.out.println( "size: "+q.list().size() );
		if(q != null && q.list() != null && q.list().size() < NumeroTentativiMassimo) {
			System.out.println("return: "+ true);
			return true;
		}else {
			System.out.println("return: "+ false);
			return false;
		}
	}
	
	
	//--------------------------------------------------------------
	//--------- RICERCHE TRANSFERT ACQUISTATI USER -----------------
	//--------------------------------------------------------------
	
	
	@Override
    @Transactional(readOnly = true)
	public RicercaTransfert getInfoAutista_by_ClientePanel_AgendaAutista(Long idRicTransfert, int NumOreInfoAutista) {
	    Date date = DateUtil.AggiungiOre_a_DataAdesso(NumOreInfoAutista);
		Criterion crit = Restrictions.and( Restrictions.eq("id", idRicTransfert), Restrictions.like("tipoServizio", Constants.SERVIZIO_AGENDA_AUTISTA),
				Restrictions.le("dataOraPrelevamentoDate", date));
		return (RicercaTransfert) getSession().createCriteria(RicercaTransfert.class).add( crit ).uniqueResult();
	}
	
	
	@Override
	@Transactional(readOnly = true)
	public boolean CheckRecensioneApprovata_User(long idUser){
		String queryString = "SELECT id_ricerca_transfert "
				+ "FROM ricerca_transfert "
				+ "WHERE json_extract(infoPasseggero, '$."+Constants.RecensioneApprovataJSON+"') = true "
				+ "AND id = :idUser "
				+ "AND approvazioneAndata = "+Constants.APPROVATA;
		Query q = this.getSession().createSQLQuery( queryString );
		q.setParameter("idUser", idUser);
		
		if(q != null && q.list() != null && q.list().size() > 0) {
			return true;
		}else {
			return false;
		}
		
		/*
		Criterion crit1 = Restrictions.and( 
				Restrictions.like("infoPasseggero", "true", MatchMode.ANYWHERE),
				Restrictions.like("infoPasseggero", Constants.RecensioneApprovataJSON, MatchMode.ANYWHERE),
				
				Restrictions.eq("USER.id", idUser), // METTERE: Constants.APPROVATA
				Restrictions.eq("approvazioneAndata", Constants.APPROVATA) 
				);
		List<RicercaTransfert> aa = getSession().createCriteria(RicercaTransfert.class)
				.createAlias("user", "USER").add( crit1 ).addOrder(Order.desc("dataOraPrelevamentoDate")).list();
		if(aa != null && aa.size() > 0) {
			return true;
		}else {
			return false;
		}
		*/
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Object[]> Recencioni_Approvate(boolean aziende){
		// String queryString = "SELECT comune_Partenza, partenzaRequest, comune_Arrivo, arrivoRequest, USER.first_name "
		String queryString = "SELECT "
				+ "RIC.comune_Partenza, "
				+ "RIC.comune_Arrivo, "
				+ "USER.first_name, "
				+ "USER.username, "
				+ "CAST(JSON_UNQUOTE(json_extract(RIC.infoPasseggero, '$."+Constants.RecensioneJSON+"')) AS CHAR) AS recensione, "
				+ "CAST(JSON_UNQUOTE(json_extract(RIC.infoPasseggero, '$."+Constants.PunteggioStelleRecensioneJSON+"')) AS CHAR) AS punteggioStelleRecensione, "
				+ "USER.email, "
				+ "RIC.id_ricerca_transfert, "
				+ "USER.denominazioneCliente, "
				+ "USER.last_name "
				
				+ "FROM ricerca_transfert AS RIC INNER JOIN app_user AS USER ON RIC.id = USER.id AND RIC.approvazioneAndata = "+Constants.APPROVATA+" "
				+ "AND json_extract(RIC.infoPasseggero, '$."+Constants.RecensioneApprovataJSON+"') = true "
				+ (aziende ? "WHERE (USER.denominazioneCliente IS NOT NULL AND USER.denominazioneCliente != '') " : "")

				+ "ORDER BY RIC.dataOraPrelevamentoDate DESC ";
		Query q = this.getSession().createSQLQuery( queryString ); List<Object[]> zz = q.list();
		/*
		for(Object[] ite_object: zz) {
			//for(Object ite_object: ObjectList){
			String aaa = (String) ite_object[0];
			String bbb = (String) ite_object[1];
			String ccc = (String) ite_object[2];
			String ddd = (String) ite_object[3];
			
			//String eee = new String((byte[])ite_object[4], StandardCharsets.UTF_8); 
			String eee = (String) ite_object[4];
			
			Object fff = ite_object[5];
			Object ggg = ite_object[6];
			Object hhh = ite_object[7];
			Object iii = ite_object[8];
			
			System.out.println("- "+aaa+" "+bbb+" "+ccc+" "+ddd+" "+eee+" N. STELLE: "+fff+" "+ggg+" "+hhh+" DENOMINAZ. CLIENTE: "+iii);
		}
		*/
		return zz;
	}
	
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Object[]> OrdinaPerRecencioniNonApprovate(Long idUser){
		String queryString = "SELECT RIC.id_ricerca_transfert, "
		+ "CAST(JSON_UNQUOTE(json_extract(RIC.infoPasseggero, '$."+Constants.RecensioneApprovataJSON+"')) AS CHAR) AS recensioneApprovata, "
		+ "CAST(JSON_UNQUOTE(json_extract(RIC.infoPasseggero, '$."+Constants.RecensioneJSON+"')) AS CHAR) AS recensione, "
		+ "json_extract(RIC.infoPasseggero, '$."+Constants.PunteggioStelleRecensioneJSON+"') AS punteggioStelleRecensione, "
		+ "USER.id, USER.email, USER.denominazioneCliente "
		+ "FROM ricerca_transfert AS RIC INNER JOIN app_user AS USER ON RIC.id = USER.id "
		
		+ "WHERE (JSON_EXTRACT(RIC.infoPasseggero, '$."+Constants.RecensioneJSON+"') IS NOT NULL "
				+ "AND CAST(JSON_UNQUOTE(json_extract(RIC.infoPasseggero, '$."+Constants.RecensioneJSON+"')) AS CHAR) != '') "
		+ (idUser != null ? "AND USER.id = :idUser " : "")
		+ "ORDER BY recensioneApprovata ASC, USER.id ASC, RIC.id_ricerca_transfert DESC ";

		Query q = this.getSession().createSQLQuery( queryString );
		if( idUser != null ) {
			q.setParameter("idUser", idUser);
		}
		
		List<Object[]> zz = q.list();
		
		/*
		for(Object[] ite_object: zz) {
			//for(Object ite_object: ObjectList){
			Object aaa = ite_object[0];
			
			//String bbb = (String) ite_object[1];
			Object bbb = ite_object[1];
			
			String ccc = (String) ite_object[2];
			Object ddd = ite_object[3];
			Object eee = ite_object[4];
			Object fff = ite_object[5];
			Object ggg = ite_object[6];
			
			System.out.println("- idTransfer: "+aaa+" Recens.Approvata: "+bbb+" TestoRecens: "+ccc+" Puntegg.Recens.: "+ddd+" idUser: "+eee+" EmailClient: "+fff+" DenominazioneAzidenale: "+ggg);
		}
		*/
		
		return zz;
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<RicercaTransfert> getTransferAcquistati_User_Approvati(long id){
		// Constants.APPROVATA;
		// Constants.IN_APPROVAZIONE;
		// Constants.NON_APPROVATA;
		Criterion crit1 = Restrictions.and( Restrictions.eq("approvazioneAndata", Constants.APPROVATA), // METTERE: Constants.APPROVATA
				Restrictions.eq("USER.id", id) );
		return getSession().createCriteria(RicercaTransfert.class)
				.createAlias("user", "USER").add( crit1 ).addOrder(Order.desc("dataOraPrelevamentoDate")).list();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<RicercaTransfert> getTransferAcquistati_User_Totali(long id){
		Criterion crit1 = Restrictions.and( Restrictions.eq("USER.id", id) );
		return getSession().createCriteria(RicercaTransfert.class)
				.createAlias("user", "USER").add( crit1 ).addOrder(Order.desc("dataOraPrelevamentoDate")).list();
	}
	

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Object[]> getTransferAcquistati_Approvati_Distinct_User(){
		// distinct
		String queryString4 = "select distinct USER FROM User USER, RicercaTransfert RIC  "
				+ "WHERE USER.id = RIC.user.id "
				+ "AND RIC.approvazioneAndata = "+Constants.APPROVATA+" ORDER BY RIC.dataOraPrelevamentoDate ASC ";
		Query q = this.getSession().createQuery( queryString4 );
		List<Object[]> zz = q.list();
		return zz;
	}
	
	
	//--------------------------------------------------------------
	//--------- RICERCHE TRANSFERT VENDITORE -----------------------
	//--------------------------------------------------------------
	
	@Override
	@Transactional(readOnly = true)
	public RicercaTransfert getRicercaTransfertVenditore(long idUserVenditore, long idTransfert){
		Criterion crit1 = Restrictions.and(
					Restrictions.eq("userVenditore", idUserVenditore),
					Restrictions.eq("id", idTransfert),
					Restrictions.isNotNull("user"));
		
		return (RicercaTransfert) getSession().createCriteria(RicercaTransfert.class).add( crit1 );
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<RicercaTransfert> getRicercheTransfertVenditore(long idUserVenditore){
		Criterion crit1 = Restrictions.and(
					Restrictions.gt("id", -1l),
					Restrictions.eq("userVenditore.id", idUserVenditore),
					Restrictions.isNotNull("user"));
		return getSession().createCriteria(RicercaTransfert.class)
			.add( crit1 ).addOrder(Order.desc("id")).list();
	}
	
	//--------------------------------------------------------------
	//--------- PRENDI CORSE RELATIVE AD AUTISTA -------------------
	//--------------------------------------------------------------
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<RicercaTransfert> getCorseAutistaAgendaAutista(long idUser){
		List<RicercaTransfert> ricercaTransfertList;

		String queryString = "SELECT DISTINCT RIC.* "
				+ "FROM ricerca_transfert RIC, aga_tariffari TARIFF, aga_giornate GIOR, autoveicolo AUTO, autista AUTISTA, app_user USER "
				+ "WHERE "
				+ "RIC.id_ricerca_transfert =  TARIFF.id_ricerca_transfert "
				+ "AND TARIFF.id_aga_giornate = GIOR.id_aga_giornate "
				+ "AND GIOR.id_autoveicolo = AUTO.id_autoveicolo  "
				+ "AND AUTO.id_autista = AUTISTA.id_autista "
				+ "AND AUTISTA.id_user = USER.id "
				+ "AND USER.id = :idUser ";

		Query query = getSession().createSQLQuery(queryString).addEntity(RicercaTransfert.class);
		ricercaTransfertList = query.setParameter("idUser", idUser).list();

		return ricercaTransfertList;
	}
	
	/**
	 * Quando si richiede il preventivo e che quindi il RichiestaAutistaParticolare_Id non è presente
	 */
	@Override
	@Transactional(readOnly = true)
	public RichiestaAutistaParticolare getCorsaAutRicTransfPartPrenotazione(String token){
		String queryString = "FROM RichiestaAutistaParticolare RICH_PART WHERE "
				+ "( json_extract(RICH_PART.ricercaTransfert.infoPasseggero, '$."+Constants.RichiestaAutistaParticolare_Id+"') is null "
						+ "OR json_extract(RICH_PART.ricercaTransfert.infoPasseggero, '$."+Constants.RichiestaAutistaParticolare_Id+"') != RICH_PART.id ) "
				+ "AND RICH_PART.token = :token";
		Query query = getSession().createQuery(queryString);
		return (RichiestaAutistaParticolare) query.setParameter("token", token).uniqueResult();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<RichiestaAutistaParticolare> getCorseAutistaRichiestaAutistaParticolare(long idUser, String token){
		List<RichiestaAutistaParticolare> richiestaAutistaPart;
		String From_RichiestaAutistaParticolare =  "FROM RichiestaAutistaParticolare RICH_PART ";
		if(token == null){
			String queryString1 = From_RichiestaAutistaParticolare + "WHERE RICH_PART.ricercaTransfert.pagamentoEseguitoMedio = true "
					+ "AND json_extract(RICH_PART.ricercaTransfert.infoPasseggero, '$."+Constants.RichiestaAutistaParticolare_Id+"') = RICH_PART.id "
					+ "AND RICH_PART.autoveicolo.autista.user.id = :idUser";
			Query query = getSession().createQuery(queryString1);
			richiestaAutistaPart = query.setParameter("idUser", idUser).list();
		}else{
			String queryString2 = From_RichiestaAutistaParticolare + "WHERE RICH_PART.ricercaTransfert.pagamentoEseguitoMedio = true "
					+ "AND json_extract(RICH_PART.ricercaTransfert.infoPasseggero, '$."+Constants.RichiestaAutistaParticolare_Id+"') = RICH_PART.id "
					+ "AND RICH_PART.token = :token";
			Query query = getSession().createQuery(queryString2);
			richiestaAutistaPart = query.setParameter("token", token).list();
		}
		return richiestaAutistaPart;
	}
	
	
	
	
	/**
	 * Quando si richiede il preventivo e che quindi il RichiestaAutistaParticolare_Id non è presente
	 */
	@Override
	@Transactional(readOnly = true)
	public RichiestaAutistaParticolare getCorsaAutRicTransfMultiploPrenotazione(String token){
		String queryString = "SELECT RICH_PART.* FROM richiesta_autista_particolare RICH_PART, ricerca_transfert RIC "
				+ "WHERE "
				+ "( json_extract(RIC.infoPasseggero, '$."+Constants.RichiestaAutistaMultiplo_Id+"') is null "
				+ "OR json_contains(json_extract(RIC.infoPasseggero, '$."+Constants.RichiestaAutistaMultiplo_Id+"'), Cast(RICH_PART.id_richiesta_autista_particolare as char)) = 0 ) "
				+ "AND RICH_PART.id_ricerca_transfert = RIC.id_ricerca_transfert "
				+ "AND RICH_PART.token = :token";
		
		Query query = getSession().createSQLQuery(queryString).addEntity(RichiestaAutistaParticolare.class);
		return (RichiestaAutistaParticolare) query.setParameter("token", token).uniqueResult();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<RichiestaAutistaParticolare> getCorseAutistaRichiestaAutistaMultiplo(long idUser, String token){
		List<RichiestaAutistaParticolare> richiestaAutistaPart;
		String QueryCommon = "AND RICH_PART.id_ricerca_transfert = RIC.id_ricerca_transfert "
				+ "AND RIC.pagamentoEseguitoMedio = true "
				+ "AND json_contains(json_extract(RIC.infoPasseggero, '$."+Constants.RichiestaAutistaMultiplo_Id+"'), Cast(RICH_PART.id_richiesta_autista_particolare as char)) = 1 ";
		if(token == null){
			String queryString = "SELECT RICH_PART.* FROM richiesta_autista_particolare RICH_PART, ricerca_transfert RIC, autoveicolo AUTO, autista AUTISTA, app_user USER "
					+ "WHERE "
					+ "RICH_PART.id_autoveicolo = AUTO.id_autoveicolo  "
					+ "AND AUTO.id_autista = AUTISTA.id_autista "
					+ "AND AUTISTA.id_user = USER.id "
					+ "AND USER.id = :idUser "
					+ QueryCommon;
			Query query = getSession().createSQLQuery(queryString).addEntity(RichiestaAutistaParticolare.class);
			richiestaAutistaPart = query.setParameter("idUser", idUser).list();
		}else{
			String queryString = "SELECT RICH_PART.* FROM richiesta_autista_particolare RICH_PART, ricerca_transfert RIC "
					+ "WHERE RICH_PART.token = :token "
					+ QueryCommon;
			Query query = getSession().createSQLQuery(queryString).addEntity(RichiestaAutistaParticolare.class);
			richiestaAutistaPart = query.setParameter("token", token).list();
		}
		return richiestaAutistaPart;
	}
	
	
	
	@Override
	@Transactional(readOnly = true)
	public Object DammiTipoServizio(long idRicTransfert) {
		// String queryString = "SELECT comune_Partenza, partenzaRequest, comune_Arrivo, arrivoRequest, USER.first_name "
		String queryString = "SELECT tipoServizio "
				+ "FROM ricerca_transfert RIC WHERE RIC.id_ricerca_transfert = :idRicTransfert ";
		Query q = this.getSession().createSQLQuery( queryString );
		return q.setParameter("idRicTransfert", idRicTransfert).uniqueResult();
	}
	
	
	private static List<Integer> DammiFiltroApprovazioni(boolean inAppr, boolean approv, boolean nonApprov) {
		List<Integer> listApprov = new ArrayList<Integer>();
		if(inAppr){
			listApprov.add( Constants.IN_APPROVAZIONE );
		}
		if(approv){
			listApprov.add( Constants.APPROVATA );
		}
		if(nonApprov){
			listApprov.add( Constants.NON_APPROVATA );
		}
		if(!inAppr && !approv && !nonApprov){
			listApprov.add( null );
		}
		return listApprov;
	}
	
	
	@Override
	@Transactional(readOnly = true)
	public RichiestaMediaAutista getRichiestaMediaAutista_AutistaAssegnatoCorsaMedia(long idTransfert) {
		Criterion crit = Restrictions.and(
					Restrictions.eq("RIC_TRANSFERT.id", idTransfert),
					Restrictions.eq("RIC_TRANSFERT.pagamentoEseguitoMedio", true),
					Restrictions.eq("RICH_MEDIA.classeAutoveicoloScelta", true),
					Restrictions.eq("chiamataPrenotata", true),
					Restrictions.eq("corsaConfermata", true)
					);
		return (RichiestaMediaAutista) getSession().createCriteria(RichiestaMediaAutista.class).add( crit )
				.createAlias("richiestaMedia", "RICH_MEDIA").createAlias("RICH_MEDIA.ricercaTransfert", "RIC_TRANSFERT").uniqueResult();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<RichiestaMediaAutista> getCorseAutistaRichiestaAutistaMedio(long idUser, String token) { 
		Criterion crit;
		if(token == null){
			crit = Restrictions.and(
					Restrictions.eq("user.id", idUser),
					Restrictions.eq("RIC_TRANSFERT.pagamentoEseguitoMedio", true),
					Restrictions.eq("chiamataPrenotata", true),
					Restrictions.eq("corsaConfermata", true),
					Restrictions.eq("RICH_MEDIA.classeAutoveicoloScelta", true));
		}else{
			crit = Restrictions.and(
					Restrictions.eq("tokenAutista", token),
					Restrictions.eq("RIC_TRANSFERT.pagamentoEseguitoMedio", true),
					Restrictions.eq("chiamataPrenotata", true),
					Restrictions.eq("corsaConfermata", true),
					Restrictions.eq("RICH_MEDIA.classeAutoveicoloScelta", true));
		}
		return getSession().createCriteria(RichiestaMediaAutista.class)
			.createAlias("autista.user", "user").createAlias("richiestaMedia", "RICH_MEDIA").createAlias("RICH_MEDIA.ricercaTransfert", "RIC_TRANSFERT")
			.add( crit ).list();
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<RichiestaMediaAutista> getCorsaMediaDisponibile_by_idAutista(long idUser, String token) { 
		Criterion crit;
		if(token == null){
			crit = Restrictions.and(
					Restrictions.eq("user.id", idUser),
					Restrictions.eq("corsaConfermata", false),
					Restrictions.eq("RIC_TRANSFERT.pagamentoEseguitoMedio", true),
					Restrictions.gt("RIC_TRANSFERT.dataOraPrelevamentoDate", new Date()),
					Restrictions.eq("RICH_MEDIA.classeAutoveicoloScelta", true));
		}else{
			crit = Restrictions.and(
					Restrictions.eq("tokenAutista", token),
					Restrictions.eq("corsaConfermata", false),
					Restrictions.eq("RIC_TRANSFERT.pagamentoEseguitoMedio", true),
					Restrictions.gt("RIC_TRANSFERT.dataOraPrelevamentoDate", new Date()),
					Restrictions.eq("RICH_MEDIA.classeAutoveicoloScelta", true));
		}
		return getSession().createCriteria(RichiestaMediaAutista.class)
				.createAlias("autista.user", "user").createAlias("richiestaMedia", "RICH_MEDIA").createAlias("RICH_MEDIA.ricercaTransfert", "RIC_TRANSFERT")
				.add( crit ).list();
	}
	
	
	
	/**
	 * CORSA PARTICOLARE GESTIONE CORSE ADMIN
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<RicercaTransfert> getCorseAutistaAgendaAutistaALL(boolean inAppr, boolean approv, boolean nonApprov, Date from, Date to, Long idRic){
		List<Integer> listApprov = DammiFiltroApprovazioni(inAppr, approv, nonApprov);
		String queryString = "SELECT DISTINCT RIC.* "
				+ "FROM aga_tariffari TARIFF, ricerca_transfert RIC "
				+ "WHERE ( RIC.approvazioneAndata IN (:listApprov) OR RIC.approvazioneRitorno IN (:listApprov) ) "
				+ "AND RIC.tipoServizio = '"+Constants.SERVIZIO_AGENDA_AUTISTA+"' "
				+ "AND RIC.id_ricerca_transfert = TARIFF.id_ricerca_transfert "
				+ (from != null ? "AND RIC.dataOraPrelevamentoDate >= :from " : "") 
				+ (to != null ? "AND RIC.dataOraPrelevamentoDate < :to " : "")
				+ (idRic != null ? "AND RIC.id_ricerca_transfert = :idRic " : "" );
		Query query = this.getSession().createSQLQuery( queryString ).addEntity(RicercaTransfert.class).setParameterList("listApprov", listApprov);
		if( from != null ) {
			query.setParameter("from", from);
		}
		if( to != null ) {
			query.setParameter("to", to);
		}
		if( idRic != null ) {
			query.setParameter("idRic", idRic);
		}
		return (List<RicercaTransfert>) query.list();
	}
	
	
	
	/**
	 * CORSA PARTICOLARE GESTIONE CORSE ADMIN
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<RicercaTransfert> getCorseAutistaRichiestaAutistaMultiplaALL(boolean inAppr, boolean approv, boolean nonApprov, Date from, Date to, Long idRic){
		List<Integer> listApprov = DammiFiltroApprovazioni(inAppr, approv, nonApprov);
		String queryString = "SELECT RIC.* "
				+ "FROM ricerca_transfert RIC "
				+ "WHERE ( RIC.approvazioneAndata IN (:listApprov) OR RIC.approvazioneRitorno IN (:listApprov) ) "
				+ "AND json_extract(RIC.infoPasseggero, '$."+Constants.RichiestaAutistaMultiplo_Id+"') IS NOT NULL "
				+ (from != null ? "AND RIC.dataOraPrelevamentoDate >= :from " : "") 
				+ (to != null ? "AND RIC.dataOraPrelevamentoDate < :to " : "")
				+ (idRic != null ? "AND RIC.id_ricerca_transfert = :idRic " : "" );

		Query query = this.getSession().createSQLQuery( queryString ).addEntity(RicercaTransfert.class)
				.setParameterList("listApprov", listApprov);
		if( from != null ) {
			query.setParameter("from", from);
		}
		if( to != null ) {
			query.setParameter("to", to);
		}
		if( idRic != null ) {
			query.setParameter("idRic", idRic);
		}
		return (List<RicercaTransfert>) query.list();
	}
	
	/**
	 * CORSA PARTICOLARE GESTIONE CORSE ADMIN
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<RichiestaAutistaParticolare> getCorseAutistaRichiestaAutistaParticolareALL(boolean inAppr, boolean approv, boolean nonApprov, Date from, Date to, Long idRic){
		List<Integer> listApprov = DammiFiltroApprovazioni(inAppr, approv, nonApprov);
		String queryString = "SELECT RICH_PART.* "
				+ "FROM richiesta_autista_particolare RICH_PART, ricerca_transfert RIC "
				+ "WHERE ( RIC.approvazioneAndata IN (:listApprov) OR RIC.approvazioneRitorno IN (:listApprov) ) "
				+ "AND json_extract(RIC.infoPasseggero, '$."+Constants.RichiestaAutistaParticolare_Id+"') = RICH_PART.id_richiesta_autista_particolare "
				+ "AND RIC.id_ricerca_transfert = RICH_PART.id_ricerca_transfert "
				+ (from != null ? "AND RIC.dataOraPrelevamentoDate >= :from " : "") 
				+ (to != null ? "AND RIC.dataOraPrelevamentoDate < :to " : "")
				+ (idRic != null ? "AND RIC.id_ricerca_transfert = :idRic " : "" );
		Query query = this.getSession().createSQLQuery( queryString ).addEntity(RichiestaAutistaParticolare.class).setParameterList("listApprov", listApprov);
		if( from != null ) {
			query.setParameter("from", from);
		}
		if( to != null ) {
			query.setParameter("to", to);
		}
		if( idRic != null ) {
			query.setParameter("idRic", idRic);
		}
		return (List<RichiestaAutistaParticolare>) query.list();
	}
	
	
	/**
	 * CORSA MEDIA GESTIONE CORSE ADMIN
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<GestioneCorseMedieAdmin> getCorseAutistaRichiestaAutistaMedioALL(boolean inAppr, boolean approv, 
			boolean nonApprov, Date from, Date to, Long idRic) throws DataIntegrityViolationException, HibernateJdbcException{
		List<Integer> listApprov = DammiFiltroApprovazioni(inAppr, approv, nonApprov);
		List<GestioneCorseMedieAdmin> gestioneCorseMedieAdmin_list = new ArrayList<GestioneCorseMedieAdmin>();
		Criterion crit1 = Restrictions.and(
				Restrictions.or( Restrictions.in("RIC_TRANSFERT.approvazioneAndata", listApprov),
						Restrictions.in("RIC_TRANSFERT.approvazioneRitorno", listApprov) ),
				Restrictions.eq("RIC_TRANSFERT.pagamentoEseguitoMedio", true));
		Criterion crit2 = Restrictions.and();
		if(from != null && to != null){
			crit2 = Restrictions.and(
					Restrictions.ge("RIC_TRANSFERT.dataOraPrelevamentoDate", from),
					Restrictions.lt("RIC_TRANSFERT.dataOraPrelevamentoDate", to));
		}else if(from != null && to == null){
			crit2 = Restrictions.and(
					Restrictions.ge("RIC_TRANSFERT.dataOraPrelevamentoDate", from));
		}else if(from == null && to != null){
			crit2 = Restrictions.and(
					Restrictions.lt("RIC_TRANSFERT.dataOraPrelevamentoDate", to));
		}
		Criterion crit3 = Restrictions.and();
		if(idRic != null){
			crit3 = Restrictions.and(
					Restrictions.eq("RIC_TRANSFERT.id", idRic));
		}
		List<RichiestaMedia> richAutistaMedioList = getSession().createCriteria(RichiestaMedia.class)
			.createAlias("ricercaTransfert", "RIC_TRANSFERT").add( Restrictions.eq("classeAutoveicoloScelta", true) ).add( crit1 ).add( crit2 ).add( crit3 ).list();
		for(RichiestaMedia richiestaMedia_ite: richAutistaMedioList){
			GestioneCorseMedieAdmin CorseAdminMedie = new GestioneCorseMedieAdmin();
			CorseAdminMedie.setRicTransfert( richiestaMedia_ite.getRicercaTransfert() );
			List<Long> idAutistaList = new ArrayList<Long>();
			List<GestioneCorseMedieAdmin.GestioneCorseMedieAdminAutisti> autistaCorsaAdmin_List = new ArrayList<GestioneCorseMedieAdminAutisti>();
			CorseAdminMedie.setClasseAutoveicoloSceltaCliente( richiestaMedia_ite.getClasseAutoveicolo() );
			CorseAdminMedie.setPrezzoCliente( richiestaMedia_ite.getPrezzoTotaleCliente().toString() );
			CorseAdminMedie.setRimborsoCliente( richiestaMedia_ite.getRimborsoCliente() );
			CorseAdminMedie.setMaggiorazioneNotturna( richiestaMedia_ite.getMaggiorazioneNotturna() );
			for(RichiestaMediaAutista richAutistaMedio_ite: richiestaMedia_ite.getRichiestaMediaAutista()){
				if( !idAutistaList.contains(richAutistaMedio_ite.getAutista().getId()) ){
					GestioneCorseMedieAdmin.GestioneCorseMedieAdminAutisti autistaCorsaAdmin = new GestioneCorseMedieAdminAutisti();
					long IDAutista = richAutistaMedio_ite.getAutista().getId();
					//l'ho messo piu su
					//CorseAdminMedie.setPrezzoCliente( richAutistaMedio_ite.getRichiestaMedia().getPrezzoTotaleCliente().toString() );
					autistaCorsaAdmin.setIdAutista( IDAutista );
					autistaCorsaAdmin.setAssegnato(false);
					autistaCorsaAdmin.setTokenAutista(richAutistaMedio_ite.getTokenAutista());
					autistaCorsaAdmin.setFullNameAutisa( richAutistaMedio_ite.getAutista().getUser().getFullName() );
					autistaCorsaAdmin.setPrezzoAutista( richAutistaMedio_ite.getPrezzoTotaleAutista().toString() );
					autistaCorsaAdmin.setNoteAutista( richAutistaMedio_ite.getAutista().getNote() );
					autistaCorsaAdmin.setPrezzoCommServ( richAutistaMedio_ite.getPrezzoCommissioneServizio().toString() );
					autistaCorsaAdmin.setCorseEffAutista( richAutistaMedio_ite.getAutista().getNumCorseEseguite() );
					autistaCorsaAdmin.setTelAutista( richAutistaMedio_ite.getAutista().getUser().getPhoneNumber() );
					autistaCorsaAdmin.setTariffaPerKm( richAutistaMedio_ite.getTariffaPerKm() );
					autistaCorsaAdmin.setMaggiorazioneNotturna( richAutistaMedio_ite.getRichiestaMedia().getMaggiorazioneNotturna() );
					autistaCorsaAdmin.setOrdineAutista( richAutistaMedio_ite.getOrdineAutista() );
					autistaCorsaAdmin.setInvioSms( richAutistaMedio_ite.isInvioSms() );
					// numero in ordine di tempo per la chiamata prenotata
					List<DataPrenotazioneCorsaAutista> ss = new ArrayList<DataPrenotazioneCorsaAutista>();
					for(RichiestaMediaAutista listRichAutistaMedio_dataPrenot_ite: richiestaMedia_ite.getRichiestaMediaAutista()){
						if(listRichAutistaMedio_dataPrenot_ite.getDataChiamataPrenotata() != null){
							DataPrenotazioneCorsaAutista aa = new DataPrenotazioneCorsaAutista();
							aa.setIdAutista( listRichAutistaMedio_dataPrenot_ite.getAutista().getId() );
							aa.setDataPrenotazioneCorsaAutista( listRichAutistaMedio_dataPrenot_ite.getDataChiamataPrenotata() );
							ss.add( aa );
						}
					}
					Collections.sort(ss);
					int i = 0;
					for(DataPrenotazioneCorsaAutista dd: ss){
						i++;
						if( IDAutista ==  dd.getIdAutista()){
							autistaCorsaAdmin.setOrdineChiamataPrenotata(i);
						}
					}
					// lista autoveicoli
					/*
					List<Autoveicolo> autoveicoliRichiestiList = new ArrayList<Autoveicolo>();
					for(RichiestaAutistaMedio listRichAutistaMedio_auto_ite: listRichAutistaMedio){
						if( IDAutista == listRichAutistaMedio_auto_ite.getAutista().getId() ){
							autoveicoliRichiestiList.add( listRichAutistaMedio_auto_ite.getAutoveicolo() );
						}
					}
					autistaCorsaAdmin.setAutoveicoliRichiestiList(autoveicoliRichiestiList);
					*/
					List<RichiestaMediaAutistaAutoveicolo> richAutistMedioAutoList = new ArrayList<RichiestaMediaAutistaAutoveicolo>();
					richAutistMedioAutoList.addAll( richAutistaMedio_ite.getRichiestaMediaAutistaAutoveicolo() );
					List<Autoveicolo> autoveicoliRichiestiList = new ArrayList<Autoveicolo>();
					//for( RichiestaMediaAutistaAutoveicolo richAutistMedioAutoList_ite: richAutistMedioAutoList){
						//autoveicoliRichiestiList.add( richAutistMedioAutoList_ite.getAutoveicolo() );
					//}
					for( RichiestaMediaAutistaAutoveicolo richAutistMedioAutoList_ite: richAutistaMedio_ite.getRichiestaMediaAutistaAutoveicolo()){
						autoveicoliRichiestiList.add( richAutistMedioAutoList_ite.getAutoveicolo() );
					}
					autistaCorsaAdmin.setAutoveicoliRichiestiList(autoveicoliRichiestiList);
					// autista assegnato alla corsa
					for(RichiestaMediaAutista listRichAutistaMedio_ass_ite: richiestaMedia_ite.getRichiestaMediaAutista()){
						if( IDAutista == listRichAutistaMedio_ass_ite.getAutista().getId() &&
								listRichAutistaMedio_ass_ite.isCorsaConfermata() ){
							autistaCorsaAdmin.setAssegnato(true);
							CorseAdminMedie.setIdAutistaAssegnato(IDAutista);
						}
					}
					autistaCorsaAdmin_List.add(autistaCorsaAdmin);
				}
				// questo mi serve per non mettere due autisti uguali nella lista
				idAutistaList.add( richAutistaMedio_ite.getAutista().getId() );
			}
			CorseAdminMedie.setGestioneCorseMedieAdminAutisti(autistaCorsaAdmin_List);
			gestioneCorseMedieAdmin_list.add(CorseAdminMedie);
		}
		return gestioneCorseMedieAdmin_list;
	}
		

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public boolean getCorsaMediaDisponibile_by_idRicerca(long idRicTransfert){
		Criterion crit1 = Restrictions.and(
				Restrictions.eq("ricercaTransfert.id", idRicTransfert),
				Restrictions.eq("invioSmsCorsaConfermata", true));
		List<RichiestaMediaAutista> richAutistaMedioList = getSession().createCriteria(RichiestaMediaAutista.class).add( crit1 )
				.createAlias("richiestaMedia.ricercaTransfert", "ricTransfert").list();
		if(richAutistaMedioList != null && richAutistaMedioList.size() == 0){
			return true;
		}else{
			return false;
		}
	}
	
	
	@Deprecated
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	/**
	 * questo qui è sbagliato ho cambiato il sistema
	 */
	public List<RichiestaMediaAutista> getAutoveicoliUtilizzabiliAutistaMedio(long idRicTransfert, long idUser){
		Criterion crit1 = Restrictions.and(
				Restrictions.eq("user.id", idUser),
				Restrictions.eq("ricTransfert.id", idRicTransfert));
		return getSession().createCriteria(RichiestaMediaAutista.class).add( crit1 )
				.createAlias("autista.user", "user").createAlias("richiestaMedia.ricercaTransfert", "ricTransfert").list();
	}
	
	
	//--------------------------------------------------------------
	//--------- PRENDI CORSE RELATIVE A CLIENTE --------------------
	//--------------------------------------------------------------
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<RicercaTransfert> getCorseClienteRichiestaAutistaMultiplo(Long idUser, Long idRicercaTransfert) {
		String queryString = "SELECT RIC.* "
				+ "FROM ricerca_transfert RIC "
				+ "WHERE " + (idUser != null ? "RIC.id = :idUser " : " RIC.id_ricerca_transfert = :idRicercaTransfert ")
				+ "AND RIC.pagamentoEseguitoMedio = true "
				+ "AND json_extract(RIC.infoPasseggero, '$."+Constants.RichiestaAutistaMultiplo_Id+"') IS NOT NULL ";
		List<RicercaTransfert> aa = null;
		if(idUser != null) {
			aa = (List<RicercaTransfert>) this.getSession()
					.createSQLQuery( queryString ).addEntity(RicercaTransfert.class).setParameter("idUser", idUser).list();
		}else if(idRicercaTransfert != null) {
			aa = (List<RicercaTransfert>) this.getSession()
					.createSQLQuery( queryString ).addEntity(RicercaTransfert.class).setParameter("idRicercaTransfert", idRicercaTransfert).list();
		}else {
			aa = null;
		}
		/*
		Query q = this.getSession().createSQLQuery( queryString );
		q.setParameter("idUser", idUser);
		*/
		return aa;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<RichiestaAutistaParticolare> getCorseClienteRichiestaAutistaParticolare(Long idUser, Long idRicercaTransfert) {
		/*
		Criterion crit1 = Restrictions.and(
				Restrictions.eq("ricTransfert.user.id", idUser),
				Restrictions.eq("ricTransfert.pagamentoEseguitoMedio", true));
		return getSession().createCriteria(RichiestaAutistaParticolare.class).add( crit1 ).createAlias("ricercaTransfert", "ricTransfert").list();
		*/
		/*
		String queryString = "SELECT * FROM data_agenzie_viaggio_bit WHERE json_extract(parametriSconto, '$."+Constants.CodiceScontoJSON+"') = :token ";
		AgenzieViaggioBit agenzieViaggioBit = (AgenzieViaggioBit) this.getSession()
				.createSQLQuery( queryString ).addEntity(AgenzieViaggioBit.class).setParameter("token", token).uniqueResult();
		return agenzieViaggioBit;
		 */
		String queryString = "SELECT RICH_PART.* "
				+ "FROM richiesta_autista_particolare RICH_PART, ricerca_transfert RIC "
				+ "WHERE RICH_PART.id_ricerca_transfert = RIC.id_ricerca_transfert "
				+ (idUser != null ? "AND RIC.id = :idUser " : "AND RIC.id_ricerca_transfert = :idRicercaTransfert ")
				+ "AND RIC.pagamentoEseguitoMedio = true "
				+ "AND json_extract(RIC.infoPasseggero, '$."+Constants.RichiestaAutistaParticolare_Id+"') = RICH_PART.id_richiesta_autista_particolare ";
				
		List<RichiestaAutistaParticolare> aa = null;
		if(idUser != null) {
			aa = (List<RichiestaAutistaParticolare>) this.getSession()
					.createSQLQuery( queryString ).addEntity(RichiestaAutistaParticolare.class).setParameter("idUser", idUser).list();
		}else if(idRicercaTransfert != null) {
			aa = (List<RichiestaAutistaParticolare>) this.getSession()
					.createSQLQuery( queryString ).addEntity(RichiestaAutistaParticolare.class).setParameter("idRicercaTransfert", idRicercaTransfert).list();
		}else {
			aa = null;
		}
		/*
		Query q = this.getSession().createSQLQuery( queryString );
		q.setParameter("idUser", idUser);
		*/
		return aa;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<RichiestaAutistaParticolare> getCorseClienteRichiestaAutistaParticolareALL(){
		Criterion crit1 = Restrictions.and(
				Restrictions.eq("invioSmsCorsaConfermata", true), Restrictions.eq("ricTransfert.pagamentoEseguitoMedio", true));
		return getSession().createCriteria(RichiestaAutistaParticolare.class).add( crit1 ).createAlias("ricercaTransfert", "ricTransfert").list();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	/**
	 * sembra strano ma lo faccio perché il pannello Cliente funziona passadogli RichiestaMediaAutista perché il pannello lo uso in condiviosione con l'autista
	 */
	public List<RicercaTransfert> getCorseClienteAgendaAutista(Long idUser, Long idRicercaTransfert) {
		String queryString = "SELECT RIC.* "
				+ "FROM ricerca_transfert RIC "
				+ "WHERE RIC.pagamentoEseguitoMedio = true "
				+ "AND RIC.tipoServizio like '"+Constants.SERVIZIO_AGENDA_AUTISTA+"' "
				+ (idUser != null ? "AND RIC.id = :idUser " : "AND RIC.id_ricerca_transfert = :idRicercaTransfert ");
		List<RicercaTransfert> aa = null;
		if(idUser != null) {
			aa = (List<RicercaTransfert>) this.getSession()
					.createSQLQuery( queryString ).addEntity(RicercaTransfert.class).setParameter("idUser", idUser).list();
		}else if(idRicercaTransfert != null) {
			aa = (List<RicercaTransfert>) this.getSession()
					.createSQLQuery( queryString ).addEntity(RicercaTransfert.class).setParameter("idRicercaTransfert", idRicercaTransfert).list();
		}else {
			aa = null;
		}
		return aa;
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	/**
	 * sembra strano ma lo faccio perché il pannello Cliente funziona passadogli RichiestaMediaAutista perché il pannello lo uso in condiviosione con l'autista
	 */
	public List<RichiestaMediaAutista> getCorseClienteRichiestaAutistaMedio(Long idUser, Long idRicercaTransfert) {
		Criterion crit1 = null;
		if(idUser != null && idRicercaTransfert == null) {
			crit1 = Restrictions.and(
					Restrictions.eq("RICERCA_TRANSFERT.user.id", idUser), Restrictions.eq("RICERCA_TRANSFERT.pagamentoEseguitoMedio", true),
					Restrictions.eq("RICHIESTA_MEDIA.classeAutoveicoloScelta", true));
			
		}else if(idUser == null && idRicercaTransfert != null) {
			crit1 = Restrictions.and(
					Restrictions.eq("RICERCA_TRANSFERT.id", idRicercaTransfert), Restrictions.eq("RICERCA_TRANSFERT.pagamentoEseguitoMedio", true),
					Restrictions.eq("RICHIESTA_MEDIA.classeAutoveicoloScelta", true));
		}else {
			crit1 = null;
		}
		List<Long> richMedio =  getSession().createCriteria(RichiestaMediaAutista.class).add( crit1 )
				.setProjection(Projections. distinct(Projections.property("RICERCA_TRANSFERT.id")))
				.createAlias("richiestaMedia", "RICHIESTA_MEDIA").createAlias("RICHIESTA_MEDIA.ricercaTransfert", "RICERCA_TRANSFERT").list();
		
		List<RichiestaMediaAutista> richiestaAutistaMedioList = new ArrayList<RichiestaMediaAutista>();
		for(Long richMedio_ite: richMedio){
			Criterion crit2 = Restrictions.and(
					Restrictions.eq("RICERCA_TRANSFERT.id", richMedio_ite),Restrictions.eq("RICHIESTA_MEDIA.classeAutoveicoloScelta", true));
			richiestaAutistaMedioList.add( (RichiestaMediaAutista) getSession().createCriteria(RichiestaMediaAutista.class)
					.createAlias("richiestaMedia", "RICHIESTA_MEDIA").createAlias("RICHIESTA_MEDIA.ricercaTransfert", "RICERCA_TRANSFERT")
					.add( crit2 ).list().get(0) );
		}
		return richiestaAutistaMedioList;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<RichiestaMediaAutista> getCorseClienteRichiestaAutistaMedioALL(){
		Criterion crit1 = Restrictions.and(Restrictions.eq("ricTransfert.pagamentoEseguitoMedio", true));
		return getSession().createCriteria(RichiestaMediaAutista.class).createAlias("richiestaMedia.ricercaTransfert", "ricTransfert")
				.add( crit1 ).list();
	}
	
	
	@Override
	@Transactional(readOnly = true)
	public RichiestaMediaAutista getCorseClienteRichAutistaMedioVerificaInvioSmsCorsaConfermata(long idRic){
		Criterion crit1 = Restrictions.and(
				Restrictions.eq("invioSmsCorsaConfermata", true), Restrictions.eq("ricTransfert.id", idRic), Restrictions.eq("ricTransfert.pagamentoEseguitoMedio", true));
		return (RichiestaMediaAutista) getSession().createCriteria(RichiestaMediaAutista.class).createAlias("richiestaMedia.ricercaTransfert", "ricTransfert")
				.add( crit1 ).uniqueResult();
	}
	
	
	//--------------------------------------------------------------
	//----- CONTROLLO CORSE SOVRAPPOSTE AUTISTA DISPONIBILE --------
	//--------------------------------------------------------------

	/**
	 * qui devo verificare se l'autista è occupato in altre corse che interferiscono con le date della corsa ricercata 
	 * e quindi l'autista deve escuderlo dagli autisti offerti
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<RichiestaMediaAutista> getCorseAutistaMedioConfermate_DateRange(Long idAutista, RicercaTransfert ricTransfertTest, long oreRange){
		Date dataPrelevamentoStart = new Date(ricTransfertTest.getDataOraPrelevamentoDate().getTime() - TimeUnit.HOURS.toMillis(oreRange));
		Date dataPrelevamentoEnd = new Date(ricTransfertTest.getDataOraPrelevamentoDate().getTime() + TimeUnit.HOURS.toMillis(oreRange));
		Criterion crit1 = Restrictions.and(
			// prendo tutti gli autisti medi perché devono essere tutti liberi per evenienza che quallo che ha confermato la corsa poi la disdice
			//Restrictions.eq("invioSmsCorsaConfermata", true),
			Restrictions.eq("autista.id", idAutista), 
			Restrictions.eq("ricTransfert.pagamentoEseguitoMedio", true),
			Restrictions.gt("ricTransfert.dataOraPrelevamentoDate", dataPrelevamentoStart),
			Restrictions.lt("ricTransfert.dataOraPrelevamentoDate", dataPrelevamentoEnd),
			// Se ho messo la corsa come non approvata allora significa che la corsa è stata cancellata e gli autisti sono quini tutti disponibili
			Restrictions.ne("ricTransfert.approvazioneAndata", Constants.NON_APPROVATA));
		Criterion crit2 = null;
		if(ricTransfertTest.getDataOraRitornoDate() != null){
			Date dataRitornoStart = new Date(ricTransfertTest.getDataOraRitornoDate().getTime() - TimeUnit.HOURS.toMillis(oreRange));
			Date dataRitornoEnd = new Date(ricTransfertTest.getDataOraRitornoDate().getTime() + TimeUnit.HOURS.toMillis(oreRange));
			crit2 = Restrictions.and( 	
					//Restrictions.eq("invioSmsCorsaConfermata", true),
					Restrictions.eq("autista.id", idAutista), 
					Restrictions.eq("ricTransfert.pagamentoEseguitoMedio", true),
					Restrictions.gt("ricTransfert.dataOraRitornoDate", dataRitornoStart),
					Restrictions.lt("ricTransfert.dataOraRitornoDate", dataRitornoEnd),
					Restrictions.ne("ricTransfert.approvazioneRitorno", Constants.NON_APPROVATA));
		}
		if(ricTransfertTest.getDataOraRitornoDate() != null){
			return getSession().createCriteria(RichiestaMediaAutista.class).add( Restrictions.or(crit1, crit2)).createAlias("richiestaMedia.ricercaTransfert", "ricTransfert")
	        		.addOrder(Order.asc("ricTransfert.dataOraPrelevamentoDate")).list();
		}else{
			return getSession().createCriteria(RichiestaMediaAutista.class).add( Restrictions.or(crit1)).createAlias("richiestaMedia.ricercaTransfert", "ricTransfert")
	        		.addOrder(Order.asc("ricTransfert.dataOraPrelevamentoDate")).list();
		}
	}
	
	
	/**
	 * qui devo verificare se l'autista è occupato in altre corse che interferiscono con le date della corsa ricercata 
	 * e quindi l'autista deve escuderlo dagli autisti offerti
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<RichiestaAutistaParticolare> getCorseAutistaParticolareConfermate_DateRange(Long idAutista, RicercaTransfert ricTransfertTest, long oreRange){
		Date dataPrelevamentoStart = new Date(ricTransfertTest.getDataOraPrelevamentoDate().getTime() - TimeUnit.HOURS.toMillis( oreRange ) );
		Date dataPrelevamentoEnd = new Date(ricTransfertTest.getDataOraPrelevamentoDate().getTime() + TimeUnit.HOURS.toMillis( oreRange ) );
		Criterion crit1 = Restrictions.and( 	
				Restrictions.eq("invioSmsCorsaConfermata", true),
				Restrictions.eq("auto.autista.id", idAutista), 
				Restrictions.gt("ricTransfert.dataOraPrelevamentoDate", dataPrelevamentoStart),
				Restrictions.lt("ricTransfert.dataOraPrelevamentoDate", dataPrelevamentoEnd),
				// Se ho messo la corsa come non approvata allora significa che la corsa è stata cancellata e gli autisti sono quini tutti disponibili
				Restrictions.ne("ricTransfert.approvazioneAndata", Constants.NON_APPROVATA));
		
		Criterion crit2 = null;
		if(ricTransfertTest.getDataOraRitornoDate() != null){
			Date dataRitornoStart = new Date(ricTransfertTest.getDataOraRitornoDate().getTime() - TimeUnit.HOURS.toMillis( oreRange ) );
			Date dataRitornoEnd = new Date(ricTransfertTest.getDataOraRitornoDate().getTime() + TimeUnit.HOURS.toMillis( oreRange ) );
			crit2 = Restrictions.and( 	
					Restrictions.eq("invioSmsCorsaConfermata", true),
					Restrictions.eq("auto.autista.id", idAutista), 
					Restrictions.gt("ricTransfert.dataOraRitornoDate", dataRitornoStart),
					Restrictions.lt("ricTransfert.dataOraRitornoDate", dataRitornoEnd),
					Restrictions.ne("ricTransfert.approvazioneRitorno", Constants.NON_APPROVATA));
		}
		if(ricTransfertTest.getDataOraRitornoDate() != null){
			return getSession().createCriteria(RichiestaAutistaParticolare.class).add( Restrictions.or(crit1, crit2))
					.createAlias("autoveicolo", "auto").createAlias("ricercaTransfert", "ricTransfert")
	        		.addOrder(Order.asc("ricTransfert.dataOraPrelevamentoDate")).list();
		}else{
			return getSession().createCriteria(RichiestaAutistaParticolare.class).add( Restrictions.or(crit1))
					.createAlias("autoveicolo", "auto").createAlias("ricercaTransfert", "ricTransfert")
	        		.addOrder(Order.asc("ricTransfert.dataOraPrelevamentoDate")).list();
		}
	}
	
	
	//--------------------------------------------------------------
	//--------------- QUERY DI TROVA AUTISTI TARIFFE ---------------
	//--------------------------------------------------------------
	
	
	// QUERY FIND INFRASTRUTTURA
	@Override
    @Transactional(readOnly = true)
	public Object getInfrastrutturaBy_PlaceId(String PlaceId){
		Criterion crit1 = Restrictions. eq("placeId", PlaceId) ;
		Object obj = getSession().createCriteria(Aeroporti.class)
				.add(crit1).uniqueResult();
		if(obj == null){
			obj = getSession().createCriteria(PortiNavali.class)
				.add(crit1).uniqueResult();
		}
		if(obj == null){
		obj = getSession().createCriteria(Musei.class)
				.add(crit1).uniqueResult();
		}
		return obj;
	}
	
	
	//--------------------------------------------------------------
	
	
	/**
	 * ORDINA LISTA INFRASTUTTRE BY LATITUDINE E LONGITUDINE
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Aeroporti> OrdinaAeroportiBy_Lat_Lng(double lat, double lng){
		/*
		String queryString = "SELECT annunci.idAnnuncio, annunci.immagine1, annunci.nomeImmagine FROM AnnunciCercaPadroneAdozione AS annunci "
    			+ "WHERE annunci.idAnnuncio IN (:listIDAnnunci) AND annunci.immagine1 IS NOT NULL AND annunci.nomeImmagine IS NOT NULL ";
		//NOTE - Here latitude = 37 & longitude = -122. So you just pass your own.
		String queryString1 = "SELECT distance.id, ( 3959 * acos( cos( radians( :lat ) ) * cos( radians( distance.lat ) ) * "+
				"cos( radians( distance.lng ) - radians( :lng ) ) + sin( radians( :lat ) ) * "+
				"sin( radians( distance.lat ) ) ) )  FROM Aeroporti AS distance HAVING distance < 25 ORDER BY distance LIMIT 0 , 20 ";
		String queryString2 = " 3956 * 2 * ASIN(SQRT(POWER(SIN((:lat - location.lat) * pi()/180 / 2), 2) + "+
				"COS(:lat * pi()/180) * COS(location.lat * pi()/180) * POWER(SIN((:lng - location.lng) * pi()/180 / 2), 2))) FROM Aeroporti AS location ";
		String hql = "Select newStatusId, max(processModifiedDate) from ProcessInstanceHistory where processInstance.processInstanceId=122 group by newStatusId having max(processModifiedDate) > 1";
		String queryString999 = "FROM Aeroporti ";

		// Closest within radius of 25 Miles
		// 37, -122 are your current coordinates
		// To search by kilometers instead of miles, replace 3959 with 6371
		String queryString3 = "SELECT id, "+
		 "( 3959 * acos( cos( radians(37) ) * cos( radians( lat ) ) "+
		  "* cos( radians( long ) - radians(-122) ) + sin( radians(37) ) "+ 
		  "* sin( radians( lat ) ) ) ) AS distance "+
		"FROM geo_features HAVING distance < 25 "+
		"ORDER BY distance LIMIT 1 ";
		*/
		
		/*
		 * QUESTA QUI FUNZIONA!!! VEDERE: https://stackoverflow.com/questions/16465779/sorting-mysql-query-by-latitude-longitude/16465874#16465874
		 */
		String queryString4 = "FROM Aeroporti AS distance ORDER BY (POW((distance.lng - :lng ),2) + POW((distance.lat - :lat ),2)) ";
    	Query query = getSession().createQuery(queryString4); 
    	query.setParameter("lat", lat);
    	query.setParameter("lng", lng);
    	//query.setMaxResults(15);
    	return  (List<Aeroporti>) query. list();

	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<PortiNavali> OrdinaPortiNavaliBy_Lat_Lng(double lat, double lng){
		String queryString4 = "FROM PortiNavali AS distance ORDER BY (POW((distance.lng - :lng ),2) + POW((distance.lat - :lat ),2))  ";
    	Query query = getSession().createQuery(queryString4); 
    	query.setParameter("lat", lat);
    	query.setParameter("lng", lng);
    	//query.setMaxResults(15);
    	return  (List<PortiNavali>) query. list();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Musei> OrdinaMuseiBy_Lat_Lng(double lat, double lng){
		String queryString4 = "FROM Musei AS distance ORDER BY (POW((distance.lng - :lng ),2) + POW((distance.lat - :lat ),2))  ";
    	Query query = getSession().createQuery(queryString4); 
    	query.setParameter("lat", lat);
    	query.setParameter("lng", lng);
    	//query.setMaxResults(15);
    	return  (List<Musei>) query. list();
	}
	
	
	// --------------------- INFRASTUTTURE --------------------------------------
	
	
	@Override
	@Transactional(readOnly = true)
	public Object getAeroporto_Musei_Porti_Province_LIKE_Url(String term){
		Criterion crit = Restrictions.like("url", term, MatchMode.EXACT) ;
		Object obj = getSession().createCriteria(Province.class).add(crit).uniqueResult();
		if(obj != null){
			return obj;
		}else{
			obj = getSession().createCriteria(Aeroporti.class).add(crit).uniqueResult();
			if(obj != null){
				return obj;
			}else{
				obj = getSession().createCriteria(Musei.class).add(crit).uniqueResult();
				if(obj != null){
					return obj;
				}else{
					obj = getSession().createCriteria(PortiNavali.class).add(crit).uniqueResult();
					return obj;
				}
			}
		}
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<AutistaAeroporti> Ricerca_Autisti_ServizioStandard_AEROPORTI(long idAeroporto){
		return getSession().createCriteria(AutistaAeroporti.class)
				.setProjection(Projections.projectionList()
						.add(Projections.property("id"), "id"))
				.setResultTransformer(Transformers.aliasToBean(AutistaAeroporti.class))
		.add(Restrictions.eq("aeroporti.id", idAeroporto )).add(Restrictions.eq("servizioAttivo", true)).list();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Tariffe> Ricerca_Tariffe_AUTISTA_ID_AEROPORTI(List<Long> listIdAeroporti, List<Long> listTipiAutoveicoli){
        return getSession().createCriteria(Tariffe.class).createAlias("autoveicolo", "auto").createAlias("autista", "autista")
			.add(Restrictions.in("autistaAeroporti.id", listIdAeroporti))
			.add(Restrictions.and( 
					Restrictions.gt("tariffeValori.tariffaAERO", BigDecimal.ZERO), 
					Restrictions.eq("autista.bannato", false),
					Restrictions.eq("autista.attivo", true),
					Restrictions. eq("auto.autoveicoloSospeso", false),
					Restrictions. eq("auto.autoveicoloCancellato", false),
					Restrictions. in("auto.tipoAutoveicolo.id", listTipiAutoveicoli))).list();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<AutistaPortiNavali> Ricerca_Autisti_ServizioStandard_PORTINAVALI(long idPorto){
		return getSession().createCriteria(AutistaPortiNavali.class)
			.setProjection(Projections.projectionList()
				.add(Projections.property("id"), "id"))
				.setResultTransformer(Transformers.aliasToBean(AutistaPortiNavali.class))
				.add(Restrictions.eq("portiNavali.id", idPorto )).add(Restrictions.eq("servizioAttivo", true)).list();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<AutistaMusei> Ricerca_Autisti_ServizioStandard_MUSEI(long idMuseo){
		List<AutistaMusei> list_1 = getSession().createCriteria(AutistaMusei.class)
		.add(Restrictions.eq("musei.id", idMuseo )).add(Restrictions.eq("servizioAttivo", true)).list();
		return list_1;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Tariffe> Ricerca_Tariffe_AUTISTA_ID_PORTINAVALI(List<Long> listIdporti, List<Long> listTipiAutoveicoli){
        return getSession().createCriteria(Tariffe.class).createAlias("autoveicolo", "auto").createAlias("autista", "autista")
        		.add(Restrictions.in("autistaPortiNavali.id", listIdporti))
        		.add( Restrictions.and(Restrictions.gt("tariffeValori.tariffaPORTO", BigDecimal.ZERO), 
        				Restrictions.eq("autista.bannato", false),
        				Restrictions.eq("autista.attivo", true),
        				Restrictions.eq("auto.autoveicoloSospeso", false),
        				Restrictions. eq("auto.autoveicoloCancellato", false),
        				Restrictions.in("auto.tipoAutoveicolo.id", listTipiAutoveicoli))).list();
	}
	
	
	// --------------------- ZONE -------------------------------------------------
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Object[]> AutistiZoneItalia_ServizioStandard_AUTO_HQL() {
		String Query = "SELECT PROV_, (select count(distinct AUTISTA) "
					+ "FROM Province PROV, AutistaZone ZONA, Autista AUTISTA "
					+ "WHERE PROV.id = PROV_.id "
					+ "AND PROV.id = ZONA.province.id "
					+ "AND ZONA.servizioAttivo = true "
					+ "AND ZONA.autista.id = AUTISTA.id "
					+ "AND AUTISTA.autistaDocumento.approvatoGenerale = true "
					+ "AND AUTISTA.attivo = true "
					+ "AND AUTISTA.bannato = false) AS CONTAS "
				+ "FROM Province PROV_ "
				//+ "ORDER BY PROV_.nomeProvincia ASC ";
				+ "ORDER BY PROV_.numeroAbitanti DESC ";
		Query q = this.getSession().createQuery( Query );
		return q.list();
	}
	
	
	/**
	 * TODO da continuare il discorso delle aziende per le richieste di corse con tanti passeggeri.
	 * Aggiungere la condizione solo azienda e autisti che hanno più van a disposizione 
	 * 
	 * vedere: https://stackoverflow.com/questions/25536868/criteria-distinct-root-entity-vs-projections-distinct
	 * https://developer.jboss.org/wiki/HibernateFAQ-AdvancedProblems?_sscc=t#jive_content_id_Why_does_Hibernate_always_initialize_a_collection_when_I_only_want_to_add_or_remove_an_element
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Object[]> Ricerca_Autisti_ServizioStandard_AUTO_HQL(long idComPart, long idProvPart, long idRegPart,
			long idComArr, long idProvArr, long idRegArr, List<Long> provinceInfra, int numeroPasseggeri, boolean soloAzienda, 
			boolean ritorno, Date dataOraPrelevamento, Date dataOraRitorno) {
String Query_NEW = "SELECT AUTO.id_autoveicolo, AUTO.annoImmatricolazione, AUTISTA.id_autista, AUTISTA.numCorseEseguite, USER.first_name, USER.last_name, USER.phone_number, "
+ "CLASSE_AUTOVEICOLO.id_classe_autoveicolo, NUM_POSTI_AUTO.numero, PROV.tariffaBase, PROV.siglaProvincia, MARCA_AUTOSCOUT.name AS marcaName, "
+ "MODELLO_AUTOSCOUT.name AS modelloName "
+ "FROM autoveicolo AUTO INNER JOIN autista AUTISTA ON AUTO.id_autista = AUTISTA.id_autista "
+ "INNER JOIN zona ZONA ON AUTISTA.id_autista = ZONA.id_autista "
+ "INNER JOIN data_modello_auto_numero_posti MODEL_AUTO_NUM_POSTI ON AUTO.id_modello_auto_numero_posti = MODEL_AUTO_NUM_POSTI.id_modello_auto_numero_posti "
	+ "AND AUTO.id_modello_auto_numero_posti = MODEL_AUTO_NUM_POSTI.id_modello_auto_numero_posti "
+ "INNER JOIN data_numero_posti_auto NUM_POSTI_AUTO ON MODEL_AUTO_NUM_POSTI.id_numero_posti_auto = NUM_POSTI_AUTO.id_numero_posti_auto "
+ "INNER JOIN app_user USER ON AUTISTA.id_user = USER.id "
+ "INNER JOIN data_modello_autoscout MODELLO_AUTOSCOUT ON MODEL_AUTO_NUM_POSTI.id_modello_autoscout = MODELLO_AUTOSCOUT.id_modello_autoscout "
+ "INNER JOIN data_marca_autoscout MARCA_AUTOSCOUT ON MODELLO_AUTOSCOUT.id_marca_auto = MARCA_AUTOSCOUT.id_marca_autoscout "
+ "INNER JOIN data_classe_autoveicolo CLASSE_AUTOVEICOLO ON MODELLO_AUTOSCOUT.id_classe_autoveicolo = CLASSE_AUTOVEICOLO.id_classe_autoveicolo "
+ "INNER JOIN data_province PROV ON ZONA.id_provincia = PROV.id_provincia "
+ "WHERE AUTISTA.approvatoGenerale = true "
+ "AND AUTISTA.attivo = true "
+ "AND AUTISTA.bannato = false "
+ (soloAzienda == false ?  "" : "AND AUTISTA.azienda = true ")
+ "AND AUTO.approvatoCartaCircolazione = true "
+ "AND AUTO.autoveicolo_sospeso = false "
+ "AND AUTO.autoveicolo_cancellato = false "
+ "AND NUM_POSTI_AUTO.numero > :numeroPasseggeri "
+ "AND ( "
	+ "(ZONA.id_provincia = :idProvPart OR ZONA.id_provincia = :idProvArr) "
	+ (provinceInfra != null && !provinceInfra.isEmpty() ? "OR (ZONA.id_provincia IN (:provinceInfra)) " : "")
+ ") "
+ "AND ZONA.servizioAttivo = true "
//------------- CONTROLLO AUTOVEICOLO CORSE SOVRAPPOSTE ST -------------------------------
+ "AND AUTO.id_autoveicolo NOT IN ( SELECT RIC_MED_AUT_AUTO.id_autoveicolo "

//+ "FROM richiesta_media_autista_autoveicolo RIC_MED_AUT_AUTO, richiesta_media_autista RIC_MED_AUT, richiesta_media RIC_MED, ricerca_transfert RIC_2 "
+ "FROM ricerca_transfert RIC_2 INNER JOIN richiesta_media RIC_MED ON RIC_MED.id_ricerca_transfert = RIC_2.id_ricerca_transfert "
+ "INNER JOIN richiesta_media_autista RIC_MED_AUT ON RIC_MED_AUT.id_richiesta_media = RIC_MED.id_richiesta_media "
+ "INNER JOIN richiesta_media_autista_autoveicolo RIC_MED_AUT_AUTO ON RIC_MED_AUT_AUTO.id_richiesta_media_autista = RIC_MED_AUT.id_richiesta_media_autista "
+ "WHERE RIC_MED_AUT.corsaConfermata = true "
	+ SQL_ControllaCorsaSovrapposta_ST_PART(ritorno)
+ ") "
//------------- CONTROLLO AUTOVEICOLO CORSE SOVRAPPOSTE PART -------------------------------
+ "AND AUTO.id_autoveicolo NOT IN ( SELECT RIC_PART.id_autoveicolo "
+ "FROM ricerca_transfert RIC_2 INNER JOIN richiesta_autista_particolare RIC_PART ON RIC_PART.id_ricerca_transfert = RIC_2.id_ricerca_transfert "
+ "WHERE json_extract(RIC_2.infoPasseggero, '$."+Constants.RichiestaAutistaParticolare_Id+"') IS NOT NULL "
+ "AND RIC_PART.id_richiesta_autista_particolare = json_extract(RIC_2.infoPasseggero, '$."+Constants.RichiestaAutistaParticolare_Id+"') "
	+ SQL_ControllaCorsaSovrapposta_ST_PART(ritorno)
+ ") "
+ "ORDER BY AUTISTA.numCorseEseguite ASC, USER.signupDate ASC  ";		
long startTime = System.nanoTime(); 
Query q = this.getSession().createSQLQuery( Query_NEW ).setParameter("idProvPart", idProvPart).setParameter("idProvArr", idProvArr)
.setParameter("numeroPasseggeri", numeroPasseggeri).setParameter("oraPrelev", dataOraPrelevamento);
if( ritorno == true ) { q.setParameter("dataOraRit", dataOraRitorno); }
if( provinceInfra != null && !provinceInfra.isEmpty() ) { q.setParameterList("provinceInfra", provinceInfra); }
List<Object[]> zz = q.list();
DammiTempoOperazione.DammiSecondi(startTime, "Ricerca_Autisti_ServizioStandard_AUTO_HQL-1");
return zz;
	}
	

	
private static String SQL_ControllaCorsaSovrapposta_ST_PART(boolean ritorno) {
final String MoltDurTraff = "2.2"; 
return "AND ( "
		+ "RIC_2.approvazioneAndata = "+Constants.IN_APPROVAZIONE+" "
		+ "AND (:oraPrelev >= RIC_2.dataOraPrelevamentoDate AND :oraPrelev < DATE_ADD(RIC_2.dataOraPrelevamentoDate, INTERVAL durataConTrafficoValue * "+MoltDurTraff+" second)) "
		+ "OR ( (:oraPrelev <= RIC_2.dataOraPrelevamentoDate AND DATE_ADD(:oraPrelev, INTERVAL durataConTrafficoValue * "+MoltDurTraff+" second) > RIC_2.dataOraPrelevamentoDate) ) "
		+ (ritorno == true ? 
			"OR( (:dataOraRit >= RIC_2.dataOraPrelevamentoDate AND :dataOraRit < DATE_ADD(RIC_2.dataOraPrelevamentoDate, INTERVAL durataConTrafficoValue * "+MoltDurTraff+" second)) "
			+ "OR (:dataOraRit <= RIC_2.dataOraPrelevamentoDate AND DATE_ADD(:dataOraRit, INTERVAL durataConTrafficoValue * "+MoltDurTraff+" second) > RIC_2.dataOraPrelevamentoDate) "
			+ ") " : "")
		+ "OR ( "
			+ "RIC_2.ritorno = true AND RIC_2.approvazioneRitorno = "+Constants.IN_APPROVAZIONE+" "
			+ "AND (:oraPrelev >= RIC_2.dataOraRitornoDate AND :oraPrelev < DATE_ADD(RIC_2.dataOraRitornoDate, INTERVAL durataConTrafficoValue * "+MoltDurTraff+" second)) "
			+ "OR ( (:oraPrelev <= RIC_2.dataOraRitornoDate AND DATE_ADD(:oraPrelev, INTERVAL durataConTrafficoValue * "+MoltDurTraff+" second) > RIC_2.dataOraRitornoDate) ) "
			+ (ritorno == true ? 
				"OR( (:dataOraRit >= RIC_2.dataOraRitornoDate AND :dataOraRit < DATE_ADD(RIC_2.dataOraRitornoDate, INTERVAL durataConTrafficoValue * "+MoltDurTraff+" second)) "
				+ "OR (:dataOraRit <= RIC_2.dataOraRitornoDate AND DATE_ADD(:dataOraRit, INTERVAL durataConTrafficoValue * "+MoltDurTraff+" second) > RIC_2.dataOraRitornoDate) "
				+ ") " : "")
		+ ") "
	+ ") ";
}
	
	
	/**
	 * Questo controllo è inutile se non permetto la gestione Disponibilità Date (Calendario Ferie) all'autista
	 */
	@Override
	@Transactional(readOnly = true)
	public boolean Autoveicolo_Disponibe(long autoveicolo, List<Date> datePrelevRitorn){
        Long rowCount = (Long) getSession().createCriteria(Disponibilita.class, "disp").createAlias("disp.disponibilitaDate", "dispDate")
        		.setProjection(Projections.rowCount())
        		.add(Restrictions.and(
					Restrictions.eq("autoveicolo.id", autoveicolo),
					Restrictions. in("dispDate.data", datePrelevRitorn))).uniqueResult();
        if(rowCount != null && rowCount > 0){
        	return false;
        }else{
        	return true;
        }
	}
	
	
	// --------------------- TARIFFE -------------------------------------------------
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Tariffe> Ricerca_Tariffe_AUTISTA_ID_ZONE(List<Long> listIdZone, List<Long> listTipiAutoveicoli){
        return getSession().createCriteria(Tariffe.class).createAlias("autoveicolo", "auto").createAlias("autista", "autista")									
    		.add(Restrictions.in("autistaZone.id", listIdZone))
    		.add( Restrictions.and(
				//Restrictions.gt("tariffeValori.tariffaST", BigDecimal.ZERO), 
				Restrictions.eq("autista.autistaDocumento.approvatoGenerale", true),
				Restrictions.eq("autista.attivo", true),
				Restrictions.eq("autista.bannato", false),
				Restrictions.eq("auto.autoveicoloCartaCircolazione.approvatoCartaCircolazione", true),
				Restrictions.eq("auto.autoveicoloSospeso", false),
				Restrictions.eq("auto.autoveicoloCancellato", false)
				//Restrictions.in("auto.tipoAutoveicolo.id", listTipiAutoveicoli)
			)).list();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Tariffe> Ricerca_Tariffe_AUTISTA_ID_ZONE_LP(List<Long> listIdZone, List<Long> listTipiAutoveicoli, int max_km_lp){
        return getSession().createCriteria(Tariffe.class).createAlias("autoveicolo", "auto").createAlias("autista", "autista")
    		.add(Restrictions.in("autistaZone.id", listIdZone))
    		.add(Restrictions.and(
				//Restrictions.gt("tariffeValori.tariffaLP", BigDecimal.ZERO),
				//Restrictions.ge("tariffeValori.km_max_LP", max_km_lp),
				Restrictions.eq("autista.autistaDocumento.approvatoGenerale", true),
				Restrictions.eq("autista.attivo", true),
				Restrictions.eq("autista.bannato", false),
				Restrictions.eq("auto.autoveicoloCartaCircolazione.approvatoCartaCircolazione", true),
				Restrictions.eq("auto.autoveicoloSospeso", false),
				Restrictions.eq("auto.autoveicoloCancellato", false)
				//Restrictions.in("auto.tipoAutoveicolo.id", listTipiAutoveicoli)
    		)).list();
	}

	
	//-------------------------------------
	
	@Override
    @Transactional(readOnly = true)
	public RicercaTransfert get(Long id){
		RicercaTransfert ricercaTransfert = (RicercaTransfert) getSession().get(RicercaTransfert.class, id);
		return ricercaTransfert;
	}
	
	
	@Override
    @Transactional(readOnly = true)
	public RicercaTransfert getCollapsePanelCorsaAdmin(Long id){
		RicercaTransfert ricTransfert = (RicercaTransfert) getSession().createCriteria(RicercaTransfert.class)
				.setProjection(Projections.projectionList()
						.add(Projections.property("collapsePanelCorseAdmin"), "collapsePanelCorseAdmin"))
							.setResultTransformer(Transformers.aliasToBean(RicercaTransfert.class))
								.add(Restrictions.eq("id", id )).uniqueResult();
		return ricTransfert;
	}
	
	
	@Transactional
	@Override
	public int updateCollapsePanelCorsaAdmin(long id, boolean collapse) {
		String query = "update RicercaTransfert set collapsePanelCorseAdmin = :collapse WHERE id = :id ";
	    Query setQuery = getSession().createQuery(query);
	    setQuery.setParameter("id", id);
	    setQuery.setParameter("collapse", collapse);

		return setQuery.executeUpdate();
	}
	
	
	@Transactional
	@Override
	public int updateApprovazioneCorsaAndataAdmin(long id, int approv) {
		String query = "update RicercaTransfert set approvazioneAndata = :approv WHERE id = :id ";
	    Query setQuery = getSession().createQuery(query);
	    setQuery.setParameter("id", id);
	    setQuery.setParameter("approv", approv);

		return setQuery.executeUpdate();
	}
	
	@Transactional
	@Override
	public int updateApprovazioneCorsaRitornoAdmin(long id, int approv) {
		String query = "update RicercaTransfert set approvazioneRitorno = :approv WHERE id = :id ";
	    Query setQuery = getSession().createQuery(query);
	    setQuery.setParameter("id", id);
	    setQuery.setParameter("approv", approv);
		return setQuery.executeUpdate();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<RicercaTransfert> getRicercaTransfertBy_LIKE(String term){
		Criterion crit1 = Restrictions.or(
			Restrictions.like("formattedAddress_Partenza", "%"+term+"%", MatchMode.END), Restrictions.like("formattedAddress_Arrivo", "%"+term+"%", MatchMode.END),
				Restrictions.eq("id", StringUtils.isNumeric(term) ? Long.parseLong(term) : null ) );
		return getSession().createCriteria(RicercaTransfert.class).add(crit1).addOrder(Order.desc("id")).list();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<RicercaTransfert> getRicercaTransfert() {
        return getSession().createCriteria(RicercaTransfert.class).addOrder(Order.desc("id")).list();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<RicercaTransfert> getRicercaTransfert_idPaymentProvider_NotNull() {
        return getSession().createCriteria(RicercaTransfert.class).add( Restrictions.isNotNull("idPaymentProvider") ).addOrder(Order.desc("id")).list();
	}
	
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public List<RicercaTransfert> getRicercaTransfertSoloRicercheEseguiteCliente(int maxResults, Integer firstResult){
		Criteria criteria = getSession().createCriteria(RicercaTransfert.class, "RIC")
			.setProjection(Projections.projectionList()
					.add(Projections.property("id"), "id")
					.add(Projections.property("visitatore"), "visitatore")
					.add(Projections.property("dataRicerca"), "dataRicerca")
					.add(Projections.property("partenzaRequest"), "partenzaRequest")
					.add(Projections.property("formattedAddress_Partenza"), "formattedAddress_Partenza")
					.add(Projections.property("dataOraPrelevamentoDate"), "dataOraPrelevamentoDate")
					.add(Projections.property("arrivoRequest"), "arrivoRequest")
					.add(Projections.property("formattedAddress_Arrivo"), "formattedAddress_Arrivo")
					.add(Projections.property("dataOraRitornoDate"), "dataOraRitornoDate")
					.add(Projections.property("numeroPasseggeri"), "numeroPasseggeri")
					.add(Projections.property("distanzaValue"), "distanzaValue")
					.add(Projections.property("distanzaValueRitorno"), "distanzaValueRitorno")
					.add(Projections.property("tipoServizio"), "tipoServizio")
					.add(Projections.property("user"), "user")).
					setResultTransformer(Transformers.aliasToBean(RicercaTransfert.class))
			.setFirstResult(firstResult).setMaxResults(maxResults)
			.addOrder(Order.desc("id"));
		return criteria.list();
	}
	
	
	// TODO ricordati di cancellarlo
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public List<RicercaTransfert> getRicercaTransfertSoloRicercheEseguiteCliente_PROVA_TEST(int maxResults, Integer firstResult){
		return getSession().createCriteria(RicercaTransfert.class, "RIC")
			.setProjection(Projections.distinct(Projections.property("distanzaValue")))
			.setFirstResult(firstResult).setMaxResults(maxResults)
			.addOrder(Order.desc("id")).list();
	}
	
	
	
	// TODO ricordati di cancellarlo
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public List<RichiestaMedia> getRicercaTransfertSoloRicercheEseguiteCliente_PROVA_TEST_2(int maxResults, Integer firstResult, 
			ClasseAutoveicolo classeAutoveicoloReale, String NomeProvincia){
		Criterion crit2 = Restrictions.and(
				//Restrictions.gt("tariffaPerKm", new BigDecimal(0.40))
				//,Restrictions.lt("tariffaPerKm", new BigDecimal(2.90))
				Restrictions.eq("classeAutoveicolo.id", classeAutoveicoloReale.getId())
				//,Restrictions.gt("RIC_TRANSFERT.distanzaValue", 96000l)
				//,Restrictions.lt("RIC_TRANSFERT.distanzaValue", 98000l)
				,Restrictions.and(
						Restrictions.or(
						//Restrictions.eq("RIC_TRANSFERT.comune_Arrivo", NomeProvincia),
						Restrictions.eq("RIC_TRANSFERT.comune_Partenza", NomeProvincia))
				)
				//,Restrictions.and(Restrictions.eq("RIC_TRANSFERT.id", 5892l))
				/*
				,Restrictions.and(
						Restrictions.or(
						Restrictions.eq("RIC_TRANSFERT.id", 6026l),
						Restrictions.eq("RIC_TRANSFERT.id", 5962l),
						Restrictions.eq("RIC_TRANSFERT.id", 5963l),
						Restrictions.eq("RIC_TRANSFERT.id", 5964l),
						Restrictions.eq("RIC_TRANSFERT.id", 5965l))
				)
				*/
				,Restrictions.and(
					Restrictions.gt("RIC_TRANSFERT.distanzaValue", 10000l)
					,Restrictions.lt("RIC_TRANSFERT.distanzaValue", 11000l)
				)
				
				);
		return getSession().createCriteria(RichiestaMedia.class)
			.createAlias("ricercaTransfert", "RIC_TRANSFERT").add(crit2)
			.setMaxResults(maxResults).setFirstResult(firstResult)
			.addOrder(Order.asc("RIC_TRANSFERT.distanzaValue")).list();
	}

	
	@Transactional(readOnly = true)
	@Override
	public int getCountRicercaTransfertSoloRicercheEseguiteCliente(){
		return (int)(long)getSession().createCriteria(RicercaTransfert.class).setProjection(Projections.rowCount()).uniqueResult();
	}
	
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public List<RicercaTransfert> getRicercaTransfertVenduti(Integer maxResult) {
		if(maxResult != null){
			return getSession().createCriteria(RicercaTransfert.class)
					.add(Restrictions.isNotNull("user")).addOrder(Order.desc("id")).setMaxResults(maxResult).list();
		}else{
			return getSession().createCriteria(RicercaTransfert.class)
					.add(Restrictions.isNotNull("user")).addOrder(Order.desc("id")).list();
		}
		
	}
	
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public List<RicercaTransfert> getRicercaTransfertRicercatiInProvinciaAutista(List<String> listSiglaProvincieAutista) {
		Criterion crit = Restrictions.or(
				Restrictions.in("siglaProvicia_Partenza", listSiglaProvincieAutista),
				Restrictions.in("siglaProvicia_Arrivo", listSiglaProvincieAutista));
		Criteria criteria = getSession().createCriteria(RicercaTransfert.class)
			.add( crit ).add(Restrictions.isNotNull("dataRicerca")).addOrder(Order.desc("id")).setMaxResults( 10 );
		return criteria.list();
	}
	
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public List<RicercaTransfert> UtimerRicercheTransfert() {
		Criteria criteria = getSession().createCriteria(RicercaTransfert.class)
			.add(Restrictions.isNotNull("dataRicerca")).addOrder(Order.desc("id")).setMaxResults( 10 );
		return criteria.list();
	}
	
	
	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public List<RicercaTransfert> getAll() {
		return getSession().createCriteria(RicercaTransfert.class).list();
	}
	
	
	@Transactional
	@Override
	public RicercaTransfert saveRicercaTransfert(RicercaTransfert ricercaTransfert) throws DataIntegrityViolationException, HibernateJdbcException {
		getSession().saveOrUpdate(ricercaTransfert);
		//getSession().flush();
		return ricercaTransfert;
	}
	
	
	@Transactional
	@Override
	public RicercaTransfert deleteRicercaTransfert(long IdRicercaTransfert) throws Exception {
		RicercaTransfert ricercaTransfert = (RicercaTransfert) getSession().get(RicercaTransfert.class, IdRicercaTransfert);
		getSession().delete( ricercaTransfert );
		//getSession().flush();
		
		return ricercaTransfert;
	}
	

	

}
