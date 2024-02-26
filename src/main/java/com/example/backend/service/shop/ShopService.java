package com.example.backend.service.shop;

import com.example.backend.root.exception.EntityExceptionSuppliers;
import com.example.backend.shop.dto.ShopDto;
import com.example.backend.shop.dto.ShopSearchResponse;
import com.example.backend.shop.entity.Shop;
import com.example.backend.shop.entity.ShopCategory;
import com.example.backend.shop.repository.ShopCategoryRepository;
import com.example.backend.shop.repository.ShopRepository;
import com.example.backend.UserAccount.entity.UserAccount;
import com.example.backend.UserAccount.repository.UserAccountRepository;
import com.example.backend.food.entity.Food;
import com.example.backend.food.repository.FoodRepository;
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
    private final FoodRepository foodRepository;
    private final ShopOwnerAuthorizationService shopOwnerAuthorizationService;

    // 회원 ID를 기반으로 해당 회원의 가게를 찾는 메서드
    public ShopDto findShopByMember(Long memberId, Authentication authentication) {
        // 인증된 사용자가 소유한 가게인지 확인하고, 소유한 경우 해당 가게 정보를 반환
        if (authentication != null && authentication.getPrincipal() instanceof UserAccount) {
            UserAccount useraccount = (UserAccount) authentication.getPrincipal();
            if (useraccount.getId().equals(memberId)) {
                Shop shop = shopRepository.findByUserAccountId(useraccount.getId())
                        .orElseThrow(EntityExceptionSuppliers.shopNotFound);
                return new ShopDto(shop);
            }
        }
        // 인증된 사용자가 없거나 인증된 사용자가 소유한 가게가 아닌 경우 예외 처리
        throw new IllegalStateException("Unauthorized access or shop not found.");
    }

    // 페이지 및 제한을 기준으로 가게 목록을 반환하는 메서드
    public List<ShopSearchResponse> getShopList(int page, int limit) {
        List<ShopSearchResponse> shopResponses = new ArrayList<>();
        shopRepository.findAll().forEach(shop -> shopResponses.add(new ShopSearchResponse(shop)));
        return shopResponses;
    }

    // 가게 생성 메서드
    public Long createShop(ShopDto shopDto, Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof UserAccount) {
            UserAccount owner = (UserAccount) authentication.getPrincipal();
            Shop shop = Shop.builder()
                    // 매장 정보 설정
                    .userAccount(owner)
                    .build();
            return shopRepository.save(shop).getId();
        } else {
            throw new IllegalStateException("Only owners can create shops.");
        }
    }

    // 가게 삭제 메서드
    public void deleteShop(Long id, Authentication authentication) {
        Shop shop = shopRepository.findById(id).orElseThrow(EntityExceptionSuppliers.shopNotFound);
        if (shop.getUserAccount() != null && authentication != null && authentication.getPrincipal() instanceof UserAccount) {
            UserAccount userAccount = (UserAccount) authentication.getPrincipal();
            if (shop.getUserAccount().getId().equals(userAccount.getId())) {
                shopRepository.delete(shop);
            } else {
                throw new IllegalStateException("Permission denied: You are not the owner of this shop.");
            }
        } else {
            throw new IllegalStateException("Only owners can delete shops.");
        }
    }

    // 가게 업데이트 메서드
    public Long updateShop(Long id, ShopDto shopDto, Authentication authentication) {
        Shop shop = shopRepository.findById(id).orElseThrow(EntityExceptionSuppliers.shopNotFound);
        if (shop.getUserAccount() != null && authentication != null && authentication.getPrincipal() instanceof UserAccount) {
            UserAccount userAccount = (UserAccount) authentication.getPrincipal();
            if (shop.getUserAccount().getId().equals(userAccount.getId())) {
                // 매장 정보 업데이트 로직
                return shop.getId();
            } else {
                throw new IllegalStateException("Permission denied: You are not the owner of this shop.");
            }
        } else {
            throw new IllegalStateException("Only owners can update shops.");
        }
    }

    public ShopDto findShop(Long id) {
        Shop shop = shopRepository.findById(id).orElseThrow(EntityExceptionSuppliers.shopNotFound);
        return new ShopDto(shop);
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

    public List<ShopSearchResponse> findByFoodName(String foodName) {
        List<Food> foods = foodRepository.findByName(foodName);
        List<ShopSearchResponse> responses = new ArrayList<>();
        for (Food food : foods) {
            Shop shop = food.getShop();
            responses.add(new ShopSearchResponse(shop));
        }
        return responses;
    }

    public List<ShopSearchResponse> searchShopsByQuery(String query) {
        List<ShopSearchResponse> searchResults = new ArrayList<>();
        List<Shop> shops = shopRepository.findByNameContaining(query);
        for (Shop shop : shops) {
            searchResults.add(new ShopSearchResponse(shop));
        }
        return searchResults;
    }
}