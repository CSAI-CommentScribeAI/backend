package com.example.backend.deliveryapp.shop.repository;

import com.example.backend.deliveryapp.shop.entity.Shop;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShopRepository extends CrudRepository<Shop, Long> {

    List<Shop> findByNameContaining(String shopName);

    Optional<Shop> findByOwnerId(Long ownerId);

}
