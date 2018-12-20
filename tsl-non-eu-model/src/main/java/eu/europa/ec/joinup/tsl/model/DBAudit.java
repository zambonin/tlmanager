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
package eu.europa.ec.joinup.tsl.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import eu.europa.ec.joinup.tsl.model.enums.AuditAction;
import eu.europa.ec.joinup.tsl.model.enums.AuditStatus;
import eu.europa.ec.joinup.tsl.model.enums.AuditTarget;

@Entity
@Table(name = "TL_AUDIT")
public class DBAudit {

    @Id
    @GeneratedValue
    @Column(name = "AUDIT_ID", nullable = false, updatable = false)
    private int id;

    @Column(name = "DATE", nullable = false, updatable = false)
    private Date date;

    @Column(name = "TARGET", nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private AuditTarget target;

    @Column(name = "ACTION", nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private AuditAction action;

    @Column(name = "STATUS", nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private AuditStatus status;

    @Column(name = "FILEID", nullable = true, updatable = false)
    private int fileId;

    @Column(name = "FILE_DIGEST", nullable = true, updatable = false)
    private String fileDigest;

    @Column(name = "COUNTRY_CODE", nullable = true, updatable = false)
    private String countryCode;

    @Column(name = "USERNAME", nullable = true, updatable = false)
    private String username;

    @Column(name = "INFOS", length = 4096, nullable = true, updatable = false)
    private String infos;

    public DBAudit() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }

        DBAudit dbAudit = (DBAudit) o;

        if (id != dbAudit.id) {
            return false;
        }
        if (!date.equals(dbAudit.date)) {
            return false;
        }
        if (target != dbAudit.target) {
            return false;
        }
        if (action != dbAudit.action) {
            return false;
        }
        if (status != dbAudit.status) {
            return false;
        }
        if (fileDigest != null ? !fileDigest.equals(dbAudit.fileDigest) : dbAudit.fileDigest != null) {
            return false;
        }
        if (countryCode != null ? !countryCode.equals(dbAudit.countryCode) : dbAudit.countryCode != null) {
            return false;
        }
        if (username != null ? !username.equals(dbAudit.username) : dbAudit.username != null) {
            return false;
        }
        return infos != null ? infos.equals(dbAudit.infos) : dbAudit.infos == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = (31 * result) + date.hashCode();
        result = (31 * result) + target.hashCode();
        result = (31 * result) + action.hashCode();
        result = (31 * result) + status.hashCode();
        result = (31 * result) + (fileDigest != null ? fileDigest.hashCode() : 0);
        result = (31 * result) + (countryCode != null ? countryCode.hashCode() : 0);
        result = (31 * result) + (username != null ? username.hashCode() : 0);
        result = (31 * result) + (infos != null ? infos.hashCode() : 0);
        return result;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public java.util.Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public AuditTarget getTarget() {
        return target;
    }

    public void setTarget(AuditTarget target) {
        this.target = target;
    }

    public AuditAction getAction() {
        return action;
    }

    public void setAction(AuditAction action) {
        this.action = action;
    }

    public AuditStatus getStatus() {
        return status;
    }

    public void setStatus(AuditStatus status) {
        this.status = status;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public String getFileDigest() {
        return fileDigest;
    }

    public void setFileDigest(String fileDigest) {
        this.fileDigest = fileDigest;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getInfos() {
        return infos;
    }

    public void setInfos(String infos) {
        this.infos = infos;
    }
}
