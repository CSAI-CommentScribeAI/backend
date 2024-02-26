package com.example.backend.deliveryapp.food.repository;

import com.example.backend.deliveryapp.food.entity.Food;
import com.example.backend.deliveryapp.food.entity.FoodSubSelectGroup;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FoodSubSelectGroupRepository extends CrudRepository<FoodSubSelectGroup, Long> {
    List<FoodSubSelectGroup> findAllByParentFood(Food food);
}
