package com.apollon.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.HibernateJdbcException;
import org.springframework.stereotype.Service;

import com.apollon.dao.ProvinceDao;
import com.apollon.model.Province;
import com.apollon.service.ProvinceManager;




/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Service("ProvinceManager")
public class ProvinceManagerImpl extends GenericManagerImpl<Province, Long> implements ProvinceManager {

	private ProvinceDao provinceDao;
	
	@Override
    @Autowired
	public void setProvinceDao(ProvinceDao provinceDao) {
		this.provinceDao = provinceDao;
	}

	
	
	@Override
	public Province get(Long id) {
		return this.provinceDao.get(id);
	}
	

	@Override
	public List<Province> getProvince() {
		return provinceDao.getProvince();
	}
	
	
	@Override
	public long getPercentualeServizioMediaProvincia() {
		return provinceDao.getPercentualeServizioMediaProvincia();
	}
	
	@Override
	public List<Province> getProvinceByIdRegione(long idRegione){
		return provinceDao.getProvinceByIdRegione(idRegione);
	}
	
	@Override
	public List<Province> getProvince_order_Abitanti(){
		return provinceDao.getProvince_order_Abitanti();
	}
	
	@Override
	public List<Province> getProvinceItaliane_order_Abitanti(int maxResults){
		return provinceDao.getProvinceItaliane_order_Abitanti(maxResults);
	}
	
	@Override
	public List<Province> getNomeProvinciaBy_Like(String term) {
		return provinceDao.getNomeProvinciaBy_Like(term);
	}
	
	@Override
	public List<Province> getNomeProvinciaBy_Like_NomeRegione(String term) {
		return provinceDao.getNomeProvinciaBy_Like_NomeRegione(term);
	}
	
	@Override
	public List<Province> getNomeProvinceByLikeNome_Chosen(String term) {
		return provinceDao.getNomeProvinceByLikeNome_Chosen(term);
	}
	
	@Override
	public List<Province> getProvinceOrdineNomeRegioneNomeProvincia_perSelectChoseBootstrap() {
		return provinceDao.getProvinceOrdineNomeRegioneNomeProvincia_perSelectChoseBootstrap();
	}
	
	
	@Override
	public List<Province> getProvinceByAutista(long idAutista) {
		return provinceDao.getProvinceByAutista(idAutista);
	}
	
	
	@Override
	public Province saveProvince(Province province) throws DataIntegrityViolationException, HibernateJdbcException {
		return provinceDao.saveProvince(province);
	}

	
	@Override
    public void removeProvince(long id) {
		provinceDao.remove(id);
    }
	
	
	
}
