package com.project.online_food_ordering_system.repository;

import com.project.online_food_ordering_system.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
}
