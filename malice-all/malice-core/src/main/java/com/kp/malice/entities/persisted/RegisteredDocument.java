package com.kp.malice.entities.persisted;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "XML_RCVD_DOCS")
public class RegisteredDocument extends RecordIdentifier {
    @Column(name = "FROM_BROKER")
    private String fromBroker;
    @Column(name = "FOR_AGENZIA")
    private String forAgenzia;
    @Column(name = "NUM_CERTIFICATO")
    private String numCertificato;
    @Column(name = "NUM_APPENDICE")
    private String numAppendice;
    @Column(name = "SCHEMA_VALIDATION")
    private boolean isSchemaConsistent;
    @Column(columnDefinition = "TEXT", name = "XML_VALIDATION_ERR_MSG")
    private String validationErrorMessage;
    @Column(columnDefinition = "TEXT", name = "XML_STRING")
    private String xmlStr;
    @Column(name = "LAVORATO")
    private boolean worked;
    @Column(name = "LAVORATO_OK")
    private boolean convertedOk;
    @Column(name = "MOV_ID")
    private Long movimentoId;
    @Column(name = "DB_PERSIST_ERR_MSG")
    private String conversionException;
    @Column(name = "ENCODING")
    private String encoding;

    public boolean isAddendum() {
        return !getNumCertificato().equals(getNumAppendice());
    }
    
    public String getNumCertificato() {
        return numCertificato;
    }

    public void setNumCertificato(String numCertificato) {
        this.numCertificato = numCertificato;
    }

    public String getNumAppendice() {
        return numAppendice;
    }

    public void setNumAppendice(String numAppendice) {
        this.numAppendice = numAppendice;
    }

    public RegisteredDocument() {

    }

    public RegisteredDocument(RecordIdentifier timestamp) {
        super(timestamp);
        setCodUteIns("Interceptor");
        setCodUteAgr("Interceptor");
    }

    public void setDocumentIsValid(boolean valid) {
        this.isSchemaConsistent = valid;
    }

    public void setValidationErrorMessage(String validationErrorMessage) {
        this.validationErrorMessage = validationErrorMessage;
    }

    public String getFromBroker() {
        return fromBroker;
    }

    public void setFromBroker(String fromBroker) {
        this.fromBroker = fromBroker;
    }

    public String getForAgenzia() {
        return forAgenzia;
    }

    public void setForAgenzia(String forAgenzia) {
        this.forAgenzia = forAgenzia;
    }

    public boolean isSchemaConsistent() {
        return isSchemaConsistent;
    }

    public String getValidationErrorMessage() {
        return validationErrorMessage;
    }

    public String getXmlStr() {
        return xmlStr;
    }

    public void setXmlStr(String xmlStr) {
        this.xmlStr = xmlStr;
    }

    public boolean isWorked() {
        return worked;
    }

    public void setWorked(boolean worked) {
        this.worked = worked;
    }

    public boolean isConvertedOk() {
        return convertedOk;
    }

    public void setConvertedOk(boolean convertedOk) {
        this.convertedOk = convertedOk;
    }

    public String getConversionException() {
        return conversionException;
    }

    public void setConversionException(String conversionException) {
        this.conversionException = conversionException;
    }

    public Long getMovimentoId() {
        return movimentoId;
    }

    public void setMovimentoId(Long movimentoId) {
        this.movimentoId = movimentoId;
    }

    public void setSchemaConsistent(boolean isSchemaConsistent) {
        this.isSchemaConsistent = isSchemaConsistent;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
}
