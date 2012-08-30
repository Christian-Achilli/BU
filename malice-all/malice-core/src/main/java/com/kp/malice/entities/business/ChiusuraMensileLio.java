package com.kp.malice.entities.business;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;

public class ChiusuraMensileLio {

    private Long id;
    private int referenceCalendarMonth = 1; //January
    private int referenceYear = 2012;
    private int totEstrattiConto = 0;
    private int totTitoli = 0;
    private BigDecimal totPremiEuroCent = BigDecimal.ZERO;
    private BigDecimal totCommissioniEuroCent = BigDecimal.ZERO;
    private Date dtInvio;
    private StatoChiusura statoChiusura = StatoChiusura.APERTA;
    private boolean nextToBeSent;
    private AgenziaRMA agenzia;
    private String nota = "";

    public ChiusuraMensileLio() {
    }

    public DateTime getPeriodo() {
        return new DateTime(referenceYear, referenceCalendarMonth, 1, 0, 0);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        DateTime dt = new DateTime(referenceYear, referenceCalendarMonth, 1, 0, 0, 0);
        return dt.toString("MMM yyyy", Locale.ITALIAN).toUpperCase();
    }

    public void setLabel(String label) {
        //ONLY FOR RF COMPATIBILITY
    }

    private boolean isDaInviare() { // creare una drools
        DateTime today = new DateTime();
        MutableDateTime dt = new MutableDateTime(referenceYear, referenceCalendarMonth, 1, 0, 0, 0, 0);
        dt.addMonths(1);
        return isNextToBeSent() && today.isAfter(dt);
    }

    public int getTotEstrattiConto() {
        return totEstrattiConto;
    }

    public void setTotEstrattiConto(int totEstrattiConto) {
        this.totEstrattiConto = totEstrattiConto;
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

    public Date getDtInvio() {
        return dtInvio;
    }

    public void setDtInvio(Date dtInvio) {
        this.dtInvio = dtInvio;
    }

    public StatoChiusura getStatoChiusura() {
        return statoChiusura;
    }

    public void setStatoChiusura(StatoChiusura statoChiusura) {
        this.statoChiusura = statoChiusura;
    }

    public String getStatoChiusuraString() {
        if (isDaInviare())
            return "DA_INVIARE";
        else
            return getStatoChiusura().name();
    }

    public void setStatoChiusuraString(String s) {
        //only for RF compatibility;
    }

    public boolean isNextToBeSent() {
        return nextToBeSent;
    }

    public void setNextToBeSent(boolean nextToBeSent) {
        this.nextToBeSent = nextToBeSent;
    }

    public enum StatoChiusura {
        INVIATA, APERTA;

        public static StatoChiusura fromString(String s) {
            for (StatoChiusura enu : values()) {
                if (enu.name().equals(s)) {
                    return enu;
                }
            }
            return null;
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + referenceCalendarMonth;
        result = prime * result + referenceYear;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ChiusuraMensileLio other = (ChiusuraMensileLio) obj;
        if (referenceCalendarMonth != other.referenceCalendarMonth)
            return false;
        if (referenceYear != other.referenceYear)
            return false;
        return true;
    }

    public int getReferenceCalendarMonth() {
        return referenceCalendarMonth;
    }

    public void setReferenceCalendarMonth(int referenceCalendarMonth) {
        this.referenceCalendarMonth = referenceCalendarMonth;
    }

    public int getReferenceYear() {
        return referenceYear;
    }

    public void setReferenceYear(int referenceYear) {
        this.referenceYear = referenceYear;
    }

    public boolean isAperta() {
        return StatoChiusura.APERTA == getStatoChiusura();
    }

    public void setAperta(boolean aperta) {
        if (aperta)
            setStatoChiusura(StatoChiusura.APERTA);
    }

    public void setAgenzia(AgenziaRMA age) {
        this.agenzia = age;
    }

    public AgenziaRMA getAgenzia() {
        return agenzia;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

}
