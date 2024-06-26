package com.apollon.webapp.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.apollon.Constants;
import com.apollon.model.Autista;
import com.apollon.model.Autoveicolo;
import com.apollon.model.MarcaAutoScout;
import com.apollon.model.ModelloAutoNumeroPosti;
import com.apollon.model.ModelloAutoScout;
import com.apollon.model.User;
import com.apollon.service.AutistaManager;
import com.apollon.service.AutoveicoloManager;
import com.apollon.service.GestioneApplicazioneManager;
import com.apollon.service.MarcaAutoScoutManager;
import com.apollon.service.ModelloAutoNumeroPostiManager;
import com.apollon.service.ModelloAutoScoutManager;
import com.apollon.util.UtilString;
import com.apollon.webapp.util.MenuAutistaAttribute;
import com.apollon.webapp.util.controller.autoveicolo.AutoveicoloUtil;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Controller
@RequestMapping("/insert-autoveicolo*")
public class InsertAutoveicoloController extends BaseFormController {

	
	private AutistaManager autistaManager;
    @Autowired
    public void setAutistaManager(final AutistaManager autistaManager) {
        this.autistaManager = autistaManager;
    }
    
    private AutoveicoloManager autoveicoloManager;
    @Autowired
    public void setAutoveicoloManager(final AutoveicoloManager autoveicoloManager) {
        this.autoveicoloManager = autoveicoloManager;
    }
    
    private MarcaAutoScoutManager marcaAutoScoutManager;
	@Autowired
	public void setMarcaAutoScoutManager(MarcaAutoScoutManager marcaAutoScoutManager) {
		this.marcaAutoScoutManager = marcaAutoScoutManager;
	}
	
	private ModelloAutoScoutManager modelloAutoScoutManager;
	@Autowired
	public void setModelloAutoScoutManager(ModelloAutoScoutManager modelloAutoScoutManager) {
		this.modelloAutoScoutManager = modelloAutoScoutManager;
	}
	
	private ModelloAutoNumeroPostiManager modelloAutoNumeroPostiManager;
	@Autowired
	public void setModelloAutoNumeroPostiManager(ModelloAutoNumeroPostiManager modelloAutoNumeroPostiManager) {
		this.modelloAutoNumeroPostiManager = modelloAutoNumeroPostiManager;
	}
	
	private GestioneApplicazioneManager gestioneApplicazioneManager;
	@Autowired
	public void setGestioneApplicazioneManager(GestioneApplicazioneManager gestioneApplicazioneManager) {
		this.gestioneApplicazioneManager = gestioneApplicazioneManager;
	}
	
	private static final String ModelloAutoNumeroPostiList = "ModelloAutoNumeroPostiList";
	private static final String MarcaAutoScout = "MarcaAutoScout";
	private static final String ModelloAutoScout = "ModelloAutoScout";
	private static final String Modifica = "Modifica";
	
	@RequestMapping(method = RequestMethod.POST)
    public ModelAndView onSubmitAutoveicolo(@ModelAttribute("autoveicolo") final Autoveicolo autoveicoloMod, final BindingResult errors, 
    		final HttpServletRequest request, final HttpServletResponse response) throws Exception{
		ModelAndView mav = new ModelAndView("insert-autoveicolo");
		log.info("sono in onSubmitAutoveicolo POST");
		final Locale locale = request.getLocale();
		try {
			Autista autista = autistaManager.get( Long.parseLong( request.getParameter("autista-id") ) );
			Autoveicolo auto = new Autoveicolo();
			boolean ModificaVar;
			if(request.getParameter(Modifica).equals("true")){
				ModificaVar = true; // MODIFICA
        	}else{
        		ModificaVar = false; // INSERIMENTO
        	}
			
			if (request.getParameter("cancel") != null) {
				return CaricaFormInsertAutoveicolo(request, mav, null, autista, false);
	        }

			if(autoveicoloMod.getId() != null && !autoveicoloMod.getId().equals("")){
				auto = autoveicoloManager.get( autoveicoloMod.getId() );
				// autoveicolo approvato non può essere modificato
				if(auto != null && auto.getAutoveicoloCartaCircolazione().isApprovatoCartaCircolazione() 
						&& request.getParameter("modifica-auto") != null && !request.isUserInRole(Constants.ADMIN_ROLE)){
					saveMessage(request, getText("autoveicolo.approvato.non.modificabile.messagio.autista", locale ) );
					return CaricaFormInsertAutoveicolo(request, mav, auto, autista, ModificaVar);
				}
			}

			if (request.getParameter("seleziona-marca-autoscout") != null) {
	        	long idMarcaAutoScout = Long.parseLong( request.getParameter("seleziona-marca-autoscout"));
	        	MarcaAutoScout marcaAutoScout = marcaAutoScoutManager.getMarcaAutoScout_by_IdAutoScout( idMarcaAutoScout );
	        	auto.setMarcaAutoScoutAppoggio(marcaAutoScout);
	        	return CaricaFormInsertAutoveicolo(request, mav, auto, autista, ModificaVar);
			}
			
			if (request.getParameter("seleziona-modello-autoscout") != null) {
	        	ModelloAutoScout modelloAutoScout = modelloAutoScoutManager.get( Long.parseLong(request.getParameter("seleziona-modello-autoscout")) );
	        	auto.setModelloAutoScoutAppoggio(modelloAutoScout);
	        	auto.setMarcaAutoScoutAppoggio(modelloAutoScout.getMarcaAutoScout());
        		return CaricaFormInsertAutoveicolo(request, mav, auto, autista, ModificaVar);
			}
			
			
			
			// CONTROLLI FIELD AUTOVEICOLO
			if(request.getParameter("marca-auto-scout").equals("") && request.getParameter("modello-auto-scout").equals("")){
				errors.rejectValue("modelloAutoNumeroPosti.modelloAutoScout.marcaAutoScout.idAutoScout", "errors.required", new Object[] { "Marca" }, "");
			}else{
				if(!request.getParameter("marca-auto-scout").equals("")){
					MarcaAutoScout marcaAutoScout =  marcaAutoScoutManager.getMarcaAutoScout_by_IdAutoScout(Long.parseLong(request.getParameter("marca-auto-scout")));
					auto.setMarcaAutoScoutAppoggio(marcaAutoScout);
				}
			}
			
			if(request.getParameter("modello-auto-scout").equals("")){
				errors.rejectValue("modelloAutoNumeroPosti.modelloAutoScout", "errors.required", new Object[] { "Modello" }, "");
			}else{
				ModelloAutoScout modelloAutoScout = modelloAutoScoutManager.get ( Long.parseLong(request.getParameter("modello-auto-scout")));
				auto.setModelloAutoScoutAppoggio(modelloAutoScout);
	        	auto.setMarcaAutoScoutAppoggio(modelloAutoScout.getMarcaAutoScout());
			}
			

			if(autoveicoloMod.getTarga().equals("")){
				errors.rejectValue("targa", "errors.required", new Object[] { "Targa" }, "");
			}else{
				// controllo targa esistente
				String Targa = StringUtils.deleteWhitespace(autoveicoloMod.getTarga().toUpperCase());
				if(!ModificaVar && autoveicoloManager.getAutoveicolo_By_Targa(autoveicoloMod.getTarga()) != null ){
					errors.rejectValue("", "errors.targa.esistente", new Object[] { Targa }, "");
				}
			}
			
			// numero posti auto
			if(request.getParameter("numero-posti-auto").equals("")){
				errors.rejectValue("modelloAutoNumeroPosti", "errors.required", new Object[] { "Numero Posti" }, "");
			}else{
				auto.setModelloAutoNumeroPosti( modelloAutoNumeroPostiManager.get(Long.parseLong(request.getParameter("numero-posti-auto"))) );
			}
			
			if(autoveicoloMod.getAnnoImmatricolazione().equals("")){
				errors.rejectValue("annoImmatricolazione", "errors.required", new Object[] { "Anno Immatricolazione" }, "");
			}else{
				if( !StringUtils.isNumeric( autoveicoloMod.getAnnoImmatricolazione() )){
					errors.rejectValue("", "errors.autoveicolo.anno.immatricolazione.format", new Object[] {}, "");
				}else{
					String[] Parametri = UtilString.RimuoviTuttiGliSpazi(gestioneApplicazioneManager.getName("PARAMETRI_CALCOLO_TARIFFA_AUTOVEICOLO").getValueString()).split("-");
					if( !AutoveicoloUtil.AnnoImmatricolazioneValido(Parametri, Integer.parseInt(autoveicoloMod.getAnnoImmatricolazione())) ){
						errors.rejectValue("", "errors.autoveicolo.max.anni.immatricolazione", new Object[] { AutoveicoloUtil.AnnoImmatricolazioneMassimo(Parametri) }, "");
					}
				}
			}
			
			if(errors != null && errors.getErrorCount() > 0 ){
				mav.addAllObjects(errors.getModel());
				return CaricaFormInsertAutoveicolo(request, mav, auto, autista, ModificaVar);
			}
			

			if (request.getParameter("modifica-auto") != null){
	        	Autoveicolo autoModificata = autoveicoloManager.get( autoveicoloMod.getId() );
	        	autoModificata.setAnnoImmatricolazione(autoveicoloMod.getAnnoImmatricolazione());
	        	ModelloAutoNumeroPosti modelloAutoNumPosti = modelloAutoNumeroPostiManager.get(Long.parseLong(request.getParameter("numero-posti-auto")));
	        	autoModificata.setModelloAutoNumeroPosti( modelloAutoNumPosti );
	        	String Targa = StringUtils.deleteWhitespace( autoveicoloMod.getTarga().toUpperCase());
	        	autoModificata.setTarga(Targa);
	        	try{
	        		autoModificata = autoveicoloManager.saveAutoveicolo(autoModificata);
				}catch (final DataIntegrityViolationException DataIntegrViolException) {
		    		DataIntegrViolException.printStackTrace();
		    		saveError(request, getText("errors.targa.esistente", new Object[] { Targa }, locale));
					return CaricaFormInsertAutoveicolo(request, mav, autoModificata, autista, true);
		        }
	        	saveMessage(request, "Autoveicolo Modificato"/*getText("user.saved", locale )*/ );
	        	return CaricaFormInsertAutoveicolo(request, mav, autoModificata, autista, true);
        	}
			
			if (request.getParameter("aggiungi") != null){
	        	Autoveicolo autoNew = new Autoveicolo();
	        	autoNew.setAutista(autista);
	        	autoNew.setAnnoImmatricolazione(autoveicoloMod.getAnnoImmatricolazione());
	        	ModelloAutoNumeroPosti modelloAutoNumPosti = modelloAutoNumeroPostiManager.get(Long.parseLong(request.getParameter("numero-posti-auto")));
	        	autoNew.setModelloAutoNumeroPosti( modelloAutoNumPosti );
	        	String Targa = StringUtils.deleteWhitespace( autoveicoloMod.getTarga().toUpperCase());
	        	autoNew.setTarga(Targa);
	        	try{
	        		autoNew = autoveicoloManager.saveAutoveicolo(autoNew);
				}catch (final DataIntegrityViolationException DataIntegrViolException) {
		    		DataIntegrViolException.printStackTrace();
		    		saveError(request, getText("errors.targa.esistente", new Object[] { Targa }, locale));
					return CaricaFormInsertAutoveicolo(request, mav, autoNew, autista, false);
		        }
	    		
	        	/*
	    		//inserisci modello auto al database se non esiste e se la marca fa match con la marca del database //Non ha piu senso ma lo faccio comunque
	    		MarcaAutoveicolo marca = marcaAutoveicoloManager.getNomeMarcaAutoveicolo(autoveicoloMod.getMarca());
	    		if(marca != null){
	    			//controllo se il modello è inserito
	    			ModelloAutoveicolo modello = modelloAutoveicoloManager.getNomeModelloAutoveicolo(autoveicoloMod.getNomeModello(), marca.getId());
	    			if(modello == null){
	    				ModelloAutoveicolo insertModello = new ModelloAutoveicolo();
	    				insertModello.setMarcaAutoveicolo(marca);
	    				insertModello.setCode(autoveicoloMod.getNomeModello());
	    				insertModello.setTitle(autoveicoloMod.getNomeModello());
	    				insertModello
	    				ModelloAutoveicolo salvatoModello = modelloAutoveicoloManager.saveModelloAutoveicolo(insertModello);
	    			}
	    		}
	    		*/
	    		
	        	saveMessage(request, "Autoveicolo Aggiunto"/*getText("user.saved", locale )*/ );
	        	return CaricaFormInsertAutoveicolo(request, mav, autoNew, autista, false);
	        }
	
    	}catch (final Exception e) {
    		e.printStackTrace();
    		Autista autista = autistaManager.get( Long.parseLong( request.getParameter("autista-id")));
			autoveicoloMod.setAutista( autista );
			Autoveicolo autoveicolo = new Autoveicolo();
			autoveicolo.setAutista( autista );
			return CaricaFormInsertAutoveicolo(request, mav, null, null, false);
        }
		return CaricaFormInsertAutoveicolo(request, mav, null, null, false);
    }


	private ModelAndView CaricaFormInsertAutoveicolo(HttpServletRequest request, ModelAndView mav, Autoveicolo autoveicolo, 
			Autista autista, boolean modificaVar) throws Exception {
		if(autista == null){
			autista = autistaManager.get( Long.parseLong( request.getParameter("autista-id")));
		}
		if(autoveicolo == null){
			autoveicolo = new Autoveicolo();
			
		}else{
			if(modificaVar == true){ // MODIFICA AUTOVEICOLO
				if(autoveicolo.getMarcaAutoScoutAppoggio() != null && autoveicolo.getMarcaAutoScoutAppoggio().getId() != 
						autoveicolo.getModelloAutoNumeroPosti().getModelloAutoScout().getMarcaAutoScout().getId() ){
					mav.addObject(MarcaAutoScout, autoveicolo.getMarcaAutoScoutAppoggio() );
					if(autoveicolo.getModelloAutoScoutAppoggio() != null){
						mav.addObject(ModelloAutoScout, autoveicolo.getModelloAutoScoutAppoggio() );
						mav.addObject(ModelloAutoNumeroPostiList, NumeroPostiAutoSelectNew(autoveicolo.getModelloAutoScoutAppoggio(), request));
					}
				}else if( autoveicolo.getModelloAutoScoutAppoggio() != null && autoveicolo.getModelloAutoScoutAppoggio().getId() !=
						autoveicolo.getModelloAutoNumeroPosti().getModelloAutoScout().getId() ){
					mav.addObject(MarcaAutoScout, autoveicolo.getMarcaAutoScoutAppoggio() );
					if(autoveicolo.getModelloAutoScoutAppoggio() != null){
						mav.addObject(ModelloAutoScout, autoveicolo.getModelloAutoScoutAppoggio() );
						mav.addObject(ModelloAutoNumeroPostiList, NumeroPostiAutoSelectNew(autoveicolo.getModelloAutoScoutAppoggio(), request));
					}
				}else{
					mav.addObject(MarcaAutoScout, autoveicolo.getModelloAutoNumeroPosti().getModelloAutoScout().getMarcaAutoScout() );
					mav.addObject(ModelloAutoScout, autoveicolo.getModelloAutoNumeroPosti().getModelloAutoScout() );
					mav.addObject(ModelloAutoNumeroPostiList, NumeroPostiAutoSelectNew(autoveicolo.getModelloAutoNumeroPosti().getModelloAutoScout(), request));
				}

			}else{ // NUOVO AUTOVEICOLO
				mav.addObject(MarcaAutoScout, autoveicolo.getMarcaAutoScoutAppoggio() );
				if(autoveicolo.getModelloAutoScoutAppoggio() != null){
					mav.addObject(ModelloAutoScout, autoveicolo.getModelloAutoScoutAppoggio() );
					mav.addObject(ModelloAutoNumeroPostiList, NumeroPostiAutoSelectNew(autoveicolo.getModelloAutoScoutAppoggio(), request));
				}
			}
		}
		
		autoveicolo.setAutista(autista);
		mav.addObject("autoveicolo", autoveicolo);
		
		List<Autoveicolo> autoveicoloList = autoveicoloManager.getAutoveicoloByAutista(autista.getId(), true);
		mav.addObject("autoveicoloList", autoveicoloList);
    	
		mav.addObject(Modifica, modificaVar);
		mav.addAllObjects( MenuAutistaAttribute.CaricaMenuAutista( autista, 1, request ) );
		return mav;
	}
	
	
    @RequestMapping(method = RequestMethod.GET) 
    protected ModelAndView insertAutoveicoloGET(final HttpServletRequest request, final HttpServletResponse response, 
    		@RequestParam(required = false, value = "idAutista") String idAutista){
    	log.info("sono in insertAutoveicoloGET GET");
    	final Locale locale = request.getLocale();
    	ModelAndView mav = new ModelAndView("insert-autoveicolo");
    	try{
    		if(request.getRemoteUser() != null){
    			Autista autista;
    			User userCorrente = getUserManager().getUserByUsername(request.getRemoteUser());
    			if (idAutista != null && !idAutista.trim().equals("") && 
    					(request.isUserInRole(Constants.GEST_AUTISTA_ROLE) || request.isUserInRole(Constants.ADMIN_ROLE)) ){
    				autista = autistaManager.get( Long.parseLong(idAutista) );
    				if( autista.getUser().getId() != userCorrente.getId() && (request.isUserInRole(Constants.GEST_AUTISTA_ROLE) && 
    						autista.getCommerciale() != null && autista.getCommerciale().getId() != userCorrente.getId())
    							|| (request.isUserInRole(Constants.GEST_AUTISTA_ROLE) && autista.getCommerciale() == null  )){
    					saveError(request, getText("errors.violation.update.autista.commerciale", locale));
    					return new ModelAndView("redirect:/admin/gestioneAutista?idAutista="+idAutista);
    				}

    			}else{
        			autista = autistaManager.getAutistaByUser(userCorrente.getId());
    			}
    			
    			if(request.getParameter("elimina") != null){
    				long idAutoveicolo = Long.parseLong(request.getParameter("elimina"));
    				Autoveicolo autoveicolo = autoveicoloManager.getConInfoAutista(idAutoveicolo);
					autoveicolo.setAutoveicoloCancellato(true);
					autoveicoloManager.saveAutoveicolo(autoveicolo);
					//autoveicoloManager.removeAutoveicolo(idAutoveicolo);
    				saveMessage(request, "autoveicolo disattivato"/*getText("user.saved", locale )*/ );
    				return CaricaFormInsertAutoveicolo(request, mav, null, autista, false);

    			}else if(request.getParameter("modifica") != null){
    				Autoveicolo autoveicolo = autoveicoloManager.getConInfoAutista(Long.parseLong(request.getParameter("modifica")));
    				return CaricaFormInsertAutoveicolo(request, mav, autoveicolo, autista, true);

    			}else if(request.getParameter("riabilita-auto") != null){
    				long idAutoveicolo = Long.parseLong(request.getParameter("riabilita-auto"));
    				Autoveicolo autoveicolo = autoveicoloManager.getConInfoAutista(idAutoveicolo);
					autoveicolo.setAutoveicoloCancellato(false);
					autoveicoloManager.saveAutoveicolo(autoveicolo);
    				saveMessage(request, "autoveicolo riabilitato"/*getText("user.saved", locale )*/ );
    				return CaricaFormInsertAutoveicolo(request, mav, null, autista, false);
    			}
    			
    			return CaricaFormInsertAutoveicolo(request, mav, null, autista, false);
    			
    		}else{
    			return new ModelAndView("redirect:/login");
    		}
    	}catch(Exception exc){
    		exc.printStackTrace();
    		saveError(request, getText("errors.cancel", locale));
    		return new ModelAndView("home-user");
    	}
    }

    
    
    private List<ModelloAutoNumeroPosti> NumeroPostiAutoSelectNew(ModelloAutoScout modelloAutoScout, HttpServletRequest request){
    	List<ModelloAutoNumeroPosti> modelloAutoNumeroPostiList = new ArrayList<ModelloAutoNumeroPosti>();
		modelloAutoNumeroPostiList.addAll( modelloAutoScout.getModelloAutoNumeroPosti() );
		return modelloAutoNumeroPostiList;
    }
   

}
