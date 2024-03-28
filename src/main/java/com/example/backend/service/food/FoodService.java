package com.example.backend.service.food;

import com.example.backend.food.entity.dto.bind.FoodInformationRequest;
import com.example.backend.food.entity.Food;
import com.example.backend.food.entity.FoodGroup;
import com.example.backend.food.entity.dto.bind.FoodDTO;
import com.example.backend.food.repository.FoodRepository;
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
public class FoodService {
    private final ShopRepository shopRepository;
    private final FoodRepository foodRepository;


    public List<FoodDTO> createFood(long shopId, Long id, FoodInformationRequest request) {
        Shop shop = shopRepository.findById(shopId).orElseThrow(EntityExceptionSuppliers.shopNotFound);
        Food food = Food.builder()
                .name(request.getName())
                .shortDesc(request.getShortDescription())
                .price(request.getPrice())
                .discountType(request.getDiscountType())
                .discountAmount(request.getDiscountAmount())
                .shop(shop)
                .status(request.getStatus()).build();
        foodRepository.save(food);
        return foodRepository.findAllByShop(shop).stream()
                .map(FoodDTO::new)
                .collect(Collectors.toList());
    }

    public List<FoodDTO> deleteFood(long foodId) {
        Food food = foodRepository.findById(foodId).orElseThrow(EntityExceptionSuppliers.foodNotFound);
        food.getJoinedGroups().stream()
                .map(FoodGroup::getGroup)
                .forEach(group -> group.removeFood(food));
        foodRepository.delete(food);
        return food.getShop().getFoods().stream().map(FoodDTO::new).collect(Collectors.toList());
    }

    public FoodDTO updateFood(long shopId, Long foodId, Long id, FoodInformationRequest request) {
        Shop shop = shopRepository.findById(shopId).orElseThrow(EntityExceptionSuppliers.shopNotFound);
        Food food = foodRepository.findByIdAndShop(foodId, shop).orElseThrow(EntityExceptionSuppliers.foodNotFound);
        food.changeName(request.getName());
        food.changeDescription(request.getShortDescription());
        food.changePrice(request.getPrice());
        food.changeDiscountType(request.getDiscountType());
        food.changeDiscountAmount(request.getDiscountAmount());
        food.changeFoodStatus(request.getStatus());
        return new FoodDTO(food);
    }
}
