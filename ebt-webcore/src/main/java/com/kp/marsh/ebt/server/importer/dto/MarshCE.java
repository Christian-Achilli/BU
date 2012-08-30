package com.kp.marsh.ebt.server.importer.dto;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MarshCE {

	String eysCode;
	
	String nominativo;
	
	String username;
	
	// codice eurosys - MarshGruppoCommerciale
	Map<String, MarshGruppoCommerciale> gruppoCommercialeMap;
	
	public MarshCE() {
		gruppoCommercialeMap = new HashMap<String, MarshGruppoCommerciale>();
	}

	public String getEysCode() {
		return eysCode;
	}

	public void setEysCode(String eysCode) {
		this.eysCode = eysCode;
	}

	public String getNominativo() {
		return nominativo;
	}

	public void setNominativo(String nominativo) {
		this.nominativo = nominativo;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Map<String, MarshGruppoCommerciale> getGruppoCommercialeMap() {
		return gruppoCommercialeMap;
	}

	
	public String getAchieved() {
		int totAchieved = 0;
		Iterator<String> itCodiciCapogruppo = getGruppoCommercialeMap().keySet().iterator();
		while (itCodiciCapogruppo.hasNext()) {
			String codiceCapogruppo = (String) itCodiciCapogruppo.next();
			MarshGruppoCommerciale buff = getGruppoCommercialeMap().get(codiceCapogruppo);
			Iterator<String> itProdotti = buff.getProductMap().keySet().iterator();
			while (itProdotti.hasNext()) {
				String nomeProdotto = (String) itProdotti.next();
				MarshProduct prodotto = buff.getProductMap().get(nomeProdotto);
				totAchieved += prodotto.getAchieved();
				
			}
		}
		
		return ""+totAchieved;
	}
	
	public String getConsuntivo() {
		int totConsuntivo = 0;
		Iterator<String> itCodiciCapogruppo = getGruppoCommercialeMap().keySet().iterator();
		while (itCodiciCapogruppo.hasNext()) {
			String codiceCapogruppo = (String) itCodiciCapogruppo.next();
			MarshGruppoCommerciale buff = getGruppoCommercialeMap().get(codiceCapogruppo);
			Iterator<String> itProdotti = buff.getProductMap().keySet().iterator();
			while (itProdotti.hasNext()) {
				String nomeProdotto = (String) itProdotti.next();
				MarshProduct prodotto = buff.getProductMap().get(nomeProdotto);
				totConsuntivo += prodotto.getActual();
				
			}
		}
		
		return ""+totConsuntivo;
	}
	
	
}
