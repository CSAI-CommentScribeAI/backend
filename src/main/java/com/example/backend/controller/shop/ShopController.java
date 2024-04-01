package com.example.backend.controller.shop;

import com.example.backend.shop.dto.ShopDto;
import com.example.backend.shop.dto.ShopSearchResponse;
import com.example.backend.service.shop.ShopService;
import com.example.backend.shop.entity.Shop;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Void> updateMyShop(@RequestBody ShopDto shopDto, Authentication authentication) {
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

    @GetMapping("/my-shops")
    public ResponseEntity<List<ShopSearchResponse>> getMyShops(Authentication authentication) {
        Shop shop = shopService.findShopByUser(authentication);
        if (shop != null) {
            return ResponseEntity.ok(shopService.findByCategory(shop.getId()));
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<ShopSearchResponse>> searchShopsByName(@RequestParam String name) {
        List<ShopSearchResponse> shopList = shopService.findByName(name);
        return ResponseEntity.ok(shopList);
    }

    @GetMapping("/search-by-category")
    public ResponseEntity<List<ShopSearchResponse>> searchShopsByCategory(@RequestParam Long categoryId) {
        List<ShopSearchResponse> shopList = shopService.findByCategory(categoryId);
        return ResponseEntity.ok(shopList);
    }

    @PostMapping("/register")
    public ResponseEntity<Long> createShop(@RequestBody ShopDto shopDto, Authentication authentication) {
        Long shopId = shopService.createShop(shopDto, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(shopId);
    }
}
