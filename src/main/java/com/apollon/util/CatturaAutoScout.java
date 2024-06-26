package com.apollon.util;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
/*
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
*/
import org.springframework.dao.DataIntegrityViolationException;

import com.apollon.Constants;
import com.apollon.dao.MarcaAutoScoutDao;
import com.apollon.dao.ModelloAutoScoutDao;
import com.apollon.dao.ProvinceDao;
import com.apollon.model.MarcaAutoScout;
import com.apollon.model.ModelloAutoScout;
import com.apollon.model.Province;
import com.apollon.webapp.util.ApplicationUtils;
import com.apollon.webapp.util.CalcoloPrezzi;

/**
 * @author Matteo - matteo.manili@gmail.com
 *	
 *	Questa Classe mi prende automaticamente velocemente i le province confinanti di una provincia dalle pagine di wikipedia>
 *	per esempio https://it.wikipedia.org/wiki/Provincia_di_Messina
 *
 *	vedere documentazione: http://www.seleniumhq.org/docs/03_webdriver.jsp
 *	e scaricare: chromedriver.exe per simulare il browser
 *
 *	Per farlo partire scrivere nel main: CatturaProvinceConfinantiWikipedia.Cattura();
 *
 */
public class CatturaAutoScout extends ApplicationUtils {

	public static ProvinceDao provinceDao = (ProvinceDao) contextDao.getBean("ProvinceDao");
	
	public static MarcaAutoScoutDao marcaAutoScoutDao = (MarcaAutoScoutDao) contextDao.getBean("MarcaAutoScoutDao");
	public static ModelloAutoScoutDao modelloAutoScoutDao = (ModelloAutoScoutDao) contextDao.getBean("ModelloAutoScoutDao");
	
	/*
	

	//mi devo fare ritornare il valore medio di una macchina da autoscout24.it, cercandola per marca, modello e anno immatricolazione

	public static Double DammiValoreMedioAuto(ModelloAutoScout modello, int annoImmatricolazione) throws NullPointerException, IOException, Exception {

		List<Double> listPrezzi = new ArrayList<Double>();
		for( int i=1; i <= 3; i++ ){
			String link = "https://www.autoscout24.it/risultati?mmvmk0="+modello.getMarcaAutoScout().getIdAutoScout()+
					"&mmvmd0="+ modello.getIdAutoScout() +"&mmvco=1&cy=I&fregfrom="+annoImmatricolazione+"&fregto="+annoImmatricolazione+"&page="+i;
			
			StringBuilder sb = new StringBuilder( link );
	        String html = UrlConnection.HttpConnection( sb );
			
	        Document doc = Jsoup.parse(html);
	        String title = doc.title();
			
			Elements eles = doc.select(".title-container");
			for (int i_a = 0; i_a < eles.size(); i_a++) {
				Elements elesPrice = eles.get(i_a).select(".cldt-price");
				String str = elesPrice.first().text().replaceAll("\\D+",""); // elimina valori non numerici
				listPrezzi.add( Double.parseDouble(str) );
			}
		}
		
		Double prezzoMedio = CalcoloPrezzi.DammiTariffaMediaDouble(listPrezzi);
		return prezzoMedio;
	}
	
	
	
	public static void CatturaMarche() {
		try {
			System.setProperty("webdriver.chrome.driver", "C:/chromedriver.exe");
			WebDriver driver = new ChromeDriver();
        	driver.get("https://www.autoscout24.it/risultati?mmvmk0=47&mmvmd0=1863&mmvco=1&cy=I&fregfrom=2015&fregto=2015");
        	System.out.println("Page title is: " + driver.getTitle());
        			
        	//List<WebElement> ahref =
        		//.findElements(By.tagName("body")).get(0);
        		//driver.findElements(By.tagName("as24-grouped-items-data-source")).ge;
        		//.findElement(By.tagName("as24-grouped-items-data-source"));
	        
        	List<WebElement> tag = driver.findElements(By.tagName("as24-grouped-items-data-source")); //.get(0).findElements(By.tagName("item"));

	        try{
	        	List<WebElement> itemList = tag.get(0).findElements(By.tagName("item"));
		        for (int i_a = 0; i_a < itemList.size(); i_a++) {
	        		System.out.println( itemList.get(i_a).getAttribute("key") +" "+ itemList.get(i_a).getAttribute("value").trim());
	        		if( !itemList.get(i_a).getAttribute("value").contains("Altro") ){
	        			MarcaAutoScout marca = new MarcaAutoScout();
	        			marca.setIdAutoScout( Long.parseLong( itemList.get(i_a).getAttribute("key") ) );
	        			marca.setName( itemList.get(i_a).getAttribute("value").trim() );
	        			marcaAutoScoutDao.saveMarcaAutoScout(marca);
	        		}
	        	}
			}catch(IndexOutOfBoundsException aa){
	    		System.out.println(aa.getMessage());
	    	}
	        
			(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
	            public Boolean apply(WebDriver d) {
	            	//return d.getTitle().toLowerCase().startsWith("cheese!");
	            	return !d.getCurrentUrl().equals("") ;
	            }
	        });

	        driver.quit();

		} catch (Exception e) {
			System.out.println("Exception e");
			e.printStackTrace();
		}
	}
	
	
	
	 // Questo codice serve a eliminare gli spazi bienchi del nome del modello.
	 // Sono spazi particolari hanno il codice char 160 chiamati (non breaking space).
	 // Per rimuovireli bisgona procedere come scritto sotto.
	 // 
	 // vedere anche https://stackoverflow.com/questions/28295504/how-to-trim-no-break-space-in-java
	 
	 
	//	List<ModelloAutoScout> modelloAutoScoutList = modelloAutoScoutDao.getModelloAutoScout();
	//	for(ModelloAutoScout modelloAutoScout_ite: modelloAutoScoutList){
			//String nomeModello = modelloAutoScout_ite.getName(); //.substring(0, 1);
			//nomeModello = nomeModello.replace("\u00A0","");
			//nomeModello = nomeModello.replace("(tutto)","(generico)");
			//modelloAutoScout_ite.setName(nomeModello);
			//modelloAutoScoutDao.saveModelloAutoScout(modelloAutoScout_ite);
	//	}
		// V = 86
		// SPAZIO BIANCO = 32
		// SPAZIO BIANCO (ALTRO TIPO) = 160
		// string.replaceAll("(^\\h*)|(\\h*$)","");
	//	System.out.println( modelloAutoScoutList.size() );
	  
	 
	
	public static void CatturaModelli() {
		try {
			System.setProperty("webdriver.chrome.driver", "C:/chromedriver.exe");
			WebDriver driver = new ChromeDriver();
			
			List<MarcaAutoScout> marcaAutoScoutList = marcaAutoScoutDao.getMarcaAutoScout();
	        for (MarcaAutoScout marcaAutoScoutList_ite: marcaAutoScoutList) {
	        	driver.get("https://www.autoscout24.it/risultati?&mmvmk0="+marcaAutoScoutList_ite.getIdAutoScout());
	        	System.out.println( "https://www.autoscout24.it/risultati?&mmvmk0="+marcaAutoScoutList_ite.getIdAutoScout() );
	        	System.out.println( marcaAutoScoutList_ite.getName() );
	        	List<WebElement> tag = driver.findElements(By.tagName("as24-plain-data-source"));
	        	try{
	        		List<WebElement> itemList = tag.get(0).findElements(By.tagName("item"));
		        	for (int i_a = 0; i_a < itemList.size(); i_a++) {
		        		if( !itemList.get(i_a).getAttribute("value").contains("Altro") ){
		        			//if( !itemList.get(i_a).getAttribute("value").contains("(tutto)") ){
		        				String modelloStr = itemList.get(i_a).getAttribute("value").trim();
		        				System.out.println( itemList.get(i_a).getAttribute("key") +" "+ modelloStr );
			        			ModelloAutoScout modello = new ModelloAutoScout();
			        			modello.setMarcaAutoScout( marcaAutoScoutList_ite );
				        		modello.setIdAutoScout( Long.parseLong(itemList.get(i_a).getAttribute("key")) );
				        		modello.setName( modelloStr );
				        		modelloAutoScoutDao.saveModelloAutoScout(modello);
		        			//}
		        		}
		        	}
	        	}catch(IndexOutOfBoundsException aa){
	        		System.out.println(aa.getMessage());
	        		
	        	}
	        }

			(new WebDriverWait(driver, 20)).until(new ExpectedCondition<Boolean>() {
	            public Boolean apply(WebDriver d) {
	            	//return d.getTitle().toLowerCase().startsWith("cheese!");
	            	return !d.getCurrentUrl().equals("") ;
	            }
	        });

	        driver.quit();

		} catch (Exception e) {
			System.out.println("Exception e");
			e.printStackTrace();
		}
	}
	
	*/
	
	
	
}
