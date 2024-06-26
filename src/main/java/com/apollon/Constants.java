package com.apollon;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Constant values used throughout the application.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public final class Constants {

    private Constants() {
        // hide me
    }
    
    public static final Locale Locale_IT = new Locale("it", "IT");

    // Facebook OAuth (Client web apollotransfert.com e localhost:8443)
    public static final String FB_APP_ID = "669241703502668";
    public static final String FB_APP_SECRET = "1dea7ac6991cee9b95303e9c54dc037f";
    
    // ID client OAuth 2.0 APOLLOTRANSFERT PRODUZIONE (Client web apollotransfert.com e localhost:8443)
    public static final String GOOGLE_CLIENT_ID = "969110475011-87vqmm871usgcoikhcjlssfmrs3gndif.apps.googleusercontent.com";
    public static final String GOOGLE_CLIENT_SECRET = "joljzn9rtl1kX9hVVXmCoSUs";
    
    // REST JWT
    public static final String JWT_SECRET = "2RpoeElkFdkld40srSr3SS6dFFUd7dIBffeEEE_fff_Edd8LBkkk8ss_www50w_dfb";
    public static final String JWT_HEADER_AUTHORIZATION = "Authorization"; // NAME HEADER REQUEST
   
    // API STANDARD MESSAGE JSON vedere: https://cloud.google.com/storage/docs/json_api/v1/status-codes
    public static final String JSON_STATUS_CODE = "status_code"; //non lo uso
    public static final String JSON_MESSAGE = "message";
    //public static final String JSON_ERROR = "error";
    //public static final String JSON_ERRORS_ARRAY = "errors"; //non lo uso
    
    // TIPO RUOLI APP
    public static final String ADMIN_ROLE = "ROLE_ADMIN";
    public static final String GEST_AUTISTA_ROLE = "ROLE_GEST_AUTISTA";
    public static final String USER_ROLE = "ROLE_USER";
    public static final String COMMERCIALISTA_ROLE = "ROLE_COMMERCIALISTA";
    public static final String VENDITORE_ROLE = "ROLE_VENDITORE";
    public static final String VENDITORE_ATTRIBUTE_NAME = "id_venditore";
    
    // TIPO USER
    public static final int USER_NORMALE = 1;
    public static final int USER_AUTISTA = 2;
    public static final int USER_VENDITORE = 3;
    
    // TIPO RUOLI
    public static final String CLIENTE = "CLIENTE_ROLE";
    public static final String AUTISTA = "AUTISTA_ROLE";
    
    // UTENTI DI TEST e RicercaTransfert di TEST
    public static final long ID_USER_VENDITORE_TEST = -4l;
    public static final long ID_USER_ACQUIRENTE_TEST = 4l;
    public static final long ID_RICERCA_TRANSFERT_TEST = -1L;
    
    
    // PAGINE PUBBLICHE e URL
    public static final String PAGE_CHI_SIAMO = "chi-siamo";
    public static final String PAGE_CONTATTI = "contatti";
    public static final String PAGE_CONSIGLI_DI_VIAGGIO = "consigli-di-viaggio";
    public static final String PAGE_NCC_AGENZIE_VIAGGIO_SIAMO = "ncc-agenzie-viaggio";
    public static final String PAGE_NCC_AZIENDE = "ncc-aziende";
    public static final String PAGE_COLLABORATORI = "collaboratori";
    public static final String PAGE_LOGIN = "login";
    public static final String PAGE_SIGNUP = "signup";
    public static final String PAGE_RECOVERPASS = "recoverpass";
    public static final String PAGE_SCRIVI_RECENSIONE = "scrivi-recensione";
    public static final String FAKE_EMAIL = "@apollontrasfert.com";
    public static final String URL_TRANSFER = "transfer-";
    public static final String URL_VISUALZZA_CORSA_CLIENTE = "visualizza-corsa-cliente";
    public static final String URL_PRENOTA_CORSA = "prenota-corsa";
    public static final String URL_PREVENTIVO_CORSA = "preventivo-corsa";
    public static final String URL_LISTA_PREVENTIVI_CLIENTE = "lista-preventivi-cliente";
    public static final String URL_AGENDA_AUTISTA = "agenda-autista";
    public static final String URL_CORSA_AGENDA_AUTISTA = "corsa-agenda-autista";
    
    public static final String PAGE_VENDITORE_TARIFFE_TRANSFER = "venditore-tariffe-transfer";
    public static final String URL_VENDITORE_TARIFFE_TRANSFER = "venditore-tariffe-transfer-";
    public static final String PAGE_TARIFFE_TRANSFER = "tariffe-transfer";
    public static final String URL_TARIFFE_TRANSFER = "tariffe-transfer-";
    public static final String PAGE_TARIFFE_AUTISTI = "tariffe-autisti";
    
    // TIPO DEVICE USER
    public static final String DEVICE_USER_DESKTOP = "desktop";
    public static final String DEVICE_USER_MOBILE = "mobile";
    
    // STRIPE
    public static final String STRIPE_SECRET_KEY_TEST = "sk_test_Sob1p6jEVeztQZOVAT66dxzP"; // TEST
    public static final String STRIPE_SECRET_KEY_LIVE = ""; // LIVE
    public static final String STRIPE_PUBLISCHABLE_KEY_TEST = "pk_test_8JXDT4fNn8DcTegyJF6UYtbS"; // TEST
    public static final String STRIPE_PUBLISCHABLE_KEY_LIVE = "pk_live_3Lb0XKJkFOXMMR35EhXqc2jw"; // LIVE

    // PAYPAL vedere (https://developer.paypal.com/developer/accounts/ 
    // --> My Apps & Credentials --> REST API apps --> ApolloTransfert --> Sandbox / Live)
    
    
    public static final String PAYPAL_SANDBOX_CLIENT_ID = "AfAqXOW4p8wJCmXh_uiuN7FshLpQ3ZftkRssRh8KiOxFtTWwSfbhn4C-QcKvmuyfSupzxZViHXah_TWZ"; // TEST 
    public static final String PAYPAL_SANDBOX_CLIENT_SECRET = "EPUvcIqzi7asnYPUcNTyk6dAx3yUwkcCETEazhHox-wbzNrC2nPZgjhJRVH9DdWcaVd0xUsQHawKcH1_"; // TEST
    public static final String PAYPAL_LIVE_CLIENT_ID = "ATAD0-u1uPpfVmWFJaiiKpzEViM3MX75DOemO9jTMZrV7F8MYuRvGkcTOiJawEMPNNViJhV1ywgWgAnm"; // LIVE 
    public static final String PAYPAL_LIVE_CLIENT_SECRET = "EPX0Bfii437DHLXZcisxjzIhrTYX9NFEEbZOBN1OFNX24gL2mh6_-9Nr11xhR56Lzrll30l7QZIfejWt"; // LIVE
    public static final String PAYPAL_SANDBOX = "sandbox"; // TEST
    public static final String PAYPAL_LIVE = "production"; // LIVE
    
    

    // GEOPLUGIN.NET
    public static final String URL_GEO_PLUGIN_API_BASE = "http://www.geoplugin.net";
    
    //RECAPTCHA
    public static final String RECAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify";
    public static final String RECAPTCHA_PUBLIC = "6Leayj0UAAAAABzoe-yIr2qCjcH-E2LZnYkEyNxm";

    public static final String RECAPTCHA_RESPONSE = "g-recaptcha-response";
    
    // GOOGLE API KEY - VEDERE: https://console.cloud.google.com
    public static final String URL_MAP_GOOGLE_PLACES_API_BASE = "https://maps.googleapis.com/maps/api";
    public static final String API_KEY_7 = "AIzaSyBUhm73MU2OkXJOsuYF2PpjUEbC_efQbJo";
    public static final String API_KEY_6 = "AIzaSyC1cPK5XE6nDVlJif24p_c5PBZ8lcRTgwo";
    public static final String API_KEY_5 = "AIzaSyBXMUQUR7uYfizOAbnteHNmZT48IhYGeF8";
    
    // GOOGLE API KEY: TEST - PRODUZIONE - VEDERE: https://console.cloud.google.com
    public static final String API_KEY_3 = "AIzaSyARDB_NFxfFcQ7DIjJgMAIJJTc4Wgv8Wh0"; // TEST
    public static final String API_KEY_1 = "AIzaSyBcvkGGJw_24wrWMgBjhswIjV0Ew4jh4fg"; // TEST JS per ricerche maps in jsp
    public static final String API_KEY_8 = "AIzaSyCpLFPwxMfjMXHHJfmi2MtKKLyJ4hE7F3w"; // PRODUZIONE
    public static final String API_KEY_4 = "AIzaSyAr-ZTOlGk3Gyr6RIb3mBwh4F-F6o_X-Tc"; // PRODUZIONE JS per ricerche maps in jsp

    /*
     * LISTA TYPES ACCETTATI
     * vedere: https://developers.google.com/places/supported_types
    IN ORDINE: via (senza numero civico)[street_address], via (con numero civico)[street_number], locali con terreno annessi ad esempio via sardegna 35 Milano [premise],  
    piazza (senza numero civico)[route], luogo di interesse (villa borghese)[point_of_interest], Aeroporto[airport], 
    quartirere (Porta Venezia, Milano, MI, Italia (è un quartiere)[neighborhood], stazione ferroviaria[train_station], stazione metropolitana[subway_station], stazione bus[bus_station], stazione di transito[transit_station]
  	*/
    
    public static final String ITALIA = "IT";
    public static final String FRANCIA = "FR";
    public static final String SVIZZERA = "CH";
    public static final String AUSTRIA = "AT";
    public static final String SAN_MARINO = "SM"; public static final String PESARO_URBINO = "PU"; public static final String RIMINI = "RN"; 
    public static final String VATICANO = "VA"; public static final String ROMA = "RM"; 
    public static final String[] NAZIONI = new String[]{ITALIA,FRANCIA,SVIZZERA,VATICANO,SAN_MARINO};

    public static final List<String> GOOGLE_MAPS_TYPES_ACCETTATI = Arrays.asList( 
			//"political"
			"street_address"
			, "street_number"
			//, "premise"
			//, "route"
			//, "point_of_interest"
			, "airport"
			//, "neighborhood"
			, "train_station"
			//, "subway_station"
			//, "bus_station"
			//, "transit_station"
			, "museum"
			,"amusement_park"
			);
    
    public static final String AgendaAutista_RimborsoCliente = "agendaAutista_RimborsoCliente";
    public static final String AgendaAutista_Percentuale_Servizio = "agendaAutista_Percentuale_Servizio";
    public static final String AgendaAutista_TariffarioId_Andata = "agendaAutista_TariffarioId_Andata";
    public static final String AgendaAutista_TariffarioId_Ritorno = "agendaAutista_TariffarioId_Ritorno";
    public static final String AgendaAutista_PrezzoTotaleCliente_Temp = "agendaAutista_PrezzoTotaleCliente_Temp";
    

    public static final String CancellaRicezionePreventiviCliente = "cancella_ricezione_preventivi_cliente";
    public static final String RichiestaPreventivi_Inviata = "richiestaPreventivi_Inviata";
    public static final String RichiestaAutistaParticolare_Id = "richiestaAutistaParticolare_Id";
    public static final String RichiestaAutistaMultiplo_Id = "richiestaAutistaMultiplo_Id";
    public static final String RichiestaAutistaMultiploRimborsoCliente = "richiestaAutistaMultiploRimborsoCliente";
    public static final String RicTransfert_IdUser = "ricTransfert_IdUser";
    public static final String RicTransfert_Email = "ricTransfert_Email";
    public static final String RicTransfert_Nome = "ricTransfert_Nome";
    public static final String RicTransfert_Cognome = "ricTransfert_Cognome";
    public static final String RicTransfert_IpAddress = "ricTransfert_IpAddress";
    public static final String RicTransfert_Token = "ricTransfert_Token";
    public static final String RicTransfert_Address = "ricTransfert_Address";
    public static final String Ritardo_Id = "ritardo_Id";
    public static final String Supplementi_Id = "supplementi_Id";
    public static final String ProvinceTragitto_Id = "provinceTragitto_Id";
    
    public static final String Preventivo_validita_data = "preventivo_validita_data";
    public static final String Preventivo_inviato_cliente = "preventivo_inviato_cliente";
    
    public static final String NomePasseggeroJSON = "nomePasseggero";
    public static final String TelefonoPasseggeroJSON = "telefonoPasseggero";
    public static final String RecensioneJSON = "recensione";
    public static final String PunteggioStelleRecensioneJSON = "punteggioStelleRecensioneJSON";
    public static final int PunteggioStelleRecensioneValoreDefaultJSON = 3;
    public static final String RecensioneApprovataJSON = "recensioneApprovata";

    public static final String PaymentProviderIdJSON = "paymentProviderId";
    public static final String PaymentProviderTipoJSON = "paymentProviderTipo";
    public static final String TIPO_PAYMENT_STRIPE_1 = "TIPO_PAYMENT_STRIPE_1"; // vecchia gestione delle api fino a febbraio 2020 (non deprecate)
    public static final String TIPO_PAYMENT_STRIPE_2 = "TIPO_PAYMENT_STRIPE_2"; // nuova gestione delle api da febbraio 2020
    public static final String TIPO_PAYMENT_PAYPAL_1 = "TIPO_PAYMENT_PAYPAL_1"; // OLD: "PAY-" // NEW "PAYID-"
    public static final String TIPO_PAYMENT_PAYPAL_2 = "TIPO_PAYMENT_PAYPAL_2"; // OLD: "PAY-" // NEW "PAYID-"
    public static final String PaymentProviderNomeClienteJSON = "paymentProviderNomeCliente";
    public static final String PaymentProviderAmountJSON = "paymentProviderAmount";
    public static final String PaymentProviderFeeJSON = "paymentProviderFee";
    public static final String PaymentProviderRefundJSON = "paymentProviderRefund";
    
    public static final int LunghezzaTokenScontoEmailMarketing = 6; 
    public static final int ValorePercentualeScontoEmailMarketing = 5;
    public static final int ValorePercentualeScontoRecensioneClienti = 10;
    
    public static final String PercentualeScontoJSON = "percentualeSconto";
    public static final String CodiceScontoJSON = "codiceSconto";
    public static final String CodiceScontoUsatoJSON = "codiceUsato";
    public static final String VecchioPrezzoJSON = "vecchioPrezzo";
    
    public static final String UrlTockenPageScriviRecensone = "urlTockenPageScriviRecensone";
    public static final String Recensoni_Transfers = "recensoni_Transfers";

    // APPROVAZIONE CORSA
    public static final int IN_APPROVAZIONE = 1;
    public static final int APPROVATA = 2;
    public static final int NON_APPROVATA = 3;
    
    // PERCETUALE COMMISSIONE VENDITORE 
    public static final String VENDIDORE_PERC_MIN = "VEND_PERC_MIN";
    public static final String VENDIDORE_PERC_MAX = "VEND_PERC_MAX";
    public static final String VENDIDORE_PERC_DEFAULT = "VEND_PERC_DEFAULT";
    
    // TIPO SERVIZIO
    public static final String SERVIZIO_AGENDA_AUTISTA = "AGA";
    public static final String SERVIZIO_STANDARD = "ST";
    public static final String SERVIZIO_LUNGA_PERCORRENZA = "LP";
    public static final String SERVIZIO_AEROPORTO = "AERO";
    public static final String SERVIZIO_PORTO_NAVALE = "PORTO";
    public static final String SERVIZIO_PARTICOLARE = "PART";
    public static final String SERVIZIO_MULTIPLO = "MULTIP";
    
    // CLASSI AUROVEICOLI
    public static final long AUTO_ECONOMY = 1l;
    public static final long AUTO_PRIMA_CLASSE = 2l;
    public static final long AUTO_LUXURY = 3l;
    public static final long AUTO_VAN_ECONOMY = 4l;
    public static final long AUTO_VAN_PRIMA_CLASSE = 5l;
    
    // CALCOLO TARIFFE PARAM AUTOVEICOLI 
    public static final int PARAM_CORSA_STANDARD = 0;
    public static final int PARAM_AUTO_ECONOMY = 1;
    public static final int PARAM_AUTO_PRIMA_CLASSE = 2;
    public static final int PARAM_AUTO_LUXURY = 3;
    public static final int PARAM_AUTO_VAN_ECONOMY = 4;
    public static final int PARAM_AUTO_VAN_PRIMA_CLASSE = 5;
    public static final int PARAM_MAX_ANNI_AUTO_PRIMA_CLASSE = 6;
    public static final int PARAM_MAX_ANNI_AUTO = 7;
    
    public static final int KILOMETRI_CORSA_10 = 10; // 10 kilometri
    public static final int KILOMETRI_CORSA_25 = 25; // 20 kilometri
    public static final int KILOMETRI_CORSA_50 = 50; // 50 kilometri
    public static final int KILOMETRI_CORSA_100 = 100; // 100 kilometri
    public static final int KILOMETRI_CORSA_500 = 500; // 500 kilometri
    public static final int[] KILOMETRI_CORSE = {KILOMETRI_CORSA_10,KILOMETRI_CORSA_25,KILOMETRI_CORSA_50,KILOMETRI_CORSA_100,KILOMETRI_CORSA_500};
    
    // FASCE KILOMETRICHE
    // mydriver.com PREZZI PRIMA CLASSE (VALORE PROV. MILANO: 4.55)
    public static final String FASCE_KILOMETRICHE = ""
		/*mydriver.com*/+ "|1:40"
		/*mydriver.com*/+ "|2:20"
		/*OK*/+ "|3:13.81|4:10.52|5:9.24|6:7.95|7:6.67|8:5.38"
		/*mydriver.com*/+ "|9:4.1"
		/*OK*/+ "|10:3.93|11:3.77|12:3.61|13:3.45|14:3.28|15:3.12|16:2.96"
		/*mydriver.com*/+ "|17:2.8"
		/*OK*/+ "|18:2.75|19:2.7|20:2.65|21:2.6|22:2.55|23:2.5|24:2.45"
		/*mydriver.com*/+ "|25:2.40"
		/*OK*/+ "|26:2.37|27:2.34|28:2.31|29:2.28|30:2.25|31:2.22|32:2.19|33:2.16|34:2.13|35:2.1|36:2.07|37:2.04|38:2.01"
		/*mydriver.com*/+ "|39:1.98"
		/*OK*/+ "|40:1.96|41:1.94|42:1.93|43:1.91|44:1.9|45:1.88|46:1.86|47:1.85|48:1.83"
		/*mydriver.com*/+ "|49:1.82"
		/*OK*/+ "|50:1.81|51:1.81|52:1.81|53:1.8|54:1.8|55:1.8|56:1.79|57:1.79|58:1.79"
		/*mydriver.com*/+ "|59:1.79"
		/*OK*/+ "|60:1.77|61:1.76|62:1.74|63:1.73|64:1.72|65:1.70|66:1.69"
		/*mydriver.com*/+ "|67:1.68"
		/*OK*/+ "|68:1.68|69:1.68|70:1.68|71:1.68|72:1.68|73:1.68|74:1.68|75:1.68"
		/*mydriver.com*/+ "|76:1.68"
		/*OK*/+ "|77:1.67|78:1.66|79:1.65|80:1.65|81:1.64|82:1.63|83:1.62|84:1.62|85:1.61|86:1.60|87:1.59|88:1.59|89:1.58|90:1.57"
		/*mydriver.com*/+ "|91:1.57"
		/*OK*/+ "|92:1.57|93:1.57|94:1.57|95:1.57|96:1.57|97:1.56|98:1.56|99:1.56|100:1.56|101:1.56|102:1.56|103:1.55|104:1.55|105:1.55|106:1.55|107:1.55|108:1.54|109:1.54|110:1.54|111:1.54"
		/*mydriver.com*/+ "|112:1.54"
		/*OK*/+ "|113:1.54|114:1.54|115:1.54|116:1.54|117:1.53|118:1.53|119:1.53|120:1.53|121:1.52|122:1.52|123:1.52|124:1.51|125:1.51|126:1.51"
		/*mydriver.com*/+ "|127:1.51"
		/*OK*/+ "|128:1.51|129:1.51|130:1.51|131:1.5|132:1.5|133:1.5|134:1.49|135:1.49|136:1.49|137:1.48|138:1.48|139:1.48|140:1.47|141:1.47|142:1.47|143:1.46|144:1.46|145:1.46"
		/*mydriver.com*/+ "|146:1.46"
		/*OK*/+ "|147:1.7|148:1.71|149:1.50|150:1.29|151:1.7|152:1.7|153:1.7|154:1.7|155:1.7|156:1.7|157:1.7|158:1.7|159:1.7|160:1.7|161:1.7"
		/*mydriver.com*/+ "|162:1.47"
		/*OK*/+ "|163:1.47|164:1.47|165:1.47|166:1.47|167:1.47|168:1.47|169:1.47|170:1.47|171:1.47|172:1.47|173:1.47|174:1.47|175:1.47|176:1.47|177:1.47|178:1.47|179:1.47"
		/*mydriver.com*/+ "|180:1.47"
		/*OK*/+ "|181:1.47|182:1.47|183:1.47|184:1.47|185:1.47|186:1.47|187:1.46|188:1.46|189:1.46|190:1.46|191:1.46|192:1.46"
		/*OK*/+ "|193:1.46|194:1.46|195:1.46|196:1.46|197:1.46|198:1.46|199:1.46|200:1.46|201:1.46|202:1.46|203:1.45|204:1.45|205:1.45|206:1.45|207:1.45|208:1.45|209:1.45|210:1.45"
		/*mydriver.com*/+ "|211:1.45"
		/*OK*/+ "|212:1.45|213:1.45|214:1.45|215:1.45|216:1.45|217:1.45|218:1.44|219:1.44|220:1.44|221:1.44|222:1.44|223:1.43|224:1.43|225:1.43|226:1.43|227:1.43|228:1.43|229:1.43|230:1.42"
		/*OK*/+ "|231:1.42|232:1.42|233:1.42|234:1.42|235:1.42|236:1.42|237:1.42|238:1.42|239:1.42|240:1.41|241:1.41|242:1.41|243:1.41|244:1.41|245:1.41|246:1.41|247:1.41|248:1.41|249:1.41|250:1.41|251:1.41|252:1.41|253:1.41|254:1.41"
		/*mydriver.com*/+ "|255:1.41"
		/*OK*/+ "|300:1.40"
		/*mydriver.com*/+ "|303:1.40"
		/*mydriver.com*/+ "|378:1.39"
		/*OK*/+ "|350:1.39|400:1.38"
		/*mydriver.com*/+ "|442:1.38"
		/*OK*/+ "|450:1.38|500:1.38"
		/*mydriver.com*/+ "|505:1.38"
		/*OK*/+ "|550:1.37"
		/*mydriver.com*/+ "|580:1.37"
		/*OK*/+ "|600:1.37"
		/*mydriver.com*/+ "|649:1.37"
		/*OK*/+ "|650:1.37|700:1.37"
		/*mydriver.com*/+ "|715:1.37"
		/*OK*/+ "|750:1.37|800:1.36"
		/*mydriver.com*/+ "|840:1.36"
		/*OK*/+ "|850:1.33|900:1.26"
		/*mydriver.com*/+ "|948:1.21"
		/*OK*/+ "|950:1.21"
		/*mydriver.com*/+ "|963:1.19"
		/*OK*/+ "|1000:1.18|1100:17.5|1200:1.17|1300:1.16|1400:1.15|1500:1.14|1600:1.13|1700:1.12|1800:1.11|1900:1.10|2000:1.9|";
    
    /*
     * Una Richiesta Distanza Fatta con Google Maps viene salvata nella Tabella Data_Distanze 
     * e se è più vecchia di 6 settimane allora dovrà essere rieseguita
     */
    public static final int MAX_NUMERO_SETTIMANE__OLD_DATA_GOOGLE_MAPS_REQUEST_DISTANCE = 6; 
    
    // ATTRIBUTE FOR MENU AUTISTA
    public static final String ATTRIBUTE_AUTISTA_AUTOVEICOLO = "ATTRIBUTE_AUTISTA_AUTOVEICOLO";
    public static final String ATTRIBUTE_AUTISTA_ZONA_LAVORO = "ATTRIBUTE_AUTISTA_ZONA_LAVORO";
    public static final String ATTRIBUTE_AUTISTA_TARIFFE = "ATTRIBUTE_AUTISTA_TARIFFE";
    public static final String ATTRIBUTE_AUTISTA_AUTO_DISPONIBILITA = "ATTRIBUTE_AUTISTA_AUTO_DISPONIBILITA";
    public static final String ATTRIBUTE_AUTISTA_DOCUMENTI = "ATTRIBUTE_AUTISTA_DOCUMENTI";
    
    public static final int LUNGHEZZA_URL_TOKEN_GENERALE = 15;
    
    // EMAIL - SMS - PARAMETER 
    public static final String EMAIL_FROM_APOLLOTRANSFERT = "EMAIL_FROM_APOLLOTRANSFERT";
    public static final String EMAIL_FROM_NCCTRANSFERONLINE = "EMAIL_FROM_NCCTRANSFERONLINE";
    public static final String SMS_TITLE_APOLLOTRANSFERT = "APOLLO";
    public static final String SMS_TITLE_NCCTRANSFERONLINE = "NCCTRANSFERONLINE";
    public static final String SMS_STATUS_SUCCESS = "success";
    
    // template_email/account_user/
    public static final String VM_EMAL_ACCUNT_CREATED = "template_email/account_user/account_created.vm";
    public static final String VM_EMAIL_CONTATTI = "template_email/account_user/contatti.vm";
    public static final String VM_EMAL_PASSWORD_RECOVERY = "template_email/account_user/password_recovery.vm";
    public static final String VM_EMAL_PASSWORD_UPDATE = "template_email/account_user/password_updated.vm";
    // template_email/common/
    public static final String VM_EMAIL_INFO_FOOTER_INCLUDE = "template_email/common/info_footer_include.vm";
    // template_email/info_autista/
    public static final String VM_EMAIL_AUTISTA_INFO_INCLUDE = "template_email/info_autista/autista_info_include.vm";
    public static final String VM_EMAIL_AUTISTA_PROFILO_APPROVATO = "template_email/info_autista/autista_profilo_approvato.vm";
    public static final String VM_EMAIL_CONTRATTO_AUTISTA = "template_email/info_autista/contratto_autista.vm";
    public static final String VM_EMAIL_CORSA_CANCELLATA_AUTISTA = "template_email/info_autista/corsa_cancellata_autista.vm";
    public static final String VM_EMAIL_CORSA_CONFERMATA_AUTISTA = "template_email/info_autista/corsa_confermata_autista.vm";
    public static final String VM_EMAIL_CORSA_DISPONIBILE_AUTISTA = "template_email/info_autista/corsa_disponibile_autista.vm";
    public static final String VM_EMAIL_PREVENTIVO_ACQUISTATO_AUTISTA = "template_email/info_autista/preventivo_acquistato_autista.vm";
    public static final String VM_EMAIL_RICHIESTA_PREVENTIVO_AUTISTA = "template_email/info_autista/richiesta_preventivo_autista.vm";
    public static final String VM_EMAIL_AGENDA_AUTISTA_CORSA_ACQUISTA = "template_email/info_autista/agenda_autista_corsa_acquistata.vm";
    public static final String VM_EMAIL_INFO_MANCANTI_AUTISTA = "template_email/info_autista/info_mancanti_autista.vm";
    public static final String VM_SCRITTURA_PRIVATA_AUTISTA = "template_email/info_autista/scrittura_privata_autista.vm";
    public static final String VM_SCRITTURA_PRIVATA_AZIENDA = "template_email/info_autista/scrittura_privata_azienda.vm";
    public static final String VM_COMUNICAZIONE_AUTISTI_AGENDA_AUTISTA = "template_email/info_autista/comunicazione_autisti_agenda_autista.vm";
    // template_email/info_cliente/
    public static final String VM_EMAIL_AVVISO_CORSA_PRENOTATA_CLIENTE = "template_email/info_cliente/avviso_corsa_prenotata_cliente.vm";
    public static final String VM_EMAIL_AVVISO_RIMBORSO_ESEGUITO = "template_email/info_cliente/avviso_rimborso_eseguito.vm";
    public static final String VM_EMAIL_CONFERMA_PAGAMENTO_RITARDO_CLIETE = "template_email/info_cliente/conferma_pagamento_ritardo_cliete.vm";
    public static final String VM_EMAIL_CONFERMA_PAGAMENTO_SUPPLEMENTO_CLIETE = "template_email/info_cliente/conferma_pagamento_supplemento_cliete.vm";
    public static final String VM_EMAIL_CORSA_VENDUTA_CLIENTE = "template_email/info_cliente/corsa_venduta_cliente.vm";
    public static final String VM_EMAIL_LISTA_PREVENTIVI_CLIENTE = "template_email/info_cliente/lista_preventivi_cliente.vm";
    public static final String VM_EMAIL_PREVENTIVO_AUTISTA_INVIATO_CLIENTE = "template_email/info_cliente/preventivo_autista_inviato_cliente.vm";
    public static final String VM_EMAIL_SOLLECITO_RITARDO_CLIENTE = "template_email/info_cliente/sollecito_ritardo_cliente.vm";
    public static final String VM_EMAIL_SOLLECITO_SUPPLEMENTO_CLIENTE = "template_email/info_cliente/sollecito_supplemento_cliente.vm";
    // template_email/info_venditore/
    public static final String VM_EMAIL_CORSA_VENDUTA_VENDITORE = "template_email/info_venditore/corsa_venduta_venditore.vm";
    public static final String VM_TARIFFE_TRANSFER_VENDITORE = "template_email/info_venditore/tariffe_transfer_venditore.vm";
    // template_email/marketing_email/
    public static final String VM_EMAIL_AGENZIA_VIAGGIO_FIERA_MILANO_CODICE_SCONTO = "template_email/marketing_email/agenzia_viaggio_fiera_milano_codice_sconto.vm";
    public static final String VM_EMAIL_AGENZIA_VIAGGIO_FIERA_MILANO = "template_email/marketing_email/agenzia_viaggio_fiera_milano.vm";
    public static final String VM_EMAIL_AGENZIA_VIAGGIO_FIERA = "template_email/marketing_email/agenzia_viaggio_fiera.vm";
    public static final String VM_EMAIL_CONSIGLIA_CORSA = "template_email/marketing_email/consiglia_corsa.vm";
    public static final String VM_EMAIL_RECENSIONE_TRANSFER_CLIENTE_CODICE_SCONTO = "template_email/marketing_email/recensione_transfer_clienti_codice_sconto.vm";
    
    // attribute email
    public static final String VM_ATTRIBUTE_AUTISTA_INFO = "INCLUDE_AUTISTA_INFO";
    public static final String VM_ATTRIBUTE_FOOTER_EMAIL = "INCLUDE_FOOTER_EMAIL";
    public static final String VM_ATTRIBUTE_MESSAGE_SOURCE = "messages";
    
    public static final int MAX_SIZE_DOCUMENT_MB = 20;
    public static final int MAX_LENGHT_NOME_FILE = 100;

    public static final String STORAGE_DIRECTORY_UPLOADS = "/uploads/";
    


    public static final String AMAZON_STORE_BUCKET_PROD_NAME = "produzione-apollotransfert"; //Bucket name should not contain uppercase characters
    public static final String AMAZON_STORE_BUCKET_TEST_NAME = "test-apollotransfert"; //Bucket name should not contain uppercase characters
    public static final String AMAZON_FILE_CONTRATTO = "nomeFileContratto";
    public static final String AMAZON_FILE_CONTRATTO_2 = "nomeFileContratto_2";
    public static final String AMAZON_FILE_PATENTE_F = "nomeFilePatenteFronte";
    public static final String AMAZON_FILE_PATENTE_R = "nomeFilePatenteRetro";
    public static final String AMAZON_FILE_CARTA_IDENTITA_F = "nomeFileCartaIdentitaFronte";
    public static final String AMAZON_FILE_CARTA_IDENTITA_R = "nomeFileCartaIdentitaRetro";
    public static final String AMAZON_FILE_CAP = "nomeFileCAP";
    public static final String AMAZON_FILE_LICENZA_NCC = "nomeFileDocumentoLicenza";
    public static final String AMAZON_FILE_RUOLO_CONDUCENTI = "nomeFileDocumentoRuoloConducenti";
    public static final String AMAZON_FILE_AUTOVEICOLO_CARTA_CIRCOLAZIONE = "nomeFileCartaCircolazione";
    public static final String AMAZON_FILE_DOCUMENTO_AGGIUNTIVO = "documentoAggiuntivo";
    
    /*
     * Numero righe per tabella display:table
     */
    public static final String PAGE_SIZE_TABLE = "page_size_table";
    public static final int PAGE_SIZE_TABLE_30 = 30;
    public static final int PAGE_SIZE_TABLE_25 = 25;
    public static final int PAGE_SIZE_TABLE_20 = 20;
    public static final int PAGE_SIZE_TABLE_15 = 15;
    public static final int PAGE_SIZE_TABLE_10 = 10;
    public static final int PAGE_SIZE_TABLE_5 = 5;
    
    /**
     * Assets Version constant
     */
    public static final String ASSETS_VERSION = "assetsVersion";
    /**
     * The name of the ResourceBundle used in this application
     */
    public static final String BUNDLE_KEY = "ApplicationResources";

    /**
     * File separator from System properties
     */
    public static final String FILE_SEP = System.getProperty("file.separator");

    /**
     * User home from System properties
     */
    public static final String USER_HOME = System.getProperty("user.home") + FILE_SEP;

    /**
     * The name of the configuration hashmap stored in application scope.
     */
    public static final String CONFIG = "appConfig";

    /**
     * Session scope attribute that holds the locale set by the user. By setting this key
     * to the same one that Struts uses, we get synchronization in Struts w/o having
     * to do extra work or have two session-level variables.
     */
    public static final String PREFERRED_LOCALE_KEY = "org.apache.struts2.action.LOCALE";

    /**
     * The request scope attribute under which an editable user form is stored
     */
    public static final String USER_KEY = "userForm";

    /**
     * The request scope attribute that holds the user list
     */
    public static final String USER_LIST = "userList";

    /**
     * The request scope attribute for indicating a newly-registered user
     */
    public static final String REGISTERED = "registered";

    /**
     * The name of the user's role list, a request-scoped attribute
     * when adding/editing a user.
     */
    public static final String USER_ROLES = "userRoles";

    /**
     * The name of the available roles list, a request-scoped attribute
     * when adding/editing a user.
     */
    public static final String AVAILABLE_ROLES = "availableRoles";
    
    public static final String AVAILABLE_TIPO_RUOLI = "availableTipoRuoli";

    /**
     * The name of the CSS Theme setting.
     * @deprecated No longer used to set themes.
     */
    public static final String CSS_THEME = "csstheme";
    
    public static final String ENCODING_UTF_8 = "UTF-8";
}
