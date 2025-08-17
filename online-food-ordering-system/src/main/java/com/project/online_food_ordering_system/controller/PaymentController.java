package com.project.online_food_ordering_system.controller;

import com.project.online_food_ordering_system.entity.Payment;
import com.project.online_food_ordering_system.repository.PaymentRepository;
import com.project.online_food_ordering_system.service.PaymentService;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    private final PaymentService paymentService;
    private final PaymentRepository paymentRepository;

    public PaymentController(PaymentService paymentService, PaymentRepository paymentRepository) {
        this.paymentService = paymentService;
        this.paymentRepository = paymentRepository;
    }

    @PostMapping("/{orderId}")
    public ResponseEntity<Payment> payForOrder(@PathVariable Long orderId,
                                               @RequestParam String paymentMode) {
        logger.info("Processing payment for orderId: {} with paymentMode: {}", orderId, paymentMode);
        Payment payment = paymentService.processPayment(orderId, paymentMode);
        logger.info("Payment processed successfully with paymentId: {}", payment.getId());
        return ResponseEntity.ok(payment);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<Payment>> getPaymentsForOrder(@PathVariable Long orderId) {
        logger.info("Fetching payments for orderId: {}", orderId);
        List<Payment> payments = paymentRepository.findByOrderId(orderId);
        if (payments.isEmpty()) {
            logger.warn("No payments found for orderId: {}", orderId);
        } else {
            logger.info("Found {} payment(s) for orderId: {}", payments.size(), orderId);
        }
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Long id) {
        logger.info("Fetching payment with id: {}", id);
        return paymentRepository.findById(id)
                .map(payment -> {
                    logger.info("Payment found with id: {}", id);
                    return ResponseEntity.ok(payment);
                })
                .orElseGet(() -> {
                    logger.warn("Payment not found with id: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }
}
