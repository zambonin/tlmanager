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
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.dto.Properties;
import eu.europa.ec.joinup.tsl.business.repository.PropertiesListRepository;
import eu.europa.ec.joinup.tsl.business.repository.PropertiesRepository;
import eu.europa.ec.joinup.tsl.model.DBProperties;
import eu.europa.ec.joinup.tsl.model.DBPropertiesList;

/**
 * Trusted list standard properties management
 */
@Service
@Transactional(value = TxType.REQUIRED)
public class PropertiesService {

    private static final String LANGUAGE_PROPERTY_CODE = "LANGUAGES";

    private static final String TSL_TAG_VALUE_PROPERTY_CODE = "TSL_TAG_VALUE";

    private static final String TL_TSL_TYPE_PROPERTY_CODE = "TL_TYPE";

    private static final String TL_STATUS_DETERMINATION_APPROACH_PROPERTY_CODE = "TL_STATUS_DETERM_TYPE";

    private static final String TL_SCHEME_COMMUNITY_RULES_PROPERTY_CODE = "TL_COMMUNITYRULE";

    private static final String SERVICE_TYPES_IDENTIFIERS_PROPERTY_CODE = "SERVICE_TYPES_IDENTIFIERS";

    private static final String SERVICE_STATUS_PROPERTY_CODE = "SERVICE_STATUS";
    
    private static final String SERVICE_PREVIOUS_STATUS_PROPERTY_CODE = "SERVICE_PREVIOUS_STATUS";

    private static final String QUALIFIERS_PROPERTY_CODE = "QUALIFIERS";
    
    private static final String MIMETYPE_PROPERTY_CODE = "MIMETYPE";

    @Autowired
    private PropertiesListRepository propListRepository;

    @Autowired
    private PropertiesRepository propRepo;

    public List<Properties> getProperties() {
        List<Properties> propList = new ArrayList<>();
        List<DBPropertiesList> dbPropListList = (List<DBPropertiesList>) propListRepository.findAll();
        for (DBPropertiesList dbPropList : dbPropListList) {
            convertToDTOs(propList, dbPropList);
        }
        return propList;
    }

    @Cacheable(value = "propertiesCache", key = "#root.methodName")
    public List<String> getServiceTypeIdentifiers() {
        DBPropertiesList propertiesList = propListRepository.findOne(SERVICE_TYPES_IDENTIFIERS_PROPERTY_CODE);
        return extractLabels(propertiesList);
    }

    @Cacheable(value = "propertiesCache", key = "#root.methodName")
    public List<String> getQualifiers() {
        DBPropertiesList propertiesList = propListRepository.findOne(QUALIFIERS_PROPERTY_CODE);
        return extractLabels(propertiesList);
    }
    
    @Cacheable(value = "propertiesCache", key = "#root.methodName")
    public List<String> getMimeTypes() {
        DBPropertiesList propertiesList = propListRepository.findOne(MIMETYPE_PROPERTY_CODE);
        return extractLabels(propertiesList);
    }

    @Cacheable(value = "propertiesCache", key = "#root.methodName")
    public List<String> getServiceStatus() {
        DBPropertiesList propertiesList = propListRepository.findOne(SERVICE_STATUS_PROPERTY_CODE);
        return extractLabels(propertiesList);
    }
    
    @Cacheable(value = "propertiesCache", key = "#root.methodName")
    public List<String> getServicePreviousStatus() {
        DBPropertiesList propertiesList = propListRepository.findOne(SERVICE_PREVIOUS_STATUS_PROPERTY_CODE);
        return extractLabels(propertiesList);
    }

    @Cacheable(value = "propertiesCache", key = "#root.methodName")
    public List<Properties> getLanguages() {
        List<Properties> propList = new ArrayList<>();
        DBPropertiesList propertiesList = propListRepository.findOne(LANGUAGE_PROPERTY_CODE);
        convertToDTOs(propList, propertiesList);
        return propList;
    }

    private void convertToDTOs(List<Properties> propList, DBPropertiesList dbPropertiesList) {
        if ((dbPropertiesList != null) && CollectionUtils.isNotEmpty(dbPropertiesList.getPropertiesInfo())) {
            for (DBProperties dbProp : dbPropertiesList.getPropertiesInfo()) {
                propList.add(new Properties(dbPropertiesList.getCode(), dbProp));
            }
        }
    }

    @Cacheable(value = "propertiesCache", key = "#root.methodName")
    public List<String> getTSLTags() {
        DBPropertiesList propertiesList = propListRepository.findOne(TSL_TAG_VALUE_PROPERTY_CODE);
        return extractLabels(propertiesList);
    }

    @Cacheable(value = "propertiesCache", key = "#root.methodName")
    public List<String> getTLTSLTypes() {
        DBPropertiesList propertiesList = propListRepository.findOne(TL_TSL_TYPE_PROPERTY_CODE);
        return extractLabels(propertiesList);
    }

    @Cacheable(value = "propertiesCache", key = "#root.methodName")
    public List<String> getTLSchemeCommunityRulesValues() {
        DBPropertiesList propertiesList = propListRepository.findOne(TL_SCHEME_COMMUNITY_RULES_PROPERTY_CODE);
        return extractLabels(propertiesList);
    }

    private List<String> extractLabels(DBPropertiesList propertiesList) {
        List<String> labels = new ArrayList<>();
        for (DBProperties dbProp : propertiesList.getPropertiesInfo()) {
            labels.add(dbProp.getLabel());
        }
        return labels;
    }

    @Cacheable(value = "propertiesCache", key = "#root.methodName")
    public List<String> getTLStatusDeterminationApproachValue() {
        DBPropertiesList propertiesList = propListRepository.findOne(TL_STATUS_DETERMINATION_APPROACH_PROPERTY_CODE);
        return extractLabels(propertiesList);
    }

    @Cacheable(value = "propertiesCache", key = "#root.methodName")
    public String getServiceTypeNationalRootCAQC() {
        List<String> serviceTypeIdentifiers = getServiceTypeIdentifiers();
        if (CollectionUtils.isNotEmpty(serviceTypeIdentifiers)) {
            for (String typeIdentifier : serviceTypeIdentifiers) {
                if (StringUtils.contains(typeIdentifier, "NationalRootCA-QC")) {
                    return typeIdentifier;
                }
            }
        }
        return StringUtils.EMPTY;
    }

    @Cacheable(value = "propertiesCache", key = "#root.methodName")
    public List<String> getServiceTypeForAsiForeSignatureSealChecks() {
        List<String> results = new ArrayList<>();
        List<String> serviceTypeIdentifiers = getServiceTypeIdentifiers();
        if (CollectionUtils.isNotEmpty(serviceTypeIdentifiers)) {
            for (String typeIdentifier : serviceTypeIdentifiers) {
                if (StringUtils.endsWith(typeIdentifier, "PSES/Q") || StringUtils.endsWith(typeIdentifier, "QESValidation/Q")) {
                    results.add(typeIdentifier);
                }
            }
        }
        return results;
    }

    @Cacheable(value = "propertiesCache", key = "#root.methodName")
    public List<String> getServiceTypeForExpiredCertRevocationInfoChecks() {
        List<String> results = new ArrayList<>();
        List<String> serviceTypeIdentifiers = getServiceTypeIdentifiers();
        if (CollectionUtils.isNotEmpty(serviceTypeIdentifiers)) {
            for (String typeIdentifier : serviceTypeIdentifiers) {
                if (StringUtils.endsWith(typeIdentifier, "CA/PKC") || StringUtils.endsWith(typeIdentifier, "OCSP") || StringUtils.endsWith(typeIdentifier, "CRL")
                        || StringUtils.endsWith(typeIdentifier, "CA/QC") || StringUtils.endsWith(typeIdentifier, "NationalRootCA-QC") || StringUtils.endsWith(typeIdentifier, "Certstatus/OCSP/QC")
                        || StringUtils.endsWith(typeIdentifier, "Certstatus/CRL/QC")) {
                    results.add(typeIdentifier);
                }
            }
        }
        return results;
    }

    @Cacheable(value = "propertiesCache", key = "#root.methodName")
    public List<String> getServiceTypeForAsiForeChecks() {
        List<String> results = new ArrayList<>();
        List<String> serviceTypeIdentifiers = getServiceTypeIdentifiers();
        if (CollectionUtils.isNotEmpty(serviceTypeIdentifiers)) {
            for (String typeIdentifier : serviceTypeIdentifiers) {
                if (StringUtils.endsWith(typeIdentifier, "CA/PKC") || StringUtils.endsWith(typeIdentifier, "OCSP") || StringUtils.endsWith(typeIdentifier, "CRL")
                        || StringUtils.endsWith(typeIdentifier, "PSES") || StringUtils.endsWith(typeIdentifier, "AdESValidation") || StringUtils.endsWith(typeIdentifier, "AdESGeneration")) {
                    results.add(typeIdentifier);
                }
            }
        }
        return results;
    }

    public Properties add(Properties prop) {
        DBProperties dbProp = new DBProperties();
        DBPropertiesList dbList = propListRepository.findOne(prop.getCodeList());
        if (dbList != null) {
            dbProp.setLabel(prop.getLabel());
            dbProp.setDescription(prop.getDescription());
            dbProp.setPropertiesList(dbList);
            propRepo.save(dbProp);
            return new Properties(dbList.getCode(), dbProp);
        } else {
            return null;
        }
    }

    public void delete(Properties prop) {
        propRepo.delete(prop.getId());
    }

}
