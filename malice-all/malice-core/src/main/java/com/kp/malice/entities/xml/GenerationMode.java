//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v3.0-03/04/2009 09:20 AM(valikov)-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.05.28 at 03:24:57 PM CEST 
//


package com.kp.malice.entities.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GenerationMode complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GenerationMode">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GenerationType" type="{http://tempuri.org/ImportDataSet.xsd}GenerationType"/>
 *         &lt;element name="ModelType" type="{http://tempuri.org/ImportDataSet.xsd}ModelType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GenerationMode", propOrder = {
    "generationType",
    "modelType"
})
public class GenerationMode {

    @XmlElement(name = "GenerationType")
    protected int generationType;
    @XmlElement(name = "ModelType")
    protected Integer modelType;

    /**
     * Gets the value of the generationType property.
     * 
     */
    public int getGenerationType() {
        return generationType;
    }

    /**
     * Sets the value of the generationType property.
     * 
     */
    public void setGenerationType(int value) {
        this.generationType = value;
    }

    /**
     * Gets the value of the modelType property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getModelType() {
        return modelType;
    }

    /**
     * Sets the value of the modelType property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setModelType(Integer value) {
        this.modelType = value;
    }

}
