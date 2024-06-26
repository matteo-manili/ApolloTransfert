package com.apollon.webapp.util.fatturazione;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import com.apollon.Constants;
import com.apollon.dao.FattureDao;
import com.apollon.dao.GestioneApplicazioneDao;
import com.apollon.model.Fatture;
import com.apollon.model.RicercaTransfert;
import com.apollon.model.Ritardi;
import com.apollon.model.Supplementi;
import com.apollon.util.DateUtil;
import com.apollon.util.UtilString;
import com.apollon.webapp.util.ApplicationUtils;
import com.apollon.webapp.util.CalcoloPrezzi;
import com.apollon.webapp.util.controller.rimborsi.RimborsiUtil;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.ColumnText;

/**
 * @author Matteo - matteo.manili@gmail.com
 * 
 * libreria: https://developers.itextpdf.com/
 * vedere: https://developers.itextpdf.com/examples/font-examples/showing-special-characters
 * 
 * INSERIRE LA DATA DI PAGAMENTO E IL PREZZO DELLA PARTITA IVA
 *
 */
public class GenerateFatturaPdf extends ApplicationUtils {
	
	private static GestioneApplicazioneDao gestioneApplicazioneDao = (GestioneApplicazioneDao) contextDao.getBean("GestioneApplicazioneDao");
	private static FattureDao fattureDao = (FattureDao) contextDao.getBean("FattureDao");
	static HttpServletRequest req;
	static BeanInfoFattura_Corsa fattCorsa;
	private static String FILE = "C:/aaa_test/Test.pdf";
	public static final String FONT_PT_SANS_WEB_REGULAR = "font_PT_Sans-Web-Regular.ttf"; // attualmente utilizzo questo si trova in webapp/fonts/...
	public static final String FONT_CARDO_REGULAR = "font_Cardo-Regular.ttf"; // questo qui fa anche i simboli come i fiori
	private static Font bigFont = new Font();
	private static Font baseFont = new Font();
	private static Font footerFont = new Font();
	private static Font baseRedFont = new Font();
	private static Font baseBlueFont = new Font();
	private static Font subFont = new Font(Font.FontFamily.COURIER, 16, Font.BOLD); // esempio non lo uso 
	
	public GenerateFatturaPdf(BeanInfoFattura_Corsa fattCorsa, HttpServletRequest request, Document document) throws DocumentException, IOException {
		this.fattCorsa = fattCorsa; this.req = request; SetFonts(); Proprieta_file_MetaDati(document, request);
	}
	
	private static void SetFonts() throws DocumentException, IOException{
		/*
	 	Se voglio cambiare font e prendrne uno dal ServletContext o da Resources fare come di seguito:
	 	String FILE_TTF = FONT_PT_SANS_WEB_REGULAR;
		File fontFile = new File(request.getServletContext().getRealPath("/")+"/fonts/"+FILE_TTF);
		InputStream is = new FileInputStream(fontFile);
		BaseFont bf = BaseFont.createFont(FILE_TTF, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, true, IOUtils.toByteArray(is), null);
		bigFont = new Font(bf, 18, Font.BOLD);
		baseFont = new Font(bf, 12, Font.NORMAL);
		footerFont = new Font(bf, 10, Font.ITALIC);
		baseRedFont = new Font(bf, 12, Font.NORMAL, BaseColor.RED);
		baseBlueFont = new Font(bf, 12, Font.NORMAL, BaseColor.BLUE);
		*/
		Font defaultFont = new Font(); defaultFont.setSize(18); defaultFont.setStyle(Font.BOLD);
		bigFont = defaultFont;
		defaultFont = new Font(); defaultFont.setSize(12); defaultFont.setStyle(Font.NORMAL);
	    baseFont = defaultFont;
	    defaultFont = new Font(); defaultFont.setSize(10); defaultFont.setStyle(Font.ITALIC);
	    footerFont = defaultFont;
	    defaultFont = new Font(); defaultFont.setSize(12); defaultFont.setStyle(Font.NORMAL); defaultFont.setColor(BaseColor.RED);
	    baseRedFont = defaultFont;
	    defaultFont = new Font(); defaultFont.setSize(12); defaultFont.setStyle(Font.NORMAL); defaultFont.setColor(BaseColor.BLUE);
	    baseBlueFont = defaultFont;
	}
	
	
	public static class Footer_Header_Fattura extends PdfPageEventHelper {
		public void onEndPage(PdfWriter writer, Document document) {
			PdfContentByte pdfContentByte = writer.getDirectContent();
			Phrase header = new Phrase("this is a header", footerFont);
			String DenominazioneDitta = (CheckDomainTranfertClienteVenditore(fattCorsa.getRicTransfert())) ?
					ApplicationMessagesUtil.DammiMessageSource("denominazione.ditta.ncctransferonline", (req!=null)?req.getLocale():Constants.Locale_IT) :
						ApplicationMessagesUtil.DammiMessageSource("denominazione.ditta.apollotransfert", (req!=null)?req.getLocale():Constants.Locale_IT);
			String Domain = (CheckDomainTranfertClienteVenditore(fattCorsa.getRicTransfert())) ?
					ApplicationMessagesUtil.DammiMessageSource("w3.domain.ncctransferonline.name", (req!=null)?req.getLocale():Constants.Locale_IT) :
						ApplicationMessagesUtil.DammiMessageSource("w3.domain.apollotransfert.name", (req!=null)?req.getLocale():Constants.Locale_IT);
			Phrase footer = new Phrase( DenominazioneDitta +
				" - "+ ApplicationMessagesUtil.DammiMessageSource("indirizzo.sede", (req!=null)?req.getLocale():Constants.Locale_IT) + " - Telefono " 
					+ ApplicationMessagesUtil.DammiMessageSource("cellulare.matteo.esteso", (req!=null)?req.getLocale():Constants.Locale_IT) +" - " + Domain, footerFont);
			ColumnText.showTextAligned( pdfContentByte, Element.ALIGN_CENTER, footer, 
					(document.right() - document.left()) / 2 + document.leftMargin(), document.bottom() - 10, 0);
		}
	}
	
	
	private static String DammiNomeCliente(RicercaTransfert ricTransfert) throws Exception {
		if(ricTransfert.getUser().getFirstName() != null && !ricTransfert.getUser().getFirstName().equals("")
				&& ricTransfert.getUser().getLastName() != null && !ricTransfert.getUser().getLastName().equals("")){
			return ricTransfert.getUser().getFirstName() +" "+ ricTransfert.getUser().getLastName();
		}else{
			return UtilString.PrimaLetteraMaiuscola( RimborsiUtil.Retrive_Amount_Rimborso_NomeCliente(ricTransfert).getNomeCliente() );
		}
	}
	

	/**
	 * Dati identificativi del compratore che sono:
	 * il Nome e Cognome del cliente Privato OPPURE la ragione sociale della Azienda, 
	 * codice fiscale del Cliente Privato OPPURE Partita iva della Azienda  
	 * Indirizzo di fatturazione
	 * @throws Exception 
	 */
	private static Paragraph DatiCliente(Paragraph prefazione) throws Exception{
		if(fattCorsa.getRicTransfert().getUser().getBillingInformation() != null &&
				fattCorsa.getRicTransfert().getUser().getBillingInformation().getDenominazioneCliente() != null && 
				!fattCorsa.getRicTransfert().getUser().getBillingInformation().getDenominazioneCliente().equals("")){
			prefazione.add(new Paragraph(fattCorsa.getRicTransfert().getUser().getBillingInformation().getDenominazioneCliente(), baseFont));
		}else{
			prefazione.add(new Paragraph(DammiNomeCliente(fattCorsa.getRicTransfert()), baseFont));
		}
		if(fattCorsa.getRicTransfert().getUser().getBillingInformation() != null &&
				fattCorsa.getRicTransfert().getUser().getBillingInformation().getIndirizzoFatturazione() != null && 
				!fattCorsa.getRicTransfert().getUser().getBillingInformation().getIndirizzoFatturazione().equals("")){
			prefazione.add(new Paragraph(fattCorsa.getRicTransfert().getUser().getBillingInformation().getIndirizzoFatturazione(), baseFont));
		}
		if(fattCorsa.getRicTransfert().getUser().getBillingInformation() != null &&
				fattCorsa.getRicTransfert().getUser().getBillingInformation().getCodiceFiscalePartitaIva() != null && 
				!fattCorsa.getRicTransfert().getUser().getBillingInformation().getCodiceFiscalePartitaIva().equals("")){
			prefazione.add(new Paragraph(fattCorsa.getRicTransfert().getUser().getBillingInformation().getCodiceFiscalePartitaIva(), baseFont));
		}
		return prefazione;
	}
	
	/**
	 * Fattura Corsa
	 * @throws Exception 
	 */
	public void Corpo_FatturaCorsa(Document document, Locale locale) throws Exception {
		Paragraph prefazione = new Paragraph();
		// WebAppName ditta
		String WebAppName = (CheckDomainTranfertClienteVenditore(fattCorsa.getRicTransfert())) ?
				ApplicationMessagesUtil.DammiMessageSource("webapp.ncctransferonline.name", locale) :
					ApplicationMessagesUtil.DammiMessageSource("webapp.apollotransfert.name", locale);
		prefazione.add(new Paragraph(WebAppName, bigFont));
		AggiungiLineaVuota(prefazione, 1);
		// partita iva e indirizzo
		prefazione.add(new Paragraph("Partita iva: "+ApplicationMessagesUtil.DammiMessageSource("partita.iva", locale) + 
				" - Fattura Numero: " + fattCorsa.getNumeroProgressivo(), baseFont) );
		AggiungiLineaVuota(prefazione, 1);
		// l'imponibile, ossia l'indicazione dell'importo prima dell'applicazione dell'aliquota IVA 
		if( fattCorsa.getRicTransfert().isPagamentoParziale() ){
			prefazione.add(new Paragraph("Prezzo imponibile: "+fattCorsa.getPrezzoServizio().replace(".", ",")+"€", baseFont));
		}else{
			prefazione.add(new Paragraph("Prezzo imponibile: "+fattCorsa.getPrezzoCliente().replace(".", ",")+"€", baseFont));
		}
		// importo dell'IVA
		prefazione.add(new Paragraph("Prezzo IVA: "+fattCorsa.getPrezzoIva().replace(".", ",")+"€", baseFont));
		// importo Prezzo Totale più eventuale Info Pagamento Contati Autisti e eventuale Maggiorazione Notturna
		String maggiorazioneNotturna = "";
		if( fattCorsa.getMaggiorazioneNotturna() != null ){
			maggiorazioneNotturna = ApplicationMessagesUtil.DammiMessageSource("prezzo.autista.inclusa.maggiorazione.notturna", 
					new String[]{ fattCorsa.getMaggiorazioneNotturna().replace(".", ",")}, locale);
		}
		if( fattCorsa.getRicTransfert().isPagamentoParziale() ){
			String PrezzoParziale = ApplicationMessagesUtil.DammiMessageSource("prezzo.cliente.parziale.piu.prezzo.autista", 
					new String[]{ fattCorsa.getPrezzoServizio().replace(".", ","), fattCorsa.getPrezzoAutista().replace(".", ",")}, locale);
			prefazione.add(new Paragraph("Prezzo TOTALE: "+PrezzoParziale + maggiorazioneNotturna, baseFont));
		}else{
			prefazione.add(new Paragraph("Prezzo TOTALE: "+fattCorsa.getPrezzoCliente().replace(".", ",")+"€" + maggiorazioneNotturna, baseFont));
		}
		prefazione.add( new Paragraph( "Pagamento Eseguito", baseBlueFont));
		AggiungiLineaVuota(prefazione, 1);
		// Dati Cliente
		AggiungiLineaVuota(DatiCliente(prefazione), 1);
		// Aggiungiamo una tabella
		CorpoFattura_Corsa_Tabella(prefazione);
		AggiungiLineaVuota(prefazione, 1);
		// INFO REGIME FORFETTARIO
		prefazione.add( new Paragraph(ApplicationMessagesUtil.DammiMessageSource("parita.iva.regime.forfettario", locale), baseFont) );
		// Aggiunta al documento
		document.add(prefazione);
		// Nuova pagina
		document.newPage();
	}
	
	
	/**
	 * Fattura Rimborso
	 * @throws Exception 
	 */
	public void Corpo_FatturaCorsaRimborso(Document document, Locale locale) throws Exception {
		Paragraph prefazione = new Paragraph();
		
		// WebAppName
		String WebAppName = (CheckDomainTranfertClienteVenditore(fattCorsa.getRicTransfert())) ?
				ApplicationMessagesUtil.DammiMessageSource("webapp.ncctransferonline.name", locale) :
					ApplicationMessagesUtil.DammiMessageSource("webapp.apollotransfert.name", locale);
		prefazione.add(new Paragraph(WebAppName, bigFont));
		AggiungiLineaVuota(prefazione, 1);
		
		// partita iva e indirizzo
		prefazione.add(new Paragraph("Partita iva: "+ApplicationMessagesUtil.DammiMessageSource("partita.iva", locale) + 
				" - Nota di Credito Numero: " + fattCorsa.getNumeroProgressivo(), baseFont) );
		AggiungiLineaVuota(prefazione, 1);

		// l'imponibile, ossia l'indicazione dell'importo prima dell'applicazione dell'aliquota IVA 
		if( fattCorsa.getRicTransfert().isPagamentoParziale() ){
			prefazione.add(new Paragraph("Prezzo imponibile: "+fattCorsa.getPrezzoServizio().replace(".", ",")+"€", baseFont));
		}else{
			prefazione.add(new Paragraph("Prezzo imponibile: "+fattCorsa.getPrezzoCliente().replace(".", ",")+"€", baseFont));
		}
		// importo dell'IVA
		prefazione.add(new Paragraph("Prezzo IVA: "+fattCorsa.getPrezzoIva().replace(".", ",")+"€", baseFont));
		// Stortno
		prefazione.add(new Paragraph("Storno Totale: "+fattCorsa.getRimborsoCliente().replace(".", ",")+"€", baseFont));
		prefazione.add(new Paragraph("Della fattura del "+DateUtil.FORMATO_DATA_ORA.format(fattCorsa.getRicTransfert().getDataRicerca()), baseFont));
		prefazione.add(new Paragraph("Rimborso Eseguito", baseBlueFont));
		AggiungiLineaVuota(prefazione, 1);
		// Dati Cliente
		AggiungiLineaVuota(DatiCliente(prefazione), 1);
		// Aggiungiamo una tabella
		CorpoFattura_Corsa_Tabella(prefazione);
		AggiungiLineaVuota(prefazione, 1);
		// INFO REGIME FORFETTARIO
		prefazione.add( new Paragraph(ApplicationMessagesUtil.DammiMessageSource("parita.iva.regime.forfettario", locale), baseFont) );
		// Aggiunta al documento
		document.add(prefazione);
		// Nuova pagina
		document.newPage();
	}
	
	
	/**
	 * Fattura Ritardo
	 * @throws Exception 
	 */
	public void Corpo_FatturaRitardo(Document document, Ritardi ritardo, Locale locale) throws Exception {
		Paragraph prefazione = new Paragraph();
		// WebAppName
		String WebAppName = (CheckDomainTranfertClienteVenditore(fattCorsa.getRicTransfert())) ?
				ApplicationMessagesUtil.DammiMessageSource("webapp.ncctransferonline.name", locale) :
					ApplicationMessagesUtil.DammiMessageSource("webapp.apollotransfert.name", locale);
		prefazione.add(new Paragraph(WebAppName, bigFont));
		AggiungiLineaVuota(prefazione, 1);
		
		// partita iva e indirizzo
		Fatture fatturaRitado = fattureDao.getFatturaBy_IdRitardo(ritardo.getId());
		prefazione.add(new Paragraph("Partita iva: "+ApplicationMessagesUtil.DammiMessageSource("partita.iva", locale) + 
				" - Fattura Numero: " + fatturaRitado.getProgressivoFattura() + " - Giorno: "+DateUtil.FORMATO_DATA_ORA.format(new Date()), baseFont) );
		AggiungiLineaVuota(prefazione, 1);

		// l'imponibile, ossia l'indicazione dell'importo prima dell'applicazione dell'aliquota IVA 
		prefazione.add(new Paragraph("Prezzo imponibile: "+ritardo.getPrezzoAndata().add( ritardo.getPrezzoRitorno() ).toString().replace(".", ",") +"€", baseFont));
		
		// importo dell'IVA
		prefazione.add(new Paragraph("Prezzo IVA: "+fattCorsa.getPrezzoIva().replace(".", ",")+"€", baseFont));
		
		prefazione.add(new Paragraph("Prezzo TOTALE: "+ritardo.getPrezzoAndata().add( ritardo.getPrezzoRitorno() ).toString().replace(".", ",") +"€", baseFont));
		if( ritardo.isPagatoAndata() && ritardo.isPagatoRitorno() ){
			prefazione.add( new Paragraph( "Pagamento Ritardo Eseguito", baseBlueFont));
		}else{
			prefazione.add( new Paragraph( "Pagamento Ritardo Non Eseguito", baseRedFont));
		}
		AggiungiLineaVuota(prefazione, 1);
		
		// Dati Cliente
		AggiungiLineaVuota(DatiCliente(prefazione), 1);
		
		BigDecimal prezzoOrarioRitardoCliete = new BigDecimal( gestioneApplicazioneDao.getName("VALORE_PERCENTUALE_SERVIZIO_E_VALORE_EURO_ORA_RITARDO_CLIENTE").getValueString() );
		long percentualeServiziorRitardoCliete = gestioneApplicazioneDao.getName("VALORE_PERCENTUALE_SERVIZIO_E_VALORE_EURO_ORA_RITARDO_CLIENTE").getValueNumber();
		BigDecimal prezzoRitardoClieteConCostoServizio = CalcoloPrezzi.CalcolaPercentuale(prezzoOrarioRitardoCliete, (int) percentualeServiziorRitardoCliete);
		BigDecimal prezzoOrarioRitardoClieteBigdecimal = prezzoOrarioRitardoCliete.add(prezzoRitardoClieteConCostoServizio).divide(new BigDecimal(2));
		
		Double OreRitardoAndata = (double)ritardo.getNumeroMezzoreRitardiAndata() / 2;
		Double OreRitardoRitorno = (double)ritardo.getNumeroMezzoreRitardiRitorno() / 2;
		Double totaleOreRitardi = OreRitardoAndata + OreRitardoRitorno;
		
		String OreRitardoAndataStr = "";
		if(fattCorsa.getRicTransfert().isRitorno()){
			OreRitardoAndataStr = "Numero Ore di Ritardo al Prelevamento Passeggero Andata: "+OreRitardoAndata.toString().replace(".", ",")+"H |";
		}else{
			OreRitardoAndataStr = "Numero Ore di Ritardo al Prelevamento Passeggero: "+OreRitardoAndata.toString().replace(".", ",")+"H |";
		}
		String OreRitardoRitornoStr = "Numero Ore di Ritardo al Prelevamento Passeggero Ritorno: "+OreRitardoRitorno.toString().replace(".", ",")+"H |";
		String totaleOreRitardiStr = "Totale ORE Ritardi: "+totaleOreRitardi.toString().replace(".", ",")+"H";
		String totalRitardoString = "";
		
		if(ritardo.getNumeroMezzoreRitardiAndata() > 0 && ritardo.getNumeroMezzoreRitardiRitorno() > 0){
			totalRitardoString = OreRitardoAndataStr +" "+ OreRitardoRitornoStr +" "+ totaleOreRitardiStr;
			
		}else if(ritardo.getNumeroMezzoreRitardiAndata() > 0){
			totalRitardoString = OreRitardoAndataStr;
			
		}else if(ritardo.getNumeroMezzoreRitardiRitorno() > 0){
			totalRitardoString = OreRitardoRitornoStr;
		}
		prefazione.add(new Paragraph( totalRitardoString +". (Costo ogni 30 minuti: "+prezzoOrarioRitardoClieteBigdecimal.toString().replace(".", ",") +"€)", baseFont));
		AggiungiLineaVuota(prefazione, 1);
		// Aggiungiamo una tabella
		CorpoFattura_Corsa_Tabella(prefazione);
		AggiungiLineaVuota(prefazione, 1);
		// INFO REGIME FORFETTARIO
		prefazione.add( new Paragraph(ApplicationMessagesUtil.DammiMessageSource("parita.iva.regime.forfettario", locale), baseFont) );
		// Aggiunta al documento
		document.add(prefazione);
		// Nuova pagina
		document.newPage();
	}
	
	/**
	 * Fattura Ritardo
	 * @throws Exception 
	 */
	public void Corpo_FatturaSupplemento(Document document, Supplementi supplemento, Locale locale) throws Exception {
		Paragraph prefazione = new Paragraph();
		// WebAppName
		String WebAppName = (CheckDomainTranfertClienteVenditore(fattCorsa.getRicTransfert())) ?
				ApplicationMessagesUtil.DammiMessageSource("webapp.ncctransferonline.name", locale) :
					ApplicationMessagesUtil.DammiMessageSource("webapp.apollotransfert.name", locale);
		prefazione.add(new Paragraph(WebAppName, bigFont));
		AggiungiLineaVuota(prefazione, 1);
		
		// partita iva e indirizzo
		Fatture fatturaSupplemento = fattureDao.getFatturaBy_IdSupplemento(supplemento.getId());
		prefazione.add(new Paragraph("Partita iva: "+ApplicationMessagesUtil.DammiMessageSource("partita.iva", locale) + 
				" - Fattura Numero: " + fatturaSupplemento.getProgressivoFattura() + " - Giorno: "+DateUtil.FORMATO_DATA_ORA.format(new Date()), baseFont) );
		AggiungiLineaVuota(prefazione, 1);

		// l'imponibile, ossia l'indicazione dell'importo prima dell'applicazione dell'aliquota IVA 
		prefazione.add(new Paragraph("Prezzo imponibile: "+supplemento.getPrezzo().toString().replace(".", ",") +"€", baseFont));
		
		// importo dell'IVA
		prefazione.add(new Paragraph("Prezzo IVA: "+fattCorsa.getPrezzoIva().replace(".", ",")+"€", baseFont));
		
		prefazione.add(new Paragraph("Prezzo TOTALE: "+supplemento.getPrezzo().toString().replace(".", ",") +"€", baseFont));
		if( supplemento.isPagato() ){
			prefazione.add( new Paragraph( "Pagamento Supplemento Eseguito", baseBlueFont));
		}else{
			prefazione.add( new Paragraph( "Pagamento Supplemento Non Eseguito", baseRedFont));
		}
		AggiungiLineaVuota(prefazione, 1);
		
		// Dati Cliente
		AggiungiLineaVuota(DatiCliente(prefazione), 1);
		
		prefazione.add(new Paragraph(supplemento.getDescrizione(), baseFont));
		AggiungiLineaVuota(prefazione, 1);
		// Aggiungiamo una tabella
		CorpoFattura_Corsa_Tabella(prefazione);
		AggiungiLineaVuota(prefazione, 1);
		// INFO REGIME FORFETTARIO
		prefazione.add( new Paragraph(ApplicationMessagesUtil.DammiMessageSource("parita.iva.regime.forfettario", locale), baseFont) );
		// Aggiunta al documento
		document.add(prefazione);
		// Nuova pagina
		document.newPage();
	}


	/**
	 * Tabella Corsa
	 */
	private static void CorpoFattura_Corsa_Tabella(Paragraph prefazione) throws BadElementException {
		prefazione.add(new Paragraph("CORSA ID: "+fattCorsa.getRicTransfert().getId() +
				", Data Prenotazione: "+DateUtil.FORMATO_DATA_ORA.format(fattCorsa.getRicTransfert().getDataRicerca()), baseFont));
		AggiungiLineaVuota(prefazione, 1);
		
		String partenza = ""; String arrivo = "";
		if(fattCorsa.getRicTransfert().getPartenzaRequest() != null && !fattCorsa.getRicTransfert().getPartenzaRequest().equals("")){
			partenza = fattCorsa.getRicTransfert().getPartenzaRequest();
		}else{
			partenza = fattCorsa.getRicTransfert().getName_Partenza();
		}
		if(fattCorsa.getRicTransfert().getArrivoRequest() != null && !fattCorsa.getRicTransfert().getArrivoRequest().equals("")){
			arrivo = fattCorsa.getRicTransfert().getArrivoRequest();
		}else{
			arrivo = fattCorsa.getRicTransfert().getName_Arrivo();
		}
		
		PdfPTable tabella;
		if(fattCorsa.isAutistaConfermato()){
			tabella = new PdfPTable(5);
		}else{
			tabella = new PdfPTable(4);
		}
		
		tabella.setHorizontalAlignment(Element.ALIGN_LEFT);
		// tabella.setBorderColor(BaseColor.GRAY);
		// tabella.setPadding(4);
		// tabella.setSpacing(4);
		// tabella.setBorderWidth(1);

		PdfPCell c1 = new PdfPCell(new Phrase("Partenza"));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setGrayFill(0.8f);
		tabella.addCell(c1);

		c1 = new PdfPCell(new Phrase("Arrivo"));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setGrayFill(0.8f);
		tabella.addCell(c1);

		c1 = new PdfPCell(new Phrase("durata"));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setGrayFill(0.8f);
		tabella.addCell(c1);

		c1 = new PdfPCell(new Phrase("distanza"));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setGrayFill(0.8f);
		tabella.addCell(c1);
		
		if(fattCorsa.isAutistaConfermato()) {
			c1 = new PdfPCell(new Phrase("Autista"));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setGrayFill(0.8f);
			tabella.addCell(c1);
		}
		tabella.setHeaderRows(1);
		tabella.addCell(partenza +" "+ DateUtil.FORMATO_DATA_ORA.format(fattCorsa.getRicTransfert().getDataOraPrelevamentoDate()));
		tabella.addCell(arrivo);
		tabella.addCell(fattCorsa.getRicTransfert().getDurataConTrafficoText());
		tabella.addCell(fattCorsa.getRicTransfert().getDistanzaText());
		if(fattCorsa.getRicTransfert().getTipoServizio().equals(Constants.SERVIZIO_MULTIPLO)) {
			String NomiAutisti = "";
			for(int ite = 0; ite < fattCorsa.getAutistaServizioMultiplo().size(); ite++) {
				if(fattCorsa.isAutistaConfermato()){
					NomiAutisti = NomiAutisti + fattCorsa.getAutistaServizioMultiplo().get(ite).getUser().getFullName();
					if( ite != fattCorsa.getAutistaServizioMultiplo().size() - 1) {
						NomiAutisti = NomiAutisti + ", ";
					}else {
						NomiAutisti = NomiAutisti + ".";
					}
				}
			}
			tabella.addCell( NomiAutisti );
		}else {
			if(fattCorsa.getRicTransfert().getTipoServizio().equals(Constants.SERVIZIO_AGENDA_AUTISTA)) {
				if( fattCorsa.getAgendaAutistaScelta().getAgendaAutista_AutistaAndata() != null ) {
					tabella.addCell( fattCorsa.getAgendaAutistaScelta().getAgendaAutista_AutistaAndata().getFullName() );
				}
			}else {
				if(fattCorsa.isAutistaConfermato()){
					tabella.addCell( fattCorsa.getAutista().getUser().getFullName() );
				}
			}
		}
		prefazione.add(tabella);
		AggiungiLineaVuota(prefazione, 1);
		if (fattCorsa.getRicTransfert().isRitorno()) { // SE RITORNO TRUE
			PdfPTable tabellaRitorno;
			if(fattCorsa.isAutistaConfermato()){
				tabellaRitorno = new PdfPTable(5);
			}else{
				tabellaRitorno = new PdfPTable(4);
			}

			tabellaRitorno.setHorizontalAlignment(Element.ALIGN_LEFT);

			PdfPCell c2 = new PdfPCell(new Phrase("Partenza"));
			c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			c2.setGrayFill(0.8f);
			tabellaRitorno.addCell(c2);

			c2 = new PdfPCell(new Phrase("Arrivo"));
			c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			c2.setGrayFill(0.8f);
			tabellaRitorno.addCell(c2);

			c2 = new PdfPCell(new Phrase("durata"));
			c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			c2.setGrayFill(0.8f);
			tabellaRitorno.addCell(c2);

			c2 = new PdfPCell(new Phrase("distanza"));
			c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			c2.setGrayFill(0.8f);
			tabellaRitorno.addCell(c2);

			c2 = new PdfPCell(new Phrase("Autista"));
			c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			c2.setGrayFill(0.8f);
			tabellaRitorno.addCell(c2);
			
			tabellaRitorno.setHeaderRows(1);
			
			tabellaRitorno.addCell(arrivo +" "+ DateUtil.FORMATO_DATA_ORA.format(fattCorsa.getRicTransfert().getDataOraRitornoDate()));
			tabellaRitorno.addCell(partenza);
			tabellaRitorno.addCell(fattCorsa.getRicTransfert().getDurataConTrafficoTextRitorno());
			tabellaRitorno.addCell(fattCorsa.getRicTransfert().getDistanzaTextRitorno());
			if(fattCorsa.getRicTransfert().getTipoServizio().equals(Constants.SERVIZIO_MULTIPLO)) {
				String NomiAutisti = "";
				for(int ite = 0; ite < fattCorsa.getAutistaServizioMultiplo().size(); ite++) {
					if(fattCorsa.isAutistaConfermato()){
						NomiAutisti = NomiAutisti + fattCorsa.getAutistaServizioMultiplo().get(ite).getUser().getFullName();
						if( ite != fattCorsa.getAutistaServizioMultiplo().size() - 1) {
							NomiAutisti = NomiAutisti + ", ";
						}else {
							NomiAutisti = NomiAutisti + ".";
						}
					}
				}
				tabella.addCell( NomiAutisti );
			}else {
				if(fattCorsa.getRicTransfert().getTipoServizio().equals(Constants.SERVIZIO_AGENDA_AUTISTA)) {
					if( fattCorsa.getAgendaAutistaScelta().getAgendaAutista_AutistaRitorno() != null ) {
						tabellaRitorno.addCell( fattCorsa.getAgendaAutistaScelta().getAgendaAutista_AutistaRitorno().getFullName() );
					}
				}else {
					if(fattCorsa.isAutistaConfermato()){
						tabellaRitorno.addCell( fattCorsa.getAutista().getUser().getFullName() );
					}
				}
			}
			prefazione.add(tabellaRitorno);
		}
	}

	
	private static void CreaLista(Section section) {
		List list = new List(true, false, 10);
		list.add(new ListItem("Punto primo"));
		list.add(new ListItem("Punto secondo"));
		list.add(new ListItem("Punto terzo"));
		section.add(list);
	}
	

	private static void AggiungiLineaVuota(Paragraph paragraph, int number) {
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
	}
	
	
	/**
	 * Informazioni di proprietà del File
	 * iText permette di aggiungere metadati al pdf che possono essere
	 * visualizzati in Adobe Reader da File -> Proprietà
	 */
	public void Proprieta_file_MetaDati(Document document, HttpServletRequest request) {
		document.addTitle("Fattura");
		document.addAuthor( ApplicationMessagesUtil.DammiMessageSource("intestatario.ditta", (request != null) ? request.getLocale() : null) );
		String DenominazioneDitta = (CheckDomainTranfertClienteVenditore(fattCorsa.getRicTransfert())) ?
				ApplicationMessagesUtil.DammiMessageSource("denominazione.ditta.ncctransferonline", (request != null)?request.getLocale():null) :
					ApplicationMessagesUtil.DammiMessageSource("denominazione.ditta.apollotransfert", (request != null)?request.getLocale():null);
		document.addCreator( DenominazioneDitta );
	}
	
	
	/** esempio
	public static void main(String[] args) {
		try {
			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document,new FileOutputStream(FILE));
			writer.setPageEvent(new MyFooter());
			document.open();
			//aggiungiMetaDati(document);
			//aggiungiPrefazione(document, new RicercaTransfert(), "99.99");
			// aggiungiContenuto(document);
			document.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	*/
	
	
	/**
	 * esempio
	 */
	private static void AggiungiContenuto(Document document) throws DocumentException {
		// Il secondo parametro è il numero di capitolo
		Chapter chapter = new Chapter(new Paragraph("Primo Capitolo", bigFont), 1);

		Paragraph sectionParagraph = new Paragraph("Sezione 1", subFont);
		Section section = chapter.addSection(sectionParagraph);
		section.add(new Paragraph("Paragrafo 1"));

		sectionParagraph = new Paragraph("Sezione 2", subFont);
		section = chapter.addSection(sectionParagraph);
		section.add(new Paragraph("Paragrafo 1"));
		section.add(new Paragraph("Paragrafo 2"));
		section.add(new Paragraph("Paragrafo 3"));

		// Aggiungiamo una lista
		CreaLista(section);

		Paragraph paragraph = new Paragraph();
		AggiungiLineaVuota(paragraph, 2);
		section.add(paragraph);

		// Aggiungiamo una tabella
		// creaTabella(section);

		// Aggiunta al documento
		document.add(chapter);

		// Prossimo capitolo

		// Il secondo parametro è il numero di capitolo
		chapter = new Chapter(new Paragraph("Secondo Capitolo", bigFont), 2);

		sectionParagraph = new Paragraph("Sezione 1", subFont);
		section = chapter.addSection(sectionParagraph);
		section.add(new Paragraph("Paragrafo 1"));

		// Aggiunta al documento
		document.add(chapter);
	}
	

}
