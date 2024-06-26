package com.apollon.dao.hibernate;

import com.apollon.Constants;
import com.apollon.dao.UserDao;
import com.apollon.model.BackupInfoUtente;
import com.apollon.model.User;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StringType;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

/**
 * This class interacts with Hibernate session to save/delete and
 * retrieve User objects.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 *         Modified by <a href="mailto:dan@getrolling.com">Dan Kibler</a>
 *         Extended to implement Acegi UserDetailsService interface by David Carter david@carter.net
 *         Modified by <a href="mailto:bwnoll@gmail.com">Bryan Noll</a> to work with
 *         the new BaseDaoHibernate implementation that uses generics.
 *         Modified by jgarcia (updated to hibernate 4)
 */
@EnableTransactionManagement
@Repository("userDao")
public class UserDaoHibernate extends GenericDaoHibernate<User, Long> implements UserDao, UserDetailsService {

    /**
     * Constructor that sets the entity to User.class.
     */
    public UserDaoHibernate() {
        super(User.class);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<User> getUsers() {
        Query qry = getSession().createQuery("from User u order by upper(u.username)");
        return qry.list();
    }
    
    
    /**
     * mi serve per visualizzare la lista di user nella tabella in /admin/users*
     */
	public int getCountUser() {
		return (int)(long)getSession().createCriteria(User.class)
			.setProjection(Projections.rowCount()).uniqueResult();
	}
	
	/**
     * mi serve per visualizzare la lista di user nella tabella in /admin/users*
     */
	@SuppressWarnings("unchecked")
	public List<User> getUserTableLimit(int maxResults, Integer firstResult) {
		String queryString = "FROM User c ORDER BY c.id DESC";
		Query query = getSession().createQuery(queryString).setMaxResults( maxResults ).setFirstResult( firstResult );
		List<User> aa = query. list();
		return aa;
	}
	
    /**
     * {@inheritDoc}
     */
    @Transactional
    public User saveUser(User user) {
        if (log.isDebugEnabled()) {
            log.debug("user's id: " + user.getId());
        }
        getSession().saveOrUpdate(user);
        // necessary to throw a DataIntegrityViolation and catch it in UserManager
        getSession().flush();
        
        //salvo backUp
        BackupInfoUtente backup = new BackupInfoUtente(user, new Date(), user.getUsername(), user.getFirstName(), 
        		user.getLastName(), user.getEmail(), user.getPhoneNumber());
        getSession().save(backup);
        
        return user;
    }

    /**
     * Overridden simply to call the saveUser method. This is happening
     * because saveUser flushes the session and saveObject of BaseDaoHibernate
     * does not.
     *
     * @param user the user to save
     * @return the modified user (with a primary key set if they're new)
     */
    @Override
    public User save(User user) {
        return this.saveUser(user);
    }

    /**
     * implementazione metodo di UserDetailsService, questo serve per la autenticazione del login.
     * <p>
     * Accetta la username, la email e il telefono cellulare che deve essere più di 5 numeri perché 
     * i telefono cellulari del mondo hanno minimo 6 numeri (a parte rare eccezzioni)
     * 
     */
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	if (log.isDebugEnabled()) {
            log.debug("username: "+username);
        }
    	String telefono = "";
    	if(username != null){
    		// elimino tutti caratteri non numerici
    		telefono = username.replaceAll("[^\\d]", "");
    	}
    	Criterion crit;
    	if(telefono.length() > 5){
    		crit = Restrictions.or(
        			Restrictions.eq("username", username),
        			Restrictions.eq("email", username),
        			Restrictions.like("phoneNumber", "%"+telefono+"%", MatchMode.END));
    	}else{
    		crit = Restrictions.or(
        			Restrictions.eq("username", username),
        			Restrictions.eq("email", username));
    	}
        List<?> users = getSession().createCriteria(User.class).add( crit ).list();
        if (users == null || users.isEmpty()) {
            throw new UsernameNotFoundException("user '" + username + "' not found...");
        } else {
            return (UserDetails) users.get(0);
        }
    }
    
    public boolean userUsernameExist(String username) {
        List<?> users = getSession().createCriteria(User.class).add(Restrictions.eq("username", username)).list();
        if (users == null || users.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }
    
    public UserDetails loadUserByTelephone(String phoneNumber) throws UsernameNotFoundException {
        List<?> users = getSession().createCriteria(User.class).add(Restrictions.eq("phoneNumber", phoneNumber)).list();
        if (users == null || users.isEmpty()) {
            throw new UsernameNotFoundException("phoneNumber '" + phoneNumber + "' not found...");
        } else {
            return (UserDetails) users.get(0);
        }
    }
    
    public boolean userTelephoneExist(String phoneNumber) {
        List<?> users = getSession().createCriteria(User.class).add(Restrictions.eq("phoneNumber", phoneNumber)).list();
        if (users == null || users.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }
    
    
    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        List<?> users = getSession().createCriteria(User.class).add(Restrictions.eq("email", email)).list();
        if (users == null || users.isEmpty()) {
            throw new UsernameNotFoundException("email '" + email + "' not found...");
        } else {
            return (UserDetails) users.get(0);
        }
    }
    
    
    public boolean userEmailExist(String email) {
        List<?> users = getSession().createCriteria(User.class).add(Restrictions.eq("email", email)). list();
        if (users == null || users.isEmpty()) {
        	return false;
        } else {
            return true;
        }
    }
    
    
    /**
     * {@inheritDoc}
     */
    public String getUserPassword(Long userId) {
        JdbcTemplate jdbcTemplate =
                new JdbcTemplate(SessionFactoryUtils.getDataSource(getSessionFactory()));
        Table table = AnnotationUtils.findAnnotation(User.class, Table.class);
        return jdbcTemplate.queryForObject(
                "select password from " + table.name() + " where id=?", String.class, userId);
    }
    
    
    /**
     * Con l'aggunga di COLLATE si rende CaseSensitive il confronto su mysql
     */
    public Long getUserIdVenditore_by_CodiceVenditore(String codiceVenditore) {
    	Criterion crit  = Restrictions.sqlRestriction("codiceVenditore = ? collate utf8mb4_bin ", codiceVenditore, new StringType());
        User user = (User)getSession().createCriteria(User.class).add( crit ).uniqueResult();
        if(user != null && !user.getCodiceVenditore().equals("")) {
        	return user.getId();
        } else {
            return null;
        }
    }
    

    /**
     * @param token
     * @return
     */
	public User getUser_by_TokenRecensione(String token) {
		
		String queryString = "SELECT * FROM app_user WHERE json_extract(website, '$."+Constants.UrlTockenPageScriviRecensone+"') = :token ";
		User user = (User) this.getSession().createSQLQuery( queryString ).addEntity(User.class).setParameter("token", token).uniqueResult();
		return user;
		
		/*
		Criterion criterion = Restrictions.and(
				Restrictions.like("website", token, MatchMode.ANYWHERE),
				Restrictions.like("website", Constants.UrlTockenPageScriviRecensone, MatchMode.ANYWHERE) );
		return (User) getSession().createCriteria(User.class).add( criterion ).uniqueResult();
		 */
	}
	
	/**
     * @param token
     * @return
     */
	public User getUser_by_TokenRecensioneCodiceSconto(String token) {
		
		String queryString = "SELECT * FROM app_user WHERE json_extract(website, '$."+Constants.CodiceScontoJSON+"') = :token ";
		User user = (User) this.getSession().createSQLQuery( queryString ).addEntity(User.class).setParameter("token", token).uniqueResult();
		return user;
		
		/*
		Criterion criterion = Restrictions.and(
				Restrictions.like("website", token, MatchMode.ANYWHERE),
				Restrictions.like("website", Constants.CodiceScontoJSON, MatchMode.ANYWHERE) );
		return (User) getSession().createCriteria(User.class).add( criterion ).uniqueResult();
		*/
	}
	
	
	@Override
    @Transactional(readOnly = true)
	public User get(Long id){
		User user = (User) getSession().get(User.class, id);
		return user;
	}


    
}
