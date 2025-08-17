package com.project.online_food_ordering_system.service.impl;

import com.project.online_food_ordering_system.entity.MenuItem;
import com.project.online_food_ordering_system.repository.MenuItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MenuItemServiceImplTest {

    @Mock
    private MenuItemRepository menuItemRepository;

    @InjectMocks
    private MenuItemServiceImpl menuItemService;

    private MenuItem menuItem;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        menuItem = new MenuItem();
        menuItem.setId(1L);
        menuItem.setName("Pizza");
        menuItem.setDescription("Cheese Pizza");
        menuItem.setPrice(200.0);
        menuItem.setImageUrl("pizza.jpg");
        menuItem.setAvailable(true);
    }

    @Test
    void testSaveMenuItem() {
        when(menuItemRepository.save(menuItem)).thenReturn(menuItem);
        MenuItem saved = menuItemService.saveMenuItem(menuItem);
        assertEquals("Pizza", saved.getName());
        verify(menuItemRepository, times(1)).save(menuItem);
    }

    @Test
    void testUpdateMenuItem() {
        MenuItem updatedItem = new MenuItem();
        updatedItem.setName("Burger");
        updatedItem.setDescription("Veg Burger");
        updatedItem.setPrice(150.0);
        updatedItem.setImageUrl("burger.jpg");
        updatedItem.setAvailable(true);

        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(menuItem));
        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(updatedItem);

        MenuItem result = menuItemService.updateMenuItem(1L, updatedItem);

        assertEquals("Burger", result.getName());
        assertEquals(150.0, result.getPrice());
        verify(menuItemRepository, times(1)).findById(1L);
        verify(menuItemRepository, times(1)).save(any(MenuItem.class));
    }

    @Test
    void testDeleteMenuItem() {
        doNothing().when(menuItemRepository).deleteById(1L);
        menuItemService.deleteMenuItem(1L);
        verify(menuItemRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetAllMenuItems() {
        when(menuItemRepository.findAll()).thenReturn(Arrays.asList(menuItem));
        List<MenuItem> items = menuItemService.getAllMenuItems();
        assertEquals(1, items.size());
        assertEquals("Pizza", items.get(0).getName());
    }

    @Test
    void testGetMenuItemById_Found() {
        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(menuItem));
        MenuItem found = menuItemService.getMenuItemById(1L);
        assertEquals("Pizza", found.getName());
    }

    @Test
    void testGetMenuItemById_NotFound() {
        when(menuItemRepository.findById(2L)).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            menuItemService.getMenuItemById(2L);
        });
        assertEquals("Menu item not found", exception.getMessage());
    }
}
