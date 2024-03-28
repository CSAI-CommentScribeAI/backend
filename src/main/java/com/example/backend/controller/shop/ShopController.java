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

    // 현재 인증된 사용자의 상점 상세 정보를 가져오기(GET 요청)
    @GetMapping("/my-shop")
    public ResponseEntity<Shop> getMyShop(Authentication authentication) {
        Shop shop = shopService.findShopByUser(authentication);
        return ResponseEntity.ok(shop);
    }

    // 현재 인증된 사용자의 상점 상세 정보 업데이트 하기(PUT 요청)
    @PutMapping("/my-shop")
    public ResponseEntity<Void> updateMyShop(@RequestBody ShopDto shopDto, Authentication authentication) {
        shopService.updateShop(shopDto, authentication);
        return ResponseEntity.ok().build();
    }

    // 현재 인증된 id로 등록한 상점을 삭제하기(DELETE 요청)
    @DeleteMapping("/my-shop")
    public ResponseEntity<Void> deleteMyShop(Authentication authentication) {
        shopService.deleteShop(authentication);
        return ResponseEntity.ok().build();
    }

    // 전체 상점 목록 가져오기(GET 요청)
    @GetMapping("/all")
    public ResponseEntity<List<ShopSearchResponse>> getAllShops() {
        List<ShopSearchResponse> shopList = shopService.findAllShops();
        return ResponseEntity.ok(shopList);
    }

    // 현재 인증된 사용자가 등록한 매장 전체 목록 보기(GET 요청)
    @GetMapping("/my-shops")
    public ResponseEntity<List<ShopSearchResponse>> getMyShops(Authentication authentication) {
        List<ShopSearchResponse> shopList = (List<ShopSearchResponse>) shopService.findShopByUser(authentication);
        return ResponseEntity.ok(shopList);
    }

    // 상점 이름으로 검색하여 해당하는 상점 목록을 가져오기(GET 요청)
    @GetMapping("/search")
    public ResponseEntity<List<ShopSearchResponse>> searchShopsByName(@RequestParam String name) {
        List<ShopSearchResponse> shopList = shopService.findByName(name);
        return ResponseEntity.ok(shopList);
    }

    // 카테고리별 상점 검색하여 해당하는 상점 목록 가져오기(GET 요청)
    @GetMapping("/search-by-category")
    public ResponseEntity<List<ShopSearchResponse>> searchShopsByCategory(@RequestParam Long categoryId) {
        List<ShopSearchResponse> shopList = shopService.findByCategory(categoryId);
        return ResponseEntity.ok(shopList);
    }

    // 상점 등록하기(POST 요청)
    @PostMapping("/register")
    public ResponseEntity<Long> createShop(@RequestBody ShopDto shopDto, Authentication authentication) {
        Long shopId = shopService.createShop(shopDto, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(shopId);
    }
}
