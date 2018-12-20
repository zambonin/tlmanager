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

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.util.TLUtils;
import eu.europa.ec.joinup.tsl.model.DBFiles;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.model.enums.TLStatus;
import eu.europa.ec.joinup.tsl.model.enums.TLType;
import eu.europa.esig.dss.client.http.DataLoader;
import eu.europa.esig.jaxb.v5.tsl.TrustStatusListTypeV5;

/**
 * Trusted list loader Utils
 */
@Service
public class TLLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(TLLoader.class);

    @Autowired
    private DataLoader dataLoader;

    @Autowired
    private TLService tlService;

    @Autowired
    private FileService fileService;

    @Autowired
    private TrustedListJaxbService jaxbService;

    @Transactional(value = TxType.REQUIRES_NEW)
    public TrustStatusListTypeV5 loadTL(String countryCode, String xmlUrl, TLType type, TLStatus status) {
        TrustStatusListTypeV5 jaxbTL = null;

        // Get current TL or create a new entry
        DBTrustedLists tl = tlService.createTL(countryCode, xmlUrl, type, status);

        DBFiles xmlFile = tl.getXmlFile();
        byte[] xmlBinaries = dataLoader.get(xmlFile.getUrl(), true);
        String xmlDigest = TLUtils.getSHA2(xmlBinaries);

        // Retrieve trusted lists with a similar XML digest from database
        List<DBTrustedLists> matchingTL = tlService.findTlSimilarDigest(xmlDigest, status);

        xmlFile = tl.getXmlFile();
        xmlFile.setLastScanDate(new Date());
        xmlFile.setDigest(xmlDigest);
        xmlFile.setFirstScanDate(new Date());

        // Check if digest match an existing file
        if (CollectionUtils.isEmpty(matchingTL)) {
            // No digest match => Store the new file
            xmlFile.setLocalPath(fileService.storeNewTL(xmlFile, xmlBinaries, countryCode));
        } else {
            // Digest match => file already stored on disk
            xmlFile.setLocalPath(matchingTL.get(0).getXmlFile().getLocalPath());
        }

        try {
            if ((xmlFile != null) && StringUtils.isNotEmpty(xmlFile.getLocalPath())) {
                File tslFile = fileService.getTSLFile(xmlFile);
                jaxbTL = jaxbService.unmarshallTSLV5(tslFile);
                tl.setIssueDate(TLUtils.toDate(jaxbTL.getSchemeInformation().getListIssueDateTime()));
                tl.setSequenceNumber(jaxbTL.getSchemeInformation().getTSLSequenceNumber().intValue());
                tl.setNextUpdateDate(TLUtils.toDate(jaxbTL.getSchemeInformation().getNextUpdate().getDateTime()));
                tl.setVersionIdentifier(tlService.extractVersionFromFile(xmlFile));
            }
        } catch (Exception e) {
            LOGGER.error("Unable to parse TSL for country " + countryCode + " : " + e.getMessage(), e);
        }
        return jaxbTL;
    }
}
