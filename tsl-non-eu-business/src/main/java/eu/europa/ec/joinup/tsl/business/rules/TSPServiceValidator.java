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
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.security.auth.x500.X500Principal;

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
import eu.europa.ec.joinup.tsl.business.dto.tl.TLAdditionnalServiceInfo;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLCertificate;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLDigitalIdentification;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLName;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLQualificationExtension;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceDto;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceExtension;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceHistory;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceProvider;
import eu.europa.ec.joinup.tsl.business.service.CertificateService;
import eu.europa.ec.joinup.tsl.business.service.PropertiesService;
import eu.europa.ec.joinup.tsl.business.util.TLUtils;
import eu.europa.ec.joinup.tsl.model.enums.CheckName;
import eu.europa.ec.joinup.tsl.model.enums.Tag;
import eu.europa.esig.dss.DSSUtils;
import eu.europa.esig.dss.x509.CertificateToken;

/**
 * Verify if values in a TL are present in the data properties
 */
@Service
public class TSPServiceValidator extends AbstractCheckValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(TSPServiceValidator.class);
    
    private static final Set<CheckName> SUPPORTED_CHECK_NAMES;
    
    static {
        SUPPORTED_CHECK_NAMES = new HashSet<>();
        
        // PRESENCE
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_SERVICE_TYPE_ID_PRESENT);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_SERVICE_STATUS_PRESENT);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_SERVICE_STATUS_STARTING_TIME_PRESENT);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_SERVICE_NAME_PRESENT);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_DIGITAL_IDENTITIES_PRESENT);
        
        // CORRECTNESS
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_SERVICE_TYPE_ID_CORRECT);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_SERVICE_STATUS_CORRECT);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_DIGITAL_IDENTITIES_CORRECT);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_SERVICE_STATUS_STARTING_TIME_ORDER);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_SERVICE_AND_HISTORY_HAVE_SAME_TYPE_IDENTIFIER);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_SERVICE_AND_HISTORY_HAVE_SAME_SUBJECT_NAME);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_SERVICE_AND_HISTORY_HAVE_SAME_X509SKI);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_SERVICE_DEFINITION_URI_PRESENT_FOR_NATIONAL_ROOT_CA_QC);
        
        // EXTENSIONS
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_QUALIFIER_URI_CORRECT_VALUE);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_SERVICE_EXTENSION_ASI_ALLOWED);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_SERVICE_EXTENSION_ASI_SIG_SEAL_ALLOWED);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_QUALIFICATION_EXTENSION_ALLOWED);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_EXPIRED_CERT_REVOC_INFO_EXTENSION_ALLOWED);
    }
    
    @Autowired
    private PropertiesService propertiesService;
    
    @Autowired
    private GenericValidator genericValidator;
    
    @Autowired
    private CertificateService certificateService;
    
    @Override
    public boolean isSupported(CheckDTO check) {
        return check.getTarget().equals(Tag.TSP_SERVICE) && SUPPORTED_CHECK_NAMES.contains(check.getName());
    }

    @Override
    public List<CheckResultDTO> validate(List<CheckDTO> checks, TL tl) {
        List<CheckResultDTO> results = new ArrayList<>();
        
        List<TLServiceProvider> serviceProviders = tl.getServiceProviders();
        if(CollectionUtils.isNotEmpty(serviceProviders)) {
            for(TLServiceProvider serviceProvider : serviceProviders) {
                List<TLServiceDto> services = serviceProvider.getTSPServices();
                if(CollectionUtils.isNotEmpty(services)) {
                    for(TLServiceDto service : services) {
                        results.addAll(validateService(service, serviceProvider, checks));
                    }
                }
            }
        }
        return results;
    }
    
    public List<CheckResultDTO> validateService(TLServiceDto service, TLServiceProvider parent, List<CheckDTO> checks) {
        List<CheckResultDTO> results = new ArrayList<>();
        
        for(CheckDTO check : checks) {
            try {
                switch (check.getName()) {
                
                case IS_SERVICE_TYPE_ID_PRESENT:
                    checkTypeIdPresent(check, service, results);
                    break;
                case IS_SERVICE_STATUS_PRESENT:
                    checkStatusPresent(check, service, results);
                    break;
                case IS_SERVICE_STATUS_STARTING_TIME_PRESENT:
                    checkStatusStartingTimePresent(check, service, results);
                    break;
                case IS_SERVICE_NAME_PRESENT:
                    checkNamePresent(check, service, results);
                    break;
                case IS_DIGITAL_IDENTITIES_PRESENT:
                    checkDigitalIdentitiesPresent(check, service, results);
                    break;
                    
                case IS_SERVICE_TYPE_ID_CORRECT:
                    checkTypeIdCorrect(check, service, results);
                    break;
                case IS_SERVICE_STATUS_CORRECT:
                    checkStatusCorrect(check, service, results);
                    break;
                case IS_SERVICE_STATUS_STARTING_TIME_ORDER:
                    checkStatusStartingTimeOrder(check, service, results);
                    break;
                case IS_SERVICE_AND_HISTORY_HAVE_SAME_TYPE_IDENTIFIER:
                    checkServiceAndHistoryTypeIdentifier(check, service, results);
                    break;
                case IS_SERVICE_AND_HISTORY_HAVE_SAME_SUBJECT_NAME:
                    checkServiceAndHistorySubjectNames(check, service, results);
                    break;
                case IS_SERVICE_AND_HISTORY_HAVE_SAME_X509SKI:
                    checkServiceAndHistoryX509SKI(check, service, results);
                    break;
                case IS_X509CERTIFICATE_ORGANIZATION_MATCH:
                    checkAllDigitalIdsOrganizationMatch(check, service, parent, results);
                    break;
                case IS_SERVICE_DEFINITION_URI_PRESENT_FOR_NATIONAL_ROOT_CA_QC:
                    checkTSPServiceDefinitionURIPresent(check, service, results);
                    break;
                case IS_QUALIFIER_URI_CORRECT_VALUE:
                    checkAllQualifiersCorrectValue(check, service, results);
                    break;
                case IS_SERVICE_EXTENSION_ASI_ALLOWED:
                    checkAllAsiExtensionVersusServiceType(check, service, results);
                    break;
                case IS_SERVICE_EXTENSION_ASI_SIG_SEAL_ALLOWED:
                    checkAllAsiSigSealExtensionVersusServiceType(check, service, results);
                    break;
                case IS_QUALIFICATION_EXTENSION_ALLOWED:
                    checkQualificationExtensionVersusServiceType(check, service, results);
                    break;
                case IS_EXPIRED_CERT_REVOC_INFO_EXTENSION_ALLOWED:
                    checkCertRevocInfoExtensionVersusServiceType(check, service, results);
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
    
    private void checkNamePresent(CheckDTO check, TLServiceDto service, List<CheckResultDTO> results) {
        addResult(check, service.getId() + "_" + Tag.SERVICE_NAME, genericValidator.isCollectionPresentAndNotEmpty(service.getServiceName()), results);
        List<TLServiceHistory> hists = service.getHistory();
        if(CollectionUtils.isNotEmpty(hists)) {
            for(TLServiceHistory hist : hists) {
                addResult(check, hist.getId() + "_" + Tag.SERVICE_NAME, genericValidator.isCollectionPresentAndNotEmpty(hist.getServiceName()), results);
            }
        }
    }

    private void checkDigitalIdentitiesPresent(CheckDTO check, TLServiceDto service, List<CheckResultDTO> results) {
        // Services
        addResult(check, service.getId() + "_" + Tag.SERVICE_DIGITAL_IDENTITY, genericValidator.isCollectionNotEmpty(service.getDigitalIdentification()), results);
        
        // History
        List<TLServiceHistory> serviceHistories = service.getHistory();
        if (CollectionUtils.isNotEmpty(serviceHistories)) {
            for (TLServiceHistory serviceHistory : serviceHistories) {
                addResult(check, serviceHistory.getId() + "_" + Tag.SERVICE_DIGITAL_IDENTITY,
                        genericValidator.isCollectionNotEmpty(serviceHistory.getDigitalIdentification()), results);
            }
        }
    }

    private void checkStatusStartingTimePresent(CheckDTO check, TLServiceDto service, List<CheckResultDTO> results) {
        addResult(check, service.getId() + "_" + Tag.SERVICE_STATUS_STARTING_TIME, service.getCurrentStatusStartingDate() != null, results);
        List<TLServiceHistory> hists = service.getHistory();
        if(CollectionUtils.isNotEmpty(hists)) {
            for(TLServiceHistory hist : hists) {
                addResult(check, hist.getId() + "_" + Tag.SERVICE_STATUS_STARTING_TIME, hist.getCurrentStatusStartingDate() != null, results);
            }
        }
    }

    private void checkTypeIdPresent(CheckDTO check, TLServiceDto service, List<CheckResultDTO> results) {
        addResult(check, service.getId() + "_" + Tag.SERVICE_TYPE_IDENTIFIER, genericValidator.isPresentAndNotEmpty(service.getTypeIdentifier()), results);
        List<TLServiceHistory> hists = service.getHistory();
        if(CollectionUtils.isNotEmpty(hists)) {
            for(TLServiceHistory hist : hists) {
                addResult(check, hist.getId() + "_" + Tag.SERVICE_TYPE_IDENTIFIER, genericValidator.isPresentAndNotEmpty(hist.getTypeIdentifier()), results);
            }
        }
    }
    
    private void checkTypeIdCorrect(CheckDTO check, TLServiceDto service, List<CheckResultDTO> results) {
        List<String> typeIds = propertiesService.getServiceTypeIdentifiers();

        addResult(check, service.getId() + "_" + Tag.SERVICE_TYPE_IDENTIFIER, 
                genericValidator.isIn(service.getTypeIdentifier(), typeIds), results);
        List<TLServiceHistory> hists = service.getHistory();
        if(CollectionUtils.isNotEmpty(hists)) {
            for(TLServiceHistory hist : hists) {
                addResult(check, hist.getId() + "_" + Tag.SERVICE_TYPE_IDENTIFIER, 
                        genericValidator.isIn(hist.getTypeIdentifier(), typeIds), results);
            }
        }
    }
    
    private void checkStatusPresent(CheckDTO check, TLServiceDto service, List<CheckResultDTO> results) {
        addResult(check, service.getId() + "_" + Tag.SERVICE_STATUS, genericValidator.isPresentAndNotEmpty(service.getCurrentStatus()), results);
        
        List<TLServiceHistory> hists = service.getHistory();
        if(CollectionUtils.isNotEmpty(hists)) {
            if(service.getHistory() != null) {
                for(TLServiceHistory hist : service.getHistory()) {
                    addResult(check, hist.getId() + "_" + Tag.SERVICE_STATUS, genericValidator.isPresentAndNotEmpty(hist.getCurrentStatus()), results);
                }
            }
        }
    }
    
    private void checkStatusCorrect(CheckDTO check, TLServiceDto service, List<CheckResultDTO> results) {
        List<String> serviceStatus = propertiesService.getServiceStatus();
        List<String> servicePreviousStatus = propertiesService.getServicePreviousStatus();
        servicePreviousStatus.addAll(serviceStatus);
        
        addResult(check, service.getId() + "_" + Tag.SERVICE_STATUS, 
                genericValidator.isIn(service.getCurrentStatus(), serviceStatus), results);
        
        List<TLServiceHistory> hists = service.getHistory();
        if(CollectionUtils.isNotEmpty(hists)) {
            if(service.getHistory() != null) {
                for(TLServiceHistory hist : service.getHistory()) {
                    addResult(check, hist.getId() + "_" + Tag.SERVICE_STATUS, 
                            genericValidator.isIn(hist.getCurrentStatus(), servicePreviousStatus), results);
                }
            }
        }
    }
    
    private void checkStatusStartingTimeOrder(CheckDTO check, TLServiceDto service, List<CheckResultDTO> results) {
        List<TLServiceHistory> serviceHistories = service.getHistory();
        if (CollectionUtils.isNotEmpty(serviceHistories)) {
            Date after = service.getCurrentStatusStartingDate();
            for (TLServiceHistory serviceHistory : serviceHistories) {
                Date before = serviceHistory.getCurrentStatusStartingDate();
                addResult(check, serviceHistory.getId() + "_" + Tag.SERVICE_STATUS_STARTING_TIME, isCorrectOrder(before, after), results);
                after = before;
            }
        }
    }
    
    private boolean isCorrectOrder(Date before, Date after) {
        return (before != null) && (after != null) && (after.after(before) || after.equals(before));
    }
    
    private void checkServiceAndHistoryTypeIdentifier(CheckDTO check, TLServiceDto service, List<CheckResultDTO> results) {
        String expectedTypeIdentifier = service.getTypeIdentifier();
        boolean validService = true;
        List<TLServiceHistory> history = service.getHistory();
        if (CollectionUtils.isNotEmpty(history)) {
            for (TLServiceHistory serviceHistory : history) {
                boolean validServiceHistory = genericValidator.isEquals(expectedTypeIdentifier, serviceHistory.getTypeIdentifier());
                addResult(check, serviceHistory.getId(), validServiceHistory, results);
                validService &= validServiceHistory;
            }
        }
        addResult(check, service.getId(), validService, results);
    }
    
    private void checkServiceAndHistorySubjectNames(CheckDTO check, TLServiceDto service, List<CheckResultDTO> results) {
        X500Principal expectedSubjectX500Principal = getSubjectX500Principal(service.getDigitalIdentification());
        if (expectedSubjectX500Principal == null) {
            LOGGER.debug("No SubjectName found for Service " + service.getId());
        } else {
            List<TLServiceHistory> history = service.getHistory();
            boolean hasOneHistoryFalse = false;
            if (CollectionUtils.isNotEmpty(history)) {
                for (TLServiceHistory serviceHistory : history) {
                    X500Principal historySubjectX500Principal = getSubjectX500Principal(serviceHistory.getDigitalIdentification());
                    if (historySubjectX500Principal != null) {
                        boolean valid = DSSUtils.x500PrincipalAreEquals(expectedSubjectX500Principal, historySubjectX500Principal);
                        addResult(check, serviceHistory.getId(), valid, results);
                        if (!valid) {
                            hasOneHistoryFalse = true;
                        }
                    }
                }
            }
            if (hasOneHistoryFalse) {
                addResult(check, service.getId(), false, results);
            }
        }
    }
    
    private X500Principal getSubjectX500Principal(List<TLDigitalIdentification> digitalIdentifications) {
        if (CollectionUtils.isNotEmpty(digitalIdentifications)) {
            for (TLDigitalIdentification identification : digitalIdentifications) {
                if (CollectionUtils.isNotEmpty(identification.getCertificateList())) {
                    for (TLCertificate cert : identification.getCertificateList()) {
                        if (cert.getToken() == null) {
                            cert.setTokenFromEncoded();
                        }
                        return cert.getToken().getSubjectX500Principal();
                    }
                } else if (StringUtils.isNotEmpty(identification.getSubjectName())) {
                    return DSSUtils.getX500PrincipalOrNull(identification.getSubjectName());
                }
            }
        }
        return null;
    }
    
    private void checkServiceAndHistoryX509SKI(CheckDTO check, TLServiceDto service, List<CheckResultDTO> results) {
        byte[] expectedSKI = getX509SKI(service.getDigitalIdentification());
        if (ArrayUtils.isNotEmpty(expectedSKI)) {
            List<TLServiceHistory> history = service.getHistory();
            boolean hasOneHistoryFalse = false;
            if (CollectionUtils.isNotEmpty(history)) {
                for (TLServiceHistory serviceHistory : history) {
                    byte[] ski = getX509SKI(serviceHistory.getDigitalIdentification());
                    if (ArrayUtils.isNotEmpty(ski)) {
                        boolean valid = Arrays.equals(expectedSKI, ski);
                        addResult(check, serviceHistory.getId(), valid, results);
                        if (!valid) {
                            hasOneHistoryFalse = true;
                        }
                    }
                }
            }
            if (hasOneHistoryFalse) {
                addResult(check, service.getId(), false, results);
            }
        }
    }
    
    /**
     * When the service type (clause 5.5.1) is "NationalRootCA-QC", this field shall be present. In other cases, this field is optional.
     */
    private void checkTSPServiceDefinitionURIPresent(CheckDTO check, TLServiceDto service, List<CheckResultDTO> results) {
        String serviceTypeNationalRootCAQC = propertiesService.getServiceTypeNationalRootCAQC();
        if (StringUtils.equals(serviceTypeNationalRootCAQC, service.getTypeIdentifier())) {
            addResult(check, service.getId() + "_" + Tag.TSP_SERVICE_DEFINITION_URI, 
                    genericValidator.isCollectionPresentAndNotEmpty(service.getTSPDefinitionUri()), results);
        }
    }
    
    private byte[] getX509SKI(List<TLDigitalIdentification> digitalIdentifications) {
        if (CollectionUtils.isNotEmpty(digitalIdentifications)) {
            for (TLDigitalIdentification identification : digitalIdentifications) {
                if (CollectionUtils.isNotEmpty(identification.getCertificateList())) {
                    for (TLCertificate cert : identification.getCertificateList()) {
                        if (cert.getToken() == null) {
                            cert.setTokenFromEncoded();
                        }
                        CertificateToken certificate = cert.getToken();
                        return TLUtils.getSki(certificate);
                    }
                } else if (ArrayUtils.isNotEmpty(identification.getX509ski())) {
                    return identification.getX509ski();
                }
            }
        }
        return null;
    }
    
    private void checkAllQualifiersCorrectValue(CheckDTO check, TLServiceDto service, List<CheckResultDTO> results) {
        List<TLServiceExtension> extensions = service.getExtension();
        if (CollectionUtils.isNotEmpty(extensions)) {
            for (TLServiceExtension extension : extensions) {
                List<TLQualificationExtension> qualificationExtensions = extension.getQualificationsExtension();
                if (CollectionUtils.isNotEmpty(qualificationExtensions)) {
                    for (TLQualificationExtension qualificationExtension : qualificationExtensions) {
                        List<String> qualifiers = qualificationExtension.getQualifTypeList();
                        if (CollectionUtils.isNotEmpty(qualifiers)) {
                            List<String> allowedQualifiers = propertiesService.getQualifiers();
                            boolean allQualifiersAllowed = true;
                            for (String qualifier : qualifiers) {
                                allQualifiersAllowed &= allowedQualifiers.contains(qualifier);
                            }
                            addResult(check, qualificationExtension.getId(), allQualifiersAllowed, results);
                        }
                    }
                }
            }
        }
    }
    
    /*
    * , it can be expected that "CA/PKC", "OCSP", CRL", "PSES", "AdESValidation", "AdESGeneration
    * " should require use of at least one of the â€œSie:aSI For*". A warning should be raised when this is not the case.
    */
   private void checkAllAsiExtensionVersusServiceType(CheckDTO check, TLServiceDto service, List<CheckResultDTO> results) {
       List<String> asiForeSignatureForeType = propertiesService.getServiceTypeForAsiForeChecks();
       List<String> allServiceTypeAllow = new ArrayList<String>();
       allServiceTypeAllow.addAll(asiForeSignatureForeType);

       if (allServiceTypeAllow.contains(service.getTypeIdentifier())) {
           // GET ALL ASI INFO
           List<TLAdditionnalServiceInfo> asiList = new ArrayList<TLAdditionnalServiceInfo>();
           List<TLServiceExtension> extensions = service.getExtension();
           if (CollectionUtils.isNotEmpty(extensions)) {
               for (TLServiceExtension extension : extensions) {
                   TLAdditionnalServiceInfo asiExtensions = extension.getAdditionnalServiceInfo();
                   if ((asiExtensions != null) && StringUtils.isNotEmpty(asiExtensions.getValue())) {
                       asiList.add(asiExtensions);
                   }
               }
           }
           boolean findAsi = false;
           // CHeck if service have an ASI with ForeSignature URI
           if (CollectionUtils.isNotEmpty(asiList) && asiForeSignatureForeType.contains(service.getTypeIdentifier())) {
               // CHeck if service have an ASI with Fore*
               for (TLAdditionnalServiceInfo asi : asiList) {
                   findAsi = asi.getValue().endsWith("ForeSignatures") || asi.getValue().endsWith("ForeSeals")
                           || asi.getValue().endsWith("ForWebSiteAuthentication");
                   if (findAsi) {
                       break;
                   }
               }
           }

           addResult(check, service.getId() + "_" + Tag.SERVICE_EXTENSION, findAsi, results);
       }
   }
   
   private void checkAllAsiSigSealExtensionVersusServiceType(CheckDTO check, TLServiceDto service, List<CheckResultDTO> results) {
       List<String> asiForeSignatureSealType = propertiesService.getServiceTypeForAsiForeSignatureSealChecks();
       List<String> allServiceTypeAllow = new ArrayList<String>();
       allServiceTypeAllow.addAll(asiForeSignatureSealType);

       if (allServiceTypeAllow.contains(service.getTypeIdentifier())) {
           // GET ALL ASI INFO
           List<TLAdditionnalServiceInfo> asiList = new ArrayList<TLAdditionnalServiceInfo>();
           List<TLServiceExtension> extensions = service.getExtension();
           if (CollectionUtils.isNotEmpty(extensions)) {
               for (TLServiceExtension extension : extensions) {
                   TLAdditionnalServiceInfo asiExtensions = extension.getAdditionnalServiceInfo();
                   if ((asiExtensions != null) && StringUtils.isNotEmpty(asiExtensions.getValue())) {
                       asiList.add(asiExtensions);
                   }
               }
           }
           boolean findAsi = false;
           // Check if service have an ASI with ForeSignature URI
           if (CollectionUtils.isNotEmpty(asiList) && asiForeSignatureSealType.contains(service.getTypeIdentifier())) {
               // CHeck if service have an ASI with ForeSignature or ForeSeal
               for (TLAdditionnalServiceInfo asi : asiList) {
                   findAsi = asi.getValue().endsWith("ForeSignatures") || asi.getValue().endsWith("ForeSeals");
                   if (findAsi) {
                       break;
                   }
               }
           }

           addResult(check, service.getId() + "_" + Tag.SERVICE_EXTENSION, findAsi, results);
       }
   }
   
   private void checkQualificationExtensionVersusServiceType(CheckDTO check, TLServiceDto service, List<CheckResultDTO> results) {
       if (CollectionUtils.isNotEmpty(service.getExtension())) {
           List<TLServiceExtension> serviceExtList = service.getExtension();
           for (TLServiceExtension ext : serviceExtList) {
               if (CollectionUtils.isNotEmpty(ext.getQualificationsExtension())) {
                   addResult(check, ext.getId() + "_" + Tag.SERVICE_EXTENSION,
                           StringUtils.endsWith(service.getTypeIdentifier(), "CA/QC"), results);
               }
           }
       }
   }
   
   private void checkCertRevocInfoExtensionVersusServiceType(CheckDTO check, TLServiceDto service, List<CheckResultDTO> results) {
       List<String> certRevocationInfoExtList = propertiesService.getServiceTypeForExpiredCertRevocationInfoChecks();
       List<String> allServiceTypeAllow = new ArrayList<String>();
       allServiceTypeAllow.addAll(certRevocationInfoExtList);

       List<TLServiceExtension> extensions = service.getExtension();
       if (CollectionUtils.isNotEmpty(extensions)) {
           for (TLServiceExtension extension : extensions) {
               if (extension.getExpiredCertsRevocationDate() != null) {
                   addResult(check, extension.getId(), allServiceTypeAllow.contains(service.getTypeIdentifier()), results);
               }
           }
       }
   }
    
    private void checkAllDigitalIdsOrganizationMatch(CheckDTO check, TLServiceDto service, TLServiceProvider serviceProvider, List<CheckResultDTO> results) {
        List<TLServiceDto> tspServices = serviceProvider.getTSPServices();
        if (CollectionUtils.isNotEmpty(tspServices)) {
            for (TLServiceDto serviceBis : tspServices) {
                if(serviceBis.equals(service)) {
                    if (CollectionUtils.isNotEmpty(service.getDigitalIdentification())) {
                        List<TLDigitalIdentification> digitalIdList = service.getDigitalIdentification();
                        for (TLDigitalIdentification digital : digitalIdList) {
                            if ((digital != null) && (CollectionUtils.isNotEmpty(digital.getCertificateList()))) {
                                for (TLCertificate cert : digital.getCertificateList()) {
                                    if (cert.getToken() == null) {
                                        cert.setTokenFromEncoded();
                                    }
                                    String organization = certificateService.getOrganization(cert.getToken());
                                    List<TLName> listToCheck = new ArrayList<TLName>();
                                    if (serviceProvider.getTSPName() != null) {
                                        listToCheck.addAll(serviceProvider.getTSPName());
                                    }
                                    if (serviceProvider.getTSPTradeName() != null) {
                                        listToCheck.addAll(serviceProvider.getTSPTradeName());
                                    }
                                    addResult(check, cert.getId(), isMatch(listToCheck, organization), results);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    private boolean isMatch(List<TLName> nameList, String organization) {
        if (CollectionUtils.isNotEmpty(nameList) && StringUtils.isNotEmpty(organization)) {
            for (TLName name : nameList) {
                if (StringUtils.equalsIgnoreCase(name.getValue(), organization)) {
                    return true;
                }
            }
        }
        return false;
    }
}
