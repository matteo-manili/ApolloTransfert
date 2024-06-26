package com.apollon.webapp.util.controller.rimborsi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.VelocityEngine;
import org.json.JSONObject;
import org.springframework.web.servlet.ModelAndView;

import com.apollon.Constants;
import com.apollon.dao.FattureDao;
import com.apollon.dao.GestioneApplicazioneDao;
import com.apollon.dao.RichiestaAutistaParticolareDao;
import com.apollon.dao.RichiestaMediaDao;
import com.apollon.model.Fatture;
import com.apollon.model.RicercaTransfert;
import com.apollon.model.RichiestaAutistaParticolare;
import com.apollon.model.RichiestaMedia;
import com.apollon.model.RichiestaMediaAutista;
import com.apollon.webapp.util.ApplicationUtils;
import com.apollon.webapp.util.bean.InfoPaymentProvider;
import com.apollon.webapp.util.controller.gestioneApplicazione.GestioneApplicazioneUtil;
import com.apollon.webapp.util.controller.home.HomeUtil;
import com.apollon.webapp.util.email.InviaEmail;
import com.apollon.webapp.util.fatturazione.BeanInfoFattura_Corsa;
import com.apollon.webapp.util.fatturazione.Fatturazione;
import com.apollon.webapp.util.fatturazione.GenerateFatturaPdf;
import com.apollon.webapp.util.fatturazione.GenerateFatturaPdf.Footer_Header_Fattura;
import com.apollon.webapp.util.sms.InvioSms;
import com.apollon.webapp.util.sms.Invio_Email_Sms_UTIL;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Capture;
import com.paypal.api.payments.DetailedRefund;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.RefundRequest;
import com.paypal.api.payments.RelatedResources;
import com.paypal.api.payments.Sale;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;
import com.paypal.http.serializer.Json;
import com.paypal.orders.Order;
import com.paypal.orders.OrdersGetRequest;
import com.stripe.Stripe;
import com.stripe.exception.InvalidRequestException;
/*
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
*/
import com.stripe.exception.StripeException;
import com.stripe.model.BalanceTransaction;
import com.stripe.model.Charge;
import com.stripe.model.ChargeCollection;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public class RimborsiUtil extends ApplicationUtils {
	
	private static final Log log = LogFactory.getLog(RimborsiUtil.class);
	private static  GestioneApplicazioneDao gestioneApplicazioneDao = (GestioneApplicazioneDao) contextDao.getBean("GestioneApplicazioneDao");
	private static  FattureDao fattureDao = (FattureDao) contextDao.getBean("FattureDao");
	private static  RichiestaMediaDao richiestaMediaDao = (RichiestaMediaDao) contextDao.getBean("RichiestaMediaDao");
	private static  RichiestaAutistaParticolareDao richiestaAutistaParticolareDao = (RichiestaAutistaParticolareDao) contextDao.getBean("RichiestaAutistaParticolareDao");
	
	
	public static void InviaEmailAvvisoRimborsoConFattura(RicercaTransfert ricTransfert, HttpServletRequest request, VelocityEngine velocityEngine) throws Exception{
		log.debug("InviaEmailAvvisoRimborsoConFattura");
		RichiestaMedia richMedia = richiestaMediaDao.getRichiestaMedia_by_IdRicercaTransfert(ricTransfert.getId());
		RichiestaAutistaParticolare richPartic = ricTransfert.getRichiestaAutistaParticolareAcquistato();
		List<RichiestaAutistaParticolare> richPartic_Mult = ricTransfert.getRichiestaAutistaParticolareAcquistato_Multiplo();
		if( ((ricTransfert.getTipoServizio() == null || ricTransfert.getTipoServizio().equals(Constants.SERVIZIO_AGENDA_AUTISTA)) && 
				ricTransfert.getAgendaAutista_RimborsoCliente() != null && ricTransfert.getAgendaAutista_RimborsoCliente().compareTo(BigDecimal.ZERO) > 0)
			||
			((ricTransfert.getTipoServizio() == null || ricTransfert.getTipoServizio().equals(Constants.SERVIZIO_STANDARD)) && 
				richMedia != null && richMedia.getRimborsoCliente() != null && richMedia.getRimborsoCliente().compareTo(BigDecimal.ZERO) > 0)
			||
			(ricTransfert.getTipoServizio().equals(Constants.SERVIZIO_PARTICOLARE) && 
				richPartic != null && richPartic.getRimborsoCliente() != null && richPartic.getRimborsoCliente().compareTo(BigDecimal.ZERO) > 0)
			||
			
			(ricTransfert.getTipoServizio().equals(Constants.SERVIZIO_MULTIPLO) && 
				richPartic_Mult != null && richPartic_Mult.get(0).getRimborsoCliente() != null && richPartic_Mult.get(0).getRimborsoCliente().compareTo(BigDecimal.ZERO) > 0)
		)
		{
			// Testo Dinamico della email
			Map<String, Object> modelVelocity = new HashMap<String, Object>();
			modelVelocity.put("nomeCognome", ricTransfert.getUser().getFullName());
			modelVelocity.put("corsaId", ricTransfert.getId());
			modelVelocity.put("corsaPartenzaArrivo", InviaEmail.CorsaAndataRitornoModelVelocity(ricTransfert, request.getLocale()));
			modelVelocity.put(Constants.VM_ATTRIBUTE_FOOTER_EMAIL, ApplicationUtils.FooterEmail(velocityEngine, modelVelocity, ricTransfert, request.getLocale()));
			// allegato
			BeanInfoFattura_Corsa fattCorsa = Fatturazione.Informazioni_FatturaCorsa( ricTransfert.getId() );
			Document document = new Document();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PdfWriter writer = PdfWriter.getInstance (document, baos);
			writer.setPageEvent( new Footer_Header_Fattura() );
			document.open();
			GenerateFatturaPdf fattPdf = new GenerateFatturaPdf(fattCorsa, request, document);
			fattPdf.Corpo_FatturaCorsaRimborso(document, request.getLocale());
			document.close();
			// file name
			String fileName = "rimborso_corsa_"+ricTransfert.getId()+".pdf";
			InviaEmail.InviaEmail_HTML_User(ricTransfert.getUser(), Constants.VM_EMAIL_AVVISO_RIMBORSO_ESEGUITO, "Rimbroso Corsa", 
					velocityEngine, modelVelocity, baos, fileName, false, (CheckDomainTranfertClienteVenditore(ricTransfert)) ? Constants.EMAIL_FROM_NCCTRANSFERONLINE : Constants.EMAIL_FROM_APOLLOTRANSFERT, false);
			// incremento avviso inviati rimborso
			Fatture fattura = fattureDao.getFatturaBy_IdRicercaTransfert_Rimbroso( ricTransfert.getId() );
			if(fattura.getNumeroAvvisiInviatiRimborso() == null || fattura.getNumeroAvvisiInviatiRimborso() == 0){
				fattura.setNumeroAvvisiInviatiRimborso( 1 );
			}else{
				fattura.setNumeroAvvisiInviatiRimborso( fattura.getNumeroAvvisiInviatiRimborso() + 1 );
			}
			fattureDao.saveFatture(fattura);
		}
	}
	
	
	public static void Invia_Sms_e_Email_CorsaCancellataMedio(RichiestaMediaAutista richAutMedio, HttpServletRequest request, VelocityEngine velocityEngine) throws Exception{
		log.debug("Invia_Sms_e_Email_CorsaConfermataMedio");
    	String telefonoDestinatario = richAutMedio.getAutista().getUser().getPhoneNumber();
    	String linkVerificaSms = request.getServerName() + request.getContextPath() + "/"+Constants.URL_PRENOTA_CORSA+"?token="+richAutMedio.getTokenAutista();
        String testoSms = "Salve "+richAutMedio.getAutista().getUser().getFirstName() +
        		" La Avvisiamo che la Corsa "+richAutMedio.getRichiestaMedia().getRicercaTransfert().getId()+" è stata CANCELLATA" +
				" fai click qui per i dettagli "+linkVerificaSms;
        String result = "";
        while( !result.equals(Constants.SMS_STATUS_SUCCESS) ){
        	result = InvioSms.Crea_SMS_Gateway(telefonoDestinatario, testoSms, Invio_Email_Sms_UTIL.TIPO_MESSAGGIO_SMS_RAPIDO);
        	if( !ApplicationUtils.CheckAmbienteApolloTransfert_Mondoserver(request) ){
        		result = Constants.SMS_STATUS_SUCCESS;
        	}
        }
        InviaEmailCorsaCancellataAutista(richAutMedio, request, velocityEngine);
    }
	
	
	/**
	 * Controllo Sicurezza invio email per non inviare email agli autisti quando faccio i test su localhost
	 * @throws MessagingException 
	 * @throws IOException 
	 */
	private static void InviaEmailCorsaCancellataAutista(RichiestaMediaAutista richAutMedio, HttpServletRequest request, VelocityEngine velocityEngine) throws MessagingException, IOException {
		if( ApplicationUtils.CheckAmbienteApolloTransfert_Mondoserver(request) || CheckEmailTesting(richAutMedio.getAutista().getUser().getEmail()) ){
			InviaEmail.InviaEmailCorsaCancellataAutista(richAutMedio, request, velocityEngine);
        }else{
        	log.debug("EMAIL NON INVIATA InviaEmailConfermaCorsaAutista: "+richAutMedio.getAutista().getUser().getFullName());
        }
	}
	

	
	public static ModelAndView MavAddObject_TypePaymentProvider(RicercaTransfert ricTransfert, ModelAndView mav) {
		final String ProviderTipo = ricTransfert.getProviderPagamentoInfo().getPaymentProviderTipo();
    	if( ProviderTipo.equals(Constants.TIPO_PAYMENT_STRIPE_1) || ProviderTipo.equals(Constants.TIPO_PAYMENT_STRIPE_2) ){
    		mav.addObject("TypePaymentProvider", "STRIPE" );
    	}else if( ProviderTipo.equals(Constants.TIPO_PAYMENT_PAYPAL_1) || ProviderTipo.equals(Constants.TIPO_PAYMENT_PAYPAL_2) ){
    		mav.addObject("TypePaymentProvider", "PAYPAL" );
    	}
		return mav;
	}
	

	private static void SalvaInfoPaymentProvider_RicercaTransfert(RicercaTransfert ricTransfert, InfoPaymentProvider infoPay) {
		JSONObject infoDatiPasseggero = HomeUtil.GetInfoDatiPasseggero(ricTransfert);
		infoDatiPasseggero.put(Constants.PaymentProviderNomeClienteJSON, infoPay.getNomeCliente());
		infoDatiPasseggero.put(Constants.PaymentProviderAmountJSON, infoPay.getAmount());
		infoDatiPasseggero.put(Constants.PaymentProviderFeeJSON, infoPay.getFee());
		infoDatiPasseggero.put(Constants.PaymentProviderRefundJSON, infoPay.getRefund());
		ricTransfert.setInfoPasseggero(infoDatiPasseggero.toString());
		ricercaTransfertDao.saveRicercaTransfert( ricTransfert );
	}
	
	
	public static InfoPaymentProvider Retrive_Amount_Rimborso_NomeCliente(long IdRicercaTransfert, String ProviderId, String ProviderTipo) {
		try {
			RicercaTransfert ricTransfert = ricercaTransfertDao.get(IdRicercaTransfert);
			return Retrive_Amount_Rimborso_NomeCliente(ricTransfert, ProviderId, ProviderTipo);
		}catch(Exception exc){
			exc.printStackTrace();
			return null;
		}
	}
	
	public static InfoPaymentProvider Retrive_Amount_Rimborso_NomeCliente(RicercaTransfert ricTransfert) {
		try {
			return Retrive_Amount_Rimborso_NomeCliente(ricTransfert, ricTransfert.getProviderPagamentoInfo().getPaymentProviderId(), 
					ricTransfert.getProviderPagamentoInfo().getPaymentProviderTipo());
		}catch(Exception exc){
			exc.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Mi ritorna Informazionio Pagamento Stripe e PayPal
	 * @throws StripeException 
	 * 
	 */
	public static InfoPaymentProvider Retrive_Amount_Rimborso_NomeCliente(RicercaTransfert ricTransfert, String ProviderId, String ProviderTipo) { 
		//log.debug("Retrive_Amount_Rimborso_NomeCliente");
		InfoPaymentProvider infoPay = new InfoPaymentProvider();
		infoPay.setPaymentProviderId(ProviderId);
		infoPay.setPaymentProviderTipo(ProviderTipo);
		if( ProviderTipo.equals(Constants.TIPO_PAYMENT_STRIPE_2) ){
			try {
				// mi ritorna il Totale Rimborsi eseguiti
				Stripe.apiKey = GestioneApplicazioneUtil.CredenzialiSecretStripe();
				PaymentIntent paymentIntent = PaymentIntent.retrieve(ProviderId);
				//System.out.println( paymentIntent.getCharges().getData().get(0).getId() );
				infoPay.setNomeCliente( paymentIntent.getCharges().getData().get(0).getBillingDetails().getName() );
				Charge charge = paymentIntent.getCharges().getData().get(0);
				Double amount = (double) charge.getAmount() / 100;
				infoPay.setAmount(new BigDecimal(amount).setScale(2, RoundingMode.HALF_EVEN));
				Double refund = (double) charge.getAmountRefunded() / 100;
				infoPay.setRefund(new BigDecimal(refund).setScale(2, RoundingMode.HALF_EVEN));
				BalanceTransaction balanceTransaction = BalanceTransaction.retrieve( charge.getBalanceTransaction() );
				Double fee = (double) balanceTransaction.getFee() / 100;
				infoPay.setFee(new BigDecimal(fee).setScale(2, RoundingMode.HALF_EVEN));
				SalvaInfoPaymentProvider_RicercaTransfert(ricTransfert, infoPay);
				return infoPay;
			}catch (InvalidRequestException e) {
				e.printStackTrace();
				infoPay.setMessaggioErrore(e.getMessage());
				return null;
			}catch (StripeException e) {
				e.printStackTrace();
				infoPay.setMessaggioErrore(e.getMessage());
				return null;
			}catch (Exception e) {
				e.printStackTrace();
				infoPay.setMessaggioErrore(e.getMessage());
				return null;
			}
		}else if( ProviderTipo.equals(Constants.TIPO_PAYMENT_STRIPE_1) ){
			try {
				// mi ritorna il Totale Rimborsi eseguiti
				Stripe.apiKey = GestioneApplicazioneUtil.CredenzialiSecretStripe();
				Charge charge = Charge.retrieve(ProviderId);
				BalanceTransaction balanceTransaction = BalanceTransaction.retrieve( charge.getBalanceTransaction() );
				JSONObject json = new JSONObject( charge.getSource() );
			    infoPay.setNomeCliente( json.getString("name") );
				Double amount = (double) charge.getAmount() / 100;
				infoPay.setAmount(new BigDecimal(amount).setScale(2, RoundingMode.HALF_EVEN));
				Double fee = (double) balanceTransaction.getFee() / 100;
				infoPay.setFee(new BigDecimal(fee).setScale(2, RoundingMode.HALF_EVEN));
				Double refund = (double) charge.getAmountRefunded() / 100;
				infoPay.setRefund(new BigDecimal(refund).setScale(2, RoundingMode.HALF_EVEN));
				SalvaInfoPaymentProvider_RicercaTransfert(ricTransfert, infoPay);
				return infoPay;
			}catch (InvalidRequestException e) {
				e.printStackTrace();
				infoPay.setMessaggioErrore(e.getMessage());
				return null;
			}catch (StripeException e) {
				e.printStackTrace();
				infoPay.setMessaggioErrore(e.getMessage());
				return null;
			}catch (Exception e) {
				e.printStackTrace();
				infoPay.setMessaggioErrore(e.getMessage());
				return null;
			}
		}else if( ProviderTipo.equals(Constants.TIPO_PAYMENT_PAYPAL_2) ){
			try {
				// ORDINE
				//System.out.println("------------------------------ Checkout SDK: ORDINE:");
				String CredenzialiPayPal = GestioneApplicazioneUtil.CredenzialiRESTPayPal();
				
				PayPalEnvironment environment = CredenzialiPayPal.split("#")[2].equals(Constants.PAYPAL_SANDBOX) ? 
						new PayPalEnvironment.Sandbox(CredenzialiPayPal.split("#")[0], CredenzialiPayPal.split("#")[1]) :
							 new PayPalEnvironment.Live(CredenzialiPayPal.split("#")[0], CredenzialiPayPal.split("#")[1]);
				PayPalHttpClient client = new PayPalHttpClient(environment);
				OrdersGetRequest requestOrder = new OrdersGetRequest( ProviderId );
				com.paypal.http.HttpResponse<Order> response = client.execute( requestOrder );
				infoPay.setNomeCliente(response.result().payer().name().surname()+" "+response.result().payer().name().givenName());
				double refund = 0;
				List<com.paypal.orders.Refund> listRefund = response.result().purchaseUnits().get(0).payments().refunds(); //com.paypal.orders.Refund
				if( listRefund != null ) {
					for( com.paypal.orders.Refund ite : listRefund){
						refund = refund + Double.parseDouble(ite.amount().value());
					}
				}
				
				infoPay.setRefund(new BigDecimal(refund).setScale(2, RoundingMode.HALF_EVEN));
				final String CaptureId = response.result().purchaseUnits().get(0).payments().captures().get(0).id();
				//System.out.println("JSON: "+new JSONObject(new Json().serialize(response.result())).toString(4));
				//System.out.println("------------------------------ RestAPI SDK Capture:");
				APIContext apiContext = new APIContext(CredenzialiPayPal.split("#")[0], CredenzialiPayPal.split("#")[1], CredenzialiPayPal.split("#")[2]);
				Capture capture = Capture.get(apiContext, CaptureId);
				double amount = Double.parseDouble(capture.getAmount().getTotal());
				infoPay.setAmount(new BigDecimal(amount).setScale(2, RoundingMode.HALF_EVEN));
				double fee = Double.parseDouble(capture.getTransactionFee().getValue());
				infoPay.setFee(new BigDecimal(fee).setScale(2, RoundingMode.HALF_EVEN));
				//System.out.println("JSON: "+capture.toString());
				SalvaInfoPaymentProvider_RicercaTransfert(ricTransfert, infoPay);
			    return infoPay;
			    
			} catch (Exception e) {
				e.printStackTrace();
				infoPay.setMessaggioErrore(e.getMessage());
				return null;
			}
		}else if( ProviderTipo.equals(Constants.TIPO_PAYMENT_PAYPAL_1) ) {
			try{
				String CredenzialiPayPal = GestioneApplicazioneUtil.CredenzialiRESTPayPal();
				//System.out.println("CredenzialiPayPal: "+CredenzialiPayPal);
				APIContext apiContext = new APIContext(CredenzialiPayPal.split("#")[0], CredenzialiPayPal.split("#")[1], CredenzialiPayPal.split("#")[2]);
				Payment payment = Payment.get(apiContext, ProviderId);
				String name = payment.getPayer().getPayerInfo().getFirstName() +" "+payment.getPayer().getPayerInfo().getLastName();
				infoPay.setNomeCliente(name);
				//System.out.println( payment.getTransactions().get(0).getRelatedResources().get(0).getSale().getTransactionFee().getValue() );
				if(payment.getTransactions().get(0).getRelatedResources().size() > 0){
					double amount = Double.parseDouble(payment.getTransactions().get(0).getAmount().getTotal());
					infoPay.setAmount(new BigDecimal(amount).setScale(2, RoundingMode.HALF_EVEN));
					double fee = Double.parseDouble(payment.getTransactions().get(0).getRelatedResources().get(0).getSale().getTransactionFee().getValue());
					infoPay.setFee(new BigDecimal(fee).setScale(2, RoundingMode.HALF_EVEN));
					double refund = 0;
					for(RelatedResources ite : payment.getTransactions().get(0).getRelatedResources()){
						try{
							refund = refund + Double.parseDouble( ite.getRefund().getAmount().getTotal() );
						}catch(NullPointerException nP){ }
					}
					//String rimborsoEseguito = payment.getTransactions().get(0).getRelatedResources().get(1).getRefund().getAmount().getTotal();
					infoPay.setRefund(new BigDecimal(refund).setScale(2, RoundingMode.HALF_EVEN));
					SalvaInfoPaymentProvider_RicercaTransfert(ricTransfert, infoPay);
					return infoPay;
				}else{
					infoPay.setMessaggioErrore( "No Informazioni PayPal" );
					return null;
				}
			}catch (PayPalRESTException payPalRESTException) {
				payPalRESTException.printStackTrace();
			    infoPay.setMessaggioErrore( ParseErrorPayPalREST(payPalRESTException) );
				return infoPay;
	        }
			
		}else {
			return null;
		}
	}
	
	
	
	/**
	 * SE PAYPAL VEDERE: 
	 * https://developer.paypal.com/docs/api/quickstart/refund-payment/
	 * https://github.com/paypal/PayPal-Java-SDK/tree/master/rest-api-sample/src/main/java/com/paypal/api/payments/servlet
	 * https://github.com/paypal/PayPal-Java-SDK/blob/master/rest-api-sample/src/main/java/com/paypal/api/payments/servlet/SaleRefundServlet.java
	 * 
	 * Ottenere ID Sale partendo da un ID Payment
	 * https://github.com/paypal/PayPal-PHP-SDK/issues/375
	 * 
	 * vedere i payment di sendobx (Test) - Il rimborso nell'ambiente di sendbox è possibile solo se totale, non parziale.
	 * https://www.sandbox.paypal.com/myaccount/home
	 * username: matteo.manili-buyer@gmail.com
	 * password: giulietta
	 * 
	 * SE STRIPE VEDERE: 
	 * https://dashboard.stripe.com/
	 * @throws UnknownHostException 
	 * 
	 * 
	 * @throws Exception 
	 */
	public static String EseguiRimborso(BigDecimal ValoreRimborso, RicercaTransfert ricTransfert, HttpServletRequest request) throws UnknownHostException {
		log.debug("EseguiRimborso");
		boolean modeStripe = (gestioneApplicazioneDao.getName("SWITCH_STRIPE_KEY_LIVE_TEST").getValueNumber() != 0l);
		boolean modePayPal = (gestioneApplicazioneDao.getName("SWITCH_PAYPAL_KEY_LIVE_TEST").getValueNumber() != 0l);
		ValoreRimborso = ValoreRimborso.setScale(2, RoundingMode.HALF_EVEN);
		if(
			( ApplicationUtils.CheckAmbienteApolloTransfert_Mondoserver(request) && modeStripe == true && modePayPal == true) 
				||
			(ApplicationUtils.CheckAmbienteApolloTransfert_Mondoserver(request) == false && modeStripe == false && modePayPal == false) ){

			final String ProviderId = ricTransfert.getProviderPagamentoInfo().getPaymentProviderId();
			final String ProviderTipo = ricTransfert.getProviderPagamentoInfo().getPaymentProviderTipo();
			
			if( ProviderTipo.equals(Constants.TIPO_PAYMENT_STRIPE_2) ){
				// RIMBORSO - STRIPE INTENT
				try {
					String ValoreAmountStripe = ValoreRimborso.toString().replace(".", "");
					Stripe.apiKey = GestioneApplicazioneUtil.CredenzialiSecretStripe();
					Map<String, Object> refundParams = new HashMap<String, Object>();
					refundParams.put("payment_intent", ProviderId);
					Map<String, String> initialMetadata = new HashMap<String, String>();
					initialMetadata.put("RicercaTransfert id:", ricTransfert.getId().toString() ); 
					refundParams.put("metadata", initialMetadata);
					refundParams.put("amount", ValoreAmountStripe);
					refundParams.put("reason", "requested_by_customer"); // Reason for the refund. If set, possible values are duplicate, fraudulent, and requested_by_customer.
					// eseguo il rimborso
					Refund.create(refundParams);
				}catch (StripeException e) {
					e.printStackTrace();
					return e.getMessage();
		        }
			}else if( ProviderTipo.equals(Constants.TIPO_PAYMENT_STRIPE_1) ){
				// RIMBORSO - STRIPE CHARGE
				try{
					String ValoreAmountStripe = ValoreRimborso.toString().replace(".", "");
					Stripe.apiKey = GestioneApplicazioneUtil.CredenzialiSecretStripe();
					Map<String, Object> refundParams = new HashMap<String, Object>();
					refundParams.put("charge", ProviderId);
					Map<String, String> initialMetadata = new HashMap<String, String>();
					initialMetadata.put("RicercaTransfert id:", ricTransfert.getId().toString() ); 
					refundParams.put("metadata", initialMetadata);
					refundParams.put("amount", ValoreAmountStripe);
					refundParams.put("reason", "requested_by_customer"); // Reason for the refund. If set, possible values are duplicate, fraudulent, and requested_by_customer.
					// eseguo il rimborso
					Refund.create(refundParams);
				}catch (StripeException e) {
					e.printStackTrace();
					return e.getMessage();
		        }
			}else if( ProviderTipo.equals(Constants.TIPO_PAYMENT_PAYPAL_2) ) {
				try {
					String ValoreAmountPayPal = ValoreRimborso.toString().replace(",", ".");
					String CredenzialiPayPal = GestioneApplicazioneUtil.CredenzialiRESTPayPal();
					PayPalEnvironment environment = new PayPalEnvironment.Sandbox(CredenzialiPayPal.split("#")[0], CredenzialiPayPal.split("#")[1]);
					PayPalHttpClient client = new PayPalHttpClient(environment);
					OrdersGetRequest requestOrder = new OrdersGetRequest( ProviderId );
					com.paypal.http.HttpResponse<Order> response = client.execute(requestOrder);
					final String Currency = response.result().purchaseUnits().get(0).payments().captures().get(0).amount().currencyCode();
					final String CaptureId = response.result().purchaseUnits().get(0).payments().captures().get(0).id();
					
					APIContext apiContext = new APIContext(CredenzialiPayPal.split("#")[0], CredenzialiPayPal.split("#")[1], CredenzialiPayPal.split("#")[2]) ;
		 			Capture capture = Capture.get(apiContext, CaptureId);
		 			com.paypal.api.payments.RefundRequest refund = new com.paypal.api.payments.RefundRequest();
		 			Amount amount = new Amount();
		 			amount.setCurrency( Currency ).setTotal( ValoreAmountPayPal );
		 			refund.setAmount(amount);
		 			DetailedRefund responseRefund = capture.refund(apiContext, refund); //refund(apiContext, refund);
				    //System.out.println(responseRefund);
				} catch (PayPalRESTException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else if( ProviderTipo.equals(Constants.TIPO_PAYMENT_PAYPAL_1) ){
				try{
					// RIMBORSO - PAYPAL 
					String ValoreAmountPayPal = ValoreRimborso.toString().replace(",", ".");
					String CredenzialiPayPal = GestioneApplicazioneUtil.CredenzialiRESTPayPal();
					APIContext apiContext = new APIContext(CredenzialiPayPal.split("#")[0], 
							CredenzialiPayPal.split("#")[1], CredenzialiPayPal.split("#")[2]) ;
					Amount amount = new Amount(); amount.setTotal( ValoreAmountPayPal ); amount.setCurrency("EUR");
					RefundRequest refund = new RefundRequest();
					refund.setDescription("rimborso id corsa: "+ricTransfert.getId()); refund.setAmount(amount);
					Payment payment = Payment.get(apiContext, ProviderId);
					final String acquistoId = payment.getTransactions().get(0).getRelatedResources().get(0).getSale().getId();
					Sale sale = new Sale(); sale.setId(acquistoId);
					// eseguo il rimborso
					DetailedRefund detRefund = sale.refund(apiContext, refund);
					// devo distinguere quale è delle due il valore che indirca il rimborso, 
					//in ambiente di test non è possibile verificarlo perché non è permesso il rmborso parziale. 
					//Probabilmente è il detRefund.getTotalRefundedAmount() mi da il totale di tutti i rimborsi e il detRefund.getAmount() solo quello singolo
					log.debug("detRefund.getAmount(): "+detRefund.getAmount());
					log.debug("detRefund.getTotalRefundedAmount(): "+detRefund.getTotalRefundedAmount());
		        } catch (PayPalRESTException payPalRESTException) {
		        	payPalRESTException.printStackTrace();
					return ParseErrorPayPalREST(payPalRESTException);
		        }
			}else{
				return "Errore getIdPaymentProvider - Rimborso non eseguito";
			}
			return CreaFatturaRimborso(ricTransfert);
		}else{
			return "Rimborso non eseguito - Ambiente di Produzione o di Test non sincronizzato con le Credenziali di Stripe o PayPal";
		}
	}

	/**
	 * La nota di credito è un documento fiscale che deve seguire gli stessi requisiti della fattura 
	 * art. 21 del DPR 633/72. Alla dicitura "fattura" va sostituita la dicitura "nota credito". 
	 * Viene adottata dal soggetto emittente per stornare in tutto o in parte una fattura precedentemente emessa.
	 */
	private static String CreaFatturaRimborso(RicercaTransfert ricTransfert)  {
		try {
			Double rimborso = Retrive_Amount_Rimborso_NomeCliente(ricTransfert).getRefund().doubleValue();
			// creo la fattura per il ritardo 
			if(fattureDao.getFatturaBy_IdRicercaTransfert_Rimbroso( ricTransfert.getId() ) == null){
				Fatture fattura = new Fatture();
				fattura.setProgressivoFattura( fattureDao.dammiNumeroProgressivoFattura() );
				fattura.setRicercaTransfertRimborso( ricTransfert );
				fattura.setNumeroAvvisiInviatiRimborso(0);
				fattureDao.saveFatture(fattura);
			}
			if( ricTransfert.getTipoServizio().equals(Constants.SERVIZIO_AGENDA_AUTISTA) ) {  // TODO NUOVO PRIMA NON C'ERA
				JSONObject infoDatiPasseggero = HomeUtil.GetInfoDatiPasseggero(ricTransfert);
				System.out.println("rimborso: "+rimborso);
				infoDatiPasseggero.put(Constants.AgendaAutista_RimborsoCliente, new BigDecimal(rimborso));
				ricTransfert.setInfoPasseggero( infoDatiPasseggero.toString() );
				ricercaTransfertDao.saveRicercaTransfert(ricTransfert);
				
			}else if( ricTransfert.getTipoServizio().equals(Constants.SERVIZIO_STANDARD) ) {  // TODO NUOVO PRIMA NON C'ERA
				// aggiorno la Richiesta media
				RichiestaMedia richiestaMedia = richiestaMediaDao.getRichiestaMedia_by_IdRicercaTransfert( ricTransfert.getId() );
				richiestaMedia.setRimborsoCliente( new BigDecimal(rimborso) );
				richiestaMediaDao.saveRichiestaMedia(richiestaMedia);
			
			}else if( ricTransfert.getTipoServizio().equals(Constants.SERVIZIO_PARTICOLARE) ) {  // TODO NUOVO PRIMA NON C'ERA
				// aggiorno la RichiestaAutistaParticolare
				RichiestaAutistaParticolare richAutPart = ricTransfert.getRichiestaAutistaParticolareAcquistato();
				System.out.println("rimborso: "+rimborso);
				richAutPart.setRimborsoCliente( new BigDecimal(rimborso) );
				richiestaAutistaParticolareDao.saveRichiestaAutistaParticolare(richAutPart);
			
			}else if( ricTransfert.getTipoServizio().equals(Constants.SERVIZIO_MULTIPLO) ) {  // TODO NUOVO PRIMA NON C'ERA
				JSONObject infoDatiPasseggero = HomeUtil.GetInfoDatiPasseggero(ricTransfert);
				System.out.println("rimborso: "+rimborso);
				infoDatiPasseggero.put(Constants.RichiestaAutistaMultiploRimborsoCliente, new BigDecimal(rimborso));
				ricTransfert.setInfoPasseggero( infoDatiPasseggero.toString() );
				ricercaTransfertDao.saveRicercaTransfert(ricTransfert);
			}
			return String.valueOf(true);
			
		}catch( Exception exc ) {
			exc.printStackTrace();
			return "rimborso eseguito ma fattura storno non generata | errore: "+exc.getMessage();
		}
	}
	
	
	private static String ParseErrorPayPalREST(PayPalRESTException payPalRESTException){
		log.debug("ParseErrorPayPal: "+payPalRESTException.getDetails().toJSON());
		final JSONObject obj = new JSONObject( payPalRESTException.getDetails().toJSON() );
		String problem = "";
		if( obj.getString("message") != null){
			problem = obj.getString("message").toString();
		}else{
			problem = payPalRESTException.getDetails().toJSON();
		}
		return problem;
	}
	
	
	
}
