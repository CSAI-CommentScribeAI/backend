package com.example.backend.dto.order;

import com.example.backend.entity.order.OrderMenu;
import lombok.*;

import java.math.BigDecimal;


@Setter
@Getter
@Data
@AllArgsConstructor
@Builder
public class OrderMenuDTO {
    private Long menuId;
    private String imageUrl;
    private int quantity;
}
