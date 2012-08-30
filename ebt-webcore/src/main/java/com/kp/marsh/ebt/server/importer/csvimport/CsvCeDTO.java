/**
 * 
 */
package com.kp.marsh.ebt.server.importer.csvimport;

/**
 *
 * Creato tramite FlatPack e contiene i dati che vogliamo mettere su M_OFF_CE_ACCOUNTS
 *
 */
public class CsvCeDTO {
	
	private String nomeUfficio;	
	private String nomeIdentificativo;
	private String codiceUfficio;
	private String codiceEurosys;
	private String adUsername;
	
	public String getNomeUfficio() {
		return nomeUfficio;
	}
	public void setNomeUfficio(String nomeUfficio) {
		this.nomeUfficio = nomeUfficio;
	}
	public String getNomeIdentificativo() {
		return nomeIdentificativo;
	}
	public void setNomeIdentificativo(String nomeIdentificativo) {
		this.nomeIdentificativo = nomeIdentificativo;
	}
	public String getCodiceUfficio() {
		return codiceUfficio;
	}
	public void setCodiceUfficio(String codiceUfficio) {
		this.codiceUfficio = codiceUfficio;
	}
	public String getCodiceEurosys() {
		return codiceEurosys;
	}
	public void setCodiceEurosys(String codiceEurosys) {
		this.codiceEurosys = codiceEurosys;
	}
	public String getAdUsernmae() {
		return adUsername;
	}
	public void setAdUsernmae(String adUsernmae) {
		this.adUsername = adUsernmae;
	}
	
	

}
