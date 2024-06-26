package com.apollon.service;

import java.util.List;

import com.apollon.dao.ListaInvioSmsDao;
import com.apollon.model.ListaInvioEmailSms;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface ListaInvioSmsManager extends GenericManager<ListaInvioEmailSms, Long> {
	
	void setListaInvioSmsDao(ListaInvioSmsDao listaInvioSmsDao);
	
	ListaInvioEmailSms get(Long id);
	
	void removeListaInvioSms(long userListaInvioSms);
	
	ListaInvioEmailSms saveListaInvioSms(ListaInvioEmailSms listaInvioSms) throws Exception;
	
	List<ListaInvioEmailSms> getListaInvioSms();
	
	List<ListaInvioEmailSms> getListaInvioSms_By_NumeroDestinatario_DataInvio_DESC(String numeroDestinatario);

	ListaInvioEmailSms getSms_By_Token(String token);

	List<ListaInvioEmailSms> getListaInvioSms_NonInviati_DataInvio_DESC();

	List<ListaInvioEmailSms> getListaInvioSms_Inviati_DataInvio_DESC();

	List<ListaInvioEmailSms> getListaInvioSms_Inviati_DataInvio_ASC();
	
	
	
	



}
