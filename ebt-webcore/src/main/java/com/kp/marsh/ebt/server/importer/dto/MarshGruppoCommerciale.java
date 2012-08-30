package com.kp.marsh.ebt.server.importer.dto;

import java.util.HashMap;
import java.util.Map;

public class MarshGruppoCommerciale {

	String codiceCapoGruppo; // codice eurosys
	
	String denominazione;
	
	// codice EBT - Marsh Product
	private Map<String, MarshProduct> productMap; // mappa prodotto (nome prodotto EBT) - marsh product per gestire i consuntivi o gli achieved
	
	public MarshGruppoCommerciale() {
		productMap = new HashMap<String, MarshProduct>();
	}

	public String getCodiceCapoGruppo() {
		return codiceCapoGruppo;
	}

	public void setCodiceCapoGruppo(String codiceCapoGruppo) {
		this.codiceCapoGruppo = codiceCapoGruppo;
	}

	public String getDenominazione() {
		return denominazione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	public Map<String, MarshProduct> getProductMap() {
		return productMap;
	}

}