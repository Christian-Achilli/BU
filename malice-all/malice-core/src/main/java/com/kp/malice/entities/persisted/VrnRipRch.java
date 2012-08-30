package com.kp.malice.entities.persisted;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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

import org.hibernate.Hibernate;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.NaturalId;

@SuppressWarnings("serial")
@Entity
@Table(name = "VRN_RIP_RSC", uniqueConstraints = @UniqueConstraint(columnNames = { "codVrnRipRsc", "codPol",
        "codCompPtf", "codCnlVnd", "codPunVnd" }))
public class VrnRipRch extends RecordIdentifier implements Serializable {

    @NaturalId
    @NotNull
    private int codVrnRipRsc; // è quello referenziato da Mov
    @NaturalId
    @NotNull
    private String codPol;
    @NaturalId
    @NotNull
    private int codCompPtf; // compagnia che ha emesso la polizza
    @NaturalId
    @NotNull
    private int codCompPtfCoas;
    @NaturalId
    @NotNull
    private int codCnlVnd;
    @NaturalId
    @NotNull
    private String codPunVnd;

    private long codPrgMov;// si riferisce al movimento che ha provocato l'ultimo aggiornameto di vrnRipRsc

    @ManyToOne
    @ForeignKey(name = "FK_POL_COMP_PTF_COAS")
    private CompPtf compPtfCoas;

    @ManyToOne
    @ForeignKey(name = "FK_POL_COMP_PTF")
    private CompPtf compPtf;

    @NotNull
    private Date datIniVdtCoas; // NOT NULL, -->> la stessa di data effetto movimento datEfftMov
    @NotNull
    private Date datFineVdtCoas; //9999-12-31
    private String codPolDlgAlr; // numero polizza di RMA, usata nel caso di delega altrui
    private BigDecimal prctQtaRis;

    @ManyToOne
    @ForeignKey(name = "FK_POL_VRN_RIP_RSC")
    private Pol pol;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "codCompPtf", referencedColumnName = "codCompPtf", insertable = false, updatable = false),
            @JoinColumn(name = "codPol", referencedColumnName = "codPol", insertable = false, updatable = false) })
    private Set<Mov> movs = new HashSet<Mov>(0);

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "codCompPtf", referencedColumnName = "codCompPtf", insertable = false, updatable = false),
            @JoinColumn(name = "codPol", referencedColumnName = "codPol", insertable = false, updatable = false) })
    private Set<RipDttTtl> dttTtlCoass = new HashSet<RipDttTtl>(0);

    public VrnRipRch() {
    }

    public VrnRipRch(RecordIdentifier ri, Pol pol, int codVrnRipRsc, int codCompCoass) {
        super(ri);
        setPol(pol);
        setCodVrnRipRsc(codVrnRipRsc);
        setCodPol(pol.getCodPol());
        setCodCompPtf(pol.getCodCompPtf());
        setCodCompPtfCoas(codCompCoass);
        setCodCnlVnd(pol.getPunVnd().getCodCnlVnd());
        setCodPunVnd(pol.getPunVnd().getCodPunVnd());
        setDatIniVdtCoas(new Date());
        setDatFineVdtCoas(new Date());
    }

    public Set<RipDttTtl> getDttTtlCoass() {
        return dttTtlCoass;
    }

    public void setDttTtlCoass(Set<RipDttTtl> dttTtlCoass) {
        this.dttTtlCoass = dttTtlCoass;
    }

    public Set<Mov> getMovs() {
        return movs;
    }

    public void setMovs(Set<Mov> movs) {
        this.movs = movs;
    }

    public Pol getPol() {
        return pol;
    }

    public void setPol(Pol pol) {
        this.pol = pol;
    }

    public long getCodPrgMov() {
        return codPrgMov;
    }

    public void setCodPrgMov(long codPrgMov) {
        this.codPrgMov = codPrgMov;
    }

    public BigDecimal getPrctQtaRis() {
        return prctQtaRis;
    }

    public void setPrctQtaRis(BigDecimal prctQtaRis) {
        this.prctQtaRis = prctQtaRis;
    }

    public int getCodVrnRipRsc() {
        return codVrnRipRsc;
    }

    public void setCodVrnRipRsc(int codVrnRipRsc) {
        this.codVrnRipRsc = codVrnRipRsc;
    }

    public String getCodPol() {
        return codPol;
    }

    public void setCodPol(String codPol) {
        this.codPol = codPol;
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

    public String getCodPunVnd() {
        return codPunVnd;
    }

    public void setCodPunVnd(String codPunVnd) {
        this.codPunVnd = codPunVnd;
    }

    public Date getDatIniVdtCoas() {
        return datIniVdtCoas;
    }

    public void setDatIniVdtCoas(Date datIniVdtCoas) {
        this.datIniVdtCoas = datIniVdtCoas;
    }

    public Date getDatFineVdtCoas() {
        return datFineVdtCoas;
    }

    public void setDatFineVdtCoas(Date datFineVdtCoas) {
        this.datFineVdtCoas = datFineVdtCoas;
    }

    public String getCodPolDlgAlr() {
        return codPolDlgAlr;
    }

    public void setCodPolDlgAlr(String codPolDlgAlr) {
        this.codPolDlgAlr = codPolDlgAlr;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + codCnlVnd;
        result = prime * result + codCompPtf;
        result = prime * result + ((codPol == null) ? 0 : codPol.hashCode());
        result = prime * result + ((codPunVnd == null) ? 0 : codPunVnd.hashCode());
        result = prime * result + codVrnRipRsc;
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
        VrnRipRch other = (VrnRipRch) obj;
        if (codCnlVnd != other.codCnlVnd)
            return false;
        if (codCompPtf != other.codCompPtf)
            return false;
        if (codPol == null) {
            if (other.codPol != null)
                return false;
        } else if (!codPol.equals(other.codPol))
            return false;
        if (codPunVnd == null) {
            if (other.codPunVnd != null)
                return false;
        } else if (!codPunVnd.equals(other.codPunVnd))
            return false;
        if (codVrnRipRsc != other.codVrnRipRsc)
            return false;
        return true;
    }

    public CompPtf getCompPtfCoas() {
        return compPtfCoas;
    }

    public void setCompPtfCoas(CompPtf compPtfCoas) {
        this.compPtfCoas = compPtfCoas;
    }

    public int getCodCompPtfCoas() {
        return codCompPtfCoas;
    }

    public void setCodCompPtfCoas(int codCompPtfCoas) {
        this.codCompPtfCoas = codCompPtfCoas;
    }

    public CompPtf getCompPtf() {
        return compPtf;
    }

    public void setCompPtf(CompPtf compPtf) {
        this.compPtf = compPtf;
    }

}
