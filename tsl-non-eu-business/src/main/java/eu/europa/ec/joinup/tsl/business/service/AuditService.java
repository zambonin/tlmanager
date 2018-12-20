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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import eu.europa.ec.joinup.tsl.business.dto.audit.Audit;
import eu.europa.ec.joinup.tsl.business.dto.audit.AuditCriteriaDTO;
import eu.europa.ec.joinup.tsl.business.dto.audit.AuditSearchDTO;
import eu.europa.ec.joinup.tsl.business.repository.AuditRepository;
import eu.europa.ec.joinup.tsl.business.util.DateUtils;
import eu.europa.ec.joinup.tsl.model.DBAudit;
import eu.europa.ec.joinup.tsl.model.DBFiles;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.model.enums.AuditAction;
import eu.europa.ec.joinup.tsl.model.enums.AuditStatus;
import eu.europa.ec.joinup.tsl.model.enums.AuditTarget;

/**
 * Manage audit log
 */
@Service
@Transactional(value = TxType.REQUIRED)
public class AuditService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditService.class);

    @Autowired
    private AuditRepository auditRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private TLService tlService;

    @Autowired
    private CountryService countryService;

    @PersistenceContext
    private EntityManager em;

    /**
     * Get all audit order by date
     *
     * @return
     */
    public List<Audit> getAllAuditOrderByDateDesc() {
        List<Audit> auditList = new ArrayList<>();
        List<DBAudit> dbList = auditRepository.findAllByOrderByDateDesc();
        for (DBAudit dbAudit : dbList) {
            auditList.add(new Audit(dbAudit));
        }
        return auditList;

    }

    /**
     * Add new audit entry in database
     *
     * @param target
     * @param action
     * @param status
     * @param cc
     * @param fileId
     * @param username
     * @param infos
     * @return
     */
    public Audit addAuditLog(AuditTarget target, AuditAction action, AuditStatus status, String cc, int fileId, String username, String infos) {
        LOGGER.debug("addAuditLog for " + target);
        DBAudit dbAudit = new DBAudit();

        dbAudit.setTarget(target);
        dbAudit.setAction(action);
        dbAudit.setStatus(status);
        dbAudit.setDate(new Date());
        dbAudit.setCountryCode(cc);
        dbAudit.setUsername(username);
        dbAudit.setInfos(infos);

        if (fileId > 0) {
            dbAudit.setFileId(fileId);
            DBFiles file = fileService.getFileById(fileId);
            if (file != null) {
                dbAudit.setFileDigest(file.getDigest());
            }
        }

        Audit audit = new Audit(auditRepository.save(dbAudit));
        LOGGER.debug("** audit id is " + audit.getId());
        return audit;
    }

    /**
     * Get trusted list ID by fileId of audit log
     *
     * @param fileId
     * @return
     */
    public Integer findTlIDByAuditXmlFileId(int fileId) {
        DBTrustedLists dbTL = tlService.findByXmlFileId(fileId);
        if (dbTL != null) {
            return dbTL.getId();
        }
        return null;
    }

    /**
     * Get audit by criteria (all fields are optionals)
     *
     * <pre>
     * AuditSearchDTO :
     * - Country Code
     * - Target
     * - Action
     * - Date
     * - MaxResult
     * </pre>
     *
     * @param auditSearch
     * @return search result
     */
    public List<Audit> searchAuditByCriteria(AuditSearchDTO auditSearch) {
        List<Audit> auditList = new ArrayList<>();
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<DBAudit> criteriaQuery = criteriaBuilder.createQuery(DBAudit.class);
        Root<DBAudit> criteriaRoot = criteriaQuery.from(DBAudit.class);
        List<Predicate> predicates = new ArrayList<>();
        if (auditSearch != null) {
            if (!StringUtils.isEmpty(auditSearch.getCountryCode())) {
                predicates.add(criteriaBuilder.equal(criteriaRoot.get("countryCode"), auditSearch.getCountryCode()));
            }
            if (auditSearch.getAction() != null) {
                predicates.add(criteriaBuilder.equal(criteriaRoot.get("action"), auditSearch.getAction()));
            }
            if (auditSearch.getTarget() != null) {
                predicates.add(criteriaBuilder.equal(criteriaRoot.get("target"), auditSearch.getTarget()));
            }
            if (auditSearch.getStartDate() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(criteriaRoot.<Date> get("date"), DateUtils.getStartOfDay(auditSearch.getStartDate())));
            }
            if (auditSearch.getEndDate() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(criteriaRoot.<Date> get("date"), DateUtils.getEndOfDay(auditSearch.getEndDate())));
            }
        }
        criteriaQuery.select(criteriaRoot).where(predicates.toArray(new Predicate[] {})).orderBy(criteriaBuilder.desc(criteriaRoot.get("date")));
        List<DBAudit> entity = new ArrayList<>();
        if (auditSearch.getMaxResult() > 0) {
            entity = em.createQuery(criteriaQuery).setMaxResults(auditSearch.getMaxResult()).getResultList();
        } else {
            entity = em.createQuery(criteriaQuery).getResultList();
        }

        for (DBAudit dbAudit : entity) {
            auditList.add(new Audit(dbAudit));
        }
        return auditList;
    }

    /**
     * Init criterias list that can be used for criteria search
     *
     * @return
     */
    public AuditCriteriaDTO initCriteria() {
        return new AuditCriteriaDTO(countryService.getAllCountryCode());
    }

}
