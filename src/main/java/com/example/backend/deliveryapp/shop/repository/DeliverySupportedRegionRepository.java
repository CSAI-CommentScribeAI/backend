package com.example.backend.deliveryapp.shop.repository;

import com.example.backend.deliveryapp.shop.entity.DeliverySupportedRegions;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliverySupportedRegionRepository extends CrudRepository<DeliverySupportedRegions, Long> {
}
