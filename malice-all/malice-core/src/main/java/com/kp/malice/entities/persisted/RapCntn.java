package com.kp.malice.entities.persisted;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@SuppressWarnings("serial")
@Entity
@Table(name = "RAP_CNTN")
public class RapCntn extends RecordIdentifier {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VRN_POL_ID", referencedColumnName = "ID")
    @NotNull
    private VrnPol vrnPol;
    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoContraenza tipoCntn;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PSN_ID", referencedColumnName = "ID")
    @NotNull
    private Psn persona;

    public RapCntn(RecordIdentifier timestamp) {
        super(timestamp);
    }

    public RapCntn() {

    }

    public enum TipoContraenza {
        CONTRAENTE;
    }

    public VrnPol getVrnPol() {
        return vrnPol;
    }

    public void setVrnPol(VrnPol vrnPol) {
        this.vrnPol = vrnPol;
    }

    public TipoContraenza getTipoCntn() {
        return tipoCntn;
    }

    public void setTipoCntn(TipoContraenza tipoCntn) {
        this.tipoCntn = tipoCntn;
    }

    public Psn getPersona() {
        return persona;
    }

    public void setPersona(Psn persona) {
        this.persona = persona;
    }

    /**
    COD_COMP_ANIA       NUMBER(5)                 NOT NULL,
    COD_POL             VARCHAR2(18 BYTE)         NOT NULL,
    COD_VRN_POL         NUMBER(5)                 NOT NULL,
    COD_UNTA_OPER_AUN   NUMBER(5)                 NOT NULL,
    COD_PSN             NUMBER(10)                NOT NULL,
    DAT_EFFT_RAP_CNTN   DATE                      NOT NULL,
    COD_PRG_IND         NUMBER(5)                 NOT NULL,
    COD_PSN0            NUMBER(10)                NOT NULL,
    COD_UNTA_OPER_AUN0  NUMBER(5)                 NOT NULL,
    DAT_FINE_RAP_CNTN   DATE,
    COD_STA_CNTN        VARCHAR2(1 BYTE),
    IDC_AVS_SCA         VARCHAR2(1 BYTE),
    DEN_QLFZ_CNTN       VARCHAR2(30 BYTE),
    COD_UTE_INS         VARCHAR2(8 BYTE)          NOT NULL,
    TMST_INS_RIG        DATE                      NOT NULL,
    COD_UTE_AGR         VARCHAR2(8 BYTE)          NOT NULL,
    TMST_AGR_RIG        DATE                      NOT NULL
    */

}
