package com.apollon.util;

import java.util.StringTokenizer;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import com.apollon.Constants;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public final class UtilString {

	
	public static int ContaParoleString( String someString ) {
		StringTokenizer tokens = new StringTokenizer(someString);
		return tokens.countTokens();
	}
	
	
	
	
	/**
	 * Il VarChar1000 di mysql, cioè definito in jpa come @Column(length = 1000) ha una lughezza massima di circa 800 in String <br>
	 * Passando una Stringa più lunga di 800 i caratteri dopo vengono rimossi
	 * @param str
	 * @return
	 */
	public static String TagliaVarChar1000(String str){
		int maxLetters = 800;
		if( str != null && str.length() > maxLetters ) {
			int restOf = str.length() - maxLetters;
			return str.substring(0, str.length() - restOf);
		}else {
			return str;
		}
	}
	
	/**
	 * <p>Mi ritorna l'ultima parola (se sono più di una parola) a destra dell'ultimo spazio</p>
	 * <pre>
	 * per esempio: "mannaggia il porco dio" mi ritorna "dio"
	 * </pre>
	 */
	public static String GetLastWord(String str){
		return str.substring(str.lastIndexOf(" ")+1);
	}
	
	/**
	 * Sostituisco gli accenti fatti con l'apice con le vocali accentate
	 */
	public static String ReplaceAccenti(String term){
		return term.replace("a'", "à").replace("e'", "è").replace("i'", "ì").replace("o'", "ò").replace("u'", "ù");
	}
	
	public static String RimuoviTuttiGliSpazi(String str){
		return str.replaceAll("\\s+","");
	}
	
	public static String RimuoviGliSpazi_Iniziali_e_finali(String str){
		return str.trim();
	}
    
    /**
     * https://howtodoinjava.com/regex/java-clean-ascii-text-non-printable-chars/
     * https://it.wikipedia.org/wiki/Carattere_di_controllo
     * https://en.wikipedia.org/wiki/Control_character
     * Ad esempio quando copio testo da una pagina web o altre fonti ci sono caratteri non visualazzibili 
     * Tutti i caratteri nella tabella ASCII al di sotto della posizione 32 fanno parte di questa categoria
     * sono detti in inglese: not printable characters
     * Provare anche: CharMatcher.JAVA_ISO_CONTROL.removeFrom(str);
     */
    public static String RimuoviCaratteriNonVisualizzabili(String str){
    	// TODO DA TESTARE
    	// removes non-printable characters from Unicode
		return str.replaceAll("\\p{C}", "");
	}
	
    
    public static String RimuoviCaratteriNonUTF8(String str){
    	// TODO DA TESTARE
    	// removes non-printable characters from Unicode
		return str.replaceAll("[^\\x20-\\x7e]", "");
	}
    
    
	/**
	 * removing invalid characters from a string to use it as a FileName
	 * 
	 */
	public static String RimuoviCaratteriIllegaliFileName(String str){
		str = str.replaceAll("[\\\\/:*?\"<>|]", "");
		if(str.length() > Constants.MAX_LENGHT_NOME_FILE){
			String extension = FilenameUtils. getExtension( str );
			str = str.substring(0, Math.min(str.length(), Constants.MAX_LENGHT_NOME_FILE));
			return str + "." + extension;
		}else{
			return str;
		}
	}
	
	/**
	 * removing invalid characters from a string to use it as a FileName
	 * e replace space con underscore
	 */
	public static String RimuoviCaratteriIllegaliFileName_e_ReplaceSpace(String str){
		str = str.replaceAll("[\\\\/:*?\"<>|]", "");
		str = str.replace(" ", "_");
		if(str.length() > Constants.MAX_LENGHT_NOME_FILE){
			String extension = FilenameUtils. getExtension( str );
			str = str.substring(0, Math.min(str.length(), Constants.MAX_LENGHT_NOME_FILE));
			return str + "." + extension;
		}else{
			return str;
		}
	}
	
	/**
	 * WordUtils.capitalizeFully(null)        = null
	 * WordUtils.capitalizeFully("")          = ""
	 * WordUtils.capitalizeFully("i am FINE") = "I Am Fine"
	 */
	public static String PrimaLetteraMaiuscola(String str){
		return WordUtils.capitalizeFully(str);
	}
	
	
	/**
	 * LettereMaiuscoleDopoilPunto
	 */
	public static String LettereMaiuscoleDopoilPunto(String descrizione){
		descrizione = StringUtils.lowerCase(descrizione);
		int pos = 0;
		boolean capitalize = true;
		StringBuilder sb = new StringBuilder(descrizione);
		while (pos < sb.length()) {
		    if (sb.charAt(pos) == '.') {
		        capitalize = true;
		    } else if (capitalize && !Character.isWhitespace(sb.charAt(pos))) {
		        sb.setCharAt(pos, Character.toUpperCase(sb.charAt(pos)));
		        capitalize = false;
		    }
		    pos++;
		}
		return descrizione = sb.toString();
	}

}
