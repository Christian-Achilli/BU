package com.kp.marsh.ebt.server.importer.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public interface IReadProperties {

	
	public void loadProperties(Properties p)  throws IOException;
	
    public String getWAITING_PATH();
    public String getIN_PROGRESS_PATH();
    public String getWORKED_PATH();
    public String getPREFISSO();
    public String getNAZIONE_DEFAULT();
}