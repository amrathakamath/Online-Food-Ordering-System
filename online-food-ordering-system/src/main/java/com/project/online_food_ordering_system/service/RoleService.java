package com.project.online_food_ordering_system.service;

import com.project.online_food_ordering_system.entity.Role;

import java.util.Optional;

public interface RoleService {
    Role saveRole(Role role);
    Optional<Role> findByName(String name); 
}
