//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.09.27 at 11:57:45 PM CEST 
//


package org.kasource.jmx.core.model.dashboard;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for attributeType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="attributeType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="numeric"/>
 *     &lt;enumeration value="text"/>
 *     &lt;enumeration value="boolean"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "attributeType")
@XmlEnum
public enum AttributeType {

    @XmlEnumValue("numeric")
    NUMERIC("numeric"),
    @XmlEnumValue("text")
    TEXT("text"),
    @XmlEnumValue("boolean")
    BOOLEAN("boolean");
    private final String value;

    AttributeType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AttributeType fromValue(String v) {
        for (AttributeType c: AttributeType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
