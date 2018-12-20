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
package eu.europa.ec.joinup.tsl.business.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.dto.tl.TLPointersToOtherTSL;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceProvider;
import eu.europa.ec.joinup.tsl.model.enums.Tag;
import eu.europa.esig.jaxb.v5.tsl.OtherTSLPointerTypeV5;
import eu.europa.esig.jaxb.v5.tsl.OtherTSLPointersTypeV5;
import eu.europa.esig.jaxb.v5.tsl.TSPTypeV5;
import eu.europa.esig.jaxb.v5.tsl.TrustServiceProviderListTypeV5;
import eu.europa.esig.jaxb.v5.tsl.TrustStatusListTypeV5;

/**
 * Get trusted list DTO from JaxB trusted list object
 */
@Service
public class TSLExtractor {

    public List<TLPointersToOtherTSL> getTLPointers(TrustStatusListTypeV5 tsl) {
        if ((tsl != null) && (tsl.getSchemeInformation() != null)) {
            return getTLPointers(0, tsl.getSchemeInformation().getPointersToOtherTSL());
        }
        return new ArrayList<>();
    }

    public List<TLPointersToOtherTSL> getTLPointers(int iddb, OtherTSLPointersTypeV5 pointersToOtherTSL) {
        List<TLPointersToOtherTSL> result = new ArrayList<>();
        int i = 0;
        if ((pointersToOtherTSL != null) && CollectionUtils.isNotEmpty(pointersToOtherTSL.getOtherTSLPointer())) {
            for (OtherTSLPointerTypeV5 otherPointer : pointersToOtherTSL.getOtherTSLPointer()) {
                i++;
                result.add(new TLPointersToOtherTSL(iddb, iddb + "_" + Tag.POINTERS_TO_OTHER_TSL + "_" + i, otherPointer));
            }
        }

        return result;
    }

    public List<TLServiceProvider> getTLProviders(int iddb, TrustServiceProviderListTypeV5 tslProviders) {
        List<TLServiceProvider> result = new ArrayList<>();
        int i = 0;
        if ((tslProviders != null) && CollectionUtils.isNotEmpty(tslProviders.getTrustServiceProvider())) {
            for (TSPTypeV5 tslProvider : tslProviders.getTrustServiceProvider()) {
                i++;
                result.add(new TLServiceProvider(iddb, iddb + "_" + Tag.TSP_SERVICE_PROVIDER + "_" + i, tslProvider));
            }
        }

        return result;
    }

}
