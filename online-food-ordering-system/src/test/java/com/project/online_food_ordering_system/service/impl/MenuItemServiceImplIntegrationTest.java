package com.project.online_food_ordering_system.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.project.online_food_ordering_system.entity.MenuItem;
import com.project.online_food_ordering_system.service.MenuItemService;

@SpringBootTest
@Transactional  
class MenuItemServiceImplIntegrationTest {

    @Autowired
    private MenuItemService menuItemService;

    @Test
    void testSaveAndFindMenuItem() {
        MenuItem menuItem = new MenuItem();
        menuItem.setName("Pasta");
        menuItem.setDescription("White Sauce Pasta");
        menuItem.setPrice(250.0);
        menuItem.setImageUrl("pasta.jpg");
        menuItem.setAvailable(true);

        MenuItem saved = menuItemService.saveMenuItem(menuItem);

        MenuItem found = menuItemService.getMenuItemById(saved.getId());

        assertNotNull(found.getId());
        assertEquals("Pasta", found.getName());
        assertEquals(250.0, found.getPrice());
    }
}
