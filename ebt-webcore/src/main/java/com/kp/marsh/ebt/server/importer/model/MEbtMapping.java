package com.kp.marsh.ebt.server.importer.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "M_EBT_MAPPING")
public class MEbtMapping implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column( name = "EYS_CODE")
	private String eysCode;	
	@Column( name = "Area_Business", length=100)
	private String areaBusiness;
	@Column(name = "Product", length=100)
	private String prodotto;

	public MEbtMapping(){
		//todo
	}
	
	public MEbtMapping(String eysCode, String areaBusiness, String prodotto) {
		this.eysCode = eysCode;
		this.areaBusiness = areaBusiness;
		this.prodotto = prodotto;
	}

	public String getEysCode() {
		return eysCode;
	}
	
	public void setEysCode(String eysCode) {
		this.eysCode = eysCode;
	}

	
	public String getAreaBusiness() {
		return this.areaBusiness;
	}

	public void setAreaBusiness(String areaBusiness) {
		this.areaBusiness = areaBusiness;
	}

	public String getProdotto() {
		return this.prodotto;
	}

	public void setProdotto(String prodotto) {
		this.prodotto = prodotto;
	}

}
