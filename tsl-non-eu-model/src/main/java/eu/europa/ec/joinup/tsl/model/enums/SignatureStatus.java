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
package eu.europa.ec.joinup.tsl.model.enums;

public enum SignatureStatus {

    VALID,

    NOT_BASELINE_B,

    INDETERMINATE,

    INVALID,

    MORE_THAN_ONE_SIGNATURE,

    NOT_SIGNED,

    FILE_NOT_FOUND,

    CANNOT_BE_VALIDATED,
    
    // Signature doesn't cover the whole document
    NOT_FULL_SCOPE;

}
