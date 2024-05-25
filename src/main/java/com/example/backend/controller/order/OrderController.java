package com.example.backend.controller.order;

import com.example.backend.dto.order.OrderDTO;
import com.example.backend.service.LocationService;
import com.example.backend.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.jaxb.SpringDataJaxb.OrderDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cart/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    private final LocationService locationService; // LocationService 추가

    @PostMapping("/")
    public ResponseEntity<?> createOrderFromCart(@RequestBody OrderDTO orderDTO, Authentication authentication) {
        // 모든 체크를 통과하면 주문 객체 생성
         OrderDTO orderSaveDTO = orderService.createOrderFromCart(authentication, orderDTO );
        if (orderSaveDTO != null) {
            return ResponseEntity.ok(orderDTO);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating order.");
        }
    }


    @PostMapping("/place")
    public ResponseEntity<Void> placeOrder(@RequestBody Long orderId) {
        try {
            // 주문 서비스를 호출하여 주문 확정
            orderService.placeOrder(orderId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
