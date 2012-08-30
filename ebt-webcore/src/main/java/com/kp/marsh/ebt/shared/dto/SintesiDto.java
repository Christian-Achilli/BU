package com.kp.marsh.ebt.shared.dto;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Informations to build gauge about one line of business.
 * @author dariobrambilla
 */
public class SintesiDto implements IsSerializable{
	
	// % saturazione del potenziale: nazionale e la situazione attuale del dato information owner
	private int indiceSaturazionePotenziale;
	private int indiceSaturazionePotenzialeTotale; // nazionale
	
	// % indice di penetrazione: situazione iniziale, media nazionale, situazione attuale
	private int indicePenetrazione;
	private int indicePenetrazioneIniziale;
	private int indicePenetrazioneTotale; // nazionale
	
	private int prodottiLavorati; //Pillole inizialmente arancioni per le quali è stato espresso un valore di potenziale, NA, PI o IAC
	private int prodottiDaLavorare; // Tutte le pillole che all'inizio dei tempi sono arancioni
	
	// identificatio dell lob cui gli indici fanno riferimento
	private String lobName;
	
	/**
	 * @param indiceSaturazionePotenziale
	 * @param indiceSaturazionePotenzialeNazionale
	 * @param indicePenetrazione
	 * @param indicePenetrazioneIniziale
	 * @param indicePenetrazioneNazionale
	 * @param lobName Nome linea di business
	 */
	public SintesiDto(int indiceSaturazionePotenziale, int indiceSaturazionePotenzialeNazionale, int indicePenetrazione, int indicePenetrazioneIniziale, int indicePenetrazioneNazionale, String lobName){
		this.indiceSaturazionePotenziale = indiceSaturazionePotenziale;
		this.indiceSaturazionePotenzialeTotale = indiceSaturazionePotenzialeNazionale;
		this.indicePenetrazione = indicePenetrazione;
		this.indicePenetrazioneIniziale = indicePenetrazioneIniziale;
		this.indicePenetrazioneTotale = indicePenetrazioneNazionale;
		this.lobName = lobName;
	}
	
	
	public SintesiDto() {}
	

	public int getIndiceSaturazionePotenziale() {
		return indiceSaturazionePotenziale;
	}

	public void setIndiceSaturazionePotenziale(int indiceSaturazionePotenziale) {
		this.indiceSaturazionePotenziale = indiceSaturazionePotenziale;
	}

	public int getIndiceSaturazionePotenzialeTotale() {
		return indiceSaturazionePotenzialeTotale;
	}

	public void setIndiceSaturazionePotenzialeTotale(
			int indiceSaturazionePotenzialeTotale) {
		this.indiceSaturazionePotenzialeTotale = indiceSaturazionePotenzialeTotale;
	}

	public int getIndicePenetrazione() {
		return indicePenetrazione;
	}

	public void setIndicePenetrazione(int indicePenetrazione) {
		this.indicePenetrazione = indicePenetrazione;
	}

	public int getIndicePenetrazioneIniziale() {
		return indicePenetrazioneIniziale;
	}

	public void setIndicePenetrazioneIniziale(int indicePenetrazioneIniziale) {
		this.indicePenetrazioneIniziale = indicePenetrazioneIniziale;
	}

	public int getIndicePenetrazioneTotale() {
		return indicePenetrazioneTotale;
	}

	public void setIndicePenetrazioneTotale(int indicePenetrazioneTotale) {
		this.indicePenetrazioneTotale = indicePenetrazioneTotale;
	}

	public String getLobName() {
		return lobName;
	}

	public void setLobName(String lobName) {
		this.lobName = lobName;
	}


	public void setProdottiLavorati(int prodottiLavorati) {
		this.prodottiLavorati = prodottiLavorati;
	}


	/**
	 * @return il numero dei prodotti lavorati tra quelli totali da lavorare
	 */
	public int getProdottiLavorati() {
		return prodottiLavorati;
	}


	public void setProdottiDaLavorare(int prodottiDaLavorare) {
		this.prodottiDaLavorare = prodottiDaLavorare;
	}


	/**
	 * @return i prodotti che devono essere lavorati in totale. NON SONO QUELLI ANCORA DA LAVORARE, MA QUELLI DA LAVORARE DI SEMPRE.
	 */
	public int getProdottiTotaliDaLavorare() {
		return prodottiDaLavorare;
	}
	
}
