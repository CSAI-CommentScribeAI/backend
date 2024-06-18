package com.example.backend.service.order;

import com.example.backend.dto.order.OrderRequestDTO;
import com.example.backend.dto.order.OrderResponseDTO;
import com.example.backend.entity.comment.Review;
import com.example.backend.entity.order.Order;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;


public interface OrderService {
    OrderResponseDTO createOrderFromCart(Authentication authentication, OrderRequestDTO orderRequestDTO);
    Order getOrderById(Long orderId);
    List<Review> getRecentReviewsForUserAndStore(Long userId, Long storeId);
    int getOrderCountForUserAndStore(Long userId, Long storeId);

    @Transactional
    void placeOrder(Long orderId, boolean approve);

    OrderResponseDTO deliveryOrder(Long orderId);

    List<OrderResponseDTO> getStoreOrders(Authentication authentication, Long storeId);

    List<OrderResponseDTO> getUserOrders(Authentication authentication);
}
