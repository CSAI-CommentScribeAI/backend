package com.example.backend.menu.repository;

import com.example.backend.menu.entity.Food;
import com.example.backend.menu.entity.FoodSubSelectGroup;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FoodSubSelectGroupRepository extends CrudRepository<FoodSubSelectGroup, Long> {
    List<FoodSubSelectGroup> findAllByParentFood(Food food);
}
