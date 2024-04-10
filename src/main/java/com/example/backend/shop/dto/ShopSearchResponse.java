package com.example.backend.shop.dto;

import com.example.backend.shop.entity.Shop;
import com.example.backend.shop.entity.ShopStatus;
import com.example.backend.shop.entity.ShopSupportedOrderType;
import com.example.backend.shop.entity.ShopSupportedPayment;
import lombok.Getter;

@Getter
public class ShopSearchResponse {
    private final String shopName;
    private final String phoneNum;
    private final String shortDescription;
    private final String longDescription;
    private final ShopSupportedOrderType supportedOrderType;
    private final ShopSupportedPayment supportedPayment;
    private final int deliveryFee;
    private final int minOrderPrice;
    private final ShopStatus shopStatus;
    private final String registerNumber;
    private final String doroAddress;
    private final int doroIndex;
    private final String detailAddress;
    private final String shopImage; // S3에서의 이미지 파일 키 또는 URL
    // 필요한 경우 추가 필드를 정의할 수 있습니다.

    public ShopSearchResponse(Shop shop) {
        this.shopName = shop.getName();
        this.phoneNum = shop.getPhoneNum();
        this.shortDescription = shop.getShortDescription();
        this.longDescription = shop.getLongDescription();
        this.supportedOrderType = shop.getSupportedOrderType();
        this.supportedPayment = shop.getSupportedPayment();
        this.deliveryFee = shop.getDeliveryFee();
        this.minOrderPrice = shop.getMinOrderPrice();
        this.shopStatus = shop.getShopStatus();
        this.registerNumber = shop.getRegisterNumber();
        this.doroAddress = shop.getDoroAddress();
        this.doroIndex = shop.getDoroIndex();
        this.detailAddress = shop.getDetailAddress();
        this.shopImage = shop.getShopImage();
    }
}
