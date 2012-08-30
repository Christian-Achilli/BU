package com.kp.marsh.ebt.shared.dto;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * 
 * Contiene l'identificativo e i valori di saturazione del potenziale di un certo information owner da mostrare sulla pagina degi istogrammi
 * 
 */
public class LightInfoOwnerDto implements IsSerializable {
	
	
	private String entityType; // la stringa che rappresenta di che tipo è questo information owner. Viene pooplato col valore della colonna owner type di INFORMATION_OWNERS
	
	private String identificativo; // stringa da visualizzare come identificativo dell'information owner
	
	private int totalePotenzialeEuro; // totale di riga
	
	private int totaleAchievedEuro; // totale di riga
	
	private LightProductInfoDto[] orderedProductArray; // contiene le info di achieved e potential sui prodotti da visualizzare
	
	
	public LightInfoOwnerDto() {}


	public String getIdentificativo() {
		return identificativo;
	}


	public void setIdentificativo(String identificativo) {
		this.identificativo = identificativo;
	}


	public int getTotalePotenzialeEuro() {
		return totalePotenzialeEuro;
	}


	public void setTotalePotenzialeEuro(int totalePotenzialeEuro) {
		this.totalePotenzialeEuro = totalePotenzialeEuro;
	}


	public int getTotaleAchievedEuro() {
		return totaleAchievedEuro;
	}


	public void setTotaleAchievedEuro(int totaleAchievedEuro) {
		this.totaleAchievedEuro = totaleAchievedEuro;
	}


	public LightProductInfoDto[] getOrderedProductArray() {
		return orderedProductArray;
	}


	public void setOrderedProductArray(LightProductInfoDto[] orderedProductArray) {
		this.orderedProductArray = orderedProductArray;
	}


	public String getEntityType() {
		return entityType;
	}


	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}
	
}
