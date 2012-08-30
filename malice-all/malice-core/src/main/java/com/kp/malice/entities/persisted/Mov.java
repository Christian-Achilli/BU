package com.kp.malice.entities.persisted;

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
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.NaturalId;

@SuppressWarnings("serial")
@Entity
@Table(name = "MOV", uniqueConstraints = @UniqueConstraint(columnNames = { "codPrgMov", "codCompPtf", "codPol" }))
public class Mov extends RecordIdentifier {

    //    PRIMARY KEY
    //    (COD_COMP_ANIA, COD_POL, COD_PRG_MOV));
    @NaturalId
    private int codCompPtf;
    @NaturalId
    private String codPol;
    @NaturalId
    private long codPrgMov; // progressivo a parità di certificato
    @NotNull
    private int codCompPtfPunVnd;
    @NotNull
    private int codCnlVnd;
    @NotNull
    private String codPunVnd;

    @Enumerated(EnumType.STRING)
    private TipoMovimento codTipMov; // 1 emissione nuovo, 2 emissione rinnovo, 3 quietanza, 4 appendice generica (addendum Lloyd's tipo 2 del campo ICType), 5 appendice rinnovo 
    @Enumerated(EnumType.STRING)
    private TipoDocumentoAssicurativo codDocAssv;// PO: polizza, QT: quietanza, AP: appendice
    private int codVrnPol;
    private int codVrnRipRsc;
    private Date datEfftMov; // data decorrenza polizza, titolo, validità appendice a seconda del tipo di movimento -->> INCEPTION DATE
    private Date datEmiDoc; // Last_Updated_Date
    @Enumerated(EnumType.STRING)
    private StatoMovimento statoMovimento; // ATTESA_PDF, LAVORABILE
    @Enumerated(EnumType.STRING)
    @NotNull
    private Source sorgente;
    private String txtMov; // per poter aggiungere dei commenti da UI

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "codCompPtf", referencedColumnName = "codCompPtf", insertable = false, updatable = false),
            @JoinColumn(name = "codPol", referencedColumnName = "codPol", insertable = false, updatable = false),
            @JoinColumn(name = "codPrgMov", referencedColumnName = "codPrgMov", insertable = false, updatable = false) })
    private Set<TtlCtb> ttlCtbs = new HashSet<TtlCtb>(0);

    //  REFERENCES AD2K.POL (COD_COMP_ANIA,COD_POL),
    @ManyToOne
    //    @JoinColumns({
    //            @JoinColumn(name = "codCompPtf", referencedColumnName = "codCompPtf", insertable = false, updatable = false),
    //            @JoinColumn(name = "codPol", referencedColumnName = "codPol", insertable = false, updatable = false) })
    @ForeignKey(name = "FK_MOV_POL")
    private Pol pol;

    //  REFERENCES AD2K.VRN_POL (COD_COMP_ANIA,COD_POL,COD_VRN_POL)
    @ManyToOne
    //    @JoinColumns({
    //            @JoinColumn(name = "codCompPtf", referencedColumnName = "codCompPtf", insertable = false, updatable = false),
    //            @JoinColumn(name = "codPol", referencedColumnName = "codPol", insertable = false, updatable = false) })
    @ForeignKey(name = "FK_MOV_VRN_POL")
    private VrnPol vrnPol;

    @ManyToOne
    //    @JoinColumns({
    //            @JoinColumn(name = "codCompPtf", referencedColumnName = "codCompPtf", insertable = false, updatable = false),
    //            @JoinColumn(name = "codPol", referencedColumnName = "codPol", insertable = false, updatable = false) })
    @ForeignKey(name = "FK_MOV_VRN_RSC")
    private VrnRipRch vrnRipRch;

    @ManyToOne(fetch = FetchType.LAZY)
    //    @JoinColumns({
    //            @JoinColumn(name = "codCompPtfPunVnd", referencedColumnName = "codCompPtf", insertable = false, updatable = false),
    //            @JoinColumn(name = "codCnlVnd", referencedColumnName = "codCnlVnd", insertable = false, updatable = false),
    //            @JoinColumn(name = "codPunVnd", referencedColumnName = "codPunVnd", insertable = false, updatable = false) })
    @ForeignKey(name = "FK_POL_PUN_VND")
    private PunVnd punVnd;

    public enum StatoMovimento {
        ATTESA_PDF, LAVORABILE, CONSOLIDATO;

        public static StatoMovimento fromString(String s) {
            for (StatoMovimento enu : values()) {
                if (enu.name().equals(s))
                    return enu;
            }
            return null;
        }
    }

    public enum TipoMovimento {
        EMISSIONE_NUOVO, EMISSIONE_RINNOVO, QUIETANZA, APPENDICE_GENERICA, APPENDICE_RINNOVO;
        // 1 emissione nuovo, 2 emissione rinnovo, 3 quietanza, 4 appendice generica (addendum Lloyd's tipo 2 del campo ICType), 5 appendice rinnovo 
    }

    public Mov() {
    }

    public Mov(RecordIdentifier ri, Pol pol, VrnPol vrnPol, VrnRipRch vrnRsc, long codPrgMov) {
        super(ri);
        setPol(pol);
        setCodPol(pol.getCodPol());
        setCodCompPtf(pol.getCodCompPtf());
        setCodPrgMov(codPrgMov);
        setVrnRipRch(vrnRsc);
        setVrnPol(vrnPol);
        setPunVnd(pol.getPunVnd());
        setCodCompPtfPunVnd(pol.getCodCompPtf());
        setCodCnlVnd(pol.getCodCnlVnd());
        setCodPunVnd(pol.getPunVnd().getCodPunVnd());
    }

    public enum Source {
        XML, FORM;
        public static Source fromString(String s) {
            for (Source enu : values()) {
                if (enu.name().equals(s))
                    return enu;
            }
            return null;
        }
    }

    public Pol getPol() {
        return this.pol;
    }

    public void setPol(Pol pol) {
        this.pol = pol;
    }

    public Date getDatEfftMov() {
        return this.datEfftMov;
    }

    public void setDatEfftMov(Date datEfftMov) {
        this.datEfftMov = datEfftMov;
    }

    public Date getDatEmiDoc() {
        return this.datEmiDoc;
    }

    public void setDatEmiDoc(Date datEmiDoc) {
        this.datEmiDoc = datEmiDoc;
    }

    public Set<TtlCtb> getTtlCtbs() {
        return ttlCtbs;
    }

    public void setTtlCtbs(Set<TtlCtb> ttlCtbs) {
        this.ttlCtbs = ttlCtbs;
    }

    public VrnRipRch getVrnRipRch() {
        return vrnRipRch;
    }

    public void setVrnRipRch(VrnRipRch vrnRipRch) {
        this.vrnRipRch = vrnRipRch;
    }

    public PunVnd getPunVnd() {
        return punVnd;
    }

    public void setPunVnd(PunVnd punVnd) {
        this.punVnd = punVnd;
    }

    public VrnPol getVrnPol() {
        return vrnPol;
    }

    public void setVrnPol(VrnPol vrnPol) {
        this.vrnPol = vrnPol;
    }

    public int getCodVrnPol() {
        return codVrnPol;
    }

    public void setCodVrnPol(int codVrnPol) {
        this.codVrnPol = codVrnPol;
    }

    public int getCodVrnRipRsc() {
        return codVrnRipRsc;
    }

    public void setCodVrnRipRsc(int codVrnRipRsc) {
        this.codVrnRipRsc = codVrnRipRsc;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + codCompPtf;
        result = prime * result + ((codPol == null) ? 0 : codPol.hashCode());
        result = prime * result + (int) (codPrgMov ^ (codPrgMov >>> 32));
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
        Mov other = (Mov) obj;
        if (codCompPtf != other.codCompPtf)
            return false;
        if (codPol == null) {
            if (other.codPol != null)
                return false;
        } else if (!codPol.equals(other.codPol))
            return false;
        if (codPrgMov != other.codPrgMov)
            return false;
        return true;
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

    public long getCodPrgMov() {
        return codPrgMov;
    }

    public void setCodPrgMov(long codPrgMov) {
        this.codPrgMov = codPrgMov;
    }

    public String getTxtMov() {
        return txtMov;
    }

    public void setTxtMov(String txtMov) {
        this.txtMov = txtMov;
    }

    public TipoMovimento getCodTipMov() {
        return codTipMov;
    }

    public void setCodTipMov(TipoMovimento codTipMov) {
        this.codTipMov = codTipMov;
    }

    public TipoDocumentoAssicurativo getCodDocAssv() {
        return codDocAssv;
    }

    public void setCodDocAssv(TipoDocumentoAssicurativo codDocAssv) {
        this.codDocAssv = codDocAssv;
    }

    public StatoMovimento getStatoMovimento() {
        return statoMovimento;
    }

    public void setStatoMovimento(StatoMovimento statoMovimento) {
        this.statoMovimento = statoMovimento;
    }

    public Source getSorgente() {
        return sorgente;
    }

    public void setSorgente(Source sorgente) {
        this.sorgente = sorgente;
    }

    public int getCodCompPtfPunVnd() {
        return codCompPtfPunVnd;
    }

    public void setCodCompPtfPunVnd(int codCompPtfPunVnd) {
        this.codCompPtfPunVnd = codCompPtfPunVnd;
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

}
