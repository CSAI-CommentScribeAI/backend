package com.example.backend.controller.shop;

import com.example.backend.root.ApiResponse;
import com.example.backend.shop.dto.CategoryDto;
import com.example.backend.service.shop.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/v1/shops/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ApiResponse<Long> createCategory(@RequestBody CategoryDto categoryDto) {
        return ApiResponse.success(categoryService.createCategory(categoryDto));
    }

    @DeleteMapping("/{categoryId}")
    public ApiResponse<Void> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ApiResponse.success(null, "Category deleted.");
    }

    @PutMapping("/{categoryId}")
    public ApiResponse<Long> updateCategory(@PathVariable Long categoryId, @RequestBody CategoryDto categoryDto) {
        return ApiResponse.success(categoryService.updateCategory(categoryId, categoryDto));
    }

    @PostMapping("/join")
    public ApiResponse<Long> joinCategory(@RequestBody JoinCategoryRequest joinCategoryRequest) {
        return ApiResponse.success(categoryService.joinShopCategory(joinCategoryRequest.getShopId(), joinCategoryRequest.getCategoryId()));
    }
}
