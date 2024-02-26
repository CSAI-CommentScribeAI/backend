package com.example.backend.UserAccount.repository;

import com.example.backend.UserAccount.entity.UserAccount;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    Optional<UserAccount> findByUserId(String userId);
    boolean existsByUserId(String userId);
}
