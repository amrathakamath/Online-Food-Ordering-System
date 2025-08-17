package com.project.online_food_ordering_system.service.impl;

import com.project.online_food_ordering_system.entity.Cart;
import com.project.online_food_ordering_system.entity.MenuItem;
import com.project.online_food_ordering_system.repository.CartRepository;
import com.project.online_food_ordering_system.repository.MenuItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CartServiceImplTest {

    @InjectMocks
    private CartServiceImpl cartService;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private MenuItemRepository menuItemRepository;

    private MenuItem menuItem;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        menuItem = new MenuItem();
        menuItem.setId(1L);
        menuItem.setName("Pizza");
        menuItem.setPrice(250.0);
    }

    @Test
    void testAddToCart_NewItem() {
        Cart cart = new Cart();
        cart.setUserId(1L);
        cart.setMenuItem(menuItem);
        cart.setQuantity(2);

        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(menuItem));
        when(cartRepository.findByUserIdAndMenuItem(1L, menuItem)).thenReturn(null);
        when(cartRepository.save(any(Cart.class))).thenAnswer(i -> i.getArguments()[0]);

        Cart savedCart = cartService.addToCart(cart);

        assertEquals(2, savedCart.getQuantity());
        assertEquals("Pizza", savedCart.getMenuItem().getName());

        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void testAddToCart_ExistingItem_UpdatesQuantity() {
        Cart existingCart = new Cart();
        existingCart.setUserId(1L);
        existingCart.setMenuItem(menuItem);
        existingCart.setQuantity(3);

        Cart cartToAdd = new Cart();
        cartToAdd.setUserId(1L);
        cartToAdd.setMenuItem(menuItem);
        cartToAdd.setQuantity(2);

        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(menuItem));
        when(cartRepository.findByUserIdAndMenuItem(1L, menuItem)).thenReturn(existingCart);
        when(cartRepository.save(any(Cart.class))).thenAnswer(i -> i.getArguments()[0]);

        Cart updatedCart = cartService.addToCart(cartToAdd);

        assertEquals(5, updatedCart.getQuantity());
        verify(cartRepository, times(1)).save(existingCart);
    }
}
