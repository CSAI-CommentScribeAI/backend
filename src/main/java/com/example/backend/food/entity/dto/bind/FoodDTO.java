package com.example.backend.food.entity.dto.bind;

import com.example.backend.food.entity.FoodStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.example.backend.food.entity.DiscountType;
import com.example.backend.food.entity.Food;
import lombok.Getter;

@Getter
public class FoodDTO {
    @JsonProperty("id")
    private final long id;

    @JsonProperty("foodName")
    private final String name;

    @JsonProperty("shortDescription")
    private final String shortDescription;

    @JsonProperty("price")
    private final int price;

    @JsonProperty("discountType")
    private final DiscountType discountType;

    @JsonProperty("discountAmount")
    private final int discountAmount;

    @JsonProperty("status")
    private final FoodStatus status;

    @JsonProperty("foodImage")
    private final String foodImage;


    public FoodDTO(Food food) {
        this.id = food.getId();
        this.name = food.getName();
        this.shortDescription = food.getShortDescription();
        this.price = food.getPrice();
        this.discountType = food.getDiscountType();
        this.discountAmount = food.getDiscountAmount();
        this.status = food.getStatus();
        this.foodImage = food.getFoodImage();
    }
}
