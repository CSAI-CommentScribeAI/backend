package com.example.backend.menu.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class FoodGroupInformationRequest {
    @NotBlank
    private String name;
}