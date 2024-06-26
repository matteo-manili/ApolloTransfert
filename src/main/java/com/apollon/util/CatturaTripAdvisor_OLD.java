package com.apollon.util;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
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
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.dao.DataIntegrityViolationException;

import com.apollon.dao.AgenzieViaggioBitDao;
import com.apollon.dao.ProvinceDao;
import com.apollon.model.AgenzieViaggioBit;
import com.apollon.model.Province;
import com.apollon.webapp.util.ApplicationUtils;
import com.apollon.webapp.util.TerritorioUtil;

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
public class CatturaTripAdvisor_OLD extends ApplicationUtils {

	public static AgenzieViaggioBitDao agenzieViaggioBitDao = (AgenzieViaggioBitDao) contextDao.getBean("AgenzieViaggioBitDao");
	public static ProvinceDao provinceDao = (ProvinceDao) contextDao.getBean("ProvinceDao");
	
	public static void Start() throws ConnectException, NullPointerException, IOException, InterruptedException {
		System.setProperty("webdriver.chrome.driver", "C:/chromedriver.exe");
		

		List<Province> provList = provinceDao.getProvince_order_Abitanti();
		List<String> SigleProvinceDisponibili = TerritorioUtil.ProvinceAutistiDisponibili_SiglaProvincia();
		
		for(String provSigla_ite: SigleProvinceDisponibili){
			String nomeProvincia = provinceDao.getProvinciaBy_SiglaProvincia(provSigla_ite).getNomeProvincia();
			//String nomeProvincia = provinceDao.getProvinciaBy_SiglaProvincia("EN").getNomeProvincia();
			WebDriver driver = new ChromeDriver();
			driver.navigate().to("https://www.tripadvisor.it/");
			WebElement inputCitta = driver.findElement(By.className("typeahead_input"));
			System.out.println( "sendKeys: "+ nomeProvincia);
			inputCitta.sendKeys( nomeProvincia );
			driver.findElement(By.id("SUBMIT_HOTELS")).click();
			driver.navigate().refresh();
			driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.MINUTES); 
			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
					.withTimeout(1, TimeUnit.MINUTES).pollingEvery(5, TimeUnit.SECONDS)
		            .ignoring(NoSuchElementException.class).ignoring(StaleElementReferenceException.class);
			//WebElement myDynamicElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("pageNumbers"))) ;
			//WebElement myDynamicElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("pageNumbers"))) ;
			//List<WebElement> myDynamicElement = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className("pageNum"))) ;
			//List<WebElement> myDynamicElement = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("pageNum"))) ;
			//WebElement ui_pagination  = wait.until(ExpectedConditions.visibilityOfElementLocated( By.className("ui_pagination") )) ;
			//ui_pagination  = wait.until(ExpectedConditions.presenceOfElementLocated( By.className("ui_pagination") )) ;
			WebElement ui_pagination  = wait.until(ExpectedConditions.presenceOfElementLocated( By.cssSelector("div[data-trackingString='pagination_h']") )) ;
			
			System.out.println("================= PRENDO HREF ULTIMA PAGINA =================");
			int numPages = Integer.parseInt(ui_pagination.getAttribute("data-numPages"));
			System.out.println( "numPages: "+numPages );
			//List<WebElement> LinkPagesList = myDynamicElement.findElements( By.tagName("a") );
			//System.out.println("myDynamicElement.size: "+myDynamicElement.size());
			String Tab_Main = driver.getWindowHandle(); // Tab corrente
			ListaDegliHotel(driver, Tab_Main, provSigla_ite);
			
			for(int aa = 2; aa <= numPages; aa++){
				try{
					//WebElement linkPage = wait.until(ExpectedConditions.elementToBeClickable( myDynamicElement.get(aa) )) ;
					//System.out.println("linkPage numero: "+linkPage.getText());
					WebElement next = wait.until(ExpectedConditions.elementToBeClickable(By.className("next")));
					//WebElement next = wait.until(ExpectedConditions.elementToBeClickable ( By.cssSelector("a[data-page-number='"+aa+"']") )) ;
					
					JavascriptExecutor jse = (JavascriptExecutor)driver;
					jse.executeScript("arguments[0].scrollIntoView()", next); 
					System.out.println("aa: "+aa);
					try{
						Actions actions = new Actions(driver);
						actions.moveToElement(next).click().perform();
						
					}catch(StaleElementReferenceException ser){
						System.out.println("StaleElementReferenceException 111");
						Actions actions = new Actions(driver);
						actions.moveToElement(next).click().perform();
					}
					driver.navigate().refresh();
					driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.MINUTES); 
					driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
					
					ListaDegliHotel(driver, Tab_Main, provSigla_ite);

				}catch(NoSuchElementException NsE){
					System.out.println("NoSuchElementException");
				}catch(StaleElementReferenceException ser){
					System.out.println("StaleElementReferenceException 222");
				}catch(Exception exc){
					System.out.println("Exception");
					exc.printStackTrace();
				}
			}
			
			driver.quit();
			
			System.out.println("================= FINE SCRAPING =================");
				
		}
	}


	private static List<WebElement> FaiLaRicercaHotel(WebDriver driver){
		
		
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
	            .withTimeout(5, TimeUnit.MINUTES).pollingEvery(5, TimeUnit.SECONDS)
	            .ignoring(NoSuchElementException.class).ignoring(StaleElementReferenceException.class);

		List<WebElement> myDynamicElement = wait.until(ExpectedConditions
				.presenceOfAllElementsLocatedBy( By.cssSelector("a[data-clickSource='HotelName']") ));
		
		return myDynamicElement;
	}
	
	
	private static void ListaDegliHotel(WebDriver driver, String Tab_Main, String SiglaProvincia){
		System.out.println("================= LISTA DEGLI HOTEL =================");

		
		List<WebElement> myDynamicElementAAA = FaiLaRicercaHotel(driver);
		int CONTA = 0;
		while(true){
			for(int aa = 0; aa < myDynamicElementAAA.size(); aa++){
				try{
					System.out.println(myDynamicElementAAA.get(aa).getAttribute("href"));
					
					WebElement hotel = myDynamicElementAAA.get(aa);
					String selectLinkOpeninNewTab = Keys.chord(Keys.CONTROL,Keys.RETURN); 
					hotel.sendKeys(selectLinkOpeninNewTab);
					for(String tab: driver.getWindowHandles()) {
						if(!tab.equals(Tab_Main)){
							driver.switchTo().window(tab);
							PrendiInformazioniHotel_e_SalvaDB(driver, SiglaProvincia);
							driver.switchTo().window(tab).close();
							driver.switchTo().window(Tab_Main);
						}else{
							driver.switchTo().window(Tab_Main);
						}
					}
				}catch(StaleElementReferenceException ser){
					System.out.println("StaleElementReferenceException ListaDegliHotel 111");
					myDynamicElementAAA = FaiLaRicercaHotel(driver);
				}
			}
			break;
		}
		
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
	            .withTimeout(5, TimeUnit.MINUTES).pollingEvery(5, TimeUnit.SECONDS)
	            .ignoring(NoSuchElementException.class).ignoring(StaleElementReferenceException.class);
		Wait<WebDriver> wait2 = new FluentWait<WebDriver>(driver)
	            .withTimeout(2, TimeUnit.SECONDS).pollingEvery(5, TimeUnit.SECONDS)
	            .ignoring(NoSuchElementException.class).ignoring(StaleElementReferenceException.class);
		
		//WebElement next = wait.until(ExpectedConditions.elementToBeClickable(By.className("next")));
		//JavascriptExecutor jse = (JavascriptExecutor)driver;
		//jse.executeScript("arguments[0].scrollIntoView()", next); 
		
		//List<WebElement> myDynamicElement = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className("property_title")));
		//List<WebElement> myDynamicElement = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("property_title")));
		List<WebElement> myDynamicElement = wait.until(ExpectedConditions
				.presenceOfAllElementsLocatedBy( By.cssSelector("a[data-clickSource='HotelName']") ));
		//List<WebElement> myDynamicElement = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy( By.cssSelector("a[data-clickSource='HotelName']") ));

		
		//List<WebElement> myDynamicElement = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className("listing_title")));
		//List<WebElement> myDynamicElement = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className("review_count")));
		//List<WebElement> divListLinkHotel = driver.findElements(By.className("meta_listing"));	
		System.out.println("myDynamicElement.size: "+myDynamicElement.size());
		for(int aa = 0; aa < myDynamicElement.size(); aa++){
			try{
				
				//WebElement Element = wait2.until(ExpectedConditions.elementToBeClickable(myDynamicElement.get(aa)));
				//System.out.println("---> "+ myDynamicElement.get(aa).getAttribute("href") );
				//WebElement AAA = wait2.until(ExpectedConditions.elementToBeClickable(myDynamicElement.get(aa)));
				
				/*
				//WebElement hotel = wait2.until(ExpectedConditions.elementToBeClickable( myDynamicElement.get(aa) ));
				JavascriptExecutor jse = (JavascriptExecutor)driver;
				jse.executeScript("arguments[0].scrollIntoView()", myDynamicElement.get(aa)); 
				try{
					Actions actions = new Actions(driver);
					actions.moveToElement(myDynamicElement.get(aa)).click().perform();
				
				}catch(StaleElementReferenceException ser){
					System.out.println("StaleElementReferenceException ListaDegliHotel");
					Actions actions = new Actions(driver);
					actions.moveToElement(myDynamicElement.get(aa)).click().perform();
				}
				*/
				//hotel.click();
				//myDynamicElement.get(aa).click();
				try{
					

				}catch(StaleElementReferenceException ser){
					System.out.println("StaleElementReferenceException ListaDegliHotel 111: ");
					ser.printStackTrace();
				}
				
				//System.out.println( hotel.getAttribute("href") );
				/*
				String selectLinkOpeninNewTab = Keys.chord(Keys.CONTROL,Keys.RETURN); 
				hotel.sendKeys(selectLinkOpeninNewTab);
				for(String tab: driver.getWindowHandles()) {
					if(!tab.equals(Tab_Main)){
						driver.switchTo().window(tab);
						PrendiInformazioniHotel_e_SalvaDB(driver, SiglaProvincia);
						driver.switchTo().window(tab).close();
						driver.switchTo().window(Tab_Main);
					}else{
						driver.switchTo().window(Tab_Main);
					}
				}
				*/
				
				
			}catch(StaleElementReferenceException ser){
				System.out.println("StaleElementReferenceException ListaDegliHotel 222");
			}catch(TimeoutException time){
				System.out.println("TimeoutException ListaDegliHotel");
			}catch(WebDriverException NsE){
				System.out.println("WebDriverException ListaDegliHotel");
				NsE.printStackTrace();
			}
		}
	}
	
	
	private static void PrendiInformazioniHotel_e_SalvaDB(WebDriver driver, String SiglaProvincia){
		try{
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
		            .withTimeout(5, TimeUnit.MINUTES).pollingEvery(5, TimeUnit.SECONDS);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("email"))); // controlla se esiste l'elemento altrimenti va in errore
			List<WebElement> myDynamicElement = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("email")));
			if(myDynamicElement != null && myDynamicElement.size() > 0 ){
				System.out.println("myDynamicElement.size(): "+myDynamicElement.size());
				for(int aa = 0; aa < myDynamicElement.size(); aa++){
					try{
						String onclickEmail = myDynamicElement.get(aa).getAttribute("onclick");
						if( onclickEmail != null && onclickEmail.contains("checkEmailAction")  ){
							onclickEmail = onclickEmail.split("checkEmailAction")[1];
							String parts[] = onclickEmail.split(",");
							onclickEmail = parts[0] + parts[1] + parts[2];
							onclickEmail = onclickEmail.replace(" ", "").replace("'", "").replace("(", "");
							System.out.println("email hotel: "+onclickEmail );
							WebElement titoloHotel = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("HEADING")));
							System.out.println("titoloHotel: "+titoloHotel.getText());
							WebElement streetAddress = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("street-address")));
							System.out.println("streetAddress: "+streetAddress.getText()+" ("+SiglaProvincia+")");
							try{
								AgenzieViaggioBit hotel = new AgenzieViaggioBit();
								hotel.setParametriSconto( CatturaEmailMarketing_UTIL.DammiTokenScontoEmailUnivoco() );
								hotel.setSitoWebScraping("www.tripadvisor.it");
								hotel.setEmail( onclickEmail );
								hotel.setNome( titoloHotel.getText() );
								hotel.setCitta_e_indirizzo( streetAddress.getText()+" ("+SiglaProvincia+")" );
								agenzieViaggioBitDao.saveAgenzieViaggioBit(hotel);
								System.out.println("..Hotel Salvato");
							}catch (final DataIntegrityViolationException dive) {
					    		System.out.println(dive.getMessage());
							}
						}
					}catch(NoSuchElementException NsE){
						System.out.println("NoSuchElementException PARTICOLARE");
					}
				}
			}
		}catch(NoSuchElementException aiobE){
			System.out.println("NoSuchElementException GENERAL");
		}
	}
	
}
