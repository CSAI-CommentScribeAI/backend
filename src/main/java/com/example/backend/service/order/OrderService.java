package com.example.backend.service.order;

import com.example.backend.dto.order.OrderDTO;
import com.example.backend.entity.comment.Review;
import com.example.backend.entity.order.UserOrder;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;


public interface OrderService {
    OrderDTO createOrderFromCart(Authentication authentication, OrderDTO orderDTO);
    UserOrder getOrderById(Long orderId);
    List<Review> getRecentReviewsForUserAndStore(Long userId, Long storeId);
    int getOrderCountForUserAndStore(Long userId, Long storeId);

    @Transactional
    void placeOrder(Long orderId, boolean approve);

    OrderDTO deliveryOrder(Long orderId);

    List<OrderDTO> getStoreOrders(Authentication authentication,Long storeId);

    List<OrderDTO> getUserOrders(Authentication authentication);
}
