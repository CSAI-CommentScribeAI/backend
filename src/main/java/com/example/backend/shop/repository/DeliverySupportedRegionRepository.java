package com.example.backend.shop.repository;

import com.example.backend.shop.entity.DeliverySupportedRegions;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliverySupportedRegionRepository extends CrudRepository<DeliverySupportedRegions, Long> {
}
