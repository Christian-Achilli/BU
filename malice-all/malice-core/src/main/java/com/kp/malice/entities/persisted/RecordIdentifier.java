package com.kp.malice.entities.persisted;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;

@SuppressWarnings("serial")
@MappedSuperclass
public class RecordIdentifier implements Serializable {

    private static Logger log = Logger.getLogger(RecordIdentifier.class);

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long recordId;
    @Version
    @Column(name = "VERSION")
    private Long version;

    @NotNull
    @Column(name = "COD_UTE_INS")
    private String codUteIns;
    @NotNull
    @Column(name = "TMST_INS_RIG")
    private Date tmstInsRig;
    @NotNull
    @Column(name = "COD_UTE_AGR")
    private String codUteAgr;
    @NotNull
    @Column(name = "TMST_AGR_RIG")
    private Date tmstAgrRig;

    public static RecordIdentifier getInitRecord() {
        RecordIdentifier rid = new RecordIdentifier();
        String username = "manuale";
        try {
            username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            log.error("CONTESTO DI SICUREZZA NON INIZIALIZZATO");
        }
        rid.setCodUteAgr(username);
        rid.setCodUteIns(username);
        Date d = new Date();
        rid.setTmstAgrRig(d);
        rid.setTmstInsRig(d);
        return rid;
    }

    public RecordIdentifier(RecordIdentifier ri) {
        setCodUteAgr(ri.getCodUteAgr());
        setCodUteIns(ri.getCodUteIns());
        setTmstAgrRig(ri.getTmstAgrRig());
        setTmstInsRig(ri.getTmstInsRig());
    }

    public RecordIdentifier() {
        super();
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getCodUteIns() {
        return codUteIns;
    }

    public void setCodUteIns(String codUteIns) {
        this.codUteIns = codUteIns;
    }

    public Date getTmstInsRig() {
        return tmstInsRig;
    }

    public void setTmstInsRig(Date tmstInsRig) {
        this.tmstInsRig = tmstInsRig;
    }

    public String getCodUteAgr() {
        return codUteAgr;
    }

    public void setCodUteAgr(String codUteAgr) {
        this.codUteAgr = codUteAgr;
    }

    public Date getTmstAgrRig() {
        return tmstAgrRig;
    }

    public void setTmstAgrRig(Date tmstAgrRig) {
        this.tmstAgrRig = tmstAgrRig;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((recordId == null) ? 0 : recordId.hashCode());
        result = prime * result + ((version == null) ? 0 : version.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RecordIdentifier other = (RecordIdentifier) obj;
        if (recordId == null) {
            if (other.recordId != null)
                return false;
        } else if (!recordId.equals(other.recordId))
            return false;
        if (version == null) {
            if (other.version != null)
                return false;
        } else if (!version.equals(other.version))
            return false;
        return true;
    }

    public void aggiornaTimestamp() {
        RecordIdentifier toTimestamp = getInitRecord();
        setTmstAgrRig(toTimestamp.getTmstInsRig());
        setCodUteAgr(toTimestamp.getCodUteIns());
    }

}