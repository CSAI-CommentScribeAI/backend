
package com.example.backend.shop.dto;

import com.example.backend.shop.entity.Shop;
import com.example.backend.shop.entity.ShopStatus;
import com.example.backend.shop.entity.ShopSupportedOrderType;
import com.example.backend.shop.entity.ShopSupportedPayment;
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