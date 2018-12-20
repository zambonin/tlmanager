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
package eu.europa.ec.joinup.tsl.business.rules;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.dto.CheckDTO;
import eu.europa.ec.joinup.tsl.business.dto.CheckResultDTO;
import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceProvider;
import eu.europa.ec.joinup.tsl.model.enums.CheckName;
import eu.europa.ec.joinup.tsl.model.enums.Tag;

@Service
public class TSPServiceProviderValidator extends AbstractCheckValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(TSPServiceProviderValidator.class);

    private static final Set<CheckName> SUPPORTED_CHECK_NAMES;

    static {
        SUPPORTED_CHECK_NAMES = new HashSet<CheckName>();

        // PRESENCE
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_TSP_NAME_PRESENT);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_TSP_TRADE_NAME_PRESENT);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_POSTAL_ADDRESS_PRESENT);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_ELECTRONIC_ADDRESS_PRESENT);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_INFORMATION_URI_PRESENT);
    }

    @Autowired
    private GenericValidator genericValidator;
    
    @Override
    public boolean isSupported(CheckDTO check) {
        return Tag.TSP_SERVICE_PROVIDER.equals(check.getTarget()) && SUPPORTED_CHECK_NAMES.contains(check.getName());
    }

    @Override
    public List<CheckResultDTO> validate(List<CheckDTO> checks, TL tl) {
        return validateListServiceProviders(checks, tl.getServiceProviders());
    }
    
    public List<CheckResultDTO> validateListServiceProviders(List<CheckDTO> checks, List<TLServiceProvider> serviceProviders) {
        List<CheckResultDTO> results = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(serviceProviders)) {
            for(TLServiceProvider serviceProvider: serviceProviders) {
                results.addAll(validateServiceProvider(checks, serviceProvider));
            }
        }
        return results;
    }
    
    public List<CheckResultDTO> validateServiceProvider(List<CheckDTO> checks, TLServiceProvider serviceProvider) {
        List<CheckResultDTO> results = new ArrayList<>();
        for(CheckDTO check : checks) {
            switch (check.getName()) {
            
            // PRESENCE
            case IS_TSP_NAME_PRESENT:
                addResult(check, serviceProvider.getId() + "_" + Tag.TSP_NAME, genericValidator.isCollectionPresentAndNotEmpty(serviceProvider.getTSPName()), results);
                break;
            case IS_TSP_TRADE_NAME_PRESENT:
                addResult(check, serviceProvider.getId() + "_" + Tag.TSP_TRADE_NAME, genericValidator.isCollectionPresentAndNotEmpty(serviceProvider.getTSPTradeName()), results);
                break;
            case IS_POSTAL_ADDRESS_PRESENT:
                addResult(check, serviceProvider.getId() + "_" + Tag.POSTAL_ADDRESSES, genericValidator.isCollectionNotEmpty(serviceProvider.getTSPPostal()), results);
                break;
            case IS_ELECTRONIC_ADDRESS_PRESENT:
                addResult(check, serviceProvider.getId() + "_" + Tag.ELECTRONIC_ADDRESS, genericValidator.isCollectionPresentAndNotEmpty(serviceProvider.getTSPElectronic()), results);
                break;
            case IS_INFORMATION_URI_PRESENT:
                addResult(check, serviceProvider.getId() + "_" + Tag.TSP_INFORMATION_URI, genericValidator.isCollectionPresentAndNotEmpty(serviceProvider.getTSPInfoUri()), results);
                break;
                
            default:
                LOGGER.warn("Unsupported check " + check.getName());
                break;
            }
        }
        return results;
    }
}
