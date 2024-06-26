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
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.dao.DataIntegrityViolationException;

import com.apollon.dao.AgenzieViaggioBitDao;
import com.apollon.dao.ProvinceDao;
import com.apollon.model.AgenzieViaggioBit;
import com.apollon.model.Province;
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
public class CatturaFederWebAziende extends ApplicationUtils {

	public static AgenzieViaggioBitDao agenzieViaggioBitDao = (AgenzieViaggioBitDao) contextDao.getBean("AgenzieViaggioBitDao");
	public static ProvinceDao provinceDao = (ProvinceDao) contextDao.getBean("ProvinceDao");
	
	public static void Start() throws ConnectException, NullPointerException, IOException {
		System.setProperty("webdriver.chrome.driver", "C:/chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		try{
			try{
				// http://www.federweb.com/NEW/ricerca-aziende-italia-2# // SOLO MANUALE, SERVE A VEDERE LA TENDINA CON LE VOCI DELLE AZIENDE
				// http://www.federweb.com/aziende-italiane-ricerca/index-nocat.php (QUESTA è LA PAGINA DA DOVE FARE PARTIRE LO SRAPING DOVE SI VEDONO GLI INPUT DEL FORM, IL LINK SOPRA FA L'INCLUDE DI QUESTA)
				List<Province> provList = provinceDao.getProvince_order_Abitanti();
				for(Province prov_ite: provList){
					System.out.println("------------- http://www.federweb.com/aziende-italiane-ricerca/index-nocat.php");
					driver.get("http://www.federweb.com/aziende-italiane-ricerca/index-nocat.php");
					WebElement inputCategoria = driver.findElement(By.id("keyword"));
					
					inputCategoria.sendKeys( "Agriturismo" );
					//inputCategoria.sendKeys( "Enti Turistici" );
					//inputCategoria.sendKeys( "Agenzie Viaggi" );
					//inputCategoria.sendKeys( "Alberghi" );
					
					WebElement inputCitta = driver.findElement(By.id("city"));
					inputCitta.sendKeys( prov_ite.getNomeProvincia() );
					//inputCitta.sendKeys( "roma" );
					WebElement randomNumber = driver.findElement(By.id("randomNumber"));
					System.out.println("randomNumber: "+randomNumber.getText());
					WebElement randomNumber2 = driver.findElement(By.id("randomNumber2"));
					System.out.println("randomNumber2: "+randomNumber2.getText());
					int resultCaptcha = Integer.parseInt(randomNumber.getText()) + Integer.parseInt(randomNumber2.getText());
					WebElement captcha = driver.findElement(By.id("captcha"));
					captcha.sendKeys( Integer.toString(resultCaptcha) );
					driver.findElement(By.name("submit")).click();
					
					// MOLTO IMPORTANTE ASPETTA 10 MINUTI CHE CARICA LA PAGINA PRIMA DI ANDARE IN ERRORE DI TIMEOUT
					driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.MINUTES); 

					String Tab_2 = "";
					String Tab_1 = driver.getWindowHandle();
					System.out.println("Tab_1: "+Tab_1);
					for(String tab: driver.getWindowHandles()) {
					    if(!tab.equals(Tab_1)){
					        driver.switchTo().window(tab);
					        Tab_2 = tab;
					        System.out.println("Tab_2: "+Tab_2);
					    }       
					}
					List<WebElement> TrList = driver.findElements(By.tagName("tr"));
					System.out.println("TrList sieze: "+TrList.size());
					for(int a = 1; a < TrList.size(); a++) {
						WebElement button = TrList.get(a).findElement(By.tagName("button"));
						if(button != null){
							System.out.println(button.getText());
							List<WebElement> TdList = TrList.get(a).findElements(By.tagName("td"));
							String nomeAzienda = TdList.get(1).getText();
							String indirizzo = TdList.get(2).getText() +" "+ TdList.get(3).getText() +" "+ TdList.get(4).getText();
							button.click();
							for(String tab: driver.getWindowHandles()) {
								if(!tab.equals(Tab_1) && !tab.equals(Tab_2)){
									driver.switchTo().window(tab);
									String Tab_3 = tab;
									PrendiInformazioniHotel_e_SalvaDB(nomeAzienda, indirizzo, driver);
									driver.switchTo().window(Tab_3).close();
								}
							}
							driver.switchTo().window(Tab_2);
						}
					}
				}
				
			}catch(TimeoutException  aa) {
				System.out.println(aa.getMessage());
			}catch(StaleElementReferenceException bb) {
				System.out.println(bb.getMessage());
			}catch(NoSuchElementException cc ){
				System.out.println(cc.getMessage());
			}catch(UnhandledAlertException dd ){
				System.out.println(dd.getMessage());
			}
			System.out.println("================= FINE SCRAPING =================");
		}catch(WebDriverException wdE){
			wdE.printStackTrace();
		}
		driver.quit();
	}
	
	
	private static void PrendiInformazioniHotel_e_SalvaDB(String nomeAzienda, String indirizzo, WebDriver driver){
		try{
			AgenzieViaggioBit hotel = new AgenzieViaggioBit();
			hotel.setParametriSconto( CatturaEmailMarketing_UTIL.DammiTokenScontoEmailUnivoco() );
			hotel.setSitoWebScraping("www.federweb.com");
			hotel.setNome(nomeAzienda);
			System.out.println("nomeAzienda: "+nomeAzienda);
			hotel.setCitta_e_indirizzo(indirizzo);
			System.out.println("indirizzo: "+indirizzo);
			List<WebElement> links = driver.findElements(By.tagName("a"));
			for(int a = 0; a < links.size(); a++) {
				String href = links.get(a).getAttribute("href");
			    Matcher m = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+").matcher(href);
			    while(m.find()){
			        hotel.setEmail( m.group() );
			        System.out.println( "email: "+m.group() );
			        agenzieViaggioBitDao.saveAgenzieViaggioBit(hotel);
					System.out.println("..Agenzia Salvata");
			        break;
			    }
			}
		}catch (final DataIntegrityViolationException dive) {
    		System.out.println(dive.getMessage());
		}catch(NoSuchElementException aa ){
			System.out.println(aa.getMessage());
		}catch(UnhandledAlertException bb ){
			System.out.println(bb.getMessage());
		}
	}
	
	
}
