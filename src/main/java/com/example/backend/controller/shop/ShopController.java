package com.example.backend.controller.shop;

import com.example.backend.shop.dto.ShopDto;
import com.example.backend.shop.dto.ShopSearchResponse;
import com.example.backend.service.shop.ShopService;
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
    public ResponseEntity<ShopDto> getMyShop(Authentication authentication) {
        ShopDto shopDto = shopService.findShopByUser(authentication);
        return ResponseEntity.ok(shopDto);
    }

    @GetMapping("/list")
    public ResponseEntity<List<ShopSearchResponse>> getShopList(@RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "10") int limit) {
        List<ShopSearchResponse> shopList = shopService.getShopList(page, limit);
        return ResponseEntity.ok(shopList);
    }

    @PostMapping("/register")
    public ResponseEntity<Long> createShop(@RequestBody ShopDto shopDto, Authentication authentication) {
        Long shopId = shopService.createShop(shopDto, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(shopId);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteShop(@PathVariable Long id, Authentication authentication) {
        shopService.deleteShop(id, authentication);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update")
    public ResponseEntity<Long> updateShop(@RequestBody ShopDto shopDto, Authentication authentication) {
        Long shopId = shopService.updateShop(shopDto, authentication);
        return ResponseEntity.ok(shopId);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ShopSearchResponse>> getAllShops() {
        List<ShopSearchResponse> shopList = shopService.findAllShops();
        return ResponseEntity.ok(shopList);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ShopSearchResponse>> searchByName(@RequestParam String name) {
        List<ShopSearchResponse> shopList = shopService.findByName(name);
        return ResponseEntity.ok(shopList);
    }

    @GetMapping("/search-by-category")
    public ResponseEntity<List<ShopSearchResponse>> searchByCategory(@RequestParam Long categoryId) {
        List<ShopSearchResponse> shopList = shopService.findByCategory(categoryId);
        return ResponseEntity.ok(shopList);
    }
}
