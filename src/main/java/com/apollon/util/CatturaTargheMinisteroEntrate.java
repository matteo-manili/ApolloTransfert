package com.apollon.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
/*
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
*/

import com.apollon.dao.AutoveicoloDao;
import com.apollon.model.Autoveicolo;
import com.apollon.webapp.util.ApplicationUtils;
/*
import com.deathbycaptcha.Captcha;
import com.deathbycaptcha.Exception;
import com.deathbycaptcha.SocketClient;
*/
/**
 * @author Matteo - matteo.manili@gmail.com
 *	
 *	Questa Classe mi prende automaticamente velocemente i dati delle auto relative alla targa nel ministero delle entratre passandogli una lista di Targhe.
 *	Sopratutto è stata pensata per risalire all'anno di immatricolazione.
 *	pagina agienzia entrate: https://www1.agenziaentrate.gov.it/servizi/bollo/calcolo/RichiestaPagamentoSemplice.htm
 *
 *	vedere documentazione: http://www.seleniumhq.org/docs/03_webdriver.jsp
 *	e scaricare: chromedriver.exe per simulare il browser https://sites.google.com/a/chromium.org/chromedriver/downloads
 *
 *	Utilizzo il lettore immagini captcha del servizio a pagamento deathbycaptcha
 *	le Credenziale di deathbycaptcha sono user: sexpistolsroma passw: giulia
 *	documentazione captcha: http://www.deathbycaptcha.com/user/captcha/1742177430
 *	e scaricare: chromedriver.exe per simulare il browser
 *
 *	Per farlo partire scrivere nel main: CatturaTargheMinisteroEntrate.CatturaDati_by_Targa(autoveicoliList);
 *
 */
public class CatturaTargheMinisteroEntrate extends ApplicationUtils {

	public static AutoveicoloDao autoveicoloDao = (AutoveicoloDao) contextDao.getBean("AutoveicoloDao");
	/*
	public static void CatturaDati_by_Targa(List<Autoveicolo> autoveicoliList) {
		try {
			System.setProperty("webdriver.chrome.driver", "C:/chromedriver.exe");
			WebDriver driver = new ChromeDriver();
			final String INFO_MINISTERO_ASSENTI = "INFO-MINISTERO-ASSENTI";
			
			for(Autoveicolo autoveicolo_ite: autoveicoliList){
				if( autoveicolo_ite.getAnnoImmatricolazione() == null ){
					if(autoveicolo_ite.getInfo() == null || autoveicolo_ite.getInfo() != null && !autoveicolo_ite.getInfo().equals(INFO_MINISTERO_ASSENTI) ){
						driver.get("https://www1.agenziaentrate.gov.it/servizi/bollo/calcolo/RichiestaPagamentoSemplice.htm");
						
						System.out.println("---------------- AUTO "+ autoveicolo_ite.getId() +" ----------------------------------");
						
						TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
						byte[] data = takesScreenshot.getScreenshotAs(OutputType.BYTES);
						//BufferedImage imageScreen = ImageIO.read(new ByteArrayInputStream(data));
				
						//ImageIcon aa = new ImageIcon(ImageIO.read(new ByteArrayInputStream(data)));
						//JOptionPane.showMessageDialog(null, aa, "Captcha image", JOptionPane.PLAIN_MESSAGE);
						
						BufferedImage imageScreen = ImageIO.read(new ByteArrayInputStream(data));
						BufferedImage dest = imageScreen.getSubimage(194, 755, 200, 100);
						
						//ImageIcon bb = new ImageIcon(dest);
						//JOptionPane.showMessageDialog(null, bb, "Captcha image", JOptionPane.PLAIN_MESSAGE);
								
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						ImageIO.write(dest, "jpg", baos);
						byte[] bytes = baos.toByteArray();
						
						
						//FileUtils.writeByteArrayToFile(new File("C:\\aaa_test\\aaacapcha.jpg"), bytes);
						//LeggiCaptcha(bytes);
						
						ByteArrayOutputStream os = new ByteArrayOutputStream();
						ImageIO.write(dest, "png", os);
						SocketClient client = new SocketClient("sexpistolsroma", "giulia");
						Captcha res = client.decode( bytes );
						
						if(res != null){
							System.out.println("CAPCHA TEXT: "+res.text);
							
							WebElement targa = driver.findElement(By.id("targ"));
							targa.sendKeys( autoveicolo_ite.getTarga() );
							
							WebElement codiceSicurezza = driver.findElement(By.id("inCaptchaChars"));
							codiceSicurezza.sendKeys( res.text );
							
							driver.findElement(By.name("procedi")).submit();
							
							List<WebElement> aaa = driver.findElements(By.tagName("p"));
							String annoImmatric = null;
							String informazioni = "";
							boolean infoBool = false;
							for (int a = 0; a < aaa.size(); a++) {
								if( aaa.get(a).getText().contains("Posti:") ){
									String aa = aaa.get(a).findElement(By.tagName("span")).getText();
									System.out.println("Posti: "+aa);
									informazioni = informazioni + " Posti: "+aa;
								}
								if( aaa.get(a).getText().contains("Codice uso:") ){
									String aa = aaa.get(a).findElement(By.tagName("span")).getText();
									System.out.println("Codice uso: "+aa);
									informazioni = informazioni + " Codice uso: "+aa;
								}
								if( aaa.get(a).getText().contains("Cilindrata:") ){
									String aa = aaa.get(a).findElement(By.tagName("span")).getText();
									System.out.println("Cilindrata: "+aa);
									informazioni = informazioni + " Cilindrata: "+aa;
								}
								if( aaa.get(a).getText().contains("Data immatricolazione:") ){
									String aa = aaa.get(a).findElement(By.tagName("span")).getText();
									System.out.println("Data immatricolazione: "+aa);
									annoImmatric = aa;
									infoBool = true;
								}
							}
							
							Autoveicolo auto = autoveicoloDao.get( autoveicolo_ite.getId() );
							auto.setAnnoImmatricolazione( annoImmatric );
							autoveicoloDao.saveAutoveicolo(auto);
						}
					}
				}
			}
			driver.quit();

			
		} catch (IOException e) {
			System.out.println("IOException e");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Exception e");
			e.printStackTrace();
		} catch (InterruptedException e) {
			System.out.println("InterruptedException e");
			e.printStackTrace();
		}
	}
	*/
	

	
	
	
	
	
}
