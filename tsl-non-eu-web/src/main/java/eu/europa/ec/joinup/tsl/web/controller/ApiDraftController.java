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

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.oxm.XmlMappingException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.europa.ec.joinup.tsl.business.dto.TrustedListsReport;
import eu.europa.ec.joinup.tsl.business.service.DraftStoreService;
import eu.europa.ec.joinup.tsl.business.service.TLDraftService;
import eu.europa.ec.joinup.tsl.business.service.TLService;
import eu.europa.ec.joinup.tsl.business.service.UserService;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.web.form.ServiceResponse;
//import eu.europa.ec.joinup.tsl.web.form.TLDraftDelete;
import eu.europa.ec.joinup.tsl.web.form.TLDraftDelete;
import eu.europa.ec.joinup.tsl.web.form.TLDraftRename;

@Controller
@RequestMapping(value = "/api/draft")
public class ApiDraftController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiDraftController.class);
    
    @Autowired
    private TLDraftService draftService;

    @Autowired
    private DraftStoreService draftStoreService;

    @Autowired
    private TLService tlService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/createDraftStore", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ServiceResponse<String> createDraftStore() {
        ServiceResponse<String> response = new ServiceResponse<>();
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isAuthenticated(SecurityContextHolder.getContext().getAuthentication().getName())) {

            response.setContent(draftStoreService.getNewDraftStore());
            response.setResponseStatus(HttpStatus.OK.toString());
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }
        return response;
    }

    @RequestMapping(value = "/checkDraftStore/{draftStoreId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ServiceResponse<Boolean> createDraftStore(@PathVariable String draftStoreId) {
        ServiceResponse<Boolean> response = new ServiceResponse<>();
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isAuthenticated(SecurityContextHolder.getContext().getAuthentication().getName())) {
            Boolean validity = draftStoreService.checkDraftStoreId(draftStoreId);
            response.setContent(validity);
            response.setResponseStatus(HttpStatus.OK.toString());
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }
        return response;
    }
    
    @RequestMapping(value = "/create/{draftStoreId}", method = RequestMethod.POST)
    public @ResponseBody ServiceResponse<TrustedListsReport> createEmptyDraft(@PathVariable String draftStoreId, @RequestBody String territory) {
        ServiceResponse<TrustedListsReport> response = new ServiceResponse<>();
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isAuthenticated(SecurityContextHolder.getContext().getAuthentication().getName())) {
            try {
                String username = SecurityContextHolder.getContext().getAuthentication().getName();
    
                DBTrustedLists newDraft = draftService.createEmptyDraft(draftStoreId, username, territory);
                TrustedListsReport tlInfo = draftService.finalizeDraftCreation(newDraft, username);
                response.setContent(tlInfo);
                response.setResponseStatus(HttpStatus.OK.toString());
            } catch (XmlMappingException | IOException e) {
                LOGGER.error("Cannot create empty TL : " + e.getMessage(), e);
                response.setResponseStatus(HttpStatus.BAD_REQUEST.toString());
            }
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }
        return response;
    }

    @RequestMapping(value = "/duplicate", method = RequestMethod.POST)
    public @ResponseBody ServiceResponse<TrustedListsReport> duplicate(@RequestBody TrustedListsReport origTLR) {
        ServiceResponse<TrustedListsReport> response = new ServiceResponse<>();
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isAuthenticated(SecurityContextHolder.getContext().getAuthentication().getName())) {
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            
            DBTrustedLists newDraft = draftService.duplicateDraft(origTLR, userName);
            TrustedListsReport tlInfo = draftService.finalizeDraftCreation(newDraft, userName);
            response.setContent(tlInfo);
            response.setResponseStatus(HttpStatus.OK.toString());
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }
        return response;
    }
    
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody ServiceResponse<String> deleteDraft(@RequestBody TLDraftDelete tlDraftDelete) {
        ServiceResponse<String> response = new ServiceResponse<>();
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isAuthenticated(SecurityContextHolder.getContext().getAuthentication().getName())) {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            if (tlService.inStore(tlDraftDelete.getTlId(), tlDraftDelete.getCookie())) {
                try {
                    tlService.deleteDraft(tlDraftDelete.getTlId(), username);
                    response.setResponseStatus(HttpStatus.OK.toString());
                } catch (Exception e) {
                    response.setResponseStatus(HttpStatus.NOT_FOUND.toString());
                }
            } else {
                response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
            }
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }
        return response;
    }
    
    @RequestMapping(value = "/rename", method = RequestMethod.PUT)
    public @ResponseBody ServiceResponse<String> renameDraft(@RequestBody TLDraftRename tlDraftRename) {
        ServiceResponse<String> response = new ServiceResponse<>();
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isAuthenticated(SecurityContextHolder.getContext().getAuthentication().getName())) {
            try {
                draftService.renameDraft(tlDraftRename.getTlId(), tlDraftRename.getNewName());
                response.setResponseStatus(HttpStatus.OK.toString());
            } catch (IllegalArgumentException e) {
                response.setResponseStatus(HttpStatus.BAD_REQUEST.toString());
                response.setErrorMessage(e.getMessage());
            }
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }
        return response;
    }
}
