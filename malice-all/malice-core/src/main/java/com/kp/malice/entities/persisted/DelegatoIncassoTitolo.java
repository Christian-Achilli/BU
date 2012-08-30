package com.kp.malice.entities.persisted;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.NamedNativeQuery;

@SuppressWarnings("serial")
@Entity
@Table(name = "DLG_INC")
public class DelegatoIncassoTitolo extends RecordIdentifier {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TTL_ID", referencedColumnName = "ID")
    @NotNull
    private TtlCtb titolo;
    @ManyToOne(fetch = FetchType.LAZY)
    @ForeignKey(name = "FK_DLG_INC_PUN_VND")
    private PunVnd punVndDelegatoIncasso;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "INC_ID", referencedColumnName = "ID")
    @NotNull
    private Incs incasso;

    public DelegatoIncassoTitolo() {

    }

    public DelegatoIncassoTitolo(TtlCtb titolo, Incs incasso, PunVnd delegatoIncasso) {
        super(incasso);
        setPunVndDelegatoIncasso(delegatoIncasso);
        setTitolo(titolo);
        setIncasso(incasso);
    }

    public PunVnd getPunVndDelegatoIncasso() {
        return punVndDelegatoIncasso;
    }

    public void setPunVndDelegatoIncasso(PunVnd punVndDelegatoIncasso) {
        this.punVndDelegatoIncasso = punVndDelegatoIncasso;
    }

    public TtlCtb getTitolo() {
        return titolo;
    }

    public void setTitolo(TtlCtb titolo) {
        this.titolo = titolo;
    }

    public Incs getIncasso() {
        return incasso;
    }

    public void setIncasso(Incs incasso) {
        this.incasso = incasso;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((incasso == null) ? 0 : incasso.hashCode());
        result = prime * result + ((punVndDelegatoIncasso == null) ? 0 : punVndDelegatoIncasso.hashCode());
        result = prime * result + ((titolo == null) ? 0 : titolo.hashCode());
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
        DelegatoIncassoTitolo other = (DelegatoIncassoTitolo) obj;
        if (incasso == null) {
            if (other.incasso != null)
                return false;
        } else if (!incasso.equals(other.incasso))
            return false;
        if (punVndDelegatoIncasso == null) {
            if (other.punVndDelegatoIncasso != null)
                return false;
        } else if (!punVndDelegatoIncasso.equals(other.punVndDelegatoIncasso))
            return false;
        if (titolo == null) {
            if (other.titolo != null)
                return false;
        } else if (!titolo.equals(other.titolo))
            return false;
        return true;
    }

}
