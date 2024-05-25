package com.example.backend.service.cart.impl;

import com.example.backend.dto.cart.CartDTO;
import com.example.backend.dto.cart.CartItemDTO;
import com.example.backend.dto.menu.MenuDTO;
import com.example.backend.entity.cart.Cart;
import com.example.backend.entity.cart.CartItem;
import com.example.backend.entity.menu.Menu;
import com.example.backend.entity.store.Store;
import com.example.backend.entity.userAccount.UserAccount;
import com.example.backend.entity.userAccount.UserAddress;
import com.example.backend.repository.UserAccount.UserAccountRepository;
import com.example.backend.repository.UserAccount.UserAddressRepository;
import com.example.backend.repository.cart.CartRepository;
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
    private final UserAddressRepository userAddressRepository;

    @Override
    public CartDTO getCartByUserId(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        UserAccount userAccount = userAccountRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("ID에 해당하는 사용자를 찾을 수 없습니다: " + userId));

        Optional<Cart> cartOptional = cartRepository.findByUserId(userId);
        return cartOptional.map(cart -> convertToCartDTO(cart, userAccount)).orElse(null);
    }

    private CartDTO convertToCartDTO(Cart cart, UserAccount userAccount) {
        List<CartItemDTO> cartItems = new ArrayList<>();
        cart.getCartItems().forEach(item -> {
            CartItemDTO itemDTO = new CartItemDTO();
            itemDTO.setMenuId(item.getMenu().getId());
            itemDTO.setMenuName(item.getMenu().getName());
            itemDTO.setImageUrl(item.getImageUrl());
            cartItems.add(itemDTO);
        });

        CartDTO cartDTO = new CartDTO();
        cartDTO.setUserId(cart.getUser().getId());
        cartDTO.setCartItems(cartItems);

        List<UserAddress> userAddresses = userAddressRepository.findByUserAccountId(userAccount.getId());
        if (userAddresses.isEmpty()) {
            throw new IllegalStateException("사용자의 주소를 찾을 수 없습니다: " + userAccount.getId());
        }
        UserAddress userAddress = userAddresses.get(0);
        cartDTO.setUserAddress(userAddress.getFullAddress());

        return cartDTO;
    }

    @Override
    @Transactional
    public CartDTO addToCart(Authentication authentication, MenuDTO menuDTO) {
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

        if (cart.getStoreId() == null || cart.getStoreId().equals(menuDTO.getStoreId())) { // 수정된 부분
            CartItem existingItem = findExistingItem(cart, menuDTO.getId());
            if (existingItem == null) {
                addNewItem(cart, menuDTO);
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

    private void addNewItem(Cart cart, MenuDTO menuDTO) {
        if (cart.getStoreId() == null) {
            cart.setStoreId(menuDTO.getStoreId()); // 수정된 부분
        }
        CartItem newCartItem = new CartItem();
        newCartItem.setMenu(Menu.builder()
                .id(menuDTO.getId())
                .name(menuDTO.getName())
                .imageUrl(menuDTO.getImageUrl())
                .price(menuDTO.getPrice())
                .menuDetail(menuDTO.getMenuDetail())
                .store(Store.builder().id(menuDTO.getStoreId()).build()) // store 설정
                .status(menuDTO.getStatus())
                .build());
        newCartItem.setImageUrl(menuDTO.getImageUrl());
        newCartItem.setCart(cart); // CartItem에 cart 설정
        cart.getCartItems().add(newCartItem);
    }

    @Override
    @Transactional
    public void removeFromCart(Authentication authentication, Long menuId) {
        Long userId = Long.parseLong(authentication.getName());
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
