package com.apollon.dao;

import java.util.List;
import com.apollon.model.ListaInvioEmailSms;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface ListaInvioSmsDao extends GenericDao<ListaInvioEmailSms, Long> {
	
	ListaInvioEmailSms get(Long id);
	
	List<ListaInvioEmailSms> getListaInvioSms();
	
	ListaInvioEmailSms saveListaInvioSms(ListaInvioEmailSms listaInvioSms);

	List<ListaInvioEmailSms> getListaInvioSms_By_NumeroDestinatario_DataInvio_DESC(String numeroDestinatario);

	ListaInvioEmailSms getSms_By_Token(String token);

	List<ListaInvioEmailSms> getListaInvioSms_NonInviati_DataInvio_DESC();

	List<ListaInvioEmailSms> getListaInvioSms_Inviati_DataInvio_DESC();

	List<ListaInvioEmailSms> getListaInvioSms_Inviati_DataInvio_ASC();

	List<Object[]> Check_TIPO_MESSAGGIO_SMS_RAPIDO();

	List<Object[]> Check_TIPO_MESSAGGIO_SMS_AVVISO_CORSA();

	

	


}
