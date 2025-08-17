package com.project.online_food_ordering_system.controller;

import com.project.online_food_ordering_system.entity.MenuItem;
import com.project.online_food_ordering_system.service.MenuItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuController {

    private static final Logger logger = LoggerFactory.getLogger(MenuController.class);

    private final MenuItemService menuItemService;

    public MenuController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<MenuItem> addMenuItem(@RequestBody MenuItem menuItem) {
        logger.info("Adding new menu item: {}", menuItem.getName());
        return ResponseEntity.ok(menuItemService.saveMenuItem(menuItem));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<MenuItem> updateMenuItem(@PathVariable Long id, @RequestBody MenuItem menuItem) {
        logger.info("Updating menu item with id: {}", id);
        return ResponseEntity.ok(menuItemService.updateMenuItem(id, menuItem));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMenuItem(@PathVariable Long id) {
        logger.info("Deleting menu item with id: {}", id);
        menuItemService.deleteMenuItem(id);
        return ResponseEntity.ok("Menu item deleted");
    }

    @GetMapping
    public ResponseEntity<List<MenuItem>> getAllMenuItems() {
        logger.info("Fetching all menu items");
        return ResponseEntity.ok(menuItemService.getAllMenuItems());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuItem> getMenuItemById(@PathVariable Long id) {
        logger.info("Fetching menu item with id: {}", id);
        return ResponseEntity.ok(menuItemService.getMenuItemById(id));
    }
}
