package com.project.online_food_ordering_system.service;

import com.project.online_food_ordering_system.entity.Order;
import com.project.online_food_ordering_system.entity.OrderStatus;
import com.project.online_food_ordering_system.entity.Payment;
import com.project.online_food_ordering_system.entity.PaymentStatus;
import com.project.online_food_ordering_system.repository.OrderRepository;
import com.project.online_food_ordering_system.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public PaymentService(PaymentRepository paymentRepository, OrderRepository orderRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Payment processPayment(Long orderId, String paymentMode) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Payment payment = new Payment();
        payment.setOrderId(order.getId());
        payment.setAmount(order.getTotalAmount());
        payment.setPaymentMode(paymentMode);

        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setTransactionId("TXN-" + java.util.UUID.randomUUID()); 

        paymentRepository.save(payment);

        order.setStatus(OrderStatus.CONFIRMED);
        orderRepository.save(order);

        return payment;
    }

}
