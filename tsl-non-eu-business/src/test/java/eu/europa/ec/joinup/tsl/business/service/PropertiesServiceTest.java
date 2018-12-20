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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;
import eu.europa.ec.joinup.tsl.business.dto.Properties;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PropertiesServiceTest extends AbstractSpringTest {

    @Autowired
    private PropertiesService propertiesService;

    @Test
    public void AgetProperties() {
        List<Properties> list = propertiesService.getProperties();
        Assert.assertNotNull(list);
        Assert.assertEquals(48, list.size());

    }

    @Test
    public void CgetTSLTagValue() {
        assertTrue(propertiesService.getTSLTags().contains("http://test/TSLTag"));
    }

    @Test
    public void FgetTLStatusDeterminationApproachValue() {
        assertEquals("http://test/StatusDetn/CC", propertiesService.getTLStatusDeterminationApproachValue().get(0));
    }

    @Test
    public void HgetServiceTypeIdentifiers() {
        List<String> serviceTypeIdentifiers = propertiesService.getServiceTypeIdentifiers();
        assertTrue(CollectionUtils.isNotEmpty(serviceTypeIdentifiers));
        assertTrue(serviceTypeIdentifiers.contains("http://test/TrstSvc/Svctype/CA/PKC"));
    }

    @Test
    public void IgetServiceStatus() {
        List<String> serviceStatus = propertiesService.getServiceStatus();
        assertTrue(CollectionUtils.isNotEmpty(serviceStatus));
        assertTrue(serviceStatus.contains("http://test/Svcstatus/status"));
    }

    @Test
    public void JgetQualifiers() {
        List<String> qualifiers = propertiesService.getQualifiers();
        assertTrue(CollectionUtils.isNotEmpty(qualifiers));
        assertTrue(qualifiers.contains("http://test/SvcInfoExt/Qualifier"));
    }

    @Test
    public void KgetTLSchemeCommunityRulesValues() {
        List<String> schemeCommunityRulesValues = propertiesService.getTLSchemeCommunityRulesValues();
        assertTrue(CollectionUtils.isNotEmpty(schemeCommunityRulesValues));
        assertTrue(schemeCommunityRulesValues.contains("http://test/schemerules/CC"));
    }

    @Test
    public void MgetServiceTypeNationalRootCAQC() {
        assertTrue(StringUtils.isNotEmpty(propertiesService.getServiceTypeNationalRootCAQC()));
    }

    @Test
    public void PgetServiceTypeForAsiForeSignatureSealChecks() {
        List<String> l = propertiesService.getServiceTypeForAsiForeSignatureSealChecks();
        Assert.assertNotNull(l);
        Assert.assertEquals(2, l.size());
    }

    @Test
    public void QgetServiceTypeForExpiredCertRevocationInfoChecks() {
        List<String> l = propertiesService.getServiceTypeForExpiredCertRevocationInfoChecks();
        Assert.assertNotNull(l);
        Assert.assertEquals(7, l.size());
    }

    @Test
    public void RgetServiceTypeForAsiForeChecks() {
        List<String> l = propertiesService.getServiceTypeForAsiForeChecks();
        Assert.assertNotNull(l);
        Assert.assertEquals(6, l.size());
    }

    @Test
    public void Sadd() {
        List<Properties> list = propertiesService.getProperties();
        Assert.assertNotNull(list);

        Properties prop = new Properties();
        prop.setCodeList("ADRTYPE");
        prop.setDescription("description");
        prop.setLabel("label");
        propertiesService.add(prop);
        List<Properties> listTmp = propertiesService.getProperties();
        Assert.assertNotNull(listTmp);
        Assert.assertEquals(list.size() + 1, listTmp.size());

    }

    @Test
    public void Tdelete() {
        List<Properties> list = propertiesService.getProperties();
        Assert.assertNotNull(list);
        propertiesService.delete(list.get(0));
        List<Properties> listBis = propertiesService.getProperties();
        Assert.assertNotNull(list);
        Assert.assertEquals(list.size() - 1, listBis.size());

    }

}
