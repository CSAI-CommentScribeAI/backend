package com.example.backend.dto.payment;

import com.example.backend.entity.payment.Payment;
import lombok.Data;

@Data
public class PaymentSuccessDto {
    private String mid;
    private String version;
    private String paymentKey;
    private String orderId;
    private String orderName;
    private String currency; // 화폐
    private String method; // 결제 수단
    private String totalAmount; // 총 결제 금액
    private String balanceAmount; // 잔액
    private String suppliedAmount; // 제공된 금액
    private String vat; // 부가세
    private String status; // 결제 상태
    private String requestedAt; // 요청 시간
    private String approvedAt; // 승인 시간
    private String useEscrow; // 에스크로 사용 여부
    private String cultureExpense; // 문화비 지원 여부
    private PaymentSuccessCardDto card;
    private String type;

    // 생성자 추가
    public PaymentSuccessDto(Payment payment) {
        this.paymentKey = payment.getPaymentKey();
        this.orderId = payment.getOrderId();
        this.orderName = payment.getOrderName();
        this.method = payment.getPayType().getDescription(); // 결제 방법 설정
        this.totalAmount = String.valueOf(payment.getAmount()); // 총 금액 설정
        this.currency = "KRW"; // 화폐 설정
        this.status = payment.isPaySuccessYN() ? "SUCCESS" : "FAIL"; // 결제 상태 설정
        this.requestedAt = payment.getCreatedTime().toString(); // 요청 시간 설정
        this.approvedAt = payment.getUpdatedTime().toString(); // 승인 시간 설정
        // 추가적인 필드 초기화
        this.balanceAmount = "0"; // 잔액 설정
        this.suppliedAmount = String.valueOf(payment.getAmount()); // 제공된 금액 설정
        this.vat = "0"; // 부가세 설정
        this.useEscrow = "false"; // 에스크로 사용 여부 설정
        this.cultureExpense = "false"; // 문화비 지원 여부 설정
    }
}
