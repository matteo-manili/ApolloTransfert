package com.apollon.dao.hibernate;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import com.apollon.dao.ModelloAutoScoutDao;
import com.apollon.model.ModelloAutoScout;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@EnableTransactionManagement
@Repository("ModelloAutoScoutDao")
public class ModelloAutoScoutDaoHibernate extends GenericDaoHibernate<ModelloAutoScout, Long> implements ModelloAutoScoutDao {

	public ModelloAutoScoutDaoHibernate() {
		super(ModelloAutoScout.class);
	}
	
	
	
	@Override
    @Transactional(readOnly = true)
	public ModelloAutoScout get(Long id){
		ModelloAutoScout modelloAutoScout = (ModelloAutoScout) getSession().get(ModelloAutoScout.class, id);
		return modelloAutoScout;
	}
	
	
	
	@Override
    @Transactional(readOnly = true)
	public Long getMaxValue_idAutoScout(){
		if( getSession().createCriteria(ModelloAutoScout.class).setProjection( Projections.max("idAutoScout")).uniqueResult() != null){
			return (Long) getSession().createCriteria(ModelloAutoScout.class)
					.setProjection( Projections.max("idAutoScout") ).uniqueResult() + 1;
		}else{
			return 1l;
		}
	}
	
	
	
	
	
	@Override
	@Transactional(readOnly = true)
	public ModelloAutoScout getModelloAutoScout_by_idModelloAutoScout(long idModelloAutoScout){
		Criterion criterion = Restrictions.eq("idAutoScout", idModelloAutoScout) ;
		return (ModelloAutoScout) getSession().createCriteria(ModelloAutoScout.class).add(criterion).uniqueResult();
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public List<ModelloAutoScout> getModelloAutoScout() {
        return getSession().createCriteria(ModelloAutoScout.class).list();
	}
	

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public List<ModelloAutoScout> getModelloAutoScoutByMarca(long idModello) {
        return getSession().createCriteria(ModelloAutoScout.class).add(Restrictions.eq("marcaAutoveicolo.id", idModello)).list();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<ModelloAutoScout> getNomeModelloList_like_NomeModello(String term){
		Criterion criterion3 = Restrictions.like("name", "%"+term+"%", MatchMode.ANYWHERE);
		return getSession().createCriteria(ModelloAutoScout.class).add(criterion3).addOrder(Order.asc("name")).list();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<ModelloAutoScout> getNomeModelloList_like_NomeMarca(String term){
		Criterion criterion3 = Restrictions.like("marcaAutoScout.name", "%"+term+"%", MatchMode.ANYWHERE);
		return getSession().createCriteria(ModelloAutoScout.class).createAlias("marcaAutoScout", "marcaAutoScout")
				.add(criterion3).addOrder(Order.asc("marcaAutoScout.name")).addOrder(Order.asc("name")).list();
	}

	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<ModelloAutoScout> getModelloAutoScoutDescrizione(String term, long idMarcaAutoScout) throws Exception {
		Criterion criterion3 = Restrictions.like("name", "%"+term+"%", MatchMode.ANYWHERE);
		Criteria criteria = getSession().createCriteria(ModelloAutoScout.class).createAlias("marcaAutoScout", "marcaAutoScout")
				.add( criterion3 ).add(Restrictions.eq("marcaAutoScout.idAutoScout", idMarcaAutoScout))
						.setMaxResults(20).addOrder(Order.asc("name"));
		return criteria.list();
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<ModelloAutoScout> getModelliAutoScout_by_UtilizzatiDagliAutisti(){
		String queryString = "select distinct MODELLO_AUTO "
				+ "FROM Autoveicolo AUTO, "
				+ "ModelloAutoNumeroPosti MODELLO_AUTO_NUM_POSTI, "
				+ "ModelloAutoScout MODELLO_AUTO, "
				+ "MarcaAutoScout MARCA_AUTO, "
				+ "ClasseAutoveicolo CLASSE_AUTO "
				+ "WHERE AUTO.modelloAutoNumeroPosti = MODELLO_AUTO_NUM_POSTI.id "
				+ "AND MODELLO_AUTO_NUM_POSTI.modelloAutoScout = MODELLO_AUTO.id "
				+ "AND MODELLO_AUTO.marcaAutoScout = MARCA_AUTO.id "
				+ "AND MODELLO_AUTO.classeAutoveicolo = CLASSE_AUTO.id ";
		// ATTENZIONE FORSE E DICO FORSE SE AGGIUNGO L'ORDINAMENTO ALLA QUERY, IL DATABASE IN PRODUZIONE 
		// (CHE è UN ALTRA VERSIONE DA QUELLO DI SVILUPPO), POTREBBE DARE ERRORI ALLA QUERY. PROVARE!
		Query q = this.getSession().createQuery( queryString );
		List<ModelloAutoScout> zz = q.list();
		/*
		System.out.println( "TOTAL: "+zz.size());
		for(Object ite: zz){
			//Object[] we = (Object[]) ite;
			ModelloAutoScout marca = (ModelloAutoScout) ite;
			//Object model = (Object) ite[1];
			System.out.println( marca.getName()  );
		}
		*/
		return zz;
	}
	
	
	@Transactional
	@Override
	public ModelloAutoScout saveModelloAutoScout(ModelloAutoScout modelloAutoScout) {
		getSession().saveOrUpdate(modelloAutoScout);
		//getSession().flush();
		return modelloAutoScout;
	}
	
	

}
