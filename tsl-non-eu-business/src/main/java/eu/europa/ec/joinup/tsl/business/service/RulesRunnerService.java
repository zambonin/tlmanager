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

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.dto.CheckDTO;
import eu.europa.ec.joinup.tsl.business.dto.CheckResultDTO;
import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLPointersToOtherTSL;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLSchemeInformation;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceDto;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceProvider;
import eu.europa.ec.joinup.tsl.business.rules.ServiceLanguageValidator;
import eu.europa.ec.joinup.tsl.business.rules.PointersToOtherTSLValidator;
import eu.europa.ec.joinup.tsl.business.rules.PostalAddressesValidator;
import eu.europa.ec.joinup.tsl.business.rules.SchemeInformationValidator;
import eu.europa.ec.joinup.tsl.business.rules.ServiceDigitalIdentityValidator;
import eu.europa.ec.joinup.tsl.business.rules.TSPServiceProviderValidator;
import eu.europa.ec.joinup.tsl.business.rules.TSPServiceValidator;
import eu.europa.ec.joinup.tsl.business.rules.ServiceURIValidator;
import eu.europa.ec.joinup.tsl.business.util.LocationUtils;
import eu.europa.ec.joinup.tsl.model.enums.Tag;

/**
 * Manage rules execution on all the part of the trusted list (all, scheme information, pointer, tsp, service, history, signature)
 */
@Service
@Transactional(value = TxType.REQUIRED)
public class RulesRunnerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RulesRunnerService.class);
    
    @Autowired
    private CheckService checkService;

    @Autowired
    private TLService tlService;

    @Autowired
    private CheckResultPersistenceService persistenceService;
    
    @Autowired
    private SchemeInformationValidator schemeInformationValidator;
    
    @Autowired
    private PointersToOtherTSLValidator pointerValidator;
    
    @Autowired
    private TSPServiceValidator serviceValidator;
    
    @Autowired
    private TSPServiceProviderValidator serviceProviderValidator;
    
    @Autowired
    private ServiceLanguageValidator languageValidator;
    
    @Autowired 
    private ServiceURIValidator uriValidator;
    
    @Autowired 
    private PostalAddressesValidator postalAddressesValidator;
    
    @Autowired 
    private ServiceDigitalIdentityValidator digitalValidator;

    /**
     * Run All rules
     *
     * @param tl
     */
    public void runAllRules(final TL tl) {
        if (tl != null) {
            List<CheckResultDTO> checkResults = new ArrayList<>();
            List<CheckDTO> checks = new ArrayList<>();
            
            checks = checkService.getTarget(Tag.SCHEME_INFORMATION);
            checkResults.addAll(schemeInformationValidator.validate(checks, tl));
            
            checks = checkService.getTarget(Tag.POINTERS_TO_OTHER_TSL);
            checkResults.addAll(pointerValidator.validate(checks, tl));
            
            checks = checkService.getTarget(Tag.TSP_SERVICE_PROVIDER);
            checkResults.addAll(serviceProviderValidator.validate(checks, tl));
            
            checks = checkService.getTarget(Tag.TSP_SERVICE);
            checkResults.addAll(serviceValidator.validate(checks, tl));
            
            checks = checkService.getTarget(Tag.LANGUAGE_FIELDS);
            checkResults.addAll(languageValidator.validate(checks, tl));
            
            checks = checkService.getTarget(Tag.URI_FIELDS);
            checkResults.addAll(uriValidator.validate(checks, tl));
            
            checks = checkService.getTarget(Tag.POSTAL_ADDRESSES);
            checkResults.addAll(postalAddressesValidator.validate(checks, tl));
            
            checks = checkService.getTarget(Tag.DIGITAL_IDENTITY);
            checkResults.addAll(digitalValidator.validate(checks, tl));
            
            finalizeCheckResults(tl, checkResults);
        }
    }

    /**
     * Clean and run checks for a TL Scheme Information
     *
     * @param tlId
     */
    public void validateSchemeInformation(int tlId) {
        Tag target = Tag.SCHEME_INFORMATION;
        TL tl = tlService.getTL(tlId);
        TLSchemeInformation schemeInformation = tl.getSchemeInformation();
        
        // Clean
        checkService.cleanResultByLocation(tlId + "_" + target);
        
        // Run checks
        List<CheckResultDTO> checkResults = new ArrayList<>();
        List<CheckDTO> checks = new ArrayList<>();
        
        checks = checkService.getTarget(Tag.SCHEME_INFORMATION);
        checkResults.addAll(schemeInformationValidator.validate(checks, tl));
        
        checks = checkService.getTarget(Tag.LANGUAGE_FIELDS);
        checkResults.addAll(languageValidator.validateSchemeInformation(checks, schemeInformation));
        
        checks = checkService.getTarget(Tag.URI_FIELDS);
        checkResults.addAll(uriValidator.validateSchemeInformation(checks, schemeInformation));
        
        checks = checkService.getTarget(Tag.POSTAL_ADDRESSES);
        checkResults.addAll(postalAddressesValidator.validateSchemeInformation(checks, schemeInformation));
        
        finalizeCheckResults(tl, checkResults);
    }

    /**
     * Clean and run checks for all PTOTSL
     *
     * @param tlId
     */
    public void validateAllPointers(int tlId) {
        Tag target = Tag.POINTERS_TO_OTHER_TSL;
        TL tl = tlService.getTL(tlId);
        List<TLPointersToOtherTSL> pointers = tl.getPointers();
        
        // Clean
        checkService.cleanResultByLocation(tlId + "_" + target);
        
        // Run checks
        List<CheckResultDTO> checkResults = new ArrayList<>();
        List<CheckDTO> checks = new ArrayList<>();
        
        checks = checkService.getTarget(Tag.POINTERS_TO_OTHER_TSL);
        checkResults.addAll(pointerValidator.validate(checks, tl));
        
        checks = checkService.getTarget(Tag.LANGUAGE_FIELDS);
        checkResults.addAll(languageValidator.validatePointers(checks, pointers));
        
        checks = checkService.getTarget(Tag.URI_FIELDS);
        checkResults.addAll(uriValidator.validatePointers(checks, pointers));
        
        checks = checkService.getTarget(Tag.DIGITAL_IDENTITY);
        checkResults.addAll(digitalValidator.validatePointers(checks, pointers));
        
        finalizeCheckResults(tl, checkResults);
    }

    /**
     * Clean and run checks for all TSP
     *
     * @param tlId
     * @param serviceProviders
     */
    public void validateAllServiceProvider(int tlId, List<TLServiceProvider> serviceProviders) {
        checkService.cleanResultByLocation(tlId + "_" + Tag.TSP_SERVICE_PROVIDER);
        if (!CollectionUtils.isEmpty(serviceProviders)) {
            // Index start at 1
            for (int index = 1; index < (serviceProviders.size() + 1); index++) {
                validateServiceProvider(tlId, index, true);
            }
        }
    }

    /**
     * Clean and run checks if not already cleaned for TSP
     *
     * @param tlId
     * @param tspIndex
     * @param resultClean
     *            : true when called through validateAllServiceProvider. Rules are cleaned in parent method
     */
    public void validateServiceProvider(int tlId, int tspIndex, boolean resultClean) {
        Tag target = Tag.TSP_SERVICE_PROVIDER;
        if (!resultClean) {
            checkService.cleanResultByLocation(tlId + "_" + target + "_" + tspIndex);
        }
        
        // Run checks
        List<CheckResultDTO> checkResults = new ArrayList<>();
        List<CheckDTO> checks = new ArrayList<>();
        
        TL tl = tlService.getTL(tlId);
        if(tl != null) {
            List<TLServiceProvider> tsps = tl.getServiceProviders();
            if(CollectionUtils.isNotEmpty(tsps)) {
                TLServiceProvider tsp = tsps.get(tspIndex-1);
                if(tsp != null) {
                    
                    checks = checkService.getTarget(target);
                    checkResults.addAll(serviceProviderValidator.validateServiceProvider(checks, tsp));
                    
                    checks = checkService.getTarget(Tag.LANGUAGE_FIELDS);
                    checkResults.addAll(languageValidator.validateServiceProvider(checks, tsp));
                    
                    checks = checkService.getTarget(Tag.URI_FIELDS);
                    checkResults.addAll(uriValidator.validateServiceProvider(checks, tsp));
                    
                    checks = checkService.getTarget(Tag.POSTAL_ADDRESSES);
                    checkResults.addAll(postalAddressesValidator.validateServiceProvider(checks, tsp));
                    
                    finalizeCheckResults(tl, checkResults);
                    return;
                }
            }
        }
        LOGGER.warn("Service " + tlId + " > " + tspIndex + " doesn't exist");
    }
    
    /**
     * Clean and run checks for a particular service
     * 
     * @param tlId
     * @param tspIndex
     * @param serviceIndex
     */
    public void validateService(int tlId, int tspIndex, int serviceIndex) {
        Tag target = Tag.TSP_SERVICE;
        checkService.cleanResultByLocation(tlId + "_" + Tag.TSP_SERVICE_PROVIDER + "_" + tspIndex + "_" + target + "_" + serviceIndex);
        
        // Run checks
        List<CheckResultDTO> checkResults = new ArrayList<>();
        List<CheckDTO> checks = new ArrayList<>();
        
        TL tl = tlService.getTL(tlId);
        if(tl != null) {
            List<TLServiceProvider> tsps = tl.getServiceProviders();
            if(CollectionUtils.isNotEmpty(tsps)) {
                TLServiceProvider tsp = tsps.get(tspIndex-1);
                if(tsp != null) {
                    List<TLServiceDto> services = tsp.getTSPServices();
                    if(CollectionUtils.isNotEmpty(services)) {
                        TLServiceDto service = services.get(serviceIndex-1);
                        if(service != null) {
                            
                            checks = checkService.getTarget(target);
                            checkResults.addAll(serviceValidator.validateService(service, tsp, checks));
                            
                            // There exists some Scheme Information checks related to Services
                            checks = checkService.getTarget(Tag.SCHEME_INFORMATION);
                            checkResults.addAll(schemeInformationValidator.validate(checks, tl));
                            
                            checks = checkService.getTarget(Tag.LANGUAGE_FIELDS);
                            checkResults.addAll(languageValidator.validateService(checks, service));
                            
                            checks = checkService.getTarget(Tag.URI_FIELDS);
                            checkResults.addAll(uriValidator.validateService(checks, service));
                            
                            checks = checkService.getTarget(Tag.DIGITAL_IDENTITY);
                            checkResults.addAll(digitalValidator.validateService(checks, service));
                            
                            finalizeCheckResults(tl, checkResults);
                            return;
                        }
                    }
                }
            }
        }
        LOGGER.warn("Service " + tlId + " > " + tspIndex + " > " + serviceIndex + " doesn't exist");
        
    }

    public void validateSignature(int tlId) {
        checkService.cleanResultByLocation(tlId + "_" + Tag.SIGNATURE);
        TL draft = tlService.getTL(tlId);
        runAllRules(draft);
    }

    /**
     * Run all checks after signature performed on draft
     *
     * @param tlId
     */
    public void validDraftAfterSignature(int tlId) {
        checkService.cleanResultByLocation(tlId + "_");
        TL draft = tlService.getTL(tlId);
        runAllRules(draft);
    }
    
    /**
     * Add location to check and persist
     * 
     * @param tl
     * @param results
     */
    public void finalizeCheckResults(TL tl, List<CheckResultDTO> results) {
        for (CheckResultDTO result : results) {
            result.setLocation(LocationUtils.idUserReadable(tl, result.getId()));
        }
        persistenceService.persistAllResults(tl.getTlId(), results);
    }

}
