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
package eu.europa.ec.joinup.tsl.web.form;

import java.util.Date;
import java.util.List;

import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceHistory;

public class TLHistoryEdition {

    private int tlId;
    private TLServiceHistory tlHistoryObj;
    private String tlTerritoryCode;
    private List<Integer> parentIndex;
    private Date lastEditedDate;
    private String cookie;
    private boolean checkToRun;

    public int getTlId() {
        return tlId;
    }

    public void setTlId(int tlId) {
        this.tlId = tlId;
    }

    public TLServiceHistory getTlHistoryObj() {
        return tlHistoryObj;
    }

    public void setTlHistoryObj(TLServiceHistory tlHistoryObj) {
        this.tlHistoryObj = tlHistoryObj;
    }

    public String getTlTerritoryCode() {
        return tlTerritoryCode;
    }

    public void setTlTerritoryCode(String tlTerritoryCode) {
        this.tlTerritoryCode = tlTerritoryCode;
    }

    public List<Integer> getParentIndex() {
        return parentIndex;
    }

    public void setParentIndex(List<Integer> parentIndex) {
        this.parentIndex = parentIndex;
    }

    public Date getLastEditedDate() {
        return lastEditedDate;
    }

    public void setLastEditedDate(Date lastEditedDate) {
        this.lastEditedDate = lastEditedDate;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public boolean isCheckToRun() {
        return checkToRun;
    }

    public void setCheckToRun(boolean checkToRun) {
        this.checkToRun = checkToRun;
    }

}
