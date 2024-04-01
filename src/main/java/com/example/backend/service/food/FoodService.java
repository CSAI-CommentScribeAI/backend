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

    @Transactional
    public List<FoodDTO> deleteFood(long foodId) {
        Food food = foodRepository.findById(foodId).orElseThrow(EntityExceptionSuppliers.foodNotFound);
        Shop shop = food.getShop();

        // 연관된 FoodGroup에서 현재 Food 제거
        food.getJoinedGroups().stream()
                .map(FoodGroup::getGroup)
                .forEach(group -> group.removeFood(food));

        // Food 엔티티 삭제
        foodRepository.delete(food);

        // Hibernate 캐시를 우회하여 최신 데이터를 가져오기 위해 Shop의 Foods 컬렉션을 새로고침
        // 영속성 컨텍스트 내의 Shop 엔티티 새로고침 (선택적)
        // entityManager.refresh(shop);

        // 삭제된 Food를 제외하고 Shop의 Foods 목록을 DTO로 변환하여 반환
        // 직접 컬렉션에서 Food 제거
        shop.getFoods().removeIf(f -> f.getId().equals(foodId));

        return shop.getFoods().stream()
                .map(FoodDTO::new)
                .collect(Collectors.toList());
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
