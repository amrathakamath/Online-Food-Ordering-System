package com.project.online_food_ordering_system.service.impl;

import com.project.online_food_ordering_system.entity.*;
import com.project.online_food_ordering_system.repository.CartRepository;
import com.project.online_food_ordering_system.repository.OrderRepository;
import com.project.online_food_ordering_system.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Cart cart;
    private MenuItem menuItem;
    private Order order;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        menuItem = new MenuItem();
        menuItem.setId(1L);
        menuItem.setName("Pizza");
        menuItem.setPrice(200.0);

        cart = new Cart();
        cart.setId(1L);
        cart.setUserId(101L);
        cart.setMenuItem(menuItem);
        cart.setQuantity(2);

        order = new Order();
        order.setId(1L);
        order.setUserId(101L);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.CONFIRMED);
        order.setTotalAmount(400.0);
    }

    @Test
    void testPlaceOrder_Success() {
        when(cartRepository.findByUserId(101L)).thenReturn(Arrays.asList(cart));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(paymentService.processPayment(anyLong(), anyString())).thenReturn(new Payment());

        Order placedOrder = orderService.placeOrder(101L, "CARD", "Bangalore");

        assertNotNull(placedOrder);
        assertEquals(101L, placedOrder.getUserId());
        assertEquals(400.0, placedOrder.getTotalAmount());
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(paymentService, times(1)).processPayment(anyLong(), eq("CARD"));
        verify(cartRepository, times(1)).deleteAll(anyList());
    }

    @Test
    void testPlaceOrder_EmptyCart() {
        when(cartRepository.findByUserId(101L)).thenReturn(Collections.emptyList());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> orderService.placeOrder(101L, "CARD", "Bangalore"));

        assertEquals("Cart is empty, cannot checkout", exception.getMessage());
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void testGetOrdersByUser() {
        when(orderRepository.findByUserId(101L)).thenReturn(Arrays.asList(order));

        var orders = orderService.getOrdersByUser(101L);

        assertEquals(1, orders.size());
        assertEquals(101L, orders.get(0).getUserId());
    }

    @Test
    void testGetOrderById_Found() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Order foundOrder = orderService.getOrderById(1L);

        assertEquals(1L, foundOrder.getId());
        assertEquals(OrderStatus.CONFIRMED, foundOrder.getStatus());
    }

    @Test
    void testGetOrderById_NotFound() {
        when(orderRepository.findById(2L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> orderService.getOrderById(2L));

        assertEquals("Order not found", exception.getMessage());
    }

    @Test
    void testUpdateOrderStatus() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order updated = orderService.updateOrderStatus(1L, OrderStatus.DELIVERED);

        assertEquals(OrderStatus.DELIVERED, updated.getStatus());
        verify(orderRepository, times(1)).save(any(Order.class));
    }
}
