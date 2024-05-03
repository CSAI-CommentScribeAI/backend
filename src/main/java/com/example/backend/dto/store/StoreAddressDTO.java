package com.example.backend.dto.store;

import com.example.backend.entity.store.StoreAddress;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreAddressDTO {
    private String fullAddress; // 전체 주소
    private String roadAddress; // 도로명 주소
    private String jibunAddress; // 지번 주소
    private String postalCode; // 우편번호
    private Double latitude; // 위도
    private Double longitude; // 경도

    // StoreAddress 엔티티에서 DTO로 변환하는 메서드
    public static StoreAddressDTO entityToDTO(StoreAddress storeAddress) {
        return StoreAddressDTO.builder()
                .fullAddress(storeAddress.getFullAddress())
                .roadAddress(storeAddress.getRoadAddress())
                .jibunAddress(storeAddress.getJibunAddress())
                .postalCode(storeAddress.getPostalCode())
                .latitude(storeAddress.getLatitude())
                .longitude(storeAddress.getLongitude())
                .build();
    }
}
