package com.example.backend.controller.payment;

import com.example.backend.config.payment.TossPaymentConfig;
import com.example.backend.dto.payment.PaymentDto;
import com.example.backend.dto.payment.PaymentFailDto;
import com.example.backend.dto.payment.PaymentResDto;
import com.example.backend.dto.payment.PaymentSuccessDto;
import com.example.backend.entity.payment.Payment;
import com.example.backend.exception.SingleResponse;
import com.example.backend.exception.SliceInfo;
import com.example.backend.exception.SliceResponseDto;
import com.example.backend.service.payment.PaymentImplService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentImplService paymentService;
    private final TossPaymentConfig tossPaymentConfig;


    @PostMapping("/toss")
    public ResponseEntity<SingleResponse<PaymentResDto>> requestTossPayment(Authentication authentication, @RequestBody @Valid PaymentDto paymentReqDto) {
        Long userId = extractUserIdFromAuthentication(authentication);
        log.debug("Request Toss Payment - userId: {}, paymentReqDto: {}", userId, paymentReqDto);
        PaymentResDto paymentResDto = paymentService.requestTossPayment(paymentReqDto.toEntity(), userId).toPaymentResDto();
        paymentResDto.setSuccessUrl(paymentReqDto.getYourSuccessUrl() == null ? tossPaymentConfig.getSuccessUrl() : paymentReqDto.getYourSuccessUrl());
        paymentResDto.setFailUrl(paymentReqDto.getYourFailUrl() == null ? tossPaymentConfig.getFailUrl() : paymentReqDto.getYourFailUrl());
        return ResponseEntity.ok().body(new SingleResponse<>(paymentResDto));
    }

    private Long extractUserIdFromAuthentication(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            return Long.parseLong(((UserDetails) principal).getUsername());
        } else if (principal instanceof Map) {
            Map<String, Object> details = (Map<String, Object>) principal;
            return (Long) details.get("userId");
        } else if (principal instanceof String) {
            return Long.parseLong((String) principal);
        } else {
            throw new IllegalArgumentException("Authentication 객체에서 userId를 추출할 수 없습니다.");
        }
    }

    @GetMapping("/toss/success")
    public ResponseEntity<SingleResponse<PaymentSuccessDto>> tossPaymentSuccess(
            @RequestParam String paymentKey,
            @RequestParam String orderId,
            @RequestParam Long amount
    ) {
        log.debug("Payment Success - paymentKey: {}, orderId: {}, amount: {}", paymentKey, orderId, amount);
        PaymentSuccessDto successDto = paymentService.tossPaymentSuccess(paymentKey, orderId, amount);
        return ResponseEntity.ok().body(new SingleResponse<>(successDto));
    }

    @GetMapping("/toss/fail")
    public ResponseEntity<SingleResponse<PaymentFailDto>> tossPaymentFail(
            @RequestParam String code,
            @RequestParam String message,
            @RequestParam String orderId
    ) {
        log.debug("Payment Fail - code: {}, message: {}, orderId: {}", code, message, orderId);
        paymentService.tossPaymentFail(code, message, orderId);
        return ResponseEntity.ok().body(new SingleResponse<>(
                PaymentFailDto.builder()
                        .errorCode(code)
                        .errorMessage(message)
                        .orderId(orderId)
                        .build()
        ));
    }

    @PostMapping("/toss/cancel/point")
    public ResponseEntity<SingleResponse<Map<String, Object>>> tossPaymentCancelPoint(
            Authentication authentication,
            @RequestParam String paymentKey,
            @RequestParam String cancelReason
    ) {
        String userEmail = String.valueOf(extractUserIdFromAuthentication(authentication));
        log.debug("Cancel Point - userEmail: {}, paymentKey: {}, cancelReason: {}", userEmail, paymentKey, cancelReason);
        return ResponseEntity.ok().body(new SingleResponse<>(
                paymentService.cancelPaymentPoint(userEmail, paymentKey, cancelReason)));
    }

    @GetMapping("/history")
    public ResponseEntity<SliceResponseDto<PaymentResDto>> getChargingHistory(Authentication authentication,
                                                                              Pageable pageable) {
        String username = String.valueOf(extractUserIdFromAuthentication(authentication));
        log.debug("Get Charging History - username: {}, pageable: {}", username, pageable);

        Slice<Payment> chargingHistories = paymentService.findAllChargingHistories(username, pageable);
        SliceInfo sliceInfo = new SliceInfo(pageable, chargingHistories.getNumberOfElements(), chargingHistories.hasNext());
        return new ResponseEntity<>(
                new SliceResponseDto<>(paymentService.chargingHistoryToChargingHistoryResponses(chargingHistories.getContent()), sliceInfo), HttpStatus.OK);
    }
}
