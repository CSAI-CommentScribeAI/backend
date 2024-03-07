package com.example.backend.service.shop;

import com.example.backend.root.exception.EntityExceptionSuppliers;
import com.example.backend.shop.dto.ShopDto;
import com.example.backend.shop.dto.ShopSearchResponse;
import com.example.backend.shop.entity.Shop;
import com.example.backend.shop.entity.ShopCategory;
import com.example.backend.shop.entity.ShopStatus;
import com.example.backend.shop.repository.ShopCategoryRepository;
import com.example.backend.shop.repository.ShopRepository;
import com.example.backend.UserAccount.entity.UserAccount;
import com.example.backend.UserAccount.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ShopService {

    private final UserAccountRepository userAccountRepository;
    private final ShopRepository shopRepository;
    private final ShopCategoryRepository shopCategoryRepository;

    public ShopDto findShopByUser(Authentication authentication) {
        UserAccount authUser = (UserAccount) authentication.getPrincipal();
        Shop shop = shopRepository.findByUserAccount(authUser)
                .orElseThrow(() -> new IllegalStateException("Shop not found for this user."));
        return new ShopDto(shop);
    }

    public List<ShopSearchResponse> getShopList(int page, int limit) {
        List<ShopSearchResponse> shopResponses = new ArrayList<>();
        shopRepository.findAll().forEach(shop -> shopResponses.add(new ShopSearchResponse(shop)));
        return shopResponses;
    }

    public Long createShop(ShopDto shopDto, Authentication authentication) {
        if (authentication == null) {
            // 적절한 예외 처리 또는 로깅
            throw new RuntimeException("Authentication information is not available.");
        }
        UserAccount authUser = (UserAccount) authentication.getPrincipal();

        Shop shop = Shop.builder()
                .name(shopDto.getName())
                .phoneNum(shopDto.getPhoneNum())
                .shortDescription(shopDto.getShortDescription())
                .longDescription(shopDto.getLongDescription()) // 추가됨
                .supportedOrderType(shopDto.getSupportedOrderType()) // 추가됨
                .supportedPayment(shopDto.getSupportedPayment()) // 추가됨
                .openTime(shopDto.getOpenTime()) // 추가됨
                .closeTime(shopDto.getCloseTime()) // 추가됨
                .deliveryFee(shopDto.getDeliveryFee()) // 추가됨
                .minOrderPrice(shopDto.getMinOrderPrice()) // 추가됨
                .shopStatus(shopDto.getShopStatus()) // 이전에는 PAUSED로 고정되었으나, DTO로부터 받은 값으로 설정
                .registerNumber(shopDto.getRegisterNumber()) // 추가됨
                .doroAddress(shopDto.getDoroAddress()) // 추가됨
                .doroIndex(shopDto.getDoroIndex()) // 추가됨
                .detailAddress(shopDto.getDetailAddress()) // 추가됨
                .userAccount(authUser) // 이전 코드 유지
                .build();

        return shopRepository.save(shop).getId();
    }

    public void deleteShop(Long id, Authentication authentication) {
        Shop shop = shopRepository.findById(id).orElseThrow(EntityExceptionSuppliers.shopNotFound);
        UserAccount authUser = (UserAccount) authentication.getPrincipal();
        if (!shop.getUserAccount().getId().equals(authUser.getId())) {
            throw new IllegalStateException("Permission denied: You are not the owner of this shop.");
        }
        shopRepository.delete(shop);
    }

    public Long updateShop(ShopDto shopDto, Authentication authentication) {
        UserAccount authUser = (UserAccount) authentication.getPrincipal();
        Shop shop = shopRepository.findByUserAccount(authUser)
                .orElseThrow(() -> new IllegalStateException("Shop not found for this user."));
        shop.setShortDescription(shopDto.getShortDescription());
        return shop.getId();
    }

    public List<ShopSearchResponse> findAllShops() {
        List<ShopSearchResponse> shopResponses = new ArrayList<>();
        shopRepository.findAll().forEach(shop -> shopResponses.add(new ShopSearchResponse(shop)));
        return shopResponses;
    }

    public List<ShopSearchResponse> findByName(String shopName) {
        return shopRepository.findByNameContaining(shopName).stream()
                .map(ShopSearchResponse::new)
                .collect(Collectors.toList());
    }

    public List<ShopSearchResponse> findByCategory(Long categoryId) {
        List<ShopCategory> shopCategories = shopCategoryRepository.findAllByCategoryId(categoryId);
        return shopCategories.stream()
                .map(ShopCategory::getShop)
                .map(ShopSearchResponse::new)
                .collect(Collectors.toList());
    }
}
