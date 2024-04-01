package com.example.backend.shop.repository;

import com.example.backend.shop.entity.Shop;
import com.example.backend.UserAccount.entity.UserAccount; // UserAccount 클래스 import 추가
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.metamodel.SingularAttribute;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShopRepository extends CrudRepository<Shop, Long> {

    List<Shop> findByNameContaining(String shopName);

    Optional<Shop> findByUserAccount(UserAccount userAccount);

    Optional<Object> findById(SingularAttribute<AbstractPersistable, Serializable> id);
}
