package com.example.backend.food.repository;

import com.example.backend.food.entity.Food;
import com.example.backend.food.entity.FoodSubSelectGroup;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FoodSubSelectGroupRepository extends CrudRepository<FoodSubSelectGroup, Long> {
    List<FoodSubSelectGroup> findAllByParentFood(Food food);
}
