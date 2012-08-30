package com.kp.malice.entities.business;

public class LioReferenceCode {

    private String codice;
    private String referente;

    public LioReferenceCode(String codice, String referente) {
        this.codice = codice;
        this.referente = referente;
    }

    @SuppressWarnings("unused")
    private LioReferenceCode() {
    }

    public String getCodice() {
        return codice;
    }

    public String getReferente() {
    	if(codice.equals("*"))
    		return referente; 
        return referente + "-" + codice;
    }

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public void setReferente(String referente) {
		this.referente = referente;
	}
    
}
