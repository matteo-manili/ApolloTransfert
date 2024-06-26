package com.apollon.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.apollon.dao.AutistaDao;
import com.apollon.model.Autista;
import com.apollon.model.AutistaAeroporti;
import com.apollon.model.AutistaPortiNavali;
import com.apollon.model.AutistaZone;
import com.apollon.model.Autoveicolo;
import com.apollon.service.AutistaManager;
import com.apollon.webapp.util.controller.documenti.DocumentiInfoUtil;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Service("AutistaManager")
public class AutistaManagerImpl extends GenericManagerImpl<Autista, Long> implements AutistaManager {

	private AutistaDao autistaDao;
	
	@Override
    @Autowired
	public void setAutistaDao(AutistaDao autistaDao) {
		this.autistaDao = autistaDao;
	}

	@Override
	public Autista saveAutista(Autista autista) throws Exception {
		return autistaDao.saveAutista(autista);
	}
	
	@Override
    public void removeAutista(long id) throws Exception, ConstraintViolationException {
		autistaDao.remove(id);
    }

	@Override
	public Autista get(Long id) {
		return this.autistaDao.get(id);
	}
	
	@Override
	public List<AutistaZone> lazyAutistaZone(long idAutista){
		return autistaDao.lazyAutistaZone(idAutista);
	}
	
	@Override
	public List<Autoveicolo> lazyAutoveicolo(long idAutista){
		return autistaDao.lazyAutoveicolo(idAutista);
	}
	
	@Override
	public List<AutistaAeroporti> lazyAutistaAeroporti(long idAutista){
		return autistaDao.lazyAutistaAeroporti(idAutista);
	}
	@Override
	public List<AutistaPortiNavali> lazyAutistaPortiNavali(long idAutista){
		return autistaDao.lazyAutistaPortiNavali(idAutista);
	}
	
	@Override
	public Autista getAutistaByUser(Long id){
		return this.autistaDao.getAutistaByUser(id);
	}
	
	@Override
	public List<Autista> getAutistaTable() {
		return autistaDao.getAutistaTable();
	}
	
	@Override
	public List<Autista> getAutistiList(){
		return autistaDao.getAutistiList();
	}
	
	@Override
	public List<Autista> getAutistaTable_2_limit(int maxResults, Integer firstResult, boolean OrdineDocumenti){
		if(OrdineDocumenti){
			List<Autista> autistiListNew = new ArrayList<Autista>();
			List<Object[]> autistiListMark = autistaDao.getDocumentiAutisti_da_Approvare(maxResults, firstResult);
			for(Object[] ite: autistiListMark){
				Autista autista = (Autista) ite[0];
				DocumentiInfoUtil docUtil = new DocumentiInfoUtil(autista);
				autista.setDocumentiCompletatiFrazione(docUtil.documentiCompletatiFrazione);
				autista.setDocumentiApprovatiFrazione(docUtil.documentiApprovatiFrazione);
				autistiListNew.add(autista);
			}
			return autistiListNew;
		}else{
			List<Autista> autistiList = autistaDao.getAutistaTable_2_limit(maxResults, firstResult);
			for(Autista autistiList_ite: autistiList){
				DocumentiInfoUtil docUtil = new DocumentiInfoUtil(autistiList_ite);
				autistiList_ite.setDocumentiCompletatiFrazione(docUtil.documentiCompletatiFrazione);
				autistiList_ite.setDocumentiApprovatiFrazione(docUtil.documentiApprovatiFrazione);
			}
			return autistiList;
		}
	}
	
	
	@Override
	public int getCountAutista(){
		return autistaDao.getCountAutista();
	}

	@Override
	public List<Autista> getAutistiBy_LIKE(String term){
		return autistaDao.getAutistiBy_LIKE(term);
	}
	
	@Override
	public int getCalcolaNumeroCorseApprovate(long idAutista) {
		return autistaDao.getCalcolaNumeroCorseApprovate(idAutista);
	}
	
	@Override
	public int updateNumeroCorseEseguite(long idAutista, int numCorseEseguite) {
		return autistaDao.updateNumeroCorseEseguite(idAutista, numCorseEseguite);
	}
	
	@Override
	public List<Autista> ListAutista_Approvati() {
		return autistaDao.ListAutista_Approvati();
	}
	
	
	
	
	
}
