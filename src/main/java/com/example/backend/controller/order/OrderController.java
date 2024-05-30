package com.example.backend.controller.order;

import com.example.backend.dto.order.OrderDTO;
import com.example.backend.service.LocationService;
import com.example.backend.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cart/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final LocationService locationService;

    @PostMapping("/")
    public ResponseEntity<OrderDTO> createOrderFromCart(@RequestBody OrderDTO orderDTO, Authentication authentication) {
        OrderDTO orderSaveDTO = orderService.createOrderFromCart(authentication, orderDTO);
        if (orderSaveDTO != null) {
            return ResponseEntity.ok(orderSaveDTO);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/place/{orderId}")
    public ResponseEntity<Void> placeOrder(@PathVariable Long orderId, @RequestParam boolean approve) {
        try {
            orderService.placeOrder(orderId, approve);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
