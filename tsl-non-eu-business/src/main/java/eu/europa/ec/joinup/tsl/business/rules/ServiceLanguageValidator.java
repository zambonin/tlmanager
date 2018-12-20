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
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceExtension;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceHistory;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceProvider;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLTakenOverBy;
import eu.europa.ec.joinup.tsl.model.enums.CheckName;
import eu.europa.ec.joinup.tsl.model.enums.Tag;

@Service
public class ServiceLanguageValidator extends AbstractCheckValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceLanguageValidator.class);
    
    private static final Set<CheckName> SUPPORTED_CHECK_NAMES;

    static {
        SUPPORTED_CHECK_NAMES = new HashSet<CheckName>();
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_ATTRIBUTE_LANG_PRESENT);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_ATTRIBUTE_LANG_LOWERCASE);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_ATTRIBUTE_LANG_ALLOWED);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_LIST_CONTAIN_LANG_EN);
    }
    
    @Autowired
    private LanguageValidator languageValidator;

    @Override
    public boolean isSupported(CheckDTO check) {
        return check.getTarget().equals(Tag.LANGUAGE_FIELDS) && SUPPORTED_CHECK_NAMES.contains(check.getName());
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
        runLanguageCheck(schemeInformation.getId() + "_" + Tag.SCHEME_NAME, checks, schemeInformation.getSchemeName(), results);
        runLanguageCheck(schemeInformation.getId() + "_" + Tag.POLICY_OR_LEGAL_NOTICE, checks, schemeInformation.getSchemePolicy(), results);
        runLanguageCheck(schemeInformation.getId() + "_" + Tag.SCHEME_OPERATOR_NAME, checks, schemeInformation.getSchemeOpeName(), results);
        runLanguageCheck(schemeInformation.getId() + "_" + Tag.ELECTRONIC_ADDRESS, checks, schemeInformation.getSchemeOpeElectronic(), results);
        runLanguageCheck(schemeInformation.getId() + "_" + Tag.SCHEME_INFORMATION_URI, checks, schemeInformation.getSchemeInfoUri(), results);
        runLanguageCheck(schemeInformation.getId() + "_" + Tag.SCHEME_TYPE_COMMUNITY_RULES, checks, schemeInformation.getSchemeTypeCommRule(), results);
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
        runLanguageCheck(pointer.getId() + "_" + Tag.SCHEME_OPERATOR_NAME, checks, pointer.getSchemeOpeName(), results);
        runLanguageCheck(pointer.getId() + "_" + Tag.SCHEME_TYPE_COMMUNITY_RULES, checks, pointer.getSchemeTypeCommunity(), results);
        return results;
    }
    
    private List<CheckResultDTO> validateServiceProviders(List<CheckDTO> checks, List<TLServiceProvider> serviceProviders) {
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
        runLanguageCheck(serviceProvider.getId() + "_" + Tag.TSP_NAME, checks, serviceProvider.getTSPTradeName(), results);
        runLanguageCheck(serviceProvider.getId() + "_" + Tag.TSP_TRADE_NAME, checks, serviceProvider.getTSPTradeName(), results);
        runLanguageCheck(serviceProvider.getId() + "_" + Tag.POSTAL_ADDRESSES, checks, serviceProvider.getTSPTradeName(), results);
        runLanguageCheck(serviceProvider.getId() + "_" + Tag.ELECTRONIC_ADDRESS, checks, serviceProvider.getTSPElectronic(), results);
        runLanguageCheck(serviceProvider.getId() + "_" + Tag.TSP_INFORMATION_URI, checks, serviceProvider.getTSPInfoUri(), results);
        return results;
    }
    
    public List<CheckResultDTO> validateService(List<CheckDTO> checks, TLServiceDto service) {
        List<CheckResultDTO> results = new ArrayList<CheckResultDTO>();
        
        List<TLServiceExtension> extensions = service.getExtension();
        if (CollectionUtils.isNotEmpty(extensions)) {
            for (TLServiceExtension extension : extensions) {
                TLTakenOverBy takenOverBy = extension.getTakenOverBy();
                if (takenOverBy != null) {
                    runLanguageCheck(takenOverBy.getId() + "_" + Tag.TSP_NAME, checks, takenOverBy.getTspName(), results);
                    runLanguageCheck(service.getId() + "_" + Tag.SERVICE_NAME, checks, service.getServiceName(), results);
                    runLanguageCheck(service.getId() + "_" + Tag.TSP_SERVICE_DEFINITION_URI, checks, service.getTSPDefinitionUri(), results);
                    runLanguageCheck(service.getId() + "_" + Tag.SCHEME_SERVICE_DEFINITION_URI, checks, service.getSchemeDefinitionUri(), results);
                }
            }
        }

        List<TLServiceHistory> serviceHistories = service.getHistory();
        if (CollectionUtils.isNotEmpty(serviceHistories)) {
            for (TLServiceHistory serviceHistory : serviceHistories) {
                List<TLServiceExtension> extensionsHistory = serviceHistory.getExtension();
                if (CollectionUtils.isNotEmpty(extensionsHistory)) {
                    for (TLServiceExtension extension : extensionsHistory) {
                        TLTakenOverBy takenOverBy = extension.getTakenOverBy();
                        if (takenOverBy != null) {
                            runLanguageCheck(takenOverBy.getId() + "_" + Tag.TSP_NAME, checks, takenOverBy.getTspName(), results);
                            runLanguageCheck(serviceHistory.getId() + "_" + Tag.SERVICE_NAME, checks, serviceHistory.getServiceName(), results);
                            runLanguageCheck(takenOverBy.getId() + "_" + Tag.SCHEME_OPERATOR_NAME, checks, takenOverBy.getOperatorName(), results);
                            runLanguageCheck(takenOverBy.getId() + "_" + Tag.SCHEME_OPERATOR_NAME, checks, takenOverBy.getOperatorName(), results);
                        }
                    }
                }
            }
        }
        return results;
    }

    public void runLanguageCheck(String location, List<CheckDTO> checks, List<? extends TLGeneric> listOfValues, List<CheckResultDTO> results) {
        for(CheckDTO check : checks) {
            switch (check.getName()) {
            case IS_LIST_CONTAIN_LANG_EN:
                addResult(check, location, languageValidator.isLanguagesContainsEnglish(listOfValues), results);
                break;
            case IS_ATTRIBUTE_LANG_PRESENT:
                isLanguageAttributePresent(check, results, listOfValues);
                break;
            case IS_ATTRIBUTE_LANG_LOWERCASE:
                isLanguageAttributeLowercase(check, results, listOfValues);
                break;
            case IS_ATTRIBUTE_LANG_ALLOWED:
                isLanguageAttributeAllowed(check, results, listOfValues);
                break;
            default:
                LOGGER.warn("Unsupported " + check);
            }
        }
    }

    private void isLanguageAttributeAllowed(CheckDTO check, List<CheckResultDTO> results, List<? extends TLGeneric> generics) {
        if (CollectionUtils.isNotEmpty(generics)) {
            for (TLGeneric generic : generics) {
                if(generic.getLanguage() != null) {
                    addResult(check, generic.getId(), languageValidator.isAllowedLanguage(generic), results);
                }
            }
        }
    }

    private void isLanguageAttributeLowercase(CheckDTO check, List<CheckResultDTO> results, List<? extends TLGeneric> generics) {
        if (CollectionUtils.isNotEmpty(generics)) {
            for (TLGeneric generic : generics) {
                if(generic.getLanguage() != null) {
                    addResult(check, generic.getId(), languageValidator.isLanguageLowerCase(generic), results);
                }
            }
        }
    }

    private void isLanguageAttributePresent(CheckDTO check, List<CheckResultDTO> results, List<? extends TLGeneric> generics) {
        if (CollectionUtils.isNotEmpty(generics)) {
            for (TLGeneric generic : generics) {
                addResult(check, generic.getId(), languageValidator.isLanguagePresent(generic), results);
            }
        }
    }
}
