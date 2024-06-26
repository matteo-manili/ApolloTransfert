package com.apollon.dao;

import com.apollon.model.User;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * User Data Access Object (GenericDao) interface.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public interface UserDao extends GenericDao<User, Long> {

    /**
     * Gets users information based on login name.
     * @param username the user's username
     * @return userDetails populated userDetails object
     * @throws org.springframework.security.core.userdetails.UsernameNotFoundException thrown when user not
     * found in database
     */
	
	/**
     * mi serve per visualizzare la lista di user nella tabella in /admin/users*
     */
	int getCountUser();
	
	/**
     * mi serve per visualizzare la lista di user nella tabella in /admin/users*
     */
	List<User> getUserTableLimit(int maxResults, Integer firstResult);
	
	@Transactional
    boolean userUsernameExist(String username);

	
    @Transactional
    boolean userTelephoneExist(String phoneNumber);
    
	
    @Transactional
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
    
    
    @Transactional
    UserDetails loadUserByTelephone(String phoneNumber) throws UsernameNotFoundException;
    
    
    @Transactional
    UserDetails loadUserByEmail(String email) throws UsernameNotFoundException;
    
   
    @Transactional
    boolean userEmailExist(String email);
    
    /**
     * Gets a list of users ordered by the uppercase version of their username.
     *
     * @return List populated list of users
     */
    List<User> getUsers();

    /**
     * Saves a user's information.
     * @param user the object to be saved
     * @return the persisted User object
     */
    @Transactional
    User saveUser(User user);

    /**
     * Retrieves the password in DB for a user
     * @param userId the user's id
     * @return the password in DB, if the user is already persisted
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    String getUserPassword(Long userId);
    
    
    @Transactional
    Long getUserIdVenditore_by_CodiceVenditore(String codiceVenditore);

    
    @Transactional
	User getUser_by_TokenRecensione(String token);
    
    
    @Transactional
    User getUser_by_TokenRecensioneCodiceSconto(String token);
    
    
}
