package com.apollon.webapp.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.apollon.dao.AgA_GiornateDao;
import com.apollon.dao.RichiestaMediaDao;
import com.apollon.webapp.util.ApplicationUtils;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public class PuliziaDatabase extends ApplicationUtils {
	private static final Log log = LogFactory.getLog(PuliziaDatabase.class);
	
	private static RichiestaMediaDao richiestaMediaDao = (RichiestaMediaDao) contextDao.getBean("RichiestaMediaDao");
	private static AgA_GiornateDao agA_GiornateDao = (AgA_GiornateDao) contextDao.getBean("AgA_GiornateDao");
	
	
	public static void EseguiPuliziaDatabase_RichiestaMedia() {
		log.debug("EseguiPuliziaDatabase_RichiestaMedia");
		richiestaMediaDao.PuliziaDataBase_RichiestaMedia();
	}
	
	public static void EseguiPuliziaDatabase_GiornateTariffari() {
		log.debug("EseguiPuliziaDatabase_GiornateTariffari");
		agA_GiornateDao.PuliziaDatabase_GiornateTariffari();
	}

	
	
}
