package com.apollon.util;


//import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;  
import java.util.List;

/*
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
*/
import org.springframework.dao.DataIntegrityViolationException;

import com.apollon.dao.ComuniDao;
import com.apollon.dao.ComuniFrazioniDao;
import com.apollon.dao.ProvinceDao;
import com.apollon.model.Comuni;
import com.apollon.model.ComuniFrazioni;
import com.apollon.model.Province;
import com.apollon.webapp.util.ApplicationUtils;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 *	Queste sono le Dipendendenze di cui ho bisogno, le versioni poi e poi-ooxml devono essere le stesse.
 *	<dependency>
	    <groupId>org.apache.poi</groupId>
	    <artifactId>poi</artifactId>
	    <version>3.17</version>
	</dependency> 
	<dependency>
	    <groupId>org.apache.poi</groupId>
	    <artifactId>poi-ooxml</artifactId>
	    <version>3.17</version>
	</dependency>
	<dependency>
	    <groupId>org.apache.commons</groupId>
	    <artifactId>commons-collections4</artifactId>
	    <version>4.1</version>
	</dependency> -->
 */

public class LeggiFileExcel extends ApplicationUtils {

	public static ProvinceDao provinceDao = (ProvinceDao) contextDao.getBean("ProvinceDao");
	public static ComuniDao comuniDao = (ComuniDao) contextDao.getBean("ComuniDao");
	public static ComuniFrazioniDao comuniFrazioniDao = (ComuniFrazioniDao) contextDao.getBean("ComuniFrazioniDao");
	
	/**
	 * getCellTypeEnum shown as deprecated for version 3.15
	 * getCellTypeEnum ill be renamed to getCellType starting from version 4.0
	 * vedere: https://www.mkyong.com/java/apache-poi-reading-and-writing-excel-file-in-java/
	 * 
	 */
	/*
	public static void InserisciFrazioni() throws IOException {
        //String excelFilePath = "Books.xlsx";
		String FILE_NAME = "C:\\aaa_test\\" + "frazioni-italia-comuni.xlsx";
		try { 
            FileInputStream excelFile = new FileInputStream(new File(FILE_NAME));
            Workbook workbook = new XSSFWorkbook(excelFile);    
            Sheet datatypeSheet = workbook.getSheetAt(0);  
            Iterator<Row> iterator = datatypeSheet.iterator();
            int riga = 0 ;
            while (iterator.hasNext()) {
                Row currentRow = iterator.next();
                riga++;
                iterator.hashCode();
                Iterator<Cell> cellIterator = currentRow.iterator();
                while (cellIterator.hasNext() && riga > 1) {
                	String NOME_FRAZIONE = "";String NOME_COMUNE = "";String NOME_PROVINCIA = "";String NOME_REGIONE = "";
                	try{
	                	NOME_FRAZIONE = UtilString.ReplaceAccenti(cellIterator.next().getStringCellValue());
	                	NOME_COMUNE = UtilString.ReplaceAccenti(cellIterator.next().getStringCellValue());
	                	NOME_PROVINCIA = cellIterator.next().getStringCellValue();
	                	NOME_REGIONE = cellIterator.next().getStringCellValue();
	                	List<Comuni> comTemp = comuniDao.getComuniByNomeComune_Equal(NOME_COMUNE);
	                	if(!NOME_FRAZIONE.equals("") && comTemp != null && comTemp.size() > 0
	                			&& comuniFrazioniDao.getComuniFrazioniByNomeFrazione_Equal(NOME_FRAZIONE, comTemp.get(0).getId()) == null){
	                		Province provincia = provinceDao.getProvinciaBy_NomeProvincia(NOME_PROVINCIA);
	                		Comuni comune = null;
	                		if(provincia != null){
	                			comune = comuniDao.getComuniByNomeComune_Equal(NOME_COMUNE, provincia.getSiglaProvincia());
	                			if(comune == null){
	                				comune = comuniDao.getComuniByNomeComune_Equal(NOME_COMUNE).get(0);
	                			}
	                		}else{
	                			comune = comuniDao.getComuniByNomeComune_Equal(NOME_COMUNE).get(0);
	                		}
	                		if(comune != null){
		                    	ComuniFrazioni frazioneNew = new ComuniFrazioni();
		                    	frazioneNew.setComune(comune);
		                    	frazioneNew.setIsola(false);
		                    	frazioneNew.setNomeFrazione(NOME_FRAZIONE);
		                    	ComuniFrazioni frazioneSave = comuniFrazioniDao.saveComuniFrazioni(frazioneNew);
		                    	System.out.println( "Frazione Salvata: "+frazioneSave.getNomeFrazione() +" - "+ NOME_COMUNE +" - "+ NOME_PROVINCIA +" - "+ NOME_REGIONE );
	                		}else{
	                			System.out.println( "Comune Frazione Non Trovato: "+NOME_FRAZIONE +" - "+ NOME_COMUNE +" - "+ NOME_PROVINCIA +" - "+ NOME_REGIONE);
	                		}
	                	}
	                	
                	}catch(DataIntegrityViolationException dive){
                		
                		
                	}catch(Exception exc){
                		System.out.println();
                		System.out.println( "Errore Frazione: "+NOME_FRAZIONE +" - "+ NOME_COMUNE +" - "+ NOME_PROVINCIA +" - "+ NOME_REGIONE);
                		exc.printStackTrace();
                	}
                    
                    //if (currentCell.getCellTypeEnum() == CellType.STRING) {
                    //} else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                    //}
                    
                }
            }
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    */
}
