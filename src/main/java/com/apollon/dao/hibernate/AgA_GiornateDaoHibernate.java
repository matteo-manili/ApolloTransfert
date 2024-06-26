package com.apollon.dao.hibernate;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.json.JSONObject;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import com.apollon.dao.AgA_GiornateDao;
import com.apollon.model.AgA_AutoveicoloModelliGiornate;
import com.apollon.model.AgA_Giornate;
import com.apollon.model.AgA_ModelliGiornate;
import com.apollon.model.AgA_ModelliTariffari;
import com.apollon.model.AgA_Tariffari;
import com.apollon.model.Autoveicolo;
import com.apollon.util.DammiTempoOperazione;
import com.apollon.util.DateUtil;
import com.apollon.webapp.rest.AgA_Calendario.Calendario_FrontEnd;
import com.apollon.webapp.rest.AgA_Calendario.Calendario_FrontEnd.GiorniMeseCalendario;
import com.apollon.webapp.rest.AgA_Calendario;
import com.apollon.webapp.rest.AgA_General;
import com.apollon.webapp.rest.AgA_Giornata;
import com.apollon.webapp.rest.AgA_Tariffario;
import com.apollon.webapp.rest.AgA_Giornata_Bean.TabellaAutoveicoloModelloTariffario;
import com.apollon.webapp.rest.AgA_Giornata_Bean.TabellaGiornataTariffario;
import com.apollon.webapp.rest.AgA_Giornata_Bean.TabellaModelloGiornata;
import com.apollon.webapp.rest.AgA_Giornata_Bean.TabellaTariffario;
import com.apollon.webapp.rest.AgA_Giornata_Bean.TabellaModelloGiornata.GiornataOrarioTariffario;
import com.apollon.webapp.rest.AgA_Giornata_Bean.TabellaModelloTariffario;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@EnableTransactionManagement
@Repository("AgA_GiornateDao")
public class AgA_GiornateDaoHibernate extends GenericDaoHibernate<AgA_Giornate, Long> implements AgA_GiornateDao {

	public AgA_GiornateDaoHibernate() {
		super(AgA_Giornate.class);
	}
	
	@Transactional
	@Override
	public AgA_Giornate saveAgA_Giornate(AgA_Giornate agA_Giornate) throws DataIntegrityViolationException, HibernateJdbcException {
		getSession().saveOrUpdate(agA_Giornate);
		//getSession().flush();
		return agA_Giornate;
	}
	
	@Override
    @Transactional(readOnly = true)
	public AgA_Giornate get(Long id){
		AgA_Giornate agA_Giornate = (AgA_Giornate) getSession().get(AgA_Giornate.class, id);
		return agA_Giornate;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<AgA_Giornate> getAgA_Giornate_by_idAutoveicolo(Long idAutoveicolo) {
        return getSession().createCriteria(AgA_Giornate.class).add(Restrictions.eq("agA_AutoveicoloModelliGiornate.id", idAutoveicolo)).list();
	}
	
	
	@Override
	@Transactional(readOnly = true)
	public AgA_Giornate getAgA_Giornate_by_dataGiornataOrario_idAutoveicolo(Date dataGiornataOrario, long idAutoveicolo) {
		Criterion crit = Restrictions.and(Restrictions.eq("dataGiornataOrario", dataGiornataOrario), Restrictions.eq("autoveicolo.id", idAutoveicolo));
        return (AgA_Giornate) getSession().createCriteria(AgA_Giornate.class).add(crit).uniqueResult();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<TabellaModelloGiornata> ListaModelliGiornata_OrariGiornataIdModelliTariffari(long idAutoveicolo) {
		String queryString_1 = "SELECT "
				+ "autoModelGiornate.id_aga_autoveicolo_modelli_giornate, autoModelGiornate.nomeGiornata, "
				+ "giornate.orario, giornate.attivo, giornate.id_aga_autoveicolo_modelli_tariffari "
				+ "FROM aga_autoveicolo_modelli_giornate autoModelGiornate, aga_modelli_giornate giornate  "
				+ "WHERE "
				+ "autoModelGiornate.id_autoveicolo = :idAutoveicolo "
				+ "AND autoModelGiornate.id_aga_autoveicolo_modelli_giornate = giornate.id_aga_autoveicolo_modelli_giornate "
				+ "ORDER BY autoModelGiornate.id_aga_autoveicolo_modelli_giornate, giornate.orario ASC ";
		List<Object[]> aaa = this.getSession().createSQLQuery( queryString_1 ).setParameter("idAutoveicolo", idAutoveicolo).list();
		/*
		for(Object[] ite : aaa) {
			System.out.println("aaa: "+ ((BigInteger)ite[0]).longValue()+" | "+ ite[1].toString() +" ||| "
					+(Integer)ite[2]+" |" +(Boolean)ite[3]+  " | "+ ((BigInteger)ite[4]).longValue() );
		}
		*/	
		List<TabellaModelloGiornata> tabellaModelloGiornataList = new ArrayList<TabellaModelloGiornata>();
		Long idGiornata = null;
		for(Object[] ite : aaa) {
			if(idGiornata == null || idGiornata != ((BigInteger)ite[0]).longValue() ) {
				TabellaModelloGiornata tabellaModelloGiornata = new TabellaModelloGiornata( ((BigInteger)ite[0]).longValue(), ite[1].toString(), null );
				List<GiornataOrarioTariffario> giornataOrarioTariffarioList = new ArrayList<GiornataOrarioTariffario>();
				for(Object[] ite_bis : aaa) {
					if(((BigInteger)ite[0]).longValue() == ((BigInteger)ite_bis[0]).longValue()) {
						GiornataOrarioTariffario giornataOrarioTariffario = new GiornataOrarioTariffario((Integer)ite_bis[2], (Boolean)ite_bis[3], ((BigInteger)ite_bis[4]).longValue());
						giornataOrarioTariffarioList.add(giornataOrarioTariffario);
					}
				}
				tabellaModelloGiornata.setGiornataOrarioTariffarioList(giornataOrarioTariffarioList);
				tabellaModelloGiornataList.add(tabellaModelloGiornata);
			}
			idGiornata = ((BigInteger)ite[0]).longValue();
		}			
		return tabellaModelloGiornataList;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public void EliminaGiornataListaTariffari_InserisciGiornataListaTariffari(Date giornata, long idModelloGiornata, long idAutoveicolo) {
		
		long startTime = System.nanoTime();
		DammiTempoOperazione.DammiSecondi(startTime, "3- ");
		
		// Elimina
		EliminaGiornataListaTariffari(giornata, idAutoveicolo);
		DammiTempoOperazione.DammiSecondi(startTime, "4- ");
		
		// Select Modello Giornata
		AgA_AutoveicoloModelliGiornate agA_AutoveicoloModelliGiornate = (AgA_AutoveicoloModelliGiornate) getSession().get(AgA_AutoveicoloModelliGiornate.class, idModelloGiornata);
		/*
		String queryString_3 = "SELECT "
				+ "giornate.id_aga_modelli_giornate, giornate.orario, giornate.attivo, "
				+ "tariffari.id_aga_autoveicolo_modelli_tariffari, tariffari.kmCorsa, tariffari.eseguiCorse, tariffari.prezzoCorsa, tariffari.kmRaggioArea "
				+ "FROM aga_modelli_giornate giornate, aga_modelli_tariffari tariffari  "
				+ "WHERE "
				+ "giornate.id_aga_autoveicolo_modelli_giornate = :idModelloGiornata "
				+ "AND giornate.id_aga_autoveicolo_modelli_tariffari = tariffari.id_aga_autoveicolo_modelli_tariffari "
				+ "ORDER BY giornate.orario ASC, tariffari.kmCorsa ASC ";
		List<Object[]> aaa = this.getSession().createSQLQuery( queryString_3 ).setParameter("idModelloGiornata", idModelloGiornata).list();
		//for(Object[] ite : aaa) {
		//	System.out.println("aaa: "+ ((BigInteger)ite[0]).longValue()+" | "+ AgA_Giornata.Set_Ora_a_Giorno(giornata, (Integer)ite[1]) +" | "+(Boolean)ite[2]+" ||| "+ 
		//			((BigInteger)ite[3]).longValue()+" | "+(Integer)ite[4]+" | "+(Boolean)ite[5]+" | "+(BigDecimal)ite[6]+" | "+(Double)ite[7] );
		//}
		AgA_General.DammiSecondiTempoOperazione(startTime, "5- ");
		List<TabellaGiornataTariffario> tabellaGiornataTariffarioList = new ArrayList<TabellaGiornataTariffario>();
		Long idGiornata = null;
		for(Object[] ite : aaa) {
			if(idGiornata == null || idGiornata != ((BigInteger)ite[0]).longValue() ) {
				TabellaGiornataTariffario tabellaGiornataTariffario = new TabellaGiornataTariffario(((BigInteger)ite[0]).longValue(), AgA_Giornata.Set_Ora_a_Giorno(giornata, (Integer)ite[1]), (Boolean)ite[2], null);
				List<TabellaTariffario> tabellaTariffarioGiornList = new ArrayList<TabellaTariffario>();
				for(Object[] ite_bis : aaa) {
					if(((BigInteger)ite[0]).longValue() == ((BigInteger)ite_bis[0]).longValue()) {
						TabellaTariffario tabellaTariffario = new TabellaTariffario(
								((BigInteger)ite_bis[3]).longValue(), (Integer)ite_bis[4], (Boolean)ite_bis[5], (BigDecimal)ite_bis[6], (Double)ite_bis[7]);
						tabellaTariffarioGiornList.add(tabellaTariffario);
					}
				}
				tabellaGiornataTariffario.setTabellaTariffarioList(tabellaTariffarioGiornList);
				tabellaGiornataTariffarioList.add(tabellaGiornataTariffario);
			}
			idGiornata = ((BigInteger)ite[0]).longValue();
		}
		*/
		
		// inserisci
		Autoveicolo autoveicolo = (Autoveicolo) getSession().get(Autoveicolo.class, idAutoveicolo);
		JSONObject gSONObject = AgA_General.GetValues_AutoClearProssimeOreGiornate( autoveicolo );
		Date date = DateUtil.AggiungiOre_a_DataAdesso( gSONObject.getInt(AgA_General.JN_AutoClearProssimeOreGiornate) );
		for(AgA_ModelliGiornate tabGiornataTariffList_ite: agA_AutoveicoloModelliGiornate.getAgA_ModelliGiornate()) {
			if( date.before( AgA_Giornata.Set_Ora_a_Giorno(giornata, tabGiornataTariffList_ite.getOrario()) ) ) {
				AgA_Giornate agA_Giornate = getAgA_Giornate_by_dataGiornataOrario_idAutoveicolo(AgA_Giornata.Set_Ora_a_Giorno(giornata, tabGiornataTariffList_ite.getOrario()), idAutoveicolo);
				if( agA_Giornate == null ) {
					agA_Giornate = new AgA_Giornate( AgA_Giornata.Set_Ora_a_Giorno(giornata, tabGiornataTariffList_ite.getOrario()), tabGiornataTariffList_ite.isAttivo(), autoveicolo);
					getSession().save(agA_Giornate);
				}
				List<AgA_Tariffari> TariffariList = getSession().createCriteria(AgA_Tariffari.class).add(Restrictions.eq("agA_Giornate.id", agA_Giornate.getId())).list();
				if(AgA_Tariffario.Check_TransferAcquistato_TariffeList(TariffariList) == false ) {
					try {
						AgA_Tariffari Tariffari_New = new AgA_Tariffari(agA_Giornate, tabGiornataTariffList_ite.getAgA_AutoveicoloModelliTariffari());
						getSession().saveOrUpdate(Tariffari_New);
					}catch(ConstraintViolationException ex) {
						log.debug("ConstraintViolationException");
						ex.printStackTrace();
					}finally {
						getSession().clear();
						//getSession().flush();
					}
				}else {
					List<AgA_ModelliTariffari> modelTariffList = getSession().createCriteria(AgA_ModelliTariffari.class)
							.add(Restrictions.eq("agA_AutoveicoloModelliTariffari.id", tabGiornataTariffList_ite.getAgA_AutoveicoloModelliTariffari().getId())).list();
					for(AgA_ModelliTariffari iteModelTariff: modelTariffList ) {
						boolean inserisci = true;
						for(AgA_Tariffari iteTariff: TariffariList ) {
							//System.out.println( iteModelTariff.getKmCorsa() );
							if( iteModelTariff.getKmCorsa() == iteTariff.getKmCorsa() && iteTariff.getRicercaTransfertAcquistato() != null ) {
								inserisci = false;
								break;
							}
						}
						if( inserisci ) {
							try {
								AgA_Tariffari newAgA_Tariffari = new AgA_Tariffari(iteModelTariff.getKmCorsa(), iteModelTariff.isEseguiCorse(), iteModelTariff.getPrezzoCorsa(), 
										iteModelTariff.getKmRaggioArea(), agA_Giornate);
								getSession().saveOrUpdate(newAgA_Tariffari);
							}catch(ConstraintViolationException ex) {
								log.debug("ConstraintViolationException");
								ex.printStackTrace();
							}finally {
								getSession().clear();
								//getSession().flush();
							}
						}
					}
				}
			}
		}
	}
	
	
	@Override
	@Transactional
	public void EliminaGiornataListaTariffari(Date giornata, long idAutoveicolo) {
		// Elimina aga_tariffari
		List<Date> giorniList = AgA_Giornata.DammiDate_DataOreZero_DataDomaniOreZero(giornata);
		String queryString_1 = "DELETE T FROM aga_tariffari T INNER JOIN aga_giornate G ON T.id_aga_giornate = G.id_aga_giornate " 
				+ "WHERE G.id_autoveicolo = :idAutoveicolo "
				+ "AND G.dataGiornataOrario >= :DataOreZero "
				+ "AND G.dataGiornataOrario < :DataDomaniOreZero "
				+ "AND T.id_ricerca_transfert IS NULL ";
		getSession().createSQLQuery( queryString_1 ).setParameter("idAutoveicolo", idAutoveicolo)
			.setParameter("DataOreZero", giorniList.get(0)).setParameter("DataDomaniOreZero", giorniList.get(1)).executeUpdate();
		
		// Elimina aga_giornate
		String queryString_2 = "DELETE FROM aga_giornate " 
				+ "WHERE aga_giornate.id_autoveicolo = :idAutoveicolo "
				+ "AND aga_giornate.dataGiornataOrario >= :DataOreZero "
				+ "AND aga_giornate.dataGiornataOrario < :DataDomaniOreZero "
				+ "AND NOT EXISTS (SELECT * FROM aga_tariffari " 
					+" WHERE aga_tariffari.id_aga_giornate = aga_giornate.id_aga_giornate AND aga_tariffari.id_ricerca_transfert IS NOT NULL) ";
		getSession().createSQLQuery( queryString_2 ).setParameter("idAutoveicolo", idAutoveicolo)
				.setParameter("DataOreZero", giorniList.get(0)).setParameter("DataDomaniOreZero", giorniList.get(1)).executeUpdate();
	}
	
	
	@Override
	@Transactional
	public void PuliziaDatabase_GiornateTariffari() {
		// Elimina aga_tariffari
		String queryString_1 = "DELETE T FROM aga_tariffari T " 
				+ "INNER JOIN aga_giornate G ON T.id_aga_giornate = G.id_aga_giornate " 
				+ "INNER JOIN autoveicolo AUTO ON AUTO.id_autoveicolo = G.id_autoveicolo " 
				+ "WHERE ( (G.dataGiornataOrario < DATE_ADD(NOW(), INTERVAL json_extract(AUTO.info, '$."+AgA_General.JN_AutoClearProssimeOreGiornate+"') HOUR)) "
					+ "OR(json_extract(AUTO.info, '$."+AgA_General.JN_AutoClearProssimeOreGiornate+"') IS NULL "
						+ "AND G.dataGiornataOrario < DATE_ADD(NOW(), INTERVAL "+AgA_General.autoClearProssimeOreGiornate+" HOUR)) )"
				+ "AND T.id_ricerca_transfert IS NULL ";
		int result1 = getSession().createSQLQuery( queryString_1 ).executeUpdate();
		// Elimina aga_giornate
		String queryString_2 = "DELETE G FROM aga_giornate G " 
				+ "INNER JOIN autoveicolo AUTO ON AUTO.id_autoveicolo = G.id_autoveicolo " 
				+ "WHERE ( (G.dataGiornataOrario < DATE_ADD(NOW(), INTERVAL json_extract(AUTO.info, '$."+AgA_General.JN_AutoClearProssimeOreGiornate+"') HOUR)) "
				+ "OR(json_extract(AUTO.info, '$."+AgA_General.JN_AutoClearProssimeOreGiornate+"') IS NULL "
					+ "AND G.dataGiornataOrario < DATE_ADD(NOW(), INTERVAL "+AgA_General.autoClearProssimeOreGiornate+" HOUR)) )"
				+ "AND NOT EXISTS (SELECT * FROM aga_tariffari T " 
					+" WHERE T.id_aga_giornate = G.id_aga_giornate AND T.id_ricerca_transfert IS NOT NULL) ";
		int result2 = getSession().createSQLQuery( queryString_2 ).executeUpdate();
		log.debug("result1: "+result1 +" result2: "+result2);
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Object> ListaTariffariGiornataOrario(Date giornata, long idAutoveicolo) {
		String queryString_1 = "SELECT "
				+ "giornate.id_aga_giornate, giornate.dataGiornataOrario, giornate.attivo, "
				+ "tariffari.id_aga_tariffari, tariffari.kmCorsa, tariffari.eseguiCorse, tariffari.prezzoCorsa, tariffari.kmRaggioArea, tariffari.id_aga_autoveicolo_modelli_tariffari "
				+ "FROM aga_giornate giornate, aga_tariffari tariffari  "
				+ "WHERE "
				+ "giornate.id_autoveicolo = :idAutoveicolo "
				+ "AND giornate.dataGiornataOrario = :giornata "
				+ "AND giornate.id_aga_giornate = tariffari.id_aga_giornate "
				+ "ORDER BY giornate.dataGiornataOrario ASC, tariffari.kmCorsa ASC ";
		List<Object[]> aaa = this.getSession().createSQLQuery( queryString_1 )
				.setParameter("idAutoveicolo", idAutoveicolo).setParameter("giornata", giornata).list();
		/*
		for(Object[] ite : aaa) {
			System.out.println("aaa: "+ ((BigInteger)ite[0]).longValue()+" | "+(Date)ite[1]+" | "+(Boolean)ite[2]+" ||| "+ 
					((BigInteger)ite[3]).longValue()+" | "+(Integer)ite[4]+" | "+(Boolean)ite[5]+" | "+(BigDecimal)ite[6]+" | "+(Double)ite[7] );
		}
		*/
		List<TabellaGiornataTariffario> tabellaGiornataTariffarioList = new ArrayList<TabellaGiornataTariffario>();
		Long idGiornata = null;
		for(Object[] ite : aaa) {
			if(idGiornata == null || idGiornata != ((BigInteger)ite[0]).longValue() ) {
				TabellaGiornataTariffario tabellaGiornataTariffario = new TabellaGiornataTariffario(((BigInteger)ite[0]).longValue(), (Date)ite[1], (Boolean)ite[2], null);
				List<TabellaTariffario> tabellaTariffarioGiornList = new ArrayList<TabellaTariffario>();
				for(Object[] ite_bis : aaa) {
					if(((BigInteger)ite[0]).longValue() == ((BigInteger)ite_bis[0]).longValue()) {
						TabellaTariffario tabellaTariffario = new TabellaTariffario(
								((BigInteger)ite_bis[3]).longValue(), (Integer)ite_bis[4], (Boolean)ite_bis[5], (BigDecimal)ite_bis[6], (Double)ite_bis[7], 
								((BigInteger)ite_bis[8]) != null ? ((BigInteger)ite_bis[8]).longValue() : null);
						tabellaTariffarioGiornList.add(tabellaTariffario);
					}
				}
				tabellaGiornataTariffario.setTabellaTariffarioList(tabellaTariffarioGiornList);
				tabellaGiornataTariffarioList.add(tabellaGiornataTariffario);
			}
			idGiornata = ((BigInteger)ite[0]).longValue();
		}
		List<TabellaAutoveicoloModelloTariffario> tabellaModelloTariffarioList = ListaModelliTariffari(idAutoveicolo);
		return new ArrayList<Object>(Arrays.asList( tabellaGiornataTariffarioList, tabellaModelloTariffarioList ));
	}
	
	
	@Override
	@Transactional(readOnly = true)
	public List<Object> ListaTariffariGiornata_ListaModelliTariffari(Date giornata, long idAutoveicolo) {
		List<TabellaGiornataTariffario> tabellaGiornataTariffarioList = ListaTariffariGiornata(giornata, idAutoveicolo);
		List<TabellaAutoveicoloModelloTariffario> tabellaModelloTariffarioList = ListaModelliTariffari(idAutoveicolo);
		return new ArrayList<Object>(Arrays.asList( tabellaGiornataTariffarioList, tabellaModelloTariffarioList ));
	}
	
	
	@SuppressWarnings("unchecked")
	public List<TabellaGiornataTariffario> ListaTariffariGiornata(Date giornata, long idAutoveicolo) {
		List<Date> giorniList = AgA_Giornata.DammiDate_DataOreZero_DataDomaniOreZero(giornata);
		String queryString_1 = "SELECT "
				+ "giornate.id_aga_giornate, giornate.dataGiornataOrario, giornate.attivo, "
				+ "tariffari.id_aga_tariffari, tariffari.kmCorsa, tariffari.eseguiCorse, tariffari.prezzoCorsa, tariffari.kmRaggioArea, tariffari.id_aga_autoveicolo_modelli_tariffari "
				+ "FROM aga_giornate giornate, aga_tariffari tariffari  "
				+ "WHERE "
				+ "giornate.id_autoveicolo = :idAutoveicolo "
				+ "AND giornate.dataGiornataOrario >= :DataOreZero "
				+ "AND giornate.dataGiornataOrario < :DataDomaniOreZero "
				+ "AND giornate.id_aga_giornate = tariffari.id_aga_giornate "
				+ "ORDER BY giornate.dataGiornataOrario ASC, tariffari.kmCorsa ASC ";
		List<Object[]> aaa = this.getSession().createSQLQuery( queryString_1 ).setParameter("idAutoveicolo", idAutoveicolo)
				.setParameter("DataOreZero", giorniList.get(0)).setParameter("DataDomaniOreZero", giorniList.get(1)).list();
		/*
		for(Object[] ite : aaa) {
			System.out.println("aaa: "+ ((BigInteger)ite[0]).longValue()+" | "+(Date)ite[1]+" | "+(Boolean)ite[2]+" ||| "+ 
					((BigInteger)ite[3]).longValue()+" | "+(Integer)ite[4]+" | "+(Boolean)ite[5]+" | "+(BigDecimal)ite[6]+" | "+(Double)ite[7] );
		}
		*/
		return GetListaTariffari(aaa);
	}
	
	
	@Override
	@Transactional(readOnly = true)
	public List<Object> ListaTariffariCalendario_ListaModelliTariffari(Calendario_FrontEnd calendario_FrontEnd, long idAutoveicolo) {
		List<TabellaGiornataTariffario> tabellaGiornataTariffarioList = ListaTariffariMese(calendario_FrontEnd, idAutoveicolo);
		List<TabellaAutoveicoloModelloTariffario> tabellaModelloTariffarioList = ListaModelliTariffari(idAutoveicolo);
		return new ArrayList<Object>(Arrays.asList( tabellaGiornataTariffarioList, tabellaModelloTariffarioList ));
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<TabellaGiornataTariffario> ListaTariffariMese(Calendario_FrontEnd calendario_FrontEnd, long idAutoveicolo) {
		long startTime = System.nanoTime();
		DammiTempoOperazione.DammiSecondi(startTime, "1- ");
		
		List<Date> giorniMeseCalendarioList = new ArrayList<Date>();
		for(GiorniMeseCalendario ite: calendario_FrontEnd.getGiorniMeseCalendarioList()) {
	    	giorniMeseCalendarioList.add( ite.getGiorno() );
	    }
		List<Date> giorniList_primo = AgA_Giornata.DammiDate_DataOreZero_DataDomaniOreZero(giorniMeseCalendarioList.get(0));
		List<Date> giorniList_ultimo = AgA_Giornata.DammiDate_DataOreZero_DataDomaniOreZero(giorniMeseCalendarioList.get(giorniMeseCalendarioList.size() - 1));
		/*
		String queryString_1 = "SELECT "
				+ "giornate.id_aga_giornate, giornate.dataGiornataOrario, giornate.attivo, "
				+ "tariffari.id_aga_tariffari, tariffari.kmCorsa, tariffari.eseguiCorse, tariffari.prezzoCorsa, tariffari.kmRaggioArea "
				+ "FROM aga_giornate giornate JOIN aga_tariffari tariffari ON giornate.id_aga_giornate = tariffari.id_aga_giornate "
				+ "WHERE "
				+ "giornate.id_autoveicolo = :idAutoveicolo "
				+ "AND giornate.dataGiornataOrario >= :giorniList_primo "
				+ "AND giornate.dataGiornataOrario < :giorniList_ultimo "
				+ ""
				+ "ORDER BY giornate.dataGiornataOrario ASC, tariffari.kmCorsa ASC ";
		*/
		
		String queryString_1 = "SELECT "
				+ "giornate.id_aga_giornate, giornate.dataGiornataOrario, giornate.attivo, "
				+ "tariffari.id_aga_tariffari, tariffari.kmCorsa, tariffari.eseguiCorse, tariffari.prezzoCorsa, tariffari.kmRaggioArea, tariffari.id_aga_autoveicolo_modelli_tariffari "
				+ "FROM aga_giornate giornate INNER JOIN aga_tariffari tariffari ON giornate.id_aga_giornate = tariffari.id_aga_giornate "
				+ "WHERE "
				+ "giornate.id_autoveicolo = :idAutoveicolo "
				+ "AND giornate.dataGiornataOrario >= :giorniList_primo "
				+ "AND giornate.dataGiornataOrario < :giorniList_ultimo "
				+ "ORDER BY giornate.id_aga_giornate ASC, tariffari.id_aga_giornate ASC, giornate.dataGiornataOrario ASC, tariffari.kmCorsa ASC ";
		
		List<Object[]> aaa = this.getSession().createSQLQuery( queryString_1 )
				.setParameter("idAutoveicolo", idAutoveicolo)
				.setParameter("giorniList_primo", giorniList_primo.get(0)).setParameter("giorniList_ultimo", giorniList_ultimo.get(1)).list();
		/*
		for(Object[] ite : aaa) {
			System.out.println("aaa: "+ ((BigInteger)ite[0]).longValue()+" | "+(Date)ite[1]+" | "+(Boolean)ite[2]+" ||| "+ 
					((BigInteger)ite[3]).longValue()+" | "+(Integer)ite[4]+" | "+(Boolean)ite[5]+" | "+(BigDecimal)ite[6]+" | "+(Double)ite[7] );
		}
		*/
		DammiTempoOperazione.DammiSecondi(startTime, "2- ");
		return GetListaTariffari(aaa);
	}
	
	
	private static List<TabellaGiornataTariffario> GetListaTariffari( List<Object[]> aaa ) {
		List<TabellaGiornataTariffario> tabellaGiornataTariffarioList = new ArrayList<TabellaGiornataTariffario>();
		List<TabellaTariffario> tabellaTariffarioGiornList = new ArrayList<TabellaTariffario>();

		for( int ite = 0 ; ite <= aaa.size()-1; ite++  ) {
			TabellaTariffario tabellaTariffario = new TabellaTariffario(((BigInteger)aaa.get(ite)[3]).longValue(), (Integer)aaa.get(ite)[4], 
					(Boolean)aaa.get(ite)[5], (BigDecimal)aaa.get(ite)[6], (Double)aaa.get(ite)[7], ((BigInteger)aaa.get(ite)[8]) != null ? ((BigInteger)aaa.get(ite)[8]).longValue() : null );
			tabellaTariffarioGiornList.add(tabellaTariffario);
			
			if( ite == aaa.size()-1 || ((BigInteger)aaa.get(ite)[0]).longValue() != ((BigInteger)aaa.get(ite + 1)[0]).longValue() ) {
				TabellaGiornataTariffario tabellaGiornataTariffario = new TabellaGiornataTariffario( ((BigInteger)aaa.get(ite)[0]).longValue(), (Date)aaa.get(ite)[1], (Boolean)aaa.get(ite)[2], 
						new ArrayList<TabellaTariffario>(tabellaTariffarioGiornList));
				tabellaGiornataTariffarioList.add( tabellaGiornataTariffario );
				tabellaTariffarioGiornList.clear();
			}
		}
		return tabellaGiornataTariffarioList;
	}
	
	/**
	 * da studiare....
	private static List<TabellaGiornataTariffario> GetListaTariffari_OLD( List<Object[]> aaa ) {
        Map<TabellaGiornataTariffario, List<Object[]>> rowsByGiornata = aaa.stream()
                .collect(Collectors.groupingBy(
                        row -> new TabellaGiornataTariffario(((BigInteger) row[0]).longValue(), (Date) row[1], (Boolean) row[2], null)));
        return rowsByGiornata.entrySet().stream().map(Test::toGiornataWithTariffarios).collect(Collectors.toList());
    } */
	/**
	 * da studiare
	 
    private static TabellaGiornataTariffario toGiornataWithTariffarios(Map.Entry<TabellaGiornataTariffario, List<Object[]>> tabellaGiornataTariffarioListEntry) {
        TabellaGiornataTariffario tabellaGiornataTariffario = tabellaGiornataTariffarioListEntry.getKey();
        List<TabellaTariffario> tabellaTariffarios = tabellaGiornataTariffarioListEntry.getValue().stream()
                .map(row -> new TabellaTariffario(((BigInteger)row[3]).longValue(), (Integer)row[4], (Boolean)row[5], (BigDecimal)row[6], (Double)row[7]))
                .collect(Collectors.toList());
        tabellaGiornataTariffario.setTabellaTariffarioList(tabellaTariffarios);
        return tabellaGiornataTariffario;
    } */
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<TabellaAutoveicoloModelloTariffario> ListaModelliTariffari(long idAutoveicolo) {
		String queryString_2 = "SELECT "
				+ "modelliTariffari.id_aga_autoveicolo_modelli_tariffari, modelliTariffari.nomeTariffario, "
				+ "tariffari.id_aga_modelli_tariffari, tariffari.kmCorsa, tariffari.eseguiCorse, tariffari.prezzoCorsa, tariffari.kmRaggioArea "
				+ "FROM aga_autoveicolo_modelli_tariffari modelliTariffari INNER JOIN aga_modelli_tariffari tariffari ON modelliTariffari.id_aga_autoveicolo_modelli_tariffari = tariffari.id_aga_autoveicolo_modelli_tariffari "
				+ "WHERE "
				+ "modelliTariffari.id_autoveicolo = :idAutoveicolo "
				+ "ORDER BY modelliTariffari.id_aga_autoveicolo_modelli_tariffari, tariffari.kmCorsa ASC ";
		List<Object[]> bbb = this.getSession().createSQLQuery( queryString_2 ).setParameter("idAutoveicolo", idAutoveicolo).list();
		/*
		for(Object[] ite : bbb) {
			System.out.println("bbb: "+ ((BigInteger)ite[0]).longValue()+" | "+ite[1].toString()+" ||| "+ 
					((BigInteger)ite[2]).longValue()+" | "+(Integer)ite[3]+" | "+(Boolean)ite[4]+" | "+(BigDecimal)ite[5]+" | "+(Double)ite[6] );
		}
		*/
		
		List<TabellaAutoveicoloModelloTariffario> tabellaModelloTariffarioList = new ArrayList<TabellaAutoveicoloModelloTariffario>();
		Long idModelloTariffario = null;
		for(Object[] ite : bbb) {
			if(idModelloTariffario == null || idModelloTariffario != ((BigInteger)ite[0]).longValue() ) {
				TabellaAutoveicoloModelloTariffario tabellaModelloTariffario = new TabellaAutoveicoloModelloTariffario(((BigInteger)ite[0]).longValue(), ite[1].toString(), null);
				List<TabellaModelloTariffario> tabellaModelloTariffarioModList = new ArrayList<TabellaModelloTariffario>();
				for(Object[] ite_bis : bbb) {
					if(((BigInteger)ite[0]).longValue() == ((BigInteger)ite_bis[0]).longValue()) {
						TabellaModelloTariffario tabellaModelloTariffarioMod = new TabellaModelloTariffario(
								((BigInteger)ite_bis[2]).longValue(), (Integer)ite_bis[3], (Boolean)ite_bis[4], (BigDecimal)ite_bis[5], (Double)ite_bis[6]);
						tabellaModelloTariffarioModList.add(tabellaModelloTariffarioMod);
					}
				}
				tabellaModelloTariffario.setTabellaModelloTariffarioList(tabellaModelloTariffarioModList);
				tabellaModelloTariffarioList.add(tabellaModelloTariffario);
			}
			idModelloTariffario = ((BigInteger)ite[0]).longValue();
		}
		return tabellaModelloTariffarioList;
	}
	
	
	
	/**
	 * per usare il setResultTransformer in un createSQLQuery usare l'alias delle colonne della select corrispondente agli attributi della classe passata a Transformers.aliasToBean
	 * e aggiungeli al addScalar
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<AgA_Giornate> ListaGiornata_DisponbileVendita_Mese(Calendario_FrontEnd tabellaMeseCalendario, long idAutoveicolo) {
		List<Date> giorniMeseCalendarioList = new ArrayList<Date>();
		for(GiorniMeseCalendario ite: tabellaMeseCalendario.getGiorniMeseCalendarioList()) {
	    	giorniMeseCalendarioList.add( ite.getGiorno() );
	    }
		List<Date> giorniList_primo = AgA_Giornata.DammiDate_DataOreZero_DataDomaniOreZero(giorniMeseCalendarioList.get(0));
		List<Date> giorniList_ultimo = AgA_Giornata.DammiDate_DataOreZero_DataDomaniOreZero(giorniMeseCalendarioList.get(giorniMeseCalendarioList.size() - 1));
		String queryString_1 = "SELECT "
				+ "giornate.id_aga_giornate AS id, giornate.dataGiornataOrario AS dataGiornataOrario "
				+ "FROM aga_giornate giornate "
				+ "WHERE "
				+ "giornate.id_autoveicolo = :idAutoveicolo "
				+ "AND giornate.dataGiornataOrario >= :giorniList_primo "
				+ "AND giornate.dataGiornataOrario < :giorniList_ultimo "
				+ "AND giornate.attivo = true "
				+ "AND EXISTS (SELECT * "
					+ "FROM aga_tariffari tariffari "
					+ "WHERE giornate.id_aga_giornate = tariffari.id_aga_giornate AND tariffari.eseguiCorse = true) "
				+ "ORDER BY giornate.dataGiornataOrario ASC ";
		
		List<AgA_Giornate> aaa = this.getSession().createSQLQuery( queryString_1 )
			.addScalar("id", StandardBasicTypes.LONG)
			// vedere soluzione: https://stackoverflow.com/questions/9533935/how-to-force-hibernate-to-return-dates-as-java-util-date-instead-of-timestamp
			// vedere anche: https://www.programcreek.com/java-api-examples/?api=org.hibernate.type.StandardBasicTypes
			.addScalar("dataGiornataOrario", StandardBasicTypes.TIMESTAMP) // NON USARE DATE, DA ERRORE CONVERTE IN java.sql.Date
			.setResultTransformer(Transformers.aliasToBean(AgA_Giornate.class))
			.setParameter("idAutoveicolo", idAutoveicolo)
			.setParameter("giorniList_primo", giorniList_primo.get(0)).setParameter("giorniList_ultimo", giorniList_ultimo.get(1)).list();
		/*
		for(AgA_Giornate ite : aaa) {
			System.out.println("aaa: "+ ite.getId() +" | "+ite.getDataGiornataOrario() );
		}
		*/
		return aaa;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public JSONObject Menu_Data(long idAutoveicolo) {
		JSONObject mainObj = new JSONObject();
		String queryString_1 = "SELECT AAA.id_aga_autoveicolo_modelli_tariffari FROM aga_autoveicolo_modelli_tariffari AAA "
				+ "WHERE AAA.id_autoveicolo = :idAutoveicolo "
				+ "AND EXISTS (SELECT BBB.id_aga_modelli_tariffari "
					+ "FROM aga_modelli_tariffari BBB "
					+ "WHERE AAA.id_aga_autoveicolo_modelli_tariffari = BBB.id_aga_autoveicolo_modelli_tariffari AND BBB.eseguiCorse = true) ";
		List<Object> createSQLQuery_1 = this.getSession().createSQLQuery( queryString_1 )
			.setParameter("idAutoveicolo", idAutoveicolo).list();
		//System.out.println("createSQLQuery_1.size(): "+createSQLQuery_1.size());
		if( createSQLQuery_1.size() == 0 ) {
			mainObj.put(AgA_General.JN_Menu_Alert_ModelTariffario, AgA_General.ALERT_MODEL_TARIFFARIO_TEXT);
		}
		String queryString_2 = "SELECT AAA.id_aga_autoveicolo_modelli_giornate FROM aga_autoveicolo_modelli_giornate AAA "
				+ "WHERE AAA.id_autoveicolo = :idAutoveicolo "
				+ "AND EXISTS (SELECT BBB.id_aga_modelli_giornate "
					+ "FROM aga_modelli_giornate BBB "
					+ "WHERE AAA.id_aga_autoveicolo_modelli_giornate = BBB.id_aga_autoveicolo_modelli_giornate AND BBB.attivo = true "
					+ "AND EXISTS (SELECT CCC.id_aga_modelli_tariffari "
						+ "FROM aga_modelli_tariffari CCC "
						+ "WHERE BBB.id_aga_autoveicolo_modelli_tariffari = CCC.id_aga_autoveicolo_modelli_tariffari AND CCC.eseguiCorse = true)) ";
		List<Object> createSQLQuery_2 = this.getSession().createSQLQuery( queryString_2 ).setParameter("idAutoveicolo", idAutoveicolo).list();
		//System.out.println("createSQLQuery_2.size(): "+createSQLQuery_2.size());
		if( createSQLQuery_2.size() == 0 ) {
			mainObj.put(AgA_General.JN_Menu_Alert_ModelGiornata, AgA_General.ALERT_MODEL_GIORNATA_TEXT);
		}
		String queryString_3 = "SELECT AAA.id_autoveicolo FROM autoveicolo AAA "
				+ "WHERE AAA.id_autoveicolo = :idAutoveicolo "
				+ "AND json_extract(AAA.info, '$."+AgA_General.JN_AreaGeog_Lat+"') IS NOT NULL "
				+ "AND json_extract(AAA.info, '$."+AgA_General.JN_AreaGeog_Lng+"') IS NOT NULL "
				+ "AND json_extract(AAA.info, '$."+AgA_General.JN_raggio+"') IS NOT NULL "
				+ "AND json_extract(AAA.info, '$."+AgA_General.JN_AreaGeog_Address+"') IS NOT NULL ";
		List<Object> createSQLQuery_3 = this.getSession().createSQLQuery( queryString_3 ).setParameter("idAutoveicolo", idAutoveicolo).list();
		//System.out.println("createSQLQuery_3.size(): "+createSQLQuery_3.size());
		if( createSQLQuery_3.size() == 0 ) {
			mainObj.put(AgA_General.JN_Menu_Alert_AreaGeog, AgA_General.ALERT_AREA_GEOG_TEXT);
			
		}else {
			String queryString_4 = "SELECT AAA.info AS info FROM autoveicolo AAA WHERE AAA.id_autoveicolo = :idAutoveicolo ";
			Autoveicolo autoveicolo = (Autoveicolo) this.getSession().createSQLQuery( queryString_4 )
					.addScalar("info", StandardBasicTypes.STRING) 
					.setResultTransformer(Transformers.aliasToBean(Autoveicolo.class))
					.setParameter("idAutoveicolo", idAutoveicolo).uniqueResult();
			String text = AgA_General.ADDRESS_RAGGIO_AREAGEOG(autoveicolo.getAgA_AreaGeografica_Address(), autoveicolo.getAgA_AreaGeografica_Raggio());
			mainObj.put(AgA_General.JN_Menu_Address_Raggio_AreaGeog, text);
		}
		//System.out.println("mainObj: "+mainObj.toString());
		return mainObj;
	}
	
	

	/**
	Puoi avere migliorato la situazione, ma continui ad avere il prodotto cartesiano tra tabelle.
	Devi sostituire il
	FROM autoveicolo AUTO, aga_giornate G, aga_tariffari T
	con
	FROM autoveicolo AUTO INNER JOIN aga_giornate G ON AUTO.id_autoveicolo = G.id_autoveicolo
	INNER JOIN aga_tariffari T ON G.id_aga_giornate = T.id_aga_giornate
	
	Con la inner join la query tira su solo i record presenti in entrambe le tabelle. Se invece lo fai in where tira su tutti i record della seconda tabella 
	moltiplicati per quelli della prima, poi la where dovrebbe andare in scan su tutto il result set. Dove la correlazione è con un primary key, 
	fai la join (inner se devono essere presenti solo i record della prima tabella con la presenza della correlazione nella seconda). Non so che db usi, 
	ma SQL server e Postgesql permettono di farti vedere anche il piano di esecuzione della query, in modo da capire quanto è performante. 
	Sfruttando in modo corretto join e indici, hai result set più snelli e quindi più veloci
	
	vedere: https://dev.mysql.com/doc/refman/8.0/en/execution-plan-information.html
	*/
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public boolean AutoveicoloDisponbileVendita(long idAutoveicolo) {
		long startTime = System.nanoTime(); 
		String queryString_2 = "SELECT G.id_aga_giornate "
		//+ "FROM autoveicolo AUTO, aga_giornate G, aga_tariffari T "
		+ "FROM autoveicolo AUTO INNER JOIN aga_giornate G ON AUTO.id_autoveicolo = G.id_autoveicolo INNER JOIN aga_tariffari T ON G.id_aga_giornate = T.id_aga_giornate "
		
		+ "WHERE AUTO.id_autoveicolo = :idAutoveicolo AND AUTO.id_autoveicolo = G.id_autoveicolo AND G.attivo = true AND G.id_aga_giornate = T.id_aga_giornate "
		+ "AND ( (T.id_aga_autoveicolo_modelli_tariffari IS NULL AND T.eseguiCorse = true ) "
		+ "OR (T.id_aga_autoveicolo_modelli_tariffari IS NOT NULL "
			+ "AND EXISTS (SELECT AUTO_MOD_TARIFF.id_aga_autoveicolo_modelli_tariffari FROM aga_autoveicolo_modelli_tariffari AUTO_MOD_TARIFF, aga_modelli_tariffari MOD_TARIFF "
				+ "WHERE T.id_aga_autoveicolo_modelli_tariffari = AUTO_MOD_TARIFF.id_aga_autoveicolo_modelli_tariffari "
				+ "AND AUTO_MOD_TARIFF.id_aga_autoveicolo_modelli_tariffari = MOD_TARIFF.id_aga_autoveicolo_modelli_tariffari AND MOD_TARIFF.eseguiCorse = true) ) ) "
		+ "AND( (DATE_ADD(NOW(), INTERVAL json_extract(AUTO.info, '$."+AgA_General.JN_AutoClearProssimeOreGiornate+"') HOUR) < G.dataGiornataOrario) "
		+ "OR(json_extract(AUTO.info, '$."+AgA_General.JN_AutoClearProssimeOreGiornate+"') IS NULL "
		+ "AND DATE_ADD(NOW(), INTERVAL "+AgA_General.autoClearProssimeOreGiornate+" HOUR) < G.dataGiornataOrario) ) ";
				
		List<Object> aaa =  this.getSession().createSQLQuery( queryString_2 ).setParameter("idAutoveicolo", idAutoveicolo).list();
		//System.out.println("aaa: "+aaa.size());
		DammiTempoOperazione.DammiSecondi(startTime, "AutoveicoloDisponbileVendita-1");
		return aaa.size() > 0 ? true : false;
	}
}
