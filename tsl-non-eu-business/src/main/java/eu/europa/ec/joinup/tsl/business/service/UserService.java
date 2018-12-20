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
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import eu.europa.ec.joinup.tsl.business.dto.User;
import eu.europa.ec.joinup.tsl.business.repository.RoleRepository;
import eu.europa.ec.joinup.tsl.business.repository.UserRepository;
import eu.europa.ec.joinup.tsl.model.DBRole;
import eu.europa.ec.joinup.tsl.model.DBUser;

/**
 * User management service
 */
@Service
@Transactional(value = TxType.REQUIRED)
public class UserService {

    private static Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private static final String superAdmin = "SUP";
    private static final String admin = "MAN";
    private static final String authenticated = "ATH";

    private static final List<String> superAdminRoles = Collections.singletonList(superAdmin);
    private static final List<String> managementRoles = Arrays.asList(superAdmin, admin);
    private static final List<String> authenticatedRoles = Arrays.asList(superAdmin, admin, authenticated);

    private static final String addRole = "addRole";
    private static final String removeRole = "removeRole";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    /*----- RIGHTS -----*/

    public boolean isManagement(String ecasId) {
        return userRepository.findByEcasIdAndRoleCodeIn(ecasId, managementRoles) != null;
    }

    public boolean isSuperAdmin(String ecasId) {
        return userRepository.findByEcasIdAndRoleCodeIn(ecasId, superAdminRoles) != null;
    }

    public boolean isAuthenticated(String ecasId) {
        return userRepository.findByEcasIdAndRoleCodeIn(ecasId, authenticatedRoles) != null;
    }

    /*----- MANAGEMENT -----*/

    /**
     * Get user order by user name
     */
    public List<User> getUsersOrderByName() {
        List<User> list = new ArrayList<>();
        List<DBUser> usrList = userRepository.findAllByOrderByNameAsc();
        usrList.sort(new Comparator<DBUser>() {

            @Override
            public int compare(DBUser u1, DBUser u2) {
                return u1.getEcasId().compareTo(u2.getEcasId());
            }
        });

        for (DBUser dbUser : usrList) {
            list.add(new User(dbUser));
        }
        return list;
    }

    public User getUser(String ecasId) {
        DBUser dbUser = getDBUser(ecasId);
        if (dbUser == null) {
            LOGGER.error("Get user null " + ecasId);
            return null;
        }
        return new User(dbUser);
    }

    /**
     * Check if user with given ecas id already exist in DB
     *
     * @param ecasId
     * @return true if user find
     */
    public boolean userExist(String ecasId) {
        return getDBUser(ecasId) != null;
    }

    public DBUser getDBUser(String ecasId) {
        if (StringUtils.isEmpty(ecasId)) {
            LOGGER.error("EcasId is null ot empty");
            return null;
        }
        return userRepository.findByEcasId(ecasId);
    }

    public String getDbUserName(int id) {
        DBUser user = userRepository.findOne(id);
        return user.getEcasId();
    }

    /**
     * Update : add role;
     *
     * @param userId
     * @param roleId
     * @return action success result
     */
    public boolean addRole(int userId, int roleId) {
        return updateRole(userId, roleId, addRole);
    }

    /**
     * Update : remove role;
     *
     * @param userId
     * @param roleId
     * @return action success result
     */
    public boolean removeRole(int userId, int roleId) {
        return updateRole(userId, roleId, removeRole);
    }

    /**
     * Update role by type (Add/Remove) Error : user is null Error : role is null
     *
     * @param userId
     * @param roleId
     * @param updateType
     * @return action success result
     */
    private boolean updateRole(int userId, int roleId, String updateType) {
        DBUser user = userRepository.findOne(userId);
        if (user == null) {
            LOGGER.error("Update role, user is null :" + userId);
            return false;
        }
        DBRole role = roleRepository.findOne(roleId);
        if (role == null) {
            LOGGER.error("Update role, role is null : " + roleId);
            return false;
        }

        if (updateType.equals(addRole)) {
            user.getRole().add(role);
        } else {
            user.getRole().remove(role);
        }
        return true;
    }

    public User addUser(String ecasName) {
        if (StringUtils.isEmpty(ecasName)) {
            LOGGER.error("Add User, Ecas name to add is not a valid string input");
            return null;
        }
        DBUser dbUser = new DBUser();
        dbUser.setEcasId(ecasName);
        try {
            userRepository.save(dbUser);
            return new User(dbUser);
        } catch (Exception e) {
            LOGGER.error("Ecas id already exist", e);
            return null;
        }
    }

    public boolean deleteUser(int id) {
        DBUser dbUser = userRepository.findOne(id);
        if (dbUser == null) {
            return false;
        }
        dbUser.getRole().removeAll(dbUser.getRole());
        userRepository.delete(dbUser);
        return true;
    }

    public String getCodeRole(int roleId) {
        DBRole role = roleRepository.findOne(roleId);
        if (role == null) {
            LOGGER.error("Get code, role is null : " + roleId);
            return null;
        }
        return role.getCode();
    }

    /**
     * Find authenticated user;
     *
     * @return list of user with an authenticated role
     */
    public List<DBUser> findAuthenticatedUser() {
        return userRepository.findByRoleCode(authenticated);
    }
}
