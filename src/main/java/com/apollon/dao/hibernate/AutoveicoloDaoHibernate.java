package com.apollon.dao.hibernate;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.apollon.Constants;
import com.apollon.dao.AutoveicoloDao;
import com.apollon.model.AgA_Giornate;
import com.apollon.model.AgA_ModelliTariffari;
import com.apollon.model.AgA_Tariffari;
import com.apollon.model.Autista;
import com.apollon.model.Autoveicolo;
import com.apollon.model.BackupInfoUtente;
import com.apollon.model.ClasseAutoveicolo;
import com.apollon.model.MarcaAutoScout;
import com.apollon.model.ModelloAutoNumeroPosti;
import com.apollon.model.ModelloAutoScout;
import com.apollon.util.DateUtil;
import com.apollon.webapp.listener.StartupListener;
import com.apollon.webapp.rest.AgA_General;
import com.apollon.webapp.util.bean.AgendaAutista_Autista;
import com.apollon.webapp.util.controller.autoveicolo.AutoveicoloUtil;
import com.apollon.webapp.util.controller.home.HomeUtil_Aga;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@EnableTransactionManagement
@Repository("AutoveicoloDao")
public class AutoveicoloDaoHibernate extends GenericDaoHibernate<Autoveicolo, Long> implements AutoveicoloDao {
	private static final Log log = LogFactory.getLog(AutoveicoloDaoHibernate.class);

	public AutoveicoloDaoHibernate() {
		super(Autoveicolo.class);
	}
	
	
	@Transactional(readOnly = true)
	@Override
	public Autoveicolo get(Long id){
		Autoveicolo autoveicolo = (Autoveicolo) getSession().get(Autoveicolo.class, id);
		return autoveicolo;
	}
	
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public List<Autoveicolo> getAutoveicolo() {
        return getSession().createCriteria(Autoveicolo.class).list();
	}
	
	
	@Transactional(readOnly = true)
	@Override
	/**
	 * MOLTO IMPORTANTE PER OTTENERE RISULTATI QUANDO C'è IL LAZY IMPOSTATO
	 * SI PUò FARE SIA CON HQL CHE CON CRITERIA, BASTA AGGIUNGERE IL JOIN FETCH PER IL PRIMO CASO O IL setFetchMode PER IL SECONDO CASO
	 */
	public Autoveicolo getConInfoAutista(Long idAuto){
		/*
		String query = "select auto from Autoveicolo auto JOIN FETCH auto.autista WHERE auto.id = :idAuto";
		Query q = this.getSession().createQuery(query);
		return (Autoveicolo) q.setParameter("idAuto", idAuto).uniqueResult();
		*/
		Criteria criteria =  getSession().createCriteria(Autoveicolo.class)
				.setFetchMode("autista", FetchMode.JOIN).add( Restrictions.eq("id", idAuto) );
		return (Autoveicolo) criteria.uniqueResult();
	}
	

	@Transactional(readOnly = true)
    @Override
	public Autoveicolo getAutoveicolo_By_Targa(String targa){
		Criterion crit = Restrictions.eq("targa", targa);
        return (Autoveicolo) getSession().createCriteria(Autoveicolo.class).add(crit).uniqueResult(); 
	}
	
	
	@Transactional
	@Override
	public boolean ControlloAutoveicoliSospesiMenu(long idAutista) {
		Long countAutoSospese =  (Long) getSession().createCriteria(Autoveicolo.class)
					.setProjection(Projections.rowCount())
					.add( Restrictions.and(
						Restrictions.eq("autista.id", idAutista), 
						Restrictions.eq("autoveicoloCancellato", false), 
						Restrictions.eq("autoveicoloSospeso", false)) 
				).uniqueResult();
		if(countAutoSospese != null && countAutoSospese > 0){
			return true;
		}else{
			return false;
		}
	}
	
	
	@Transactional
	@Override
	public boolean autoveicoliPresentiNonCancellati(long idAutista) {
		Criterion criterion = Restrictions. and( 
				Restrictions.eq("autista.id", idAutista),
				Restrictions.eq("autoveicoloCancellato", false));
		Long counAutoveicoli = (Long) getSession().createCriteria(Autoveicolo.class)
				.setProjection(Projections.rowCount())
				.add(criterion).uniqueResult();
		if(counAutoveicoli != null && counAutoveicoli > 0){
			return true;
		}else{
			return false;
		}
	}
	

	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	/**
	 * 	Mi ritorna una lista di dati Object composta di Object[]: Autoveicolo, Long Autista id, Int Autista percentualeServizio
	 */
	public List<Object> getAutoveicoliCalcoloTariffe_percentualeAutistaList(long idUser) {
		String queryString4 = "SELECT c, c.autista.id, c.autista.percentualeServizio FROM Autoveicolo c WHERE c.autista.user.id = :id  ";
		Query query = getSession().createQuery(queryString4);
		// Questo funziona solo quando nel SELECT c'è un solo tipo di Oggetto.
		// Query query = getSession().createQuery(queryString4).setResultTransformer(Transformers.aliasToBean(Autoveicolo.class)); 
		query.setParameter("id", idUser);
		List<Object> list = query. list();
		/*
		for(Object ite: aa){
			Object[] we = (Object[]) ite;
			Autoveicolo autov = (Autoveicolo)we[0];
		}
		*/
		return list;
	}
	
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public List<Autoveicolo> getAutoveicolo_by_OrderModelloAutoScout() {
        return getSession().createCriteria(Autoveicolo.class).createAlias("modelloAutoNumeroPosti", "modelloAutoNumeroPosti")
        		.addOrder(Order.desc("autoveicoloCartaCircolazione.approvatoCartaCircolazione"))
        		.addOrder(Order.asc("modelloAutoNumeroPosti.modelloAutoScout"))
        		.addOrder(Order.asc("annoImmatricolazione"))
        		.list();
	}
	
	
	/**
	 * visualizzaAutoCancellate TRUE: include anche le auto cancellate, FALSE: esclude le auto cancellate
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public List<Autoveicolo> getAutoveicoloByAutista(long idAutista, boolean visualizzaAutoCancellate) {
		Criterion crit;
		if(visualizzaAutoCancellate){
			crit = Restrictions.eq("autista.id", idAutista);
		}else{ 
			crit = Restrictions.and(
				Restrictions.eq("autista.id", idAutista),Restrictions.eq("autoveicoloCancellato", false) );
		}
        return getSession().createCriteria(Autoveicolo.class)
        		.add(crit).addOrder(Order.asc("autoveicoloCancellato")).addOrder(Order.asc("id")).list(); 
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public List<Autoveicolo> getAutoveicoloByAutista_Agenda(long idAutista) {
		String query = "SELECT "
				+ "AUTO.id_autoveicolo, "
				+ "MARCA_AUTOSCOUT.name AS marcaName, "
				+ "MODELLO_AUTOSCOUT.name AS modelloName, "
				+ "AUTO.targa, "
				+ "AUTO.annoImmatricolazione, "
				+ "CLASSE_AUTOVEICOLO.id_classe_autoveicolo "
				
		+ "FROM autista AUTISTA, autoveicolo AUTO, data_modello_auto_numero_posti MODEL_AUTO_NUM_POSTI, data_modello_autoscout MODELLO_AUTOSCOUT, "
		+ "data_marca_autoscout MARCA_AUTOSCOUT, data_classe_autoveicolo CLASSE_AUTOVEICOLO "
		+ "WHERE "
		+ "AUTISTA.id_autista = :idAutista AND AUTISTA.id_autista = AUTO.id_autista "
		+ "AND AUTO.approvatoCartaCircolazione = true AND AUTO.autoveicolo_sospeso = false AND AUTO.autoveicolo_cancellato = false "
		+ "AND AUTO.id_modello_auto_numero_posti = MODEL_AUTO_NUM_POSTI.id_modello_auto_numero_posti "
		+ "AND MODEL_AUTO_NUM_POSTI.id_modello_autoscout = MODELLO_AUTOSCOUT.id_modello_autoscout "
		+ "AND MODELLO_AUTOSCOUT.id_marca_auto = MARCA_AUTOSCOUT.id_marca_autoscout "
		+ "AND MODELLO_AUTOSCOUT.id_classe_autoveicolo = CLASSE_AUTOVEICOLO.id_classe_autoveicolo ";
				
		List<Object[]> result = this.getSession().createSQLQuery( query ).setParameter("idAutista", idAutista).list();
		
		List<Autoveicolo> listAutoveicolo = new ArrayList<Autoveicolo>();
		for(Object[] ite : result) {
			MarcaAutoScout marcaAutoScout = new MarcaAutoScout(); marcaAutoScout.setName( (String)ite[1] );
			ClasseAutoveicolo classeAutoveicolo = new ClasseAutoveicolo(); classeAutoveicolo.setId( ((BigInteger)ite[5]).longValue() );
			ModelloAutoScout modelloAutoScout = new ModelloAutoScout(); modelloAutoScout.setClasseAutoveicolo(classeAutoveicolo); 
				modelloAutoScout.setMarcaAutoScout(marcaAutoScout); modelloAutoScout.setName( (String)ite[2] );
			ModelloAutoNumeroPosti modelloAutoNumeroPosti = new ModelloAutoNumeroPosti(); modelloAutoNumeroPosti.setModelloAutoScout(modelloAutoScout);
			Autoveicolo auto = new Autoveicolo(); auto.setId( ((BigInteger)ite[0]).longValue() ); auto.setTarga( (String)ite[3] ); auto.setAnnoImmatricolazione( (String)ite[4] );
				auto.setModelloAutoNumeroPosti(modelloAutoNumeroPosti); auto.getAutoveicoloCartaCircolazione().setApprovatoCartaCircolazione(true);
					auto.setAutoveicoloCancellato(false); auto.setAutoveicoloSospeso(false);
			listAutoveicolo.add(auto);
		}
		return listAutoveicolo;
	}
	
	/*
	+ "AND (AUTO.approvatoCartaCircolazione = true OR AUTO.id_autoveicolo = 2 OR AUTO.id_autoveicolo = 4) " // TODO ricordati di togliere questa auto (CIRO PASQUALE) (ADMIN)
	+ "AND AUTISTA.bannato = false "
	+ "AND AUTO.autoveicolo_sospeso = false AND AUTO.autoveicolo_cancellato = false "
	+ "AND AUTO.id_modello_auto_numero_posti = MODEL_AUTO_NUM_POSTI.id_modello_auto_numero_posti "
	+ "AND MODEL_AUTO_NUM_POSTI.id_modello_autoscout = MODELLO_AUTOSCOUT.id_modello_autoscout "
	+ "AND MODELLO_AUTOSCOUT.id_marca_auto = MARCA_AUTOSCOUT.id_marca_autoscout "
	+ "AND MODELLO_AUTOSCOUT.id_classe_autoveicolo = CLASSE_AUTOVEICOLO.id_classe_autoveicolo "
	*/
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Autoveicolo> getAutovecoloList_like_NomeMarca(String term){
		Criterion criterion = Restrictions.like("CCC.name", "%"+term+"%", MatchMode.ANYWHERE);
		
		return getSession().createCriteria(Autoveicolo.class).createAlias("modelloAutoNumeroPosti", "AAA")
				.createAlias("AAA.modelloAutoScout", "BBB").createAlias("BBB.marcaAutoScout", "CCC")
				.add(criterion).addOrder(Order.asc("CCC.name")).addOrder(Order.asc("BBB.name")).list();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Autoveicolo> getAutovecoloList_like_NomeModello(String term){
		Criterion criterion = Restrictions.like("BBB.name", "%"+term+"%", MatchMode.ANYWHERE);
		return getSession().createCriteria(Autoveicolo.class).createAlias("modelloAutoNumeroPosti", "AAA")
				.createAlias("AAA.modelloAutoScout", "BBB").createAlias("BBB.marcaAutoScout", "CCC")
				.add(criterion).addOrder(Order.asc("CCC.name")).addOrder(Order.asc("BBB.name")).list();
	}
	
	
	@Transactional
	@Override
	public Autoveicolo saveAutoveicolo(Autoveicolo autoveicolo) throws DataIntegrityViolationException, HibernateJdbcException {
		getSession().saveOrUpdate(autoveicolo);
		BackupInfoUtente backup = new BackupInfoUtente(autoveicolo, new Date(), autoveicolo.getModelloAutoNumeroPosti().getModelloAutoScout().getMarcaAutoScout().getName(), 
				autoveicolo.getModelloAutoNumeroPosti().getModelloAutoScout().getName(), autoveicolo.getTarga(), autoveicolo.getAnnoImmatricolazione());
        getSession().save(backup);
		return autoveicolo;
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<Object[]>Result_AgendaAutista_TEST(Date dataOraPrelevamento, long durataConTrafficoValue) {
		String queryString_1 = "SELECT GIORNATE_2.id_aga_giornate, GIORNATE_2.dataGiornataOrario "
					+ "FROM aga_giornate GIORNATE_2, aga_tariffari TARIFF_2, ricerca_transfert RIC_2  "
					+ "WHERE "
					//+ "AND GIORNATE_2.dataGiornataOrario > DATE_SUB( :oraPrelevamento , INTERVAL 12 HOUR) "
					//+ "AND GIORNATE_2.dataGiornataOrario <  DATE_ADD( :oraPrelevamento , INTERVAL 12 HOUR) "
					+ " TARIFF_2.id_aga_giornate = GIORNATE_2.id_aga_giornate "
					+ "AND TARIFF_2.id_ricerca_transfert IS NOT NULL "
					+ "AND RIC_2.id_ricerca_transfert = TARIFF_2.id_ricerca_transfert "
					+ "AND ( "
						+ "(TARIFF_2.id_aga_tariffari = json_extract(RIC_2.infoPasseggero, '$."+Constants.AgendaAutista_TariffarioId_Andata+"') "
						+ "AND :oraPrelevamento >= RIC_2.dataOraPrelevamentoDate "
						+ "AND :oraPrelevamento < DATE_ADD(RIC_2.dataOraPrelevamentoDate, INTERVAL 0 second)) "
						+ "OR "	
						+ "(RIC_2.ritorno = 1 AND TARIFF_2.id_aga_tariffari = json_extract(RIC_2.infoPasseggero, '$."+Constants.AgendaAutista_TariffarioId_Ritorno+"') "
						+ "AND  :oraPrelevamento >= RIC_2.dataOraRitornoDate "
						+ "AND :oraPrelevamento < DATE_ADD(RIC_2.dataOraRitornoDate, INTERVAL 0 second)) "
						+ "OR "
						+ "(TARIFF_2.id_aga_tariffari = json_extract(RIC_2.infoPasseggero, '$."+Constants.AgendaAutista_TariffarioId_Andata+"') "
						+ "AND :oraPrelevamento <= RIC_2.dataOraPrelevamentoDate "
						+ "AND DATE_ADD(:oraPrelevamento, INTERVAL 0 second) > RIC_2.dataOraPrelevamentoDate) "
						+ "OR "
						+ "(RIC_2.ritorno = 1 AND TARIFF_2.id_aga_tariffari = json_extract(RIC_2.infoPasseggero, '$."+Constants.AgendaAutista_TariffarioId_Ritorno+"') "
						+ "AND :oraPrelevamento <= RIC_2.dataOraRitornoDate "
						+ "AND DATE_ADD(:oraPrelevamento, INTERVAL 0 second) > RIC_2.dataOraRitornoDate) "
					+ ") "
				+ "";
		List<Object[]> result = this.getSession().createSQLQuery( queryString_1 )
				.setParameter("oraPrelevamento", DateUtil.TogliMinutiSecondiMillisecondi(dataOraPrelevamento))
				//.setParameter("durataConTrafficoValue", durataConTrafficoValue)
				.list();
		for(Object[] ite : result) {
			//log.debug("aaa: "+" | "+((BigInteger)ite[0]).longValue()+" | "+(Date)ite[1] );
		}
		return result;

	}
	
	
	/**
	 * Questa Query è concepita per dare il risultato di una sola corsa, cioè quando si richiede anche il ritorno del transfer questa query va eseguita una seconda volta, 
	 * la seconda è per il ritorno, passandogli i paramentri del ritorno.
	 * 
	 * vedere: https://gist.github.com/antoniodipinto/cae89efdd4b906011aba05aca38bdfc8?fbclid=IwAR099fKEfmF7ttYi51sXQHFVZIUlaAwbFQjoZlQwrBGnpfivCmzyhD77AEQ
	 * e conversazione messenger con Antonio Di Pinto da account Matteo Henry Chinaski
	 *	SELECT *,
	 *	(((acos(sin((LATITUDE_VALUE*pi()/180)) * sin(('nome_colonna_latitudine'*pi()/180))+cos((LATITUDE_VALUE*pi()/180)) * cos(('nome_colonna_latitudine'*pi()/180)) * cos(((LONGITUDE_VALUE- 'nome_colonna_longitudine'e)*pi()/180))))*180/pi())*60*1.1515*1.609344) 
	 *	as distance
	 *	FROM coordinates_table
	 *	ORDER BY distance ASC;
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<AgendaAutista_Autista> Result_AgendaAutista(long PercentualeServizio, int numeroPasseggeri, Date dataOraPrelevamento, long distanza, double latPartenza, double lngPartenza,
			double latArrivo, double lngArrivo) {
		
final String MoltDurTraff = "2.2"; 

String queryString_1 = "SELECT "
// USER
+"USER.id, USER.email, USER.first_name, USER.last_name, USER.phone_number, "
// AUTISTA
+"AUTISTA.id_autista, AUTISTA.partitaIvaDenominazione, AUTISTA.azienda, AUTISTA.numCorseEseguite, "
// AUTOVEICOLO
+"AUTO.id_autoveicolo, AUTO.annoImmatricolazione, AUTO.targa, "
// MODELLO e MARCA AUTOVEICOLO
+"MODELLO_AUTOSCOUT.name AS modelloName, MARCA_AUTOSCOUT.name AS marcaName, "
// CLASSE AUTOVEICOLO
+"CLASSE_AUTOVEICOLO.id_classe_autoveicolo, "
// NUMERO POSTI AUTO
+"NUM_POSTI_AUTO.numero, "
//GIORNATE
+ "GIORNATE.id_aga_giornate, GIORNATE.dataGiornataOrario, "
//TARIFFE
+ "TARIFF.id_aga_tariffari, TARIFF.kmCorsa AS kmCorsa_1, TARIFF.prezzoCorsa AS prezzoCorsa_1, TARIFF.kmRaggioArea AS kmRaggioArea_1, "
//MODELLI TARIFFE
+ "MOD_TARIFF.id_aga_autoveicolo_modelli_tariffari, MOD_TARIFF.id_aga_modelli_tariffari, MOD_TARIFF.kmCorsa AS kmCorsa_2, MOD_TARIFF.prezzoCorsa AS prezzoCorsa_2, "
	+ "MOD_TARIFF.kmRaggioArea AS kmRaggioArea_2, "
// AUTISTA
+ "AUTISTA.note "

+ "FROM app_user USER, autista AUTISTA, autoveicolo AUTO, data_modello_auto_numero_posti MODEL_AUTO_NUM_POSTI, data_modello_autoscout MODELLO_AUTOSCOUT, "
+ "data_marca_autoscout MARCA_AUTOSCOUT, data_classe_autoveicolo CLASSE_AUTOVEICOLO, data_numero_posti_auto NUM_POSTI_AUTO, "
+ "aga_giornate GIORNATE, aga_tariffari TARIFF  "
+ "LEFT JOIN aga_modelli_tariffari MOD_TARIFF ON TARIFF.id_aga_autoveicolo_modelli_tariffari = MOD_TARIFF.id_aga_autoveicolo_modelli_tariffari "

+ "WHERE USER.id = AUTISTA.id_user AND AUTISTA.id_autista = AUTO.id_autista "
+ "AND (AUTISTA.approvatoGenerale = true OR AUTISTA.id_autista = 2 OR AUTISTA.id_autista = 3) " // TODO ricordati di togliere questo autista (CIRO PASQUALE) (ADMIN)
+ "AND (AUTISTA.attivo = true OR AUTISTA.id_autista = 2 OR AUTISTA.id_autista = 3) " // TODO ricordati di togliere questo autista (CIRO PASQUALE) (ADMIN)
+ "AND (AUTO.approvatoCartaCircolazione = true OR AUTO.id_autoveicolo = 2 OR AUTO.id_autoveicolo = 4) " // TODO ricordati di togliere questa auto (CIRO PASQUALE) (ADMIN)
+ "AND AUTISTA.bannato = false "
+ "AND AUTO.autoveicolo_sospeso = false AND AUTO.autoveicolo_cancellato = false "
+ "AND AUTO.id_modello_auto_numero_posti = MODEL_AUTO_NUM_POSTI.id_modello_auto_numero_posti "
+ "AND MODEL_AUTO_NUM_POSTI.id_modello_autoscout = MODELLO_AUTOSCOUT.id_modello_autoscout "
+ "AND MODELLO_AUTOSCOUT.id_marca_auto = MARCA_AUTOSCOUT.id_marca_autoscout "
+ "AND MODELLO_AUTOSCOUT.id_classe_autoveicolo = CLASSE_AUTOVEICOLO.id_classe_autoveicolo "
+ "AND MODEL_AUTO_NUM_POSTI.id_numero_posti_auto = NUM_POSTI_AUTO.id_numero_posti_auto "
+ "AND NUM_POSTI_AUTO.numero > :numeroPasseggeri "
+ "AND AUTO.id_autoveicolo = GIORNATE.id_autoveicolo "
+ "AND GIORNATE.id_aga_giornate = TARIFF.id_aga_giornate "
+ "AND GIORNATE.attivo = true "
+ "AND GIORNATE.dataGiornataOrario = :oraPrelev "
		
//------------- CONTROLLO CORSA VENDUTO DURATA TRAFFICO CORSA -------------------------------
+ "AND AUTO.id_autoveicolo NOT IN ( "
	+ "SELECT GIORNATE_2.id_autoveicolo FROM aga_giornate GIORNATE_2, aga_tariffari TARIFF_2, ricerca_transfert RIC_2  "
	+ "WHERE TARIFF_2.id_aga_giornate = GIORNATE_2.id_aga_giornate "
	+ "AND TARIFF_2.id_ricerca_transfert IS NOT NULL "
	+ "AND RIC_2.id_ricerca_transfert = TARIFF_2.id_ricerca_transfert "
	+ "AND ( "
		+ "(RIC_2.approvazioneAndata = "+Constants.IN_APPROVAZIONE +" AND TARIFF_2.id_aga_tariffari = json_extract(RIC_2.infoPasseggero, '$."+Constants.AgendaAutista_TariffarioId_Andata+"') "
		+ "AND :oraPrelev >= RIC_2.dataOraPrelevamentoDate AND :oraPrelev < DATE_ADD(RIC_2.dataOraPrelevamentoDate, INTERVAL durataConTrafficoValue * "+MoltDurTraff+" second)) "
		+ "OR "	
		+ "(RIC_2.ritorno = 1 AND RIC_2.approvazioneRitorno = "+Constants.IN_APPROVAZIONE +" AND TARIFF_2.id_aga_tariffari = json_extract(RIC_2.infoPasseggero, '$."+Constants.AgendaAutista_TariffarioId_Ritorno+"') "
		+ "AND :oraPrelev >= RIC_2.dataOraRitornoDate AND :oraPrelev < DATE_ADD(RIC_2.dataOraRitornoDate, INTERVAL durataConTrafficoValueRitorno * "+MoltDurTraff+" second)) "
		+ "OR "
		+ "(RIC_2.approvazioneAndata = "+Constants.IN_APPROVAZIONE +" AND TARIFF_2.id_aga_tariffari = json_extract(RIC_2.infoPasseggero, '$."+Constants.AgendaAutista_TariffarioId_Andata+"') "
		+ "AND :oraPrelev <= RIC_2.dataOraPrelevamentoDate AND DATE_ADD(:oraPrelev, INTERVAL durataConTrafficoValue * "+MoltDurTraff+" second) > RIC_2.dataOraPrelevamentoDate) "
		+ "OR "
		+ "(RIC_2.ritorno = 1 AND RIC_2.approvazioneRitorno = "+Constants.IN_APPROVAZIONE +" AND TARIFF_2.id_aga_tariffari = json_extract(RIC_2.infoPasseggero, '$."+Constants.AgendaAutista_TariffarioId_Ritorno+"') "
		+ "AND :oraPrelev <= RIC_2.dataOraRitornoDate AND DATE_ADD(:oraPrelev, INTERVAL durataConTrafficoValueRitorno * "+MoltDurTraff+" second) > RIC_2.dataOraRitornoDate) "
	+ ") "
+ ")"
	
//------------------- CONTROLLO CANCELLA ORE AUTOMATICO ----------------------
+ "AND ( (DATE_ADD(NOW(), INTERVAL json_extract(AUTO.info, '$."+AgA_General.JN_AutoClearProssimeOreGiornate+"') HOUR) < :dataOraPrelevamento) "
	+ " OR (json_extract(AUTO.info, '$."+AgA_General.JN_AutoClearProssimeOreGiornate+"') IS NULL "
		+ "AND DATE_ADD(NOW(), INTERVAL "+AgA_General.autoClearProssimeOreGiornate+" HOUR) < :dataOraPrelevamento) ) "
		
// ------------------ CONTROLLO DISTANZA LATITUDINE LONGITUDINE CORSA - RAGGIO AREA RIMESSA --------------------
+ "AND json_extract(AUTO.info, '$."+AgA_General.JN_AreaGeog_Lat+"') IS NOT NULL "
+ "AND json_extract(AUTO.info, '$."+AgA_General.JN_AreaGeog_Lng+"') IS NOT NULL "
+ "AND ( ( ((TARIFF.kmCorsa = :kmPartenza AND TARIFF.eseguiCorse = true) AND "+CalcoloDistanzaRaggioArea(":latPartenza", ":lngPartenza")+" < TARIFF.kmRaggioArea) "
		+ "OR ((MOD_TARIFF.kmCorsa = :kmPartenza AND MOD_TARIFF.eseguiCorse = true) AND "+CalcoloDistanzaRaggioArea(":latPartenza", ":lngPartenza")+" < MOD_TARIFF.kmRaggioArea) ) "
	+ "OR "
	+ "( ((TARIFF.kmCorsa = :kmPartenza AND TARIFF.eseguiCorse = true) AND "+CalcoloDistanzaRaggioArea(":latArrivo", ":lngArrivo")+" < TARIFF.kmRaggioArea) "
		+ "OR ((MOD_TARIFF.kmCorsa = :kmPartenza AND MOD_TARIFF.eseguiCorse = true) AND "+CalcoloDistanzaRaggioArea(":latArrivo", ":lngArrivo")+" < MOD_TARIFF.kmRaggioArea) ) "						
+ ") "
		
+ " ";
		
		List<Object[]> result = this.getSession().createSQLQuery( queryString_1 )
				.setParameter("numeroPasseggeri", numeroPasseggeri).setParameter("oraPrelev", DateUtil.TogliMinutiSecondiMillisecondi(dataOraPrelevamento))
				.setParameter("kmPartenza", (int) (long) distanza / 1000 ).setParameter("latPartenza", latPartenza).setParameter("lngPartenza", lngPartenza)
				.setParameter("latArrivo", latArrivo).setParameter("lngArrivo", lngArrivo).setParameter("dataOraPrelevamento", dataOraPrelevamento).list();
		for(Object[] ite : result) {
			log.debug("aaa: " //27 COLONNNE
			//USER
			+" | "+((BigInteger)ite[0]).longValue()+" | "+(String)ite[1]+" | "+(String)ite[2]+" | "+(String)ite[3]+" | "+(String)ite[4]
			//AUTISTA
			+" | "+((BigInteger)ite[5]).longValue()+" | "+(String)ite[6]+" | "+(Boolean)ite[7]+" | "+(Integer)ite[8]+" | "+(String)ite[27]
			//AUTOVEICOLO
			+" | "+((BigInteger)ite[9]).longValue()+" | "+(String)ite[10]+" | "+(String)ite[11]
			//MODELLO e MARCA AUTOVEICOLO
			+" | "+(String)ite[12]+" | "+(String)ite[13]
			//CLASSE AUTOVEICOLO
			+" | "+((BigInteger)ite[14]).longValue()
			//NUMERO POSTI AUTO
			+" | "+(Integer)ite[15]
			//GIORNATE
			+" | "+((BigInteger)ite[16]).longValue()+" | "+(Date)ite[17]
			//TARIFFE
			+" | "+((BigInteger)ite[18]).longValue()+" | "+ (Integer)ite[19] +" | "+ (BigDecimal)ite[20] +" | "+ (Double)ite[21]
			//MODELLI TARIFFE
			+" | "+ ((BigInteger)ite[22]!= null ? ((BigInteger)ite[22]).longValue() : null)+" | "+ ((BigInteger)ite[23]!= null ? ((BigInteger)ite[23]).longValue() : null)
				+" | "+ (Integer)ite[24] +" | "+ (BigDecimal)ite[25] +" | "+ (Double)ite[26] );
		}
		
		if( result.size() > 0 ) {
			List<AgendaAutista_Autista> agendaAutista_AutistaList = new ArrayList<AgendaAutista_Autista>();
			for(Object[] ite : result) {
				Integer kmCorsa = null;
			    BigDecimal prezzoCorsa = null;
			    double kmRaggioArea = 0;
			    Long idTariffario = null;
			    // SE PRESENTI IdModelliTarffari allora copiarlo in tariffaio l'orario giornato
			    if( (BigInteger)ite[22] != null  ) {
					// mi faccio tornare la lista ModelliTariffari
					List<AgA_ModelliTariffari> modelliTariffariList = getSession().createCriteria(AgA_ModelliTariffari.class)
			        		.add(Restrictions.eq("agA_AutoveicoloModelliTariffari.id", ((BigInteger)ite[22]).longValue() )).list();
					// Prendo la GiornataOrario
					AgA_Giornate agA_Giornate = (AgA_Giornate)getSession().get(AgA_Giornate.class, ((BigInteger)ite[16]).longValue());
					// Creo la lista Tariffari speculare al ModelloTariffio
					for( AgA_ModelliTariffari mT_Ite : modelliTariffariList ) {
						AgA_Tariffari agA_TariffariNew = new AgA_Tariffari(mT_Ite.getKmCorsa(), mT_Ite.isEseguiCorse(), mT_Ite.getPrezzoCorsa(), mT_Ite.getKmRaggioArea(), agA_Giornate);
						getSession().save(agA_TariffariNew);
						if( ((BigInteger)ite[23]).longValue() == mT_Ite.getId() ) {
							idTariffario = agA_TariffariNew.getId();
							kmCorsa = agA_TariffariNew.getKmCorsa();
							prezzoCorsa = agA_TariffariNew.getPrezzoCorsa();
							kmRaggioArea = agA_TariffariNew.getKmRaggioArea();
						}
					}
					// elimino il tariffio con l'id ModelloTariffario
					getSession().createQuery("DELETE AgA_Tariffari WHERE id = :X").setParameter("X", ((BigInteger)ite[18]).longValue()).executeUpdate();
			    }else {
			    	idTariffario = ((BigInteger)ite[18]).longValue();
			    	kmCorsa = (Integer)ite[19];
	    			prezzoCorsa = (BigDecimal)ite[20] ;
					kmRaggioArea = (Double)ite[21];
			    }
			    AgendaAutista_Autista agendaAutista_Autista = new AgendaAutista_Autista(((BigInteger)ite[0]).longValue(), (String)ite[1], (String)ite[2], (String)ite[3], 
			    		(String)ite[4], ((BigInteger)ite[5]).longValue(), (String)ite[6], (Boolean)ite[7], (Integer)ite[8], (String)ite[27], ((BigInteger)ite[9]).longValue(), (String)ite[10], 
			    		(String)ite[11], (String)ite[12], (String)ite[13], ((BigInteger)ite[14]).longValue(), (Integer)ite[15], ((BigInteger)ite[16]).longValue(), (Date)ite[17], 
					idTariffario, kmCorsa, prezzoCorsa, kmRaggioArea, AutoveicoloUtil.DammiAutoClasseReale(((BigInteger)ite[14]).longValue(), (String)ite[10]), 
					latPartenza, lngPartenza, latArrivo, lngArrivo, null);
			    try {
					agendaAutista_Autista = HomeUtil_Aga.AggiungiPrezzi_ad_AgendaAutista_Autista(agendaAutista_Autista, PercentualeServizio);
				} catch (Exception e) {
					e.printStackTrace();
					agendaAutista_Autista = null;
				}
			    agendaAutista_AutistaList.add(agendaAutista_Autista);
			}
			return agendaAutista_AutistaList;
			
		}else {
			return null;
		}
	}
	

	private static String CalcoloDistanzaRaggioArea(String Lat, String Lng) {
		return " (((acos(sin(( "+Lat+" * pi()/180)) * sin((json_extract(AUTO.info, '$."+AgA_General.JN_AreaGeog_Lat+"') * pi() / 180)) "
			+ "+ cos(( "+Lat+" * pi()/180)) * cos((json_extract(AUTO.info, '$."+AgA_General.JN_AreaGeog_Lat+"') * pi() / 180)) "
			+ "* cos((( "+Lng+" - json_extract(AUTO.info, '$."+AgA_General.JN_AreaGeog_Lng+"')) * pi()/180)))) * 180 / pi()) *60 *1.1515 *1.609344) ";
		
	}
}
