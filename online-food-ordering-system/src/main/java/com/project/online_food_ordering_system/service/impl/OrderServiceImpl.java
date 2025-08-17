package com.project.online_food_ordering_system.service.impl;

import com.project.online_food_ordering_system.entity.Cart;
import com.project.online_food_ordering_system.entity.Order;
import com.project.online_food_ordering_system.entity.OrderItem;
import com.project.online_food_ordering_system.entity.OrderStatus;
import com.project.online_food_ordering_system.entity.Payment;
import com.project.online_food_ordering_system.repository.CartRepository;
import com.project.online_food_ordering_system.repository.OrderRepository;
import com.project.online_food_ordering_system.service.OrderService;
import com.project.online_food_ordering_system.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private PaymentService paymentService; 

    @Override
    public Order placeOrder(Long userId, String paymentMode, String deliveryAddress) {
        List<Cart> cartItems = cartRepository.findByUserId(userId);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty, cannot checkout");
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.CONFIRMED); // confirmed since payment will be processed
        order.setPaymentMode(paymentMode);
        order.setDeliveryAddress(deliveryAddress);

        double total = 0;

        for (Cart cart : cartItems) {
            OrderItem item = new OrderItem();
            item.setMenuId(cart.getMenuItem().getId());
            item.setItemName(cart.getMenuItem().getName());
            item.setQuantity(cart.getQuantity());
            item.setPrice(cart.getMenuItem().getPrice());

            total += cart.getMenuItem().getPrice() * cart.getQuantity();
            order.addOrderItem(item);
        }

        order.setTotalAmount(total);

        Order savedOrder = orderRepository.save(order);

        Payment payment = paymentService.processPayment(savedOrder.getId(), paymentMode);

        cartRepository.deleteAll(cartItems);

        return savedOrder;
    }

    @Override
    public List<Order> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Override
    public Order updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(status);
        return orderRepository.save(order);
    }
}
