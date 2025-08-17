package com.project.online_food_ordering_system.service.impl;

import com.project.online_food_ordering_system.entity.Role;
import com.project.online_food_ordering_system.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class RoleServiceIntegrationTest {

    @Autowired
    private RoleServiceImpl roleService;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void testSaveRole() {
        Role role = new Role("ROLE_TEST_SAVE");
        Role saved = roleService.saveRole(role);

        assertNotNull(saved.getId());
        assertEquals("ROLE_TEST_SAVE", saved.getName());
    }

    @Test
    void testFindByName() {
        Role role = new Role("ROLE_TEST_FIND");
        roleRepository.save(role);

        Optional<Role> found = roleService.findByName("ROLE_TEST_FIND");

        assertTrue(found.isPresent());
        assertEquals("ROLE_TEST_FIND", found.get().getName());
    }

    @Test
    void testEnsureDefaultRolesExist_DoesNotDuplicate() {
        long countBefore = roleRepository.count();

        roleService.ensureDefaultRolesExist();

        long countAfter = roleRepository.count();

        assertEquals(countBefore, countAfter);
    }
}
