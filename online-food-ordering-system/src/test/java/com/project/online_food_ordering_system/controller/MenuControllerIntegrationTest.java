package com.project.online_food_ordering_system.controller;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MenuControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = "ADMIN") 
    void testCreateReadUpdateDeleteMenuItem() throws Exception {
        MvcResult result = mockMvc.perform(post("/menu")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Pizza\",\"description\":\"Cheese Pizza\",\"price\":299.0}"))
                .andExpect(status().isOk())   
                .andExpect(jsonPath("$.name").value("Pizza"))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        Integer id = JsonPath.parse(responseBody).read("$.id", Integer.class);

        mockMvc.perform(get("/menu/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Pizza"));

        mockMvc.perform(put("/menu/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Pizza\",\"description\":\"Veg Loaded Pizza\",\"price\":350.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Veg Loaded Pizza"))
                .andExpect(jsonPath("$.price").value(350.0));

        mockMvc.perform(delete("/menu/" + id))
                .andExpect(status().isOk())   
                .andExpect(content().string("Menu item deleted"));

        mockMvc.perform(get("/menu/" + id))
                .andExpect(status().is4xxClientError());
    }
}
