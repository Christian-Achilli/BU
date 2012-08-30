package com.kp.malice.entities.persisted;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.ForeignKey;

@SuppressWarnings("serial")
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Table(name = "ENTITY_ALIAS")
public class EntityAlias extends RecordIdentifier implements Serializable {

    @ManyToOne
    @JoinColumns({ @JoinColumn(name = "ID_UOA", referencedColumnName = "codUntaOperAun", insertable = true, updatable = true) })
    @ForeignKey(name = "FK_ENTITY_ALIAS_UOA")
    @NotNull
    private UntaOperAun untaOperAun;

    @ManyToOne
    @JoinColumns({ @JoinColumn(name = "ALIAS_NAME", referencedColumnName = "ID", insertable = true, updatable = true) })
    @ForeignKey(name = "FK_ENTITY_ALIAS_ALIAS_NAME")
    @NotNull
    private AliasName aliasName;

    @ManyToOne
    @JoinColumns({ @JoinColumn(name = "ENCODING_CODE", referencedColumnName = "ID", insertable = true, updatable = true) })
    @ForeignKey(name = "FK_ENTITY_ALIAS_ENCODING_NAME")
    @NotNull
    private EncodingName encodingName;

    @Column(name = "ALIAS_CODE")
    private String aliasCode;

    public EntityAlias() {
    }

    public EntityAlias(RecordIdentifier timestampCreazione, UntaOperAun uoa, AliasName aliasName,
            EncodingName encodingName, String aliasCode) {
        super(timestampCreazione);
        setUntaOperAun(uoa);
        setAliasName(aliasName);
        setEncodingName(encodingName);
        setAliasCode(aliasCode);
    }

    public UntaOperAun getUntaOperAun() {
        return untaOperAun;
    }

    public void setUntaOperAun(UntaOperAun untaOperAun) {
        this.untaOperAun = untaOperAun;
    }

    public AliasName getAliasName() {
        return aliasName;
    }

    public void setAliasName(AliasName aliasName) {
        this.aliasName = aliasName;
    }

    public EncodingName getEncodingName() {
        return encodingName;
    }

    public void setEncodingName(EncodingName encodingName) {
        this.encodingName = encodingName;
    }

    public String getAliasCode() {
        return aliasCode;
    }

    public void setAliasCode(String aliasCode) {
        this.aliasCode = aliasCode;
    }

}
