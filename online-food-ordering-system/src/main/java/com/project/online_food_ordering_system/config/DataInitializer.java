package com.project.online_food_ordering_system.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.project.online_food_ordering_system.entity.Role;
import com.project.online_food_ordering_system.repository.RoleRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public DataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
        if (roleRepository.findByName("ROLE_USER").isEmpty()) {
            roleRepository.save(new Role("ROLE_USER"));
            System.out.println(" Default ROLE_USER created");
        }
    }
}
