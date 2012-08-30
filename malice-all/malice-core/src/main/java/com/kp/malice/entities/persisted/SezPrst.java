package com.kp.malice.entities.persisted;

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
@Table(name = "SEZ_PRST", uniqueConstraints = @UniqueConstraint(columnNames = { "codVrnPol", "codOggAsct",
        "codCompPtf", "codPol", "codPro", "codVrnPro", "codSez" }))
public class SezPrst extends RecordIdentifier {

    //    PRIMARY KEY
    //    (COD_COMP_ANIA, COD_POL, COD_VRN_POL, COD_OGG_ASCT, COD_PRO, COD_VRN_PRO, COD_SEZ));
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
    @NaturalId
    @NotNull
    private long codPro = 1;
    @NaturalId
    @NotNull
    private int codVrnPro = 1;
    // 10.002
    @NaturalId
    @NotNull
    private String codSez;//10

    //    FOREIGN KEY (COD_COMP_ANIA, COD_POL, COD_VRN_POL, COD_OGG_ASCT) 
    //    REFERENCES AD2K.OGG_ASCT (COD_COMP_ANIA,COD_POL,COD_VRN_POL,COD_OGG_ASCT));
    @ManyToOne(fetch = FetchType.LAZY)
    //    @JoinColumns({
    //            @JoinColumn(name = "codCompPtf", referencedColumnName = "codCompPtf", insertable = false, updatable = false),
    //            @JoinColumn(name = "codPol", referencedColumnName = "codPol", insertable = false, updatable = false),
    //            @JoinColumn(name = "codVrnPol", referencedColumnName = "codVrnPol", insertable = false, updatable = false),
    //            @JoinColumn(name = "codOggAsct", referencedColumnName = "codOggAsct", insertable = false, updatable = false) })
    @ForeignKey(name = "FK_SEZ_PRST_OGG_ASCT")
    private OggAsct oggAsct;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "codCompPtf", referencedColumnName = "codCompPtf", insertable = false, updatable = false),
            @JoinColumn(name = "codPol", referencedColumnName = "codPol", insertable = false, updatable = false),
            @JoinColumn(name = "codVrnPol", referencedColumnName = "codVrnPol", insertable = false, updatable = false),
            @JoinColumn(name = "codOggAsct", referencedColumnName = "codOggAsct", insertable = false, updatable = false),
            @JoinColumn(name = "codPro", referencedColumnName = "codPro", insertable = false, updatable = false),
            @JoinColumn(name = "codVrnPro", referencedColumnName = "codVrnPro", insertable = false, updatable = false),
            @JoinColumn(name = "codSez", referencedColumnName = "codSez", insertable = false, updatable = false) })
    private Set<GarPrst> garPrsts = new HashSet<GarPrst>(0);

    public SezPrst() {
    }

    public SezPrst(RecordIdentifier ri, OggAsct oggAsct, int codPro, int codVrnPro, String codSez) {
        super(ri);
        setOggAsct(oggAsct);
        setCodOggAsct(codOggAsct);
        setCodVrnPol(codVrnPol);
        setCodPol(oggAsct.getCodPol());
        setCodCompPtf(oggAsct.getCodCompPtf());
        setCodVrnPol(oggAsct.getCodVrnPol());
        setCodPro(codPro);
        setCodVrnPro(codVrnPro);
        setCodSez(codSez);
    }

    public OggAsct getOggAsct() {
        return this.oggAsct;
    }

    public void setOggAsct(OggAsct oggAsct) {
        this.oggAsct = oggAsct;
    }

    public Set<GarPrst> getGarPrsts() {
        return this.garPrsts;
    }

    public void setGarPrsts(Set<GarPrst> garPrsts) {
        this.garPrsts = garPrsts;
    }

    public long getCodPro() {
        return codPro;
    }

    public void setCodPro(long codPro) {
        this.codPro = codPro;
    }

    public int getCodVrnPro() {
        return codVrnPro;
    }

    public void setCodVrnPro(int codVrnPro) {
        this.codVrnPro = codVrnPro;
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

    public int getCodOggAsct() {
        return codOggAsct;
    }

    public void setCodOggAsct(int codOggAsct) {
        this.codOggAsct = codOggAsct;
    }

    public String getCodSez() {
        return codSez;
    }

    public void setCodSez(String codSez) {
        this.codSez = codSez;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + codCompPtf;
        result = prime * result + codOggAsct;
        result = prime * result + ((codPol == null) ? 0 : codPol.hashCode());
        result = prime * result + (int) (codPro ^ (codPro >>> 32));
        result = prime * result + ((codSez == null) ? 0 : codSez.hashCode());
        result = prime * result + codVrnPol;
        result = prime * result + codVrnPro;
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
        SezPrst other = (SezPrst) obj;
        if (codCompPtf != other.codCompPtf)
            return false;
        if (codOggAsct != other.codOggAsct)
            return false;
        if (codPol == null) {
            if (other.codPol != null)
                return false;
        } else if (!codPol.equals(other.codPol))
            return false;
        if (codPro != other.codPro)
            return false;
        if (codSez == null) {
            if (other.codSez != null)
                return false;
        } else if (!codSez.equals(other.codSez))
            return false;
        if (codVrnPol != other.codVrnPol)
            return false;
        if (codVrnPro != other.codVrnPro)
            return false;
        return true;
    }

}
