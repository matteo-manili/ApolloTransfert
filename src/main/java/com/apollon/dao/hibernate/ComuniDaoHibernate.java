package com.apollon.dao.hibernate;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.StringType;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import com.apollon.dao.ComuniDao;
import com.apollon.model.Comuni;
import com.apollon.model.CoordGeoProvince;
import com.apollon.model.Province;
import com.apollon.model.Regioni;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@EnableTransactionManagement
@Repository("ComuniDao")
public class ComuniDaoHibernate extends GenericDaoHibernate<Comuni, Long> implements ComuniDao {

	public ComuniDaoHibernate() {
		super(Comuni.class);
	}
	
	
	@Override
    @Transactional(readOnly = true)
	public Comuni get(Long id){
		Comuni comuni = (Comuni) getSession().get(Comuni.class, id);
		return comuni;
	}
	
	@Override
    @Transactional(readOnly = true)
	public long get_idRegione(long idComune){
		return (long) getSession().createCriteria(Comuni.class)
				.setProjection(Projections.projectionList()
			.add(Projections.property("regioni.id")))
			.add( Restrictions.eq("id", idComune) ).uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<Comuni> getComuni() {
        return getSession().createCriteria(Comuni.class).list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<Comuni> getComuniByIdProvincia(Long idProvincia) {
        return getSession().createCriteria(Comuni.class).add(Restrictions.eq("province.id", idProvincia)).addOrder(Order.asc("nomeComune")).list();
	}
	
	@Override
	@Transactional(readOnly = true)
	public Comuni getComuniByNomeComune_Equal(String nomeComune, String siglaProvincia) {
		/*
		String queryStr = "SELECT nomeComune FROM data_comuni WHERE nomeComune = :nomeComune collate utf8mb4_bin ";
		Query query = getSession().createSQLQuery( queryStr );
		query.setParameter("nomeComune", nomeComune);
		Object aa = query.uniqueResult();
		*/
		// così distingue Paternò da Paterno e con l'upper non fa distinzione tra Paternò e paterno
		// igLa tua posizionenoreCase può essere usato solo nel Restrictions.eq ma non nel Restrictions.sqlRestriction, per questo nel primo uso l'upperCase
		log.debug( "nomeComune: "+nomeComune +" siglaProvincia: "+siglaProvincia );
		/*
		Comuni comune = null;
		if(!siglaProvincia.equals(Constants.PROVINCIA_STRANIERA)) {
			comune = (Comuni) getSession().createCriteria(Comuni.class).createAlias("province", "PROV")
					.add(Restrictions.sqlRestriction("UPPER(nomeComune) = ? collate utf8mb4_bin ", nomeComune.toUpperCase(), new StringType()))
					.add(Restrictions.eq("PROV.siglaProvincia", siglaProvincia).ignoreCase()).uniqueResult();
		}else if( nomeComune == null && siglaProvincia != null ){
			comune = (Comuni) getSession().createCriteria(Comuni.class).createAlias("province", "PROV")
					.add(Restrictions.sqlRestriction("UPPER(nomeComune) = ? collate utf8mb4_bin ", Constants.COMUNE_STRANIERO, new StringType()))
					.add(Restrictions.eq("PROV.siglaProvincia", siglaProvincia).ignoreCase()).uniqueResult();
		}
		*/
		
		/*
		Comuni comune = (Comuni) getSession().createCriteria(Comuni.class).createAlias("province", "PROV")
				.add(Restrictions.sqlRestriction("UPPER(nomeComune) = ? collate utf8mb4_bin ", nomeComune.toUpperCase(), new StringType()))
				.add(Restrictions.eq("PROV.siglaProvincia", siglaProvincia).ignoreCase()).uniqueResult();
		 */	
		
		String queryString_1 = "SELECT COM.id_comune, COM.isola AS isolaCOM, PROV.id_provincia, PROV.isola AS isolaPROV, PROV.percentualeServizio, REG.id_regione, REG.isola AS isolaREG "
				+ "FROM data_comuni COM, data_province PROV, data_regioni REG  "
				+ "WHERE UPPER(COM.nomeComune) = :NOME_COMUNE "
				+ "AND PROV.siglaProvincia = :SIGLA_PROV "
				+ "AND COM.id_provincia = PROV.id_provincia AND PROV.id_regione = REG.id_regione ";
		Object[] result = (Object[]) this.getSession().createSQLQuery( queryString_1 )
				.setParameter("NOME_COMUNE", nomeComune.toUpperCase()).setParameter("SIGLA_PROV", siglaProvincia).uniqueResult();
		
		
		Regioni regione = new Regioni();
		regione.setId( ((BigInteger)result[5]).longValue() );
		regione.setIsola( (Boolean)result[6] );
		
		Province provincia = new Province();
		provincia.setId( ((BigInteger)result[2]).longValue() );
		provincia.setIsola( (Boolean)result[3] );
		provincia.setPercentualeServizio( (Integer)result[4] );
		provincia.setRegioni(regione);
		
		Comuni comune = new Comuni();
		comune.setId( ((BigInteger)result[0]).longValue() );
		comune.setIsola( (Boolean)result[1] );
		comune.setProvince(provincia);
		comune.setRegioni(regione);
		
		/*
		if(comune != null){
			return comune;
		}else{
			return null;
		}
		*/
		return comune;
		
		
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	/**
	 * Ho messo list perché ci sono alcuni (pochi) comuni che hanno lo stesso nome ma cmq. sempre di province diverse
	 */
	public List<Comuni> getComuniByNomeComune_Equal(String nomeComune) {
		// così distingue Paternò da Paterno e con l'upper non fa distinzione tra Paternò e paterno
		// ignoreCase può essere usato solo nel Restrictions.eq ma non nel Restrictions.sqlRestriction, per questo nel primo uso l'upperCase
		List<Comuni> comuniList = getSession().createCriteria(Comuni.class)
			.add(Restrictions.sqlRestriction("UPPER(nomeComune) = ? collate utf8mb4_bin ", nomeComune.toUpperCase(), new StringType())).list();
		if(comuniList != null){
			return comuniList;
		}else{
			return null;
		}
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Comuni> getComuniByNomeComune_Equal_List(String term) {
		Criterion crit1 = Restrictions.eq("nomeComune", term).ignoreCase() ;
		return getSession().createCriteria(Comuni.class).add(crit1)
				.addOrder(Order.asc("nomeComune")).list();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Comuni> getNomeComuneBy_Like(String term) {
		Criterion crit1 = Restrictions.like("nomeComune", "%"+term+"%").ignoreCase() ;
		return getSession().createCriteria(Comuni.class).add(crit1)
				.addOrder(Order.asc("nomeComune")).list();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Comuni> getNomeComuneByLikeNome_Chosen(String term) {
		Criterion criterion3 = Restrictions.like("nomeComune", term+"%", MatchMode.START) ;
		Criteria criteria = getSession().createCriteria(Comuni.class)
			.setProjection(Projections.projectionList()
				.add(Projections.property("nomeComune"), "nomeComune")
				.add(Projections.property("id"), "id"))
			.setResultTransformer(Transformers.aliasToBean(Comuni.class))
			.add( criterion3 ).setMaxResults(5).addOrder( Order.asc("nomeComune") );
			//.setFirstResult(first)
			//.setMaxResults(max);
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Long> getListComuniByRegione_soloID(long idRegione) {
        Criterion criterion1 = Restrictions.eq("regioni.id", idRegione);
		Criteria criteria = getSession().createCriteria(Comuni.class)
				.setProjection(Projections.projectionList()
					.add(Projections.property("id"), "id")).add( criterion1 );
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Long> getListComuniByProvincia_soloID(long idProvincia) {
        Criterion criterion1 = Restrictions.eq("province.id", idProvincia);
		Criteria criteria = getSession().createCriteria(Comuni.class)
				.setProjection(Projections.projectionList()
					.add(Projections.property("id"), "id")).add( criterion1 );
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Long> getListComuniByComuneAppartenente_soloID_Suggerimenti(long idComune_Provincia) {
        Criterion criterion1 = Restrictions.eq("province.id", idComune_Provincia);
		Criteria criteria = getSession().createCriteria(Comuni.class)
				.setProjection(Projections.projectionList()
					.add(Projections.property("id"), "id")).add( criterion1 );
		return criteria.list();
	}
	
	
	String TABELLE_ALTRE_COMUNI_2 = 
			"SELECT com.nomeComune AS COM, prov.nomeProvincia AS PROV, reg.nomeRegione AS REG "
			+ "FROM data_comuni com, data_province prov, data_regioni reg "
			+ "WHERE com.id_provincia = prov.id_provincia "
			+ "AND prov.id_regione = reg.id_regione "
			+ "AND com.nomeComune NOT IN (SELECT comune FROM italy_cities) "
			+ "ORDER BY com.nomeComune ASC ";
	
	String TABELLE_ALTRE_COMUNI_1 = 
			"SELECT com.comune AS COM, com.provincia AS PROV, reg.regione AS REG "
			+ "FROM italy_cities com, italy_provincies prov, italy_regions reg "
			+ "WHERE com.provincia = prov.sigla "
			+ "AND prov.id_regione = reg.id_regione "
			+ "AND com.comune NOT IN (SELECT nomeComune FROM data_comuni) "
			+ "ORDER BY com.comune ASC ";
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Object> getComuni_Altri() { 
		Query query = getSession().createSQLQuery( TABELLE_ALTRE_COMUNI_1 );
		List<Object> list = query. list();
		/*
		for(Object ite: list){
			Object[] we = (Object[]) ite;
		}
		*/
		return list;
	}
	
	@Override
	@Transactional(readOnly = false)
	public int Update_Clear_Column_Catasto(){
		String aaa = "UPDATE data_comuni SET catasto = '' ";
			return getSession().createSQLQuery( aaa ).executeUpdate();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Object> getComuni_Altri_Tutti() { 
		String aaa = "SELECT com.comune AS COM, com.provincia AS PROV, reg.regione AS REG, cap.cap AS CAP, com.istat AS ISTAT "
			+ "FROM italy_cities com, italy_provincies prov, italy_regions reg, italy_cap cap "
			+ "WHERE com.istat = cap.istat "
			+ "AND com.provincia = prov.sigla "
			+ "AND prov.id_regione = reg.id_regione ";
		Query query = getSession().createSQLQuery( aaa );
		List<Object> list = query. list();
		/*
		for(Object ite: list){
			Object[] we = (Object[]) ite;
		}
		*/
		return list;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Object> getComuni_Duplicati() { 
		String ComuniDuplicati = "SELECT nomeComune, COUNT(*) "
				+ "FROM data_comuni "
				+ "GROUP BY nomeComune "
				+ "HAVING COUNT(*) > 1";
		Query query = getSession().createSQLQuery( ComuniDuplicati );
		List<Object> list = query. list();
		/*
		for(Object ite: list){
			Object[] we = (Object[]) ite;
		}
		*/
		return list;
	}
	
	
	
	
	@Override
	@Transactional
	public Comuni saveComuni(Comuni comuni) throws DataIntegrityViolationException, HibernateJdbcException {
		getSession().saveOrUpdate(comuni);
		//getSession().flush();
		return comuni;
	}


	
	

}
