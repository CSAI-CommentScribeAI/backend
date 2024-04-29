package com.example.backend.repository.store;

import com.example.backend.entity.store.Category;
import com.example.backend.entity.store.Store;
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
    Optional<Store> findByUserAccount(String userId);

    List<Store> findByCategory(Category category);

    List<Store> findByNameContainingIgnoreCase(String name);


}

