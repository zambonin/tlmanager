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

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.dto.CheckDTO;
import eu.europa.ec.joinup.tsl.business.dto.CheckResultDTO;
import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLName;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLSchemeInformation;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLSchemeTypeCommunityRule;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceDto;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceProvider;
import eu.europa.ec.joinup.tsl.business.service.PropertiesService;
import eu.europa.ec.joinup.tsl.model.enums.CheckName;
import eu.europa.ec.joinup.tsl.model.enums.Tag;

/**
 * Verify if values in a TL are present in the data properties
 */
@Service
public class SchemeInformationValidator extends AbstractCheckValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(SchemeInformationValidator.class);
    
    private static final Set<CheckName> SUPPORTED_CHECK_NAMES;
    
    static {
        SUPPORTED_CHECK_NAMES = new HashSet<>();
        
        // PRESENCE
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_TSL_TAG_PRESENT);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_TL_TYPE_PRESENT);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_OPERATOR_NAME_PRESENT);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_POSTAL_ADDRESS_PRESENT);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_ELECTRONIC_ADDRESS_PRESENT);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_SCHEME_NAME_PRESENT);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_INFORMATION_URI_PRESENT);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_STATUS_DETERMINATION_PRESENT);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_COMMUNITY_RULES_PRESENT);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_ISSUE_DATE_PRESENT);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_NEXT_UPDATE_PRESENT);
        
        // CORRECTNESS
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_TSL_TAG_CORRECT);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_SEQUENCE_NUMBER_GREATER_THAN_ZERO);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_TL_TYPE_CORRECT);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_ISSUE_DATE_IN_THE_PAST);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_NEXT_UPDATE_IN_THE_FUTURE);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_NEXT_UPDATE_MAX_6_MONTHS_AFTER_ISSUE_DATE);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_HISTORICAL_INFORMATION_PERIOD_CORRECT);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_SCHEME_NAME_START_WITH_COUNTRY_CODE);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_STATUS_DETERMINATION_CORRECT);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_COMMUNITY_RULES_CORRECT);
    }
    
    @Autowired
    private PropertiesService propertiesService;
    
    @Autowired
    private GenericValidator genericValidator;
    
    @Autowired
    private DateValidator dateValidator;
    
    @Override
    public boolean isSupported(CheckDTO check) {
        return check.getTarget().equals(Tag.SCHEME_INFORMATION) && SUPPORTED_CHECK_NAMES.contains(check.getName());
    }

    @Override
    public List<CheckResultDTO> validate(List<CheckDTO> checks, TL tl) {
        return validateSchemeInformation(tl, checks);
    }
    
    private List<CheckResultDTO> validateSchemeInformation(TL tl, List<CheckDTO> checks) {
        List<CheckResultDTO> results = new ArrayList<>();
        TLSchemeInformation schemeInformation = tl.getSchemeInformation();
        
        for(CheckDTO check : checks) {
            switch (check.getName()) {
            
            // PRESENCE CHECKS
            case IS_TSL_TAG_PRESENT:
                addResult(check, tl.getSchemeInformation().getId() + "_" + Tag.TSL_TAG, genericValidator.isPresentAndNotEmpty(tl.getTslTag()), results);
                break;
            case IS_TL_TYPE_PRESENT:
                addResult(check, schemeInformation.getId() + "_" + Tag.TSL_TYPE, genericValidator.isPresentAndNotEmpty(schemeInformation.getType()), results);
                break;
            case IS_OPERATOR_NAME_PRESENT:
                addResult(check, schemeInformation.getId() + "_" + Tag.SCHEME_OPERATOR_NAME, genericValidator.isCollectionPresentAndNotEmpty(schemeInformation.getSchemeOpeName()), results);
                break;
            case IS_POSTAL_ADDRESS_PRESENT:
                addResult(check, schemeInformation.getId() + "_" + Tag.POSTAL_ADDRESSES, genericValidator.isCollectionNotEmpty(schemeInformation.getSchemeOpePostal()), results);
                break;
            case IS_ELECTRONIC_ADDRESS_PRESENT:
                addResult(check, schemeInformation.getId() + "_" + Tag.ELECTRONIC_ADDRESS, genericValidator.isCollectionPresentAndNotEmpty(schemeInformation.getSchemeOpeElectronic()), results);
                break;
            case IS_SCHEME_NAME_PRESENT:
                addResult(check, schemeInformation.getId() + "_" + Tag.SCHEME_NAME, genericValidator.isCollectionPresentAndNotEmpty(schemeInformation.getSchemeName()), results);
                break;
            case IS_INFORMATION_URI_PRESENT:
                addResult(check, schemeInformation.getId() + "_" + Tag.SCHEME_INFORMATION_URI, genericValidator.isCollectionPresentAndNotEmpty(schemeInformation.getSchemeInfoUri()), results);
                break;
            case IS_STATUS_DETERMINATION_PRESENT:
                addResult(check, schemeInformation.getId() + "_" + Tag.STATUS_DETERMINATION, genericValidator.isPresentAndNotEmpty(schemeInformation.getStatusDetermination()), results);
                break;
            case IS_COMMUNITY_RULES_PRESENT:
                addResult(check, schemeInformation.getId() + "_" + Tag.SCHEME_TYPE_COMMUNITY_RULES, genericValidator.isCollectionPresentAndNotEmpty(schemeInformation.getSchemeTypeCommRule()), results);
                break;
            case IS_ISSUE_DATE_PRESENT:
                addResult(check, schemeInformation.getId() + "_" + Tag.ISSUE_DATE, genericValidator.isPresent(schemeInformation.getIssueDate()), results);
                break;
            case IS_NEXT_UPDATE_PRESENT:
                addResult(check, schemeInformation.getId() + "_" + Tag.NEXT_UPDATE, !isNextUpdateShouldBePresent(tl) || genericValidator.isPresent(schemeInformation.getNextUpdateDate()), results);
                break;
                
                // CORRECTNESS
            case IS_TSL_TAG_CORRECT:
                checkTslTag(check, tl, results);
                break;
            case IS_SEQUENCE_NUMBER_GREATER_THAN_ZERO:
                addResult(check, schemeInformation.getId() + "_" + Tag.SEQUENCE_NUMBER, genericValidator.isGreaterThanZero(schemeInformation.getSequenceNumber()), results);
                break;
            case IS_TL_TYPE_CORRECT:
                checkTlType(check, tl, results);
                break;
            case IS_ISSUE_DATE_IN_THE_PAST:
                addResult(check, schemeInformation.getId() + "_" + Tag.ISSUE_DATE, dateValidator.isDateInThePast(schemeInformation.getIssueDate()), results);
                break;
            case IS_NEXT_UPDATE_IN_THE_FUTURE:
                addResult(check, schemeInformation.getId() + "_" + Tag.NEXT_UPDATE, dateValidator.isDateInTheFuture(schemeInformation.getNextUpdateDate()), results);
                break;
            case IS_NEXT_UPDATE_MAX_6_MONTHS_AFTER_ISSUE_DATE:
                addResult(check, schemeInformation.getId() + "_" + Tag.NEXT_UPDATE,
                        dateValidator.isDateDifferenceOfMax6Months(schemeInformation.getIssueDate(), schemeInformation.getNextUpdateDate()), results);
                break;
            case IS_HISTORICAL_INFORMATION_PERIOD_CORRECT:
                addResult(check, schemeInformation.getId() + "_" + Tag.HISTORICAL_PERIOD, isCorrectHistoryInformationPeriod(schemeInformation.getHistoricalPeriod()), results);
                break;
            case IS_SCHEME_NAME_START_WITH_COUNTRY_CODE:
                isSchemeNamesStartWithCountryCode(check, tl, results);
                break;
            case IS_STATUS_DETERMINATION_CORRECT:
                checkStatusDetermination(check, tl, results);
                break;
            case IS_COMMUNITY_RULES_CORRECT:
                checkCommunityRule(check, tl, results);
                break;
                
            default:
                LOGGER.warn("Unsupported check " + check.getName());
                break;
            }
        }
        return results;
    }
    
    private void checkTslTag(CheckDTO check, TL tl, List<CheckResultDTO> results) {
        List<String> tags = propertiesService.getTSLTags();
        
        addResult(check, tl.getSchemeInformation().getId() + "_" + Tag.TSL_TAG, 
                genericValidator.isIn(tl.getTslTag(), tags), results);
    }
    
    private void checkTlType(CheckDTO check, TL tl, List<CheckResultDTO> results) {
        List<String> types = propertiesService.getTLTSLTypes();
        
        addResult(check, tl.getSchemeInformation().getId() + "_" + Tag.TSL_TYPE, 
                genericValidator.isIn(tl.getSchemeInformation().getType(), types), results);
    }
    
    private void checkStatusDetermination(CheckDTO check, TL tl, List<CheckResultDTO> results) {
        List<String> statusDetermination = propertiesService.getTLStatusDeterminationApproachValue();
        
        addResult(check, tl.getSchemeInformation().getId() + "_" + Tag.STATUS_DETERMINATION, 
                genericValidator.isIn(tl.getSchemeInformation().getStatusDetermination(), statusDetermination), results);
    }
    
    private void checkCommunityRule(CheckDTO check, TL tl, List<CheckResultDTO> results) {
        List<String> commRulesValues = propertiesService.getTLSchemeCommunityRulesValues();
        
        List<TLSchemeTypeCommunityRule> commRules = tl.getSchemeInformation().getSchemeTypeCommRule();
        if(CollectionUtils.isNotEmpty(commRules)) {
            for(TLSchemeTypeCommunityRule commRule : commRules) {
                addResult(check, tl.getSchemeInformation().getId() + "_" + Tag.SCHEME_TYPE_COMMUNITY_RULES, 
                        genericValidator.isIn(commRule.getValue(), commRulesValues), results);
            }
        }
    }
    
    private void isSchemeNamesStartWithCountryCode(CheckDTO check, TL tl, List<CheckResultDTO> results) {
        List<TLName> schemeNames = tl.getSchemeInformation().getSchemeName();
        String territory = tl.getSchemeInformation().getTerritory();
        
        if (CollectionUtils.isNotEmpty(schemeNames)) {
            String expectedStart = StringUtils.upperCase(territory) + ":";
            for (TLName schemeName : schemeNames) {
                addResult(check, schemeName.getId(), StringUtils.startsWith(schemeName.getValue(), expectedStart), results);
            }
        }
    }
    
    private boolean isCorrectHistoryInformationPeriod(Integer historicalPeriod) {
        Integer expectedHistoryInformationPeriod = 65535;
        return historicalPeriod >= 0 && historicalPeriod <= expectedHistoryInformationPeriod;
    }
    
    private boolean isNextUpdateShouldBePresent(TL tl) {
        List<TLServiceProvider> serviceProviders = tl.getServiceProviders();
        if(CollectionUtils.isNotEmpty(serviceProviders)) {
            for(TLServiceProvider serviceProvider : serviceProviders) {
                List<TLServiceDto> services = serviceProvider.getTSPServices();
                if(CollectionUtils.isNotEmpty(services)) {
                    for(TLServiceDto service : services) {
                        String status = service.getCurrentStatus();
                        if(!status.contains("withdrawn") && !status.contains("deprecatedbynationallaw") && !status.contains("deprecatedatnationallevel")) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

}
