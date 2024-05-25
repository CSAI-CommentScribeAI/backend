package com.example.backend.service.order;

import com.example.backend.dto.order.OrderDTO;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;


public interface OrderService {
    OrderDTO createOrderFromCart(Authentication authentication, OrderDTO orderDTO);

    @Transactional
    void placeOrder(Long orderId, boolean approve);

}
