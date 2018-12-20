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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;
import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLInformationExtension;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLSchemeInformation;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.model.enums.TLStatus;
import eu.europa.ec.joinup.tsl.model.enums.TLType;
import eu.europa.ec.joinup.tsl.model.enums.Tag;

@Transactional
public class TLEditSchemeInfoServiceTest extends AbstractSpringTest {

    private static final String BG_COUNTRY_CODE = "BG";
    private static final String BE_COUNTRY_CODE = "BE";
    private static final int SN = 123;

    @Autowired
    private TLLoader tlLoader;

    @Autowired
    private TLService tlService;

    @Autowired
    private TlEditSchemeInfoService tlEditSchemeInfoService;

    @Before
    public void initialize() {
        tlLoader.loadTL(BG_COUNTRY_CODE, "http://crc.bg/files/_en/TSL_BG.xml", TLType.TL, TLStatus.DRAFT);
        tlLoader.loadTL(BE_COUNTRY_CODE, "https://tsl.belgium.be/tsl-be.xml", TLType.TL, TLStatus.DRAFT);

        TL eetl = tlService.getTL(1);
        TL betl = tlService.getTL(2);
        assertTrue(eetl.getSchemeInformation().getTerritory().equalsIgnoreCase(BG_COUNTRY_CODE));
        assertTrue(betl.getSchemeInformation().getTerritory().equalsIgnoreCase(BE_COUNTRY_CODE));
    }

    @Test
    public void editTerritory() {
        TL eetl = tlService.getTL(1);

        TLSchemeInformation tlScheme = new TLSchemeInformation();
        tlScheme.setTerritory(BE_COUNTRY_CODE);

        TLSchemeInformation schemeInfoUpdated = tlEditSchemeInfoService.edit(eetl.getTlId(), tlScheme, Tag.TERRITORY.toString(), "");
        // CHECK RETURN
        assertTrue(schemeInfoUpdated.getTerritory().equalsIgnoreCase(BE_COUNTRY_CODE));

        DBTrustedLists dbeetl = tlService.getDbTL(1);
        // CHECK DB
        assertTrue(dbeetl.getTerritory().getCodeTerritory().equalsIgnoreCase(BE_COUNTRY_CODE));

        TL eetlUpdated = tlService.getTL(1);
        // CHECK XML FILE
        assertTrue(eetlUpdated.getSchemeInformation().getTerritory().equalsIgnoreCase(BE_COUNTRY_CODE));
    }

    @Test
    public void editSN() {
        TL eetl = tlService.getTL(1);
        assertTrue(eetl.getSchemeInformation().getTerritory().equalsIgnoreCase(BG_COUNTRY_CODE));

        TLSchemeInformation tlScheme = new TLSchemeInformation();
        tlScheme.setSequenceNumber(SN);

        TLSchemeInformation schemeInfoUpdated = tlEditSchemeInfoService.edit(eetl.getTlId(), tlScheme, Tag.SEQUENCE_NUMBER.toString(), "");
        // CHECK RETURN
        assertTrue(schemeInfoUpdated.getSequenceNumber() == SN);

        DBTrustedLists dbeetl = tlService.getDbTL(1);
        // CHECK DB
        assertTrue(dbeetl.getSequenceNumber() == SN);

        TL eetlUpdated = tlService.getTL(1);
        // CHECK XML FILE
        assertTrue(eetlUpdated.getSchemeInformation().getSequenceNumber() == SN);
    }

    @Test
    public void editIssueDate() {
        TL eetl = tlService.getTL(1);
        TL betl = tlService.getTL(2);
        assertTrue(eetl.getSchemeInformation().getTerritory().equalsIgnoreCase(BG_COUNTRY_CODE));
        assertTrue(betl.getSchemeInformation().getTerritory().equalsIgnoreCase(BE_COUNTRY_CODE));

        TLSchemeInformation schemeInfoUpdated = tlEditSchemeInfoService.edit(eetl.getTlId(), betl.getSchemeInformation(), Tag.ISSUE_DATE.toString(), "");
        // CHECK RETURN
        assertTrue(schemeInfoUpdated.getIssueDate().equals(betl.getSchemeInformation().getIssueDate()));

        DBTrustedLists dbeetl = tlService.getDbTL(1);
        // CHECK DB
        assertTrue(dbeetl.getIssueDate().equals(betl.getSchemeInformation().getIssueDate()));

        TL eetlUpdated = tlService.getTL(1);
        // CHECK XML FILE
        assertTrue(eetlUpdated.getSchemeInformation().getIssueDate().equals(betl.getSchemeInformation().getIssueDate()));
    }

    @Test
    public void editNextDate() {
        TL eetl = tlService.getTL(1);
        TL betl = tlService.getTL(2);
        assertTrue(eetl.getSchemeInformation().getTerritory().equalsIgnoreCase(BG_COUNTRY_CODE));
        assertTrue(betl.getSchemeInformation().getTerritory().equalsIgnoreCase(BE_COUNTRY_CODE));

        TLSchemeInformation schemeInfoUpdated = tlEditSchemeInfoService.edit(eetl.getTlId(), betl.getSchemeInformation(), Tag.NEXT_UPDATE.toString(), "");
        // CHECK RETURN
        assertTrue(schemeInfoUpdated.getNextUpdateDate().equals(betl.getSchemeInformation().getNextUpdateDate()));

        DBTrustedLists dbee = tlService.getDbTL(1);
        // CHECK DB
        assertTrue(dbee.getNextUpdateDate().equals(betl.getSchemeInformation().getNextUpdateDate()));

        TL eetlUpdated = tlService.getTL(1);
        // CHECK XML FILE
        assertTrue(eetlUpdated.getSchemeInformation().getNextUpdateDate().equals(betl.getSchemeInformation().getNextUpdateDate()));
        // assertFalse(eetlUpdated.getSchemeInformation().getIssueDate().equals(betl.getSchemeInformation().getIssueDate()));
    }

    @Test
    public void editSchemeOperatorName() {
        TL eetl = tlService.getTL(1);
        TL betl = tlService.getTL(2);
        assertTrue(eetl.getSchemeInformation().getTerritory().equalsIgnoreCase(BG_COUNTRY_CODE));
        assertTrue(betl.getSchemeInformation().getTerritory().equalsIgnoreCase(BE_COUNTRY_CODE));

        TLSchemeInformation schemeInfoUpdated = tlEditSchemeInfoService.edit(eetl.getTlId(), betl.getSchemeInformation(), Tag.SCHEME_OPERATOR_NAME.toString(), "");
        // CHECK RETURN
        assertTrue(CollectionUtils.isEqualCollection(schemeInfoUpdated.getSchemeOpeName(), betl.getSchemeInformation().getSchemeOpeName()));

        TL eetlUpdated = tlService.getTL(1);
        // CHECK XML FILE
        assertTrue(CollectionUtils.isEqualCollection(eetlUpdated.getSchemeInformation().getSchemeOpeName(), betl.getSchemeInformation().getSchemeOpeName()));
    }

    @Test
    public void editPostalAddress() {
        TL eetl = tlService.getTL(1);
        TL betl = tlService.getTL(2);
        assertTrue(eetl.getSchemeInformation().getTerritory().equalsIgnoreCase(BG_COUNTRY_CODE));
        assertTrue(betl.getSchemeInformation().getTerritory().equalsIgnoreCase(BE_COUNTRY_CODE));

        TLSchemeInformation schemeInfoUpdated = tlEditSchemeInfoService.edit(eetl.getTlId(), betl.getSchemeInformation(), Tag.POSTAL_ADDRESSES.toString(), "");
        // CHECK RETURN
        assertTrue(CollectionUtils.isEqualCollection(schemeInfoUpdated.getSchemeOpePostal(), betl.getSchemeInformation().getSchemeOpePostal()));

        TL eetlUpdated = tlService.getTL(1);
        // CHECK XML FILE
        assertTrue(CollectionUtils.isEqualCollection(eetlUpdated.getSchemeInformation().getSchemeOpePostal(), betl.getSchemeInformation().getSchemeOpePostal()));
    }

    @Test
    public void editElectronicAddress() {
        TL eetl = tlService.getTL(1);
        TL betl = tlService.getTL(2);
        assertTrue(eetl.getSchemeInformation().getTerritory().equalsIgnoreCase(BG_COUNTRY_CODE));
        assertTrue(betl.getSchemeInformation().getTerritory().equalsIgnoreCase(BE_COUNTRY_CODE));

        TLSchemeInformation schemeInfoUpdated = tlEditSchemeInfoService.edit(eetl.getTlId(), betl.getSchemeInformation(), Tag.ELECTRONIC_ADDRESS.toString(), "");
        // CHECK RETURN
        assertTrue(CollectionUtils.isEqualCollection(schemeInfoUpdated.getSchemeOpeElectronic(), betl.getSchemeInformation().getSchemeOpeElectronic()));

        TL eetlUpdated = tlService.getTL(1);
        // CHECK XML FILE
        assertTrue(CollectionUtils.isEqualCollection(eetlUpdated.getSchemeInformation().getSchemeOpeElectronic(), betl.getSchemeInformation().getSchemeOpeElectronic()));
        assertFalse(CollectionUtils.isEqualCollection(eetlUpdated.getSchemeInformation().getSchemeOpeElectronic(), eetl.getSchemeInformation().getSchemeOpeElectronic()));
    }

    @Test
    public void editTslType() {
        TL eetl = tlService.getTL(1);
        TL betl = tlService.getTL(2);
        assertTrue(eetl.getSchemeInformation().getTerritory().equalsIgnoreCase(BG_COUNTRY_CODE));
        assertTrue(betl.getSchemeInformation().getTerritory().equalsIgnoreCase(BE_COUNTRY_CODE));

        TLSchemeInformation schemeInfoUpdated = tlEditSchemeInfoService.edit(eetl.getTlId(), betl.getSchemeInformation(), Tag.TSL_TYPE.toString(), "");
        // CHECK RETURN
        assertTrue(schemeInfoUpdated.getType().equalsIgnoreCase(betl.getSchemeInformation().getType()));

        TL eetlUpdated = tlService.getTL(1);
        // CHECK XML FILE
        assertTrue(eetlUpdated.getSchemeInformation().getType().equalsIgnoreCase(betl.getSchemeInformation().getType()));
    }

    @Test
    public void editStatusDeterm() {
        TL eetl = tlService.getTL(1);
        TL betl = tlService.getTL(2);
        assertTrue(eetl.getSchemeInformation().getTerritory().equalsIgnoreCase(BG_COUNTRY_CODE));
        assertTrue(betl.getSchemeInformation().getTerritory().equalsIgnoreCase(BE_COUNTRY_CODE));

        TLSchemeInformation schemeInfoUpdated = tlEditSchemeInfoService.edit(eetl.getTlId(), betl.getSchemeInformation(), Tag.STATUS_DETERMINATION.toString(), "");
        // CHECK RETURN
        assertTrue(schemeInfoUpdated.getStatusDetermination().equalsIgnoreCase(betl.getSchemeInformation().getStatusDetermination()));

        TL eetlUpdated = tlService.getTL(1);
        // CHECK XML FILE
        assertTrue(eetlUpdated.getSchemeInformation().getStatusDetermination().equalsIgnoreCase(betl.getSchemeInformation().getStatusDetermination()));
    }

    @Test
    public void editSchemeTypeCommunity() {
        TL eetl = tlService.getTL(1);
        TL betl = tlService.getTL(2);
        assertTrue(eetl.getSchemeInformation().getTerritory().equalsIgnoreCase(BG_COUNTRY_CODE));
        assertTrue(betl.getSchemeInformation().getTerritory().equalsIgnoreCase(BE_COUNTRY_CODE));

        TLSchemeInformation schemeInfoUpdated = tlEditSchemeInfoService.edit(eetl.getTlId(), betl.getSchemeInformation(), Tag.SCHEME_TYPE_COMMUNITY_RULES.toString(), "");
        // CHECK RETURN
        assertTrue(CollectionUtils.isEqualCollection(schemeInfoUpdated.getSchemeTypeCommRule(), betl.getSchemeInformation().getSchemeTypeCommRule()));

        TL eetlUpdated = tlService.getTL(1);
        // CHECK XML FILE
        assertTrue(CollectionUtils.isEqualCollection(eetlUpdated.getSchemeInformation().getSchemeTypeCommRule(), betl.getSchemeInformation().getSchemeTypeCommRule()));
    }

    @Test
    public void editSchemeName() {
        TL eetl = tlService.getTL(1);
        TL betl = tlService.getTL(2);
        assertTrue(eetl.getSchemeInformation().getTerritory().equalsIgnoreCase(BG_COUNTRY_CODE));
        assertTrue(betl.getSchemeInformation().getTerritory().equalsIgnoreCase(BE_COUNTRY_CODE));

        TLSchemeInformation schemeInfoUpdated = tlEditSchemeInfoService.edit(eetl.getTlId(), betl.getSchemeInformation(), Tag.SCHEME_NAME.toString(), "");
        // CHECK RETURN
        assertTrue(CollectionUtils.isEqualCollection(schemeInfoUpdated.getSchemeName(), betl.getSchemeInformation().getSchemeName()));

        TL eetlUpdated = tlService.getTL(1);
        // CHECK XML FILE
        assertTrue(CollectionUtils.isEqualCollection(eetlUpdated.getSchemeInformation().getSchemeName(), betl.getSchemeInformation().getSchemeName()));
    }

    @Test
    public void editPolicyOrLegal() {
        TL eetl = tlService.getTL(1);
        TL betl = tlService.getTL(2);
        assertTrue(eetl.getSchemeInformation().getTerritory().equalsIgnoreCase(BG_COUNTRY_CODE));
        assertTrue(betl.getSchemeInformation().getTerritory().equalsIgnoreCase(BE_COUNTRY_CODE));

        TLSchemeInformation schemeInfoUpdated = tlEditSchemeInfoService.edit(eetl.getTlId(), betl.getSchemeInformation(), Tag.POLICY_OR_LEGAL_NOTICE.toString(), "");
        // CHECK RETURN
        assertTrue(CollectionUtils.isEqualCollection(schemeInfoUpdated.getSchemePolicy(), betl.getSchemeInformation().getSchemePolicy()));

        TL eetlUpdated = tlService.getTL(1);
        // CHECK XML FILE
        assertTrue(CollectionUtils.isEqualCollection(eetlUpdated.getSchemeInformation().getSchemePolicy(), betl.getSchemeInformation().getSchemePolicy()));
    }

    @Test
    public void editDistributionList() {
        TL eetl = tlService.getTL(1);
        TL betl = tlService.getTL(2);
        assertTrue(eetl.getSchemeInformation().getTerritory().equalsIgnoreCase(BG_COUNTRY_CODE));
        assertTrue(betl.getSchemeInformation().getTerritory().equalsIgnoreCase(BE_COUNTRY_CODE));

        TLSchemeInformation schemeInfoUpdated = tlEditSchemeInfoService.edit(eetl.getTlId(), betl.getSchemeInformation(), Tag.DISTRIBUTION_LIST.toString(), "");
        // CHECK RETURN
        assertTrue(CollectionUtils.isEqualCollection(schemeInfoUpdated.getDistributionPoint(), betl.getSchemeInformation().getDistributionPoint()));

        TL eetlUpdated = tlService.getTL(1);
        // CHECK XML FILE
        assertTrue(CollectionUtils.isEqualCollection(eetlUpdated.getSchemeInformation().getDistributionPoint(), betl.getSchemeInformation().getDistributionPoint()));
    }

    @Test
    public void editInformatioNUriList() {
        TL eetl = tlService.getTL(1);
        TL betl = tlService.getTL(2);
        assertTrue(eetl.getSchemeInformation().getTerritory().equalsIgnoreCase(BG_COUNTRY_CODE));
        assertTrue(betl.getSchemeInformation().getTerritory().equalsIgnoreCase(BE_COUNTRY_CODE));

        TLSchemeInformation schemeInfoUpdated = tlEditSchemeInfoService.edit(eetl.getTlId(), betl.getSchemeInformation(), Tag.SCHEME_INFORMATION_URI.toString(), "");
        // CHECK RETURN
        assertTrue(CollectionUtils.isEqualCollection(schemeInfoUpdated.getSchemeInfoUri(), betl.getSchemeInformation().getSchemeInfoUri()));

        TL eetlUpdated = tlService.getTL(1);
        // CHECK XML FILE
        assertTrue(CollectionUtils.isEqualCollection(eetlUpdated.getSchemeInformation().getSchemeInfoUri(), betl.getSchemeInformation().getSchemeInfoUri()));
    }

    @Test
    public void editDefaultList() {
        TL eetl = tlService.getTL(1);
        TL betl = tlService.getTL(2);
        assertTrue(eetl.getSchemeInformation().getTerritory().equalsIgnoreCase(BG_COUNTRY_CODE));
        assertTrue(betl.getSchemeInformation().getTerritory().equalsIgnoreCase(BE_COUNTRY_CODE));

        betl.getSchemeInformation().getSchemeInfoUri().get(0).setLanguage("CZ");

        TLSchemeInformation schemeInfoUpdated = tlEditSchemeInfoService.edit(eetl.getTlId(), betl.getSchemeInformation(), Tag.KEY_USAGE_BIT.toString(), "");
        // CHECK RETURN
        assertFalse(CollectionUtils.isEqualCollection(schemeInfoUpdated.getSchemeInfoUri(), betl.getSchemeInformation().getSchemeInfoUri()));

        TL eetlUpdated = tlService.getTL(1);
        // CHECK XML FILE
        assertFalse(CollectionUtils.isEqualCollection(eetlUpdated.getSchemeInformation().getSchemeInfoUri(), betl.getSchemeInformation().getSchemeInfoUri()));
    }
    
    @Test
    public void editHistPeriod() {
        int vaeee = 90;
         
        TL eetl = tlService.getTL(1);
        assertTrue(eetl.getSchemeInformation().getTerritory().equalsIgnoreCase(BG_COUNTRY_CODE));
    
        TLSchemeInformation tlScheme = new TLSchemeInformation();
        tlScheme.setHistoricalPeriod(vaeee);

        TLSchemeInformation schemeInfoUpdated = tlEditSchemeInfoService.edit(eetl.getTlId(), tlScheme, Tag.HISTORICAL_PERIOD.toString(), "");
        // CHECK RETURN
        assertTrue(schemeInfoUpdated.getHistoricalPeriod() == vaeee);

        TL eetlUpdated = tlService.getTL(1);
        // CHECK XML FILE
        assertTrue(eetlUpdated.getSchemeInformation().getHistoricalPeriod() == vaeee);
    }
    
    @Test
    public void editTslTag() {
        String tag = "test";
        
        TL eetl = tlService.getTL(1);
        TL betl = tlService.getTL(2);
        assertTrue(eetl.getSchemeInformation().getTerritory().equalsIgnoreCase(BG_COUNTRY_CODE));
        assertTrue(betl.getSchemeInformation().getTerritory().equalsIgnoreCase(BE_COUNTRY_CODE));

        tlEditSchemeInfoService.edit(eetl.getTlId(), betl.getSchemeInformation(), Tag.TSL_TAG.toString(), tag);
        TL tlUpdated = tlService.getTL(1);
        // CHECK RETURN
        assertTrue(tlUpdated.getTslTag().equalsIgnoreCase(tag));
    }
    
    @Test
    public void editExtensions() {
        boolean critical = true;
        String vaeee = "test";
        
        TL eetl = tlService.getTL(1);
        TL betl = tlService.getTL(2);
        assertTrue(eetl.getSchemeInformation().getTerritory().equalsIgnoreCase(BG_COUNTRY_CODE));
        assertTrue(betl.getSchemeInformation().getTerritory().equalsIgnoreCase(BE_COUNTRY_CODE));

        List<TLInformationExtension> exts = new ArrayList<TLInformationExtension>();
        TLInformationExtension ext = new TLInformationExtension();
        ext.setCritical(critical);
        ext.setValue(vaeee);
        exts.add(ext);
        betl.getSchemeInformation().setExtensions(exts);
        
        TLSchemeInformation schemeInfoUpdated = tlEditSchemeInfoService.edit(eetl.getTlId(), betl.getSchemeInformation(), Tag.SCHEME_EXTENSION.toString(), "");
        // CHECK RETURN
        assertTrue(schemeInfoUpdated.getExtensions().get(0).getCritical() == critical);
        assertTrue(schemeInfoUpdated.getExtensions().get(0).getValue().equals(vaeee));

        TL eetlUpdated = tlService.getTL(1);
        // CHECK XML FILE
        assertTrue(eetlUpdated.getSchemeInformation().getExtensions().get(0).getCritical() == critical);
        assertTrue(eetlUpdated.getSchemeInformation().getExtensions().get(0).getValue().equals(vaeee));
    }
}
