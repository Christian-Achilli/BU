package com.kp.malice.entities.persisted;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.NamedNativeQueries;
import org.hibernate.annotations.NamedNativeQuery;

import com.kp.malice.entities.business.ChiusuraMensileLio.StatoChiusura;

@SuppressWarnings("serial")
@Entity
@Table(name = "CHR_MENSILI_LIO")
@NamedNativeQueries({
        @NamedNativeQuery(name = "chr.tuttePerAgenzia", query = "select * from CHR_MENSILI_LIO "
                + "where UOA_ID = :uoaId order by ANNO_RIF DESC, MESE_RIF DESC", resultClass = ChrMslLio.class),
        @NamedNativeQuery(name = "chr.perMesePerAgenzia", query = "select * from CHR_MENSILI_LIO "
                + "where UOA_ID = :uoaId and MESE_RIF = :meseRif and ANNO_RIF = :annoRif", resultClass = ChrMslLio.class) })
public class ChrMslLio extends RecordIdentifier {

    @Column(name = "MESE_RIF")
    private int meseDiRiferimento;
    @Column(name = "ANNO_RIF")
    private int annoDiRiferimento;
    @Column(name = "NUM_EC")
    private int numEstrattiConto = 0;
    @Column(name = "NUM_TTL")
    private int numTitoli = 0;
    @Column(name = "TOT_COMMISSIONI")
    private BigDecimal totCommissioni = BigDecimal.ZERO;
    @Column(name = "TOT_PREMI")
    private BigDecimal totPremi = BigDecimal.ZERO;
    @Column(name = "ST_CHR")
    @Enumerated(EnumType.STRING)
    private StatoChiusura statoChiusura = StatoChiusura.APERTA;
    @Column(name = "DT_INVIO")
    private Date dtInvio;
    @Column(name = "NOTA")
    private String nota;
    @Column(name = "SENT_TO")
    private String inviataA;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UOA_ID", referencedColumnName = "ID")
    private UntaOperAun untaOperAun;
    @OneToMany(mappedBy = "chiusura", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("recordId desc")
    private Set<EstrContLio> estrattiConto = new HashSet<EstrContLio>(0);

    public ChrMslLio() {

    }

    public ChrMslLio(RecordIdentifier ri) {
        super(ri);
    }

    public int getNumTitoli() {
        return numTitoli;
    }

    public void setNumTitoli(int numTitoli) {
        this.numTitoli = numTitoli;
    }

    public BigDecimal getTotCommissioni() {
        return totCommissioni;
    }

    public void setTotCommissioni(BigDecimal totCommissioni) {
        this.totCommissioni = totCommissioni;
    }

    public BigDecimal getTotPremi() {
        return totPremi;
    }

    public void setTotPremi(BigDecimal totPremi) {
        this.totPremi = totPremi;
    }

    public UntaOperAun getUntaOperAun() {
        return untaOperAun;
    }

    public void setUntaOperAun(UntaOperAun untaOperAun) {
        this.untaOperAun = untaOperAun;
    }

    public Set<EstrContLio> getEstrattiConto() {
        return estrattiConto;
    }

    public void setEstrattiConto(Set<EstrContLio> estrattiConto) {
        this.estrattiConto = estrattiConto;
    }

    public int getNumEstrattiConto() {
        return numEstrattiConto;
    }

    public void setNumEstrattiConto(int numEstrattiConto) {
        this.numEstrattiConto = numEstrattiConto;
    }

    public StatoChiusura getStatoChiusura() {
        return statoChiusura;
    }

    public void setStatoChiusura(StatoChiusura statoChiusura) {
        this.statoChiusura = statoChiusura;
    }

    public Date getDtInvio() {
        return dtInvio;
    }

    public void setDtInvio(Date dtInvio) {
        this.dtInvio = dtInvio;
    }

    public String getInviataA() {
        return inviataA;
    }

    public void setInviataA(String inviataA) {
        this.inviataA = inviataA;
    }

    public int getAnnoDiRiferimento() {
        return annoDiRiferimento;
    }

    public void setAnnoDiRiferimento(int annoDiRiferimento) {
        this.annoDiRiferimento = annoDiRiferimento;
    }

    public int getMeseDiRiferimento() {
        return meseDiRiferimento;
    }

    public void setMeseDiRiferimento(int meseDiRiferimento) {
        this.meseDiRiferimento = meseDiRiferimento;
    }

    public void add(TtlCtb ttl) {
        setNumTitoli(getNumTitoli() + 1);
        setTotCommissioni(getTotCommissioni().add(ttl.getTotCommissioni()));
        setTotPremi(getTotPremi().add(ttl.getPremioTotale()));
    }

    public void remove(TtlCtb ttl) {
        setNumTitoli(getNumTitoli() - 1);
        setTotCommissioni(getTotCommissioni().subtract(ttl.getTotCommissioni()));
        setTotPremi(getTotPremi().subtract(ttl.getPremioTotale()));
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }
}
