package com.kp.malice.entities.business;

public class PuntoVenditaRMAPerLloyds {
    private Long id; // quello del PUN_VND associato
    private long codiceCompagnia;
    private String codicePuntoVendita;
    private AgenziaRMA agenzia;
    private LloydsBroker broker;
    private String description;

    public PuntoVenditaRMAPerLloyds() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AgenziaRMA getAgenzia() {
        return agenzia;
    }

    public void setAgenzia(AgenziaRMA agenzia) {
        this.agenzia = agenzia;
    }

    public LloydsBroker getBroker() {
        return broker;
    }

    public void setBroker(LloydsBroker broker) {
        this.broker = broker;
    }

    public long getCodiceCompagnia() {
        return codiceCompagnia;
    }

    public void setCodiceCompagnia(long codiceCompagnia) {
        this.codiceCompagnia = codiceCompagnia;
    }

    public String getCodicePuntoVendita() {
        return codicePuntoVendita;
    }

    public void setCodicePuntoVendita(String codicePuntoVendita) {
        this.codicePuntoVendita = codicePuntoVendita;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
