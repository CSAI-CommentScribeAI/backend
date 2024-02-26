package com.example.backend.controller.shop;

import com.example.backend.shop.dto.ShopDto;
import com.example.backend.shop.dto.ShopSearchResponse;
import com.example.backend.root.ApiResponse;
import com.example.backend.service.shop.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/shops")
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;

    @GetMapping
    public ApiResponse<List<ShopDto>> getShopList(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "limit", defaultValue = "10") int limit) {
        List<ShopSearchResponse> shopSearchResponses = shopService.getShopList(page, limit);
        List<ShopDto> shopDtos = shopSearchResponses.stream()
                .map(shopSearchResponse -> new ShopDto())
                .collect(Collectors.toList());
        return ApiResponse.success(shopDtos);
    }
    @GetMapping("/{shopId}")
    public ApiResponse<ShopDto> getShopDetail(@PathVariable Long shopId) {
        return ApiResponse.success(shopService.findShop(shopId));
    }

    @GetMapping("/member/{memberId}")
    public ApiResponse<ShopDto> getShopDetailByMember(
            @PathVariable Long memberId,
            Authentication authentication) {
        return ApiResponse.success(shopService.findShopByMember(memberId, authentication));
    }

    @PostMapping("/register")
    public ApiResponse<Long> registerShop(@RequestBody ShopDto shopDto,
                                          Authentication authentication) {
        return ApiResponse.success(shopService.createShop(shopDto, authentication));
    }

    @PutMapping("/{shopId}/update")
    public ApiResponse<Long> updateShop(@PathVariable Long shopId,
                                        @RequestBody ShopDto shopDto,
                                        Authentication authentication) {
        return ApiResponse.success(shopService.updateShop(shopId, shopDto, authentication));
    }

    @DeleteMapping("/{shopId}")
    public ApiResponse<Object> withdrawShop(@PathVariable Long shopId,
                                            Authentication authentication) {
        shopService.deleteShop(shopId, authentication);
        return ApiResponse.success(null);
    }

    @DeleteMapping("/profile/{fileNo}")
    public ApiResponse<Object> deleteProfileImage(@PathVariable Long fileNo) {
        return ApiResponse.success(null);
    }
}
