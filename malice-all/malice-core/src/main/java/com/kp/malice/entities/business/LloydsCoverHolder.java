package com.kp.malice.entities.business;

public class LloydsCoverHolder {

    private Long idEntityAliasLloydsCorp; // record su entity alias per l'encoding Lloyd's corp
    private Long idUoa;
    private String aliasCodeLloyds; //il valore in alias code del record identificato dall'idEntityAliasLloydsCorp, i.e. 110081-JAS
    private String description; // la descrizione della UOA associata
    private LioReferenceCode lioReferenceCode;

    public LloydsCoverHolder() {
    }

    public Long getId() {
        return idEntityAliasLloydsCorp;
    }

    public void setId(Long id) {
        this.idEntityAliasLloydsCorp = id;
    }

    public void setCodiceLloyds(String codice) {
        this.aliasCodeLloyds = codice;
    }

    public String getCodiceLloyds() {
        return aliasCodeLloyds;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getIdUoa() {
        return idUoa;
    }

    public void setIdUoa(Long idUoa) {
        this.idUoa = idUoa;
    }

    public LioReferenceCode getLioReferenceCode() {
        return lioReferenceCode;
    }

    public void setLioReferenceCode(LioReferenceCode lioReferenceCode) {
        this.lioReferenceCode = lioReferenceCode;
    }

}
