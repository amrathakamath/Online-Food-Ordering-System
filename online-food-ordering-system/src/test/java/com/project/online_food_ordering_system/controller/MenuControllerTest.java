package com.project.online_food_ordering_system.controller;

import com.project.online_food_ordering_system.entity.MenuItem;
import com.project.online_food_ordering_system.service.MenuItemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MenuController.class)
@AutoConfigureMockMvc(addFilters = false)
class MenuControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MenuItemService menuItemService;

    @Test
    void testAddMenuItem() throws Exception {
        MenuItem menuItem = new MenuItem();
        menuItem.setId(1L);
        menuItem.setName("Pizza");

        when(menuItemService.saveMenuItem(any(MenuItem.class))).thenReturn(menuItem);

        mockMvc.perform(post("/menu")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"name\":\"Pizza\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Pizza"));
    }

    @Test
    void testUpdateMenuItem() throws Exception {
        MenuItem updatedItem = new MenuItem();
        updatedItem.setId(1L);
        updatedItem.setName("Burger");

        when(menuItemService.updateMenuItem(eq(1L), any(MenuItem.class))).thenReturn(updatedItem);

        mockMvc.perform(put("/menu/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"name\":\"Burger\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Burger"));
    }

    @Test
    void testDeleteMenuItem() throws Exception {
        doNothing().when(menuItemService).deleteMenuItem(1L);

        mockMvc.perform(delete("/menu/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Menu item deleted"));

        verify(menuItemService, times(1)).deleteMenuItem(1L);
    }

    @Test
    void testGetAllMenuItems() throws Exception {
        MenuItem item1 = new MenuItem();
        item1.setId(1L);
        item1.setName("Pizza");

        MenuItem item2 = new MenuItem();
        item2.setId(2L);
        item2.setName("Pasta");

        when(menuItemService.getAllMenuItems()).thenReturn(List.of(item1, item2));

        mockMvc.perform(get("/menu"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Pizza"))
                .andExpect(jsonPath("$[1].name").value("Pasta"));
    }

    @Test
    void testGetMenuItemById() throws Exception {
        MenuItem item = new MenuItem();
        item.setId(1L);
        item.setName("Pizza");

        when(menuItemService.getMenuItemById(1L)).thenReturn(item);

        mockMvc.perform(get("/menu/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Pizza"));
    }
}
