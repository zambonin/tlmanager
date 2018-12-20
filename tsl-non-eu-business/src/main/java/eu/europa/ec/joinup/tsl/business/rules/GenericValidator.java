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

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.dto.tl.TLGeneric;

@Service
public class GenericValidator {

    public boolean isPresent(Object object) {
        return object != null;
    }

    public boolean isGreaterThanZero(Integer integer) {
        return ((integer != null) && (integer.intValue() > 0));
    }

    public boolean isEquals(String expectedString, String value) {
        return StringUtils.equals(expectedString, value);
    }

    public boolean isNotEmpty(String value) {
        return StringUtils.isNotEmpty(value);
    }

    public boolean isCollectionNotEmpty(List<?> objects) {
        return CollectionUtils.isNotEmpty(objects);
    }
    
    public boolean isPresentAndNotEmpty(String value) {
        return isPresent(value) && isNotEmpty(value);
    }
    
    public boolean isCollectionPresentAndNotEmpty(List<? extends TLGeneric> objects) {
        if(!isCollectionNotEmpty(objects)) {
            return false;
        }
        for(TLGeneric object : objects) {
            if(!isNotEmpty(object.getValue())) {
                return false;
            }
        }
        return true;
    }

    public boolean isLowerCase(String value) {
        return StringUtils.equals(value, StringUtils.lowerCase(value)) && isNotEmpty(value);
    }

    public boolean isIn(String value, List<String> allowedValues) {
        return (allowedValues != null) && allowedValues.contains(value);
    }

    public boolean isEquals(Integer expected, Integer value) {
        return (expected != null) && (value != null) && (expected.intValue() == value.intValue());
    }
}
