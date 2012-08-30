/**
 * 
 */
package com.kp.marsh.ebt.server.webapp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

import org.apache.log4j.Logger;

import com.google.inject.Singleton;

/**
 * @author christianachilli
 * Interface to marsh active directory service
 */
@Singleton
public class MarshAuthClientForProd implements IMarshAuthService {
	
	private static Logger log = Logger.getLogger(MarshAuthClientForProd.class);
	
	public MarshAuthClientForProd() {}
	
	
	public String authenticate(String username, String password) {
		try {
		    // Construct data
		    String data = URLEncoder.encode("utn", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
		    data += "&" + URLEncoder.encode("pss", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");

		    // Send data
		    URL url = new URL("https://168.168.32.2/WebAuthentication/auth.aspx");
		    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
		    conn.setHostnameVerifier(new CustomizedHostNameVerifier());
		    conn.setDoOutput(true);
		    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		    wr.write(data);
		    wr.flush();

		    // Get the response
		    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		    String line = null;
		    while ((line = rd.readLine()) != null) {
		        if(username.equals(line)) {
		        	return username;
		        }
		    }
		    wr.close();
		    rd.close();
		} catch (Exception e) {
			log.error("Errore nella chiamata ad active directory");
			e.printStackTrace();
		}
		
		return "";
	}
	
	

}
