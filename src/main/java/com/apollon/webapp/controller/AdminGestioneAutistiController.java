package com.apollon.webapp.controller;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.apollon.Constants;
import com.apollon.model.Autista;
import com.apollon.model.User;
import com.apollon.service.AutistaManager;
import com.apollon.service.RoleManager;
import com.apollon.service.TipoRuoliManager;
import com.apollon.service.UserManager;
import com.apollon.util.UtilBukowski;
import com.apollon.util.UtilString;
import com.apollon.webapp.util.ControllerUtil;
import com.apollon.webapp.util.MenuAutistaAttribute;
import com.apollon.webapp.util.controller.documenti.DocumentiInfoUtil;
import com.apollon.webapp.util.controller.documenti.InsertDocumentiUtil;
import com.apollon.webapp.util.email.InviaEmail;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Implementation of <strong>SimpleFormController</strong> that interacts with
 * the {@link UserManager} to retrieve/persist values to the database.
 *
 * <p><a href="UserFormController.java.html"><i>View Source</i></a>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
@Controller
@RequestMapping("/admin/gestioneAutista*")
public class AdminGestioneAutistiController extends BaseFormController {


	private AutistaManager autistaManager;
    @Autowired
    public void setAutistaManager(final AutistaManager autistaManager) {
        this.autistaManager = autistaManager;
    }
	
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
    
    private VelocityEngine velocityEngine;
   	@Autowired(required = false)
   	public void setVelocityEngine(VelocityEngine velocityEngine) {
   		this.velocityEngine = velocityEngine;
   	}
    

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView onSubmit(@ModelAttribute("autista") final Autista autistaForm, BindingResult errors, 
    		final HttpServletRequest request, final HttpServletResponse response) {
    	log.info("sono in GestioneAutistaController.onSubmit POST");
    	ModelAndView mav = new ModelAndView("admin/gestione-autista");
    	try {
	        if(request.getParameter("cancel") != null) {
	    		return CaricaFormGestioneAutisti(mav, request.getParameter("autista-id").toString(), null, request);
	        }
	        if(!request.isUserInRole(Constants.GEST_AUTISTA_ROLE) && !request.isUserInRole(Constants.ADMIN_ROLE)){
	            response.sendError(HttpServletResponse.SC_FORBIDDEN);
	            throw new AccessDeniedException("Non hai il permesso per modificare gli autisti.");
	        }
	        User userCorrente = getUserManager().getUserByUsername(request.getRemoteUser());
	        if(request.getParameter("autista-user-id") != null && !request.getParameter("autista-user-id").equals("")){
	    		User tipoUser = getUserManager().get( Long.parseLong(request.getParameter("autista-user-id")) );
	    		if( tipoUser.getId() != userCorrente.getId() && !request.isUserInRole(Constants.ADMIN_ROLE) && (tipoUser.getRoles().contains(roleManager.getRole(Constants.ADMIN_ROLE)) || 
		        		tipoUser.getRoles().contains(roleManager.getRole(Constants.GEST_AUTISTA_ROLE))) ){
	        		errors.rejectValue("", "errors.update.admin.violation");
	        		mav.addAllObjects(errors.getModel());
	        		return CaricaFormGestioneAutisti(mav, "", autistaManager.getAutistaByUser(tipoUser.getId()), request);
	        	}
			}
	        /**
    		 * utilizzo sia la verifica con validation.xml e sia quella manuale, sommando gli esiti.
    		 * questo deve stare sotto il ANNULLA altrmenti il ANNULLA non funziona
    		 */
			if(validator != null) { // validator is null during testing
	    		validator.validate(autistaForm, errors);
	            if (errors.hasErrors() ) { // don't validate when deleting
	            	mav.addAllObjects(errors.getModel());
	            }
	        }
	    	Autista autistaDati = null;
	    	if(request.getParameter("autista-id") != null && !request.getParameter("autista-id").equals("")){
	    		autistaDati = autistaManager.get( Long.parseLong( request.getParameter("autista-id") ) );
	    	}else{
	    		autistaDati = new Autista(new User(), true);
	    	}
	    	//rimuovo gli spazi
	    	autistaDati.getUser().setEmail( UtilString.RimuoviTuttiGliSpazi(autistaForm.getUser().getEmail()).toLowerCase() );
	    	
	    	
	    	
	    	autistaDati.setNumCorseEseguite( autistaForm.getNumCorseEseguite() );
	    	autistaDati.setBannato( autistaForm.isBannato() );
	    	autistaDati.setAttivo( autistaForm.isAttivo() );
	    	// non lo uso più nella jsp, attenzione che l'ho messo a EAGER
	    	if(request.getParameter("id-commerciale") != null && !request.getParameter("id-commerciale").equals("")){
	    		autistaDati.setCommerciale( getUserManager().get(Long.parseLong(request.getParameter("id-commerciale"))) );
			}
	    	if(request.getParameter("first-name") != null && request.getParameter("first-name").equals("")){
				errors.rejectValue("user.firstName", "errors.required", new Object[] { "nome" }, "");
			}else{
				autistaDati.getUser().setFirstName( UtilString.PrimaLetteraMaiuscola(request.getParameter("first-name")).trim() );
			}
	    	if(request.getParameter("last-name") != null && request.getParameter("last-name").equals("")){
				errors.rejectValue("user.lastName", "errors.required", new Object[] { "cognome" }, "");
	    	}else{
	    		autistaDati.getUser().setLastName( UtilString.PrimaLetteraMaiuscola(request.getParameter("last-name")).trim() );
			}
	    	if(request.getParameter("password") != null && request.getParameter("password").equals("")){
				errors.rejectValue("user.password", "errors.required", new Object[] { "password" }, "");
			}else{
				autistaDati.getUser().setPassword( request.getParameter("password") );
			}
	    	if(request.getParameter("number-tel-autista-pre") != null && request.getParameter("number-tel-autista-pre").equals("")){
				errors.rejectValue("user.phoneNumber", "errors.required", new Object[] { "telefono" }, "");
			}else{
				if(request.getParameter("number-tel-autista") != null && request.getParameter("number-tel-autista").equals("") || request.getParameter("number-tel-autista").equals("noValidNumber")){
					errors.rejectValue("user.phoneNumber", "errors.format.phoneNumber", new Object[] { request.getParameter("number-tel-autista-pre") }, "");
				}else{
					autistaDati.getUser().setPhoneNumber( request.getParameter("number-tel-autista") );
				}
			}
	    	if(request.getParameter("carta-identita") != null && !request.getParameter("carta-identita").equals("")){
	    		autistaDati.getAutistaDocumento().setNumeroCartaIdentita( request.getParameter("carta-identita") );
	    	}
	    	if(autistaDati != null && autistaDati.getId() != null){
	        	if(request.getParameter("tipoAutistaAziendaCheck") != null){
	        		InsertDocumentiUtil.AbilitaAzienda(request, autistaDati);
				}else{
					InsertDocumentiUtil.DisabilitaAzienda(request, autistaDati);
				}
	    	}
	    	if(request.getParameter("note") != null){
	    		autistaDati.setNote( request.getParameter("note") );
	    	}
	        if(errors != null && errors.getErrorCount() > 0 ){
				mav.addAllObjects(errors.getModel());
				return CaricaFormGestioneAutisti(mav, "", autistaDati, request);
			}
    		/**
	         * MODIFICA AUTISTA
	         */
	        if(request.getParameter("modifica") != null) {
	        	if( (request.isUserInRole(Constants.GEST_AUTISTA_ROLE) && autistaDati.getCommerciale() != null 
	        			&& autistaDati.getCommerciale().getId() == userCorrente.getId()) || request.isUserInRole(Constants.ADMIN_ROLE) || autistaDati.getUser().getId() == userCorrente.getId() ){
		        	User user = getUserManager().get( autistaDati.getUser().getId() );
		        	user.setFirstName( autistaDati.getUser().getFirstName() );
					user.setLastName( autistaDati.getUser().getLastName() );
					if( !user.getEmail().equalsIgnoreCase( autistaDati.getUser().getEmail() ) ){
						if(autistaDati.getUser().getEmail() != null && !autistaDati.getUser().getEmail().equals("")){
			    			if( getUserManager().userEmailExist( autistaDati.getUser().getEmail() )) {
			    				errors.rejectValue("user.email", "errors.existing.autista.email.user", new Object[] { autistaDati.getUser().getEmail() }, "");
			    				mav.addAllObjects(errors.getModel());
			    				return CaricaFormGestioneAutisti(mav, "", autistaDati, request);
			    			}else{
			    				user.setEmail( autistaDati.getUser().getEmail() );
			    			}
			    		}else{
			    			user.setEmail( user.getUsername()+Constants.FAKE_EMAIL );
			    		}
					}
					if( !user.getPhoneNumber().equals( request.getParameter("number-tel-autista") ) ){
						if(request.getParameter("number-tel-autista") != null && !request.getParameter("number-tel-autista").equals("")){
							if( getUserManager().userTelephoneExist( request.getParameter("number-tel-autista") )) {
								errors.rejectValue("user.phoneNumber", "errors.existing.autista.phoneNumber.user", new Object[] { request.getParameter("number-tel-autista") }, "");
			    				mav.addAllObjects(errors.getModel());
			    				return CaricaFormGestioneAutisti(mav, "", autistaDati, request);
							}else{
								user.setPhoneNumber( request.getParameter("number-tel-autista") );
							}
						}
					}
					if( request.getParameter("newPassword") != null && !request.getParameter("newPassword").equals("") ){
						user.setPassword( request.getParameter("newPassword") );
					}
					user = getUserManager().saveUser( user );
		        	autistaDati.setUser( user );
		        	Autista autistaModificato = autistaManager.saveAutista(autistaDati);
		        	saveMessage(request, "Autista "+autistaModificato.getUser().getFirstName()+" "+autistaModificato.getUser().getLastName()+" Modificato." );
		        	return CaricaFormGestioneAutisti(mav, autistaModificato.getId().toString(), null, request);
	        	}else{
	        		errors.rejectValue("", "errors.violation.update.autista.commerciale");
    				mav.addAllObjects(errors.getModel());
    				return CaricaFormGestioneAutisti(mav, "", autistaDati, request);
	        	}
	        	
	        /**
	         * AGGIUNGI AUTISTA
	         */
			}else if(request.getParameter("aggiungi") != null){
				User user = new User();
				user.setFirstName( autistaDati.getUser().getFirstName() );
				user.setLastName( autistaDati.getUser().getLastName() );

				String usernameGenerata = UtilBukowski.GeneraUsername(1);
				
				while( getUserManager().userUsernameExist(usernameGenerata) == true ){
	    			usernameGenerata = UtilBukowski.GeneraUsername(1);
	    		}
	    		user.setUsername( usernameGenerata );
				if( getUserManager().userTelephoneExist( autistaDati.getUser().getPhoneNumber() )) {
					errors.rejectValue("user.phoneNumber", "errors.existing.autista.phoneNumber.user", new Object[] { autistaDati.getUser().getPhoneNumber() }, "");
    				mav.addAllObjects(errors.getModel());
    				return CaricaFormGestioneAutisti(mav, "", autistaDati, request);
				}else{
					user.setPhoneNumber( request.getParameter("number-tel-autista") );
				}

	    		if(autistaDati.getUser().getEmail() != null && !autistaDati.getUser().getEmail().equals("")){
	    			if( getUserManager().userEmailExist( autistaDati.getUser().getEmail() )) {
	    				errors.rejectValue("user.email", "errors.existing.autista.email.user", new Object[] { autistaDati.getUser().getEmail() }, "");
	    				mav.addAllObjects(errors.getModel());
	    				return CaricaFormGestioneAutisti(mav, "", autistaDati, request);
	    			}else{
	    				user.setEmail( autistaDati.getUser().getEmail() );
	    			}
	    		}else{
	    			user.setEmail( usernameGenerata+Constants.FAKE_EMAIL );
	    		}
				user.setPhoneNumber( autistaDati.getUser().getPhoneNumber() );
    			user.setPassword( autistaDati.getUser().getPassword() );

	    		user.addRole(roleManager.getRole(Constants.USER_ROLE));
	    		user.addTipoRuoli( tipoRuoliManager.getTipoRuoliByName(Constants.AUTISTA) );
	    		user.setEnabled(true);
	    		user.setSignupDate(new Date());
	    		
	    		user = getUserManager().saveUser( user );
	        	autistaDati.setUser( user );
	        	
	        	User commerciale = getUserManager().getUserByUsername(request.getRemoteUser());
	        	autistaDati.setCommerciale( commerciale );
	        	
	        	Autista autistaSalvato = autistaManager.saveAutista(autistaDati);
	        	saveMessage(request, "Autista "+autistaSalvato.getUser().getFirstName()+" "+autistaSalvato.getUser().getLastName()+" Salvato." );
	        	try {
	        		InviaEmail.sendUserEmailAccountCreated(user, request.getParameter("password"), velocityEngine, request);
		        } catch (final MailException me) {
		            saveError(request, getText("errors.sending.email.confirm", new Object[] { user.getEmail() },  request.getLocale() ) );
		        }
	        	return CaricaFormGestioneAutisti(mav, autistaSalvato.getId().toString(), null, request);
	        }
        
	        return CaricaFormGestioneAutisti(mav, "", null, request);
	        
		} catch (Exception exc) {
			exc.printStackTrace();
			saveError(request,  "errore generale: "+exc.getMessage() /*getText("errors.save", locale)*/ );
			return new ModelAndView("admin/gestione-autista");
		}
    }

    
    private ModelAndView CaricaFormGestioneAutisti(ModelAndView mav, String idAutista, Autista autistaError, HttpServletRequest request) throws Exception {
    	if (idAutista != null && !idAutista.trim().equals("") ) {
    		// autista esistente
			Autista autista = autistaManager.get( Long.parseLong(idAutista) );
			if(autista != null){
				DocumentiInfoUtil docUtil = new DocumentiInfoUtil(autista);
				autista.setDocumentiCompletatiFrazione(docUtil.documentiCompletatiFrazione);
				autista.setDocumentiApprovatiFrazione( docUtil.documentiApprovatiFrazione );
				mav.addObject("autista", autista );
				mav.addObject("modifica", true );
				// INFO AUTISTA
				mav.addAllObjects( MenuAutistaAttribute.CaricaMenuAutista( autista, 0, null ) );
				
			}else{
				// nuovo autista
				Autista newAutista = new Autista(new User(), true);
				mav.addObject("autista", newAutista );
				mav.addObject("modifica", false );
			}
    	}else{
	    	if(autistaError != null){
				// autista con errori
	    		if(autistaError.getId() != null){
	    			mav.addObject("autista", autistaError);
					mav.addObject("modifica", true );
					// INFO AUTISTA
					mav.addAllObjects( MenuAutistaAttribute.CaricaMenuAutista( autistaManager.get(autistaError.getId()), 0, null ));
	    		}else{
	    			mav.addObject("autista", autistaError);
					mav.addObject("modifica", false );
	    		}
			}else{
				// nuovo autista
				Autista newAutista = new Autista(new User(), true);
				mav.addObject("autista", newAutista );
				mav.addObject("modifica", false );
			}
    	}
    	final int PageSizeTable = Constants.PAGE_SIZE_TABLE_10;
    	mav.addObject(Constants.PAGE_SIZE_TABLE, PageSizeTable);
    	int page = ControllerUtil.DammiPageNumberDisplayTable_by_QueryString(request, PageSizeTable); 
        if (request.getParameter("ordinamento") != null && request.getParameter("ordinamento").equals("documenti")) {
    		mav.addObject("autistiList", autistaManager.getAutistaTable_2_limit(PageSizeTable, page, true));
    		mav.addObject("resultSize", autistaManager.getCountAutista() );
        }else if (request.getParameter("q") == null || "".equals(request.getParameter("q"))){
			mav.addObject("autistiList", autistaManager.getAutistaTable_2_limit(PageSizeTable, page, false));
			mav.addObject("resultSize", autistaManager.getCountAutista() );
    	}else{
    		mav.addObject("autistiList", autistaManager.getAutistiBy_LIKE( request.getParameter("q")));
        	mav.addObject("resultSize", autistaManager.getCountAutista() );
    	}
    	return mav;
    }
    

    @RequestMapping(method = RequestMethod.GET)
    protected ModelAndView showForm(final HttpServletRequest request, final HttpServletResponse response, 
    		@RequestParam(required = false, value = "idAutista") String idAutista) {
    	log.info("sono in GestioneAutistaController.showHome GET");
    	ModelAndView mav = new ModelAndView("admin/gestione-autista");
    	try {
	    	if(!request.isUserInRole(Constants.GEST_AUTISTA_ROLE) && !request.isUserInRole(Constants.ADMIN_ROLE)){
				response.sendError(HttpServletResponse.SC_FORBIDDEN);
	            throw new AccessDeniedException("Non hai il permesso per modificare gli autisti.");
	        }
	    	return CaricaFormGestioneAutisti(mav, idAutista, null, request);
    	} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
    }


}
