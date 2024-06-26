package com.apollon.util;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.validator.routines.EmailValidator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.springframework.dao.DataIntegrityViolationException;

import com.apollon.dao.AgenzieViaggioBitDao;
import com.apollon.dao.ProvinceDao;
import com.apollon.model.AgenzieViaggioBit;
import com.apollon.webapp.util.ApplicationUtils;
import com.apollon.webapp.util.TerritorioUtil;
import com.apollon.webapp.util.geogoogle.GMaps_Api;
import com.apollon.webapp.util.geogoogle.RicercaTransfert_GoogleMaps_Info;
import com.google.common.base.CharMatcher;

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
 * Per Errore StaleElementReferenceException, vedere: https://www.seleniumhq.org/exceptions/stale_element_reference.jsp
 * 
 * Per The element is not visible to click. VEDERE: https://stackoverflow.com/questions/11908249/debugging-element-is-not-clickable-at-point-error
 * 
 * Quando viene fuori errore Element is not clickable at point (36, 72). Other element would receive the click:
 * vedere soluzione https://stackoverflow.com/questions/44912203/selenium-web-driver-java-element-is-not-clickable-at-point-36-72-other-el/44916498
 * e fare:
 * Actions actions = new Actions(driver);
 * actions.moveToElement( driver.findElement(By.id( idStrutture )) ).click().build().perform();
 *
 */
public class CatturaTripAdvisor extends ApplicationUtils implements Runnable {

	public static AgenzieViaggioBitDao agenzieViaggioBitDao = (AgenzieViaggioBitDao) contextDao.getBean("AgenzieViaggioBitDao");
	public static ProvinceDao provinceDao = (ProvinceDao) contextDao.getBean("ProvinceDao");
	private String SiglaProvincia = ""; 
	private Integer MaxNumeroPaginePerProvincia = 1;
	
	/**
	 * Non fare partire piu di 4 thread Contermporaneamente (altrimenti si impiccia) 
	 */
	final private static int NumeroRicerceContemporaneeDefault = 5; 
	/**
	 * massimo fare 60 pagine perché otre non si trovano più email (nel caso di Roma dove ci sono più hotel)
	 * Di media una provincia ha 20 pagine, ma roma ne ha oltre 200 pagine
	 */
	final private static int MaxNumeroPaginePerProvinciaDefault = 60; 
	
	public CatturaTripAdvisor(String SiglaProvincia, Integer MaxNumeroPaginePerProvincia) {
		this.SiglaProvincia = SiglaProvincia;
		this.MaxNumeroPaginePerProvincia = MaxNumeroPaginePerProvincia;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			Start(SiglaProvincia, MaxNumeroPaginePerProvincia);
		} catch (NullPointerException | IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Se Passo null e null utilizza i valori de default
	 * Vedere per Esecuzione Thread Multipo:
	 * https://crunchify.com/how-to-run-multiple-threads-concurrently-in-java-executorservice-approach/
	 */
	public static void StartScrapingMultiploProvinceDisponibili(Integer NumeroRicerceContemporanee, Integer MaxNumeroPaginePerProvincia){
		System.out.println("ORA INIZIO: "+new Date());
		if(NumeroRicerceContemporanee == null){
			NumeroRicerceContemporanee = NumeroRicerceContemporaneeDefault;
		}
		ExecutorService executorService = Executors.newFixedThreadPool(NumeroRicerceContemporanee);
		for( String ite: TerritorioUtil.ProvinceAutistiDisponibili_SiglaProvincia() ){
			Runnable worker = new CatturaTripAdvisor(ite, MaxNumeroPaginePerProvincia);
			executorService.execute(worker);
		}
		executorService.shutdown();
		// Wait until all threads are finish
		while (!executorService.isTerminated()) {
			
		}
		System.out.println("\nFinished all threads");
		System.out.println("ORA FINE: "+new Date());
	}
	/**
	 * Start
	 */
	public static void Start(String SiglaProvincia, Integer MaxNumeroPaginePerProvincia) throws ConnectException, IOException, InterruptedException {
		List<String> SigleProvinceDisponibili = new ArrayList<String>();
		if(SiglaProvincia != null && !SiglaProvincia.contentEquals("")){
			SigleProvinceDisponibili.add( SiglaProvincia );
		}else{
			SigleProvinceDisponibili = TerritorioUtil.ProvinceAutistiDisponibili_SiglaProvincia();
		}
		if(MaxNumeroPaginePerProvincia == null){
			MaxNumeroPaginePerProvincia = MaxNumeroPaginePerProvinciaDefault;
		}
		System.setProperty("webdriver.chrome.driver", "C:/chromedriver.exe");
		for(String provSigla_ite: SigleProvinceDisponibili){
			String nomeProvincia = provinceDao.getProvinciaBy_SiglaProvincia(provSigla_ite).getNomeProvincia();
			//String nomeProvincia = provinceDao.getProvinciaBy_SiglaProvincia("EN").getNomeProvincia();
			WebDriver driver = new ChromeDriver();
			driver.navigate().to("https://www.tripadvisor.it/Hotels");
			WebElement inputCitta = driver.findElement(By.className("typeahead_input"));
			System.out.println( "sendKeys: "+ nomeProvincia);
			inputCitta.sendKeys( nomeProvincia );
			driver.findElement(By.id("SUBMIT_HOTELS")).click();
			driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.MINUTES);
			Wait<WebDriver> waitNumeroPagine = new FluentWait<WebDriver>(driver)
					.withTimeout(3, TimeUnit.MINUTES).pollingEvery(5, TimeUnit.SECONDS)
					.ignoring(NoSuchElementException.class);
			System.out.println("================= PRENDO NUMERO PAGINE "+nomeProvincia+" =================");
			int NumeroPagine = 1;
			try{
				WebElement ui_pagination  = waitNumeroPagine.until(ExpectedConditions.presenceOfElementLocated( By.cssSelector("div[data-trackingString='pagination_h']") )) ;
				NumeroPagine = Integer.parseInt(ui_pagination.getAttribute("data-numPages"));
			}catch(StaleElementReferenceException ser){
				System.out.println("StaleElementReferenceException NumeroPagine");
			}catch(TimeoutException time){
				System.out.println("TimeoutException NumeroPagine");
			}
			System.out.println("Numero Pagine: "+NumeroPagine );
			String Tab_Main = driver.getWindowHandle(); // Tab corrente
			ListaDegliHotel(driver, Tab_Main, provSigla_ite);
			Wait<WebDriver> waitNext = new FluentWait<WebDriver>(driver)
					.withTimeout(1, TimeUnit.MINUTES).pollingEvery(5, TimeUnit.SECONDS)
		            .ignoring(NoSuchElementException.class).ignoring(StaleElementReferenceException.class);
			for(int aa = 2; aa <= NumeroPagine && aa <= MaxNumeroPaginePerProvincia; aa++){ 
				try{
					WebElement next = waitNext.until(ExpectedConditions.elementToBeClickable(By.className("next")));
					JavascriptExecutor jse = (JavascriptExecutor)driver;
					jse.executeScript("arguments[0].scrollIntoView()", next);
					System.out.println("Pagina: "+aa);
					try{
						Actions actions = new Actions(driver);
						actions.moveToElement(next).click().perform();
					}catch(StaleElementReferenceException ser){
						System.out.println("StaleElementReferenceException 111");
						Actions actions = new Actions(driver);
						actions.moveToElement(next).click().perform();
						driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.MINUTES);
					}
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
			System.out.println("================= FINE SCRAPING "+nomeProvincia+" =================");
		}
	}

	private static List<WebElement> FaiLaRicercaListaHotel(WebDriver driver) throws TimeoutException{
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
	            .withTimeout(3, TimeUnit.MINUTES).pollingEvery(5, TimeUnit.SECONDS)
	            .ignoring(NoSuchElementException.class).ignoring(StaleElementReferenceException.class);
        List<WebElement> aa = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy( By.cssSelector("a[data-clickSource='HotelName']") ));
        //System.out.println("visibilityOfAllElementsLocatedBy");
        //List<WebElement> aa = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy( By.cssSelector("a[data-clickSource='HotelName']") ));
        //System.out.println("presenceOfAllElementsLocatedBy");
        return aa;
	}
	
	private static WebElement FaiLaRicercaSingoloHotel(WebDriver driver, WebElement webElement) throws TimeoutException{
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
	            .withTimeout(1, TimeUnit.MINUTES).pollingEvery(5, TimeUnit.SECONDS)
	            .ignoring(NoSuchElementException.class).ignoring(StaleElementReferenceException.class);
        WebElement elementHotel = wait.until(ExpectedConditions.elementToBeClickable(webElement));
        //System.out.println("visibilityOf");
        return elementHotel;
	}
	/**
	 * ListaDegliHotel
	 */
	private static void ListaDegliHotel(WebDriver driver, String Tab_Main, String SiglaProvincia){
		try{
			System.out.println("================= LISTA DEGLI HOTEL "+SiglaProvincia+" =================");
			while(true){
				List<WebElement> myDynamicElement = FaiLaRicercaListaHotel(driver);
				System.out.println( "myDynamicElement.size: "+myDynamicElement.size() );
				Iterator<WebElement> myDynamicElementIterator = myDynamicElement.iterator();
				while(myDynamicElementIterator.hasNext()){
					try{
						WebElement webElemnt = FaiLaRicercaSingoloHotel(driver, myDynamicElementIterator.next());
						System.out.println("HOTEL: "+webElemnt.getText());
						Actions actions = new Actions(driver);
						actions.moveToElement( webElemnt ).click().perform();
						for(String tab: driver.getWindowHandles()) {
							if(!tab.equals(Tab_Main)){
								driver.switchTo().window(tab);
								PrendiInformazioniHotel_e_SalvaDB(driver, SiglaProvincia, Tab_Main, tab);
								driver.switchTo().window(tab).close();
								driver.switchTo().window(Tab_Main);
							}else{
								driver.switchTo().window(Tab_Main);
							}
						}
					}catch(NoSuchElementException NsE){
						System.out.println("NoSuchElementException ListaDegliHotel "+SiglaProvincia);
						myDynamicElementIterator = FaiLaRicercaListaHotel(driver).iterator();
						//elementHotel = FaiLaRicercaSingoloHotel(driver, myDynamicElement.get(aa));
					}catch(StaleElementReferenceException ser){
						System.out.println("StaleElementReferenceException ListaDegliHotel "+SiglaProvincia);
						myDynamicElementIterator = FaiLaRicercaListaHotel(driver).iterator();
						//elementHotel = FaiLaRicercaSingoloHotel(driver, myDynamicElement.get(aa));
					}catch(TimeoutException NsE){
						System.out.println("TimeoutException ListaDegliHotel "+SiglaProvincia);
						myDynamicElementIterator = FaiLaRicercaListaHotel(driver).iterator();
					}
				}
				break;
			}
		}catch(TimeoutException NsE){
			System.out.println("TimeoutException ListaDegliHotel "+SiglaProvincia);
		}
	}
	
	private static List<WebElement> PrendiInformazioniHotelEmail(WebDriver driver){
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
	            .withTimeout(10, TimeUnit.SECONDS).pollingEvery(5, TimeUnit.SECONDS)
	            .ignoring(StaleElementReferenceException.class);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("email"))); // controlla se esiste l'elemento altrimenti va in errore
		return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("email")));
	}
	private static Wait<WebDriver> WaitPrendiInformazioniHotel(WebDriver driver){
		return new FluentWait<WebDriver>(driver)
	            .withTimeout(5, TimeUnit.SECONDS).pollingEvery(5, TimeUnit.SECONDS);
	}
	/**
	 * PrendiInformazioniHotel_e_SalvaDB
	 */
	private static void PrendiInformazioniHotel_e_SalvaDB(WebDriver driver, String SiglaProvincia, String Tab_Main, String Tab_Hotel){
		try{
			List<WebElement> myDynamicElement = PrendiInformazioniHotelEmail(driver);
			while(true){
				for(int aa = 0; aa < myDynamicElement.size(); aa++){
					try{
						String onclickEmail = myDynamicElement.get(aa).getAttribute("onclick");
						if( onclickEmail != null && onclickEmail.contains("checkEmailAction") ){
							onclickEmail = onclickEmail.split("checkEmailAction")[1];
							String parts[] = onclickEmail.split(",");
							onclickEmail = parts[0] + parts[1] + parts[2];
							onclickEmail = onclickEmail.replace(" ", "").replace("'", "").replace("(", "");
							EmailValidator validator = EmailValidator.getInstance();
							if(validator.isValid(onclickEmail)){
								System.out.println("email hotel: "+onclickEmail);
								WebElement titoloHotel = WaitPrendiInformazioniHotel(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("HEADING")));
								System.out.println(" | titoloHotel: "+titoloHotel.getText());
								WebElement streetAddress = WaitPrendiInformazioniHotel(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("street-address")));
								System.out.println(" | streetAddress: "+"("+SiglaProvincia+") "+streetAddress.getText());
								try{
									AgenzieViaggioBit hotel = new AgenzieViaggioBit();
									hotel.setParametriSconto( CatturaEmailMarketing_UTIL.DammiTokenScontoEmailUnivoco() );
									hotel.setSitoWebScraping("www.tripadvisor.it");
									hotel.setEmail( PulisciEmail(onclickEmail) );
									hotel.setNome( titoloHotel.getText() );
									hotel.setCitta_e_indirizzo( "("+SiglaProvincia+") "+streetAddress.getText() );
									agenzieViaggioBitDao.saveAgenzieViaggioBit(hotel);
									System.out.println("..Hotel Salvato");
								}catch (final DataIntegrityViolationException dive) {
						    		System.out.println(dive.getMessage());
								}
							}
						}
					}catch(NoSuchElementException NsE){
						System.out.println("NoSuchElementException HOTEL");
						myDynamicElement = PrendiInformazioniHotelEmail(driver);
					}catch(StaleElementReferenceException ser){
						System.out.println("StaleElementReferenceException HOTEL");
						myDynamicElement = PrendiInformazioniHotelEmail(driver);
					}
				}
				break;
			}
		}catch(NoSuchElementException nse){
			System.out.println("NoSuchElementException PrendiInformazioniHotel_e_SalvaDB");
			PrendiInformazioniHotel(driver, SiglaProvincia);
		}catch(TimeoutException timeExc){
			System.out.println("TimeoutException PrendiInformazioniHotel_e_SalvaDB");
		}
	}
	/**
	 * PrendiSitoViaGoogle
	 */
	private static void PrendiInformazioniHotel(WebDriver driver, String SiglaProvincia){
		int attempts = 0;
	    while(attempts < 3) {
			try {
				System.out.println("-------------------------");
				WebElement TitoloHotel = WaitPrendiInformazioniHotel(driver).until(ExpectedConditions.presenceOfElementLocated(By.id("HEADING")));
				WebElement StreetAddress = WaitPrendiInformazioniHotel(driver).until(ExpectedConditions.presenceOfElementLocated(By.className("street-address")));
				String TitoloHotelString = TitoloHotel.getText(); String StreetAddressString = StreetAddress.getText();
				RicercaTransfert_GoogleMaps_Info psg = new RicercaTransfert_GoogleMaps_Info();
				GMaps_Api GMaps_Api = new GMaps_Api();
				psg = GMaps_Api.GoogleMaps_PlaceTextSearch_Easy(TitoloHotelString +", "+SiglaProvincia, "IT");
				//psg = GMaps_Api.GoogleMaps_PlaceTextSearch("Princeps Boutique Hotel, Roma", "IT");
				//psg = GMaps_Api.GoogleMaps_PlaceTextSearch("via villadossola 7, Roma", "IT");
				//ChIJzUf9FKVhLxMR7D-KnC_K_eI
				if(psg != null && psg.getPlace_id() != null && !psg.getPlace_id().equals("")){
					psg.setPlace_id(psg.getPlace_id());
					psg = GMaps_Api.GoogleMaps_PlaceDetails(psg, "IT");
					if(psg != null && psg.getWebSite() != null && !psg.getWebSite().equals("")){
						System.out.println("WEBSITE: "+psg.getWebSite());
						System.out.println("TitoloHotel: "+TitoloHotelString);
						System.out.println("StreetAddress: "+StreetAddressString);
						SalvaSitoGoogle(driver, TitoloHotelString, psg, SiglaProvincia, StreetAddressString);
					}
				}
			}catch(NoSuchElementException NsE){
				System.out.println("NoSuchElementException PrendiInformazioniHotel");
			}catch(StaleElementReferenceException ser){
				System.out.println("StaleElementReferenceException PrendiInformazioniHotel");
			}catch(Exception e1) {
				System.out.println("Exception PrendiInformazioniHotel: "+e1.getMessage());
				e1.printStackTrace();
			}
			attempts++;
	    }
	}
	
	/**
	 * SalvaSitoGoogle
	 */
	private static void SalvaSitoGoogle(WebDriver driver, String TitoloHotel, RicercaTransfert_GoogleMaps_Info psg, String SiglaProvincia, String streetAddressHotel){
		try{
			String EmailSito = "";
			driver.navigate().to( psg.getWebSite() );
			//System.out.println(driver.getTitle());
			//System.out.println(driver.getPageSource());
			Document doc = Jsoup.parse(driver.getPageSource());
			Elements links = doc.select("a[href]");
			EmailSito = CercaEmailSourcePage(links, EmailSito);
			// cerco nella pagina Contatti
			if(EmailSito.equals("")){
				for(Element link : links) {
					if(link.attr("href").contains("contatt") || link.attr("href").contains("contact")){
						System.out.println("VADO ALLA PAGINA CONTATTI: "+link.attr("href") );
						String pageLinkContatti = link.attr("href");
						if( !link.attr("href").contains("http") ){ // nel caso è un link relativo, aggiungo alla home il link contatti
							pageLinkContatti = psg.getWebSite() + link.attr("href");
							pageLinkContatti.replace("//", "/");
						}
						driver.navigate().to( pageLinkContatti );
						//System.out.println(driver.getTitle());
						//System.out.println(driver.getPageSource());
						doc = Jsoup.parse(driver.getPageSource());
						links = doc.select("a[href]");
						EmailSito = CercaEmailSourcePage(links, EmailSito);
						break;
					}
				}
			}
			if(!EmailSito.equals("")){
				try{
					AgenzieViaggioBit hotel = new AgenzieViaggioBit();
					hotel.setParametriSconto( CatturaEmailMarketing_UTIL.DammiTokenScontoEmailUnivoco() );
					hotel.setSitoWebScraping("www.tripadvisor.it");
					hotel.setEmail( PulisciEmail(EmailSito) );
					hotel.setNome( TitoloHotel );
					hotel.setCitta_e_indirizzo( "("+SiglaProvincia+") "+streetAddressHotel );
					hotel.setSitoWeb( psg.getWebSite() );
					agenzieViaggioBitDao.saveAgenzieViaggioBit(hotel);
					System.out.println("..Hotel Salvato api Google");
				}catch (final DataIntegrityViolationException dive) {
		    		System.out.println("DataIntegrityViolationException SalvaSitoGoogle "+dive.getMessage());
				}
			}else{
				System.out.println("EmailSito NON TROVATA");
			}
		}catch(NoSuchElementException NsE){
			System.out.println("NoSuchElementException SalvaSitoGoogle");
		}catch(StaleElementReferenceException ser){
			System.out.println("StaleElementReferenceException SalvaSitoGoogle");
		}catch(TimeoutException time){
			System.out.println("TimeoutException SalvaSitoGoogle");
		}catch(WebDriverException wde){
			System.out.println("WebDriverException SalvaSitoGoogle");
		}
	}
	
	private static String CercaEmailSourcePage(Elements links, String EmailSito){
		EmailValidator validator = EmailValidator.getInstance();
		for(Element link : links) {
			if(link.attr("href").contains("mailto:")){
				EmailSito = link.attr("href").split("mailto:")[1];
				EmailSito = RimuoviEventualeSubject(EmailSito);
				EmailSito = PulisciEmail(EmailSito);
				if( validator.isValid(EmailSito) ){
					break;
				}else{
					EmailSito = "";
				}
				
			}
		}
		if(EmailSito.equals("")){
			for(Element link : links) {
				if(link.attr("href").contains("@")){
					EmailSito = RimuoviEventualeSubject(link.attr("href"));
					EmailSito = PulisciEmail(EmailSito);
					if( validator.isValid(EmailSito) ){
						break;
					}else{
						EmailSito = "";
					}
				}
			}
		}
		return EmailSito;
	}

	/**
	 * Rimuove il Subject a destra: info@bnbpavia.com?subject=informazioni 
	 */
	private static String RimuoviEventualeSubject(String EmailSito){
		if(EmailSito.contains("?")){
			int startIndex = EmailSito.indexOf("?");
			EmailSito = EmailSito.substring(0, startIndex);
		}
		return EmailSito;
	}
	
	private static String PulisciEmail(String email){
		email = UtilString.RimuoviCaratteriNonVisualizzabili(email);
		email = UtilString.RimuoviCaratteriIllegaliFileName(email);
		email = UtilString.RimuoviTuttiGliSpazi(email);
		return email;
	}
	
	
	

}
