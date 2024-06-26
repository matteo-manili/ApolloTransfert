package com.apollon.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.routines.IntegerValidator;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public class NumberUtil implements Serializable {
	private static final long serialVersionUID = 8815411004795743282L;
	private static final Log log = LogFactory.getLog(NumberUtil.class);
	
	
	public static List<Long> DammiDuplicati(List<Long> listaLong){
		final Set<Long> listAutoDuplicate = new HashSet<Long>(); 
		final Set<Long> list = new HashSet<Long>();  
		for (Long idAuto_ite : listaLong){
			if (!list.add(idAuto_ite)){
				listAutoDuplicate.add(idAuto_ite);
			}
		}
		List<Long> sortedList = new ArrayList<Long>(listAutoDuplicate);
		Collections.sort(sortedList);
		return sortedList;
	}
	
	
	
	public static int DammiNumeroAltrimentiZeroInt(String numberString){
		IntegerValidator aa = new IntegerValidator();
		if(aa.validate(numberString) != null){
			return aa.validate(numberString);
		}else{
			return 0;
		}
	}
	
	public static BigDecimal DammiNumeroAltrimentiZeroBigDecimal(String numberString) throws ParseException{
		try{
			numberString = numberString.replace(",", ".");
			BigDecimal euro = new BigDecimal(numberString).setScale(2, RoundingMode.HALF_EVEN);
			if(euro.compareTo(BigDecimal.ZERO) != 0){
				return euro;
			}else{
				return BigDecimal.ZERO;
			}
		}catch(NumberFormatException nfe){
			log.debug("NumberFormatException nfe");
			return BigDecimal.ZERO;
		}
	}
	
	public static ArrayList<String> removeDuplicates(ArrayList<String> list) {
		// Store unique items in result.
		ArrayList<String> result = new ArrayList<>();
		// Record encountered Strings in HashSet.
		HashSet<String> set = new HashSet<>();
		// Loop over argument list.
		for (String item : list) {
		
		    // If String is not in set, add it to the list and the set.
		    if (!set.contains(item)) {
			result.add(item);
			set.add(item);
		    }
		}
		return result;
	}
	
	public static ArrayList<Long> removeDuplicatesLong(List<Long> inputList) {
		// Store unique items in result.
		ArrayList<Long> result = new ArrayList<>();
		// Record encountered Strings in HashSet.
		HashSet<Long> set = new HashSet<>();
		// Loop over argument list.
		for (Long item : inputList) {
		    // If String is not in set, add it to the list and the set.
		    if (!set.contains(item)) {
				result.add(item);
				set.add(item);
		    }
		}
		return result;
	}
	
	/**
	 * vedere: https://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal-places
	 */
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();
	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}
	
	

	
	
}
