package com.kp.malice.entities.persisted;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NaturalId;

@SuppressWarnings("serial")
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Table(name = "UNTA_OPER_AUN", uniqueConstraints = @UniqueConstraint(columnNames = { "codUntaOperAun" }))
public class UntaOperAun extends RecordIdentifier implements java.io.Serializable {

    //    PRIMARY KEY
    //    (COD_UNTA_OPER_AUN));
    @NotNull
    @NaturalId
    private long codUntaOperAun;
    private String description;
    private String shortDescription;
    private String indMail;
    private String codDstNzle;
    private String codNumTel;
    private String codNumFax;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumns({ @JoinColumn(name = "codUntaOperAun", referencedColumnName = "codUntaOperAun", insertable = false, updatable = false, nullable = true) })
    private Set<PunVnd> punVnds = new HashSet<PunVnd>(0);

    public UntaOperAun() {
    }

    public UntaOperAun(RecordIdentifier rid, long codUntaOperAun, String description, String shortDescription) {
        super(rid);
        setCodUntaOperAun(codUntaOperAun);
        setDescription(description);
        setShortDescription(shortDescription);
    }

    public long getCodUntaOperAun() {
        return this.codUntaOperAun;
    }

    public void setCodUntaOperAun(long codUntaOperAun) {
        this.codUntaOperAun = codUntaOperAun;
    }

    public Set<PunVnd> getPunVnds() {
        return this.punVnds;
    }

    public void setPunVnds(Set<PunVnd> punVnds) {
        this.punVnds = punVnds;
    }

    public String getIndMail() {
        return indMail;
    }

    public void setIndMail(String indMail) {
        this.indMail = indMail;
    }

    public String getCodDstNzle() {
        return codDstNzle;
    }

    public void setCodDstNzle(String codDstNzle) {
        this.codDstNzle = codDstNzle;
    }

    public String getCodNumTel() {
        return codNumTel;
    }

    public void setCodNumTel(String codNumTel) {
        this.codNumTel = codNumTel;
    }

    public String getCodNumFax() {
        return codNumFax;
    }

    public void setCodNumFax(String codNumFax) {
        this.codNumFax = codNumFax;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (int) (codUntaOperAun ^ (codUntaOperAun >>> 32));
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
        UntaOperAun other = (UntaOperAun) obj;
        if (codUntaOperAun != other.codUntaOperAun)
            return false;
        return true;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

}
