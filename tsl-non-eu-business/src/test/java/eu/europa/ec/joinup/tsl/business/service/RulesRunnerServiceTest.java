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
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;
import eu.europa.ec.joinup.tsl.business.dto.TLDifference;
import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLDigitalIdentification;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceDto;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceProvider;
import eu.europa.ec.joinup.tsl.business.repository.TLRepository;
import eu.europa.ec.joinup.tsl.model.DBFiles;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.model.enums.TLType;

public class RulesRunnerServiceTest extends AbstractSpringTest {

    @Autowired
    private TLRepository tlRepository;

    @Autowired
    private TrustedListJaxbService jaxbService;

    @Autowired
    private TLBuilder tlBuilder;

    @Test
    public void compareSK() throws Exception {
        int previousTLId = createTLinDB(TLType.TL);
        TL previous = tlBuilder.buildTLV5(previousTLId, jaxbService.unmarshallTSLV5(new File("src/test/resources/tsl/SK/2016-10-13_13-05-40.xml")));
        List<TLDifference> diffs = new ArrayList<>();
        for (TLServiceProvider tsp : previous.getServiceProviders()) {
            tsp.asPublishedDiff(tsp, tsp.getId());
            for (TLServiceDto service : tsp.getTSPServices()) {
                for (TLDigitalIdentification dit : service.getDigitalIdentification()) {
                    diffs.addAll(dit.asPublishedDiff(dit));
                }
            }
        }
    }

    private int createTLinDB(TLType type) {
        DBTrustedLists trustedList = new DBTrustedLists();
        trustedList.setType(type);
        trustedList.setXmlFile(new DBFiles());
        tlRepository.save(trustedList);
        return trustedList.getId();
    }

    // TODO(5.4.RC1) : Implement integration test

    // @Test
    // public void analyzeTL() throws Exception {
    //
    // int lotlId = createTLinDB(TLType.TL);
    //
    // TrustStatusListTypeV5 tsl = jaxbService.unmarshallTSLV5(new File("src/test/resources/tsl/AT/2016-10-13_13-09-04.xml"));
    // TL tl = tlBuilder.buildTLV5(lotlId, tsl);
    // System.out.println("************** runAllRules for TL : " + tl.getServiceProviders().size());
    // rulesRunnerService.runAllRules(tl, null);
    //
    // List<CheckDTO> checkResults = checkService.getTLChecksResult(lotlId);
    // assertTrue(CollectionUtils.isNotEmpty(checkResults));
    //
    // rulesRunnerService.runAllRules(tl, null);
    // }

    // @Test
    // public void analyzeTLServiceProvider() throws Exception {
    //
    // int previousTLId = createTLinDB(TLType.TL);
    // TL previous = tlBuilder.buildTL(previousTLId, jaxbService.unmarshallTSL(new
    // File("src/test/resources/tsl/BE-TEST/2016-10-13_12-55-39-bis.xml")));
    //
    // rulesRunnerService.validateAllServiceProvider(previousTLId, previous.getServiceProviders());
    //
    // List<CheckDTO> checkResults = checkService.getTLChecksResult(previousTLId);
    // assertTrue(CollectionUtils.isNotEmpty(checkResults));
    //
    // }

    // @Test
    // public void analyzeTLPointers() throws Exception {
    // int previousTLId = createTLinDB(TLType.TL);
    // rulesRunnerService.validateAllPointers(previousTLId);
    //
    // List<CheckDTO> checkResults = checkService.getTLChecksResult(previousTLId);
    // assertTrue(CollectionUtils.isNotEmpty(checkResults));
    //
    // }

}
