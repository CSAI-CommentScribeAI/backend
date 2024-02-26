package com.example.backend.deliveryapp.food.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.example.backend.deliveryapp.food.entity.FoodSub;
import lombok.Getter;

@Getter
public class FoodSubDTO {
    @JsonProperty("id")
    private final long id;

    @JsonProperty("name")
    private final String name;

    @JsonProperty("price")
    private final int price;

    @JsonProperty("group")
    private FoodSubSelectGroupDTO group;


    public FoodSubDTO(FoodSub foodSub) {
        this.id = foodSub.getId();
        this.name = foodSub.getName();
        this.price = foodSub.getPrice();
        if(foodSub.getSelectGroup() != null) {
            this.group = new FoodSubSelectGroupDTO(foodSub.getSelectGroup());
        }
    }
}
