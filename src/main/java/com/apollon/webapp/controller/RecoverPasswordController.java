package com.apollon.webapp.controller;

import java.util.Locale;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.apollon.model.User;
import com.apollon.webapp.util.ControllerUtil;

import org.springframework.context.NoSuchMessageException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Update Password Controller.
 * 
 * @author ivangsa
 */
@Controller
public class RecoverPasswordController extends BaseFormController {

    public static final String RECOVERY_PASSWORD_TEMPLATE = "/updatePassword?username={username}&token={token}";

	@RequestMapping(value = "/recoverpass*", method = RequestMethod.GET)
	protected ModelAndView recoverpass(final HttpServletRequest request, final HttpServletResponse response) {
		log.info("sono in UpdatePasswordController.recoverpass GET");
		final Locale locale = request.getLocale();
		ModelAndView mav = new ModelAndView("recover-password");
		try{
			return mav;
		}catch(Exception exc){
			exc.printStackTrace();
			saveError(request, getText("errors.cancel", locale));
			return new ModelAndView("home-user");
		}
	}
	
	@RequestMapping(value = "/recoverpass*", method = RequestMethod.POST)
	protected ModelAndView recoverpassPost(final HttpServletRequest request, 
			@RequestParam(value = "emailPhoneNumber", required = true) final String emailPhoneNumber) {
		log.info("sono in UpdatePasswordController.recoverpassPost POST");
		final Locale locale = request.getLocale();
		ModelAndView mav = new ModelAndView("recover-password");
		try {
            boolean res = getUserManager().sendPasswordRecoveryEmail(emailPhoneNumber, ControllerUtil.getAppURL(request) + RECOVERY_PASSWORD_TEMPLATE, request);
            if( res ){
            	saveMessage(request, getText("updatePassword.recoveryToken.sent", request.getLocale()));
            }else{
            	saveMessage(request, getText("updatePassword.recoveryToken.sms.sent", request.getLocale()));
            }
            return mav;
            
        }catch (final UsernameNotFoundException ignored) {
        	log.debug("UsernameNotFoundException");
            saveError(request, getText("login.recoveryToken.error", request.getLocale()));
            return mav;
        }catch(Exception exc){
			exc.printStackTrace();
			saveError(request, getText("errors.sending.email", locale));
			return mav;
		}
	}
    
    
    /**
     *
     * @param username
     * @param request
     * @return
     */
    @RequestMapping(value = "/requestRecoveryToken*", method = RequestMethod.GET)
    public String requestRecoveryToken(
            @RequestParam(value = "username", required = true) final String username,
            final HttpServletRequest request) {
        log.debug("Sending recovery token to user " + username);
        try {
            getUserManager().sendPasswordRecoveryEmail(username, ControllerUtil.getAppURL(request) + RECOVERY_PASSWORD_TEMPLATE, request);
        } catch (final UsernameNotFoundException ignored) {
            // lets ignore this
        } catch (MessagingException e) {
			e.printStackTrace();
		} catch (NoSuchMessageException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
        saveMessage(request, getText("updatePassword.recoveryToken.sent", request.getLocale()));
        return "redirect:/";
    }

    /**
     *
     * @param username
     * @param token
     * @return
     */
    @RequestMapping(value = "/updatePassword*", method = RequestMethod.GET)
    public ModelAndView showForm(
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "token", required = false) final String token,
            final HttpServletRequest request){
        if (StringUtils.isBlank(username)) {
            username = request.getRemoteUser();
        }
        if (StringUtils.isNotBlank(token) && !getUserManager().isRecoveryTokenValid(username, token)) {
            saveError(request, getText("updatePassword.invalidToken", request.getLocale()));
            return new ModelAndView("redirect:/");
        }

        return new ModelAndView("updatePasswordForm").addObject("username", username).addObject("token", token);
    }

    /**
     *
     * @param username
     * @param token
     * @param password
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/updatePassword*", method = RequestMethod.POST)
    public ModelAndView onSubmit(
            @RequestParam(value = "username", required = true) final String username,
            @RequestParam(value = "token", required = false) final String token,
            @RequestParam(value = "currentPassword", required = false) final String currentPassword,
            @RequestParam(value = "password", required = true) final String password,
            final HttpServletRequest request) throws Exception{
        log.debug("PasswordRecoveryController onSubmit for username: " + username);

        final Locale locale = request.getLocale();
        
        if (request.getParameter("cancel") != null) {
        	return new ModelAndView("redirect:/userform");
        }
        
        if (StringUtils.isEmpty(password)) {
            saveError(request, getText("errors.required", getText("updatePassword.newPassword.label", locale), locale));
            return showForm(username, null, request);
        }

        User user = null;
        final boolean usingToken = StringUtils.isNotBlank(token);
        if (usingToken) {
            log.debug("Updating Password for username " + username + ", using reset token");
            user = getUserManager().updatePassword(username, null, token, password,
                    ControllerUtil.getAppURL(request) + "/home-user", request);

        } else {
            log.debug("Updating Password for username " + username + ", using current password");
            if (!username.equals(request.getRemoteUser())) {
                throw new AccessDeniedException("You do not have permission to modify other users password.");
            }
            user = getUserManager().updatePassword(username, currentPassword, null, password,
                    ControllerUtil.getAppURL(request) + "/home-user", request);
        }

        if (user != null) {
            saveMessage(request, getText("updatePassword.success", new Object[] { username }, locale));
        }
        else {
            if (usingToken) {
                saveError(request, getText("updatePassword.invalidToken", locale));
            }
            else {
                saveError(request, getText("updatePassword.invalidPassword", locale));
                return showForm(username, null, request);
            }
        }

        return new ModelAndView("redirect:/userform");
    }

}
