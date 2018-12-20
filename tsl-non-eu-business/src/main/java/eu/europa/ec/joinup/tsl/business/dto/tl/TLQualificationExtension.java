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
package eu.europa.ec.joinup.tsl.business.dto.tl;

import java.util.ArrayList;
import java.util.List;

import eu.europa.ec.joinup.tsl.model.enums.Tag;
import eu.europa.esig.jaxb.v5.ecc.CriteriaListTypeV5;
import eu.europa.esig.jaxb.v5.ecc.QualificationElementTypeV5;
import eu.europa.esig.jaxb.v5.ecc.QualifierTypeV5;
import eu.europa.esig.jaxb.v5.ecc.QualifiersTypeV5;

public class TLQualificationExtension extends AbstractTLDTO {

    private List<String> qualifTypeList;
    private TLCriteria criteria;

    public TLQualificationExtension() {
    }

    public TLQualificationExtension(int iddb, String location, QualificationElementTypeV5 qet) {
        super(iddb, location);
        List<String> qualifTypeList = new ArrayList<>();

        for (QualifierTypeV5 qualifType : qet.getQualifiers().getQualifier()) {
            qualifTypeList.add(qualifType.getUri());
        }
        setQualifTypeList(qualifTypeList);

        CriteriaListTypeV5 clt = qet.getCriteriaList();
        setCriteria(new TLCriteria(iddb, getId() + "_" + Tag.CRITERIA + "_", clt));

    }

    public QualificationElementTypeV5 asTSLTypeV5() {
        QualificationElementTypeV5 qet = new QualificationElementTypeV5();
        CriteriaListTypeV5 extClt = new CriteriaListTypeV5();

        if (getCriteria() != null) {
            extClt = getCriteria().asTSLTypeV5();
        }

        if (getQualifTypeList() != null) {
            if (qet.getQualifiers() == null) {
                qet.setQualifiers(new QualifiersTypeV5());
            }
            for (String str : getQualifTypeList()) {
                QualifierTypeV5 qt = new QualifierTypeV5();
                qt.setUri(str);
                qet.getQualifiers().getQualifier().add(qt);
            }
        }

        qet.setCriteriaList(extClt);
        return qet;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((criteria == null) ? 0 : criteria.hashCode());
        result = (prime * result) + ((qualifTypeList == null) ? 0 : qualifTypeList.hashCode());
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
        TLQualificationExtension other = (TLQualificationExtension) obj;
        if (criteria == null) {
            if (other.criteria != null) {
                return false;
            }
        } else if (!criteria.equals(other.criteria)) {
            return false;
        }
        if (qualifTypeList == null) {
            if (other.qualifTypeList != null) {
                return false;
            }
        } else if (!qualifTypeList.equals(other.qualifTypeList)) {
            return false;
        }
        return true;
    }

    public List<String> getQualifTypeList() {
        return qualifTypeList;
    }

    public void setQualifTypeList(List<String> qualifTypeList) {
        this.qualifTypeList = qualifTypeList;
    }

    public TLCriteria getCriteria() {
        return criteria;
    }

    public void setCriteria(TLCriteria criteria) {
        this.criteria = criteria;
    }

}
