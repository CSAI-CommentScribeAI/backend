package com.example.backend.dto.order;

import com.example.backend.entity.order.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long orderId;
    private OrderStatus orderStatus;
    private Long storeId;
    private String storeName;
    private String storeImageUrl;
    private int totalPrice;
    private Long userId;
    private List<OrderMenuDTO> orderMenus;
    private String userAddress;
    private LocalDateTime createdAt;


}