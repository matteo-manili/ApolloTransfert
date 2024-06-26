package com.apollon.webapp.util;

import java.io.File;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.apollon.Constants;
import com.apollon.dao.AeroportiDao;
import com.apollon.dao.MuseiDao;
import com.apollon.dao.PortiNavaliDao;
import com.apollon.dao.RegioniDao;
import com.apollon.model.Aeroporti;
import com.apollon.model.Musei;
import com.apollon.model.PortiNavali;
import com.apollon.model.Province;
import com.apollon.util.PropertiesFileUtil;
import com.apollon.webapp.util.controller.gestioneApplicazione.GestioneApplicazioneUtil;
import com.apollon.webapp.util.controller.tariffe.MenuTariffeTransfer;
import com.apollon.webapp.util.controller.tariffe.MenuTariffeTransfer.MenuTerrTariffeTransfer;
import com.apollon.webapp.util.controller.tariffe.MenuTariffeTransfer.MenuTerrTariffeTransfer.MenuTerrTariffeTransferElement;
import com.redfin.sitemapgenerator.ChangeFreq;
import com.redfin.sitemapgenerator.W3CDateFormat;
import com.redfin.sitemapgenerator.WebSitemapGenerator;
import com.redfin.sitemapgenerator.WebSitemapUrl;
import com.redfin.sitemapgenerator.W3CDateFormat.Pattern;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 * Questa classe passandogli un indirizzo crea una parte del link adatto agli standard seo, ad esempio:
 * http://www.servizidog.it/servizidog/adozione/la-cinquina-bufalotta/via-alberto-lollio/3
 * 
 *  L'intervallo di valori validi della PRORITY è compreso tra 0.0 e 1.0, La priorità predefinita di una pagina è 0.5
 *  tutti gli urli devono finire con lo slash "/"
 *  
 *  vedere pagina https://www.sitemaps.org/it/protocol.html
 */


public class Sitemap extends ApplicationUtils implements Serializable {
	private static final long serialVersionUID = 1172645121550728878L;
	private static final Log log = LogFactory.getLog(Sitemap.class);
	public static RegioniDao regioniDao = (RegioniDao) contextDao.getBean("RegioniDao");
	private static AeroportiDao aeroportiDao = (AeroportiDao) contextDao.getBean("AeroportiDao");
	private static MuseiDao museiDao = (MuseiDao) contextDao.getBean("MuseiDao");
	private static PortiNavaliDao portiNavaliDao = (PortiNavaliDao) contextDao.getBean("PortiNavaliDao");
	
	public static String CreaSitemapUrl(ServletContext servletContext) {
		final double priority_1_00 = 1.00;
		final double priority_0_85 = 0.85;
		final double priority_0_69 = 0.69;
		
		try {
			if(GestioneApplicazioneUtil.CheckContextPathCorretto(servletContext)) {
				String fileUploadDirectory = servletContext.getRealPath("/");
				//String contentP = servletContext.getContextPath();
				File myDir = new File(fileUploadDirectory);
				//File myFile = new File(myDir, nameFileSitemap);
				String DomaninName = (ApplicationUtils.CheckAmbienteApolloTransfert_Static_ContextNameProduzione_OR_ContextNameSviluppo(servletContext)) ?
						ApplicationMessagesUtil.DammiMessageSource("https.w3.domain.apollotransfert.name") : 
							ApplicationMessagesUtil.DammiMessageSource("https.w3.domain.ncctransferonline.name");
				WebSitemapGenerator wsg = new WebSitemapGenerator(DomaninName, myDir);
				W3CDateFormat dateFormat = new W3CDateFormat(Pattern.DAY);
				dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
				
				wsg = WebSitemapGenerator.builder(DomaninName, myDir).dateFormat(dateFormat).build();
				WebSitemapUrl url;
				url = new WebSitemapUrl.Options(DomaninName+"/").priority(priority_1_00).changeFreq(ChangeFreq.DAILY).build();
				wsg.addUrl(url);
				url = new WebSitemapUrl.Options(DomaninName+"/"+Constants.PAGE_LOGIN).priority(priority_0_85).changeFreq(ChangeFreq.DAILY).build();
				wsg.addUrl(url);
				url = new WebSitemapUrl.Options(DomaninName+"/"+Constants.PAGE_SIGNUP).priority(priority_0_85).changeFreq(ChangeFreq.DAILY).build();
				wsg.addUrl(url);
				url = new WebSitemapUrl.Options(DomaninName+"/"+Constants.PAGE_RECOVERPASS).priority(priority_0_85).changeFreq(ChangeFreq.DAILY).build();
				wsg.addUrl(url);
				url = new WebSitemapUrl.Options(DomaninName+"/"+Constants.PAGE_CHI_SIAMO).priority(priority_0_85).changeFreq(ChangeFreq.DAILY).build();
				wsg.addUrl(url);
				url = new WebSitemapUrl.Options(DomaninName+"/"+Constants.PAGE_CONTATTI).priority(priority_0_85).changeFreq(ChangeFreq.DAILY).build();
				wsg.addUrl(url);
				url = new WebSitemapUrl.Options(DomaninName+"/"+Constants.PAGE_TARIFFE_TRANSFER).priority(priority_0_85).changeFreq(ChangeFreq.DAILY).build();
				wsg.addUrl(url);
				url = new WebSitemapUrl.Options(DomaninName+"/"+Constants.PAGE_COLLABORATORI).priority(priority_0_85).changeFreq(ChangeFreq.DAILY).build();
				wsg.addUrl(url);
				url = new WebSitemapUrl.Options(DomaninName+"/"+Constants.PAGE_NCC_AGENZIE_VIAGGIO_SIAMO).priority(priority_0_85).changeFreq(ChangeFreq.DAILY).build();
				wsg.addUrl(url);
				url = new WebSitemapUrl.Options(DomaninName+"/"+Constants.PAGE_NCC_AZIENDE).priority(priority_0_85).changeFreq(ChangeFreq.DAILY).build();
				wsg.addUrl(url);
				url = new WebSitemapUrl.Options(DomaninName+"/"+Constants.PAGE_CONSIGLI_DI_VIAGGIO).priority(priority_0_85).changeFreq(ChangeFreq.DAILY).build();
				wsg.addUrl(url);
				// TARIFFE TRANSFER
				MenuTerrTariffeTransfer menuTerrTariffeTransfer = MenuTariffeTransfer.DammiMenuItems_MacroRegioni_Regioni(Constants.Locale_IT, null, regioniDao.MenuTariffe_Province());
				for(MenuTerrTariffeTransferElement ite: menuTerrTariffeTransfer.getMacroRegioni() ) {
					url = new WebSitemapUrl.Options(DomaninName+"/"+ite.getUrl()).priority(
							ite.getUrl().equals(Constants.PAGE_TARIFFE_TRANSFER) ? priority_0_85 : priority_0_69
							).changeFreq(ChangeFreq.DAILY).build();
					wsg.addUrl(url);
				}
				for(MenuTerrTariffeTransferElement ite: menuTerrTariffeTransfer.getRegioni() ) {
					url = new WebSitemapUrl.Options(DomaninName+"/"+ite.getUrl()).priority(priority_0_69).changeFreq(ChangeFreq.DAILY).build();
					wsg.addUrl(url);
				}
				for(MenuTerrTariffeTransferElement ite: menuTerrTariffeTransfer.getProvince() ) {
					url = new WebSitemapUrl.Options(DomaninName+"/"+ite.getUrl()).priority(priority_0_69).changeFreq(ChangeFreq.DAILY).build();
					wsg.addUrl(url);
				}
				// CONSIGLI DI VIAGGIO
				PropertiesFileUtil mpc = new PropertiesFileUtil();
		        Set<Object> keys = mpc.getAllKeys();
		        for(int conta = 0; conta <= keys.size(); conta++) {
		        	for(Object k: keys) {
			            String key = (String)k;
			            if( key.startsWith( ("consigli.post.url."+conta+".").toString() ) ) {
			            	//System.out.println(key+": "+mpc.getPropertyValue(key)); 
			            	url = new WebSitemapUrl.Options(DomaninName+"/"+Constants.PAGE_CONSIGLI_DI_VIAGGIO+"/"+mpc.getPropertyValue(key)).priority(priority_0_69).changeFreq(ChangeFreq.DAILY ).build();
							wsg.addUrl(url);
			            }
			        }
		        }
				
				/*
				// solo in www.apollotransfert.com
				if(ApplicationUtils.CheckAmbienteApolloTransfert_Static_ContextNameProduzione_OR_ContextNameSviluppo(servletContext)) {
					//List<Province> provList = provinceDao.getProvince();
					List<Province> provList = TerritorioUtil.ProvinceAutistiDisponibili_Provincia();
					for(Province prov_ite : provList){
						//if( !prov_ite.getNomeProvincia().equals(Constants.PROVINCIA_STRANIERA) ){
						url = new WebSitemapUrl.Options(DomaninName+"/"+Constants.URL_TRANSFER + prov_ite.getUrl()+"/")
							.lastMod( new Date() ).priority(priority_0_69).changeFreq(ChangeFreq.DAILY ).build();
						wsg.addUrl(url);
					}
					List<Aeroporti> listAeroporti = aeroportiDao.getAeroporti();
					for(Aeroporti aero_ite : listAeroporti) {
						url = new WebSitemapUrl.Options(DomaninName+"/"+Constants.URL_TRANSFER + aero_ite.getUrl()+"/")
							.lastMod( new Date() ).priority(priority_0_69).changeFreq(ChangeFreq.DAILY ).build();
						wsg.addUrl(url);
					}
					List<Musei> Musei = museiDao.getMusei();
					for(Musei museo_ite: Musei){
						url = new WebSitemapUrl.Options(DomaninName+"/"+Constants.URL_TRANSFER + museo_ite.getUrl()+"/")
							.lastMod( new Date() ).priority(priority_0_69).changeFreq(ChangeFreq.DAILY ).build();
						wsg.addUrl(url);
					}
					List<PortiNavali> portiNav = portiNavaliDao.getPortiNavali();
					for(PortiNavali porti_ite : portiNav){
						url = new WebSitemapUrl.Options(DomaninName+"/"+Constants.URL_TRANSFER + porti_ite.getUrl()+"/")
							.lastMod( new Date() ).priority(priority_0_69).changeFreq(ChangeFreq.DAILY ).build();
						wsg.addUrl(url);
					}
				}
				*/
				
				wsg.write();
				String Esito = "Creazione Sitemap.xml complete [OK] "+fileUploadDirectory;
				log.debug(Esito);
				return Esito;
			}else{
				return "Sitemap Non Creata";
			}
		} catch (MalformedURLException e) {
			log.debug("ERRORE MalformedURLException CreaSitemap.creaSitemapProUrlProfili");
			e.printStackTrace();
			return e.getMessage();
		}catch (Exception exc) {
			log.debug("ERRORE Exception CreaSitemap.creaSitemapProUrlProfili");
			exc.printStackTrace();
			return exc.getMessage();
		}
	}
	
}
