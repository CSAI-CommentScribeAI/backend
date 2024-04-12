package com.example.backend.service.menu;

import com.example.backend.menu.dto.FoodSubInformationRequest;
import com.example.backend.menu.entity.Food;
import com.example.backend.menu.entity.FoodSub;
import com.example.backend.menu.entity.FoodSubSelectGroup;
import com.example.backend.menu.dto.FoodSubDTO;
import com.example.backend.menu.repository.FoodRepository;
import com.example.backend.menu.repository.FoodSubRepository;
import com.example.backend.menu.repository.FoodSubSelectGroupRepository;
import com.example.backend.root.exception.EntityExceptionSuppliers;
import com.example.backend.shop.entity.Shop;
import com.example.backend.shop.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FoodSubService {
    private final ShopRepository shopRepository;
    private final FoodRepository foodRepository;
    private final FoodSubRepository foodSubRepository;
    private final FoodSubSelectGroupRepository foodSubSelectGroupRepository;

    public List<FoodSubDTO> createFoodSub(long shopId, long foodId, FoodSubInformationRequest request) {
        Food food = foodRepository.findById(foodId).orElseThrow(EntityExceptionSuppliers.foodNotFound);
        Shop shop = shopRepository.findById(shopId).orElseThrow(EntityExceptionSuppliers.shopNotFound);
        FoodSub.FoodSubBuilder foodSubBuilder = FoodSub.builder()
                .shop(shop)
                .food(food)
                .name(request.getName())
                .price(request.getPrice());

        if(request.getGroup() != null) {
            foodSubSelectGroupRepository.findById(request.getGroup())
                    .ifPresent(foodSubBuilder::group);
        }

        FoodSub foodSub = foodSubBuilder.build();
        foodSubRepository.save(foodSub);
        return foodSubRepository.findAllByFood(food).stream()
                .map(FoodSubDTO::new)
                .collect(Collectors.toList());
    }

    public List<FoodSubDTO> deleteFoodSub(long foodSubId) {
        FoodSub foodSub = foodSubRepository.findById(foodSubId).orElseThrow(EntityExceptionSuppliers.foodSubNotFound);
        foodSub.withdrawGroup();
        foodSubRepository.delete(foodSub);
        return foodSubRepository.findAllByFood(foodSub.getFood()).stream()
                .map(FoodSubDTO::new)
                .collect(Collectors.toList());
    }

    public FoodSubDTO updateFoodSub(long foodSubId, FoodSubInformationRequest request) {
        FoodSub foodSub = foodSubRepository.findById(foodSubId).orElseThrow(EntityExceptionSuppliers.foodSubNotFound);
        foodSub.changeName(request.getName());
        foodSub.changePrice(request.getPrice());
        if(request.getGroup() != null) {
            FoodSubSelectGroup group = foodSubSelectGroupRepository.findById(request.getGroup()).orElseThrow(EntityExceptionSuppliers.foodSubSelectGroupNotFound);
            foodSub.changeGroup(group);
        }
        return new FoodSubDTO(foodSub);
    }

    public List<FoodSubDTO> getFoodSubFromSubGroup(long groupId) {
        FoodSubSelectGroup group = foodSubSelectGroupRepository.findById(groupId).orElseThrow(EntityExceptionSuppliers.foodSubSelectGroupNotFound);
        return group.getFoods().stream()
                .map(foodSub -> new FoodSubDTO(foodSub))
                .collect(Collectors.toList());
    }
}
