package com.example.backend.service.shop;

import com.example.backend.UserAccount.entity.UserAccount;
import com.example.backend.UserAccount.repository.UserAccountRepository;
import com.example.backend.service.aws.S3Service;
import com.example.backend.shop.dto.ShopDto;
import com.example.backend.shop.dto.ShopSearchResponse;
import com.example.backend.shop.entity.Shop;
import com.example.backend.shop.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
@RequiredArgsConstructor
public class ShopService {

    private final UserAccountRepository userAccountRepository;
    private final ShopRepository shopRepository;
    private final S3Service s3Service;
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public Shop findShopByUser(Authentication authentication) {
        UserAccount authUser = getAuthenticatedUser(authentication);
        return shopRepository.findByUserAccount(authUser)
                .orElseThrow(() -> new IllegalStateException("Shop not found for this user."));
    }

    public Long createShop(ShopDto shopDto, Authentication authentication) throws IOException {
        UserAccount authUser = getAuthenticatedUser(authentication);
        MultipartFile shopImageFile = shopDto.getShopImage();
        String shopName = shopDto.getName();
        String folderName = "shop/" + shopName + "/shoplogo/";

        String s3FileKey = s3Service.uploadFile(shopImageFile, folderName);

        LocalTime openTime = LocalTime.parse(shopDto.getOpenTime(), timeFormatter);
        LocalTime closeTime = LocalTime.parse(shopDto.getCloseTime(), timeFormatter);

        Shop shop = Shop.builder()
                .name(shopDto.getName())
                .phoneNum(shopDto.getPhoneNum())
                .shortDescription(shopDto.getShortDescription())
                .longDescription(shopDto.getLongDescription())
                .supportedOrderType(shopDto.getSupportedOrderType())
                .supportedPayment(shopDto.getSupportedPayment())
                .openTime(openTime)
                .closeTime(closeTime)
                .deliveryFee(shopDto.getDeliveryFee())
                .minOrderPrice(shopDto.getMinOrderPrice())
                .shopStatus(shopDto.getShopStatus())
                .registerNumber(shopDto.getRegisterNumber())
                .doroAddress(shopDto.getDoroAddress())
                .doroIndex(shopDto.getDoroIndex())
                .detailAddress(shopDto.getDetailAddress())
                .shopImage(s3FileKey)
                .userAccount(authUser)
                .build();

        return shopRepository.save(shop).getId();
    }

    public Long updateShop(ShopDto shopDto, Authentication authentication) throws IOException {
        Shop shop = findShopByUser(authentication);

        if (shopDto.getShopImage() != null && !shopDto.getShopImage().isEmpty()) {
            if (shop.getShopImage() != null) {
                s3Service.deleteFile(shop.getShopImage());
            }
            String shopName = shopDto.getName();
            String folderName = "shop/" + shopName + "/shoplogo/";
            String s3FileKey = s3Service.uploadFile(shopDto.getShopImage(), folderName);
            shop.setShopImage(s3FileKey);
        }

        shop.setShortDescription(shopDto.getShortDescription());
        return shop.getId();
    }

    public Long deleteShop(Authentication authentication) {
        Shop shop = findShopByUser(authentication);
        if (shop.getShopImage() != null) {
            s3Service.deleteFile(shop.getShopImage());
        }
        shopRepository.delete(shop);
        return shop.getId();
    }

    public List<ShopSearchResponse> findAllShops() {
        Iterable<Shop> shops = shopRepository.findAll();
        return StreamSupport.stream(shops.spliterator(), false)
                .map(ShopSearchResponse::new) // Shop 객체를 ShopSearchResponse 생성자로 전달
                .collect(Collectors.toList());
    }

    private UserAccount getAuthenticatedUser(Authentication authentication) {
        long userId = Long.parseLong(authentication.getName());
        return userAccountRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with userId: " + userId));
    }
}
