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

    @DeleteMapping("/delete")
    public ApiResponse<Void> deleteCategory(@PathVariable Long categoryId) { // Category ID 노출하지 않음
        categoryService.deleteCategory(categoryId);
        return ApiResponse.success(null, "Category deleted.");
    }

    @PutMapping("/update")
    public ApiResponse<Long> updateCategory(@PathVariable Long categoryId, @RequestBody CategoryDto categoryDto) { // Category ID 노출하지 않음
        return ApiResponse.success(categoryService.updateCategory(categoryId, categoryDto)); // 카테고리 ID를 함께 전달
    }
}
