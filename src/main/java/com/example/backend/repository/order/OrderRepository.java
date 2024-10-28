package com.example.backend.repository.order;

import com.example.backend.entity.order.UserOrder;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<UserOrder, Long> {
    Optional<UserOrder> findById(Long userId);

    @Query("SELECT COUNT(o) FROM UserOrder o WHERE o.userAccount.id = :userId AND o.storeId = :storeId")
    int countByUserIdAndStoreId(@Param("userId") Long userId, @Param("storeId") Long storeId);

    List<UserOrder> findByStoreId(Long storeId);

    List<UserOrder> findByUserAccountId(Long userId);
}


