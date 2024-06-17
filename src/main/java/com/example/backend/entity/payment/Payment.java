package com.example.backend.entity.payment;

import com.example.backend.dto.payment.PayType;
import com.example.backend.dto.payment.PaymentResDto;
import com.example.backend.entity.TimeZone;
import com.example.backend.entity.userAccount.UserAccount;
import lombok.*;

import javax.persistence.*;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="Payment")
public class Payment extends TimeZone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id", nullable = false, unique = true)
    private Long paymentId;

    @Column(nullable = false, name = "pay_type")
    @Enumerated(EnumType.STRING)
    private PayType payType;

    @Column(nullable = false, name = "pay_amount")
    private Long amount;

    @Column(nullable = false, name = "pay_name")
    private String orderName;

    @Column(nullable = false, name = "order_id")
    private String orderId;

    private boolean paySuccessYN;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "userAccount")
    private UserAccount user;

    @Column
    private String paymentKey;

    @Column
    private String failReason;

    @Column
    private boolean cancelYN;

    @Column
    private String cancelReason;

    public PaymentResDto toPaymentResDto() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedCreateTime = getCreatedTime().format(formatter);

        return PaymentResDto.builder()
                .payType(payType.getDescription())
                .amount(amount)
                .orderName(orderName)
                .orderId(orderId)
                .userEmail(user.getEmail())
                .userNickName(user.getNickname())
                .createTime(formattedCreateTime)
                .cancelYN(cancelYN)
                .failReason(failReason)
                .build();
    }
}
