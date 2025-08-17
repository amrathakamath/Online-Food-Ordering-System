package com.project.online_food_ordering_system.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.online_food_ordering_system.entity.Role;

public interface RoleRepository extends JpaRepository<Role,Long> {
	Optional<Role> findByName(String name);
}
