package com.example.backend.food.entity.dto.bind;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class FoodGroupInformationRequest {
    @NotBlank
    private String name;
}
