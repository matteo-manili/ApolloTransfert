package com.apollon.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Scanner;

import com.apollon.Constants;
import com.apollon.dao.ProvinceDao;
import com.apollon.model.ClasseAutoveicolo;
import com.apollon.model.RichiestaMedia;
import com.apollon.webapp.util.ApplicationUtils;
import com.apollon.webapp.util.CalcoloPrezzi;
import com.apollon.webapp.util.controller.tariffe.TariffeUtil;

/**
 * @author Matteo - matteo.manili@gmail.com
 * 
 * Questa classe funziona che copiando e incollando l'url prodotto dal codice secondo determinate coordinate, 
 * sul browser si visualizza il prezzo della corsa di www.welcomepickups.com
 * 
 * La classe Scanner chiediPrezzo serve a ricevere in input dalla console un lavore
 * 
 */
public class CatturaTariffeWelcomepickups_com extends ApplicationUtils {

	public static ProvinceDao provinceDao = (ProvinceDao) contextDao.getBean("ProvinceDao");
	
	
	public static void main(String[] args){ 

		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
		otherSymbols.setDecimalSeparator('.');
		DecimalFormat df = new DecimalFormat("##.##",otherSymbols);
		df.setRoundingMode(RoundingMode.DOWN);
		
		String Fasce = Constants.FASCE_KILOMETRICHE;
		// metto a zero i value delle fasce
		NavigableMap<Integer, Float> mapSetZero = TariffeUtil.GetMapFasce(Fasce);
		for(Map.Entry<Integer, Float> item : mapSetZero.entrySet()){
				item.setValue(0.0f);
		}
		Fasce = TariffeUtil.GetStringMapFasce(mapSetZero);
		
		String NomeProvincia = "Milano";
		String[] Parametri = UtilString.RimuoviTuttiGliSpazi(gestioneApplicazioneDao.getName("PARAMETRI_CALCOLO_TARIFFA_AUTOVEICOLO").getValueString()).split("-");
		ClasseAutoveicolo classeAutoveicoloReale = new ClasseAutoveicolo(); classeAutoveicoloReale.setId(1l);
		BigDecimal valoreProvincia = provinceDao.getProvinciaBy_NomeProvincia( NomeProvincia ).getTariffaBase();
		List<RichiestaMedia> richiestaMediaList = ricercaTransfertDao.getRicercaTransfertSoloRicercheEseguiteCliente_PROVA_TEST_2(99999, 0, classeAutoveicoloReale, NomeProvincia);
		List<Integer> listaKilometri = new ArrayList<Integer>();
		for(RichiestaMedia richMedia_ite: richiestaMediaList){
			int kilometri = (int)(long)richMedia_ite.getRicercaTransfert().getDistanzaValue()/1000;
			if(richMedia_ite.getRicercaTransfert().isRitorno() == false && !listaKilometri.contains(kilometri)){
				listaKilometri.add(kilometri);
				System.out.println("("+richMedia_ite.getRicercaTransfert().getId()+") "+richMedia_ite.getRicercaTransfert().getComune_Partenza()+" -> "+richMedia_ite.getRicercaTransfert().getComune_Arrivo()+" | "+kilometri +" kilometri ");
				//System.out.println(ite.getClasseAutoveicolo().getNome());
				//System.out.println("getMaggiorazioneNotturna: "+ite.getMaggiorazioneNotturna());
				System.out.println("getTariffaPerKm: "+richMedia_ite.getTariffaPerKm()+"€");
				BigDecimal PrezzoAutista = CalcoloPrezzi.CalcolaPrezzoPerMetri(kilometri, richMedia_ite.getTariffaPerKm(), false);
				System.out.println("getPrezzoTotaleAutista: "+PrezzoAutista +"€");
				
				StampaUrlConnection("milan", String.valueOf(richMedia_ite.getRicercaTransfert().getLat_Partenza()), String.valueOf(richMedia_ite.getRicercaTransfert().getLng_Partenza()), 
						String.valueOf(richMedia_ite.getRicercaTransfert().getLat_Arrivo()), String.valueOf(richMedia_ite.getRicercaTransfert().getLng_Arrivo()));
				System.out.println();
	
				// questo serve a impostare la tariffa
				double ProvinciaBase_e_PunteggioAuto = CalcoloPrezzi.CalcolaTariffa_ProvinciaBase_e_PunteggioAuto(valoreProvincia.doubleValue(), 17);
				
				Scanner chiediPrezzo = new Scanner(System.in);
				try{
					Double PrezzoWelcomePickups = Double.parseDouble(chiediPrezzo.nextLine());
					Integer kilometroFascia = Integer.parseInt(chiediPrezzo.nextLine());
					double valoreFascia = PrezzoWelcomePickups / ProvinciaBase_e_PunteggioAuto / kilometri / 100 * 80;
					System.out.println("valoreFascia: "+valoreFascia);
					NavigableMap<Integer, Float> map = TariffeUtil.SetValueKilometroFascia(TariffeUtil.GetMapFasce(Fasce), kilometroFascia, (float)Double.parseDouble(df.format(valoreFascia)));
					Fasce = TariffeUtil.GetStringMapFasce(map);
					System.out.println("getPrezzoTotaleAutista NEW: "+TariffeUtil.DammiTariffa_ValoreAuto(Parametri, classeAutoveicoloReale, valoreProvincia, Fasce, kilometri) +"€");
				}catch(NumberFormatException nfe){
					System.out.println("#### NON INSERITO: "+nfe.getMessage());
				}
				
				//System.out.println("getPrezzoTotaleCliente: "+ite.getPrezzoTotaleCliente());
				System.out.println("--------------------------------------------------------");
			}
		}
	
	}
	
	private static void StampaUrlConnection(String citta, String from_lat, String from_lng, String to_lat, String to_lng){
		String UrlConnection = " https://traveler.welcomepickups.com/"+citta+"/transfer/new?utf8=%E2%9C%93&_method=patch"
				+"&from_lat="+from_lat
				+"&from_lng="+from_lng
				+"&from_type=google+maps+search"
				+"&from_category=street_address"
				+"&to_lat="+to_lat
				+"&to_lng="+to_lng
				+"&to_type=google+maps+search"
				+"&to_category=street_address"
				+"&time=09%3A00"
				+"&passengers=1"
				+"&luggage=1";
		System.out.println( UrlConnection );
	}
	
}
