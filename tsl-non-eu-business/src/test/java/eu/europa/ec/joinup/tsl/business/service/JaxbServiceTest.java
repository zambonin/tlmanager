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

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;
import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.esig.jaxb.v5.tsl.TrustStatusListTypeV5;

public class JaxbServiceTest extends AbstractSpringTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(JaxbServiceTest.class);

    @Autowired
    private TrustedListJaxbService jaxbService;

    @Autowired
    private TLBuilder tlBuilder;

    @Test
    public void testUnmashallMarshallTSL() throws Exception {
        InputStream is = new FileInputStream("src/test/resources/lotl.xml");
        TrustStatusListTypeV5 tsl = jaxbService.unmarshallTSLV5(is);
        Assert.assertNotNull(tsl);
    }

    @Test
    public void testUnmashallMarshallBE() throws Exception {
        InputStream is = new FileInputStream("src/test/resources/tsl/BE-TEST/2016-10-13_12-55-38.xml");
        OutputStream os = new ByteArrayOutputStream();
        TrustStatusListTypeV5 tsl = jaxbService.unmarshallTSLV5(is);

        TL tl = tlBuilder.buildTLV5(1, tsl);

        TrustStatusListTypeV5 asTSLTypeV5 = tl.asTSLTypeV5();
        jaxbService.marshallTSL(asTSLTypeV5, os);
        IOUtils.closeQuietly(os);
        IOUtils.closeQuietly(is);

        LOGGER.info(os.toString());
    }

    @Test
    public void testUnmashallMarshallSK() throws Exception {
        InputStream is = new FileInputStream("src/test/resources/tsl/SK/2016-10-13_13-05-40.xml");
        OutputStream os = new ByteArrayOutputStream();
        TrustStatusListTypeV5 tsl = jaxbService.unmarshallTSLV5(is);

        TL tl = tlBuilder.buildTLV5(1, tsl);

        TrustStatusListTypeV5 asTSLTypeV5 = tl.asTSLTypeV5();
        jaxbService.marshallTSL(asTSLTypeV5, os);
        IOUtils.closeQuietly(os);
        IOUtils.closeQuietly(is);

        LOGGER.info(os.toString());
    }

    @Test
    public void testUnmashallMarshallATWithServiceHistory() throws Exception {
        InputStream is = new FileInputStream("src/test/resources/tsl/AT/2016-10-13_13-09-04.xml");
        OutputStream os = new ByteArrayOutputStream();
        TrustStatusListTypeV5 tsl = jaxbService.unmarshallTSLV5(is);

        TL tl = tlBuilder.buildTLV5(1, tsl);

        TrustStatusListTypeV5 asTSLTypeV5 = tl.asTSLTypeV5();
        jaxbService.marshallTSL(asTSLTypeV5, os);
        IOUtils.closeQuietly(os);
        IOUtils.closeQuietly(is);

        LOGGER.info(os.toString());
    }

}
