package com.apollon.dao.hibernate;

import java.util.ArrayList;
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
import com.apollon.dao.ProvinceDao;
import com.apollon.model.Province;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@EnableTransactionManagement
@Repository("ProvinceDao")
public class ProvinceDaoHibernate extends GenericDaoHibernate<Province, Long> implements ProvinceDao {

	public ProvinceDaoHibernate() {
		super(Province.class);
	}
	
	
	
	@Override
    @Transactional(readOnly = true)
	public Province get(Long id){
		Province province = (Province) getSession().get(Province.class, id);
		return province;
	}
	
	
	@Override
    @Transactional(readOnly = true)
	public long get_idRegione(long idprovincia){
		return (long) getSession().createCriteria(Province.class)
				.setProjection(Projections.projectionList()
			.add(Projections.property("regioni.id")))
			.add( Restrictions.eq("id", idprovincia) ).uniqueResult();
	}
	
	
	
	
	@Transactional(readOnly = true)
	@Override
	public long getPercentualeServizioMediaProvincia(){
		double valueDouble = (double)getSession().createCriteria(Province.class)
				.setProjection(Projections.avg("percentualeServizio")).uniqueResult();
		return (new Double( valueDouble )).longValue(); //129
	}
	
	
	@Transactional(readOnly = true)
	@Override
	public long getTariffaBaseMediaProvincia(){
		double valueDouble = (double)getSession().createCriteria(Province.class)
				.setProjection(Projections.avg("tariffaBase")).uniqueResult();
		return (new Double( valueDouble )).longValue(); //129
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Province> getProvince() {
        return getSession().createCriteria(Province.class).addOrder(Order.asc("nomeProvincia")).list();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Province> getProvinceByIdRegione(Long idRegione) {
        return getSession().createCriteria(Province.class).add(Restrictions.eq("regioni.id", idRegione))
        		.addOrder(Order.asc("nomeProvincia")).list();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<String> getProvince_SigleProvinciaList(){
		String Query = "SELECT PROV.siglaProvincia "
			+ "FROM Province PROV "
			+ "WHERE PROV.id > 0 "
			+ "ORDER BY PROV.siglaProvincia ASC ";
		List<Object[]> siglePrvinceList = this.getSession().createQuery( Query ).list();
		List<String> ListSiglaProvince = new ArrayList<String>();
		for(Object ite: siglePrvinceList){
			ListSiglaProvince.add( (String)ite );
		}
		return ListSiglaProvince;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Province> getProvince_order_Abitanti() {
        return getSession().createCriteria(Province.class)
        		.add(Restrictions.gt("id", 0l)).addOrder(Order.desc("numeroAbitanti")).list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Province> getProvinceItaliane_order_Abitanti(int maxResults) {
        return getSession().createCriteria(Province.class).createAlias("regioni", "REGIONI").createAlias("REGIONI.nazione", "NAZIONE")
        		.add(Restrictions.gt("id", 0l)).add( Restrictions.eq("NAZIONE.id", 1l) ).addOrder(Order.desc("numeroAbitanti")).setMaxResults(maxResults).list();
	}
	
	
	/*
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Province> getProvinceOrderAbitanti_By_MacroRegione(Long idMacroRegione) {
        return getSession().createCriteria(Province.class)
        		.createAlias("regioni", "REGIONI").createAlias("REGIONI.macroRegioni", "MACRO_REGIONI")
        		.add(Restrictions.eq("MACRO_REGIONI.id", idMacroRegione))
        		.addOrder(Order.desc("numeroAbitanti")).list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Province> getProvinceOrderAbitanti_By_MacroRegione(Long idMacroRegione, int maxResults) {
        return getSession().createCriteria(Province.class).createAlias("regioni", "REGIONI").createAlias("REGIONI.macroRegioni", "MACRO_REGIONI")
        		.add(Restrictions.eq("MACRO_REGIONI.id", idMacroRegione)).addOrder(Order.desc("numeroAbitanti")).setMaxResults(maxResults).list();
	}
	*/
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Province> getNomeProvinciaBy_Like(String term) {
		Criterion crit1 = Restrictions.like("nomeProvincia", term+"%", MatchMode.START);
		Criteria criteria = getSession().createCriteria(Province.class).add( crit1 )
				.setMaxResults(5).addOrder( Order.asc("nomeProvincia") );
		return criteria.list();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Province> getNomeProvinciaBy_Like_NomeRegione(String term) {
		Criterion crit1 = Restrictions.like("regioni.nomeRegione", term+"%", MatchMode.START);
		Criteria criteria =  getSession().createCriteria(Province.class).createAlias("regioni", "regioni")
				.add( crit1 ).addOrder(Order.asc("regioni.nomeRegione")).addOrder(Order.asc("nomeProvincia"));
		return criteria.list();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public Province getProvinciaBy_NomeProvincia(String term) {
		Query queryObject = getSession().createQuery("FROM Province WHERE nomeProvincia LIKE :term");
		List<Province> listProv = queryObject.setString("term", "%"+term).list();
		if(listProv != null && listProv.size() > 0){
			return listProv.get(0);
		}else{
			return null;
		}
	}
	

	@Override
	@Transactional(readOnly = true)
	public Province getProvinciaBy_SiglaProvincia(String term) {
		Query queryObject = getSession().createQuery("FROM Province WHERE siglaProvincia LIKE :term");
		return (Province) queryObject.setString("term", term).uniqueResult();
	}
	
	
	@Override
	@Transactional(readOnly = true)
	public Province getProvinciaBy_SiglaProvincia_e_idRegione(String termSiglaProvincia, long idRegione) {
		Query queryObject = getSession().createQuery("SELECT PROV FROM Province PROV, Regioni REG WHERE "
				+ "PROV.siglaProvincia LIKE :termSiglaProvincia "
				+ "AND PROV.regioni = REG.id "
				+ "AND REG.id = :idRegione " );
		return (Province) queryObject.setString("termSiglaProvincia", termSiglaProvincia).setLong("idRegione", idRegione).uniqueResult();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Province> getNomeProvinceByLikeNome_Chosen(String term) {
		Criterion criterion3 = Restrictions.like("nomeProvincia", term+"%", MatchMode.START) ;
		Criteria criteria = getSession().createCriteria(Province.class)
				.setProjection(Projections.projectionList()
					.add(Projections.property("nomeProvincia"), "nomeProvincia")
					.add(Projections.property("id"), "id"))
				.setResultTransformer(Transformers.aliasToBean(Province.class))
				.add( criterion3 ).setMaxResults(5).addOrder( Order.asc("nomeProvincia") );
				//.setFirstResult(first)
				//.setMaxResults(max);
		return criteria.list();
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Province> getProvinceOrdineNomeRegioneNomeProvincia_perSelectChoseBootstrap() {
        Criteria criteria =	getSession().createCriteria(Province.class);
        criteria.createAlias("regioni", "ts"); //questo è l'unico modo per fare un OrderBy con riferimento a un altra tabella
        criteria.addOrder(Order.asc("ts.nomeRegione")); //questo è l'unico modo per fare un OrderBy con riferimento a un altra tabella
        criteria.addOrder(Order.asc("nomeProvincia"));
        return criteria.list();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Province> getProvinceByAutista(long idAutista) {
        return getSession().createCriteria(Province.class).add(Restrictions.eq("autista.id", idAutista)).addOrder(Order.asc("id")).list(); 

	}
	
	
	@Transactional
	@Override
	public Province saveProvince(Province province) throws DataIntegrityViolationException, HibernateJdbcException {
		getSession().saveOrUpdate(province);
		//getSession().flush();
		return province;
	}
	
	

}
