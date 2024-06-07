package com.example.backend.repository.store;

import com.example.backend.entity.store.Category;
import com.example.backend.entity.store.Store;
import com.example.backend.entity.userAccount.UserAccount;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {
    @Override
    Optional<Store> findById(Long id);

    @Query("select s from Store s join fetch s.menus m where m.store.id = :id")
    Optional<Store> findByIdWithMenus(Long id);

    @Query("select s from Store s join fetch s.userAccount u where s.userAccount.id = :userId")
    Optional<Store> findByUserAccount_Id(Long userId);

    @Query("SELECT s FROM Store s WHERE s.category = :category AND " +
            "(6371 * acos(cos(radians(:latitude)) * cos(radians(s.storeAddress.latitude)) " +
            "* cos(radians(s.storeAddress.longitude) - radians(:longitude)) + " +
            "sin(radians(:latitude)) * sin(radians(s.storeAddress.latitude)))) <= :distance")
    List<Store> findStoresWithinDistanceAndCategory(@Param("latitude") double latitude,
                                                    @Param("longitude") double longitude,
                                                    @Param("distance") double distance,
                                                    @Param("category") Category category);

    List<Store> findByNameContainingIgnoreCase(String name);

    List<Store> findByUserAccount(UserAccount userAccount);

    @Query("SELECT s FROM Store s WHERE " +
            "(6371 * acos(cos(radians(:latitude)) * cos(radians(s.storeAddress.latitude)) " +
            "* cos(radians(s.storeAddress.longitude) - radians(:longitude)) + " +
            "sin(radians(:latitude)) * sin(radians(s.storeAddress.latitude)))) <= :distance")
    Page<Store> findStoresWithinDistance(@Param("latitude") double latitude,
                                         @Param("longitude") double longitude,
                                         @Param("distance") double distance,
                                         Pageable pageable);

}
