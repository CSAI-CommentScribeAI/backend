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
    private Long id;
    private Long orderId;
    private Long menuId;
    private String imageUrl;
    private int quantity;
    public static OrderMenuDTO mapFromOrderMenu(OrderMenu orderMenu) {
        return OrderMenuDTO.builder()
                .id(orderMenu.getId())
                .orderId(orderMenu.getId())
                .menuId(orderMenu.getMenu().getId())
                .imageUrl(orderMenu.getImageUrl())
                .quantity(orderMenu.getQuantity())
                .build();
    }
}
