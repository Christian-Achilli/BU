package com.kp.malice.entities.business;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.kp.malice.businessRules.FunzioniAbilitate;
import com.kp.malice.entities.business.IncassoTitoloLloyds.MezzoPagamento;
import com.kp.malice.entities.business.IncassoTitoloLloyds.StatoIncasso;
import com.kp.malice.entities.business.IncassoTitoloLloyds.TipoOperazione;
import com.kp.malice.entities.miscellaneous.DettaglioIncassoTitoloLloyds;

public class TitoloLloyds {       
	
	private Long id;
    private Long version;
    private int numeroRata;
    private String numeroAppendice;
    private Date dataProssimaQuietanza;
    private Date dataIncassoMessaInCopertura; // la data dell'incasso che mette il titolo in copertura: Se l'incasso è prima SOSPESO poi RECUPERATO, è la data dell'incasso RECUPERATO
    private Date dataAnnulloTitolo;
    private Date dataRegistrazione;
    private String notaAnnulloTitolo;
    private String currency;
    private BigDecimal netEuroCent = BigDecimal.ZERO;
    private BigDecimal accessoriEuroCent = BigDecimal.ZERO;
    private BigDecimal taxesEuroCent = BigDecimal.ZERO;
    private BigDecimal commissionsOnNetEuroCent = BigDecimal.ZERO;
    private BigDecimal commissionsOnAccessoriEuroCent = BigDecimal.ZERO;
    private BigDecimal percentageCommissionsOnNet = BigDecimal.ZERO;
    private BigDecimal percentageCommissionsOnAccessori = BigDecimal.ZERO;
    private Date inceptionDate;
    private Date expiryDate;
    private CertificatoLloyds certificatoLloyds;
    private ContraentePolizzaLloyds contraente = new ContraentePolizzaLloyds();
    private StatoTitolo statoTitolo;
    private String codiceCig;
    private String codiceSubagente;
    private List<IncassoTitoloLloyds> incassi = new ArrayList<IncassoTitoloLloyds>(0);
    private FunzioniAbilitate funzioniAbilitate; //TODO doesn't belong here
    private String progressivo;
    
    private static Logger log =  Logger.getLogger(TitoloLloyds.class);
    
    public boolean isIncassato() {
        return dataIncassoMessaInCopertura != null;
    }

    public BigDecimal getPremioLordoTotaleEuroCent() {
        return getNetEuroCent().add(getAccessoriEuroCent()).add(getTaxesEuroCent());
    }

    public BigDecimal getImponibileEuroCent() {
        return getNetEuroCent().add(getAccessoriEuroCent());
    }

    public BigDecimal getRimessaLloydsEuroCent() {
        BigDecimal premioTotale = getNetEuroCent().add(getAccessoriEuroCent()).add(getTaxesEuroCent());
        BigDecimal commissioniTotali = getCommissionsOnNetEuroCent().add(getCommissionsOnAccessoriEuroCent());
        BigDecimal rimessaLloyds = premioTotale.subtract(commissioniTotali);
        return rimessaLloyds;
    }

    public String getStringStatoTitolo() {
        return getStatoTitolo().name();
    }

    public void setStringStatoTitolo(String s) {
        setStatoTitolo(StatoTitolo.fromString(s));
    }

    public void setUltimoIncassoCheHaMessoIlTitoloInStatoIncassato(IncassoTitoloLloyds incasso) {
        // serve solo per la compatibilità del proxy: se c'è get ci vuole anche set
    }

    public enum StatoTitolo {
        DA_INCASSARE, INCASSATO, ANNULLATO, CONSOLIDATO;

        public static StatoTitolo fromString(String s) {
            for (StatoTitolo enu : values()) {
                if (enu.name().equals(s)) {
                    return enu;
                }
            }
            return null;
        }

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public int getNumeroRata() {
        return numeroRata;
    }

    public void setNumeroRata(int numeroRata) {
        this.numeroRata = numeroRata;
    }

    public Date getDataProssimaQuietanza() {
        return dataProssimaQuietanza;
    }

    public void setDataProssimaQuietanza(Date dataProssimaQuietanza) {
        this.dataProssimaQuietanza = dataProssimaQuietanza;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getPercentageCommissionsOnNet() {
        return percentageCommissionsOnNet;
    }

    public void setPercentageCommissionsOnNet(BigDecimal percentageCommissionsOnNet) {
        this.percentageCommissionsOnNet = percentageCommissionsOnNet;
    }

    public BigDecimal getPercentageCommissionsOnAccessori() {
        return percentageCommissionsOnAccessori;
    }

    public void setPercentageCommissionsOnAccessori(BigDecimal percentageCommissionsOnAccessori) {
        this.percentageCommissionsOnAccessori = percentageCommissionsOnAccessori;
    }

    public Date getInceptionDate() {
        return inceptionDate;
    }

    public void setInceptionDate(Date inceptionDate) {
        this.inceptionDate = inceptionDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public CertificatoLloyds getCertificatoLloyds() {
        return certificatoLloyds;
    }

    public void setCertificatoLloyds(CertificatoLloyds certificatoLloyds) {
        this.certificatoLloyds = certificatoLloyds;
    }

    public StatoTitolo getStatoTitolo() {
        return statoTitolo;
    }

    public void setStatoTitolo(StatoTitolo statoTitolo) {
        this.statoTitolo = statoTitolo;
    }

    public String getCodiceCig() {
        return codiceCig;
    }

    public void setCodiceCig(String codiceCig) {
        this.codiceCig = codiceCig;
    }

    public String getCodiceSubagente() {
        return codiceSubagente;
    }

    public void setCodiceSubagente(String codiceSubagente) {
        this.codiceSubagente = codiceSubagente;
    }

    public IncassoTitoloLloyds getUltimoIncassoCheHaMessoIlTitoloInStatoIncassato() {
        return getIncassiOrderByDataInserimentoDesc().isEmpty() ? null : (getIncassiOrderByDataInserimentoDesc().get(0)
                .isStorno() ? null : getIncassiOrderByDataInserimentoDesc().get(0));
    }

    public List<IncassoTitoloLloyds> getIncassiOrderByDataInserimentoDesc() {
        return incassi;
    }

    public void setIncassiOrderByDataInserimentoDesc(List<IncassoTitoloLloyds> incassi) {
        this.incassi = incassi;
    }

    public BigDecimal getNetEuroCent() {
        return netEuroCent;
    }

    public void setNetEuroCent(BigDecimal netEuroCent) {
        this.netEuroCent = netEuroCent;
    }

    public BigDecimal getAccessoriEuroCent() {
        return accessoriEuroCent;
    }

    public void setAccessoriEuroCent(BigDecimal accessoriEuroCent) {
        this.accessoriEuroCent = accessoriEuroCent;
    }

    public BigDecimal getTaxesEuroCent() {
        return taxesEuroCent;
    }

    public void setTaxesEuroCent(BigDecimal taxesEuroCent) {
        this.taxesEuroCent = taxesEuroCent;
    }

    public BigDecimal getCommissionsOnNetEuroCent() {
        return commissionsOnNetEuroCent;
    }

    public void setCommissionsOnNetEuroCent(BigDecimal commissionsOnNetEuroCent) {
        this.commissionsOnNetEuroCent = commissionsOnNetEuroCent;
    }

    public BigDecimal getCommissionsOnAccessoriEuroCent() {
        return commissionsOnAccessoriEuroCent;
    }

    public void setCommissionsOnAccessoriEuroCent(BigDecimal commissionsOnAccessoriEuroCent) {
        this.commissionsOnAccessoriEuroCent = commissionsOnAccessoriEuroCent;
    }

    public void incassa(IncassoTitoloLloyds incasso) throws UnsupportedOperationException {

        if (null == incasso)
            throw new IllegalArgumentException("INCASSO NULLO");
        if (null == incasso.getDataIncasso())
            throw new IllegalArgumentException("MANCA LA DATA DI INCASSO");
        if (null == incasso.getImportoIncassoEuroCent())
            throw new IllegalArgumentException("MANCA LA CIFRA DI INCASSO");
        if (null == incasso.getMezzoPagamento())
            throw new IllegalArgumentException("MANCA IL MEZZO DI PAGAMENTO");
        if (null == incasso.getStatoIncasso())
            throw new IllegalArgumentException("MANCA LO STATO DI INCASSO");
        incasso.setTipoOperazioneEnum(TipoOperazione.INCASSO);
        getIncassiOrderByDataInserimentoDesc().add(0, incasso);
        setDataIncassoMessaInCopertura(incasso.getDataIncasso());
        if(!(StatoTitolo.CONSOLIDATO == getStatoTitolo()))
            setStatoTitolo(StatoTitolo.INCASSATO);
    }

    public void rimuoviIncasso(IncassoTitoloLloyds incasso) throws UnsupportedOperationException {
        if (!isIncassato()) {
            throw new UnsupportedOperationException("NON E' POSSIBILE RIMUOVERE L'INCASSO PERCHÈ IL TITOLO NON È INCASSATO");
        }
        setDataIncassoMessaInCopertura(null);
        if(!(StatoTitolo.CONSOLIDATO == getStatoTitolo()))
            setStatoTitolo(StatoTitolo.DA_INCASSARE); //TODO work around per recupero titolo consolidato
        getIncassiOrderByDataInserimentoDesc().remove(0);
    }

    public void storna() throws UnsupportedOperationException {
        if (!isIncassato()) {
            throw new UnsupportedOperationException("IL TITOLO NON È STORNABILE PERCHÈ NON È INCASSATO");
        }
        setDataIncassoMessaInCopertura(null);
        DettaglioIncassoTitoloLloyds storno = getUltimoIncassoCheHaMessoIlTitoloInStatoIncassato().getStorno();
        if(!(StatoTitolo.CONSOLIDATO == getStatoTitolo()))
            setStatoTitolo(StatoTitolo.DA_INCASSARE); //TODO work around per recupero titolo consolidato
        getIncassiOrderByDataInserimentoDesc().add(0, storno);
    }

    public void recupera(BigDecimal sommaRecuperata, Date dataIncasso, MezzoPagamento mezzoPagamento)
            throws UnsupportedOperationException {
        if (!isIncassato()) {
            throw new UnsupportedOperationException("IL TITOLO NON È RECUPERABILE PERCHÈ NON È INCASSATO");
        }
        storna();
        DettaglioIncassoTitoloLloyds incassoEffettivo = new DettaglioIncassoTitoloLloyds();
        incassoEffettivo.setTipoOperazioneEnum(TipoOperazione.INCASSO);
        incassoEffettivo.setDataInserimento(new Date());
        incassoEffettivo.setDataIncasso(dataIncasso);
        incassoEffettivo.setImportoIncassoEuroCent(sommaRecuperata);
        incassoEffettivo.setMezzoPagamento(mezzoPagamento);
        incassoEffettivo.setStatoIncasso(StatoIncasso.EFFETTIVO.toString());
        incassa(incassoEffettivo);
    }

    public void annulla(Date dataAnnullamento, String nota) {
        this.dataAnnulloTitolo = dataAnnullamento;
        this.notaAnnulloTitolo = nota;
        setStatoTitolo(StatoTitolo.ANNULLATO);
    }

    public BigDecimal getTotaleImportoIncassiEuroCent() {
        BigDecimal totale = BigDecimal.ZERO;
        for (IncassoTitoloLloyds incasso : getIncassiOrderByDataInserimentoDesc()) {
            totale = totale.add(incasso.getImportoIncassoEuroCent());
        }
        return totale;
    }

    public Date getDataIncassoMessaInCopertura() {
        return dataIncassoMessaInCopertura;
    }

    public void setDataIncassoMessaInCopertura(Date dataIncassoMessaInCopertura) {
        this.dataIncassoMessaInCopertura = dataIncassoMessaInCopertura;
    }

    public Date getDataAnnulloTitolo() {
        return dataAnnulloTitolo;
    }

    public String getNotaAnnulloTitolo() {
        return notaAnnulloTitolo;
    }

    public void revocaAnnullo() {
        this.dataAnnulloTitolo = null;
        setStatoTitolo(StatoTitolo.DA_INCASSARE);
    }

    public void setDataAnnulloTitolo(Date dataAnnulloTitolo) {
        this.dataAnnulloTitolo = dataAnnulloTitolo;
    }

    public void setNotaAnnulloTitolo(String notaAnnulloTitolo) {
        this.notaAnnulloTitolo = notaAnnulloTitolo;
    }

    public void setFunzioniAbilitate(FunzioniAbilitate fa) {
        this.funzioniAbilitate = fa;
    }

    public FunzioniAbilitate getFunzioniAbilitate() {
        return funzioniAbilitate;
    }

    public String getNumeroAppendice() {
        if (StringUtils.isEmpty(numeroAppendice))
            return "";
        return numeroAppendice;
    }

    public void setNumeroAppendice(String numAppendice) {
        this.numeroAppendice = numAppendice;
    }

    public Date getDataRegistrazione() {
        return dataRegistrazione;
    }

    public void setDataRegistrazione(Date dataRegistrazione) {
        this.dataRegistrazione = dataRegistrazione;
    }

    public BigDecimal getCommissioniTotaliEuroCent() {
        return getCommissionsOnAccessoriEuroCent().add(getCommissionsOnNetEuroCent());
    }

    public LioReferenceCode getLioReferenceCode() {
        LloydsCoverHolder ch = getCertificatoLloyds().getFilieraLloyds().getCoverHolder();
        return null == ch ? getCertificatoLloyds().getFilieraLloyds().getBroker().getLioReferenceCode() : ch
                .getLioReferenceCode();
    }

    public void consolida() {
        setStatoTitolo(StatoTitolo.CONSOLIDATO);
    }

    public String getProgressivo() {
        return progressivo;
    }

    public void setProgressivo(String progressivo) {
        this.progressivo = progressivo;
    }
    
    public ContraentePolizzaLloyds getContraente() {
        return contraente;
    }

    public void setContraente(ContraentePolizzaLloyds contraente) {
        this.contraente = contraente;
    }

}
