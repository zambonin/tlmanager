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

package eu.europa.esig.jaxb.v5.tsl;

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
import eu.europa.esig.jaxb.v5.xmldsig.SignatureTypeV5;

/**
 * <p>
 * Java class for TrustStatusListType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TrustStatusListType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://uri.etsi.org/02231/v2#}SchemeInformation"/&gt;
 *         &lt;element ref="{http://uri.etsi.org/02231/v2#}TrustServiceProviderList" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}Signature" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="TSLTag" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" /&gt;
 *       &lt;attribute name="Id" type="{http://www.w3.org/2001/XMLSchema}ID" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TrustStatusListType", propOrder = { "schemeInformation", "trustServiceProviderList", "signature" })
public class TrustStatusListTypeV5 implements Serializable {

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "SchemeInformation", required = true)
    protected TSLSchemeInformationTypeV5 schemeInformation;
    @XmlElement(name = "TrustServiceProviderList")
    protected TrustServiceProviderListTypeV5 trustServiceProviderList;
    @XmlElement(name = "Signature", namespace = "http://www.w3.org/2000/09/xmldsig#")
    protected SignatureTypeV5 signature;
    @XmlAttribute(name = "TSLTag", required = true)
    @XmlSchemaType(name = "anyURI")
    protected String tslTag;
    @XmlAttribute(name = "Id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;

    /**
     * Gets the value of the schemeInformation property.
     * 
     * @return possible object is {@link TSLSchemeInformationTypeV5 }
     * 
     */
    public TSLSchemeInformationTypeV5 getSchemeInformation() {
        return schemeInformation;
    }

    /**
     * Sets the value of the schemeInformation property.
     * 
     * @param value
     *            allowed object is {@link TSLSchemeInformationTypeV5 }
     * 
     */
    public void setSchemeInformation(TSLSchemeInformationTypeV5 value) {
        this.schemeInformation = value;
    }

    /**
     * Gets the value of the trustServiceProviderList property.
     * 
     * @return possible object is {@link TrustServiceProviderListTypeV5 }
     * 
     */
    public TrustServiceProviderListTypeV5 getTrustServiceProviderList() {
        return trustServiceProviderList;
    }

    /**
     * Sets the value of the trustServiceProviderList property.
     * 
     * @param value
     *            allowed object is {@link TrustServiceProviderListTypeV5 }
     * 
     */
    public void setTrustServiceProviderList(TrustServiceProviderListTypeV5 value) {
        this.trustServiceProviderList = value;
    }

    /**
     * Gets the value of the signature property.
     * 
     * @return possible object is {@link SignatureTypeV5 }
     * 
     */
    public SignatureTypeV5 getSignature() {
        return signature;
    }

    /**
     * Sets the value of the signature property.
     * 
     * @param value
     *            allowed object is {@link SignatureTypeV5 }
     * 
     */
    public void setSignature(SignatureTypeV5 value) {
        this.signature = value;
    }

    /**
     * Gets the value of the tslTag property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getTSLTag() {
        return tslTag;
    }

    /**
     * Sets the value of the tslTag property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setTSLTag(String value) {
        this.tslTag = value;
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