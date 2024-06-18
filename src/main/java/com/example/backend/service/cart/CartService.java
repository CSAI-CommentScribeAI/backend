package com.example.backend.service.cart;

import com.example.backend.dto.cart.CartDTO;
import com.example.backend.dto.menu.MenuResponseDTO;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;

public abstract class CartService {
    public abstract CartDTO getCartByUserId(Authentication authentication);
    @Transactional
    public abstract CartDTO addToCart(Authentication authentication, MenuResponseDTO menuDTO);
    public abstract void removeFromCart(Authentication authentication, Long menuId);
    public abstract void clearCart(Authentication authentication);
    public abstract CartDTO viewCart(Authentication authentication);
}
