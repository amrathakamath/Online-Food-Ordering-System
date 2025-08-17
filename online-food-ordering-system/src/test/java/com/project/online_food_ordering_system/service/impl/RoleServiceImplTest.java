package com.project.online_food_ordering_system.service.impl;

import com.project.online_food_ordering_system.entity.Role;
import com.project.online_food_ordering_system.repository.RoleRepository;
import com.project.online_food_ordering_system.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveRole() {
        Role role = new Role("ROLE_USER");
        when(roleRepository.save(any(Role.class))).thenReturn(role);

        Role saved = roleService.saveRole(role);

        verify(roleRepository, times(1)).save(role);
    }

    @Test
    void testFindByName() {
        Role role = new Role("ROLE_ADMIN");
        when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(Optional.of(role));

        Optional<Role> found = roleService.findByName("ROLE_ADMIN");

        verify(roleRepository, times(1)).findByName("ROLE_ADMIN");
    }

    @Test
    void testEnsureDefaultRolesExist_WhenMissing() {
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.empty());
        when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(Optional.empty());

        roleService.ensureDefaultRolesExist();

        verify(roleRepository, times(1))
                .save(argThat(role -> role.getName().equals("ROLE_USER")));
        verify(roleRepository, times(1))
                .save(argThat(role -> role.getName().equals("ROLE_ADMIN")));
    }

    @Test
    void testEnsureDefaultRolesExist_WhenAlreadyExists() {
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(new Role("ROLE_USER")));
        when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(Optional.of(new Role("ROLE_ADMIN")));

        roleService.ensureDefaultRolesExist();

        verify(roleRepository, never()).save(any(Role.class));
    }
}
