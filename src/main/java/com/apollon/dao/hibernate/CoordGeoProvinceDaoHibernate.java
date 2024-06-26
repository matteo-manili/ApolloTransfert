package com.apollon.dao.hibernate;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import com.apollon.dao.CoordGeoProvinceDao;
import com.apollon.model.CoordGeoProvince;
import com.apollon.model.Province;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@EnableTransactionManagement
@Repository("CoordGeoProvinceDao")
public class CoordGeoProvinceDaoHibernate extends GenericDaoHibernate<CoordGeoProvince, Long> implements CoordGeoProvinceDao {

	public CoordGeoProvinceDaoHibernate() {
		super(CoordGeoProvince.class);
	}
	
	
	@Override
    @Transactional(readOnly = true)
	public CoordGeoProvince get(Long id){
		CoordGeoProvince agA_ModelliGiornate = (CoordGeoProvince) getSession().get(CoordGeoProvince.class, id);
		return agA_ModelliGiornate;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<CoordGeoProvince> getCoordGeoProvince_by_idAutoveicoloModelGiornata(Long idAutoveicoloModelGiornata) {
        return getSession().createCriteria(CoordGeoProvince.class)
        		.add(Restrictions.eq("agA_AutoveicoloModelliGiornate.id", idAutoveicoloModelGiornata)).list();
	}
	

	/**
	 * utilizzare il roundCoordinates il valore 2, perché è il giusto comporsomesso per trovare le province senza fare troppe chiamate API GOOGLE 
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public CoordGeoProvince getCoordGeoProvince_by_LatLng(double lat, double lng, int roundCoordinates) {
        String queryString_1 = "SELECT COORD.id_coordinate_geo_province AS id, COORD.dataRequestCoordGeo AS dataRequestCoordGeo, PROV.siglaProvincia AS siglaProvincia "
        		+"FROM data_coordinate_geo_province COORD INNER JOIN data_province PROV ON COORD.id_provincia = PROV.id_provincia  "
        		+ "WHERE ROUND(COORD.lat, "+roundCoordinates+") = :LAT AND ROUND(COORD.lng, "+roundCoordinates+") = :LNG  ";
        List<CoordGeoProvince> coordGeoProvince =  this.getSession().createSQLQuery( queryString_1 )
				.addScalar("id", StandardBasicTypes.LONG)
				.addScalar("dataRequestCoordGeo", StandardBasicTypes.DATE)
				.addScalar("siglaProvincia", StandardBasicTypes.STRING)
				.setResultTransformer(Transformers.aliasToBean(CoordGeoProvince.class))
				.setParameter("LAT", lat).setParameter("LNG", lng).list();
        // Elimino quelli più vecchi di due anni (nel caso in cui vengono spostati i confini delle province)
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -2);
        Date beforeYear = cal.getTime();
        if( coordGeoProvince != null ) {
        	for(CoordGeoProvince ite: coordGeoProvince  ) {
        		if( beforeYear.before( ite.getDataRequestCoordGeo()) ) {
            		return ite;
            	}else {
            		this.getSession().delete( getSession().get(CoordGeoProvince.class, ite.getId()) );
            		return null;
            	}
        	}
        }
        return coordGeoProvince != null && coordGeoProvince.size() > 0 ? coordGeoProvince.get(0) : null;
	}
	

	@Transactional
	@Override
	public CoordGeoProvince saveCoordGeoProvince(CoordGeoProvince agA_ModelliGiornate) throws DataIntegrityViolationException, HibernateJdbcException {
		getSession().saveOrUpdate(agA_ModelliGiornate);
		//getSession().flush();
		return agA_ModelliGiornate;
	}
	
	


	

}
