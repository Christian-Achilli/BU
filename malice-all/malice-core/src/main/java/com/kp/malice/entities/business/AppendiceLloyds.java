package com.kp.malice.entities.business;

public class AppendiceLloyds{

    private String codiceAppendice;
    private CertificatoLloyds certificatoLloyds;

    public AppendiceLloyds() {
        super();
    }

    public AppendiceLloyds(String codiceAppendice) {
        super();
        this.codiceAppendice = codiceAppendice;
    }

    public String getCodiceAppendice() {
        return codiceAppendice;
    }

    public void setCodiceAppendice(String codiceAppendice) {
        this.codiceAppendice = codiceAppendice;
    }

    public CertificatoLloyds getCertificatoLloyds() {
        return certificatoLloyds;
    }

    public void setCertificatoLloyds(CertificatoLloyds certificatoLloyds) {
        this.certificatoLloyds = certificatoLloyds;
    }

}
