package com.example.backend.service.cart.impl;

import com.example.backend.dto.cart.CartDTO;
import com.example.backend.dto.cart.CartItemDTO;
import com.example.backend.entity.cart.Cart;
import com.example.backend.entity.cart.CartItem;
import com.example.backend.entity.menu.Menu;
import com.example.backend.entity.userAccount.UserAccount;
import com.example.backend.entity.userAccount.UserAddress;
import com.example.backend.repository.UserAccount.UserAccountRepository;
import com.example.backend.repository.UserAccount.UserAddressRepository;
import com.example.backend.repository.cart.CartRepository;
import com.example.backend.repository.menu.MenuRepository;
import com.example.backend.service.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CartServiceImpl extends CartService {
    private final CartRepository cartRepository;
    private final UserAccountRepository userAccountRepository;
    private final MenuRepository menuRepository;
    private final UserAddressRepository userAddressRepository;

    @Override
    public CartDTO getCartByUserId(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        UserAccount userAccount = userAccountRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("ID에 해당하는 사용자를 찾을 수 없습니다: " + userId));

        Optional<Cart> cartOptional = cartRepository.findByUserId(userId);
        if (!cartOptional.isPresent()) {
            return null; // Or handle differently if a new Cart should be created
        }

        return convertToCartDTO(cartOptional.get(), userAccount);
    }

    private CartDTO convertToCartDTO(Cart cart, UserAccount userAccount) {
        List<CartItemDTO> cartItems = new ArrayList<>();
        cart.getCartItems().forEach(item -> {
            CartItemDTO itemDTO = new CartItemDTO();
            itemDTO.setMenuId(item.getMenu().getId());
            itemDTO.setMenuName(item.getMenu().getName());
            itemDTO.setImageUrl(item.getImageUrl());
            itemDTO.setQuantity(item.getQuantity());
            itemDTO.setPrice(item.getMenu().getPrice());
            cartItems.add(itemDTO);
        });

        CartDTO cartDTO = new CartDTO();
        cartDTO.setUserId(cart.getUser().getId());
        cartDTO.setCartStatus(cart.getCartStatus());
        cartDTO.setTotalPrice(cart.getTotalPrice());
        cartDTO.setCartItems(cartItems);

        UserAddress userAddress = userAddressRepository.findById(userAccount.getId())
                .orElseThrow(() -> new IllegalStateException("사용자의 주소를 찾을 수 없습니다: " + userAccount.getId()));

        cartDTO.setUserAddress(userAddress.getFullAddress());

        return cartDTO;
    }

    @Override
    @Transactional
    public CartDTO addToCart(Authentication authentication, CartItemDTO cartItemDTO) {
        Long userId = Long.parseLong(authentication.getName());
        UserAccount user = userAccountRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다: " + userId));

        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    cartRepository.save(newCart);
                    return newCart;
                });

        Menu menu = menuRepository.findById(cartItemDTO.getMenuId())
                .orElseThrow(() -> new IllegalArgumentException("메뉴를 찾을 수 없습니다. Menu ID: " + cartItemDTO.getMenuId()));

        if (cart.getStoreId() == null || cart.getStoreId().equals(menu.getStore().getId())) {
            CartItem existingItem = findExistingItem(cart, menu.getId());
            if (existingItem != null) {
                existingItem.setQuantity(existingItem.getQuantity() + cartItemDTO.getQuantity());
            } else {
                addNewItem(cart, menu, cartItemDTO);
            }
            cartRepository.save(cart);
        } else {
            throw new IllegalStateException("장바구니는 오직 한 가게서만 담을 수 있습니다.");
        }

        return convertToCartDTO(cart, user);
    }

    private CartItem findExistingItem(Cart cart, Long menuId) {
        return cart.getCartItems().stream()
                .filter(item -> item.getMenu().getId().equals(menuId))
                .findFirst()
                .orElse(null);
    }

    private void addNewItem(Cart cart, Menu menu, CartItemDTO cartItemDTO) {
        if (cart.getStoreId() == null) {
            cart.setStoreId(menu.getStore().getId());
        }
        CartItem newCartItem = new CartItem();
        newCartItem.setMenu(menu);
        newCartItem.setImageUrl(menu.getImageUrl());
        newCartItem.setQuantity(cartItemDTO.getQuantity());
        cart.getCartItems().add(newCartItem);
    }

    @Override
    @Transactional
    public void removeFromCart(Authentication authentication, Long menuId) {
        Long userId = Long.parseLong(authentication.getName());
        UserAccount user = userAccountRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다: " + userId));

        Optional<Cart> cartOptional = cartRepository.findByUserId(userId);
        if (cartOptional.isPresent()) {
            Cart cart = cartOptional.get();
            cart.getCartItems().removeIf(cartItem -> cartItem.getMenu().getId().equals(menuId));
            cartRepository.save(cart);
        }
    }

    @Override
    @Transactional
    public void clearCart(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        UserAccount user = userAccountRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다: " + userId));

        Optional<Cart> cartOptional = cartRepository.findByUserId(userId);
        if (cartOptional.isPresent()) {
            Cart cart = cartOptional.get();
            cart.getCartItems().clear();
            cartRepository.save(cart);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public CartDTO viewCart(Authentication authentication) {
        return getCartByUserId(authentication);
    }
}
