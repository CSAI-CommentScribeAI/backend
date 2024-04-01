package com.example.backend.service.shop;

import com.example.backend.root.exception.EntityExceptionSuppliers;
import com.example.backend.shop.dto.CategoryDto;
import com.example.backend.shop.entity.Category;
import com.example.backend.shop.entity.Shop;
import com.example.backend.shop.entity.ShopCategory;
import com.example.backend.shop.repository.CategoryRepository;
import com.example.backend.shop.repository.ShopCategoryRepository;
import com.example.backend.shop.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        Category category = categoryRepository.findById(id)
                .orElseThrow(EntityExceptionSuppliers.categoryNotFound);

        // 해당 카테고리와 연관된 모든 ShopCategory 삭제
        List<ShopCategory> shopCategories = shopCategoryRepository.findByCategory(category);
        for (ShopCategory shopCategory : shopCategories) {
            shopCategoryRepository.delete(shopCategory);
        }

        // Category 삭제
        categoryRepository.delete(category);
    }

    public CategoryDto findCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(EntityExceptionSuppliers.categoryNotFound);
        return new CategoryDto(category);
    }

    public Long updateCategory(Long id, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(EntityExceptionSuppliers.categoryNotFound);
        category.changeName(categoryDto.getName());
        return category.getId();
    }

    public Long joinShopCategory(long shopId, long categoryId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(EntityExceptionSuppliers.shopNotFound);
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(EntityExceptionSuppliers.categoryNotFound);

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
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(EntityExceptionSuppliers.shopNotFound);
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(EntityExceptionSuppliers.categoryNotFound);
        shopCategoryRepository.deleteByShopAndCategory(shop, category);
    }
}
