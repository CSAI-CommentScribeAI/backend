package com.example.backend.service.cart;

import com.example.backend.dto.cart.CartDTO;
import com.example.backend.dto.cart.CartItemDTO;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public abstract class CartService {
    public abstract CartDTO getCartByUserId(Authentication authentication);

    @Transactional
    public abstract CartDTO viewCart(Authentication authentication);

    @Transactional
    public abstract CartDTO addToCart(Authentication authentication, CartItemDTO cartItemDTO);
    @Transactional
    public abstract void removeFromCart(Authentication authentication, Long menuId);
    @Transactional
    public abstract void clearCart(Authentication authentication);
}
