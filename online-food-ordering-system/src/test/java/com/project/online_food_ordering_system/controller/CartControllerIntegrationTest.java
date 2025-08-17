package com.project.online_food_ordering_system.controller;

import com.project.online_food_ordering_system.entity.Cart;
import com.project.online_food_ordering_system.entity.MenuItem;
import com.project.online_food_ordering_system.repository.CartRepository;
import com.project.online_food_ordering_system.repository.MenuItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CartControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @BeforeEach
    void setUp() {
        // create a menu item
        MenuItem menuItem = new MenuItem();
        menuItem.setName("Burger");
        menuItem.setPrice(150.0);
        menuItem.setAvailable(true);
        menuItemRepository.save(menuItem);

        // add to cart
        Cart cart = new Cart();
        cart.setUserId(1L);
        cart.setMenuItem(menuItem);
        cart.setQuantity(2);
        cartRepository.save(cart);
    }

    @Test
    void testGetCartItems() throws Exception {
        mockMvc.perform(get("/cart/1"))
                .andExpect(status().isOk()); 
    }
}
