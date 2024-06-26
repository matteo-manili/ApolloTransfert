package com.apollon.webapp.util;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import com.apollon.Constants;
import com.apollon.dao.VisitatoriDao;
import com.apollon.model.Visitatori;
import com.apollon.util.UrlConnection;


/**
 * @author Matteo - matteo.manili@gmail.com
 * 
	- Max Mind find location using Ip Address - per prendere info address IP
	esempio: http://www.mkyong.com/java/java-find-location-using-ip-address/
	scaricare il database: http://dev.maxmind.com/geoip/legacy/geolite/ (GeoLite City: GeoLiteCity.dat)
	installazione http://dev.maxmind.com/geoip/legacy/install/city/ 
	leggere il .dat con le api: http://dev.maxmind.com/geoip/legacy/downloadable/ (com.maxmind.geoip.LookupService)
	
	
	- Memory Caching and Other Options
	
	The following options can be passed as the second parameter to the LookupService constructor.
	
	GEOIP_STANDARD - Read database from file system. Uses the least memory.
	GEOIP_MEMORY_CACHE - Load database into memory. This provides faster performance but uses more memory
	GEOIP_CHECK_CACHE - Check for updated database. If database has been updated, reload file handle and/or memory cache.
	GEOIP_INDEX_CACHE - Cache only the most frequently accessed index portion of the database, resulting in faster lookups than GEOIP_STANDARD, 
	but less memory usage than GEOIP_MEMORY_CACHE. This is useful for larger databases such as GeoIP Legacy Organization and GeoIP Legacy City. 
	Note: for GeoIP Legacy Country, Region and Netspeed databases, GEOIP_INDEX_CACHE is equivalent to GEOIP_MEMORY_CACHE.
	
	
	// -----------------------------------
	 * 
	 * 
	 * http://ipinfo.io/ 
	
 * 
 */

public class InfoUserConnectAddressMain extends ApplicationUtils {

	private static final Log log = LogFactory.getLog(InfoUserConnectAddressMain.class);
	private static VisitatoriDao visitatoriDao = (VisitatoriDao) contextDao.getBean("VisitatoriDao"); 

	public static String DammiIPaddress(final HttpServletRequest request) throws Exception{
		String IpAddress = "";
		IpAddress = request.getHeader("X-FORWARDED-FOR");
		if(IpAddress == null) {
			IpAddress = request.getRemoteAddr();
			if(IpAddress.equals("0:0:0:0:0:0:0:1")) {
				log.debug("DammiIPaddress NULL");
			}
		}
		return IpAddress;
	}
	

	/**
	 * Salva il visitatore e setta i valori di latitudine e longitudine alla classe 
	 */
	public static Visitatori SalvaVisitatore(final HttpServletRequest request) throws Exception {
		try{
			final String IpAddress = DammiIPaddress(request);
			String DeviceType = DammiDeviceType(request);
    		String PaginaProvenienza = ControllerUtil.getPageRefererURL(request);
    		/*
    		 * IP-API.COM (questo da i risultati più veritieri. FINORA IL MIGLIORE PRENDE POSIZIONE GIUSTA CELLULARI)
			{"as":"AS24608 H3G-AS",* "city":"Rome",* "country":"Italy",* "countryCode":"IT",* "isp":"H3G Italy",* "lat":41.8929,* "lon":12.4825,* "org":"H3G Italy",
			 "query":"37.227.188.159",* "region":"62",* "regionName":"Latium",* "status":"success",* "timezone":"Europe/Rome",* "zip":""} 
    		 */
			StringBuilder sb = new StringBuilder("http://ip-api.com/json/"+IpAddress);
			JSONObject jsonObj = new JSONObject( UrlConnection.HttpConnection( sb ) );
			Visitatori visitatore = null;
			if( jsonObj != null && jsonObj.toString().contains("status") && jsonObj.getString("status").equals("success") ){
				visitatore = new Visitatori(IpAddress, DeviceType, PaginaProvenienza, new Date(), jsonObj.getString("org"), jsonObj.getString("isp"), 
						jsonObj.getString("region"), jsonObj.getString("countryCode"), jsonObj.getString("country"), jsonObj.getString("regionName"), jsonObj.getString("city"), 
						jsonObj.getString("zip"), jsonObj.getDouble("lat"), jsonObj.getDouble("lon"));
				visitatore = visitatoriDao.saveVisitatori(visitatore);
			}
			
			return CheckNullVisitatore(visitatore);

			/**
			 * MAX_MIND (questo è quello statico con il file grosso NON RINTRACCIA BENE I CELLULARI)
			 */
			/*
			Info_IP_Address result = dammiINFO_MaxMind_Plugin(request.getServletContext(), ipAddress );
			if(result != null){
				visit = new Visitatori(ipAddress, "MAX_MIND", newDate, "", "", 
						result.getRegionCode(), result.getCountryCode(), result.getCountryName(), 
						result.getRegionName(), result.getCity(), result.getPostalCode(), result.getLatitude(), result.getLongitude());
				visitatoriDao.saveVisitatori(visit);
			}
			*/
			
			/**
			 *  IPINFO.IO (A VOLTE NON CARICA LE INFORMAZIONI E NON RINTRACCIA BENE I CELLULARI )
			 *  NON VA BENE PERCHé SPESSO NON TROVA GLI IP
			 */
			/*
			sb = new StringBuilder( "http://ipinfo.io/"+ipAddress+"/geo"  );
			jsonObj = new JSONObject( UrlConnection.HttpConnection( sb ) );
			if(jsonObj != null && jsonObj.toString().contains("ip") && jsonObj.toString().contains("city") && jsonObj.toString().contains("region") && 
					jsonObj.toString().contains("country") && jsonObj.toString().contains("loc") && jsonObj.toString().contains("postal") && 
						jsonObj.toString().contains("loc")){
				jsonObj.getString("ip") ;
				jsonObj.getString("city") ;
				jsonObj.getString("region") ;
				jsonObj.getString("country") ;
				String stringLatLng = jsonObj.getString("loc") ;
					String[] parts = stringLatLng.split(",");
					double latitude = Double.parseDouble(parts[0]);
					double longitude = Double.parseDouble(parts[1]);
				jsonObj.getString("postal") ;
				Visitatori visit = new Visitatori(jsonObj.getString("ip"), "IPINFO.IO", newDate, "", "", 
						"", jsonObj.getString("country"), "", jsonObj.getString("region"), jsonObj.getString("city"), 
						jsonObj.getString("postal"), latitude, longitude);
				visitatoriDao.saveVisitatori(visit);
			}
			*/
		}catch(Exception ex){
			System.out.println(ex.getMessage());
			return CheckNullVisitatore(null);
		}
	}

	/**
	 * Mi ritorna se è una chiamata da un Mobile o da un Desktop
	 */
	public static String DammiDeviceType(final HttpServletRequest request){
		if(request.getHeader("User-Agent").indexOf("Mobile") != -1) {
		    //you're in mobile land
			return Constants.DEVICE_USER_MOBILE;
		} else {
		    //nope, this is probably a desktop
			return Constants.DEVICE_USER_DESKTOP;
		}
	}
	
	private static Visitatori CheckNullVisitatore(Visitatori visitatore){
		if(visitatore != null){
			return visitatore;
		}else{
			visitatore = new Visitatori();
			visitatore.setLatitude( 41.899993896484375 ); // roma
			visitatore.setLongitude( 12.483306884765625 ); // roma
			return visitatore;
		}
	}

	
	
	/**
	 * lo posso usare da ChiamateAjax
	 */
	/*
	public Visitatori DammiInfoIpRequest_MaxMind(final HttpServletRequest request) throws Exception{
		Info_IP_Address result = dammiINFO_MaxMind_Plugin(request.getServletContext(), DammiIPaddress(request) );
		if(result != null){
			Visitatori visit = new Visitatori();
			visit.setLatitude( result.getLatitude() );
			visit.setLongitude( result.getLongitude()); 
			return visit;
		}else{
			Visitatori visit = new Visitatori();
			visit.setLatitude( 41.899993896484375 ); // roma
			visit.setLongitude( 12.483306884765625 ); // roma
			return visit;
		}
	}
	*/
	
	/*
	private Info_IP_Address dammiINFO_MaxMind_Plugin(final ServletContext context, final String IP) throws Exception{
		//File db = new File("src/main/resources/GeoLiteCity.dat");
		//ClassLoader classLoader = getClass().getClassLoader();
		//ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		//InputStream is = classloader.getResourceAsStream("test.csv");
		//File file = new File(classloader.getResource("GeoLiteCity.dat").getFile());
		//File db = new File( this.getClass().getClassLoader().getResource("GeoLiteCity.dat").getFile() );
		//File db = new File("src/main/resources/GeoLiteCity.dat");
		//InputStream is = context.getResourceAsStream("GeoLiteCity.dat");
		
		File db = new File( context.getRealPath("/WEB-INF/GeoLiteCity.dat").toString() );
		
		LookupService lookup = new LookupService  (db, LookupService.GEOIP_STANDARD);
		com.maxmind.geoip.Location locationServices = lookup.getLocation( IP );

        if(locationServices != null){
        	Info_IP_Address ricTrans_MM_info = new Info_IP_Address();
        	ricTrans_MM_info.setCity(locationServices.city);
        	ricTrans_MM_info.setCountryCode(locationServices.countryCode);
        	ricTrans_MM_info.setCountryName(locationServices.countryName);
        	ricTrans_MM_info.setLatitude(locationServices.latitude);
        	ricTrans_MM_info.setLongitude(locationServices.longitude);
        	ricTrans_MM_info.setPostalCode(locationServices.postalCode);
        	ricTrans_MM_info.setRegionCode(locationServices.region);
        	ricTrans_MM_info.setRegionName( com.maxmind.geoip.regionName.regionNameByCode(
        			locationServices.countryCode, locationServices.region) );
        	return ricTrans_MM_info;
        }else{
        	return null;
        }
	}
	*/
	
	
}
