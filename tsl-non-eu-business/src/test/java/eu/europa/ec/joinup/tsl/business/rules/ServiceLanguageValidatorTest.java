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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;
import eu.europa.ec.joinup.tsl.business.dto.CheckDTO;
import eu.europa.ec.joinup.tsl.business.dto.CheckResultDTO;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLName;
import eu.europa.ec.joinup.tsl.model.DBCheck;
import eu.europa.ec.joinup.tsl.model.enums.CheckName;
import eu.europa.ec.joinup.tsl.model.enums.CheckStatus;
import eu.europa.ec.joinup.tsl.model.enums.Tag;

public class ServiceLanguageValidatorTest extends AbstractSpringTest {

    @Autowired
    private ServiceLanguageValidator languageValidator;

    private int getNbErrors(List<CheckResultDTO> results) {
        int nbErrors = 0;
        for (CheckResultDTO dbCheckResult : results) {
            if (CheckStatus.ERROR.equals(dbCheckResult.getStatus())) {
                nbErrors++;
            }
        }
        return nbErrors;
    }

    @Test
    public void isAttributeLanguagePresentOK() {
        DBCheck check = new DBCheck();
        check.setTarget(Tag.LANGUAGE_FIELDS);
        check.setName(CheckName.IS_ATTRIBUTE_LANG_PRESENT);
        check.setPriority(CheckStatus.ERROR);

        CheckDTO checkDto = new CheckDTO(check);

        assertTrue(languageValidator.isSupported(checkDto));

        List<TLName> names = new ArrayList<>();
        TLName n1 = new TLName();
        n1.setId("1");
        n1.setLanguage("bla");
        names.add(n1);
        TLName n2 = new TLName();
        n2.setLanguage("bla");
        names.add(n2);
        
        List<CheckDTO> checks = new ArrayList<>();
        checks.add(checkDto);

        List<CheckResultDTO> results = new ArrayList<>();
        languageValidator.runLanguageCheck("location", checks, names, results);
        assertEquals(2, CollectionUtils.size(results));
        assertEquals(0, getNbErrors(results));
    }

    @Test
    public void isAttributeLanguagePresentKO() {
        DBCheck check = new DBCheck();
        check.setTarget(Tag.LANGUAGE_FIELDS);
        check.setName(CheckName.IS_ATTRIBUTE_LANG_PRESENT);
        check.setPriority(CheckStatus.ERROR);

        CheckDTO checkDto = new CheckDTO(check);

        List<TLName> names = new ArrayList<>();
        TLName n1 = new TLName();
        n1.setId("1");
        names.add(n1);
        TLName n2 = new TLName();
        n2.setLanguage("bla");
        names.add(n2);
        
        List<CheckDTO> checks = new ArrayList<>();
        checks.add(checkDto);

        List<CheckResultDTO> results = new ArrayList<>();
        languageValidator.runLanguageCheck("location", checks, names, results);
        assertEquals(2, CollectionUtils.size(results));
        assertEquals(1, getNbErrors(results));
    }

    @Test
    public void isAttributeLanguageLowerCaseOK() {
        DBCheck check = new DBCheck();
        check.setTarget(Tag.LANGUAGE_FIELDS);
        check.setName(CheckName.IS_ATTRIBUTE_LANG_LOWERCASE);
        check.setPriority(CheckStatus.ERROR);

        CheckDTO checkDto = new CheckDTO(check);

        assertTrue(languageValidator.isSupported(checkDto));

        List<TLName> names = new ArrayList<>();
        TLName n1 = new TLName();
        n1.setId("1");
        n1.setLanguage("bla");
        names.add(n1);
        TLName n2 = new TLName();
        n2.setLanguage("bla");
        names.add(n2);
        
        List<CheckDTO> checks = new ArrayList<>();
        checks.add(checkDto);

        List<CheckResultDTO> results = new ArrayList<>();
        languageValidator.runLanguageCheck("location", checks, names, results);
        assertEquals(2, CollectionUtils.size(results));
        assertEquals(0, getNbErrors(results));
    }

    @Test
    public void isAttributeLanguageLowerCaseKO() {
        DBCheck check = new DBCheck();
        check.setTarget(Tag.LANGUAGE_FIELDS);
        check.setName(CheckName.IS_ATTRIBUTE_LANG_LOWERCASE);
        check.setPriority(CheckStatus.ERROR);

        CheckDTO checkDto = new CheckDTO(check);

        List<TLName> names = new ArrayList<>();
        TLName n1 = new TLName();
        n1.setId("1");
        n1.setLanguage("BLA");
        names.add(n1);
        TLName n2 = new TLName();
        n2.setLanguage("bla");
        names.add(n2);
        
        List<CheckDTO> checks = new ArrayList<>();
        checks.add(checkDto);

        List<CheckResultDTO> results = new ArrayList<>();
        languageValidator.runLanguageCheck("location", checks, names, results);
        assertEquals(2, CollectionUtils.size(results));
        assertEquals(1, getNbErrors(results));
    }

    @Test
    public void isAttributeLanguageAllowedOK() {
        DBCheck check = new DBCheck();
        check.setTarget(Tag.LANGUAGE_FIELDS);
        check.setName(CheckName.IS_ATTRIBUTE_LANG_ALLOWED);
        check.setPriority(CheckStatus.ERROR);

        CheckDTO checkDto = new CheckDTO(check);

        assertTrue(languageValidator.isSupported(checkDto));

        List<TLName> names = new ArrayList<>();
        TLName n1 = new TLName();
        n1.setId("1");
        n1.setLanguage("fr");
        names.add(n1);
        TLName n2 = new TLName();
        n2.setLanguage("en");
        names.add(n2);
        
        List<CheckDTO> checks = new ArrayList<>();
        checks.add(checkDto);

        List<CheckResultDTO> results = new ArrayList<>();
        languageValidator.runLanguageCheck("location", checks, names, results);
        assertEquals(2, CollectionUtils.size(results));
        assertEquals(CheckStatus.SUCCESS, results.get(0).getStatus());
        assertEquals(CheckStatus.SUCCESS, results.get(1).getStatus());
    }

    @Test
    public void isAttributeLanguageAllowedKO() {
        DBCheck check = new DBCheck();
        check.setTarget(Tag.LANGUAGE_FIELDS);
        check.setName(CheckName.IS_ATTRIBUTE_LANG_ALLOWED);
        check.setPriority(CheckStatus.ERROR);

        CheckDTO checkDto = new CheckDTO(check);

        List<TLName> names = new ArrayList<>();
        TLName n1 = new TLName();
        n1.setId("1");
        n1.setLanguage("xx");
        names.add(n1);
        TLName n2 = new TLName();
        n2.setLanguage("en");
        names.add(n2);
        
        List<CheckDTO> checks = new ArrayList<>();
        checks.add(checkDto);

        List<CheckResultDTO> results = new ArrayList<>();
        languageValidator.runLanguageCheck("location", checks, names, results);
        assertEquals(2, CollectionUtils.size(results));
        assertEquals(1, getNbErrors(results));
    }

    @Test
    public void isListContainLanguageENOK() {
        DBCheck check = new DBCheck();
        check.setTarget(Tag.LANGUAGE_FIELDS);
        check.setName(CheckName.IS_LIST_CONTAIN_LANG_EN);
        check.setPriority(CheckStatus.ERROR);

        CheckDTO checkDto = new CheckDTO(check);

        assertTrue(languageValidator.isSupported(checkDto));

        List<TLName> names = new ArrayList<>();
        TLName n1 = new TLName();
        n1.setLanguage("fr");
        names.add(n1);
        TLName n2 = new TLName();
        n2.setLanguage("en");
        names.add(n2);
        
        List<CheckDTO> checks = new ArrayList<>();
        checks.add(checkDto);

        List<CheckResultDTO> results = new ArrayList<>();
        languageValidator.runLanguageCheck("location", checks, names, results);
        assertEquals(1, CollectionUtils.size(results));
        assertEquals(0, getNbErrors(results));
    }

    @Test
    public void isListContainLanguageENKO() {
        DBCheck check = new DBCheck();
        check.setTarget(Tag.LANGUAGE_FIELDS);
        check.setName(CheckName.IS_LIST_CONTAIN_LANG_EN);
        check.setPriority(CheckStatus.ERROR);

        CheckDTO checkDto = new CheckDTO(check);

        List<TLName> names = new ArrayList<>();
        TLName n1 = new TLName();
        n1.setLanguage("fr");
        names.add(n1);
        TLName n2 = new TLName();
        n2.setLanguage("es");
        names.add(n2);
        
        List<CheckDTO> checks = new ArrayList<>();
        checks.add(checkDto);

        List<CheckResultDTO> results = new ArrayList<>();
        languageValidator.runLanguageCheck("location", checks, names, results);
        assertEquals(1, CollectionUtils.size(results));
        assertEquals(1, getNbErrors(results));
    }

}
