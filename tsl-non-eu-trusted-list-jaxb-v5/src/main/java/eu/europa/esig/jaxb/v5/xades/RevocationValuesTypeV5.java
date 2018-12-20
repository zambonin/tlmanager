/*******************************************************************************
 * DIGIT-TSL - Trusted List Manager
 * Copyright (C) 2018 European Commission, provided under the CEF E-Signature programme
 *  
 * This file is part of the "DIGIT-TSL - Trusted List Manager" project.
 *  
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or (at
 * your option) any later version.
 *  
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 ******************************************************************************/
//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.01.06 at 08:00:20 AM CET 
//

package eu.europa.esig.jaxb.v5.xades;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * <p>
 * Java class for RevocationValuesType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RevocationValuesType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="CRLValues" type="{http://uri.etsi.org/01903/v1.3.2#}CRLValuesType" minOccurs="0"/&gt;
 *         &lt;element name="OCSPValues" type="{http://uri.etsi.org/01903/v1.3.2#}OCSPValuesType" minOccurs="0"/&gt;
 *         &lt;element name="OtherValues" type="{http://uri.etsi.org/01903/v1.3.2#}OtherCertStatusValuesType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="Id" type="{http://www.w3.org/2001/XMLSchema}ID" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RevocationValuesType", propOrder = { "crlValues", "ocspValues", "otherValues" })
public class RevocationValuesTypeV5 implements Serializable {

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "CRLValues")
    protected CRLValuesTypeV5 crlValues;
    @XmlElement(name = "OCSPValues")
    protected OCSPValuesTypeV5 ocspValues;
    @XmlElement(name = "OtherValues")
    protected OtherCertStatusValuesTypeV5 otherValues;
    @XmlAttribute(name = "Id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;

    /**
     * Gets the value of the crlValues property.
     * 
     * @return possible object is {@link CRLValuesTypeV5 }
     * 
     */
    public CRLValuesTypeV5 getCRLValues() {
        return crlValues;
    }

    /**
     * Sets the value of the crlValues property.
     * 
     * @param value
     *            allowed object is {@link CRLValuesTypeV5 }
     * 
     */
    public void setCRLValues(CRLValuesTypeV5 value) {
        this.crlValues = value;
    }

    /**
     * Gets the value of the ocspValues property.
     * 
     * @return possible object is {@link OCSPValuesTypeV5 }
     * 
     */
    public OCSPValuesTypeV5 getOCSPValues() {
        return ocspValues;
    }

    /**
     * Sets the value of the ocspValues property.
     * 
     * @param value
     *            allowed object is {@link OCSPValuesTypeV5 }
     * 
     */
    public void setOCSPValues(OCSPValuesTypeV5 value) {
        this.ocspValues = value;
    }

    /**
     * Gets the value of the otherValues property.
     * 
     * @return possible object is {@link OtherCertStatusValuesTypeV5 }
     * 
     */
    public OtherCertStatusValuesTypeV5 getOtherValues() {
        return otherValues;
    }

    /**
     * Sets the value of the otherValues property.
     * 
     * @param value
     *            allowed object is {@link OtherCertStatusValuesTypeV5 }
     * 
     */
    public void setOtherValues(OtherCertStatusValuesTypeV5 value) {
        this.otherValues = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setId(String value) {
        this.id = value;
    }

}
