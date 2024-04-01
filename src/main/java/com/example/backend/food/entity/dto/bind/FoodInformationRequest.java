package com.example.backend.food.entity.dto.bind;

import com.example.backend.food.entity.DiscountType;
import com.example.backend.food.entity.FoodStatus;
import com.example.backend.shop.dto.ShopDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Getter
@Setter
@NoArgsConstructor
public class FoodInformationRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String shortDescription;

    @Positive
    private int price;

    @NotNull
    private DiscountType discountType;

    @PositiveOrZero
    private int discountAmount;

    @NotNull
    private FoodStatus status;

    private Long shopId; // Add shopId field

    @Builder
    public FoodInformationRequest(String name, String shortDescription, int price, DiscountType discountType, int discountAmount, FoodStatus status, Long shopId) {
        this.name = name;
        this.shortDescription = shortDescription;
        this.price = price;
        this.discountType = discountType;
        this.discountAmount = discountAmount;
        this.status = status;
        this.shopId = shopId;
    }
}
