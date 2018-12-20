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
package eu.europa.ec.joinup.tsl.business.dto.pdf;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import eu.europa.ec.joinup.tsl.business.dto.CheckResultDTO;
import eu.europa.ec.joinup.tsl.model.enums.CheckStatus;

@XStreamAlias("root")
public class PDFChecksChanges {

    @XStreamAsAttribute
    private int numberOfCheck = 0;
    @XStreamAsAttribute
    private int warningChecks;
    @XStreamAsAttribute
    private int errorChecks;
    @XStreamAsAttribute
    private int infoChecks;
    @XStreamAsAttribute
    private String countryName;
    @XStreamAsAttribute
    private String generationDate;
    @XStreamAsAttribute
    private String sequence;

    @XStreamOmitField
    private List<PDFCheck> checks;

    private List<PDFCheck> signatureChecks;
    private List<PDFCheck> tlChecks;
    private List<PDFCheck> schemeInformationChecks;
    private List<PDFCheck> pointersToOtherTslChecks;
    private List<PDFCheck> serviceProviderChecks;
    
    @XStreamAsAttribute
    private PDFInfoTL currentTL;

    public PDFChecksChanges() {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        generationDate = dateFormat.format(currentDate);
    }

    public void setChecks(List<CheckResultDTO> checks) {
        if (checks == null) {
            checks = new ArrayList<>();
        }
        Collections.sort(checks, new Comparator<CheckResultDTO>() {
            @Override
            public int compare(CheckResultDTO o1, CheckResultDTO o2) {
                if (o1.getStatus().equals(o2.getStatus())) {
                    return o1.getLocation().compareTo(o2.getLocation());
                } else {
                    return o1.getStatus().toString().compareTo(o2.getStatus().toString());
                }
            }
        });

        this.checks = createCheckList(checks);
        extractChecks();
        countChecks(checks);
    }

    private void extractChecks() {
        tlChecks = new ArrayList<>();
        signatureChecks = new ArrayList<>();
        serviceProviderChecks = new ArrayList<>();
        schemeInformationChecks = new ArrayList<>();
        pointersToOtherTslChecks = new ArrayList<>();

        for (PDFCheck check : checks) {
            if (check.getLocationId().contains("TSP_SERVICE_PROVIDER")) {
                serviceProviderChecks.add(check);
            } else if (check.getLocationId().contains("SCHEME_INFORMATION")) {
                schemeInformationChecks.add(check);
            } else if (check.getLocationId().contains("POINTERS_TO_OTHER_TSL")) {
                pointersToOtherTslChecks.add(check);
            } else if (check.getLocationId().contains("SIGNATURE")) {
                signatureChecks.add(check);
            } else {
                tlChecks.add(check);
            }
        }
    }

    private void countChecks(List<CheckResultDTO> checks) {
        errorChecks = 0;
        warningChecks = 0;
        infoChecks = 0;

        for (CheckResultDTO check : checks) {
            if (check.getStatus() == CheckStatus.ERROR) {
                errorChecks++;
            } else if (check.getStatus() == CheckStatus.WARNING) {
                warningChecks++;
            } else if (check.getStatus() == CheckStatus.INFO) {
                infoChecks++;
            }
        }

        numberOfCheck = errorChecks + warningChecks + infoChecks;
    }

    private List<PDFCheck> createCheckList(List<CheckResultDTO> list) {
        List<PDFCheck> result = new ArrayList<>();
        CheckResultDTO previous = null;
        for (CheckResultDTO check : list) {
            if (previous == null) {
                PDFCheck toSerialize = new PDFCheck();
                toSerialize.add(check);
                result.add(toSerialize);
                previous = check;
            } else {
                if (previous.getLocation().equals(check.getLocation())) {
                    result.get(result.size() - 1).add(check);
                } else {
                    PDFCheck toSerialize = new PDFCheck();
                    toSerialize.add(check);
                    result.add(toSerialize);
                    previous = check;
                }
            }
        }
        return result;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public PDFInfoTL getCurrentTL() {
        return currentTL;
    }

    public void setCurrentTL(PDFInfoTL currentTL) {
        this.currentTL = currentTL;
    }

    public List<PDFCheck> getSchemeInformationChecks() {
        return schemeInformationChecks;
    }

    public void setSchemeInformationChecks(List<PDFCheck> schemeInformationChecks) {
        this.schemeInformationChecks = schemeInformationChecks;
    }

    public List<PDFCheck> getPointersToOtherTslChecks() {
        return pointersToOtherTslChecks;
    }

    public void setPointersToOtherTslChecks(List<PDFCheck> pointersToOtherTslChecks) {
        this.pointersToOtherTslChecks = pointersToOtherTslChecks;
    }

    public List<PDFCheck> getServiceProviderChecks() {
        return serviceProviderChecks;
    }

    public void setServiceProviderChecks(List<PDFCheck> serviceProviderChecks) {
        this.serviceProviderChecks = serviceProviderChecks;
    }

    public List<PDFCheck> getSignatureChecks() {
        return signatureChecks;
    }

    public void setSignatureChecks(List<PDFCheck> signatureChecks) {
        this.signatureChecks = signatureChecks;
    }

    public List<PDFCheck> getTlChecks() {
        return tlChecks;
    }

    public void setTlChecks(List<PDFCheck> tlChecks) {
        this.tlChecks = tlChecks;
    }

}
