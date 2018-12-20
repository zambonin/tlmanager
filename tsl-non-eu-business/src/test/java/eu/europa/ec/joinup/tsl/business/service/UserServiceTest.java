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

import java.util.List;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;
import eu.europa.ec.joinup.tsl.business.dto.User;
import eu.europa.ec.joinup.tsl.model.DBUser;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserServiceTest extends AbstractSpringTest {

    @Autowired
    private UserService userService;

    @Test
    public void AisManagement() {
        Assert.assertTrue(userService.isSuperAdmin("test"));
        Assert.assertFalse(userService.isSuperAdmin("adm"));
    }

    @Test
    public void BisAdmin() {
        Assert.assertTrue(userService.isManagement("adm"));
        Assert.assertFalse(userService.isManagement("psc"));
    }

    @Test
    public void FisAuthenticated() {
        Assert.assertTrue(userService.isAuthenticated("test"));
        Assert.assertTrue(userService.isAuthenticated("adm"));
        Assert.assertTrue(userService.isAuthenticated("testAth"));
        Assert.assertFalse(userService.isAuthenticated("toto"));
    }

    @Test
    public void GgetUsers() {
        List<User> dbu = userService.getUsersOrderByName();
        Assert.assertEquals(8, dbu.size());
    }
    
    @Test
    public void HgetUser() {
        List<User> dbu = userService.getUsersOrderByName();
        User user = userService.getUser("adm");
        Assert.assertNotNull(user);
        Assert.assertEquals("Admin", user.getName());
        Assert.assertNull(userService.getUser("note"));
        Assert.assertEquals(8, dbu.size());
    }

    @Test
    public void IgetDbUser() {
        DBUser dbUser = userService.getDBUser("adm");
        Assert.assertNotNull(dbUser);
        Assert.assertEquals("Admin", dbUser.getName());
        Assert.assertNull(userService.getDBUser("note"));
    }

    @Test
    public void JaddRole() {
        User user = userService.getUser("test");
        Assert.assertNotNull(user);
        Assert.assertNotNull(user.getRole());
        int nbRoles = user.getRole().size();
        userService.addRole(user.getId(), 2);

        User userBis = userService.getUser("test");
        Assert.assertEquals(nbRoles + 1, userBis.getRole().size());
    }

    @Test
    public void KremoveRole() {
        User user = userService.getUser("test");
        Assert.assertNotNull(user);
        Assert.assertNotNull(user.getRole());
        int nbRoles = user.getRole().size();
        userService.removeRole(user.getId(), user.getRole().get(0).getId());

        User userBis = userService.getUser("test");
        Assert.assertEquals(nbRoles - 1, userBis.getRole().size());
    }

    @Test
    public void MaddUser() {
        Assert.assertEquals(8, userService.getUsersOrderByName().size());
        User newUser = userService.addUser("newUser");
        Assert.assertNotNull(newUser);
        Assert.assertEquals(9, userService.getUsersOrderByName().size());
    }

    @Test
    public void NremoveUser() {
        Assert.assertEquals(9, userService.getUsersOrderByName().size());
        Assert.assertTrue(userService.deleteUser(1));
        Assert.assertFalse(userService.deleteUser(9999));
        Assert.assertEquals(8, userService.getUsersOrderByName().size());
    }

    @Test
    public void OgetCodeRole() {
        String role = userService.getCodeRole(1);
        Assert.assertEquals("SUP", role);
        role = userService.getCodeRole(9999);
        Assert.assertNull(role);
    }

    @Test
    public void PgetUserATH() {
        List<DBUser> users = userService.findAuthenticatedUser();
        Assert.assertNotNull(users);
        Assert.assertEquals(6, users.size());
    }
}
