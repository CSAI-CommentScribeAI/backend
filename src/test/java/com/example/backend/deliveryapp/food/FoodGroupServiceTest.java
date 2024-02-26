package com.example.backend.deliveryapp.food;

import com.example.backend.deliveryapp.controller.bind.FoodGroupInformationRequest;
import com.example.backend.deliveryapp.food.entity.*;
import com.example.backend.deliveryapp.food.entity.dto.GroupDTO;
import com.example.backend.deliveryapp.food.repository.FoodGroupRepository;
import com.example.backend.deliveryapp.food.repository.FoodRepository;
import com.example.backend.deliveryapp.food.repository.GroupRepository;
import com.example.backend.deliveryapp.service.FoodGroupService;
import com.example.backend.deliveryapp.service.FoodService;
import com.example.backend.deliveryapp.root.exception.EntityExceptionSuppliers;
import com.example.backend.deliveryapp.shop.entity.Shop;
import com.example.backend.deliveryapp.shop.entity.ShopStatus;
import com.example.backend.deliveryapp.shop.entity.ShopSupportedOrderType;
import com.example.backend.deliveryapp.shop.entity.ShopSupportedPayment;
import com.example.backend.deliveryapp.shop.repository.ShopRepository;
import com.example.backend.deliveryapp.user.owner.entity.Owner;
import com.example.backend.deliveryapp.user.owner.repository.OwnerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class FoodGroupServiceTest {
    @Autowired
    FoodService foodService;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    FoodGroupService foodGroupService;
    @Autowired
    FoodRepository foodRepository;
    @Autowired
    ShopRepository shopRepository;
    @Autowired
    OwnerRepository ownerRepository;
    @Autowired
    FoodGroupRepository foodGroupRepository;

    Owner owner;
    Shop shop;

    @BeforeEach
    void init() {
        owner = new Owner("username", "email", "phoneNum");
        ownerRepository.save(owner);
        shop = new Shop(
                "shop_test",
                "012-345-6789",
                "Shop for food service test.",
                "Shop for food service test.",
                ShopSupportedOrderType.BOTH,
                ShopSupportedPayment.CARD,
                LocalTime.now(),
                LocalTime.now(),
                2000,
                15000,
                ShopStatus.DEACTIVATED,
                "123-456-7890",
                owner,
                "Doro-ro",
                123,
                "Seoul");
        shopRepository.save(shop);
    }

    @Test
    @DisplayName("Group creation test.")
    void createGroup() {
        FoodGroupInformationRequest request = new FoodGroupInformationRequest();
        request.setName("FOOD_GROUP");
        List<GroupDTO> foodGroup = foodGroupService.createFoodGroup(shop.getId(), request);
        assertEquals(request.getName(), foodGroup.get(0).getName());
        Group createdGroup = groupRepository.findById(foodGroup.get(0).getId()).orElseThrow(EntityExceptionSuppliers.groupNotFound);
        assertEquals(request.getName(), createdGroup.getName());
    }

    @Test
    @DisplayName("Group delete test.")
    void deleteGroup() {
        Food food = foodRepository.save(Food.builder()
                .name("NAME")
                .discountType(DiscountType.FIXED)
                .discountAmount(2500)
                .shop(shop)
                .shortDesc("SHORT_DESCRIPTION")
                .price(7500)
                .status(FoodStatus.UNAVAILABLE).build());
        Group group1 = groupRepository.save(Group.builder()
                .name("GROUP_NAME1")
                .shop(shop).build());
        groupRepository.save(Group.builder()
                .name("GROUP_NAME2")
                .shop(shop).build());
        foodGroupService.joinFoodGroup(food.getId(), group1.getId());
        foodGroupService.deleteFoodGroup(group1.getId());

        assertTrue(foodGroupRepository.findByFoodAndGroup(food, group1).isEmpty());
    }

    @Test
    @DisplayName("Group join test.")
    void joinGroup() {
        Food food = foodRepository.save(Food.builder()
                .name("Chicken Burrito")
                .discountAmount(1500)
                .discountType(DiscountType.FIXED)
                .price(4500)
                .status(FoodStatus.UNAVAILABLE)
                .shop(shop)
                .shortDesc("Burrito made of chicken!").build());
        Group group1 = groupRepository.save(Group.builder()
                .name("FRIED_DISHES")
                .shop(shop).build());
        Group group2 = groupRepository.save(Group.builder()
                .name("WELL_DISHES")
                .shop(shop).build());

        List<GroupDTO> joinedGroups = foodGroupService.joinFoodGroup(food.getId(), group1.getId());
        assertEquals(group1.getId(), joinedGroups.get(0).getId());

        assertTrue(groupRepository.findById(group1.getId()).orElseThrow(EntityExceptionSuppliers.groupNotFound)
                .getIncludingFoods().stream().map(FoodGroup::getFood).anyMatch(food::equals));
        assertTrue(foodRepository.findById(food.getId()).orElseThrow(EntityExceptionSuppliers.foodNotFound)
                .getJoinedGroups().stream().map(FoodGroup::getGroup).anyMatch(group1::equals));
        assertTrue(foodGroupRepository.existsByFoodAndGroup(food, group1));

        joinedGroups = foodGroupService.joinFoodGroup(food.getId(), group2.getId());
        assertTrue(joinedGroups.stream().anyMatch(groupDTO -> groupDTO.getId() == group2.getId()));

        assertTrue(groupRepository.findById(group2.getId()).orElseThrow(EntityExceptionSuppliers.groupNotFound)
                .getIncludingFoods().stream().map(FoodGroup::getFood).anyMatch(food::equals));
        assertTrue(foodRepository.findById(food.getId()).orElseThrow(EntityExceptionSuppliers.foodNotFound)
                .getJoinedGroups().stream().map(FoodGroup::getGroup).anyMatch(group2::equals));
        assertTrue(foodGroupRepository.existsByFoodAndGroup(food, group2));
    }

    @Test
    @DisplayName("Group withdraw test.")
    void withdrawGroup() {
        Group group1 = Group.builder()
                .name("FRIED_DISHES")
                .shop(shop).build();
        Group group2 = Group.builder()
                .name("WELL_DISHES")
                .shop(shop).build();

        Food food = Food.builder()
                .name("Chicken Burrito")
                .discountAmount(1500)
                .discountType(DiscountType.FIXED)
                .price(4500)
                .status(FoodStatus.UNAVAILABLE)
                .shop(shop)
                .shortDesc("Burrito made of chicken!").build();

        group1.addFood(food);
        group2.addFood(food);
        foodRepository.save(food);

        List<GroupDTO> joinedGroups = foodGroupService.withdrawFoodGroup(food.getId(), group1.getId());
        assertTrue(joinedGroups.stream().noneMatch(groupDTO -> groupDTO.getId() == group1.getId()));
        assertTrue(joinedGroups.stream().anyMatch(groupDTO -> groupDTO.getId() == group2.getId()));

        List<Group> groups = foodRepository.findById(food.getId()).orElseThrow(EntityExceptionSuppliers.foodNotFound).getJoinedGroups()
                .stream().map(FoodGroup::getGroup).collect(Collectors.toList());
        assertTrue(groups.stream().noneMatch(group1::equals));
        assertTrue(groups.stream().anyMatch(group2::equals));

        assertTrue(foodGroupRepository.findByFoodAndGroup(food, group1).isEmpty());
        assertTrue(foodGroupRepository.findByFoodAndGroup(food, group2).isPresent());
    }
}
