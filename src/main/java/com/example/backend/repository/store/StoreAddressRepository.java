package com.example.backend.repository.store;

import com.example.backend.entity.store.StoreAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreAddressRepository extends JpaRepository<StoreAddress, Long> {
    List<StoreAddress> findByStoreId(Long storeId);
}
