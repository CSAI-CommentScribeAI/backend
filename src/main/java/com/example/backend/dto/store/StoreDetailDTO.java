package com.example.backend.dto.store;

import lombok.Builder;
import lombok.Data;
import com.example.backend.entity.userAccount.UserAddress;
import java.time.LocalDateTime;

@Data
@Builder
public class StoreDetailDTO {
    private Long id; // 상점 ID
    private int currentAmount; // 현재 주문 수량
    private LocalDateTime startTime; // 주문 시작 시간
    private LocalDateTime endTime; // 주문 종료 시간
    private StoreDTO store; // 상점 정보
    private UserAddress userAddress; // 주문 배송지


}
