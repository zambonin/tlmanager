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

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;
import eu.europa.ec.joinup.tsl.business.dto.audit.Audit;
import eu.europa.ec.joinup.tsl.business.dto.audit.AuditSearchDTO;
import eu.europa.ec.joinup.tsl.business.repository.AuditRepository;
import eu.europa.ec.joinup.tsl.model.enums.AuditAction;
import eu.europa.ec.joinup.tsl.model.enums.AuditStatus;
import eu.europa.ec.joinup.tsl.model.enums.AuditTarget;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AuditServiceTest extends AbstractSpringTest {

    @Autowired
    private AuditService auditService;

    @Autowired
    private AuditRepository auditRepository;

    @Test
    public void AtestAddAudit() {
        auditRepository.deleteAll();
        Audit audit = auditService.addAuditLog(AuditTarget.DRAFT_TL, AuditAction.CREATE, AuditStatus.SUCCES, "BE", 0, "TLM", "TEST INFOS SUPP");
        Assert.assertNotNull(audit);
        assertSame(audit.getTarget(), AuditTarget.DRAFT_TL);

        List<Audit> auditList = auditService.getAllAuditOrderByDateDesc();
        Assert.assertNotNull(auditList);
        assertTrue(auditList.size() > 0);
    }

    @Test
    public void BtestAuditBy() {
        auditRepository.deleteAll();
        Audit audit = auditService.addAuditLog(AuditTarget.DRAFT_TL, AuditAction.CREATE, AuditStatus.SUCCES, "BE", 0, "TLM", "TEST INFOS SUPP");
        Assert.assertNotNull(audit);
        assertSame(audit.getTarget(), AuditTarget.DRAFT_TL);

        audit = auditService.addAuditLog(AuditTarget.DRAFT_TL, AuditAction.CREATE, AuditStatus.SUCCES, "LU", 0, "TLM", "TEST INFOS SUPP");
        Assert.assertNotNull(audit);
        assertSame(audit.getTarget(), AuditTarget.DRAFT_TL);

        Calendar c = new GregorianCalendar();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 1);
        Date d1 = c.getTime();

        AuditSearchDTO dto = new AuditSearchDTO();
        dto.setCountryCode("BE");
        dto.setTarget(AuditTarget.DRAFT_TL);
        dto.setAction(AuditAction.CREATE);
        dto.setStartDate(d1);

        List<Audit> auditList = auditService.searchAuditByCriteria(dto);
        Assert.assertNotNull(auditList);
        assertTrue(auditList.size() == 1);

        dto.setCountryCode("BE");
        dto.setTarget(AuditTarget.DRAFT_TL);
        dto.setAction(AuditAction.CREATE);
        dto.setStartDate(d1);

        List<Audit> auditList2 = auditService.searchAuditByCriteria(dto);
        Assert.assertNotNull(auditList2);
        assertTrue(auditList2.size() == 1);

        dto = new AuditSearchDTO();
        dto.setAction(AuditAction.CREATE);
        List<Audit> auditList3 = auditService.searchAuditByCriteria(dto);
        Assert.assertNotNull(auditList3);
        assertTrue(auditList3.size() == 2);

    }

}
