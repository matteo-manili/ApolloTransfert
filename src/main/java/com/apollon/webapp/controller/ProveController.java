package com.apollon.webapp.controller;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.apollon.Constants;
import com.apollon.model.RicercaTransfert;
import com.apollon.service.GestioneApplicazioneManager;
import com.apollon.service.MailEngine;
import com.apollon.service.RicercaTransfertManager;
import com.apollon.webapp.util.LoginFacebook_Profile_Bean;
import com.apollon.webapp.util.LoginFacebook_Profile_Modal;
import com.apollon.webapp.util.LoginGoogle;
import com.apollon.webapp.util.controller.gestioneApplicazione.GestioneApplicazioneUtil;
import com.apollon.webapp.util.controller.home.HomeUtil;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.stripe.Stripe;
import com.stripe.model.Charge;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Controller
public class ProveController extends BaseFormController {
    
	protected MailEngine mailEngine = null;
	protected SimpleMailMessage message = null;
	
	private GestioneApplicazioneManager gestioneApplicazioneManager;
	@Autowired
	public void setGestioneApplicazioneManager(GestioneApplicazioneManager gestioneApplicazioneManager) {
		this.gestioneApplicazioneManager = gestioneApplicazioneManager;
	}
	
	private RicercaTransfertManager ricercaTransfertManager;
	@Autowired
	public void setRicercaTransfertManager(RicercaTransfertManager ricercaTransfertManager) {
		this.ricercaTransfertManager = ricercaTransfertManager;
	}
	
	@Autowired
    public void setMailEngine(MailEngine mailEngine) {
        this.mailEngine = mailEngine;
    }
	
	@Autowired
    public void setMessage(SimpleMailMessage message) {
        this.message = message;
    }

    @RequestMapping(value = "/prove", method = RequestMethod.POST)
	public String HomePost(final HttpServletRequest request, final String idRicTransfertDuplic ){
    	log.info("sono in onSubmitProve POST");
		try {

			final String[] groupRadioClassiAutoveicoli = request.getParameterValues("tipoUser");
            if(groupRadioClassiAutoveicoli != null) {
                for(final String groupRadioCalleAutoveicoli_check : groupRadioClassiAutoveicoli) {
                	
                    System.out.println(groupRadioCalleAutoveicoli_check);
                }
            }
			
            if( request.getParameterValues("tipoUser")[0].equals("2") ){
            	System.out.println("PORCODIo 2");
            }

	        return "redirect:/prove";
        }
		catch (final Exception e) {
    		e.printStackTrace();
    		return "redirect:/prove";
        }
    }
    
    
    private ModelAndView CaricaFormAutistaProve(ModelAndView mav, Locale locale, StringBuilder sb, RicercaTransfert ricercaTransfert) throws Exception{
        
    	// solo andata: 13175l
    	// andata e ritorno: 13162l
    	ricercaTransfert = ricercaTransfertManager.get( 13162l );
    	mav.addObject("ricercaTransfert", ricercaTransfert);
    	
    	// ROBA PAYPAL
    	String CredenzialiPayPal = GestioneApplicazioneUtil.CredenzialiWebButtonPayPal();
		mav.addObject("ID_CLIENT", CredenzialiPayPal.split("#")[0]); 
		mav.addObject("TYPE_ENV", CredenzialiPayPal.split("#")[1]);
		
		// ROBA STRIPE
		long stripeSWITCH = gestioneApplicazioneManager.getName("SWITCH_STRIPE_KEY_LIVE_TEST").getValueNumber();
		if(stripeSWITCH == 0l) {
			Stripe.apiKey = Constants.STRIPE_SECRET_KEY_TEST; // TEST
		}else {
			Stripe.apiKey = Constants.STRIPE_SECRET_KEY_LIVE; // LIVE
		}
		PaymentIntentCreateParams createParams = new PaymentIntentCreateParams.Builder().setCurrency("eur").setAmount(Long.parseLong("123"))
				.setDescription("tel. customer: "+"3289126869").build();
		PaymentIntent intent = PaymentIntent.create(createParams);
		String ClientSecretStripe = intent.getClientSecret();

		
		
		mav.addObject("CLIENT_SECRET_STRIPE", ClientSecretStripe);
		String CredenzialiStripe = GestioneApplicazioneUtil.CredenzialiPublishableStripe();
		mav.addObject("STRIPE_PUBLISCHABLE_KEY", CredenzialiStripe);
    	
    	// ALTRA ROBA
    	mav.addObject("languageDataTables", locale.getDisplayLanguage(new Locale("en")));
    	mav.addObject("memory", sb);
    	return mav;
    }
    


    @RequestMapping(value = "/prove", method = RequestMethod.GET)
    protected ModelAndView ProveGET(final HttpServletRequest request, final HttpServletResponse response) {
    	log.info("sono in prove GET");
    	final Locale locale = request.getLocale();
    	ModelAndView mav = new ModelAndView("prove");
    	//ModelAndView mav = new ModelAndView("home");
    	try{
    		//------------------------------
		
    		
    		mav.addObject("FB_APP_ID", Constants.FB_APP_ID);
    		
    		if( request.getParameter("access_token") != null ) {
	    		String access_token=(String)request.getParameter("access_token");
	    		LoginFacebook_Profile_Modal obj_Profile_Modal = new LoginFacebook_Profile_Modal();
	    		LoginFacebook_Profile_Bean obj_Profile_Bean = obj_Profile_Modal.call_me(access_token);

	    		System.out.println("facebook user: "+obj_Profile_Bean.getEmail()+" | "+obj_Profile_Bean.getFirst_name()+" | "+obj_Profile_Bean.getId());
    		}
    		
    		//------------------------------
    		NumberFormat format = NumberFormat.getInstance();
    		Runtime runtime = Runtime.getRuntime();
    		StringBuilder sb = new StringBuilder();
    		
    		// Get current size of heap in bytes
    		long heapSize = runtime.totalMemory();
    		System.out.println("runtime.totalMemory(): "+runtime.totalMemory());
    		// Get maximum size of heap in bytes. The heap cannot grow beyond this size. 
    		// Any attempt will result in an OutOfMemoryException.
    		long heapMaxSize = runtime.maxMemory();
    		System.out.println("runtime.maxMemory(): "+runtime.maxMemory());
    		// Get amount of free memory within the heap in bytes. This size will increase 
    		// after garbage collection and decrease as new objects are created.
    		long heapFreeSize = runtime.freeMemory();
    		System.out.println("runtime.freeMemory(): "+runtime.freeMemory());
    		System.out.println("-------------------------------");
    		System.out.println("allocated memory: " + format.format(heapSize / 1024) +" ( /1024)");
    		System.out.println("max memory: " + format.format(heapMaxSize / 1024) +" ( /1024)");
    		System.out.println("free memory: " + format.format(heapFreeSize / 1024) +" ( /1024)");
    		System.out.println("total free memory: " + format.format((heapFreeSize + (heapMaxSize - heapSize)) / 1024) +" (free memory + (max memory - allocated memory) /1024)");
    		
    		sb.append("allocated memory: " + format.format(heapSize / 1024) +"</br>");
    		sb.append("max memory: " + format.format(heapMaxSize / 1024) +"</br>");
    		sb.append("free memory: " + format.format(heapFreeSize / 1024) +"</br>");
    		sb.append("total free memory: " + format.format((heapFreeSize + (heapMaxSize - heapSize)) / 1024) +"</br>");
    		
    		
    		/*
    		try {
    			System.out.println( "RISULTATO getHostName: "+ InetAddress.getLocalHost().getHostName() );
    			System.out.println( "RISULTATO getLocalHost: "+ InetAddress.getLocalHost().getLocalHost() );
    			System.out.println( "RISULTATO getHostAddress: "+ InetAddress.getLocalHost().getHostAddress() ); 
    		} catch (UnknownHostException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		*/

    		/*
			//-------------------
    		RicercaTransfert ricTransfert = ricercaTransfertManager.get( 5283l );
    		RichiestaMediaAutista richiestaMediaAutista = ricercaTransfertManager.getRichiestaMediaAutista_AutistaAssegnatoCorsaMedia(ricTransfert.getId());
			if(richiestaMediaAutista != null){
				System.out.println("AUTISTA ASSEGNATO RimborsiUtil.InviaEmailCorsaCancellataAutista");
				RimborsiUtil.Invia_Sms_e_Email_CorsaCancellataMedio(richiestaMediaAutista, request, velocityEngine);
			}else{
				System.out.println("NON INVIATA RimborsiUtil.InviaEmailCorsaCancellataAutista");
			}
    		
    		//InviaEmail.InviaEmailCorsaVendutaCliente(ricercaTransfertManager.get(5282l), request, velocityEngine);
    		//-------------------
    		*/
    		
    		/*
    		//-------------------
    		long startTime = System.nanoTime();
    		HttpServletRequest requests = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
    		System.out.println("requests.getServerName(): "+requests.getServerName());
			if(request.getHeader("User-Agent").indexOf("Mobile") != -1) {
				mav.addObject("device", "mobile");
			}else{
				mav.addObject("device", "desktop");
			}
			*/
    		
    		/*
			// FAI OPERAZIONI PER MISURARE IL TEMPO OPERERAZIONI.....
    		long endTime = System.nanoTime();
    		long duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.
    		double seconds = (double)duration / 1000000000.0;
    		System.out.println("SECONDI: "+seconds);
    		//-------------------
    		*/
    		mav = CaricaFormAutistaProve(mav, locale, sb, null);
	    	return mav;
    	}catch(Exception exc){
    		exc.printStackTrace();
    		saveError(request, getText("errors.cancel", locale));
    		return new ModelAndView("prove");
    	}
    }

    @RequestMapping(value = "/proveLoginGoogle", method = RequestMethod.POST)
    protected ModelAndView proveLoginGoogleGET(final HttpServletRequest request, final HttpServletResponse response) {
    	log.info("sono in proveLoginGoogle POST");
    	final Locale locale = request.getLocale();
    	String paginaJsp = "prove";
    	ModelAndView mav = new ModelAndView( paginaJsp );
    	try{
    		
    		String idToken = request.getParameter("id_token");
    		log.info("idToken: "+idToken);
    		GoogleIdToken.Payload payLoad = LoginGoogle.getPayload(idToken);
            String name = (String) payLoad.get("name");
            String email = payLoad.getEmail();
            log.info("User name: " + name);
            log.info("User email: " + email);
    		
    		
    		mav = CaricaFormAutistaProve(mav, locale, null, null);
	    	return mav;
    	}catch(Exception exc){
    		exc.printStackTrace();
    		saveError(request, getText("errors.cancel", locale));
    		return new ModelAndView( paginaJsp );
    	}
    }
    
    

    
    @RequestMapping(value = "/provews", method = RequestMethod.GET)
    protected ModelAndView provewsGET(final HttpServletRequest request, final HttpServletResponse response) {
    	log.info("sono in provews GET");
    	final Locale locale = request.getLocale();
    	String paginaJsp = "prove_06";
    	ModelAndView mav = new ModelAndView( paginaJsp );
    	try{
    		
    		mav = CaricaFormAutistaProve(mav, locale, null, null);
	    	return mav;
    	}catch(Exception exc){
    		exc.printStackTrace();
    		saveError(request, getText("errors.cancel", locale));
    		return new ModelAndView( paginaJsp );
    	}
    }
    
} //fine classe
