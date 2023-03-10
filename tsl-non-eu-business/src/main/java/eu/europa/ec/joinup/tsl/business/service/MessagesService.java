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
package eu.europa.ec.joinup.tsl.business.service;

import java.util.Enumeration;
import java.util.ResourceBundle;

import org.springframework.stereotype.Service;

/**
 * Loop through messages.properties file and return file as angular $scope property in string
 */
@Service
public class MessagesService {
    private static final ResourceBundle bundle = ResourceBundle.getBundle("messages");

    @SuppressWarnings("rawtypes")
    public StringBuffer getMessagesBundle() {
        StringBuffer sb = new StringBuffer();
        Enumeration bundleKeys = bundle.getKeys();
        while (bundleKeys.hasMoreElements()) {

            String key = (String) bundleKeys.nextElement();
            String value = bundle.getString(key);
            sb.append("$scope.");
            key = key.replace(".", "_");
            sb.append(key);
            sb.append(" = \"");
            sb.append(value);
            sb.append("\";\n");

        }

        return sb;

    }
}
