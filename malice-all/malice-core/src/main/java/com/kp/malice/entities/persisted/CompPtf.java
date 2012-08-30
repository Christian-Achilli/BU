package com.kp.malice.entities.persisted;

import java.io.Serializable;
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
@Table(name = "COMP_PTF", uniqueConstraints = @UniqueConstraint(columnNames = { "codCompPtf" }))
public class CompPtf extends RecordIdentifier implements Serializable {

    @NaturalId
    @NotNull
    private int codCompPtf;

    @NotNull
    private String denCompPtf;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumns({ @JoinColumn(name = "codCompPtf", referencedColumnName = "codCompPtf", insertable = true, updatable = true, nullable = true) })
    private Set<PunVnd> punVnds = new HashSet<PunVnd>(0);

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumns({ @JoinColumn(name = "codCompPtf", referencedColumnName = "codCompPtf", insertable = true, updatable = true, nullable = true) })
    private Set<Pol> pols = new HashSet<Pol>(0);

    public CompPtf() {
    }

    public CompPtf(RecordIdentifier rid, int codCompPtf, String denominazione) {
        super(rid);
        setCodCompPtf(codCompPtf);
        setDenCompPtf(denominazione);
    }

    public int getCodCompPtf() {
        return this.codCompPtf;
    }

    public void setCodCompPtf(int codCompPtf) {
        this.codCompPtf = codCompPtf;
    }

    public String getDenCompPtf() {
        return this.denCompPtf;
    }

    public void setDenCompPtf(String denCompPtf) {
        this.denCompPtf = denCompPtf;
    }

    public Set<PunVnd> getPunVnds() {
        return this.punVnds;
    }

    public void setPunVnds(Set<PunVnd> punVnds) {
        this.punVnds = punVnds;
    }

    public Set<Pol> getPols() {
        return this.pols;
    }

    public void setPols(Set<Pol> pols) {
        this.pols = pols;
    }

    @Override
    public int hashCode() {
        final long prime = 31;
        long result = super.hashCode();
        result = prime * result + codCompPtf;
        return (int) result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        CompPtf other = (CompPtf) obj;
        if (codCompPtf != other.codCompPtf)
            return false;
        return true;
    }

}
