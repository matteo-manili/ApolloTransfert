package com.apollon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StringType;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.apollon.dao.ComuniFrazioniDao;
import com.apollon.model.ComuniFrazioni;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@EnableTransactionManagement
@Repository("ComuniFrazioniDao")
public class ComuniFrazioniDaoHibernate extends GenericDaoHibernate<ComuniFrazioni, Long> implements ComuniFrazioniDao {

	public ComuniFrazioniDaoHibernate() {
		super(ComuniFrazioni.class);
	}
	
	
	@Override
    @Transactional(readOnly = true)
	public ComuniFrazioni get(Long id){
		ComuniFrazioni comuniFrazioni = (ComuniFrazioni) getSession().get(ComuniFrazioni.class, id);
		return comuniFrazioni;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<ComuniFrazioni> getComuniFrazioni() {
        return getSession().createCriteria(ComuniFrazioni.class).list();
	}
	
	
	@Override
	@Transactional(readOnly = true)
	public ComuniFrazioni getComuniFrazioniByNomeFrazione_Equal(String nomeFrazione, Long idComune) {
		if(idComune != null){
			ComuniFrazioni comune = (ComuniFrazioni) getSession().createCriteria(ComuniFrazioni.class).createAlias("comune", "COM")
					.add(Restrictions.sqlRestriction("UPPER(nomeFrazione) = ? collate utf8mb4_bin ", nomeFrazione.toUpperCase(), new StringType()))
					.add(Restrictions.eq("COM.id", idComune)).uniqueResult();
			if(comune != null){
				return comune;
			}else{
				return null;
			}
		}else{
			return null;
		}
		
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<ComuniFrazioni> getComuniFrazioniByNomeFrazione_Equal(String nomeFrazione) {
		List<ComuniFrazioni> frazioneList =  getSession().createCriteria(ComuniFrazioni.class)
			.add(Restrictions.sqlRestriction("UPPER(nomeFrazione) = ? collate utf8mb4_bin ", nomeFrazione.toUpperCase(), new StringType())).list();
		if(frazioneList != null){
			return frazioneList;
		}else{
			return null;
		}
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<ComuniFrazioni> getNomeFrazioneBy_Like(String term) {
		Criterion crit1 = Restrictions.like("nomeFrazione", "%"+term+"%").ignoreCase() ;
		return getSession().createCriteria(ComuniFrazioni.class).add(crit1)
				.addOrder(Order.asc("nomeFrazione")).list();
	}
	
	
	@Override
	@Transactional
	public ComuniFrazioni saveComuniFrazioni(ComuniFrazioni comuniFrazioni) throws DataIntegrityViolationException, HibernateJdbcException {
		getSession().saveOrUpdate(comuniFrazioni);
		//getSession().flush();
		return comuniFrazioni;
	}


	
	

}
