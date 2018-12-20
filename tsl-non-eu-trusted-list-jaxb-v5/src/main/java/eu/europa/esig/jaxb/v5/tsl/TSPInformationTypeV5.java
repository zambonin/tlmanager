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
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for TSPInformationType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TSPInformationType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="TSPName" type="{http://uri.etsi.org/02231/v2#}InternationalNamesType"/&gt;
 *         &lt;element name="TSPTradeName" type="{http://uri.etsi.org/02231/v2#}InternationalNamesType" minOccurs="0"/&gt;
 *         &lt;element name="TSPAddress" type="{http://uri.etsi.org/02231/v2#}AddressType"/&gt;
 *         &lt;element name="TSPInformationURI" type="{http://uri.etsi.org/02231/v2#}NonEmptyMultiLangURIListType"/&gt;
 *         &lt;element name="TSPInformationExtensions" type="{http://uri.etsi.org/02231/v2#}ExtensionsListType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TSPInformationType", propOrder = { "tspName", "tspTradeName", "tspAddress", "tspInformationURI", "tspInformationExtensions" })
public class TSPInformationTypeV5 implements Serializable {

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "TSPName", required = true)
    protected InternationalNamesTypeV5 tspName;
    @XmlElement(name = "TSPTradeName")
    protected InternationalNamesTypeV5 tspTradeName;
    @XmlElement(name = "TSPAddress", required = true)
    protected AddressTypeV5 tspAddress;
    @XmlElement(name = "TSPInformationURI", required = true)
    protected NonEmptyMultiLangURIListTypeV5 tspInformationURI;
    @XmlElement(name = "TSPInformationExtensions")
    protected ExtensionsListTypeV5 tspInformationExtensions;

    /**
     * Gets the value of the tspName property.
     * 
     * @return possible object is {@link InternationalNamesTypeV5 }
     * 
     */
    public InternationalNamesTypeV5 getTSPName() {
        return tspName;
    }

    /**
     * Sets the value of the tspName property.
     * 
     * @param value
     *            allowed object is {@link InternationalNamesTypeV5 }
     * 
     */
    public void setTSPName(InternationalNamesTypeV5 value) {
        this.tspName = value;
    }

    /**
     * Gets the value of the tspTradeName property.
     * 
     * @return possible object is {@link InternationalNamesTypeV5 }
     * 
     */
    public InternationalNamesTypeV5 getTSPTradeName() {
        return tspTradeName;
    }

    /**
     * Sets the value of the tspTradeName property.
     * 
     * @param value
     *            allowed object is {@link InternationalNamesTypeV5 }
     * 
     */
    public void setTSPTradeName(InternationalNamesTypeV5 value) {
        this.tspTradeName = value;
    }

    /**
     * Gets the value of the tspAddress property.
     * 
     * @return possible object is {@link AddressTypeV5 }
     * 
     */
    public AddressTypeV5 getTSPAddress() {
        return tspAddress;
    }

    /**
     * Sets the value of the tspAddress property.
     * 
     * @param value
     *            allowed object is {@link AddressTypeV5 }
     * 
     */
    public void setTSPAddress(AddressTypeV5 value) {
        this.tspAddress = value;
    }

    /**
     * Gets the value of the tspInformationURI property.
     * 
     * @return possible object is {@link NonEmptyMultiLangURIListTypeV5 }
     * 
     */
    public NonEmptyMultiLangURIListTypeV5 getTSPInformationURI() {
        return tspInformationURI;
    }

    /**
     * Sets the value of the tspInformationURI property.
     * 
     * @param value
     *            allowed object is {@link NonEmptyMultiLangURIListTypeV5 }
     * 
     */
    public void setTSPInformationURI(NonEmptyMultiLangURIListTypeV5 value) {
        this.tspInformationURI = value;
    }

    /**
     * Gets the value of the tspInformationExtensions property.
     * 
     * @return possible object is {@link ExtensionsListTypeV5 }
     * 
     */
    public ExtensionsListTypeV5 getTSPInformationExtensions() {
        return tspInformationExtensions;
    }

    /**
     * Sets the value of the tspInformationExtensions property.
     * 
     * @param value
     *            allowed object is {@link ExtensionsListTypeV5 }
     * 
     */
    public void setTSPInformationExtensions(ExtensionsListTypeV5 value) {
        this.tspInformationExtensions = value;
    }

}
