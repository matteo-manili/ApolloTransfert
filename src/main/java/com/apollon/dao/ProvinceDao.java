package com.apollon.dao;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;

import com.apollon.model.Province;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface ProvinceDao extends GenericDao<Province, Long> {
	
	Province get(Long id);
	
	List<Province> getProvince();
	
	Province saveProvince(Province province) throws DataIntegrityViolationException, HibernateJdbcException;

	List<Province> getProvinceByAutista(long idAutista);

	List<Province> getProvinceOrdineNomeRegioneNomeProvincia_perSelectChoseBootstrap();

	List<Province> getProvinceByIdRegione(Long idRegione);
	
	List<String> getProvince_SigleProvinciaList();
	
	List<Province> getProvince_order_Abitanti();
	
	List<Province> getProvinceItaliane_order_Abitanti(int maxResults);

	List<Province> getNomeProvinceByLikeNome_Chosen(String term);

	List<Province> getNomeProvinciaBy_Like(String term);
	
	List<Province> getNomeProvinciaBy_Like_NomeRegione(String term);

	Province getProvinciaBy_NomeProvincia(String term);

	long get_idRegione(long idprovincia);

	Province getProvinciaBy_SiglaProvincia(String term);

	long getPercentualeServizioMediaProvincia();
	
	long getTariffaBaseMediaProvincia();

	Province getProvinciaBy_SiglaProvincia_e_idRegione(String termSiglaProvincia, long idRegione);

	
	

	

	

	


}
