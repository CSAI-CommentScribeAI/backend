package com.example.backend.service.cart;

import com.example.backend.dto.cart.CartDTO;
import com.example.backend.dto.cart.CartItemDTO;
import org.springframework.security.core.Authentication;

public abstract class CartService {
    public abstract CartDTO getCartByUserId(Authentication authentication);
    public abstract CartDTO addToCart(Authentication authentication, Long menuId);
    public abstract void removeFromCart(Authentication authentication, Long menuId);
    public abstract void clearCart(Authentication authentication);
    public abstract CartDTO viewCart(Authentication authentication);
}
