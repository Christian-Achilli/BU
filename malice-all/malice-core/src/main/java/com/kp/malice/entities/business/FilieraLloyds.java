package com.kp.malice.entities.business;

public class FilieraLloyds {

    private AgenziaRMA agenziaRma;
    private BindingAuthority bindingAuthority;
    private LloydsCoverHolder coverHolder;
    private LloydsBroker broker;

    public FilieraLloyds() {

    }

    public String getReferente() throws Exception {
        return coverHolder == null ? broker.getDescription() : coverHolder.getDescription();
    }

    public void setReferente(String s) {
        // lasciata per compatibilità col proxy
    }

    public BindingAuthority getBindingAuthority() {
        return bindingAuthority;
    }

    public void setBindingAuthority(BindingAuthority bindingAuthority) {
        this.bindingAuthority = bindingAuthority;
    }

    public LloydsCoverHolder getCoverHolder() {
        return coverHolder;
    }

    public void setCoverHolder(LloydsCoverHolder coverHolder) {
        this.coverHolder = coverHolder;
    }

    public AgenziaRMA getAgenziaRma() {
        return agenziaRma;
    }

    public void setAgenziaRma(AgenziaRMA agenziaRma) {
        this.agenziaRma = agenziaRma;
    }

    public LloydsBroker getBroker() {
        return broker;
    }

    public void setBroker(LloydsBroker broker) {
        this.broker = broker;
    }

}
