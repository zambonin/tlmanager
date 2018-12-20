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
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.TimeZone;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.oxm.XmlMappingException;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.dto.TrustedListsReport;
import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLSchemeInformation;
import eu.europa.ec.joinup.tsl.business.repository.TLRepository;
import eu.europa.ec.joinup.tsl.business.util.FileUtils;
import eu.europa.ec.joinup.tsl.business.util.TLUtils;
import eu.europa.ec.joinup.tsl.model.DBCountries;
import eu.europa.ec.joinup.tsl.model.DBFiles;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.model.enums.AuditAction;
import eu.europa.ec.joinup.tsl.model.enums.AuditStatus;
import eu.europa.ec.joinup.tsl.model.enums.AuditTarget;
import eu.europa.ec.joinup.tsl.model.enums.MimeType;
import eu.europa.ec.joinup.tsl.model.enums.TLStatus;
import eu.europa.ec.joinup.tsl.model.enums.TLType;
import eu.europa.esig.jaxb.v5.tsl.TrustStatusListTypeV5;

/**
 * Draft management (clone, conflict management, import)
 */
@Service
@Transactional(value = TxType.REQUIRED)
public class TLDraftService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TLDraftService.class);
    private static final ResourceBundle bundle = ResourceBundle.getBundle("messages");

    @Autowired
    private AuditService auditService;

    @Autowired
    private CountryService countryService;

    @Autowired
    private TrustedListJaxbService jaxbService;

    @Autowired
    private FileService fileService;

    @Autowired
    private TLBuilder tlBuilder;

    @Autowired
    private TLRepository tlRepository;
    @Autowired
    private TLService tlService;

    @Autowired
    private TLValidator tlValidator;

    @Autowired
    private RulesRunnerService rulesRunner;
    
    @Value("${tsl.folder}")
    private String folderPath;

    @Value("${tmpPrefixFile}")
    private String prefixTmpFile;

    /**
     * Verify draft, set TL status, add audit entry and return TrustedListsReport
     * 
     * @param draft
     * @param name
     *            User ID
     */
    public TrustedListsReport finalizeDraftCreation(DBTrustedLists draft, String name) {
        auditService.addAuditLog(AuditTarget.DRAFT_TL, AuditAction.CREATE, AuditStatus.SUCCES, draft.getTerritory().getCodeTerritory(), draft.getXmlFile().getId(), name, "TLID:" + draft.getId());
        // CHECK SIGNATURE STATUS
        tlValidator.checkTlWithKeyStore(draft);
        // EXECUTE ALL CHECK
        TL draftTL = tlService.getTL(draft.getId());
        rulesRunner.runAllRules(draftTL);

        tlService.setTlCheckStatus(draftTL.getTlId());
        // RETURN TL REPORT
        TrustedListsReport report = tlService.getTLInfo(draft.getId());
        return report;
    }

    /**
     * Return a DBTrustedLists clone of the TL in parameters
     * 
     * @param tl
     * @param nameExt the extension added at the end of the name of the DBTrustedLists
     */
    public DBTrustedLists clone(TL tl, String nameExt) {
        DBTrustedLists dbTl = tlService.getDbTL(tl.getTlId());
        DBTrustedLists newDbTl = tlService.createTL(dbTl.getTerritory().getCodeTerritory(), dbTl.getXmlFile().getUrl(), dbTl.getType(), dbTl.getStatus());
        newDbTl.setName(dbTl.getName() + nameExt);
        newDbTl.setDraftStoreId(dbTl.getDraftStoreId());
        newDbTl.setNextUpdateDate(tl.getSchemeInformation().getNextUpdateDate());
        newDbTl.setIssueDate(tl.getSchemeInformation().getIssueDate());
        newDbTl.setSequenceNumber(tl.getSchemeInformation().getSequenceNumber());

        DBFiles xmlFile = new DBFiles();
        xmlFile.setMimeTypeFile(MimeType.XML);
        xmlFile.setLocalPath(fileService.storeNewTL(xmlFile, jaxbService.marshallToBytesAsV5(tl), dbTl.getTerritory().getCodeTerritory()));
        newDbTl.setXmlFile(xmlFile);
        newDbTl.setVersionIdentifier(tlService.extractVersionFromFile(xmlFile));

        return newDbTl;
    }
    
    /**
     * Create a clone of draft when edition conflict occurs (concurrent edition of the same TL)
     *
     * @param tl
     */
    public DBTrustedLists conflictTLtoDraft(TL tl) {
        if (tl.getDbStatus().equals(TLStatus.DRAFT)) {
            clone(tl, "_tmp");
        }
        return null;
    }
    
    /**
     * Create an empty draft
     * 
     * @param cookieId
     * @param userName
     * @param territory
     * @return
     * @throws IOException 
     * @throws XmlMappingException 
     */
    public DBTrustedLists createEmptyDraft(String cookieId, String userName, String territory) throws XmlMappingException, IOException {

        // Create TL and set territory
        TL draftTLDTO = new TL();
        TLSchemeInformation TLinfos = new TLSchemeInformation();
        TLinfos.setSequenceNumber(1);
        TLinfos.setTerritory(territory);
        TLinfos.setTlIdentifier(5);
        
        // Compute Issue and Next Update dates
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        Date date = new Date();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 1);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        TLinfos.setIssueDate(cal.getTime());
        
        cal.add(Calendar.MONTH, 6);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        TLinfos.setNextUpdateDate(cal.getTime());
        
        // Set TL
        draftTLDTO.setSchemeInformation(TLinfos);

        // Generate XML file from TL
        byte[] draftBinaries = jaxbService.marshallToBytesAsV5(draftTLDTO);
        try {
            DBTrustedLists newDraft = createDraftFromXML(draftBinaries, cookieId, userName);
            return newDraft;
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage());
        }
        return null;
    }

    /** 
     * Duplicate a draft
     * 
     * @param origTLR
     * @param userName
     */
    public DBTrustedLists duplicateDraft(TrustedListsReport origTLR, String userName) {
        TL draftTL = tlService.getTL(origTLR.getId());
        DBTrustedLists newDraft = clone(draftTL, "_dup");
        return newDraft;
    }

    /**
     * Create new draft based on trusted list XML file byte array
     *
     * @param byteArray
     * @param cookieId
     * @param userName
     * @param migrate
     * @throws XmlMappingException
     * @throws IOException
     */
    public DBTrustedLists createDraftFromXML(byte[] byteArray, String cookieId, String userName) throws XmlMappingException, IOException, IllegalArgumentException {
        // CREATE FILE FROM BYTE.
        File originalFile = File.createTempFile(prefixTmpFile, "xml");

        FileOutputStream fos = new FileOutputStream(originalFile);
        fos.write(byteArray);
        fos.close();
        // Check INPUT FILE VERSION
        String fileVersion = FileUtils.getTlVersion(originalFile);

        TL draftTLDTO = null;
        DBTrustedLists draftTL = new DBTrustedLists();
        if (fileVersion.equals("5")) {
            TrustStatusListTypeV5 tslTypeV5 = jaxbService.unmarshallTSLV5(byteArray);
            draftTLDTO = tlBuilder.buildTLV5(0, tslTypeV5);
            draftTL.setVersionIdentifier(5);
        } else if (!fileVersion.equals("")) { // If fileVersion is empty -> problem while parsing XML (!= wrong version)
            LOGGER.error("VERSION  [" + fileVersion + "] is not supported");
            throw new IllegalStateException(bundle.getString("error.version.unsupported").replaceAll("%VERSION%", fileVersion));
        } else {
            LOGGER.error("VERSION is empty");
            throw new IllegalStateException(bundle.getString("error.import.version.undefined"));
        }

        DBCountries country = countryService.getCountryByTerritory(draftTLDTO.getSchemeInformation().getTerritory());

        if (country != null) {
            DBFiles draftXmlFile = createEmptyFile(MimeType.XML);

            String draftXmlPath = fileService.storeNewDraftTL(MimeType.XML, byteArray, country.getCodeTerritory(), null);
            draftXmlFile.setLocalPath(draftXmlPath);
            draftXmlFile.setDigest(TLUtils.getSHA2(byteArray));

            draftTL.setTerritory(country);
            draftTL.setStatus(TLStatus.DRAFT);
            draftTL.setType(TLType.TL);

            draftTL.setDraftStoreId(cookieId);
            draftTL.setSequenceNumber(draftTLDTO.getSchemeInformation().getSequenceNumber());
            draftTL.setIssueDate(draftTLDTO.getSchemeInformation().getIssueDate());
            draftTL.setNextUpdateDate(draftTLDTO.getSchemeInformation().getNextUpdateDate());

            draftTL.setXmlFile(draftXmlFile);
            draftTL.setLastEditedDate(new Date());
            draftTL.setCreatedBy(userName);
            tlRepository.save(draftTL);
            draftTL.setName(getDraftName(country, draftTL.getId()));
            originalFile.delete();
            return draftTL;
        } else {
            LOGGER.error("Country code does not exist : " + draftTLDTO.getSchemeInformation().getTerritory());
            throw new IllegalArgumentException(bundle.getString("draft.error.importUnknownCountry"));
        }
    }

    private String getDraftName(DBCountries country, int id) {
        return TLStatus.DRAFT + "_" + country.getCodeTerritory() + "_" + id;
    }

    private DBFiles createEmptyFile(MimeType mimeType) {
        DBFiles draftFile = new DBFiles();
        draftFile.setMimeTypeFile(mimeType);
        return draftFile;
    }
    
    /**
     * Rename a draft
     * 
     * @param draftId
     * @param newName
     */
    public TL renameDraft(int draftId, String newName) throws IllegalArgumentException {
        if(newName.equals("")) {
            throw new IllegalArgumentException("Draft name can not be empty.");
        }
        TL draft = tlService.getTL(draftId);
        draft.setDbName(newName);
        DBTrustedLists dbtl = tlService.getDbTL(draftId);
        dbtl.setName(newName);
        return draft;
    }

}
