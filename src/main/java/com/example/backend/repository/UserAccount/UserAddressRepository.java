package com.example.backend.repository.UserAccount;

import com.example.backend.entity.userAccount.UserAddress;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {
    List<UserAddress> findByUserAccountId(Long userAccountId);

}
