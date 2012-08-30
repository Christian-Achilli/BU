package com.kp.marsh.ebt.server.importer.dto;

import java.util.HashMap;
import java.util.Map;

public class MarshOffice {

	String eysCode;
	
	String officeName;
	
	// codice eurosys - MarshCE
	Map<String, MarshCE> marshCEMap;

	
	public MarshOffice() {
		marshCEMap = new HashMap<String, MarshCE>();
	}
	
	
	public String getEysCode() {
		return eysCode;
	}

	public void setEysCode(String eysCode) {
		this.eysCode = eysCode;
	}

	public String getOfficeName() {
		return officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}


	public Map<String, MarshCE> getMarshCEMap() {
		return marshCEMap;
	}
	
	
}
