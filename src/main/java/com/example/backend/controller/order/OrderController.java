//package com.example.backend.controller.order;
//
//import com.example.backend.dto.order.OrderDTO;
//import com.example.backend.service.LocationService;
//import com.example.backend.service.order.OrderService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/cart/orders")
//@RequiredArgsConstructor
//public class OrderController {
//
//    private final OrderService orderService;
//
//    private final LocationService locationService; // LocationService 추가
//
//    @PostMapping("/")
//    public ResponseEntity<?> createOrderFromCart(@RequestBody Long userAddressId, Authentication authentication) {
//        // 주문을 생성하기 전에 사용자의 위치와 상점 위치를 받아 거리를 계산
//        double userLat = 0.0; // 예시 값
//        double userLon = 0.0;
//        double storeLat = 0.0;
//        double storeLon = 0.0;
//
//        // 위치 서비스를 사용해 배달 가능 거리인지 확인
//        boolean isWithinRange = locationService.isWithinDeliveryRange(storeLat, storeLon, userLat, userLon, 3.0);
//        if (!isWithinRange) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Delivery range exceeded.");
//        }
//
//        // 모든 체크를 통과하면 주문 객체 생성
//        OrderDTO orderDTO = orderService.createOrderFromCart(authentication, userAddressId);
//        if (orderDTO != null) {
//            return ResponseEntity.ok(orderDTO);
//        } else {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating order.");
//        }
//    }
//
//
//    @PostMapping("/place")
//    public ResponseEntity<Void> placeOrder(@RequestBody Long orderId) {
//        try {
//            // 주문 서비스를 호출하여 주문 확정
//            orderService.placeOrder(orderId);
//            return ResponseEntity.ok().build();
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }
//
//}
