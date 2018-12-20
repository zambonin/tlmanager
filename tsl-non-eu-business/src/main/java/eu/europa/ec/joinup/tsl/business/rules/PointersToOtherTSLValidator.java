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
import eu.europa.ec.joinup.tsl.business.dto.tl.TLDigitalIdentification;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLPointersToOtherTSL;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLSchemeTypeCommunityRule;
import eu.europa.ec.joinup.tsl.business.service.CountryService;
import eu.europa.ec.joinup.tsl.business.service.PropertiesService;
import eu.europa.ec.joinup.tsl.model.enums.CheckName;
import eu.europa.ec.joinup.tsl.model.enums.Tag;

@Service
public class PointersToOtherTSLValidator extends AbstractCheckValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(PointersToOtherTSLValidator.class);

    private static final Set<CheckName> SUPPORTED_CHECK_NAMES;

    static {
        SUPPORTED_CHECK_NAMES = new HashSet<>();

        SUPPORTED_CHECK_NAMES.add(CheckName.IS_LOCATION_PRESENT);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_OPERATOR_NAME_PRESENT);

        SUPPORTED_CHECK_NAMES.add(CheckName.IS_TERRITORY_PRESENT);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_TERRITORY_CORRECT);

        SUPPORTED_CHECK_NAMES.add(CheckName.IS_TL_TYPE_PRESENT);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_TL_TYPE_CORRECT);

        SUPPORTED_CHECK_NAMES.add(CheckName.IS_DIGITAL_IDENTITIES_PRESENT);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_DIGITAL_IDENTITIES_CORRECT);
        
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_COMMUNITY_RULES_PRESENT);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_COMMUNITY_RULES_CORRECT);
        
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_MIME_TYPE_PRESENT);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_MIME_TYPE_CORRECT);
    }

    @Autowired
    private GenericValidator genericValidator;
    
    @Autowired
    private PropertiesService propertiesService;
    
    @Autowired
    private CountryService countryService;

    @Override
    public boolean isSupported(CheckDTO check) {
        return Tag.POINTERS_TO_OTHER_TSL.equals(check.getTarget()) && SUPPORTED_CHECK_NAMES.contains(check.getName());
    }

    @Override
    public List<CheckResultDTO> validate(List<CheckDTO> checks, TL tl) {
        List<CheckResultDTO> results = new ArrayList<>();
        results.addAll(validatePointerList(tl.getTlId(), checks, tl.getPointers()));
        return results;
    }

    private List<CheckResultDTO> validatePointerList(int tlId, List<CheckDTO> checks, List<TLPointersToOtherTSL> pointers) {
        List<CheckResultDTO> results = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(pointers)) {
            for(TLPointersToOtherTSL pointer: pointers) {
                results.addAll(validatePointer(tlId, checks, pointer));
            }
        } 
        return results;
    }
    
    private List<CheckResultDTO> validatePointer(int tlId, List<CheckDTO> checks, TLPointersToOtherTSL pointer) {
        List<CheckResultDTO> results = new ArrayList<>();
        
        for(CheckDTO check : checks) {
            switch (check.getName()) {
            case IS_LOCATION_PRESENT:
                addResult(check, pointer.getId() + "_" + Tag.POINTER_LOCATION, genericValidator.isPresentAndNotEmpty(pointer.getTlLocation()), results);
                break;
                
            case IS_OPERATOR_NAME_PRESENT:
                addResult(check, pointer.getId() + "_" + Tag.SCHEME_OPERATOR_NAME, genericValidator.isCollectionPresentAndNotEmpty(pointer.getSchemeOpeName()), results);
                break;
                
            case IS_TERRITORY_PRESENT:
                addResult(check, pointer.getId() + "_" + Tag.POINTER_SCHEME_TERRITORY, genericValidator.isPresentAndNotEmpty(pointer.getSchemeTerritory()), results);
                break;
            case IS_TERRITORY_CORRECT:
                checkTerritory(check, pointer, results);
                break;
                
            case IS_TL_TYPE_PRESENT:
                addResult(check, pointer.getId() + "_" + Tag.POINTER_TYPE, genericValidator.isPresentAndNotEmpty(pointer.getTlType()), results);
                break;
            case IS_TL_TYPE_CORRECT:
                checkTlType(check, pointer, results);
                break;
                
            case IS_DIGITAL_IDENTITIES_PRESENT:
                addResult(check, pointer.getId() + "_" + Tag.SERVICE_DIGITAL_IDENTITY, genericValidator.isCollectionNotEmpty(pointer.getServiceDigitalId()), results);
                break;
            case IS_DIGITAL_IDENTITIES_CORRECT:
                checkServicesDigitalIdentitiesCorrect(check, pointer, results);
                break;
                
            case IS_COMMUNITY_RULES_PRESENT:
                addResult(check, pointer.getId() + "_" + Tag.POINTER_COMMUNITY_RULE, genericValidator.isCollectionPresentAndNotEmpty(pointer.getSchemeTypeCommunity()), results);
                break;
            case IS_COMMUNITY_RULES_CORRECT:
                checkCommunityRule(check, pointer, results);
                break;
                
            case IS_MIME_TYPE_PRESENT:
                checkMimeTypePresent(check, pointer, results);
                break;
            case IS_MIME_TYPE_CORRECT:
                checkMimeType(check, pointer, results);
                break;
                
            default:
                LOGGER.warn("Unsupported check " + check.getName());
                break;
            }
        }
        
        return results;
    }

    private void checkServicesDigitalIdentitiesCorrect(CheckDTO check, TLPointersToOtherTSL pointer, List<CheckResultDTO> results) {
        List<TLDigitalIdentification> serviceDigitalIds = pointer.getServiceDigitalId();
        if (CollectionUtils.isNotEmpty(serviceDigitalIds)) {
            for (TLDigitalIdentification digitalIdentification : serviceDigitalIds) {
                addResult(check, digitalIdentification.getId(), genericValidator.isCollectionNotEmpty(digitalIdentification.getCertificateList()), results);
            }
        }
    }
    
    private void checkTerritory(CheckDTO check, TLPointersToOtherTSL pointer, List<CheckResultDTO> results) {
        List<String> territories = countryService.getAllCountryCode();
        
        addResult(check, pointer.getId() + "_" + Tag.POINTER_SCHEME_TERRITORY, 
                genericValidator.isIn(pointer.getSchemeTerritory(), territories), results);
    }
    
    private void checkTlType(CheckDTO check, TLPointersToOtherTSL pointer, List<CheckResultDTO> results) {
        List<String> types = propertiesService.getTLTSLTypes();
        
        addResult(check, pointer.getId() + "_" + Tag.POINTER_TYPE, 
                genericValidator.isIn(pointer.getTlType(), types), results);
    }
    
    private void checkCommunityRule(CheckDTO check, TLPointersToOtherTSL pointer, List<CheckResultDTO> results) {
        List<String> commRulesValues = propertiesService.getTLSchemeCommunityRulesValues();
        
        List<TLSchemeTypeCommunityRule> commRules = pointer.getSchemeTypeCommunity();
        if(CollectionUtils.isNotEmpty(commRules)) {
            for(TLSchemeTypeCommunityRule commRule : commRules) {
                addResult(check, pointer.getId() + "_" + Tag.POINTER_COMMUNITY_RULE, 
                        genericValidator.isIn(commRule.getValue(), commRulesValues), results);
            }
        }
    }
    
    private void checkMimeTypePresent(CheckDTO check, TLPointersToOtherTSL pointer, List<CheckResultDTO> results) {
        if (pointer.getMimeType() == null) {
            addResult(check, pointer.getId() + "_" + Tag.POINTER_MIME_TYPE, false, results);
        }
    }
    
    private void checkMimeType(CheckDTO check, TLPointersToOtherTSL pointer, List<CheckResultDTO> results) {
        if (pointer.getMimeType() != null) {
            List<String> mimeTypes = propertiesService.getMimeTypes();
            
            addResult(check, pointer.getId() + "_" + Tag.POINTER_MIME_TYPE, 
                    genericValidator.isIn(pointer.getMimeType().toString(), mimeTypes), results);
        }
    }

}
