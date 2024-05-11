package com.example.backend.dto.cart;

import lombok.*;

@Data
@NoArgsConstructor
public class CartItemDTO {
    private Long menuId;
    private String menuName;
    private String imageUrl;
    private int quantity;

    public CartItemDTO(Long menuId, String menuName, String imageUrl,int  quantity) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
    }
}
