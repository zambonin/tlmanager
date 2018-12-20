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

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.europa.ec.joinup.tsl.business.dto.TLDifference;
import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.business.service.TLService;
import eu.europa.ec.joinup.tsl.business.service.SignatureChangeService;
import eu.europa.ec.joinup.tsl.model.enums.Tag;
import eu.europa.ec.joinup.tsl.web.form.ServiceResponse;

@Controller
@RequestMapping(value = "/api/changes")
public class ApiChangesController {

    @Autowired
    private TLService tlService;

    @Autowired
    private SignatureChangeService signatureChangeService;

    @RequestMapping(value = "/draft/{currentId}/{comparedId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ServiceResponse<List<List<TLDifference>>> getChangescurrent(@PathVariable("currentId") int currentId, @PathVariable("comparedId") int comparedId) {
        TL current = tlService.getTL(currentId);
        TL compared = tlService.getTL(comparedId);
        ServiceResponse<List<List<TLDifference>>> response = new ServiceResponse<List<List<TLDifference>>>();
        List<List<TLDifference>> diffList = new ArrayList<List<TLDifference>>();
        List<TLDifference> diffSchemeInfo = new ArrayList<TLDifference>();
        List<TLDifference> diffPointers = new ArrayList<TLDifference>();
        List<TLDifference> diffSignature = new ArrayList<TLDifference>();

        diffSchemeInfo.addAll(current.asPublishedDiff(compared));
        diffPointers.addAll(current.getPointersDiff(compared.getPointers(), compared.getId() + "_" + Tag.POINTERS_TO_OTHER_TSL));

        diffList.add(diffSchemeInfo);
        diffList.add(diffPointers);
        if ((current.getServiceProviders() != null) && (compared.getServiceProviders() != null)) {
            List<TLDifference> diffProviders = new ArrayList<TLDifference>();
            diffProviders.addAll(current.getTrustServiceProvidersDiff(compared.getServiceProviders(), current.getId() + "_" + Tag.TSP_SERVICE_PROVIDER));
            diffList.add(diffProviders);
        }

        diffSignature = signatureChangeService.getSignatureChanges(current, compared);
        diffList.add(diffSignature);

        response.setContent(diffList);
        response.setResponseStatus(HttpStatus.OK.toString());
        return response;
    }

}
