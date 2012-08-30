package com.kp.malice.entities.persisted;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.NamedNativeQueries;
import org.hibernate.annotations.NamedNativeQuery;
import org.hibernate.annotations.NaturalId;

import com.kp.malice.entities.business.TitoloLloyds.StatoTitolo;

@SuppressWarnings("serial")
@Entity
@Table(name = "TTL_CTB")
@NamedNativeQueries({
        @NamedNativeQuery(name = "ttlctb.inPeriodoCopertura", query = "select * from TTL_CTB ttl where ttl.punVnd_ID in (:pvId) "
                + "and ttl.datIniCpr between date_format(:startPeriod, '%Y%m%d') and date_format(:endPeriod, '%Y%m%d')", resultClass = TtlCtb.class),
        @NamedNativeQuery(name = "ttlctb.trovaTitoliPerChiusura", query = "select t.* from TTL_CTB t, ESTRATTI_CONTO ec, ESTR_CNT_TTL ect "
                + "where ec.CHR_ID = :chrId and ect.ESTR_ID = ec.ID and t.ID = ect.TTL_ID", resultClass = TtlCtb.class) })
public class TtlCtb extends RecordIdentifier {

    //    PRIMARY KEY
    //    (COD_COMP_ANIA, COD_CNL_VND, COD_PUN_VND, DAT_ANN_EMI, COD_PRG_TTL));
    @NaturalId
    @NotNull
    private int codCompPtf; // (ex codCompAnia) FK sulla tabell comp_ptf
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
    private long codPrgTtl; // il numero di scontrini emessi dal punto vendita, progressivo rispetto alle chiavi precedenti codCompPtf codCnlVnd codPunVnd datAnnEmi
    @NaturalId
    @NotNull
    private long codPrgMov;
    @NaturalId
    @NotNull
    private String codPol;

    private Date datIniCpr; // data inizio copertura: controllare in installment details
    private Date datScaCpr; // data scadenza copertura
    private Date datCtbEmis;
    private Date datCtb;
    private Date datInc; // la data comunicata a LIO
    private Date datAnn; // la data in cui il titolo viene annullato
    @Size(max = 1024)
    private String notaDiAnnullo; // la nota di annullamento
    @Enumerated(EnumType.STRING)
    private StatoTitolo statoTitolo; // da incassare, incassato, consolidato, resocontato  
    private String codCig; // codice CIG: obbligatorio se l'incasso è delegato a LIO
    private String codSubAge; // codice sub agebte di 3 byte
    private BigDecimal impTotNet = BigDecimal.ZERO; // totale netto
    private BigDecimal impTotAcc = BigDecimal.ZERO; // tot accessori
    private BigDecimal impTotIpt = BigDecimal.ZERO; // totale imposte impTotIpt
    // Il lordo che paga il cliente è: impTotNet + impTotAcc + impTotIpt 
    private BigDecimal impSerSntNzle = BigDecimal.ZERO; // è un di cui delle imposte
    private BigDecimal impPvgIncNet = BigDecimal.ZERO; // totale provvigioni su netto
    private BigDecimal impPvgIncAcc = BigDecimal.ZERO; // totale provvigioni su accessori
    private BigDecimal prctPvgIncNet = BigDecimal.ZERO;// quella della garanzia
    private BigDecimal prctPvgIncAcc = BigDecimal.ZERO; // quella della garanzia

    @ManyToOne(fetch = FetchType.LAZY)
    @ForeignKey(name = "FK_PUN_VND_TTL_CTB")
    private PunVnd punVnd;
    @ManyToOne(fetch = FetchType.EAGER)
    @ForeignKey(name = "FK_MOV_TTL_CTB")
    private Mov mov;
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "codCompPtf", referencedColumnName = "codCompPtf", insertable = true, updatable = true, nullable = true),
            @JoinColumn(name = "codPol", referencedColumnName = "codPol", insertable = true, updatable = true, nullable = true),
            @JoinColumn(name = "codPrgTtl", referencedColumnName = "codPrgTtl", insertable = true, updatable = true, nullable = true) })
    private Set<RipDttTtl> ripartoTitoloCoass = new HashSet<RipDttTtl>(0);

    @OneToMany(mappedBy = "titolo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("recordId desc")
    private Set<DelegatoIncassoTitolo> delegatiIncasso;

    public BigDecimal getTotCommissioni() {
        return getImpPvgIncAcc().add(getImpPvgIncNet());
    }

    public BigDecimal getPremioTotale() {
        return getImpTotAcc().add(getImpTotNet().add(getImpTotIpt()));
    }

    public BigDecimal getRimessa() {
        return getPremioTotale().subtract(getTotCommissioni());
    }

    public ArrayList<Incs> getIncassi() {
        ArrayList<Incs> incassi = new ArrayList<Incs>();
        if (null != delegatiIncasso)
            for (DelegatoIncassoTitolo delegato : delegatiIncasso) {
                incassi.add(delegato.getIncasso());
            }
        return incassi;
    }

    public TtlCtb() {
    }

    public TtlCtb(RecordIdentifier ri, PunVnd punVen, Mov mov, int datAnnEmi, long codPrgTtl) {
        super(ri);
        setMov(mov);
        setPunVnd(punVen);
        setDatAnnEmi(datAnnEmi);
        setCodPrgTtl(codPrgTtl);
        setCodPrgMov(mov.getCodPrgMov());
        setCodPol(mov.getCodPol());
        setCodPunVnd(punVen.getCodPunVnd());
        setCodCompPtf(punVen.getCodCompPtf());
        setCodCnlVnd(punVen.getCodCnlVnd());
    }

    public PunVnd getPunVnd() {
        return this.punVnd;
    }

    public void setPunVnd(PunVnd punVnd) {
        this.punVnd = punVnd;
    }

    public Mov getMov() {
        return this.mov;
    }

    public void setMov(Mov mov) {
        this.mov = mov;
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

    public long getCodPrgMov() {
        return codPrgMov;
    }

    public void setCodPrgMov(long codPrgMov) {
        this.codPrgMov = codPrgMov;
    }

    public String getCodPol() {
        return codPol;
    }

    public void setCodPol(String codPol) {
        this.codPol = codPol;
    }

    public Date getDatIniCpr() {
        return datIniCpr;
    }

    public void setDatIniCpr(Date datIniCpr) {
        this.datIniCpr = datIniCpr;
    }

    public Date getDatScaCpr() {
        return datScaCpr;
    }

    public void setDatScaCpr(Date datScaCpr) {
        this.datScaCpr = datScaCpr;
    }

    public Date getDatCtbEmis() {
        return datCtbEmis;
    }

    public void setDatCtbEmis(Date datCtbEmis) {
        this.datCtbEmis = datCtbEmis;
    }

    public Date getDatCtb() {
        return datCtb;
    }

    public void setDatCtb(Date datCtb) {
        this.datCtb = datCtb;
    }

    public Date getDatInc() {
        return datInc;
    }

    public void setDatInc(Date datInc) {
        this.datInc = datInc;
    }

    public String getCodCig() {
        return codCig;
    }

    public void setCodCig(String codCig) {
        this.codCig = codCig;
    }

    public String getCodSubAge() {
        return codSubAge;
    }

    public void setCodSubAge(String codSubAge) {
        this.codSubAge = codSubAge;
    }

    public BigDecimal getImpTotNet() {
        return impTotNet;
    }

    public void setImpTotNet(BigDecimal impTotNet) {
        this.impTotNet = impTotNet;
    }

    public BigDecimal getImpTotAcc() {
        return impTotAcc;
    }

    public void setImpTotAcc(BigDecimal impTotAcc) {
        this.impTotAcc = impTotAcc;
    }

    public BigDecimal getImpTotIpt() {
        return impTotIpt;
    }

    public void setImpTotIpt(BigDecimal impTotIpt) {
        this.impTotIpt = impTotIpt;
    }

    public BigDecimal getImpSerSntNzle() {
        return impSerSntNzle;
    }

    public void setImpSerSntNzle(BigDecimal impSerSntNzle) {
        this.impSerSntNzle = impSerSntNzle;
    }

    public BigDecimal getImpPvgIncNet() {
        return impPvgIncNet;
    }

    public void setImpPvgIncNet(BigDecimal impPvgIncNet) {
        this.impPvgIncNet = impPvgIncNet;
    }

    public BigDecimal getImpPvgIncAcc() {
        return impPvgIncAcc;
    }

    public void setImpPvgIncAcc(BigDecimal impPvgIncAcc) {
        this.impPvgIncAcc = impPvgIncAcc;
    }

    public Set<RipDttTtl> getRipartoTitoloCoass() {
        return ripartoTitoloCoass;
    }

    public void setRipartoTitoloCoass(Set<RipDttTtl> ripartoTitoloCoass) {
        this.ripartoTitoloCoass = ripartoTitoloCoass;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + codCnlVnd;
        result = prime * result + codCompPtf;
        result = prime * result + (int) (codPrgTtl ^ (codPrgTtl >>> 32));
        result = prime * result + ((codPunVnd == null) ? 0 : codPunVnd.hashCode());
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
        TtlCtb other = (TtlCtb) obj;
        if (codCnlVnd != other.codCnlVnd)
            return false;
        if (codCompPtf != other.codCompPtf)
            return false;
        if (codPrgTtl != other.codPrgTtl)
            return false;
        if (codPunVnd == null) {
            if (other.codPunVnd != null)
                return false;
        } else if (!codPunVnd.equals(other.codPunVnd))
            return false;
        if (datAnnEmi != other.datAnnEmi)
            return false;
        return true;
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

    public StatoTitolo getStatoTitolo() {
        return statoTitolo;
    }

    public void setStatoTitolo(StatoTitolo statoTitolo) {
        this.statoTitolo = statoTitolo;
    }

    public Set<DelegatoIncassoTitolo> getDelegatiIncasso() {
        return delegatiIncasso;
    }

    public void setDelegatiIncasso(Set<DelegatoIncassoTitolo> delegatiIncasso) {
        this.delegatiIncasso = delegatiIncasso;
    }

    public Date getDatAnn() {
        return datAnn;
    }

    public void setDatAnn(Date datAnn) {
        this.datAnn = datAnn;
    }

    public String getNotaDiAnnullo() {
        return notaDiAnnullo;
    }

    public void setNotaDiAnnullo(String notaDiAnnullo) {
        this.notaDiAnnullo = notaDiAnnullo;
    }

}
