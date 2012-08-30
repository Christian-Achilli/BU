/**
 * 
 */
package com.kp.marsh.ebt.server.importer.csvimport;

/**
 * @author christianachilli
 *
 * Creato tramite FlatPack e contiene i dati che vogliamo mettere su M_ACHIEVED
 *
 */
public class CsvAchievedDTO {
	
	private String annoContabile;
	
	private String codEysCE;
	
	private String nomeCE;
	
	private String codEysCapogruppo;
	
	private String nomeCapogruppo;
	
	private String codEysGaranzia;
	
	private double totaleProvvigioni;

	public String getAnnoContabile() {
		return annoContabile;
	}

	public void setAnnoContabile(String annoContabile) {
		this.annoContabile = annoContabile;
	}

	public String getCodEysCE() {
		return codEysCE;
	}

	public void setCodEysCE(String codEysCE) {
		this.codEysCE = codEysCE;
	}

	public String getNomeCE() {
		return nomeCE;
	}

	public void setNomeCE(String nomeCE) {
		this.nomeCE = nomeCE;
	}

	public String getCodEysCapogruppo() {
		return codEysCapogruppo;
	}

	public void setCodEysCapogruppo(String codEysCapogruppo) {
		this.codEysCapogruppo = codEysCapogruppo;
	}

	public String getNomeCapogruppo() {
		return nomeCapogruppo;
	}

	public void setNomeCapogruppo(String nomeCapogruppo) {
		this.nomeCapogruppo = nomeCapogruppo;
	}

	public String getCodEysGaranzia() {
		return codEysGaranzia;
	}

	public void setCodEysGaranzia(String codEysGaranzia) {
		this.codEysGaranzia = codEysGaranzia;
	}

	public double getTotaleProvvigioni() {
		return totaleProvvigioni;
	}

	public void setTotaleProvvigioni(double totaleProvvigioni) {
		this.totaleProvvigioni = totaleProvvigioni;
	}

}
