package com.apollon.util;

import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.apollon.webapp.util.controller.home.CalcoloTariffe_Base;


/**
 * 
 * Fare partire il cronometro mettendo: long startTime = System.nanoTime(); 
 * e poi richiamare: DammiTempoOperazione.DammiSecondi(startTime, "1- "); ogni degli algoritimi da misuare 
 * 
 * @author Matteo
 *
 */
public class DammiTempoOperazione {
	private static final Log log = LogFactory.getLog(DammiTempoOperazione.class);
	
	public static void DammiSecondi(long startTime, String commento) {
		long endTime = System.nanoTime();
		long duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.
		double seconds = (double)duration / 1000000000.0;
		
		//double seconds_2 = TimeUnit.NANOSECONDS.toSeconds(duration);
		//log.debug("COMMENTO: "+commento+" seconds_2: "+seconds_2); // non da i decimali
		
		seconds = seconds < 0 ? 0d : seconds;
		//log.info("COMMENTO: "+commento+" SECONDI: "+seconds);
		log.info("COMMENTO: "+commento+" SECONDI: "+seconds);
	}
	
	
	public static void DammiMilliSecondi(long startTime, String commento) {
		long endTime = System.nanoTime();
		long duration = (endTime - startTime); 
		double millisecondi = TimeUnit.NANOSECONDS.toMillis(duration);
		millisecondi = millisecondi < 0 ? 0d : millisecondi;
		log.info("COMMENTO: "+commento+" millisecondi: "+millisecondi);
	}
	
	
}
