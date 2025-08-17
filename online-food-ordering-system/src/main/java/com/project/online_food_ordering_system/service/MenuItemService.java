package com.project.online_food_ordering_system.service;

import com.project.online_food_ordering_system.entity.MenuItem;
import java.util.List;

public interface MenuItemService {
    MenuItem saveMenuItem(MenuItem menuItem);
    MenuItem updateMenuItem(Long id, MenuItem menuItem);
    void deleteMenuItem(Long id);
    List<MenuItem> getAllMenuItems();
    MenuItem getMenuItemById(Long id);
}
