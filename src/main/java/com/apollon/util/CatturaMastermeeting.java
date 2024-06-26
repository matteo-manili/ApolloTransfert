package com.apollon.util;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.dao.DataIntegrityViolationException;

import com.apollon.dao.AgenzieViaggioBitDao;
import com.apollon.model.AgenzieViaggioBit;
import com.apollon.webapp.util.ApplicationUtils;

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
 * Quando viene fuori errore Element is not clickable at point (36, 72). Other element would receive the click:
 * vedere soluzione https://stackoverflow.com/questions/44912203/selenium-web-driver-java-element-is-not-clickable-at-point-36-72-other-el/44916498
 * e fare:
 * Actions actions = new Actions(driver);
 * actions.moveToElement( driver.findElement(By.id( idStrutture )) ).click().build().perform();
 *
 */
public class CatturaMastermeeting extends ApplicationUtils {

	public static AgenzieViaggioBitDao agenzieViaggioBitDao = (AgenzieViaggioBitDao) contextDao.getBean("AgenzieViaggioBitDao");
	
	public static void Start() throws ConnectException, NullPointerException, IOException {
		//int [] CodiciRegioni = {2,3,7,8,22,14,15,1022,9,18,19,1,13,6,1023,10,11,23,12,5,20,16,17}; // TUTTE !!!!
		//int [] CodiciRegioni = {14,15,1022,9,18,19,1,13,6,1023,10,11,23,12,5,20,16,17};
		//int [] CodiciRegioni = {2,8,22,14,15,1022,9,18,19,1,13,6,1023,10,11,23,12,5,20,16,17};
		int [] CodiciRegioni = {17};
		// regione 8 ne ha 9 hotel
		System.setProperty("webdriver.chrome.driver", "C:/chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		try{
			List<Long> IdStruttureTotaliList = new ArrayList<Long>();
			for(int CodiceRegione_ite: CodiciRegioni) {
				System.out.println("------------- http://mastermeeting.it/directory/strutture-servizi?reg="+CodiceRegione_ite+"&source=r_str");
				driver.get("http://mastermeeting.it/directory/strutture-servizi?reg="+CodiceRegione_ite+"&source=r_str");
				List<Integer> IdPaginationList = new ArrayList<Integer>();
				List<WebElement> hrefList = driver.findElements(By.tagName("a"));
				for(int a = 0; a < hrefList.size(); a++) {
					String href = hrefList.get(a).getAttribute("href");
					if(href != null && href.contains("/strutture-servizi?source=r_str&page=") && hrefList.get(a).getText() != null && !hrefList.get(a).getText().equals("") ){
						IdPaginationList.add( Integer.parseInt(hrefList.get(a).getText()) );
					}
				}
				if(IdPaginationList.size() == 0){
					IdPaginationList.add( 0 );
				}
				Collections.sort(IdPaginationList); // ordino la lista
				for(int IdPaginationList_ite: IdPaginationList) {
					System.out.println("----- PAGINA "+IdPaginationList_ite+" -----");
					if(IdPaginationList_ite > 1){
						driver.get("http://mastermeeting.it/directory/strutture-servizi?reg="+CodiceRegione_ite+"&source=r_str&page="+IdPaginationList_ite);
					}
					List<WebElement> linksList = driver.findElements(By.className("awe-btn-default"));
					List<String> IdStrutturePageList = new ArrayList<String>();
					for(int a = 0; a < linksList.size(); a++) {
						IdStrutturePageList.add(linksList.get(a).getAttribute("id"));
					}
					try{
						for(String idStrutturaPage_ite: IdStrutturePageList) {
							if( !IdStruttureTotaliList.contains(Long.parseLong(idStrutturaPage_ite)) ){
								driver.get("http://mastermeeting.it/directory/strutture-servizi?source=d_str&id_strutture="+idStrutturaPage_ite);
								PrendiInformazioniHotel_e_SalvaDB(idStrutturaPage_ite, driver);
								IdStruttureTotaliList.add(Long.parseLong(idStrutturaPage_ite));
								driver.navigate().back(); // Ritorno indietro ---->
							}
						}
					}catch(StaleElementReferenceException e) {
						System.out.println(e.getMessage());
					}catch(NoSuchElementException aa ){
						System.out.println(aa.getMessage());
					}catch(UnhandledAlertException bb ){
						System.out.println(bb.getMessage());
					}
				}
			}
			System.out.println("================= FINE SCRAPING =================");
		}catch(WebDriverException wdE){
			wdE.printStackTrace();
		}
		driver.quit();
	}
	
	private static void PrendiInformazioniHotel_e_SalvaDB(String idStruttura, WebDriver driver){
		try{
			AgenzieViaggioBit hotel = new AgenzieViaggioBit();
			hotel.setParametriSconto( CatturaEmailMarketing_UTIL.DammiTokenScontoEmailUnivoco() );
			String nomeHotel = driver.findElement(By.className("big")).getText();
			System.out.println("NOME HOTEL: "+nomeHotel);
			hotel.setNome(nomeHotel);
			List<WebElement> infoNdrList = driver.findElements(By.className("infoNdr"));
			if(infoNdrList.get(0).getText() != null){
				System.out.println( "Indirizzo: "+infoNdrList.get(0).getText() );
				hotel.setCitta_e_indirizzo( infoNdrList.get(0).getText() );
			}
			for(int a = 0; a < infoNdrList.size(); a++) {
				if( infoNdrList.get(a).getText() != null && infoNdrList.get(a).getText().contains("E-MAIL:") ){
					String s = infoNdrList.get(a).getText();
				    Matcher m = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+").matcher(s);
				    while (m.find()) {
				        System.out.println( "email: "+m.group() );
				        hotel.setEmail( m.group()  );
				        break;
				    }
				}
			}
			agenzieViaggioBitDao.saveAgenzieViaggioBit(hotel);
			System.out.println("..Agenzia Salvata");
		}catch (final DataIntegrityViolationException dive) {
    		System.out.println(dive.getMessage());
		}catch(NoSuchElementException aa ){
			System.out.println(aa.getMessage());
		}catch(UnhandledAlertException bb ){
			System.out.println(bb.getMessage());
		}
	}
	
}
