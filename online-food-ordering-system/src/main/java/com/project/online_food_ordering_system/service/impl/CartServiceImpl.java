package com.project.online_food_ordering_system.service.impl;

import com.project.online_food_ordering_system.dto.CartItemDTO;
import com.project.online_food_ordering_system.entity.Cart;
import com.project.online_food_ordering_system.entity.MenuItem;
import com.project.online_food_ordering_system.repository.CartRepository;
import com.project.online_food_ordering_system.repository.MenuItemRepository;
import com.project.online_food_ordering_system.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Override
    public Cart addToCart(Cart cart) {
        System.out.println(">>> Adding to cart: userId=" + cart.getUserId() 
            + ", menuItemId=" + cart.getMenuItem().getId() 
            + ", qty=" + cart.getQuantity());

        MenuItem menuItem = menuItemRepository.findById(cart.getMenuItem().getId())
                .orElseThrow(() -> new RuntimeException("Menu item not found"));

        Cart existing = cartRepository.findByUserIdAndMenuItem(cart.getUserId(), menuItem);

        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + cart.getQuantity());
            return cartRepository.save(existing);
        }

        cart.setMenuItem(menuItem);
        return cartRepository.save(cart);
    }


    @Override
    public List<Cart> getCartByUser(Long userId) {
        return cartRepository.findByUserId(userId);
    }

    @Override
    public void removeCartItem(Long id) {
        cartRepository.deleteById(id);
    }

    @Override
    public List<CartItemDTO> getCartDetailsByUser(Long userId) {
        List<Cart> carts = cartRepository.findByUserId(userId);
        List<CartItemDTO> dtoList = new ArrayList<>();

        for (Cart c : carts) {
            MenuItem menu = c.getMenuItem();
            dtoList.add(new CartItemDTO(
                    c.getId(),
                    menu.getId(),
                    menu.getName(),
                    menu.getPrice(),
                    c.getQuantity()
            ));
        }

        return dtoList;
    }

    @Override
    public void clearCartByUser(Long userId) {
        List<Cart> carts = cartRepository.findByUserId(userId);
        cartRepository.deleteAll(carts);
    }

    @Override
    public Cart getCartById(Long id) {
        return cartRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
    }
}
