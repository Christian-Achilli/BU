package com.kp.malice.businessRules;

public class FunzioniAbilitate {
    private boolean annulloTitolo;
    private boolean incassoTitolo;
    private boolean stornoIncasso;
    private boolean recuperoSospeso;
    private boolean creaRettificaProvvigioni;
    private boolean modificaTitolo;
    private boolean delegaIncasso;
    private boolean revocaAnnulloTitolo;

    public boolean isAnnulloTitolo() {
        return annulloTitolo;
    }

    public void setAnnulloTitolo(boolean annulloTitolo) {
        this.annulloTitolo = annulloTitolo;
    }

    public boolean isIncassoTitolo() {
        return incassoTitolo;
    }

    public void setIncassoTitolo(boolean incassoTitolo) {
        this.incassoTitolo = incassoTitolo;
    }

    public boolean isStornoIncasso() {
        return stornoIncasso;
    }

    public void setStornoIncasso(boolean stornoIncasso) {
        this.stornoIncasso = stornoIncasso;
    }

    public boolean isRecuperoSospeso() {
        return recuperoSospeso;
    }

    public void setRecuperoSospeso(boolean recuperoSospeso) {
        this.recuperoSospeso = recuperoSospeso;
    }

    public boolean isCreaRettificaProvvigioni() {
        return creaRettificaProvvigioni;
    }

    public void setCreaRettificaProvvigioni(boolean creaRettificaProvvigioni) {
        this.creaRettificaProvvigioni = creaRettificaProvvigioni;
    }

    public boolean isModificaTitolo() {
        return modificaTitolo;
    }

    public void setModificaTitolo(boolean modificaTitolo) {
        this.modificaTitolo = modificaTitolo;
    }

    public boolean isDelegaIncasso() {
        return delegaIncasso;
    }

    public void setDelegaIncasso(boolean delegaIncasso) {
        this.delegaIncasso = delegaIncasso;
    }

    public boolean isRevocaAnnulloTitolo() {
        return revocaAnnulloTitolo;
    }

    public void setRevocaAnnulloTitolo(boolean delegaAnnulloTitolo) {
        this.revocaAnnulloTitolo = delegaAnnulloTitolo;
    }

    @Override
    public String toString() {
        return "FunzioniAbilitate [annulloTitolo=" + annulloTitolo + ", incassoTitolo=" + incassoTitolo
                + ", stornoIncasso=" + stornoIncasso + ", recuperoSospeso=" + recuperoSospeso
                + ", creaRettificaProvvigioni=" + creaRettificaProvvigioni + ", modificaTitolo=" + modificaTitolo
                + ", delegaIncasso=" + delegaIncasso + ", revocaAnnulloTitolo=" + revocaAnnulloTitolo + "]";
    }

}
