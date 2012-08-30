package com.kp.malice.entities.persisted;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.NaturalId;

@SuppressWarnings("serial")
@Entity
@Table(name = "RIP_DTT_TTL")
public class RipDttTtl extends RecordIdentifier implements java.io.Serializable {

    //    PRIMARY KEY
    //    (COD_COMP_ANIA, COD_CNL_VND, COD_PUN_VND, DAT_ANN_EMI, COD_PRG_TTL) -->> TTL_CTB
    @NaturalId
    @NotNull
    private int codCompPtf; // compagnia che ha emesso la polizza
    @NaturalId
    @NotNull
    private int codCnlVnd;
    @NaturalId
    @NotNull
    private String codPunVnd;
    @NaturalId
    @NotNull
    private int datAnnEmi;
    @NaturalId
    @NotNull
    private long codPrgTtl;
    //    (COD_COMP_PTF, COD_POL, COD_VRN_RIP_RSC, comp_ania0, datIniVdtCoas) -->> VRN_RIP_RSC
    @NaturalId
    @NotNull
    private int codCompPtfCoas; // eventuale compagnia di coas
    @NaturalId
    @NotNull
    private int codVrnRipRsc; // è quello referenziato da Mov
    @NaturalId
    @NotNull
    private String codPol;
    //    (COD_COMP_PTF, COD_POL, COD_VRN_POL, COD_OGG_ASCT, COD_PRTA_ASCT, COD_PRO, COD_VRN_PRO, COD_SEZ, COD_UNT) -->> GAR_PRST
    @NaturalId
    @NotNull
    private int codVrnPol;
    @NaturalId
    @NotNull
    private int codOggAsct;
    @NaturalId
    @NotNull
    private long codPro;
    @NaturalId
    @NotNull
    private int codVrnPro;
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

    private BigDecimal impNet = BigDecimal.ZERO;
    private BigDecimal impAcc = BigDecimal.ZERO;
    private BigDecimal impSsn = BigDecimal.ZERO;
    private BigDecimal impIpt = BigDecimal.ZERO;
    private BigDecimal impPvgIncNet = BigDecimal.ZERO;
    private BigDecimal impPvgIncAcc = BigDecimal.ZERO;
    private BigDecimal prctAlqIpt = BigDecimal.ZERO;

    @ManyToOne
    //    @JoinColumns({
    //            @JoinColumn(name = "codCompPtfCoas", referencedColumnName = "codCompPtfCoas", insertable = false, updatable = false),
    //            @JoinColumn(name = "codPol", referencedColumnName = "codPol", insertable = false, updatable = false) })
    @ForeignKey(name = "FK_RIP_DTT_TTL_VRN_RIP_RSC")
    private VrnRipRch vrnRipRischio;
    @ManyToOne
    //    @JoinColumns({
    //            @JoinColumn(name = "codCompPtf", referencedColumnName = "codCompPtf", insertable = false, updatable = false),
    //            @JoinColumn(name = "codPol", referencedColumnName = "codPol", insertable = false, updatable = false),
    //            @JoinColumn(name = "codVrnPol", referencedColumnName = "codVrnPol", insertable = false, updatable = false),
    //            @JoinColumn(name = "codOggAsct", referencedColumnName = "codOggAsct", insertable = false, updatable = false),
    //            @JoinColumn(name = "codPro", referencedColumnName = "codPro", insertable = false, updatable = false),
    //            @JoinColumn(name = "codVrnPro", referencedColumnName = "codVrnPro", insertable = false, updatable = false),
    //            @JoinColumn(name = "codSez", referencedColumnName = "codSez", insertable = false, updatable = false),
    //            @JoinColumn(name = "codPrtaAsct", referencedColumnName = "codPrtaAsct", insertable = false, updatable = false),
    //            @JoinColumn(name = "codUnt", referencedColumnName = "codUnt", insertable = false, updatable = false) })
    @ForeignKey(name = "FK_RIP_DTT_TTL_GAR_PRST")
    private GarPrst garPrst;
    @ManyToOne
    //    @JoinColumns({
    //            @JoinColumn(name = "codCompPtf", referencedColumnName = "codCompPtf", insertable = false, updatable = false),
    //            @JoinColumn(name = "codPol", referencedColumnName = "codPol", insertable = false, updatable = false),
    //            @JoinColumn(name = "codPrgTtl", referencedColumnName = "codPrgTtl", insertable = false, updatable = false) })
    @ForeignKey(name = "FK_RIP_DTT_TTL_TTL_CTB")
    private TtlCtb ttlCtb;

    public RipDttTtl() {
    }

    public RipDttTtl(RecordIdentifier ri, TtlCtb ttl, GarPrst gar, VrnRipRch vrnRsc) {
        super(ri);
        setVrnRipRischio(vrnRsc);
        setGarPrst(gar);
        setTtlCtb(ttl);
        setCodVrnRipRsc(vrnRsc.getCodVrnRipRsc());
        setCodPol(vrnRsc.getCodPol());
        setCodCompPtf(vrnRsc.getCodCompPtf());
        setCodCompPtfCoas(vrnRsc.getCodCompPtfCoas());
        setCodCnlVnd(ttl.getPunVnd().getCodCnlVnd());
        setCodPunVnd(ttl.getPunVnd().getCodPunVnd());
        setDatAnnEmi(ttl.getDatAnnEmi());
        setCodPrgTtl(ttl.getCodPrgTtl());
        setCodSez(gar.getCodSez());
        setCodUnt(gar.getCodUnt());
        setCodPro(gar.getCodPro());
    }

    public GarPrst getGarPrst() {
        return this.garPrst;
    }

    public void setGarPrst(GarPrst garPrst) {
        this.garPrst = garPrst;
    }

    public BigDecimal getImpAcc() {
        return this.impAcc;
    }

    public void setImpAcc(BigDecimal impAcc) {
        this.impAcc = impAcc;
    }

    public BigDecimal getImpNet() {
        return this.impNet;
    }

    public void setImpNet(BigDecimal impNet) {
        this.impNet = impNet;
    }

    public BigDecimal getImpSsn() {
        return this.impSsn;
    }

    public void setImpSsn(BigDecimal impSsn) {
        this.impSsn = impSsn;
    }

    public BigDecimal getImpIpt() {
        return this.impIpt;
    }

    public void setImpIpt(BigDecimal impIpt) {
        this.impIpt = impIpt;
    }

    public BigDecimal getImpPvgIncNet() {
        return this.impPvgIncNet;
    }

    public void setImpPvgIncNet(BigDecimal impPvgIncNet) {
        this.impPvgIncNet = impPvgIncNet;
    }

    public BigDecimal getImpPvgIncAcc() {
        return this.impPvgIncAcc;
    }

    public void setImpPvgIncAcc(BigDecimal impPvgIncAcc) {
        this.impPvgIncAcc = impPvgIncAcc;
    }

    public BigDecimal getPrctAlqIpt() {
        return this.prctAlqIpt;
    }

    public void setPrctAlqIpt(BigDecimal prctAlqIpt) {
        this.prctAlqIpt = prctAlqIpt;
    }

    public VrnRipRch getVrnRipRischio() {
        return vrnRipRischio;
    }

    public void setVrnRipRischio(VrnRipRch vrnRipRischio) {
        this.vrnRipRischio = vrnRipRischio;
    }

    public TtlCtb getTtlCtb() {
        return ttlCtb;
    }

    public void setTtlCtb(TtlCtb ttlCtb) {
        this.ttlCtb = ttlCtb;
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

    public int getDatAnnEmi() {
        return datAnnEmi;
    }

    public void setDatAnnEmi(int datAnnEmi) {
        this.datAnnEmi = datAnnEmi;
    }

    public long getCodPrgTtl() {
        return codPrgTtl;
    }

    public void setCodPrgTtl(long codPrgTtl) {
        this.codPrgTtl = codPrgTtl;
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

    public int getCodCompPtfCoas() {
        return codCompPtfCoas;
    }

    public void setCodCompPtfCoas(int codCompPtfCoas) {
        this.codCompPtfCoas = codCompPtfCoas;
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
        result = prime * result + codCnlVnd;
        result = prime * result + codCompPtf;
        result = prime * result + codOggAsct;
        result = prime * result + ((codPol == null) ? 0 : codPol.hashCode());
        result = prime * result + (int) (codPrgTtl ^ (codPrgTtl >>> 32));
        result = prime * result + (int) (codPro ^ (codPro >>> 32));
        result = prime * result + codPrtaAsct;
        result = prime * result + ((codPunVnd == null) ? 0 : codPunVnd.hashCode());
        result = prime * result + ((codSez == null) ? 0 : codSez.hashCode());
        result = prime * result + ((codUnt == null) ? 0 : codUnt.hashCode());
        result = prime * result + codVrnPol;
        result = prime * result + codVrnPro;
        result = prime * result + codVrnRipRsc;
        result = prime * result + datAnnEmi;
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
        RipDttTtl other = (RipDttTtl) obj;
        if (codCnlVnd != other.codCnlVnd)
            return false;
        if (codCompPtf != other.codCompPtf)
            return false;
        if (codOggAsct != other.codOggAsct)
            return false;
        if (codPol == null) {
            if (other.codPol != null)
                return false;
        } else if (!codPol.equals(other.codPol))
            return false;
        if (codPrgTtl != other.codPrgTtl)
            return false;
        if (codPro != other.codPro)
            return false;
        if (codPrtaAsct != other.codPrtaAsct)
            return false;
        if (codPunVnd == null) {
            if (other.codPunVnd != null)
                return false;
        } else if (!codPunVnd.equals(other.codPunVnd))
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
        if (codVrnRipRsc != other.codVrnRipRsc)
            return false;
        if (datAnnEmi != other.datAnnEmi)
            return false;
        return true;
    }

}
