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
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceProvider;
import eu.europa.ec.joinup.tsl.model.enums.TLStatus;
import eu.europa.ec.joinup.tsl.model.enums.TLType;

@Transactional
public class TLEditServiceProviderServiceTest extends AbstractSpringTest {

    private static final String BG_COUNTRY_CODE = "BG";
    private static final String BE_COUNTRY_CODE = "BE";

    @Autowired
    private TLLoader tlLoader;

    @Autowired
    private TLService tlService;

    @Autowired
    private TlEditServiceProviderService tlEditServiceProviderService;

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

        int nbreSvc = eetl.getServiceProviders().size();
        TLServiceProvider svc = eetl.getServiceProviders().get(0);
        svc.setId("");
        TLServiceProvider svcUpdated = tlEditServiceProviderService.edit(eetl.getTlId(), svc);
        // CHECK RETURN
        assertTrue(svcUpdated.getId().equalsIgnoreCase(""));

        TL tlUpdated = tlService.getTL(1);
        assertTrue((tlUpdated.getServiceProviders().size() - 1) == nbreSvc);
    }

    @Test
    public void editSvc() {
        TL eetl = tlService.getTL(1);
        assertTrue(eetl.getSchemeInformation().getTerritory().equalsIgnoreCase(BG_COUNTRY_CODE));

        TL betl = tlService.getTL(2);
        assertTrue(betl.getSchemeInformation().getTerritory().equalsIgnoreCase(BE_COUNTRY_CODE));

        int nbreSvc = eetl.getServiceProviders().size();

        TLServiceProvider svcDe = eetl.getServiceProviders().get(0);
        TLServiceProvider svcBe = betl.getServiceProviders().get(0);

        svcBe.setId(svcDe.getId());

        TLServiceProvider svcUpdated = tlEditServiceProviderService.edit(eetl.getTlId(), svcBe);

        assertTrue(svcUpdated.getId().equalsIgnoreCase(svcDe.getId()));

        TL tlUpdated = tlService.getTL(1);
        assertTrue(tlUpdated.getServiceProviders().size() == nbreSvc);
        assertTrue(CollectionUtils.isEqualCollection(tlUpdated.getServiceProviders().get(0).getTSPName(), svcBe.getTSPName()));
    }

    @Test
    public void deleteSvc() {

        TL eetl = tlService.getTL(1);
        assertTrue(eetl.getSchemeInformation().getTerritory().equalsIgnoreCase(BG_COUNTRY_CODE));

        int nbreSvc = eetl.getServiceProviders().size();

        int nbre = tlEditServiceProviderService.delete(eetl.getTlId(), eetl.getServiceProviders().get(0));
        // CHECK RETURN
        assertTrue(nbre == 1);

        TL tlUpdated = tlService.getTL(1);
        assertTrue((tlUpdated.getServiceProviders().size() + 1) == nbreSvc);

    }

}
