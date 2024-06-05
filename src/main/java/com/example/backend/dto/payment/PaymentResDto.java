package com.example.backend.dto.payment;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResDto {
    private String payType;
    private Long amount;
    private String orderName;
    private String orderId;
    private String userEmail;
    private String userNickName;
    private String successUrl;
    private String failUrl;
    private String failReason;
    private boolean cancelYN;
    private String cancelReason;
    private String createTime;
}