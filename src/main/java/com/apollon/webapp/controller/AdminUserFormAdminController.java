package com.apollon.webapp.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.app.VelocityEngine;

import com.apollon.Constants;
import com.apollon.model.Autista;
import com.apollon.model.Role;
import com.apollon.model.TipoRuoli;
import com.apollon.model.User;
import com.apollon.service.AutistaManager;
import com.apollon.service.RoleManager;
import com.apollon.service.TipoRuoliManager;
import com.apollon.service.UserManager;
import com.apollon.util.UtilString;
import com.apollon.util.customexception.UserExistsException;
import com.apollon.webapp.util.ControllerUtil;
import com.apollon.webapp.util.email.InviaEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mail.MailException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
@RequestMapping("/admin/userform-admin*")
public class AdminUserFormAdminController extends BaseFormController {

	
	private TipoRuoliManager tipoRuoliManager;
    @Autowired
    public void setTipoRuoliManager(final TipoRuoliManager tipoRuoliManager) {
        this.tipoRuoliManager = tipoRuoliManager;
    }
	
	private RoleManager roleManager;
    @Autowired
    public void setRoleManager(final RoleManager roleManager) {
        this.roleManager = roleManager;
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

    @Override
    @InitBinder
    protected void initBinder(final HttpServletRequest request, final ServletRequestDataBinder binder) {
        super.initBinder(request, binder);
        binder.setDisallowedFields("password", "confirmPassword");
    }

    /**
     * Load user object from db before web data binding in order to keep properties not populated from web post.
     * 
     * @param request
     * @return
     */
    @ModelAttribute("user")
    protected User loadUser(final HttpServletRequest request) {
        final String userId = request.getParameter("id");
        if (isFormSubmission(request) && StringUtils.isNotBlank(userId)) {
            return getUserManager().getUser(userId);
        }
        return new User();
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView onSubmit(@ModelAttribute("user") final User user, final BindingResult errors, final HttpServletRequest request, 
    		final HttpServletResponse response) throws Exception {
    	final Locale locale = request.getLocale();
        if (request.getParameter("cancel") != null) {
        	return new ModelAndView("redirect:/admin/users"); 
        }
        if(!request.isUserInRole(Constants.ADMIN_ROLE)){
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            throw new AccessDeniedException("Non hai il permesso per modificare gli autisti.");
        }
        if( !request.isUserInRole(Constants.ADMIN_ROLE) && user.getRoles().contains(roleManager.getRole(Constants.ADMIN_ROLE)) ){
    		errors.rejectValue("", "errors.update.admin.violation");
    		return new ModelAndView("admin/userform-admin").addAllObjects(errors.getModel());
    	}
        if (validator != null) { // validator is null during testing
            validator.validate(user, errors);
            if (errors.hasErrors() && request.getParameter("delete") == null) { // don't validate when deleting
                return new ModelAndView("admin/userform-admin");
            }
        }
        List<TipoRuoli> tipoRuoliList = new ArrayList<TipoRuoli>();
		tipoRuoliList.addAll( user.getTipoRuoli() );
        Autista autista = autistaManager.getAutistaByUser( user.getId() );
        if (request.getParameter("delete") != null) {
        	try{
        		// se è un autista gli disattivo il profilo autista
        		if( tipoRuoliList.contains(tipoRuoliManager.getTipoRuoliByName(Constants.AUTISTA)) && autista != null){
        			autista.setAttivo(false);
        			autista.setNote("[Emilinato dal Sistema] "+autista.getNote());
        			//TODO decidere se eliminare gli eventuali documenti che ha carcato (magari no)
        			autistaManager.saveAutista(autista);
        		}
	            //getUserManager().removeUser(user.getId().toString()); // Non devo eliminare lo USER, ma lo metto solo in Disabilitazione
        		user.setEnabled(false);
        		getUserManager().saveUser(user);
	            saveMessage(request, getText("user.deleted", user.getFullName(), locale));
	            return new ModelAndView("redirect:users");
        	}catch(DataIntegrityViolationException exc){
            	exc.printStackTrace();
            	errors.rejectValue("", "errors.constraint.violation.user");
            	//addAttribute("searchError", getText("errors.constraint.violation.user", locale));
            	Model model = new ExtendedModelMap();
            	model.addAttribute( getUserManager().getUsers());
            	model.addAttribute("searchError", getText("errors.constraint.violation.user", locale));
            	return new ModelAndView("admin/user-list", model.asMap());
            }
        } else {
            // only attempt to change roles if user is admin for other users,
            // showForm() method will handle populating
            if (request.isUserInRole(Constants.ADMIN_ROLE)) {
            	//rimuovo gli spazi
            	user.setEmail( UtilString.RimuoviTuttiGliSpazi(user.getEmail()) );
                final String[] userRoles = request.getParameterValues("userRoles");
                if(user.getCodiceVenditore() == null || user.getCodiceVenditore().equals("")) {
                	user.setCodiceVenditore( user.getEmail().substring(0, 1).toUpperCase() + user.getId().toString() );
                }
                if (userRoles != null) {
                    user.getRoles().clear();
                    for (final String roleName : userRoles) {
                        user.addRole(roleManager.getRole(roleName));
                    }
                }
                final String[] userTipoRuoliCheck = request.getParameterValues("userTipoRuoliCheck");
                //List<TipoRuoli> TipoRuoliList = tipoRuoliManager.getAll();
                try{
	        		if(userTipoRuoliCheck != null && Arrays.asList(userTipoRuoliCheck).contains( Constants.AUTISTA) 
	        				&& !user.getTipoRuoli().contains( tipoRuoliManager.getTipoRuoliByName(Constants.AUTISTA) )){
	        			autista = new Autista(user, true);
	            		autistaManager.saveAutista(autista);
	            		user.addTipoRuoli( tipoRuoliManager.getTipoRuoliByName(Constants.AUTISTA) );
	        		}
	        		if(userTipoRuoliCheck != null && Arrays.asList(userTipoRuoliCheck).contains(Constants.CLIENTE) &&
	        				!user.getTipoRuoli().contains( tipoRuoliManager.getTipoRuoliByName(Constants.CLIENTE) )){
	            		user.addTipoRuoli( tipoRuoliManager.getTipoRuoliByName(Constants.CLIENTE) );
	        		}
	        		if(user.getTipoRuoli().contains(tipoRuoliManager.getTipoRuoliByName(Constants.AUTISTA)) &&
	        				(userTipoRuoliCheck == null || !Arrays.asList(userTipoRuoliCheck).contains(Constants.AUTISTA)) ){
	        			// elimina autista
	        			autista = autistaManager.getAutistaByUser( user.getId() );
	        			autistaManager.removeAutista(autista.getId());
	        			// elimina tipo ruolo autista
	        			user.getTipoRuoli().remove( tipoRuoliManager.getTipoRuoliByName(Constants.AUTISTA) );
	        		}
	        		if(user.getTipoRuoli().contains(tipoRuoliManager.getTipoRuoliByName(Constants.CLIENTE)) &&
	        				(userTipoRuoliCheck == null || !Arrays.asList(userTipoRuoliCheck).contains(Constants.CLIENTE)) ){
	        			// elimina tipo ruolo cliente
	        			user.getTipoRuoli().remove( tipoRuoliManager.getTipoRuoliByName(Constants.CLIENTE) );
	        		}
                }catch(DataIntegrityViolationException exc){
                	exc.printStackTrace();
                	errors.rejectValue("", "errors.constraint.violation.autista");
                	return new ModelAndView("admin/userform-admin").addAllObjects(errors.getModel());
                }catch(Exception ex){
                	ex.printStackTrace();
                	return new ModelAndView("admin/userform-admin");
                }
            } else {
                // if user is not an admin then load roles from the database
                // (or any other user properties that should not be editable
                // by users without admin role)
                final User cleanUser = getUserManager().getUserByUsername(
                        request.getRemoteUser());
                user.setRoles(cleanUser.getRoles());
                user.setTipoRuoli(cleanUser.getTipoRuoli());
            }

            final Integer originalVersion = user.getVersion();

            // set a random password if user is added by admin
            if (originalVersion == null && StringUtils.isBlank(user.getPassword())) {
                user.setPassword(UUID.randomUUID().toString()); // XXX review if
                // UUID is a
                // good choice
                // here
            }
            try {
            	user.getBillingInformation().setCountry( request.getParameter("country-select") );
                getUserManager().saveUser(user);
                
            } catch (final AccessDeniedException ade) {
                // thrown by UserSecurityAdvice configured in aop:advisor userManagerSecurity
                log.warn(ade.getMessage());
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return null;
            } catch (final UserExistsException e) {
                errors.rejectValue("", "errors.existing.user.admin",
                        new Object[] { user.getUsername(), user.getEmail(), user.getPhoneNumber() }, "duplicate user");
                // reset the version # to what was passed in
                user.setVersion(originalVersion);
                return new ModelAndView("admin/userform-admin").addAllObjects(errors.getModel());
            }catch(DataIntegrityViolationException exc){
            	exc.printStackTrace();
            	errors.rejectValue("", "errors.constraint.violation.user");
            	return new ModelAndView("admin/userform-admin").addAllObjects(errors.getModel());
            }
            if (!StringUtils.equals(request.getParameter("from"), "list")) {
                saveMessage(request, getText("user.saved", user.getFullName(), locale));
                // return to main Menu
                return new ModelAndView("redirect:home-user");
            } else {
                if (StringUtils.isBlank(request.getParameter("version"))) {
                    saveMessage(request, getText("user.added", user.getFullName(), locale));
                    try {
                        final String resetPasswordUrl = getUserManager().buildRecoveryPasswordUrl(user, RecoverPasswordController.RECOVERY_PASSWORD_TEMPLATE);
                        InviaEmail.sendUserEmailAccountCreatedFromAdmin(user, ControllerUtil.getAppURL(request) + resetPasswordUrl, velocityEngine, request);
                    } catch (final MailException me) {
                        saveError(request, me.getCause().getLocalizedMessage());
                    }
                    return new ModelAndView("redirect:users");
                    
                } else {
                    saveMessage(request, getText("user.updated.byAdmin", user.getFullName(), locale));
                    return new ModelAndView("redirect:users");
                }
            }
        }

    }

    @ModelAttribute
    @RequestMapping(method = RequestMethod.GET)
    protected User showForm(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        // If not an administrator, make sure user is not trying to add or edit another user
        if ( !request.isUserInRole(Constants.ADMIN_ROLE) && !isFormSubmission(request) ) {
            if (isAdd(request) || request.getParameter("id") != null) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                log.warn("User '" + request.getRemoteUser() + "' is trying to edit user with id '" +
                        request.getParameter("id") + "'");
                throw new AccessDeniedException("You do not have permission to modify other users.");
            }
        }
        if (!isFormSubmission(request)) {
            final String userId = request.getParameter("id");
            User user;
            if (userId == null && !isAdd(request)) {
                user = getUserManager().getUserByUsername(request.getRemoteUser());
            } else if (!StringUtils.isBlank(userId) && !"".equals(request.getParameter("version"))) {
                user = getUserManager().getUser(userId);
            } else {
                user = new User();
                user.addRole(new Role(Constants.USER_ROLE));
            }
            return user;
        } else {
            // populate user object from database, so all fields don't need to be hidden fields in form
            return getUserManager().getUser(request.getParameter("id"));
        }
    }

    private boolean isFormSubmission(final HttpServletRequest request) {
        return request.getMethod().equalsIgnoreCase("post");
    }

    protected boolean isAdd(final HttpServletRequest request) {
        final String method = request.getParameter("method");
        return (method != null && method.equalsIgnoreCase("add"));
    }
    
}
