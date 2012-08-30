package com.kp.malice.entities.persisted;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.NaturalId;

@SuppressWarnings("serial")
@Entity
@Table(name = "CNL_VND", uniqueConstraints = @UniqueConstraint(columnNames = { "codCompPtf", "codCnlVnd" }))
public class CnlVnd extends RecordIdentifier implements Serializable {

    @NotNull
    @NaturalId
    private int codCnlVnd;
    @NaturalId
    @NotNull
    private int codCompPtf;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    //    @JoinColumns({ @JoinColumn(name = "codCompPtf", referencedColumnName = "codCompPtf", insertable = false, updatable = false) })
    @ForeignKey(name = "FK_CNL_VND_COMP_PTF")
    private CompPtf compPtf;

    @NotNull
    private String denCnlVnd;

    public CnlVnd() {
    }

    public CnlVnd(RecordIdentifier ri, int codiceCanale, String denominazione, CompPtf compPtf) {
        super(ri);
        setCodCnlVnd(codiceCanale);
        setCodCompPtf(compPtf.getCodCompPtf());
        setCompPtf(compPtf);
        setDenCnlVnd(denominazione);
    }

    public int getCodCnlVnd() {
        return codCnlVnd;
    }

    public void setCodCnlVnd(int codCnlVnd) {
        this.codCnlVnd = codCnlVnd;
    }

    public String getDenCnlVnd() {
        return denCnlVnd;
    }

    public void setDenCnlVnd(String denCnlVnd) {
        this.denCnlVnd = denCnlVnd;
    }

    public CompPtf getCompPtf() {
        return compPtf;
    }

    public void setCompPtf(CompPtf compPtf) {
        this.compPtf = compPtf;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + codCnlVnd;
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
        CnlVnd other = (CnlVnd) obj;
        if (codCnlVnd != other.codCnlVnd)
            return false;
        return true;
    }

    public int getCodCompPtf() {
        return codCompPtf;
    }

    public void setCodCompPtf(int codCompPtf) {
        this.codCompPtf = codCompPtf;
    }

}
