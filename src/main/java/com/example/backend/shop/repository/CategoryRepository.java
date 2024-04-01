package com.example.backend.shop.repository;

import com.example.backend.shop.entity.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Long> {
    void deleteByName(String name);
}