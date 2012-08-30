package com.kp.malice.entities.business;

public class LloydsBroker {

    private String description; // la descrizione della UOA associata
    private Long idCanaleVendita;
    private Integer codiceCanaleVendita;
    private Long idUoa;
    private LioReferenceCode lioReferenceCode;

    public LloydsBroker() {
    }

    public Long getIdCanaleVendita() {
        return idCanaleVendita;
    }

    public void setIdCanaleVendita(Long idCanaleVendita) {
        this.idCanaleVendita = idCanaleVendita;
    }

    public Long getIdUoa() {
        return idUoa;
    }

    public void setIdUoa(Long idUoa) {
        this.idUoa = idUoa;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCodiceCanaleVendita() {
        return codiceCanaleVendita;
    }

    public void setCodiceCanaleVendita(Integer codiceCanaleVendita) {
        this.codiceCanaleVendita = codiceCanaleVendita;
    }

    public LioReferenceCode getLioReferenceCode() {
        return lioReferenceCode;
    }

    public void setLioReferenceCode(LioReferenceCode lioReferenceCode) {
        this.lioReferenceCode = lioReferenceCode;
    }

}
