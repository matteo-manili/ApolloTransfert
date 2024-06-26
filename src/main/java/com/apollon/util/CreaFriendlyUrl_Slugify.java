package com.apollon.util;

import java.io.IOException;

import com.github.slugify.Slugify;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 * Questa classe passandogli un indirizzo crea una parte del link adatto agli standard seo, ad esempio:
 * http://www.servizidog.it/servizidog/adozione/la-cinquina-bufalotta/via-alberto-lollio/3
 */


public final class CreaFriendlyUrl_Slugify {

	public static String creaUrl(String urlBase) throws IOException{
		//creo la url profilo in base all'indirizzo
		//esempio: http://www.servizidog.it/servizidog/adozione/la-cinquina-bufalotta/via-alberto-lollio/3
		Slugify slugify = new Slugify();
		String urlSlug = null;
    	urlSlug = slugify.slugify(urlBase);
		return urlSlug;
	}

	public static String creaUrlAnnunci(String urlBase, String luogo, String nome, long id) throws IOException{
		//creo la url profilo in base all'indirizzo
		//esempio: http://www.servizidog.it/servizidog/adozione/la-cinquina-bufalotta/via-alberto-lollio/3
		Slugify slugify = new Slugify();
		String urlSlug = null;
    	urlSlug = slugify.slugify(urlBase) +"/"+
    				slugify.slugify(luogo) +"/"+ 
    				slugify.slugify(nome) +"/"+ 
    				slugify.slugify(String.valueOf(id));
		return urlSlug;
	}
	
}
