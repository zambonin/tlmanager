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

package eu.europa.esig.jaxb.v5.ecc;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for QualificationElementType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="QualificationElementType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Qualifiers" type="{http://uri.etsi.org/TrstSvc/SvcInfoExt/eSigDir-1999-93-EC-TrustedList/#}QualifiersType"/&gt;
 *         &lt;element name="CriteriaList" type="{http://uri.etsi.org/TrstSvc/SvcInfoExt/eSigDir-1999-93-EC-TrustedList/#}CriteriaListType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QualificationElementType", propOrder = { "qualifiers", "criteriaList" })
public class QualificationElementTypeV5 implements Serializable {

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "Qualifiers", required = true)
    protected QualifiersTypeV5 qualifiers;
    @XmlElement(name = "CriteriaList", required = true)
    protected CriteriaListTypeV5 criteriaList;

    /**
     * Gets the value of the qualifiers property.
     * 
     * @return possible object is {@link QualifiersTypeV5 }
     * 
     */
    public QualifiersTypeV5 getQualifiers() {
        return qualifiers;
    }

    /**
     * Sets the value of the qualifiers property.
     * 
     * @param value
     *            allowed object is {@link QualifiersTypeV5 }
     * 
     */
    public void setQualifiers(QualifiersTypeV5 value) {
        this.qualifiers = value;
    }

    /**
     * Gets the value of the criteriaList property.
     * 
     * @return possible object is {@link CriteriaListTypeV5 }
     * 
     */
    public CriteriaListTypeV5 getCriteriaList() {
        return criteriaList;
    }

    /**
     * Sets the value of the criteriaList property.
     * 
     * @param value
     *            allowed object is {@link CriteriaListTypeV5 }
     * 
     */
    public void setCriteriaList(CriteriaListTypeV5 value) {
        this.criteriaList = value;
    }

}