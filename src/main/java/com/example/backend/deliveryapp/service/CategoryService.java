package com.example.backend.deliveryapp.service;

import com.example.backend.deliveryapp.root.exception.EntityExceptionSuppliers;
import com.example.backend.deliveryapp.shop.dto.CategoryDto;
import com.example.backend.deliveryapp.shop.entity.Category;
import com.example.backend.deliveryapp.shop.entity.Shop;
import com.example.backend.deliveryapp.shop.entity.ShopCategory;
import com.example.backend.deliveryapp.shop.repository.CategoryRepository;
import com.example.backend.deliveryapp.shop.repository.ShopCategoryRepository;
import com.example.backend.deliveryapp.shop.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ShopRepository shopRepository;
    private final ShopCategoryRepository shopCategoryRepository;

    public Long createCategory(CategoryDto categoryDto) {
        Category category = Category.builder()
                .name(categoryDto.getName())
                .build();
        return categoryRepository.save(category).getId();
    }

    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(EntityExceptionSuppliers.categoryNotFound);
        categoryRepository.delete(category);
    }

    public CategoryDto findCategory(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(EntityExceptionSuppliers.categoryNotFound);
        return new CategoryDto(category);
    }

    public Long updateCategory(Long id, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(id).orElseThrow(EntityExceptionSuppliers.categoryNotFound);
        category.changeName(categoryDto.getName());
        return category.getId();
    }

    public Long joinShopCategory(long shopId, long categoryId) {
        Shop shop = shopRepository.findById(shopId).orElseThrow(EntityExceptionSuppliers.shopNotFound);
        Category category = categoryRepository.findById(categoryId).orElseThrow(EntityExceptionSuppliers.categoryNotFound);

        if(shopCategoryRepository.existsByShopAndCategory(shop, category)) {
            ShopCategory shopCategory = shopCategoryRepository.findByShopAndCategory(shop, category);
            return shopCategory.getId();
        }
        ShopCategory shopCategory = ShopCategory.builder()
                .shop(shop)
                .category(category)
                .build();

        shopCategoryRepository.save(shopCategory);

        return shopCategory.getId();
    }

    public void withdrawShopCategory(long shopId, long categoryId) {
        Shop shop = shopRepository.findById(shopId).orElseThrow(EntityExceptionSuppliers.shopNotFound);
        Category category = categoryRepository.findById(categoryId).orElseThrow(EntityExceptionSuppliers.categoryNotFound);
        shopCategoryRepository.deleteByShopAndCategory(shop, category);
    }
}
