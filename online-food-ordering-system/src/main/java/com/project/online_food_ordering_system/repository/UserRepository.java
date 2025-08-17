package com.project.online_food_ordering_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.online_food_ordering_system.entity.User;

public interface UserRepository extends JpaRepository<User,Long>{
	User findByUsername(String username);
    User findByEmail(String email);
}
