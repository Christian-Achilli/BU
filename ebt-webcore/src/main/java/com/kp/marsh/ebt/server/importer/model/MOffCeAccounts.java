package com.kp.marsh.ebt.server.importer.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="M_OFF_CE_ACCOUNTS")
public class MOffCeAccounts implements java.io.Serializable {
	@Id
	@Column(name="Codice_Eurosys")
	private String codiceEurosys;
	@Id
	@Column(name="nome_identificativo")
	private String nomeIdentificativo;
	@Id
	@Column(name="codice_ufficio")
	private String codiceUfficio;
	@Id
	@Column(name="nome_ufficio")
	private String nomeUfficio;
	@Id
	@Column(name="ad_username")
	private String adUsername;

	public MOffCeAccounts() {
	}

	public MOffCeAccounts(String codiceEurosys, String nomeIdentificativo,
			String codiceUfficio, String nomeUfficio, String adUsername) {
		this.codiceEurosys = codiceEurosys;
		this.nomeIdentificativo = nomeIdentificativo;
		this.codiceUfficio = codiceUfficio;
		this.nomeUfficio = nomeUfficio;
		this.adUsername = adUsername;
	}

	public String getCodiceEurosys() {
		return this.codiceEurosys;
	}

	public void setCodiceEurosys(String codiceEurosys) {
		this.codiceEurosys = codiceEurosys;
	}

	public String getNomeIdentificativo() {
		return this.nomeIdentificativo;
	}

	public void setNomeIdentificativo(String nomeIdentificativo) {
		this.nomeIdentificativo = nomeIdentificativo;
	}

	public String getCodiceUfficio() {
		return this.codiceUfficio;
	}

	public void setCodiceUfficio(String codiceUfficio) {
		this.codiceUfficio = codiceUfficio;
	}

	public String getNomeUfficio() {
		return this.nomeUfficio;
	}

	public void setNomeUfficio(String nomeUfficio) {
		this.nomeUfficio = nomeUfficio;
	}

	public String getAdUsername() {
		return this.adUsername;
	}

	public void setAdUsername(String adUsername) {
		this.adUsername = adUsername;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof MOffCeAccounts))
			return false;
		MOffCeAccounts castOther = (MOffCeAccounts) other;

		return ((this.getCodiceEurosys() == castOther.getCodiceEurosys()) || (this
				.getCodiceEurosys() != null
				&& castOther.getCodiceEurosys() != null && this
				.getCodiceEurosys().equals(castOther.getCodiceEurosys())))
				&& ((this.getNomeIdentificativo() == castOther
						.getNomeIdentificativo()) || (this
						.getNomeIdentificativo() != null
						&& castOther.getNomeIdentificativo() != null && this
						.getNomeIdentificativo().equals(
								castOther.getNomeIdentificativo())))
				&& ((this.getCodiceUfficio() == castOther.getCodiceUfficio()) || (this
						.getCodiceUfficio() != null
						&& castOther.getCodiceUfficio() != null && this
						.getCodiceUfficio()
						.equals(castOther.getCodiceUfficio())))
				&& ((this.getNomeUfficio() == castOther.getNomeUfficio()) || (this
						.getNomeUfficio() != null
						&& castOther.getNomeUfficio() != null && this
						.getNomeUfficio().equals(castOther.getNomeUfficio())))
				&& ((this.getAdUsername() == castOther.getAdUsername()) || (this
						.getAdUsername() != null
						&& castOther.getAdUsername() != null && this
						.getAdUsername().equals(castOther.getAdUsername())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getCodiceEurosys() == null ? 0 : this.getCodiceEurosys()
						.hashCode());
		result = 37
				* result
				+ (getNomeIdentificativo() == null ? 0 : this
						.getNomeIdentificativo().hashCode());
		result = 37
				* result
				+ (getCodiceUfficio() == null ? 0 : this.getCodiceUfficio()
						.hashCode());
		result = 37
				* result
				+ (getNomeUfficio() == null ? 0 : this.getNomeUfficio()
						.hashCode());
		result = 37
				* result
				+ (getAdUsername() == null ? 0 : this.getAdUsername()
						.hashCode());
		return result;
	}

}
