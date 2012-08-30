package com.kp.malice.entities.business;

import java.math.BigDecimal;

public class DettaglioCoassicurazione {
    private CompagniaPortafoglio compagniaAssuntrice;
    private BigDecimal percentualeAssunzione;

    public CompagniaPortafoglio getCompagniaAssuntrice() {
        return compagniaAssuntrice;
    }

    public void setCompagniaAssuntrice(CompagniaPortafoglio compagniaAssuntrice) {
        this.compagniaAssuntrice = compagniaAssuntrice;
    }

    public BigDecimal getPercentualeAssunzione() {
        return percentualeAssunzione;
    }

    public void setPercentualeAssunzione(BigDecimal percentualeAssunzione) {
        this.percentualeAssunzione = percentualeAssunzione;
    }

}
