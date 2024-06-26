package com.apollon.util;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import javax.servlet.ServletContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.routines.IntegerValidator;
import org.springframework.web.multipart.MultipartFile;
import com.apollon.Constants;
import com.apollon.dao.GestioneApplicazioneDao;
import com.apollon.webapp.util.ApplicationUtils;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public final class UtilBukowski {
	
	
	public static File convertMultipartFile_to_File(ServletContext context, MultipartFile multipartFile) throws IOException, IllegalStateException{  
		File convFile = new File( context.getRealPath(Constants.STORAGE_DIRECTORY_UPLOADS) + multipartFile.getOriginalFilename());
        multipartFile.transferTo(convFile);
	    return convFile; //convFile;
	}
	
	/**
	 * 
	 * passare come paramentro: 
	 * <p>
	 * 0 = customer_ 
	 * <p>
	 * 1 = autista_
	 */
	public static final String GeneraUsername(int type){
		String typeStr = "";
		if(type == 0){
			typeStr = "customer_";
		}else if(type == 1){
			typeStr = "autista_";
		}else if(type == 2){
			typeStr = "venditore_";
		}else{
			return null;
		}
		Random generator = new Random();
		int num1 = 0;
	    num1 = generator.nextInt(99999);
	    String concatenatedName = typeStr + num1;
		return concatenatedName;	
	}
	
	
	public static String getUserPasswordCustomer(int numChars) {
		String CharSet = "ABCDEFGHJKLMNOPQRSTUVWXYZ1234567890";
	    String Token = "";
	    for (int a = 1; a <= numChars; a++) {
	        Token += CharSet.charAt(new Random().nextInt(CharSet.length()));
	    }
	    return Token;
	}

	
	public static String getRandomToken_Numeri(int numChars) {
		String CharSet = "1234567890";
	    String Token = "";
	    for (int a = 1; a <= numChars; a++) {
	        Token += CharSet.charAt(new Random().nextInt(CharSet.length()));
	    }
	    return Token;
	}
	
	
	/**
	 * Passando come paramtro a codCorsa specifichi il tipo di corsa Particolare: 0 Media: 1
	 */
	public static String getRandomToken_RichiestaAutista(int numChars, String tipoCorsa) {
		return tipoCorsa + getRandomToken__LettPiccole_LettGrandi_Numeri(numChars);
	}
	
	
	public static String getRandomToken__LettPiccole_LettGrandi_Numeri(int numChars) {
		/*
		 * SMS SKEBBY Alcuni caratteri contano doppio: 
			[	Parentesi quadra aperta
			\	Backslash
			]	Parentesi quadra chiusa
			^	Potenza
			{	Parentesi graffa aperta
			|	Barra verticale
			}	Parentesi graffa chiusa
			~	Tilde
			€	Simbolo dell'euro
			Per maggiori dettagli consulta http://en.wikipedia.org/wiki/GSM_03.38
		 */
		/*
		 * ATTENZIONE: NON USARE IL CARATTERE #, /, ?, +, & ecc ecc NEL TOKEN, PERCHE' PASSANDOLO COME GET LA URL TRONCA IL TOKEN E DA ERRORE LA SERVLET
		 */
		String CharSet = "abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNOPQRSTUVWXYZ1234567890";
	    String token = "";
	    for (int a = 1; a <= numChars; a++) {
	        token += CharSet.charAt(new Random().nextInt(CharSet.length()));
	    }
	    return token;
	}
	
	/**
	 * Passando come paramtro a codCorsa specifichi il tipo di corsa Particolare: 0 Media: 1
	 */
	public static String getRandomToken__LettGrandi_Numeri(int numChars) {
		/*
		 * SMS SKEBBY Alcuni caratteri contano doppio: 
			[	Parentesi quadra aperta
			\	Backslash
			]	Parentesi quadra chiusa
			^	Potenza
			{	Parentesi graffa aperta
			|	Barra verticale
			}	Parentesi graffa chiusa
			~	Tilde
			€	Simbolo dell'euro
			Per maggiori dettagli consulta http://en.wikipedia.org/wiki/GSM_03.38
		 */
		/*
		 * ATTENZIONE: NON USARE IL CARATTERE #, /, ?, +, & ecc ecc NEL TOKEN, PERCHE' PASSANDOLO COME GET LA URL TRONCA IL TOKEN E DA ERRORE LA SERVLET
		 */
		String CharSet = "ABCDEFGHJKLMNOPQRSTUVWXYZ1234567890";
	    String token = "";
	    for (int a = 1; a <= numChars; a++) {
	        token += CharSet.charAt(new Random().nextInt(CharSet.length()));
	    }
	    return token;
	}
	
}
