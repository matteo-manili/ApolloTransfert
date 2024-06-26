package com.apollon.webapp.rest;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.core.userdetails.UserDetails;

import com.apollon.Constants;
import com.apollon.dao.AutistaDao;
import com.apollon.model.Autista;
import com.apollon.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


/**
 * vedere: https://jwt.io/
 * vedere: https://italiancoders.it/autenticazione-di-servizi-rest-con-jwt-spring/
 * vedere: https://developer.okta.com/blog/2018/10/31/jwts-with-java
 * vedere: https://stormpath.com/blog/jwt-java-create-verify
 * 
 * @author Matteo
 *
 */
public class JwtTokenUtil implements Serializable {
	private static final long serialVersionUID = -3301605591108950415L;
	private static final Log log = LogFactory.getLog(JwtTokenUtil.class);
	public static ApplicationContext contextDao = new ClassPathXmlApplicationContext("App-Database-Spring-Module-Web.xml");
	public static AutistaDao autistaDao = (AutistaDao) contextDao.getBean("AutistaDao");
	private static final String CLAIM_KEY_CREATED = "iat";
	private static final String CLAIM_KEY_EXPIRED = "exp";
	private static final String CLAIM_KEY_SUBJECT = "sub";
	
	private static final String CLAIM_KEY_ID_AUTISTA = "idAutista";
	private static final String CLAIM_KEY_FULL_NAME_USER = "fullNameUser";
	private static final String CLAIM_KEY_DENOMINAZIONE_AZIENDA = "autistaDenominazioneAzienda";
	private static final String CLAIM_KEY_AUTISTA_ATTIVO = "autistaAttivo";
	private static final String CLAIM_KEY_AUTISTA_APPROVATO_GENERALE = "autistaApprovato";
	private static final String CLAIM_KEY_AUTISTA_BANNATO = "autistaBann";
	
	/*
    static final String CLAIM_KEY_USERNAME = "sub";
    static final String CLAIM_KEY_AUDIENCE = "audience";
    static final String CLAIM_KEY_AUTHORITIES = "roles";
    static final String CLAIM_KEY_IS_ENABLED = "isEnabled";

    private static final String AUDIENCE_UNKNOWN = "unknown";
    private static final String AUDIENCE_WEB = "web";
    private static final String AUDIENCE_MOBILE = "mobile";
    private static final String AUDIENCE_TABLET = "tablet";
    */

	public static class InfoJwt {
		Long jwtCreated;
		Long jwtExpired;
		String username;
		Long idAutista;
		String fullNameAutista;
		String denominazioneAziendaAutista;
		public Long getJwtCreated() {
			return jwtCreated;
		}
		public void setJwtCreated(Long jwtCreated) {
			this.jwtCreated = jwtCreated;
		}
		public Long getJwtExpired() {
			return jwtExpired;
		}
		public void setJwtExpired(Long jwtExpired) {
			this.jwtExpired = jwtExpired;
		}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public Long getIdAutista() {
			return idAutista;
		}
		public void setIdAutista(Long idAutista) {
			this.idAutista = idAutista;
		}
		public String getFullNameAutista() {
			return fullNameAutista;
		}
		public void setFullNameAutista(String fullNameAutista) {
			this.fullNameAutista = fullNameAutista;
		}
		public String getDenominazioneAziendaAutista() {
			return denominazioneAziendaAutista;
		}
		public void setDenominazioneAziendaAutista(String denominazioneAziendaAutista) {
			this.denominazioneAziendaAutista = denominazioneAziendaAutista;
		}
	}
	
    
	private static SecretKeySpec getSecret() {
    	//We will sign our JWT with our ApiKey secret
    	SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
	    byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(Constants.JWT_SECRET);
	    SecretKeySpec signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
	    return signingKey;
    }

	/**
	 * Mi ritorna null se il token non è valido
	 * @param token
	 * @return
	 */
    public static InfoJwt Check_e_InfoAutista_Jwt(String token) {
    	//log.debug("get_InfoAutistaJwt");
        Long IdAutista = null;
        try {
        	final Claims claims = getClaimsFromToken(token);
        	boolean Autista_Attivo = (boolean) claims.get(CLAIM_KEY_AUTISTA_ATTIVO);
            boolean Autista_Approvato_Generale = (boolean) claims.get(CLAIM_KEY_AUTISTA_APPROVATO_GENERALE);
            boolean Autista_Bannato = (boolean) claims.get(CLAIM_KEY_AUTISTA_BANNATO);
            IdAutista = Long.parseLong( claims.get(CLAIM_KEY_ID_AUTISTA).toString() ) ;
            Date JwtExpired = new Date( ((Number)claims.get(CLAIM_KEY_EXPIRED)).longValue() ) ;
            if( ( !isTokenExpired(JwtExpired) && Autista_Attivo && Autista_Approvato_Generale && !Autista_Bannato) || (IdAutista == 2) ) {
            	InfoJwt infoJwt = new InfoJwt();
            	//se faccio il cast direttamente a (Long) può dare: java.lang.Integer cannot be cast to java.lang.Long
            	infoJwt.setJwtCreated( ((Number)claims.get(CLAIM_KEY_CREATED)).longValue() );
            	infoJwt.setJwtExpired( ((Number)claims.get(CLAIM_KEY_EXPIRED)).longValue() );
            	infoJwt.setUsername( claims.get(CLAIM_KEY_SUBJECT).toString() );
            	infoJwt.setIdAutista(IdAutista);
            	infoJwt.setFullNameAutista( claims.get(CLAIM_KEY_FULL_NAME_USER).toString() );
            	infoJwt.setDenominazioneAziendaAutista( claims.get(CLAIM_KEY_DENOMINAZIONE_AZIENDA).toString() );
            	return infoJwt;
            }else {
            	return null;
            }
        } catch (Exception e) {
        	e.printStackTrace();
        	return null;
        }
    }
    
    private static Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(getSecret()).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    
    public static String GenerateToken(UserDetails userDetails) throws JsonProcessingException {
    	User user = (User) userDetails;
    	Autista autista = autistaDao.getAutistaByUser( user.getId() );
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_CREATED, new Date());
        claims.put(CLAIM_KEY_EXPIRED, generateExpirationDate());
        claims.put(CLAIM_KEY_SUBJECT, user.getUsername());
        claims.put(CLAIM_KEY_ID_AUTISTA, autista.getId());
        claims.put(CLAIM_KEY_FULL_NAME_USER, autista.getUser().getFullName());
        claims.put(CLAIM_KEY_DENOMINAZIONE_AZIENDA, autista.getAutistaDocumento().getPartitaIvaDenominazione());
        claims.put(CLAIM_KEY_AUTISTA_ATTIVO, autista.isAttivo());
        claims.put(CLAIM_KEY_AUTISTA_APPROVATO_GENERALE, autista.getAutistaDocumento().isApprovatoGenerale());
        claims.put(CLAIM_KEY_AUTISTA_BANNATO, autista.isBannato());
        /*
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        //claims.put(CLAIM_KEY_AUDIENCE, generateAudience(device));
        claims.put(CLAIM_KEY_CREATED, new Date());
        List<String> auth = userDetails.getAuthorities().stream().map(role-> role.getAuthority()).collect(Collectors.toList());
        claims.put(CLAIM_KEY_AUTHORITIES, auth);
        claims.put(CLAIM_KEY_IS_ENABLED, userDetails.isEnabled());
        */
        return generateToken(claims);
    }
    
    
    public static String generateToken(Map<String, Object> claims) {
    	try {
				return Jwts.builder()
						.setClaims(claims)
						.signWith(SignatureAlgorithm.HS256, getSecret())
						.compact();
    		/*
    	    JwtBuilder builder = Jwts.builder()
                    .setClaims(claims)
                    .setExpiration(generateExpirationDate()) // non usare, setta una data troncata
                    .signWith(SignatureAlgorithm.HS256, getSecret());
            return builder.compact();
            */
    	}catch (Exception exc) {
    		exc.printStackTrace();
    		return null;
    	}
    }
    
    private static Date generateExpirationDate() {
    	//vedere (per fare conversione giorni -> millisecondi) http://www.unitconversion.org/time/day-to-milliseconds-conversion.html
    	//1296000000 = 15 giorni
    	//2592000000 = 30 giorni
    	return new Date(new Date().getTime() + 1296000000l);
    }
    
    
    private static Boolean isTokenExpired(Date JwtExpired) {
        return JwtExpired.before(new Date());
    }
    
    
    public String getUsernameFromToken(String token) {
        String username;
        try {
            final Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }
    
    
    /*
    public String refreshToken(String token) {
        String refreshedToken;
        try {
            final Claims claims = getClaimsFromToken(token);
            claims.put(CLAIM_KEY_CREATED, new Date());
            claims.put(CLAIM_KEY_EXPIRED, generateExpirationDate());
            refreshedToken = generateToken(claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }
    */
    
    /*
    public Boolean validateToken(String token, UserDetails userDetails) {
        User user = (User) userDetails;
        final String username = getUsernameFromToken(token);
        return (username.equals(user.getUsername()) && !isTokenExpired(token));
    }
    */
    
    /*
    public Boolean canTokenBeRefreshed(String token) {
        final Date created = getCreatedDateFromToken(token);
        return  (!isTokenExpired(token) || ignoreTokenExpiration(token));
    }
    */
    
    /*
    private Boolean ignoreTokenExpiration(String token) {
        String audience = getAudienceFromToken(token);
        return (AUDIENCE_TABLET.equals(audience) || AUDIENCE_MOBILE.equals(audience));
    }
    */

    /*
    public static User getUserDetails(String token) {
        if(token == null){
            return null;
        }
        try {
            final Claims claims = getClaimsFromToken(token);
            List<SimpleGrantedAuthority> authorities = null;
            if (claims.get(CLAIM_KEY_AUTHORITIES) != null) {
                authorities = ((List<String>) claims.get(CLAIM_KEY_AUTHORITIES)).stream().map(role-> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
            }

            return new User( claims.getSubject(), "",authorities, (boolean) claims.get(CLAIM_KEY_IS_ENABLED) );
        } catch (Exception e) {
            return null;
        }

    }
	*/
	
    /*
    public String getAudienceFromToken(String token) {
        String audience;
        try {
            final Claims claims = getClaimsFromToken(token);
            audience = (String) claims.get(CLAIM_KEY_AUDIENCE);
        } catch (Exception e) {
            audience = null;
        }
        return audience;
    }
    */
    
    /*
    private String generateAudience(Device device) {
        String audience = AUDIENCE_UNKNOWN;
        if (device.isNormal()) {
            audience = AUDIENCE_WEB;
        } else if (device.isTablet()) {
            audience = AUDIENCE_TABLET;
        } else if (device.isMobile()) {
            audience = AUDIENCE_MOBILE;
        }
        return audience;
    }
    */
}
