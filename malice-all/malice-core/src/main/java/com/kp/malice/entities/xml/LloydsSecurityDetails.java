//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v3.0-03/04/2009 09:20 AM(valikov)-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.05.28 at 03:24:57 PM CEST 
//


package com.kp.malice.entities.xml;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for LloydsSecurityDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="LloydsSecurityDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SyndicateCode" type="{http://tempuri.org/ImportDataSet.xsd}SyndicateCode"/>
 *         &lt;element name="SyndicateShare" type="{http://tempuri.org/ImportDataSet.xsd}Percentage"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LloydsSecurityDetails", propOrder = {
    "syndicateCode",
    "syndicateShare"
})
public class LloydsSecurityDetails {

    @XmlElement(name = "SyndicateCode", required = true)
    protected String syndicateCode;
    @XmlElement(name = "SyndicateShare", required = true)
    protected BigDecimal syndicateShare;

    /**
     * Gets the value of the syndicateCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSyndicateCode() {
        return syndicateCode;
    }

    /**
     * Sets the value of the syndicateCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSyndicateCode(String value) {
        this.syndicateCode = value;
    }

    /**
     * Gets the value of the syndicateShare property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getSyndicateShare() {
        return syndicateShare;
    }

    /**
     * Sets the value of the syndicateShare property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setSyndicateShare(BigDecimal value) {
        this.syndicateShare = value;
    }

}
