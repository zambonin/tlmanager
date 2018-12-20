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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DLSequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.joinup.tsl.business.dto.TLDifference;
import eu.europa.ec.joinup.tsl.business.util.ChangeUtils;
import eu.europa.ec.joinup.tsl.business.util.TLUtils;
import eu.europa.ec.joinup.tsl.model.enums.Tag;
import eu.europa.esig.dss.DSSUtils;
import eu.europa.esig.dss.DigestAlgorithm;
import eu.europa.esig.dss.x509.CertificateToken;
import eu.europa.esig.jaxb.v5.tsl.ExtensionTypeV5;
import eu.europa.esig.jaxb.v5.tsl.ExtensionsListTypeV5;
import eu.europa.esig.jaxb.v5.tsl.InternationalNamesTypeV5;
import eu.europa.esig.jaxb.v5.tsl.MultiLangNormStringTypeV5;
import eu.europa.esig.jaxb.v5.tsl.ServiceHistoryInstanceTypeV5;

public class TLServiceHistory extends AbstractTLDTO {

    private static final Logger LOGGER = LoggerFactory.getLogger(TLServiceHistory.class);

    private String typeIdentifier;
    private List<TLName> serviceName;
    private List<TLDigitalIdentification> serviceDigitalIdentification;
    private String currentStatus;
    private Date currentStatusStartingDate;
    private List<TLServiceExtension> extension;

    public TLServiceHistory() {
    }

    public TLServiceHistory(int iddb, String location, ServiceHistoryInstanceTypeV5 svcHistory) {
        super(iddb, location);
        setTypeIdentifier(svcHistory.getServiceTypeIdentifier());
        setCurrentStatus(svcHistory.getServiceStatus());
        setCurrentStatusStartingDate(TLUtils.toDate(svcHistory.getStatusStartingTime()));

        int i;
        List<TLName> serviceName = new ArrayList<>();
        if (svcHistory.getServiceName() != null) {
            i = 0;
            for (MultiLangNormStringTypeV5 name : svcHistory.getServiceName().getName()) {
                i++;
                serviceName.add(new TLName(getTlId(), getId() + "_" + Tag.SERVICE_NAME + "_" + i, name));
            }
        }
        setServiceName(serviceName);

        List<TLDigitalIdentification> digitalId = new ArrayList<>();
        if (svcHistory.getServiceDigitalIdentity() != null) {
            digitalId.add(new TLDigitalIdentification(getTlId(), getId() + "_" + Tag.SERVICE_DIGITAL_IDENTITY + "_1", svcHistory.getServiceDigitalIdentity()));
        }
        setDigitalIdentification(digitalId);

        List<TLServiceExtension> extensions = new ArrayList<>();
        if (svcHistory.getServiceInformationExtensions() != null) {
            i = 0;
            for (ExtensionTypeV5 extension : svcHistory.getServiceInformationExtensions().getExtension()) {
                i++;
                extensions.add(new TLServiceExtension(getTlId(), getId() + "_" + Tag.SERVICE_EXTENSION + "_" + i, extension));
            }
        }
        setExtension(extensions);

    }

    public TLServiceHistory(int iddb, String location, TLServiceDto serviceToHistory) {
        super(iddb, location);
        setTypeIdentifier(serviceToHistory.getTypeIdentifier());
        setCurrentStatus(serviceToHistory.getCurrentStatus());
        setCurrentStatusStartingDate(serviceToHistory.getCurrentStatusStartingDate());

        if (serviceToHistory.getServiceName() != null) {
            setServiceName(serviceToHistory.getServiceName());
        }

        // OPTIONNAL IN STANDARD
        if (serviceToHistory.getExtension() != null) {
            List<TLServiceExtension> extList = new ArrayList<>();
            for (TLServiceExtension published : serviceToHistory.getExtension()) {
                TLServiceExtension draft = new TLServiceExtension();
                draft.setCritical(published.isCritical());
                draft.setExpiredCertsRevocationDate(published.getExpiredCertsRevocationDate());
                draft.setId(published.getId());
                draft.setTakenOverBy(published.getTakenOverBy());
                draft.setTlId(published.getTlId());

                if (published.getAdditionnalServiceInfo() != null) {
                    TLAdditionnalServiceInfo tmpAddService = new TLAdditionnalServiceInfo();
                    tmpAddService.setLanguage(published.getAdditionnalServiceInfo().getLanguage());
                    tmpAddService.setValue(published.getAdditionnalServiceInfo().getValue());
                    draft.setAdditionnalServiceInfo(tmpAddService);
                }

                if (published.getQualificationsExtension() != null) {
                    List<TLQualificationExtension> qaList = new ArrayList<>();
                    for (TLQualificationExtension qa : published.getQualificationsExtension()) {
                        TLQualificationExtension tmpQaExt = new TLQualificationExtension();
                        tmpQaExt.setCriteria(qa.getCriteria());
                        tmpQaExt.setId(qa.getId());
                        // tmpQaExt.setQualifTypeList(new ArrayList<String>(qa.getQualifTypeList()));
                        List<String> strList = new ArrayList<>();
                        for (String str : qa.getQualifTypeList()) {
                            strList.add(str);
                        }
                        tmpQaExt.setQualifTypeList(strList);
                        qaList.add(tmpQaExt);
                    }
                    draft.setQualificationsExtension(qaList);
                }

                extList.add(draft);
            }

            setExtension(extList);
        }

        if (serviceToHistory.getDigitalIdentification() != null) {
            List<TLDigitalIdentification> digitalIdHistoryList = new ArrayList<>();
            for (TLDigitalIdentification digitalId : serviceToHistory.getDigitalIdentification()) {
                TLDigitalIdentification digitalIdToHistorize = new TLDigitalIdentification();
                // TLCertificate cert = new TLCertificate(digitalId.getCertificateList().get(0).getTlId(),
                // digitalId.getCertificateList().get(0).getId(),
                // digitalId.getCertificateList().get(0).getCertEncoded());
                // RECUPERER LE SKI DU CERTIF? SI + CERTIFICAT?
                // RECUPERER LE SKI DU CHAMP SKI? SI PAS COHERENT AVEC CERTIF?
                if ((digitalId.getCertificateList() != null) && (digitalId.getCertificateList().get(0) != null)) {
                    CertificateToken cert = DSSUtils.loadCertificate(digitalId.getCertificateList().get(0).getCertEncoded());
                    digitalIdToHistorize.setSubjectName(cert.getSubjectX500Principal().toString());

                    if (TLUtils.getSki(cert) != null) {
                        digitalIdToHistorize.setX509ski(TLUtils.getSki(cert));
                    } else {
                        DLSequence seq1;
                        try {
                            seq1 = (DLSequence) DERSequence.fromByteArray(cert.getPublicKey().getEncoded());
                            DERBitString item2 = (DERBitString) seq1.getObjectAt(1);
                            DLSequence seq2 = (DLSequence) DERSequence.fromByteArray(item2.getBytes());
                            digitalIdToHistorize.setX509ski(DSSUtils.digest(DigestAlgorithm.SHA1, seq2.getEncoded()));
                        } catch (IOException e) {
                            LOGGER.error("Unable to parse certificate '" + Base64.encodeBase64String(cert.getEncoded()) + "' : " + e.getMessage(), e);
                            cert = null;
                        }
                    }

                    digitalIdHistoryList.add(digitalIdToHistorize);
                }

            }
            setDigitalIdentification(digitalIdHistoryList);
        }

    }

    public ServiceHistoryInstanceTypeV5 asTSLTypeV5(TLCertificate serviceCert) {
        ServiceHistoryInstanceTypeV5 svcHistoryInstanceType = new ServiceHistoryInstanceTypeV5();
        svcHistoryInstanceType.setServiceStatus(getCurrentStatus());
        svcHistoryInstanceType.setServiceTypeIdentifier(getTypeIdentifier());
        if (currentStatusStartingDate != null) {
            svcHistoryInstanceType.setStatusStartingTime(TLUtils.toXMLGregorianDate(currentStatusStartingDate));
        }

        // svcHistoryInstanceType.setServiceDigitalIdentity(value);
        if ((getDigitalIdentification() != null) && CollectionUtils.isNotEmpty(getDigitalIdentification())) {
            svcHistoryInstanceType.setServiceDigitalIdentity(getDigitalIdentification().get(0).asHistoricTSLTypeV5(serviceCert));
        }

        // svcHistoryInstanceType.setServiceName(value);
        if ((getServiceName() != null) && CollectionUtils.isNotEmpty(getServiceName())) {
            InternationalNamesTypeV5 intNameType = new InternationalNamesTypeV5();
            for (TLName name : getServiceName()) {
                intNameType.getName().add(name.asTSLTypeV5());
            }
            svcHistoryInstanceType.setServiceName(intNameType);
        }

        // svcHistoryInstanceType.setServiceInformationExtensions(value);
        if ((getExtension() != null) && CollectionUtils.isNotEmpty(getExtension())) {
            ExtensionsListTypeV5 extList = new ExtensionsListTypeV5();
            for (TLServiceExtension svcExt : getExtension()) {
                extList.getExtension().add(svcExt.asTSLTypeV5());
            }
            svcHistoryInstanceType.setServiceInformationExtensions(extList);
        }

        return svcHistoryInstanceType;
    }

    public List<TLDifference> asPublishedDiff(TLServiceHistory publishedTlHistory) {
        List<TLDifference> diffList = new ArrayList<>();
        /*************************************
         * List<TLName> serviceName;
         *************************************/
        if (CollectionUtils.isNotEmpty(getServiceName())) {
            List<TLDifference> changeList = ChangeUtils.diffOfTLNameList(getServiceName(), publishedTlHistory.getServiceName(), getId() + "_" + Tag.SERVICE_NAME);
            if (changeList.size() > 0) {
                diffList.addAll(changeList);
            }
        } else {
            if ((publishedTlHistory.getServiceName() != null) && CollectionUtils.isNotEmpty(publishedTlHistory.getServiceName())) {
                diffList.addAll(ChangeUtils.initEmptyCurrentListDifference(publishedTlHistory.getServiceName(), getId() + '_' + Tag.SERVICE_NAME));
            }
        }

        /*************************************
         * List<TLServiceExtension> extension;
         *************************************/
        List<TLServiceExtension> tmp = !CollectionUtils.isEmpty(getExtension()) ? new ArrayList<>(getExtension()) : new ArrayList<TLServiceExtension>();
        List<TLServiceExtension> tmpPublished = !CollectionUtils.isEmpty(publishedTlHistory.getExtension()) ? new ArrayList<>(publishedTlHistory.getExtension()) : new ArrayList<TLServiceExtension>();

        // DELETE EQUALS EXTENSION
        if (!CollectionUtils.isEmpty(getExtension())) {
            for (int i = 0; i < getExtension().size(); i++) {
                if (tmpPublished.contains(getExtension().get(i))) {
                    tmp.remove(getExtension().get(i));
                    tmpPublished.remove(getExtension().get(i));
                }
            }
        }

        diffList.addAll(ChangeUtils.diffOfExtension(tmp, tmpPublished, getId()));

        /*************************************
         * List<TLDigitalIdentity> digitalIdentification;
         *************************************/
        diffList.addAll(ChangeUtils.diffOfDigitalList(getDigitalIdentification(), publishedTlHistory.getDigitalIdentification(), getId()));

        return diffList;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((currentStatus == null) ? 0 : currentStatus.hashCode());
        result = (prime * result) + ((currentStatusStartingDate == null) ? 0 : currentStatusStartingDate.hashCode());
        result = (prime * result) + ((extension == null) ? 0 : extension.hashCode());
        result = (prime * result) + ((serviceDigitalIdentification == null) ? 0 : serviceDigitalIdentification.hashCode());
        result = (prime * result) + ((serviceName == null) ? 0 : serviceName.hashCode());
        result = (prime * result) + ((typeIdentifier == null) ? 0 : typeIdentifier.hashCode());
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
        TLServiceHistory other = (TLServiceHistory) obj;
        if (currentStatus == null) {
            if (other.currentStatus != null) {
                return false;
            }
        } else if (!currentStatus.equals(other.currentStatus)) {
            return false;
        }
        if (currentStatusStartingDate == null) {
            if (other.currentStatusStartingDate != null) {
                return false;
            }
        } else if (!currentStatusStartingDate.equals(other.currentStatusStartingDate)) {
            return false;
        }
        if (extension == null) {
            if (other.extension != null) {
                return false;
            }
        } else if (!extension.equals(other.extension)) {
            return false;
        }
        if (serviceDigitalIdentification == null) {
            if (other.serviceDigitalIdentification != null) {
                return false;
            }
        } else if (!serviceDigitalIdentification.equals(other.serviceDigitalIdentification)) {
            return false;
        }
        if (serviceName == null) {
            if (other.serviceName != null) {
                return false;
            }
        } else if (!serviceName.equals(other.serviceName)) {
            return false;
        }
        if (typeIdentifier == null) {
            if (other.typeIdentifier != null) {
                return false;
            }
        } else if (!typeIdentifier.equals(other.typeIdentifier)) {
            return false;
        }
        return true;
    }

    public String getTypeIdentifier() {
        return typeIdentifier;
    }

    public void setTypeIdentifier(String typeIdentifier) {
        this.typeIdentifier = typeIdentifier;
    }

    public List<TLName> getServiceName() {
        return serviceName;
    }

    public void setServiceName(List<TLName> serviceName) {
        this.serviceName = serviceName;
    }

    public List<TLDigitalIdentification> getDigitalIdentification() {
        return serviceDigitalIdentification;
    }

    public void setDigitalIdentification(List<TLDigitalIdentification> serviceDigitalIdentification) {
        this.serviceDigitalIdentification = serviceDigitalIdentification;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public Date getCurrentStatusStartingDate() {
        return currentStatusStartingDate;
    }

    public void setCurrentStatusStartingDate(Date currentStatusStartingDate) {
        this.currentStatusStartingDate = currentStatusStartingDate;
    }

    public List<TLServiceExtension> getExtension() {
        return extension;
    }

    public void setExtension(List<TLServiceExtension> extension) {
        this.extension = extension;
    }

}
