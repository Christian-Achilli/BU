package com.kp.malice.entities.business;

import java.math.BigDecimal;

public class RischioAssicurato {

    private Long id;
    private String codice; // codice ramo . codice rischio
    private BigDecimal importoPremioAnnuoNetto; // importo premio annuo netto -->> da risk premium details:premium
    private BigDecimal importoPremioAnnuoAccessori; // importo premio annuo accessori da risk premium details:accessori
    private BigDecimal importoPremioAnnuoImposte;
    private BigDecimal percentualeProvvigioniIncassoSulNetto; // percentuale provvigioni di incasso netto del risk premium details: 10%
    private BigDecimal percentualeProvvigioniIncassoSugliAccessori; // percentuale provvigioni di incasso degli accessori del risk premium details: 100%

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getImportoPremioAnnuoNetto() {
        return importoPremioAnnuoNetto;
    }

    public void setImportoPremioAnnuoNetto(BigDecimal importoPremioAnnuoNetto) {
        this.importoPremioAnnuoNetto = importoPremioAnnuoNetto;
    }

    public BigDecimal getImportoPremioAnnuoAccessori() {
        return importoPremioAnnuoAccessori;
    }

    public void setImportoPremioAnnuoAccessori(BigDecimal importoPremioAnnuoAccessori) {
        this.importoPremioAnnuoAccessori = importoPremioAnnuoAccessori;
    }

    public BigDecimal getImportoPremioAnnuoImposte() {
        return importoPremioAnnuoImposte;
    }

    public void setImportoPremioAnnuoImposte(BigDecimal importoPremioAnnuoImposte) {
        this.importoPremioAnnuoImposte = importoPremioAnnuoImposte;
    }

    public BigDecimal getPercentualeProvvigioniIncassoSulNetto() {
        return percentualeProvvigioniIncassoSulNetto;
    }

    public void setPercentualeProvvigioniIncassoSulNetto(BigDecimal percentualeProvvigioniIncassoSulNetto) {
        this.percentualeProvvigioniIncassoSulNetto = percentualeProvvigioniIncassoSulNetto;
    }

    public BigDecimal getPercentualeProvvigioniIncassoSugliAccessori() {
        return percentualeProvvigioniIncassoSugliAccessori;
    }

    public void setPercentualeProvvigioniIncassoSugliAccessori(BigDecimal percentualeProvvigioniIncassoSugliAccessori) {
        this.percentualeProvvigioniIncassoSugliAccessori = percentualeProvvigioniIncassoSugliAccessori;
    }

}
