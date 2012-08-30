package com.kp.malice.entities.business;

import java.math.BigDecimal;

public class EstrattoContoLio {

    private Long id;
    private String label = "";
    private int totTitoli = 0;
    private BigDecimal totPremiEuroCent = BigDecimal.ZERO;
    private BigDecimal totCommissioniEuroCent = BigDecimal.ZERO;
    private BigDecimal totRimessaEuroCent = BigDecimal.ZERO;

    public EstrattoContoLio() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getTotTitoli() {
        return totTitoli;
    }

    public void setTotTitoli(int totTitoli) {
        this.totTitoli = totTitoli;
    }

    public BigDecimal getTotPremiEuroCent() {
        return totPremiEuroCent;
    }

    public void setTotPremiEuroCent(BigDecimal totPremiEuroCent) {
        this.totPremiEuroCent = totPremiEuroCent;
    }

    public BigDecimal getTotCommissioniEuroCent() {
        return totCommissioniEuroCent;
    }

    public void setTotCommissioniEuroCent(BigDecimal totCommissioniEuroCent) {
        this.totCommissioniEuroCent = totCommissioniEuroCent;
    }

    public BigDecimal getTotRimessaEuroCent() {
        return totRimessaEuroCent;
    }

    public void setTotRimessaEuroCent(BigDecimal totRimessaEuroCent) {
        this.totRimessaEuroCent = totRimessaEuroCent;
    }

}
