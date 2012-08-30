package com.kp.marsh.ebt.server.importer.csvimport;

import com.google.inject.Provider;


public class PzMapAnagraficheConfigProvider  implements Provider<String> {

	public String get() {
		return "/pzmap.anagCe.xml";
		
	}
	
}
