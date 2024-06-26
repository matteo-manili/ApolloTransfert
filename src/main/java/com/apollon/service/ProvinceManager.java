package com.apollon.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;

import com.apollon.dao.ProvinceDao;
import com.apollon.model.Province;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface ProvinceManager extends GenericManager<Province, Long> {
	
	void setProvinceDao(ProvinceDao provinceDao);
	
	
	Province get(Long id);
	
	List<Province> getProvinceByAutista(long idAutista);
	
	List<Province> getProvince();
	
	Province saveProvince(Province province) throws DataIntegrityViolationException, HibernateJdbcException;
	
	void removeProvince(long userProvince);

	List<Province> getProvinceOrdineNomeRegioneNomeProvincia_perSelectChoseBootstrap();

	List<Province> getProvinceByIdRegione(long idRegione);
	
	List<Province> getProvince_order_Abitanti();
	
	List<Province> getProvinceItaliane_order_Abitanti(int maxResults);

	List<Province> getNomeProvinceByLikeNome_Chosen(String term);

	List<Province> getNomeProvinciaBy_Like(String term);

	List<Province> getNomeProvinciaBy_Like_NomeRegione(String term);

	long getPercentualeServizioMediaProvincia();


	


	


	

}
