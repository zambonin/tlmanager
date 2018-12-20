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
package eu.europa.ec.joinup.tsl.business.rules;

import java.util.Date;
import java.util.Objects;

import javax.security.auth.x500.X500Principal;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.dto.tl.TLCertificate;
import eu.europa.ec.joinup.tsl.business.util.TLUtils;
import eu.europa.esig.dss.DSSUtils;
import eu.europa.esig.dss.utils.Utils;
import eu.europa.esig.dss.x509.CertificateToken;

@Service
public class DigitalIdentityValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(DigitalIdentityValidator.class);

    public boolean isBase64Certificate(byte[] certificateBinaries) {
        try {
            CertificateToken certificate = DSSUtils.loadCertificate(certificateBinaries);
            if (certificate != null) {
                return true;
            }
        } catch (Exception e) {
            LOGGER.debug("Certificate " + Base64.encodeBase64String(certificateBinaries) + " is not correct");
        }
        return false;
    }

    public boolean isCorrectX509SKI(byte[] ski, CertificateToken certificate) {
        try {
            byte[] expectedSki = TLUtils.getSki(certificate);
            if (!Objects.deepEquals(expectedSki, ski)) {
                LOGGER.debug("Wrong X509SKI detected " + Base64.encodeBase64String(ski) + " should be " + Base64.encodeBase64String(expectedSki));
            } else {
                return true;
            }
        } catch (Exception e) {
            LOGGER.debug("Unable to compute SKI for Certificate " + Utils.toBase64(certificate.getEncoded()));
        }
        return false;
    }

    public boolean isCorrectX509SubjectName(String subjectName, CertificateToken certificate) {
        X500Principal x500Principal = DSSUtils.getX500PrincipalOrNull(subjectName);
        if (!DSSUtils.x500PrincipalAreEquals(certificate.getSubjectX500Principal(), x500Principal)) {
            if (!certificate.getSubjectX500Principal().toString().equalsIgnoreCase(subjectName)) {
                LOGGER.debug("Wrong SubjectName detected " + x500Principal + " should be " + certificate.getSubjectX500Principal());
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public boolean isNotExpired(TLCertificate certificate) {
        Date currentDate = new Date();
        if (certificate.getCertAfter().before(currentDate)) {
            return false;
        }
        return true;
    }
}
