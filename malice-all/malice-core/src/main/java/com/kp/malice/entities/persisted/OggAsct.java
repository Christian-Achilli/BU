package com.kp.malice.entities.persisted;

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

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.NaturalId;

@SuppressWarnings("serial")
@Entity
@Table(name = "OGG_ASCT", uniqueConstraints = @UniqueConstraint(columnNames = { "codVrnPol", "codOggAsct",
        "codCompPtf", "codPol" }))
public class OggAsct extends RecordIdentifier {

    //    PRIMARY KEY
    //    (COD_COMP_ANIA, COD_POL, COD_VRN_POL, COD_OGG_ASCT));
    @NaturalId
    @NotNull
    private int codCompPtf;
    @NaturalId
    @NotNull
    private String codPol;
    @NaturalId
    @NotNull
    private int codVrnPol;
    @NaturalId
    @NotNull
    private int codOggAsct = 1;

    @ManyToOne(fetch = FetchType.LAZY)
    @ForeignKey(name = "FK_OGG_ASCT_VRN_POL")
    private VrnPol vrnPol;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "codCompPtf", referencedColumnName = "codCompPtf", insertable = false, updatable = false),
            @JoinColumn(name = "codPol", referencedColumnName = "codPol", insertable = false, updatable = false),
            @JoinColumn(name = "codVrnPol", referencedColumnName = "codVrnPol", insertable = false, updatable = false),
            @JoinColumn(name = "codOggAsct", referencedColumnName = "codOggAsct", insertable = false, updatable = false) })
    private Set<SezPrst> sezPrsts = new HashSet<SezPrst>(0);

    private String dscOgg; // vuoto
    private long codBne; // fisso a 1
    private Date datInclOgg; // data effetto del movimento di emissione
    private Date datEsclOgg; // 9999-12-31
    private Integer numSin; // 0
    private BigDecimal impTotLqt; // 0

    public OggAsct() {
    }

    public OggAsct(RecordIdentifier ri, VrnPol vrnPol, int codOggAsct) {
        super(ri);
        setVrnPol(vrnPol);
        setCodOggAsct(codOggAsct);
        setCodVrnPol(codVrnPol);
        setCodPol(vrnPol.getCodPol());
        setCodCompPtf(vrnPol.getCodCompPtf());
        setCodVrnPol(vrnPol.getCodVrnPol());
    }

    public VrnPol getVrnPol() {
        return this.vrnPol;
    }

    public void setVrnPol(VrnPol vrnPol) {
        this.vrnPol = vrnPol;
    }

    public String getDscOgg() {
        return this.dscOgg;
    }

    public void setDscOgg(String dscOgg) {
        this.dscOgg = dscOgg;
    }

    public long getCodBne() {
        return this.codBne;
    }

    public void setCodBne(long codBne) {
        this.codBne = codBne;
    }

    public Date getDatInclOgg() {
        return this.datInclOgg;
    }

    public void setDatInclOgg(Date datInclOgg) {
        this.datInclOgg = datInclOgg;
    }

    public Date getDatEsclOgg() {
        return this.datEsclOgg;
    }

    public void setDatEsclOgg(Date datEsclOgg) {
        this.datEsclOgg = datEsclOgg;
    }

    public Integer getNumSin() {
        return this.numSin;
    }

    public void setNumSin(Integer numSin) {
        this.numSin = numSin;
    }

    public BigDecimal getImpTotLqt() {
        return this.impTotLqt;
    }

    public void setImpTotLqt(BigDecimal impTotLqt) {
        this.impTotLqt = impTotLqt;
    }

    public Set<SezPrst> getSezPrsts() {
        return this.sezPrsts;
    }

    public void setSezPrsts(Set<SezPrst> sezPrsts) {
        this.sezPrsts = sezPrsts;
    }

    public int getCodOggAsct() {
        return codOggAsct;
    }

    public void setCodOggAsct(int codOggAsct) {
        this.codOggAsct = codOggAsct;
    }

    public int getCodCompPtf() {
        return codCompPtf;
    }

    public void setCodCompPtf(int codCompPtf) {
        this.codCompPtf = codCompPtf;
    }

    public String getCodPol() {
        return codPol;
    }

    public void setCodPol(String codPol) {
        this.codPol = codPol;
    }

    public int getCodVrnPol() {
        return codVrnPol;
    }

    public void setCodVrnPol(int codVrnPol) {
        this.codVrnPol = codVrnPol;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + codCompPtf;
        result = prime * result + codOggAsct;
        result = prime * result + ((codPol == null) ? 0 : codPol.hashCode());
        result = prime * result + codVrnPol;
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
        OggAsct other = (OggAsct) obj;
        if (codCompPtf != other.codCompPtf)
            return false;
        if (codOggAsct != other.codOggAsct)
            return false;
        if (codPol == null) {
            if (other.codPol != null)
                return false;
        } else if (!codPol.equals(other.codPol))
            return false;
        if (codVrnPol != other.codVrnPol)
            return false;
        return true;
    }

}
