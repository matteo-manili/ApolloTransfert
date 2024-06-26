package com.apollon.dao.hibernate;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import com.apollon.dao.RegioniDao;
import com.apollon.model.MacroRegioni;
import com.apollon.model.Province;
import com.apollon.model.Regioni;
import com.apollon.util.DateUtil;
import com.apollon.webapp.util.controller.tariffe.MenuTariffeTransfer.TerritorioAndataArrivo;
import com.itextpdf.text.log.SysoCounter;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@EnableTransactionManagement
@Repository("RegioniDao")
public class RegioniDaoHibernate extends GenericDaoHibernate<Regioni, Long> implements RegioniDao {

	public RegioniDaoHibernate() {
		super(Regioni.class);
	}
	
	
	
	@Override
    @Transactional(readOnly = true)
	public Regioni get(Long id) {
		Regioni regioni = (Regioni) getSession().get(Regioni.class, id);
		return regioni;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Regioni> getRegioni() {
        return getSession().createCriteria(Regioni.class).addOrder(Order.asc("nomeRegione")).list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Regioni> getRegioniItaliane() {
		return getSession().createCriteria(Regioni.class).createAlias("nazione", "NAZIONE").add(Restrictions.ge("id", 1l)).add( Restrictions.eq("NAZIONE.id", 1l) )
				.addOrder(Order.asc("nomeRegione")).list();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<MacroRegioni> getMacroRegioni() {
        return getSession().createCriteria(MacroRegioni.class).addOrder(Order.asc("id")).list();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<MacroRegioni> getMacroRegioniList() {
        return getSession().createCriteria(MacroRegioni.class).add(Restrictions.ge("id", 1l)).addOrder(Order.asc("id")).list();
	}

	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Regioni> getMacroRegioni_by_id(long idMacroRegione) {
        return getSession().createCriteria(Regioni.class).add( Restrictions.eq("macroRegioni.id", idMacroRegione)).addOrder(Order.asc("nomeRegione")).list();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Regioni> getMacroRegioniNordItalia() {
        return getSession().createCriteria(Regioni.class).add( Restrictions.eq("macroRegioni.id", 1)).addOrder(Order.asc("nomeRegione")).list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Regioni> getMacroRegioniCentroItalia() {
        return getSession().createCriteria(Regioni.class).add( Restrictions.eq("macroRegioni.id", 2)).addOrder(Order.asc("nomeRegione")).list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Regioni> getMacroRegioniSudItalia() {
        return getSession().createCriteria(Regioni.class).add( Restrictions.eq("macroRegioni.id", 3)).addOrder(Order.asc("nomeRegione")).list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Regioni> getRegioniByAutista(long idAutista) {
        return getSession().createCriteria(Regioni.class).add(Restrictions.eq("autista.id", idAutista)).addOrder(Order.asc("id")).list(); 
	}
	
	@Override
	@Transactional(readOnly = true)
	public Regioni getRegioneBy_NomeRegione_e_SiglaNazione(String termNomeRegione, String termSiglaNazione) {
		Query queryObject = getSession().createQuery("SELECT REG FROM Regioni REG, Nazioni NAZ WHERE "
				+ "REG.nomeRegione LIKE :termNomeRegione "
				+ "AND REG.nazione = NAZ.id "
				+ "AND NAZ.siglaNazione LIKE :termSiglaNazione " );
		return (Regioni) queryObject.setString("termNomeRegione", termNomeRegione).setString("termSiglaNazione", termSiglaNazione).uniqueResult();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Regioni> getNomeRegioneBy_Like(String term) {
		Criterion crit1 = Restrictions. like("nomeRegione", "%"+term+"%", MatchMode.END);
		return getSession().createCriteria(Regioni.class).add(crit1) .list();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Regioni> getNomeRegioneByLikeNome_Chosen(String term) {
		Criterion criterion3 = Restrictions. like("nomeRegione", term+"%", MatchMode.START) ;
		Criteria criteria = getSession().createCriteria(Regioni.class)
				.setProjection(Projections.projectionList()
					.add(Projections.property("nomeRegione"), "nomeRegione")
					.add(Projections.property("id"), "id"))
				.setResultTransformer(Transformers.aliasToBean(Regioni.class))
				.add( criterion3 ).setMaxResults(5).addOrder( Order.asc("nomeRegione") );
				//.setFirstResult(first)
				//.setMaxResults(max);
		return criteria.list();
	}
	
	
	@Override
	@Transactional(readOnly = true)
	public Object dammiMenuTerrTariffeTransfer_LIKE_Url(String term){
		Criterion crit = Restrictions.like("url", term, MatchMode.EXACT) ;
		Object obj = getSession().createCriteria(MacroRegioni.class).add(crit).uniqueResult();
		if(obj != null){
			return obj;
		}else{
			obj = getSession().createCriteria(Regioni.class).add(crit).uniqueResult();
			if(obj != null){
				return obj;
			}else{
				obj = getSession().createCriteria(Province.class).add(crit).uniqueResult();
					return obj;
			}
		}
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Object[]> MenuTariffe_Province() {
		String queryString = "SELECT PROVINCE.id_provincia, PROVINCE.nomeProvincia, PROVINCE.url AS PROVINCE_url, "
				+ "REGIONI.id_regione, REGIONI.nomeRegione, REGIONI.url AS REGIONI_url,  "
				+ "MACRO_REGIONI.id_macro_regioni, MACRO_REGIONI.description, MACRO_REGIONI.url AS MACRO_REGIONI_url " 
				+ "FROM data_province PROVINCE INNER JOIN data_regioni REGIONI ON PROVINCE.id_regione = REGIONI.id_regione "
				+ "INNER JOIN data_macro_regioni MACRO_REGIONI ON REGIONI.id_macro_regioni = MACRO_REGIONI.id_macro_regioni "
				+ " ";
		
		List<Object[]> resultList = this.getSession().createSQLQuery( queryString ).list();
		
		for(Object[] ite_object: resultList) {
			long 	var_1 = ((BigInteger)ite_object[0]).longValue();
			String 	var_2 = (String) ite_object[1];
			String 	var_3 = (String) ite_object[2];
			long 	var_4 = ((BigInteger)ite_object[3]).longValue();
			String 	var_5 = (String) ite_object[4];
			String 	var_6 = (String) ite_object[5];
			long 	var_7 = ((BigInteger)ite_object[6]).longValue();
			String 	var_8 = (String) ite_object[7];
			String 	var_9 = (String) ite_object[8];
			//System.out.println(var_1+" | "+var_2+" | "+var_3+" | "+var_4+" | "+var_5+" | "+var_6+" | "+var_7+" | "+var_8+" | "+var_9); 
		}
		
		return resultList;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<List<Object[]>> Menu_Lista_ProvinceItalianeOrderByAbitanti_MaxResult(int maxResults, Integer MaxNumeroSettimane_OldDataRequestDistance) {
		List<List<Object[]>> listOfLists = new ArrayList<List<Object[]>>();
		Date MaxNumeroSettimane_OldDataRequestDistance_Date = DateUtil.TogliSettimaneDaAdesso(MaxNumeroSettimane_OldDataRequestDistance);
		String queryString = "SELECT "
		+ "PROVINCE.id_provincia, "
		+ "PROVINCE.nomeProvincia, "
		+ "PROVINCE.lat, "
		+ "PROVINCE.lng, "
		+ "PROVINCE.tariffaBase, "
		+ "PROVINCE.percentualeServizio, "
		+ "PROV_CONFINANTI.provincia_conf_id, "
		+ "(SELECT PROVINCE.nomeProvincia FROM data_province PROVINCE WHERE PROVINCE.id_provincia = PROV_CONFINANTI.provincia_conf_id ) AS nomeProvincia_conf, "
		+ "(SELECT PROVINCE.lat FROM data_province PROVINCE WHERE PROVINCE.id_provincia = PROV_CONFINANTI.provincia_conf_id ) AS lat_conf, "
		+ "(SELECT PROVINCE.lng FROM data_province PROVINCE WHERE PROVINCE.id_provincia = PROV_CONFINANTI.provincia_conf_id ) AS lng_conf, "
		+ "(SELECT PROVINCE.tariffaBase FROM data_province PROVINCE WHERE PROVINCE.id_provincia = PROV_CONFINANTI.provincia_conf_id ) AS tariffaBase_conf, "
		+ "(SELECT PROVINCE.numeroAbitanti FROM data_province PROVINCE WHERE PROVINCE.id_provincia = PROV_CONFINANTI.provincia_conf_id ) AS numeroAbitanti_conf, "
		+ "DISTANZE.metriDistanza "
		
		+ "FROM (SELECT * FROM data_province ORDER BY numeroAbitanti DESC LIMIT :maxResults) AS PROVINCE "
		+ "INNER JOIN data_province_confinanti AS PROV_CONFINANTI ON PROVINCE.id_provincia = PROV_CONFINANTI.provincia_id "
		+ "LEFT JOIN data_distanze AS DISTANZE ON DISTANZE.id_provinciaAndata = PROVINCE.id_provincia AND DISTANZE.id_provinciaArrivo = PROV_CONFINANTI.provincia_conf_id "
			+ "AND DISTANZE.dataRequestDistance >= :MaxNumeroSettimane_OldDataRequestDistance_Date "
		+ "ORDER BY PROVINCE.numeroAbitanti DESC, numeroAbitanti_conf DESC  ";
		
		List<Object[]> resultList = this.getSession().createSQLQuery( queryString ).setParameter("maxResults", maxResults)
				.setParameter("MaxNumeroSettimane_OldDataRequestDistance_Date", MaxNumeroSettimane_OldDataRequestDistance_Date).list();
		listOfLists.add( resultList );
		/*
		for(Object[] ite_object: resultList) {
			//provincia
			Long 		var_0 = ite_object[0] != null ? ((BigInteger)ite_object[0]).longValue() : null;
			String 		var_1 = (String)ite_object[1];
			Double 		var_2 = (Double)ite_object[2];
			Double 		var_3 = (Double)ite_object[3];
			BigDecimal 	var_4 = (BigDecimal)ite_object[4];
			Integer 	var_5 = (Integer)ite_object[5];
			// province confinanti
			Long 		var_6 = ite_object[6] != null ?((BigInteger)ite_object[6]).longValue() : null;
			String 		var_7 = (String)ite_object[7];
			Double 		var_8 = ite_object != null ? (Double)ite_object[8] : null;
			Double 		var_9 = ite_object != null ? (Double)ite_object[9] : null;
			BigDecimal 	var_10 = (BigDecimal)ite_object[10];
			BigInteger	var_11 = (BigInteger)ite_object[11];
			// distanza
			Long 		var_12 = ite_object[12] != null ? var_12 = ((BigInteger)ite_object[12]).longValue() : null;
			System.out.println(var_0+" | " +var_1+" | "+var_2+" | " +var_3+" | "+var_4+" | "
					+var_5+" | "+var_6+" | "+var_7+" | "+var_8+" | "+var_9+" | "+var_10 +" | "+var_11+" | "+var_12); 
		}
		*/
		listOfLists.add( Menu_Lista_Aeroporti(resultList, MaxNumeroSettimane_OldDataRequestDistance) );
		listOfLists.add( Menu_Lista_PortiNavali(resultList, MaxNumeroSettimane_OldDataRequestDistance) );
		listOfLists.add( Menu_Lista_Musei(resultList, MaxNumeroSettimane_OldDataRequestDistance) );
		return listOfLists;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<List<Object[]>> Menu_Lista_MacroRegioneOrderByAbitanti_MaxResult(Long idMacroRegione, int maxResults, Integer MaxNumeroSettimane_OldDataRequestDistance) {
		List<List<Object[]>> listOfLists = new ArrayList<List<Object[]>>();
		Date MaxNumeroSettimane_OldDataRequestDistance_Date = DateUtil.TogliSettimaneDaAdesso(MaxNumeroSettimane_OldDataRequestDistance);
		String queryString = "SELECT "
		+ "PROVINCE.id_provincia, "
		+ "PROVINCE.nomeProvincia, "
		+ "PROVINCE.lat, "
		+ "PROVINCE.lng, "
		+ "PROVINCE.tariffaBase, "
		+ "PROVINCE.percentualeServizio, "
		+ "PROV_CONFINANTI.provincia_conf_id, "
		+ "(SELECT PROVINCE.nomeProvincia FROM data_province PROVINCE WHERE PROVINCE.id_provincia = PROV_CONFINANTI.provincia_conf_id ) AS nomeProvincia_conf, "
		+ "(SELECT PROVINCE.lat FROM data_province PROVINCE WHERE PROVINCE.id_provincia = PROV_CONFINANTI.provincia_conf_id ) AS lat_conf, "
		+ "(SELECT PROVINCE.lng FROM data_province PROVINCE WHERE PROVINCE.id_provincia = PROV_CONFINANTI.provincia_conf_id ) AS lng_conf, "
		+ "(SELECT PROVINCE.tariffaBase FROM data_province PROVINCE WHERE PROVINCE.id_provincia = PROV_CONFINANTI.provincia_conf_id ) AS tariffaBase_conf, "
		+ "(SELECT PROVINCE.numeroAbitanti FROM data_province PROVINCE WHERE PROVINCE.id_provincia = PROV_CONFINANTI.provincia_conf_id ) AS numeroAbitanti_conf, "
		+ "DISTANZE.metriDistanza "
		
		+ "FROM (SELECT PROV.* FROM data_macro_regioni AS MACRO_REGIONI INNER JOIN data_regioni AS REGIONI "
		+ "ON MACRO_REGIONI.id_macro_regioni = :idMacroRegione AND MACRO_REGIONI.id_macro_regioni = REGIONI.id_macro_regioni "
		+ "INNER JOIN data_province AS PROV ON REGIONI.id_regione = PROV.id_regione ORDER BY PROV.numeroAbitanti DESC LIMIT :maxResults) AS PROVINCE  "
		+ "INNER JOIN data_province_confinanti AS PROV_CONFINANTI ON PROVINCE.id_provincia = PROV_CONFINANTI.provincia_id "
		+ "LEFT JOIN data_distanze AS DISTANZE ON DISTANZE.id_provinciaAndata = PROVINCE.id_provincia AND DISTANZE.id_provinciaArrivo = PROV_CONFINANTI.provincia_conf_id "
			+ "AND DISTANZE.dataRequestDistance >= :MaxNumeroSettimane_OldDataRequestDistance_Date "
		+ "ORDER BY PROVINCE.numeroAbitanti DESC, numeroAbitanti_conf DESC  ";
		
		List<Object[]> resultList = this.getSession().createSQLQuery( queryString ).setParameter("idMacroRegione", idMacroRegione)
				.setParameter("maxResults", maxResults).setParameter("MaxNumeroSettimane_OldDataRequestDistance_Date", MaxNumeroSettimane_OldDataRequestDistance_Date).list();
		listOfLists.add( resultList );
		/*
		for(Object[] ite_object: resultList) {
			//provincia
			Long 		var_0 = ite_object[0] != null ? ((BigInteger)ite_object[0]).longValue() : null;
			String 		var_1 = (String)ite_object[1];
			Double 		var_2 = (Double)ite_object[2];
			Double 		var_3 = (Double)ite_object[3];
			BigDecimal 	var_4 = (BigDecimal)ite_object[4];
			Integer 	var_5 = (Integer)ite_object[5];
			// province confinanti
			Long 		var_6 = ite_object[6] != null ?((BigInteger)ite_object[6]).longValue() : null;
			String 		var_7 = (String)ite_object[7];
			Double 		var_8 = ite_object != null ? (Double)ite_object[8] : null;
			Double 		var_9 = ite_object != null ? (Double)ite_object[9] : null;
			BigDecimal 	var_10 = (BigDecimal)ite_object[10];
			BigInteger	var_11 = (BigInteger)ite_object[11];
			// distanza
			Long 		var_12 = ite_object[12] != null ? var_12 = ((BigInteger)ite_object[12]).longValue() : null;
			System.out.println(var_0+" | " +var_1+" | "+var_2+" | " +var_3+" | "+var_4+" | "
					+var_5+" | "+var_6+" | "+var_7+" | "+var_8+" | "+var_9+" | "+var_10 +" | "+var_11+" | "+var_12); 
		}
		*/
		listOfLists.add( Menu_Lista_Aeroporti(resultList, MaxNumeroSettimane_OldDataRequestDistance) );
		listOfLists.add( Menu_Lista_PortiNavali(resultList, MaxNumeroSettimane_OldDataRequestDistance) );
		listOfLists.add( Menu_Lista_Musei(resultList, MaxNumeroSettimane_OldDataRequestDistance) );
		return listOfLists;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<List<Object[]>> Menu_Lista_RegioneOrderByAbitanti_MaxResult(Long idRegione, int maxResults, Integer MaxNumeroSettimane_OldDataRequestDistance) {
		List<List<Object[]>> listOfLists = new ArrayList<List<Object[]>>();
		Date MaxNumeroSettimane_OldDataRequestDistance_Date = DateUtil.TogliSettimaneDaAdesso(MaxNumeroSettimane_OldDataRequestDistance);
		String queryString = "SELECT "
		+ "PROVINCE.id_provincia, "
		+ "PROVINCE.nomeProvincia, "
		+ "PROVINCE.lat, "
		+ "PROVINCE.lng, "
		+ "PROVINCE.tariffaBase, "
		+ "PROVINCE.percentualeServizio, "
		+ "PROV_CONFINANTI.provincia_conf_id, "
		+ "(SELECT PROVINCE.nomeProvincia FROM data_province PROVINCE WHERE PROVINCE.id_provincia = PROV_CONFINANTI.provincia_conf_id ) AS nomeProvincia_conf, "
		+ "(SELECT PROVINCE.lat FROM data_province PROVINCE WHERE PROVINCE.id_provincia = PROV_CONFINANTI.provincia_conf_id ) AS lat_conf, "
		+ "(SELECT PROVINCE.lng FROM data_province PROVINCE WHERE PROVINCE.id_provincia = PROV_CONFINANTI.provincia_conf_id ) AS lng_conf, "
		+ "(SELECT PROVINCE.tariffaBase FROM data_province PROVINCE WHERE PROVINCE.id_provincia = PROV_CONFINANTI.provincia_conf_id ) AS tariffaBase_conf, "
		+ "(SELECT PROVINCE.numeroAbitanti FROM data_province PROVINCE WHERE PROVINCE.id_provincia = PROV_CONFINANTI.provincia_conf_id ) AS numeroAbitanti_conf, "
		+ "DISTANZE.metriDistanza "
		
		+ "FROM (SELECT PROV.* FROM data_regioni AS REGIONI INNER JOIN data_province AS PROV "
			+ "ON REGIONI.id_regione = :idRegione AND REGIONI.id_regione = PROV.id_regione ORDER BY PROV.numeroAbitanti DESC LIMIT :maxResults) AS PROVINCE "
		+ "INNER JOIN data_province_confinanti AS PROV_CONFINANTI ON PROVINCE.id_provincia = PROV_CONFINANTI.provincia_id "
		+ "LEFT JOIN data_distanze AS DISTANZE ON DISTANZE.id_provinciaAndata = PROVINCE.id_provincia AND DISTANZE.id_provinciaArrivo = PROV_CONFINANTI.provincia_conf_id "
			+ "AND DISTANZE.dataRequestDistance >= :MaxNumeroSettimane_OldDataRequestDistance_Date "
		+ "ORDER BY PROVINCE.numeroAbitanti DESC, numeroAbitanti_conf DESC  ";
		
		List<Object[]> resultList = this.getSession().createSQLQuery( queryString ).setParameter("idRegione", idRegione)
				.setParameter("maxResults", maxResults).setParameter("MaxNumeroSettimane_OldDataRequestDistance_Date", MaxNumeroSettimane_OldDataRequestDistance_Date).list();
		listOfLists.add( resultList );
		/*
		for(Object[] ite_object: resultList) {
			//provincia
			Long 		var_0 = ite_object[0] != null ? ((BigInteger)ite_object[0]).longValue() : null;
			String 		var_1 = (String)ite_object[1];
			Double 		var_2 = (Double)ite_object[2];
			Double 		var_3 = (Double)ite_object[3];
			BigDecimal 	var_4 = (BigDecimal)ite_object[4];
			Integer 	var_5 = (Integer)ite_object[5];
			// province confinanti
			Long 		var_6 = ite_object[6] != null ?((BigInteger)ite_object[6]).longValue() : null;
			String 		var_7 = (String)ite_object[7];
			Double 		var_8 = ite_object != null ? (Double)ite_object[8] : null;
			Double 		var_9 = ite_object != null ? (Double)ite_object[9] : null;
			BigDecimal 	var_10 = (BigDecimal)ite_object[10];
			BigInteger	var_11 = (BigInteger)ite_object[11];
			// distanza
			Long 		var_12 = ite_object[12] != null ? var_12 = ((BigInteger)ite_object[12]).longValue() : null;
			System.out.println(var_0+" | " +var_1+" | "+var_2+" | " +var_3+" | "+var_4+" | "
					+var_5+" | "+var_6+" | "+var_7+" | "+var_8+" | "+var_9+" | "+var_10 +" | "+var_11+" | "+var_12); 
		}
		*/
		listOfLists.add( Menu_Lista_Aeroporti(resultList, MaxNumeroSettimane_OldDataRequestDistance) );
		listOfLists.add( Menu_Lista_PortiNavali(resultList, MaxNumeroSettimane_OldDataRequestDistance) );
		listOfLists.add( Menu_Lista_Musei(resultList, MaxNumeroSettimane_OldDataRequestDistance) );
		return listOfLists;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<List<Object[]>> Menu_Lista_ProvinciaOrderByAbitanti_MaxResult(Long idProvincia, Integer MaxNumeroSettimane_OldDataRequestDistance) {
		List<List<Object[]>> listOfLists = new ArrayList<List<Object[]>>();
		Date MaxNumeroSettimane_OldDataRequestDistance_Date = DateUtil.TogliSettimaneDaAdesso(MaxNumeroSettimane_OldDataRequestDistance);
		String queryString = "SELECT "
		+ "PROVINCE.id_provincia, "
		+ "PROVINCE.nomeProvincia, "
		+ "PROVINCE.lat, "
		+ "PROVINCE.lng, "
		+ "PROVINCE.tariffaBase, "
		+ "PROVINCE.percentualeServizio, "
		+ "PROV_CONFINANTI.provincia_conf_id, "
		+ "(SELECT PROVINCE.nomeProvincia FROM data_province PROVINCE WHERE PROVINCE.id_provincia = PROV_CONFINANTI.provincia_conf_id ) AS nomeProvincia_conf, "
		+ "(SELECT PROVINCE.lat FROM data_province PROVINCE WHERE PROVINCE.id_provincia = PROV_CONFINANTI.provincia_conf_id ) AS lat_conf, "
		+ "(SELECT PROVINCE.lng FROM data_province PROVINCE WHERE PROVINCE.id_provincia = PROV_CONFINANTI.provincia_conf_id ) AS lng_conf, "
		+ "(SELECT PROVINCE.tariffaBase FROM data_province PROVINCE WHERE PROVINCE.id_provincia = PROV_CONFINANTI.provincia_conf_id ) AS tariffaBase_conf, "
		+ "(SELECT PROVINCE.numeroAbitanti FROM data_province PROVINCE WHERE PROVINCE.id_provincia = PROV_CONFINANTI.provincia_conf_id ) AS numeroAbitanti_conf, "
		+ "DISTANZE.metriDistanza "
		
		+ "FROM data_province AS PROVINCE "
		+ "INNER JOIN data_province_confinanti AS PROV_CONFINANTI ON PROVINCE.id_provincia = :idProvincia AND PROVINCE.id_provincia = PROV_CONFINANTI.provincia_id "
		+ "LEFT JOIN data_distanze AS DISTANZE ON DISTANZE.id_provinciaAndata = PROVINCE.id_provincia AND DISTANZE.id_provinciaArrivo = PROV_CONFINANTI.provincia_conf_id "
			+ "AND DISTANZE.dataRequestDistance >= :MaxNumeroSettimane_OldDataRequestDistance_Date "
		+ "ORDER BY PROVINCE.numeroAbitanti DESC, numeroAbitanti_conf DESC  ";
		
		List<Object[]> resultList = this.getSession().createSQLQuery( queryString ).setParameter("idProvincia", idProvincia)
				.setParameter("MaxNumeroSettimane_OldDataRequestDistance_Date", MaxNumeroSettimane_OldDataRequestDistance_Date).list();
		listOfLists.add( resultList );
		/*
		for(Object[] ite_object: resultList) {
			//provincia
			Long 		var_0 = ite_object[0] != null ? ((BigInteger)ite_object[0]).longValue() : null;
			String 		var_1 = (String)ite_object[1];
			Double 		var_2 = (Double)ite_object[2];
			Double 		var_3 = (Double)ite_object[3];
			BigDecimal 	var_4 = (BigDecimal)ite_object[4];
			Integer 	var_5 = (Integer)ite_object[5];
			// province confinanti
			Long 		var_6 = ite_object[6] != null ?((BigInteger)ite_object[6]).longValue() : null;
			String 		var_7 = (String)ite_object[7];
			Double 		var_8 = ite_object != null ? (Double)ite_object[8] : null;
			Double 		var_9 = ite_object != null ? (Double)ite_object[9] : null;
			BigDecimal 	var_10 = (BigDecimal)ite_object[10];
			BigInteger	var_11 = (BigInteger)ite_object[11];
			// distanza
			Long 		var_12 = ite_object[12] != null ? var_12 = ((BigInteger)ite_object[12]).longValue() : null;
			System.out.println(var_0+" | " +var_1+" | "+var_2+" | " +var_3+" | "+var_4+" | "
					+var_5+" | "+var_6+" | "+var_7+" | "+var_8+" | "+var_9+" | "+var_10 +" | "+var_11+" | "+var_12); 
		}
		*/
		listOfLists.add( Menu_Lista_Aeroporti(resultList, MaxNumeroSettimane_OldDataRequestDistance) );
		listOfLists.add( Menu_Lista_PortiNavali(resultList, MaxNumeroSettimane_OldDataRequestDistance) );
		listOfLists.add( Menu_Lista_Musei(resultList, MaxNumeroSettimane_OldDataRequestDistance) );
		return listOfLists;
	}
	
	
	@SuppressWarnings("unchecked")
	private List<Object[]> Menu_Lista_Aeroporti(List<Object[]> provinceList, Integer MaxNumeroSettimane_OldDataRequestDistance) {
		Date MaxNumeroSettimane_OldDataRequestDistance_Date = DateUtil.TogliSettimaneDaAdesso(MaxNumeroSettimane_OldDataRequestDistance);
		List<Long> ProvinceListLong = new ArrayList<Long>();
		Long idProvinciaAndata = null;
		for(Object[] ite_object: provinceList) {
			if( !ProvinceListLong.contains(((BigInteger)ite_object[0]).longValue())) {
				ProvinceListLong.add(((BigInteger)ite_object[0]).longValue());
				idProvinciaAndata = ((BigInteger)ite_object[0]).longValue();
			}
			ProvinceListLong.add(((BigInteger)ite_object[6]).longValue());
		}
		String queryString = "SELECT "
		+ ":idProvinciaAndata AS idProvinciaAndata, "
		+ "AEROPORTI.id_aeroporto, "
		+ "AEROPORTI.nomeAeroporto, "
		+ "AEROPORTI.lat, "
		+ "AEROPORTI.lng, "
		+ "PROVINCE.tariffaBase, "
		+ "DISTANZE.metriDistanza "
		+ "FROM data_province AS PROVINCE INNER JOIN data_comuni AS COMUNI ON PROVINCE.id_provincia IN (:ProvinceListLong) AND PROVINCE.id_provincia = COMUNI.id_provincia "
		+ "INNER JOIN data_aeroporti AS AEROPORTI ON COMUNI.id_comune = AEROPORTI.id_comune "
		+ "LEFT JOIN data_distanze AS DISTANZE ON DISTANZE.id_provinciaAndata = :idProvinciaAndata AND DISTANZE.id_aeroportoArrivo = AEROPORTI.id_aeroporto "
			+ "AND DISTANZE.dataRequestDistance >= :MaxNumeroSettimane_OldDataRequestDistance_Date "
		+ "ORDER BY AEROPORTI.numeroVoliAnno DESC ";
		
		List<Object[]> resultList = this.getSession().createSQLQuery( queryString ).setParameter("idProvinciaAndata", idProvinciaAndata).setParameterList("ProvinceListLong", ProvinceListLong)
				.setParameter("MaxNumeroSettimane_OldDataRequestDistance_Date", MaxNumeroSettimane_OldDataRequestDistance_Date).list();
		/*
		for(Object[] ite_object: resultList) {
			//aeroporti
			Long 		var_0 = ite_object[0] != null ? ((BigInteger)ite_object[0]).longValue() : null;
			Long 		var_1 = ite_object[1] != null ? ((BigInteger)ite_object[1]).longValue() : null;
			String 		var_2 = (String)ite_object[2];
			Double 		var_3 = (Double)ite_object[3];
			Double 		var_4 = (Double)ite_object[4];
			BigDecimal 	var_5 = (BigDecimal)ite_object[5];
			// distanza
			Long 		var_6 = ite_object[6] != null ? ((BigInteger)ite_object[6]).longValue() : null;
			System.out.println(var_0+" | " +var_1+" | "+var_2+" | " +var_3+" | "+var_4+" | "+var_5+" | "+var_6); 
		}
		*/
		return resultList;
	}
	
	
	@SuppressWarnings("unchecked")
	private List<Object[]> Menu_Lista_PortiNavali(List<Object[]> provinceList, Integer MaxNumeroSettimane_OldDataRequestDistance) {
		Date MaxNumeroSettimane_OldDataRequestDistance_Date = DateUtil.TogliSettimaneDaAdesso(MaxNumeroSettimane_OldDataRequestDistance);
		List<Long> ProvinceListLong = new ArrayList<Long>();
		Long idProvinciaAndata = null;
		for(Object[] ite_object: provinceList) {
			if( !ProvinceListLong.contains( ((BigInteger)ite_object[0]).longValue() )) {
				ProvinceListLong.add(((BigInteger)ite_object[0]).longValue());
				idProvinciaAndata = ((BigInteger)ite_object[0]).longValue();
			}
			ProvinceListLong.add(((BigInteger)ite_object[6]).longValue());
		}
		String queryString = "SELECT "
		+ ":idProvinciaAndata AS idProvinciaAndata, "
		+ "PORTI_NAV.id_porto_navale, "
		+ "PORTI_NAV.nomePorto, "
		+ "PORTI_NAV.lat, "
		+ "PORTI_NAV.lng, "
		+ "PROVINCE.tariffaBase, "
		+ "DISTANZE.metriDistanza "
		+ "FROM data_province AS PROVINCE INNER JOIN data_comuni AS COMUNI ON PROVINCE.id_provincia IN (:ProvinceListLong) AND PROVINCE.id_provincia = COMUNI.id_provincia "
		+ "INNER JOIN data_porti_navali AS PORTI_NAV ON COMUNI.id_comune = PORTI_NAV.id_comune "
		+ "LEFT JOIN data_distanze AS DISTANZE ON DISTANZE.id_provinciaAndata = :idProvinciaAndata AND DISTANZE.id_portoArrivo = PORTI_NAV.id_porto_navale "
			+ "AND DISTANZE.dataRequestDistance >= :MaxNumeroSettimane_OldDataRequestDistance_Date "
		+ "ORDER BY PORTI_NAV.numeroPartenzeAnno DESC ";
		
		List<Object[]> resultList = this.getSession().createSQLQuery( queryString ).setParameter("idProvinciaAndata", idProvinciaAndata).setParameterList("ProvinceListLong", ProvinceListLong)
				.setParameter("MaxNumeroSettimane_OldDataRequestDistance_Date", MaxNumeroSettimane_OldDataRequestDistance_Date).list();
		/*
		for(Object[] ite_object: resultList) {
			//porti navali
			Long 		var_0 = ite_object[0] != null ? ((BigInteger)ite_object[0]).longValue() : null;
			Long 		var_1 = ite_object[1] != null ? ((BigInteger)ite_object[1]).longValue() : null;
			String 		var_2 = (String)ite_object[2];
			Double 		var_3 = (Double)ite_object[3];
			Double 		var_4 = (Double)ite_object[4];
			BigDecimal 	var_5 = (BigDecimal)ite_object[5];
			// distanza
			Long 		var_6 = ite_object[6] != null ? ((BigInteger)ite_object[6]).longValue() : null;
			System.out.println(var_0+" | " +var_1+" | "+var_2+" | " +var_3+" | "+var_4+" | "+var_5+" | "+var_6); 
		}
		*/
		return resultList;
	}
	
	@SuppressWarnings("unchecked")
	private List<Object[]> Menu_Lista_Musei(List<Object[]> provinceList, Integer MaxNumeroSettimane_OldDataRequestDistance) {
		Date MaxNumeroSettimane_OldDataRequestDistance_Date = DateUtil.TogliSettimaneDaAdesso(MaxNumeroSettimane_OldDataRequestDistance);
		List<Long> ProvinceListLong = new ArrayList<Long>();
		Long idProvinciaAndata = null;
		for(Object[] ite_object: provinceList) {
			if( !ProvinceListLong.contains( ((BigInteger)ite_object[0]).longValue() )) {
				ProvinceListLong.add(((BigInteger)ite_object[0]).longValue());
				idProvinciaAndata = ((BigInteger)ite_object[0]).longValue();
			}
			ProvinceListLong.add(((BigInteger)ite_object[6]).longValue());
		}
		String queryString = "SELECT "
		+ ":idProvinciaAndata AS idProvinciaAndata, "
		+ "MUSEI.id_museo, "
		+ "MUSEI.nomeMuseo, "
		+ "MUSEI.lat, "
		+ "MUSEI.lng, "
		+ "PROVINCE.tariffaBase, "
		+ "DISTANZE.metriDistanza "
		+ "FROM data_province AS PROVINCE INNER JOIN data_comuni AS COMUNI ON PROVINCE.id_provincia IN (:ProvinceListLong) AND PROVINCE.id_provincia = COMUNI.id_provincia "
		+ "INNER JOIN data_musei AS MUSEI ON COMUNI.id_comune = MUSEI.id_comune "
		+ "LEFT JOIN data_distanze AS DISTANZE ON DISTANZE.id_provinciaAndata = :idProvinciaAndata AND DISTANZE.id_museoArrivo = MUSEI.id_museo "
			+ "AND DISTANZE.dataRequestDistance >= :MaxNumeroSettimane_OldDataRequestDistance_Date "
		+ "ORDER BY MUSEI.numeroVisiteAnno DESC ";
		
		List<Object[]> resultList = this.getSession().createSQLQuery( queryString ).setParameter("idProvinciaAndata", idProvinciaAndata).setParameterList("ProvinceListLong", ProvinceListLong)
				.setParameter("MaxNumeroSettimane_OldDataRequestDistance_Date", MaxNumeroSettimane_OldDataRequestDistance_Date).list();
		/*
		for(Object[] ite_object: resultList) {
			//porti navali
			Long 		var_0 = ite_object[0] != null ? ((BigInteger)ite_object[0]).longValue() : null;
			Long 		var_1 = ite_object[1] != null ? ((BigInteger)ite_object[1]).longValue() : null;
			String 		var_2 = (String)ite_object[2];
			Double 		var_3 = (Double)ite_object[3];
			Double 		var_4 = (Double)ite_object[4];
			BigDecimal 	var_5 = (BigDecimal)ite_object[5];
			// distanza
			Long 		var_6 = ite_object[6] != null ? ((BigInteger)ite_object[6]).longValue() : null;
			System.out.println(var_0+" | " +var_1+" | "+var_2+" | " +var_3+" | "+var_4+" | "+var_5+" | "+var_6); 
		}
		*/
		return resultList;
	}
	
	
	@Override
	@Transactional
	public Regioni saveRegioni(Regioni regioni) throws DataIntegrityViolationException, HibernateJdbcException {
		getSession().saveOrUpdate(regioni);
		//getSession().flush();
		return regioni;
	}
	
	@Override
	@Transactional
	public MacroRegioni saveMacroRegioni(MacroRegioni macroRegioni) throws DataIntegrityViolationException, HibernateJdbcException {
		getSession().saveOrUpdate(macroRegioni);
		//getSession().flush();
		return macroRegioni;
	}

}
