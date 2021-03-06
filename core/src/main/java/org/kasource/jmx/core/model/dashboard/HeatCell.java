//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.10.01 at 09:58:22 AM CEST 
//


package org.kasource.jmx.core.model.dashboard;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="normalizationFunction" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="data" type="{http://kasource.org/schema/ka-jmx}attributeValue"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "normalizationFunction",
    "data"
})
@XmlRootElement(name = "heatCell")
public class HeatCell {

    protected String normalizationFunction;
    @XmlElement(required = true)
    protected AttributeValue data;

    /**
     * Gets the value of the normalizationFunction property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNormalizationFunction() {
        return normalizationFunction;
    }

    /**
     * Sets the value of the normalizationFunction property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNormalizationFunction(String value) {
        this.normalizationFunction = value;
    }

    /**
     * Gets the value of the data property.
     * 
     * @return
     *     possible object is
     *     {@link AttributeValue }
     *     
     */
    public AttributeValue getData() {
        return data;
    }

    /**
     * Sets the value of the data property.
     * 
     * @param value
     *     allowed object is
     *     {@link AttributeValue }
     *     
     */
    public void setData(AttributeValue value) {
        this.data = value;
    }

}
