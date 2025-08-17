package com.project.online_food_ordering_system.controller;

import com.project.online_food_ordering_system.dto.CartItemDTO;
import com.project.online_food_ordering_system.entity.Cart;
import com.project.online_food_ordering_system.entity.Order;
import com.project.online_food_ordering_system.service.CartService;
import com.project.online_food_ordering_system.service.OrderService;
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

@WebMvcTest(CartController.class)
@AutoConfigureMockMvc(addFilters = false)
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @MockBean
    private OrderService orderService;

    @Test
    void testAddCartItem() throws Exception {
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUserId(101L);

        when(cartService.addToCart(any(Cart.class))).thenReturn(cart);

        mockMvc.perform(post("/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"userId\":101}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testGetCartItems() throws Exception {
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUserId(101L);

        when(cartService.getCartByUser(101L)).thenReturn(List.of(cart));

        mockMvc.perform(get("/cart/101"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void testGetCartDetails() throws Exception {
        CartItemDTO dto = new CartItemDTO(1L, 10L, "Pizza", 200.0, 2);

        when(cartService.getCartDetailsByUser(101L)).thenReturn(List.of(dto));

        mockMvc.perform(get("/cart/details/101"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].menuName").value("Pizza"));
    }

    @Test
    void testDeleteCartItem() throws Exception {
        doNothing().when(cartService).removeCartItem(1L);

        mockMvc.perform(delete("/cart/1"))
                .andExpect(status().isOk());

        verify(cartService, times(1)).removeCartItem(1L);
    }

    @Test
    void testClearCart() throws Exception {
        doNothing().when(cartService).clearCartByUser(101L);

        mockMvc.perform(delete("/cart/clear/101"))
                .andExpect(status().isOk());

        verify(cartService, times(1)).clearCartByUser(101L);
    }

    @Test
    void testCheckout() throws Exception {
        Order order = new Order();
        order.setId(999L);

        when(orderService.placeOrder(101L, "CARD", "Bangalore")).thenReturn(order);

        mockMvc.perform(post("/cart/checkout")
                        .param("userId", "101")
                        .param("paymentMode", "CARD")
                        .param("deliveryAddress", "Bangalore"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(999));
    }
}
