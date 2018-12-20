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
package eu.europa.ec.joinup.tsl.business.dto.tl;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import eu.europa.esig.dss.DSSUtils;
import eu.europa.esig.dss.x509.CertificateToken;

public class CertificateTest {

    public final static String base64 = "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";

    @Test
    public void testCertificate() {

        CertificateToken certToken = DSSUtils.loadCertificateFromBase64EncodedString(base64);
        TLCertificate cert = new TLCertificate(certToken.getEncoded());
        assertNotNull(cert);

    }
}
