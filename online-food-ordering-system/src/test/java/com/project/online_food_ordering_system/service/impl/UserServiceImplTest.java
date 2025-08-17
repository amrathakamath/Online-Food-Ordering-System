package com.project.online_food_ordering_system.service.impl;

import com.project.online_food_ordering_system.entity.Role;
import com.project.online_food_ordering_system.entity.User;
import com.project.online_food_ordering_system.repository.UserRepository;
import com.project.online_food_ordering_system.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleService roleService;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveUser_WithDefaultRole() {
        User user = new User();
        user.setUsername("john");
        user.setPassword("plain123");

        when(passwordEncoder.encode("plain123")).thenReturn("encoded123");
        when(roleService.findByName("ROLE_USER")).thenReturn(Optional.empty());
        when(roleService.saveRole(any(Role.class))).thenReturn(new Role("ROLE_USER"));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User savedUser = userService.saveUser(user);

        assertEquals("encoded123", savedUser.getPassword());
        assertNotNull(savedUser.getRoles());
        assertTrue(savedUser.getRoles().stream().anyMatch(r -> r.getName().equals("ROLE_USER")));

        verify(passwordEncoder, times(1)).encode("plain123");
        verify(roleService, times(1)).saveRole(any(Role.class));
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testSaveUser_WithExistingRole() {
        User user = new User();
        user.setUsername("alice");
        user.setPassword("pass");
        user.setRoles(Set.of(new Role("ROLE_ADMIN")));

        when(passwordEncoder.encode("pass")).thenReturn("encodedPass");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User savedUser = userService.saveUser(user);

        assertEquals("encodedPass", savedUser.getPassword());
        assertTrue(savedUser.getRoles().stream().anyMatch(r -> r.getName().equals("ROLE_ADMIN")));

        verify(roleService, never()).saveRole(any(Role.class));
    }

    @Test
    void testFindByUsername() {
        User user = new User();
        user.setUsername("bob");

        when(userRepository.findByUsername("bob")).thenReturn(user);

        User found = userService.findByUsername("bob");

        assertNotNull(found);
        assertEquals("bob", found.getUsername());
        verify(userRepository, times(1)).findByUsername("bob");
    }

    @Test
    void testFindByEmail() {
        User user = new User();
        user.setEmail("bob@test.com");

        when(userRepository.findByEmail("bob@test.com")).thenReturn(user);

        User found = userService.findByEmail("bob@test.com");

        assertNotNull(found);
        assertEquals("bob@test.com", found.getEmail());
        verify(userRepository, times(1)).findByEmail("bob@test.com");
    }

    @Test
    void testLoadUserByUsername_Success() {
        User user = new User();
        user.setUsername("john");
        user.setPassword("encoded123");
        user.setRoles(Set.of(new Role("ROLE_USER")));

        when(userRepository.findByUsername("john")).thenReturn(user);

        UserDetails userDetails = userService.loadUserByUsername("john");

        assertEquals("john", userDetails.getUsername());
        assertEquals("encoded123", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void testLoadUserByUsername_NotFound() {
        when(userRepository.findByUsername("ghost")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("ghost"));
    }
}
