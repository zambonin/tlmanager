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

import java.util.concurrent.Future;

import javax.mail.internet.InternetAddress;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.service.CacheDataLoader;

@Service
public class URIValidator extends GenericValidator {

    private static final String MAILTO = "mailto:";
    
    private static final Logger LOGGER = LoggerFactory.getLogger(URIValidator.class);

    @Autowired
    private CacheDataLoader cacheDataLoader;

    @Cacheable(value = "externalResourcesCache", key = "#url")
    public boolean isCorrectUrl(String url) {
        String[] schemes = { "http", "https" }; // DEFAULT schemes = "http", "https", "ftp"
        UrlValidator urlValidator = new UrlValidator(schemes);
        boolean valid = urlValidator.isValid(url);
        if (!valid) {
            LOGGER.debug("Not valid URL detected '" + url + "'");
        }
        return valid;
    }

    public boolean isCorrectUri(String uri) {
        if ((uri != null) && StringUtils.isNotEmpty(uri)) {
            if (isMailTo(uri)) {
                return isCorrectMailTo(uri);
            } else {
                return isCorrectUrl(uri);
            }
        }
        return false;
    }
    
    @Async
    public Future<Boolean> isAccessibleUri(String uri) {
        return new AsyncResult<>(cacheDataLoader.isAccessibleUri(uri));
    }

    public boolean isSecureURI(String uri) {
        if (uri.toLowerCase().startsWith("https")) {
            return true;
        } else {
            return false;
        }
    }
    
    private boolean isCorrectMailTo(String email) {
        email = email.substring(MAILTO.length());
        try {
            InternetAddress emailAddress = new InternetAddress(email);
            emailAddress.validate();
            return true;
        } catch (Exception e) {
            LOGGER.warn("Unable to validate email address '" + email + "' : " + e.getMessage());
            return false;
        }
    }

    public boolean isMailTo(String uri) {
        return StringUtils.startsWith(uri, MAILTO);
    }
}
