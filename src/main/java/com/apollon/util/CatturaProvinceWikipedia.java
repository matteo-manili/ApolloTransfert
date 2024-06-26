package com.apollon.util;


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
/*
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
*/
import org.springframework.dao.DataIntegrityViolationException;

import com.apollon.dao.ProvinceDao;
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
 */
public class CatturaProvinceWikipedia extends ApplicationUtils {

	public static ProvinceDao provinceDao = (ProvinceDao) contextDao.getBean("ProvinceDao");
	
	/*
	
	
	public static void CatturaNumeroAbitanti() {
		try {
			
			
		//	List<Province> provinceList = provinceDao.getProvince();
		//	for (Province prov_ite: provinceList) {
		//		List<Province> list = new ArrayList<Province>(prov_ite.getProvinceConfinanti());
		//		if(list == null || list.size() == 0){
		//		}
		//		for (Province prov_conf: list) {
		//			prov_conf.getNomeProvincia();
		//		}
		//	}
			
			// ------------------------------------------------------------------------------------------
			
			System.setProperty("webdriver.chrome.driver", "C:/chromedriver.exe");
			WebDriver driver = new ChromeDriver();
			List<Province> listProvince = provinceDao.getProvince();
	        for (Province prov_ite: listProvince) {
	        	driver.get("https://it.wikipedia.org/wiki/Provincia_di_"+prov_ite.getNomeProvincia());
		        System.out.println("============================== "+ prov_ite.getNomeProvincia() +" ====================================");
		        List<WebElement> tr = driver.findElements(By.tagName("tr"));
				for (int i = 0; i < tr.size(); i++) {
					if( tr.get(i).getText().contains("Abitanti") ){
						try{
							String part = tr.get(i).findElements(By.tagName("td")).get(0).getText().replace(" ", ""); ;
							System.out.println(part); String NumeroAbitanti = "";
							for (int ite = 0; ite < part.length(); ite++){
							    char c = part.charAt(ite);   
							    if(StringUtils.isNumeric( Character.toString(c) )){
							    	NumeroAbitanti += Character.toString(c);
							    }else{
							    	break;
							    }
							}
							System.out.println(NumeroAbitanti);
							if(NumeroAbitanti != null && !NumeroAbitanti.equals("") && StringUtils.isNumeric( NumeroAbitanti )){
								prov_ite.setNumeroAbitanti( Integer.parseInt( NumeroAbitanti ) );
								provinceDao.saveProvince( prov_ite );
								System.out.println("[OK SALVATO]" );
							}else{
								System.out.println("[NON SALVATO]");
							}
						}catch (final NumberFormatException numberFormatException) {
				    		System.out.println(numberFormatException.getMessage());
				    		System.out.println("[NON SALVATO]");
				    		
				        }catch (final Exception exception) {
				    		System.out.println(exception.getMessage());
				    		System.out.println("[NON SALVATO]");
				        }
					}
				}
				(new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
		            public Boolean apply(WebDriver d) {
		            	//return d.getTitle().toLowerCase().startsWith("cheese!");
		            	return !d.getCurrentUrl().equals("") ;
		            }
		        });
	        }
	        
	        driver.quit();

		} catch (Exception e) {
			System.out.println("Exception e");
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	
	
	public static void CatturaProvinceConfinanti() {
		try {
			
			List<Province> provinceList = provinceDao.getProvince();
			for (Province prov_ite: provinceList) {
				List<Province> list = new ArrayList<Province>(prov_ite.getProvinceConfinanti());
				if(list == null || list.size() == 0){
				}
				for (Province prov_conf: list) {
					prov_conf.getNomeProvincia();
				}
			}
			
			// ------------------------------------------------------------------------------------------
			
			System.setProperty("webdriver.chrome.driver", "C:/chromedriver.exe");
			WebDriver driver = new ChromeDriver();
			List<Province> listProvince = provinceDao.getProvince();
	        for (Province prov_ite: listProvince) {
	        	driver.get("https://it.wikipedia.org/wiki/Provincia_di_"+prov_ite.getNomeProvincia());
	        	
		        System.out.println("============================== "+ prov_ite.getNomeProvincia() +" ====================================");
			
		        List<WebElement> tr = driver.findElements(By.tagName("tr"));
				for (int i = 0; i < tr.size(); i++) {
					if( tr.get(i).getText().contains("Province confinanti") ){
						Province provConfinanti = prov_ite;
						List<WebElement> ahref = tr.get(i).findElements(By.tagName("a"));
						for (int i_a = 0; i_a < ahref.size(); i_a++) {
							System.out.print( ahref.get(i_a).getText() );
							try{
								//Province provinciaConfinante = provinceDao.getProvinciaBy_NomeProvincia(ahref.get(i_a).getText());
								Province provinciaConfinante = provinceDao.getProvinciaBy_NomeProvincia( UtilString.GetLastWord(ahref.get(i_a).getText()) );
								if(provinciaConfinante != null){
									provConfinanti.addProvinciaConfinante( provinciaConfinante );
									provinceDao.saveProvince(provConfinanti);
									System.out.println( " [OK] "+provinciaConfinante.getNomeProvincia() );
								}else{
									System.out.println( " [NON SALVATO]" );
								}
							}catch (final DataIntegrityViolationException dataIntegrViolException) {
					    		System.out.println("DataIntegrityViolationException: ");
					        }
						}
					}
				}
				(new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
		            public Boolean apply(WebDriver d) {
		            	//return d.getTitle().toLowerCase().startsWith("cheese!");
		            	return !d.getCurrentUrl().equals("") ;
		            }
		        });
	        }
	        
	        driver.quit();

		} catch (Exception e) {
			System.out.println("Exception e");
			e.printStackTrace();
		}
	}
	
	

	
	*/
	
	
	
}
