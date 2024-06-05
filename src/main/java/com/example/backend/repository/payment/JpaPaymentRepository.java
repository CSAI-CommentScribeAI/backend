package com.example.backend.repository.payment;

import com.example.backend.entity.payment.Payment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaPaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByOrderId(String orderId);
    Optional<Payment> findByPaymentKeyAndUser_Email(String paymentKey, String email);
    Slice<Payment> findAllByUser_Email(String email, Pageable pageable);
    Optional<Payment> findByPaymentKey(String paymentKey);
}