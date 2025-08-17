package com.project.online_food_ordering_system.controller;

import com.project.online_food_ordering_system.security.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(SecurityConfig.class)   
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testPayForOrder() throws Exception {
        mockMvc.perform(post("/api/payments/1")
                .param("paymentMode", "CASH"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetPaymentById() throws Exception {
        mockMvc.perform(get("/api/payments/1"))
                .andExpect(status().isOk());
    }
}
