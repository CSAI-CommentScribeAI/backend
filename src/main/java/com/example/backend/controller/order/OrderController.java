package com.example.backend.controller.order;

import com.example.backend.dto.order.OrderDTO;
import com.example.backend.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    @PostMapping("/")
    public ResponseEntity<OrderDTO> createOrderFromCart(@RequestBody Long userAddressId, Authentication authentication) {
        OrderDTO orderDTO = orderService.createOrderFromCart(authentication, userAddressId);
        if (orderDTO != null) {
            return ResponseEntity.ok(orderDTO);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PostMapping("/place")
    public ResponseEntity<Void> placeOrder(@RequestBody Long orderId) {
        orderService.placeOrder(orderId);
        return ResponseEntity.ok().build();
    }
}
