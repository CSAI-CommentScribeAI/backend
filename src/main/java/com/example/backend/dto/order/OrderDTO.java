package com.example.backend.dto.order;

import com.example.backend.entity.order.Order;
import com.example.backend.entity.order.OrderStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long id;
    private OrderStatus orderStatus;
    private Long storeId;
    private int totalPrice;
    private Long userId;
    private List<OrderMenuDTO> orderMenus;
    private String userAddress;
    private LocalDateTime createdAt;


}