package com.example.backend.controller.shop;

import lombok.Data;

@Data
public class JoinCategoryRequest {
    private Long shopId;
    private Long categoryId;
}