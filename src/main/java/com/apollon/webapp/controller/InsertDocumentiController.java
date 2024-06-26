package com.apollon.webapp.controller;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.routines.checkdigit.IBANCheckDigit;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.apollon.Constants;
import com.apollon.model.Autista;
import com.apollon.model.AutistaLicenzeNcc;
import com.apollon.model.AutistaSottoAutisti;
import com.apollon.model.Autoveicolo;
import com.apollon.model.Comuni;
import com.apollon.model.DocumentiCap;
import com.apollon.model.DocumentiIscrizioneRuolo;
import com.apollon.model.DocumentiPatente;
import com.apollon.model.Province;
import com.apollon.model.User;
import com.apollon.service.AutistaLicenzeNccManager;
import com.apollon.service.AutistaManager;
import com.apollon.service.AutistaSottoAutistiManager;
import com.apollon.service.AutoveicoloManager;
import com.apollon.service.ComuniManager;
import com.apollon.service.DocumentiCapManager;
import com.apollon.service.DocumentiIscrizioneRuoloManager;
import com.apollon.service.DocumentiPatenteManager;
import com.apollon.service.ProvinceManager;
import com.apollon.util.DateUtil;
import com.apollon.util.UtilString;
import com.apollon.webapp.util.AmazonStorageFiles;
import com.apollon.webapp.util.MenuAutistaAttribute;
import com.apollon.webapp.util.controller.documenti.DocumentiInfoUtil;
import com.apollon.webapp.util.controller.documenti.InsertDocumentiUtil;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Controller
public class InsertDocumentiController extends BaseFormController {
    
	private VelocityEngine velocityEngine;
	@Autowired(required = false)
	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}
	
    private AutistaManager autistaManager;
    @Autowired
    public void setAutistaManager(final AutistaManager autistaManager) {
        this.autistaManager = autistaManager;
    }
    
    private DocumentiPatenteManager documentiPatenteManager;
    @Autowired
    public void setDocumentiPatenteManager(final DocumentiPatenteManager documentiPatenteManager) {
        this.documentiPatenteManager = documentiPatenteManager;
    }
    
    private DocumentiCapManager documentiCapManager;
    @Autowired
    public void setDocumentiCapManager(final DocumentiCapManager documentiCapManager) {
        this.documentiCapManager = documentiCapManager;
    }
    
    private AutistaLicenzeNccManager autistaLicenzeNccManager;
    @Autowired
    public void setAutistaLicenzeNccManager(final AutistaLicenzeNccManager autistaLicenzeNccManager) {
        this.autistaLicenzeNccManager = autistaLicenzeNccManager;
    }
    
    private DocumentiIscrizioneRuoloManager documentiIscrizioneRuoloManager;
    @Autowired
    public void setDocumentiIscrizioneRuoloManager(final DocumentiIscrizioneRuoloManager documentiIscrizioneRuoloManager) {
        this.documentiIscrizioneRuoloManager = documentiIscrizioneRuoloManager;
    }
    
    private AutistaSottoAutistiManager autistaSottoAutistiManager;
    @Autowired
    public void setAutistaSottoAutistiManager(final AutistaSottoAutistiManager autistaSottoAutistiManager) {
        this.autistaSottoAutistiManager = autistaSottoAutistiManager;
    }
	
    private AutoveicoloManager autoveicoloManager;
    @Autowired
    public void setAutoveicoloManager(final AutoveicoloManager autoveicoloManager) {
        this.autoveicoloManager = autoveicoloManager;
    }
    
    private ComuniManager comuniManager;
    @Autowired
    public void setComuniManager(final ComuniManager comuniManager) {
        this.comuniManager = comuniManager;
    }
    
    private ProvinceManager provinceManager;
    @Autowired
    public void setProvinceManager(final ProvinceManager provinceManager) {
        this.provinceManager = provinceManager;
    }
    

    
	@RequestMapping(value = "/insert-documenti", method = RequestMethod.POST)
    public ModelAndView onSubmitDocumenti(@ModelAttribute("autista") final Autista autistaMod, final BindingResult errors,
    		final HttpServletRequest request, final HttpServletResponse response){
		log.info("sono in onSubmitDocumenti POST");
		final Locale locale = request.getLocale();
		ModelAndView mav = new ModelAndView("insert-documenti");
		if (request.getParameter("cancel") != null) {
			
        }
		try {
			Autista autistaCorrente = autistaManager.get(autistaMod.getId());
			mav.addObject("tab_doc", "collaboratoreTab"); // DEFAULT TAB APERTO
			//----------------------------------------------
			// APPROVAZIONE GENERALE E MESSAGGIO PER AUTISTA
			//----------------------------------------------
			if(request.getParameter("salva-messaggio-autista") != null){
				autistaCorrente.getAutistaDocumento().setMessaggioAutistaDocumenti(request.getParameter("messaggio-autista") );
				autistaManager.saveAutista(autistaCorrente);
			}
			if (request.isUserInRole(Constants.GEST_AUTISTA_ROLE) || request.isUserInRole(Constants.ADMIN_ROLE)){
	        	if(request.getParameter("approvato-generale") != null && !request.getParameter("approvato-generale").equals("")) {
	        		mav.addObject("tab_doc", "collaboratoreTab");
	        		boolean valApprovato = Boolean.parseBoolean( request.getParameter("approvato-generale") );
	        		if( valApprovato ){
	        			autistaCorrente.getAutistaDocumento().setApprovatoGenerale(false);
	        		}else{
	        			autistaCorrente.getAutistaDocumento().setApprovatoGenerale(true);
	        		}
	        		autistaManager.saveAutista(autistaCorrente);
	        		
	        		InsertDocumentiUtil insertDocumentiUtil = new InsertDocumentiUtil();
	        		boolean esito = insertDocumentiUtil.InviaEmailAutistaProfiloApprovato(autistaCorrente, velocityEngine, request);
	        		if(esito){
	        			saveMessage(request, "Email Approvazione Profilo inviata "+autistaCorrente.getUser().getEmail());
	        		}
	        	}
			}

			
			//---------------------------------
			// AUTISTA - DOCUMENTI - CONTRATTO
			//---------------------------------
			if (request.isUserInRole(Constants.GEST_AUTISTA_ROLE) || request.isUserInRole(Constants.ADMIN_ROLE)){
	        	if(request.getParameter("contratto-approvato") != null && !request.getParameter("contratto-approvato").equals("")) {
	        		mav.addObject("tab_doc", "contrattoTab");
	        		boolean valApprovato = Boolean.parseBoolean( request.getParameter("contratto-approvato") );
	        		if( valApprovato ){
	        			autistaCorrente.getAutistaDocumento().setApprovatoContratto(false);
	        		}else{
	        			autistaCorrente.getAutistaDocumento().setApprovatoContratto(true);
	        		}
	        		autistaManager.saveAutista(autistaCorrente);
	        	}
			}
			if(request.getParameter("inserisci-contratto") != null){
				mav.addObject("tab_doc", "contrattoTab");
				// MultipartHttpServletRequest
	        	MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
				// UPLOAD DOCUMENTO PATENTE FRONTE
				MultipartFile multipartFileDocContratto = multipartRequest.getFile("documento-contratto");
		        if(!multipartFileDocContratto.isEmpty()){
		        	long fileSizeInBytes = multipartFileDocContratto.getBytes().length;
		        	long fileSizeInKB = fileSizeInBytes / 1024;
		        	long fileSizeInMB = fileSizeInKB / 1024;
		        	if (fileSizeInMB >= Constants.MAX_SIZE_DOCUMENT_MB) {
		        		errors.rejectValue("", "errors.collaboratore.documento.contratto.size", new Object[] { Constants.MAX_SIZE_DOCUMENT_MB }, "");
		        	}else{
		        		String fileName = UtilString.RimuoviCaratteriIllegaliFileName(multipartFileDocContratto.getOriginalFilename());
		        		autistaCorrente.getAutistaDocumento().setNomeFileContratto(fileName);
		        		autistaManager.saveAutista(autistaCorrente);
						AmazonStorageFiles.CaricareFile_AmazonWebService(request, multipartFileDocContratto, autistaCorrente, Constants.AMAZON_FILE_CONTRATTO);
		        	}
		        }
		        MultipartFile multipartFileDocContratto_2 = multipartRequest.getFile("documento-contratto-2");
		        if(!multipartFileDocContratto_2.isEmpty()){
		        	long fileSizeInBytes = multipartFileDocContratto_2.getBytes().length;
		        	long fileSizeInKB = fileSizeInBytes / 1024;
		        	long fileSizeInMB = fileSizeInKB / 1024;
		        	if (fileSizeInMB >= Constants.MAX_SIZE_DOCUMENT_MB) {
		        		errors.rejectValue("", "errors.collaboratore.documento.contratto.size", new Object[] { Constants.MAX_SIZE_DOCUMENT_MB }, "");
		        	}else{
		        		String fileName = UtilString.RimuoviCaratteriIllegaliFileName(multipartFileDocContratto_2.getOriginalFilename());
		        		autistaCorrente.getAutistaDocumento().setNomeFileContratto_2(fileName);
		        		autistaManager.saveAutista(autistaCorrente);
						AmazonStorageFiles.CaricareFile_AmazonWebService(request, multipartFileDocContratto_2, autistaCorrente, Constants.AMAZON_FILE_CONTRATTO_2);
		        	}
		        }
			}
			if(request.getParameter("remove-documento-contratto") != null){
	        	mav.addObject("tab_doc", "contrattoTab");
	    		AmazonStorageFiles.EliminareFile_AmazonWebService(request, autistaCorrente, Constants.AMAZON_FILE_CONTRATTO);
	    		autistaCorrente.getAutistaDocumento().setNomeFileContratto(null);
	    		autistaManager.saveAutista(autistaCorrente);
	    		return caricaFormAutistaDocumenti(mav, autistaCorrente, request);
	        }
			if(request.getParameter("remove-documento-contratto-2") != null){
	        	mav.addObject("tab_doc", "contrattoTab");
	    		AmazonStorageFiles.EliminareFile_AmazonWebService(request, autistaCorrente, Constants.AMAZON_FILE_CONTRATTO_2);
	    		autistaCorrente.getAutistaDocumento().setNomeFileContratto_2(null);
	    		autistaManager.saveAutista(autistaCorrente);
	    		return caricaFormAutistaDocumenti(mav, autistaCorrente, request);
	        }
			
			
			
			//------------------------------
			// AUTISTA - DOCUMENTI
			//------------------------------
			if (request.isUserInRole(Constants.GEST_AUTISTA_ROLE) || request.isUserInRole(Constants.ADMIN_ROLE)){
	        	if(request.getParameter("info-collaboratore-approvato") != null && !request.getParameter("info-collaboratore-approvato").equals("")) {
	        		mav.addObject("tab_doc", "collaboratoreTab");
	        		boolean valApprovato = Boolean.parseBoolean( request.getParameter("info-collaboratore-approvato") );
	        		if( valApprovato ){
	        			autistaCorrente.getAutistaDocumento().setApprovatoDocumenti(false);
	        		}else{
	        			autistaCorrente.getAutistaDocumento().setApprovatoDocumenti(true);
	        		}
	        		autistaManager.saveAutista(autistaCorrente);
	        		InsertDocumentiUtil docUtil = new InsertDocumentiUtil();
	        		boolean esito = docUtil.CheckInviaEmailContrattoAutista(autistaCorrente, velocityEngine, request);
	        		if(esito){
	        			saveMessage(request, "Email Contratto inviata "+autistaCorrente.getUser().getEmail());
	        		}
	        	}
			}
			if(request.getParameter("inserisci-info-collaboratore") != null && !autistaCorrente.getAutistaDocumento().isApprovatoDocumenti()){
	        	mav.addObject("tab_doc", "collaboratoreTab");
	        	if(request.getParameter("collaboratore-partita-iva") != null && !request.getParameter("collaboratore-partita-iva").equals("")){
					if( !StringUtils.isNumeric( request.getParameter("collaboratore-partita-iva") )){
		    			errors.rejectValue("", "errors.collaboratore.documento.partita.iva.invalid", new Object[] { request.getParameter("collaboratore-partita-iva") }, "");
		    		}else{
		    			String numeroPartitaIVa = StringUtils.deleteWhitespace( request.getParameter("collaboratore-partita-iva").toUpperCase());
	    				autistaCorrente.getAutistaDocumento().setPartitaIva( numeroPartitaIVa );
	    				try{
	    					autistaManager.saveAutista(autistaCorrente);
		        		}catch (final DataIntegrityViolationException DataIntegrViolException) {
		        			saveError(request, getText("errors.collaboratore.documento.partita.iva.existing", new Object[] { request.getParameter("collaboratore-partita-iva") },  request.getLocale() ));  
		        			return caricaFormAutistaDocumenti(mav, autistaCorrente, request);
		        		}
		    		}
				}
				if(request.getParameter("collaboratore-partita-iva-denominazione") != null && !request.getParameter("collaboratore-partita-iva-denominazione").equals("")){
    				autistaCorrente.getAutistaDocumento().setPartitaIvaDenominazione(request.getParameter("collaboratore-partita-iva-denominazione").toUpperCase().trim() );
    				autistaManager.saveAutista(autistaCorrente);
    			}
				if(request.getParameter("collaboratore-iban") != null && !request.getParameter("collaboratore-iban").equals("")){
					String iban = StringUtils.deleteWhitespace( request.getParameter("collaboratore-iban").toUpperCase());
		    		IBANCheckDigit ibanCheckDigit = new IBANCheckDigit();
			    	if( ibanCheckDigit.isValid( iban )){
			    		autistaCorrente.getAutistaDocumento().setIban( iban );
			    		try{
	    					autistaManager.saveAutista(autistaCorrente);
		        		}catch (final DataIntegrityViolationException DataIntegrViolException) {
		        			saveError(request, getText("errors.collaboratore.documento.iban.existing", new Object[] { request.getParameter("collaboratore-iban") },  request.getLocale() ));
		        			return caricaFormAutistaDocumenti(mav, autistaCorrente, request);
		        		}
			    	}else{
			    		errors.rejectValue("", "errors.format.iban", new Object[] { request.getParameter("collaboratore-iban") }, "");
			    	}
				}
				if(request.getParameter("collaboratore-patente") != null && !request.getParameter("collaboratore-patente").trim().equals("")){
	        		String numeroPatente = StringUtils.deleteWhitespace( request.getParameter("collaboratore-patente").toUpperCase());
	        		if( autistaCorrente.getAutistaDocumento().getDocumentiPatente() != null){
	        			autistaCorrente.getAutistaDocumento().getDocumentiPatente().setNumeroPatente( numeroPatente );
	        		}else{
	        			autistaCorrente.getAutistaDocumento().setDocumentiPatente( new DocumentiPatente(numeroPatente, null, null, null) );
	        		}
    				try{
    					autistaManager.saveAutista(autistaCorrente);
	        		}catch (final DataIntegrityViolationException DataIntegrViolException) {
	        			saveError(request, getText("errors.collaboratore.documento.patente.existing", new Object[] { request.getParameter("collaboratore-patente") },  request.getLocale() ));
	        			return caricaFormAutistaDocumenti(mav, autistaCorrente, request);
	        		}
    			}
				if(request.getParameter("collaboratore-scadenza-patente") != null){
            		String dataIscrizioneAlboString = request.getParameter("collaboratore-scadenza-patente");
            		if(dataIscrizioneAlboString.equals("")){
            			if(autistaCorrente.getAutistaDocumento().getDocumentiPatente() != null){
            				autistaCorrente.getAutistaDocumento().getDocumentiPatente().setDataScadenzaPatente(null);
                		}
            		}else{
            			try{
            				Date dataIscrizioneAlbo = DateUtil.FormatoData_1.parse( dataIscrizioneAlboString );
	            			if(autistaCorrente.getAutistaDocumento().getDocumentiPatente() != null){
	            				autistaCorrente.getAutistaDocumento().getDocumentiPatente().setDataScadenzaPatente(dataIscrizioneAlbo);
	                		}else{
	                			autistaCorrente.getAutistaDocumento().setDocumentiPatente( new DocumentiPatente(null, dataIscrizioneAlbo, null, null) );
	                		}
	            			autistaManager.saveAutista(autistaCorrente);
	            		}catch(ParseException pe ){
	            			errors.rejectValue("", "errors.collaboratore.documento.patente.scadenza.data.invalid", new Object[] {  }, "");
	            		}
            		}
    			}
	        	// MultipartHttpServletRequest
	        	MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
	        	
				// UPLOAD DOCUMENTO PATENTE FRONTE
				MultipartFile multipartFileDocPatenteFronte = multipartRequest.getFile("collaboratore-documento-patente-fronte");
		        if(!multipartFileDocPatenteFronte.isEmpty()){
		        	long fileSizeInBytes = multipartFileDocPatenteFronte.getBytes().length;
		        	long fileSizeInKB = fileSizeInBytes / 1024;
		        	long fileSizeInMB = fileSizeInKB / 1024;
		        	if (fileSizeInMB >= Constants.MAX_SIZE_DOCUMENT_MB) {
		        		errors.rejectValue("", "errors.collaboratore.documento.patente.fronte.size", new Object[] { Constants.MAX_SIZE_DOCUMENT_MB }, "");
		        	}else{
		        		String fileName = UtilString.RimuoviCaratteriIllegaliFileName(multipartFileDocPatenteFronte.getOriginalFilename());
		        		if( autistaCorrente.getAutistaDocumento().getDocumentiPatente() != null){
		        			autistaCorrente.getAutistaDocumento().getDocumentiPatente().setNomeFilePatenteFronte(fileName);
		        		}else{
		        			autistaCorrente.getAutistaDocumento().setDocumentiPatente( new DocumentiPatente(null, null, fileName, null) );
		        		}
		        		autistaManager.saveAutista(autistaCorrente);
						AmazonStorageFiles.CaricareFile_AmazonWebService(request, multipartFileDocPatenteFronte, autistaCorrente.getAutistaDocumento().getDocumentiPatente(), Constants.AMAZON_FILE_PATENTE_F);
		        	}
		        }
		        // UPLOAD DOCUMENTO PATENTE RETRO
				MultipartFile multipartFileDocPatenteRetro = multipartRequest.getFile("collaboratore-documento-patente-retro");
		        if(!multipartFileDocPatenteRetro.isEmpty()){
		        	long fileSizeInBytes = multipartFileDocPatenteRetro.getBytes().length;
		        	long fileSizeInKB = fileSizeInBytes / 1024;
		        	long fileSizeInMB = fileSizeInKB / 1024;
		        	if (fileSizeInMB >= Constants.MAX_SIZE_DOCUMENT_MB) {
		        		errors.rejectValue("", "errors.collaboratore.documento.patente.retro.size", new Object[] { Constants.MAX_SIZE_DOCUMENT_MB }, "");
		        	}else{
		        		String fileName = UtilString.RimuoviCaratteriIllegaliFileName(multipartFileDocPatenteRetro.getOriginalFilename());
		        		if( autistaCorrente.getAutistaDocumento().getDocumentiPatente() != null){
		        			autistaCorrente.getAutistaDocumento().getDocumentiPatente().setNomeFilePatenteRetro(fileName);
		        		}else{
		        			autistaCorrente.getAutistaDocumento().setDocumentiPatente( new DocumentiPatente(null, null, null, fileName) );
		        		}
		        		autistaManager.saveAutista(autistaCorrente);
						AmazonStorageFiles.CaricareFile_AmazonWebService(request, multipartFileDocPatenteRetro, autistaCorrente.getAutistaDocumento().getDocumentiPatente(), Constants.AMAZON_FILE_PATENTE_R);
		        	}
		        }
		        // UPLOAD DOCUMENTO AGGIUNTIVO
				MultipartFile multipartFileDocumentoAggiutivo = multipartRequest.getFile("collaboratore-documento-aggiuntivo");
		        if(!multipartFileDocumentoAggiutivo.isEmpty()){
		        	long fileSizeInBytes = multipartFileDocumentoAggiutivo.getBytes().length;
		        	long fileSizeInKB = fileSizeInBytes / 1024;
		        	long fileSizeInMB = fileSizeInKB / 1024;
		        	if (fileSizeInMB >= Constants.MAX_SIZE_DOCUMENT_MB) {
		        		errors.rejectValue("", "errors.collaboratore.documento.aggiuntivo.size", new Object[] { Constants.MAX_SIZE_DOCUMENT_MB }, "");
		        	}else{
		        		String fileName = UtilString.RimuoviCaratteriIllegaliFileName(multipartFileDocumentoAggiutivo.getOriginalFilename());
		        		autistaCorrente.getAutistaDocumento().setDocumentoAggiuntivo(fileName);
		        		autistaManager.saveAutista(autistaCorrente);
						AmazonStorageFiles.CaricareFile_AmazonWebService(request, multipartFileDocumentoAggiutivo, autistaCorrente, Constants.AMAZON_FILE_DOCUMENTO_AGGIUNTIVO);
		        	}
		        }
			}
			if(request.getParameter("remove-collaboratore-documento-patente-fronte") != null && !autistaCorrente.getAutistaDocumento().isApprovatoDocumenti()){
	        	mav.addObject("tab_doc", "collaboratoreTab");
	    		Long idAutistaDocumentoPatente = Long.parseLong( request.getParameter("remove-collaboratore-documento-patente-fronte") );
	    		DocumentiPatente docPatenteF = documentiPatenteManager.get(idAutistaDocumentoPatente);
	    		AmazonStorageFiles.EliminareFile_AmazonWebService(request, docPatenteF, Constants.AMAZON_FILE_PATENTE_F);
	    		autistaCorrente.getAutistaDocumento().getDocumentiPatente().setNomeFilePatenteFronte(null);
	    		autistaManager.saveAutista(autistaCorrente);
	    		return caricaFormAutistaDocumenti(mav, autistaCorrente, request);
	        }
			if(request.getParameter("remove-collaboratore-documento-patente-retro") != null && !autistaCorrente.getAutistaDocumento().isApprovatoDocumenti()){
	        	mav.addObject("tab_doc", "collaboratoreTab");
	    		Long idAutistaDocumentoPatente = Long.parseLong( request.getParameter("remove-collaboratore-documento-patente-retro") );
	    		DocumentiPatente docPatenteR = documentiPatenteManager.get(idAutistaDocumentoPatente);
	    		AmazonStorageFiles.EliminareFile_AmazonWebService(request, docPatenteR, Constants.AMAZON_FILE_PATENTE_R);
	    		autistaCorrente.getAutistaDocumento().getDocumentiPatente().setNomeFilePatenteRetro(null);
	    		autistaManager.saveAutista(autistaCorrente);
	    		return caricaFormAutistaDocumenti(mav, autistaCorrente, request);
	        }
			if(request.getParameter("remove-collaboratore-documento-aggiuntivo") != null){
	        	mav.addObject("tab_doc", "collaboratoreTab");
	    		AmazonStorageFiles.EliminareFile_AmazonWebService(request, autistaCorrente, Constants.AMAZON_FILE_DOCUMENTO_AGGIUNTIVO);
	    		autistaCorrente.getAutistaDocumento().setDocumentoAggiuntivo(null);
	    		autistaManager.saveAutista(autistaCorrente);
	    		return caricaFormAutistaDocumenti(mav, autistaCorrente, request);
	        }
			
			
			
			//--------------------------------------
	        // AUTISTA - ISCRIZIONE RUOLO CONDUCENTI
	        //--------------------------------------
	        if(request.getParameter("seleziona-provincia-ruolo-collaboratore") != null ){
	        	mav.addObject("tab_doc", "iscrizioneRuoloTab");
	        	if( !autistaCorrente.getAutistaDocumento().isApprovatoDocumenti() ){
		        	String idProvinciaCameraCommercio = request.getParameter("seleziona-provincia-ruolo-collaboratore");
		        	Province provinciaRuolo = provinceManager.get( Long.parseLong(idProvinciaCameraCommercio) );
		        	if(autistaCorrente.getAutistaDocumento().getDocumentiIscrizioneRuolo() != null) {
		        		autistaCorrente.getAutistaDocumento().getDocumentiIscrizioneRuolo().setProvinciaRuoloConducenti(provinciaRuolo);
					}else{
						DocumentiIscrizioneRuolo documentiIscrRuolo = new DocumentiIscrizioneRuolo(null, null, null, provinciaRuolo);
						autistaCorrente.getAutistaDocumento().setDocumentiIscrizioneRuolo(documentiIscrRuolo);
					}
		        	try{
	    				autistaManager.saveAutista(autistaCorrente);
	    				return caricaFormAutistaDocumenti(mav, autistaCorrente, request);
		    		}catch (final DataIntegrityViolationException DataIntegrViolException) {
		    			saveError(request, getText("errors.iscrizione.ruolo.existing", new Object[] { 
		    					autistaCorrente.getAutistaDocumento().getDocumentiIscrizioneRuolo().getNumeroRuoloConducenti()
		    						+" "+provinciaRuolo.getNomeProvincia() }, request.getLocale() ));
		    			return caricaFormAutistaDocumenti(mav, autistaCorrente, request);
		    		}
	        	}
	        }
	        if(request.getParameter("salva-ruolo-collaboratore") != null && !autistaCorrente.getAutistaDocumento().isApprovatoDocumenti() 
	        		&& autistaCorrente.getAutistaDocumento().getDocumentiIscrizioneRuolo() != null) {
	        	mav.addObject("tab_doc", "iscrizioneRuoloTab");
	        	Province provinciaRuolo =  provinceManager.get( Long.parseLong(request.getParameter("collaboratore-id-provincia-ruolo")) );
	        	autistaCorrente.getAutistaDocumento().getDocumentiIscrizioneRuolo().setProvinciaRuoloConducenti(provinciaRuolo);
	        	if(request.getParameter("collaboratore-numero-ruolo") != null && !request.getParameter("collaboratore-numero-ruolo").trim().equals("")){
		    		String NumeroRuolo = UtilString.RimuoviTuttiGliSpazi(request.getParameter("collaboratore-numero-ruolo"));
		    		if( !StringUtils.isNumeric( NumeroRuolo )){
		    			errors.rejectValue("", "errors.iscrizione.ruolo.numero.invalid", new Object[] { NumeroRuolo }, "");
		    		}else{
		    			autistaCorrente.getAutistaDocumento().getDocumentiIscrizioneRuolo().setNumeroRuoloConducenti(NumeroRuolo);
		    			try{
		    				autistaManager.saveAutista(autistaCorrente);
			    		}catch (final DataIntegrityViolationException DataIntegrViolException) {
			    			saveError(request, getText("errors.iscrizione.ruolo.existing", new Object[] { NumeroRuolo
			    					+" "+provinciaRuolo.getNomeProvincia() }, request.getLocale() ));
			    			return caricaFormAutistaDocumenti(mav, autistaCorrente, request);
			    		}
		    		}
		    	}
    			if(request.getParameter("collaboratore-data-iscrizione-ruolo") != null){
            		String dataIscrizioneAlboString = request.getParameter("collaboratore-data-iscrizione-ruolo");
            		if(dataIscrizioneAlboString.equals("")){
            			autistaCorrente.getAutistaDocumento().getDocumentiIscrizioneRuolo().setDataIscrizioneRuoloConducenti(null);
            		}else{
	            		try{
	            			Date dataIscrizioneAlbo = DateUtil.FormatoData_1.parse( dataIscrizioneAlboString );
	            			autistaCorrente.getAutistaDocumento().getDocumentiIscrizioneRuolo().setDataIscrizioneRuoloConducenti(dataIscrizioneAlbo);
	            			autistaManager.saveAutista(autistaCorrente);
	            		}catch(ParseException pe ){
	            			errors.rejectValue("", "errors.iscrizione.ruolo.data.invalid", new Object[] {  }, "");
	            		}
            		}
    			}
    			if(request.getParameter("collaboratore-cap") != null && !request.getParameter("collaboratore-cap").trim().equals("")){
	        		String numeroCap = StringUtils.deleteWhitespace( request.getParameter("collaboratore-cap").toUpperCase());
	        		if( autistaCorrente.getAutistaDocumento().getDocumentiCap() != null){
	        				autistaCorrente.getAutistaDocumento().getDocumentiCap().setNumeroCAP( numeroCap );
	        		}else{
	        			autistaCorrente.getAutistaDocumento().setDocumentiCap( new DocumentiCap(numeroCap, null, null) );
	        		}
    				try{
    					autistaManager.saveAutista(autistaCorrente);
	        		}catch (final DataIntegrityViolationException DataIntegrViolException) {
	        			saveError(request, getText("errors.collaboratore.documento.cap.existing", new Object[] { request.getParameter("collaboratore-cap") },  request.getLocale() ));
	        			return caricaFormAutistaDocumenti(mav, autistaCorrente, request);
	        		}
    			}
    			if(request.getParameter("collaboratore-scadenza-cap") != null){
            		String dataIscrizioneAlboString = request.getParameter("collaboratore-scadenza-cap");
            		if(dataIscrizioneAlboString.equals("")){
            			if(autistaCorrente.getAutistaDocumento().getDocumentiCap() != null){
            				autistaCorrente.getAutistaDocumento().getDocumentiCap().setDataScadenzaCAP(null);
                		}
            		}else{
            			try{
            				Date dataScadenzaCAP = DateUtil.FormatoData_1.parse( dataIscrizioneAlboString );
	            			if(autistaCorrente.getAutistaDocumento().getDocumentiCap() != null){
	            				autistaCorrente.getAutistaDocumento().getDocumentiCap().setDataScadenzaCAP(dataScadenzaCAP);
	                		}else{
	                			autistaCorrente.getAutistaDocumento().setDocumentiCap( new DocumentiCap(null, dataScadenzaCAP, null) );
	                		}
	            		}catch(ParseException pe ){
	            			errors.rejectValue("", "errors.collaboratore.documento.cap.scadenza.data.invalid", new Object[] {  }, "");
	            		}
            		}
            		autistaManager.saveAutista(autistaCorrente);
    			}
    			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
				MultipartFile multipartFileDocIscrizioneRuolo = multipartRequest.getFile("collaboratore-documento-iscrizione-ruolo");
		        if(!multipartFileDocIscrizioneRuolo.isEmpty()){
		        	long fileSizeInBytes = multipartFileDocIscrizioneRuolo.getBytes().length;
		        	long fileSizeInKB = fileSizeInBytes / 1024;
		        	long fileSizeInMB = fileSizeInKB / 1024;
		        	if (fileSizeInMB >= Constants.MAX_SIZE_DOCUMENT_MB) {
		        		errors.rejectValue("", "errors.iscrizione.ruolo.documento.size", new Object[] { Constants.MAX_SIZE_DOCUMENT_MB }, "");
		        	}else{
		        		String fileName = UtilString.RimuoviCaratteriIllegaliFileName(multipartFileDocIscrizioneRuolo.getOriginalFilename());
		        		autistaCorrente.getAutistaDocumento().getDocumentiIscrizioneRuolo().setNomeFileDocumentoRuoloConducenti(fileName);
		        		autistaManager.saveAutista(autistaCorrente);
		        		AmazonStorageFiles.CaricareFile_AmazonWebService(request, multipartFileDocIscrizioneRuolo, autistaCorrente.getAutistaDocumento().getDocumentiIscrizioneRuolo(), Constants.AMAZON_FILE_RUOLO_CONDUCENTI);
		        	}
		        }
		        // UPLOAD DOCUMENTO CAP
		        MultipartFile multipartFileDocCap = multipartRequest.getFile("collaboratore-documento-cap");
		        if(!multipartFileDocCap.isEmpty()){
		        	long fileSizeInBytes = multipartFileDocCap.getBytes().length;
		        	long fileSizeInKB = fileSizeInBytes / 1024;
		        	long fileSizeInMB = fileSizeInKB / 1024;
		        	if (fileSizeInMB >= Constants.MAX_SIZE_DOCUMENT_MB) {
		        		errors.rejectValue("", "errors.collaboratore.documento.cap.size", new Object[] { Constants.MAX_SIZE_DOCUMENT_MB }, "");
		        	}else{
		        		String fileName = UtilString.RimuoviCaratteriIllegaliFileName(multipartFileDocCap.getOriginalFilename());
		        		if( autistaCorrente.getAutistaDocumento().getDocumentiCap() != null){
		        			autistaCorrente.getAutistaDocumento().getDocumentiCap().setNomeFileCAP(fileName);
		        		}else{
		        			autistaCorrente.getAutistaDocumento().setDocumentiCap( new DocumentiCap(null, null, fileName) );
		        		}
		        		autistaManager.saveAutista(autistaCorrente);
						AmazonStorageFiles.CaricareFile_AmazonWebService(request, multipartFileDocCap, autistaCorrente.getAutistaDocumento().getDocumentiCap(), Constants.AMAZON_FILE_CAP);
		        	}
		        }
	        }
	        if(request.getParameter("remove-collaboratore-documento-iscrizione-ruolo") != null && !autistaCorrente.getAutistaDocumento().isApprovatoDocumenti()){
	        	mav.addObject("tab_doc", "iscrizioneRuoloTab");
	    		Long idDocumentiIscrizioneRuolo = Long.parseLong(request.getParameter("remove-collaboratore-documento-iscrizione-ruolo"));
	    		DocumentiIscrizioneRuolo docIscrizioneRuolo = documentiIscrizioneRuoloManager.get(idDocumentiIscrizioneRuolo);
	    		AmazonStorageFiles.EliminareFile_AmazonWebService(request, docIscrizioneRuolo, Constants.AMAZON_FILE_RUOLO_CONDUCENTI);
	    		autistaCorrente.getAutistaDocumento().getDocumentiIscrizioneRuolo().setNomeFileDocumentoRuoloConducenti(null);
	    		autistaManager.saveAutista(autistaCorrente);
	    		
	    		return caricaFormAutistaDocumenti(mav, autistaCorrente, request);
	        }
	        if(request.getParameter("remove-collaboratore-documento-cap") != null && !autistaCorrente.getAutistaDocumento().isApprovatoDocumenti()){
	        	mav.addObject("tab_doc", "iscrizioneRuoloTab");
	    		Long idAutistaDocumentoCAP = Long.parseLong( request.getParameter("remove-collaboratore-documento-cap") );
	    		DocumentiCap docPatente = documentiCapManager.get(idAutistaDocumentoCAP);
	    		AmazonStorageFiles.EliminareFile_AmazonWebService(request, docPatente, Constants.AMAZON_FILE_CAP);
	    		autistaCorrente.getAutistaDocumento().getDocumentiCap().setNomeFileCAP(null);
	    		autistaManager.saveAutista(autistaCorrente);
	    		return caricaFormAutistaDocumenti(mav, autistaCorrente, request);
	        }
	        
			
			//----------------------
	        // LICENZA/A NCC AUTISTA
			//----------------------
	        if (request.isUserInRole(Constants.GEST_AUTISTA_ROLE) || request.isUserInRole(Constants.ADMIN_ROLE)){
	        	if(request.getParameter("licenza-ncc-approvato") != null && !request.getParameter("licenza-ncc-approvato").equals("")) {
	        		mav.addObject("tab_doc", "licenzeTab");
	        		Long idLicenzaApprovata = Long.parseLong( request.getParameter("licenza-ncc-approvato") );
	        		AutistaLicenzeNcc autistaLicenzeNcc = autistaLicenzeNccManager.get( idLicenzaApprovata );
	        		if( autistaLicenzeNcc.isApprovato() ){
	        			autistaLicenzeNcc.setApprovato(false);
	        		}else{
	        			autistaLicenzeNcc.setApprovato(true);
	        		}
	        		autistaLicenzeNccManager.saveAutistaLicenzeNcc(autistaLicenzeNcc);
	        		InsertDocumentiUtil docUtil = new InsertDocumentiUtil();
	        		boolean esito = docUtil.CheckInviaEmailContrattoAutista(autistaCorrente, velocityEngine, request);
	        		if(esito){
	        			saveMessage(request, "Email Contratto inviata "+autistaCorrente.getUser().getEmail());
	        		}
	        	}
			}
			if(request.getParameter("seleziona-comune-licenza") != null) {
	        	mav.addObject("tab_doc", "licenzeTab");
	        	String idComuneLicenza = request.getParameter("seleziona-comune-licenza");
	        	Comuni comuneLicenza = comuniManager.get( Long.parseLong(idComuneLicenza) );
	        	AutistaLicenzeNcc autistaLicenzeNccNew = new AutistaLicenzeNcc();
	        	autistaLicenzeNccNew.setComune(comuneLicenza);
	        	mav.addObject("autistaLicenzeNcc", autistaLicenzeNccNew);
	        	return caricaFormAutistaDocumenti(mav, autistaCorrente, request);
	        }
	        if(request.getParameter("aggiungi-licenza") != null) {
	        	mav.addObject("tab_doc", "licenzeTab");
	        	AutistaLicenzeNcc autistaLicenzeNccNew = new AutistaLicenzeNcc();
	    		String comuneId = request.getParameter("id-comune-licenza");
	    		Comuni comune = comuniManager.get( Long.parseLong( comuneId ) );
	    		autistaLicenzeNccNew.setComune( comune );
	    		if(request.getParameter("numero-licenza") != null && request.getParameter("numero-licenza").trim().equals("")){
		    		errors.rejectValue("", "errors.licenza.ncc.numero", new Object[] {  }, "");
		    		mav.addObject("autistaLicenzeNcc", autistaLicenzeNccNew);
		    	}else{
		    		String numeroLicenza = UtilString.RimuoviTuttiGliSpazi(request.getParameter("numero-licenza"));
		    		if( !StringUtils.isNumeric( numeroLicenza )){
		    			errors.rejectValue("", "errors.licenza.ncc.numero.invalid", new Object[] { numeroLicenza }, "");
		    			mav.addObject("autistaLicenzeNcc", autistaLicenzeNccNew);
		    		}else{
		    			autistaLicenzeNccNew.setNumeroLicenza( Integer.valueOf(numeroLicenza) ); //rimuovo eventuali 0 davanti
		    			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		    			MultipartFile multipartFileDocLicenzaNcc = multipartRequest.getFile("licenza-documento");
    			        if(multipartFileDocLicenzaNcc.isEmpty()){
    			        	errors.rejectValue("", "errors.documento.licenza", new Object[] {  }, "");
    			        	mav.addObject("autistaLicenzeNcc", autistaLicenzeNccNew);
    			        }else{
    			        	long fileSizeInBytes = multipartFileDocLicenzaNcc.getBytes().length;
    			        	long fileSizeInKB = fileSizeInBytes / 1024;
    			        	long fileSizeInMB = fileSizeInKB / 1024;
    			        	if (fileSizeInMB >= Constants.MAX_SIZE_DOCUMENT_MB) {
    			        		errors.rejectValue("", "errors.documento.licenza.size", new Object[] { Constants.MAX_SIZE_DOCUMENT_MB }, "");
    			        		mav.addObject("autistaSottoAutista", autistaLicenzeNccNew);
    			        	}else{
    			        		String fileName = UtilString.RimuoviCaratteriIllegaliFileName(multipartFileDocLicenzaNcc.getOriginalFilename());
    			        		autistaLicenzeNccNew.setNomeFileDocumentoLicenza(fileName);
    			        		autistaLicenzeNccNew.setAutista(autistaCorrente);
    			    			autistaLicenzeNccNew.setApprovato(false);
    				    		try{
    				    			autistaLicenzeNccManager.saveAutistaLicenzeNcc(autistaLicenzeNccNew);
    				    			AmazonStorageFiles.CaricareFile_AmazonWebService(request, multipartFileDocLicenzaNcc, autistaLicenzeNccNew, Constants.AMAZON_FILE_LICENZA_NCC);
    				    		}catch (final DataIntegrityViolationException DataIntegrViolException) {
    				    			saveError(request, getText("errors.licenza.ncc.existing", new Object[] { comune.getNomeComune()+" "+numeroLicenza }, request.getLocale() ));
    				    			return caricaFormAutistaDocumenti(mav, autistaCorrente, request);
    				    		}
    			        	}
    			        }
		    		}
		    	}
	    	}
	        if(request.getParameter("remove-licenza-ncc") != null) {
	        	mav.addObject("tab_doc", "licenzeTab");
	        	Long idLicenzaRemove = Long.parseLong( request.getParameter("remove-licenza-ncc") );
	        	if(!autistaLicenzeNccManager.get(idLicenzaRemove).isApprovato()){
	        		AmazonStorageFiles.EliminareFile_AmazonWebService(request, autistaLicenzeNccManager.get(idLicenzaRemove), Constants.AMAZON_FILE_LICENZA_NCC);
		    		autistaLicenzeNccManager.removeAutistaLicenzeNcc(idLicenzaRemove);
		    		return caricaFormAutistaDocumenti(mav, autistaCorrente, request);
	        	}
	    	}

	        
	        //----------------------------------------------------------------------------------------------------------------
	        // SOTTO AUTISTI AZIENDA, DIPENDENTI - RUOLO - PATENTE - CAP
	        //----------------------------------------------------------------------------------------------------------------
	        if (request.isUserInRole(Constants.GEST_AUTISTA_ROLE) || request.isUserInRole(Constants.ADMIN_ROLE)){
	        	if(request.getParameter("sotto-autista-approvato") != null && !request.getParameter("sotto-autista-approvato").equals("")) {
	        		mav.addObject("tab_doc", "dipendentiTab");
	        		Long idSottoAutistaApprovato = Long.parseLong( request.getParameter("sotto-autista-approvato") );
	        		AutistaSottoAutisti autistaSottoAutisti = autistaSottoAutistiManager.get( idSottoAutistaApprovato );
	        		if( autistaSottoAutisti.isApprovato() ){
	        			autistaSottoAutisti.setApprovato(false);
	        		}else{
	        			autistaSottoAutisti.setApprovato(true);
	        		}
	        		autistaSottoAutistiManager.saveAutistaSottoAutisti(autistaSottoAutisti);
	        		InsertDocumentiUtil docUtil = new InsertDocumentiUtil();
	        		boolean esito = docUtil.CheckInviaEmailContrattoAutista(autistaCorrente, velocityEngine, request);
	        		if(esito){
	        			saveMessage(request, "Email Contratto inviata "+autistaCorrente.getUser().getEmail());
	        		}
	        	}
			}
	        if(request.getParameter("seleziona-provincia-ruolo") != null) {
	        	mav.addObject("tab_doc", "dipendentiTab");
	        	String idProvinciaCameraCommercio = request.getParameter("seleziona-provincia-ruolo");
	        	Province provinciaRuolo = provinceManager.get( Long.parseLong(idProvinciaCameraCommercio) );
	        	DocumentiIscrizioneRuolo documentiIscrRuolo = new DocumentiIscrizioneRuolo(null, null, null, provinciaRuolo);
	        	AutistaSottoAutisti autistaSottoAutistaNew = new AutistaSottoAutisti(false, null, null, autistaCorrente, 
	        			documentiIscrRuolo, null, null);
	        	mav.addObject("autistaSottoAutista", autistaSottoAutistaNew);
	        	return caricaFormAutistaDocumenti(mav, autistaCorrente, request);
	        }
	        if(request.getParameter("inserisci-sotto-autista") != null) {
	        	mav.addObject("tab_doc", "dipendentiTab");
	        	DocumentiPatente documentiPatente = new DocumentiPatente(); 
	        	DocumentiCap documentiCap = new DocumentiCap();
	        	DocumentiIscrizioneRuolo documentiIscrRuolo =  new DocumentiIscrizioneRuolo();
	        	AutistaSottoAutisti autistaSottoAutistaNew = new AutistaSottoAutisti();
	        	autistaSottoAutistaNew.setAutista(autistaCorrente);
	        	Province provinciaRuolo =  provinceManager.get( Long.parseLong(request.getParameter("sotto-autista-id-provincia-ruolo")) );
	        	documentiIscrRuolo.setProvinciaRuoloConducenti( provinciaRuolo );
	        	autistaSottoAutistaNew.setDocumentiIscrizioneRuolo(documentiIscrRuolo);
	        	if(request.getParameter("sotto-autista-numero-ruolo") != null && request.getParameter("sotto-autista-numero-ruolo").equals("")){
		    		errors.rejectValue("", "errors.iscrizione.ruolo.numero", new Object[] {  }, "");
		    		mav.addObject("autistaSottoAutista", autistaSottoAutistaNew);
		    	}else{
		    		String NumeroRuolo = UtilString.RimuoviTuttiGliSpazi(request.getParameter("sotto-autista-numero-ruolo"));
		    		if( !StringUtils.isNumeric( NumeroRuolo )){
		    			errors.rejectValue("", "errors.iscrizione.ruolo.numero.invalid", new Object[] { NumeroRuolo }, "");
		    			mav.addObject("autistaSottoAutista", autistaSottoAutistaNew);
		    		}else{
		    			documentiIscrRuolo.setNumeroRuoloConducenti(NumeroRuolo);
		    			autistaSottoAutistaNew.setDocumentiIscrizioneRuolo(documentiIscrRuolo);
		    			if(request.getParameter("sotto-autista-data-iscrizione-ruolo") != null && request.getParameter("sotto-autista-data-iscrizione-ruolo").equals("")){
		    				errors.rejectValue("", "errors.iscrizione.ruolo.data", new Object[] {  }, "");
		    				mav.addObject("autistaSottoAutista", autistaSottoAutistaNew);
		    			}else{
		            		String dataIscrizioneAlboString = request.getParameter("sotto-autista-data-iscrizione-ruolo");
		            		Date dataIscrizioneAlbo = null;
		            		try{
		            			dataIscrizioneAlbo = DateUtil.FormatoData_1.parse( dataIscrizioneAlboString );
		            		}catch(ParseException pe ){
		            			errors.rejectValue("", "errors.iscrizione.ruolo.data.invalid", new Object[] {  }, "");
		            			mav.addObject("autistaSottoAutista", autistaSottoAutistaNew);
		            		}
		            		if(dataIscrizioneAlbo != null){
		            			documentiIscrRuolo.setDataIscrizioneRuoloConducenti(dataIscrizioneAlbo);
		            			autistaSottoAutistaNew.setDocumentiIscrizioneRuolo(documentiIscrRuolo);
		            			if(request.getParameter("sotto-autista-nome-autista") != null && request.getParameter("sotto-autista-nome-autista").equals("")){
		            				errors.rejectValue("", "errors.sotto.autista.nome.autista", new Object[] {  }, "");
		            				mav.addObject("autistaSottoAutista", autistaSottoAutistaNew);
		            			}else{
		            				autistaSottoAutistaNew.setNome(UtilString.PrimaLetteraMaiuscola(request.getParameter("sotto-autista-nome-autista")).trim());
		            				if(request.getParameter("sotto-autista-cognome-autista") != null && request.getParameter("sotto-autista-cognome-autista").equals("")){
			            				errors.rejectValue("", "errors.sotto.autista.cognome.autista", new Object[] {  }, "");
			            				mav.addObject("autistaSottoAutista", autistaSottoAutistaNew);
		            				}else{
			            				autistaSottoAutistaNew.setCognome(UtilString.PrimaLetteraMaiuscola(request.getParameter("sotto-autista-cognome-autista")).trim());
			            				if(request.getParameter("sotto-autista-patente-autista") != null && request.getParameter("sotto-autista-patente-autista").equals("")){
			            					errors.rejectValue("", "errors.sotto.autista.patente.autista", new Object[] {  }, "");
			            					mav.addObject("autistaSottoAutista", autistaSottoAutistaNew);
		            					}else{
			            					documentiPatente.setNumeroPatente( request.getParameter("sotto-autista-patente-autista").toUpperCase() );
			            					autistaSottoAutistaNew.setDocumentiPatente(documentiPatente);
			            					if(request.getParameter("sotto-autista-scadenza-patente-autista") != null && request.getParameter("sotto-autista-scadenza-patente-autista").equals("")){
			            						errors.rejectValue("", "errors.sotto.autista.patente.scadenza.data.autista", new Object[] {  }, "");
				            					mav.addObject("autistaSottoAutista", autistaSottoAutistaNew);
			            					}else{
			            						Date dataScadenzaPatente = null;
			            						try{
			            							dataScadenzaPatente = DateUtil.FormatoData_1.parse(request.getParameter("sotto-autista-scadenza-patente-autista"));
		            		            		}catch(ParseException pe ){
		            		            			errors.rejectValue("", "errors.collaboratore.documento.patente.scadenza.data.invalid", new Object[] {  }, "");
		            		            			mav.addObject("autistaSottoAutista", autistaSottoAutistaNew);
		            		            		}
			            						if(dataScadenzaPatente != null){
			            							documentiPatente.setDataScadenzaPatente(dataScadenzaPatente);
					            					autistaSottoAutistaNew.setDocumentiPatente(documentiPatente);
					            					if(request.getParameter("sotto-autista-cap-autista") != null && request.getParameter("sotto-autista-cap-autista").equals("")){
						            					errors.rejectValue("", "errors.sotto.autista.cap.autista", new Object[] {  }, "");
						            					mav.addObject("autistaSottoAutista", autistaSottoAutistaNew);
						            				}else{
					            						documentiCap.setNumeroCAP( request.getParameter("sotto-autista-cap-autista").toUpperCase() );
						            					autistaSottoAutistaNew.setDocumentiCap(documentiCap);
				
						            					if(request.getParameter("sotto-autista-scadenza-cap-autista") != null && request.getParameter("sotto-autista-scadenza-cap-autista").equals("")){
						            						errors.rejectValue("", "errors.sotto.autista.cap.scadenza.data.autista", new Object[] {  }, "");
							            					mav.addObject("autistaSottoAutista", autistaSottoAutistaNew);
						            					}else{
						            						Date dataScadenzaCAP = null;
						            						try{
						            							dataScadenzaCAP = DateUtil.FormatoData_1.parse(request.getParameter("sotto-autista-scadenza-cap-autista"));
					            		            		}catch(ParseException pe ){
					            		            			errors.rejectValue("", "errors.collaboratore.documento.cap.scadenza.data.invalid", new Object[] {  }, "");
					            		            			mav.addObject("autistaSottoAutista", autistaSottoAutistaNew);
					            		            		}
						            						if(dataScadenzaCAP != null){
						            							documentiCap.setDataScadenzaCAP(dataScadenzaCAP);
								            					autistaSottoAutistaNew.setDocumentiCap(documentiCap);
								            					MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
								            					MultipartFile multipartFileDocIscrizioneRuolo = multipartRequest.getFile("sotto-autista-documento-iscrizione-ruolo");
								            			        if(multipartFileDocIscrizioneRuolo.isEmpty()){
								            			        	errors.rejectValue("", "errors.iscrizione.ruolo.documento", new Object[] {  }, "");
								            			        	mav.addObject("autistaSottoAutista", autistaSottoAutistaNew);
								            			        }else{
								            			        	long fileSizeInBytes = multipartFileDocIscrizioneRuolo.getBytes().length;
								            			        	long fileSizeInKB = fileSizeInBytes / 1024;
								            			        	long fileSizeInMB = fileSizeInKB / 1024;
								            			        	if (fileSizeInMB >= Constants.MAX_SIZE_DOCUMENT_MB) {
								            			        		errors.rejectValue("", "errors.iscrizione.ruolo.documento.size", new Object[] { Constants.MAX_SIZE_DOCUMENT_MB }, "");
								            			        		mav.addObject("autistaSottoAutista", autistaSottoAutistaNew);
								            			        	}else{
								            			        		String fileName = UtilString.RimuoviCaratteriIllegaliFileName(multipartFileDocIscrizioneRuolo.getOriginalFilename());
										            					documentiIscrRuolo.setNomeFileDocumentoRuoloConducenti(fileName);
										            					autistaSottoAutistaNew.setDocumentiIscrizioneRuolo(documentiIscrRuolo);
										            					MultipartFile multipartFileDocFilePatenteFronte = multipartRequest.getFile("sotto-autista-documento-patente-fronte");
										            			        if(multipartFileDocFilePatenteFronte.isEmpty()){
										            			        	errors.rejectValue("", "errors.sotto.autista.documento.patente.fronte", new Object[] {  }, "");
										            			        	mav.addObject("autistaSottoAutista", autistaSottoAutistaNew);
										            			        }else{
										            			        	fileSizeInBytes = multipartFileDocFilePatenteFronte.getBytes().length;
										            			        	fileSizeInKB = fileSizeInBytes / 1024;
										            			        	fileSizeInMB = fileSizeInKB / 1024;
										            			        	if (fileSizeInMB >= Constants.MAX_SIZE_DOCUMENT_MB) {
										            			        		errors.rejectValue("", "errors.sotto.autista.documento.patente.fronte.size", new Object[] { Constants.MAX_SIZE_DOCUMENT_MB }, "");
										            			        		mav.addObject("autistaSottoAutista", autistaSottoAutistaNew);
										            			        	}else{
										            			        		fileName = UtilString.RimuoviCaratteriIllegaliFileName(multipartFileDocFilePatenteFronte.getOriginalFilename());
												            					documentiPatente.setNomeFilePatenteFronte(fileName);
												            					autistaSottoAutistaNew.setDocumentiPatente(documentiPatente);
												            					MultipartFile multipartFileDocFilePatenteRetro = multipartRequest.getFile("sotto-autista-documento-patente-retro");
												            			        if(multipartFileDocFilePatenteRetro.isEmpty()){
												            			        	errors.rejectValue("", "errors.sotto.autista.documento.patente.retro", new Object[] {  }, "");
												            			        	mav.addObject("autistaSottoAutista", autistaSottoAutistaNew);
												            			        }else{
												            			        	fileSizeInBytes = multipartFileDocFilePatenteRetro.getBytes().length;
												            			        	fileSizeInKB = fileSizeInBytes / 1024;
												            			        	fileSizeInMB = fileSizeInKB / 1024;
												            			        	if (fileSizeInMB >= Constants.MAX_SIZE_DOCUMENT_MB) {
												            			        		errors.rejectValue("", "errors.sotto.autista.documento.patente.retro.size", new Object[] { Constants.MAX_SIZE_DOCUMENT_MB }, "");
												            			        		mav.addObject("autistaSottoAutista", autistaSottoAutistaNew);
												            			        	}else{
												            			        		fileName = UtilString.RimuoviCaratteriIllegaliFileName(multipartFileDocFilePatenteRetro.getOriginalFilename());
														            					documentiPatente.setNomeFilePatenteRetro(fileName);
														            					autistaSottoAutistaNew.setDocumentiPatente(documentiPatente);
														            					MultipartFile multipartFileDocFileCAP = multipartRequest.getFile("sotto-autista-documento-cap");
														            			        if(multipartFileDocFileCAP.isEmpty()){
														            			        	errors.rejectValue("", "errors.sotto.autista.documento.cap", new Object[] {  }, "");
														            			        	mav.addObject("autistaSottoAutista", autistaSottoAutistaNew);
														            			        }else{
														            			        	fileSizeInBytes = multipartFileDocFileCAP.getBytes().length;
														            			        	fileSizeInKB = fileSizeInBytes / 1024;
														            			        	fileSizeInMB = fileSizeInKB / 1024;
														            			        	if (fileSizeInMB >= Constants.MAX_SIZE_DOCUMENT_MB) {
														            			        		errors.rejectValue("", "errors.sotto.autista.documento.cap.size", new Object[] { Constants.MAX_SIZE_DOCUMENT_MB }, "");
														            			        		mav.addObject("autistaSottoAutista", autistaSottoAutistaNew);
														            			        	}else{
														            			        		fileName = UtilString.RimuoviCaratteriIllegaliFileName(multipartFileDocFileCAP.getOriginalFilename());
																            					documentiCap.setNomeFileCAP(fileName);
																            					autistaSottoAutistaNew.setDocumentiCap(documentiCap);
															            						try{
															            							documentiCap = documentiCapManager.saveDocumentiCap(documentiCap);
															            						}catch (final DataIntegrityViolationException DataIntegrViolException) {
															            							saveError(request, getText("errors.collaboratore.documento.cap.existing", new Object[] { documentiCap.getNumeroCAP() }, request.getLocale() ));
															            							return caricaFormAutistaDocumenti(mav, autistaCorrente, request);
															            						}
															            						try{
															            							documentiPatente = documentiPatenteManager.saveDocumentiPatente(documentiPatente);
															            						}catch (final DataIntegrityViolationException DataIntegrViolException) {
															            							saveError(request, getText("errors.collaboratore.documento.patente.existing", new Object[] { documentiPatente.getNumeroPatente() }, request.getLocale() ));
															            							return caricaFormAutistaDocumenti(mav, autistaCorrente, request);
															            						}
															            						try{
															            							documentiIscrRuolo = documentiIscrizioneRuoloManager.saveDocumentiIscrizioneRuolo(documentiIscrRuolo);
															            						}catch (final DataIntegrityViolationException DataIntegrViolException) {
															            							saveError(request, getText("errors.iscrizione.ruolo.existing", new Object[] { documentiIscrRuolo.getNumeroRuoloConducenti()
																            		    					+" "+documentiIscrRuolo.getProvinciaRuoloConducenti().getNomeProvincia() }, request.getLocale() ));
															            							return caricaFormAutistaDocumenti(mav, autistaCorrente, request);
															            						}
																            					try{
																            						autistaSottoAutistaNew.setDocumentiCap(documentiCap);
																            						autistaSottoAutistaNew.setDocumentiPatente(documentiPatente);
																            						autistaSottoAutistaNew.setDocumentiIscrizioneRuolo(documentiIscrRuolo);
																            						autistaSottoAutistiManager.saveAutistaSottoAutisti(autistaSottoAutistaNew);
																            						AmazonStorageFiles.CaricareFile_AmazonWebService(request, multipartFileDocIscrizioneRuolo, autistaSottoAutistaNew.getDocumentiIscrizioneRuolo(), Constants.AMAZON_FILE_RUOLO_CONDUCENTI);
																            						AmazonStorageFiles.CaricareFile_AmazonWebService(request, multipartFileDocFilePatenteFronte, autistaSottoAutistaNew.getDocumentiPatente(), Constants.AMAZON_FILE_PATENTE_F);
																            						AmazonStorageFiles.CaricareFile_AmazonWebService(request, multipartFileDocFilePatenteRetro, autistaSottoAutistaNew.getDocumentiPatente(), Constants.AMAZON_FILE_PATENTE_R);
																            						AmazonStorageFiles.CaricareFile_AmazonWebService(request, multipartFileDocFileCAP, autistaSottoAutistaNew.getDocumentiCap(), Constants.AMAZON_FILE_CAP);
																            		    		}catch (final Exception exc) {
																            		    			saveError(request, getText("errors.detail", new Object[]{ exc.getMessage() }, request.getLocale() ));
																            		    			return caricaFormAutistaDocumenti(mav, autistaCorrente, request);
																            		    		}
														            			        	}
														            			        }
												            			        	}
												            			        }
										            			        	}
										            			        }
								            			        	}
								            			        }
						            						}
						            					}
						            				}
			            						}
				            				}
			            				}
			            			}
		            			}
		            		}
		            	}
		    		}
		    	}
	        }
	        if(request.getParameter("remove-sotto-autista") != null) {
	        	mav.addObject("tab_doc", "dipendentiTab");
	    		Long idAutistaSottoAutistaRemove = Long.parseLong( request.getParameter("remove-sotto-autista") );
	    		InsertDocumentiUtil.EliminaSottoAutistae_e_Files(request, idAutistaSottoAutistaRemove);
	    		return caricaFormAutistaDocumenti(mav, autistaCorrente, request);
	    	}
			
	        
	        //----------------------------------------------
	        // AUTOVEICOLO - INSERISCI CARTA DI CIRCOLAZIONE
	        //----------------------------------------------
	        if(request.isUserInRole(Constants.GEST_AUTISTA_ROLE) || request.isUserInRole(Constants.ADMIN_ROLE)){
	        	if(request.getParameter("carta-circolazione-approvata") != null && !request.getParameter("carta-circolazione-approvata").equals("")) {
	        		mav.addObject("tab_doc", "cartaCircolazioneTab");
	        		Long idCartaCircolApprovata = Long.parseLong( request.getParameter("carta-circolazione-approvata") );
	        		Autoveicolo autoveicolo = autoveicoloManager.get( idCartaCircolApprovata );
	        		if( autoveicolo.getAutoveicoloCartaCircolazione().isApprovatoCartaCircolazione() ){
	        			autoveicolo.getAutoveicoloCartaCircolazione().setApprovatoCartaCircolazione(false);
	        		}else{
	        			autoveicolo.getAutoveicoloCartaCircolazione().setApprovatoCartaCircolazione(true);
	        		}
	        		autoveicoloManager.saveAutoveicolo(autoveicolo);
	        		InsertDocumentiUtil docUtil = new InsertDocumentiUtil();
	        		boolean esito = docUtil.CheckInviaEmailContrattoAutista(autistaCorrente, velocityEngine, request);
	        		if(esito){
	        			saveMessage(request, "Email Contratto inviata "+autistaCorrente.getUser().getEmail());
	        		}
	        	}
			}
	        if(request.getParameter("inserisci-carta-circolazione") != null) {
	        	mav.addObject("tab_doc", "cartaCircolazioneTab");
	        	List<Autoveicolo> autoveicoloList = autoveicoloManager.getAutoveicoloByAutista(autistaCorrente.getId(), false);
	        	for(Autoveicolo auto_ite: autoveicoloList){
		        	MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
					MultipartFile multipartFileDocIscrizioneRuolo = multipartRequest.getFile("auto-documento-carta-circolazione-"+auto_ite.getId());
			        if(multipartFileDocIscrizioneRuolo != null && !multipartFileDocIscrizioneRuolo.isEmpty() && !auto_ite.getAutoveicoloCartaCircolazione().isApprovatoCartaCircolazione() ){
			        	long fileSizeInBytes = multipartFileDocIscrizioneRuolo.getBytes().length;
			        	long fileSizeInKB = fileSizeInBytes / 1024;
			        	long fileSizeInMB = fileSizeInKB / 1024;
			        	if (fileSizeInMB >= Constants.MAX_SIZE_DOCUMENT_MB) {
			        		errors.rejectValue("", "errors.auto.documento.carta.circolazione.size", new Object[] { Constants.MAX_SIZE_DOCUMENT_MB }, "");
			        	}else{
			        		String fileName = UtilString.RimuoviCaratteriIllegaliFileName(multipartFileDocIscrizioneRuolo.getOriginalFilename());
			        		auto_ite.getAutoveicoloCartaCircolazione().setNomeFileCartaCircolazione(fileName);
							AmazonStorageFiles.CaricareFile_AmazonWebService(request, multipartFileDocIscrizioneRuolo, auto_ite, Constants.AMAZON_FILE_AUTOVEICOLO_CARTA_CIRCOLAZIONE);
							autoveicoloManager.saveAutoveicolo(auto_ite);
			        	}
			        }
	        	}
	        }
	        if(request.getParameter("remove-auto-documento-carta-circolazione") != null) {
	        	mav.addObject("tab_doc", "cartaCircolazioneTab");
	    		Long idAutoCartaCircolazioneRemove = Long.parseLong( request.getParameter("remove-auto-documento-carta-circolazione") );
	    		Autoveicolo autoveicolo = autoveicoloManager.get( idAutoCartaCircolazioneRemove );
	    		if( !autoveicolo.getAutoveicoloCartaCircolazione().isApprovatoCartaCircolazione() ){
		    		//List<KeyVersion> keys = new ArrayList<KeyVersion>();
		    		//keys.add(new KeyVersion( autoveicolo.getClass().getName() + "/" + autoveicolo.getId().toString() +"/"+ Constants.AMAZON_FILE_SOTTO_AUTISTA_RUOLO_CONDUCENTI ));
		    		//AmazonStorageFiles.EliminareMultipleFile_AmazonWebService(keys);
		    		AmazonStorageFiles.EliminareFile_AmazonWebService(request, autoveicolo, Constants.AMAZON_FILE_AUTOVEICOLO_CARTA_CIRCOLAZIONE);
		    		autoveicolo.getAutoveicoloCartaCircolazione().setNomeFileCartaCircolazione(null);
		    		autoveicolo.getAutoveicoloCartaCircolazione().setApprovatoCartaCircolazione(false);
		    		autoveicoloManager.saveAutoveicolo(autoveicolo);
		    		return caricaFormAutistaDocumenti(mav, autistaCorrente, request);
	    		}
	        }
	        
	        if(errors != null && errors.getErrorCount() > 0 ){
				mav.addAllObjects(errors.getModel());
				return caricaFormAutistaDocumenti(mav, autistaCorrente, request);
			}
	        return caricaFormAutistaDocumenti(mav, autistaCorrente, request);
	     
    	}catch (final DataIntegrityViolationException dataIntegrViolException) {
    		saveError(request, getText("errors.save", locale));
            return mav;
        }
		catch (final Exception e) {
    		e.printStackTrace();
    		saveError(request, getText("errors.save", locale));
            return mav;
        }
    }

	
    private ModelAndView caricaFormAutistaDocumenti(ModelAndView mav, Autista autistaCorrente, HttpServletRequest request) throws Exception{
        mav.addObject("autista", autistaCorrente);
        List<AutistaLicenzeNcc>  autistaLicenzeNccList = autistaLicenzeNccManager.getAutistaLicenzeNcc_By_Autista(autistaCorrente.getId());
        mav.addObject("autistaLicenzeNccList", autistaLicenzeNccList);
        if(autistaCorrente.isAzienda() == true){
        	List<AutistaSottoAutisti>  autistaSottoAutistiList = autistaSottoAutistiManager.getAutistaSottoAutisti_By_Autista(autistaCorrente.getId());
        	mav.addObject("autistaSottoAutistiList", autistaSottoAutistiList);
        }
        List<Autoveicolo> autoveicoloList = autoveicoloManager.getAutoveicoloByAutista(autistaCorrente.getId(), false);
        mav.addObject("autoveicoloList", autoveicoloList);
        mav.addObject("maxMbDcument", Constants.MAX_SIZE_DOCUMENT_MB);
        
        DocumentiInfoUtil docUtil = new DocumentiInfoUtil(autistaCorrente);
        mav.addObject("tab_doc_check", docUtil.tabDocCheck );
        mav.addObject("documentiCompletatiEsclusoContratto", docUtil.documentiCompletatiEsclusoContratto );
        mav.addObject("documentiApprovatiEsclusoContratto", docUtil.documentiApprovatiEsclusoContratto );
        
        mav.addAllObjects( MenuAutistaAttribute.CaricaMenuAutista( autistaCorrente, 5, request ) );
		return mav;
    }
	
	
    @RequestMapping(value = "/insert-documenti", method = RequestMethod.GET)
    protected ModelAndView insertDocumenti(final HttpServletRequest request, final HttpServletResponse response,
    		@RequestParam(required = false, value = "idAutista") String idAutista) {
    	log.info("sono in insertDocumenti GET");
    	final Locale locale = request.getLocale();
    	ModelAndView mav = new ModelAndView("insert-documenti");
    	try{
    		if(request.getRemoteUser() != null){
    			Autista autista;
    			if (idAutista != null && !idAutista.trim().equals("") && 
    					(request.isUserInRole(Constants.GEST_AUTISTA_ROLE) || request.isUserInRole(Constants.ADMIN_ROLE)) ){
    				autista = autistaManager.get( Long.parseLong(idAutista) );
    				User userCorrente = getUserManager().getUserByUsername(request.getRemoteUser());
    				if( autista.getUser().getId() != userCorrente.getId() && (request.isUserInRole(Constants.GEST_AUTISTA_ROLE) && autista.getCommerciale() != null && autista.getCommerciale().getId() != userCorrente.getId()) 
    						|| (request.isUserInRole(Constants.GEST_AUTISTA_ROLE) && autista.getCommerciale() == null)  ){
    					saveError(request, getText("errors.violation.update.autista.commerciale", locale));
    					return new ModelAndView("redirect:/admin/gestioneAutista?idAutista="+idAutista);
    				}
    			}else{
    				User user = getUserManager().getUserByUsername(request.getRemoteUser());
        			autista = autistaManager.getAutistaByUser(user.getId());
    			}
    			
    			mav = caricaFormAutistaDocumenti(mav, autista, request).addObject("tab_doc", "collaboratoreTab");
				return mav;
    		}else{
    			return new ModelAndView("redirect:/login");
    		}
    	}catch(Exception exc){
    		exc.printStackTrace();
    		saveError(request, getText("errors.cancel", locale));
    		return new ModelAndView("insert-documenti");
    	}
    }
    
} //fine classe