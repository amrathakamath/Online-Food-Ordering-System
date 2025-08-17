package com.project.online_food_ordering_system.service.impl;

import com.project.online_food_ordering_system.entity.*;
import com.project.online_food_ordering_system.repository.CartRepository;
import com.project.online_food_ordering_system.repository.MenuItemRepository;
import com.project.online_food_ordering_system.repository.OrderRepository;
import com.project.online_food_ordering_system.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceIntegrationTest {

    @Autowired
    private OrderServiceImpl orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private PaymentService paymentService; // You may need a test stub or H2-compatible impl

    private MenuItem menuItem;
    private Cart cart;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
        cartRepository.deleteAll();
        menuItemRepository.deleteAll();

        menuItem = new MenuItem();
        menuItem.setName("Pizza");
        menuItem.setPrice(200.0);
        menuItem = menuItemRepository.save(menuItem);

        cart = new Cart();
        cart.setUserId(101L);
        cart.setMenuItem(menuItem);
        cart.setQuantity(2);
        cart = cartRepository.save(cart);
    }

    @Test
    void testPlaceOrder_Success() {
        Order order = orderService.placeOrder(101L, "CARD", "Bangalore");

        assertNotNull(order.getId());
        assertEquals(101L, order.getUserId());
        assertEquals(400.0, order.getTotalAmount());
        assertEquals(OrderStatus.CONFIRMED, order.getStatus());

        List<Cart> cartItems = cartRepository.findByUserId(101L);
        assertTrue(cartItems.isEmpty());
    }

    @Test
    void testGetOrdersByUser() {
        orderService.placeOrder(101L, "CARD", "Bangalore");

        List<Order> orders = orderService.getOrdersByUser(101L);
        assertEquals(1, orders.size());
        assertEquals(101L, orders.get(0).getUserId());
    }

    @Test
    void testGetOrderById() {
        Order savedOrder = orderService.placeOrder(101L, "CARD", "Bangalore");

        Order fetchedOrder = orderService.getOrderById(savedOrder.getId());
        assertEquals(savedOrder.getId(), fetchedOrder.getId());
        assertEquals(savedOrder.getTotalAmount(), fetchedOrder.getTotalAmount());
    }

    @Test
    void testUpdateOrderStatus() {
        Order savedOrder = orderService.placeOrder(101L, "CARD", "Bangalore");

        Order updatedOrder = orderService.updateOrderStatus(savedOrder.getId(), OrderStatus.DELIVERED);
        assertEquals(OrderStatus.DELIVERED, updatedOrder.getStatus());
    }
}
