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
import eu.europa.ec.joinup.tsl.business.dto.tl.TLGeneric;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLPointersToOtherTSL;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLSchemeInformation;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceDto;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceProvider;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLSupplyPoint;
import eu.europa.ec.joinup.tsl.model.enums.CheckName;
import eu.europa.ec.joinup.tsl.model.enums.Tag;

@Service
public class ServiceURIValidator extends AbstractCheckValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceURIValidator.class);

    private static final Set<CheckName> SUPPORTED_CHECK_NAMES;

    static {
        SUPPORTED_CHECK_NAMES = new HashSet<CheckName>();
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_URIS_VALID);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_URIS_ACCESSIBLE);
    }
    
    @Autowired
    private URIValidator uriValidator;

    @Override
    public boolean isSupported(CheckDTO check) {
        return check.getTarget().equals(Tag.URI_FIELDS) && SUPPORTED_CHECK_NAMES.contains(check.getName());
    }
    
    @Override
    public List<CheckResultDTO> validate(List<CheckDTO> checks, TL tl) {
        List<CheckResultDTO> results = new ArrayList<CheckResultDTO>();

        results.addAll(validateSchemeInformation(checks, tl.getSchemeInformation()));
        results.addAll(validatePointers(checks, tl.getPointers()));
        results.addAll(validateServiceProviders(checks, tl.getServiceProviders()));
        
        return results;
    }
    
    public List<CheckResultDTO> validateSchemeInformation(List<CheckDTO> checks, TLSchemeInformation schemeInformation) {
        List<CheckResultDTO> results = new ArrayList<CheckResultDTO>();
        runUriCheck(schemeInformation.getId() + "_" + Tag.ELECTRONIC_ADDRESS, checks, genericstoListOfString(schemeInformation.getSchemeOpeElectronic()), results);
        runUriCheck(schemeInformation.getId() + "_" + Tag.SCHEME_INFORMATION_URI, checks, genericstoListOfString(schemeInformation.getSchemeInfoUri()), results);
        runUriCheck(schemeInformation.getId() + "_" + Tag.DISTRIBUTION_LIST, checks, genericstoListOfString(schemeInformation.getDistributionPoint()), results);
        return results;
    }
    
    public List<CheckResultDTO> validatePointers(List<CheckDTO> checks, List<TLPointersToOtherTSL> pointers) {
        List<CheckResultDTO> results = new ArrayList<CheckResultDTO>();
        if (CollectionUtils.isNotEmpty(pointers)) {
            for (TLPointersToOtherTSL pointer : pointers) {
                results.addAll(validatePointer(checks, pointer));
            }
        }
        return results;
    }
    
    public List<CheckResultDTO> validatePointer(List<CheckDTO> checks, TLPointersToOtherTSL pointer) {
        List<CheckResultDTO> results = new ArrayList<CheckResultDTO>();
        List<String> list = new ArrayList<>();
        list.add(pointer.getTlLocation());
        runUriCheck(pointer.getId() + "_" + Tag.POINTER_LOCATION, checks, list, results);
        return results;
    }
    
    public List<CheckResultDTO> validateServiceProviders(List<CheckDTO> checks, List<TLServiceProvider> serviceProviders) {
        List<CheckResultDTO> results = new ArrayList<CheckResultDTO>();
        if (CollectionUtils.isNotEmpty(serviceProviders)) {
            for (TLServiceProvider serviceProvider : serviceProviders) {
                results.addAll(validateServiceProvider(checks, serviceProvider));
                
                List<TLServiceDto> services = serviceProvider.getTSPServices();
                if(CollectionUtils.isNotEmpty(services)) {
                    for(TLServiceDto service : services) {
                        results.addAll(validateService(checks, service));
                    }
                }
            }
        }
        return results;
    }
    
    public List<CheckResultDTO> validateServiceProvider(List<CheckDTO> checks, TLServiceProvider serviceProvider) {
        List<CheckResultDTO> results = new ArrayList<CheckResultDTO>();
        runUriCheck(serviceProvider.getId() + "_" + Tag.ELECTRONIC_ADDRESS, checks, genericstoListOfString(serviceProvider.getTSPElectronic()), results);
        runUriCheck(serviceProvider.getId() + "_" + Tag.TSP_INFORMATION_URI, checks, genericstoListOfString(serviceProvider.getTSPInfoUri()), results);
        return results;
    }
    
    public List<CheckResultDTO> validateService(List<CheckDTO> checks, TLServiceDto service) {
        List<CheckResultDTO> results = new ArrayList<CheckResultDTO>();
        runUriCheck(service.getId() + "_" + Tag.TSP_SERVICE_DEFINITION_URI, checks, genericstoListOfString(service.getTSPDefinitionUri()), results);
        runUriCheck(service.getId() + "_" + Tag.SCHEME_SERVICE_DEFINITION_URI, checks, genericstoListOfString(service.getSchemeDefinitionUri()), results);
        runUriCheck(service.getId() + "_" + Tag.SERVICE_SUPPLY_POINT, checks, supplyPointstoListOfString(service.getSupplyPoint()), results);
        return results;
    }
    
    public List<String> genericstoListOfString(List<? extends TLGeneric> generics) {
        List<String> list = new ArrayList<>();
        
        if (CollectionUtils.isNotEmpty(generics)) {
            for (TLGeneric generic : generics) {
                list.add(generic.getValue());
            }
        }
        return list;
    }
    
    public List<String> supplyPointstoListOfString(List<TLSupplyPoint> supplyPoints) {
        List<String> list = new ArrayList<>();
        
        if (CollectionUtils.isNotEmpty(supplyPoints)) {
            for (TLSupplyPoint supplyPoint : supplyPoints) {
                list.add(supplyPoint.getValue());
            }
        }
        return list;
    }
    
    public void runUriCheck(String location, List<CheckDTO> checks, List<String> listOfUris, List<CheckResultDTO> results) {
        for(CheckDTO check : checks) {
            switch (check.getName()) {
            case IS_URIS_VALID:
                isUrisValid(location, check, results, listOfUris);
                break;
            case IS_URIS_ACCESSIBLE:
                isUrisAccessible(location, check, results, listOfUris);
                break;
            default:
                LOGGER.warn("Unsupported " + check);
            }
        }
    }
    
    private void isUrisValid(String location, CheckDTO check, List<CheckResultDTO> results, List<String> listOfUris) {
        if (CollectionUtils.isNotEmpty(listOfUris)) {
            for (String uri : listOfUris) {
                addResult(check, location, uriValidator.isCorrectUri(uri), results);
            }
        }
    }

    private void isUrisAccessible(String location, CheckDTO check, List<CheckResultDTO> results, List<String> listOfUris) {
        if (CollectionUtils.isNotEmpty(listOfUris)) {
            for (String uri : listOfUris) {
                if (!uriValidator.isMailTo(uri)) {
                    runAsync(check, location, uriValidator.isAccessibleUri(uri), results);
                }
            }
        }
    }
}
