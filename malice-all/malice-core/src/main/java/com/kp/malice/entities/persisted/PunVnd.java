package com.kp.malice.entities.persisted;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.NaturalId;

@SuppressWarnings("serial")
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Table(name = "PUN_VND", uniqueConstraints = @UniqueConstraint(columnNames = { "codCompPtf", "codCnlVnd", "codPunVnd" }))
public class PunVnd extends RecordIdentifier implements java.io.Serializable {

    //    PRIMARY KEY
    //    (COD_COMP_ANIA, COD_CNL_VND, COD_PUN_VND));
    @NotNull
    @NaturalId
    private int codCompPtf;
    @NotNull
    @NaturalId
    private int codCnlVnd;
    @NotNull
    @NaturalId
    private String codPunVnd;
    @NotNull
    private long codUntaOperAun;
    private String denPunVnd;
    private Date datCostPunVnd;
    private Date datChiPunVnd;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ForeignKey(name = "FK_PUN_VND_COMP_PTF")
    private CompPtf compPtf;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ForeignKey(name = "FK_PUN_VND_CNL_VND")
    private CnlVnd cnlVnd;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @ForeignKey(name = "FK_PUN_VND_UOA")
    private UntaOperAun untaOperAun;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "codCompPtf", referencedColumnName = "codCompPtf", insertable = true, updatable = true, nullable = true),
            @JoinColumn(name = "codCnlVnd", referencedColumnName = "codCnlVnd", insertable = true, updatable = true, nullable = true),
            @JoinColumn(name = "codPunVnd", referencedColumnName = "codPunVnd", insertable = true, updatable = true, nullable = true) })
    private Set<Pol> pols = new HashSet<Pol>(0);

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "codCompPtf", referencedColumnName = "codCompPtf", insertable = true, updatable = true, nullable = true),
            @JoinColumn(name = "codCnlVnd", referencedColumnName = "codCnlVnd", insertable = true, updatable = true, nullable = true),
            @JoinColumn(name = "codPunVnd", referencedColumnName = "codPunVnd", insertable = true, updatable = true, nullable = true) })
    private Set<TtlCtb> ttlCtbs = new HashSet<TtlCtb>(0);

    public PunVnd() {
    }

    public PunVnd(RecordIdentifier rid, CnlVnd canaleVendita, String codicePuntoVendita, CompPtf compPtf,
            UntaOperAun uoa) {
        super(rid);
        setCnlVnd(canaleVendita);
        setCodPunVnd(codicePuntoVendita);
        setCompPtf(compPtf);
        setCodCnlVnd(canaleVendita.getCodCnlVnd());
        setCodCompPtf(compPtf.getCodCompPtf());
        setUntaOperAun(uoa);
        setCodUntaOperAun(uoa.getCodUntaOperAun());
    }

    public CompPtf getCompPtf() {
        return this.compPtf;
    }

    public void setCompPtf(CompPtf compPtf) {
        this.compPtf = compPtf;
    }

    public UntaOperAun getUntaOperAun() {
        return this.untaOperAun;
    }

    public void setUntaOperAun(UntaOperAun untaOperAun) {
        this.untaOperAun = untaOperAun;
    }

    public String getDenPunVnd() {
        return this.denPunVnd;
    }

    public void setDenPunVnd(String denPunVnd) {
        this.denPunVnd = denPunVnd;
    }

    public Date getDatCostPunVnd() {
        return this.datCostPunVnd;
    }

    public void setDatCostPunVnd(Date datCostPunVnd) {
        this.datCostPunVnd = datCostPunVnd;
    }

    public Date getDatChiPunVnd() {
        return this.datChiPunVnd;
    }

    public void setDatChiPunVnd(Date datChiPunVnd) {
        this.datChiPunVnd = datChiPunVnd;
    }

    public Set<Pol> getPols() {
        return this.pols;
    }

    public void setPols(Set<Pol> pols) {
        this.pols = pols;
    }

    public Set<TtlCtb> getTtlCtbs() {
        return this.ttlCtbs;
    }

    public void setTtlCtbs(Set<TtlCtb> ttlCtbs) {
        this.ttlCtbs = ttlCtbs;
    }

    public String getCodPunVnd() {
        return codPunVnd;
    }

    public void setCodPunVnd(String codPunVnd) {
        this.codPunVnd = codPunVnd;
    }

    public CnlVnd getCnlVnd() {
        return cnlVnd;
    }

    public void setCnlVnd(CnlVnd cnlVnd) {
        this.cnlVnd = cnlVnd;
    }

    public int getCodCompPtf() {
        return codCompPtf;
    }

    public void setCodCompPtf(int codCompPtf) {
        this.codCompPtf = codCompPtf;
    }

    public int getCodCnlVnd() {
        return codCnlVnd;
    }

    public void setCodCnlVnd(int codCnlVnd) {
        this.codCnlVnd = codCnlVnd;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + codCnlVnd;
        result = prime * result + codCompPtf;
        result = prime * result + ((codPunVnd == null) ? 0 : codPunVnd.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        PunVnd other = (PunVnd) obj;
        if (codCnlVnd != other.codCnlVnd)
            return false;
        if (codCompPtf != other.codCompPtf)
            return false;
        if (codPunVnd == null) {
            if (other.codPunVnd != null)
                return false;
        } else if (!codPunVnd.equals(other.codPunVnd))
            return false;
        return true;
    }

    public long getCodUntaOperAun() {
        return codUntaOperAun;
    }

    public void setCodUntaOperAun(long codUntaOperAun) {
        this.codUntaOperAun = codUntaOperAun;
    }

}
