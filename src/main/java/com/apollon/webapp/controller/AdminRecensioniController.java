package com.apollon.webapp.controller;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
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
import com.apollon.model.RicercaTransfert;
import com.apollon.model.User;
import com.apollon.service.RicercaTransfertManager;
import com.apollon.util.UtilString;
import com.apollon.webapp.util.RecensioneTransferUtil;
import com.apollon.webapp.util.bean.RecensioneTransfer;
import com.apollon.webapp.util.controller.home.HomeUtil;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
@Controller
@RequestMapping("/admin/admin-recensioni*")
public class AdminRecensioniController extends BaseFormController {
    
	private RicercaTransfertManager ricercaTransfertManager;
	@Autowired
	public void setRicercaTransfertManager(RicercaTransfertManager ricercaTransfertManager) {
		this.ricercaTransfertManager = ricercaTransfertManager;
	}
	
	private static final String PageResult = "admin/admin-table-recensioni";
	
	@RequestMapping(method = RequestMethod.POST)
    public ModelAndView onTable_POST(@ModelAttribute("recensioneTransferUtil") final RecensioneTransferUtil recensioneTransferUtil, final BindingResult errors, final HttpServletRequest request, final HttpServletResponse response) throws Exception{
		ModelAndView mav = new ModelAndView( PageResult );
		final Locale locale = request.getLocale();
		if (request.getParameter("cancel") != null) { }
		try {
			User user = getUserManager().get( Long.parseLong(request.getParameter("user-id")) );
			if(user != null && request.getParameter("salva-recensione") != null) {
				List<RicercaTransfert> listaTransfr = user.getRecensioneTransfer().getRicercaTransfertList_Totali();
				for(RicercaTransfert ite_ric: listaTransfr) {
					if( true ) {
						String StellaPunteggio = request.getParameter("stella-punteggio-"+ite_ric.getId());
						String TextRecensione = request.getParameter("text-recensione-"+ite_ric.getId());
						
						TextRecensione = UtilString.TagliaVarChar1000(TextRecensione);
						JSONObject infoDatiPasseggero = HomeUtil.GetInfoDatiPasseggero(ite_ric);
						infoDatiPasseggero.put(Constants.PunteggioStelleRecensioneJSON, (StellaPunteggio.equals("0") ? 
								Constants.PunteggioStelleRecensioneValoreDefaultJSON : Integer.parseInt(StellaPunteggio)) );
						infoDatiPasseggero.put(Constants.RecensioneJSON, TextRecensione);

						if( request.getParameter("approvata-recensione-"+ite_ric.getId()) == null){
							infoDatiPasseggero.put(Constants.RecensioneApprovataJSON, false);
						}else{
							infoDatiPasseggero.put(Constants.RecensioneApprovataJSON, true);
						}
						
						ite_ric.setInfoPasseggero(infoDatiPasseggero.toString());
						ite_ric = ricercaTransfertManager.saveRicercaTransfert( ite_ric );
						System.out.println("sasassa: "+ite_ric.getInfoPasseggero());
					}
				}
				
				//mav.addObject("NomeMittente", request.getParameter("cliente-firstName"));
				saveMessage(request, getText("Recensione Salvata", request.getLocale()));
				return caricaAdminTable(mav, null, user.getId().toString(), false);
			}

	        return caricaAdminTable(mav, null, null, false);

    	}catch (final DataIntegrityViolationException dataIntegrViolException) {
    		saveError(request, getText("errors.chiaveDuplicata", locale));
    		dataIntegrViolException.printStackTrace();
    		return caricaAdminTable(mav, null, null, false);
        }
		catch (final Exception e) {
    		e.printStackTrace();
    		saveError(request, getText("errors.save", locale));
            return caricaAdminTable(mav, null, null, false);
        }
    }
	
	
    private ModelAndView caricaAdminTable(ModelAndView mav, String query, String idUser, boolean modifica) throws Exception{
    	if( query == null || "".equals(query.trim()) ) {
    		List<Object[]> recensioniList = ricercaTransfertManager.OrdinaPerRecencioniNonApprovate(null);
			mav.addObject("recensioniList", CaricaRicercaTransfert(recensioniList) );
        }else{
        	List<Object[]> recensioniList = ricercaTransfertManager.OrdinaPerRecencioniNonApprovate( Long.parseLong(idUser) );
			mav.addObject("recensioniList", CaricaRicercaTransfert(recensioniList) );
        }
    	if(idUser == null || "".equals(idUser.trim())) {
    		
    	}else{
    		mav.addObject("recensioneTransferUtil", getUserManager().getUser(idUser).getRecensioneTransfer() );
    	}
    	return mav;
    }
	
	
    @RequestMapping(method = RequestMethod.GET)
    protected ModelAndView onTable_GET( @RequestParam(required = false, value = "q") String query, @RequestParam(required = false, value = "idUser") String idUser ) {
    	ModelAndView mav = new ModelAndView( PageResult );
    	try{
			mav = caricaAdminTable(mav, query, idUser, false);
			return mav;
    	}catch(Exception exc){
    		exc.printStackTrace();
    		return mav;
    	}
    }
    

    private static List<RecensioneTransfer> CaricaRicercaTransfert(List<Object[]> ObjectList) {
		List<RecensioneTransfer> recensioneTransfertList = new ArrayList<RecensioneTransfer>(); 
		for(Object[] ite_object: ObjectList){
			//System.out.println("ssaasas: "+ite_object[2].toString().length());
			
			BigInteger id = (BigInteger) ite_object[0];
			String recensioneApprovata = ite_object[1] != null && ite_object[1].toString().length() == 1 ? String.valueOf(ite_object[1]) : (String) ite_object[1];
			
			
			String recensione = ite_object[2] != null && ite_object[2].toString().length() == 1 ? String.valueOf(ite_object[2]) : (String) ite_object[2];
			//String recensione = new String((byte[])ite_object[2], StandardCharsets.UTF_8);
			
			String punteggioStelleRecensione = ite_object[3] != null && ite_object[3].toString().length() == 1 ? String.valueOf(ite_object[3]) : (String) ite_object[3];
			BigInteger idUser = (BigInteger) ite_object[4];

			RecensioneTransfer recensioneTransfert = new RecensioneTransfer();  
			recensioneTransfert.setIdRicercaTransfert( id.longValue() );
			recensioneTransfert.setRecensioneApprovata( Boolean.valueOf(recensioneApprovata) );
			recensioneTransfert.setRecensione( recensione );
			recensioneTransfert.setPunteggioStelleRecensione( (punteggioStelleRecensione != null ? Integer.parseInt(punteggioStelleRecensione) : null) );
			
			User user = new User();
			user.setId( (idUser != null ? idUser.longValue() : null)  );
			user.setEmail( (String) ite_object[5] ); 
			user.getBillingInformation().setDenominazioneCliente( (String) ite_object[6] );
			
			recensioneTransfert.setUser(user);

			
			recensioneTransfertList.add(recensioneTransfert);
			//System.out.println( id +" "+aaa+" "+bbb+" "+ccc+" "+idUser);
		}
		return recensioneTransfertList;
	}
    
    
    
} //fine classe
