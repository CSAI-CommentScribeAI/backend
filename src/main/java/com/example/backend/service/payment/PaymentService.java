package com.example.backend.service.payment;

import com.example.backend.dto.payment.PaymentSuccessDto;
import com.example.backend.entity.payment.Payment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface PaymentService {
    Payment requestTossPayment(Payment payment, Long userId);
    PaymentSuccessDto tossPaymentSuccess(String paymentKey, String orderId, Long amount);
    PaymentSuccessDto requestPaymentAccept(String paymentKey, String orderId, Long amount);
    Slice<Payment> findAllChargingHistories(String username, Pageable pageable);
    Payment verifyPayment(String orderId, Long amount);
}
