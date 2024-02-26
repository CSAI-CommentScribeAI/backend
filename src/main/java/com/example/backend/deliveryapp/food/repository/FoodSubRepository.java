package com.example.backend.deliveryapp.food.repository;

import com.example.backend.deliveryapp.food.entity.Food;
import com.example.backend.deliveryapp.food.entity.FoodSub;
import com.example.backend.deliveryapp.food.entity.FoodSubSelectGroup;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodSubRepository extends CrudRepository<FoodSub, Long> {
    List<FoodSub> findAllBySelectGroup(FoodSubSelectGroup group);

    List<FoodSub> findAllByFood(Food food);
}
