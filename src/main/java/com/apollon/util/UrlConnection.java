package com.apollon.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import com.apollon.Constants;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public class UrlConnection {
    
	public static String HttpConnection(StringBuilder sb) throws ConnectException, IOException, UnknownHostException, NullPointerException {
		StringBuilder sbuild = new StringBuilder();
		URL url = new URL(sb.toString());
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		InputStreamReader in = new InputStreamReader(conn.getInputStream(), Constants.ENCODING_UTF_8);
		BufferedReader br = new BufferedReader( in );
		String line = "";
		while ((line = br.readLine()) != null) {
			//System.out.println(line);
			sbuild.append(line);
		}
		br.close();
		in.close();
	    return sbuild.toString();
    }
    
    
    

}
