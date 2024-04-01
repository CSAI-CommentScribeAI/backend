package com.example.backend.food.repository;

import com.example.backend.food.entity.Food;
import com.example.backend.food.entity.FoodSub;
import com.example.backend.food.entity.FoodSubSelectGroup;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodSubRepository extends CrudRepository<FoodSub, Long> {
    List<FoodSub> findAllBySelectGroup(FoodSubSelectGroup group);

    List<FoodSub> findAllByFood(Food food);
}
