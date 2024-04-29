
package com.example.backend.shop.dto;

import com.example.backend.shop.entity.Category;
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
}