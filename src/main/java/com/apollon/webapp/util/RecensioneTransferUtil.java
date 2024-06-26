package com.apollon.webapp.util;

import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import com.apollon.Constants;
import com.apollon.model.RicercaTransfert;
import com.apollon.model.User;
import com.apollon.util.UtilBukowski;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public class RecensioneTransferUtil extends ApplicationUtils {
	
	public static final String URL_PAGE_TOKEN_RECENSIONE = "token";
	
	private Long idUser;
	private String urlTockenPageScriviRecensone;
	private String codiceSconto;
	private Integer percentualeSconto;
	private Boolean codiceScontoUsato;
	private Boolean codiceScontoAttivo;
	private List<RicercaTransfert> ricercaTransfertList_Approvati;
	private List<RicercaTransfert> ricercaTransfertList_Totali; // lo uso per le pagine admin
	
	public RecensioneTransferUtil() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Costructor 
	 * 
	 * @param jsonString
	 * @param idUser
	 */
	public RecensioneTransferUtil(String jsonString, long idUser) {
		try {
			if( jsonString != null && !jsonString.contentEquals("") ){
				CaricaRecensioniCiente(jsonString, idUser);
			}else{
				GeneraInformazioniRecensioneCliente( idUser );
			}
		}catch(JSONException JsonExc ){
			GeneraInformazioniRecensioneCliente( idUser );
		}
	}
	
	
	
	private void CaricaRecensioniCiente(String jsonString, long idUser) throws JSONException {
		JSONObject json = new JSONObject(jsonString);
		urlTockenPageScriviRecensone = json.getString(Constants.UrlTockenPageScriviRecensone);
		codiceSconto = json.getString(Constants.CodiceScontoJSON);
		percentualeSconto = json.getInt(Constants.PercentualeScontoJSON);
		codiceScontoUsato = json.getBoolean(Constants.CodiceScontoUsatoJSON);
		codiceScontoAttivo = ricercaTransfertDao.CheckRecensioneApprovata_User( idUser );
		ricercaTransfertList_Approvati = ricercaTransfertDao.getTransferAcquistati_User_Approvati( idUser );
		ricercaTransfertList_Totali = ricercaTransfertDao.getTransferAcquistati_User_Totali( idUser ); // lo uso per le pagine admin
		this.idUser = idUser;
	}
	
	
	/**
	 * Metodo che serve a popolare le informazione di Recensione di un cliente all'acquisto di un transfer
	 * 
	 * @param idUser
	 * @return
	 */
	private void GeneraInformazioniRecensioneCliente( long idUser) {
		/*
		JSONObject datiRecensioniCliente = (user.getWebsite() != null && !user.getWebsite().contentEquals("") 
		? new JSONObject(user.getWebsite()) : new JSONObject() );
		*/
		User user = (User) userDao.get(idUser); //loadUserByEmail(email);
		//User user = userDao.get(-2l); //loadUserByEmail(email);

		JSONObject datiRecensioniCliente = new JSONObject();
		String TokenCodiceSconto = "";
		while( true ){
			TokenCodiceSconto = UtilBukowski.getRandomToken__LettGrandi_Numeri( Constants.LunghezzaTokenScontoEmailMarketing );
			if( ControllaTokenCodiceScontoUnivoco_Globale(TokenCodiceSconto) ) {
				datiRecensioniCliente.put(Constants.CodiceScontoJSON, TokenCodiceSconto);
				break;
			}
		}
		String tokenUrl = "";
		while( true ){
			tokenUrl = UtilBukowski.getRandomToken__LettPiccole_LettGrandi_Numeri( Constants.LUNGHEZZA_URL_TOKEN_GENERALE );
			if(userDao.getUser_by_TokenRecensione(tokenUrl) == null ) {
				datiRecensioniCliente.put(Constants.UrlTockenPageScriviRecensone, tokenUrl);
				break;
			}
		}
		datiRecensioniCliente.put(Constants.PercentualeScontoJSON, Constants.ValorePercentualeScontoRecensioneClienti);
		datiRecensioniCliente.put(Constants.CodiceScontoUsatoJSON, false);
		user.setWebsite( datiRecensioniCliente.toString() );
		userDao.saveUser(user);
		CaricaRecensioniCiente(datiRecensioniCliente.toString(), idUser); 
	}
	
	
	public Long getIdUser() {
		return idUser;
	}
	
	public String getUrlTockenPageScriviRecensone() {
		return urlTockenPageScriviRecensone;
	}

	public String getCodiceSconto() {
		return codiceSconto;
	}

	public Integer getPercentualeSconto() {
		return percentualeSconto;
	}

	public Boolean getCodiceScontoUsato() {
		return codiceScontoUsato;
	}
	
	public Boolean getCodiceScontoAttivo() {
		return codiceScontoAttivo;
	}

	public List<RicercaTransfert> getRicercaTransfertList_Approvati() {
		return ricercaTransfertList_Approvati;
	}

	public List<RicercaTransfert> getRicercaTransfertList_Totali() {
		return ricercaTransfertList_Totali;
	}

	

	
}
