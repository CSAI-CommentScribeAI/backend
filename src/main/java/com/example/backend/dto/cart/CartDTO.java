package com.example.backend.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {
    private Long cartId;
    private Long userId;
    private String cartStatus;
    private int totalPrice;
    private List<CartItemDTO> cartItems;
    private String userAddress;
}