package com.apollon.service.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.app.VelocityEngine;
import com.apollon.Constants;
import com.apollon.dao.UserDao;
import com.apollon.model.User;
import com.apollon.service.MailEngine;
import com.apollon.service.UserManager;
import com.apollon.service.UserService;
import com.apollon.util.customexception.UserExistsException;
import com.apollon.webapp.util.email.InviaEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.NoSuchMessageException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.jws.WebService;
import javax.servlet.http.HttpServletRequest;

import java.util.List;


/**
 * Implementation of UserManager interface.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
@Service("userManager")
@WebService(serviceName = "UserService", endpointInterface = "com.apollon.service.UserService")
public class UserManagerImpl extends GenericManagerImpl<User, Long> implements UserManager, UserService {
	
    private PasswordEncoder passwordEncoder;
    private UserDao userDao;

    private MailEngine mailEngine;
    @SuppressWarnings("unused")
	private SimpleMailMessage message;
    private PasswordTokenManager passwordTokenManager;

    @Autowired
    @Qualifier("passwordEncoder")
    public void setPasswordEncoder(final PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Autowired
    public void setUserDao(final UserDao userDao) {
        this.dao = userDao;
        this.userDao = userDao;
    }

    @Autowired(required = false)
    public void setMailEngine(final MailEngine mailEngine) {
        this.mailEngine = mailEngine;
    }

    @Autowired(required = false)
    public void setMailMessage(final SimpleMailMessage message) {
        this.message = message;
    }

    @Autowired(required = false)
    public void setPasswordTokenManager(final PasswordTokenManager passwordTokenManager) {
        this.passwordTokenManager = passwordTokenManager;
    }

    private VelocityEngine velocityEngine;
   	@Autowired(required = false)
   	public void setVelocityEngine(VelocityEngine velocityEngine) {
   		this.velocityEngine = velocityEngine;
   	}
    
    
    /**
     * Velocity template name to send users a password recovery mail (default
     * tmp_emailPasswordRecovery.vm).
     *
     * @param passwordRecoveryTemplate the Velocity template to use (relative to classpath)
     * @see com.apollon.service.MailEngine#sendMessage(org.springframework.mail.SimpleMailMessage, String, java.util.Map)
     */
    /*
    public void setPasswordRecoveryTemplate(final String passwordRecoveryTemplate) {
        this.passwordRecoveryTemplate = passwordRecoveryTemplate;
    }
    */

    /**
     * Velocity template name to inform users their password was updated
     * (default tmp_emailPasswordUpdated.vm).
     *
     * @param passwordUpdatedTemplate the Velocity template to use (relative to classpath)
     * @see com.apollon.service.MailEngine#sendMessage(org.springframework.mail.SimpleMailMessage, String, java.util.Map)
     */
    /*
    public void setPasswordUpdatedTemplate(final String passwordUpdatedTemplate) {
        this.passwordUpdatedTemplate = passwordUpdatedTemplate;
    }
    */

    /**
     * {@inheritDoc}
     */
    @Override
    public User getUser(final String userId) {
        return userDao.get(new Long(userId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> getUsers() {
        return userDao.getAllDistinct();
    }
    
    @Override
    public List<User> getUserTable() {
        return userDao.getUsers();
    }
    
    @Override
    public boolean userUsernameExist(final String username)  {
    	return userDao.userUsernameExist(username);
    }
    
    @Override
    public boolean userTelephoneExist(final String phoneNumber)  {
    	return userDao.userTelephoneExist(phoneNumber);
    }
    
    @Override
    public boolean userEmailExist(final String email)  {
    	return userDao.userEmailExist(email);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User saveUser(final User user) throws UserExistsException {

        if (user.getVersion() == null) {
            // if new user, lowercase userId
            user.setUsername(user.getUsername().toLowerCase());
        }

        // Get and prepare password management-related artifacts
        boolean passwordChanged = false;
        if (passwordEncoder != null) {
            // Check whether we have to encrypt (or re-encrypt) the password
            if (user.getVersion() == null) {
                // New user, always encrypt
                passwordChanged = true;
            } else {
                // Existing user, check password in DB
                final String currentPassword = userDao.getUserPassword(user.getId());
                if (currentPassword == null) {
                    passwordChanged = true;
                } else {
                    if (!currentPassword.equals(user.getPassword())) {
                        passwordChanged = true;
                    }
                }
            }

            // If password was changed (or new user), encrypt it
            if (passwordChanged) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
        } else {
            log.warn("PasswordEncoder not set, skipping password encryption...");
        }

        try {
            return userDao.saveUser(user);
        } catch (final Exception e) {
            e.printStackTrace();
            log.warn(e.getMessage());
            throw new UserExistsException("User '" + user.getUsername() + "' already exists!");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeUser(final User user) {
        log.debug("removing user: " + user);
        userDao.remove(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeUser(final String userId) {
        log.debug("removing user: " + userId);
        userDao.remove(new Long(userId));
    }

    /**
     * {@inheritDoc}
     *
     * @param username the login name of the human
     * @return User the populated user object
     * @throws org.springframework.security.core.userdetails.UsernameNotFoundException thrown when username not found
     */
    @Override
    public User getUserByUsername(final String username) throws UsernameNotFoundException {
        return (User) userDao.loadUserByUsername(username);
    }

    
    @Override
    public User loadUserByTelephone(final String phoneNumber) throws UsernameNotFoundException {
        return (User) userDao.loadUserByTelephone(phoneNumber);
    }
    
    /**
     * mi serve per visualizzare la lista di user nella tabella in /admin/users*
     */
    @Override
    public int getCountUser() {
        return userDao.getCountUser();
    }

	/**
	 * mi serve per visualizzare la lista di user nella tabella in /admin/users*
	 */
	@Override
	public List<User> getUserTableLimit(int maxResults, Integer firstResult) {
	    return userDao.getUserTableLimit(maxResults, firstResult);
	}


    
    @Override
    public User getUserByEmail(final String email) throws UsernameNotFoundException {
        return (User) userDao.loadUserByEmail(email);
    }

    
    @Override
    public Long getUserIdVenditore_by_CodiceVenditore(String codiceVenditore) {
        return userDao.getUserIdVenditore_by_CodiceVenditore(codiceVenditore);
    }
    
    
    @Override
    public User getUser_by_TokenRecensione(String token) {
        return userDao.getUser_by_TokenRecensione(token);
    }
    
    @Override
    public User getUser_by_TokenRecensioneCodiceSconto(String token) {
        return userDao.getUser_by_TokenRecensioneCodiceSconto(token);
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> search(final String searchTerm) {
        return super.search(searchTerm, User.class);
    }

    @Override
    public String buildRecoveryPasswordUrl(final User user, final String urlTemplate) {
        final String token = generateRecoveryToken(user);
        final String username = user.getUsername();
        return StringUtils.replaceEach(urlTemplate,
                new String[]{"{username}", "{token}"},
                new String[]{username, token});
    }

    @Override
    public String generateRecoveryToken(final User user) {
        return passwordTokenManager.generateRecoveryToken(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isRecoveryTokenValid(final String username, final String token) {
        return isRecoveryTokenValid(getUserByUsername(username), token);
    }

    @Override
    public boolean isRecoveryTokenValid(final User user, final String token) {
        return passwordTokenManager.isRecoveryTokenValid(user, token);
    }

    /**
     * {@inheritDoc}
     * @throws Exception 
     * @throws NoSuchMessageException 
     */
    @Override
    public boolean sendPasswordRecoveryEmail(final String username, final String urlTemplate, HttpServletRequest request) throws NoSuchMessageException, Exception {
        log.debug("Sending password recovery token to user: " + username);
        final User user = getUserByUsername(username);
        final String url = buildRecoveryPasswordUrl(user, urlTemplate);
        if( !user.getEmail().contains(Constants.FAKE_EMAIL) ){
    		InviaEmail.sendUserEmailPasswordRecovery(user, url, velocityEngine, request);
            return true;
        }else{
        	return false;
        	
        }
    }


    /**
     * {@inheritDoc}
     * @throws Exception 
     * @throws NoSuchMessageException 
     */
    @Override
    public User updatePassword(final String username, final String currentPassword, final String recoveryToken, 
    		final String newPassword, final String applicationUrl, HttpServletRequest request) throws NoSuchMessageException, Exception {
        User user = getUserByUsername(username);
        if (isRecoveryTokenValid(user, recoveryToken)) {
            log.debug("Updating password from recovery token for user: " + username);
            user.setPassword(newPassword);
            user = saveUser(user);
            passwordTokenManager.invalidateRecoveryToken(user, recoveryToken);
            InviaEmail.sendUserEmailPasswordUpdate(user, applicationUrl, velocityEngine, request);
            return user;
        } else if (StringUtils.isNotBlank(currentPassword)) {
            if (passwordEncoder.matches(currentPassword, user.getPassword())) {
                log.debug("Updating password (providing current password) for user:" + username);
                user.setPassword(newPassword);
                user = saveUser(user);
                return user;
            }
        }
        // or throw exception
        return null;
    }

    
    

    

	
}
