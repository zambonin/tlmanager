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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * <p>
 * Java class for TSPServiceInformationType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TSPServiceInformationType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://uri.etsi.org/02231/v2#}ServiceTypeIdentifier"/&gt;
 *         &lt;element name="ServiceName" type="{http://uri.etsi.org/02231/v2#}InternationalNamesType"/&gt;
 *         &lt;element ref="{http://uri.etsi.org/02231/v2#}ServiceDigitalIdentity"/&gt;
 *         &lt;element ref="{http://uri.etsi.org/02231/v2#}ServiceStatus"/&gt;
 *         &lt;element name="StatusStartingTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="SchemeServiceDefinitionURI" type="{http://uri.etsi.org/02231/v2#}NonEmptyMultiLangURIListType" minOccurs="0"/&gt;
 *         &lt;element ref="{http://uri.etsi.org/02231/v2#}ServiceSupplyPoints" minOccurs="0"/&gt;
 *         &lt;element name="TSPServiceDefinitionURI" type="{http://uri.etsi.org/02231/v2#}NonEmptyMultiLangURIListType" minOccurs="0"/&gt;
 *         &lt;element name="ServiceInformationExtensions" type="{http://uri.etsi.org/02231/v2#}ExtensionsListType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TSPServiceInformationType", propOrder = { "serviceTypeIdentifier", "serviceName", "serviceDigitalIdentity", "serviceStatus", "statusStartingTime", "schemeServiceDefinitionURI",
        "serviceSupplyPoints", "tspServiceDefinitionURI", "serviceInformationExtensions" })
public class TSPServiceInformationTypeV5 implements Serializable {

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "ServiceTypeIdentifier", required = true)
    @XmlSchemaType(name = "anyURI")
    protected String serviceTypeIdentifier;
    @XmlElement(name = "ServiceName", required = true)
    protected InternationalNamesTypeV5 serviceName;
    @XmlElement(name = "ServiceDigitalIdentity", required = true)
    protected DigitalIdentityListTypeV5 serviceDigitalIdentity;
    @XmlElement(name = "ServiceStatus", required = true)
    @XmlSchemaType(name = "anyURI")
    protected String serviceStatus;
    @XmlElement(name = "StatusStartingTime", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar statusStartingTime;
    @XmlElement(name = "SchemeServiceDefinitionURI")
    protected NonEmptyMultiLangURIListTypeV5 schemeServiceDefinitionURI;
    @XmlElement(name = "ServiceSupplyPoints")
    protected ServiceSupplyPointsTypeV5 serviceSupplyPoints;
    @XmlElement(name = "TSPServiceDefinitionURI")
    protected NonEmptyMultiLangURIListTypeV5 tspServiceDefinitionURI;
    @XmlElement(name = "ServiceInformationExtensions")
    protected ExtensionsListTypeV5 serviceInformationExtensions;

    /**
     * Gets the value of the serviceTypeIdentifier property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getServiceTypeIdentifier() {
        return serviceTypeIdentifier;
    }

    /**
     * Sets the value of the serviceTypeIdentifier property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setServiceTypeIdentifier(String value) {
        this.serviceTypeIdentifier = value;
    }

    /**
     * Gets the value of the serviceName property.
     * 
     * @return possible object is {@link InternationalNamesTypeV5 }
     * 
     */
    public InternationalNamesTypeV5 getServiceName() {
        return serviceName;
    }

    /**
     * Sets the value of the serviceName property.
     * 
     * @param value
     *            allowed object is {@link InternationalNamesTypeV5 }
     * 
     */
    public void setServiceName(InternationalNamesTypeV5 value) {
        this.serviceName = value;
    }

    /**
     * Gets the value of the serviceDigitalIdentity property.
     * 
     * @return possible object is {@link DigitalIdentityListTypeV5 }
     * 
     */
    public DigitalIdentityListTypeV5 getServiceDigitalIdentity() {
        return serviceDigitalIdentity;
    }

    /**
     * Sets the value of the serviceDigitalIdentity property.
     * 
     * @param value
     *            allowed object is {@link DigitalIdentityListTypeV5 }
     * 
     */
    public void setServiceDigitalIdentity(DigitalIdentityListTypeV5 value) {
        this.serviceDigitalIdentity = value;
    }

    /**
     * Gets the value of the serviceStatus property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getServiceStatus() {
        return serviceStatus;
    }

    /**
     * Sets the value of the serviceStatus property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setServiceStatus(String value) {
        this.serviceStatus = value;
    }

    /**
     * Gets the value of the statusStartingTime property.
     * 
     * @return possible object is {@link XMLGregorianCalendar }
     * 
     */
    public XMLGregorianCalendar getStatusStartingTime() {
        return statusStartingTime;
    }

    /**
     * Sets the value of the statusStartingTime property.
     * 
     * @param value
     *            allowed object is {@link XMLGregorianCalendar }
     * 
     */
    public void setStatusStartingTime(XMLGregorianCalendar value) {
        this.statusStartingTime = value;
    }

    /**
     * Gets the value of the schemeServiceDefinitionURI property.
     * 
     * @return possible object is {@link NonEmptyMultiLangURIListTypeV5 }
     * 
     */
    public NonEmptyMultiLangURIListTypeV5 getSchemeServiceDefinitionURI() {
        return schemeServiceDefinitionURI;
    }

    /**
     * Sets the value of the schemeServiceDefinitionURI property.
     * 
     * @param value
     *            allowed object is {@link NonEmptyMultiLangURIListTypeV5 }
     * 
     */
    public void setSchemeServiceDefinitionURI(NonEmptyMultiLangURIListTypeV5 value) {
        this.schemeServiceDefinitionURI = value;
    }

    /**
     * Gets the value of the serviceSupplyPoints property.
     * 
     * @return possible object is {@link ServiceSupplyPointsTypeV5 }
     * 
     */
    public ServiceSupplyPointsTypeV5 getServiceSupplyPoints() {
        return serviceSupplyPoints;
    }

    /**
     * Sets the value of the serviceSupplyPoints property.
     * 
     * @param value
     *            allowed object is {@link ServiceSupplyPointsTypeV5 }
     * 
     */
    public void setServiceSupplyPoints(ServiceSupplyPointsTypeV5 value) {
        this.serviceSupplyPoints = value;
    }

    /**
     * Gets the value of the tspServiceDefinitionURI property.
     * 
     * @return possible object is {@link NonEmptyMultiLangURIListTypeV5 }
     * 
     */
    public NonEmptyMultiLangURIListTypeV5 getTSPServiceDefinitionURI() {
        return tspServiceDefinitionURI;
    }

    /**
     * Sets the value of the tspServiceDefinitionURI property.
     * 
     * @param value
     *            allowed object is {@link NonEmptyMultiLangURIListTypeV5 }
     * 
     */
    public void setTSPServiceDefinitionURI(NonEmptyMultiLangURIListTypeV5 value) {
        this.tspServiceDefinitionURI = value;
    }

    /**
     * Gets the value of the serviceInformationExtensions property.
     * 
     * @return possible object is {@link ExtensionsListTypeV5 }
     * 
     */
    public ExtensionsListTypeV5 getServiceInformationExtensions() {
        return serviceInformationExtensions;
    }

    /**
     * Sets the value of the serviceInformationExtensions property.
     * 
     * @param value
     *            allowed object is {@link ExtensionsListTypeV5 }
     * 
     */
    public void setServiceInformationExtensions(ExtensionsListTypeV5 value) {
        this.serviceInformationExtensions = value;
    }

}
