package com.kp.malice.entities.persisted;

import java.math.BigDecimal;
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
@Table(name = "GAR_PRST", uniqueConstraints = @UniqueConstraint(columnNames = { "codVrnPol", "codOggAsct",
        "codCompPtf", "codPol", "codPro", "codVrnPro", "codSez", "codPrtaAsct", "codUnt" }))
public class GarPrst extends RecordIdentifier {

    // PRIMARY KEY
    //    (COD_COMP_ANIA, COD_POL, COD_VRN_POL, COD_OGG_ASCT, COD_PRTA_ASCT, COD_PRO, COD_VRN_PRO, COD_SEZ, COD_UNT));
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
    @NaturalId
    @NotNull
    private int codPrtaAsct = 1; //1
    // 10.002
    @NaturalId
    @NotNull
    private String codUnt; //002

    private String codEstnGar; // codice rischio lloyd's -->> ci andrà il codice IASSICUR
    private BigDecimal impPreAnnNet; // importo premio annuo netto -->> da risk premium details:premium
    private BigDecimal impPreAnnAcc; // importo premio annuo accessori da risk premium details:accessori
    private BigDecimal prctAlqIpt; // percentuale -->> recuperare dalla anagrafica dei rischi/garanzie fornita da Toniutti
    private BigDecimal impPreAnnIpt;
    private BigDecimal impPreSsn;
    private BigDecimal prctPvgIncNet; // percentuale provvigioni di incasso netto del risk premium details: 10%
    private BigDecimal prctPvgIncAcc; // percentuale provvigioni di incasso degli accessori del risk premium details: 100%

    // FOREIGN KEYS
    //    REFERENCES AD2K.SEZ_PRST (COD_COMP_ANIA,COD_POL,COD_VRN_POL,COD_OGG_ASCT,COD_PRO,COD_VRN_PRO,COD_SEZ),
    @ManyToOne(fetch = FetchType.LAZY)
    //    @JoinColumns({
    //            @JoinColumn(name = "codCompPtf", referencedColumnName = "codCompPtf", insertable = false, updatable = false),
    //            @JoinColumn(name = "codPol", referencedColumnName = "codPol", insertable = false, updatable = false),
    //            @JoinColumn(name = "codVrnPol", referencedColumnName = "codVrnPol", insertable = false, updatable = false),
    //            @JoinColumn(name = "codOggAsct", referencedColumnName = "codOggAsct", insertable = false, updatable = false),
    //            @JoinColumn(name = "codPro", referencedColumnName = "codPro", insertable = false, updatable = false),
    //            @JoinColumn(name = "codVrnPro", referencedColumnName = "codVrnPro", insertable = false, updatable = false),
    //            @JoinColumn(name = "codSez", referencedColumnName = "codSez", insertable = false, updatable = false) })
    @ForeignKey(name = "FK_GAR_PRST_SEZ_PRST")
    private SezPrst sezPrst;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "codCompPtf", referencedColumnName = "codCompPtf", insertable = false, updatable = false),
            @JoinColumn(name = "codPol", referencedColumnName = "codPol", insertable = false, updatable = false),
            @JoinColumn(name = "codVrnPol", referencedColumnName = "codVrnPol", insertable = false, updatable = false),
            @JoinColumn(name = "codOggAsct", referencedColumnName = "codOggAsct", insertable = false, updatable = false),
            @JoinColumn(name = "codPro", referencedColumnName = "codPro", insertable = false, updatable = false),
            @JoinColumn(name = "codVrnPro", referencedColumnName = "codVrnPro", insertable = false, updatable = false),
            @JoinColumn(name = "codSez", referencedColumnName = "codSez", insertable = false, updatable = false),
            @JoinColumn(name = "codPrtaAsct", referencedColumnName = "codPrtaAsct", insertable = false, updatable = false),
            @JoinColumn(name = "codUnt", referencedColumnName = "codUnt", insertable = false, updatable = false) })
    private Set<RipDttTtl> dttTtlsCoass = new HashSet<RipDttTtl>(0);

    public GarPrst() {

    }

    public GarPrst(RecordIdentifier ri, SezPrst sez, int codPrtaAsct, String codUnt) {
        super(ri);
        setCodCompPtf(sez.getCodCompPtf());
        setCodPol(sez.getCodPol());
        setCodVrnPol(sez.getCodVrnPol());
        setCodOggAsct(sez.getCodOggAsct());
        setCodPro(sez.getCodPro());
        setCodVrnPro(sez.getCodVrnPro());
        setCodSez(sez.getCodSez());
        setCodPrtaAsct(codPrtaAsct);
        setCodUnt(codUnt);
        setSezPrst(sez);
    }

    public SezPrst getSezPrst() {
        return this.sezPrst;
    }

    public void setSezPrst(SezPrst sezPrst) {
        this.sezPrst = sezPrst;
    }

    public String getCodEstnGar() {
        return this.codEstnGar;
    }

    public void setCodEstnGar(String codEstnGar) {
        this.codEstnGar = codEstnGar;
    }

    public Set<RipDttTtl> getDttTtlsCoass() {
        return this.dttTtlsCoass;
    }

    public void setDttTtlsCoass(Set<RipDttTtl> dttTtls) {
        this.dttTtlsCoass = dttTtls;
    }

    public BigDecimal getImpPreAnnNet() {
        return impPreAnnNet;
    }

    public void setImpPreAnnNet(BigDecimal impPreAnnNet) {
        this.impPreAnnNet = impPreAnnNet;
    }

    public BigDecimal getImpPreAnnAcc() {
        return impPreAnnAcc;
    }

    public void setImpPreAnnAcc(BigDecimal impPreAnnAcc) {
        this.impPreAnnAcc = impPreAnnAcc;
    }

    public BigDecimal getPrctAlqIpt() {
        return prctAlqIpt;
    }

    public void setPrctAlqIpt(BigDecimal prctAlqIpt) {
        this.prctAlqIpt = prctAlqIpt;
    }

    public BigDecimal getImpPreAnnIpt() {
        return impPreAnnIpt;
    }

    public void setImpPreAnnIpt(BigDecimal impPreAnnIpt) {
        this.impPreAnnIpt = impPreAnnIpt;
    }

    public BigDecimal getImpPreSsn() {
        return impPreSsn;
    }

    public void setImpPreSsn(BigDecimal impPreSsn) {
        this.impPreSsn = impPreSsn;
    }

    public BigDecimal getPrctPvgIncNet() {
        return prctPvgIncNet;
    }

    public void setPrctPvgIncNet(BigDecimal prctPvgIncNet) {
        this.prctPvgIncNet = prctPvgIncNet;
    }

    public BigDecimal getPrctPvgIncAcc() {
        return prctPvgIncAcc;
    }

    public void setPrctPvgIncAcc(BigDecimal prctPvgIncAcc) {
        this.prctPvgIncAcc = prctPvgIncAcc;
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

    public int getCodPrtaAsct() {
        return codPrtaAsct;
    }

    public void setCodPrtaAsct(int codPrtaAsct) {
        this.codPrtaAsct = codPrtaAsct;
    }

    public String getCodSez() {
        return codSez;
    }

    public void setCodSez(String codSez) {
        this.codSez = codSez;
    }

    public String getCodUnt() {
        return codUnt;
    }

    public void setCodUnt(String codUnt) {
        this.codUnt = codUnt;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + codCompPtf;
        result = prime * result + codOggAsct;
        result = prime * result + ((codPol == null) ? 0 : codPol.hashCode());
        result = prime * result + (int) (codPro ^ (codPro >>> 32));
        result = prime * result + codPrtaAsct;
        result = prime * result + ((codSez == null) ? 0 : codSez.hashCode());
        result = prime * result + ((codUnt == null) ? 0 : codUnt.hashCode());
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
        GarPrst other = (GarPrst) obj;
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
        if (codPrtaAsct != other.codPrtaAsct)
            return false;
        if (codSez == null) {
            if (other.codSez != null)
                return false;
        } else if (!codSez.equals(other.codSez))
            return false;
        if (codUnt == null) {
            if (other.codUnt != null)
                return false;
        } else if (!codUnt.equals(other.codUnt))
            return false;
        if (codVrnPol != other.codVrnPol)
            return false;
        if (codVrnPro != other.codVrnPro)
            return false;
        return true;
    }

}
