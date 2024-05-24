package com.example.backend.dto.cart;

import lombok.*;

@Data
@NoArgsConstructor
public class CartItemDTO {
    private Long menuId;
    private String menuName;
    private String imageUrl;
    private int quantity;
    private int price;


    public CartItemDTO(Long menuId, String menuName, String imageUrl, int quantity, int price) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
        this.price = price;
    }
}
