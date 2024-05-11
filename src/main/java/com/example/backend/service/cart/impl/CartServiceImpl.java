package com.example.backend.service.cart.impl;

import com.example.backend.dto.cart.CartDTO;
import com.example.backend.dto.cart.CartItemDTO;
import com.example.backend.entity.cart.Cart;
import com.example.backend.entity.cart.CartItem;
import com.example.backend.entity.menu.Menu;
import com.example.backend.entity.userAccount.UserAccount;
import com.example.backend.repository.UserAccount.UserAccountRepository;
import com.example.backend.repository.cart.CartRepository;
import com.example.backend.repository.menu.MenuRepository;
import com.example.backend.service.cart.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CartServiceImpl extends CartService {
    private final CartRepository cartRepository;
    private final UserAccountRepository userAccountRepository;
    private final MenuRepository menuRepository;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository,
                           UserAccountRepository userAccountRepository,
                           MenuRepository menuRepository) {
        this.cartRepository = cartRepository;
        this.userAccountRepository = userAccountRepository;
        this.menuRepository = menuRepository;
    }

    @Override
    public CartDTO getCartByUserId(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        UserAccount userAccount = userAccountRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("ID에 해당하는 사용자를 찾을 수 없습니다: " + userId));

        Cart cart = cartRepository.findByUserId(userId);
        if (cart == null) {
            return null;
        }

        return convertToCartDTO(cart);
    }

    private CartDTO convertToCartDTO(Cart cart) {
        List<CartItemDTO> cartItems = new ArrayList<>();
        cart.getCartItems().forEach(item -> {
            CartItemDTO itemDTO = new CartItemDTO();
            itemDTO.setMenuId(item.getMenu().getId());
            itemDTO.setImageUrl(item.getImageUrl());
            itemDTO.setQuantity(item.getQuantity());
            cartItems.add(itemDTO);
        });

        CartDTO cartDTO = new CartDTO();
        cartDTO.setUserId(cart.getUser().getId());
        cartDTO.setCartStatus(cart.getCartStatus());
        cartDTO.setTotalPrice(cart.getTotalPrice());
        cartDTO.setStoreId(cart.getStoreId());
        cartDTO.setCartItems(cartItems);

        return cartDTO;
    }
    @Override
    @Transactional
    public List<CartItemDTO> addToCart(Authentication authentication, CartItemDTO cartItemDTO) {
        Long userId = Long.parseLong(authentication.getName());
        UserAccount user = userAccountRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다: " + userId));

        Cart cart = cartRepository.findByUserId(userId);
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cart.setCartStatus("WAITING");
            cartRepository.save(cart);
        }

        Menu menu = menuRepository.findById(cartItemDTO.getMenuId())
                .orElseThrow(() -> new IllegalArgumentException("메뉴를 찾을 수 없습니다. Menu ID: " + cartItemDTO.getMenuId()));

        CartItem cartItem = new CartItem();
        cartItem.setMenu(menu);
        cartItem.setImageUrl(menu.getImageUrl());
        cartItem.setQuantity(cartItemDTO.getQuantity());
        cartItem.setCart(cart);

        cart.getCartItems().add(cartItem);

        // 카트의 총 가격 및 가게 ID를 재계산
        cart.calculateTotalPriceAndStoreId();
        cartRepository.save(cart);
        // 갱신된 카트 아이템 목록 생성
        List<CartItemDTO> cartItemDTOs = cart.getCartItems().stream()
                .map(item -> new CartItemDTO(
                        item.getMenu().getId(),
                        item.getMenu().getName(),
                        item.getMenu().getImageUrl(),
                        item.getQuantity()
                ))
                .collect(Collectors.toList());

        return cartItemDTOs;
    }


    @Transactional
    public void removeFromCart(Authentication authentication, Long menuId) {
        Long userId = Long.parseLong(authentication.getName());
        UserAccount user = userAccountRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다: " + userId));

        Cart cart = cartRepository.findByUserId(userId);
        if (cart != null) {
            cart.getCartItems().removeIf(cartItem -> cartItem.getMenu().getId().equals(menuId));
            cart.calculateTotalPriceAndStoreId();
            cartRepository.save(cart);
        }
    }

    @Override
    @Transactional
    public void clearCart(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        UserAccount user = userAccountRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다: " + userId));

        Cart cart = cartRepository.findByUserId(userId);
        if (cart != null) {
            cart.getCartItems().clear();
            cart.calculateTotalPriceAndStoreId();
            cartRepository.save(cart);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public CartDTO viewCart(Authentication authentication) {
        return getCartByUserId(authentication);
    }
}
