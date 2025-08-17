package com.project.online_food_ordering_system.service;

import com.project.online_food_ordering_system.entity.User;

public interface UserService {
	User saveUser(User user);
    User findByUsername(String username);
    User findByEmail(String email);
}
