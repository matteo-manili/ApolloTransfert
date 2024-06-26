package com.apollon.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

public class PropertiesFileUtil {

	private Properties prop = null;
   
    public PropertiesFileUtil() {
        try {
        	this.prop = new Properties();
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			InputStream resourceStream = loader.getResourceAsStream( "ApplicationResources.properties" );
			prop.load(resourceStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
     
    public Set<Object> getAllKeys() {
    	Set<Object> keys = prop.keySet();
        return keys;
    }
    
    
    public String getPropertyValue(String key) {
    	return this.prop.getProperty(key);
    }
    
    
    public static void main(String a[]) {
    	PropertiesFileUtil mpc = new PropertiesFileUtil();
        Set<Object> keys = mpc.getAllKeys();
        for(Object k:keys) {
            String key = (String)k;
            System.out.println(key+": "+mpc.getPropertyValue(key));
        }
    }
	
	
}
