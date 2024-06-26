package com.apollon.webapp.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.apollon.Constants;
import com.apollon.model.Ritardi;
import com.apollon.model.Supplementi;
import com.apollon.service.GestioneApplicazioneManager;
import com.apollon.service.RitardiManager;
import com.apollon.service.SupplementiManager;
import com.apollon.webapp.util.controller.gestioneApplicazione.GestioneApplicazioneUtil;
import com.apollon.webapp.util.controller.home.HomeUtil;
import com.apollon.webapp.util.controller.ritardi.RitardiUtil;
import com.apollon.webapp.util.email.InviaEmail;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.PaymentIntent;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Controller
public class Pagamento_Ritardo_Supplemento_Controller extends BaseFormController {
    
	
	private RitardiManager ritardiManager;
    @Autowired
    public void setRitardiManager(RitardiManager ritardiManager) {
		this.ritardiManager = ritardiManager;
	}
    
    private SupplementiManager supplementiManager;
    @Autowired
    public void setSupplementiManager(SupplementiManager supplementiManager) {
		this.supplementiManager = supplementiManager;
	}
    
    private GestioneApplicazioneManager gestioneApplicazioneManager;
    @Autowired
    public void setGestioneApplicazioneManager(GestioneApplicazioneManager gestioneApplicazioneManager) {
		this.gestioneApplicazioneManager = gestioneApplicazioneManager;
	}
    
    private VelocityEngine velocityEngine;
	@Autowired(required = false)
	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}
	

    //-----------------------------------------------------------------------------------------------
    //------------------------------------ PAGAMENTO RITARDO ----------------------------------------
    //-----------------------------------------------------------------------------------------------
	
	private String MessaggioPagamentoRitardoEseguito(BigDecimal totBigDecimal, Ritardi ritardo) {
		return "Pagamento Ritardo Eseguito: "+totBigDecimal.toString().replace(".", ",") + "€ - Corsa Numero: "+ritardo.getRicercaTransfert().getId();
	}
	
	@RequestMapping(value = "/pagamentoRitardo", method = RequestMethod.POST)
    public ModelAndView pagamentoRitardo( final HttpServletRequest request, final HttpServletResponse response) throws Exception {
		log.info("sono in pagamentoRitardo POST");
		ModelAndView mav = new ModelAndView("payment-ritardo");
		if (request.getParameter("cancel") != null) {
			return CaricaPagamentoRitardo(new ModelAndView("home-user"), null);
        }
		Ritardi ritardo = ritardiManager.get( Long.parseLong( request.getParameter("ritardo-id") ) );
		try{
			if(request.getParameter("pagamento-stripe") != null) {
				try{
					BigDecimal totBigDecimal = ritardo.getPrezzoAndata().add(ritardo.getPrezzoRitorno());
					if(ritardo.isPagatoAndata() && ritardo.isPagatoRitorno() ){
						saveMessage(request, "pagamento Ritardo già eseguito.");
						return CaricaPagamentoRitardo(mav, ritardo);
					}
					String stripeId_PaymentIntent = request.getParameter("stripePaymentIntent");
					// FACCIO IL PAGAMENTO
					Charge charge = HomeUtil.SalvaInfoStripe_StrongCostumerAuthentication(stripeId_PaymentIntent, ritardo);
					if( charge != null && charge.getStatus().equals("succeeded") ){
						// SALVO IL PAGAMENTO ESEGUITO
						ritardo.setPagatoAndata(true); ritardo.setPagatoRitorno(true); ritardo.setIdPaymentProvider(charge.getId());
						ritardiManager.saveRitardi(ritardo);
						//TODO INVIA EMAIL CONFERMA PAGAMENTO RITORARDO
						InviaEmail.Invia_Email_ConfermaPagamentoRitardoCliente(ritardo, request, velocityEngine);
						saveMessage(request, MessaggioPagamentoRitardoEseguito(totBigDecimal, ritardo));				
						return new ModelAndView("redirect:/home-user");
		
					}else if( charge != null && charge.getFailureMessage() != null ){
						saveMessage(request, "pagamento non eseguito: "+charge.getFailureMessage() +" code: "+charge.getFailureCode() );
					}else{
						saveMessage(request, "pagamento non eseguito." );
					}
					return CaricaPagamentoRitardo(mav, ritardo);
				} catch (StripeException e) {
					saveError(request,  "errore carta di credito. ("+e.getMessage()+")" );
					e.printStackTrace();
					return CaricaPagamentoRitardo(mav, ritardo);
				}
			}
			if(request.getParameter("pagamento-paypal") != null){
				if( request.getParameter("payment-paypal-error") == null && request.getParameter("payment-paypal-id") != null ){
					String payPalPaymentId = request.getParameter("payment-paypal-id");
					BigDecimal totBigDecimal = ritardo.getPrezzoAndata().add(ritardo.getPrezzoRitorno());
					String totaleEuro = totBigDecimal.toString();
					totaleEuro = totaleEuro.replace(".", ""); // questo perche stripe prende le valute in centesimi, per esempio 3.30 euro sono 330 centesimi
					if(ritardo.isPagatoAndata() && ritardo.isPagatoRitorno()){
						saveMessage(request, "Pagamento Ritardo già eseguito.");
						return CaricaPagamentoRitardo(mav, ritardo);
					}
					// SALVO IL PAGAMENTO ESEGUITO
					ritardo.setPagatoAndata(true); ritardo.setPagatoRitorno(true); ritardo.setIdPaymentProvider(payPalPaymentId);
					ritardiManager.saveRitardi(ritardo);
					// TODO INVIO EMAIL CONFERMA PAGAMENTO RITARDO
					InviaEmail.Invia_Email_ConfermaPagamentoRitardoCliente(ritardo, request, velocityEngine);
					saveMessage(request, MessaggioPagamentoRitardoEseguito(totBigDecimal, ritardo));
					return new ModelAndView("redirect:/home-user");
				}else{
					saveMessage(request, "pagamento PayPal non eseguito: "+request.getParameter("payment-paypal-error"));
					return CaricaPagamentoRitardo(mav, ritardo);
				}
			}
		}catch (Exception e) {
			saveError(request,  "errore Generale. ("+e.getMessage()+")" );
			e.printStackTrace();
			return CaricaPagamentoRitardo(mav, ritardo);
		}
		return mav;
    }
	
    private ModelAndView CaricaPagamentoRitardo(ModelAndView mav, Ritardi ritardo) throws Exception {
    	mav.addObject("ritardo", ritardo);
		// INIZIO NUOVO STRIPE -----------------------------------------------------
    	String CredenzialiStripe = GestioneApplicazioneUtil.CredenzialiPublishableStripe();
		mav.addObject("STRIPE_PUBLISCHABLE_KEY", CredenzialiStripe);  
		BigDecimal totBigDecimal = ritardo.getPrezzoAndata().add(ritardo.getPrezzoRitorno());
		Map<String, Object> paymentintentParams = new HashMap<String, Object>();
		paymentintentParams.put("amount", HomeUtil.DammiMonetaStripe(totBigDecimal));
		paymentintentParams.put("currency", "eur");
		long stripeSWITCH = gestioneApplicazioneManager.getName("SWITCH_STRIPE_KEY_LIVE_TEST").getValueNumber();
    	if(stripeSWITCH == 0l){
			Stripe.apiKey = Constants.STRIPE_SECRET_KEY_TEST; // TEST
		}else{
			Stripe.apiKey = Constants.STRIPE_SECRET_KEY_LIVE; // LIVE
		}
		PaymentIntent intent = PaymentIntent.create(paymentintentParams);
		mav.addObject("client_secret", intent.getClientSecret());
		// FINE NUOVO STRIPE -----------------------------------------------------
		
		String CredenzialiPayPal = GestioneApplicazioneUtil.CredenzialiWebButtonPayPal();
		mav.addObject("ID_CLIENT", CredenzialiPayPal.split("#")[0]);
		mav.addObject("TYPE_ENV", CredenzialiPayPal.split("#")[1]);
		// TEST O PRODUZIONE SMS, STRIPE E PAYPAL
    	long smsSkebbyAbilitato = gestioneApplicazioneManager.getName("INVIO_SMS_ABILITATO").getValueNumber();
		if(smsSkebbyAbilitato == 0l){
			mav.addObject("smsSkebbyAbilitato", false);
		}else{
			mav.addObject("smsSkebbyAbilitato", true);
		}
		if(stripeSWITCH == 0l){
			mav.addObject("stripeSWITCH", false);  
		}else{
			mav.addObject("stripeSWITCH", true);  
		}
		long payPalSWITCH = gestioneApplicazioneManager.getName("SWITCH_PAYPAL_KEY_LIVE_TEST").getValueNumber();
		if(payPalSWITCH == 0l){
			mav.addObject("payPalSWITCH", false);
		}else{
			mav.addObject("payPalSWITCH", true);
		}
		return RitardiUtil.AddAttribute_Ritardi(mav);
    }
	
    @RequestMapping(value = "/pagamentoRitardo", method = RequestMethod.GET)
    protected ModelAndView pagamentoRitardo( @RequestParam(required = false, value = "courseId") String courseId) {
    	log.info("sono in pagamentoRitardo GET");
    	ModelAndView mav = new ModelAndView("payment-ritardo");
    	try{
    		Ritardi ritardo = ritardiManager.getRitardoBy_IdRicercaTransfert( Long.parseLong( courseId ) );
			mav = CaricaPagamentoRitardo(mav, ritardo);
			return mav;
    	}catch(Exception exc){
    		exc.printStackTrace();
    		return mav;
    	}
    }
   
    //-----------------------------------------------------------------------------------------------
    //------------------------------------ PAGAMENTO SUPPLEMENTO ------------------------------------
    //-----------------------------------------------------------------------------------------------
    
	private String MessaggioPagamentoSupplementoEseguito(BigDecimal totBigDecimal, Supplementi supplemento){
		return "Pagamento Supplemento Eseguito: "+totBigDecimal.toString().replace(".", ",") + "€ - Corsa Numero: "+supplemento.getRicercaTransfert().getId();
	}
    
    @RequestMapping(value = "/pagamentoSupplemento", method = RequestMethod.POST)
    public ModelAndView pagamentoSupplemento( final HttpServletRequest request, final HttpServletResponse response) throws Exception {
		log.info("sono in pagamentoSupplemento POST");
		ModelAndView mav = new ModelAndView("payment-supplemento"); 
		if (request.getParameter("cancel") != null) {
			return CaricaPagamentoSupplemento(new ModelAndView("home-user"), null);
        }
		Supplementi supplemento = supplementiManager.get( Long.parseLong( request.getParameter("supplemento-id") ) );
		try {
			if(request.getParameter("pagamento-stripe") != null) {
				try{
					BigDecimal totBigDecimal = supplemento.getPrezzo();
					if(supplemento.isPagato() ){
						saveMessage(request, "pagamento Supplemento già eseguito.");
						return CaricaPagamentoSupplemento(mav, supplemento);
					}
					String stripeId_PaymentIntent = request.getParameter("stripePaymentIntent"); //stripeId_PaymentIntent
					Charge charge = HomeUtil.SalvaInfoStripe_StrongCostumerAuthentication(stripeId_PaymentIntent, supplemento);
					if( charge != null && charge.getStatus().equals("succeeded") ){
						// SALVO IL PAGAMENTO ESEGUITO
						supplemento.setPagato(true); supplemento.setIdPaymentProvider( charge.getId() );
						supplementiManager.saveSupplementi(supplemento);
						//TODO INVIA EMAIL CONFERMA PAGAMENTO RITORARDO
						InviaEmail.Invia_Email_ConfermaPagamentoSupplementoCliente(supplemento, request, velocityEngine);
						saveMessage(request, MessaggioPagamentoSupplementoEseguito(totBigDecimal, supplemento));
						return new ModelAndView("redirect:/home-user");
					}else if( charge != null && charge.getFailureMessage() != null ) {
						saveMessage(request, "pagamento non eseguito: "+charge.getFailureMessage() +" code: "+charge.getFailureCode() );
					}else {
						saveMessage(request, "pagamento non eseguito." );
					}
					return CaricaPagamentoSupplemento(mav, supplemento);
				} catch (StripeException e) {
					saveError(request,  "errore carta di credito. ("+e.getMessage()+")" );
					e.printStackTrace();
					return CaricaPagamentoSupplemento(mav, supplemento);
				}
			}
			if(request.getParameter("pagamento-paypal") != null) {
				if( request.getParameter("payment-paypal-error") == null && request.getParameter("payment-paypal-id") != null ){
					String payPalPaymentId = request.getParameter("payment-paypal-id");
					BigDecimal totBigDecimal = supplemento.getPrezzo();
					String totaleEuro = totBigDecimal.toString();
					totaleEuro = totaleEuro.replace(".", ""); // questo perche stripe prende le valute in centesimi, per esempio 3.30 euro sono 330 centesimi
					if(supplemento.isPagato()){
						saveMessage(request, "Pagamento Supplemento già eseguito.");
						return CaricaPagamentoSupplemento(mav, supplemento);
					}
					// SALVO IL PAGAMENTO ESEGUITO
					supplemento.setPagato(true); supplemento.setIdPaymentProvider(payPalPaymentId);
					supplementiManager.saveSupplementi(supplemento);
					// TODO INVIO EMAIL CONFERMA PAGAMENTO SUPPLEMENTO
					InviaEmail.Invia_Email_ConfermaPagamentoSupplementoCliente(supplemento, request, velocityEngine);
					saveMessage(request, MessaggioPagamentoSupplementoEseguito(totBigDecimal, supplemento));
					return new ModelAndView("redirect:/home-user");
				}else{
					saveMessage(request, "pagamento PayPal non eseguito: "+request.getParameter("payment-paypal-error"));
					return CaricaPagamentoSupplemento(mav, supplemento);
				}
			}
	    }catch (Exception e) {
			saveError(request,  "errore Generale. ("+e.getMessage()+")" );
			e.printStackTrace();
			return CaricaPagamentoSupplemento(mav, supplemento);
		}
		return mav;
    }
	
    private ModelAndView CaricaPagamentoSupplemento(ModelAndView mav, Supplementi supplemento) throws StripeException {
    	mav.addObject("supplemento", supplemento);
    	// INIZIO NUOVO STRIPE -----------------------------------------------------
    	String CredenzialiStripe = GestioneApplicazioneUtil.CredenzialiPublishableStripe();
		mav.addObject("STRIPE_PUBLISCHABLE_KEY", CredenzialiStripe);  
		Map<String, Object> paymentintentParams = new HashMap<String, Object>();
		paymentintentParams.put("amount", HomeUtil.DammiMonetaStripe(supplemento.getPrezzo()));
		paymentintentParams.put("currency", "eur");
		long stripeSWITCH = gestioneApplicazioneManager.getName("SWITCH_STRIPE_KEY_LIVE_TEST").getValueNumber();
    	if(stripeSWITCH == 0l){
			Stripe.apiKey = Constants.STRIPE_SECRET_KEY_TEST; // TEST
		}else{
			Stripe.apiKey = Constants.STRIPE_SECRET_KEY_LIVE; // LIVE
		}
		PaymentIntent intent = PaymentIntent.create(paymentintentParams);
		mav.addObject("client_secret", intent.getClientSecret());
		// FINE NUOVO STRIPE -----------------------------------------------------
		
		String CredenzialiPayPal = GestioneApplicazioneUtil.CredenzialiWebButtonPayPal();
		mav.addObject("ID_CLIENT", CredenzialiPayPal.split("#")[0]);
		mav.addObject("TYPE_ENV", CredenzialiPayPal.split("#")[1]);
		// TEST O PRODUZIONE SMS, STRIPE E PAYPAL
    	long smsSkebbyAbilitato = gestioneApplicazioneManager.getName("INVIO_SMS_ABILITATO").getValueNumber();
		if(smsSkebbyAbilitato == 0l){
			mav.addObject("smsSkebbyAbilitato", false);
		}else{
			mav.addObject("smsSkebbyAbilitato", true);
		}
		if(stripeSWITCH == 0l){
			mav.addObject("stripeSWITCH", false);  
		}else{
			mav.addObject("stripeSWITCH", true);  
		}
		long payPalSWITCH = gestioneApplicazioneManager.getName("SWITCH_PAYPAL_KEY_LIVE_TEST").getValueNumber();
		if(payPalSWITCH == 0l){
			mav.addObject("payPalSWITCH", false);
		}else{
			mav.addObject("payPalSWITCH", true);
		}
		return mav;
    }
    
    @RequestMapping(value = "/pagamentoSupplemento", method = RequestMethod.GET)
    protected ModelAndView pagamentoSupplemento( @RequestParam(required = false, value = "idSupplemento") String idSupplemento) {
    	log.info("sono in pagamentoSupplemento GET");
    	ModelAndView mav = new ModelAndView("payment-supplemento");
    	try{
    		Supplementi supplemento = supplementiManager.get( Long.parseLong( idSupplemento ) );
			mav = CaricaPagamentoSupplemento(mav, supplemento);
			return mav;
    	}catch(Exception exc){
    		exc.printStackTrace();
    		return mav;
    	}
    }
    
    
} //fine classe
