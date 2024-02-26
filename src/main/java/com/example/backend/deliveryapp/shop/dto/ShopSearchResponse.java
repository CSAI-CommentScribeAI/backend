package com.example.backend.deliveryapp.shop.dto;

import com.example.backend.deliveryapp.shop.entity.Shop;
import com.example.backend.deliveryapp.shop.entity.ShopStatus;
import com.example.backend.deliveryapp.shop.entity.ShopSupportedOrderType;
import com.example.backend.deliveryapp.shop.entity.ShopSupportedPayment;
import lombok.Getter;

@Getter
public class ShopSearchResponse {
    private String shopName;
    private String phoneNum;
    private ShopSupportedOrderType supportedOrderType;
    private ShopSupportedPayment supportedPayment;
    private int deliveryFee;
    private int minOrderPrice;
    private ShopStatus shopStatus;

    // 수정된 생성자: 음식 목록 매개변수 제거
    public ShopSearchResponse(Shop shop) {
        this.shopName = shop.getName();
        this.phoneNum = shop.getPhoneNum();
        this.supportedOrderType = shop.getSupportedOrderType();
        this.supportedPayment = shop.getSupportedPayment();
        this.deliveryFee = shop.getDeliveryFee();
        this.minOrderPrice = shop.getMinOrderPrice();
        this.shopStatus = shop.getShopStatus();
    }
}
