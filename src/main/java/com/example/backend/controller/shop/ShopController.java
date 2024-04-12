package com.example.backend.controller.shop;

import com.example.backend.service.shop.ShopService;
import com.example.backend.shop.dto.ShopDto;
import com.example.backend.shop.dto.ShopSearchResponse;
import com.example.backend.shop.entity.Shop;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/shops")
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;

    @GetMapping("/my-shop")
    public ResponseEntity<Shop> getMyShop(Authentication authentication) {
        Shop shop = shopService.findShopByUser(authentication);
        return ResponseEntity.ok(shop);
    }

    @PutMapping("/my-shop")
    public ResponseEntity<Void> updateMyShop(@ModelAttribute ShopDto shopDto, Authentication authentication) throws IOException {
        shopService.updateShop(shopDto, authentication);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/my-shop")
    public ResponseEntity<Void> deleteMyShop(Authentication authentication) {
        shopService.deleteShop(authentication);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<ShopSearchResponse>> getAllShops() {
        List<ShopSearchResponse> shopList = shopService.findAllShops();
        return ResponseEntity.ok(shopList);
    }

    @PostMapping("/register")
    public ResponseEntity<Long> createShop(@ModelAttribute ShopDto shopDto, Authentication authentication) throws IOException {
        Long shopId = shopService.createShop(shopDto, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(shopId);
    }
}
