package com.example.backend.UserAccount.repository;

import com.example.backend.UserAccount.entity.UserAddress;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {
    List<UserAddress> findByUserAccountId(Long userAccountId);

}
