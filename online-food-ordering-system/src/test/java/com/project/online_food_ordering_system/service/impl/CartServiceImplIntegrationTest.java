package com.project.online_food_ordering_system.service.impl;

import com.project.online_food_ordering_system.entity.Cart;
import com.project.online_food_ordering_system.entity.MenuItem;
import com.project.online_food_ordering_system.repository.CartRepository;
import com.project.online_food_ordering_system.repository.MenuItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CartServiceImplIntegrationTest {

    @Autowired
    private CartServiceImpl cartService;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Test
    void testAddToCart_NewItem() {
        MenuItem menuItem = new MenuItem();
        menuItem.setName("Burger");
        menuItem.setPrice(120.0);
        menuItem = menuItemRepository.save(menuItem);

        Cart cart = new Cart();
        cart.setUserId(1L);
        cart.setMenuItem(menuItem);
        cart.setQuantity(2);

        Cart savedCart = cartService.addToCart(cart);

        assertNotNull(savedCart.getId());
        assertEquals(2, savedCart.getQuantity());
        assertEquals("Burger", savedCart.getMenuItem().getName());
    }
}
