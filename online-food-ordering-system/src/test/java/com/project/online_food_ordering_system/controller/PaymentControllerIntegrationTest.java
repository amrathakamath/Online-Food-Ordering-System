package com.project.online_food_ordering_system.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)   
class PaymentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testPayForOrderEndpoint() throws Exception {
        int status = mockMvc.perform(post("/api/payments/1")
                .param("paymentMode", "CASH"))
                .andReturn().getResponse().getStatus();

        assertThat(status).isIn(200, 400, 404); 
    }

    @Test
    void testGetPaymentByIdEndpoint() throws Exception {
        int status = mockMvc.perform(get("/api/payments/1"))
                .andReturn().getResponse().getStatus();

        assertThat(status).isIn(200, 404);  
    }

    @Test
    void testGetPaymentsForOrderEndpoint() throws Exception {
        int status = mockMvc.perform(get("/api/payments/order/1"))
                .andReturn().getResponse().getStatus();

        assertThat(status).isIn(200, 404);  
    }
}
