package com.example.backend.service.payment;

import com.example.backend.config.payment.TossPaymentConfig;
import com.example.backend.dto.payment.PaymentResDto;
import com.example.backend.dto.payment.PaymentSuccessDto;
import com.example.backend.entity.payment.Payment;
import com.example.backend.entity.userAccount.UserAccount;
import com.example.backend.repository.UserAccount.UserAccountRepository;
import com.example.backend.repository.payment.JpaPaymentRepository;
import com.nimbusds.jose.shaded.json.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PaymentImplService implements PaymentService {

    private final JpaPaymentRepository jpaPaymentRepository;
    private final UserAccountRepository userAccountRepository;
    private final TossPaymentConfig tossPaymentConfig;

    @Override
    @Transactional
    public Payment requestTossPayment(Payment payment, Long userId) {
        UserAccount userAccount = userAccountRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾지 못했습니다: " + userId));

        if (payment.getAmount() < 1000) {
            throw new IllegalArgumentException("결제 금액이 1000원보다 적습니다.");
        }
        payment.setUser(userAccount);
        return jpaPaymentRepository.save(payment);
    }

    @Override
    @Transactional
    public PaymentSuccessDto tossPaymentSuccess(String paymentKey, String tossOrderId, Long amount) {
        // 토스 orderId를 원래의 orderId로 변환
        String orderId = convertToOriginalOrderId(tossOrderId);
        System.out.println(orderId);
        // 데이터베이스에서 주문을 찾음
        Payment payment = jpaPaymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));

        // 결제 정보 업데이트
        payment.setPaymentKey(paymentKey);
        payment.setPaySuccessYN(true);
        jpaPaymentRepository.save(payment);

        return new PaymentSuccessDto(payment);
    }

    // 원래의 주문 번호로 변환하는 함수
    public String convertToOriginalOrderId(String tossOrderId) {
        // "order_" 접두사를 제거하여 원래 주문 번호를 반환
        if (tossOrderId.startsWith("order_")) {
            return tossOrderId.substring(6); // "order_" 이후의 부분 반환
        }
        return tossOrderId; // 접두사가 없다면 원래 번호 반환
    }

    @Override
    @Transactional
    public PaymentSuccessDto requestPaymentAccept(String paymentKey, String orderId, Long amount) {
        System.out.println(orderId);
        RestTemplate restTemplate = new RestTemplate();

        // 헤더에 인증 정보를 추가합니다.
        HttpHeaders headers = new HttpHeaders();
        String authHeader = "Basic " + Base64.getEncoder().encodeToString((tossPaymentConfig.getTestClientApiKey() + ":" + tossPaymentConfig.getTestSecretKey()).getBytes());
        headers.set(HttpHeaders.AUTHORIZATION, authHeader);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 요청에 필요한 데이터를 설정합니다.
        JSONObject params = new JSONObject();
        params.put("orderId", orderId);
        params.put("amount", amount);

        // API 요청을 보냅니다.
        PaymentSuccessDto result = null;
        try {
            result = restTemplate.postForObject(
                    TossPaymentConfig.URL + paymentKey,
                    new HttpEntity<>(params, headers),
                    PaymentSuccessDto.class
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("Payment already processed.");
        }

        return result;
    }

    @Override
    public Payment verifyPayment(String orderId, Long amount) {
        System.out.println(orderId);
        Payment payment = jpaPaymentRepository.findByOrderId(orderId).orElseThrow(() -> new IllegalArgumentException("Payment not found."));
        if (!payment.getAmount().equals(amount)) {
            throw new IllegalArgumentException("amount 페이가 일치하지 않습니다");
        }
        return payment;
    }

    @Transactional
    public void tossPaymentFail(String code, String message, String orderId) {
        System.out.println(orderId);
        Payment payment = jpaPaymentRepository.findByOrderId(orderId).orElseThrow(() -> new IllegalArgumentException("Payment not found."));
        payment.setPaySuccessYN(false);
        payment.setFailReason(message);
        jpaPaymentRepository.save(payment);
    }

    @Transactional
    public Map<String, Object> cancelPaymentPoint(String userEmail, String paymentKey, String cancelReason) {
        Payment payment = jpaPaymentRepository.findByPaymentKeyAndUser_Email(paymentKey, userEmail).orElseThrow(() -> new IllegalArgumentException("결제를 찾지 못했습니다"));
        payment.setCancelYN(true);
        payment.setCancelReason(cancelReason);
        jpaPaymentRepository.save(payment);
        return tossPaymentCancel(paymentKey, cancelReason);
    }

    public Map<String, Object> tossPaymentCancel(String paymentKey, String cancelReason) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = getHeaders();
        JSONObject params = new JSONObject();
        params.put("cancelReason", cancelReason);

        return restTemplate.postForObject(TossPaymentConfig.URL + paymentKey + "/cancel",
                new HttpEntity<>(params, headers),
                Map.class);
    }

    @Override
    public Slice<Payment> findAllChargingHistories(String username, Pageable pageable) {
        userAccountRepository.findByUserId(username);
        return jpaPaymentRepository.findAllByUser_Email(username,
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                        Sort.Direction.DESC, "paymentId")
        );
    }

    public List<PaymentResDto> chargingHistoryToChargingHistoryResponses(List<Payment> payments) {
        return payments.stream()
                .map(Payment::toPaymentResDto)
                .collect(Collectors.toList());
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        String encodedAuthKey = Base64.getEncoder().encodeToString((tossPaymentConfig.getTestSecretKey() + ":").getBytes(StandardCharsets.UTF_8));
        headers.setBasicAuth(encodedAuthKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }
}
