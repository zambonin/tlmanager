/*******************************************************************************
 * DIGIT-TSL - Trusted List Manager non-EU
 * Copyright (C) 2018 European Commission, provided under the CEF E-Signature programme
 *  
 * This file is part of the "DIGIT-TSL - Trusted List Manager non-EU" project.
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
package eu.europa.ec.joinup.tsl.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "TL_PROPERTIES")
public class DBProperties {

    @Id
    @GeneratedValue
    @Column(name = "PROPERTIES_ID", nullable = false, updatable = false)
    private int id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "PROPERTIES_LIST_CODE", nullable = false, updatable = false)
    private DBPropertiesList propertiesList;

    @Column(name = "DESCRIPTION", length = 2048, nullable = true, updatable = false)
    private String description;

    @Column(name = "LABEL", length = 2048, nullable = false, updatable = false)
    private String label;

    public DBProperties() {
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        DBProperties other = (DBProperties) obj;
        if (id != other.id) {
            return false;
        }
        return true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public DBPropertiesList getPropertiesList() {
        return propertiesList;
    }

    public void setPropertiesList(DBPropertiesList propertiesList) {
        this.propertiesList = propertiesList;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

}
