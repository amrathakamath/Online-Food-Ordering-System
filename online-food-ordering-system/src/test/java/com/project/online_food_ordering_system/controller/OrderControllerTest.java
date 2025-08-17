package com.project.online_food_ordering_system.controller;

import com.project.online_food_ordering_system.entity.Order;
import com.project.online_food_ordering_system.entity.OrderStatus;
import com.project.online_food_ordering_system.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    private Order getSampleOrder(Long id, Long userId, String status) {
        Order order = new Order();
        order.setId(id);
        order.setUserId(userId);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(200.0);
        order.setStatus(OrderStatus.valueOf(status));
        return order;
    }

    @Test
    void testPlaceOrder() throws Exception {
        Order order = getSampleOrder(1L, 101L, "PENDING");

        when(orderService.placeOrder(eq(101L), eq("CARD"), eq("Bangalore")))
                .thenReturn(order);

        mockMvc.perform(post("/orders/place")
                        .param("userId", "101")
                        .param("paymentMode", "CARD")
                        .param("deliveryAddress", "Bangalore")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void testGetOrdersByUser() throws Exception {
        Order order1 = getSampleOrder(1L, 101L, "PENDING");
        Order order2 = getSampleOrder(2L, 101L, "DELIVERED");

        when(orderService.getOrdersByUser(101L)).thenReturn(List.of(order1, order2));

        mockMvc.perform(get("/orders/user/101"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].status").value("DELIVERED"));
    }

    @Test
    void testGetOrderDetailsFound() throws Exception {
        Order order = getSampleOrder(1L, 101L, "PENDING");

        when(orderService.getOrderById(1L)).thenReturn(order);

        mockMvc.perform(get("/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void testGetOrderDetailsNotFound() throws Exception {
        when(orderService.getOrderById(99L)).thenReturn(null);

        mockMvc.perform(get("/orders/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateOrderStatusSuccess() throws Exception {
        Order updatedOrder = getSampleOrder(1L, 101L, "DELIVERED");

        when(orderService.updateOrderStatus(1L, OrderStatus.DELIVERED))
                .thenReturn(updatedOrder);

        mockMvc.perform(put("/orders/1/status")
                        .param("status", "DELIVERED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DELIVERED"));
    }

    @Test
    void testUpdateOrderStatusNotFound() throws Exception {
        when(orderService.updateOrderStatus(99L, OrderStatus.CANCELLED))
                .thenReturn(null);

        mockMvc.perform(put("/orders/99/status")
                        .param("status", "CANCELLED"))
                .andExpect(status().isNotFound());
    }
}
