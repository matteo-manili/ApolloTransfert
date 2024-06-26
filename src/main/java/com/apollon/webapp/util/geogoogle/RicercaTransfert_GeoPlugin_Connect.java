package com.apollon.webapp.util.geogoogle;


import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;

import com.apollon.Constants;
import com.apollon.util.UrlConnection;
import com.apollon.webapp.util.ApplicationUtils;


/**
 * @author Matteo - matteo.manili@gmail.com
 * 
 * questa classe restitusce informazioni per mezzo delle API di GeoPlugin http://www.geoplugin.com/ 
 * vedere http://www.geoplugin.com/quickstart#how_to_geo-localize_your_visitors
 * 
 */
public class RicercaTransfert_GeoPlugin_Connect extends ApplicationUtils implements Serializable {

	private static final Log log = LogFactory.getLog(RicercaTransfert_GeoPlugin_Connect.class);
	private static final long serialVersionUID = -3854206387225890213L;
	
    public RicercaTransfert_GeoPlugin_Info dammiINFO_Geo_Plugin(final String IP) throws JSONException, NullPointerException, Exception{
    	log.debug("dammiINFO_Geo_Plugin");
		final String JSON_XML = "/json.gp";
        StringBuilder sb = new StringBuilder(Constants.URL_GEO_PLUGIN_API_BASE + JSON_XML );
		sb.append("?ip=" + IP);
		JSONObject obj = new JSONObject( UrlConnection.HttpConnection( sb ) );
		String status = Integer.toString( obj.getInt("geoplugin_status") );
		
        if(status.equals("200")){
        	RicercaTransfert_GeoPlugin_Info ricTrans_GP_info = new RicercaTransfert_GeoPlugin_Info();
        	ricTrans_GP_info.setStatus( status );
        	ricTrans_GP_info.setLatitude( obj.getDouble("geoplugin_latitude") );
        	ricTrans_GP_info.setLongitude( obj.getDouble("geoplugin_longitude") );
        	
        	/*
        	ricTrans_GP_info.setStatus( status );
        	ricTrans_GP_info.setLatitude( Double.parseDouble((String)jsonObject.get("geoplugin_latitude")) );
        	ricTrans_GP_info.setLongitude( Double.parseDouble((String)jsonObject.get("geoplugin_longitude")) );
        	*/
        	return ricTrans_GP_info;
        
        }else{
        	log.debug("INFO USER NO RICEVUTO: "+IP);
        	return null;
        }

	}

	
}
