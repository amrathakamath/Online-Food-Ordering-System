package com.project.online_food_ordering_system.service;

import com.project.online_food_ordering_system.entity.Order;
import com.project.online_food_ordering_system.entity.OrderStatus;

import java.util.List;

public interface OrderService {
    Order placeOrder(Long userId, String paymentMode, String deliveryAddress);

    List<Order> getOrdersByUser(Long userId);

    Order getOrderById(Long id);

    Order updateOrderStatus(Long orderId, OrderStatus status);
}
