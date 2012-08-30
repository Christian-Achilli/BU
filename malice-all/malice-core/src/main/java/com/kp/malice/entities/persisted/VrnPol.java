package com.kp.malice.entities.persisted;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
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
import org.hibernate.annotations.Type;

import com.kp.malice.entities.persisted.Pol.StatoPolizza;

@SuppressWarnings("serial")
@Entity
@Table(name = "VRN_POL", uniqueConstraints = @UniqueConstraint(columnNames = { "codVrnPol", "codCompPtf", "codPol" }))
public class VrnPol extends RecordIdentifier implements java.io.Serializable {

    @NaturalId
    @NotNull
    private int codCompPtf;
    @NaturalId
    @NotNull
    private String codPol;
    @NaturalId
    @NotNull
    private int codVrnPol;
    @ManyToOne
    @ForeignKey(name = "FK_VRN_POL_POL")
    private Pol pol;
    @ManyToOne
    @ForeignKey(name = "FK_VRN_POL_PUN_VND")
    @NotNull
    private PunVnd punVnd;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "codCompPtf", referencedColumnName = "codCompPtf", insertable = false, updatable = false),
            @JoinColumn(name = "codPol", referencedColumnName = "codPol", insertable = false, updatable = false),
            @JoinColumn(name = "codVrnPol", referencedColumnName = "codVrnPol", insertable = false, updatable = false) })
    private Set<OggAsct> oggAscts = new HashSet<OggAsct>(0);
    private long codPrgMov; // si riferisce al movimento che ha provocato l'ultimo aggiornameto di vrnPol
    private Date datEfftPol; // data effetto polizza: inception date nel caso di emissione polizza
    private Date datScaOrgnPol; // data scadenza polizza: expiry date nel caso di emissione polizza. rimane fisso. Quello che cambia nel caso di prolungamento di questa polizza è la datScaUltRin
    private Date datScaUltRin; // parte a null. se c'è una appendice di rinnovo (che è diverso da rinnovo con nuovo certificato) è uguale alla scadenza della appendice di rinnovo
    private Date datEfftUltRin; // data effetto ultimo rinnovo: l'effetto del movimento che ha fatto il rinnovo tramite appendice (prolungamento polizza attuale)
    private Date datPrssQtzt; // installment_date della seconda riga di installment details. Nei movimenti di quietanzamento successivi la stessa riga di installment details aggiorna questo valore 
    @Enumerated
    private StatoPolizza codStaPol; // stato della polizza: in vigore, sospesa, annullata. parte "in vigore"
    //BINDING AUTHORITY
    private String registrationNumber;
    private Date registrationDate;
    private String brokerRef;
    // fine BA
    private int codFrz; // numero di rate: numero di installments
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean idcTaciRin; // indicatore tacito rinnovo
    //    private BigDecimal impTotIpbAnn; // totale imponibile polizza (il periodo potrebbe essere diverso da un anno): premium + accessori presi da Total Premium Details
    private BigDecimal impTotNetAnn = BigDecimal.ZERO;
    private BigDecimal impTotAccAnn = BigDecimal.ZERO;
    private BigDecimal impTotIpt = BigDecimal.ZERO; // importo totale imposte
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "codCompPtf", referencedColumnName = "codCompPtf", insertable = false, updatable = false),
            @JoinColumn(name = "codPol", referencedColumnName = "codPol", insertable = false, updatable = false) })
    private Set<Mov> movs = new HashSet<Mov>(0);
    @OneToMany(mappedBy = "vrnPol", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<RapCntn> rapportiContraenza = new HashSet<RapCntn>(0);
    private String codAppendice;

    public VrnPol() {
    }

    public VrnPol(RecordIdentifier ri, Pol pol, int codVrnPol) {
        super(ri);
        setPol(pol);
        setCodVrnPol(codVrnPol);
        setCodPol(pol.getCodPol());
        setCodCompPtf(pol.getCompPtf().getCodCompPtf());
    }

    public Pol getPol() {
        return this.pol;
    }

    public void setPol(Pol pol) {
        this.pol = pol;
    }

    public int getCodFrz() {
        return this.codFrz;
    }

    public void setCodFrz(int codFrz) {
        this.codFrz = codFrz;
    }

    public Set<OggAsct> getOggAscts() {
        return this.oggAscts;
    }

    public void setOggAscts(Set<OggAsct> oggAscts) {
        this.oggAscts = oggAscts;
    }

    public Set<Mov> getMovs() {
        return movs;
    }

    public void setMovs(Set<Mov> movs) {
        this.movs = movs;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getBrokerRef() {
        return brokerRef;
    }

    public void setBrokerRef(String brokerRef) {
        this.brokerRef = brokerRef;
    }

    public int getCodVrnPol() {
        return codVrnPol;
    }

    public void setCodVrnPol(int codVrnPol) {
        this.codVrnPol = codVrnPol;
    }

    public long getCodPrgMov() {
        return codPrgMov;
    }

    public void setCodPrgMov(long codPrgMov) {
        this.codPrgMov = codPrgMov;
    }

    public Date getDatEfftPol() {
        return datEfftPol;
    }

    public void setDatEfftPol(Date dtoEfftPol) {
        this.datEfftPol = dtoEfftPol;
    }

    public Date getDatScaOrgnPol() {
        return datScaOrgnPol;
    }

    public void setDatScaOrgnPol(Date datScaOrgnPol) {
        this.datScaOrgnPol = datScaOrgnPol;
    }

    public Date getDatScaUltRin() {
        return datScaUltRin;
    }

    public void setDatScaUltRin(Date datScaUltRin) {
        this.datScaUltRin = datScaUltRin;
    }

    public Date getDatEfftUltRin() {
        return datEfftUltRin;
    }

    public void setDatEfftUltRin(Date datEfftUltRin) {
        this.datEfftUltRin = datEfftUltRin;
    }

    public Date getDatPrssQtzt() {
        return datPrssQtzt;
    }

    public void setDatPrssQtzt(Date datPrssQtzt) {
        this.datPrssQtzt = datPrssQtzt;
    }

    public StatoPolizza getCodStaPol() {
        return codStaPol;
    }

    public void setCodStaPol(StatoPolizza codStaPol) {
        this.codStaPol = codStaPol;
    }

    public boolean isIdcTaciRin() {
        return idcTaciRin;
    }

    public void setIdcTaciRin(boolean idcTaciRin) {
        this.idcTaciRin = idcTaciRin;
    }

    public BigDecimal getImpTotIpt() {
        return impTotIpt;
    }

    public void setImpTotIpt(BigDecimal impTotIpt) {
        this.impTotIpt = impTotIpt;
    }

    public PunVnd getPunVnd() {
        return punVnd;
    }

    public void setPunVnd(PunVnd punVnd) {
        this.punVnd = punVnd;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + codCompPtf;
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
        VrnPol other = (VrnPol) obj;
        if (codCompPtf != other.codCompPtf)
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

    public Set<RapCntn> getRapportiContraenza() {
        return rapportiContraenza;
    }

    public void setRapportiContraenza(Set<RapCntn> rapportiContraenza) {
        this.rapportiContraenza = rapportiContraenza;
    }

    public BigDecimal getImpTotNetAnn() {
        return impTotNetAnn;
    }

    public void setImpTotNetAnn(BigDecimal impTotNetAnn) {
        this.impTotNetAnn = impTotNetAnn;
    }

    public BigDecimal getImpTotAccAnn() {
        return impTotAccAnn;
    }

    public void setImpTotAccAnn(BigDecimal impTotAccAnn) {
        this.impTotAccAnn = impTotAccAnn;
    }

    public String getCodAppendice() {
        return codAppendice;
    }

    public void setCodAppendice(String codAppendice) {
        this.codAppendice = codAppendice;
    }

}
