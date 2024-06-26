package com.apollon.webapp.util;



import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.apollon.dao.GestioneApplicazioneDao;



/**
 * @author Matteo - matteo.manili@gmail.com
 *	
 */
public final class DownloadFile extends ApplicationUtils{
	
	/**
	 * Ritorna la pagina 404
	 */
	public static void Response404FileNonDisponibile(HttpServletResponse response) throws IOException{
		response.sendError(HttpServletResponse. SC_NOT_FOUND); // ritorna la 404
	}
	
}
