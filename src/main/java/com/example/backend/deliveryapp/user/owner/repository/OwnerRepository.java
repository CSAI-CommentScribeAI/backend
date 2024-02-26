package com.example.backend.deliveryapp.user.owner.repository;

import com.example.backend.deliveryapp.user.owner.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnerRepository extends JpaRepository<Owner, Long> {
}
