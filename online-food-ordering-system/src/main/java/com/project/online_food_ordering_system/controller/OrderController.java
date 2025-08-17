package com.project.online_food_ordering_system.controller;

import com.project.online_food_ordering_system.entity.Order;
import com.project.online_food_ordering_system.entity.OrderStatus;
import com.project.online_food_ordering_system.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @PostMapping("/place")
    public Order placeOrder(@RequestParam Long userId,
                            @RequestParam String paymentMode,
                            @RequestParam String deliveryAddress) {
        logger.info("Placing order for userId: {} with paymentMode: {} and deliveryAddress: {}", 
                    userId, paymentMode, deliveryAddress);
        Order order = orderService.placeOrder(userId, paymentMode, deliveryAddress);
        logger.info("Order placed successfully with orderId: {}", order.getId());
        return order;
    }

    @GetMapping("/user/{userId}")
    public List<Order> getOrdersByUser(@PathVariable Long userId) {
        logger.info("Fetching orders for userId: {}", userId);
        return orderService.getOrdersByUser(userId);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderDetails(@PathVariable Long orderId) {
        logger.info("Fetching order details for orderId: {}", orderId);
        Order order = orderService.getOrderById(orderId);
        if (order != null) {
            logger.info("Order found for orderId: {}", orderId);
            return ResponseEntity.ok(order);
        } else {
            logger.warn("Order not found for orderId: {}", orderId);
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable Long id,
                                                   @RequestParam OrderStatus status) {
        logger.info("Updating status of orderId: {} to {}", id, status);
        Order updatedOrder = orderService.updateOrderStatus(id, status);
        if (updatedOrder != null) {
            logger.info("Order status updated successfully for orderId: {}", id);
            return ResponseEntity.ok(updatedOrder);
        } else {
            logger.warn("Failed to update order status. Order not found for orderId: {}", id);
            return ResponseEntity.notFound().build();
        }
    }
}
