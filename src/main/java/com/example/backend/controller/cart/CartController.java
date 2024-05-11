package com.example.backend.controller.cart;

import com.example.backend.dto.ResponseDTO;
import com.example.backend.dto.cart.CartDTO;
import com.example.backend.dto.cart.CartItemDTO;
import com.example.backend.service.cart.impl.CartServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartServiceImpl cartService;

    @Autowired
    public CartController(CartServiceImpl cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<CartDTO>> viewCart(Authentication authentication) {
        CartDTO cartDTO = cartService.viewCart(authentication);
        return ResponseEntity.ok(new ResponseDTO<>(HttpStatus.OK.value(), cartDTO));
    }

    @PostMapping("/add")
    public ResponseEntity<List<CartItemDTO>> addToCart(Authentication authentication, @RequestBody CartItemDTO cartItemDTO) {
        List<CartItemDTO> cartItems = cartService.addToCart(authentication, cartItemDTO);
        return ResponseEntity.ok(cartItems);
    }
    @DeleteMapping("/remove/{menuId}")
    public ResponseEntity<ResponseDTO<Void>> removeFromCart(Authentication authentication, @PathVariable Long menuId) {
        cartService.removeFromCart(authentication, menuId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/clear")
    public ResponseEntity<ResponseDTO<Void>> clearCart(Authentication authentication) {
        cartService.clearCart(authentication);
        return ResponseEntity.noContent().build();
    }
}
