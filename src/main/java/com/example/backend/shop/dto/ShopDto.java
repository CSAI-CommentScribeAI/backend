package com.example.backend.shop.dto;

import com.example.backend.shop.entity.Shop;
import com.example.backend.shop.entity.ShopStatus;
import com.example.backend.shop.entity.ShopSupportedOrderType;
import com.example.backend.shop.entity.ShopSupportedPayment;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.example.backend.shop.entity.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
public class ShopDto {

    @NotNull(message = "Name is mandatory")
    private String name;

    @JsonProperty("phoneNum")
    private String phoneNum;

    @Range(min = 1, max = 100, message = "Short Description is between 0 and 100")
    private String shortDescription;

    @Range(min = 1, max = 1000, message = "Long Description is between 0 and 1000")
    private String longDescription;

    @JsonProperty("supportedOrderType")
    private ShopSupportedOrderType supportedOrderType;

    @JsonProperty("supportedPayment")
    private ShopSupportedPayment supportedPayment;

    @JsonProperty("openTime")
    private LocalTime openTime;

    @JsonProperty("closeTime")
    private LocalTime closeTime;

    @Min(0)
    private int deliveryFee;

    @Min(0)
    private int minOrderPrice;

    @JsonProperty("shopStatus")
    private ShopStatus shopStatus;

    @NotNull(message = "RegisterNumber is mandatory")
    private String registerNumber;

    @NotNull(message = "Owner is mandatory")
    private long ownerId;

    @JsonProperty("doroAddress")
    private String doroAddress;

    @JsonProperty("doroIndex")
    private int doroIndex;

    @JsonProperty("detailAddress")
    private String detailAddress;


    public ShopDto(Shop shop) {
        this.name = shop.getName();
        this.phoneNum = shop.getPhoneNum();
        this.shortDescription = shop.getShortDescription();
        this.longDescription = shop.getLongDescription();
        this.supportedOrderType = shop.getSupportedOrderType();
        this.supportedPayment = shop.getSupportedPayment();
        this.openTime = shop.getOpenTime();
        this.closeTime = shop.getCloseTime();
        this.deliveryFee = shop.getDeliveryFee();
        this.minOrderPrice = shop.getMinOrderPrice();
        this.shopStatus = shop.getShopStatus();
        this.registerNumber = shop.getRegisterNumber();
        this.ownerId = shop.getUserAccount().getId();
        this.doroAddress = shop.getDoroAddress();
        this.doroIndex = shop.getDoroIndex();
        this.detailAddress = shop.getDetailAddress();
    }

}
