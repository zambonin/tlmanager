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
package eu.europa.ec.joinup.tsl.business.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.StringUtils;

import eu.europa.ec.joinup.tsl.business.dto.TLDifference;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLDefinitionUri;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLDigitalIdentification;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLDistributionPoint;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLElectronicAddress;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLGeneric;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLInformationExtension;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLInformationUri;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLName;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLPostalAddress;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLSchemePolicy;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLSchemeTypeCommunityRule;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceExtension;
import eu.europa.ec.joinup.tsl.model.enums.Tag;

/**
 * Calcul TLDifferences of different trusted list objects
 */
public class ChangeUtils {

    private static final ResourceBundle bundle = ResourceBundle.getBundle("messages");

    public static List<TLDifference> diffOfTLNameList(List<TLName> draft, List<TLName> published, String parentId) {
        List<TLDifference> diffList = new ArrayList<>();

        // COPY VALUE OF DRAFT DATA
        List<TLName> tmp = new ArrayList<>(draft);
        List<TLName> tmpPublished = new ArrayList<>();
        if (!CollectionUtils.isEmpty(published)) {
            tmpPublished = new ArrayList<>(published);
        }

        // DELETE EQUALS
        for (int i = 0; i < draft.size(); i++) {
            if (tmpPublished.contains(draft.get(i))) {
                tmpPublished.remove(draft.get(i));
                tmp.remove(draft.get(i));
            }
        }

        // CHECK OTHERS
        for (TLName draftName : tmp) {
            boolean find = false;
            // LANGUAGE AND VALUE NOT PRESENT
            if (draftName.getLanguage() != null) {
                List<TLDifference> nameTmpDifList = new ArrayList<>();
                for (TLName publishedName : tmpPublished) {
                    if (draftName.getLanguage().equalsIgnoreCase(publishedName.getLanguage())) {
                        // SAME LANGUAGE
                        TLDifference schemeNameDIff = draftName.asPublishedDiff(publishedName);
                        if (schemeNameDIff != null) {
                            nameTmpDifList.add(schemeNameDIff);
                            tmpPublished.remove(publishedName);
                            find = true;
                            break;
                        }
                    }
                }

                if (!nameTmpDifList.isEmpty()) {
                    diffList.addAll(nameTmpDifList);
                }

                // IF NOT FIND LANGUAGE --> NEW LANGUAGE
                if (!find) {
                    diffList.add(new TLDifference(draftName.getId(), "", draftName.getLanguage() + " - " + draftName.getValue()));
                }

            } else {
                // NO LANGUAGE --> NO CHANGE
                diffList.add(new TLDifference(draftName.getId(), "", bundle.getString("changes.noLanguage")));
            }
        }

        // DELETE ITEM IN DIFF LIST
        for (TLName publishedName : tmpPublished) {
            diffList.add(new TLDifference(parentId, publishedName.getLanguage() + " - " + publishedName.getValue(), ""));
        }

        // ADD ALL CHANGES POINTER DIFF LIST
        return diffList;

    }

    public static List<TLDifference> diffOfPostalList(List<TLPostalAddress> draft, List<TLPostalAddress> published, String parentId) {
        List<TLDifference> diffList = new ArrayList<>();

        // COPY VALUE OF DRAFT DATA
        List<TLPostalAddress> tmp = new ArrayList<>(draft);
        List<TLPostalAddress> tmpPublished = new ArrayList<>(published);

        // DELETE EQUALS
        for (int i = 0; i < draft.size(); i++) {
            if (tmpPublished.contains(draft.get(i))) {
                tmpPublished.remove(draft.get(i));
                tmp.remove(draft.get(i));
            }
        }

        // CHECK OTHERS
        for (TLPostalAddress draftName : tmp) {
            boolean find = false;
            // LANGUAGE AND VALUE NOT PRESENT
            if (draftName.getLanguage() != null) {
                List<TLDifference> nameTmpDifList = new ArrayList<>();
                for (TLPostalAddress publishedName : tmpPublished) {
                    if (draftName.getLanguage().equalsIgnoreCase(publishedName.getLanguage())) {
                        // SAME LANGUAGE
                        TLDifference schemeNameDIff = draftName.asPublishedDiff(publishedName);
                        if (schemeNameDIff != null) {
                            nameTmpDifList.add(schemeNameDIff);
                            tmpPublished.remove(publishedName);
                            find = true;
                            break;
                        }
                    }
                }

                if (!nameTmpDifList.isEmpty()) {
                    diffList.addAll(nameTmpDifList);
                }

                // IF NOT FIND LANGUAGE --> NEW LANGUAGE
                if (!find) {
                    diffList.add(new TLDifference(draftName.getId(), "", draftName.getLanguage() + " - " + draftName.getStreet()));
                }

            } else {
                // NO LANGUAGE --> NO CHANGE
                diffList.add(new TLDifference(draftName.getId(), "", bundle.getString("changes.noLanguage")));
            }
        }

        // DELETE ITEM IN DIFF LIST
        for (TLPostalAddress publishedName : tmpPublished) {
            diffList.add(new TLDifference(parentId, publishedName.getLanguage() + " - " + publishedName.getStreet(), ""));
        }

        // ADD ALL CHANGES POINTER DIFF LIST
        return diffList;

    }

    public static List<TLDifference> diffOfElectronic(List<TLElectronicAddress> draft, List<TLElectronicAddress> published, String parentId) {
        List<TLDifference> diffList = new ArrayList<>();

        // COPY VALUE OF DRAFT DATA
        List<TLElectronicAddress> tmp = new ArrayList<>(draft);
        List<TLElectronicAddress> tmpPublished = new ArrayList<>(published);

        // DELETE EQUALS
        for (int i = 0; i < draft.size(); i++) {
            if (tmpPublished.contains(draft.get(i))) {
                tmpPublished.remove(draft.get(i));
                tmp.remove(draft.get(i));
            }
        }

        // CHECK OTHERS
        for (TLElectronicAddress draftName : tmp) {
            boolean find = false;
            // LANGUAGE AND VALUE NOT PRESENT
            if (draftName.getLanguage() != null) {
                List<TLDifference> nameTmpDifList = new ArrayList<>();
                for (TLElectronicAddress publishedName : tmpPublished) {
                    if (draftName.getLanguage().equalsIgnoreCase(publishedName.getLanguage())) {
                        // SAME LANGUAGE
                        TLDifference schemeNameDIff = draftName.asPublishedDiff(publishedName);
                        if (schemeNameDIff != null) {
                            nameTmpDifList.add(schemeNameDIff);
                            tmpPublished.remove(publishedName);
                            find = true;
                            break;
                        }
                    }
                }

                if (!nameTmpDifList.isEmpty()) {
                    diffList.addAll(nameTmpDifList);
                }

                // IF NOT FIND LANGUAGE --> NEW LANGUAGE
                if (!find) {
                    diffList.add(new TLDifference(draftName.getId(), "", draftName.getLanguage() + " - " + draftName.getValue()));
                }

            } else {
                // NO LANGUAGE --> NO CHANGE
                diffList.add(new TLDifference(draftName.getId(), "", bundle.getString("changes.noLanguage")));
            }
        }

        // DELETE ITEM IN DIFF LIST
        for (TLElectronicAddress publishedName : tmpPublished) {
            diffList.add(new TLDifference(parentId, publishedName.getLanguage() + " - " + publishedName.getValue(), ""));
        }

        // ADD ALL CHANGES POINTER DIFF LIST
        return diffList;

    }

    public static List<TLDifference> diffOfTLDefUriList(List<TLDefinitionUri> draft, List<TLDefinitionUri> published, String parentId) {
        List<TLDifference> diffList = new ArrayList<>();

        // COPY VALUE OF DRAFT DATA
        List<TLDefinitionUri> tmp = new ArrayList<>(draft);
        List<TLDefinitionUri> tmpPublished = new ArrayList<>(published);

        // DELETE EQUALS
        for (int i = 0; i < draft.size(); i++) {
            if (tmpPublished.contains(draft.get(i))) {
                tmpPublished.remove(draft.get(i));
                tmp.remove(draft.get(i));
            }
        }

        // CHECK OTHERS
        for (TLDefinitionUri draftName : tmp) {
            boolean find = false;
            // LANGUAGE AND VALUE NOT PRESENT
            if (draftName.getLanguage() != null) {
                List<TLDifference> nameTmpDifList = new ArrayList<>();
                for (TLDefinitionUri publishedName : tmpPublished) {
                    if (draftName.getLanguage().equalsIgnoreCase(publishedName.getLanguage())) {
                        // SAME LANGUAGE
                        TLDifference schemeNameDIff = draftName.asPublishedDiff(publishedName);
                        if (schemeNameDIff != null) {
                            nameTmpDifList.add(schemeNameDIff);
                            tmpPublished.remove(publishedName);
                            find = true;
                            break;
                        }
                    }
                }

                if (!nameTmpDifList.isEmpty()) {
                    diffList.addAll(nameTmpDifList);
                }

                // IF NOT FIND LANGUAGE --> NEW LANGUAGE
                if (!find) {
                    diffList.add(new TLDifference(draftName.getId(), "", draftName.getLanguage() + " - " + draftName.getValue()));
                }

            } else {
                // NO LANGUAGE --> NO CHANGE
                diffList.add(new TLDifference(draftName.getId(), "", bundle.getString("changes.noLanguage")));
            }
        }

        // DELETE ITEM IN DIFF LIST
        for (TLDefinitionUri publishedName : tmpPublished) {
            diffList.add(new TLDifference(parentId, publishedName.getLanguage() + " - " + publishedName.getValue(), ""));
        }

        // ADD ALL CHANGES POINTER DIFF LIST
        return diffList;

    }

    public static List<TLDifference> diffOfTLInfoUriList(List<TLInformationUri> draft, List<TLInformationUri> published, String parentId) {
        List<TLDifference> diffList = new ArrayList<>();

        // COPY VALUE OF DRAFT DATA
        List<TLInformationUri> tmp = new ArrayList<>(draft);
        List<TLInformationUri> tmpPublished = new ArrayList<>(published);

        // DELETE EQUALS
        for (int i = 0; i < draft.size(); i++) {
            if (tmpPublished.contains(draft.get(i))) {
                tmpPublished.remove(draft.get(i));
                tmp.remove(draft.get(i));
            }
        }

        // CHECK OTHERS
        for (TLInformationUri draftName : tmp) {
            boolean find = false;
            // LANGUAGE AND VALUE NOT PRESENT
            if (draftName.getLanguage() != null) {
                List<TLDifference> nameTmpDifList = new ArrayList<>();
                for (TLInformationUri publishedName : tmpPublished) {
                    if (draftName.getLanguage().equalsIgnoreCase(publishedName.getLanguage())) {
                        // SAME LANGUAGE
                        TLDifference schemeNameDIff = draftName.asPublishedDiff(publishedName);
                        if (schemeNameDIff != null) {
                            nameTmpDifList.add(schemeNameDIff);
                            tmpPublished.remove(publishedName);
                            find = true;
                            break;
                        }
                    }
                }

                if (!nameTmpDifList.isEmpty()) {
                    diffList.addAll(nameTmpDifList);
                }

                // IF NOT FIND LANGUAGE --> NEW LANGUAGE
                if (!find) {
                    diffList.add(new TLDifference(draftName.getId(), "", draftName.getLanguage() + " - " + draftName.getValue()));
                }

            } else {
                // NO LANGUAGE --> NO CHANGE
                diffList.add(new TLDifference(draftName.getId(), "", bundle.getString("changes.noLanguage")));
            }
        }

        // DELETE ITEM IN DIFF LIST
        for (TLInformationUri publishedName : tmpPublished) {
            diffList.add(new TLDifference(parentId, publishedName.getLanguage() + " - " + publishedName.getValue(), ""));
        }

        // ADD ALL CHANGES POINTER DIFF LIST
        return diffList;

    }

    public static List<TLDifference> diffOfDistributionPoint(List<TLDistributionPoint> draft, List<TLDistributionPoint> published, String parentId) {
        List<TLDifference> diffList = new ArrayList<>();

        // COPY VALUE OF DRAFT DATA
        List<TLDistributionPoint> tmp = new ArrayList<>(draft);
        List<TLDistributionPoint> tmpPublished = new ArrayList<>(published);

        // DELETE EQUALS
        for (int i = 0; i < draft.size(); i++) {
            if (tmpPublished.contains(draft.get(i))) {
                tmpPublished.remove(draft.get(i));
                tmp.remove(draft.get(i));
            }
        }

        // CHECK OTHERS
        int i = 0;
        for (TLDistributionPoint draftName : tmp) {
            if ((tmpPublished.size() > i) && (tmpPublished.get(i) != null)) {
                diffList.add(new TLDifference(draftName.getId(), tmpPublished.get(i).getValue(), draftName.getValue()));
                tmpPublished.remove(tmpPublished.get(i));
            } else {
                diffList.add(new TLDifference(draftName.getId(), "", draftName.getValue()));
            }
        }

        // DELETE ITEM IN DIFF LIST
        for (TLDistributionPoint publishedName : tmpPublished) {
            diffList.add(new TLDifference(parentId, publishedName.getValue(), ""));
        }

        // ADD ALL CHANGES DIFF LIST
        return diffList;
    }

    public static List<TLDifference> diffOfTLTypeCommunityList(List<TLSchemeTypeCommunityRule> draft, List<TLSchemeTypeCommunityRule> published, String parentId) {
        List<TLDifference> diffList = new ArrayList<>();

        // COPY VALUE OF DRAFT DATA
        List<TLSchemeTypeCommunityRule> tmp = new ArrayList<>(draft);
        List<TLSchemeTypeCommunityRule> tmpPublished = new ArrayList<>(published);

        // DELETE EQUALS
        for (int i = 0; i < draft.size(); i++) {
            if (tmpPublished.contains(draft.get(i))) {
                tmpPublished.remove(draft.get(i));
                tmp.remove(draft.get(i));
            }
        }

        // CHECK OTHERS
        for (TLSchemeTypeCommunityRule draftName : tmp) {
            boolean find = false;
            // LANGUAGE AND VALUE NOT PRESENT
            if (draftName.getLanguage() != null) {
                List<TLDifference> nameTmpDifList = new ArrayList<>();
                for (TLSchemeTypeCommunityRule publishedName : tmpPublished) {
                    if (draftName.getLanguage().equalsIgnoreCase(publishedName.getLanguage())) {
                        // SAME LANGUAGE
                        TLDifference schemeNameDIff = draftName.asPublishedDiff(publishedName);
                        if (schemeNameDIff != null) {
                            nameTmpDifList.add(schemeNameDIff);
                            tmpPublished.remove(publishedName);
                            find = true;
                            break;
                        }
                    }
                }

                if (!nameTmpDifList.isEmpty()) {
                    diffList.addAll(nameTmpDifList);
                }

                // IF NOT FIND LANGUAGE --> NEW LANGUAGE
                if (!find) {
                    diffList.add(new TLDifference(draftName.getId(), "", draftName.getLanguage() + " - " + draftName.getValue()));
                }

            } else {
                // NO LANGUAGE --> NO CHANGE
                diffList.add(new TLDifference(draftName.getId(), "", bundle.getString("changes.noLanguage")));
            }
        }

        // DELETE ITEM IN DIFF LIST
        for (TLSchemeTypeCommunityRule publishedName : tmpPublished) {
            diffList.add(new TLDifference(parentId, publishedName.getLanguage() + " - " + publishedName.getValue(), ""));
        }

        // ADD ALL CHANGES POINTER DIFF LIST
        return diffList;

    }
    
    public static List<TLDifference> diffOfTLExtensionList(List<TLInformationExtension> draft, List<TLInformationExtension> published, String parentId) {
        List<TLDifference> diffList = new ArrayList<>();

        // COPY VALUE OF DRAFT DATA
        List<TLInformationExtension> tmp = new ArrayList<>(draft);
        List<TLInformationExtension> tmpPublished = new ArrayList<>(published);

        // DELETE EQUALS
        for (int i = 0; i < draft.size(); i++) {
            if (tmpPublished.contains(draft.get(i))) {
                tmpPublished.remove(draft.get(i));
                tmp.remove(draft.get(i));
            }
        }

        // CHECK OTHERS
        for (TLInformationExtension draftExtension : tmp) {
            boolean find = false;
            // VALUE NOT PRESENT
            if (draftExtension.getValue() != null) {
                List<TLDifference> extensionTmpDifList = new ArrayList<>();
                for (TLInformationExtension publishedExtension : tmpPublished) {
                    if (draftExtension.getValue().equalsIgnoreCase(publishedExtension.getValue())) {
                        // SAME LANGUAGE
                        if(draftExtension.getCritical() != publishedExtension.getCritical()) {
                            // DIFFERENT CRITICALITY
                            TLDifference schemeExtensionDIff = new TLDifference(parentId, 
                                    publishedExtension.toString(), draftExtension.toString());
                            extensionTmpDifList.add(schemeExtensionDIff);
                        }
                        tmpPublished.remove(publishedExtension);
                        find = true;
                        break;
                    }
                }

                if (!extensionTmpDifList.isEmpty()) {
                    diffList.addAll(extensionTmpDifList);
                }

                // IF NOT FIND VALUE --> NEW VALUE
                if (!find) {
                    diffList.add(new TLDifference(draftExtension.getId(), "", draftExtension.toString()));
                }

            } else {
                // NO VALUE --> NO CHANGE
                diffList.add(new TLDifference(draftExtension.getId(), "", bundle.getString("changes.noValue")));
            }
        }

        // DELETE ITEM IN DIFF LIST
        for (TLInformationExtension publishedExtension : tmpPublished) {
            diffList.add(new TLDifference(parentId, publishedExtension.toString(), ""));
        }

        // ADD ALL CHANGES POINTER DIFF LIST
        return diffList;

    }

    public static List<TLDifference> diffOfTLPolicyList(List<TLSchemePolicy> draft, List<TLSchemePolicy> published, String parentId) {
        List<TLDifference> diffList = new ArrayList<>();

        // COPY VALUE OF DRAFT DATA
        List<TLSchemePolicy> tmp = new ArrayList<>(draft);
        List<TLSchemePolicy> tmpPublished = new ArrayList<>(published);

        // DELETE EQUALS
        for (int i = 0; i < draft.size(); i++) {
            if (tmpPublished.contains(draft.get(i))) {
                tmpPublished.remove(draft.get(i));
                tmp.remove(draft.get(i));
            }
        }

        // CHECK OTHERS
        for (TLSchemePolicy draftName : tmp) {
            boolean find = false;
            // LANGUAGE AND VALUE NOT PRESENT
            if (draftName.getLanguage() != null) {
                List<TLDifference> nameTmpDifList = new ArrayList<>();
                for (TLSchemePolicy publishedName : tmpPublished) {
                    if (draftName.getLanguage().equalsIgnoreCase(publishedName.getLanguage())) {
                        // SAME LANGUAGE
                        TLDifference schemeNameDIff = draftName.asPublishedDiff(publishedName);
                        if (schemeNameDIff != null) {
                            nameTmpDifList.add(schemeNameDIff);
                            tmpPublished.remove(publishedName);
                            find = true;
                            break;
                        }
                    }
                }

                if (!nameTmpDifList.isEmpty()) {
                    diffList.addAll(nameTmpDifList);
                }

                // IF NOT FIND LANGUAGE --> NEW LANGUAGE
                if (!find) {
                    diffList.add(new TLDifference(draftName.getId(), "", draftName.getLanguage() + " - " + draftName.getValue()));
                }

            } else {
                // NO LANGUAGE --> NO CHANGE
                diffList.add(new TLDifference(draftName.getId(), "", bundle.getString("changes.noLanguage")));
            }
        }

        // DELETE ITEM IN DIFF LIST
        for (TLSchemePolicy publishedName : tmpPublished) {
            diffList.add(new TLDifference(parentId, publishedName.getLanguage() + " - " + publishedName.getValue(), ""));
        }

        // ADD ALL CHANGES POINTER DIFF LIST
        return diffList;

    }

    public static List<TLDifference> diffOfDigitalList(List<TLDigitalIdentification> draft, List<TLDigitalIdentification> publishedTlDigiId, String parentId) {
        List<TLDigitalIdentification> tmpPublished = new ArrayList<>(publishedTlDigiId);
        List<TLDifference> diffList = new ArrayList<>();

        if (draft != null) {
            for (TLDigitalIdentification draftDigit : draft) {
                Boolean match = false;
                for (TLDigitalIdentification publishedDigit : publishedTlDigiId) {
                    if (TLDigitalIdentityUtils.matchTLDigitalIdentification(draftDigit, publishedDigit)) {
                        match = true;
                        tmpPublished.remove(publishedDigit);
                        if (!draftDigit.equals(publishedDigit)) {
                            diffList.addAll(draftDigit.asPublishedDiff(publishedDigit));
                        }
                        break;
                    }
                }
                publishedTlDigiId = new ArrayList<>(tmpPublished);
                if (!match) {
                    diffList.add(new TLDifference(draftDigit.getId(), "", draftDigit.getName()));
                }
            }
        } else {
            if (publishedTlDigiId != null) {
                for (TLDigitalIdentification publishedDigit : publishedTlDigiId) {
                    diffList.add(new TLDifference(parentId, publishedDigit.getName(), ""));
                }

            }
        }

        if (!tmpPublished.isEmpty()) {
            for (TLDigitalIdentification tmpDigit : tmpPublished) {
                diffList.add(new TLDifference(parentId + "_" + Tag.SERVICE_DIGITAL_IDENTITY, tmpDigit.getName(), ""));
            }
        }
        return diffList;
    }

    public static <T extends TLGeneric> List<TLDifference> initEmptyCurrentListDifference(List<T> objects, String parent) {
        List<TLDifference> differences = new ArrayList<>();
        for (T object : objects) {
            String language = !StringUtils.isEmpty(object.getLanguage()) ? object.getLanguage() + " - " : "";
            String value = !StringUtils.isEmpty(object.getValue()) ? object.getValue() : "";
            differences.add(new TLDifference(parent, language + value, ""));
        }
        return differences;
    }

    public static List<TLDifference> initEmptyCurrentPostalAddressDifference(List<TLPostalAddress> objects, String parent) {
        List<TLDifference> differences = new ArrayList<>();
        for (TLPostalAddress object : objects) {
            String language = !StringUtils.isEmpty(object.getLanguage()) ? object.getLanguage() + " - " : "";
            String street = !StringUtils.isEmpty(object.getStreet()) ? object.getStreet() : "";
            differences.add(new TLDifference(parent, language + street, ""));
        }
        return differences;
    }
    
    public static List<TLDifference> initEmptyExtensionDifference(List<TLInformationExtension> objects, String parent) {
        List<TLDifference> differences = new ArrayList<>();
        for (TLInformationExtension object : objects) {
            differences.add(new TLDifference(parent, object.toString(), ""));
        }
        return differences;
    }

    public static List<TLDifference> diffOfExtension(List<TLServiceExtension> tmp, List<TLServiceExtension> tmpPublished, String parent) {
        List<TLDifference> diffList = new ArrayList<>();

        List<TLServiceExtension> publishedQE = new ArrayList<>();
        List<TLServiceExtension> draftQE = new ArrayList<>();

        // CHECK OTHERS tspServices
        for (TLServiceExtension draft : tmp) {
            if (draft.getAdditionnalServiceInfo() != null) {
                // Additionnal Service Info : value
                String current = !StringUtils.isEmpty(draft.getAdditionnalServiceInfo().getValue()) ? draft.getAdditionnalServiceInfo().getValue() : bundle.getString("change.newAddExt");
                diffList.add(new TLDifference(draft.getId() + "_" + Tag.SERVICE_ADDITIONNAL_EXT, "", current));
            } else if (CollectionUtils.isNotEmpty(draft.getQualificationsExtension())) {
                draftQE.add(draft);
            } else if (draft.getExpiredCertsRevocationDate() != null) {
                // Expired Cert Revocation
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                diffList.add(new TLDifference(draft.getId() + "_" + Tag.SERVICE_EXPIRED_CERT_REVOCATION, "", format.format(draft.getExpiredCertsRevocationDate())));
            } else if (draft.getTakenOverBy() != null) {
                // Taken over by
                String territory = !StringUtils.isEmpty(draft.getTakenOverBy().getTerritory()) ? draft.getTakenOverBy().getTerritory() : bundle.getString("tUndefined");

                String uri;
                if ((draft.getTakenOverBy().getUrl() != null) && !StringUtils.isEmpty(draft.getTakenOverBy().getUrl().getValue())) {
                    uri = draft.getTakenOverBy().getUrl().getValue();
                } else {
                    uri = bundle.getString("tUndefined");
                }
                diffList.add(new TLDifference(draft.getId() + '_' + Tag.SERVICE_TAKEN_OVER_BY, "", territory + " - " + uri));
            } else {
                // Undefined
                diffList.add(new TLDifference(draft.getId(), "", bundle.getString("changes.newExt")));
            }
        }

        // DELETE ITEM IN DIFF LIST
        for (TLServiceExtension published : tmpPublished) {
            String language = bundle.getString("tUndefined");
            String value = bundle.getString("tUndefined");
            // Additionnal Service Info
            if (published.getAdditionnalServiceInfo() != null) {
                language = published.getAdditionnalServiceInfo().getLanguage() != null ? published.getAdditionnalServiceInfo().getLanguage() : bundle.getString("tUndefined");

                value = published.getAdditionnalServiceInfo().getValue() != null ? published.getAdditionnalServiceInfo().getValue() : bundle.getString("tUndefined");

                diffList.add(new TLDifference(parent + "_" + Tag.SERVICE_EXTENSION + "_" + Tag.SERVICE_ADDITIONNAL_EXT, language + " - " + value, ""));
            } else if (CollectionUtils.isNotEmpty(published.getQualificationsExtension())) {
                publishedQE.add(published);
            } else if (published.getTakenOverBy() != null) {
                // Taken over by
                if (!CollectionUtils.isEmpty(published.getTakenOverBy().getOperatorName())) {
                    language = published.getTakenOverBy().getOperatorName().get(0).getLanguage() != null ? published.getTakenOverBy().getOperatorName().get(0).getLanguage()
                            : bundle.getString("tUndefined");

                    value = published.getTakenOverBy().getOperatorName().get(0).getValue() != null ? published.getTakenOverBy().getOperatorName().get(0).getValue() : bundle.getString("tUndefined");
                }
                diffList.add(new TLDifference(parent + "_" + Tag.SERVICE_EXTENSION + "_" + Tag.SERVICE_TAKEN_OVER_BY, language + " - " + value, ""));
            } else if (published.getExpiredCertsRevocationDate() != null) {
                // Expired Cert Revocation
                diffList.add(new TLDifference(parent + "_" + Tag.SERVICE_EXTENSION + "_" + Tag.SERVICE_EXPIRED_CERT_REVOCATION, published.getExpiredCertsRevocationDate().toString(), ""));
            } else {
                // Undefined
                diffList.add(new TLDifference(parent + "_" + Tag.SERVICE_EXTENSION, bundle.getString("tUndefined"), ""));
            }
        }

        if (!CollectionUtils.isEmpty(draftQE) || !CollectionUtils.isEmpty(publishedQE)) {
            for (TLServiceExtension published : publishedQE) {
                Boolean publishedMatch = false;
                for (TLServiceExtension draft : draftQE) {
                    if (published.equals(draft)) {
                        publishedMatch = true;
                        break;
                    }
                }
                if (!publishedMatch) {
                    diffList.add(new TLDifference(parent + "_" + Tag.SERVICE_EXTENSION, "Previous entry deleted or updated", ""));
                }
            }

            for (TLServiceExtension draft : draftQE) {
                Boolean draftMatch = false;
                for (TLServiceExtension published : publishedQE) {
                    if (draft.equals(published)) {
                        draftMatch = true;
                        break;
                    }
                }
                if (!draftMatch) {
                    diffList.add(new TLDifference(draft.getId() + "_" + Tag.SERVICE_QUALIFICATION_EXT, "", "New entry or previous entry updated"));
                }
            }
        }

        return diffList;
    }

}
