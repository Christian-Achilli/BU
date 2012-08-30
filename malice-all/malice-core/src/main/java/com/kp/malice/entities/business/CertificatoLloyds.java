package com.kp.malice.entities.business;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CertificatoLloyds {

    private Long id;
    private String numero;
    private Date dataEmissioneDocumento;
    private Date dataProposta;
    private PuntoVenditaRMAPerLloyds puntoVendita;
    private FilieraLloyds filieraLloyds = new FilieraLloyds();
    private FilieraLloydsOpenMarket filieraLloydsOpenMarket = new FilieraLloydsOpenMarket();
    private Date inceptionDate;
    private String inceptionH24Time;
    private Date expiryDate;
    private String expiryH24Time;
    private String currency;
    private BigDecimal premioNettoPolizzaEuroCent = BigDecimal.ZERO;
    private BigDecimal accessoriEuroCent = BigDecimal.ZERO;
    private BigDecimal commissioniSulNettoEuroCent = BigDecimal.ZERO;
    private BigDecimal commissioniAccessoriEuroCent = BigDecimal.ZERO;
    private BigDecimal totaleImposteEuroCent = BigDecimal.ZERO;
    private int numberOfInstallments;
    private int numberOfInsurers;
    private int numberOfRisks;
    private boolean tacitRenewal;
    private List<DettaglioCoassicurazione> coassicuratori = new ArrayList<DettaglioCoassicurazione>(0);
    private List<RischioAssicurato> rischi = new ArrayList<RischioAssicurato>(0);
    private List<TitoloLloyds> titoli = new ArrayList<TitoloLloyds>(0);

    public CertificatoLloyds() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Date getDataEmissioneDocumento() {
        return dataEmissioneDocumento;
    }

    public void setDataEmissioneDocumento(Date dataEmissioneDocumento) {
        this.dataEmissioneDocumento = dataEmissioneDocumento;
    }

    public PuntoVenditaRMAPerLloyds getPuntoVendita() {
        return puntoVendita;
    }

    public void setPuntoVendita(PuntoVenditaRMAPerLloyds puntoVendita) {
        this.puntoVendita = puntoVendita;
    }

    public FilieraLloyds getFilieraLloyds() {
        return filieraLloyds;
    }

    public void setFilieraLloyds(FilieraLloyds filieraLloyds) {
        this.filieraLloyds = filieraLloyds;
    }

    public Date getInceptionDate() {
        return inceptionDate;
    }

    public void setInceptionDate(Date inceptionDate) {
        this.inceptionDate = inceptionDate;
    }

    public String getInceptionH24Time() {
        return inceptionH24Time;
    }

    public void setInceptionH24Time(String inceptionH24Time) {
        this.inceptionH24Time = inceptionH24Time;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getExpiryH24Time() {
        return expiryH24Time;
    }

    public void setExpiryH24Time(String expiryH24Time) {
        this.expiryH24Time = expiryH24Time;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getNumberOfInstallments() {
        return numberOfInstallments;
    }

    public void setNumberOfInstallments(int numberOfInstallments) {
        this.numberOfInstallments = numberOfInstallments;
    }

    public int getNumberOfInsurers() {
        return numberOfInsurers;
    }

    public void setNumberOfInsurers(int numberOfInsurers) {
        this.numberOfInsurers = numberOfInsurers;
    }

    public int getNumberOfRisks() {
        return numberOfRisks;
    }

    public void setNumberOfRisks(int numberOfRisks) {
        this.numberOfRisks = numberOfRisks;
    }

    public boolean isTacitRenewal() {
        return tacitRenewal;
    }

    public void setTacitRenewal(boolean tacitRenewal) {
        this.tacitRenewal = tacitRenewal;
    }

    public List<DettaglioCoassicurazione> getCoassicuratori() {
        return coassicuratori;
    }

    public void setCoassicuratori(List<DettaglioCoassicurazione> coassicuratori) {
        this.coassicuratori = coassicuratori;
    }

    public List<RischioAssicurato> getRischi() {
        return rischi;
    }

    public void setRischi(List<RischioAssicurato> rischi) {
        this.rischi = rischi;
    }

    public List<TitoloLloyds> getTitoli() {
        return titoli;
    }

    public void setTitoli(List<TitoloLloyds> titoli) {
        this.titoli = titoli;
    }

    /**
     * @param codice codRamo.CodUnta
     */
    public void setRischio(String codice) {
        RischioAssicurato ra = new RischioAssicurato();
        ra.setCodice(codice);
        getRischi().clear();
        getRischi().add(ra);
    }

    public String getRischio() {
        return getRischi().isEmpty() ? "" : getRischi().get(0).getCodice();
    }

    public Date getDataProposta() {
        return dataProposta;
    }

    public void setDataProposta(Date dataProposta) {
        this.dataProposta = dataProposta;
    }

    public void setQuotaLloyds(BigDecimal decimal) {
        DettaglioCoassicurazione dettaglioCoassicurazione = new DettaglioCoassicurazione();
        dettaglioCoassicurazione.setCompagniaAssuntrice(new LloydsCorp());
        dettaglioCoassicurazione.setPercentualeAssunzione(decimal);
        coassicuratori.clear();
        coassicuratori.add(dettaglioCoassicurazione);
    }

    public BigDecimal getQuotaLloyds() {
        return coassicuratori.isEmpty() ? BigDecimal.ZERO : coassicuratori.get(0).getPercentualeAssunzione();
    }

    public FilieraLloydsOpenMarket getFilieraLloydsOpenMarket() {
        return filieraLloydsOpenMarket;
    }

    public void setFilieraLloydsOpenMarket(FilieraLloydsOpenMarket filieraLloydsOpenMarket) {
        this.filieraLloydsOpenMarket = filieraLloydsOpenMarket;
    }

    public String getTipoCoassicurazione() {
        return "100% Lloyd's";

    }

    public BigDecimal getPremioNettoPolizzaEuroCent() {
        return premioNettoPolizzaEuroCent;
    }

    public void setPremioNettoPolizzaEuroCent(BigDecimal premioNettoPolizzaEuroCent) {
        this.premioNettoPolizzaEuroCent = premioNettoPolizzaEuroCent;
    }

    public BigDecimal getAccessoriEuroCent() {
        return accessoriEuroCent;
    }

    public void setAccessoriEuroCent(BigDecimal accessoriEuroCent) {
        this.accessoriEuroCent = accessoriEuroCent;
    }

    public BigDecimal getCommissioniSulNettoEuroCent() {
        return commissioniSulNettoEuroCent;
    }

    public void setCommissioniSulNettoEuroCent(BigDecimal commissioniSulNettoEuroCent) {
        this.commissioniSulNettoEuroCent = commissioniSulNettoEuroCent;
    }

    public BigDecimal getCommissioniAccessoriEuroCent() {
        return commissioniAccessoriEuroCent;
    }

    public void setCommissioniAccessoriEuroCent(BigDecimal commissioniAccessoriEuroCent) {
        this.commissioniAccessoriEuroCent = commissioniAccessoriEuroCent;
    }

    public BigDecimal getTotaleImposteEuroCent() {
        return totaleImposteEuroCent;
    }

    public void setTotaleImposteEuroCent(BigDecimal totaleImposteEuroCent) {
        this.totaleImposteEuroCent = totaleImposteEuroCent;
    }

}
