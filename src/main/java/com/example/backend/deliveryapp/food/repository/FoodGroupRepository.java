package com.example.backend.deliveryapp.food.repository;

import com.example.backend.deliveryapp.food.entity.Food;
import com.example.backend.deliveryapp.food.entity.FoodGroup;
import com.example.backend.deliveryapp.food.entity.Group;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;


public interface FoodGroupRepository extends CrudRepository<FoodGroup, Long> {
    boolean existsByFoodAndGroup(Food food, Group group);

    Optional<FoodGroup> findByFoodAndGroup(Food food, Group group);

    void deleteByFoodAndGroup(Food food, Group group);

    List<FoodGroup> findAllByFood(Food food);

    List<FoodGroup> findAllByGroup(Group group);
}
