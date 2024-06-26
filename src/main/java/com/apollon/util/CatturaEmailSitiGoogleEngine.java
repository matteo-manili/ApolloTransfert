package com.apollon.util;

import java.io.IOException;
import java.net.ConnectException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.validator.routines.EmailValidator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
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
public class CatturaEmailSitiGoogleEngine extends CatturaEmailMarketing_UTIL implements Runnable {

	public static AgenzieViaggioBitDao agenzieViaggioBitDao = (AgenzieViaggioBitDao) contextDao.getBean("AgenzieViaggioBitDao");
	public static ProvinceDao provinceDao = (ProvinceDao) contextDao.getBean("ProvinceDao");

	private static final String ExcludeParoleChiavi = "+-paginegialle+-paginebianche+-indeed+-infojobs+-linkedin";
	private String parolaChiave_ite;
	private Integer MaxNumeroLinkGoogle;
	private List<String> ListParoleChiave;
	
	@Override
	public void run() {
		try {
			Start(parolaChiave_ite, MaxNumeroLinkGoogle);
		} catch (NullPointerException | IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public CatturaEmailSitiGoogleEngine(String parolaChiave_ite, Integer MaxNumeroLinkGoogle, List<String> listParoleChiave) {
		this.parolaChiave_ite = parolaChiave_ite;
		this.MaxNumeroLinkGoogle = MaxNumeroLinkGoogle;
		this.ListParoleChiave = listParoleChiave;
	}


	/**
	 * Se Passo null e null utilizza i valori de default
	 * Vedere per Esecuzione Thread Multipo:
	 * https://crunchify.com/how-to-run-multiple-threads-concurrently-in-java-executorservice-approach/
	 */
	public static void StartScrapingMultiplo(Integer NumeroFinestreRicercheContemporanee, Integer MaxNumeroLinkGoogle){
		System.out.println("ORA INIZIO: "+new Date());
		
		List<String> ListParoleChiave_Scelte = new ArrayList<String>();
		ListParoleChiave_Scelte.add("agenzia di viaggi");
		
		/*
		ListParoleChiave_Scelte.add("pharmaceuticals");
		ListParoleChiave_Scelte.add("azienda-farmaceutica");
		
		ListParoleChiave_Scelte.add("hotel+5+stelle");
		ListParoleChiave_Scelte.add("chirurgia+privata");
		ListParoleChiave_Scelte.add("clinica+privata");
		ListParoleChiave_Scelte.add("ospedale+privato");
		ListParoleChiave_Scelte.add("centro+congressi");
		*/
		
		ExecutorService executorService = Executors.newFixedThreadPool(NumeroFinestreRicercheContemporanee);
		for( String ite: ListParoleChiave_Scelte ){
			Runnable worker = new CatturaEmailSitiGoogleEngine(ite, MaxNumeroLinkGoogle, null);
			executorService.execute(worker);
		}
		executorService.shutdown();
		// Wait until all threads are finish
		while (!executorService.isTerminated()) {
			
		}
		System.out.println("\nFinished all threads");
		System.out.println("ORA FINE: "+new Date());
	}
	
	private static List<WebElement> PrendiElementiClassName_rc(WebDriver driver) throws TimeoutException{
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
	            .withTimeout(5, TimeUnit.SECONDS).pollingEvery(2, TimeUnit.SECONDS)
	            .ignoring(NoSuchElementException.class).ignoring(StaleElementReferenceException.class);
		List<WebElement> webElement = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy( By.className("rc") ));
        return webElement;
	}
	
	/** Start */
	public static void Start(String parolaChiave_ite, Integer MaxNumeroLinkGoogle) throws ConnectException, IOException, InterruptedException {
		//System.setProperty("webdriver.gecko.driver", "C:/geckodriver.exe");
		//WebDriver driver = new  FirefoxDriver();
		System.setProperty("webdriver.chrome.driver", "C:/chromedriver.exe");
		WebDriver driver = new  ChromeDriver();
		
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date date = new Date();
		System.out.println("INIZIO: "+dateFormat.format(date));
		
		//for(String parolaChiave_ite: ListParoleChiave){
			List<String> SigleProvinceDisponibili = TerritorioUtil.ProvinceAutistiDisponibili_SiglaProvincia();
			//List<String> SigleProvinceDisponibili = new ArrayList<String>();
			//SigleProvinceDisponibili.add("VA");
			//SigleProvinceDisponibili.add("PZ");
			//SigleProvinceDisponibili.add("RE");
			
			for(String provSigla_ite: SigleProvinceDisponibili){
				String nomeProvincia = provinceDao.getProvinciaBy_SiglaProvincia(provSigla_ite).getNomeProvincia();
				String ParolaChiave = parolaChiave_ite+ "+" +nomeProvincia + ExcludeParoleChiavi;
				
				for(int numLinkGoogle = 0; numLinkGoogle <= MaxNumeroLinkGoogle; numLinkGoogle = numLinkGoogle + 10){
					try{
						String Tab_Main = driver.getWindowHandle(); // Tab corrente
						if(numLinkGoogle > 0) {
							driver.navigate().to( "https://www.google.it/search?q="+ParolaChiave+"&start="+Integer.toString(numLinkGoogle)  );
						}else {
							driver.navigate().to( "https://www.google.it/search?q="+ParolaChiave);
						}
						System.out.println("--------------- RicercaGoogle: "+ParolaChiave+" | "+numLinkGoogle );
						List<WebElement> listWebElement_CITE = PrendiElementiClassName_rc(driver);
						for(WebElement ite: listWebElement_CITE) {
							try{
								WebElement primoLink = ite.findElements(By.tagName("a")).get(0);
								String urlSito = primoLink.getAttribute("href");
								System.out.println("URL: "+urlSito);
								String selectLinkOpeninNewTab = Keys.chord(Keys.CONTROL,Keys.RETURN); 
								primoLink.sendKeys(selectLinkOpeninNewTab);
								// La navigazione va veloce e quando succede (raramente) che il link google scarica un file (invece di andare su un sito) 
								// il tab viene aperto e subito richiusto ma il driver.getWindowHandles() legge due tab aperti invece che solo il principale
								// così da provocare un NoSuchWindowException. La soluzione e fare aspettare 2/3 secondi prima di fare il driver.getWindowHandles().
								Thread.sleep(2000);  
								for(String tab: driver.getWindowHandles()) {
									if(!tab.equals(Tab_Main)){
										driver.switchTo().window(tab);
										try {
											SalvaSito(driver, "", urlSito, provSigla_ite, ParolaChiave);
										}catch(Exception exc){
											System.out.println("Exception SalvaSito "+urlSito +" | "+ParolaChiave +" "+numLinkGoogle);
											exc.printStackTrace();
										}

										driver.switchTo().window(tab).close();
										driver.switchTo().window(Tab_Main);
									}else{
										driver.switchTo().window(Tab_Main);
									}
								}
							}catch(NoSuchWindowException NswE){
								System.out.println("NoSuchWindowException primoLink "+ParolaChiave +" "+numLinkGoogle);
							}catch(NoSuchElementException NsE){
								System.out.println("NoSuchElementException primoLink "+ParolaChiave +" "+numLinkGoogle);
							}catch(StaleElementReferenceException ser){
								System.out.println("StaleElementReferenceException primoLink "+ParolaChiave +" "+numLinkGoogle);
							}catch(TimeoutException NsE){
								System.out.println("TimeoutException primoLink "+ParolaChiave +" "+numLinkGoogle);
							}catch(Exception exc){
								System.out.println("Exception primoLink "+ParolaChiave +" "+numLinkGoogle);
								exc.printStackTrace();
							}
						}
					}catch(NoSuchWindowException exc){
						System.out.println("NoSuchWindowException Ricerca google "+ParolaChiave +" "+numLinkGoogle);
						exc.printStackTrace();
						break; // ESCO DAL CICLO !!! (NON FACCIO RIFARE TANTE CHIAMATE CON RISPOSTA NEGATIVA A GOOGLE)
					}catch(TimeoutException exc){
						System.out.println("TimeoutException Ricerca google "+ParolaChiave +" "+numLinkGoogle);
						exc.printStackTrace();
						break; // ESCO DAL CICLO !!! (NON FACCIO RIFARE TANTE CHIAMATE CON RISPOSTA NEGATIVA A GOOGLE)
					}catch(Exception exc){
						System.out.println("Exception Ricerca google "+ParolaChiave +" "+numLinkGoogle);
						exc.printStackTrace();
					}
				}
			}
		//}
		date = new Date();
		System.out.println("FINE: "+dateFormat.format(date));
		driver.quit();
	}


	/**
	 * SalvaSitoGoogle
	 */
	private static void SalvaSito(WebDriver driver, String Titolo, String urlSito, String SiglaProvincia, String ParoleChiaviGoogle) 
			throws NoSuchWindowException, Exception{
		try{
			//int a = Integer.parseInt("ssss"); // provoco eccezzione
			System.out.println("LAVORO IL SITO");
			String EmailSito = "";
			Document doc = Jsoup.parse(driver.getPageSource());
			Elements links = doc.select("a[href]");
			System.out.println(driver.getTitle());
			Titolo = doc.title();
			if(EmailSito.equals("")){
				for(Element link : links) {
					if( (link.attr("href").contains("contatt") || link.attr("href").contains("contact") || link.attr("href").contains("urp")) 
							&& !link.attr("href").contains("@") ){
						System.out.println("VADO ALLA PAGINA CONTATTI: "+link.attr("href") );
						String pageLinkContatti = link.attr("href");
						if( !link.attr("href").contains("http") ){ // nel caso è un link relativo, aggiungo alla home il link contatti
							pageLinkContatti = urlSito + "/" + link.attr("href");
							pageLinkContatti.replace("//", "/"); 
						}
						driver.navigate().to( pageLinkContatti );
						doc = Jsoup.parse(driver.getPageSource());
						links = doc.select("a[href]");
						EmailSito = CercaEmailSourcePage(links, EmailSito);
						if(EmailSito.equals("")){
							EmailSito = CercaEmailSourcePage_SUPER(driver.getPageSource(), EmailSito);
						}
						break;
					}
				}
			}
			if(EmailSito.equals("")){
				driver.navigate().back();
				doc = Jsoup.parse(driver.getPageSource());
				links = doc.select("a[href]");
				EmailSito = CercaEmailSourcePage(links, EmailSito);
				if(EmailSito.equals("")){
					EmailSito = CercaEmailSourcePage_SUPER(driver.getPageSource(), EmailSito);
				}
			}
			
			if(!EmailSito.equals("")){
				try{
					AgenzieViaggioBit hotel = new AgenzieViaggioBit();
					hotel.setParametriSconto( DammiTokenScontoEmailUnivoco() );
					hotel.setSitoWebScraping("www.google.it | "+ParoleChiaviGoogle);
					hotel.setEmail( PulisciEmail(EmailSito) );
					hotel.setNome( Titolo );
					hotel.setCitta_e_indirizzo( "("+SiglaProvincia+") " );
					hotel.setSitoWeb( urlSito);
					agenzieViaggioBitDao.saveAgenzieViaggioBit(hotel);
					System.out.println(".. Salvato Sito Google");
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
	
	private static String CercaEmailSourcePage_SUPER(String bodiText, String EmailSito) throws Exception {
		EmailValidator validator = EmailValidator.getInstance();
		//System.out.println( bodiText );
		if(EmailSito.equals("")){
			String RegexEmail = "[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+";
			Matcher m = Pattern.compile( RegexEmail ).matcher( bodiText );
		    while(m.find()) {
				EmailSito = PulisciEmail(m.group());
				if( validator.isValid(EmailSito) ){
					break;
				}else{
					EmailSito = "";
				}
		    }
		}
		return EmailSito;
	}
	
	private static String CercaEmailSourcePage(Elements links, String EmailSito) throws Exception {
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
	private static String RimuoviEventualeSubject(String EmailSito) throws Exception {
		if(EmailSito.contains("?")){
			int startIndex = EmailSito.indexOf("?");
			EmailSito = EmailSito.substring(0, startIndex);
		}
		return EmailSito;
	}
	
	private static String PulisciEmail(String email) throws Exception {
		email = UtilString.RimuoviCaratteriNonVisualizzabili(email);
		email = UtilString.RimuoviCaratteriIllegaliFileName(email);
		email = UtilString.RimuoviTuttiGliSpazi(email);
		return email;
	}

	
	
	
	

}
