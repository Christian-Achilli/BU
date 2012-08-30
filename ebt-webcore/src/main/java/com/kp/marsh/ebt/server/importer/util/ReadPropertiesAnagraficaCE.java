package com.kp.marsh.ebt.server.importer.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

public class ReadPropertiesAnagraficaCE implements IReadProperties  {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(ReadPropertiesAnagraficaCE.class);
    
	private String WAITING_PATH;
	private String IN_PROGRESS_PATH ;				
	private String WORKED_PATH;
	private String PREFISSO_ANAGRAFICA;
	private String NAZIONE_DEFAULT;
    
    @Inject ReadPropertiesAnagraficaCE() {}
    
    public void loadProperties(Properties p) throws IOException{
       
        WAITING_PATH  = p.getProperty("WAITING_PATH");
        IN_PROGRESS_PATH = p.getProperty("IN_PROGRESS_PATH");	
        WORKED_PATH = p.getProperty("WORKED_PATH");
        PREFISSO_ANAGRAFICA = p.getProperty("PREFISSO_ANAGRAFICA_CE");
        NAZIONE_DEFAULT = p.getProperty("NAZIONE_DEFAULT");
    }
    
    
    
    public String getWAITING_PATH(){
    	return WAITING_PATH;
    }
    public String getIN_PROGRESS_PATH(){
    	return IN_PROGRESS_PATH;
    }
    public String getWORKED_PATH(){
    	return WORKED_PATH;
    }

    public String getNAZIONE_DEFAULT(){
    	return NAZIONE_DEFAULT;
    }
    
    @Override
    public String getPREFISSO() {
    	return PREFISSO_ANAGRAFICA;
    }

}