package com.example.backend.dto.order;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDTO {
    private Long storeId;
    private int totalPrice;
    private String userAddress;
    private List<OrderMenuDTO> orderMenus;
}
