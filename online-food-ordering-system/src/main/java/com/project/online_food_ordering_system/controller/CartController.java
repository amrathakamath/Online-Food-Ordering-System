package com.project.online_food_ordering_system.controller;

import com.project.online_food_ordering_system.dto.CartItemDTO;
import com.project.online_food_ordering_system.entity.Cart;
import com.project.online_food_ordering_system.entity.Order;
import com.project.online_food_ordering_system.service.CartService;
import com.project.online_food_ordering_system.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @PostMapping
    public Cart addCartItem(@RequestBody Cart cart) {
        logger.info("Adding item to cart for userId: {}", cart.getUserId());
        return cartService.addToCart(cart);
    }

    @GetMapping("/{userId}")
    public List<Cart> getCartItems(@PathVariable Long userId) {
        logger.info("Fetching cart items for userId: {}", userId);
        return cartService.getCartByUser(userId);
    }

    @GetMapping("/details/{userId}")
    public List<CartItemDTO> getCartDetails(@PathVariable Long userId) {
        logger.info("Fetching cart details for userId: {}", userId);
        return cartService.getCartDetailsByUser(userId);
    }

    @DeleteMapping("/{id}")
    public void deleteCartItem(@PathVariable Long id) {
        logger.info("Deleting cart item with id: {}", id);
        cartService.removeCartItem(id);
    }

    @DeleteMapping("/clear/{userId}")
    public void clearCart(@PathVariable Long userId) {
        logger.info("Clearing cart for userId: {}", userId);
        cartService.clearCartByUser(userId);
    }

    @PostMapping("/checkout")
    public ResponseEntity<Order> checkout(
            @RequestParam Long userId,
            @RequestParam String paymentMode,
            @RequestParam String deliveryAddress) {

        logger.info("Checkout started for userId: {} with paymentMode: {} and deliveryAddress: {}", 
                    userId, paymentMode, deliveryAddress);

        Order order = orderService.placeOrder(userId, paymentMode, deliveryAddress);

        logger.info("Order placed successfully with orderId: {}", order.getId());
        return ResponseEntity.ok(order);
    }
}
