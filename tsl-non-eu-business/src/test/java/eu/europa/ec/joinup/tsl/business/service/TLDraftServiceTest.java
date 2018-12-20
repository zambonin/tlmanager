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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;
import eu.europa.ec.joinup.tsl.business.dto.TrustedListsReport;
import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLSchemeInformation;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceProvider;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.model.enums.SignatureStatus;
import eu.europa.ec.joinup.tsl.model.enums.TLStatus;
import eu.europa.ec.joinup.tsl.model.enums.TLType;

public class TLDraftServiceTest extends AbstractSpringTest {

    @Autowired
    private TLService tlService;

    @Autowired
    private TLDraftService draftService;

    @Autowired
    private TLValidator tlValidator;

    @Autowired
    private DraftStoreService draftStoreService;

    @Test
    public void createDraftFromBinaries() throws Exception {
        byte[] byteArray = IOUtils.toByteArray(new FileInputStream("src/test/resources/tsl/AT/2016-10-13_13-09-04.xml"));
        DBTrustedLists draftTSL = draftService.createDraftFromXML(byteArray, draftStoreService.getNewDraftStore(), "test-man");
        assertNotNull(draftTSL);
        assertEquals("AT", draftTSL.getTerritory().getCodeTerritory());
        assertEquals(TLType.TL, draftTSL.getType());
        assertEquals(TLStatus.DRAFT, draftTSL.getStatus());
        assertEquals(29, draftTSL.getSequenceNumber());
        assertNotNull(draftTSL.getIssueDate());
        assertNotNull(draftTSL.getNextUpdateDate());
        assertTrue(StringUtils.isNotEmpty(draftTSL.getName()));

        tlValidator.checkTlWithKeyStore(draftTSL);
        TrustedListsReport report = tlService.getTLInfo(draftTSL.getId());
        assertNotNull(report);
    }
    
    @Test
    public void createEmptyDraft() throws Exception {
        String territory = "BE";
        DBTrustedLists draft = draftService.createEmptyDraft(draftStoreService.getNewDraftStore(), "test-man", territory);
        TrustedListsReport tlr = draftService.finalizeDraftCreation(draft, "test-man");
        
        TL tl = tlService.getTL(tlr.getId());
        TLSchemeInformation TLinfos = tl.getSchemeInformation();
        assertTrue(TLinfos.getSequenceNumber() == 1);
        assertEquals(TLinfos.getTerritory(), territory);
        assertTrue(TLinfos.getTlIdentifier() == 5);
        assertEquals(tlr.getTlStatus(), TLStatus.DRAFT);
        assertEquals(tlr.getSigStatus(), SignatureStatus.NOT_SIGNED);
        assertNotNull(TLinfos.getNextUpdateDate());
        
        // Some empty fields
        assertEquals(tl.getServiceProviders(), new ArrayList<TLServiceProvider>());
    }

    @Test
    public void duplicateDraft()  throws Exception {
        // Version to duplicate
        byte[] byteArray = IOUtils.toByteArray(new FileInputStream("src/test/resources/tsl/AT/2016-10-13_13-09-04.xml"));
        DBTrustedLists draftTSL = draftService.createDraftFromXML(byteArray, draftStoreService.getNewDraftStore(), "test-man");
        TrustedListsReport tlr1 = draftService.finalizeDraftCreation(draftTSL, "test-man");
        
        // Duplicate
        DBTrustedLists db2 = draftService.duplicateDraft(tlr1, "test-man");
        TrustedListsReport tlr2 = draftService.finalizeDraftCreation(db2, "test-man");
        
        // Tests
        assertNotEquals(tlr1.getId(), tlr2.getId());
        assertEquals(tlr1.getName()+"_dup", tlr2.getName());
        assertEquals(tlr1.getTerritoryCode(), tlr2.getTerritoryCode());
        assertEquals(tlr1.getTerritoryCode(), tlr2.getTerritoryCode());
        assertEquals(tlr2.getSigStatus(), SignatureStatus.NOT_SIGNED);
    }

    @Test
    public void renameDraft() throws Exception {
        String name = "test";
                
        byte[] byteArray = IOUtils.toByteArray(new FileInputStream("src/test/resources/tsl/AT/2016-10-13_13-09-04.xml"));
        DBTrustedLists draftTSL = draftService.createDraftFromXML(byteArray, draftStoreService.getNewDraftStore(), "test-man");
                
        // Rename
        TL updatedDraft = draftService.renameDraft(draftTSL.getId(), name);
        DBTrustedLists dbDraft = tlService.getDbTL(draftTSL.getId());
                
        // Tests
        assertEquals(updatedDraft.getDbName(), name);
        assertEquals(dbDraft.getName(), name);
    }
}
