package com.example.backend.deliveryapp.shop.dto;

import com.example.backend.deliveryapp.shop.entity.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class CategoryDto {
    @NotNull(message = "Name is mandatory")
    private String name;

    public CategoryDto(Category category) {
        this.name = category.getName();
    }

    public CategoryDto(String name) {
        this.name = name;
    }
}
