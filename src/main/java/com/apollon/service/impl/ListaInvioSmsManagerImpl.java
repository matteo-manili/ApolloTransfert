package com.apollon.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apollon.dao.ListaInvioSmsDao;
import com.apollon.model.ListaInvioEmailSms;
import com.apollon.service.ListaInvioSmsManager;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Service("ListaInvioSmsManager")
public class ListaInvioSmsManagerImpl extends GenericManagerImpl<ListaInvioEmailSms, Long> implements ListaInvioSmsManager {

	private ListaInvioSmsDao listaInvioSmsDao;
	
	@Override
    @Autowired
	public void setListaInvioSmsDao(ListaInvioSmsDao listaInvioSmsDao) {
		this.listaInvioSmsDao = listaInvioSmsDao;
	}

	@Override
	public ListaInvioEmailSms get(Long id) {
		return this.listaInvioSmsDao.get(id);
	}
	
	@Override
	public List<ListaInvioEmailSms> getListaInvioSms() {
		return listaInvioSmsDao.getListaInvioSms();
	}

	@Override
	public List<ListaInvioEmailSms> getListaInvioSms_By_NumeroDestinatario_DataInvio_DESC(String numeroDestinatario) {
		return listaInvioSmsDao.getListaInvioSms_By_NumeroDestinatario_DataInvio_DESC(numeroDestinatario);
	}

	@Override
	public ListaInvioEmailSms getSms_By_Token(String token) {
		return listaInvioSmsDao.getSms_By_Token(token);
	}

	@Override
	public List<ListaInvioEmailSms> getListaInvioSms_NonInviati_DataInvio_DESC() {
		return listaInvioSmsDao.getListaInvioSms_NonInviati_DataInvio_DESC();
	}

	@Override
	public List<ListaInvioEmailSms> getListaInvioSms_Inviati_DataInvio_DESC() {
		return listaInvioSmsDao.getListaInvioSms_Inviati_DataInvio_DESC();
	}

	@Override
	public List<ListaInvioEmailSms> getListaInvioSms_Inviati_DataInvio_ASC() {
		return listaInvioSmsDao.getListaInvioSms_Inviati_DataInvio_ASC();
	}

	
	
	
	@Override
	public ListaInvioEmailSms saveListaInvioSms(ListaInvioEmailSms listaInvioSms) throws Exception {
		return listaInvioSmsDao.saveListaInvioSms(listaInvioSms);
	}

	@Override
    public void removeListaInvioSms(long id) {
		listaInvioSmsDao.remove(id);
    }


	
	
}
