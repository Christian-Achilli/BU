package com.kp.malice.entities.business;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.kp.malice.entities.persisted.Link;

public class WelcomeInfo {

    private Date meseAperto = new Date();
    private BigInteger numTtlDaIncassare = BigInteger.ZERO;
    private BigDecimal totPremiDaIncassareEuroCent = BigDecimal.ZERO;
    private BigInteger numIncassiSospesi = BigInteger.ZERO;
    private BigDecimal totPremiInSospesoEuroCent = BigDecimal.ZERO;
    private BigDecimal totPremiIncassatiAnnoEuroCent = BigDecimal.ZERO;
    private BigDecimal totProvvigioniIncassateAnnoEuroCent = BigDecimal.ZERO;
    private List<Link> links;

    public WelcomeInfo() {
    }

    public Date getMeseAperto() {
        return meseAperto;
    }

    public BigInteger getNumTtlDaIncassare() {
        return numTtlDaIncassare;
    }

    public void setNumTtlDaIncassare(BigInteger numTtlDaIncassare) {
        this.numTtlDaIncassare = numTtlDaIncassare;
    }

    public BigDecimal getTotPremiDaIncassareEuroCent() {
        return totPremiDaIncassareEuroCent;
    }

    public void setTotPremiDaIncassareEuroCent(BigDecimal totPremiDaIncassareEuroCent) {
        this.totPremiDaIncassareEuroCent = totPremiDaIncassareEuroCent;
    }

    public BigInteger getNumIncassiSospesi() {
        return numIncassiSospesi;
    }

    public void setNumIncassiSospesi(BigInteger numIncassiSospesi) {
        this.numIncassiSospesi = numIncassiSospesi;
    }

    public BigDecimal getTotPremiInSospesoEuroCent() {
        return totPremiInSospesoEuroCent;
    }

    public void setTotPremiInSospesoEuroCent(BigDecimal totPremiInSospesoEuroCent) {
        this.totPremiInSospesoEuroCent = totPremiInSospesoEuroCent;
    }

    public BigDecimal getTotPremiIncassatiAnnoEuroCent() {
        return totPremiIncassatiAnnoEuroCent;
    }

    public void setTotPremiIncassatiAnnoEuroCent(BigDecimal totPremiIncassatiAnnoEuroCent) {
        this.totPremiIncassatiAnnoEuroCent = totPremiIncassatiAnnoEuroCent;
    }

    public BigDecimal getTotProvvigioniIncassateAnnoEuroCent() {
        return totProvvigioniIncassateAnnoEuroCent;
    }

    public void setTotProvvigioniIncassateAnnoEuroCent(BigDecimal totProvvigioniIncassateAnnoEuroCent) {
        this.totProvvigioniIncassateAnnoEuroCent = totProvvigioniIncassateAnnoEuroCent;
    }

    public void setMeseAperto(Date meseAperto) {
        this.meseAperto = meseAperto;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

}
