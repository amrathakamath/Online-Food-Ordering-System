package com.project.online_food_ordering_system.controller;

import com.project.online_food_ordering_system.entity.Order;
import com.project.online_food_ordering_system.entity.OrderStatus;
import com.project.online_food_ordering_system.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest 
@AutoConfigureMockMvc(addFilters = false) 
class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderRepository orderRepository;

    private Long orderId;

    @BeforeEach
    void setup() {
        orderRepository.deleteAll();

        Order order = new Order();
        order.setUserId(101L);
        order.setDeliveryAddress("Bangalore");
        order.setPaymentMode("CARD");
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(250.0);

        order = orderRepository.save(order);
        orderId = order.getId();
    }

    @Test
    void testGetOrderDetails() throws Exception {
        mockMvc.perform(get("/orders/" + orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderId))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void testUpdateOrderStatus() throws Exception {
        mockMvc.perform(put("/orders/" + orderId + "/status")
                        .param("status", "DELIVERED")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DELIVERED"));

        Optional<Order> updated = orderRepository.findById(orderId);
        assertThat(updated).isPresent();
        assertThat(updated.get().getStatus()).isEqualTo(OrderStatus.DELIVERED);
    }

    @Test
    void testGetOrdersByUser() throws Exception {
        mockMvc.perform(get("/orders/user/101"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(101))
                .andExpect(jsonPath("$[0].status").value("PENDING"));
    }
}
