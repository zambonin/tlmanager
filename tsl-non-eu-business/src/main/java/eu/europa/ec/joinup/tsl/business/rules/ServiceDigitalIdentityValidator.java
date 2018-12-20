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
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.dto.CheckDTO;
import eu.europa.ec.joinup.tsl.business.dto.CheckResultDTO;
import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLCertificate;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLDigitalIdentification;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLPointersToOtherTSL;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceDto;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceHistory;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceProvider;
import eu.europa.ec.joinup.tsl.model.enums.CheckName;
import eu.europa.ec.joinup.tsl.model.enums.Tag;

@Service
public class ServiceDigitalIdentityValidator extends AbstractCheckValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceDigitalIdentityValidator.class);

    private static final Set<CheckName> SUPPORTED_CHECK_NAMES;

    static {
        SUPPORTED_CHECK_NAMES = new HashSet<>();

        SUPPORTED_CHECK_NAMES.add(CheckName.IS_VALID_CERTIFICATE);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_SUBJECT_NAME_MATCH);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_X509SKI_MATCH);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_CERTIFICATE_EXPIRED);
    }

    @Autowired
    private DigitalIdentityValidator digitalIdentityValidator;

    @Override
    public boolean isSupported(CheckDTO check) {
        return Tag.DIGITAL_IDENTITY.equals(check.getTarget()) && SUPPORTED_CHECK_NAMES.contains(check.getName());
    }

    @Override
    public List<CheckResultDTO> validate(List<CheckDTO> checks, TL tl) {
        List<CheckResultDTO> results = new ArrayList<>();

        List<TLPointersToOtherTSL> pointers = tl.getPointers();
        results.addAll(validatePointers(checks, pointers));

        List<TLServiceProvider> serviceProviders = tl.getServiceProviders();
        if (CollectionUtils.isNotEmpty(serviceProviders)) {
            for (TLServiceProvider serviceProvider : serviceProviders) {
                List<TLServiceDto> services = serviceProvider.getTSPServices();
                if (CollectionUtils.isNotEmpty(services)) {
                    for (TLServiceDto service : services) {
                        results.addAll(validateService(checks, service));
                    }
                }
            }
        }
        return results;
    }
    
    public List<CheckResultDTO> validatePointers(List<CheckDTO> checks, List<TLPointersToOtherTSL> pointers) {
        List<CheckResultDTO> results = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(pointers)) {
            for (TLPointersToOtherTSL pointer : pointers) {
                List<TLDigitalIdentification> serviceDigitalIds = pointer.getServiceDigitalId();
                if (CollectionUtils.isNotEmpty(serviceDigitalIds)) {
                    results.addAll(checkDigitalIdentifications(checks, serviceDigitalIds));
                }
            }
        }
        return results;
    }
    
    public List<CheckResultDTO> validateService(List<CheckDTO> checks, TLServiceDto service) {
        List<CheckResultDTO> results = new ArrayList<>();

        List<TLDigitalIdentification> serviceDigitalIds = service.getDigitalIdentification();
        if (CollectionUtils.isNotEmpty(serviceDigitalIds)) {
            results.addAll(checkDigitalIdentifications(checks, serviceDigitalIds));
        }
        List<TLServiceHistory> serviceHistories = service.getHistory();
        if (CollectionUtils.isNotEmpty(serviceHistories)) {
            for (TLServiceHistory serviceHistory : serviceHistories) {
                List<TLDigitalIdentification> historyDigitalIdentifications = serviceHistory.getDigitalIdentification();
                if (CollectionUtils.isNotEmpty(historyDigitalIdentifications)) {
                    results.addAll(checkDigitalIdentifications(checks, historyDigitalIdentifications));
                }
            }
        }
        return results;
    }

    private List<CheckResultDTO> checkDigitalIdentifications(List<CheckDTO> checks, List<TLDigitalIdentification> digitalIdentification) {
        List<CheckResultDTO> results = new ArrayList<>();
        
        for(CheckDTO check : checks) {
            try {
                switch (check.getName()) {
                case IS_VALID_CERTIFICATE:
                    checkAllEncodedCertificate(check, digitalIdentification, results);
                    break;
                case IS_SUBJECT_NAME_MATCH:
                    checkAllSubjectName(check, digitalIdentification, results);
                    break;
                case IS_X509SKI_MATCH:
                    checkAllX509SKI(check, digitalIdentification, results);
                    break;
                case IS_CERTIFICATE_EXPIRED:
                    checkAllExpiredDate(check, digitalIdentification, results);
                    break;
                default:
                    LOGGER.warn("Unsupported check " + check.getName());
                    break;
                }
            } catch (Exception e) {
                LOGGER.warn("Catch error : " + e.getMessage());
            }
        }
        return results;
    }

    private void checkAllEncodedCertificate(CheckDTO check, List<TLDigitalIdentification> digitalIdentification, List<CheckResultDTO> results) {
        for (TLDigitalIdentification identification : digitalIdentification) {
            if ((identification != null) && (identification.getCertificateList() != null)) {
                for (TLCertificate cert : identification.getCertificateList()) {
                    addResult(check, identification.getId() + "_" + Tag.X509_CERTIFICATE, digitalIdentityValidator.isBase64Certificate(cert.getCertEncoded()), results);
                }
            }
        }
    }

    private void checkAllSubjectName(CheckDTO check, List<TLDigitalIdentification> digitalIdentifications, List<CheckResultDTO> results) {
        for (TLDigitalIdentification identification : digitalIdentifications) {
            if ((identification != null) && CollectionUtils.isNotEmpty(identification.getCertificateList()) && (StringUtils.isNotEmpty(identification.getSubjectName()))) {
                for (TLCertificate cert : identification.getCertificateList()) {
                    if (cert.getToken() == null) {
                        cert.setTokenFromEncoded();
                    }
                    boolean result = digitalIdentityValidator.isCorrectX509SubjectName(identification.getSubjectName(), cert.getToken());
                    addResult(check, identification.getId() + "_" + Tag.X509_SUBJECT_NAME, result, results);
                }
            }
        }
    }

    private void checkAllX509SKI(CheckDTO check, List<TLDigitalIdentification> digitalIdentifications, List<CheckResultDTO> results) {
        for (TLDigitalIdentification identification : digitalIdentifications) {
            if ((identification != null) && CollectionUtils.isNotEmpty(identification.getCertificateList()) && (ArrayUtils.isNotEmpty(identification.getX509ski()))) {
                for (TLCertificate cert : identification.getCertificateList()) {
                    if (cert.getToken() == null) {
                        cert.setTokenFromEncoded();
                    }
                    boolean result = digitalIdentityValidator.isCorrectX509SKI(identification.getX509ski(), cert.getToken());
                    addResult(check, identification.getId() + "_" + Tag.X509_SKI, result, results);
                }
            }
        }
    }

    private void checkAllExpiredDate(CheckDTO check, List<TLDigitalIdentification> digitalIdentification, List<CheckResultDTO> results) {
        for (TLDigitalIdentification identification : digitalIdentification) {
            if ((identification != null) && CollectionUtils.isNotEmpty(identification.getCertificateList())) {
                for (TLCertificate certificate : identification.getCertificateList()) {
                    if (certificate.getToken() == null) {
                        certificate.setTokenFromEncoded();
                    }
                    boolean result = digitalIdentityValidator.isNotExpired(certificate);
                    addResult(check, certificate.getId(), result, results);
                }
            }
        }

    }

}
