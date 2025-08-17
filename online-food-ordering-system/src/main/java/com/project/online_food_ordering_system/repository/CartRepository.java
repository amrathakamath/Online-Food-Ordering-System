package com.project.online_food_ordering_system.repository;
import com.project.online_food_ordering_system.entity.MenuItem;

import com.project.online_food_ordering_system.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {
	Cart findByUserIdAndMenuItem(Long userId, MenuItem menuItem);
	List<Cart> findByUserId(Long userId);
}
