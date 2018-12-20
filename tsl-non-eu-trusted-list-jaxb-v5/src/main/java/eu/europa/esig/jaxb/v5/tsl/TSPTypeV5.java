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
 * Java class for TSPType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TSPType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://uri.etsi.org/02231/v2#}TSPInformation"/&gt;
 *         &lt;element ref="{http://uri.etsi.org/02231/v2#}TSPServices"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TSPType", propOrder = { "tspInformation", "tspServices" })
public class TSPTypeV5 implements Serializable {

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "TSPInformation", required = true)
    protected TSPInformationTypeV5 tspInformation;
    @XmlElement(name = "TSPServices", required = true)
    protected TSPServicesListTypeV5 tspServices;

    /**
     * Gets the value of the tspInformation property.
     * 
     * @return possible object is {@link TSPInformationTypeV5 }
     * 
     */
    public TSPInformationTypeV5 getTSPInformation() {
        return tspInformation;
    }

    /**
     * Sets the value of the tspInformation property.
     * 
     * @param value
     *            allowed object is {@link TSPInformationTypeV5 }
     * 
     */
    public void setTSPInformation(TSPInformationTypeV5 value) {
        this.tspInformation = value;
    }

    /**
     * Gets the value of the tspServices property.
     * 
     * @return possible object is {@link TSPServicesListTypeV5 }
     * 
     */
    public TSPServicesListTypeV5 getTSPServices() {
        return tspServices;
    }

    /**
     * Sets the value of the tspServices property.
     * 
     * @param value
     *            allowed object is {@link TSPServicesListTypeV5 }
     * 
     */
    public void setTSPServices(TSPServicesListTypeV5 value) {
        this.tspServices = value;
    }

}