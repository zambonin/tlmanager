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

import static org.junit.Assert.assertTrue;

import javax.transaction.Transactional;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;
import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLPointersToOtherTSL;
import eu.europa.ec.joinup.tsl.model.enums.TLStatus;
import eu.europa.ec.joinup.tsl.model.enums.TLType;
import eu.europa.ec.joinup.tsl.model.enums.Tag;

@Transactional
public class TLEditPointerServiceTest extends AbstractSpringTest {

    private static final String EU_COUNTRY_CODE = "EU";
    private static final String BE_COUNTRY_CODE = "BE";

    @Autowired
    private TLLoader tlLoader;

    @Autowired
    private TLService tlService;

    @Autowired
    private TlEditPointerService tlEditPointerService;

    @Before
    public void initialize() {
        tlLoader.loadTL(EU_COUNTRY_CODE, "https://ec.europa.eu/information_society/policy/esignature/trusted-list/tl-mp.xml", TLType.TL, TLStatus.DRAFT);
        tlLoader.loadTL(BE_COUNTRY_CODE, "http://tsl.belgium.be/tsl-be.xml", TLType.TL, TLStatus.DRAFT);

        TL eutl = tlService.getTL(1);
        TL betl = tlService.getTL(2);
        assertTrue(eutl.getSchemeInformation().getTerritory().equalsIgnoreCase(EU_COUNTRY_CODE));
        assertTrue(betl.getSchemeInformation().getTerritory().equalsIgnoreCase(BE_COUNTRY_CODE));
    }

    @Test
    public void addPointer() {

        TL eutl = tlService.getTL(1);
        TL betl = tlService.getTL(2);
        assertTrue(eutl.getSchemeInformation().getTerritory().equalsIgnoreCase(EU_COUNTRY_CODE));
        assertTrue(betl.getSchemeInformation().getTerritory().equalsIgnoreCase(BE_COUNTRY_CODE));

        int nbreEU = eutl.getPointers().size();
        TLPointersToOtherTSL newPointer = betl.getPointers().get(0);
        newPointer.setId("");
        TLPointersToOtherTSL pointerUpdated = tlEditPointerService.edit(eutl.getTlId(), newPointer, Tag.ISSUE_DATE.toString());
        // CHECK RETURN
        assertTrue(pointerUpdated.getId().equalsIgnoreCase(""));

        TL eutlUpdated = tlService.getTL(1);
        assertTrue((eutlUpdated.getPointers().size() - 1) == nbreEU);
    }

    @Test
    public void editPointer() {

        TL eutl = tlService.getTL(1);
        TL betl = tlService.getTL(2);
        assertTrue(eutl.getSchemeInformation().getTerritory().equalsIgnoreCase(EU_COUNTRY_CODE));
        assertTrue(betl.getSchemeInformation().getTerritory().equalsIgnoreCase(BE_COUNTRY_CODE));

        int nbreEU = eutl.getPointers().size();

        TLPointersToOtherTSL edtPointer = eutl.getPointers().get(0);
        TLPointersToOtherTSL newPointer = betl.getPointers().get(0);
        edtPointer.setMimeType(newPointer.getMimeType());
        edtPointer.setSchemeOpeName(newPointer.getSchemeOpeName());
        edtPointer.setSchemeTerritory(newPointer.getSchemeTerritory());
        edtPointer.setSchemeTypeCommunity(newPointer.getSchemeTypeCommunity());
        edtPointer.setServiceDigitalId(newPointer.getServiceDigitalId());
        edtPointer.setTlLocation(newPointer.getTlLocation());

        TLPointersToOtherTSL pointerUpdated = tlEditPointerService.edit(eutl.getTlId(), edtPointer, "");
        // CHECK RETURN
        assertTrue(pointerUpdated.getId().equalsIgnoreCase(eutl.getPointers().get(0).getId()));

        TL eutlUpdated = tlService.getTL(1);
        assertTrue((eutlUpdated.getPointers().size()) == nbreEU);
        TLPointersToOtherTSL editedPointer = eutl.getPointers().get(0);
        assertTrue(editedPointer.getId().equalsIgnoreCase(edtPointer.getId()));

        assertTrue(editedPointer.getMimeType().equals(newPointer.getMimeType()));
        assertTrue(editedPointer.getSchemeTerritory().equals(newPointer.getSchemeTerritory()));
        assertTrue(editedPointer.getTlLocation().equals(newPointer.getTlLocation()));
        assertTrue(CollectionUtils.isEqualCollection(editedPointer.getSchemeOpeName(), newPointer.getSchemeOpeName()));
        assertTrue(CollectionUtils.isEqualCollection(editedPointer.getSchemeTypeCommunity(), newPointer.getSchemeTypeCommunity()));
        assertTrue(CollectionUtils.isEqualCollection(editedPointer.getServiceDigitalId(), newPointer.getServiceDigitalId()));
    }

    @Test
    public void deletePointer() {

        TL eutl = tlService.getTL(1);
        assertTrue(eutl.getSchemeInformation().getTerritory().equalsIgnoreCase(EU_COUNTRY_CODE));

        int nbreEU = eutl.getPointers().size();

        int nbre = tlEditPointerService.delete(eutl.getTlId(), eutl.getPointers().get(0), "");
        // CHECK RETURN
        assertTrue(nbre == 1);

        TL eutlUpdated = tlService.getTL(1);
        assertTrue((eutlUpdated.getPointers().size() + 1) == nbreEU);
    }

}
