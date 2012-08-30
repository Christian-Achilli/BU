package com.kp.malice.entities.persisted;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.NaturalId;

@SuppressWarnings("serial")
@Entity
@Table(name = "POL", uniqueConstraints = @UniqueConstraint(columnNames = { "codCompPtf", "codPol" }))
public class Pol extends RecordIdentifier {

    @NaturalId
    @NotNull
    private String codPol;
    @NaturalId
    @NotNull
    private int codCompPtf;
    @NotNull
    @NaturalId
    private int codCompPtfPunVnd;
    @NotNull
    @NaturalId
    private int codCnlVnd;
    @NotNull
    @NaturalId
    private String codPunVnd;

    @ManyToOne(fetch = FetchType.LAZY)
    @ForeignKey(name = "FK_POL_COMP_PTF")
    private CompPtf compPtf;

    @ManyToOne(fetch = FetchType.EAGER)
    @ForeignKey(name = "FK_POL_PUN_VND")
    private PunVnd punVnd;

    @Column(name = "COD_TIP_COAS")
    @Enumerated(EnumType.STRING)
    @NotNull
    private TipoCoass codTipCoas;//1=100% cmp_ptf, 2=delega comp_ptf (Lloyd's lead), 3= delega altrui (RMA delegataria)
    @Column(name = "COD_CH")
    private String codiceCoverHolder; //pin1-pin2

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "codCompPtf", referencedColumnName = "codCompPtf", insertable = false, updatable = false, nullable = true),
            @JoinColumn(name = "codPol", referencedColumnName = "codPol", insertable = false, updatable = false, nullable = true) })
    @OrderBy("recordId desc")
    private Set<VrnPol> vrnPols = new HashSet<VrnPol>(0);

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "codCompPtf", referencedColumnName = "codCompPtf", insertable = false, updatable = false, nullable = true),
            @JoinColumn(name = "codPol", referencedColumnName = "codPol", insertable = false, updatable = false, nullable = true) })
    @OrderBy("recordId desc")
    private Set<VrnRipRch> vrnRipRchs = new HashSet<VrnRipRch>(0);

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "codCompPtf", referencedColumnName = "codCompPtf", insertable = false, updatable = false, nullable = true),
            @JoinColumn(name = "codPol", referencedColumnName = "codPol", insertable = false, updatable = false, nullable = true) })
    @OrderBy("recordId desc")
    private Set<Mov> movs = new HashSet<Mov>(0);

    public Pol() {
    }

    public Pol(RecordIdentifier whoAndWhen, String codPol, CompPtf compPtf, PunVnd punVnd) {
        super(whoAndWhen);
        setCodPol(codPol);
        setCompPtf(compPtf);
        setCodCompPtf(compPtf.getCodCompPtf());
        setCodCnlVnd(punVnd.getCodCnlVnd());
        setCodPunVnd(punVnd.getCodPunVnd());
        setPunVnd(punVnd);
        setCodCompPtfPunVnd(compPtf.getCodCompPtf());
    }

    public enum TipoCoass {
        //1=100% cmp_ptf, 2=delega comp_ptf (Lloyd's lead), 3= delega altrui (RMA delegataria)
        COMP_PTF, COMP_PTF_LEAD, DELEGA_ALTRUI;
    }

    public enum StatoPolizza {
        IN_VIGORE, SOSPESA, ANNULLATA;
    }

    public TipoCoass getCodTipCoas() {
        return this.codTipCoas;
    }

    public void setCodTipCoas(TipoCoass codTipCoas) {
        this.codTipCoas = codTipCoas;
    }

    public PunVnd getPunVnd() {
        return punVnd;
    }

    public void setPunVnd(PunVnd punVnd) {
        this.punVnd = punVnd;
    }

    public CompPtf getCompPtf() {
        return compPtf;
    }

    public void setCompPtf(CompPtf compPtf) {
        this.compPtf = compPtf;
    }

    public Set<VrnRipRch> getVrnRipRchs() {
        return vrnRipRchs;
    }

    public void setVrnRipRchs(Set<VrnRipRch> vrnRipRchs) {
        this.vrnRipRchs = vrnRipRchs;
    }

    public String getCodiceCoverHolder() {
        return codiceCoverHolder;
    }

    public void setCodiceCoverHolder(String codiceCoverHolder) {
        this.codiceCoverHolder = codiceCoverHolder;
    }

    public String getCodPol() {
        return codPol;
    }

    public void setCodPol(String codPol) {
        this.codPol = codPol;
    }

    public Set<VrnPol> getVrnPols() {
        return vrnPols;
    }

    public void setVrnPols(Set<VrnPol> vrnPols) {
        this.vrnPols = vrnPols;
    }

    public int getCodCompPtf() {
        return codCompPtf;
    }

    public void setCodCompPtf(int codCompPtf) {
        this.codCompPtf = codCompPtf;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + codCompPtf;
        result = prime * result + ((codPol == null) ? 0 : codPol.hashCode());
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
        Pol other = (Pol) obj;
        if (codCompPtf != other.codCompPtf)
            return false;
        if (codPol == null) {
            if (other.codPol != null)
                return false;
        } else if (!codPol.equals(other.codPol))
            return false;
        return true;
    }

    public Set<Mov> getMovs() {
        return movs;
    }

    public void setMovs(Set<Mov> movs) {
        this.movs = movs;
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

    public int getCodCompPtfPunVnd() {
        return codCompPtfPunVnd;
    }

    public void setCodCompPtfPunVnd(int codCompPtfPunVnd) {
        this.codCompPtfPunVnd = codCompPtfPunVnd;
    }

}
