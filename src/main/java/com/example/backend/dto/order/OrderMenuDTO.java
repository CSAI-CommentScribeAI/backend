package com.example.backend.dto.order;

import lombok.*;


@Setter
@Getter
@Data
@AllArgsConstructor
@Builder
public class OrderMenuDTO {
    private Long menuId;
    private String menuName;
    private String imageUrl;
    private int quantity;
}
