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
public class CsvGruppiCommercialiDTO {
	
	private String codEysCapogruppo;
	
	private String nomeCapogruppo;
	
	private String codEysCE;

	private String nomeCE;

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
	
}
