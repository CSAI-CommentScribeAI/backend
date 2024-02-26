package com.example.backend.deliveryapp.controller;

import com.example.backend.deliveryapp.root.ApiResponse;
import com.example.backend.deliveryapp.shop.dto.CategoryDto;
import com.example.backend.deliveryapp.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ApiResponse<Long> createCategory(@RequestBody CategoryDto categoryDto) {
        return ApiResponse.success(categoryService.createCategory(categoryDto));
    }

    @DeleteMapping("/{categoryId}")
    public ApiResponse<Void> deleteCategory(@PathVariable Long categoryId) { // Category ID 노출하지 않음
        categoryService.deleteCategory(categoryId);
        return ApiResponse.success(null, "Category deleted.");
    }

    @PutMapping("/{categoryId}")
    public ApiResponse<Long> updateCategory(@PathVariable Long categoryId, @RequestBody CategoryDto categoryDto) { // Category ID 노출하지 않음
        return ApiResponse.success(categoryService.updateCategory(categoryId, categoryDto)); // 카테고리 ID를 함께 전달
    }
}
