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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import eu.europa.ec.joinup.tsl.business.dto.TLDifference;
import eu.europa.ec.joinup.tsl.business.util.AnyTypeUtils;
import eu.europa.ec.joinup.tsl.business.util.TLDigitalIdentityUtils;
import eu.europa.ec.joinup.tsl.business.util.TLUtils;
import eu.europa.ec.joinup.tsl.model.enums.Tag;
import eu.europa.esig.dss.DSSUtils;
import eu.europa.esig.dss.x509.CertificateToken;
import eu.europa.esig.jaxb.v5.tsl.AnyTypeV5;
import eu.europa.esig.jaxb.v5.tsl.DigitalIdentityListTypeV5;
import eu.europa.esig.jaxb.v5.tsl.DigitalIdentityTypeV5;

public class TLDigitalIdentification extends AbstractTLDTO {

    private static final Logger LOGGER = LoggerFactory.getLogger(TLDigitalIdentification.class);
    private String subjectName;
    private byte[] x509ski;
    private List<Object> other;
    private List<TLCertificate> certificateList;

    public TLDigitalIdentification() {
    }

    public TLDigitalIdentification(int iddb, String location, DigitalIdentityListTypeV5 identityList) {
        super(iddb, location);
        List<TLCertificate> list = new ArrayList<>();
        int i = 0;
        List<Object> objFinal = new ArrayList<>();
        for (DigitalIdentityTypeV5 dit : identityList.getDigitalId()) {
            if (dit.getX509Certificate() != null) {
                i++;
                list.add(new TLCertificate(iddb, location + "_" + Tag.X509_CERTIFICATE + "_" + i, dit.getX509Certificate()));
            } else if (dit.getOther() != null) {
                List<Object> objList = dit.getOther().getContent();
                for (Object obj : objList) {
                    String str = AnyTypeUtils.convertOtherTag(obj);
                    if (!StringUtils.isEmpty(str)) {
                        objFinal.add(str);
                    }
                }
            } else if (dit.getX509SubjectName() != null) {
                subjectName = dit.getX509SubjectName();
            } else if (dit.getX509SKI() != null) {
                x509ski = dit.getX509SKI();
            }
        }
        if (objFinal.size() > 0) {
            setOther(objFinal);
        }
        if (list.size() > 0) {
            setCertificateList(list);
        }
    }

    public TLDigitalIdentification(byte[] input) {
        super(0, "0");

        List<TLCertificate> list = new ArrayList<>();

        TLCertificate certificate = new TLCertificate(input);

        if (certificate.getToken() != null) {
            list.add(certificate);
            setCertificateList(list);
            setSubjectName(certificate.getToken().getSubjectX500Principal().toString());
            setX509ski(TLUtils.getSki(certificate.getToken()));
        } else {
            setCertificateList(null);
        }
    }

    public DigitalIdentityListTypeV5 asTSLTypeV5() {
        DigitalIdentityListTypeV5 digiType = new DigitalIdentityListTypeV5();

        if (getCertificateList() != null) {
            for (TLCertificate cert : getCertificateList()) {
                DigitalIdentityTypeV5 tslDigitalId = new DigitalIdentityTypeV5();
                tslDigitalId.setX509Certificate(cert.getCertEncoded());
                digiType.getDigitalId().add(tslDigitalId);
            }

            if (!StringUtils.isEmpty(getSubjectName())) {
                DigitalIdentityTypeV5 tslDigitalId2 = new DigitalIdentityTypeV5();
                tslDigitalId2.setX509SubjectName(getSubjectName());
                digiType.getDigitalId().add(tslDigitalId2);
            }
            if ((getX509ski() != null) && (getX509ski().length > 0)) {
                DigitalIdentityTypeV5 tslDigitalId3 = new DigitalIdentityTypeV5();
                tslDigitalId3.setX509SKI(getX509ski());
                digiType.getDigitalId().add(tslDigitalId3);
            }

        } else {
            // No certificate, but perhaps SKI and/Or SUbject Name --> Historic!
            // IF USER WANT TO PUBLISH IN MP XML
            if (!StringUtils.isEmpty((getSubjectName()))) {
                DigitalIdentityTypeV5 tslDigitalIdSN = new DigitalIdentityTypeV5();
                tslDigitalIdSN.setX509SubjectName(getSubjectName());
                digiType.getDigitalId().add(tslDigitalIdSN);
            }

            if ((getX509ski() != null) && (getX509ski().length > 0)) {
                DigitalIdentityTypeV5 tslDigitalIdSKI = new DigitalIdentityTypeV5();
                tslDigitalIdSKI.setX509SKI(getX509ski());
                digiType.getDigitalId().add(tslDigitalIdSKI);
            }
        }

        if (getOther() != null) {
            for (Object o : getOther()) {
                AnyTypeV5 others = new AnyTypeV5();
                others.getContent().add(o);
                DigitalIdentityTypeV5 tslDigitalId3 = new DigitalIdentityTypeV5();
                tslDigitalId3.setOther(others);
                digiType.getDigitalId().add(tslDigitalId3);

            }
        }

        return digiType;
    }

    public DigitalIdentityListTypeV5 asHistoricTSLTypeV5(TLCertificate serviceCert) {
        DigitalIdentityListTypeV5 digiType = new DigitalIdentityListTypeV5();
        String tmpCertSubjectName = "";
        byte[] tmpCertSki = null;
        if (getCertificateList() != null) {

            for (int i = 0; i < getCertificateList().size(); i++) {
                CertificateToken certT = DSSUtils.loadCertificate(serviceCert.getCertEncoded());
                tmpCertSubjectName = certT.getSubjectX500Principal().toString();
                tmpCertSki = TLUtils.getSki(certT);
            }

            if (!StringUtils.isEmpty(getSubjectName())) {
                DigitalIdentityTypeV5 tslDigitalId2 = new DigitalIdentityTypeV5();
                tslDigitalId2.setX509SubjectName(getSubjectName());
                digiType.getDigitalId().add(tslDigitalId2);
            } else {
                DigitalIdentityTypeV5 tslDigitalId2 = new DigitalIdentityTypeV5();
                tslDigitalId2.setX509SubjectName(tmpCertSubjectName);
                digiType.getDigitalId().add(tslDigitalId2);
            }

            if ((getX509ski() != null) && (getX509ski().length > 0)) {
                DigitalIdentityTypeV5 tslDigitalId3 = new DigitalIdentityTypeV5();
                tslDigitalId3.setX509SKI(getX509ski());
                digiType.getDigitalId().add(tslDigitalId3);
            } else {
                DigitalIdentityTypeV5 tslDigitalId3 = new DigitalIdentityTypeV5();
                LOGGER.debug("SKI to set : " + tmpCertSki);
                tslDigitalId3.setX509SKI(tmpCertSki);
                digiType.getDigitalId().add(tslDigitalId3);

            }

        } else {
            // No certificate, but perhaps SKI and/Or SUbject Name --> Historic!
            // IF USER WANT TO PUBLISH IN MP XML
            if (serviceCert != null) {
                CertificateToken certT = DSSUtils.loadCertificate(serviceCert.getCertEncoded());
                tmpCertSubjectName = certT.getSubjectX500Principal().toString();
                tmpCertSki = TLUtils.getSki(certT);
            }

            DigitalIdentityTypeV5 tslDigitalIdSN = new DigitalIdentityTypeV5();
            if (!StringUtils.isEmpty(getSubjectName())) {
                tslDigitalIdSN.setX509SubjectName(getSubjectName());
                digiType.getDigitalId().add(tslDigitalIdSN);
            } else {
                if (!StringUtils.isEmpty(tmpCertSubjectName)) {
                    tslDigitalIdSN.setX509SubjectName(tmpCertSubjectName);
                    digiType.getDigitalId().add(tslDigitalIdSN);
                }
            }

            DigitalIdentityTypeV5 tslDigitalIdSKI = new DigitalIdentityTypeV5();
            if ((getX509ski() != null) && (getX509ski().length > 0)) {
                tslDigitalIdSKI.setX509SKI(getX509ski());
                digiType.getDigitalId().add(tslDigitalIdSKI);
            } else {
                if (tmpCertSki != null) {
                    tslDigitalIdSKI.setX509SKI(tmpCertSki);
                    digiType.getDigitalId().add(tslDigitalIdSKI);
                }
            }
        }

        AnyTypeV5 others = new AnyTypeV5();
        if (getOther() != null) {
            for (Object o : getOther()) {
                others.getContent().add(o);
                DigitalIdentityTypeV5 tslDigitalId3 = new DigitalIdentityTypeV5();
                tslDigitalId3.setOther(others);
                digiType.getDigitalId().add(tslDigitalId3);

            }
        }

        return digiType;
    }

    public List<TLDifference> asPublishedDiff(TLDigitalIdentification publishedTl) {
        List<TLDifference> diffList = new ArrayList<>();

        // Certificates
        List<TLCertificate> currentCertificates = new ArrayList<>();
        if (getCertificateList() != null) {
            currentCertificates = getCertificateList();
        }
        // Certificates : removed
        List<TLCertificate> publishedCertificates = new ArrayList<>();
        if (publishedTl.getCertificateList() != null) {
            publishedCertificates = publishedTl.getCertificateList();
        }
        // Certificates : Added
        for (TLCertificate certificate : currentCertificates) {
            if (!publishedCertificates.contains(certificate)) {
                diffList.add(new TLDifference(certificate.getId(), "", certificate.certToString()));
            }

        }
        // Certificates: Deleted
        for (TLCertificate certificate : publishedCertificates) {
            if (!currentCertificates.contains(certificate)) {
                diffList.add(new TLDifference(getId() + "_" + Tag.X509_CERTIFICATE, certificate.certToString(), ""));
            }

        }

        // SubjectName
        if (getSubjectName() != null) {
            if (publishedTl.getSubjectName() != null) {
                if (!getSubjectName().equals(publishedTl.getSubjectName())) {
                    diffList.add(new TLDifference(getId() + "_" + Tag.X509_SUBJECT_NAME, publishedTl.getSubjectName().toString(), getSubjectName().toString()));
                }
            } else {
                diffList.add(new TLDifference(getId() + "_" + Tag.X509_SUBJECT_NAME, "", getSubjectName().toString()));
            }
        } else if (publishedTl.getSubjectName() != null) {
            diffList.add(new TLDifference(getId() + "_" + Tag.X509_SUBJECT_NAME, publishedTl.getSubjectName().toString(), ""));
        }

        // Init Other Tag
        List<String> currentOther = new ArrayList<>();
        List<String> publishedOther = new ArrayList<>();
        if (getOther() != null) {
            for (Object o : getOther()) {
                String str = AnyTypeUtils.convertOtherTag(o);
                if (str != null) {
                    currentOther.add(str);
                }
            }
        }
        if (publishedTl.getOther() != null) {
            for (Object o : publishedTl.getOther()) {
                String str = AnyTypeUtils.convertOtherTag(o);
                if (str != null) {
                    publishedOther.add(str);
                }
            }
        }
        // Other : added
        for (String tmpElement : currentOther) {
            if (!publishedOther.contains(tmpElement)) {
                diffList.add(new TLDifference(getId() + "_" + Tag.OTHER, "", tmpElement));
            }
        }
        // Other : removed
        for (String tmpElement : publishedOther) {
            if (!currentOther.contains(tmpElement)) {
                diffList.add(new TLDifference(getId() + "_" + Tag.OTHER, tmpElement, ""));
            }
        }
        // X509 Ski
        if (getX509ski() != null) {
            if (publishedTl.getX509ski() != null) {
                if (!Objects.deepEquals(getX509ski(), publishedTl.getX509ski())) {
                    diffList.add(new TLDifference(getId() + "_" + Tag.X509_SKI, TLUtils.getStringSKIfromBytes(publishedTl.getX509ski()), TLUtils.getStringSKIfromBytes(getX509ski())));
                }
            } else {
                diffList.add(new TLDifference(getId() + "_" + Tag.X509_SKI, "", TLUtils.getStringSKIfromBytes(getX509ski())));
            }
        } else if (publishedTl.getX509ski() != null) {
            diffList.add(new TLDifference(getId() + "_" + Tag.X509_SKI, TLUtils.getStringSKIfromBytes(publishedTl.getX509ski()), ""));
        }
        return diffList;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((certificateList == null) ? 0 : certificateList.hashCode());
        result = (prime * result) + ((other == null) ? 0 : other.hashCode());
        result = (prime * result) + ((subjectName == null) ? 0 : subjectName.hashCode());
        result = (prime * result) + Arrays.hashCode(x509ski);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        TLDigitalIdentification other = (TLDigitalIdentification) obj;
        if (certificateList == null) {
            if (other.certificateList != null) {
                return false;
            }
        } else if (!certificateList.equals(other.certificateList)) {
            return false;
        }
        if (this.other == null) {
            if (other.other != null) {
                return false;
            }
        } else if (!TLDigitalIdentityUtils.equalsBetweenOther(this.other, other.other)) {
            return false;
        }
        if (subjectName == null) {
            if (other.subjectName != null) {
                return false;
            }
        } else if (!subjectName.equals(other.subjectName)) {
            return false;
        }
        if (!Arrays.equals(x509ski, other.x509ski)) {
            return false;
        }
        return true;
    }

    public List<Object> getOther() {
        return other;
    }

    public void setOther(List<Object> other) {
        this.other = other;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public byte[] getX509ski() {
        return x509ski;
    }

    public void setX509ski(byte[] x509ski) {
        this.x509ski = x509ski;
    }

    public List<TLCertificate> getCertificateList() {
        return certificateList;
    }

    public void setCertificateList(List<TLCertificate> certificateList) {
        this.certificateList = certificateList;
    }

    public String getName() {
        if ((getCertificateList() != null) && (getCertificateList().get(0) != null)) {
            return getCertificateList().get(0).certToString();
        } else if (!StringUtils.isEmpty(getSubjectName())) {
            return getSubjectName();
        } else if (!StringUtils.isEmpty(getX509ski())) {
            return TLUtils.getStringSKIfromBytes(getX509ski());
        } else if (getOther() != null) {
            String other = AnyTypeUtils.convertOtherTag(getOther().get(0));
            if (other != null) {
                return other;
            } else {
                return "Other";
            }
        }
        return "Undefined";
    }

}
