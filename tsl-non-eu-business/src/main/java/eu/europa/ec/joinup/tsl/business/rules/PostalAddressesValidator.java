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
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.dto.CheckDTO;
import eu.europa.ec.joinup.tsl.business.dto.CheckResultDTO;
import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLPostalAddress;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLSchemeInformation;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceProvider;
import eu.europa.ec.joinup.tsl.business.service.CountryService;
import eu.europa.ec.joinup.tsl.model.enums.CheckName;
import eu.europa.ec.joinup.tsl.model.enums.Tag;

@Service
public class PostalAddressesValidator extends AbstractCheckValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(PostalAddressesValidator.class);

    private static final Set<CheckName> SUPPORTED_CHECK_NAMES;

    static {
        SUPPORTED_CHECK_NAMES = new HashSet<CheckName>();

        SUPPORTED_CHECK_NAMES.add(CheckName.IS_POSTAL_ADDRESSES_STREET_NOT_EMPTY);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_POSTAL_ADDRESSES_LOCALITY_NOT_EMPTY);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_POSTAL_ADDRESSES_COUNTRY_NOT_EMPTY);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_POSTAL_ADDRESSES_COUNTRY_ALLOWED);
    }

    @Autowired
    private GenericValidator genericValidator;

    @Autowired
    private CountryService countryService;

    @Override
    public boolean isSupported(CheckDTO check) {
        return Tag.POSTAL_ADDRESSES.equals(check.getTarget()) && SUPPORTED_CHECK_NAMES.contains(check.getName());
    }

    @Override
    public List<CheckResultDTO> validate(List<CheckDTO> checks, TL tl) {
        List<CheckResultDTO> results = new ArrayList<CheckResultDTO>();

        TLSchemeInformation schemeInformation = tl.getSchemeInformation();
        results.addAll(validateSchemeInformation(checks, schemeInformation));
        List<TLServiceProvider> serviceProviders = tl.getServiceProviders();
        results.addAll(validateServiceProviders(checks, serviceProviders));

        return results;
    }
    
    public List<CheckResultDTO> validateSchemeInformation(List<CheckDTO> checks, TLSchemeInformation schemeInformation) {
        List<CheckResultDTO> results = new ArrayList<CheckResultDTO>();

        List<TLPostalAddress> postalAddresses = schemeInformation.getSchemeOpePostal();
        results.addAll(checkPostalAddresses(checks, schemeInformation.getId() + "_" + Tag.POSTAL_ADDRESSES, postalAddresses));
        return results;
    }
    
    private List<CheckResultDTO> validateServiceProviders(List<CheckDTO> checks, List<TLServiceProvider> serviceProviders) {
        List<CheckResultDTO> results = new ArrayList<CheckResultDTO>();

        if (CollectionUtils.isNotEmpty(serviceProviders)) {
            for (TLServiceProvider serviceProvider : serviceProviders) {
                results.addAll(validateServiceProvider(checks, serviceProvider));
            }
        }
        return results;
    }
    
    public List<CheckResultDTO> validateServiceProvider(List<CheckDTO> checks, TLServiceProvider serviceProvider) {
        List<CheckResultDTO> results = new ArrayList<CheckResultDTO>();
        results.addAll(checkPostalAddresses(checks, serviceProvider.getId() + "_" + Tag.POSTAL_ADDRESSES, serviceProvider.getTSPPostal()));
        return results;
    }

    private List<CheckResultDTO> checkPostalAddresses(List<CheckDTO> checks, String location, List<TLPostalAddress> postalAddresses) {
        List<CheckResultDTO> results = new ArrayList<>();
        for(CheckDTO check : checks) {
            switch (check.getName()) {
            case IS_POSTAL_ADDRESSES_STREET_NOT_EMPTY:
                isStreetsNotEmpty(check, results, postalAddresses);
                break;
            case IS_POSTAL_ADDRESSES_LOCALITY_NOT_EMPTY:
                isLocalityNotEmpty(check, results, postalAddresses);
                break;
            case IS_POSTAL_ADDRESSES_COUNTRY_NOT_EMPTY:
                isCountryNotEmpty(check, results, postalAddresses);
                break;
            case IS_POSTAL_ADDRESSES_COUNTRY_ALLOWED:
                isCountryAllowed(check, results, postalAddresses);
                break;
            default:
                LOGGER.warn("Unsupported " + check);
            }
        }
        return results;
    }

    private void isStreetsNotEmpty(CheckDTO check, List<CheckResultDTO> results, List<TLPostalAddress> addresses) {
        if (CollectionUtils.isNotEmpty(addresses)) {
            for (TLPostalAddress address : addresses) {
                addResult(check, address.getId(), genericValidator.isNotEmpty(address.getStreet()), results);
            }
        }
    }

    private void isLocalityNotEmpty(CheckDTO check, List<CheckResultDTO> results, List<TLPostalAddress> addresses) {
        if (CollectionUtils.isNotEmpty(addresses)) {
            for (TLPostalAddress address : addresses) {
                addResult(check, address.getId(), genericValidator.isNotEmpty(address.getLocality()), results);
            }
        }
    }

    private void isCountryNotEmpty(CheckDTO check, List<CheckResultDTO> results, List<TLPostalAddress> addresses) {
        if (CollectionUtils.isNotEmpty(addresses)) {
            for (TLPostalAddress address : addresses) {
                addResult(check, address.getId(), genericValidator.isNotEmpty(address.getCountryCode()), results);
            }
        }
    }

    private void isCountryAllowed(CheckDTO check, List<CheckResultDTO> results, List<TLPostalAddress> addresses) {
        if (CollectionUtils.isNotEmpty(addresses)) {
            for (TLPostalAddress address : addresses) {
                if (StringUtils.isNotEmpty(address.getCountryCode())) {
                    addResult(check, address.getId(), countryService.isExist(address.getCountryCode()), results);
                }

            }
        }
    }

}
