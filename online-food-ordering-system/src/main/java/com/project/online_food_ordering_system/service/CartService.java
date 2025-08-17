package com.project.online_food_ordering_system.service;

import com.project.online_food_ordering_system.dto.CartItemDTO;
import com.project.online_food_ordering_system.entity.Cart;

import java.util.List;

public interface CartService {
    Cart addToCart(Cart cart);
    List<Cart> getCartByUser(Long userId);
    void removeCartItem(Long id);

    List<CartItemDTO> getCartDetailsByUser(Long userId);
    void clearCartByUser(Long userId);
    Cart getCartById(Long id);
}
