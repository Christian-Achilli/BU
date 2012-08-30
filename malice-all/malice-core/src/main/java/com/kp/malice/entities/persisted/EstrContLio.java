package com.kp.malice.entities.persisted;

import java.math.BigDecimal;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.NamedNativeQueries;
import org.hibernate.annotations.NamedNativeQuery;

import com.kp.malice.entities.business.LioReferenceCode;

@SuppressWarnings("serial")
@Entity
@Table(name = "ESTRATTI_CONTO")
@NamedNativeQueries({
        @NamedNativeQuery(name = "ec.perLioCode", query = "select * from ESTRATTI_CONTO "
                + "where LIO_REF_CODE = :lioCode and CHR_ID = :chId", resultClass = EstrContLio.class),
        @NamedNativeQuery(name = "ec.trovaEcPerTitolo", query = "select ec.* from  ESTR_CNT_TTL ect, ESTRATTI_CONTO ec "
                + "where ect.TTL_ID = :ttlId and ect.ESTR_ID = ec.ID", resultClass = EstrContLio.class) })
public class EstrContLio extends RecordIdentifier {

    @Column(name = "LIO_REF_CODE")
    private String lioReferenceCodeString;
    @Column(name = "NUM_TTL")
    private int numTitoli = 0;
    @Column(name = "TOT_COMMISSIONI")
    private BigDecimal totCommissioni = BigDecimal.ZERO;
    @Column(name = "TOT_PREMI")
    private BigDecimal totPremi = BigDecimal.ZERO;
    @Column(name = "TOT_RIMESSE")
    private BigDecimal totRimesse = BigDecimal.ZERO;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CHR_ID", referencedColumnName = "ID")
    private ChrMslLio chiusura;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PUN_VND_ID", referencedColumnName = "ID")
    private PunVnd punVndDelegatoIncasso;
    @OneToMany(mappedBy = "estrattoConto", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("recordId desc")
    private Set<EstrCntTtl> titoliEstratto;

    public EstrContLio() {

    }

    public boolean mustBeDeletedIfEmpty() {
        return getNumTitoli() == 0
                && (("9000".equals(getLioReferenceCodeString())) || "9005".equals(getLioReferenceCodeString()));
    }

    public EstrContLio(RecordIdentifier ri) {
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

    public BigDecimal getTotRimesse() {
        return totRimesse;
    }

    public void setTotRimesse(BigDecimal totRimesse) {
        this.totRimesse = totRimesse;
    }

    public PunVnd getPunVndDelegatoIncasso() {
        return punVndDelegatoIncasso;
    }

    public void setPunVndDelegatoIncasso(PunVnd punVndDelegatoIncasso) {
        this.punVndDelegatoIncasso = punVndDelegatoIncasso;
    }

    public ChrMslLio getChiusura() {
        return chiusura;
    }

    public void setChiusura(ChrMslLio chiusura) {
        this.chiusura = chiusura;
    }

    public Set<EstrCntTtl> getTitoliEstratto() {
        return titoliEstratto;
    }

    public void setTitoliEstratto(Set<EstrCntTtl> titoliEstratto) {
        this.titoliEstratto = titoliEstratto;
    }

    public void add(TtlCtb ttl) {
        setNumTitoli(getNumTitoli() + 1);
        setTotCommissioni(getTotCommissioni().add(ttl.getTotCommissioni()));
        setTotPremi(getTotPremi().add(ttl.getPremioTotale()));
        setTotRimesse(getTotRimesse().add(ttl.getRimessa()));
    }

    public void remove(TtlCtb ttl) {
        setNumTitoli(getNumTitoli() - 1);
        setTotCommissioni(getTotCommissioni().subtract(ttl.getTotCommissioni()));
        setTotPremi(getTotPremi().subtract(ttl.getPremioTotale()));
        setTotRimesse(getTotRimesse().subtract(ttl.getRimessa()));
    }

    public String getLioReferenceCodeString() {
        return lioReferenceCodeString;
    }

    public void setLioReferenceCodeString(String lioReferenceCodeString) {
        this.lioReferenceCodeString = lioReferenceCodeString;
    }
}
