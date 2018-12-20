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
package eu.europa.ec.joinup.tsl.business.dto.tl;

import eu.europa.esig.jaxb.v5.tsl.MultiLangStringTypeV5;
import eu.europa.esig.jaxb.v5.tsl.NonEmptyMultiLangURITypeV5;

public class TLSchemePolicy extends TLGeneric {

    public TLSchemePolicy() {
    }

    public TLSchemePolicy(int iddb, String location, NonEmptyMultiLangURITypeV5 uri) {
        super(iddb, location, uri);
    }

    public TLSchemePolicy(int iddb, String location, MultiLangStringTypeV5 name) {
        super(iddb, location, name);
    }

    public MultiLangStringTypeV5 asTSLTypeV5() {
        MultiLangStringTypeV5 multiString = new MultiLangStringTypeV5();
        multiString.setLang(this.getLanguage());
        multiString.setValue(this.getValue());
        return multiString;
    }

}
