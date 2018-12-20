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
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * <p>
 * Java class for SignedSignaturePropertiesType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SignedSignaturePropertiesType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="SigningTime" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="SigningCertificate" type="{http://uri.etsi.org/01903/v1.3.2#}CertIDListType" minOccurs="0"/&gt;
 *         &lt;element name="SignaturePolicyIdentifier" type="{http://uri.etsi.org/01903/v1.3.2#}SignaturePolicyIdentifierType" minOccurs="0"/&gt;
 *         &lt;element name="SignatureProductionPlace" type="{http://uri.etsi.org/01903/v1.3.2#}SignatureProductionPlaceType" minOccurs="0"/&gt;
 *         &lt;element name="SignerRole" type="{http://uri.etsi.org/01903/v1.3.2#}SignerRoleType" minOccurs="0"/&gt;
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
@XmlType(name = "SignedSignaturePropertiesType", propOrder = { "signingTime", "signingCertificate", "signaturePolicyIdentifier", "signatureProductionPlace", "signerRole" })
public class SignedSignaturePropertiesTypeV5 implements Serializable {

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "SigningTime")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar signingTime;
    @XmlElement(name = "SigningCertificate")
    protected CertIDListTypeV5 signingCertificate;
    @XmlElement(name = "SignaturePolicyIdentifier")
    protected SignaturePolicyIdentifierTypeV5 signaturePolicyIdentifier;
    @XmlElement(name = "SignatureProductionPlace")
    protected SignatureProductionPlaceTypeV5 signatureProductionPlace;
    @XmlElement(name = "SignerRole")
    protected SignerRoleTypeV5 signerRole;
    @XmlAttribute(name = "Id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;

    /**
     * Gets the value of the signingTime property.
     * 
     * @return possible object is {@link XMLGregorianCalendar }
     * 
     */
    public XMLGregorianCalendar getSigningTime() {
        return signingTime;
    }

    /**
     * Sets the value of the signingTime property.
     * 
     * @param value
     *            allowed object is {@link XMLGregorianCalendar }
     * 
     */
    public void setSigningTime(XMLGregorianCalendar value) {
        this.signingTime = value;
    }

    /**
     * Gets the value of the signingCertificate property.
     * 
     * @return possible object is {@link CertIDListTypeV5 }
     * 
     */
    public CertIDListTypeV5 getSigningCertificate() {
        return signingCertificate;
    }

    /**
     * Sets the value of the signingCertificate property.
     * 
     * @param value
     *            allowed object is {@link CertIDListTypeV5 }
     * 
     */
    public void setSigningCertificate(CertIDListTypeV5 value) {
        this.signingCertificate = value;
    }

    /**
     * Gets the value of the signaturePolicyIdentifier property.
     * 
     * @return possible object is {@link SignaturePolicyIdentifierTypeV5 }
     * 
     */
    public SignaturePolicyIdentifierTypeV5 getSignaturePolicyIdentifier() {
        return signaturePolicyIdentifier;
    }

    /**
     * Sets the value of the signaturePolicyIdentifier property.
     * 
     * @param value
     *            allowed object is {@link SignaturePolicyIdentifierTypeV5 }
     * 
     */
    public void setSignaturePolicyIdentifier(SignaturePolicyIdentifierTypeV5 value) {
        this.signaturePolicyIdentifier = value;
    }

    /**
     * Gets the value of the signatureProductionPlace property.
     * 
     * @return possible object is {@link SignatureProductionPlaceTypeV5 }
     * 
     */
    public SignatureProductionPlaceTypeV5 getSignatureProductionPlace() {
        return signatureProductionPlace;
    }

    /**
     * Sets the value of the signatureProductionPlace property.
     * 
     * @param value
     *            allowed object is {@link SignatureProductionPlaceTypeV5 }
     * 
     */
    public void setSignatureProductionPlace(SignatureProductionPlaceTypeV5 value) {
        this.signatureProductionPlace = value;
    }

    /**
     * Gets the value of the signerRole property.
     * 
     * @return possible object is {@link SignerRoleTypeV5 }
     * 
     */
    public SignerRoleTypeV5 getSignerRole() {
        return signerRole;
    }

    /**
     * Sets the value of the signerRole property.
     * 
     * @param value
     *            allowed object is {@link SignerRoleTypeV5 }
     * 
     */
    public void setSignerRole(SignerRoleTypeV5 value) {
        this.signerRole = value;
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
