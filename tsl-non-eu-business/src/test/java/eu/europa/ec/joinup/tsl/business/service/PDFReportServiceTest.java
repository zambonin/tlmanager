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
import java.util.Date;


import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;
import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.business.repository.TLRepository;
import eu.europa.ec.joinup.tsl.model.DBFiles;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.model.enums.MimeType;
import eu.europa.ec.joinup.tsl.model.enums.TLStatus;
import eu.europa.ec.joinup.tsl.model.enums.TLType;
import eu.europa.esig.jaxb.v5.tsl.TrustStatusListTypeV5;

public class PDFReportServiceTest extends AbstractSpringTest {

    @Autowired
    private PDFReportService reportService;

    @Autowired
    private TLBuilder tlBuilder;

    @Autowired
    private TrustedListJaxbService jaxbService;

    @Autowired
    private TLRepository tlRepository;

    @Autowired
    private RulesRunnerService rulesRunnerService;

    @Test
    @DirtiesContext
    public void testCheckResultOrdered() throws Exception {
        File file = new File("src/test/resources/orderedCheckTest.pdf");
        if (file.exists()) {
            file.delete();
        }

        int id = createTLinDB(TLType.TL);
        TrustStatusListTypeV5 tsl = jaxbService.unmarshallTSLV5(new File("src/test/resources/tsl/BE-TEST/2016-10-13_12-55-38.xml"));
        TL tl = tlBuilder.buildTLV5(id, tsl);
        tl.setDbCountryName("Belgium");
        tl.setDbName("DRAFT_BE");
        tl.setDbStatus(TLStatus.DRAFT);
        tl.setCheckEdited(new Date());
        tl.setTlId(id);

        DBTrustedLists dbTL = new DBTrustedLists();
        dbTL.setId(id);
        dbTL.setSequenceNumber(10000);
        dbTL.setName("db-tl");
        DBFiles xmlFile = new DBFiles();
        xmlFile.setMimeTypeFile(MimeType.XML);
        xmlFile.setLocalPath("src/test/resources/tsl/BE/2016-10-13_12-55-38.xml");
        xmlFile.setId(1000);
        dbTL.setXmlFile(xmlFile);
        dbTL.setType(TLType.TL);
        tlRepository.save(dbTL);
        rulesRunnerService.runAllRules(tl);

        FileOutputStream fos = new FileOutputStream(file);
        reportService.generateTLReport(tl, fos);

        Assert.assertTrue(file.exists());
    }

    private int createTLinDB(TLType type) {
        DBTrustedLists trustedList = new DBTrustedLists();
        trustedList.setType(type);
        trustedList.setXmlFile(new DBFiles());
        tlRepository.save(trustedList);
        return trustedList.getId();
    }

    @Test
    @DirtiesContext
    public void testCheckResultOrderedCZ() throws Exception {
        File file = new File("target/orderedCheckTestCZ.pdf");
        if (file.exists()) {
            file.delete();
        }

        int cztlId = createTLinDB(TLType.TL);
        TrustStatusListTypeV5 tsl = jaxbService.unmarshallTSLV5(new File("src/test/resources/tsl/CZ/2016-10-13_13-09-15.xml"));
        TL tl = tlBuilder.buildTLV5(cztlId, tsl);
        tl.setDbCountryName("Czech Republic");
        tl.setDbName("DRAFT - CZ test");
        tl.setDbStatus(TLStatus.DRAFT);
        tl.setCheckEdited(new Date());
        tl.setTlId(1);

        FileOutputStream fos = new FileOutputStream(file);
        reportService.generateTLReport(tl, fos);

        Assert.assertTrue(file.exists());
    }
}
