/*******************************************************************************
 * DIGIT-TSL - Trusted List Manager
 * Copyright (C) 2018 European Commission, provided under the CEF E-Signature programme
 *  
 * This file is part of the "DIGIT-TSL - Trusted List Manager" project.
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
package eu.europa.ec.joinup.tsl.web.controller;

import java.util.List;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLPointersToOtherTSL;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLSchemeInformation;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceDto;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceHistory;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceProvider;
import eu.europa.ec.joinup.tsl.business.service.RulesRunnerService;
import eu.europa.ec.joinup.tsl.business.service.TLService;
import eu.europa.ec.joinup.tsl.business.service.TLValidator;
import eu.europa.ec.joinup.tsl.business.service.TlEditHistoryService;
import eu.europa.ec.joinup.tsl.business.service.TlEditPointerService;
import eu.europa.ec.joinup.tsl.business.service.TlEditSchemeInfoService;
import eu.europa.ec.joinup.tsl.business.service.TlEditServiceProviderService;
import eu.europa.ec.joinup.tsl.business.service.TlEditServiceService;
import eu.europa.ec.joinup.tsl.business.service.UserService;
import eu.europa.ec.joinup.tsl.business.util.TLUtils;
import eu.europa.ec.joinup.tsl.model.enums.SignatureStatus;
import eu.europa.ec.joinup.tsl.model.enums.Tag;
import eu.europa.ec.joinup.tsl.web.form.ServiceResponse;
import eu.europa.ec.joinup.tsl.web.form.TLHistoryEdition;
import eu.europa.ec.joinup.tsl.web.form.TLInformationEdition;
import eu.europa.ec.joinup.tsl.web.form.TLPointerEdition;
import eu.europa.ec.joinup.tsl.web.form.TLServiceEdition;
import eu.europa.ec.joinup.tsl.web.form.TLServiceProviderEdition;

@Controller
@RequestMapping(value = "/api/tl/edit")
public class ApiTlEditionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiTlEditionController.class);

    private static ResourceBundle bundle = ResourceBundle.getBundle("messages");

    @Autowired
    private TlEditSchemeInfoService tlEditSchemeInfoService;

    @Autowired
    private TlEditPointerService tlEditPointerService;

    @Autowired
    private TlEditServiceProviderService tlEditServiceProviderService;

    @Autowired
    private TlEditServiceService tlEditServiceService;

    @Autowired
    private TlEditHistoryService tlEditHistoryService;

    @Autowired
    private TLService tlService;

    @Autowired
    private TLValidator tlValidator;

    @Autowired
    private RulesRunnerService rulesRunner;

    @Autowired
    private UserService userService;

    /**
     * Edit scheme information (one specific attribute) in the trusted list and run rules on all scheme information (without pointer)
     *
     * @param tlInformationEdition
     * @return
     */
    @RequestMapping(value = "/tlSchemeInfo", method = RequestMethod.PUT)
    public @ResponseBody ServiceResponse<TLSchemeInformation> editTlSchemeInfo(@RequestBody TLInformationEdition tlInformationEdition) {
        ServiceResponse<TLSchemeInformation> response = new ServiceResponse<>();
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isAuthenticated(SecurityContextHolder.getContext().getAuthentication().getName())) {
            LOGGER.info("Edition of TL : " + tlInformationEdition.getTlId() + " attribute : " + tlInformationEdition.getEditAttribute());
            if (tlService.inStore(tlInformationEdition.getTlId(), tlInformationEdition.getCookie())) {
                if (TLUtils.checkDate(tlService.getEdt(tlInformationEdition.getTlId()), tlInformationEdition.getLastEditedDate())) {

                    TLSchemeInformation schemeInfoUpdated = tlEditSchemeInfoService.edit(tlInformationEdition.getTlId(), tlInformationEdition.getTlSchemeInfoObj(),
                            tlInformationEdition.getEditAttribute(), tlInformationEdition.getTslValue());

                    if (schemeInfoUpdated != null) {
                        // GET SCHEME INFO WITH ID
                        TLSchemeInformation schemeInfoUpdatedWithId = new TLSchemeInformation(tlInformationEdition.getTlId(), schemeInfoUpdated.asTSLTypeV5());

                        if (!tlService.getSignatureInfo(tlInformationEdition.getTlId()).getIndication().equals(SignatureStatus.NOT_SIGNED)) {
                            tlValidator.checkAllSignature(tlService.getDbTL(tlInformationEdition.getTlId()));
                        }

                        // CHECK SCHEME INFORMATION ONLY
                        if (tlInformationEdition.isCheckToRun()) {

                            rulesRunner.validateSchemeInformation(tlInformationEdition.getTlId());
                            tlService.setTlCheckStatus(tlInformationEdition.getTlId());
                        }

                        response.setContent(schemeInfoUpdatedWithId);

                        response.setResponseStatus(HttpStatus.OK.toString());
                    } else {
                        response.setResponseStatus(HttpStatus.BAD_REQUEST.toString());
                    }
                } else {
                    response.setResponseStatus(HttpStatus.CONFLICT.toString());
                }

            } else {
                response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
            }
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }
        return response;
    }

    /**
     * Edit/create pointer entry in the trusted list and run rules on pointer
     *
     * @param tlPointerEdition
     * @return
     */
    @RequestMapping(value = "/tlPointer", method = RequestMethod.PUT)
    public @ResponseBody ServiceResponse<TLPointersToOtherTSL> editPointer(@RequestBody TLPointerEdition tlPointerEdition) {
        ServiceResponse<TLPointersToOtherTSL> response = new ServiceResponse<>();
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isAuthenticated(SecurityContextHolder.getContext().getAuthentication().getName())) {
            if (!tlPointerEdition.objectCorrect()) {
                response.setResponseStatus(HttpStatus.BAD_REQUEST.toString());
            }
            if (tlService.inStore(tlPointerEdition.getTlId(), tlPointerEdition.getCookie())) {
                if (TLUtils.checkDate(tlService.getEdt(tlPointerEdition.getTlId()), tlPointerEdition.getLastEditedDate())) {
                    TLPointersToOtherTSL pointerUpdated = tlEditPointerService.edit(tlPointerEdition.getTlId(), tlPointerEdition.getTlPointerObj(), tlPointerEdition.getEditAttribute());
                    TLPointersToOtherTSL pointerUpdatedWithId = null;
                    if (pointerUpdated != null) {
                        String id = pointerUpdated.getId();
                        TL tl = tlService.getTL(tlPointerEdition.getTlId());
                        // UPDATE OF EXISTING POINTER
                        if ((id != null) && !id.equalsIgnoreCase("")) {
                            pointerUpdatedWithId = new TLPointersToOtherTSL(tlPointerEdition.getTlId(), id, pointerUpdated.asTSLTypeV5());
                        } else {
                            // CREATION OF NEW POINTER
                            // GET "i" of NEXT POINTER

                            pointerUpdatedWithId = new TLPointersToOtherTSL(tlPointerEdition.getTlId(), tlPointerEdition.getTlId() + "_" + Tag.POINTERS_TO_OTHER_TSL + "_" + tl.getPointers().size(),
                                    pointerUpdated.asTSLTypeV5());
                        }

                        if (!tlService.getSignatureInfo(tlPointerEdition.getTlId()).getIndication().equals(SignatureStatus.NOT_SIGNED)) {
                            tlValidator.checkAllSignature(tlService.getDbTL(tlPointerEdition.getTlId()));
                        }

                        if (tlPointerEdition.isCheckToRun()) {
                            rulesRunner.validateAllPointers(tlPointerEdition.getTlId());
                            tlService.setTlCheckStatus(tlPointerEdition.getTlId());
                        }
                        response.setResponseStatus(HttpStatus.OK.toString());
                    } else {
                        response.setResponseStatus(HttpStatus.BAD_REQUEST.toString());
                    }

                    response.setContent(pointerUpdatedWithId);

                } else {
                    response.setResponseStatus(HttpStatus.CONFLICT.toString());
                }

            } else {
                response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
            }
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }
        return response;
    }

    @RequestMapping(value = "/delPointer", method = RequestMethod.PUT)
    public @ResponseBody ServiceResponse<List<TLPointersToOtherTSL>> deletePointer(@RequestBody TLPointerEdition tlPointerEdition) {
        ServiceResponse<List<TLPointersToOtherTSL>> response = new ServiceResponse<>();
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isAuthenticated(SecurityContextHolder.getContext().getAuthentication().getName())) {
            if (tlService.inStore(tlPointerEdition.getTlId(), tlPointerEdition.getCookie())) {
                if (TLUtils.checkDate(tlService.getEdt(tlPointerEdition.getTlId()), tlPointerEdition.getLastEditedDate())) {
                    int nbreRemoved = tlEditPointerService.delete(tlPointerEdition.getTlId(), tlPointerEdition.getTlPointerObj(), tlPointerEdition.getEditAttribute());

                    if (nbreRemoved > 0) {
                        TL tl = tlService.getTL(tlPointerEdition.getTlId());
                        response.setContent(tl.getPointers());
                        if (!tl.getSigStatus().equals(SignatureStatus.NOT_SIGNED)) {
                            tlValidator.checkAllSignature(tlService.getDbTL(tlPointerEdition.getTlId()));
                        }
                        // RUN ALL POINTERS CHECK
                        if (tlPointerEdition.isCheckToRun()) {

                            rulesRunner.validateAllPointers(tlPointerEdition.getTlId());
                            tlService.setTlCheckStatus(tlPointerEdition.getTlId());
                        }
                        response.setResponseStatus(HttpStatus.OK.toString());
                    } else {
                        response.setResponseStatus(HttpStatus.BAD_REQUEST.toString());
                    }

                } else {
                    response.setResponseStatus(HttpStatus.CONFLICT.toString());
                }

            } else {
                response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
            }
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }
        return response;
    }

    /********** SERVICE PROVIDER **********/

    @RequestMapping(value = "/tlServiceProvider", method = RequestMethod.PUT)
    public @ResponseBody ServiceResponse<TLServiceProvider> editServiceProvider(@RequestBody TLServiceProviderEdition tlServiceProviderEdition) {
        ServiceResponse<TLServiceProvider> response = new ServiceResponse<>();
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isAuthenticated(SecurityContextHolder.getContext().getAuthentication().getName())) {
            if (tlService.inStore(tlServiceProviderEdition.getTlId(), tlServiceProviderEdition.getCookie())) {
                if (TLUtils.checkDate(tlService.getEdt(tlServiceProviderEdition.getTlId()), tlServiceProviderEdition.getLastEditedDate())) {
                    TLServiceProvider serviceProviderUpdated = tlEditServiceProviderService.edit(tlServiceProviderEdition.getTlId(), tlServiceProviderEdition.getTlServiceProviderObj());
                    TLServiceProvider serviceProviderUpdatedWithId = null;
                    if (serviceProviderUpdated != null) {
                        TL tl = tlService.getTL(tlServiceProviderEdition.getTlId());
                        String id = serviceProviderUpdated.getId();
                        // UPDATE OF EXISTING PROVIDER
                        if ((id != null) && !id.equalsIgnoreCase("")) {
                            serviceProviderUpdatedWithId = new TLServiceProvider(tlServiceProviderEdition.getTlId(), id, serviceProviderUpdated.asTSLTypeV5());
                        } else {
                            // CREATION OF NEW PROVIDER
                            // GET "i" of NEXT PROVIDER
                            serviceProviderUpdatedWithId = new TLServiceProvider(tlServiceProviderEdition.getTlId(),
                                    tlServiceProviderEdition.getTlId() + "_" + Tag.TSP_SERVICE_PROVIDER + "_" + tl.getServiceProviders().size(), serviceProviderUpdated.asTSLTypeV5());
                        }

                        if (!tlService.getSignatureInfo(tlServiceProviderEdition.getTlId()).getIndication().equals(SignatureStatus.NOT_SIGNED)) {
                            tlValidator.checkAllSignature(tlService.getDbTL(tlServiceProviderEdition.getTlId()));
                        }

                        // CHECK PROVIDER ONLY
                        if (tlServiceProviderEdition.isCheckToRun()) {
                            rulesRunner.validateServiceProvider(tlServiceProviderEdition.getTlId(), getTSPId(serviceProviderUpdatedWithId), false);
                            tlService.setTlCheckStatus(tlServiceProviderEdition.getTlId());
                        }
                        response.setResponseStatus(HttpStatus.OK.toString());

                    } else {
                        response.setResponseStatus(HttpStatus.BAD_REQUEST.toString());
                    }

                    response.setContent(serviceProviderUpdatedWithId);

                } else {
                    response.setResponseStatus(HttpStatus.CONFLICT.toString());
                }

            } else {
                response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
            }
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }
        return response;
    }

    @RequestMapping(value = "/delServiceProvider", method = RequestMethod.PUT)
    public @ResponseBody ServiceResponse<List<TLServiceProvider>> deleteServiceProvider(@RequestBody TLServiceProviderEdition tlServiceProviderEdition) {
        ServiceResponse<List<TLServiceProvider>> response = new ServiceResponse<>();
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isAuthenticated(SecurityContextHolder.getContext().getAuthentication().getName())) {
            if (tlService.inStore(tlServiceProviderEdition.getTlId(), tlServiceProviderEdition.getCookie())) {
                if (TLUtils.checkDate(tlService.getEdt(tlServiceProviderEdition.getTlId()), tlServiceProviderEdition.getLastEditedDate())) {
                    int nbreRemoved = tlEditServiceProviderService.delete(tlServiceProviderEdition.getTlId(), tlServiceProviderEdition.getTlServiceProviderObj());

                    if (nbreRemoved > 0) {
                        TL tl = tlService.getTL(tlServiceProviderEdition.getTlId());
                        response.setContent(tl.getServiceProviders());
                        if (!tl.getSigStatus().equals(SignatureStatus.NOT_SIGNED)) {
                            tlValidator.checkAllSignature(tlService.getDbTL(tlServiceProviderEdition.getTlId()));
                        }
                        // RUN ALL PROVIDER CHECK
                        if (tlServiceProviderEdition.isCheckToRun()) {

                            rulesRunner.validateAllServiceProvider(tl.getTlId(), tl.getServiceProviders());
                            tlService.setTlCheckStatus(tlServiceProviderEdition.getTlId());
                        }
                        response.setResponseStatus(HttpStatus.OK.toString());
                    } else {
                        response.setResponseStatus(HttpStatus.BAD_REQUEST.toString());
                    }

                } else {
                    response.setResponseStatus(HttpStatus.CONFLICT.toString());
                }

            } else {
                response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
            }
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }

        return response;
    }

    /********** SERVICE **********/

    @RequestMapping(value = "/tlService", method = RequestMethod.PUT)
    public @ResponseBody ServiceResponse<TLServiceDto> editService(@RequestBody TLServiceEdition tlServiceEdition) {
        ServiceResponse<TLServiceDto> response = new ServiceResponse<>();
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isAuthenticated(SecurityContextHolder.getContext().getAuthentication().getName())) {
            if (tlService.inStore(tlServiceEdition.getTlId(), tlServiceEdition.getCookie())) {
                if (TLUtils.checkDate(tlService.getEdt(tlServiceEdition.getTlId()), tlServiceEdition.getLastEditedDate())) {
                    TLServiceDto serviceUpdated = tlEditServiceService.edit(tlServiceEdition.getTlId(), tlServiceEdition.getTlServiceObj(), tlServiceEdition.getParentIndex());
                    TLServiceDto serviceUpdatedWithId = null;
                    if (serviceUpdated != null) {
                        TL tl = tlService.getTL(tlServiceEdition.getTlId());
                        String id = serviceUpdated.getId();
                        // UPDATE OF EXISTING SERVICE
                        if ((id != null) && !id.equalsIgnoreCase("")) {
                            serviceUpdatedWithId = new TLServiceDto(tlServiceEdition.getTlId(), id, serviceUpdated.asTSLTypeV5());

                        } else {
                            // CREATION OF NEW SERVICE
                            // GET "i" of NEXT SERVICE
                            String newId = tlServiceEdition.getTlId() + "_" + Tag.TSP_SERVICE_PROVIDER + "_" + (tlServiceEdition.getParentIndex().get(0) + 1) + "_" + Tag.TSP_SERVICE + "_"
                                    + tl.getServiceProviders().get(tlServiceEdition.getParentIndex().get(0)).getTSPServices().size();
                            serviceUpdatedWithId = new TLServiceDto(tlServiceEdition.getTlId(), newId, serviceUpdated.asTSLTypeV5());

                        }

                        if (!tlService.getSignatureInfo(tlServiceEdition.getTlId()).getIndication().equals(SignatureStatus.NOT_SIGNED)) {
                            tlValidator.checkAllSignature(tlService.getDbTL(tlServiceEdition.getTlId()));
                        }

                        if (tlServiceEdition.isCheckToRun()) {
                            int parentId = tlServiceEdition.getParentIndex().get(0) + 1;
                            rulesRunner.validateService(tlServiceEdition.getTlId(), parentId, getServiceId(parentId, serviceUpdatedWithId));
                            tlService.setTlCheckStatus(tlServiceEdition.getTlId());
                        }

                        response.setResponseStatus(HttpStatus.OK.toString());
                    } else {
                        response.setResponseStatus(HttpStatus.BAD_REQUEST.toString());
                    }

                    // CHECKS ARE LAUNCH ON ONE SERVICE PROVIDER in the ApiChecksController [launchSvcProviderCheck]
                    response.setContent(serviceUpdatedWithId);

                } else {
                    response.setResponseStatus(HttpStatus.CONFLICT.toString());
                }

            } else {
                response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
            }
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }
        return response;
    }

    @RequestMapping(value = "/delService", method = RequestMethod.PUT)
    public @ResponseBody ServiceResponse<List<TLServiceProvider>> deleteService(@RequestBody TLServiceEdition tlServiceEdition) {
        ServiceResponse<List<TLServiceProvider>> response = new ServiceResponse<>();
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isAuthenticated(SecurityContextHolder.getContext().getAuthentication().getName())) {
            if (tlService.inStore(tlServiceEdition.getTlId(), tlServiceEdition.getCookie())) {
                if (TLUtils.checkDate(tlService.getEdt(tlServiceEdition.getTlId()), tlServiceEdition.getLastEditedDate())) {
                    int nbreRemoved = tlEditServiceService.delete(tlServiceEdition.getTlId(), tlServiceEdition.getTlServiceObj());

                    if (nbreRemoved > 0) {
                        TL tl = tlService.getTL(tlServiceEdition.getTlId());
                        response.setContent(tl.getServiceProviders());
                        if (!tl.getSigStatus().equals(SignatureStatus.NOT_SIGNED)) {
                            tlValidator.checkAllSignature(tlService.getDbTL(tlServiceEdition.getTlId()));
                        }
                        // RUN ALL PROVIDER CHECK
                        if (tlServiceEdition.isCheckToRun()) {

                            rulesRunner.validateAllServiceProvider(tl.getTlId(), tl.getServiceProviders());
                            tlService.setTlCheckStatus(tlServiceEdition.getTlId());
                        }
                        response.setResponseStatus(HttpStatus.OK.toString());
                    } else {
                        response.setResponseStatus(HttpStatus.BAD_REQUEST.toString());
                    }

                } else {
                    response.setResponseStatus(HttpStatus.CONFLICT.toString());
                }

            } else {
                response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
            }
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }
        return response;
    }

    /********** HISTORY **********/

    @RequestMapping(value = "/tlServiceHistory", method = RequestMethod.PUT)
    public @ResponseBody ServiceResponse<List<TLServiceHistory>> editHistory(@RequestBody TLHistoryEdition tlServiceHistory) {
        ServiceResponse<List<TLServiceHistory>> response = new ServiceResponse<>();
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isAuthenticated(SecurityContextHolder.getContext().getAuthentication().getName())) {

            if (tlService.inStore(tlServiceHistory.getTlId(), tlServiceHistory.getCookie())) {
                if (TLUtils.checkDate(tlService.getEdt(tlServiceHistory.getTlId()), tlServiceHistory.getLastEditedDate())) {
                    TLServiceHistory historyUpdated = tlEditHistoryService.edit(tlServiceHistory.getTlId(), tlServiceHistory.getTlHistoryObj(), tlServiceHistory.getParentIndex());
                    if (historyUpdated != null) {
                        TL tl = tlService.getTL(tlServiceHistory.getTlId());
                        // ParentIndex is list of the parent index of current history
                        // parentIndex[0] -> provider; parentIndex[1] -> service;
                        response.setContent(tl.getServiceProviders().get(tlServiceHistory.getParentIndex().get(0)).getTSPServices().get(tlServiceHistory.getParentIndex().get(1)).getHistory());
                        if (!tlService.getSignatureInfo(tlServiceHistory.getTlId()).getIndication().equals(SignatureStatus.NOT_SIGNED)) {
                            tlValidator.checkAllSignature(tlService.getDbTL(tlServiceHistory.getTlId()));
                        }

                        if (tlServiceHistory.isCheckToRun()) {
                            rulesRunner.validateService(tlServiceHistory.getTlId(), (tlServiceHistory.getParentIndex().get(0) + 1), (tlServiceHistory.getParentIndex().get(1) + 1));
                            tlService.setTlCheckStatus(tlServiceHistory.getTlId());
                        }

                        response.setResponseStatus(HttpStatus.OK.toString());
                    } else {
                        response.setResponseStatus(HttpStatus.BAD_REQUEST.toString());
                    }

                } else {
                    response.setResponseStatus(HttpStatus.CONFLICT.toString());
                }

            } else {
                response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
            }
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }

        return response;
    }

    @RequestMapping(value = "/delHistory", method = RequestMethod.PUT)
    public @ResponseBody ServiceResponse<List<TLServiceProvider>> deleteHistory(@RequestBody TLHistoryEdition tlServiceHistory) {
        ServiceResponse<List<TLServiceProvider>> response = new ServiceResponse<>();
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isAuthenticated(SecurityContextHolder.getContext().getAuthentication().getName())) {
            if (tlService.inStore(tlServiceHistory.getTlId(), tlServiceHistory.getCookie())) {
                if (TLUtils.checkDate(tlService.getEdt(tlServiceHistory.getTlId()), tlServiceHistory.getLastEditedDate())) {
                    int nbreRemoved = tlEditHistoryService.delete(tlServiceHistory.getTlId(), tlServiceHistory.getTlHistoryObj());

                    if (nbreRemoved > 0) {
                        TL tl = tlService.getTL(tlServiceHistory.getTlId());
                        response.setContent(tl.getServiceProviders());
                        if (!tl.getSigStatus().equals(SignatureStatus.NOT_SIGNED)) {
                            tlValidator.checkAllSignature(tlService.getDbTL(tlServiceHistory.getTlId()));
                        }
                        if (tlServiceHistory.isCheckToRun()) {

                            rulesRunner.validateAllServiceProvider(tl.getTlId(), tl.getServiceProviders());
                            tlService.setTlCheckStatus(tlServiceHistory.getTlId());
                        }
                        response.setResponseStatus(HttpStatus.OK.toString());
                    } else {
                        response.setResponseStatus(HttpStatus.BAD_REQUEST.toString());
                    }

                } else {
                    response.setResponseStatus(HttpStatus.CONFLICT.toString());
                }

            } else {
                response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
            }
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }

        return response;
    }

    /* Private method */
    /**
     * Get trust service provider index in TL from id
     *
     * @exception id
     *                is null or doesn't contain Tag.TSP_SERVICE_PROVIDER
     * @param tsp
     * @return
     */
    private int getTSPId(TLServiceProvider tsp) {
        try {
            String[] tspId = tsp.getId().split(Tag.TSP_SERVICE_PROVIDER + "_");
            return Integer.parseInt(tspId[1]);
        } catch (Exception e) {
            LOGGER.error("TSP id is null or don't respect format 'tlId_TSP_SERVICE_PROVIDER'.", e);
            throw new IllegalStateException(bundle.getString("tledition.general.error"));
        }
    }

    /**
     * Get service index in TL from id
     *
     * @exception id
     *                is null or doesn't contain Tag.TSP_SERVICE
     * @param service
     * @return
     */
    private int getServiceId(int tspIndex, TLServiceDto service) {
        try {
            String[] serviceId = service.getId().split(Tag.TSP_SERVICE_PROVIDER + "_" + tspIndex + "_" + Tag.TSP_SERVICE + "_");
            return Integer.parseInt(serviceId[1]);
        } catch (Exception e) {
            LOGGER.error("Service id is null or don't respect format 'tlId_TSP_SERVICE_PROVIDER'.", e);
            throw new IllegalStateException(bundle.getString("tledition.general.error"));
        }
    }

}
