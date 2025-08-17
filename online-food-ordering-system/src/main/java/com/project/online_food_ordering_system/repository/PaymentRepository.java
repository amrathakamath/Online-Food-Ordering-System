package com.project.online_food_ordering_system.repository;

import com.project.online_food_ordering_system.entity.Payment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
	List<Payment> findByOrderId(Long orderId);
}
