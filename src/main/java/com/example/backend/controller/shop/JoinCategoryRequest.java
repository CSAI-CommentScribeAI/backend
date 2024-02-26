package com.example.backend.controller.shop;

import lombok.Data;

@Data
public class JoinCategoryRequest {
    private Long shopId;
    private Long categoryId;

    // 생성자, getter 및 setter 메서드

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
