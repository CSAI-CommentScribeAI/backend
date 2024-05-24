package com.example.backend.controller.cart;

import com.example.backend.dto.ResponseDTO;
import com.example.backend.dto.cart.CartDTO;
import com.example.backend.dto.cart.CartItemDTO;
import com.example.backend.service.cart.impl.CartServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartServiceImpl cartService;

    @GetMapping
    public ResponseEntity<ResponseDTO<CartDTO>> viewCart(Authentication authentication) {
        CartDTO cartDTO = cartService.viewCart(authentication);
        return ResponseEntity.ok(new ResponseDTO<>(HttpStatus.OK.value(), cartDTO));
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseDTO<CartDTO>> addToCart(Authentication authentication, @RequestBody CartItemDTO cartItemDTO) {
        CartDTO cartDTO = cartService.addToCart(authentication, cartItemDTO);
        return ResponseEntity.ok(new ResponseDTO<>(HttpStatus.OK.value(), cartDTO));
    }

    @DeleteMapping("/remove/{menuId}")
    public ResponseEntity<ResponseDTO<Void>> removeFromCart(Authentication authentication, @PathVariable Long menuId) {
        cartService.removeFromCart(authentication, menuId);
        return ResponseEntity.ok(new ResponseDTO<>(HttpStatus.NO_CONTENT.value(), null));
    }

    @DeleteMapping("/clear")
    public ResponseEntity<ResponseDTO<Void>> clearCart(Authentication authentication) {
        cartService.clearCart(authentication);
        return ResponseEntity.ok(new ResponseDTO<>(HttpStatus.NO_CONTENT.value(), null));
    }
}
