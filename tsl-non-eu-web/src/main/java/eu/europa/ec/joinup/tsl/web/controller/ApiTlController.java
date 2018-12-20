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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.europa.ec.joinup.tsl.business.dto.TLSignature;
import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.business.service.RulesRunnerService;
import eu.europa.ec.joinup.tsl.business.service.TLDraftService;
import eu.europa.ec.joinup.tsl.business.service.TLService;
import eu.europa.ec.joinup.tsl.business.service.TLValidator;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.model.enums.TLStatus;
import eu.europa.ec.joinup.tsl.web.form.ServiceResponse;
import eu.europa.ec.joinup.tsl.web.form.TLCookie;

@Controller
@RequestMapping(value = "/api/tl")
public class ApiTlController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiTlController.class);

    @Autowired
    private TLDraftService draftService;

    @Autowired
    private TLService tlService;

    @Autowired
    private TLValidator tlValidator;

    @Autowired
    private RulesRunnerService rulesRunner;

    @RequestMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ServiceResponse<TL> getTlById(@RequestBody TLCookie object) {
        ServiceResponse<TL> response = new ServiceResponse<>();

        if (tlService.inStore(object.getTlId(), object.getCookie())) {
            try {
                TL tl = tlService.getTL(object.getTlId());
                if (tl != null) {
                    if (tl.getDbStatus().equals(TLStatus.DRAFT)) {
                        tlService.updateLastAccessDate(tl.getTlId());
                    }
                    response.setContent(tl);
                    response.setResponseStatus(HttpStatus.OK.toString());
                } else {
                    response.setResponseStatus(HttpStatus.NOT_FOUND.toString());
                }
            } catch (IllegalStateException e) {
                response.setErrorMessage(e.getMessage());
                response.setResponseStatus(HttpStatus.BAD_REQUEST.toString());
                LOGGER.error("Error while trying to parse a TL ", e);
            }
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }
        return response;
    }

    @RequestMapping(value = "/edtDate/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ServiceResponse<Date> getEditedDate(@PathVariable int id) {
        ServiceResponse<Date> response = new ServiceResponse<>();
        Date edt = tlService.getEdt(id);
        if (edt != null) {
            response.setContent(edt);
            response.setResponseStatus(HttpStatus.OK.toString());
        } else {
            response.setResponseStatus(HttpStatus.NOT_FOUND.toString());
        }
        return response;
    }

    @RequestMapping(value = "/conflict", method = RequestMethod.PUT)
    @ResponseBody
    public ServiceResponse<TL> getDraftConflictTL(@RequestBody TL tl) {
        ServiceResponse<TL> response = new ServiceResponse<>();
        if (tl.getDbStatus().equals(TLStatus.DRAFT)) {
            if ((SecurityContextHolder.getContext().getAuthentication() != null)) {
                response.setResponseStatus(HttpStatus.OK.toString());

                DBTrustedLists draft = draftService.conflictTLtoDraft(tl);

                // CHECK SIGNATURE STATUS
                tlValidator.checkTlWithKeyStore(draft);

                TL draftTL = tlService.getTL(draft.getId());
                rulesRunner.runAllRules(draftTL);

                tlService.setTlCheckStatus(draftTL.getTlId());

                response.setContent(draftTL);
                response.setResponseStatus(HttpStatus.OK.toString());
            } else {
                response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
            }
        } else {
            response.setResponseStatus(HttpStatus.FORBIDDEN.toString());
        }

        return response;
    }

    @RequestMapping(value = "/download/{id}/{cookieId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
    public void download(@PathVariable int id, @PathVariable String cookieId, HttpServletRequest request, HttpServletResponse response) {
        if (tlService.inStore(id, cookieId)) {
            try {
                // TODO : Update method with file utils
                final int TAILLE_TAMPON = 10240; // 10ko
                File fichier = tlService.getXmlFile(id);
                TL tl = tlService.getTL(id);

                if (!fichier.exists()) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }

                String type = "application/octet-stream";

                response.reset();
                response.setBufferSize(TAILLE_TAMPON);
                response.setContentType(type);
                response.setHeader("Content-Length", String.valueOf(fichier.length()));
                response.setHeader("Content-Disposition",
                        "attachment; filename=\"" + tl.getDbName() + ".xml\"");

                BufferedInputStream entree = null;
                BufferedOutputStream sortie = null;
                try {
                    entree = new BufferedInputStream(new FileInputStream(fichier), TAILLE_TAMPON);
                    sortie = new BufferedOutputStream(response.getOutputStream(), TAILLE_TAMPON);

                    byte[] tampon = new byte[TAILLE_TAMPON];
                    int longueur;
                    while ((longueur = entree.read(tampon)) > 0) {
                        sortie.write(tampon, 0, longueur);
                    }
                } finally {
                    sortie.close();
                    entree.close();
                }
            } catch (IOException ex) {
                throw new RuntimeException("IOError writing file to output stream");
            }
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }

    }

    @RequestMapping(value = "/signatureInfo", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ServiceResponse<TLSignature> getSignatureInfo(@RequestBody TLCookie cookie) {
        ServiceResponse<TLSignature> response = new ServiceResponse<>();
        if (tlService.inStore(cookie.getTlId(), cookie.getCookie())) {
            response.setContent(tlService.getSignatureInfo(cookie.getTlId()));
            response.setResponseStatus(HttpStatus.OK.toString());
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }
        return response;
    }

    @RequestMapping(value = "/download/sha2/{id}/{cookieId}", method = RequestMethod.GET)
    public void downloadSha2(@PathVariable int id, @PathVariable String cookieId, HttpServletRequest request, HttpServletResponse response) {
        if (tlService.inStore(id, cookieId)) {
            try {
                TL tl = tlService.getTL(id);
                String sha2Value = tlService.getSha2Value(id);

                String type = "application/octet-stream";

                response.reset();
                response.setContentType(type);
                response.setHeader("Content-Disposition",
                        "attachment; filename=\"" + tl.getDbName() + ".sha2\"");

                OutputStream outStream = response.getOutputStream();
                outStream.write(sha2Value.getBytes());
                outStream.flush();
                outStream.close();

            } catch (IOException ex) {
                throw new RuntimeException("IOError writing file to output stream");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/switchCheckToRun", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ServiceResponse<Boolean> switchCheckToRun(@RequestBody TLCookie cookie) {
        ServiceResponse<Boolean> response = new ServiceResponse<>();
        if (tlService.inStore(cookie.getTlId(), cookie.getCookie())) {
            response.setContent(tlService.switchCheckToRun(cookie.getTlId()));
            response.setResponseStatus(HttpStatus.OK.toString());
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }
        return response;
    }

}
