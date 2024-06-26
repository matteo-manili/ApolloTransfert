package com.apollon.webapp.controller;

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
import com.apollon.model.Autista;
import com.apollon.model.AutistaZone;
import com.apollon.model.Autoveicolo;
import com.apollon.model.ClasseAutoveicolo;
import com.apollon.model.User;
import com.apollon.service.AutistaManager;
import com.apollon.service.AutoveicoloManager;
import com.apollon.service.ClasseAutoveicoloManager;
import com.apollon.service.GestioneApplicazioneManager;
import com.apollon.service.ModelloAutoScoutManager;
import com.apollon.util.UtilString;
import com.apollon.webapp.util.MenuAutistaAttribute;
import com.apollon.webapp.util.bean.Tariffe_Zone;
import com.apollon.webapp.util.controller.autoveicolo.AutoveicoloUtil;
import com.apollon.webapp.util.controller.tariffe.TariffeUtil;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Controller
public class InsertTariffeController extends BaseFormController {
    
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
	
	private GestioneApplicazioneManager gestioneApplicazioneManager;
    @Autowired
    public void setGestioneApplicazioneManager(final GestioneApplicazioneManager gestioneApplicazioneManager) {
        this.gestioneApplicazioneManager = gestioneApplicazioneManager;
    }
	
	@RequestMapping(value = "/insert-tariffe", method = RequestMethod.POST)
    public ModelAndView onSubmitTariffeNew( @ModelAttribute("autista") final Autista autistaMod, final BindingResult errors,
    		final HttpServletRequest request, final HttpServletResponse response){
		log.info("sono in onSubmitTariffeNew POST");
		final Locale locale = request.getLocale();
		ModelAndView mav = new ModelAndView("insert-tariffe");
		try{
	        mav = CaricaFormAutistaTariffe(mav, new Autista(), request);
            return mav;
    	}catch (final DataIntegrityViolationException dataIntegrViolException) {
    		saveError(request, getText("errors.chiaveDuplicata", locale));
            return mav;
        }catch (final Exception e) {
    		e.printStackTrace();
    		saveError(request, getText("errors.save", locale));
            return mav;
        }
    }

    private ModelAndView CaricaFormAutistaTariffe(ModelAndView mav, Autista autistaCorrente, HttpServletRequest request) throws Exception{
    	String[] Parametri = UtilString.RimuoviTuttiGliSpazi(gestioneApplicazioneManager.getName("PARAMETRI_CALCOLO_TARIFFA_AUTOVEICOLO").getValueString()).split("-");
    	mav.addAllObjects( MenuAutistaAttribute.CaricaMenuAutista( autistaCorrente, 3, request ) );
        mav.addObject("autista", autistaCorrente);
        List<ClasseAutoveicolo> classeAutoveicoloList = classeAutoveicoloManager.getClasseAutoveicolo();
        mav.addObject("anno_minino_prima_classe", AutoveicoloUtil.DammiAnniMaxImmatricPrimaClasse_e_Luxury(Parametri));
        mav.addObject("anno_minino_luxury_intermedia", AutoveicoloUtil.DammiAnniMaxImmatric_Intermedia_Luxury(Parametri));
        mav.addObject("classi_auto_list", classeAutoveicoloList);
        // Creazione Tabella Generale Auto per ClasseAutoveicolo
        mav.addObject("modelloAutoveicoloList", modelloAutoScoutManager.getModelliAutoScout_by_UtilizzatiDagliAutisti());
        String tariffe_Zone_ListStringObject = "tariffe_Zone_List";
        if(autistaCorrente != null){
	        List<Autoveicolo> autoList = autoveicoloManager.getAutoveicoloByAutista(autistaCorrente.getId(), false);
	        List<AutistaZone> zoneList = autistaManager.lazyAutistaZone(autistaCorrente.getId());
	        // Autoveicoli Tariffe
			List<Tariffe_Zone> tariffe_Zone_List = TariffeUtil.getTariffe_Zone_List_New(Parametri, zoneList, autoList, Constants.FASCE_KILOMETRICHE, Constants.KILOMETRI_CORSE);
	        mav.addObject(tariffe_Zone_ListStringObject, tariffe_Zone_List);
	        
	        /*
	        // Creazione Tabella Generale Tariffe ClasseAutoveicolo per Provincia 
	        List<Tariffe_Provincie_ClasseAuto> tariffeProvClasseAutoList = new ArrayList<Tariffe_Provincie_ClasseAuto>();
	        for(AutistaZone AutistaZone_ite : zoneList) {
	        	Tariffe_Provincie_ClasseAuto tariffeProvClasseAuto = new Tariffe_Provincie_ClasseAuto();
	        	if(AutistaZone_ite.getComuni() != null){
	        		tariffeProvClasseAuto.setProvincia(AutistaZone_ite.getComuni().getProvince());
	        	}else if(AutistaZone_ite.getProvince() != null){
	        		tariffeProvClasseAuto.setProvincia(AutistaZone_ite.getProvince());
	        	}
	        	List<Tariffe_Provincie_ClasseAuto.ClasseAutoveicoloTariffe> classeAutoveicoloTariffeList = 
	        			new ArrayList<Tariffe_Provincie_ClasseAuto.ClasseAutoveicoloTariffe>();
	        	for(ClasseAutoveicolo classeAutoveicolo_ite : classeAutoveicoloList) {
	        		BigDecimal tariffaST = new BigDecimal(
	        				TariffeUtil.DammiTariffa_ValoreAuto_Per_Table(Parametri, classeAutoveicolo_ite, tariffeProvClasseAuto.getProvincia(), Constants.SERVIZIO_STANDARD));
	        		BigDecimal tariffaLP = new BigDecimal(
	        				TariffeUtil.DammiTariffa_ValoreAuto_Per_Table(Parametri, classeAutoveicolo_ite, tariffeProvClasseAuto.getProvincia(), Constants.SERVIZIO_LUNGA_PERCORRENZA));
	        		Tariffe_Provincie_ClasseAuto.ClasseAutoveicoloTariffe classeAutoveicoloTariffe = 
	        				new Tariffe_Provincie_ClasseAuto.ClasseAutoveicoloTariffe(classeAutoveicolo_ite, tariffaST.setScale(2, RoundingMode.HALF_EVEN), tariffaLP.setScale(2, RoundingMode.HALF_EVEN));
	        		classeAutoveicoloTariffeList.add(classeAutoveicoloTariffe);
	        	}
	        	tariffeProvClasseAuto.setClasseAutoveicoloTariffe(classeAutoveicoloTariffeList);
	        	tariffeProvClasseAutoList.add(tariffeProvClasseAuto);
	        }
	        mav.addObject(tariffeProvClasseAutoListStringObject, tariffeProvClasseAutoList);
	        */
        }else{
        	mav.addObject(tariffe_Zone_ListStringObject, null);
        }
    	return mav;
    }
	
	
    @RequestMapping(value = "/insert-tariffe", method = RequestMethod.GET)
    protected ModelAndView insertTariffeNew(final HttpServletRequest request, final HttpServletResponse response,
    		@RequestParam(required = false, value = "idAutista") String idAutista) {
    	log.info("sono in insertTariffeNew GET");
    	final Locale locale = request.getLocale();
    	ModelAndView mav = new ModelAndView("insert-tariffe");
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
    			mav = CaricaFormAutistaTariffe(mav, autista, request);
				return mav;
    		}else{
    			return new ModelAndView("redirect:/login");
    		}
    	}catch(Exception exc){
    		exc.printStackTrace();
    		saveError(request, getText("errors.cancel", locale));
    		return new ModelAndView("insert-tariffe");
    	}
    }

    
} //fine classe
