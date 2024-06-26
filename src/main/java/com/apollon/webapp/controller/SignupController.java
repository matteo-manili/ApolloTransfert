package com.apollon.webapp.controller;

import java.util.Date;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.apollon.Constants;
import com.apollon.model.Autista;
import com.apollon.model.User;
import com.apollon.service.AutistaManager;
import com.apollon.service.RoleManager;
import com.apollon.service.TipoRuoliManager;
import com.apollon.util.UtilBukowski;
import com.apollon.util.UtilString;
import com.apollon.util.customexception.UserExistsException;
import com.apollon.webapp.util.ApplicationUtils;
import com.apollon.webapp.util.InfoUserConnectAddressMain;
import com.apollon.webapp.util.email.InviaEmail;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.tuckey.web.filters.urlrewrite.utils.StringUtils;

/**
 * Controller to signup new users.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
@Controller
public class SignupController extends BaseFormController {
	
    private RoleManager roleManager;
    @Autowired
    public void setRoleManager(final RoleManager roleManager) {
        this.roleManager = roleManager;
    }
    
    private TipoRuoliManager tipoRuoliManager;
    @Autowired
    public void setTipoRuoliManager(final TipoRuoliManager tipoRuoliManager) {
        this.tipoRuoliManager = tipoRuoliManager;
    }
    
    private AutistaManager autistaManager;
    @Autowired
    public void setAutistaManager(final AutistaManager autistaManager) {
        this.autistaManager = autistaManager;
    }
    
    private VelocityEngine velocityEngine;
   	@Autowired(required = false)
   	public void setVelocityEngine(VelocityEngine velocityEngine) {
   		this.velocityEngine = velocityEngine;
   	}
    
   	@RequestMapping(value="/{lang}/signup", method = RequestMethod.POST)
    public ModelAndView onSubmit(final User userForm, final BindingResult errors, final HttpServletRequest request, final HttpServletResponse response, 
    		@PathVariable("lang") final String language) throws Exception {

    	ModelAndView mav = new ModelAndView("signup");
    	
    	Locale locale = request.getLocale();
    	
        if (request.getParameter("cancel") != null) {
        	return caricaFormSignUp(mav, new User(), null);
        }

        if(validator != null) { // validator is null during testing
    		validator.validate(userForm, errors);
            if (errors.hasErrors() ) { // don't validate when deleting
            	mav.addAllObjects(errors.getModel());
            }
        }
        //rimuovo gli spazi
        userForm.setEmail( UtilString.RimuoviTuttiGliSpazi(userForm.getEmail()).toLowerCase() );
        if(request.getParameter("number-tel-autista-pre") != null && request.getParameter("number-tel-autista-pre").equals("")){
			errors.rejectValue("phoneNumber", "errors.required", new Object[] { "telefono" }, "");
		}else{
			if(request.getParameter("number-tel-autista") != null && request.getParameter("number-tel-autista").equals("") || request.getParameter("number-tel-autista").equals("noValidNumber")){
				errors.rejectValue( "phoneNumber", "errors.format.phoneNumber", new Object[] { request.getParameter("number-tel-autista-pre") }, "");
			}else{
				userForm.setPhoneNumber( request.getParameter("number-tel-autista") );
			}
		}

        if(request.getParameter("password") != null && request.getParameter("password").equals("") || StringUtils.isBlank(request.getParameter("password"))) {
        	errors.rejectValue("password", "errors.required", new Object[] { getText("user.password", locale) },"");
        }else{
        	if(request.getParameter("conferma-password") != null && !request.getParameter("conferma-password").equals( request.getParameter("password")) ) {
        		errors.rejectValue("confirmPassword", "errors.twofields",new Object[] { getText("user.password", locale), getText("user.confirmPassword", locale) }, "" );
        	}else {
        		userForm.setPassword( request.getParameter("password") );
        	}
        }
        
        if(userForm.isCheckPrivacyPolicy() == false){
        	errors.rejectValue("checkPrivacyPolicy", "errors.checked", new Object[] { getText("consenso.trattamento.dati", locale) },"");
        }
        
        
        
        /*
        if( request.getParameterValues("tipoUser") != null ){
        	System.out.println("tipoUser: "+ request.getParameterValues("tipoUser")[0] );
        }
        */
        if(errors != null && errors.getErrorCount() > 0 ){
			mav.addAllObjects(errors.getModel());
			return caricaFormSignUp(mav, userForm, Integer.parseInt(request.getParameterValues("tipoUser")[0]));
		}
        
        userForm.setFirstName( UtilString.PrimaLetteraMaiuscola( userForm.getFirstName() ).trim() );
        userForm.setLastName( UtilString.PrimaLetteraMaiuscola( userForm.getLastName() ).trim() );
        
        userForm.setEnabled(true);
        userForm.setSignupDate(new Date());
        
        // Set the default user role on this new user
        userForm.addRole(roleManager.getRole(Constants.USER_ROLE));
        
        //INIZIO NUOVO
        if( request.getParameterValues("tipoUser")[0].equals(Integer.toString(Constants.USER_VENDITORE))){
        	userForm.addRole(roleManager.getRole(Constants.VENDITORE_ROLE));
        	String codiceVenditore = userForm.getFirstName().toUpperCase();
        	int conta = 1;
        	while( getUserManager().getUserIdVenditore_by_CodiceVenditore(codiceVenditore) != null ){
        		codiceVenditore = userForm.getFirstName().toUpperCase() + conta++;
        	}
        	userForm.setCodiceVenditore(codiceVenditore);
        }
        //FINE NUOVO
        
        // setto il tipo ruolo Cliente
        userForm.addTipoRuoli( tipoRuoliManager.getTipoRuoliByName(Constants.CLIENTE));
        
        String usernameGenerata = DammiUsername(request);
        
		while( getUserManager().userUsernameExist(usernameGenerata) == true ){
			usernameGenerata = DammiUsername(request);
		}
		userForm.setUsername( usernameGenerata );
		userForm.setIpAddressSignUp( InfoUserConnectAddressMain.DammiIPaddress(request) );
		
        try {
        	User userSalvato = this.getUserManager().saveUser(userForm);

        	//INIZIO NUOVO
        	if( request.getParameterValues("tipoUser")[0].equals(Integer.toString(Constants.USER_AUTISTA))){
            	Autista autista = new Autista(userSalvato, true);
        		autistaManager.saveAutista(autista);
        		userSalvato.addTipoRuoli( tipoRuoliManager.getTipoRuoliByName(Constants.AUTISTA) );
        		userSalvato = this.getUserManager().saveUser(userSalvato);
        	}
        	//FINE NUOVO
        	
	        saveMessage(request, getText("user.registered", userSalvato.getUsername(), locale));
	        request.getSession().setAttribute(Constants.REGISTERED, Boolean.TRUE);
	        // log user in automatically
	        final UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
	        		userSalvato.getUsername(), userSalvato.getPassword(), userSalvato.getAuthorities());
	        auth.setDetails(userSalvato);
	        SecurityContextHolder.getContext().setAuthentication(auth);
	        // Send user an e-mail
	        if (log.isDebugEnabled()) {
	            log.debug("Sending user '" + userSalvato.getUsername() + "' an account information e-mail");
	        }
	        try {
	        	InviaEmail.sendUserEmailAccountCreated(userSalvato, request.getParameter("password"), velocityEngine, request);
	        } catch (final MailException me) {
	        	me.printStackTrace();
	            saveError(request, getText("errors.sending.email.confirm", new Object[] { userSalvato.getEmail() },  locale ) );
	        }
	        // vado alla home utente
	        //mav = caricaFormSignUp( new ModelAndView("home-user"), userSalvato, false, true);
	        return new ModelAndView("redirect:/home-user?registrazioneEseguita=true");
	        
        } catch (final AccessDeniedException ade) {
            // thrown by UserSecurityAdvice configured in aop:advisor userManagerSecurity
            log.warn(ade.getMessage());
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return null;
        } catch (final UserExistsException e) {
            errors.rejectValue("username", "errors.existing.user", new Object[] { userForm.getEmail(), userForm.getPhoneNumber() }, "duplicate user");
            mav.addAllObjects(errors.getModel());
            return caricaFormSignUp(mav, userForm, Integer.parseInt(request.getParameterValues("tipoUser")[0]));
        }
    }
    
    private String DammiUsername(HttpServletRequest request){
    	if( request.getParameterValues("tipoUser")[0].equals( Integer.toString(Constants.USER_NORMALE) /*"1"*/)){
        	return UtilBukowski.GeneraUsername(0);
        }else if( request.getParameterValues("tipoUser")[0].equals( Integer.toString(Constants.USER_AUTISTA) /*"2"*/)){
        	return UtilBukowski.GeneraUsername(1);
        }else if( request.getParameterValues("tipoUser")[0].equals( Integer.toString(Constants.USER_VENDITORE) /*"3"*/)){
        	return UtilBukowski.GeneraUsername(2);
        }else{
        	return null;
        }
    }
    
    private ModelAndView caricaFormSignUp(ModelAndView mav, User user, Integer tipoUser) throws Exception{
    	mav.addObject("tipoUser", (tipoUser == null ? Constants.USER_NORMALE : tipoUser) );
    	mav.addObject("user", user);
    	if( ApplicationUtils.CheckAmbienteVenditore(getServletContext()) ){
    		mav.addObject("ambienteVenditore", true);
    	}
    	return mav;
    }
    
    
    @RequestMapping(value = "/{lang}/signup", method = RequestMethod.GET)
    protected ModelAndView SignUpGET(final HttpServletRequest request, final HttpServletResponse response, 
    		@RequestParam(value = "tipoUser", required = false) final String tipoUser, @PathVariable("lang") final String language){
    	log.info("sono in SignUpGET GET");
    	final Locale locale = request.getLocale();
    	ModelAndView mav = new ModelAndView("signup");
    	try {
    		if(tipoUser != null && tipoUser.equals("autista")){
    			return caricaFormSignUp(mav, new User(), Constants.USER_AUTISTA);
    		}else if(tipoUser != null && tipoUser.equals("venditore")){
    			return caricaFormSignUp(mav, new User(), Constants.USER_VENDITORE);
    		}else{
    			return caricaFormSignUp(mav, new User(), Constants.USER_NORMALE );
    		}
		} catch (Exception exc) {
			exc.printStackTrace();
    		saveError(request, getText("errors.cancel", locale));
    		return new ModelAndView("signup");
		}
    }

    
}
