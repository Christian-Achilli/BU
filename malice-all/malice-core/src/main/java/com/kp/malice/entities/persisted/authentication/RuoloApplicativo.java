package com.kp.malice.entities.persisted.authentication;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.kp.malice.entities.persisted.RecordIdentifier;

@SuppressWarnings("serial")
@Entity
@Table(name = "RUOLO_MALICE")
public class RuoloApplicativo extends RecordIdentifier {

    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoRuolo tipoRuolo;

    public enum TipoRuolo {
        ROLE_AGENTE, ROLE_LIO, ROLE_MONITOR;

        public static TipoRuolo fromString(String role) {
            for (TipoRuolo ruolo : values()) {
                if (ruolo.name().equals(role))
                    return ruolo;
            }
            return null;
        }
    }

    public RuoloApplicativo() {

    }

    public RuoloApplicativo(RecordIdentifier timeStamp) {
        super(timeStamp);
    }

    public TipoRuolo getTipoRuolo() {
        return tipoRuolo;
    }

    public void setTipoRuolo(TipoRuolo tipoRuolo) {
        this.tipoRuolo = tipoRuolo;
    }

}
