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

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;
import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceDto;
import eu.europa.ec.joinup.tsl.model.enums.TLStatus;
import eu.europa.ec.joinup.tsl.model.enums.TLType;

@Transactional
public class TLEditServiceServiceTest extends AbstractSpringTest {

    private static final String BG_COUNTRY_CODE = "BG";
    private static final String BE_COUNTRY_CODE = "BE";

    @Autowired
    private TLLoader tlLoader;

    @Autowired
    private TLService tlService;

    @Autowired
    private TlEditServiceService tlEditServiceService;

    @Before
    public void initialize() {
        tlLoader.loadTL(BG_COUNTRY_CODE, "http://crc.bg/files/_en/TSL_BG.xml", TLType.TL, TLStatus.DRAFT);
        tlLoader.loadTL(BE_COUNTRY_CODE, "http://tsl.belgium.be/tsl-be.xml", TLType.TL, TLStatus.DRAFT);

        TL eetl = tlService.getTL(1);
        assertTrue(eetl.getSchemeInformation().getTerritory().equalsIgnoreCase(BG_COUNTRY_CODE));

        TL betl = tlService.getTL(2);
        assertTrue(betl.getSchemeInformation().getTerritory().equalsIgnoreCase(BE_COUNTRY_CODE));
    }

    @Test
    public void addSvc() {

        TL eetl = tlService.getTL(1);
        assertTrue(eetl.getSchemeInformation().getTerritory().equalsIgnoreCase(BG_COUNTRY_CODE));

        int nbreSvc = eetl.getServiceProviders().get(0).getTSPServices().size();
        TLServiceDto svc = eetl.getServiceProviders().get(0).getTSPServices().get(0);
        svc.setId("");
        List<Integer> intList = new ArrayList<>();
        intList.add(0);
        TLServiceDto svcUpdated = tlEditServiceService.edit(eetl.getTlId(), svc, intList);
        // CHECK RETURN
        assertTrue(svcUpdated.getId().equalsIgnoreCase(""));

        TL tlUpdated = tlService.getTL(1);
        assertTrue((tlUpdated.getServiceProviders().get(0).getTSPServices().size() - 1) == nbreSvc);
    }

    @Test
    public void editSvc() {
        TL eetl = tlService.getTL(1);
        assertTrue(eetl.getSchemeInformation().getTerritory().equalsIgnoreCase(BG_COUNTRY_CODE));

        TL betl = tlService.getTL(2);
        assertTrue(betl.getSchemeInformation().getTerritory().equalsIgnoreCase(BE_COUNTRY_CODE));

        int nbreSvc = eetl.getServiceProviders().get(0).getTSPServices().size();

        TLServiceDto svcDe = eetl.getServiceProviders().get(0).getTSPServices().get(0);
        TLServiceDto svcBe = betl.getServiceProviders().get(0).getTSPServices().get(0);

        svcBe.setId(svcDe.getId());

        List<Integer> intList = new ArrayList<>();
        intList.add(0);
        TLServiceDto svcUpdated = tlEditServiceService.edit(eetl.getTlId(), svcBe, intList);

        assertTrue(svcUpdated.getId().equalsIgnoreCase(svcDe.getId()));

        TL tlUpdated = tlService.getTL(1);
        assertTrue(tlUpdated.getServiceProviders().get(0).getTSPServices().size() == nbreSvc);
        assertTrue(tlUpdated.getServiceProviders().get(0).getTSPServices().get(0).getCurrentStatus().equalsIgnoreCase(svcBe.getCurrentStatus()));
    }

    @Test
    public void deleteSvc() {

        TL eetl = tlService.getTL(1);
        assertTrue(eetl.getSchemeInformation().getTerritory().equalsIgnoreCase(BG_COUNTRY_CODE));

        int nbreSvc = eetl.getServiceProviders().get(0).getTSPServices().size();

        int nbre = tlEditServiceService.delete(eetl.getTlId(), eetl.getServiceProviders().get(0).getTSPServices().get(0));
        // CHECK RETURN
        assertTrue(nbre == 1);

        TL tlUpdated = tlService.getTL(1);
        assertTrue((tlUpdated.getServiceProviders().get(0).getTSPServices().size() + 1) == nbreSvc);

    }

}
