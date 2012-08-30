package com.kp.malice.entities.business;

import org.apache.log4j.Logger;

public class GrafiData {

	private static Logger log = Logger.getLogger(GrafiData.class);

	private String montanti;
	private String premiProvvigioni;

	public GrafiData() {
	}

	public String getMontanti() {
		return montanti;
	}

	public void setMontanti(String montanti) {
		this.montanti = montanti;
	}

	public String getPremiProvvigioni() {
		return premiProvvigioni;
	}

	public void setPremiProvvigioni(String premiProvvigioni) {
		this.premiProvvigioni = premiProvvigioni;
	}


}
