package com.example.backend.service.shop;

import com.example.backend.shop.dto.ShopDto;
import com.example.backend.shop.dto.ShopSearchResponse;
import com.example.backend.shop.entity.Shop;
import com.example.backend.shop.entity.ShopCategory;
import com.example.backend.shop.repository.ShopCategoryRepository;
import com.example.backend.shop.repository.ShopRepository;
import com.example.backend.UserAccount.entity.UserAccount;
import com.example.backend.UserAccount.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
@Transactional
@RequiredArgsConstructor
public class ShopService {

    private final UserAccountRepository userAccountRepository;
    private final ShopRepository shopRepository;
    private final ShopCategoryRepository shopCategoryRepository;

    public Shop findShopByUser(Authentication authentication) {
        long userId = Long.parseLong(authentication.getName());
        UserAccount authUser = userAccountRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with userId: " + userId));
        return shopRepository.findByUserAccount(authUser)
                .orElseThrow(() -> new IllegalStateException("Shop not found for this user."));
    }

    public List<ShopSearchResponse> getShopList(int page, int limit) {
        Iterable<Shop> iterable = shopRepository.findAll();
        Stream<Shop> stream = StreamSupport.stream(iterable.spliterator(), false);
        return stream
                .map(ShopSearchResponse::new)
                .collect(Collectors.toList());
    }

    public Long createShop(ShopDto shopDto, Authentication authentication) {
        if (authentication == null) {
            throw new RuntimeException("Authentication information is not available.");
        }

        long userId = Long.parseLong(authentication.getName());
        UserAccount authUser = userAccountRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with userId: " + userId));

        Shop shop = Shop.builder()
                .name(shopDto.getName())
                .phoneNum(shopDto.getPhoneNum())
                .shortDescription(shopDto.getShortDescription())
                .longDescription(shopDto.getLongDescription())
                .supportedOrderType(shopDto.getSupportedOrderType())
                .supportedPayment(shopDto.getSupportedPayment())
                .openTime(shopDto.getOpenTime())
                .closeTime(shopDto.getCloseTime())
                .deliveryFee(shopDto.getDeliveryFee())
                .minOrderPrice(shopDto.getMinOrderPrice())
                .shopStatus(shopDto.getShopStatus())
                .registerNumber(shopDto.getRegisterNumber())
                .doroAddress(shopDto.getDoroAddress())
                .doroIndex(shopDto.getDoroIndex())
                .detailAddress(shopDto.getDetailAddress())
                .userAccount(authUser)
                .build();

        return shopRepository.save(shop).getId();
    }

    public Long deleteShop(Authentication authentication) {
        Shop shop = findShopByUser(authentication);
        long userId = Long.parseLong(authentication.getName());
        UserAccount authUser = userAccountRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with userId: " + userId));
        if (!shop.getUserAccount().getId().equals(authUser.getId())) {
            throw new IllegalStateException("Permission denied: You are not the owner of this shop.");
        }
        shopRepository.delete(shop);
        return shop.getId();
    }

    public Long updateShop(ShopDto shopDto, Authentication authentication) {
        long userId = Long.parseLong(authentication.getName());
        UserAccount authUser = userAccountRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with userId: " + userId));
        Shop shop = shopRepository.findByUserAccount(authUser)
                .orElseThrow(() -> new IllegalStateException("Shop not found for this user."));
        shop.setShortDescription(shopDto.getShortDescription());
        return shop.getId();
    }

    public List<ShopSearchResponse> findAllShops() {
        Iterable<Shop> iterable = shopRepository.findAll();
        Stream<Shop> stream = StreamSupport.stream(iterable.spliterator(), false);
        return stream
                .map(ShopSearchResponse::new)
                .collect(Collectors.toList());
    }

    public List<ShopSearchResponse> findByName(String shopName) {
        Iterable<Shop> iterable = shopRepository.findByNameContaining(shopName);
        Stream<Shop> stream = StreamSupport.stream(iterable.spliterator(), false);
        return stream
                .map(ShopSearchResponse::new)
                .collect(Collectors.toList());
    }

    public List<ShopSearchResponse> findByCategory(Long categoryId) {
        List<ShopCategory> shopCategories = shopCategoryRepository.findAllByCategoryId(categoryId);
        List<Shop> shops = shopCategories.stream()
                .map(ShopCategory::getShop)
                .collect(Collectors.toList());
        return shops.stream()
                .map(ShopSearchResponse::new)
                .collect(Collectors.toList());
    }
}
