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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.XmlMappingException;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.dto.TLSignature;
import eu.europa.ec.joinup.tsl.business.dto.TrustedListsReport;
import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.business.repository.TLRepository;
import eu.europa.ec.joinup.tsl.business.util.FileUtils;
import eu.europa.ec.joinup.tsl.business.util.TLUtils;
import eu.europa.ec.joinup.tsl.model.DBCountries;
import eu.europa.ec.joinup.tsl.model.DBDraftStore;
import eu.europa.ec.joinup.tsl.model.DBFiles;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.model.enums.AuditAction;
import eu.europa.ec.joinup.tsl.model.enums.AuditStatus;
import eu.europa.ec.joinup.tsl.model.enums.AuditTarget;
import eu.europa.ec.joinup.tsl.model.enums.CheckStatus;
import eu.europa.ec.joinup.tsl.model.enums.MimeType;
import eu.europa.ec.joinup.tsl.model.enums.SignatureStatus;
import eu.europa.ec.joinup.tsl.model.enums.TLStatus;
import eu.europa.ec.joinup.tsl.model.enums.TLType;
import eu.europa.esig.dss.DSSDocument;
import eu.europa.esig.jaxb.v5.tsl.TrustStatusListTypeV5;

/**
 * Trusted list management functions
 */
@Service
@Transactional(value = TxType.REQUIRED)
public class TLService {

	private static final Logger LOGGER = LoggerFactory.getLogger(TLService.class);

	private static ResourceBundle bundle = ResourceBundle.getBundle("messages");

	@Autowired
	private TLRepository tlRepository;

	@Autowired
	private CountryService countryService;

	@Autowired
	private FileService fileService;

	@Autowired
	private TrustedListJaxbService jaxbService;

	@Autowired
	private TLBuilder tlBuilder;

	@Autowired
	private DraftStoreService draftStoreService;

	@Autowired
	private AuditService auditService;

	/* ----- ----- Get trusted lists (draft) ----- ----- */

	/**
	 * Get draft trusted lists of @draftStoreId
	 *
	 * @param draftStoreId
	 */
	public List<TrustedListsReport> getDraft(String draftStoreId) {
		DBDraftStore ds = draftStoreService.findOne(draftStoreId);
		draftStoreService.updateLastVerificationDate(ds);
		List<TrustedListsReport> list = new ArrayList<>();
		if (ds != null) {
			List<DBTrustedLists> lst = ds.getDraftList();
			for (DBTrustedLists trustedListsDB : lst) {
				TrustedListsReport rt = tlReport(trustedListsDB);
				list.add(rt);
			}
		}
		return list;
	}

	/**
	 * Get all DB TrustedList not archived order by status(desc)
	 */
	public List<DBTrustedLists> findTL() {
		return tlRepository.findAll();
	}

	/**
	 * Init TrustedListsReport from @DBTrustedLists entry
	 *
	 * @param trustedListsDB
	 * @return
	 */
	private TrustedListsReport tlReport(DBTrustedLists trustedListsDB) {
		TrustedListsReport rt = new TrustedListsReport();

		DBFiles xmlFile = trustedListsDB.getXmlFile();
		if (xmlFile != null) {
			if (trustedListsDB.getCheckStatus() != null) {
				if (trustedListsDB.getCheckStatus().equals(CheckStatus.ERROR)) {
					rt.setCheckStatus(trustedListsDB.getCheckStatus());
				} else {
					rt.setCheckStatus(TLUtils.getTLStatus(trustedListsDB.getCheckStatus(), xmlFile));
				}
			}

			if (xmlFile.getSignatureInformation() != null) {
				rt.setSigStatus(xmlFile.getSignatureInformation().getIndication());
			}
			rt.setLastScanDate(xmlFile.getLastScanDate());

		}

		rt.setIssueDate(trustedListsDB.getIssueDate());
		rt.setNextUpdateDate(trustedListsDB.getNextUpdateDate());
		rt.setId(trustedListsDB.getId());
		rt.setName(trustedListsDB.getName());
		rt.setSequenceNumber(trustedListsDB.getSequenceNumber());
		rt.setCheckToRun(trustedListsDB.getCheckToRun());
		rt.setTlType(trustedListsDB.getType());
		DBCountries country = trustedListsDB.getTerritory();
		if (country != null) {
			rt.setTerritoryCode(country.getCodeTerritory());
			rt.setCountryName(country.getCountryName());
		}
		rt.setTlStatus(trustedListsDB.getStatus());

		return rt;
	}

	/* ----- ----- Get trusted list ----- ----- */

	/**
	 * Get trusted list by given @id as TL dto format
	 *
	 * @param id
	 */
	public TL getTL(int id) {
		DBTrustedLists tldb = getDbTL(id);
		return getDtoTL(tldb);
	}

	/**
	 * Get trusted list by given @id as @DBTrustedLists format
	 *
	 * @param id
	 */
	public DBTrustedLists getDbTL(int id) {
		return tlRepository.findOne(id);
	}

	/**
	 * Get trusted list from xml file @id as @DBTrustedLists format
	 *
	 * @param id
	 */
	public DBTrustedLists findByXmlFileId(int id) {
		return tlRepository.findByXmlFileId(id);
	}

	/**
	 * Get trusted list as TL dto format from @DBTrustedLists format
	 *
	 * @param tldb
	 * @return
	 */
	public TL getDtoTL(DBTrustedLists tldb) {
		TL mytl = null;
		if (tldb != null) {

			DBFiles dbf = tldb.getXmlFile();
			File xml = fileService.getTSLFile(dbf);

			if (tldb.getVersionIdentifier() == 0) {
				LOGGER.error(bundle.getString("error.tl.version.undefined").replace("%TLID%",
						Integer.toString(tldb.getId())));
				return null;
			} else if (tldb.getVersionIdentifier() == 5) {
				TrustStatusListTypeV5 unmarshall;
				try {
					unmarshall = jaxbService.unmarshallTSLV5(xml);
				} catch (XmlMappingException | IOException e) {
					throw new IllegalStateException(bundle.getString("error.proccess.xml.file"));
				}
				if ((unmarshall != null) && (unmarshall.getSchemeInformation() != null)) {
					mytl = tlBuilder.buildTLV5(tldb.getId(), unmarshall);
					mytl.setDbName(tldb.getName());
					mytl.setDbStatus(tldb.getStatus());
					mytl.setDbCountryName(tldb.getTerritory().getCountryName());
					mytl.setLastEdited(tldb.getLastEditedDate());
					mytl.setCheckToRun(tldb.isCheckToRun());
					mytl.setCheckEdited(tldb.getCheckDate());
					DBFiles xmlFile = tldb.getXmlFile();
					if (tldb.getCheckStatus() != null) {
						if (tldb.getCheckStatus().equals(CheckStatus.ERROR)) {
							mytl.setCheckStatus(tldb.getCheckStatus());

						} else {
							mytl.setCheckStatus(TLUtils.getTLStatus(tldb.getCheckStatus(), xmlFile));
						}
					}

					if (xmlFile != null) {
						if (xmlFile.getSignatureInformation() != null) {
							mytl.setSigStatus(xmlFile.getSignatureInformation().getIndication());
						}

						mytl.setFirstScanDate(xmlFile.getFirstScanDate());
					}
				}
			} else {
				throw new IllegalStateException(bundle.getString("error.version.unsupported").replaceAll("%VERSION%",
						Integer.toString(tldb.getVersionIdentifier())));
			}
		}
		return mytl;
	}

	/**
	 * Get trusted list by given @id as @TrustedListsReport format
	 *
	 * @param id
	 * @return
	 */
	public TrustedListsReport getTLInfo(int id) {
		DBTrustedLists tldb = getDbTL(id);
		return getTLInfo(tldb);
	}

	/**
	 * Get trusted list by given @tldb as @TrustedListsReport format
	 *
	 * @param tldb
	 */
	public TrustedListsReport getTLInfo(DBTrustedLists tldb) {
		TrustedListsReport rt = tlReport(tldb);
		return rt;
	}
	
	/**
	 * Return every draft related to a given country
	 * 
	 * @param territory
	 * @return
	 */
    public List<DBTrustedLists> getDraftsByCountry(String territory) {
        DBCountries country = countryService.getCountryByTerritory(territory);
        return tlRepository.findByTerritoryAndStatusAndArchiveFalse(country, TLStatus.DRAFT);
     }

	/* ----- ----- Trusted list XML ----- ----- */

	/**
	 * Extract file version from TL XML by xml file @dbF
	 *
	 * @param tldb
	 */
	public int extractVersionFromFile(DBFiles dbf) {
		File xml = fileService.getTSLFile(dbf);
		String fileVersion = FileUtils.getTlVersion(xml);
		return Integer.valueOf(fileVersion);
	}

	/**
	 * Get XML file signature info by trusted list @id
	 *
	 * @param id
	 * @return
	 */
	public TLSignature getSignatureInfo(int id) {
		DBTrustedLists tldb = getDbTL(id);
		if ((tldb.getXmlFile() != null) && (tldb.getXmlFile().getSignatureInformation() != null)) {
			return new TLSignature(tldb.getXmlFile().getSignatureInformation());
		}
		return null;
	}

	/**
	 * Get XMLStream & remove signature if exist
	 *
	 * @param id
	 * @return trustedlist xml input stream
	 */
	public InputStream getXmlStreamToSign(int id) {
		File xml = getXmlFile(id);
		try {
			return new FileInputStream(xml);
		} catch (FileNotFoundException e) {
			LOGGER.error("XML FILE NOT FOUND FOR TL ID : " + id + " / " + e.getMessage(), e);
			return null;
		}
	}

	/**
	 * Get trusted list XML of given ID
	 *
	 * @param id
	 */
	public File getXmlFile(int id) {
		DBTrustedLists tldb = getDbTL(id);
		DBFiles dbf = tldb.getXmlFile();
		File xml = fileService.getTSLFile(dbf);
		if (xml != null) {
			return xml;
		}
		return null;
	}

	/**
	 * Update @tlId XML file digest based on XML signedDoc value
	 *
	 * @param signedDoc
	 * @param tlId
	 */
	public void updateSignedXMLFile(DSSDocument signedDoc, int tlId) {
		DBTrustedLists dbTl = getDbTL(tlId);
		DBFiles xmlFile = dbTl.getXmlFile();
		xmlFile.setLocalPath(fileService.storeNewDraftTL(xmlFile.getMimeTypeFile(), signedDoc,
				dbTl.getTerritory().getCodeTerritory(), xmlFile.getLocalPath()));
		try {
			xmlFile.setDigest(TLUtils.getSHA2(IOUtils.toByteArray(signedDoc.openStream())));
		} catch (IOException e) {
			LOGGER.error("Update Signed XML File" + e);
		}
		tlRepository.save(dbTl);
		auditService.addAuditLog(AuditTarget.DRAFT_TL, AuditAction.UPDATE, AuditStatus.SUCCES,
				dbTl.getTerritory().getCodeTerritory(), xmlFile.getId(), "SYSTEM",
				"CLASS:TLSERVICE.UPDATESIGNEDXMLFILE,TLID:" + tlId + ";PREVIOUS_STEP:SIGNED");
	}

	/**
	 * Get SHA2 value of trusted list @id XML file
	 *
	 * @param id
	 */
	public String getSha2Value(int id) {
		FileInputStream fileInputStream = null;
		File file = getXmlFile(id);
		byte[] bFile = new byte[(int) file.length()];

		try {
			// convert file into array of bytes
			fileInputStream = new FileInputStream(file);
			fileInputStream.read(bFile);
			fileInputStream.close();

			String digestOfXml = TLUtils.getSHA2(bFile);
			return digestOfXml;
		} catch (Exception e) {
			LOGGER.error("getSha2Value Error");
		}

		return "";
	}

	/**
	 * Get trusted list with similar XML digest
	 * 
	 * @param xmlDigest
	 */
	public List<DBTrustedLists> findTlSimilarDigest(String xmlDigest, TLStatus status) {
	    return tlRepository.findByXmlFileDigestAndStatusOrderByNameAsc(xmlDigest, status);
	}

	/* ----- ----- Trusted list creation ----- ----- */

	/**
	 * Create new trusted list entry
	 *
	 * @param xmlUrl
	 * @param type
	 * @param status
	 * @param country
	 * @param tlCurrent
	 * @return
	 */
	public DBTrustedLists createTL(String countryCode, String xmlUrl, TLType type, TLStatus status) {
	    DBCountries country = countryService.getCountryByTerritory(countryCode);
	    
		DBTrustedLists tl;
		tl = new DBTrustedLists();
		tl.setTerritory(country);
		tl.setName(type + " - " + country.getCodeTerritory());
		tl.setType(type);
		tl.setStatus(status);
		tl.setLastEditedDate(new Date());

		DBFiles xmlFile = new DBFiles();
		xmlFile.setUrl(xmlUrl);
		xmlFile.setMimeTypeFile(MimeType.XML);
		tl.setXmlFile(xmlFile);

		tl.setLastEditedDate(new Date());
		tlRepository.save(tl);
		return tl;
	}

	/**
	 * Create clone of trusted list @tlId without signature
	 *
	 * @param tlId
	 */
	public void createFileWithoutSignature(int tlId) {
		TL tl = getTL(tlId);
		DBTrustedLists tldb = getDbTL(tlId);

		byte[] updatedTL = jaxbService.marshallToBytesAsV5(tl);
		if (ArrayUtils.isNotEmpty(updatedTL)) {
			DBFiles xmlFile = tldb.getXmlFile();
			if (!xmlFile.getSignatureInformation().getIndication().equals(SignatureStatus.NOT_SIGNED)) {
				xmlFile.setLocalPath(fileService.storeNewDraftTL(xmlFile.getMimeTypeFile(), updatedTL,
						tldb.getTerritory().getCodeTerritory(), xmlFile.getLocalPath()));
			}
			tldb.setLastEditedDate(new Date());
			tlRepository.save(tldb);
		}
	}

	/* ----- ----- Trusted list deletion ----- ----- */

	/**
	 * Delete trusted list by @dbID
	 *
	 * @param dbId
	 */
	public void deleteDraft(int dbId, String username) {
		DBTrustedLists tsl = getDbTL(dbId);
		deleteDraft(tsl);
		auditService.addAuditLog(AuditTarget.DRAFT_TL, AuditAction.DELETE, AuditStatus.SUCCES, tsl.getTerritory().getCodeTerritory(), dbId, username,
                "TLID:" + dbId);
	}

	/**
	 * Delete trusted list by @DBTrustedLists entry
	 *
	 * @param tsl
	 */
	public void deleteDraft(DBTrustedLists tsl) {
		if (tsl.getXmlFile() != null) {
			DBFiles xmlFile = tsl.getXmlFile();
			File originalFile = fileService.getTSLFile(xmlFile);
			originalFile.delete();
			LOGGER.info("FILE " + xmlFile.getLocalPath() + " IS DELETED FROM FILE SYSTEM");
		}
		tlRepository.delete(tsl.getId());
	}
	
	/* ----- ----- Trusted list informations update/get ----- ----- */

	/**
	 * Switch checkToRun of trusted list @tlId
	 *
	 * @param tlId
	 */
	public boolean switchCheckToRun(int tlId) {
		DBTrustedLists tl = getDbTL(tlId);
		boolean checkToRun = !tl.isCheckToRun();
		tl.setCheckToRun(checkToRun);
		tl.setLastEditedDate(new Date());
		tlRepository.save(tl);
		return checkToRun;
	}

	/**
	 * Update trusted list lastAccessDate by @tlId
	 *
	 * @param tlId
	 */
	public void updateLastAccessDate(int tlId) {
		tlRepository.updateLastAccessDate(new Date(), tlId);
	}

	/**
	 * Get lastEditionDate by trusted list @tlId
	 *
	 * @param tlId
	 */
	public Date getEdt(int tlId) {
		DBTrustedLists tldb = getDbTL(tlId);
		return tldb.getLastEditedDate();
	}

	/**
	 * Update trusted list checkDate to date by @tlId
	 *
	 * @param tlId
	 */
	public void updateCheckDate(int tlId) {
		DBTrustedLists dbTL = getDbTL(tlId);
		dbTL.setCheckDate(new Date());
		tlRepository.save(dbTL);
	}

	/**
	 * Archive trusted list by @DBTrustedLists entry
	 *
	 * @param tl
	 * @param status
	 */
	public void archive(DBTrustedLists tl, Boolean status) {
		tl.setArchive(status);
		tlRepository.save(tl);
	}

	/**
	 * Update trusted list checkStatus by @tlId
	 *
	 * @param tlId
	 */
	public void setTlCheckStatus(int tlId) {
		CheckStatus tmp = CheckStatus.SUCCESS;
		DBTrustedLists dbTl = getDbTL(tlId);
		tmp = TLUtils.getCheckStatus(dbTl.getCheckResults());
		LOGGER.info("Check status for " + dbTl.getName() + " / " + dbTl.getId() + " is set to  : " + tmp.toString());
		dbTl.setCheckStatus(tmp);
	}

	/**
	 * Check if trusted list is TL or an existing draft TL. True is found
	 *
	 * @param tlid
	 * @param cookie
	 */
	public boolean inStore(int tlid, String cookie) {
		DBTrustedLists tl = getDbTL(tlid);
		return ((cookie != null) && tl.getDraftStoreId().equals(cookie)
				&& draftStoreService.checkDraftStoreId(tl.getDraftStoreId()));
	}
}
