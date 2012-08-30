package com.kp.malice.entities.business;

import java.util.ArrayList;
import java.util.List;

public class AgenziaRMA {

    private Long id;//della UOA associata
    private String omcCode;
    private String description;
    private String shortDescription;
    private String email;
    private List<PuntoVenditaRMAPerLloyds> puntiVendita = new ArrayList<PuntoVenditaRMAPerLloyds>(0);

    public AgenziaRMA() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<PuntoVenditaRMAPerLloyds> getPuntiVendita() {
        return puntiVendita;
    }

    public void setPuntiVendita(List<PuntoVenditaRMAPerLloyds> puntiVendita) {
        this.puntiVendita = puntiVendita;
    }

    public String getOmcCode() {
        return omcCode;
    }

    public void setOmcCode(String omcCode) {
        this.omcCode = omcCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

}
