package com.example.backend.dto.store;

import com.example.backend.entity.store.Category;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreListDTO {
    private String name; // 매장명
    private int minOrderPrice; // 최소 주문금액
    private String storeUrl; // 배경 이미지
    private Category category;
    private String info;
    private String businessLicense; // 사업자 등록증
    private String fullAddress; // 전체 주소
    private String roadAddress; // 도로 주소
    private String jibunAddress; // 지번 주소
    private String postalCode; // 우편번호
    private Double latitude; // 위도
    private Double longitude; // 경도
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime openTime;

    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime closeTime; // 마감 시간
}
