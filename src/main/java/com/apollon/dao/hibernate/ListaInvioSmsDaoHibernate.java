package com.apollon.dao.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.apollon.dao.ListaInvioSmsDao;
import com.apollon.model.ListaInvioEmailSms;
import com.apollon.webapp.util.sms.Invio_Email_Sms_UTIL;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@EnableTransactionManagement
@Repository("ListaInvioSmsDao")
public class ListaInvioSmsDaoHibernate extends GenericDaoHibernate<ListaInvioEmailSms, Long> implements ListaInvioSmsDao {

	public ListaInvioSmsDaoHibernate() {
		super(ListaInvioEmailSms.class);
	}
	
	@Override
    @Transactional(readOnly = true)
	public ListaInvioEmailSms get(Long id){
		ListaInvioEmailSms listaInvioSms = (ListaInvioEmailSms) getSession().get(ListaInvioEmailSms.class, id);
		return listaInvioSms;
	}
	
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public List<ListaInvioEmailSms> getListaInvioSms() {
        return getSession().createCriteria(ListaInvioEmailSms.class).list();
	}
	

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Object[]> Check_TIPO_MESSAGGIO_SMS_AVVISO_CORSA(){
		
		// listEmailSms.numeroDestinatario, listEmailSms.testoMessaggio, listEmailSms.token, listEmailSms.dataInvio
		String queryString = "select listEmailSms FROM ListaInvioEmailSms listEmailSms "
				+ "WHERE "
				+ "( "
				+ "listEmailSms.tipoMessaggio = "+Invio_Email_Sms_UTIL.TIPO_MESSAGGIO_SMS_AVVISO_MATTEO_CORSA_VENDUTA+" OR " 
				
				+ "listEmailSms.tipoMessaggio = "+Invio_Email_Sms_UTIL.TIPO_MESSAGGIO_SMS_AVVISO_CORSA_ANDATA_CLIENTE_CONTATTO_AUTISTA_DISPONIBLE +" OR " 
				+ "listEmailSms.tipoMessaggio = "+Invio_Email_Sms_UTIL.TIPO_MESSAGGIO_SMS_AVVISO_CORSA_RITORNO_CLIENTE_CONTATTO_AUTISTA_DISPONIBLE +" OR " 
				
				+ "listEmailSms.tipoMessaggio = "+Invio_Email_Sms_UTIL.TIPO_MESSAGGIO_SMS_AVVISO_CORSA_ANDATA_CLIENTE_24_ORE +" OR "
				+ "listEmailSms.tipoMessaggio = "+Invio_Email_Sms_UTIL.TIPO_MESSAGGIO_SMS_AVVISO_CORSA_RITORNO_CLIENTE_24_ORE +" OR "
				
				+ "listEmailSms.tipoMessaggio = "+Invio_Email_Sms_UTIL.TIPO_MESSAGGIO_SMS_AVVISO_CORSA_ANDATA_AUTISTA_CONTATTO_CLIENTE_DISPONIBLE +" OR "
				+ "listEmailSms.tipoMessaggio = "+Invio_Email_Sms_UTIL.TIPO_MESSAGGIO_SMS_AVVISO_CORSA_RITORNO_AUTISTA_CONTATTO_CLIENTE_DISPONIBLE +" OR "
				
				+ "listEmailSms.tipoMessaggio = "+Invio_Email_Sms_UTIL.TIPO_MESSAGGIO_SMS_AVVISO_CORSA_ANDATA_AUTISTA_48_ORE +" OR "
				+ "listEmailSms.tipoMessaggio = "+Invio_Email_Sms_UTIL.TIPO_MESSAGGIO_SMS_AVVISO_CORSA_RITORNO_AUTISTA_48_ORE+" OR "
				
				+ "listEmailSms.tipoMessaggio = "+Invio_Email_Sms_UTIL.TIPO_MESSAGGIO_SMS_AVVISO_CORSA_ANDATA_AUTISTA_24_ORE +" OR "
				+ "listEmailSms.tipoMessaggio = "+Invio_Email_Sms_UTIL.TIPO_MESSAGGIO_SMS_AVVISO_CORSA_RITORNO_AUTISTA_24_ORE +" OR "
				
				+ "listEmailSms.tipoMessaggio = "+Invio_Email_Sms_UTIL.TIPO_MESSAGGIO_EMAIL_SCRIVI_RECENSIONE_TRANSFER +"  "
				+ " ) "
				+ "AND listEmailSms.attivo = true "
				+ "AND listEmailSms.inviato = false ";

		Query q = this.getSession().createQuery( queryString );
		//q.setParameter("now", new Date());
		List<Object[]> zz = q.list();
		return zz;
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Object[]> Check_TIPO_MESSAGGIO_SMS_RAPIDO(){
		
		// listEmailSms.numeroDestinatario, listEmailSms.testoMessaggio, listEmailSms.token, listEmailSms.dataInvio
		String queryString = "select listEmailSms FROM ListaInvioEmailSms listEmailSms "
				+ "WHERE listEmailSms.dataInvio <= :now "
				+ "AND listEmailSms.tipoMessaggio = "+Invio_Email_Sms_UTIL.TIPO_MESSAGGIO_SMS_RAPIDO+" "
				+ "AND listEmailSms.attivo = true "
				+ "AND listEmailSms.inviato = false ORDER BY listEmailSms.dataInvio ASC ";

		Query q = this.getSession().createQuery( queryString );
		q.setParameter("now", new Date());
		List<Object[]> zz = q.list();
		return zz;
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<ListaInvioEmailSms> getListaInvioSms_Inviati_DataInvio_ASC() {
		Criterion criterion1 = Restrictions.eq("inviato", true);
        return getSession().createCriteria(ListaInvioEmailSms.class).add(criterion1)
        		.addOrder( Order.asc("dataInvio") ).list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<ListaInvioEmailSms> getListaInvioSms_Inviati_DataInvio_DESC() {
		Criterion criterion1 = Restrictions.eq("inviato", true);
        return getSession().createCriteria(ListaInvioEmailSms.class).add(criterion1)
        		.addOrder( Order.desc("dataInvio") ).list();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<ListaInvioEmailSms> getListaInvioSms_NonInviati_DataInvio_DESC() {
		Criterion criterion1 = Restrictions.eq("inviato", false);
        return getSession().createCriteria(ListaInvioEmailSms.class).add(criterion1)
        		.addOrder( Order.desc("dataInvio") ).list();
	}
	
	
	@Override
	@Transactional(readOnly = true)
	public ListaInvioEmailSms getSms_By_Token(String token){
		Criterion criterion1 = Restrictions.eq("token", token) ;
		Criteria criteria = getSession().createCriteria(ListaInvioEmailSms.class).add(criterion1);
		return (ListaInvioEmailSms) criteria.uniqueResult();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<ListaInvioEmailSms> getListaInvioSms_By_NumeroDestinatario_DataInvio_DESC(String numeroDestinatario) {
		Criterion criterion1 = Restrictions.eq("numeroDestinatario", numeroDestinatario);
        return getSession().createCriteria(ListaInvioEmailSms.class).add(criterion1)
        		.addOrder( Order.desc("dataInvio") ).list();
	}
	
	
	
	@Transactional
	@Override
	public ListaInvioEmailSms saveListaInvioSms(ListaInvioEmailSms listaInvioSms) {
		getSession().saveOrUpdate(listaInvioSms);
		getSession().flush();
		return listaInvioSms;
	}
	
	

}
