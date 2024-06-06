package com.example.backend.dto.cart;

import lombok.*;

@Data
@NoArgsConstructor
public class CartItemDTO {

    private Long menuId;
    private String menuName;
    private String imageUrl;
    private int price;
}
