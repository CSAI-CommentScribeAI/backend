package com.example.backend.repository.order;

import com.example.backend.entity.order.Order;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findById(Long userId);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.userAccount.id = :userId AND o.storeId = :storeId")
    int countByUserIdAndStoreId(@Param("userId") Long userId, @Param("storeId") Long storeId);

}


