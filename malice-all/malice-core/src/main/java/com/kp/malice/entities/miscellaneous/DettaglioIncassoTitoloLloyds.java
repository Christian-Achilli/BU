package com.kp.malice.entities.miscellaneous;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.kp.malice.entities.business.ContraentePolizzaLloyds.Gender;
import com.kp.malice.entities.business.IncassoTitoloLloyds;

public class DettaglioIncassoTitoloLloyds extends IncassoTitoloLloyds {

	private String firstName;
	private String lastName;
	private String numeroTitolo;
	private String codPol;
	private BigInteger codPrgMov;
	
	private String gender;

	private String companyName;

	public DettaglioIncassoTitoloLloyds() {
	}

	public String getCodPol() {
		return codPol;
	}

	public void setCodPol(String codPol) {
		this.codPol = codPol;
	}

	public BigInteger getCodPrgMov() {
		return codPrgMov;
	}

	public void setCodPrgMov(BigInteger codPrgMov) {
		this.codPrgMov = codPrgMov;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getNumeroTitolo() {
		String codPrgMovFormatted = String.format("%02d", codPrgMov);
		return codPol+"-"+codPrgMovFormatted;
	}

	public void setNumeroTitolo(String numeroTitolo) {
		this.numeroTitolo = numeroTitolo;
	}

	public String getIdentificativoContraente() {
		return Gender.fromString(gender)==Gender.C? companyName : firstName +" "+ lastName;
	}

	public void setIdentificativoContraente(String identificativoContraente) {
		// nothing to do
	}
}
