package com.apollon.webapp.controller;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.apollon.model.MarcaAutoScout;
import com.apollon.model.ModelloAutoNumeroPosti;
import com.apollon.model.ModelloAutoScout;
import com.apollon.model.NumeroPostiAuto;
import com.apollon.service.ClasseAutoveicoloManager;
import com.apollon.service.GestioneApplicazioneManager;
import com.apollon.service.MarcaAutoScoutManager;
import com.apollon.service.ModelloAutoNumeroPostiManager;
import com.apollon.service.ModelloAutoScoutManager;
import com.apollon.service.NumeroPostiAutoManager;
import com.apollon.util.UtilString;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */

@Controller
public class AdminModelloAutoveicoloController extends BaseFormController {
    
	
	private GestioneApplicazioneManager gestioneApplicazioneManager;
	@Autowired
	public void setGestioneApplicazioneManager(GestioneApplicazioneManager gestioneApplicazioneManager) {
		this.gestioneApplicazioneManager = gestioneApplicazioneManager;
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
	
	private ClasseAutoveicoloManager classeAutoveicoloManager;
	@Autowired
	public void setClasseAutoveicoloManager(ClasseAutoveicoloManager classeAutoveicoloManager) {
		this.classeAutoveicoloManager = classeAutoveicoloManager;
	}
	
	private ModelloAutoNumeroPostiManager modelloAutoNumeroPostiManager;
	@Autowired
	public void setModelloAutoNumeroPostiManager(ModelloAutoNumeroPostiManager modelloAutoNumeroPostiManager) {
		this.modelloAutoNumeroPostiManager = modelloAutoNumeroPostiManager;
	}
	
	private NumeroPostiAutoManager numeroPostiAutoManager;
	@Autowired
	public void setNumeroPostiAutoManager(NumeroPostiAutoManager numeroPostiAutoManager) {
		this.numeroPostiAutoManager = numeroPostiAutoManager;
	}
	

	@RequestMapping(value = "/admin/admin-tableModelloAutoveicolo", method = RequestMethod.POST)
    public ModelAndView onTable_POST( @ModelAttribute("modelloAutoveicolo") final ModelloAutoScout modelloAutoveicoloMod, final BindingResult errors,
    		final HttpServletRequest request, final HttpServletResponse response) throws Exception{
		ModelAndView mav = new ModelAndView("admin/admin-table-modello-autoveicolo");
		final Locale locale = request.getLocale();
		
		try {
			
			if (request.getParameter("cancel") != null) {
				return CaricaModelloAutoveicolo(mav, new ModelloAutoScout(), null, null, null, false);
	        }
			
			if (request.getParameter("seleziona-marca-autoscout") != null) {
	        	long idMarcaAutoScout = Long.parseLong( request.getParameter("seleziona-marca-autoscout") );
	        	MarcaAutoScout marcaAutoScout =  marcaAutoScoutManager.getMarcaAutoScout_by_IdAutoScout( idMarcaAutoScout );
	        	
	        	if(request.getParameter("modifica").equals("true")){
	        		ModelloAutoScout modelloAutoScout = modelloAutoScoutManager.get(modelloAutoveicoloMod.getId());
	        		modelloAutoScout.setMarcaAutoScout(marcaAutoScout);
	        		return CaricaModelloAutoveicolo(mav, modelloAutoScout, null, null, null, true);
				}else{
					ModelloAutoScout modelloAutoScout = new ModelloAutoScout();
		        	modelloAutoScout.setMarcaAutoScout(marcaAutoScout);
					return CaricaModelloAutoveicolo(mav, modelloAutoScout, null, null, null, false);
				}
				
	        }
			
			// Check Campi ModelloAuto
			if (request.getParameter("modifica-modello") != null || request.getParameter("inserisci-modello") != null) {
				if(request.getParameter("marca-auto-scout") == null || request.getParameter("marca-auto-scout").trim().equals("")){
					errors.rejectValue("marcaAutoScout", "errors.required", new Object[] { "Marca" }, "");
				}else{
					MarcaAutoScout marcaAutoScout =  marcaAutoScoutManager.get(Long.parseLong(request.getParameter("marca-auto-scout")));
					modelloAutoveicoloMod.setMarcaAutoScout(marcaAutoScout);
				}
	        	
	        	if(request.getParameter("modello-auto") == null || request.getParameter("modello-auto").trim().equals("")){
					errors.rejectValue("name", "errors.required", new Object[] { "Nome Modello" }, "");
				}else{
					modelloAutoveicoloMod.setName(request.getParameter("modello-auto"));
				}
	        	
	        	final String[] groupRadioClassiAutoveicoli = request.getParameterValues("classe-autoveicolo");
                if(groupRadioClassiAutoveicoli != null) {
                    for(final String groupRadioCalleAutoveicoli_check : groupRadioClassiAutoveicoli) {
                        modelloAutoveicoloMod.setClasseAutoveicolo(classeAutoveicoloManager.get(Long.parseLong(groupRadioCalleAutoveicoli_check)));
                    }
                }else{
                	errors.rejectValue("classeAutoveicolo", "errors.required", new Object[] { "Classe Autoveicolo" }, "");
                }
                
                
                // TODO
                // Salvataggio Posti Auto Modello
                //final String[] groupCheckBoxNumeroPostiAuto = request.getParameterValues("numero-posti-auto");
                //for(final String groupCheckBoxNumeroPostiAuto_check : groupCheckBoxNumeroPostiAuto) {
                //}
                //boolean cbState = request.getParameter( "cb_id" ) != null;
                
                for(NumeroPostiAuto numPostiAuto_ite: numeroPostiAutoManager.getNumeroPostiAuto()){
                	try{
	                	if(request.getParameter("numero-posti-auto-"+numPostiAuto_ite.getId() ) != null){
	                		// AGGIUNGO
	                		ModelloAutoNumeroPosti modAutoNumPostiNew = new ModelloAutoNumeroPosti();
	                		modAutoNumPostiNew.setModelloAutoScout( modelloAutoScoutManager.get(modelloAutoveicoloMod.getId()) );
	                		modAutoNumPostiNew.setNumeroPostiAuto(numPostiAuto_ite);
	                		modelloAutoNumeroPostiManager.saveModelloAutoNumeroPosti(modAutoNumPostiNew);
	                	}else{ 
	                		// ELIMINO
	                		ModelloAutoNumeroPosti modAutoNumPosti = modelloAutoNumeroPostiManager
	                				.getModelloAutoNumeroPosti_By_ModelloAutoScout_NumeroPosti(modelloAutoveicoloMod.getId(), numPostiAuto_ite.getId());
	                		if(modAutoNumPosti != null){
	                			modelloAutoNumeroPostiManager.removeModelloAutoNumeroPosti(modAutoNumPosti.getId());
	                		}
	                	}
                	}catch (final DataIntegrityViolationException ee) {
                		ee.printStackTrace();
                	}
                }
                
	        	if(errors != null && errors.getErrorCount() > 0 ){
					mav.addAllObjects(errors.getModel());
					if(request.getParameter("modifica").equals("true")){ 
						return CaricaModelloAutoveicolo(mav, modelloAutoveicoloMod, null, null, null, true);
					}else{
						return CaricaModelloAutoveicolo(mav, modelloAutoveicoloMod, null, null, null, false);
					}
				}
			}
			
			if(request.getParameter("modifica-modello") != null) {
				ModelloAutoScout modelloAutoScoutUpdate = modelloAutoScoutManager.get(modelloAutoveicoloMod.getId());
				if (request.isUserInRole(Constants.ADMIN_ROLE)) {
	        		modelloAutoScoutUpdate.setMarcaAutoScout(modelloAutoveicoloMod.getMarcaAutoScout());
	        		modelloAutoScoutUpdate.setName(modelloAutoveicoloMod.getName());
				}
	        	modelloAutoScoutUpdate.setClasseAutoveicolo( modelloAutoveicoloMod.getClasseAutoveicolo() );
	        	try{
	        		modelloAutoScoutUpdate = modelloAutoScoutManager.saveModelloAutoScout(modelloAutoScoutUpdate);
		        	saveMessage(request, "Modello Modificato" );
		        	return CaricaModelloAutoveicolo(mav, modelloAutoScoutUpdate, null, null, modelloAutoveicoloMod.getId(), true);
		        }catch (final DataIntegrityViolationException dataIntegrViolException) {
		        	dataIntegrViolException.printStackTrace();
		    		saveError(request, dataIntegrViolException.getMessage());
		    		return CaricaModelloAutoveicolo(mav, null, null, null, modelloAutoveicoloMod.getId(), true);
		        }
			}
			
			
			if (request.getParameter("inserisci-modello") != null && request.isUserInRole(Constants.ADMIN_ROLE)) {
	        	ModelloAutoScout modelloAutoScoutNew = new ModelloAutoScout();
	        	modelloAutoScoutNew.setIdAutoScout( modelloAutoScoutManager.getMaxValue_idAutoScout() );
        		modelloAutoScoutNew.setMarcaAutoScout(modelloAutoveicoloMod.getMarcaAutoScout());
        		modelloAutoScoutNew.setName(modelloAutoveicoloMod.getName());
	        	modelloAutoScoutNew.setClasseAutoveicolo( modelloAutoveicoloMod.getClasseAutoveicolo() );
	        	try{
	        		modelloAutoScoutNew = modelloAutoScoutManager.saveModelloAutoScout(modelloAutoScoutNew);
		        	saveMessage(request, "Nuovo Modello Inserito" );
		        	return CaricaModelloAutoveicolo(mav, modelloAutoScoutNew, null, null, null, true);
		        }catch (final DataIntegrityViolationException dataIntegrViolException) {
		        	dataIntegrViolException.printStackTrace();
		    		saveError(request, dataIntegrViolException.getMessage());
		    		return CaricaModelloAutoveicolo(mav, modelloAutoScoutNew, null, null, null, false);
		        }
	        }
			
			
			if (request.getParameter("elimina-modello") != null && request.isUserInRole(Constants.ADMIN_ROLE)){
				try{
					modelloAutoScoutManager.removeModelloAutoScout(modelloAutoveicoloMod.getId());
					saveMessage(request, "Modello Eliminato" );
		        	return CaricaModelloAutoveicolo(mav, new ModelloAutoScout(), null, null, null, false);
		        }catch (final DataIntegrityViolationException dataIntegrViolException) {
		        	dataIntegrViolException.printStackTrace();
		    		saveError(request, dataIntegrViolException.getMessage());
		    		return CaricaModelloAutoveicolo(mav, null, null, null, modelloAutoveicoloMod.getId(), true);
		        }
	        }
			
			
			return CaricaModelloAutoveicolo(mav, null, null, null, null, false);

		}catch (final Exception e) {
    		e.printStackTrace();
    		saveError(request, getText("errors.save", locale));
            return CaricaModelloAutoveicolo(mav, null, null, null, null, false);
        }
    }
	

	
	private ModelAndView CaricaModelloAutoveicolo(ModelAndView mav, ModelloAutoScout modelloAutoScout, String search_type, 
			String query, Long idModelloAutoveicolo, boolean modifica) throws Exception{
		mav.addObject("modifica", modifica );
		mav.addObject("classeAutoveicoloList", classeAutoveicoloManager.getClasseAutoveicolo() );
		
		//Numero Anni Massimo per Immatricolazione Prima Classe
		String[] Parametri = UtilString.RimuoviTuttiGliSpazi(gestioneApplicazioneManager.getName("PARAMETRI_CALCOLO_TARIFFA_AUTOVEICOLO").getValueString()).split("-");
		
		int NumeroAnniMaxImmatricPrimaClasse = Integer.parseInt(Parametri[Constants.PARAM_MAX_ANNI_AUTO_PRIMA_CLASSE]);
		int AnnoAuttuale = Calendar.getInstance().get(Calendar.YEAR);
		int AnniMaxImmatricPrimaClasse = AnnoAuttuale - NumeroAnniMaxImmatricPrimaClasse;
		mav.addObject("AnniMaxImmatricPrimaClasse", AnniMaxImmatricPrimaClasse);
		mav.addObject("numeroPostiAutoList", numeroPostiAutoManager.getNumeroPostiAuto());
		
    	if(query == null || "".equals(query.trim()) && search_type == null || "".equals(search_type.trim())) {
    		mav.addObject("modelloAutoveicoloList", modelloAutoScoutManager.getModelliAutoScout_by_UtilizzatiDagliAutisti());
    		
        }else{
        	if(search_type.equals("marca")){
    			mav.addObject("modelloAutoveicoloList", modelloAutoScoutManager.getNomeModelloList_like_NomeMarca(query));
    			
    		}else if(search_type.equals("modello")){
    			mav.addObject("modelloAutoveicoloList", modelloAutoScoutManager.getNomeModelloList_like_NomeModello(query));
    			
    		}else if(search_type.equals("lista-completa")){
    			mav.addObject("modelloAutoveicoloList", modelloAutoScoutManager.getModelloAutoScout());
    		}
        }
    	if( idModelloAutoveicolo != null ){
			mav.addObject("modelloAutoveicolo", modelloAutoScoutManager.get( idModelloAutoveicolo ) );
			mav.addObject("modelloAutoNumeroPostiList", modelloAutoNumeroPostiManager.getModelloAutoNumeroPosti_By_IdModelloAutoScout( idModelloAutoveicolo ) );
    	}else{
    		mav.addObject("modelloAutoveicolo", new ModelloAutoScout() );
    	}
    	
    	return mav;
    }
	
	
    @RequestMapping(value = "/admin/admin-tableModelloAutoveicolo", method = RequestMethod.GET)
    protected ModelAndView onTable_GET( 
    		@RequestParam(required = false, value = "q") String query,
    		@RequestParam(required = false, value = "search_type") String search_type,
    		@RequestParam(required = false, value = "idModelloAutoveicolo") String idModelloAutoveicolo) {
    	log.debug("admin/admin-tableModelloAutoveicolo GET");
    	ModelAndView mav = new ModelAndView("admin/admin-table-modello-autoveicolo");
    	try{

			//Autista autistaCorrente = getAutistaManager().getAutistaByUser(user.getId());
    		if( idModelloAutoveicolo != null ){
    			mav = CaricaModelloAutoveicolo(mav, null, null, null, Long.parseLong( idModelloAutoveicolo ), true);
    		}else{
    			mav = CaricaModelloAutoveicolo(mav, new ModelloAutoScout(), search_type, query, null, false);
    		}

			return mav;

    	}catch(Exception exc){
    		exc.printStackTrace();
    		return mav;
    	}
    }
    

    
    
    
} //fine classe
